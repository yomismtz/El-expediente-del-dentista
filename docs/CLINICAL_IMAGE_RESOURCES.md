# Clinical image resources

Clinical images live primarily in `app/src/main/res/drawable-nodpi/`. Keeping photographic and educational raster assets in `drawable-nodpi` prevents Android density scaling from changing their intrinsic pixel dimensions.

## Resource families

| Prefix | Purpose |
| --- | --- |
| `allimg_` | General clinical and educational reference library |
| `edu_` | Purpose-built educational illustrations and visual guides |
| `clin_` | Clinical photography/framing examples |
| `atm_` | TMJ-specific references and animation |
| `face13_` | Facial morphology and symmetry reference set |
| `skin20_` | Skin/extraoral reference set |
| `anomaly49_` | Dental/developmental anomaly reference set |
| `icdas_uploaded_` | ICDAS visual reference set |
| `new17_` | Dentition/arch/midline expanded references |
| `new77_` | Occlusion, facial examination and appliance expanded references |
| `uploaded80_` | Curated references used by CPOD, O'Leary, IPC, IHOS, pulpal, periodontal, caries and orthodontic modules |

## Integration points

- `LocalClinicalImageGalleryV45.kt` is a central registry for the broad local clinical gallery.
- `UploadedClinicalReferencesV51.kt` maps curated uploaded resources to specific clinical modules.
- `clinical_drawable_aliases.xml` provides stable semantic drawable names for screens that should not depend directly on source filenames.
- Launcher graphics remain in `drawable/` and `mipmap-*/`.

## Maintenance rules

1. Do not delete an image solely because its filename is old or numbered.
2. Before deletion, check Kotlin `R.drawable.*` references and XML `@drawable/*` references, including aliases.
3. Add new clinical raster images to `drawable-nodpi` with lowercase Android-safe names.
4. Prefer semantic aliases when a source filename is temporary or upload-derived.
5. Keep GIF resources in `drawable-nodpi` when they are intentionally rendered by the local animated-image component.
6. Build Android after any resource rename, deletion or alias change.

## Known placeholders

Several semantic entries in `clinical_drawable_aliases.xml` currently resolve to `ic_app_logo`. These are intentional placeholders until a verified clinical reference is assigned. They must not be interpreted as completed clinical imagery.
