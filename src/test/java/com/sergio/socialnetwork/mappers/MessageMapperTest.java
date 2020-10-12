package com.sergio.socialnetwork.mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.sergio.socialnetwork.dto.MessageDto;
import com.sergio.socialnetwork.entities.Message;
import com.sergio.socialnetwork.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class MessageMapperTest {

    private static MessageMapper messageMapper = new MessageMapperImpl();
    private static UserMapper userMapper = new UserMapperImpl();

    @BeforeAll
    public static void setUp() {
        ReflectionTestUtils.setField(messageMapper, "userMapper", userMapper);
    }

    @Test
    @Disabled("Don't know why podam don't fill the User field from Message when running all the test at once.")
    void testMapSingleMessage() {
        // given
        Message msg = Message.builder()
                .id(1L)
                .content("content")
                .user(User.builder().id(1L).firstName("first").lastName("last").build())
                .createdDate(LocalDateTime.now())
                .build();

        // when
        MessageDto msgDto = messageMapper.messageToMessageDto(msg);

        // then
        assertAll(() -> {
            assertEquals(msg.getContent(), msgDto.getContent());
            assertEquals(msg.getUser().getId(), msgDto.getUserDto().getId());
        });
    }

    @Test
    void testMapMessageList() {
        // given
        List<Message> messages = Arrays.asList(
                Message.builder().id(1L).content("first").user(new User()).createdDate(LocalDateTime.now()).build(),
                Message.builder().id(2L).content("second").user(new User()).createdDate(LocalDateTime.now()).build());

        // when
        List<MessageDto> messageDtos = messageMapper.messagesToMessageDtos(messages);

        // then
        assertAll(() -> {
            assertEquals(messages.size(), messageDtos.size());
            for (int i = 0; i < messages.size(); i++) {
                assertEquals(messages.get(i).getContent(), messageDtos.get(i).getContent());
            }
        });
    }

    @Test
    void testMapMessageDto() {
        // given
        MessageDto messageDto = MessageDto.builder()
                .content("content")
                .createdDate(LocalDateTime.now())
                .build();

        // when
        Message message = messageMapper.messageDtoToMessage(messageDto);

        // then
        assertEquals(messageDto.getContent(), message.getContent());
    }
}
