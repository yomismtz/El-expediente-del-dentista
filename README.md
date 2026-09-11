# El expediente del dentista

Aplicación educativa Android, bilingüe y completamente offline para enseñar cómo se llena un expediente clínico odontológico mediante formularios, índices y razonamiento clínico guiado.

> **Uso educativo.** No está diseñada para almacenar, administrar ni sustituir expedientes clínicos reales, ni para emitir diagnósticos clínicos autónomos.

## Qué incluye

- Configuración Español / English y Doctor / Doctora.
- Personalización dentro de la app: seis paletas de color y cuatro estilos tipográficos.
- Ficha de identificación y anamnesis guiada por grupos de enfermedades y clasificación ASA.
- Nota de ingreso generada automáticamente con la información capturada durante la sesión.
- Odontograma interactivo, ICDAS, CPOD/ceod, O'Leary, IPC e IHOS.
- Periodontograma por diente con sondaje de seis sitios, sangrado, placa, supuración, recesión, movilidad y furcación.
- Orientación diagnóstica pulpar y periapical a partir de signos, síntomas, pruebas clínicas y hallazgos radiográficos seleccionados.
- Diagnóstico y tratamiento por diente: tres opciones educativas por diagnóstico y retroalimentación de la alternativa preferente.
- Sugerencias educativas de mantenimiento de espacio/ortodoncia preventiva ante pérdidas o extracciones de dentición temporal.
- Generador de ejemplos de notas de evolución según los tratamientos seleccionados.

## Privacidad y funcionamiento

La app no solicita permiso de Internet. Los datos clínicos introducidos son datos de práctica y viven únicamente en la sesión activa; al reiniciar la sesión se eliminan. Solo las preferencias de idioma, tratamiento Doctor/Doctora, paleta y tipografía se guardan localmente en el dispositivo.

## Compilar

Requisitos: JDK 17, Android SDK 35 y Gradle 8.9.

```bash
gradle :app:assembleDebug
```

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`. GitHub Actions también compila cada cambio enviado a `main` y publica el APK de depuración como artefacto del workflow.
