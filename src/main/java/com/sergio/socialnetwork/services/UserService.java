package com.sergio.socialnetwork.services;

import java.nio.CharBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sergio.socialnetwork.dto.ProfileDto;
import com.sergio.socialnetwork.dto.SignUpDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import com.sergio.socialnetwork.entities.User;
import com.sergio.socialnetwork.exceptions.AppException;
import com.sergio.socialnetwork.mappers.UserMapper;
import com.sergio.socialnetwork.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public ProfileDto getProfile(Long userId) {
        User user = getUser(userId);
        log.trace("Reading profile for user {}", userId);
        return userMapper.userToProfileDto(user);
    }

    public void addFriend(UserDto userDto, Long friendId) {
        User user = getUser(userDto.getId());

        User newFriend = getUser(friendId);

        if (user.getFriends() == null) {
            user.setFriends(new ArrayList<>());
        }

        user.getFriends().add(newFriend);

        log.info("Current user {} is now friend with {}", userDto.getId(), friendId);

        userRepository.save(user);
    }

    public List<UserSummaryDto> searchUsers(String term) {
        List<User> users = userRepository.search("%" + term + "%");
        List<UserSummaryDto> usersToBeReturned = new ArrayList<>();

        users.forEach(user ->
                usersToBeReturned.add(new UserSummaryDto(user.getId(), user.getFirstName(), user.getLastName()))
        );

        log.debug("Searching for user by '{}'. Found {} users", term, usersToBeReturned.size());

        return usersToBeReturned;
    }

    public UserDto signUp(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));

        User savedUser = userRepository.save(user);

        log.info("Creating new user {}", userDto.getLogin());

        return userMapper.toUserDto(savedUser);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }
}
