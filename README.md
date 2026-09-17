# El expediente del dentista

Aplicación educativa Android, bilingüe y completamente offline para enseñar cómo se llena un expediente clínico odontológico mediante formularios, índices y razonamiento clínico guiado.

> **Uso educativo.** No está diseñada para almacenar, administrar ni sustituir expedientes clínicos reales, emitir diagnósticos clínicos autónomos ni prescribir medicamentos.

## Qué incluye

- Configuración Español / English y Doctor / Doctora.
- Personalización dentro de la app con 13 paletas inspiradas en aves y cuatro estilos tipográficos.
- Ficha de identificación y anamnesis guiada por grupos de enfermedades y clasificación ASA.
- Acceso rápido de **Signos y síntomas** para documentar dolor, localización, inicio/evolución, intensidad y signos asociados sin recorrer toda la historia clínica.
- Exploración general, cabeza y cuello, músculos faciales/masticatorios, cuello, cadenas ganglionares y ATM.
- Examen de mucosas por región y morfología.
- Nota de ingreso generada con la información capturada durante la sesión.
- Odontograma interactivo, ICDAS, CPOD/ceod, O'Leary, IPC e IHOS.
- Periodontograma por diente con sondaje de seis sitios, sangrado, placa, supuración, recesión, movilidad y furcación.
- Orientación diagnóstica pulpar y periapical a partir de signos, síntomas, pruebas clínicas y hallazgos radiográficos seleccionados.
- Diagnóstico y tratamiento por diente con opciones educativas y retroalimentación.
- Endodoncia y prótesis con módulos interactivos de práctica.
- Auxiliares diagnósticos y ortodóncicos educativos.
- Generador de ejemplos de notas de evolución según los tratamientos seleccionados.
- **Calculadoras clínicas educativas**:
  - dosis pediátrica a partir de peso, dosis indicada en mg/kg, frecuencia y concentración;
  - anestésicos locales para lidocaína con/sin epinefrina, mepivacaína con/sin epinefrina y articaína;
  - cálculo de mg por cartucho, máximo conservador por peso y cantidad de epinefrina cuando corresponda.
- Navegación atrás con historial y gesto desde el borde izquierdo una vez completado el onboarding.

## Seguridad de las calculadoras

Las calculadoras hacen aritmética; **no seleccionan un medicamento ni recomiendan una dosis**. Los valores de anestésicos locales están configurados como referencias educativas pediátricas conservadoras y deben cotejarse con la ficha técnica del producto realmente disponible, el protocolo institucional y las condiciones clínicas de la persona. El volumen del cartucho se introduce explícitamente porque puede variar entre presentaciones y mercados.

## Privacidad y funcionamiento

La app no solicita permiso de Internet. Los datos clínicos de práctica viven únicamente en la sesión activa y no se guardan como expediente clínico. Las preferencias de idioma, tratamiento Doctor/Doctora, paleta y tipografía sí se guardan localmente en el dispositivo.

La web es estática. Los campos de Signos y síntomas y las calculadoras funcionan localmente mediante JavaScript y no incluyen una función para enviar esos valores a un servidor del proyecto.

Parte del contenido visual, educativo, organización y apoyo de redacción del proyecto se desarrolló con asistencia de ChatGPT (OpenAI), bajo revisión y adaptación del autor. Esta mención no implica patrocinio ni respaldo de OpenAI.

## Compilar

Requisitos: JDK 17, Android SDK 35 y Gradle 8.9.

```bash
gradle :app:assemblePreview
```

El APK de revisión se genera en `app/build/outputs/apk/preview/app-preview.apk` y usa un `applicationId` separado para poder coexistir con otras instalaciones durante pruebas.

## Validación automática

El workflow `Android build` valida la variante Preview con pruebas unitarias, Lint, compilación de APK, compilación instrumental y una prueba de arranque en Android 10. GitHub Pages y el APK público se despliegan únicamente desde `main`.
