# El expediente del dentista

Aplicación educativa Android, bilingüe y completamente offline para enseñar cómo se llena un expediente clínico odontológico mediante formularios, índices y razonamiento clínico guiado.

> **Uso educativo.** No está diseñada para almacenar, administrar ni sustituir expedientes clínicos reales, ni para emitir diagnósticos clínicos autónomos.

## Qué incluye

- Configuración Español / English y Doctor / Doctora.
- Personalización dentro de la app con 13 paletas inspiradas en aves y cuatro estilos tipográficos.
- Ficha de identificación y anamnesis guiada por grupos de enfermedades y clasificación ASA.
- Nota de ingreso generada automáticamente con la información capturada durante la sesión.
- Odontograma interactivo, ICDAS, CPOD/ceod, O'Leary, IPC e IHOS.
- Periodontograma por diente con sondaje de seis sitios, sangrado, placa, supuración, recesión, movilidad y furcación.
- Orientación diagnóstica pulpar y periapical a partir de signos, síntomas, pruebas clínicas y hallazgos radiográficos seleccionados.
- Diagnóstico y tratamiento por diente con opciones educativas y retroalimentación.
- Endodoncia y prótesis con módulos interactivos de práctica.
- Auxiliares diagnósticos y ortodóncicos educativos.
- Generador de ejemplos de notas de evolución según los tratamientos seleccionados.
- Navegación atrás con historial y gesto desde el borde izquierdo una vez completado el onboarding.

## Privacidad y funcionamiento

La app no solicita permiso de Internet. Los datos clínicos introducidos son datos de práctica y viven únicamente en la sesión activa; no se guardan como expediente clínico. La sesión se mantiene durante recreaciones normales de la pantalla, como una rotación, y se pierde al finalizar el proceso de la app. Las preferencias de idioma, tratamiento Doctor/Doctora, paleta y tipografía sí se guardan localmente en el dispositivo.

## Compilar

Requisitos: JDK 17, Android SDK 35 y Gradle 8.9.

```bash
gradle :app:assembleDebug
```

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`.

## Validación automática

El workflow `Android build` se ejecuta en cambios hacia `main` y valida, en este orden:

```bash
gradle :app:testDebugUnitTest --stacktrace
gradle :app:lintDebug --stacktrace
gradle :app:assembleDebug --stacktrace
```

GitHub Pages y el APK público se despliegan únicamente desde `main`.
