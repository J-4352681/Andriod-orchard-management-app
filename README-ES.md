# LA JUSTA

- Aplicación Android que funciona como cliente haciendo peticiones http a un sistema de administración de huertas.

- La aplicación fue hecha utilzando **Android**, **Retrofit** y **Google Maps**.

## Imágenes

- ![Capturas de pantall](https://github.com/J-4352681/Andriod-orchard-management-app/blob/main/img/screenshots-ES.png?raw=true "Capturas de pantalla")

## Uso

- Descargar e installar [Docker Compose](https://docs.docker.com/compose/install/)

- Acceder a la carpeta `bd` y ejecutar Docker Compose para que arranque a funcionar la base de datos en red local:
  ```Bash
  cd bd
  docker compose up
  ```

- Obtener la IP de la PC que tiene la base de datos:
  ```Bash
  ifconfig
  ```

- Asignar la IP a la variable `baseIP` de la actividad principal `MainActivity.kt`

- Ejecutar la aplicación en un dispositivo móvil
