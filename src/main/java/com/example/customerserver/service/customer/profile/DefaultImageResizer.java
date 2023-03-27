package com.example.customerserver.service.customer.profile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class DefaultImageResizer implements ImageResizer {
    private final int NEW_WIDTH;
    private final int NEW_HEIGHT;

    @Value("${profile.dir}")
    public String logoDir;

    public DefaultImageResizer() {
        NEW_WIDTH = 250;
        NEW_HEIGHT = 250;
    }

    @Override
    public String resizeImageAndSave(String imageName) {
        try {
            BufferedImage inputImage = ImageIO.read(getInputFile(imageName));
            BufferedImage outputImage = new BufferedImage(NEW_WIDTH, NEW_HEIGHT, inputImage.getType());
            Graphics2D graphics = outputImage.createGraphics();
            graphics.drawImage(inputImage, 0, 0, NEW_WIDTH, NEW_HEIGHT, null);
            graphics.dispose();
            return saveAndReturnStoreName(outputImage, imageName);
        } catch (IOException e) {
            throw new IllegalStateException("파일 사이즈를 재조정하는데 실패 했습니다.", e);
        }
    }

    private File getInputFile(String imageName) {
        return new File(getPullPath(imageName));
    }

    private String saveAndReturnStoreName(BufferedImage outputImage, String imageName) throws IOException {
        File outputFile = new File(getPullPath(imageName));
        ImageIO.write(outputImage, ImageExtension.getExtension(imageName), outputFile);
        return imageName;
    }

    private String getPullPath(String imageName) {
        return logoDir + imageName;
    }
}
