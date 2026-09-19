package com.Jas.shop_backend.api.controller;

import com.Jas.shop_backend.Service.ImageKitStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/images")
public class ImageController {

    private static final Set<String> ALLOWED_FOLDERS = Set.of("products", "categories");

    private final ImageKitStorageService imageKitStorageService;

    public ImageController(ImageKitStorageService imageKitStorageService) {
        this.imageKitStorageService = imageKitStorageService;
    }

    /**
     * Upload an image to ImageKit via the backend (private key stays server-side).
     *
     * Resulting ImageKit path:
     *   - with name:    /{folder}/{safe-name}/{uuid}.ext  e.g. /products/red-shirt/abc.jpg
     *   - without name: /{folder}/{uuid}.ext              e.g. /categories/abc.jpg
     *
     * @param file   the image file
     * @param folder target folder — must be "products" or "categories"
     * @param name   optional product/category name used as a subfolder
     * @return the public ImageKit CDN URL of the uploaded image
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("folder") String folder,
            @RequestParam(value = "name", required = false, defaultValue = "") String name
    ) {
        if (!ALLOWED_FOLDERS.contains(folder)) {
            return ResponseEntity.badRequest()
                    .body("Invalid folder. Allowed values: " + ALLOWED_FOLDERS);
        }

        try {
            // Build subfolder: products/red-shirt  or just  products
            String targetFolder = folder;
            if (name != null && !name.isBlank()) {
                String safeName = name.toLowerCase().replaceAll("[^a-z0-9-_]", "-");
                targetFolder = folder + "/" + safeName;
            }

            String publicUrl = imageKitStorageService.uploadImage(file, targetFolder);
            return ResponseEntity.ok(publicUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to ImageKit", e);
        }
    }
}