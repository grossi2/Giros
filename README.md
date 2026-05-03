# Giros

Giros es una app Android hecha con Kotlin y Jetpack Compose para crear ruedas personalizadas, girarlas y obtener rankings aleatorios sin repetir opciones.

## Estado actual

La app hoy permite:

- crear ruedas con nombre y opciones
- guardar ruedas localmente en el dispositivo
- listar, editar y borrar ruedas
- elegir una rueda guardada
- girar la rueda y generar un ranking sin repetición
- reiniciar el sorteo usando la rueda original

## Stack

- Android nativo
- Kotlin
- Jetpack Compose
- ViewModel
- persistencia local en JSON

## Estructura principal

```text
app/src/main/java/com/example/giros/
├── data/
├── model/
├── ui/
│   ├── components/
│   └── screens/
└── viewmodel/
```

## Cómo ejecutar

1. Abrí el proyecto en Android Studio.
2. Esperá el sync de Gradle.
3. Ejecutá el módulo `app` en un emulador o dispositivo.

## Documento de inicio

La definición inicial del proyecto está en [Docs/GIROS_START.md](Docs/GIROS_START.md).
