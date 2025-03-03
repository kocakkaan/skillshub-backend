package com.reply.skillshub.base.services.images;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ResizeImageService {
    

    /*
     * Resize an image to a target width and height
     * width and height are in pixels
     */
    public static byte[] resizeImage(InputStream originalImage, int targetWidth, int targetHeight, String formatName) {
        try {
            BufferedImage originalBufferedImage = ImageIO.read(originalImage);
            BufferedImage resizedBufferedImage = resizeImage(originalBufferedImage, targetWidth, targetHeight);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedBufferedImage, formatName, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            
        }
    }

    private static BufferedImage resizeImage(BufferedImage newImage, int targetWidth, int targetHeight) {
        int newImageWidth = newImage.getWidth();
        int newImageHeight = newImage.getHeight();
  
        // Calculate the aspect ratio
        double newImageAspectRatio = (double) newImageWidth / newImageHeight;
        double targetAspectRatio = (double) targetWidth / targetHeight;
  
        // Calculate the new dimensions while maintaining the aspect ratio
        int scaledWidth;
        int scaledHeight;
        if (newImageAspectRatio > targetAspectRatio) {
            scaledWidth = targetWidth;
            scaledHeight = (int) (targetWidth / newImageAspectRatio);
        } else {
            scaledHeight = targetHeight;
            scaledWidth = (int) (targetHeight * newImageAspectRatio);
        }
  
        // Create a new BufferedImage with the calculated dimensions
        BufferedImage resizedImage = new BufferedImage(scaledWidth, scaledHeight, newImage.getType());
  
        // Draw the original image onto the new BufferedImage while scaling it
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(newImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
  
        return resizedImage;
    }
    
}
