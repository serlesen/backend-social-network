package com.sergio.socialnetwork.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {

    private Long id;
    private String title;
    private String path;
    private UserSummaryDto userDto;
    private LocalDateTime createdDate;

}
