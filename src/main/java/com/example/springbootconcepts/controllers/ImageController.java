package com.example.springbootconcepts.controllers;

import com.example.springbootconcepts.dto.ImageInfo;
import com.example.springbootconcepts.exceptionHandlers.exceptions.ErrorResponse;
import com.example.springbootconcepts.services.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final Logger log = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;
    private final Path uploadDir;


    public ImageController(ImageService imageService,
                           @Value("${app.upload-dir:uploads}") String uploadDir) {
        this.imageService = imageService;
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            log.error("Failed to create upload directory: {}", this.uploadDir, e);
            throw new RuntimeException("Could not initialize upload directory", e);
        }
    }

    @PostMapping
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file) {
        try {
            ImageInfo info = imageService.store(file);
            return ResponseEntity.ok(info);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body(new ErrorResponse("Server error"));
        }
    }
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable(name = "filename") String fileName) {
        try {
            Path file = uploadDir.resolve(fileName).normalize().toAbsolutePath();

            // prevent path traversal
            if (!file.startsWith(uploadDir.toAbsolutePath())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            if (!Files.exists(file) || !Files.isReadable(file)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(file.toUri());
            String contentType = Files.probeContentType(file);
            MediaType mediaType = contentType != null
                    ? MediaType.parseMediaType(contentType)
                    : MediaType.APPLICATION_OCTET_STREAM;

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, String>>> listImages() {
        try (Stream<Path> stream = Files.list(uploadDir)) {
            List<Map<String, String>> images = stream
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(name -> {
                        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/api/images/")
                                .path(name)
                                .toUriString();
                        Map<String, String> entry = new HashMap<>();
                        entry.put("filename", name);
                        entry.put("url", url);
                        return entry;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(images);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
