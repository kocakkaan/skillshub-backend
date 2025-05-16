package com.reply.skillshub.base.services.images;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.reply.skillshub.base.exceptionhandling.exeptions.ImageProcessingException;

public class CropImageService {

    /*
     * Scales an image to fit target dimensions and then crops the center
     * to the exact target width and height.
     * width and height are in pixels
     */
    public static byte[] cropAndScaleImage(InputStream originalImageStream, int targetWidth, int targetHeight,
            String formatName) {
        try {
            BufferedImage originalBufferedImage = ImageIO.read(originalImageStream);
            if (originalBufferedImage == null) {
                throw new ImageProcessingException(
                        "Could not read image from input stream. The stream might be empty, corrupted or represent an unsupported image format.");
            }
            BufferedImage processedImage = performCropAndScale(originalBufferedImage, targetWidth, targetHeight);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processedImage, formatName, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new ImageProcessingException("An I/O error occurred during image processing: " + e.getMessage());
        } finally {

        }
    }

    private static BufferedImage performCropAndScale(BufferedImage originalImage, int targetWidth, int targetHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        double originalAspectRatio = (double) originalWidth / originalHeight;
        double targetAspectRatio = (double) targetWidth / targetHeight;

        int scaledWidth;
        int scaledHeight;

        // Scale image to fill target dimensions while preserving aspect ratio.
        // Ensures scaled image can be cropped to target size.
        if (originalAspectRatio > targetAspectRatio) {
            // Image is wider - scale by height.
            scaledHeight = targetHeight;
            scaledWidth = (int) (scaledHeight * originalAspectRatio);
        } else {
            // Image is taller or equal - scale by width.
            scaledWidth = targetWidth;
            scaledHeight = (int) (scaledWidth / originalAspectRatio);
        }

        // Preserve image type: default if unknown (e.g. some PNGs)
        int imageType = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        // Create a temporary BufferedImage with the new scaled dimensions
        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // Center-crop coordinates
        int cropX = (scaledWidth - targetWidth) / 2;
        int cropY = (scaledHeight - targetHeight) / 2;

        // Prevent out-of-bounds crop for small or rounded images
        int finalCropWidth = targetWidth;
        int finalCropHeight = targetHeight;

        if (cropX < 0) {
            // Scaled width < target; adjust crop
            finalCropWidth = scaledWidth;
            cropX = 0;
        } else if (cropX + targetWidth > scaledWidth) {
            // Crop exceeds right edge
            finalCropWidth = scaledWidth - cropX;
        }

        if (cropY < 0) {
            // Scaled height < target; adjust crop
            finalCropHeight = scaledHeight;
            cropY = 0;
        } else if (cropY + targetHeight > scaledHeight) {
            // Crop exceeds bottom edge
            finalCropHeight = scaledHeight - cropY;
        }

        // Sanity check: ensure crop dimensions are not negative or zero
        if (finalCropWidth <= 0 || finalCropHeight <= 0) {
            throw new ImageProcessingException(
                    "Cannot crop to target dimensions; image is too small after scaling or target dimensions are invalid. Scaled dimensions: "
                            + scaledWidth + "x" + scaledHeight + ", Target: " + targetWidth + "x" + targetHeight);
        }

        // Crop the scaled image
        BufferedImage croppedImage = scaledImage.getSubimage(cropX, cropY, finalCropWidth, finalCropHeight);

        return croppedImage;
    }
}
