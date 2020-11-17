package com.sergio.socialnetwork.mappers;

import java.util.ArrayList;
import java.util.List;

import com.sergio.socialnetwork.dto.MessageDto;
import com.sergio.socialnetwork.entities.Message;
import com.sergio.socialnetwork.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import uk.co.jemos.podam.api.DefaultClassInfoStrategy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.jupiter.api.Assertions.*;

public class MessageMapperTest {

    private static MessageMapper messageMapper = new MessageMapperImpl();
    private static UserMapper userMapper = new UserMapperImpl();

    private static PodamFactory podamFactory;

    @BeforeAll
    public static void setUp() {
        podamFactory = new PodamFactoryImpl();

        // to avoid infinite loops creating random data
        DefaultClassInfoStrategy classInfoStrategy = DefaultClassInfoStrategy.getInstance();
        classInfoStrategy.addExcludedField(User.class, "messages");
        classInfoStrategy.addExcludedField(User.class, "images");
        classInfoStrategy.addExcludedField(User.class, "friends");

        podamFactory.setClassStrategy(classInfoStrategy);

        ReflectionTestUtils.setField(messageMapper, "userMapper", userMapper);
    }

    @Test
    @Disabled("Don't know why podam don't fill the User field from Message when running all the test at once.")
    void testMapSingleMessage() {
        // given
        Message msg = podamFactory.manufacturePojo(Message.class);

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
        List<Message> messages = podamFactory.manufacturePojo(ArrayList.class, Message.class);

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
        MessageDto messageDto = podamFactory.manufacturePojo(MessageDto.class);

        // when
        Message message = messageMapper.messageDtoToMessage(messageDto);

        // then
        assertEquals(messageDto.getContent(), message.getContent());
    }
}
