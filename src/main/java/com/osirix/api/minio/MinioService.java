package com.osirix.api.minio;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import io.minio.http.Method;

/**
 * Service interface for interacting with a MinIO object storage server.
 * This service handles operations such as ensuring bucket existence, generating presigned URLs,
 * uploading files, listing objects, and deleting objects, specifically tailored for application assets.
 *
 * @author Alonso Marrero Bello
 */
public interface MinioService {

    /**
     * Ensures that the main 'app-repo' bucket exists in MinIO.
     * If it doesn't exist, this method should attempt to create it.
     */
    void ensureAppRepoBucketExists();

    /**
     * Generates a presigned URL for a specific operation (GET or PUT) on an object within an application's storage.
     *
     * @param appId           The application ID, used to namespace objects (e.g., as a top-level folder).
     * @param fileType        The type of folder within the application's storage (e.g., FILES for binaries, MEDIA for images).
     * @param objectName      The name of the object in MinIO (e.g., "document.pdf", "icon.png").
     * @param method          The HTTP method for which the URL is signed (e.g., {@link Method#GET}, {@link Method#PUT}).
     * @param expiryInMinutes The validity period of the URL in minutes.
     * @param contentType     Optional (primarily for PUT operations): The Content-Type header the client must use when
     *                        uploading the object. If null or empty for PUT, MinIO might not validate this header
     *                        during the upload, or it might default based on the object name.
     * @return The generated presigned URL as a String.
     * @throws Exception If an error occurs while generating the URL (e.g., MinIO client error, invalid parameters).
     */
    String getPresignedUrlForObject(String appId, AppFileType fileType, String objectName, Method method, int expiryInMinutes, String contentType) throws Exception;

    /**
     * Generates presigned DOWNLOAD URLs for all existing objects within a specific folder ('files' or 'media')
     * of an application.
     *
     * @param appId           The application ID.
     * @param fileType        The specific folder type (FILES or MEDIA) for which to list and generate URLs.
     * @param expiryInMinutes The validity period of the generated URLs in minutes.
     * @return A map where the key is the object name and the value is its presigned download URL.
     * @throws Exception If an error occurs during the process (e.g., listing objects, generating URLs).
     */
    Map<String, String> getPresignedDownloadUrlsForFolder(String appId, AppFileType fileType, int expiryInMinutes) throws Exception;

    /**
     * Uploads a batch of files for an application: an icon, several images to the 'media' folder,
     * and a ZIP file to the 'files' folder. This operation is typically performed directly from the server.
     *
     * @param appId         The application ID.
     * @param iconFile      The icon file (as a {@link MultipartFile}). It will be saved with its original name in the appId/media/ path.
     * @param imageFiles    A list of image files (List of {@link MultipartFile}). They will be saved with their original names in the appId/media/ path.
     * @param zipFile       The ZIP archive file (as a {@link MultipartFile}). It will be saved with its original name in the appId/files/ path.
     * @return A map with {@link AppFileType} as the key and a list of the names of the successfully uploaded objects as the value.
     * @throws Exception If an error occurs during the upload of any of the files.
     */
    Map<AppFileType, List<String>> uploadAppBatchFiles(String appId, MultipartFile iconFile, List<MultipartFile> imageFiles, MultipartFile zipFile) throws Exception;

    /**
     * Lists all objects (names only) within the 'files' and 'media' folders of a specific application.
     *
     * @param appId The application ID.
     * @return A map where the key is {@link AppFileType} (FILES or MEDIA) and the value is a list of object names
     *         found in the respective subfolder for the given application.
     * @throws Exception If an error occurs while listing the objects.
     */
    Map<AppFileType, List<String>> listAllAppObjects(String appId) throws Exception;


    /**
     * Deletes a specific object from a designated folder ('files' or 'media') of an application.
     *
     * @param appId      The application ID.
     * @param fileType   The type of folder (FILES or MEDIA) from which the object will be deleted.
     * @param objectName The name of the object to delete.
     * @throws Exception If an error occurs during the deletion process.
     */
    void deleteObject(String appId, AppFileType fileType, String objectName) throws Exception;

    /**
     * Checks if a specific object exists within a designated folder of an application.
     *
     * @param appId      The application ID.
     * @param fileType   The type of folder (FILES or MEDIA) where the object is expected.
     * @param objectName The name of the object to check for existence.
     * @return {@code true} if the object exists, {@code false} otherwise.
     * @throws Exception If an error occurs during the verification process (other than the object simply not being found,
     *                   which should return false).
     */
    boolean objectExists(String appId, AppFileType fileType, String objectName) throws Exception;

}