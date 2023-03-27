package com.example.customerserver.service.customer.profile;

public interface FileManager {
    String getFullPath(String profileName);

    String download(String fileUrl);
}
