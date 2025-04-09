package com.plasto.api.feature.file;

import com.plasto.api.domain.File;
import com.plasto.api.feature.file.dto.FileResponse;
import com.plasto.api.feature.file.dto.FileViewResponse;
import com.plasto.api.init.MediaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Value("${media.base-uri}")
    private String baseUri;

    @Value("${media.image-end-point}")
    private String imageEndpoint;

    @Value("${media.storage-path}")
    private  String ROOT_FOLDER;

    @Override
    public FileResponse uploadSingleFile(MultipartFile file) {
        String folderName = getValidFolder(file);
        String extension = MediaUtil.extractExtension(Objects.requireNonNull(file.getOriginalFilename()));

        String newName;
        do {
            newName = UUID.randomUUID().toString();
        } while (fileRepository.existsByFileName(newName + "." + extension));

        String fileName = newName + "." + extension;
        Path folderPath = Paths.get(ROOT_FOLDER, folderName);

        try {
            Files.createDirectories(folderPath);
            Path filePath = folderPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed", e);
        }

        File fileObject = new File();
        fileObject.setFileName(fileName);
        fileObject.setFileSize(file.getSize());
        fileObject.setContentType(file.getContentType());
        fileObject.setFolder(folderName);
        fileObject.setExtension(extension);
        fileRepository.save(fileObject);

        return FileResponse.builder()
                .name(fileName)
                .contentType(file.getContentType())
                .extension(extension)
                .size(file.getSize())
                .uri(baseUri + imageEndpoint + "/view/" + fileName)
                .build();
    }

    @Override
    public List<FileResponse> loadAllFiles() {
        List<File> files = fileRepository.findAll();
        List<FileResponse> responses = new ArrayList<>();
        for (File file : files) {
            FileResponse response = FileResponse.builder()
                    .name(file.getFileName())
                    .contentType(file.getContentType())
                    .extension(file.getExtension())
                    .size(file.getFileSize())
                    .uri(baseUri + imageEndpoint + "/view/" + file.getFileName())
                    .build();
            responses.add(response);
        }
        return responses;
    }

    @Override
    public FileResponse loadFileByName(String fileName) {
        try {
            String contentType = getContentType(fileName);
            String extension = MediaUtil.extractExtension(fileName);

            return FileResponse.builder()
                    .name(fileName)
                    .contentType(contentType)
                    .extension(extension)
                    .uri(baseUri + imageEndpoint + "/view/" + fileName)
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteFileByName(String fileName) {
        File file = fileRepository.findByFileName(fileName).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        Path filePath = Paths.get(ROOT_FOLDER, file.getFolder(), file.getFileName());

        try {
            Files.deleteIfExists(filePath);
            fileRepository.delete(file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file", e);
        }
    }

    @Override
    public Resource downloadFileByName(String fileName) {
        File file = fileRepository.findByFileName(fileName).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        Path filePath = Paths.get(ROOT_FOLDER, file.getFolder(), file.getFileName());

        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid file path", e);
        }
    }

    @Override
    public FileViewResponse viewFileByFileName(String fileName) {
        File file = fileRepository.findByFileName(fileName).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        Path filePath = Paths.get(ROOT_FOLDER, file.getFolder(), file.getFileName());

        try {
            InputStream inputStream = Files.newInputStream(filePath);
            InputStreamResource resource = new InputStreamResource(inputStream);

            return FileViewResponse.builder()
                    .fileName(file.getFileName())
                    .fileSize(file.getFileSize())
                    .contentType(file.getContentType())
                    .stream(resource)
                    .build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not read file", e);
        }
    }

    private String getContentType(String fileName) {
        File fileObject = fileRepository.findByFileName(fileName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
        return fileObject.getContentType();
    }

    private static String getValidFolder(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.startsWith("video/") || contentType.startsWith("image/") || contentType.equals("application/pdf"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file type.");
        }
        return contentType.split("/")[0];
    }
}
