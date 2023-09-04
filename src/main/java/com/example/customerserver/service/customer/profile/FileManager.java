package com.example.customerserver.service.customer.profile;

public interface FileManager {

	String getFullPath(final String profileName);

	String download(final String fileUrl);
}
