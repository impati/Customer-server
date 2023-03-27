package com.example.customerserver.service.customer.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProfileManager implements FileManager {

    private final static String DEFAULT = "default.png";
    private final ImageResizer imageResizer;
    @Value("${profile.dir}")
    private String profileDir;

    @Override
    public String getFullPath(String profileName) {
        return profileDir + profileName;
    }

    public String download(String fileUrl) {
        if (!StringUtils.hasText(fileUrl)) return DEFAULT;
        String storeName = createUniqueName() + ImageExtension.extractExtension(fileUrl);
        try (InputStream in = new URL(fileUrl).openStream()) {
            Path imagePath = Paths.get(getFullPath(storeName));
            Files.copy(in, imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageResizer.resizeImageAndSave(storeName);
    }

    private String createUniqueName() {
        return UUID.randomUUID().toString();
    }
}
