# El expediente del dentista

Aplicación educativa Android, bilingüe y orientada al trabajo sin conexión. Su finalidad es enseñar a integrar un expediente odontológico, comprender los hallazgos y desarrollar razonamiento clínico guiado. No sustituye la valoración profesional, la supervisión docente ni un expediente clínico institucional.

> **Uso educativo.** La aplicación no debe utilizarse como sistema institucional de expediente clínico ni como fuente autónoma de diagnóstico o prescripción.

## Qué incluye

- Interfaz en español e inglés y tratamiento Doctor / Doctora.
- Personalización con **24 paletas de animales**, cada una con ocho colores funcionales, incluidos colores de texto; siete estilos tipográficos, cuatro tamaños de texto y tres formas de tarjeta.
- Ficha de identificación didáctica y anamnesis guiada, con clasificación ASA.
- Nota de ingreso generada a partir de la información capturada.
- Odontograma interactivo, ICDAS, CPOD/ceod, O'Leary, IPC e IHOS.
- Periodontograma por diente con sondaje de seis sitios, sangrado, placa, supuración, recesión, movilidad y furcación.
- Orientación educativa pulpar y periapical basada en signos, síntomas, pruebas y hallazgos seleccionados.
- Diagnóstico y alternativas educativas de tratamiento por diente.
- Orientación educativa sobre mantenimiento de espacio y ortodoncia preventiva.
- Ejemplos de notas de evolución.
- Herramientas educativas de medicamentos y anestésicos con cálculos por peso cuando hay referencias disponibles. Los resultados no son prescripciones ni autorizaciones de administración.

## Funcionamiento offline

El manifiesto Android no solicita el permiso de Internet. Las funciones principales deben conservar su operación sin servicios remotos. Esta condición debe seguir verificándose en cada integración de módulos y recursos visuales.

La app no utiliza el mecanismo de respaldo automático de Android (android:allowBackup="false"). Los expedientes y preferencias se conservan en el almacenamiento local de la aplicación.

## Almacenamiento, privacidad y respaldos

La política de almacenamiento real es la siguiente:

- **Expedientes principales:** ClinicalRecordStore guarda los registros en SharedPreferences y los cifra con AES-GCM mediante una clave de Android Keystore. También contiene una ruta de migración para registros antiguos almacenados en texto.
- **Campos adicionales de cada expediente:** RecordFieldStore guarda cada campo por identificador de práctica. Las nuevas escrituras utilizan AES-GCM con una clave independiente de Android Keystore. Los valores anteriores, que estaban serializados y codificados en Base64 sin cifrado, se migran al leerse: primero se crea y verifica el valor cifrado; después, el valor nuevo y la eliminación del anterior se aplican juntos en una sola actualización. Si la clave no está disponible o la verificación falla, la versión anterior no se elimina y la escritura se bloquea para evitar degradar el almacenamiento a texto sin protección.
- **Preferencias:** idioma, tratamiento, paleta, tipografía, tamaño de texto y forma de tarjetas se guardan localmente en preferencias de la aplicación.
- **Fotografías:** al seleccionar una imagen o tomar una fotografía, la ficha intenta copiar sus bytes al directorio privado de la aplicación filesDir/record_photos/{id}. Las fotografías anteriores guardadas como URI de proveedor, archivo temporal de caché o adjunto restaurado se migran a ese directorio al abrir la ficha, si la URI de origen sigue siendo legible. La ficha sustituye la URI guardada sólo después de comprobar que la copia privada se creó correctamente. Si no puede leer la fuente, conserva la URI original y muestra una advertencia: esa fotografía puede no estar disponible sin conexión. Los archivos de fotografía se guardan en el espacio privado de la app, pero no se cifran individualmente con AES-GCM; el cifrado AES-GCM descrito arriba corresponde a los campos almacenados en SharedPreferences.
- **Exportación y restauración:** los respaldos JSON contienen información clínica y pueden incluir fotografías codificadas en Base64. El archivo exportado no se cifra automáticamente. La persona usuaria elige dónde guardarlo y debe protegerlo como información sensible. La restauración crea un expediente nuevo. Si falla la importación de campos o fotografías, se elimina el expediente parcial para no dejar una restauración incompleta.
- **Eliminación:** eliminar una práctica elimina sus campos locales, las fotografías privadas del directorio filesDir/record_photos/{id} y los adjuntos restaurados en filesDir/record_attachments/{id}. No elimina archivos que permanezcan en aplicaciones o proveedores de contenido externos.

Si los datos cifrados no pueden descifrarse, la aplicación muestra una advertencia y evita sobrescribirlos con valores iniciales. Conserva el dispositivo y no elimines la práctica; utiliza un respaldo válido cuando exista. Las claves de Android Keystore son propias del dispositivo y no se incluyen en un respaldo JSON.

## Compilar

Requisitos: JDK 17, Gradle 8.9 y Android SDK con la plataforma y herramientas utilizadas por el workflow (Android 36 / Build Tools 36.0.0).

```bash
gradle :app:testDebugUnitTest
gradle :app:assembleDebug
```

El APK de depuración se genera en app/build/outputs/apk/debug/app-debug.apk. GitHub Actions ejecuta las pruebas unitarias y genera el APK al actualizar main. La compilación de AAB de distribución sólo se ejecuta cuando están configurados los secretos de firma de release del repositorio.

## Verificación pendiente

La auditoría corrigió el cifrado de campos adicionales, la migración compatible de sus valores anteriores, la eliminación de adjuntos y fotografías privadas al borrar un expediente, el rollback de importaciones incompletas y la copia/migración de fotografías al almacenamiento privado. La prueba final de los selectores de documentos y cámara debe realizarse en dispositivos Android reales; las URI que el proveedor no permita leer se conservan con advertencia y no se consideran garantizadas offline.
