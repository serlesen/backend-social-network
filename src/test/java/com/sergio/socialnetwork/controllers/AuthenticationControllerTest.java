package com.sergio.socialnetwork.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergio.socialnetwork.dto.SignUpDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testSignUp() throws Exception {
        // given
        SignUpDto signUpDto = SignUpDto.builder()
                .firstName("first")
                .lastName("last")
                .login("login")
                .password("pass".toCharArray())
                .build();

        when(userService.signUp(any()))
                .thenReturn(UserDto.builder().id(1L)
                        .firstName("first")
                        .lastName("last")
                        .token("token")
                        .build());

        // when then
        mockMvc.perform(post("/v1/signUp")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(signUpDto))
        ).andExpect(status().is(201))
                .andExpect(jsonPath("$.token", is("token")));
    }

    @Test
    void testSignOut() throws Exception {
        // given

        // when then
        mockMvc.perform(post("/v1/signOut"))
                .andExpect(status().is(204));
    }
}
