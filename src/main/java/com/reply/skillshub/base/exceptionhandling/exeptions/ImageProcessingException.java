package com.reply.skillshub.base.exceptionhandling.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImageProcessingException extends BaseException {

    public ImageProcessingException() {
        super(ErrorCode.IMAGE_PROCESSING_ERROR, "Something went wrong when processing the image");
    }

    public ImageProcessingException(String message) {
        super(ErrorCode.IMAGE_PROCESSING_ERROR, message);
    }
}
