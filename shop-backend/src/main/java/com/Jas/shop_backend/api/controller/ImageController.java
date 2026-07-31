package com.Jas.shop_backend.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    // ⬇️ CHANGED root folder
    private final Path root = Paths.get("productImages");

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("name") String name
    ) {
        try {
            // sanitize product name
            String safeName = name
                    .toLowerCase()
                    .replaceAll("[^a-z0-9-_]", "-");

            // productImages/name/
            Path productDir = root.resolve(safeName);
            Files.createDirectories(productDir);

            String filename = UUID.randomUUID() + getExtension(file);
            Path target = productDir.resolve(filename);

            Files.copy(file.getInputStream(), target);

            // public URL
            String url = "/productImages/" + safeName + "/" + filename;
            return ResponseEntity.ok(url);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // helper
    private String getExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        return (name != null && name.contains("."))
                ? name.substring(name.lastIndexOf("."))
                : ".jpg";
    }
}