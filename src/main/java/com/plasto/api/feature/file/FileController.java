package com.plasto.api.feature.file;


import com.plasto.api.feature.file.dto.FileResponse;
import com.plasto.api.feature.file.dto.FileViewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;


    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasAnyAuthority('file:write')")
    @PostMapping(value = "", consumes = "multipart/form-data")
    FileResponse uploadFile(@RequestPart MultipartFile file) {

        return fileService.uploadSingleFile(file);
    }


    @GetMapping()
//    @PreAuthorize("hasAnyAuthority('file:read')")
    List<FileResponse> loadAllFile() {
        return fileService.loadAllFiles();
    }


    @GetMapping("/{fileName}")
//    @PreAuthorize("hasAnyAuthority('file:read')")
    FileResponse loadFileByName(@PathVariable String fileName) {
        return fileService.loadFileByName(fileName);
    }


    //    @PreAuthorize("hasAnyAuthority('file:delete')")
    @DeleteMapping("/{fileName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteFileByName(@PathVariable String fileName) {
        fileService.deleteFileByName(fileName);
    }

    // produces = Accept
    // consumes = Content-Type
    @GetMapping(path = "/{fileName}/download",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<?> downloadFileByName(@PathVariable String fileName) {

        System.out.println("Start download");

        Resource resource = fileService.downloadFileByName(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping(value = "/view/{fileName}")
    public ResponseEntity<InputStreamResource> viewByFileName(@PathVariable String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        FileViewResponse file = fileService.viewFileByFileName(fileName);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .contentLength(file.fileSize())
                .body(file.stream());
    }

}
