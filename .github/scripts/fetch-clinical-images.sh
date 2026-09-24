#!/usr/bin/env bash
set -euo pipefail

OUT="app/src/main/res/drawable-nodpi"
mkdir -p "$OUT"

fetch() {
  local url="$1"
  local file="$2"
  local tmp="$OUT/.${file}.download.$$.$RANDOM"
  echo "Descargando $file"
  if curl -L --fail --connect-timeout 6 --max-time 45 --retry 3 --retry-delay 2 --retry-max-time 140 \
    -A "YSM-Expediente-Educational-App/1.0 (https://github.com/yomismtz/El-expediente-del-dentista)" \
    "$url" -o "$tmp" >/dev/null 2>&1 && [ -s "$tmp" ]; then
    mv -f "$tmp" "$OUT/$file"
    echo "OK $file"
  else
    rm -f "$tmp"
    echo "AVISO: no se pudo obtener $file; la validación obligatoria impedirá publicar un recurso faltante." >&2
  fi
  return 0
}

commons() {
  local encoded_name="$1"
  local file="$2"
  local api="https://commons.wikimedia.org/w/api.php?action=query&format=json&formatversion=2&prop=imageinfo&iiprop=url&iiurlwidth=1600&titles=File%3A${encoded_name}"
  local json direct

  echo "Resolviendo Wikimedia Commons: $file"
  if ! json="$(curl -L --fail --connect-timeout 6 --max-time 30 --retry 3 --retry-delay 2 \
      -A "YSM-Expediente-Educational-App/1.0 (https://github.com/yomismtz/El-expediente-del-dentista)" \
      "$api" 2>/dev/null)"; then
    echo "AVISO: no se pudo resolver la ficha de Commons para $file." >&2
    return 0
  fi

  direct="$(printf '%s' "$json" | python3 -c '
import json, sys
try:
    data=json.load(sys.stdin)
    page=data["query"]["pages"][0]
    info=page["imageinfo"][0]
    print(info.get("thumburl") or info["url"])
except Exception:
    pass
')" || true

  if [ -z "$direct" ]; then
    echo "AVISO: Commons no devolvió URL original para $file." >&2
    return 0
  fi
  fetch "$direct" "$file"
  sleep 0.35
}

# Retratos principales.
commons "86th%20Dental%20Squadron%E2%80%99s%20Maj%20Van%20Hoof%20sees%20the%20person%20behind%20the%20procedure%20%289505983%29.jpg" "clinician_doctor_photo.jpg"
commons "Dentist.2.jpg" "clinician_doctora_photo.jpg"

# Referencias clínicas exantemáticas y dermatológicas.
fetch "https://wwwn.cdc.gov/phil///PHIL_Images/20040908/2d4664936550421d85a71364ed879b68/6121_lores.jpg" "clinical_varicella.jpg"
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/10491/10491_lores.jpg" "clinical_smallpox.jpg"
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/4497/4497_lores.jpg" "clinical_measles.jpg"
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/712/712_lores.jpg" "clinical_rubella.jpg"
commons "Atopic%20dermatitis%20close%20up%20ac.jpeg" "clinical_eczema.jpg"
commons "2803%20Psoriasis.jpg" "clinical_psoriasis.jpg"

# Antecedentes personales patológicos: anatomía educativa para enfermedades internas.
# NIDDK Media Library: recursos sin costo para reutilización; crédito requerido a NIDDK/NIH.
# Se empaquetan en el APK durante el build para que el atlas funcione sin conexión.
fetch "https://www.niddk.nih.gov/media-assets/17489/N00039-H.jpg" "pathological_digestive_niddk.jpg"
fetch "https://www.niddk.nih.gov/media-assets/17940/N01055-H.jpg" "pathological_kidney_niddk.jpg"
fetch "https://www.niddk.nih.gov/media-assets/17934/N01124-H.jpg" "pathological_endocrine_niddk.jpg"

