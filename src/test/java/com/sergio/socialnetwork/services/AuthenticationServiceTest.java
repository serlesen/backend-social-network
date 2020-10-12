package com.sergio.socialnetwork.services;

import java.util.Optional;

import com.sergio.socialnetwork.dto.CredentialsDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.entities.User;
import com.sergio.socialnetwork.mappers.UserMapper;
import com.sergio.socialnetwork.mappers.UserMapperImpl;
import com.sergio.socialnetwork.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testAuthentication() {
        // given
        CredentialsDto credentialsDto = CredentialsDto.builder()
                .login("login")
                .password("password".toCharArray())
                .build();

        User user = User.builder()
                .firstName("first")
                .lastName("last")
                .password("password")
                .build();

        when(userRepository.findByLogin(credentialsDto.getLogin()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // when
        UserDto userDto = authenticationService.authenticate(credentialsDto);

        // then
        assertAll(() -> {
            assertEquals(user.getFirstName(), userDto.getFirstName());
            assertEquals(user.getLastName(), userDto.getLastName());
        });
        verify(userMapper).toUserDto(any());
        verify(passwordEncoder).matches(any(), any());
    }

    @Test
    void testFindByLogin() {
        // given
        String login = "login";

        User user = User.builder()
                .firstName("first")
                .lastName("last")
                .build();

        when(userRepository.findByLogin(login))
                .thenReturn(Optional.of(user));

        // when
        UserDto userDto = authenticationService.findByLogin(login);

        // then
        assertAll(() -> {
            assertEquals(user.getFirstName(), userDto.getFirstName());
            assertEquals(user.getLastName(), userDto.getLastName());
        });
    }
}
