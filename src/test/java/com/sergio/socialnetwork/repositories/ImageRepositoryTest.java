package com.sergio.socialnetwork.repositories;

import java.util.Arrays;
import java.util.List;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.sergio.socialnetwork.entities.Image;
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
class ImageRepositoryTest {

    @Autowired
    private ImageRepository repository;

    @Test
    @DatabaseSetup("/dataset/images.xml")
    void testFindImagesFromFriends() {
        // given
        List<Long> friendIds = Arrays.asList(10L);

        // when
        List<Image> images = repository.findCommunityImages(friendIds, PageRequest.of(0, 10));

        // then
        assertAll(() -> {
            assertEquals(1, images.size());
            assertEquals("title", images.get(0).getTitle());
        });
    }
}