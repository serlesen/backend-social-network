package com.sergio.socialnetwork.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.sergio.socialnetwork.dto.ProfileDto;
import com.sergio.socialnetwork.dto.SignUpDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import com.sergio.socialnetwork.entities.User;
import com.sergio.socialnetwork.mappers.UserMapper;
import com.sergio.socialnetwork.mappers.UserMapperImpl;
import com.sergio.socialnetwork.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @Test
    void testProfileData() {
        // given
        Optional<User> user = Optional.of(User.builder()
                .id(10L)
                .firstName("first")
                .lastName("last")
                .login("login")
                .password("pass")
                .createdDate(LocalDateTime.now())
                .build()
        );
        Mockito.when(userRepository.findById(10L)).thenReturn(user);

        // when
        ProfileDto profile = userService.getProfile(10L);

        // then
        verify(userRepository).findById(10L);
        assertAll(() -> {
            assertEquals("first", profile.getUserDto().getFirstName());
            assertEquals("last", profile.getUserDto().getLastName());
        });
    }

    @Test
    void testAddFriend() {
        // given
        UserDto userDto = new UserDto(1L, "first", "last", "login", "token");
        long friendId = 10L;

        Optional<User> user = Optional.of(User.builder()
                .id(1L)
                .firstName("first")
                .lastName("last")
                .login("login")
                .password("pass")
                .createdDate(LocalDateTime.now())
                .build());
        Mockito.when(userRepository.findById(1L)).thenReturn(user);

        Optional<User> friend = Optional.of(User.builder()
                .id(10L)
                .firstName("first_friend")
                .lastName("last_friend")
                .login("login_friend")
                .password("pass")
                .createdDate(LocalDateTime.now())
                .build());
        Mockito.when(userRepository.findById(10L)).thenReturn(friend);

        // when
        userService.addFriend(userDto, friendId);

        // then
        verify(userRepository).save(argThat(argument -> argument.getFriends().size() == 1));
    }

    @Test
    void testSignUp() {
        // given
        SignUpDto signUpDto = new SignUpDto("first", "last", "login", "pass".toCharArray());

        User user = User.builder()
                .id(10L)
                .firstName("first")
                .lastName("last")
                .login("login")
                .password("pass")
                .createdDate(LocalDateTime.now())
                .build();

        when(userRepository.findByLogin("login"))
                .thenReturn(Optional.empty());
        when(userRepository.save(argThat(argument -> argument.getFirstName().equals(signUpDto.getFirstName())
                && argument.getLastName().equals(signUpDto.getLastName()))))
                .thenReturn(user);
        when(passwordEncoder.encode(any()))
                .thenReturn("encodedPassword");

        // when
        UserDto userDto = userService.signUp(signUpDto);

        // then
        assertAll(() -> {
            assertEquals(signUpDto.getFirstName(), userDto.getFirstName());
            assertEquals(signUpDto.getLastName(), userDto.getLastName());
        });
    }

    @Test
    void testSearchUser() {
        // given
        String term = "term";

        when(userRepository.search("%term%"))
                .thenReturn(Arrays.asList(User.builder()
                        .id(21L)
                        .firstName("first")
                        .lastName("last")
                        .login("login")
                        .password("pass")
                        .createdDate(LocalDateTime.now())
                        .build()));

        // when
        List<UserSummaryDto> users = userService.searchUsers(term);

        // then
        assertAll(() -> {
            assertEquals(1, users.size());
            assertEquals("first", users.get(0).getFirstName());
        });
        verify(userRepository).search("%term%");
    }
}
