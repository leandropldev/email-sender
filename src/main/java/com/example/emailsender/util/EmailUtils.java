package com.example.emailsender.util;

public class EmailUtils {

    public static final String URI_VALIDATE_TOKEN = "/api/v1/users?token=";

    public static String getMessage(String name, String host, String token){
        return new StringBuilder("Hello " + name + ", \n\n")
            .append("Your new account has been created! \n")
            .append("In order to validate, please access the link below: \n")
            .append(getLinkToValidate(host, token) + "\n\n")
            .append("Best regards, \n")
            .append("Support Team")
            .toString();
    }

    public static String getLinkToValidate(String host, String token){
        return host + URI_VALIDATE_TOKEN + token;
    }
}
