package com.osirix.api.minio;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.minio.BucketExistsArgs;
import io.minio.GetBucketPolicyArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.SetBucketPolicyArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;

@Service
public class MinioServiceImpl implements MinioService {
	
	private static final Logger logger = LoggerFactory.getLogger(MinioServiceImpl.class);

    private final MinioClient minioClient;
    private final String mainBucketName; 

    public MinioServiceImpl(MinioClient minioClient,
                                   @Value("${minio.bucket-name}") String mainBucketName) {
        this.minioClient = minioClient;
        this.mainBucketName = mainBucketName;
    }
    
    private String buildAppFolderPath(String appId, AppFileType fileType) {
        if (appId == null || appId.isBlank()) {
            throw new IllegalArgumentException("App ID no puede ser nulo o vacío.");
        }
        if (fileType == null) {
            throw new IllegalArgumentException("AppFileType no puede ser nulo.");
        }
        return String.format("%s/%s/", appId.trim(), fileType.getFolderName());
    }

    private String buildObjectPath(String appId, AppFileType fileType, String objectName) {
        if (objectName == null || objectName.isBlank()) {
            throw new IllegalArgumentException("Nombre del objeto no puede ser nulo o vacío.");
        }
        return buildAppFolderPath(appId, fileType) + objectName.trim();
    }

    private String getDefaultAppRepoBucketPolicy(String bucketName) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode policy = objectMapper.createObjectNode();
        policy.put("Version", "2012-10-17");

        ArrayNode statementArray = objectMapper.createArrayNode();

        ObjectNode statementMediaPublicRead = objectMapper.createObjectNode();
        statementMediaPublicRead.put("Effect", "Allow");
        statementMediaPublicRead.put("Principal", objectMapper.createObjectNode().put("AWS", "*"));
        statementMediaPublicRead.set("Action", objectMapper.createArrayNode().add("s3:GetObject"));
        statementMediaPublicRead.set("Resource", objectMapper.createArrayNode().add(String.format("arn:aws:s3:::%s/*/media/*", bucketName)));
        statementArray.add(statementMediaPublicRead);


