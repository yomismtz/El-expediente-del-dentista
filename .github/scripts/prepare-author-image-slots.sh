#!/usr/bin/env bash
set -euo pipefail

OUT="app/src/main/res/drawable-nodpi"
SRC="$OUT/folder_cover_ysm.webp"
mkdir -p "$OUT"

# Recursos proporcionados por la autora.
# Se reservan únicamente estos slots locales. Las imágenes clínicas externas NO
# reciben placeholders: fetch-clinical-images.sh debe obtener y validar el recurso
# real o la compilación falla.
cp "$SRC" "$OUT/kennedy_reference.webp"
cp "$SRC" "$OUT/contraceptives_reference.webp"
cp "$SRC" "$OUT/general_inspection_reference.webp"
cp "$SRC" "$OUT/cranial_shape_reference.webp"
