# OsiriX-API

# How to start the project from Spring tool suite

Before starting the project from Spring tool suite, you must create a docker compose from the docker image found in the files.

It is also important to have an .env file like the following in the external folder to the project folder:

```bash
DATABASE_HOST=localhost
DATABASE_PORT=3306
DATABASE_USER=osirix_user
DATABASE_PASSWORD=EstaEsUnaPasswordSuperSegura1234
DATABASE_NAME=osirix_db
SERVER_PORT=8080
JWT_PRIVATE_KEY=964ffdb5e95797ad2be8a036924d97a1df90a004b69e25093959be7d5104601b
```

To start the project from Spring tool suite, you must first install the dependencies, to ensure that the project is imported correctly it is recommended to right click on the folder from the IDE, select the "Maven" option and then update project.

To start the project, right click again on the folder and select "Run as" and then "Spring Boot App".

This action will start a development server on port 8080.

By clicking on the following link you will be able to access the [API endpoints](https://osirix-3346.postman.co/workspace/Osirix-Workspace~6c977007-c487-4e21-8887-25ccadeeee99/collection/34224866-42ca2462-ae32-46af-b54f-3995d3a0348d?action=share&creator=34224866)
