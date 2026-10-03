package com.yomismtz.expedientedeldentista.clinical

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class SavedRecord(
    val id: String,
    val title: String,
    val updatedAt: Long,
    val session: EducationalSession
)

class ClinicalRecordStore(context: Context) {
    private val prefs = context.getSharedPreferences("clinical_records_v1", Context.MODE_PRIVATE)
    private val keyAlias = "expediente_dentista_records_v1"

    fun loadAll(): List<SavedRecord> {
        val secure = prefs.getString("records_secure", null)
        val raw = if (!secure.isNullOrBlank()) {
            decrypt(secure) ?: "[]"
        } else {
            val legacy = prefs.getString("records", "[]") ?: "[]"
            if (legacy != "[]") {
                val migrated = runCatching {
                    val a = JSONArray(legacy)
                    (0 until a.length()).mapNotNull { i -> runCatching { recordFromJson(a.getJSONObject(i)) }.getOrNull() }
                }.getOrDefault(emptyList())
                write(migrated)
            }
            legacy
        }
        return runCatching {
            val a = JSONArray(raw)
            (0 until a.length()).mapNotNull { i -> runCatching { recordFromJson(a.getJSONObject(i)) }.getOrNull() }
                .sortedByDescending { it.updatedAt }
        }.getOrDefault(emptyList())
    }

    fun create(session: EducationalSession = EducationalSession()): SavedRecord {
        val now = System.currentTimeMillis()
        return SavedRecord(UUID.randomUUID().toString(), recordTitle(session, now), now, session).also { save(it) }
    }

    fun save(record: SavedRecord) {
        val records = loadAll().filterNot { it.id == record.id }.toMutableList()
        val now = System.currentTimeMillis()
        records += record.copy(title = recordTitle(record.session, now), updatedAt = now)
        write(records)
    }

    fun touch(id: String) {
        val records = loadAll().map { if (it.id == id) it.copy(updatedAt = System.currentTimeMillis()) else it }
        write(records)
    }

    fun delete(id: String) = write(loadAll().filterNot { it.id == id })

    fun exportRecordJson(id: String): JSONObject? = loadAll().firstOrNull { it.id == id }?.let { record ->
        JSONObject()
            .put("format", "YSM_DENTAL_RECORD")
            .put("version", 1)
            .put("exportedAt", System.currentTimeMillis())
            .put("record", recordToJson(record))
    }

    fun importRecordJson(root: JSONObject): SavedRecord {
        require(root.optString("format") == "YSM_DENTAL_RECORD") { "Formato de respaldo no reconocido" }
        require(root.optInt("version", 0) == 1) { "Versión de respaldo no compatible" }
        val source = recordFromJson(root.getJSONObject("record"))
        val now = System.currentTimeMillis()
        val imported = source.copy(id = UUID.randomUUID().toString(), updatedAt = now)
        save(imported)
        return loadAll().first { it.id == imported.id }
    }

    private fun write(records: List<SavedRecord>) {
        val a = JSONArray()
        records.forEach { a.put(recordToJson(it)) }
        val payload = a.toString()
        val encrypted = encrypt(payload)
        if (encrypted != null) {
            prefs.edit().putString("records_secure", encrypted).remove("records").apply()
        } else {
            // Fail closed: never downgrade clinical records to plaintext when the
            // Android Keystore is unavailable or encryption fails.
            // A legacy plaintext copy, if present, is intentionally preserved so
            // migration can be retried after the Keystore becomes available.
            throw IllegalStateException("No se pudo cifrar el expediente con Android Keystore")
        }
    }

    private fun secretKey(): SecretKey? = runCatching {
        val ks = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val existing = ks.getKey(keyAlias, null)
        if (existing is SecretKey) return@runCatching existing
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        generator.init(
            KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setUserAuthenticationRequired(false)
                .build()
        )
        generator.generateKey()
    }.getOrNull()

    private fun encrypt(value: String): String? = runCatching {
        val key = secretKey() ?: return@runCatching null
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val ciphertext = cipher.doFinal(value.toByteArray(StandardCharsets.UTF_8))
        Base64.encodeToString(iv + ciphertext, Base64.NO_WRAP)
    }.getOrNull()

