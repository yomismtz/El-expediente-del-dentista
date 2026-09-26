package com.yomismtz.expedientedeldentista.clinical

import android.content.Context
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

    fun loadAll(): List<SavedRecord> {
        val raw = prefs.getString("records", "[]") ?: "[]"
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

    private fun write(records: List<SavedRecord>) {
        val a = JSONArray()
        records.forEach { a.put(recordToJson(it)) }
        prefs.edit().putString("records", a.toString()).apply()
    }

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
            periodontogram=perio, pulpal=o.optJSONObject("pulpal")?.let(::pulpalFromJson)?:PulpalAssessment()
        )
    }

    private fun profileToJson(p:PatientProfile)=JSONObject().put("exerciseName",p.exerciseName).put("patientInitials",p.patientInitials).put("age",p.age).put("sex",p.sex).put("birthDate",p.birthDate).put("occupation",p.occupation).put("reasonForVisit",p.reasonForVisit).put("currentCondition",p.currentCondition).put("medications",p.medications).put("allergies",p.allergies).put("bloodPressure",p.bloodPressure).put("heartRate",p.heartRate).put("respiratoryRate",p.respiratoryRate).put("temperature",p.temperature)
    private fun profileFromJson(o:JSONObject)=PatientProfile(exerciseName=o.optString("exerciseName"),patientInitials=o.optString("patientInitials"),age=o.optString("age"),sex=o.optString("sex"),birthDate=o.optString("birthDate"),occupation=o.optString("occupation"),reasonForVisit=o.optString("reasonForVisit"),currentCondition=o.optString("currentCondition"),medications=o.optString("medications"),allergies=o.optString("allergies"),bloodPressure=o.optString("bloodPressure"),heartRate=o.optString("heartRate"),respiratoryRate=o.optString("respiratoryRate"),temperature=o.optString("temperature"))

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
