package com.Jas.shop_backend.api.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final Cloudinary cloudinary;

    public ImageController(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

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

            // Upload directly to Cloudinary folder "productImages/name"
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "productImages/" + safeName
                    )
            );

            // Get secure HTTPS URL returned by Cloudinary
            String url = uploadResult.get("secure_url").toString();
            return ResponseEntity.ok(url);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to Cloudinary", e);
        }
    }
}