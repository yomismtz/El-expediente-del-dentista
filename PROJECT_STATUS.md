# Estado del proyecto · YSM Expediente

Última actualización: **v0.33 de revisión**.

## Propósito
Aplicación educativa offline para estudiantes de odontología. Enseña cómo llenar un expediente clínico físico y ofrece exámenes interactivos y herramientas de cálculo. No es un expediente electrónico de pacientes, no prescribe medicamentos y no sustituye supervisión docente ni diagnóstico clínico definitivo.

## Identidad e interfaz
- Nombre: **YSM Expediente · El expediente del dentista**.
- Logo/icono: ave académica + expediente dental + diente.
- Slogan: **“Deja volar tu imaginación y tus conocimientos renacerán”**.
- Paleta predeterminada: **Agaporni**.
- Paletas inspiradas en aves, idioma Español/English y tratamiento Doctor/Doctora.
- Configuración persistente de idioma, tratamiento, paleta, tipografía y tamaño de letra.

## Jerarquía del expediente · v0.33
- Al abrir el expediente se muestran dos hojas de navegación, siguiendo el esquema docente entregado.
- **Izquierda, verde limón:** autorización de actividades, diagnóstico/tratamiento, sesiones, endodoncia, prótesis, periodontograma, cirugía, trastornos temporomandibulares, signos/síntomas, calculadora, O’Leary y CAMBRA.
- **Derecha, azul aqua:** identificación, historia clínica, mucosas, auxiliares, odontograma/exámenes diagnósticos, presupuesto, consentimiento, solicitud de tratamiento, evolución, cabeza/cuello, ATM, oclusión, anomalías dentales, anomalías de erupción y hábitos/parafunciones.
- Historia clínica abre un segundo nivel con identificación, motivo/padecimiento actual, antecedentes heredo-familiares, personales no patológicos, personales patológicos, quirúrgicos/traumáticos, exploración física y antecedentes ortodónticos.
- Exploración física abre signos vitales, signos/síntomas, exploración general y cabeza/cuello.
- Odontograma abre un hub con odontograma clínico, CPOD, ceod, ICDAS, IHOS e IPC.
- CPOD y ceod comparten el módulo existente y se diferencian mediante el selector de dentición permanente/temporal.
- CAMBRA queda como módulo educativo de riesgo de caries con indicadores de enfermedad, factores de riesgo y factores protectores.

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

## Calculadoras clínicas · v0.32+
- El peso se introduce una sola vez y se pueden seleccionar varios medicamentos.
- La frecuencia se expresa como **cada cuántas horas**.
- Catálogo actual: paracetamol, ibuprofeno, naproxeno, amoxicilina, amoxicilina/ácido clavulánico, azitromicina, cefalexina, claritromicina, clindamicina, metronidazol, aciclovir y valaciclovir.
- Las dosis se basan prioritariamente en AAPD Useful Medications for Oral Conditions y las presentaciones se contrastaron con fuentes mexicanas.
- Amoxicilina/clavulanato está separada por relación 4:1 y 7:1 para evitar intercambios incorrectos de presentación/esquema.
- Los anestésicos locales conservan límites pediátricos y alertas de edad; el resultado es educativo y no prescribe cuántos cartuchos aplicar.

## Índices y exámenes
- IPC persistente por sitios.
- IHOS con sustituciones/exclusiones y dientes faltantes.
- O’Leary con conjunto de dientes evaluables propio.
- CPOD/ceod, ICDAS, odontograma, periodontograma y diagnóstico pulpar/periapical.

## Web
- La web replica el folder verde limón / azul aqua y los submenús Historia clínica y Odontograma.
- Conserva Signos/síntomas y las calculadoras verificadas v0.32.
- Los cálculos se realizan localmente en el navegador y no se envían a un servidor del proyecto.

## Validación
La v0.33 está en proceso de validación automática. No debe marcarse como estable hasta completar pruebas unitarias, Lint, `assemblePreview` y prueba de arranque real en Android 10.

`main` permanece sin modificar hasta revisión/merge.