package com.reply.skillshub.base.services.images;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public class FileTypeDetector {

    public static Optional<String> detectFileType(String filePath) {
        if (filePath == null) {
            return Optional.empty();
        }
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] fileSignature = new byte[4];
            int bytesRead = fis.read(fileSignature);

            if (bytesRead < 4) {
                throw new IOException("Unable to read enough bytes to determine file type.");
            }

            if (isPdf(fileSignature)) {
                return Optional.of("PDF");
            } else if (isJpeg(fileSignature)) {
                return Optional.of("JPEG");
            } else if (isPng(fileSignature)) {
                return Optional.of("PNG");
            } else if (isGif(fileSignature)) {
                return Optional.of("GIF");
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            System.out.println(e.getStackTrace()); // TODO: decide on how to handle this
            return Optional.empty();

        }
    }

    private static boolean isPdf(byte[] fileSignature) {
        return fileSignature[0] == 0x25 && fileSignature[1] == 0x50 &&
               fileSignature[2] == 0x44 && fileSignature[3] == 0x46;
    }

    private static boolean isJpeg(byte[] fileSignature) {
        return fileSignature[0] == (byte) 0xFF && fileSignature[1] == (byte) 0xD8 &&
               fileSignature[2] == (byte) 0xFF;
    }

    private static boolean isPng(byte[] fileSignature) {
        return fileSignature[0] == (byte) 0x89 && fileSignature[1] == 0x50 &&
               fileSignature[2] == 0x4E && fileSignature[3] == 0x47;
    }

    private static boolean isGif(byte[] fileSignature) {
        return fileSignature[0] == 0x47 && fileSignature[1] == 0x49 &&
               fileSignature[2] == 0x46 && fileSignature[3] == 0x38;
    }
    
}
