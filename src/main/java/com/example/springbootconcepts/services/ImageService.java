package com.example.springbootconcepts.services;

import com.example.springbootconcepts.dto.ImageInfo;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service
public class ImageService {
    private final Path uploadDir = Path.of("uploads");


    public ImageService() throws IOException {
        Files.createDirectories(uploadDir);
    }


    public ImageInfo store(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Empty file");
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image file");
        }
        if (file.getSize() > 5L * 1024 * 1024) throw new IllegalArgumentException("File too large");


        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String generated = UUID.randomUUID().toString() + (ext != null ? ("." + ext) : "");
        Path target = uploadDir.resolve(generated);
        try (var in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }


                String url = "/uploads/" + generated; // simple static serving path; see config
        return new ImageInfo(generated, file.getSize(), url);
    }


}