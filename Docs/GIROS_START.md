# GIROS_START.md

## Proyecto

**Nombre:** Giros  
**Tipo:** app Android de decisiones random con rueda editable.  
**Objetivo:** crear ruedas personalizadas, girarlas varias veces y obtener un ranking aleatorio sin repetir opciones.

---

## Idea principal

La app permite crear una rueda con varias porciones. Cada porción tiene un nombre cargado por el usuario.

El usuario configura:

- nombre de la rueda;
- cantidad de porciones;
- nombres de las porciones;
- cantidad de giros;
- cantidad de puestos del ranking.

Cuando una opción sale elegida, se agrega al ranking y se quita de la rueda temporal. La rueda se achica a medida que avanza el sorteo.

La rueda guardada original no debe perder opciones. El sorteo trabaja con una copia temporal.

---

## Versión 1

La primera versión debe ser simple, offline y funcional.

Debe permitir:

1. Crear una rueda.
2. Editar una rueda.
3. Guardar ruedas con nombre.
4. Listar ruedas guardadas.
5. Elegir una rueda guardada.
6. Configurar cantidad de giros o puestos del ranking.
7. Girar la rueda.
8. Mostrar resultado de cada giro.
9. Quitar de la rueda la opción que salió.
10. Mostrar ranking parcial.
11. Mostrar ranking final.
12. Reiniciar sorteo con la rueda original.

---

## Reglas

- Una rueda debe tener al menos 2 opciones.
- No se puede pedir un ranking mayor que la cantidad de opciones.
- Una opción que ya salió no vuelve a participar en el mismo ranking.
- La rueda guardada original no se modifica durante el sorteo.
- El usuario puede borrar ruedas guardadas.
- El usuario puede editar nombres de opciones.

---

## Tecnología elegida

- Android nativo.
- Kotlin.
- Jetpack Compose.
- Arquitectura simple con Model, Repository, ViewModel y UI.
- Guardado local.
- Git.
- GitLab.

Para empezar, se puede usar almacenamiento local simple. Luego se puede mejorar con DataStore o Room.

---

## Estructura sugerida

```text
giros/
├── app/
│   └── src/
│       └── main/
│           └── java/com/example/giros/
│               ├── MainActivity.kt
│               ├── data/
│               │   ├── WheelRepository.kt
│               │   └── WheelStorage.kt
│               ├── model/
│               │   ├── Wheel.kt
│               │   ├── WheelOption.kt
│               │   └── SpinResult.kt
│               ├── ui/
│               │   ├── components/
│               │   │   └── WheelCanvas.kt
│               │   ├── screens/
│               │   │   ├── HomeScreen.kt
│               │   │   ├── EditWheelScreen.kt
│               │   │   ├── SpinConfigScreen.kt
│               │   │   ├── SpinScreen.kt
│               │   │   └── RankingScreen.kt
│               │   └── theme/
│               ├── util/
│               │   └── RandomSelector.kt
│               └── viewmodel/
│                   ├── WheelViewModel.kt
│                   └── SpinViewModel.kt
├── docs/
│   └── GIROS_START.md
├── README.md
└── .gitignore