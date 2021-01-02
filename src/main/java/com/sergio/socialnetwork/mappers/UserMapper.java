package com.sergio.socialnetwork.mappers;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import com.sergio.socialnetwork.dto.ProfileDto;
import com.sergio.socialnetwork.dto.SignUpDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.dto.UserStatus;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import com.sergio.socialnetwork.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", imports = { UserStatus.class })
public interface UserMapper {

    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "age", expression = "java(birthDateToAge(user))", dependsOn = "status")
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "login", ignore = true)
    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    void updateUser(@MappingTarget User target, UserDto input);

    default int birthDateToAge(User user) {
        Period period = Period.between(LocalDate.now(), user.getBirthDate());
        return period.getYears();
    }

    UserSummaryDto toUserSummary(User user);

    List<UserSummaryDto> toUserSummaryDtos(List<User> users);

    @Mapping(target = "userDto.id", source = "id")
    @Mapping(target = "userDto.firstName", source = "firstName")
    @Mapping(target = "userDto.lastName", source = "lastName")
    ProfileDto userToProfileDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
