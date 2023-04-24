# Rangiffler
Welcome to Rangiffler! This project has been inspired by my training at `QA.GURU`, and I'm excited to share some of the
key accomplishments I've achieved throughout my studies.
I hope that this repository will provide insight into my skills and approach to work.

This repository showcases the work I've done using technologies:
- Java 17
- Gradle 7.6
- Spring Authorization Server
- Spring OAuth 2.0 Resource Server
- Spring Web
- Spring gRPC
- Spring Web Services
- Spring Data JPA
- Postgres
- JUnit 5 (Extensions, Resolvers, etc)
- Retrofit 2
- Allure
- Selenide

These technologies have been used to develop a backend on microservices and create end-to-end automated tests.

The main technology used for the frontend is React.

---

## Preparations for working with the Rangiffler project

#### 1. Install Docker by following the instructions for your operating system
- [Windows](https://docs.docker.com/desktop/install/windows-install/)
- [Mac](https://docs.docker.com/desktop/install/mac-install/) (for ARM and Intel, different packages)
- [Linux](https://docs.docker.com/desktop/install/linux-install/)

After installation, make sure the docker commands work by running `docker -v` in the command prompt/terminal.

#### 2. Pull the postgres container version 15.1 by running the following command

```bash
docker pull postgres:15.1
```

To verify that the image has been downloaded, run the command `docker images`.

#### 3. Create a volume for saving the data from the database in docker on your computer

```bash
docker volume create pgdata
```

#### 4. Run the database with the following command

```bash
docker run --name rangiffler-all -p 5432:5432 -e POSTGRES_PASSWORD=secret -v pgdata:/var/lib/postgresql/data -d postgres:15.1
```

#### 5. Connect to the Postgres database using a visualizing program (e.g., IntelliJ IDEA, DBeaver, Datagrip, or PgAdmin 4) and create empty databases for microservices with the following SQL commands
```sql
CREATE DATABASE "rangiffler-auth" WITH OWNER postgres;
CREATE DATABASE "rangiffler-geo" WITH OWNER postgres;
CREATE DATABASE "rangiffler-photo" WITH OWNER postgres;
CREATE DATABASE "rangiffler-users" WITH OWNER postgres;
```

To connect to the Postgres database, use the following information:
- Host: `localhost`
- Port: `5432`
- User: `postgres`
- Password: `secret`
- Database name: `postgres`

#### 6. Install Java version 17 or newer, as the project does not support versions below 17
To check the installed version, run the command `java -version`.

#### 7. Install the package manager for building the front-end (npm)
The recommended version of Node.js is 18.13.0 (LTS).
Follow the [instructions](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm) to download and install Node.js and npm.

---

## Running Rangiffler

#### 1. Launch the Frontend

* Navigate to the frontend module:
```bash
cd rangiffler-client
```

* Install dependencies:
```bash
cd npm i
```
You can skip this step for subsequent launches if no changes have been made to the frontend.

* Launch the frontend:
```bash
npm start
```
The frontend will start in your browser at port 3001: http://127.0.0.1:3001/.

#### 2. Launch the remaining services in any order: Rangiffler-auth, Rangiffler-users, Rangiffler-geo, Rangiffler-photo, Rangiffler-gateway

#### Launch from IDE
In the RangifflerAuthApplication, RangifflerGatewayApp, RangifflerGeoApplication, RangifflerUsersApplication, and RangifflerPhotoApplication classes, select 'run' in your IDE.

#### Launch using Gradle
```bash
cd rangiffler-auth
gradle bootRun
```
```bash
cd rangiffler-gateway
gradle bootRun
```
```bash
cd rangiffler-users
gradle bootRun
```
```bash
cd rangiffler-geo
gradle bootRun
```
```bash
cd rangiffler-photo
gradle bootRun
```

#### 3. Open the frontend in your browser http://127.0.0.1:3001/ and enjoy!

---

## Running e2e Tests and viewing report

### Launching tests
#### Launch from IDE
Go to the rangiffler-e-2-e-tests module and select 'run' in your IDE.

#### Launch using Gradle
Execute the following command:
```bash
cd rangiffler-e-2-e-tests
gradle clean test
```

### Viewing Allure Report
Execute the following command:
```bash
cd rangiffler-e-2-e-tests
gradle allureServe
```

This command launches the Allure report in your default browser. The report will contain detailed information on the tests run.

---
Thanks for reading!
