# Estado del proyecto · YSM Expediente

Última actualización: **v0.32 de revisión**.

## Propósito
Aplicación educativa offline para estudiantes de odontología. Enseña cómo llenar un expediente clínico físico y ofrece exámenes interactivos y herramientas de cálculo. No es un expediente electrónico de pacientes, no prescribe medicamentos y no sustituye supervisión docente ni diagnóstico clínico definitivo.

## Identidad e interfaz
- Nombre: **YSM Expediente · El expediente del dentista**.
- Logo/icono: ave académica + expediente dental + diente.
- Slogan: **“Deja volar tu imaginación y tus conocimientos renacerán”**.
- Paleta predeterminada: **Agaporni**.
- Paletas inspiradas en aves, idioma Español/English y tratamiento Doctor/Doctora.
- Configuración persistente de idioma, tratamiento, paleta, tipografía y tamaño de letra.
- Créditos y privacidad reconocen la asistencia de ChatGPT (OpenAI) durante desarrollo, bajo revisión y adaptación del autor, sin implicar respaldo de OpenAI.

## Estabilidad y distribución
- La carga inicial de preferencias está protegida con valores de respaldo para evitar fallos de arranque por configuración inválida.
- La sesión educativa se mantiene en memoria durante la ejecución y no se almacena como expediente clínico.
- La variante `preview` usa un `applicationId` separado para pruebas.
- GitHub Pages y el APK público se despliegan únicamente desde `main`.
- CI valida pruebas unitarias, Lint, ensamblado Preview y arranque real en emulador Android 10.
- v0.30 corrigió el cierre al iniciar causado por recursos JPG inválidos del onboarding.

## Exploración clínica
- Exploración general persistente.
- Cabeza y cuello: cráneo, cara, músculos de la expresión facial, músculos de la masticación, cuello y cadenas ganglionares.
- ATM: apertura/cierre, lateralidades, protrusión/retrusión, apertura máxima, DVO/DVR, dolor, chasquido, crepitación, bloqueo, trayectoria y palpación.
- Examen de mucosas persistente por región y morfología.
- Signos y síntomas con acceso rápido desde el folder.

## Calculadoras clínicas · v0.32

### Medicamentos pediátricos
- El peso se introduce una sola vez.
- Se pueden seleccionar varios medicamentos a la vez.
- La frecuencia se presenta como **cada cuántas horas**, no como “número de tomas al día”.
- Categorías: analgésicos, AINE, antibióticos, nitroimidazoles y antivirales.
- Catálogo actual: paracetamol, ibuprofeno, naproxeno, amoxicilina, amoxicilina/ácido clavulánico, azitromicina, cefalexina, claritromicina, clindamicina, metronidazol, aciclovir y valaciclovir.
- Las dosis se basan prioritariamente en **AAPD Useful Medications for Oral Conditions, revisión 2025**.
- Las presentaciones se contrastaron con fuentes mexicanas, incluyendo información para prescribir y registros/productos comercializados en México.
- Líquidos: cálculo automático de mg por toma y mL por toma.
- Tabletas/cápsulas: muestra equivalencia matemática y advierte que no implica que una forma farmacéutica pueda fraccionarse.
- Formas tópicas: no se fuerzan a un cálculo mg/kg.
- Se muestra máximo por toma, máximo diario y/o máximo por kg sólo cuando la referencia lo especifica.
- Corrección v0.32: amoxicilina/clavulanato queda separada por relación. 4:1 (125/31.25 y 250/62.5 mg/5 mL) usa esquema cada 8 h; 7:1 (200/28.5 y 400/57 mg/5 mL, además de 875/125 mg) usa esquema cada 12 h. La presentación 600/42.9 mg/5 mL se excluye de la calculadora general por tener una relación/esquema especial y no ser intercambiable volumen por volumen.
- La app conserva advertencias de alergias, función renal/hepática, interacciones, indicación correcta y uso prudente de antimicrobianos.

### Anestésicos locales
- Lidocaína 2% + epinefrina: máximo dental pediátrico conservador 4.4 mg/kg.
- Lidocaína 2% sin vasoconstrictor: 4.4 mg/kg como referencia conservadora.
- Mepivacaína 3% sin vasoconstrictor: 4.4 mg/kg.
- Articaína 4% + epinefrina: 7 mg/kg; alerta en menores de 4 años.
- Bupivacaína 0.5% + epinefrina: 1.3 mg/kg; alerta en menores de 12 años.
- La tabla AAPD actual no lista mepivacaína 2% + epinefrina; lista mepivacaína 2% + levonordefrina 1:20,000, por lo que no se presenta como “mepivacaína con epinefrina”.
- Se calcula concentración, mg/cartucho, máximo por peso, máximo teórico de cartuchos y epinefrina por cartucho cuando corresponde. El resultado no indica cuántos cartuchos se deben aplicar.

## Índices y exámenes
- IPC persistente por sitios.
- IHOS con sustituciones/exclusiones y dientes faltantes.
- O’Leary con conjunto de dientes evaluables propio y manejo de todos excluidos.
- CPOD/ceod, ICDAS, odontograma, periodontograma y diagnóstico pulpar/periapical.

## Web
- La web replica el acceso a Signos/síntomas y Calculadoras.
- La calculadora pediátrica web usa el mismo catálogo verificado v0.32, selección múltiple, presentaciones y frecuencia por horas.
- Los cálculos se realizan localmente en el navegador y no se envían a un servidor del proyecto.

## Validación v0.32
Workflow Android build **#187** sobre `b2db8b815d16e1380e8cae165ca7f6d5118de906`:
- pruebas unitarias ✅
- Android Lint ✅
- `assemblePreview` ✅
- APK Preview ✅
- arranque real en Android 10 ✅

`main` permanece sin modificar hasta revisión/merge.
