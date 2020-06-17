package com.sergio.socialnetwork.mappers;

import java.util.List;

import com.sergio.socialnetwork.dto.MessageDto;
import com.sergio.socialnetwork.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
    uses = {UserMapper.class})
public interface MessageMapper {

    List<MessageDto> messagesToMessageDtos(List<Message> messages);

    @Mapping(target = "userDto", source = "user")
    MessageDto messageToMessageDto(Message message);

    Message messageDtoToMessage(MessageDto messageDto);
}
