package com.example.emailsender.service;

import com.example.emailsender.dto.UserDto;
import com.example.emailsender.model.User;

public interface UserService {
    User saveUser(UserDto userDto);
    Boolean verifyToken(String token);
}
