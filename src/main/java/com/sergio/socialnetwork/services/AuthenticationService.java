package com.sergio.socialnetwork.services;

import com.sergio.socialnetwork.dto.CredentialsDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.entities.MongoUser;
import com.sergio.socialnetwork.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserDto authenticate(CredentialsDto credentialsDto) {
        MongoUser user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {

            return new UserDto(user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getLogin());
        }
        throw new RuntimeException("Invalid password");
    }

    public UserDto findByLogin(String login) {
        MongoUser user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        return new UserDto(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getLogin());
    }
}
