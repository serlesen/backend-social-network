package com.sergio.socialnetwork.repositories;

import java.util.Arrays;
import java.util.List;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.sergio.socialnetwork.entities.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
class MessageRepositoryTest {

    @Autowired
    private MessageRepository repository;

    @Test
    @DatabaseSetup("/dataset/messages.xml")
    void testFindByUser() {
        // given
        Long userId = 10L;

        // when
        List<Message> messages = repository.findByUserId(userId);

        // then
        assertAll(() -> {
            assertEquals(1, messages.size());
            assertEquals(100, messages.get(0).getId());
        });
    }

    @Test
    @DatabaseSetup("/dataset/messages.xml")
    void testFindMessagesFromFriends() {
        // given
        List<Long> friendIds = Arrays.asList(10L);

        // when
        List<Message> messages = repository.findCommunityMessages(friendIds, PageRequest.of(0, 10));

        // then
        assertAll(() -> {
            assertEquals(1, messages.size());
            assertEquals("content", messages.get(0).getContent());
        });
    }
}