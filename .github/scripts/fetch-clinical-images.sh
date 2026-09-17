#!/usr/bin/env bash
set -euo pipefail

OUT="app/src/main/res/drawable-nodpi"
mkdir -p "$OUT"

fetch() {
  local url="$1"
  local file="$2"
  echo "Descargando $file"
  curl -L --fail --retry 3 --retry-delay 2 \
    -A "YSM-Expediente-Educational-App/1.0" \
    "$url" -o "$OUT/$file"
  test -s "$OUT/$file"
}

# Exantemas. CDC Public Health Image Library (PHIL): dominio público.
fetch "https://wwwn.cdc.gov/phil///PHIL_Images/20040908/2d4664936550421d85a71364ed879b68/6121_lores.jpg" "clinical_varicella.jpg"
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/10491/10491_lores.jpg" "clinical_smallpox.jpg"
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/4497/4497_lores.jpg" "clinical_measles.jpg"
fetch "https://wwwn.cdc.gov/phil/PHIL_Images/712/712_lores.jpg" "clinical_rubella.jpg"

# Dermatología. Wikimedia Commons; la atribución/licencia se muestra en la app.
fetch "https://upload.wikimedia.org/wikipedia/commons/9/9c/Atopic_dermatitis.png" "clinical_eczema.png"
fetch "https://upload.wikimedia.org/wikipedia/commons/0/01/Psoriasis.jpg" "clinical_psoriasis.jpg"

# Cabeza y cuello. Wikimedia Commons.
fetch "https://upload.wikimedia.org/wikipedia/commons/3/35/Hairline.jpg" "hairline_reference.jpg"
fetch "https://upload.wikimedia.org/wikipedia/commons/5/5a/Head_diameter_measurement.jpg" "head_circumference_reference.jpg"

# Anatomía de la ATM. OpenStax College, CC BY 3.0, Wikimedia Commons.
fetch "https://commons.wikimedia.org/wiki/Special:Redirect/file/913%20Tempomandibular%20Joint.jpg" "tmj_anatomy.jpg"

echo "Imágenes clínicas descargadas correctamente."
