package com.osirix.api.minio;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import io.minio.http.Method;


public interface MinioService {

	/**
     * Asegura que el bucket principal 'app-repo' exista.
     */
    void ensureAppRepoBucketExists();

    /**
     * Genera una URL prefirmada para una operación específica (GET o PUT) en un objeto.
     *
     * @param appId           El ID de la aplicación.
     * @param fileType        El tipo de carpeta (FILES o MEDIA).
     * @param objectName      El nombre del objeto en MinIO (e.g., "documento.pdf", "icon.png").
     * @param method          El método HTTP para el cual se firma la URL (Method.GET o Method.PUT).
     * @param expiryInMinutes Tiempo de validez de la URL en minutos.
     * @param contentType     Opcional (principalmente para PUT): El Content-Type que el cliente debe usar.
     *                        Si es null o vacío para PUT, MinIO no validará este header en la subida.
     * @return La URL prefirmada.
     * @throws Exception Si ocurre un error al generar la URL.
     */
    String getPresignedUrlForObject(String appId, AppFileType fileType, String objectName, Method method, int expiryInMinutes, String contentType) throws Exception;
    
    /**
     * Genera URLs prefirmadas de DESCARGA para todos los objetos existentes dentro de la carpeta 'files' (o 'media') de una app.
     *
     * @param appId           El ID de la aplicación.
     * @param fileType        La carpeta específica (FILES o MEDIA) para la cual listar y generar URLs.
     * @param expiryInMinutes Tiempo de validez de las URLs en minutos.
     * @return Un mapa donde la clave es el nombre del objeto y el valor es su URL prefirmada de descarga.
     * @throws Exception Si ocurre un error.
     */
    Map<String, String> getPresignedDownloadUrlsForFolder(String appId, AppFileType fileType, int expiryInMinutes) throws Exception;

    /**
     * Sube un conjunto de archivos: un icono, varias imágenes a la carpeta 'media',
     * y un archivo ZIP a la carpeta 'files' de una aplicación.
     * Esta operación se realiza directamente desde el servidor.
     *
     * @param appId         El ID de la aplicación.
     * @param iconFile      El archivo del icono (MultipartFile). Se guardará con su nombre original en appId/media/.
     * @param imageFiles    Una lista de archivos de imagen (List<MultipartFile>). Se guardarán con sus nombres originales en appId/media/.
     * @param zipFile       El archivo ZIP (MultipartFile). Se guardará con su nombre original en appId/files/.
     * @return Un mapa con AppFileType como clave y una lista de los nombres de los objetos subidos como valor.
     * @throws Exception Si ocurre un error durante la subida de alguno de los archivos.
     */
    Map<AppFileType, List<String>> uploadAppBatchFiles(String appId, MultipartFile iconFile, List<MultipartFile> imageFiles, MultipartFile zipFile) throws Exception;

    /**
     * Lista todos los objetos (solo nombres) dentro de las carpetas 'files' y 'media' de una aplicación.
     *
     * @param appId El ID de la aplicación.
     * @return Un mapa donde la clave es AppFileType (FILES o MEDIA) y el valor es una lista de nombres de objetos
     *         encontrados en la respectiva subcarpeta.
     * @throws Exception Si ocurre un error al listar.
     */
    Map<AppFileType, List<String>> listAllAppObjects(String appId) throws Exception;


    /**
     * Elimina un objeto de una carpeta específica de una app.
     *
     * @param appId      El ID de la aplicación.
     * @param fileType   El tipo de archivo/carpeta (FILES o MEDIA).
     * @param objectName El nombre del objeto a eliminar.
     * @throws Exception Si ocurre un error al eliminar.
     */
    void deleteObject(String appId, AppFileType fileType, String objectName) throws Exception;

    /**
     * Verifica si un objeto específico existe.
     * @param appId El ID de la aplicación.
     * @param fileType El tipo de archivo/carpeta (FILES o MEDIA).
     * @param objectName El nombre del objeto.
     * @return true si existe, false si no.
     * @throws Exception Si ocurre un error durante la verificación (distinto a no encontrar el objeto).
     */
    boolean objectExists(String appId, AppFileType fileType, String objectName) throws Exception;
	
}
