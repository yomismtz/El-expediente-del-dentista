package com.yomismtz.expedientedeldentista.ui

internal data class ClinicalVitalBandV20(val labelEs:String,val labelEn:String,val rrMin:Int,val rrMax:Int,val hrMin:Int,val hrMax:Int,val source:String)

internal fun clinicalVitalBandV20(age:Int):ClinicalVitalBandV20 = when {
    age < 1 -> ClinicalVitalBandV20("0–11 meses","0–11 months",21,45,100,159,"CEWT · Children's Health Queensland · septiembre 2026")
    age <= 4 -> ClinicalVitalBandV20("1–4 años","1–4 years",16,35,90,139,"CEWT · Children's Health Queensland · septiembre 2026")
    age <= 11 -> ClinicalVitalBandV20("5–11 años","5–11 years",16,30,80,129,"CEWT · Children's Health Queensland · septiembre 2026")
    age <= 17 -> ClinicalVitalBandV20("12–17 años","12–17 years",16,25,60,119,"CEWT · Children's Health Queensland · septiembre 2026")
    else -> ClinicalVitalBandV20("≥18 años","≥18 years",12,20,60,100,"Referencia adulta educativa")
}

internal fun clinicalVitalMeaningV20(parameter:String,value:Double?,min:Double,max:Double,lang:String):String {
    if(value==null)return tr(lang,"Sin dato.","No value.")
    return when {
        value<min->when(parameter){
            "FR"->tr(lang,"BAJA: puede indicar bradipnea o depresión respiratoria; correlacionar con sedantes, opioides, estado de conciencia y enfermedad neurológica.","LOW: may indicate bradypnea or respiratory depression; correlate with sedatives, opioids, mental status and neurologic disease.")
            "FC"->tr(lang,"BAJA: puede corresponder a bradicardia; valorar síntomas, medicamentos y perfusión.","LOW: may represent bradycardia; assess symptoms, medications and perfusion.")
            else->tr(lang,"Por debajo de la referencia; repetir y contextualizar.","Below reference; repeat and contextualize.")
        }
        value>max->when(parameter){
            "FR"->tr(lang,"ALTA: taquipnea; puede aparecer con fiebre, dolor, ansiedad, hipoxemia, acidosis o enfermedad respiratoria.","HIGH: tachypnea; may occur with fever, pain, anxiety, hypoxemia, acidosis or respiratory disease.")
            "FC"->tr(lang,"ALTA: taquicardia; puede aparecer con dolor, ansiedad, fiebre, deshidratación, hipoxemia, anemia o fármacos.","HIGH: tachycardia; may occur with pain, anxiety, fever, dehydration, hypoxemia, anemia or medications.")
            else->tr(lang,"Por encima de la referencia; repetir y contextualizar.","Above reference; repeat and contextualize.")
        }
        else->tr(lang,"Dentro de la referencia etaria.","Within the age reference.")
    }
}

internal fun clinicalReferenceSourcesV20(lang:String):String=tr(lang,
    "Referencias: AAP 2017 para TA pediátrica por edad/sexo/talla; CEWT/Children's Health Queensland para FC/FR por edad; ADA Standards 2026 para glucosa; FDA para limitaciones de SpO₂; AHA para TA adulta. La tabla pediátrica simplificada es sólo de cribado.",
    "References: AAP 2017 for pediatric BP by age/sex/height; CEWT/Children's Health Queensland for age-based HR/RR; ADA 2026 for glucose; FDA for SpO₂ limitations; AHA for adult BP. The simplified pediatric table is screening-only."
)