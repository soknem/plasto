package com.plasto.api.feature.file;

import com.plasto.api.feature.file.dto.FileResponse;
import com.plasto.api.feature.file.dto.FileViewResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * File interface which contains methods to manage file upload,view,delete
 *
 * @author Pov soknem
 * @since 1.0 (2024)
 */
public interface FileService {

    /**
     * Uploads a single media file.
     *
     * @param file is the file to upload
     * @return {@link FileResponse}
     * @author Pov soknem
     * @since 1.0 (2024)
     */
    FileResponse uploadSingleFile(MultipartFile file);

    /**
     * get all file metadata and url
     *
     * @return {@link List<FileResponse>}
     * @author Pov soknem
     * @since 1.0 (2024)
     */
    List<FileResponse> loadAllFiles();

    /**
     * get file by filename
     *
     * @param fileName is the file name to get
     * @return {@link FileResponse}
     * @author Pov soknem
     * @since 1.0 (2024)
     */

    FileResponse loadFileByName(String fileName);

    /**
     * delete file by file name
     *
     * @param fileName is the filename to delete
     * @author Pov soknem
     * @since 1.0 (2024)
     */

    void deleteFileByName(String fileName);

    /**
     * download file by file name
     *
     * @param fileName is the file name to download
     * @return {@link  Resource}
     * @author Pov soknem
     * @since 1.0 (2024)
     */
    Resource downloadFileByName(String fileName);

    /**
     * view file by file name
     *
     * @param fileName the name of the file to check for existence
     * @return {@link FileViewResponse}
     * @throws ErrorResponseException          if an error response is received from the server
     * @throws IOException                     if an I/O error occurs
     * @throws NoSuchAlgorithmException        if the specified algorithm is not available
     * @throws InvalidKeyException             if the key is invalid
     * @author Pov soknem
     * @since 1.0 (2024)
     */
    FileViewResponse viewFileByFileName(String fileName) throws
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException;


}
