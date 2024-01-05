# LA JUSTA

- Android application that works as a client making HTTP requests to a orchard management system.

- The application was made using **Android**, **Retrofit**, and **Google Maps**.

## Images

- _TO BE COMPLETED_

## Usage

- Download and install [Docker Compose](https://docs.docker.com/compose/install/)

- Access the `bd` folder and run Docker Compose to start the database on the local network:
  ```Bash
  cd bd
  docker compose up
  ```

- Obtain the IP of the PC hosting the database:
  ```Bash
  ifconfig
  ```

- Assign the IP to the `baseIP` variable in the main activity `MainActivity.kt`

- Run the application on a mobile device
