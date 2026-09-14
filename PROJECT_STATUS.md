# Estado del proyecto · YSM Expediente

Última actualización: **v0.26 de revisión**.

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
- CI valida pruebas unitarias, Lint, ensamblado Preview, prueba instrumental y arranque en emulador Android 10.

## Exploración clínica incorporada
- Exploración general.
- Cabeza y cuello: cráneo, cara, músculos de la expresión facial, músculos de la masticación, cuello y cadenas ganglionares.
- ATM con apertura/cierre, lateralidades, protrusión/retrusión, apertura máxima, DVO/DVR, dolor, chasquido, crepitación, bloqueo y trayectoria.
- Examen de mucosas por región y morfología.
- Signos vitales, oclusión, postura y auxiliares.

## Registros e índices
- Odontograma por cuadrantes.
- ICDAS, CPOD/ceod, O'Leary, IPC e IHOS.
- Periodontograma por diente con seis sitios.
- Diagnóstico pulpar y periapical educativos.
- Endodoncia, prótesis, cirugía, consentimiento y notas de evolución.

## Nuevo en v0.26
### Signos y síntomas
Acceso rápido persistente durante la sesión para registrar:
- dolor, localización, inicio y evolución;
- intensidad y carácter;
- desencadenantes y factores de alivio;
- aumento de volumen, sangrado, fiebre;
- limitación de apertura, alteración sensitiva y disfagia;
- notas y redacción rápida sugerida.

### Calculadoras clínicas
- **Dosis pediátrica:** calcula mg por dosis, mg/día y mL por dosis a partir de peso, dosis indicada, frecuencia y concentración. La app no selecciona el medicamento ni la dosis.
- **Anestésicos locales:** lidocaína con epinefrina, lidocaína sola, mepivacaína, mepivacaína con epinefrina y articaína.
- Calcula concentración en mg/mL, mg por cartucho, máximo conservador por peso, máximo teórico de cartuchos y epinefrina por cartucho cuando aplica.
- El volumen del cartucho se introduce de forma explícita porque varía por presentación/mercado.
- Se advierte que la ficha técnica, vasoconstrictor, edad, enfermedades, embarazo, medicamentos concomitantes y otras condiciones pueden imponer un límite menor.

## Web
- Nuevo acceso a **Signos y síntomas**.
- Nueva sección **Calculadoras clínicas** con cálculo local en JavaScript.
- Los valores introducidos en estos formularios no se envían a un servidor del proyecto ni se guardan en una base de datos.
- Política de privacidad actualizada para reflejar estas funciones.

## Correcciones previas importantes
- IPC conserva códigos detallados durante la sesión.
- IHOS respeta sustituciones, exclusiones y dientes ausentes.
- O’Leary mantiene sus propios dientes evaluables y excluye la cara oclusal del denominador.
- Se agregaron pruebas de regresión para CPOD, IPC, IHOS y O’Leary.

## Próximos puntos
1. Continuar reorganizando el folder principal conforme a la estructura completa de Historia clínica definida para el proyecto.
2. Ampliar Historia clínica con antecedentes y exploración física por subviñetas.
3. Añadir CAMBRA al flujo principal.
4. Seguir ampliando pruebas automáticas y validación en teléfonos reales.

## Repositorio
https://github.com/yomismtz/El-expediente-del-dentista
