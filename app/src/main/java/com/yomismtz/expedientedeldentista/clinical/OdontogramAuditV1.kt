package com.yomismtz.expedientedeldentista.clinical

data class OdontogramAuditIssue(
    val tooth: Int,
    val code: String,
    val messageEs: String,
    val messageEn: String
)

/**
 * Finds contradictions between whole-tooth status, the conventional odontogram and ICDAS.
 * It never changes or deletes clinical findings automatically.
 */
fun auditOdontogram(session: EducationalSession): List<OdontogramAuditIssue> {
    val issues = mutableListOf<OdontogramAuditIssue>()
    session.teeth.forEach { (tooth, record) ->
        val marks = session.odontogramSurfaces[tooth].orEmpty()
        val icdas = session.icdasSurfaceRecords[tooth].orEmpty()
        val specialCodes = icdas.values.mapNotNull { it.specialCode }.toSet()
        val absent = record.status == ToothStatus.MISSING_CARIES || record.status == ToothStatus.MISSING_OTHER
        if (absent && marks.isNotEmpty()) {
            issues += OdontogramAuditIssue(tooth, "MISSING_WITH_SURFACE_MARKS",
                "El diente está marcado ausente, pero conserva marcas de superficies.",
                "The tooth is marked missing but still has conventional surface marks.")
        }
        val compatibleSpecialStatus = when (record.status) {
            ToothStatus.MISSING_CARIES -> 97 in specialCodes
            ToothStatus.MISSING_OTHER -> 98 in specialCodes
            ToothStatus.UNERUPTED -> 99 in specialCodes
            else -> false
        }
        if ((absent || record.status == ToothStatus.UNERUPTED) && icdas.isNotEmpty() && !compatibleSpecialStatus) {
            issues += OdontogramAuditIssue(tooth, "TOOTH_STATUS_WITH_ICDAS",
                "El estado del diente no coincide con los registros ICDAS conservados; revisar antes de cerrar.",
                "The tooth status conflicts with retained ICDAS records; review before closing.")
        }
        if (record.status != ToothStatus.MISSING_CARIES && 97 in specialCodes) {
            issues += OdontogramAuditIssue(tooth, "ICDAS_97_STATUS_MISMATCH",
                "ICDAS 97 indica ausencia por caries; revisar el estado general del diente.",
                "ICDAS 97 indicates missing due to caries; review whole-tooth status.")
        }
        if (record.status != ToothStatus.MISSING_OTHER && 98 in specialCodes) {
            issues += OdontogramAuditIssue(tooth, "ICDAS_98_STATUS_MISMATCH",
                "ICDAS 98 indica ausencia por otra causa; revisar el estado general del diente.",
                "ICDAS 98 indicates missing for another reason; review whole-tooth status.")
        }
        if (record.status != ToothStatus.UNERUPTED && 99 in specialCodes) {
            issues += OdontogramAuditIssue(tooth, "ICDAS_99_STATUS_MISMATCH",
                "ICDAS 99 indica diente no erupcionado; revisar el estado general del diente.",
                "ICDAS 99 indicates an unerupted tooth; review whole-tooth status.")
        }
        marks.forEach { (surface, mark) ->
            val code = icdas[surface] ?: return@forEach
            if (mark == SurfaceMark.CARIES && code.cariesCode == 0 && code.specialCode == null) {
                issues += OdontogramAuditIssue(tooth, "CARIES_SURFACE_ICDAS_SOUND",
                    "La cara $surface está marcada con caries en el odontograma, pero ICDAS registra caries 0.",
                    "Surface $surface is marked carious in the odontogram but ICDAS records caries 0.")
            }
            if (mark == SurfaceMark.RESTORATION && code.restorationCode == 0 && code.specialCode == null) {
                issues += OdontogramAuditIssue(tooth, "RESTORATION_SURFACE_ICDAS_UNRESTORED",
                    "La cara $surface está marcada como restauración, pero ICDAS registra restauración 0.",
                    "Surface $surface is marked restored but ICDAS records restoration 0.")
            }
        }
    }
    return issues.distinctBy { Triple(it.tooth, it.code, it.messageEs) }
}
