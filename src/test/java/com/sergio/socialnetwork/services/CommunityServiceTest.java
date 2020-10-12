package com.sergio.socialnetwork.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.sergio.socialnetwork.dto.ImageDto;
import com.sergio.socialnetwork.dto.MessageDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import com.sergio.socialnetwork.entities.Image;
import com.sergio.socialnetwork.entities.Message;
import com.sergio.socialnetwork.entities.User;
import com.sergio.socialnetwork.mappers.MessageMapper;
import com.sergio.socialnetwork.mappers.MessageMapperImpl;
import com.sergio.socialnetwork.mappers.UserMapper;
import com.sergio.socialnetwork.mappers.UserMapperImpl;
import com.sergio.socialnetwork.repositories.ImageRepository;
import com.sergio.socialnetwork.repositories.MessageRepository;
import com.sergio.socialnetwork.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommunityServiceTest {

    @InjectMocks
    private CommunityService communityService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ImageRepository imageRepository;

    @Spy
    private MessageMapper messageMapper = new MessageMapperImpl();

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(communityService, "nfsPath", "/tmp");
        ReflectionTestUtils.setField(messageMapper, "userMapper", userMapper);
    }

    @Test
    void testGetCommunityMessages() {
        // given
        UserDto userDto = new UserDto(10L, "first", "last", "login", "token");

        List<Message> message = Arrays.asList(
                Message.builder().id(1L).content("title1").createdDate(LocalDateTime.now().minus(5, ChronoUnit.SECONDS)).build(),
                Message.builder().id(2L).content("title2").createdDate(LocalDateTime.now()).build());

        User friend = User.builder()
                .id(21L)
                .firstName("first_friend")
                .lastName("last_friend")
                .login("login_friend")
                .password("pass")
                .messages(message)
                .createdDate(LocalDateTime.now())
                .build();
        Optional<User> user = Optional.of(User.builder()
                .id(10L)
                .firstName("first")
                .lastName("last")
                .login("login")
                .password("pass")
                .friends(Collections.singletonList(friend))
                .createdDate(LocalDateTime.now())
                .build());
        Mockito.when(userRepository.findById(10L)).thenReturn(user);
        Mockito.when(messageRepository.findCommunityMessages(Arrays.asList(21L, 10L), PageRequest.of(1, 10)))
                .thenReturn(message);

        // when
        List<MessageDto> messageDtos = communityService.getCommunityMessages(userDto, 1);

        // then
        assertAll(() -> {
            assertEquals(message.size(), messageDtos.size());
            assertEquals(message.get(0).getContent(), messageDtos.get(0).getContent());
            assertEquals(message.get(1).getContent(), messageDtos.get(1).getContent());
        });
    }

    @Test
    void testGetCommunityImages() {
        // given
        UserDto userDto = UserDto.builder().id(10L).firstName("first").lastName("last").token("token").build();

        List<Image> images = Arrays.asList(
                Image.builder().id(1L).title("title1").createdDate(LocalDateTime.now().minus(5, ChronoUnit.SECONDS)).build(),
                Image.builder().id(2L).title("title2").createdDate(LocalDateTime.now()).build());

        User friend = User.builder()
                .id(21L)
                .firstName("first_friend")
                .lastName("last_friend")
                .login("login_friend")
                .password("pass")
                .images(images)
                .createdDate(LocalDateTime.now())
                .build();
        Optional<User> user = Optional.of(User.builder()
                .id(10L)
                .firstName("first")
                .lastName("last")
                .login("login")
                .password("pass")
                .friends(Collections.singletonList(friend))
                .createdDate(LocalDateTime.now())
                .build());
        Mockito.when(userRepository.findById(10L)).thenReturn(user);
        Mockito.when(imageRepository.findCommunityImages(Arrays.asList(21L, 10L), PageRequest.of(1, 10)))
                .thenReturn(images);

        // when
        List<ImageDto> imageDtos = communityService.getCommunityImages(userDto, 1);

        // then
        assertAll(() -> {
            assertEquals(images.size(), imageDtos.size());
            assertEquals(images.get(0).getTitle(), imageDtos.get(0).getTitle());
            assertEquals(images.get(1).getTitle(), imageDtos.get(1).getTitle());
        });
    }

    @Test
    void testPostMessage() {
        // given
        UserDto userDto = UserDto.builder().id(10L).firstName("first").lastName("last").token("token").build();
        MessageDto messageDto = MessageDto.builder()
                .content("content")
                .userDto(new UserSummaryDto(10L, "first", "last"))
                .createdDate(LocalDateTime.now())
                .build();

        User user = User.builder()
                .id(10L)
                .firstName("first")
                .lastName("last")
                .login("login")
                .password("pass")
                .createdDate(LocalDateTime.now())
                .build();

        Optional<User> oUser = Optional.of(user);
        Mockito.when(userRepository.findById(10L)).thenReturn(oUser);

        when(messageRepository.save(any()))
                .thenReturn(new Message(1L, "content", user, LocalDateTime.now()));

        // when
        MessageDto newMessage = communityService.postMessage(userDto, messageDto);

        // then
        assertEquals(messageDto.getContent(), newMessage.getContent());
        assertEquals(messageDto.getUserDto().getId(), newMessage.getUserDto().getId());
        verify(messageRepository).save(any());
    }

    @Test
    void testPostImage() throws IOException {
        // given
        UserDto userDto = UserDto.builder().id(10L).firstName("first").lastName("last").token("token").build();

        Optional<User> user = Optional.of(User.builder()
                .id(10L)
                .firstName("first")
                .lastName("last")
                .login("login")
                .password("pass")
                .createdDate(LocalDateTime.now())
                .build());
        Mockito.when(userRepository.findById(10L)).thenReturn(user);

        // when
        ImageDto image = communityService.postImage(userDto, mock(MultipartFile.class), "title");

        // then
        verify(userRepository).save(argThat(argument -> argument.getImages().size() == 1));
        assertEquals("title", image.getTitle());
    }
}
