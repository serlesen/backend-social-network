package com.sergio.socialnetwork.controllers;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("/dataset/users.xml")
class UserControllerIT {

    public MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetProfile() throws Exception {
        // given

        // when then
        mockMvc.perform(get("/v1/users/11/profile")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJsb2dpbiIsIm5hbWUiOiJmaXJzdCBsYXN0IiwiaWF0IjoxNTE2MjM5MDIyfQ.TaQxRW4vZY2V0gJsd4HQXASTzh7qsSTfvGzlFQPtz_Q"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.userDto.id", is(11)))
                .andExpect(jsonPath("$.userDto.firstName", is("John")));
    }

    @Test
    void testAddFriend() throws Exception {
        // given

        // when then
        mockMvc.perform(post("/v1/users/friends/11")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJsb2dpbiIsIm5hbWUiOiJmaXJzdCBsYXN0IiwiaWF0IjoxNTE2MjM5MDIyfQ.TaQxRW4vZY2V0gJsd4HQXASTzh7qsSTfvGzlFQPtz_Q"))
                .andExpect(status().is(204));
    }

    @Test
    void testSearchFriends() throws Exception {
        // given

        // when then
        mockMvc.perform(post("/v1/users/search")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJsb2dpbiIsIm5hbWUiOiJmaXJzdCBsYXN0IiwiaWF0IjoxNTE2MjM5MDIyfQ.TaQxRW4vZY2V0gJsd4HQXASTzh7qsSTfvGzlFQPtz_Q")
                .param("term", "Doe"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].firstName", is("John")))
                .andExpect(jsonPath("$.[0].lastName", is("Doe")));
    }

}