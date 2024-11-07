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
    public static byte[] resizeImage(InputStream originalImage, int targetWidth, int targetHeight) {
        try {
            BufferedImage originalBufferedImage = ImageIO.read(originalImage);
            BufferedImage resizedBufferedImage = resizeImage(originalBufferedImage, targetWidth, targetHeight);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedBufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
  
        // Calculate the aspect ratio
        double aspectRatio = (double) originalWidth / originalHeight;
  
        // Calculate the new dimensions while maintaining the aspect ratio
        int newWidth;
        int newHeight;
        if ((targetWidth / aspectRatio) <= targetHeight) {
            newWidth = targetWidth;
            newHeight = (int) (targetWidth / aspectRatio);
        } else {
            newHeight = targetHeight;
            newWidth = (int) (targetHeight * aspectRatio);
        }
  
        // Create a new BufferedImage with the calculated dimensions
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
  
        // Draw the original image onto the new BufferedImage while scaling it
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
  
        return resizedImage;
    }
    
}
