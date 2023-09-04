package com.example.customerserver.service.customer.profile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultImageResizer implements ImageResizer {

	private static final int NEW_WIDTH = 250;
	private static final int NEW_HEIGHT = 250;

	@Value("${profile.dir}")
	public String logoDir;

	@Override
	public String resizeImageAndSave(final String imageName) {
		try {
			final BufferedImage inputImage = ImageIO.read(getInputFile(imageName));
			final BufferedImage outputImage = new BufferedImage(NEW_WIDTH, NEW_HEIGHT, inputImage.getType());
			final Graphics2D graphics = outputImage.createGraphics();
			graphics.drawImage(inputImage, 0, 0, NEW_WIDTH, NEW_HEIGHT, null);
			graphics.dispose();
			return saveAndReturnStoreName(outputImage, imageName);
		} catch (IOException e) {
			throw new IllegalStateException("파일 사이즈를 재조정하는데 실패 했습니다.", e);
		}
	}

	private File getInputFile(final String imageName) {
		return new File(getPullPath(imageName));
	}

	private String saveAndReturnStoreName(final BufferedImage outputImage, final String imageName) throws IOException {
		final File outputFile = new File(getPullPath(imageName));
		ImageIO.write(outputImage, ImageExtension.getExtension(imageName), outputFile);
		return imageName;
	}

	private String getPullPath(final String imageName) {
		return logoDir + imageName;
	}
}
