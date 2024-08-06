package com.reply.skillshub.base.exceptionhandling;

import java.net.BindException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.reply.skillshub.openapi.model.Error;
import com.reply.skillshub.openapi.model.ErrorDetail;

import jakarta.validation.constraints.NotNull;

import com.reply.skillshub.base.exceptionhandling.exeptions.BaseException;
import com.reply.skillshub.base.exceptionhandling.exeptions.ErrorCode;
import com.reply.skillshub.base.exceptionhandling.exeptions.ObjectDetail;

@RestControllerAdvice
public class SkillhubExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SkillhubExceptionHandler.class);
    
    @ExceptionHandler({BaseException.class})
    public ResponseEntity<Object> handleDefaultBaseExceptions(BaseException e, WebRequest webRequest) {
        LOG.debug("Exception sent:", e);
        var errorData = new Error().errorCode(e.getErrorCode().toString()).message(e.getMessage()).details(e.getDetails().stream().map(this::convertDetailToErrorDetail).toList());
        return new ResponseEntity<>(errorData, new HttpHeaders(), determineHttpStatus(e));
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errorData = new Error().errorCode(ErrorCode.API_VALIDATION_ERROR.toString());
        errorData.setMessage("Invalid Request Content");
        for (var error: ex.getFieldErrors()) {
            ErrorDetail detail = new ErrorDetail();
            detail.setDetail(error.getDefaultMessage());
            detail.setTopic(error.getObjectName() + ": " + error.getField());
            errorData.addDetailsItem(detail);
        }
        return new ResponseEntity<>(errorData, new HttpHeaders(), HttpStatus.ACCEPTED);
	}


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleDefaultBaseExceptions(Exception e, WebRequest webRequest) {
        LOG.debug("Exception sent:", e);
        var errorData = new Error().errorCode(ErrorCode.DEFAULT.name()).message(e.getMessage());
        return new ResponseEntity<>(errorData, new HttpHeaders(), determineHttpStatus(e));
    }



    private HttpStatus determineHttpStatus(Exception e) {
        var responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);
        return responseStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : responseStatus.value();
    }

    private ErrorDetail convertDetailToErrorDetail(ObjectDetail objectDetail) {
        return new ErrorDetail().detail(objectDetail.getDetail()).topic(objectDetail.getTopic());
    }
    
}
