package com.sergio.socialnetwork.services;

import java.nio.CharBuffer;
import java.util.List;

import com.sergio.socialnetwork.dto.CredentialsDto;
import com.sergio.socialnetwork.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto authenticate(CredentialsDto credentialsDto) {
        String encodedMasterPassword = passwordEncoder.encode(CharBuffer.wrap("the-password"));
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), encodedMasterPassword)) {
            return this.findByLogin(credentialsDto.getLogin());
        }
        throw new RuntimeException("Invalid password");
    }

    public UserDto findByLogin(String login) {
        if ("sergio".equals(login)) {
            return new UserDto(1L, "Sergio", "Lema", "sergio", "token", List.of("ROLE_VIEWER", "ROLE_EDITOR"));
        }
        if ("john".equals(login)) {
            return new UserDto(1L, "John", "Doe", "john", "token", List.of("ROLE_VIEWER"));
        }
        throw new RuntimeException("Invalid login");
    }
}
