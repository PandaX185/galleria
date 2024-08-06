package com.panda.galleria.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ImageUtil {
    @Value("${server.port}")
    private String SERVER_PORT;

    public String uploadImage(MultipartFile photo) {
        try {
            String UPLOAD_DIR = "uploads/";
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String uniqueFilename = UUID.randomUUID() + "_" + photo.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + uniqueFilename);
            Files.write(filePath, photo.getBytes());
            return "http://localhost:" + SERVER_PORT + "/" + UPLOAD_DIR + uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload photo", e);
        }
    }
}
