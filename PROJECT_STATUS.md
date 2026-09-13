# Estado del proyecto · YSM Expediente

Última actualización planificada: **v0.16-debug**.

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
- La pantalla de configuración cambia entre 1, 2 o 3 columnas según ancho disponible y usa desplazamiento vertical para teléfonos compactos o letra grande.

## Módulos principales ya incorporados
- Nota de ingreso como centro de navegación.
- Odontograma por cuadrantes.
- ICDAS, CPOD/ceod, O'Leary, IPC, IHOS y periodontograma.
- Signos vitales, ATM, oclusión, mucosas, postura y auxiliares.
- Diagnóstico pulpar y periapical interactivos.
- Endodoncia interactiva con conductometría y protocolos educativos.
- Prótesis interactiva: dos arcadas, presente/ausente, espacios edéntulos, Kennedy y modificaciones, reglas de Applegate, Seibert, retenedores PPR, PPR, prótesis total y fija.
- En cada examen debe existir una guía de **qué escribir al final en el expediente físico**.

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

## Cambio v0.15
- Sustituye el onboarding anterior por una pantalla inicial estable.
- Corrige el flujo de **Continuar** para evitar el cierre reportado de la app.
- Agrega las 10 paletas inspiradas en aves y Agaporni como predeterminada.

## Cambio v0.16
- Añade tamaño de letra persistente.
- Añade botón de configuración visible dentro de la app.
- Permite cambiar idioma, Doctor/Doctora, paleta, tipo de letra y tamaño sin reinstalar.
- Guarda cada cambio inmediatamente en preferencias locales.
- Respeta el tamaño de fuente de Android además del tamaño elegido en YSM Expediente.
- Clasifica el ancho de pantalla como compacto, mediano o expandido para adaptar la pantalla de configuración.
- Evita depender de alturas fijas en Configuración y usa contenido desplazable para mejorar el uso con letra grande.

## Próximos puntos sugeridos
1. Propagar el comportamiento adaptable a las hojas dobles y componentes antiguos con medidas fijas, especialmente en teléfonos muy estrechos y con escalas de fuente extremas.
2. Terminar de propagar las paletas de aves a componentes antiguos con colores codificados de forma fija.
3. Continuar con Cirugía interactiva y protocolos.
4. Seguir refinando Prótesis con diseño visual más anatómico y componentes removibles/fijos.

## Repositorio
https://github.com/yomismtz/El-expediente-del-dentista
