#!/usr/bin/env bash
set -euo pipefail

OUT="app/src/main/res/drawable-nodpi"
mkdir -p "$OUT"

fetch() {
  local url="$1"
  local file="$2"
  echo "Descargando $file"
  if ! curl -L --fail --connect-timeout 4 --max-time 12 --retry 1 --retry-delay 1 \
    -A "YSM-Expediente-Educational-App/1.0" \
    "$url" -o "$OUT/$file"; then
    echo "AVISO: no se pudo descargar $file; la compilación continuará y la app mostrará la referencia disponible empaquetada/local." >&2
    rm -f "$OUT/$file"
    return 0
  fi
  if [ ! -s "$OUT/$file" ]; then
    echo "AVISO: $file quedó vacío; se elimina y continúa la compilación." >&2
    rm -f "$OUT/$file"
  fi
}

commons() {
  local encoded_name="$1"
  local file="$2"
  fetch "https://commons.wikimedia.org/wiki/Special:Redirect/file/${encoded_name}" "$file"
}

# Exantemas. CDC Public Health Image Library (PHIL): dominio público.
fetch "https://wwwn.cdc.gov/phil///PHIL_Images/20040908/2d4664936550421d85a71364ed879b68/6121_lores.jpg" "clinical_varicella.jpg"
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/10491/10491_lores.jpg" "clinical_smallpox.jpg"
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/4497/4497_lores.jpg" "clinical_measles.jpg"
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/712/712_lores.jpg" "clinical_rubella.jpg"

# Dermatología. Wikimedia Commons; atribución/licencia documentadas en privacy.html.
commons "Atopic_dermatitis.png" "clinical_eczema.png"
commons "Psoriasis.jpg" "clinical_psoriasis.jpg"

# Cabeza y cuello. Wikimedia Commons.
commons "Hairline.jpg" "hairline_reference.jpg"
commons "Head_diameter_measurement.jpg" "head_circumference_reference.jpg"

# ATM: anatomía, RM, movimientos e imagen radiográfica real.
commons "Temporomandibular%20joint.png" "ref_tmj_anatomy.png"
commons "ARTICULACION%20TEMPOROMANDIBULAR.jpg" "ref_tmj_mri.jpg"
commons "TMJ%20movements.jpg" "ref_tmj_movements.jpg"
commons "TMJ%20panorama.jpg" "ref_tmj_panorama.jpg"

# Oclusión: fotografías clínicas reales.
commons "Deciduous%20teeth%20by%20David%20Shankbone%20new.jpg" "ref_deciduous_teeth.jpg"
commons "Class%201%20bimaxillary%20protrusion.jpg" "ref_angle_i.jpg"
commons "Zahnfehlstellung%20Angle-Klasse%20II-1.jpg" "ref_angle_ii.jpg"
commons "Zahnfehlstellung%20Angle-Klasse%20III.jpg" "ref_angle_iii.jpg"
commons "Overjet.jpg" "ref_overjet.jpg"
commons "Anterior%20open%20bite%20malocclusion.jpg" "ref_open_bite.jpg"
commons "Deep%20bite.jpg" "ref_deep_bite.jpg"
commons "Canted%20occlusal%20plane.jpg" "ref_midline.jpg"
commons "Anterior%20crossbite.jpg" "ref_crossbite_anterior.jpg"
commons "Crossbite.jpg" "ref_crossbite_posterior.jpg"
commons "Brian%20diastema.png" "ref_diastema.png"
commons "Sever%20Crowding%20of%20teeth.jpg" "ref_crowding.jpg"

# Anomalías dentales: fotografías/radiografías reales o figuras clínicas publicadas.
commons "HIPODONCIA%20DENTAL.jpg" "ref_hypodontia.jpg"
commons "Supernumerary%20teeth.jpg" "ref_supernumerary.jpg"
commons "Axenfeld%20syndrome.jpg" "ref_microdontia.jpg"
commons "Kbg.jpg" "ref_macrodontia.jpg"
commons "DentalFusion.jpg" "ref_fusion.jpg"
commons "Dens%20invaginatus%20-%20Typen%20nach%20Oehlers%201957.png" "ref_dens_invaginatus.png"
commons "Dens%20evaginatus.jpg" "ref_dens_evaginatus.jpg"
commons "Taurodontism.jpg" "ref_taurodontism.jpg"
commons "Teeth%20displaying%20Enamel%20hypoplasia%20lines.jpg" "ref_enamel_hypoplasia.jpg"
commons "Hipomineralizaci%C3%B3n%20en%20Incisivos.jpg" "ref_hypomineralization.jpg"

# Erupción/posición: radiografías y fotografía clínica de referencia.
commons "Impactedcanine.JPG" "ref_impacted_canine.jpg"
commons "Impacted%202nd%20molar.png" "ref_impacted_second_molar.png"
commons "Basic%20panoramic%20radiograph.jpg" "ref_impacted_panorama.jpg"
commons "Baby%20teeth%20in%20human%20infant.jpg" "ref_infant_teeth.jpg"

# Mucosas: fotografías clínicas reales.
commons "Aphthous%20ulcer.jpg" "ref_aphthous.jpg"
commons "Orale%20Leukoplakie.jpg" "ref_leukoplakia.jpg"
commons "Lichen%20planus.jpg" "ref_lichen_planus.jpg"
commons "Herpes%20labialis.jpg" "ref_herpes.jpg"
commons "Human%20tongue%20infected%20with%20oral%20candidiasis.jpg" "ref_candidiasis.jpg"
commons "Mucocele%20of%20Lower%20Lip%20%2849425670122%29.jpg" "ref_mucocele.jpg"
commons "Ranula%20human%2009.jpg" "ref_ranula.jpg"
commons "Geographic%20tongue.JPG" "ref_geographic_tongue.jpg"
commons "Fissured%20geographic%20tongue.jpg" "ref_fissured_tongue.jpg"
commons "Angular%20Cheilitis.JPG" "ref_angular_cheilitis.jpg"

echo "Descarga de referencias clínicas finalizada; los fallos individuales no bloquean la compilación."
