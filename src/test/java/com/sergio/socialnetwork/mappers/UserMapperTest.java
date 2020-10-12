package com.sergio.socialnetwork.mappers;

import java.time.LocalDateTime;

import com.sergio.socialnetwork.dto.ImageDto;
import com.sergio.socialnetwork.dto.ProfileDto;
import com.sergio.socialnetwork.dto.SignUpDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.entities.Image;
import com.sergio.socialnetwork.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private static UserMapper mapper;

    @BeforeAll
    public static void setUp() {
        mapper = new UserMapperImpl();
    }

    @Test
    void testUserMapper() {
        // given
        User user = User.builder()
                .id(1L)
                .firstName("first")
                .lastName("last")
                .login("login")
                .password("pass")
                .createdDate(LocalDateTime.now())
                .build();

        // when
        UserDto userDto = mapper.toUserDto(user);

        // then
        assertAll(
                () -> {
                    assertEquals(user.getFirstName(), userDto.getFirstName());
                    assertEquals(user.getLastName(), userDto.getLastName());
                }
        );
    }

    @Test
    void testMapProfile() {
        // given
        User user = User.builder().id(1L)
                .firstName("first")
                .lastName("last")
                .login("login")
                .password("pass")
                .createdDate(LocalDateTime.now())
                .build();

        // when
        ProfileDto profile = mapper.userToProfileDto(user);

        // then
        assertAll(() -> {
            assertEquals(user.getFirstName(), profile.getUserDto().getFirstName());
            assertEquals(user.getLastName(), profile.getUserDto().getLastName());
        });
    }

    @Test
    void testMapSignUp() {
        // given
        SignUpDto signUpDto = SignUpDto.builder()
                .firstName("first")
                .lastName("last")
                .login("login")
                .password("pass".toCharArray())
                .build();

        // when
        User user = mapper.signUpToUser(signUpDto);

        // then
        assertAll(() -> {
            assertEquals(signUpDto.getFirstName(), user.getFirstName());
            assertEquals(signUpDto.getLastName(), user.getLastName());
            assertEquals(signUpDto.getLogin(), user.getLogin());
        });
    }

    @Test
    void testMapImage() {
        // given
        Image image = Image.builder()
                .id(1L)
                .title("title")
                .path("path")
                .createdDate(LocalDateTime.now())
                .build();

        // when
        ImageDto imageDto = mapper.imageToImageDto(image);

        // then
        assertAll(() -> {
            assertEquals(image.getTitle(), imageDto.getTitle());
            assertEquals(image.getPath(), imageDto.getPath());
        });
    }
}
