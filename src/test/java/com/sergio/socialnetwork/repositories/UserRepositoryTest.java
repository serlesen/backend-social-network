package com.sergio.socialnetwork.repositories;

import java.util.List;
import java.util.Optional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.sergio.socialnetwork.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class UserRepositoryTest {

    @Autowired
    public UserRepository userRepository;

    @Test
    @DatabaseSetup("/dataset/users.xml")
    void testFindByLogin() {
        // given
        String login = "login";

        // when
        Optional<User> user = userRepository.findByLogin(login);

        // then
        assertAll(() -> {
            assertTrue(user.isPresent());
            assertEquals(login, user.get().getLogin());
            assertEquals(10L, user.get().getId());
        });
    }

    @Test
    @DatabaseSetup("/dataset/users.xml")
    void testSearchUsers() {
        // given
        String term = "%John%";

        // when
        List<User> users = userRepository.search(term);

        // then
        assertAll(() -> {
            assertEquals(1, users.size());
            assertEquals("John", users.get(0).getFirstName());
        });
    }
}
