package com.example.customerserver.service.customer.profile;

import java.util.Arrays;

public enum ImageExtension {

	PNG("png"),
	BMP("bmp"),
	RLE("rle"),
	DIB("dib"),
	JPEG("jpeg"),
	JPG("jpg"),
	GIF("gif"),
	TIF("tif"),
	TIFF("tiff"),
	RAW("raw");

	private final String name;

	ImageExtension(final String name) {
		this.name = name;
	}

	public static String extractExtension(final String fileName) {
		return Arrays.stream(ImageExtension.values())
			.filter(imageExtension -> fileName.toLowerCase().contains(imageExtension.name))
			.map(imageExtension -> "." + imageExtension.name)
			.reduce((first, second) -> second).orElseThrow(IllegalStateException::new);
	}

	public static String getExtension(final String fileName) {
		return Arrays.stream(ImageExtension.values())
			.filter(imageExtension -> fileName.toLowerCase().contains(imageExtension.name))
			.map(imageExtension -> imageExtension.name)
			.reduce((first, second) -> second).orElseThrow(IllegalStateException::new);
	}
}
