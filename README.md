# OsiriX-API

# Como arrancar el proyecto desde Spring tool suite

Antes de arrancar el proyecto desde Spring tool suite, debes crear un docker compose a partir de la imagen de docker que se encuentra en los archivos.

Es importante tambien tener un archivo .env como el siguiente en la carpeta de externa a la carpeta del proyecto:

```bash
DATABASE_HOST=localhost
DATABASE_PORT=3306
DATABASE_USER=osirix_user
DATABASE_PASSWORD=EstaEsUnaPasswordSuperSegura1234
DATABASE_NAME=osirix_db
SERVER_PORT=8080
JWT_PRIVATE_KEY=964ffdb5e95797ad2be8a036924d97a1df90a004b69e25093959be7d5104601b
```

Para arrancar el proyecto desde Spring tool suite, primero debes instalar las dependencias, para asegurar que el proyecto se importa correctamente es recomendable hacer click derecho en la carpeta desde el IDE, seleccionar la opcion "Maven" y posteriormente update project.

Para arrancar el proyecto, has click derecho de nuevo en la carpeta y seleccionar "Run as" y luego "Spring Boot App".

Esta accion iniciará un servidor de desarrollo en el puerto 8080.

Al hacer click en el siguiente enlace podra acceder a los [endpoints de la API](https://osirix-3346.postman.co/workspace/Osirix-Workspace~6c977007-c487-4e21-8887-25ccadeeee99/collection/34224866-42ca2462-ae32-46af-b54f-3995d3a0348d?action=share&creator=34224866)
