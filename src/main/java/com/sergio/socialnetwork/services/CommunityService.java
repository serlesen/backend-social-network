package com.sergio.socialnetwork.services;

import com.sergio.socialnetwork.dto.ImageDto;
import com.sergio.socialnetwork.dto.MessageDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import com.sergio.socialnetwork.entities.MongoMessage;
import com.sergio.socialnetwork.entities.MongoUser;
import com.sergio.socialnetwork.repositories.MessageRepository;
import com.sergio.socialnetwork.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommunityService {

    private static final int PAGE_SIZE = 10;

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public CommunityService(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public List<MessageDto> getCommunityMessages(UserDto userDto, int page) {
        MongoUser user = getUser(userDto);

        List<String> ids = new ArrayList<>();
        ids.add(user.getId());
        user.getFriends().stream().forEach(friend -> ids.add(friend.getId()));

        Iterable<MongoMessage> messages = messageRepository.findAllByUserIdIn(ids, PageRequest.of(page, PAGE_SIZE));

        List<MessageDto> messageDtoList = new ArrayList<>();
        messages.forEach(message -> messageDtoList.add(new MessageDto(
                message.getId(),
                message.getContent(),
                new UserSummaryDto(message.getUser().getId(), message.getUser().getFirstName(), message.getUser().getLastName()),
                message.getCreatedDate())));

        return messageDtoList;
    }

    public List<ImageDto> getCommunityImages(int page) {
        return Arrays.asList(new ImageDto(1L, "First title", null),
                new ImageDto(2L, "Second title", null));
    }

    public MessageDto postMessage(UserDto userDto, MessageDto messageDto) {
        MongoUser user = getUser(userDto);

        MongoMessage message = new MongoMessage(
                messageDto.getContent(),
                user.buildShareableUser(),
                LocalDateTime.now());

        MongoMessage savedMessage = messageRepository.save(message);

        return new MessageDto(savedMessage.getId(),
                savedMessage.getContent(),
                new UserSummaryDto(savedMessage.getUser().getId(), savedMessage.getUser().getFirstName(), savedMessage.getUser().getLastName()),
                savedMessage.getCreatedDate());
    }

    public ImageDto postImage(MultipartFile file, String title) {
        return new ImageDto(3L, "New Title", null);
    }

    private MongoUser getUser(UserDto userDto) {
        return userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
