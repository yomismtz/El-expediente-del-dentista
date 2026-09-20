# Auditoría de imágenes clínicas y educativas

Última revisión: 2026-09-19

## Criterios

Una imagen se conserva sólo cuando:
1. corresponde al hallazgo/concepto mostrado;
2. la fuente permite identificar razonablemente qué representa;
3. la licencia o condición de dominio público es compatible con su uso;
4. el archivo que usa la app se empaqueta localmente;
5. el recurso supera la validación automática de formato y tamaño mínimo;
6. la interfaz usa `ContentScale.Fit` y no amplía el raster por encima de su ancho intrínseco.

La aplicación instalada **no descarga imágenes clínicas durante el uso normal**. Las referencias externas se obtienen durante la compilación y se empaquetan como recursos Android. Las pantallas clínicas activas usan `OfflineClinicalImageV50`, que mantiene `ContentScale.Fit`, centra el recurso y evita ampliar un raster más allá de su ancho intrínseco.

## Auditoría vigente

| Recurso local | Concepto | Fuente / licencia | Decisión |
|---|---|---|---|
| clinician_doctor_photo.jpg | Retrato Doctor | Wikimedia/DVIDS, dominio público | CONSERVAR |
| clinician_doctora_photo.jpg | Retrato Doctora | Wikimedia, Erik Christensen, CC BY-SA 3.0 | CONSERVAR |
| icdas_codes_photo.jpg | ICDAS 0–6 | Gugnani et al., Fig. 1A–G, CC BY 3.0 | CONSERVAR. Pie bibliográfico visible. |
| clinical_varicella.jpg | Varicela | CDC PHIL 6121, dominio público | CONSERVAR |
| clinical_smallpox.jpg | Viruela | CDC PHIL 10491, dominio público | CONSERVAR con identificación corregida: muestra lesiones tempranas en lengua, no distribución cutánea general. |
| clinical_measles.jpg | Sarampión | CDC PHIL 4497, dominio público | CONSERVAR |
| clinical_rubella.jpg | Rubéola | CDC PHIL 712, dominio público | CONSERVAR |
| clinical_eczema.jpg | Dermatitis atópica | Wikimedia, Assianir, CC BY-SA 3.0 | SUSTITUYE a Atopic_dermatitis.png. Nueva referencia 2448×3264 px. |
| clinical_psoriasis.jpg | Psoriasis | Wikimedia, Dr. Gandikota Raghurama Rao, CC BY 4.0 | SUSTITUYE a Psoriasis.jpg. Nueva referencia 1139×749 px. |
| hairline_reference.jpg | Línea de implantación del cabello | Wikimedia, Acr319, dominio público | CONSERVAR como referencia general. La app aclara que no representa por sí sola implantación alta/baja ni recesión. |
| head_circumference_reference.jpg | Técnica de medición cefálica | U.S. Air Force / A1C Anania Tekurio vía Wikimedia, dominio público | CONSERVAR. Se presenta como demostración de medición, no como ejemplo de microcefalia o macrocefalia. |
| ref_tmj_anatomy.png | Anatomía normal de ATM | Frank Gaillard/Radiopaedia vía Wikimedia, CC BY-SA 3.0/GFDL | CONSERVAR. Es ilustración médica, no fotografía. |
| ref_tmj_mri.jpg | RM de ATM | ARTICULATIONMAN/Wikimedia, CC BY-SA 4.0 | CONSERVAR |
| ref_tmj_movements.jpg | Sobre de movimientos mandibulares | Rjmedink/Wikimedia, CC BY-SA 4.0 | CONSERVAR. Identificada explícitamente como diagrama educativo. |
| ref_tmj_panorama.jpg | Cóndilo y fosa articular | ANUG/Wikimedia, CC BY-SA 4.0 | CONSERVAR como referencia anatómica radiográfica; no se presenta como diagnóstico de TTM. |
| ref_deciduous_teeth.jpg | Dentición temporal | David Shankbone/Wikimedia, CC BY-SA 3.0/GFDL | CONSERVAR como referencia anatómica. Se retiró la insinuación de que la foto demuestra un plano terminal específico. |
| ref_angle_i.jpg | Angle Clase I | Challiyan/Wikimedia, CC BY-SA 4.0 | CONSERVAR; el pie aclara que Clase I no equivale a oclusión globalmente normal. |
| ref_angle_ii.jpg | Angle Clase II/1 | Georg Risse/Wikimedia, CC BY-SA/GFDL | CONSERVAR |
| ref_angle_iii.jpg | Angle Clase III | Georg Risse/Wikimedia, CC BY-SA/GFDL | CONSERVAR |
| ref_overjet.jpg | Overjet | Rama2k1/Wikimedia, CC BY-SA 4.0 | CONSERVAR |
| ref_open_bite.jpg | Mordida abierta anterior | Challiyan/Wikimedia, CC BY-SA 4.0 | CONSERVAR |
| ref_deep_bite.jpg | Mordida profunda | Challiyan/Wikimedia, CC BY-SA 4.0 | CONSERVAR |
| ref_midline.jpg | Desviación de línea media dental | DRosenbach/Wikimedia, dominio público · File:Deviated midline 2.JPG | **SUSTITUIDA**. Se retiró Canted occlusal plane.jpg porque no era una referencia específica de línea media. La nueva foto documenta discrepancia de línea media y además muestra desgaste por bruxismo, dato que se declara en el pie. |
| ref_crossbite_anterior.jpg | Mordida cruzada anterior | Challiyan/Wikimedia, CC BY-SA 4.0 | CONSERVAR |
| ref_diastema.png | Diastema maxilar medio | Ian Furst/Wikimedia, CC BY-SA 4.0 | CONSERVAR |
| ref_crowding.jpg | Apiñamiento severo | Challiyan/Wikimedia, CC BY-SA 4.0 | CONSERVAR |
| ref_hypodontia.jpg | Hipodoncia | Ramirotomasi/Wikimedia, CC BY-SA 4.0 | CONSERVAR |
| ref_supernumerary.jpg | Supernumerarios/mesiodens | Albert/Wikimedia, dominio público | CONSERVAR |
| ref_fusion.jpg | Posible fusión dental | Roquex/Wikimedia, CC0 | CONSERVAR sólo con advertencia de que el caso fuente no tiene confirmación radiográfica. |
| ref_dens_invaginatus.png | Dens invaginatus, clasificación Oehlers | Wikimedia, CC BY 3.0 | CONSERVAR como esquema didáctico identificado; no fotografía clínica. |
| ref_taurodontism.jpg | Taurodontismo | Challiyan/Wikimedia, CC BY-SA 4.0 | CONSERVAR |
| ref_enamel_hypoplasia.jpg | Hipoplasia del esmalte | Otis Historical Archives/NMHM vía Wikimedia, CC BY 2.0 | CONSERVAR |
| ref_hypomineralization.jpg | Hipomineralización de incisivo | Federico Morales Corona/Wikimedia, CC BY-SA 4.0 | CONSERVAR |
| ref_impacted_canine.jpg | Canino impactado | DRosenbach/Wikimedia, CC BY-SA 3.0 | CONSERVAR, sin ampliar por encima del tamaño nativo. |
| ref_impacted_second_molar.png | Segundo molar impactado | Coronation Dental Specialty Group/Wikimedia, CC BY-SA 3.0 | CONSERVAR, sin ampliar por encima del tamaño nativo. |
| ref_impacted_panorama.jpg | Panorámica con dientes impactados / desarrollo y posición | Coronation Dental Specialty Group/Wikimedia, CC BY 3.0 | CONSERVAR. Se retiró su uso como ejemplo específico de “retraso/asimetría eruptiva”; ahora sólo enseña lectura de desarrollo, posición y retención. |
| ref_infant_teeth.jpg | Dientes temporales en lactante | Chrisbwah/Wikimedia, CC BY-SA 3.0 | CONSERVAR; el pie aclara que no es un ejemplo de diente natal/neonatal. |
| ref_aphthous.jpg | Úlcera aftosa | Genppy/Wikimedia, licencia libre indicada en ficha | CONSERVAR |
| ref_leukoplakia.jpg | Leucoplasia oral | Klaus D. Peter/Wikimedia, CC BY 3.0 DE | CONSERVAR; pie aclara contexto histopatológico del caso fuente. |
| ref_lichen_planus.jpg | Liquen plano oral reticular | Ian Furst/Wikimedia, CC BY-SA 4.0 | CONSERVAR |
| ref_herpes.jpg | Herpes labial | Metju12/Wikimedia, dominio público | CONSERVAR |
| ref_candidiasis.jpg | Candidiasis oral | James Heilman, MD/Wikimedia, CC BY-SA 3.0/GFDL | CONSERVAR |
| ref_mucocele.jpg | Mucocele de labio inferior | Ed Uthman/Wikimedia, licencia Creative Commons | CONSERVAR |
| ref_ranula.jpg | Ránula | Ph0t0happy/Wikimedia, CC BY-SA 3.0/GFDL | CONSERVAR |
| ref_geographic_tongue.jpg | Lengua geográfica | Martanopue/Wikimedia, CC BY-SA 3.0 | CONSERVAR |
| ref_fissured_tongue.jpg | Lengua fisurada/geográfica | Kozlovsk/Wikimedia, licencia indicada en ficha | CONSERVAR |
| ref_angular_cheilitis.jpg | Queilitis angular | James Heilman, MD/Wikimedia, CC BY-SA 3.0 | CONSERVAR |
| general_inspection_reference | Tabla de inspección general | Imagen proporcionada por la autora | CONSERVAR exacta, completa y sin recorte/rediseño |

