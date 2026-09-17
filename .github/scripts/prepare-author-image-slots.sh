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

# Marcadores de reserva para que aapt2 siempre tenga los IDs del atlas.
# fetch-clinical-images.sh reemplaza cada archivo sólo si la descarga real fue exitosa.
for f in \
  clinical_varicella.jpg clinical_smallpox.jpg clinical_measles.jpg clinical_rubella.jpg \
  clinical_eczema.png clinical_psoriasis.jpg \
  hairline_reference.jpg head_circumference_reference.jpg \
  ref_tmj_anatomy.png ref_tmj_mri.jpg ref_tmj_movements.jpg ref_tmj_panorama.jpg \
  ref_deciduous_teeth.jpg ref_angle_i.jpg ref_angle_ii.jpg ref_angle_iii.jpg \
  ref_overjet.jpg ref_open_bite.jpg ref_deep_bite.jpg ref_midline.jpg \
  ref_crossbite_anterior.jpg ref_crossbite_posterior.jpg ref_diastema.png ref_crowding.jpg \
  ref_hypodontia.jpg ref_supernumerary.jpg ref_microdontia.jpg ref_macrodontia.jpg \
  ref_fusion.jpg ref_dens_invaginatus.png ref_dens_evaginatus.jpg ref_taurodontism.jpg \
  ref_enamel_hypoplasia.jpg ref_hypomineralization.jpg \
  ref_impacted_canine.jpg ref_impacted_second_molar.png ref_impacted_panorama.jpg ref_infant_teeth.jpg \
  ref_aphthous.jpg ref_leukoplakia.jpg ref_lichen_planus.jpg ref_herpes.jpg \
  ref_candidiasis.jpg ref_mucocele.jpg ref_ranula.jpg ref_geographic_tongue.jpg \
  ref_fissured_tongue.jpg ref_angular_cheilitis.jpg
  do
    cp "$SRC" "$OUT/$f"
  done
