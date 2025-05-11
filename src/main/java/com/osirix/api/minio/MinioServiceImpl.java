package com.osirix.api.minio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.Result;
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
    
    // Método de ayuda para construir la ruta base de la app (appId/fileType/)
    private String buildAppFolderPath(String appId, AppFileType fileType) {
        if (appId == null || appId.isBlank()) {
            throw new IllegalArgumentException("App ID no puede ser nulo o vacío.");
        }
        if (fileType == null) {
            throw new IllegalArgumentException("AppFileType no puede ser nulo.");
        }
        // Asegura que termine con / para que actúe como un prefijo de carpeta
        return String.format("%s/%s/", appId.trim(), fileType.getFolderName());
    }

    // Método de ayuda para construir la ruta completa del objeto
    private String buildObjectPath(String appId, AppFileType fileType, String objectName) {
        if (objectName == null || objectName.isBlank()) {
            throw new IllegalArgumentException("Nombre del objeto no puede ser nulo o vacío.");
        }
        return buildAppFolderPath(appId, fileType) + objectName.trim();
    }

    @Override
    @PostConstruct // Para que se ejecute al iniciar la aplicación
    public void ensureAppRepoBucketExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(mainBucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(mainBucketName).build());
                logger.info("Bucket '{}' creado exitosamente.", mainBucketName);
            } else {
                logger.info("Bucket '{}' ya existe.", mainBucketName);
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            logger.error("Error al verificar o crear el bucket '{}': {}", mainBucketName, e.getMessage(), e);
            throw new RuntimeException("No se pudo inicializar el bucket de MinIO: " + mainBucketName, e);
        }
    }

    @Override
    public String getPresignedUrlForObject(String appId, AppFileType fileType, String objectName,
                                           Method method, int expiryInMinutes, String contentType) throws Exception {
        String fullObjectPath = buildObjectPath(appId, fileType, objectName);
        logger.debug("Generando URL prefirmada: Method={}, Bucket={}, Object={}, Expiry={}min, ContentType={}",
                method, mainBucketName, fullObjectPath, expiryInMinutes, contentType);

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
            logger.info("URL prefirmada generada para {}: {}", fullObjectPath, url.substring(0, url.indexOf("?"))); 
            return url;
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            logger.error("Error al generar URL prefirmada para '{}' en bucket '{}': {}",
                    fullObjectPath, mainBucketName, e.getMessage(), e);
            throw new Exception("No se pudo generar la URL prefirmada para el objeto: " + fullObjectPath, e);
        }
    }

    @Override
    public Map<String, String> getPresignedDownloadUrlsForFolder(String appId, AppFileType fileType, int expiryInMinutes) throws Exception {
        String folderPath = buildAppFolderPath(appId, fileType); // e.g., "app123/files/"
        logger.debug("Generando URLs prefirmadas de descarga para la carpeta: Bucket={}, Path={}, Expiry={}min",
                mainBucketName, folderPath, expiryInMinutes);

        Map<String, String> presignedUrls = new HashMap<>();

        try {
            // Listar objetos en la carpeta especificada
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
                logger.trace("URL generada para {}: {}", objectNameInFolder, presignedUrl.substring(0, presignedUrl.indexOf("?")));
            }

            logger.info("Se generaron {} URLs prefirmadas para la carpeta {}", presignedUrls.size(), folderPath);
            return presignedUrls;

        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            logger.error("Error al generar URLs prefirmadas para la carpeta '{}' en bucket '{}': {}",
                    folderPath, mainBucketName, e.getMessage(), e);
            throw new Exception("No se pudo generar las URLs prefirmadas para la carpeta: " + folderPath, e);
        }
    }

	@Override
	public Map<AppFileType, List<String>> uploadAppBatchFiles(String appId, MultipartFile iconFile,
			List<MultipartFile> imageFiles, MultipartFile zipFile) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<AppFileType, List<String>> listAllAppObjects(String appId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteObject(String appId, AppFileType fileType, String objectName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean objectExists(String appId, AppFileType fileType, String objectName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	

}
