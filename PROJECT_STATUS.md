# Estado del proyecto · YSM Expediente

Última actualización planificada: **v0.17-debug**.

## Propósito
Aplicación educativa offline para estudiantes de odontología. Enseña cómo llenar un expediente clínico físico y ofrece exámenes interactivos. No es un expediente electrónico de pacientes y no debe sustituir supervisión docente ni diagnóstico clínico definitivo.

## Interfaz e identidad
- Nombre: **YSM Expediente · El expediente del dentista**.
- Paleta predeterminada: **Agaporni** (lavanda, lila, púrpura, menta/turquesa).
- Paletas disponibles: Agaporni, Tucán, Pavo real, Fénix, Colibrí, Pato mandarín, Ninfa, Martín pescador, Guacamaya y Quetzal.
- Primera pantalla: selección de idioma, Doctor/Doctora y paleta antes de continuar.
- Idiomas actuales: Español e Inglés.
- Botón de **Configuración** disponible dentro de la app.
- Configuración persistente: idioma, Doctor/Doctora, paleta, tipo de letra y tamaño de letra se restauran al volver a abrir la app.
- Tamaños internos: pequeña, normal, grande y muy grande.
- La tipografía sigue usando `sp`, por lo que también respeta la escala de letra configurada en Android.

## Sistema adaptable v0.17
- La interfaz clasifica el ancho disponible como compacto, mediano o expandido.
- Si Android usa letra grande, las cuadrículas reducen automáticamente el número de columnas y priorizan lectura sobre densidad.
- Las pantallas principales son desplazables verticalmente y evitan depender de alturas fijas.
- La carpeta clínica cambia de diseño: una sección por vez en teléfono pequeño, dos columnas en tamaño medio y tres hojas/grupos en pantalla amplia.
- El centro de exámenes cambia de 1 a 3 columnas según el dispositivo.
- La barra flotante de “Qué escribir” / “Nota de ingreso” se simplifica a iconos en pantallas estrechas o con letra grande.
- Las paletas nuevas se toman desde `MaterialTheme` en las nuevas interfaces para que Agaporni, Tucán, Pavo real, Fénix, Colibrí, Pato mandarín, Ninfa, Martín pescador, Guacamaya y Quetzal cambien realmente el aspecto.

## Módulos principales ya incorporados
- Nota de ingreso como centro de navegación.
- Odontograma por cuadrantes.
- ICDAS, CPOD/ceod, O'Leary, IPC, IHOS y periodontograma.
- Signos vitales, ATM, oclusión, mucosas, postura y auxiliares.
- Diagnóstico pulpar y periapical interactivos.
- Endodoncia interactiva con conductometría y protocolos educativos.
- Prótesis interactiva: dos arcadas, presente/ausente, espacios edéntulos, Kennedy y modificaciones, reglas de Applegate, Seibert, retenedores PPR, PPR, prótesis total y fija.
- En cada examen debe existir una guía de **qué escribir al final en el expediente físico**.

## Pantallas responsivas nuevas v0.17
- **Odontograma:** dentición permanente/temporal, cuadrantes adaptables, dientes en rejilla dinámica, selector de caries/restauración/sellador, diagrama de superficies y presente/ausente.
- **IPC:** sextantes adaptables, selección de diente, seis sitios, códigos 0–4/X y resumen automático sin comprimir texto.
- **IHOS:** dientes índice, sustitutos, exclusión de sitio no evaluable, detritos/cálculo 0–3 y resultado automático.
- **Kennedy:** dos arcadas, presente/ausente, detección de espacios, Kennedy I–IV, modificaciones, segundos molares que no se reemplazan y reglas de Applegate paso a paso.
- **Prótesis:** desde Kennedy adaptable se conserva acceso al diseñador completo de PPR, total y fija.
- **Centro de exámenes:** tarjetas de acceso que cambian automáticamente de 1, 2 o 3 columnas.

## Prótesis · estado actual
- Tocar cada diente para marcarlo presente/ausente.
- Detectar espacios edéntulos automáticamente.
- Proponer Kennedy I, II, III o IV y modificaciones.
- Explicar qué espacio determina la clase y por qué.
- Aplicar reglas de Applegate paso a paso.
- Clasificación de Seibert.
- Retenedores Akers, RPI, RPA, barra I y combinado.
- Diseñador PPR con descansos, retención, planos guía y base/malla.
- Prótesis total y fija; materiales y terminaciones.

## Próximos puntos sugeridos
1. Llevar el mismo patrón adaptable a ICDAS, O'Leary, periodontograma, ATM, oclusión, mucosas, endodoncia y formularios largos.
2. Terminar de sustituir colores antiguos codificados directamente por colores de `MaterialTheme`.
3. Continuar con Cirugía interactiva y protocolos una vez que la base visual adaptable esté estable en teléfonos reales.
4. Seguir refinando Prótesis con diseño anatómico más visual para descansos, retenedores, conectores, bases y pónticos.

## Repositorio
https://github.com/yomismtz/El-expediente-del-dentista
