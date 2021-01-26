package com.sergio.socialnetwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.sergio.socialnetwork.config.UserAuthenticationProvider;
import com.sergio.socialnetwork.dto.ImageDto;
import com.sergio.socialnetwork.dto.MessageDto;
import com.sergio.socialnetwork.repositories.MessageRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
class CommunityControllerIT {

    public MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    public MessageRepository messageRepository;

    @Autowired
    public UserAuthenticationProvider userAuthenticationProvider;

    private ObjectMapper mapper = new ObjectMapper();

    private static PodamFactory podamFactory;

    @BeforeAll
    public static void setUpAll() {
        podamFactory = new PodamFactoryImpl();
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
    }

    @DatabaseSetup("/dataset/messages.xml")
    @Test
    void testGetMessages() throws Exception {
        // when then
        mockMvc.perform(get("/v1/community/messages")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJsb2dpbiIsIm5hbWUiOiJmaXJzdCBsYXN0IiwiaWF0IjoxNTE2MjM5MDIyfQ.TaQxRW4vZY2V0gJsd4HQXASTzh7qsSTfvGzlFQPtz_Q"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].content", is("content")));
    }

    @DatabaseSetup("/dataset/images.xml")
    @Test
    void testGetImages() throws Exception {
        // when then
        mockMvc.perform(get("/v1/community/images")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJsb2dpbiIsIm5hbWUiOiJmaXJzdCBsYXN0IiwiaWF0IjoxNTE2MjM5MDIyfQ.TaQxRW4vZY2V0gJsd4HQXASTzh7qsSTfvGzlFQPtz_Q"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].title", is("title")));
    }

    @DatabaseSetup("/dataset/messages.xml")
    @Test
    void testPostMessage() throws Exception {
        // given
        MessageDto messageDto = podamFactory.manufacturePojo(MessageDto.class);

        // when then
        mockMvc.perform(post("/v1/community/messages")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJsb2dpbiIsIm5hbWUiOiJmaXJzdCBsYXN0IiwiaWF0IjoxNTE2MjM5MDIyfQ.TaQxRW4vZY2V0gJsd4HQXASTzh7qsSTfvGzlFQPtz_Q")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(messageDto)))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.content", is(messageDto.getContent())));
    }

    @DatabaseSetup("/dataset/images.xml")
    @Test
    void testPostImage() throws Exception {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile("file", "sample.txt", "text/plain",
                getClass().getClassLoader().getResourceAsStream("files/sample.txt"));

        ImageDto imageDto = podamFactory.manufacturePojo(ImageDto.class);

        // when then
        mockMvc.perform(multipart("/v1/community/images").file(multipartFile)
                .param("title", imageDto.getTitle())
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJsb2dpbiIsIm5hbWUiOiJmaXJzdCBsYXN0IiwiaWF0IjoxNTE2MjM5MDIyfQ.TaQxRW4vZY2V0gJsd4HQXASTzh7qsSTfvGzlFQPtz_Q"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.title", is(imageDto.getTitle())));
    }
}