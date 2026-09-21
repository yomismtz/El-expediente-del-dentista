# Identidad visual de El expediente del dentista

## Dirección visual

La aplicación usa una línea **semirrealista educativa**: profesional, clara, amable y compatible con material clínico real. La interfaz no debe alternar entre caricaturas infantiles, emoji decorativos y fotografías clínicas como si pertenecieran al mismo nivel visual.

### Jerarquía de imágenes

1. **Reconocimiento clínico**: usar fotografía clínica, radiografía o imagen diagnóstica real cuando el objetivo sea identificar una lesión, anomalía, relación o hallazgo.
2. **Anatomía, técnica o proceso**: usar ilustración semirrealista educativa con anatomía reconocible, proporciones coherentes, sombreado moderado y fondo limpio.
3. **Diagramas funcionales**: se permiten esquemas cuando la precisión pedagógica sea superior a una fotografía, pero deben conservar apariencia clínica y no caricaturesca.
4. **Emoji**: no usar como ilustración principal, marcador clínico o sustituto de anatomía. Pueden aparecer únicamente como texto accesorio cuando no compitan con el contenido clínico.
5. **Imágenes del autor/usuario**: conservar completas, sin recorte ni rediseño, usando ContentScale.Fit.

## Material 3

La app mantiene Material 3 como base. El tema define:
- esquinas extra small 8 dp;
- small 12 dp;
- medium 16 dp;
- large 20 dp;
- extra large 28 dp;
- elevación habitual de tarjetas: 1 dp;
- elevación de superficies focales: 2 dp;
- borde estándar: 1 dp con outlineVariant.

Los componentes heredados deben reutilizar MaterialTheme.shapes y VisualSpacingV49 en lugar de radios/espaciados aislados.

## Espaciado

VisualSpacingV49 es la escala de referencia:
- xxs 4 dp
- xs 6 dp
- sm 8 dp
- md 12 dp
- lg 16 dp
- xl 20 dp
- xxl 24 dp

## Fotografías e ilustraciones

Usar EducationalVisualFrameV49 para fotografías, radiografías e ilustraciones educativas:
- fondo neutro surfaceVariant;
- ContentScale.Fit;
- borde fino;
- radio Material 3;
- título y pie separados de la imagen;
- crédito/licencia visible cuando la imagen sea externa.

No incrustar créditos encima de la imagen si pueden mostrarse en el pie.

## Colores clínicos

No codificar colores RGB aislados dentro de pantallas. Usar:
- clinicalCariesColorV49()
- clinicalRestorationColorV49()
- clinicalSealantColorV49()
- clinicalHealthySurfaceV49()

Los colores se adaptan a la paleta seleccionada y al modo claro/oscuro.

## Paletas de aves

La paleta de ave aporta identidad cromática, pero los contenedores se suavizan mediante el esquema Material 3 para mantener legibilidad clínica. El color de la paleta no debe teñir fotografías clínicas ni alterar su lectura.

Mientras no exista una ilustración semirrealista específica y consistente para todas las aves, evitar mostrar emoji de ave junto a retratos fotográficos. Mostrar el nombre de la paleta y sus muestras de color.

## Modo oscuro

La aplicación sigue el modo del sistema. Fotografías e ilustraciones mantienen fondo neutro; texto, bordes, superficies y barras del sistema usan el esquema oscuro derivado de la paleta seleccionada.

No usar colores claros/negros fijos para texto o fondos salvo recursos clínicos que requieran una convención explícita.

## Revisión de una pantalla antigua

Antes de considerar una pantalla visualmente actualizada:
1. reemplazar Card con forma/borde/elevación compartidos;
2. usar ResponsiveScreenV17/ResponsiveSectionV17 o SectionCard;
3. eliminar emoji decorativos que funcionen como iconografía principal;
4. sustituir colores directos por MaterialTheme.colorScheme o tokens clínicos;
5. encuadrar imágenes con EducationalVisualFrameV49;
6. revisar contraste en claro y oscuro;
7. verificar teléfono, tablet y fuente grande.
