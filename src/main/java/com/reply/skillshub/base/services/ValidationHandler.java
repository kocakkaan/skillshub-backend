package com.reply.skillshub.base.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reply.skillshub.base.exceptionhandling.exeptions.ObjectDetail;
import com.reply.skillshub.base.exceptionhandling.exeptions.ValidationException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidationHandler<T> {

    private Logger LOG = LoggerFactory.getLogger(ValidationHandler.class);

    private final Validator validator;

    public ValidationHandler() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void validate(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            
            List<ObjectDetail> details = new ArrayList<>();

            violations.forEach(violation -> {
                ObjectDetail objectDetail = new ObjectDetail();
                objectDetail.setTopic(violation.getPropertyPath().toString());
                objectDetail.setDetail(violation.getMessage());
                details.add(objectDetail);
            });
            
            LOG.error("validation failed", violations);
            
            throw new ValidationException(details);
        }
    }
    
}
