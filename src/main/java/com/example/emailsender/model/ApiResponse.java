package com.example.emailsender.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ApiResponse {
    protected String timestamp;
    protected HttpStatus status;
    protected String message;
    protected Map<?, ?> data;
 }
