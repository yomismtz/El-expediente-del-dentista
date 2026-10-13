# Auditoría clínica de índices odontológicos

Fecha: 2026-10-12  
Rama revisada: `audit-clinical-indices`  
Alcance: revisión estática del código y contraste con referencias primarias accesibles. No equivale a validación clínica formal ni a pruebas de ejecución.

## Hallazgos principales

### CPOD/ceod — requiere validación de contenido y flujo
- El modelo contempla 32 dientes permanentes (incluidos terceros molares) y 20 temporales. El recuento por diente es compatible con la estructura habitual del índice, y el código diferencia dientes ausentes por caries de ausencias por otras causas.
- La pantalla exige que todos los dientes del dominio tengan un registro explícito antes de presentar un total como completo. Esto evita interpretar dientes sin registrar como sanos.
- Se retiraron las bandas universales “baja/moderada/alta/muy alta” asociadas a valores enteros de CPOD/ceod. El índice es un recuento; las categorías de severidad dependen de edad, población y protocolo. Ahora el texto evita asignar un umbral universal.
- Limitación pendiente: el modelo de estado por diente no permite representar simultáneamente restauración y caries recurrente. Para el total por diente, la caries debe prevalecer sobre el componente obturado, pero la ficha clínica debería poder conservar ambos hallazgos por separado si se necesita documentarlos.
- Verificar en pruebas funcionales que el estado de diente no erupcionado, sellado, extracción indicada y ausencia por otras causas nunca se sume erróneamente como C/P/O o c/e/o.

### O’Leary
- La fórmula implementada es superficies positivas / superficies evaluadas × 100, con cuatro superficies por diente presente (vestibular, lingual/palatina, mesial y distal); las superficies oclusales no forman parte del denominador.
- Una entrada ausente en el mapa se trata como pendiente, mientras que un registro explícito vacío significa 0 superficies positivas. Esta distinción es apropiada para no confundir “no registrado” con “sin placa”.
- Pendiente de validación: revisar el manejo de terceros molares y los criterios de inclusión de dientes según el protocolo concreto que se decida adoptar; documentar si se usa la convención de cuatro superficies para todos los dientes presentes o si se excluyen terceros molares.

### IHOS / OHI-S
- La fórmula debe ser promedio del índice de detritos + promedio del índice de cálculo, cada componente calculado sobre los sitios puntuados; rango total 0–6. Los criterios de puntuación de 0–3 y los seis sitios deben coincidir con Greene–Vermillion.
- Se corrigió el uso automático de terceros molares como sustitutos. La pantalla y el motor ahora se limitan a los sustitutos adyacentes estándar: 17 por 16, 21 por 11, 27 por 26, 37 por 36, 41 por 31 y 47 por 46.
- El motor calcula solo cuando hay puntuación de detritos y cálculo en los seis sitios seleccionados. La pantalla permite excluir sitios; debe probarse que la exclusión se haga conforme al protocolo y que el resultado no se presente como comparable si se calcula con menos de seis sitios.
- Pendiente: contrastar cada ilustración y cada descripción de puntuación 0–3 con la ficha original de Greene–Vermillion; las imágenes locales no sustituyen esa validación.

### IPC / CPI periodontal
- Se corrigió el nombre de la herramienta: IPC significa Índice Periodontal Comunitario (Community Periodontal Index, CPI), no “índice de placa comunitario”.
- Los códigos 0–4 corresponden, de forma resumida, a salud, sangrado, cálculo/factor retentivo, bolsa de 4–5 mm y bolsa de 6 mm o más; X indica sextante no evaluable/excluido.
- Limitación importante: la interfaz actual permite registrar seis sitios por diente y después calcula un código máximo por sextante si hay al menos dos dientes evaluables. Eso es una implementación educativa de cribado, no una reproducción completa y estricta de todos los protocolos de encuesta OMS, que varían por edición, edad, dientes índice y sitios examinados. No se debe presentar como diagnóstico periodontal ni como encuesta epidemiológica estandarizada sin elegir explícitamente un protocolo.
- Pendiente: diferenciar “sin registro” de “sextante excluido” (X) y comprobar que un código 0 no se asigne por defecto a sitios no examinados. Calibrar la fuerza de sondaje y el uso de sonda CPI corresponde al procedimiento clínico, no a la aplicación.

### ICDAS
- Los códigos coronales de caries 0–6 y los códigos de restauración/sellador 0–8 están separados en el modelo de dos dígitos. Los códigos especiales 90–93 y 96–99 se manejan separadamente.
- Las descripciones de 0–6 son resúmenes reconocibles de los criterios ICDAS, pero no son suficientes por sí solas para estandarizar el examen. El código 1 requiere secado; el examen debe seguir las instrucciones de inspección visual, limpieza, secado e iluminación del protocolo ICDAS completo.
- Los códigos especiales 96 (superficie no examinable), 97/98 (diente ausente) y 99 (no erupcionado) no deben tratarse como grados de caries 0–6 ni sumarse al CPOD como si fueran superficies cariadas.
- Pendiente: añadir una referencia explícita a la guía oficial ICDAS en la aplicación y verificar las reglas de superficies no examinables, restauraciones y códigos especiales en los flujos de migración de datos antiguos.

## Referencias contrastadas

1. World Health Organization. *Oral Health Surveys: Basic Methods*, 5th ed. 2013. Incluye definiciones de CPOD/ceod y CPI y los códigos usados en encuestas.  
   https://www.who.int/publications-detail-redirect/9789241548649
2. O’Leary TJ, Drake RB, Naylor JE. “The Plaque Control Record.” *Journal of Periodontology*. 1972;43(1):38. DOI: 10.1902/jop.1972.43.1.38.  
   https://pubmed.ncbi.nlm.nih.gov/4500182/
3. Greene JC, Vermillion JR. *The Simplified Oral Hygiene Index (OHI-S)*. Debe contrastarse la ficha de seis superficies y las reglas de sustitución con la publicación/manual original antes de certificar la herramienta.
4. ICDAS Foundation. Los criterios completos ICDAS deben ser la referencia normativa para los códigos 0–6, restauraciones y códigos especiales; la aplicación actualmente no enlaza una fuente oficial explícita en el código revisado.

## Estado de validación

- Revisión estática de código: realizada en los archivos indicados.
- Correcciones aplicadas en esta rama: nombre IPC; sustitutos IHOS; retirada de bandas universales no sustentadas para CPOD/ceod.
- Compilación Gradle, pruebas unitarias y pruebas en dispositivo: **no ejecutadas en este entorno**. No se declara la auditoría como aprobada hasta que esas pruebas se ejecuten y un profesional odontólogo revise los criterios y las imágenes.
