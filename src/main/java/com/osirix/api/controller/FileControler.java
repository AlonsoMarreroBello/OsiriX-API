package com.osirix.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.osirix.api.minio.AppFileType;
import com.osirix.api.minio.MinioServiceImpl;

import io.minio.http.Method;

@RestController
@RequestMapping("/api/v1") 
public class FileControler {
	
	private static final Logger logger = LoggerFactory.getLogger(FileControler.class);
	
	@Autowired
	MinioServiceImpl minioService;

	@GetMapping("/app/{appId}/download/{fileName}")
	public ResponseEntity<?> getDownloadUrl(
	        @PathVariable String appId,
	        @PathVariable String fileName) {
	    try {
	        String presignedUrl = minioService.getPresignedUrlForObject(
	            appId,
	            AppFileType.FILES, // o MEDIA si es una imagen, depende del uso
	            fileName,
	            Method.GET,
	            5, // minutos válidos
	            "GET"
	        );
	        return ResponseEntity.ok(Map.of("url", presignedUrl));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Map.of("error", "No se pudo generar la URL de descarga."));
	    }
	}
	@GetMapping("/app/getall")
	//@PreAuthorize("authentication.principal.user instanceof T(com.osirix.api.entity.User)")
	public Map<String, String> getMethodNamea() {
		try {
			return minioService.getPresignedDownloadUrlsForFolder("1", AppFileType.FILES, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
	
	@PostMapping(value = "/batch-upload/{appId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("@appSecurity.canManageAppAssets(authentication, #appId)")
    public ResponseEntity<Map<String, Object>> uploadAppAssetsBatch(
            @PathVariable String appId,
            @RequestPart(value = "iconFile", required = true) MultipartFile iconFile,
            @RequestPart(value = "imageFiles", required = true) List<MultipartFile> imageFiles,
            @RequestPart(value = "zipFile", required = true) MultipartFile zipFile) {

        if (iconFile.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El archivo de icono no puede estar vacío."));
        }

        if (CollectionUtils.isEmpty(imageFiles) || imageFiles.stream().anyMatch(MultipartFile::isEmpty)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Se debe proporcionar al menos una imagen válida y ninguna imagen puede estar vacía."));
        }


        if (zipFile.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El archivo ZIP no puede estar vacío."));
        }

        try {
            List<MultipartFile> validImageFiles = imageFiles.stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .collect(Collectors.toList());

            if (validImageFiles.isEmpty() && !CollectionUtils.isEmpty(imageFiles)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Todas las imágenes proporcionadas están vacías."));
            }


            Map<AppFileType, List<String>> uploadedFiles = minioService.uploadAppBatchFiles(
                    appId,
                    iconFile,
                    validImageFiles, 
                    zipFile
            );

            Map<String, Object> response = Map.of(
                    "message", "Archivos subidos exitosamente para la app: " + appId,
                    "uploadedFiles", uploadedFiles
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Ocurrió un error interno al procesar la subida de archivos.",
                                              "details", e.getMessage()));
        }
    }
	
	@GetMapping("/list-all")
	public ResponseEntity<?> listAllApplicationObjects(@PathVariable String appId) {
	    logger.info("Solicitud para listar todos los objetos de la app: {}", appId);
	    try {
	        Map<AppFileType, List<String>> allObjects = minioService.listAllAppObjects(appId);

	        if (allObjects.values().stream().allMatch(List::isEmpty)) {
	            return ResponseEntity.noContent().build();
	        }

	        return ResponseEntity.ok(allObjects);
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Map.of("error", "Error interno al listar los objetos de la aplicación."));
	    }
	}
	
	@DeleteMapping("/{fileTypeString}/{objectName}")
	@PreAuthorize("@appSecurity.canManageAppAssets(authentication, #appId)")
	public ResponseEntity<Map<String, String>> deleteApplicationObject(
	        @PathVariable String appId,
	        @PathVariable String fileTypeString, // "files" o "media"
	        @PathVariable String objectName) {

	    AppFileType fileType;
	    try {
	        fileType = AppFileType.fromString(fileTypeString);
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(Map.of("error", "Tipo de archivo inválido: " + fileTypeString + ". Use 'files' o 'media'."));
	    }

	    try {
	        if (!minioService.objectExists(appId, fileType, objectName)) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "El archivo a eliminar no existe."));
	        }

	        minioService.deleteObject(appId, fileType, objectName);
	        return ResponseEntity.ok(Map.of("message", "Archivo '" + objectName + "' eliminado exitosamente de la carpeta " + fileType.getFolderName() + "."));

	    } catch (IllegalArgumentException e) { 
	        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Map.of("error", "Error interno al eliminar el archivo."));
	    }
	}
	

}
