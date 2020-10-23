package com.sergio.socialnetwork.controllers;

import java.util.Arrays;

import com.sergio.socialnetwork.dto.ProfileDto;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import com.sergio.socialnetwork.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class UserControllerTest {

    public MockMvc mockMvc;

    @Autowired
    public UserController userController;

    @MockBean
    public UserService userService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetProfile() throws Exception {
        // given
        UserSummaryDto userDto = new UserSummaryDto();
        userDto.setId(1L);

        ProfileDto profile = new ProfileDto();
        profile.setUserDto(userDto);

        when(userService.getProfile(1L))
                .thenReturn(profile);

        // when then
        mockMvc.perform(get("/v1/users/1/profile"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.userDto.id", is(1)));
    }

    @Test
    void testAddFriend() throws Exception {
        // given
        Long friendId = 1L;

        // when then
        mockMvc.perform(post("/v1/users/friends/" + friendId))
                .andExpect(status().is(204));
        verify(userService).addFriend(any(), eq(friendId));
    }

    @Test
    void testSearchFriends() throws Exception {
        // given
        when(userService.searchUsers("term"))
                .thenReturn(Arrays.asList(UserSummaryDto.builder()
                        .id(1L)
                        .firstName("first")
                        .lastName("last")
                        .build()));

        // when then
        mockMvc.perform(post("/v1/users/search").param("term", "term"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].firstName", is("first")));
    }

}