#!/usr/bin/env bash
set -uo pipefail

OUT="app/src/main/res/drawable-nodpi"
mkdir -p "$OUT"

fetch() {
  local url="$1"
  local file="$2"
  local tmp="$OUT/.${file}.download.$$.$RANDOM"
  echo "Descargando $file"
  if curl -L --fail --connect-timeout 3 --max-time 12 --retry 1 --retry-delay 1 --retry-max-time 25 \
    -A "YSM-Expediente-Educational-App/1.0" \
    "$url" -o "$tmp" >/dev/null 2>&1 && [ -s "$tmp" ]; then
    mv -f "$tmp" "$OUT/$file"
    echo "OK $file"
  else
    rm -f "$tmp"
    echo "AVISO: no se pudo descargar $file; se conserva el recurso local de reserva." >&2
  fi
  return 0
}

commons() {
  local encoded_name="$1"
  local file="$2"
  fetch "https://commons.wikimedia.org/wiki/Special:Redirect/file/${encoded_name}" "$file"
}

# Retratos principales. Se empaquetan en drawable-nodpi para conservar la fotografía completa.
# Doctor: U.S. Air Force / DVIDS, dominio público (2026), cirujano oral y maxilofacial en entorno clínico.
commons "86th%20Dental%20Squadron%E2%80%99s%20Maj%20Van%20Hoof%20sees%20the%20person%20behind%20the%20procedure%20%289505983%29.jpg" "clinician_doctor_photo.jpg" &
# Doctora: Erik Christensen, CC BY-SA 3.0, odontóloga y asistente en consultorio dental.
commons "Dentist.2.jpg" "clinician_doctora_photo.jpg" &

# Atlas ICDAS 0–6.
# Fuente clínica y licencia: Gugnani N, Pandit IK, Srivastava N, Gupta M, Sharma M.
# International Caries Detection and Assessment System (ICDAS): A New Concept.
# Int J Clin Pediatr Dent. 2011;4(2):93-100. Fig. 1A-G. CC BY 3.0.
# https://pmc.ncbi.nlm.nih.gov/articles/PMC5030492/
# Se intenta primero extraer la página que contiene la figura desde el PDF oficial del editor.
# Si el runner no dispone de pdftoppm o el editor bloquea la descarga, se usa un espejo visual
# de la misma secuencia educativa únicamente como respaldo técnico.
fetch_icdas_atlas() {
  local pdf="$OUT/.icdas_article.$.$RANDOM.pdf"
  local prefix="$OUT/.icdas_page.$.$RANDOM"
  local target="$OUT/icdas_codes_photo.jpg"
  local official_pdf="https://www.ijcpd.com/doi/pdf/10.5005/jp-journals-10005-1089"
  local mirror="https://image.slidesharecdn.com/icdascariesppt-200302055140/75/Icdas-caries-ppt-8-2048.jpg"

  echo "Preparando atlas fotográfico ICDAS 0–6"
  if curl -L --fail --connect-timeout 4 --max-time 25 --retry 1 --retry-delay 1 --retry-max-time 35 \
    -A "YSM-Expediente-Educational-App/1.0" "$official_pdf" -o "$pdf" >/dev/null 2>&1 \
    && [ -s "$pdf" ] && command -v pdftoppm >/dev/null 2>&1; then
    if pdftoppm -f 3 -l 3 -singlefile -jpeg -r 180 "$pdf" "$prefix" >/dev/null 2>&1 \
      && [ -s "$prefix.jpg" ]; then
      mv -f "$prefix.jpg" "$target"
      rm -f "$pdf"
      echo "OK icdas_codes_photo.jpg · fuente CC BY 3.0"
      return 0
    fi
  fi

  rm -f "$pdf" "$prefix.jpg"
  if curl -L --fail --connect-timeout 4 --max-time 20 --retry 1 --retry-delay 1 --retry-max-time 30 \
    -A "YSM-Expediente-Educational-App/1.0" "$mirror" -o "$target" >/dev/null 2>&1 \
    && [ -s "$target" ]; then
    echo "OK icdas_codes_photo.jpg · respaldo visual; citar fuente clínica original CC BY 3.0"
    return 0
  fi

  rm -f "$target"
  echo "AVISO: no se pudo preparar el atlas ICDAS; se conservará el recurso local de reserva." >&2
  return 0
}

fetch_icdas_atlas &