    private fun decrypt(encoded: String): String? = runCatching {
        val bytes = Base64.decode(encoded, Base64.DEFAULT)
        require(bytes.size > 12)
        val iv = bytes.copyOfRange(0, 12)
        val ciphertext = bytes.copyOfRange(12, bytes.size)
        val key = secretKey() ?: return@runCatching null
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8)
    }.getOrNull()

    private fun recordTitle(s: EducationalSession, time: Long): String {
        val n = s.profile.patientInitials.trim().uppercase().ifBlank { s.profile.exerciseName.trim() }
        return if (n.isNotBlank()) n else "Expediente " + java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(time))
    }

    private fun recordToJson(r: SavedRecord) = JSONObject()
        .put("id", r.id).put("title", r.title).put("updatedAt", r.updatedAt).put("session", sessionToJson(r.session))

    private fun recordFromJson(o: JSONObject) = SavedRecord(
        o.getString("id"), o.optString("title", "Expediente"), o.optLong("updatedAt", 0L), sessionFromJson(o.getJSONObject("session"))
    )

    private fun sessionToJson(s: EducationalSession): JSONObject {
        val o = JSONObject()
        o.put("profile", profileToJson(s.profile))
        o.put("history", historyToJson(s.history))
        o.put("teeth", JSONObject().also { j -> s.teeth.forEach { (k,v) -> j.put(k.toString(), JSONObject().put("status",v.status.name).put("icdas",v.icdas).put("diagnosisId",v.diagnosisId).put("treatmentId",v.treatmentId)) } })
        fun surfaceMap(m: Map<Int, Map<Surface, *>>, encode:(Any)->Any): JSONObject = JSONObject().also { root ->
            m.forEach { (tooth, surfaces) -> root.put(tooth.toString(), JSONObject().also { z -> surfaces.forEach { (surface,value) -> z.put(surface.name, encode(value as Any)) } }) }
        }
        o.put("odontogramSurfaces", surfaceMap(s.odontogramSurfaces) { (it as SurfaceMark).name })
        o.put("icdasSurfaces", surfaceMap(s.icdasSurfaces) { it as Int })
        o.put("oleary", JSONObject().also { j -> s.oleary.forEach { (k,v) -> j.put(k.toString(), JSONArray(v.map { it.name })) } })
        o.put("presentTeeth", JSONArray(s.presentTeeth.toList()))
        o.put("ipcCodes", JSONArray(s.ipcCodes))
        o.put("ihosDebris", intMap(s.ihosDebris)); o.put("ihosCalculus", intMap(s.ihosCalculus))
        o.put("periodontogram", JSONObject().also { j -> s.periodontogram.forEach { (k,v) -> j.put(k.toString(), perioToJson(v)) } })
        o.put("pulpal", pulpalToJson(s.pulpal))
        o.put("clinicalEvents", JSONArray().also { a -> s.clinicalEvents.forEach { e -> a.put(JSONObject().put("timestamp",e.timestamp).put("type",e.type).put("detail",e.detail)) } })
        o.put("clinicalMeasurements", JSONArray().also { a -> s.clinicalMeasurements.forEach { m ->
            a.put(JSONObject().put("timestamp",m.timestamp).put("systolic",m.systolic).put("diastolic",m.diastolic).put("heartRate",m.heartRate).put("respiratoryRate",m.respiratoryRate).put("spo2",m.spo2).put("temperature",m.temperature).put("glucose",m.glucose).put("weightKg",m.weightKg).put("bmi",m.bmi).put("pain",m.pain))
        } })
        o.put("informedConsents", JSONArray().also { a -> s.informedConsents.forEach { c ->
            a.put(JSONObject().put("timestamp",c.timestamp).put("procedure",c.procedure).put("toothOrSite",c.toothOrSite).put("diagnosis",c.diagnosis).put("benefits",c.benefits).put("risks",c.risks).put("alternatives",c.alternatives).put("questionsAnswered",c.questionsAnswered).put("understood",c.understood).put("accepted",c.accepted).put("declined",c.declined).put("notes",c.notes).put("responsible",c.responsible))
        } })
        o.put("medicationsStructured", JSONArray().also { a -> s.medicationsStructured.forEach { m ->
            a.put(JSONObject().put("id",m.id).put("name",m.name).put("activeIngredient",m.activeIngredient).put("dose",m.dose).put("unit",m.unit).put("route",m.route).put("frequency",m.frequency).put("schedule",m.schedule).put("startDate",m.startDate).put("endDate",m.endDate).put("indication",m.indication).put("prescriber",m.prescriber).put("asNeeded",m.asNeeded).put("active",m.active).put("notes",m.notes))
        } })
        return o
    }

    private fun sessionFromJson(o: JSONObject): EducationalSession {
        fun <T> keys(j:JSONObject, f:(String)->T): List<T> { val out=mutableListOf<T>(); val it=j.keys(); while(it.hasNext()) out+=f(it.next()); return out }
        fun surfaceMarks(name:String)=o.optJSONObject(name)?.let { root -> keys(root) { tk -> tk.toInt() to keys(root.getJSONObject(tk)) { sk -> Surface.valueOf(sk) to SurfaceMark.valueOf(root.getJSONObject(tk).getString(sk)) }.toMap() }.toMap() } ?: emptyMap()
        fun surfaceInts(name:String)=o.optJSONObject(name)?.let { root -> keys(root) { tk -> tk.toInt() to keys(root.getJSONObject(tk)) { sk -> Surface.valueOf(sk) to root.getJSONObject(tk).getInt(sk) }.toMap() }.toMap() } ?: emptyMap()
        val teeth=o.optJSONObject("teeth")?.let { j -> keys(j){k-> val v=j.getJSONObject(k); k.toInt() to ToothRecord(ToothStatus.valueOf(v.getString("status")),v.optInt("icdas"),v.optString("diagnosisId").takeIf{it.isNotBlank()&&it!="null"},v.optString("treatmentId").takeIf{it.isNotBlank()&&it!="null"})}.toMap()}?: emptyMap()
        val oleary=o.optJSONObject("oleary")?.let { j -> keys(j){k-> k.toInt() to jsonStrings(j.getJSONArray(k)).map{Surface.valueOf(it)}.toSet()}.toMap()}?: emptyMap()
        val perio=o.optJSONObject("periodontogram")?.let { j -> keys(j){k->k.toInt() to perioFromJson(j.getJSONObject(k))}.toMap()}?: emptyMap()
        return EducationalSession(
            profile=o.optJSONObject("profile")?.let(::profileFromJson)?:PatientProfile(),
            history=o.optJSONObject("history")?.let(::historyFromJson)?:HistoryState(),
            teeth=teeth, odontogramSurfaces=surfaceMarks("odontogramSurfaces"), icdasSurfaces=surfaceInts("icdasSurfaces"),
            oleary=oleary, presentTeeth=jsonInts(o.optJSONArray("presentTeeth")).toSet(), ipcCodes=jsonStrings(o.optJSONArray("ipcCodes")).ifEmpty{List(6){"0"}},
            ihosDebris=jsonIntMap(o.optJSONObject("ihosDebris")), ihosCalculus=jsonIntMap(o.optJSONObject("ihosCalculus")),
            periodontogram=perio, pulpal=o.optJSONObject("pulpal")?.let(::pulpalFromJson)?:PulpalAssessment(),
            clinicalEvents=o.optJSONArray("clinicalEvents")?.let { a -> (0 until a.length()).map { i -> val e=a.getJSONObject(i); ClinicalEvent(e.optLong("timestamp"),e.optString("type"),e.optString("detail")) } } ?: emptyList(),
            clinicalMeasurements=o.optJSONArray("clinicalMeasurements")?.let { a -> (0 until a.length()).map { i -> val m=a.getJSONObject(i); ClinicalMeasurement(m.optLong("timestamp"),m.optInt("systolic").takeIf{m.has("systolic")&&!m.isNull("systolic")},m.optInt("diastolic").takeIf{m.has("diastolic")&&!m.isNull("diastolic")},m.optInt("heartRate").takeIf{m.has("heartRate")&&!m.isNull("heartRate")},m.optInt("respiratoryRate").takeIf{m.has("respiratoryRate")&&!m.isNull("respiratoryRate")},m.optInt("spo2").takeIf{m.has("spo2")&&!m.isNull("spo2")},m.optDouble("temperature").takeIf{m.has("temperature")&&!m.isNull("temperature")},m.optInt("glucose").takeIf{m.has("glucose")&&!m.isNull("glucose")},m.optDouble("weightKg").takeIf{m.has("weightKg")&&!m.isNull("weightKg")},m.optDouble("bmi").takeIf{m.has("bmi")&&!m.isNull("bmi")},m.optInt("pain").takeIf{m.has("pain")&&!m.isNull("pain")}) } } ?: emptyList(),
            informedConsents=o.optJSONArray("informedConsents")?.let { a -> (0 until a.length()).map { i -> val c=a.getJSONObject(i); InformedConsent(c.optLong("timestamp"),c.optString("procedure"),c.optString("toothOrSite"),c.optString("diagnosis"),c.optString("benefits"),c.optString("risks"),c.optString("alternatives"),c.optBoolean("questionsAnswered"),c.optBoolean("understood"),c.optBoolean("accepted"),c.optBoolean("declined"),c.optString("notes"),c.optString("responsible")) } } ?: emptyList(),
            medicationsStructured=o.optJSONArray("medicationsStructured")?.let { a -> (0 until a.length()).map { i -> val m=a.getJSONObject(i); MedicationRecord(m.optString("id").ifBlank{UUID.randomUUID().toString()},m.optString("name"),m.optString("activeIngredient"),m.optString("dose"),m.optString("unit"),m.optString("route"),m.optString("frequency"),m.optString("schedule"),m.optString("startDate"),m.optString("endDate"),m.optString("indication"),m.optString("prescriber"),m.optBoolean("asNeeded"),m.optBoolean("active",true),m.optString("notes")) } } ?: emptyList()
        )
    }

    private fun profileToJson(p:PatientProfile)=JSONObject().put("exerciseName",p.exerciseName).put("patientInitials",p.patientInitials).put("age",p.age).put("sex",p.sex).put("birthDate",p.birthDate).put("occupation",p.occupation).put("reasonForVisit",p.reasonForVisit).put("currentCondition",p.currentCondition).put("medications",p.medications).put("allergies",p.allergies).put("bloodPressure",p.bloodPressure).put("heartRate",p.heartRate).put("respiratoryRate",p.respiratoryRate).put("temperature",p.temperature).put("spo2",p.spo2).put("weightKg",p.weightKg).put("heightCm",p.heightCm).put("bmi",p.bmi).put("cardiovascularHistory",p.cardiovascularHistory).put("anticoagulantsAntiplatelets",p.anticoagulantsAntiplatelets).put("betaBlockersAntiarrhythmics",p.betaBlockersAntiarrhythmics).put("diabetesTreatment",p.diabetesTreatment).put("hepaticRenalDisease",p.hepaticRenalDisease).put("pregnancyStatus",p.pregnancyStatus).put("clinicalSigns",p.clinicalSigns).put("painScore",p.painScore).put("glucose",p.glucose).put("glucoseContext",p.glucoseContext)
    private fun profileFromJson(o:JSONObject)=PatientProfile(
        exerciseName=o.optString("exerciseName"),patientInitials=o.optString("patientInitials"),age=o.optString("age"),sex=o.optString("sex"),birthDate=o.optString("birthDate"),occupation=o.optString("occupation"),reasonForVisit=o.optString("reasonForVisit"),currentCondition=o.optString("currentCondition"),medications=o.optString("medications"),allergies=o.optString("allergies"),bloodPressure=o.optString("bloodPressure"),heartRate=o.optString("heartRate"),respiratoryRate=o.optString("respiratoryRate"),temperature=o.optString("temperature"),spo2=o.optString("spo2"),weightKg=o.optString("weightKg"),heightCm=o.optString("heightCm"),bmi=o.optString("bmi"),cardiovascularHistory=o.optString("cardiovascularHistory"),anticoagulantsAntiplatelets=o.optString("anticoagulantsAntiplatelets"),betaBlockersAntiarrhythmics=o.optString("betaBlockersAntiarrhythmics"),diabetesTreatment=o.optString("diabetesTreatment"),hepaticRenalDisease=o.optString("hepaticRenalDisease"),pregnancyStatus=o.optString("pregnancyStatus"),clinicalSigns=o.optString("clinicalSigns"),painScore=o.optString("painScore"),glucose=o.optString("glucose"),glucoseContext=o.optString("glucoseContext")
    )

    private fun historyToJson(h:HistoryState)=JSONObject().put("diseases",JSONObject().also{j->h.diseases.forEach{(k,v)->j.put(k,JSONObject().put("present",v.present).put("onset",v.onset).put("treatment",v.treatment).put("currentStatus",v.currentStatus).put("complications",v.complications))}}).put("asaClass",h.asaClass).put("asaEmergency",h.asaEmergency).put("tobaccoAlcohol",h.tobaccoAlcohol).put("hospitalizations",h.hospitalizations).put("pregnancy",h.pregnancy)
    private fun historyFromJson(o:JSONObject):HistoryState { val d=o.optJSONObject("diseases"); val m=mutableMapOf<String,DiseaseAnswer>(); if(d!=null){val it=d.keys();while(it.hasNext()){val k=it.next();val v=d.getJSONObject(k);m[k]=DiseaseAnswer(v.optBoolean("present"),v.optString("onset"),v.optString("treatment"),v.optString("currentStatus"),v.optString("complications"))}};return HistoryState(m,o.optInt("asaClass",1),o.optBoolean("asaEmergency"),o.optString("tobaccoAlcohol"),o.optString("hospitalizations"),o.optString("pregnancy")) }

    private fun perioToJson(p:PerioRecord)=JSONObject().put("probingDepths",JSONArray(p.probingDepths)).put("bleeding",p.bleeding).put("plaque",p.plaque).put("suppuration",p.suppuration).put("mobility",p.mobility).put("furcation",p.furcation).put("recessionMm",p.recessionMm).put("bleedingSites",JSONArray(p.bleedingSites.toList())).put("plaqueSites",JSONArray(p.plaqueSites.toList())).put("suppurationSites",JSONArray(p.suppurationSites.toList())).put("recessionBySite",JSONArray(p.recessionBySite))
    private fun perioFromJson(o:JSONObject)=PerioRecord(jsonInts(o.optJSONArray("probingDepths")).ifEmpty{List(6){0}},o.optBoolean("bleeding"),o.optBoolean("plaque"),o.optBoolean("suppuration"),o.optInt("mobility"),o.optInt("furcation"),o.optInt("recessionMm"),jsonInts(o.optJSONArray("bleedingSites")).toSet(),jsonInts(o.optJSONArray("plaqueSites")).toSet(),jsonInts(o.optJSONArray("suppurationSites")).toSet(),jsonInts(o.optJSONArray("recessionBySite")).ifEmpty{List(6){0}})

    private fun pulpalToJson(p:PulpalAssessment)=JSONObject().put("tooth",p.tooth).put("spontaneousPain",p.spontaneousPain).put("nightPain",p.nightPain).put("coldPositive",p.coldPositive).put("coldLingering",p.coldLingering).put("heatPositive",p.heatPositive).put("sweetsPain",p.sweetsPain).put("percussionPain",p.percussionPain).put("palpationPain",p.palpationPain).put("swelling",p.swelling).put("fistula",p.fistula).put("sensitivityNegative",p.sensitivityNegative).put("apicalRadiolucency",p.apicalRadiolucency).put("widenedPdl",p.widenedPdl).put("apicalRadiopacity",p.apicalRadiopacity).put("deepCariesOrExposure",p.deepCariesOrExposure).put("previousRootCanal",p.previousRootCanal).put("previousPartialEndo",p.previousPartialEndo)
    private fun pulpalFromJson(o:JSONObject)=PulpalAssessment(o.optInt("tooth"),o.optBoolean("spontaneousPain"),o.optBoolean("nightPain"),o.optBoolean("coldPositive"),o.optBoolean("coldLingering"),o.optBoolean("heatPositive"),o.optBoolean("sweetsPain"),o.optBoolean("percussionPain"),o.optBoolean("palpationPain"),o.optBoolean("swelling"),o.optBoolean("fistula"),o.optBoolean("sensitivityNegative"),o.optBoolean("apicalRadiolucency"),o.optBoolean("widenedPdl"),o.optBoolean("apicalRadiopacity"),o.optBoolean("deepCariesOrExposure"),o.optBoolean("previousRootCanal"),o.optBoolean("previousPartialEndo"))

    private fun intMap(m:Map<Int,Int>)=JSONObject().also{j->m.forEach{(k,v)->j.put(k.toString(),v)}}
    private fun jsonIntMap(j:JSONObject?):Map<Int,Int>{if(j==null)return emptyMap();val m=mutableMapOf<Int,Int>();val it=j.keys();while(it.hasNext()){val k=it.next();m[k.toInt()]=j.optInt(k)};return m}
    private fun jsonInts(a:JSONArray?):List<Int>{if(a==null)return emptyList();return (0 until a.length()).map{a.optInt(it)}}
    private fun jsonStrings(a:JSONArray?):List<String>{if(a==null)return emptyList();return (0 until a.length()).map{a.optString(it)}}
}
