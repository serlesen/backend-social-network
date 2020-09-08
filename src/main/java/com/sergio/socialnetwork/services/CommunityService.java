package com.sergio.socialnetwork.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sergio.socialnetwork.dto.ImageDto;
import com.sergio.socialnetwork.dto.MessageDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.entities.Image;
import com.sergio.socialnetwork.entities.Message;
import com.sergio.socialnetwork.entities.User;
import com.sergio.socialnetwork.exceptions.AppException;
import com.sergio.socialnetwork.mappers.MessageMapper;
import com.sergio.socialnetwork.mappers.UserMapper;
import com.sergio.socialnetwork.repositories.ImageRepository;
import com.sergio.socialnetwork.repositories.MessageRepository;
import com.sergio.socialnetwork.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class CommunityService {

    @Value("${app.nfs.path:/tmp}")
    private String nfsPath;

    private static final int PAGE_SIZE = 10;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    private final MessageMapper messageMapper;

    private final UserMapper userMapper;

    public List<MessageDto> getCommunityMessages(UserDto userDto, int page) {
        User user = getUser(userDto);

        List<Long> friendIds = Optional.of(user.getFriends())
                .map(friends -> friends.stream().map(User::getId).collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        friendIds.add(user.getId());

        List<Message> messages = messageRepository.findCommunityMessages(friendIds, PageRequest.of(page, PAGE_SIZE));

        return messageMapper.messagesToMessageDtos(messages);
    }

    public List<ImageDto> getCommunityImages(UserDto userDto, int page) {
        User user = getUser(userDto);

        List<Long> friendIds = Optional.of(user.getFriends())
                .map(friends -> friends.stream().map(User::getId).collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        friendIds.add(user.getId());

        List<Image> images = imageRepository.findCommunityImages(friendIds,
                PageRequest.of(page, PAGE_SIZE));
        return userMapper.imagesToImageDtos(images);
    }

    public MessageDto postMessage(UserDto userDto, MessageDto messageDto) {
        User user = getUser(userDto);

        Message message = messageMapper.messageDtoToMessage(messageDto);
        message.setUser(user);

        if (user.getMessages() == null) {
            user.setMessages(new ArrayList<>());
        }
        user.getMessages().add(message);

        Message savedMessage = messageRepository.save(message);

        return messageMapper.messageToMessageDto(savedMessage);
    }

    public ImageDto postImage(UserDto userDto, MultipartFile file, String title) throws IOException {
        User user = getUser(userDto);

        String path = this.nfsPath
                + "/" + user.getId()
                + "/" + UUID.randomUUID()
                + "-" + file.getOriginalFilename();

        Image image = Image.builder()
                .title(title)
                .user(user)
                .path(path)
                .build();

        File storedImage = new File(path);
        if (!storedImage.getParentFile().exists()) {
            storedImage.getParentFile().mkdir();
        }
        storedImage.createNewFile();
        file.transferTo(storedImage);

        if (user.getImages() == null) {
            user.setImages(new ArrayList<>());
        }
        user.getImages().add(image);

        userRepository.save(user);

        return userMapper.imageToImageDto(image);
    }

    private User getUser(UserDto userDto) {
        return userRepository.findById(userDto.getId())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }
}
