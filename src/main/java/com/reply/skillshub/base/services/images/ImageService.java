package com.reply.skillshub.base.services.images;

import lombok.Data;

public class ImageService {

    // public Image loadImage(String path) {
    //     String type = FileTypeDetector.detectFileType(path).orElse("png");
    //     // byte[] pictureData = 


    // }

    @Data
    public class Image {
        String type;
        byte[] pictureDate;
    }   
}