## Imágenes retiradas en esta auditoría

| Recurso retirado | Motivo |
|---|---|
| ref_crossbite_posterior.jpg / Crossbite.jpg | El diagnóstico era correcto (mordida cruzada posterior unilateral), pero el original es 422×250 px. Se retira del atlas para evitar una referencia clínica borrosa ampliada. |
| ref_microdontia.jpg / Axenfeld syndrome.jpg | El montaje sí contiene microdoncia/hipodoncia, pero mezcla hallazgos oculares de síndrome de Axenfeld-Rieger. Es ambiguo como referencia dental aislada. |
| ref_macrodontia.jpg / Kbg.jpg | Sí muestra macrodoncia de incisivos centrales en KBG, pero es una imagen sindrómica de sólo 285×381 px. |
| ref_dens_evaginatus.jpg / Dens evaginatus.jpg | El concepto corresponde, pero el original es sólo 226×324 px y no alcanza el estándar visual del atlas. |
| Atopic_dermatitis.png | Correcta, pero sustituida por una fotografía de dermatitis atópica de mayor resolución. |
| Psoriasis.jpg | Correcta, pero sólo 146×299 px; sustituida por una referencia CC BY 4.0 de mayor resolución. |

Los conceptos retirados del **atlas fotográfico** siguen pudiendo enseñarse en texto/razonamiento. No se reintroducirá una imagen hasta contar con una fuente directa, licencia compatible y resolución suficiente.

## Dependencia de red en tiempo de ejecución

- Las rutas clínicas activas usan recursos empaquetados en el APK.
- El antiguo `OcclusionPhotoAtlasV42Screen`, que todavía contenía `AsyncImage` y URLs de Wikimedia, fue convertido en un wrapper de compatibilidad hacia el atlas offline V46.
- Las URLs visibles en pies/créditos son texto de atribución y no se usan para cargar la imagen durante el uso normal.
- CI ejecuta `.github/scripts/verify-clinical-image-ui.py` y falla si las pantallas auditadas reintroducen `AsyncImage`, `ContentScale.Crop` o `ContentScale.FillBounds`.

## Control automático del empaquetado

`.github/scripts/verify-clinical-images.py` comprueba durante CI:
- que cada recurso requerido exista localmente;
- que no sea HTML/error descargado con extensión de imagen;
- que sea JPEG o PNG válido;
- que supere un umbral mínimo de 400 px en el lado largo y 300 px en el corto.

Si una imagen necesaria no cumple, la compilación falla antes de generar el APK. Esto evita depender de una URL durante el uso normal y evita publicar una compilación con recursos clínicos ausentes o corruptos.
