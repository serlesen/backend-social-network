package com.sergio.socialnetwork.dto;

import java.util.List;

public class ProfileDto {

    private UserSummaryDto userDto;
    private List<UserSummaryDto> friends;
    private List<MessageDto> messages;
    private List<ImageDto> images;

    public ProfileDto() {
        super();
    }

    public ProfileDto(UserSummaryDto userDto, List<UserSummaryDto> friends, List<MessageDto> messages, List<ImageDto> images) {
        this.userDto = userDto;
        this.friends = friends;
        this.messages = messages;
        this.images = images;
    }

    public UserSummaryDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserSummaryDto userDto) {
        this.userDto = userDto;
    }

    public List<UserSummaryDto> getFriends() {
        return friends;
    }

    public void setFriends(List<UserSummaryDto> friends) {
        this.friends = friends;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }

    public List<ImageDto> getImages() {
        return images;
    }

    public void setImages(List<ImageDto> images) {
        this.images = images;
    }
}
