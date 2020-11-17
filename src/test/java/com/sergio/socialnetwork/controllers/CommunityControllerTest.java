package com.sergio.socialnetwork.controllers;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sergio.socialnetwork.dto.ImageDto;
import com.sergio.socialnetwork.dto.MessageDto;
import com.sergio.socialnetwork.services.CommunityService;
import org.junit.jupiter.api.BeforeAll;
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
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

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

    private static PodamFactory podamFactory;

    @BeforeAll
    public static void setUpAll() {
        podamFactory = new PodamFactoryImpl();
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(communityController).build();

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
    }

    @Test
    void testGetMessages() throws Exception {
        // given
        MessageDto messageDto = podamFactory.manufacturePojo(MessageDto.class);
        List<MessageDto> messages = Arrays.asList(messageDto);
        when(communityService.getCommunityMessages(any(), anyInt()))
                .thenReturn(messages);

        // when then
        mockMvc.perform(get("/v1/community/messages"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].content", is(messageDto.getContent())));
    }

    @Test
    void testGetImages() throws Exception {
        // given
        ImageDto imageDto = podamFactory.manufacturePojo(ImageDto.class);
        List<ImageDto> images = Arrays.asList(imageDto);
        when(communityService.getCommunityImages(any(), anyInt()))
                .thenReturn(images);

        // when then
        mockMvc.perform(get("/v1/community/images"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].title", is(imageDto.getTitle())));
    }

    @Test
    void testPostMessage() throws Exception {
        // given
        MessageDto messageDto = podamFactory.manufacturePojo(MessageDto.class);
        when(communityService.postMessage(any(), any()))
                .thenReturn(messageDto);

        // when then
        mockMvc.perform(post("/v1/community/messages")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(messageDto)))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.content", is(messageDto.getContent())));
    }

    @Test
    void testPostImage() throws Exception {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile("file", "sample.txt", "text/plain",
                getClass().getClassLoader().getResourceAsStream("files/sample.txt"));

        ImageDto imageDto = podamFactory.manufacturePojo(ImageDto.class);
        when(communityService.postImage(any(), any(), any()))
                .thenReturn(imageDto);

        // when then
        mockMvc.perform(multipart("/v1/community/images").file(multipartFile).param("title", imageDto.getTitle()))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.title", is(imageDto.getTitle())));
    }
}