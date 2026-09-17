#!/usr/bin/env bash
set -euo pipefail

OUT="app/src/main/res/drawable-nodpi"
SRC="$OUT/folder_cover_ysm.webp"
mkdir -p "$OUT"

# Recursos proporcionados por la autora.
cp "$SRC" "$OUT/kennedy_reference.webp"
cp "$SRC" "$OUT/contraceptives_reference.webp"
cp "$SRC" "$OUT/general_inspection_reference.webp"
cp "$SRC" "$OUT/cranial_shape_reference.webp"

# Marcadores neutros válidos. Sólo permanecen si una descarga externa falla.
# Es importante que el contenido binario coincida con la extensión para AAPT2.
PNG_PLACEHOLDER="$RUNNER_TEMP/ysm-placeholder.png"
JPG_PLACEHOLDER="$RUNNER_TEMP/ysm-placeholder.jpg"
printf '%s' 'iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAIAAAAlC+aJAAAAYUlEQVR4nO3PQQ0AIBDAMMC/3fsjgkdDsirY9sysnx0d8KoBrQGtAa0BrQGtAa0BrQGtAa0BrQGtAa0BrQGtAa0BrQGtAa0BrQGtAa0BrQGtAa0BrQGtAa0BrQGtAa0B7QLLDANfMvOvcQAAAABJRU5ErkJggg==' | base64 -d > "$PNG_PLACEHOLDER"
printf '%s' '/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBT/wAARCABAAEADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD9OKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAP/2Q==' | base64 -d > "$JPG_PLACEHOLDER"

PNG_FILES=(
  clinical_eczema.png ref_tmj_anatomy.png ref_diastema.png
  ref_dens_invaginatus.png ref_impacted_second_molar.png
)
JPG_FILES=(
  clinical_varicella.jpg clinical_smallpox.jpg clinical_measles.jpg clinical_rubella.jpg
  clinical_psoriasis.jpg hairline_reference.jpg head_circumference_reference.jpg
  ref_tmj_mri.jpg ref_tmj_movements.jpg ref_tmj_panorama.jpg
  ref_deciduous_teeth.jpg ref_angle_i.jpg ref_angle_ii.jpg ref_angle_iii.jpg
  ref_overjet.jpg ref_open_bite.jpg ref_deep_bite.jpg ref_midline.jpg
  ref_crossbite_anterior.jpg ref_crossbite_posterior.jpg ref_crowding.jpg
  ref_hypodontia.jpg ref_supernumerary.jpg ref_microdontia.jpg ref_macrodontia.jpg
  ref_fusion.jpg ref_dens_evaginatus.jpg ref_taurodontism.jpg
  ref_enamel_hypoplasia.jpg ref_hypomineralization.jpg
  ref_impacted_canine.jpg ref_impacted_panorama.jpg ref_infant_teeth.jpg
  ref_aphthous.jpg ref_leukoplakia.jpg ref_lichen_planus.jpg ref_herpes.jpg
  ref_candidiasis.jpg ref_mucocele.jpg ref_ranula.jpg ref_geographic_tongue.jpg
  ref_fissured_tongue.jpg ref_angular_cheilitis.jpg
)

for f in "${PNG_FILES[@]}"; do cp "$PNG_PLACEHOLDER" "$OUT/$f"; done
for f in "${JPG_FILES[@]}"; do cp "$JPG_PLACEHOLDER" "$OUT/$f"; done
