# Pathological personal history — visual atlas coverage

This file tracks only clinically justified visual resources. Visible findings may use verified clinical photographs; internal diseases without a diagnostic external appearance use educational anatomy/schematic resources.

## Already integrated
- Varicella — CDC PHIL public-domain clinical photograph.
- Smallpox — CDC PHIL public-domain historical clinical photograph.
- Measles — CDC PHIL public-domain clinical photograph.
- Rubella — CDC PHIL public-domain clinical photograph.
- Atopic dermatitis — Wikimedia Commons, CC BY-SA 3.0.
- Psoriasis — Wikimedia Commons, CC BY 4.0.

## Verified authoritative resources queued for offline packaging
- Gastrointestinal/hepatic — NIDDK/NIH Digestive System, media asset 17489: https://www.niddk.nih.gov/news/media-library/17489. NIDDK states its Media Library images are copyright-free to the public at no cost; required credit: National Institute of Diabetes and Digestive and Kidney Diseases, National Institutes of Health.
- Renal/urinary — NIDDK/NIH Spanish kidney illustration, media asset 17940: https://www.niddk.nih.gov/news/media-library/17940. Same NIDDK Media Library reuse terms and credit requirement.
- Cardiovascular — NIAID/NIH BioArt Human Heart, BIOART-000228: https://bioart.niaid.nih.gov/bioart/228. Licensing: Public Domain; creator Ryan Kissinger; credit Courtesy of NIAID.
- Respiratory — NIAID/NIH BioArt Human Lungs, BIOART-000231: https://bioart.niaid.nih.gov/bioart/231. Licensing: Public Domain; creator Ryan Kissinger; credit Courtesy of NIAID. For tuberculosis teaching, BIOART-000527 provides an infected-lung schematic and is also Public Domain.
- Neurologic — NIAID/NIH BioArt Human Anatomy, BIOART-000519: https://bioart.niaid.nih.gov/bioart/519. Licensing: Public Domain; includes brain and nervous-system context. Use only as anatomy, never to imply epilepsy, Parkinson disease, stroke or MS from appearance. BIOART-000424 (Pyramidal Neuron) is an optional Public Domain cellular teaching visual.
- Rheumatologic / musculoskeletal — NIAID/NIH BioArt Human Arm Bones, BIOART-000208: https://bioart.niaid.nih.gov/bioart/208. Licensing: Public Domain; creator Ryan Kissinger; credit Courtesy of NIAID. This is an anatomy/context visual only; it must not be labeled as a photograph of rheumatoid arthritis, lupus, Sjögren disease or scleroderma.
- Endocrine/metabolic — NIDDK/NIH media asset 17934: https://www.niddk.nih.gov/news/media-library/17934. It depicts pituitary, thyroid/parathyroids and pancreas/islets and is a substantially better endocrine-system teaching resource than a generic body image. NIDDK Media Library reuse terms: copyright-free to the public at no cost; required credit: National Institute of Diabetes and Digestive and Kidney Diseases, National Institutes of Health. For focused thyroid teaching, NIDDK asset 17772 depicts the pituitary–TSH–thyroid–T3/T4 relationship; for adrenal teaching, asset 18226 depicts hypothalamic/adrenal cortisol regulation. These are anatomy/physiology resources, not diagnostic appearances.
- Hematologic — NIAID/NIH BioArt Venule Cross Section, BIOART-000539: https://bioart.niaid.nih.gov/bioart/539. Licensing: Public Domain; creator Ryan Kissinger; credit Courtesy of NIAID. Use as blood-vessel/anatomy context only. Clinical photographs, when added, must depict the actual observable sign (for example petechiae/ecchymosis) and not claim a systemic diagnosis from appearance.
- Multisystem anatomy fallback — NIAID/NIH BioArt Human Anatomy, BIOART-000519: https://bioart.niaid.nih.gov/bioart/519. Licensing: Public Domain; includes stomach, liver, colon, trachea, lungs, brain and circulatory system. Use only as anatomy, never as a diagnostic appearance.
- Oral candidiasis (relevant to immunodeficiency, inhaled corticosteroids and other risk contexts) — CDC PHIL ID 6067: https://wwwn.cdc.gov/phil/Details.aspx?pid=6067. Copyright restrictions: none; public domain. The photograph is a manifestation example, not proof of a systemic diagnosis.

## Resource decisions still requiring packaging/integration
All multisystem categories now have a clinically safe authoritative resource decision. Remaining work is implementation: obtain the permitted representation, package it under Android resources for offline use, add compact source/rights labels in the card UI, and verify rendering in portrait/landscape. Endocrine/metabolic now has a specific NIDDK endocrine illustration (asset 17934), replacing the prior generic-anatomy fallback as the preferred resource.

## Integration rules
1. Package the selected resource locally so the teaching atlas works without a network connection when the source terms permit redistribution.
2. Display source and rights/credit next to every visual.
3. Use clinical photographs only for genuinely observable manifestations. Internal diseases use anatomy, imaging or educational schematics.
4. Do not infer or teach a systemic diagnosis from facial/body appearance alone.
5. Keep dental relevance explicit: medication effects, bleeding risk, xerostomia, candidiasis, periodontal/healing implications, positioning/tolerance, mucosal findings and required medical coordination as appropriate.
6. When a source offers several representations, retain the exact asset ID and credit in the app so future replacement/upscaling does not lose provenance.

## Acceptance criteria
Every visual must have a source and rights statement, be packaged for offline use when licensing permits, include dental relevance, and avoid implying that a photograph alone establishes a systemic diagnosis. Final completion requires all categories above to have a definitive resource decision, local offline packaging, UI integration, and a successful Android workflow with APK Preview artifact.
