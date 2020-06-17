package com.sergio.socialnetwork.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {

    private UserSummaryDto userDto;
    private List<UserSummaryDto> friends;
    private List<MessageDto> messages;
    private List<ImageDto> images;

}
