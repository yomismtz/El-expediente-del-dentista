# Estado del proyecto · YSM Expediente

Última actualización: **v0.19-debug**.

## Propósito
Aplicación educativa offline para estudiantes de odontología. Enseña cómo llenar un expediente clínico físico y ofrece exámenes interactivos. No es un expediente electrónico de pacientes y no sustituye supervisión docente ni diagnóstico clínico definitivo.

## Identidad e interfaz
- Nombre: **YSM Expediente · El expediente del dentista**.
- Logo/icono: ave académica + expediente dental + diente.
- Slogan: **“Deja volar tu imaginación y tus conocimientos renacerán”**.
- Paleta predeterminada: **Agaporni**.
- Paletas: Agaporni, Tucán, Pavo real, Fénix, Colibrí, Pato mandarín, Ninfa, Martín pescador, Guacamaya y Quetzal.
- Idiomas: Español e Inglés.
- Primera configuración: idioma, Doctor/Doctora y paleta.
- Configuración persistente: idioma, tratamiento, paleta, tipo de letra y tamaño de letra se restauran al reabrir.
- La app respeta también la escala de fuente configurada en Android.

## Correcciones v0.19 tras prueba en teléfono real
- La barra superior dejó de ser flotante: **Nota de ingreso** y **Configuración** ocupan espacio propio y ya no deben tapar títulos/subtítulos.
- Nueva navegación adaptable para teléfonos compactos, estándar y grandes.
- O’Leary e ICDAS comparten un nuevo diente visto desde **oclusal**, sin raíces, con contorno coronario, caras V/L-P/M/D y centro oclusal cuando corresponde.
- Selectores dentales separados en **dos filas: maxilar arriba y mandibular abajo**.
- **CPOD/ceod ahora es interactivo**: cada diente puede marcarse sano, cariado, obturado, ausente por caries, ausente por otra causa o sellado; el resultado cambia automáticamente.
- **Mucosas**: nueva boca abierta frontal con labios, carrillos, dientes, encía, paladar duro/blando, úvula, lengua, piso y orofaringe; las regiones son seleccionables.
- **Signos vitales**: agrega interpretación educativa de temperatura y glucosa capilar según contexto.
  - temperatura: alrededor de 37 °C habitual; ≥38 °C fiebre; ≤35 °C muy baja, con advertencia sobre sitio/método.
  - glucosa: alerta <70 mg/dL; ayuno 70–99 habitual; 100–125 elevado; ≥126 requiere evaluación/confirmación; objetivos frecuentes ADA 80–130 preprandial y <180 mg/dL pico posprandial.
  - una lectura capilar aislada no diagnostica diabetes.
- **Oclusión**: dibujos propios para plano terminal recto, escalón mesial/distal, Angle molar I/II/III, relación canina I/II/III, overjet, overbite/mordida abierta y mordida cruzada.
- Fuentes conceptuales resumidas en la app: NHS, MedlinePlus, CDC, ADA y Columbia University; se prioriza protocolo institucional.

## Sistema adaptable
- Ancho clasificado como compacto, mediano o expandido.
- Con letra grande se reducen columnas automáticamente.
- Contenido largo usa desplazamiento vertical.
- Carpeta y centro de exámenes adaptan cantidad de columnas al dispositivo.
- Nuevas pantallas toman colores de `MaterialTheme` para respetar la paleta elegida.

## Módulos principales incorporados
- Nota de ingreso como centro de navegación.
- Identificación, anamnesis/ASA, medicamentos y alergias.
- Signos vitales, ATM, oclusión, mucosas, postura y auxiliares.
- Odontograma por cuadrantes.
- ICDAS, CPOD/ceod, O'Leary, IPC, IHOS y periodontograma.
- Diagnóstico pulpar y periapical interactivos.
- Endodoncia interactiva con conductometría y protocolos educativos.
- Prótesis: Kennedy/Applegate, Seibert, retenedores PPR, PPR, total y fija.
- En los exámenes se conserva la guía de **qué escribir al final en el expediente físico**.

## Prótesis · estado actual
- Dos arcadas con presente/ausente.
- Detección automática de espacios edéntulos.
- Kennedy I–IV y modificaciones.
- Reglas de Applegate paso a paso.
- Seibert.
- Akers, RPI, RPA, barra I y combinado.
- Diseñador PPR con descansos, retención, planos guía y base/malla.
- Prótesis total y fija; materiales, terminaciones y pónticos.

## Próximos puntos sugeridos
1. Probar v0.19 en teléfono real, especialmente encabezados, boca abierta, caras dentales y dibujos de oclusión.
2. Llevar el mismo pulido visual a periodontograma, ATM, endodoncia y formularios largos.
3. Sustituir colores antiguos codificados directamente por `MaterialTheme` donde aún existan.
4. Continuar con Cirugía interactiva y protocolos después de estabilizar la interfaz.

## Repositorio
https://github.com/yomismtz/El-expediente-del-dentista
