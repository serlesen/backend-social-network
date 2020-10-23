package com.sergio.socialnetwork.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sergio.socialnetwork.dto.ImageDto;
import com.sergio.socialnetwork.dto.MessageDto;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import com.sergio.socialnetwork.services.CommunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommunityControllerTest {

    public MockMvc mockMvc;

    @Autowired
    public CommunityController communityController;

    @MockBean
    public CommunityService communityService;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(communityController).build();

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
    }

    @Test
    void testGetMessages() throws Exception {
        // given
        List<MessageDto> messages = Arrays.asList(MessageDto.builder()
                .content("msg")
                .userDto(new UserSummaryDto())
                .createdDate(LocalDateTime.now())
                .build());
        when(communityService.getCommunityMessages(any(), anyInt()))
                .thenReturn(messages);

        // when then
        mockMvc.perform(get("/v1/community/messages"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].content", is("msg")));
    }

    @Test
    void testGetImages() throws Exception {
        // given
        List<ImageDto> images = Arrays.asList(ImageDto.builder()
                .title("title")
                .userDto(new UserSummaryDto())
                .createdDate(LocalDateTime.now())
                .build());
        when(communityService.getCommunityImages(any(), anyInt()))
                .thenReturn(images);

        // when then
        mockMvc.perform(get("/v1/community/images"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].title", is("title")));
    }

    @Test
    void testPostMessage() throws Exception {
        // given
        MessageDto message = MessageDto.builder()
                .content("msg")
                .userDto(new UserSummaryDto())
                .createdDate(LocalDateTime.now())
                .build();
        when(communityService.postMessage(any(), any()))
                .thenReturn(message);

        // when then
        mockMvc.perform(post("/v1/community/messages")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(message)))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.content", is("msg")));
    }

    @Test
    void testPostImage() throws Exception {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile("file", "sample.txt", "text/plain",
                getClass().getClassLoader().getResourceAsStream("files/sample.txt"));

        ImageDto image = ImageDto.builder()
                .title("title")
                .userDto(new UserSummaryDto())
                .createdDate(LocalDateTime.now())
                .build();
        when(communityService.postImage(any(), any(), any()))
                .thenReturn(image);

        // when then
        mockMvc.perform(multipart("/v1/community/images").file(multipartFile).param("title", "title"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.title", is("title")));
    }
}