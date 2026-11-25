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
- **Campos adicionales de cada expediente:** RecordFieldStore guarda cada campo por identificador de práctica. Las nuevas escrituras utilizan AES-GCM con una clave independiente de Android Keystore. Los valores anteriores, que estaban serializados y codificados en Base64 sin cifrado, se migran al leerse: primero se guarda y verifica una copia cifrada y después se elimina la clave antigua. Si la clave no está disponible o la verificación falla, la versión anterior no se elimina y la escritura se bloquea para evitar degradar el almacenamiento a texto sin protección.
- **Preferencias:** idioma, tratamiento, paleta, tipografía, tamaño de texto y forma de tarjetas se guardan localmente en preferencias de la aplicación.
- **Fotografías:** los respaldos intentan leer los bytes de las URI registradas en los campos de fotografía. Al restaurar un respaldo, los adjuntos incluidos se copian al almacenamiento privado de la aplicación en filesDir/record_attachments/<id>. La copia privada de cada imagen en el momento de seleccionarla o capturarla no queda demostrada por la auditoría de los archivos de almacenamiento; por tanto, no se debe asumir que cualquier URI de proveedor externo seguirá accesible sin conexión. Ese flujo debe probarse y, si es necesario, completarse en el módulo de fotografía.
- **Exportación y restauración:** los respaldos JSON contienen información clínica y pueden incluir fotografías codificadas en Base64. El archivo exportado no se cifra automáticamente. La persona usuaria elige dónde guardarlo y debe protegerlo como información sensible. La restauración crea un expediente nuevo. Si falla la importación de campos o fotografías, se elimina el expediente parcial para no dejar una restauración incompleta.
- **Eliminación:** eliminar una práctica elimina sus campos locales y los adjuntos restaurados en el directorio privado de esa práctica. No elimina archivos que permanezcan en aplicaciones o proveedores de contenido externos.

Si los datos cifrados no pueden descifrarse, la aplicación muestra una advertencia y evita sobrescribirlos con valores iniciales. Conserva el dispositivo y no elimines la práctica; utiliza un respaldo válido cuando exista. Las claves de Android Keystore son propias del dispositivo y no se incluyen en un respaldo JSON.

## Compilar

Requisitos: JDK 17, Gradle 8.9 y Android SDK con la plataforma y herramientas utilizadas por el workflow (Android 36 / Build Tools 36.0.0).

```bash
gradle :app:testDebugUnitTest
gradle :app:assembleDebug
```

El APK de depuración se genera en app/build/outputs/apk/debug/app-debug.apk. GitHub Actions ejecuta las pruebas unitarias y genera el APK al actualizar main. La compilación de AAB de distribución sólo se ejecuta cuando están configurados los secretos de firma de release del repositorio.

## Verificación pendiente

La auditoría de esta etapa corrigió el cifrado de campos adicionales, la migración compatible de sus valores anteriores, la eliminación de adjuntos restaurados y el rollback de importaciones incompletas. Falta verificar el módulo visual de selección/captura de fotografías en un dispositivo Android y demostrar que cada foto nueva se copia al almacenamiento privado al seleccionarla, de modo que su disponibilidad offline no dependa de una URI externa.
