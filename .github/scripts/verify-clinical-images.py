#!/usr/bin/env python3
"""Validate clinical assets prepared by fetch-clinical-images.sh."""
from __future__ import annotations
import pathlib, struct, sys

REQUIRED = [
    "clinician_doctor_photo.jpg", "clinician_doctora_photo.jpg",
    "clinical_varicella.jpg", "clinical_smallpox.jpg", "clinical_measles.jpg", "clinical_rubella.jpg",
    "clinical_eczema.jpg", "clinical_psoriasis.jpg",
    # Multisystem pathological-history resources. These are anatomy/education images,
    # not photographs from which a systemic diagnosis may be inferred.
    "pathological_digestive_niddk.jpg", "pathological_kidney_niddk.jpg", "pathological_endocrine_niddk.jpg",
    "hairline_reference.jpg", "head_circumference_reference.jpg",
    "ref_tmj_anatomy.png", "ref_tmj_mri.jpg", "ref_tmj_movements.jpg", "ref_tmj_panorama.jpg",
    "ref_deciduous_teeth.jpg", "ref_mixed_dentition.jpg", "ref_permanent_dentition.jpg",
    "ref_angle_i.jpg", "ref_angle_ii.jpg", "ref_angle_iii.jpg", "ref_overjet.jpg", "ref_open_bite.jpg",
    "ref_deep_bite.jpg", "ref_midline.jpg", "ref_crossbite_anterior.jpg", "ref_diastema.png", "ref_crowding.jpg",
    "ref_hypodontia.jpg", "ref_supernumerary.jpg", "ref_fusion.jpg", "ref_dens_invaginatus.png",
    "ref_taurodontism.jpg", "ref_enamel_hypoplasia.jpg", "ref_hypomineralization.jpg",
    "ref_impacted_canine.jpg", "ref_impacted_second_molar.png", "ref_impacted_panorama.jpg", "ref_infant_teeth.jpg",
    "ref_aphthous.jpg", "ref_leukoplakia.jpg", "ref_lichen_planus.jpg", "ref_herpes.jpg", "ref_candidiasis.jpg",
    "ref_mucocele.jpg", "ref_ranula.jpg", "ref_geographic_tongue.jpg", "ref_fissured_tongue.jpg", "ref_angular_cheilitis.jpg",
]
MIN_SHORT_SIDE = 300
MIN_LONG_SIDE = 400

def png_size(data: bytes):
    if len(data) < 24 or data[:8] != b"\x89PNG\r\n\x1a\n": return None
    return struct.unpack(">II", data[16:24])

def jpeg_size(data: bytes):
    if len(data) < 4 or data[:2] != b"\xff\xd8": return None
    i=2
    while i+9 < len(data):
        if data[i] != 0xFF: i+=1; continue
        while i < len(data) and data[i] == 0xFF: i+=1
        if i >= len(data): break
        marker=data[i]; i+=1
        if marker in (0xD8,0xD9): continue
        if i+2 > len(data): break
        n=int.from_bytes(data[i:i+2],"big")
        if n < 2 or i+n > len(data): break
        if marker in {0xC0,0xC1,0xC2,0xC3,0xC5,0xC6,0xC7,0xC9,0xCA,0xCB,0xCD,0xCE,0xCF}:
            if n < 7: return None
            return int.from_bytes(data[i+5:i+7],"big"), int.from_bytes(data[i+3:i+5],"big")
        i += n
    return None

def image_size(path: pathlib.Path):
    data=path.read_bytes(); lower=data[:256].lstrip().lower()
    if lower.startswith((b"<!doctype html",b"<html",b"<?xml")): return None
    return png_size(data) or jpeg_size(data)

def main():
    root=pathlib.Path(sys.argv[1] if len(sys.argv)>1 else "app/src/main/res/drawable-nodpi")
    failures=[]
    for name in REQUIRED:
        path=root/name
        if not path.is_file() or path.stat().st_size == 0: failures.append(f"{name}: missing"); continue
        size=image_size(path)
        if size is None: failures.append(f"{name}: invalid/unsupported image content"); continue
        w,h=size; short,long=sorted((w,h))
        if short < MIN_SHORT_SIDE or long < MIN_LONG_SIDE:
            failures.append(f"{name}: {w}x{h}px below minimum {MIN_LONG_SIDE}x{MIN_SHORT_SIDE}px educational threshold"); continue
        print(f"VERIFIED {name}: {w}x{h}px")
    if failures:
        print("\nClinical image validation failed:",file=sys.stderr)
        for f in failures: print(f" - {f}",file=sys.stderr)
        return 1
    print(f"Validated {len(REQUIRED)} required offline clinical/educational images.")
    return 0
if __name__ == "__main__": raise SystemExit(main())
