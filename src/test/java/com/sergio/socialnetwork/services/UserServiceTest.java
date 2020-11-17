package com.sergio.socialnetwork.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.sergio.socialnetwork.dto.ProfileDto;
import com.sergio.socialnetwork.dto.SignUpDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import com.sergio.socialnetwork.entities.Image;
import com.sergio.socialnetwork.entities.Message;
import com.sergio.socialnetwork.entities.User;
import com.sergio.socialnetwork.mappers.UserMapper;
import com.sergio.socialnetwork.mappers.UserMapperImpl;
import com.sergio.socialnetwork.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import uk.co.jemos.podam.api.DefaultClassInfoStrategy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

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

    private static PodamFactory podamFactory;

    @BeforeAll
    public static void setUp() {
         podamFactory = new PodamFactoryImpl();

        // to avoid infinite loops creating random data
        DefaultClassInfoStrategy classInfoStrategy = DefaultClassInfoStrategy.getInstance();
        classInfoStrategy.addExcludedField(Image.class, "user");
        classInfoStrategy.addExcludedField(Message.class, "user");

        podamFactory.setClassStrategy(classInfoStrategy);
    }

    @Test
    void testProfileData() {
        // given
        Optional<User> user = Optional.of(podamFactory.manufacturePojo(User.class));
        Mockito.when(userRepository.findById(10L)).thenReturn(user);

        // when
        ProfileDto profile = userService.getProfile(10L);

        // then
        verify(userRepository).findById(10L);
        assertAll(() -> {
            assertEquals(user.get().getFirstName(), profile.getUserDto().getFirstName());
            assertEquals(user.get().getLastName(), profile.getUserDto().getLastName());
        });
    }

    @Test
    void testAddFriend() {
        // given
        UserDto userDto = podamFactory.manufacturePojo(UserDto.class);

        Optional<User> user = Optional.of(podamFactory.manufacturePojo(User.class));
        user.get().setFriends(null);
        Mockito.when(userRepository.findById(userDto.getId())).thenReturn(user);

        Optional<User> friend = Optional.of(podamFactory.manufacturePojo(User.class));
        Mockito.when(userRepository.findById(friend.get().getId())).thenReturn(friend);

        // when
        userService.addFriend(userDto, friend.get().getId());

        // then
        verify(userRepository).save(argThat(argument -> argument.getFriends().size() == 1));
    }

    @Test
    void testSignUp() {
        // given
        SignUpDto signUpDto = podamFactory.manufacturePojo(SignUpDto.class);
        User user = podamFactory.manufacturePojo(User.class);

        when(userRepository.findByLogin(signUpDto.getLogin()))
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
            assertEquals(user.getFirstName(), userDto.getFirstName());
            assertEquals(user.getLastName(), userDto.getLastName());
        });
    }

    @Test
    void testSearchUser() {
        // given
        String term = "term";
        User user = podamFactory.manufacturePojo(User.class);

        when(userRepository.search("%term%"))
                .thenReturn(Arrays.asList(user));

        // when
        List<UserSummaryDto> users = userService.searchUsers(term);

        // then
        assertAll(() -> {
            assertEquals(1, users.size());
            assertEquals(user.getFirstName(), users.get(0).getFirstName());
        });
        verify(userRepository).search("%term%");
    }
}
