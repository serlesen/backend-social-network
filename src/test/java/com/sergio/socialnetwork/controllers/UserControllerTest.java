package com.sergio.socialnetwork.controllers;

import java.util.Arrays;

import com.sergio.socialnetwork.dto.ProfileDto;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import com.sergio.socialnetwork.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class UserControllerTest {

    public MockMvc mockMvc;

    @Autowired
    public UserController userController;

    @MockBean
    public UserService userService;

    private static PodamFactory podamFactory;

    @BeforeAll
    public static void setUpAll() {
        podamFactory = new PodamFactoryImpl();
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetProfile() throws Exception {
        // given
        ProfileDto profile = podamFactory.manufacturePojo(ProfileDto.class);

        Long userId = profile.getUserDto().getId();
        when(userService.getProfile(userId))
                .thenReturn(profile);

        // when then
        mockMvc.perform(get("/v1/users/" + userId + "/profile"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.userDto.id", is(userId)));
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
        UserSummaryDto userSummaryDto = podamFactory.manufacturePojo(UserSummaryDto.class);

        when(userService.searchUsers("term"))
                .thenReturn(Arrays.asList(userSummaryDto));

        // when then
        mockMvc.perform(post("/v1/users/search").param("term", "term"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].firstName", is(userSummaryDto.getFirstName())));
    }

}