# Referencias antropométricas, ATM, dentición y oclusión.
commons "Hairline.jpg" "hairline_reference.jpg"
commons "Head_diameter_measurement.jpg" "head_circumference_reference.jpg"
commons "Temporomandibular%20joint.png" "ref_tmj_anatomy.png"
commons "ARTICULACION%20TEMPOROMANDIBULAR.jpg" "ref_tmj_mri.jpg"
commons "TMJ%20movements.jpg" "ref_tmj_movements.jpg"
commons "TMJ%20panorama.jpg" "ref_tmj_panorama.jpg"
commons "Deciduous%20teeth%20by%20David%20Shankbone%20new.jpg" "ref_deciduous_teeth.jpg"
commons "MixedDentition.jpg" "ref_mixed_dentition.jpg"
commons "PermanentTeeth.jpg" "ref_permanent_dentition.jpg"
commons "Class%201%20bimaxillary%20protrusion.jpg" "ref_angle_i.jpg"
commons "Class2division1malocclusion.jpg" "ref_angle_ii.jpg"
commons "Class%203%20Malocclusion.jpg" "ref_angle_iii.jpg"
commons "Overjet.jpg" "ref_overjet.jpg"
commons "Anterior%20open%20bite%20malocclusion.jpg" "ref_open_bite.jpg"
commons "Deep%20bite.jpg" "ref_deep_bite.jpg"
commons "Deviated%20midline%202.JPG" "ref_midline.jpg"
commons "Anterior%20crossbite.jpg" "ref_crossbite_anterior.jpg"
commons "Brian%20diastema.png" "ref_diastema.png"
commons "Sever%20Crowding%20of%20teeth.jpg" "ref_crowding.jpg"
commons "HIPODONCIA%20DENTAL.jpg" "ref_hypodontia.jpg"
commons "Supernumerary%20teeth.jpg" "ref_supernumerary.jpg"
commons "DentalFusion.jpg" "ref_fusion.jpg"
commons "Dens%20invaginatus%20-%20Typen%20nach%20Oehlers%201957.png" "ref_dens_invaginatus.png"
commons "Taurodontism.jpg" "ref_taurodontism.jpg"
commons "Teeth%20displaying%20Enamel%20hypoplasia%20lines.jpg" "ref_enamel_hypoplasia.jpg"
commons "Hipomineralizaci%C3%B3n%20en%20Incisivos.jpg" "ref_hypomineralization.jpg"
commons "Impactedcanine.JPG" "ref_impacted_canine.jpg"
commons "Impacted%202nd%20molar.png" "ref_impacted_second_molar.png"
commons "Basic%20panoramic%20radiograph.jpg" "ref_impacted_panorama.jpg"
commons "Baby%20teeth%20in%20human%20infant.jpg" "ref_infant_teeth.jpg"

# Mucosa oral; candidiasis también sirve como manifestación visible en el atlas patológico.
commons "Aphthous%20ulcer.jpg" "ref_aphthous.jpg"
commons "Orale%20Leukoplakie.jpg" "ref_leukoplakia.jpg"
commons "Lichen%20planus.jpg" "ref_lichen_planus.jpg"
commons "Herpes%20labialis.jpg" "ref_herpes.jpg"
commons "Human%20tongue%20infected%20with%20oral%20candidiasis.jpg" "ref_candidiasis.jpg"
commons "Mucocele02-17-06cropped.jpg" "ref_mucocele.jpg"
commons "Ranula%20human%2009.jpg" "ref_ranula.jpg"
commons "Geographic%20tongue.JPG" "ref_geographic_tongue.jpg"
commons "Fissured%20geographic%20tongue.jpg" "ref_fissured_tongue.jpg"
commons "Angular%20Cheilitis.JPG" "ref_angular_cheilitis.jpg"

python3 .github/scripts/verify-clinical-images.py "$OUT"
echo "Referencias clínicas y recursos patológicos verificados y empaquetados para uso sin conexión."
