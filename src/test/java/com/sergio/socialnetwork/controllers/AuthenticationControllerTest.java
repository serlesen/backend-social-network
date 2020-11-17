package com.sergio.socialnetwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergio.socialnetwork.dto.SignUpDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class AuthenticationControllerTest {

    public MockMvc mockMvc;

    @Autowired
    public AuthenticationController authenticationController;

    @MockBean
    public UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static PodamFactory podamFactory;

    @BeforeAll
    public static void setUpAll() {
        podamFactory = new PodamFactoryImpl();
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testSignUp() throws Exception {
        // given
        SignUpDto signUpDto = podamFactory.manufacturePojo(SignUpDto.class);
        UserDto userDto = podamFactory.manufacturePojo(UserDto.class);

        when(userService.signUp(any()))
                .thenReturn(userDto);

        // when then
        mockMvc.perform(post("/v1/signUp")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(signUpDto))
        ).andExpect(status().is(201))
                .andExpect(jsonPath("$.token", is(userDto.getToken())));
    }

    @Test
    void testSignOut() throws Exception {
        // given

        // when then
        mockMvc.perform(post("/v1/signOut"))
                .andExpect(status().is(204));
    }
}
