package com.aws.lambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;


public class Hello implements RequestHandler<Map<String, Object>, String> 
{
	private final ObjectMapper objectMapper = new ObjectMapper();

    public String handleRequest(Map<String, Object> event, Context context) {
        String name = (String) event.get("name");
        String city = (String) event.get("city");

        String message = "Hello " + name + " from " + city;
        return message;
    }
	
}
	