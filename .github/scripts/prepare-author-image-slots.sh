#!/usr/bin/env bash
set -euo pipefail

OUT="app/src/main/res/drawable-nodpi"
SRC="$OUT/folder_cover_ysm.webp"
mkdir -p "$OUT"

# Recursos temporales sólo para que aapt2 reserve los identificadores.
# Tras compilar, estas cuatro entradas se sustituyen por las imágenes
# proporcionadas por la autora antes de volver a firmar el APK.
cp "$SRC" "$OUT/kennedy_reference.webp"
cp "$SRC" "$OUT/contraceptives_reference.webp"
cp "$SRC" "$OUT/general_inspection_reference.webp"
cp "$SRC" "$OUT/cranial_shape_reference.webp"
