package com.example.customerserver.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.customerserver.service.customer.profile.ProfileManager;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class ResourceController {

	private final ProfileManager profileManager;

	@GetMapping(value = "/profile/{filename}")
	public ResponseEntity<Resource> findLogo(@PathVariable final String filename) throws IOException {
		final String inputFile = profileManager.getFullPath(filename);
		final Path path = new File(inputFile).toPath();
		final FileSystemResource resource = new FileSystemResource(path);
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(Files.probeContentType(path)))
			.body(resource);
	}
}
