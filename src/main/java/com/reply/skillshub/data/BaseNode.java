package com.reply.skillshub.data;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.reply.skillshub.data.person.Person;

public class BaseNode {

    @LastModifiedDate
    Date lastModifiedDate;

    @LastModifiedBy
    Person lastModifiedBy;

    @CreatedBy
    Person createdBy;

    @CreatedDate
    Date createdDate;
}