# Referencias clínicas.
fetch "https://wwwn.cdc.gov/phil///PHIL_Images/20040908/2d4664936550421d85a71364ed879b68/6121_lores.jpg" "clinical_varicella.jpg" &
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/10491/10491_lores.jpg" "clinical_smallpox.jpg" &
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/4497/4497_lores.jpg" "clinical_measles.jpg" &
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/712/712_lores.jpg" "clinical_rubella.jpg" &
commons "Atopic_dermatitis.png" "clinical_eczema.png" &
commons "Psoriasis.jpg" "clinical_psoriasis.jpg" &
commons "Hairline.jpg" "hairline_reference.jpg" &
commons "Head_diameter_measurement.jpg" "head_circumference_reference.jpg" &
commons "Temporomandibular%20joint.png" "ref_tmj_anatomy.png" &
commons "ARTICULACION%20TEMPOROMANDIBULAR.jpg" "ref_tmj_mri.jpg" &
commons "TMJ%20movements.jpg" "ref_tmj_movements.jpg" &
commons "TMJ%20panorama.jpg" "ref_tmj_panorama.jpg" &
commons "Deciduous%20teeth%20by%20David%20Shankbone%20new.jpg" "ref_deciduous_teeth.jpg" &
commons "Class%201%20bimaxillary%20protrusion.jpg" "ref_angle_i.jpg" &
commons "Zahnfehlstellung%20Angle-Klasse%20II-1.jpg" "ref_angle_ii.jpg" &
commons "Zahnfehlstellung%20Angle-Klasse%20III.jpg" "ref_angle_iii.jpg" &
commons "Overjet.jpg" "ref_overjet.jpg" &
commons "Anterior%20open%20bite%20malocclusion.jpg" "ref_open_bite.jpg" &
commons "Deep%20bite.jpg" "ref_deep_bite.jpg" &
commons "Canted%20occlusal%20plane.jpg" "ref_midline.jpg" &
commons "Anterior%20crossbite.jpg" "ref_crossbite_anterior.jpg" &
commons "Crossbite.jpg" "ref_crossbite_posterior.jpg" &
commons "Brian%20diastema.png" "ref_diastema.png" &
commons "Sever%20Crowding%20of%20teeth.jpg" "ref_crowding.jpg" &
commons "HIPODONCIA%20DENTAL.jpg" "ref_hypodontia.jpg" &
commons "Supernumerary%20teeth.jpg" "ref_supernumerary.jpg" &
commons "Axenfeld%20syndrome.jpg" "ref_microdontia.jpg" &
commons "Kbg.jpg" "ref_macrodontia.jpg" &
commons "DentalFusion.jpg" "ref_fusion.jpg" &
commons "Dens%20invaginatus%20-%20Typen%20nach%20Oehlers%201957.png" "ref_dens_invaginatus.png" &
commons "Dens%20evaginatus.jpg" "ref_dens_evaginatus.jpg" &
commons "Taurodontism.jpg" "ref_taurodontism.jpg" &
commons "Teeth%20displaying%20Enamel%20hypoplasia%20lines.jpg" "ref_enamel_hypoplasia.jpg" &
commons "Hipomineralizaci%C3%B3n%20en%20Incisivos.jpg" "ref_hypomineralization.jpg" &
commons "Impactedcanine.JPG" "ref_impacted_canine.jpg" &
commons "Impacted%202nd%20molar.png" "ref_impacted_second_molar.png" &
commons "Basic%20panoramic%20radiograph.jpg" "ref_impacted_panorama.jpg" &
commons "Baby%20teeth%20in%20human%20infant.jpg" "ref_infant_teeth.jpg" &
commons "Aphthous%20ulcer.jpg" "ref_aphthous.jpg" &
commons "Orale%20Leukoplakie.jpg" "ref_leukoplakia.jpg" &
commons "Lichen%20planus.jpg" "ref_lichen_planus.jpg" &
commons "Herpes%20labialis.jpg" "ref_herpes.jpg" &
commons "Human%20tongue%20infected%20with%20oral%20candidiasis.jpg" "ref_candidiasis.jpg" &
commons "Mucocele%20of%20Lower%20Lip%20%2849425670122%29.jpg" "ref_mucocele.jpg" &
commons "Ranula%20human%2009.jpg" "ref_ranula.jpg" &
commons "Geographic%20tongue.JPG" "ref_geographic_tongue.jpg" &
commons "Fissured%20geographic%20tongue.jpg" "ref_fissured_tongue.jpg" &
commons "Angular%20Cheilitis.JPG" "ref_angular_cheilitis.jpg" &

wait || true

echo "Referencias clínicas y retratos odontológicos procesados. Las descargas válidas se empaquetan dentro del APK; las fallidas conservan el recurso local de reserva."