        policy.set("Statement", statementArray);

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(policy);
        } catch (JsonProcessingException e) {
            logger.error("Error al generar la política JSON del bucket", e);
            throw new RuntimeException("No se pudo generar la política del bucket.", e);
        }
    }


    @Override
    @PostConstruct
    public void ensureAppRepoBucketExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(mainBucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(mainBucketName).build());
                logger.info("Bucket '{}' creado exitosamente.", mainBucketName);

                String policyJson = getDefaultAppRepoBucketPolicy(mainBucketName);
                logger.debug("Aplicando la siguiente política al bucket '{}':\n{}", mainBucketName, policyJson);
                minioClient.setBucketPolicy(
                        SetBucketPolicyArgs.builder()
                                .bucket(mainBucketName)
                                .config(policyJson)
                                .build());
                logger.info("Política aplicada exitosamente al bucket '{}'.", mainBucketName);
            } else {
                logger.info("Bucket '{}' ya existe.", mainBucketName);
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("No se pudo inicializar el bucket de MinIO y su política: " + mainBucketName, e);
        }
    }

    @Override
    public String getPresignedUrlForObject(String appId, AppFileType fileType, String objectName,
                                           Method method, int expiryInMinutes, String contentType) throws Exception {
        String fullObjectPath = buildObjectPath(appId, fileType, objectName);

        GetPresignedObjectUrlArgs.Builder argsBuilder = GetPresignedObjectUrlArgs.builder()
                .method(method)
                .bucket(mainBucketName)
                .object(fullObjectPath)
                .expiry(expiryInMinutes, TimeUnit.MINUTES);

        if (method == Method.PUT && contentType != null && !contentType.isBlank()) {
            argsBuilder.extraHeaders(Map.of("Content-Type", contentType));
        }

        try {
            String url = minioClient.getPresignedObjectUrl(argsBuilder.build());
            return url;
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new Exception("No se pudo generar la URL prefirmada para el objeto: " + fullObjectPath, e);
        }
    }

    @Override
    public Map<String, String> getPresignedDownloadUrlsForFolder(String appId, AppFileType fileType, int expiryInMinutes) throws Exception {
        String folderPath = buildAppFolderPath(appId, fileType);
        

        Map<String, String> presignedUrls = new HashMap<>();

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(mainBucketName)
                            .prefix(folderPath) // Muy importante para listar solo dentro de la carpeta
                            .recursive(true) 
                            .build());

            for (Result<Item> result : results) {
                Item item = result.get();
                String objectNameInFolder = item.objectName().substring(folderPath.length());

                
                if (objectNameInFolder.isEmpty() || item.isDir()) { 
                    continue;
                }

                // Generar URL prefirmada para este objeto específico
                String presignedUrl = getPresignedUrlForObject(appId, fileType, objectNameInFolder, Method.GET, expiryInMinutes, null);
                presignedUrls.put(objectNameInFolder, presignedUrl);
            }

            return presignedUrls;

        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new Exception("No se pudo generar las URLs prefirmadas para la carpeta: " + folderPath, e);
        }
    }

    @Override
    public Map<AppFileType, List<String>> uploadAppBatchFiles(String appId,
                                                              MultipartFile iconFile,
                                                              List<MultipartFile> imageFiles,
                                                              MultipartFile zipFile) throws Exception {
        Map<AppFileType, List<String>> uploadedObjects = new HashMap<>();
        uploadedObjects.put(AppFileType.MEDIA, new ArrayList<>());
        uploadedObjects.put(AppFileType.FILES, new ArrayList<>());

        if (iconFile != null && !iconFile.isEmpty()) {
            String originalIconExtension = getFileExtension(iconFile.getOriginalFilename());
            String iconObjectName = "icono" + (originalIconExtension.isEmpty() ? "" : "." + originalIconExtension);
            // La ruta completa se construye usando buildObjectPath, que ya antepone appId/fileType/
            String iconFullPath = buildObjectPath(appId, AppFileType.MEDIA, iconObjectName);
            try {
                uploadMultipartFile(iconFullPath, iconFile);
                uploadedObjects.get(AppFileType.MEDIA).add(iconObjectName);
            } catch (Exception e) {
                throw new Exception("Error al subir el icono: " + iconObjectName, e);
            }
        }

        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile imageFile = imageFiles.get(i);
                if (imageFile != null && !imageFile.isEmpty()) {
                    String originalImageExtension = getFileExtension(imageFile.getOriginalFilename());
                    String imageObjectName = "img" + i + (originalImageExtension.isEmpty() ? "" : "." + originalImageExtension);
                    String imageFullPath = buildObjectPath(appId, AppFileType.MEDIA, imageObjectName);
                    try {
                        uploadMultipartFile(imageFullPath, imageFile);
                        uploadedObjects.get(AppFileType.MEDIA).add(imageObjectName);
                        logger.debug("Imagen '{}' subida exitosamente a {}", imageObjectName, imageFullPath);
                    } catch (Exception e) {
                        logger.error("Error al subir la imagen '{}' para appId {}: {}", imageObjectName, appId, e.getMessage(), e);
                        throw new Exception("Error al subir la imagen: " + imageObjectName, e);
                    }
                }
            }
        } 

        if (zipFile != null && !zipFile.isEmpty()) {
            String zipOriginalName = sanitizeFilename(zipFile.getOriginalFilename()); // Usamos el nombre sanitizado
            String zipFullPath = buildObjectPath(appId, AppFileType.FILES, zipOriginalName);
            try {
                uploadMultipartFile(zipFullPath, zipFile);
                uploadedObjects.get(AppFileType.FILES).add(zipOriginalName);
                logger.debug("Archivo ZIP '{}' subido exitosamente a {}", zipOriginalName, zipFullPath);
            } catch (Exception e) {
                logger.error("Error al subir el archivo ZIP '{}' para appId {}: {}", zipOriginalName, appId, e.getMessage(), e);
                throw new Exception("Error al subir el archivo ZIP: " + zipOriginalName, e);
            }
        } 

        return uploadedObjects;
    }

    private void uploadMultipartFile(String fullObjectPath, MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(mainBucketName)
                            .object(fullObjectPath)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            logger.error("Error en MinIO al subir a '{}': {}", fullObjectPath, e.getMessage());
            throw e;
        }
    }

    private String sanitizeFilename(String filename) {
        if (filename == null) {
            return "unknown_file";
        }
        String sanitized = filename.replace("..", "");
        sanitized = sanitized.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
        sanitized = sanitized.replaceAll("^[.\\-_]+", "").replaceAll("[.\\-_]+$", "");
        return sanitized.isEmpty() ? "renamed_file" : sanitized;
    }

    /**
     * Método de ayuda para obtener la extensión de un archivo.
     * @param filename El nombre del archivo.
     * @return La extensión sin el punto, o una cadena vacía si no hay extensión.
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    @Override
    public Map<AppFileType, List<String>> listAllAppObjects(String appId) throws Exception {
        logger.debug("Listando todos los objetos para appId: {}", appId);
        Map<AppFileType, List<String>> appObjects = new HashMap<>();

        // Iterar sobre cada tipo de archivo (FILES y MEDIA)
        for (AppFileType fileType : AppFileType.values()) {
            String folderPathPrefix = buildAppFolderPath(appId, fileType); // e.g., "app123/files/" o "app123/media/"
            List<String> objectNamesInFolder = new ArrayList<>();

            logger.trace("Listando objetos en: Bucket='{}', Prefix='{}'", mainBucketName, folderPathPrefix);

            try {
                Iterable<Result<Item>> results = minioClient.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(mainBucketName)
                                .prefix(folderPathPrefix)
                                .recursive(true)
                                .build());

                for (Result<Item> result : results) {
                    Item item = result.get();
                    String relativeObjectName = item.objectName().substring(folderPathPrefix.length());

                    if (!relativeObjectName.isEmpty() && !item.isDir()) {
                        objectNamesInFolder.add(relativeObjectName);
                    }
                }
                appObjects.put(fileType, objectNamesInFolder);
                logger.debug("Encontrados {} objetos en {} para appId {}", objectNamesInFolder.size(), fileType.getFolderName(), appId);

            } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
                throw new Exception("Error al listar objetos para " + fileType.name() + " de la app " + appId, e);
            }
        }
        return appObjects;
    }

    @Override
    public void deleteObject(String appId, AppFileType fileType, String objectName) throws Exception {
        String fullObjectPath = buildObjectPath(appId, fileType, objectName);

        try {
            boolean exists = objectExists(appId, fileType, objectName); 
            if (!exists) {
                return;
            }

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(mainBucketName)
                            .object(fullObjectPath)
                            .build());
            logger.info("Objeto '{}' eliminado exitosamente.", fullObjectPath);

        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new Exception("No se pudo eliminar el objeto: " + fullObjectPath, e);
        }
    }

	@Override
	public boolean objectExists(String appId, AppFileType fileType, String objectName) throws Exception {
	    String fullObjectPath = buildObjectPath(appId, fileType, objectName);
	    logger.debug("Verificando existencia del objeto: Bucket='{}', Object='{}'", mainBucketName, fullObjectPath);

	    try {
	        // statObject() lanza una excepción si el objeto no existe.
	        // Si no lanza excepción, el objeto existe.
	        minioClient.statObject(
	                StatObjectArgs.builder()
	                        .bucket(mainBucketName)
	                        .object(fullObjectPath)
	                        .build());
	        
	        return true;// El objeto existe
	        
	    } catch (ErrorResponseException e) {
	    	
	        if (e.errorResponse().code().equals("NoSuchKey") || e.errorResponse().code().equals("NoSuchObject")) {
	            return false; // El objeto no existe
	        }
	        throw new Exception("Error al verificar la existencia del objeto: " + fullObjectPath, e);
	        
	    } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
	        throw new Exception("Error al verificar la existencia del objeto: " + fullObjectPath, e);
	    }
	}

	

}
