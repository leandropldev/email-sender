package com.example.emailsender.controller;

import com.example.emailsender.dto.UserDto;
import com.example.emailsender.model.ApiResponse;
import com.example.emailsender.model.User;
import com.example.emailsender.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserDto userDto) {
        User user = userService.saveUser(userDto);
        return ResponseEntity.created(URI.create("")).body(
            ApiResponse.builder()
                .status(HttpStatus.CREATED)
                .message("User created with success!")
                .timestamp(LocalDateTime.now().toString())
                .data(Map.of("user", user))
                .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> confirmUserAccount(@RequestParam("token") String token) {
        Boolean isSuccess = userService.verifyToken(token);
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .status(HttpStatus.OK)
                        .message("User validated with success!")
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of("success", isSuccess))
                        .build()
        );
    }
}
