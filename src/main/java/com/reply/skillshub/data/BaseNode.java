package com.reply.skillshub.data;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.reply.skillshub.data.user.User;

import lombok.Data;

@Data
public class BaseNode {

    @LastModifiedDate
    Date lastModifiedDate;

    @LastModifiedBy
    User lastModifiedBy;

    @CreatedBy
    User createdBy;

    @CreatedDate
    Date createdDate;
}
