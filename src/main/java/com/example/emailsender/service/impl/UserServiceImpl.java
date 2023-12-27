package com.example.emailsender.service.impl;

import com.example.emailsender.dto.UserDto;
import com.example.emailsender.model.Confirmation;
import com.example.emailsender.model.User;
import com.example.emailsender.repository.ConfirmationRepository;
import com.example.emailsender.repository.UserRepository;
import com.example.emailsender.service.EmailService;
import com.example.emailsender.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    @Override
    public User saveUser(UserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail()))
            throw new RuntimeException("This user email already exists!");

        User user = userRepository.save(
            User.builder()
                .isEnable(false)
                .email(userDto.getEmail())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .build()
        );

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

        //emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());
        //emailService.sendMimeMessageWithAttachment(user.getName(), user.getEmail(), confirmation.getToken());
        //emailService.sendMimeMessageWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
        emailService.sendHtmlEmailWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());

        return user;
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token!"));

        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        user.setEnable(true);
        userRepository.save(user);

        return Boolean.TRUE;
    }
}
