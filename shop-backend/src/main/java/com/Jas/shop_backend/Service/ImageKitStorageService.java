package com.Jas.shop_backend.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageKitStorageService {

    @Value("${imagekit.private-key}")
    private String privateKey;

    private static final String UPLOAD_URL = "https://upload.imagekit.io/api/v1/files/upload";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Uploads an image to ImageKit and returns its public URL.
     *
     * Files are stored at:
     *   /products/{product-name}/{uuid}.ext
     *   /categories/{category-name}/{uuid}.ext
     *
     * @param file   the image file to upload
     * @param folder the target folder path inside ImageKit (e.g. "products/red-shirt")
     * @return the public CDN URL of the uploaded image
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        // Generate a unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".jpg";
        String uniqueFilename = UUID.randomUUID() + extension;

        // ImageKit uses HTTP Basic Auth: privateKey as username, empty password
        String credentials = privateKey + ":";
        String auth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", auth);

        // Build multipart form body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // Wrap bytes in a named ByteArrayResource so Spring sets the correct Content-Disposition
        byte[] fileBytes = file.getBytes();
        ByteArrayResource fileResource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return uniqueFilename;
            }
        };

        body.add("file", fileResource);
        body.add("fileName", uniqueFilename);
        body.add("folder", "/" + folder);   // e.g. /products/red-shirt

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(UPLOAD_URL, requestEntity, Map.class);

        Map<?, ?> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("url")) {
            throw new RuntimeException("ImageKit upload failed: unexpected response - " + responseBody);
        }

        return (String) responseBody.get("url");
    }
}
