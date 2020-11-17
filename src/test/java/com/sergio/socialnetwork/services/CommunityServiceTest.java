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
import org.junit.jupiter.api.BeforeAll;
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
import uk.co.jemos.podam.api.DefaultClassInfoStrategy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

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

    private static PodamFactory podamFactory;

    @BeforeAll
    public static void setUpAll() {
        podamFactory = new PodamFactoryImpl();

        // to avoid infinite loops creating random data
        DefaultClassInfoStrategy classInfoStrategy = DefaultClassInfoStrategy.getInstance();
        classInfoStrategy.addExcludedField(Image.class, "user");
        classInfoStrategy.addExcludedField(Message.class, "user");

        podamFactory.setClassStrategy(classInfoStrategy);
    }

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(communityService, "nfsPath", "/tmp");
        ReflectionTestUtils.setField(messageMapper, "userMapper", userMapper);
    }

    @Test
    void testGetCommunityMessages() {
        // given
        UserDto userDto = podamFactory.manufacturePojo(UserDto.class);

        List<Message> messages = Arrays.asList(
                Message.builder().id(1L).content("title1").createdDate(LocalDateTime.now().minus(5, ChronoUnit.SECONDS)).build(),
                Message.builder().id(2L).content("title2").createdDate(LocalDateTime.now()).build());

        User friend = podamFactory.manufacturePojo(User.class);
        friend.setMessages(messages);
        Optional<User> user = Optional.of(podamFactory.manufacturePojo(User.class));
        user.get().setFriends(Collections.singletonList(friend));

        Mockito.when(userRepository.findById(userDto.getId())).thenReturn(user);
        Mockito.when(messageRepository.findCommunityMessages(Arrays.asList(friend.getId(), user.get().getId()), PageRequest.of(1, 10)))
                .thenReturn(messages);

        // when
        List<MessageDto> messageDtos = communityService.getCommunityMessages(userDto, 1);

        // then
        assertAll(() -> {
            assertEquals(messages.size(), messageDtos.size());
            assertEquals(messages.get(0).getContent(), messageDtos.get(0).getContent());
            assertEquals(messages.get(1).getContent(), messageDtos.get(1).getContent());
        });
    }

    @Test
    void testGetCommunityImages() {
        // given
        UserDto userDto = podamFactory.manufacturePojo(UserDto.class);

        List<Image> images = Arrays.asList(
                Image.builder().id(1L).title("title1").createdDate(LocalDateTime.now().minus(5, ChronoUnit.SECONDS)).build(),
                Image.builder().id(2L).title("title2").createdDate(LocalDateTime.now()).build());

        User friend = podamFactory.manufacturePojo(User.class);
        friend.setImages(images);
        Optional<User> user = Optional.of(podamFactory.manufacturePojo(User.class));
        user.get().setFriends(Collections.singletonList(friend));
        Mockito.when(userRepository.findById(userDto.getId())).thenReturn(user);
        Mockito.when(imageRepository.findCommunityImages(Arrays.asList(friend.getId(), user.get().getId()), PageRequest.of(1, 10)))
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
        UserDto userDto = podamFactory.manufacturePojo(UserDto.class);
        MessageDto messageDto = podamFactory.manufacturePojo(MessageDto.class);

        User user = podamFactory.manufacturePojo(User.class);

        Optional<User> oUser = Optional.of(user);
        Mockito.when(userRepository.findById(userDto.getId())).thenReturn(oUser);

        Message message = podamFactory.manufacturePojo(Message.class);
        when(messageRepository.save(any()))
                .thenReturn(message);

        // when
        MessageDto newMessage = communityService.postMessage(userDto, messageDto);

        // then
        assertEquals(message.getContent(), newMessage.getContent());
        verify(messageRepository).save(any());
    }

    @Test
    void testPostImage() throws IOException {
        // given
        UserDto userDto = podamFactory.manufacturePojo(UserDto.class);

        Optional<User> user = Optional.of(podamFactory.manufacturePojo(User.class));
        user.get().setImages(null);
        Mockito.when(userRepository.findById(userDto.getId())).thenReturn(user);

        // when
        ImageDto image = communityService.postImage(userDto, mock(MultipartFile.class), "title");

        // then
        verify(userRepository).save(argThat(argument -> argument.getImages().size() == 1));
        assertEquals("title", image.getTitle());
    }
}
