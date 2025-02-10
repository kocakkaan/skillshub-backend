package com.reply.skillshub.data;

import org.thymeleaf.context.Context;
import lombok.Data;

@Data
public class EmailRequest {

    	
	String subject;
	String template;
	String recipient;
	Context context;
    
}
