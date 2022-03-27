package com.sergio.socialnetwork.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.sergio.socialnetwork.dto.ImageDto;
import com.sergio.socialnetwork.dto.MessageDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CommunityService {

    public List<MessageDto> getCommunityMessages(UserDto user, int page) {
        return Arrays.asList(new MessageDto(1L,
                        "First message " + user.getFirstName(),
                        new UserSummaryDto(10L, "Sergio", "Lema"),
                        LocalDateTime.parse("2020-11-21T13:31:33")
                ),
                new MessageDto(2L,
                        "Second message " + user.getFirstName(),
                        new UserSummaryDto(11L, "John", "Smith"),
                        LocalDateTime.parse("2020-11-22T14:21:34")
                        )
        );
    }

    public List<ImageDto> getCommunityImages(int page) {
        return Arrays.asList(new ImageDto(1L, "First title", null),
                new ImageDto(2L, "Second title", null));
    }

    public MessageDto postMessage(MessageDto messageDto) {
        return new MessageDto(3L,
                "New message",
                new UserSummaryDto(10L, "Sergio", "Lema"),
                LocalDateTime.now());
    }

    public ImageDto postImage(MultipartFile file, String title) {
        return new ImageDto(3L, "New Title", null);
    }
}
