# Estado del proyecto · YSM Expediente

Última actualización: **v0.21-debug**.

## Propósito
Aplicación educativa offline para estudiantes de odontología. Enseña cómo llenar un expediente clínico físico y ofrece exámenes interactivos. No es un expediente electrónico de pacientes y no sustituye supervisión docente ni diagnóstico clínico definitivo.

## Identidad e interfaz
- Nombre: **YSM Expediente · El expediente del dentista**.
- Logo/icono: ave académica + expediente dental + diente.
- Slogan: **“Deja volar tu imaginación y tus conocimientos renacerán”**.
- Paleta predeterminada: **Agaporni**.
- Paletas: Agaporni, Tucán, Pavo real, Fénix, Colibrí, Pato mandarín, Ninfa, Martín pescador, Guacamaya, Quetzal, Trichoglossus moluccanus, Cuervo y Abejaruco.
- Idiomas: Español e Inglés.
- Primera configuración: idioma, Doctor/Doctora y paleta.
- Configuración persistente: idioma, tratamiento, paleta, tipo de letra y tamaño de letra se restauran al reabrir.
- La app respeta también la escala de fuente configurada en Android.

## Cambios principales hasta v0.21
- Navegación atrás con historial real dentro de los módulos y gesto desde el borde izquierdo.
- El gesto de regreso queda desactivado durante la configuración inicial para evitar salidas accidentales.
- Onboarding actualizado con avatares Doctor/Doctora y nuevas paletas de aves.
- Barra superior reservada para **Nota de ingreso** y **Configuración**, sin superponerse al contenido.
- Navegación adaptable para teléfonos compactos, estándar y grandes.
- O’Leary e ICDAS comparten un diente visto desde oclusal, sin raíces, con caras V/L-P/M/D y centro oclusal cuando corresponde.
- Selectores dentales separados en maxilar y mandíbula.
- CPOD/ceod interactivo con cálculo automático.
- Mucosas, signos vitales, oclusión, auxiliares, periodontograma, endodoncia y prótesis cuentan con módulos educativos interactivos.

## Correcciones de estabilidad y publicación
- El ejercicio educativo activo se conserva durante recreaciones de la Activity, como una rotación de pantalla, mediante un `ViewModel` en memoria.
- GitHub Pages y el APK público se despliegan únicamente desde `main`; una rama de desarrollo ya no puede reemplazar accidentalmente la publicación de producción.
- El workflow de Android ejecuta pruebas unitarias y Android Lint antes de generar el APK.

## Sistema adaptable
- Ancho clasificado como compacto, mediano o expandido.
- Con letra grande se reducen columnas automáticamente.
- Contenido largo usa desplazamiento vertical.
- Carpeta y centro de exámenes adaptan la cantidad de columnas al dispositivo.
- Las pantallas nuevas toman colores de `MaterialTheme` para respetar la paleta elegida.

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
1. Probar v0.21 en teléfono real, especialmente navegación atrás, gesto lateral, onboarding y recreación por rotación.
2. Añadir pruebas unitarias para los motores de índices odontológicos y periodontales.
3. Persistir explícitamente en el modelo del ejercicio las selecciones de sustitutos/exclusiones de IHOS para que el resumen global use exactamente la misma selección que la pantalla.
4. Sustituir colores antiguos codificados directamente por `MaterialTheme` donde aún existan.
5. Continuar con Cirugía interactiva y protocolos después de estabilizar la interfaz.

## Repositorio
https://github.com/yomismtz/El-expediente-del-dentista
