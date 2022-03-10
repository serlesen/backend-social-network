package com.sergio.socialnetwork.services;

import com.sergio.socialnetwork.dto.ProfileDto;
import com.sergio.socialnetwork.dto.SignUpDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.dto.UserSummaryDto;
import com.sergio.socialnetwork.entities.MongoUser;
import com.sergio.socialnetwork.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ProfileDto getProfile(String userId) {
        MongoUser user = getUser(userId);
        return new ProfileDto(new UserSummaryDto(user.getId(), user.getFirstName(), user.getLastName()),
                null,
                null,
                null);
    }

    public void addFriend(UserDto userDto, String friendId) {
        MongoUser user = getUser(userDto.getId());

        MongoUser newFriend = getUser(friendId);

        if (user.getFriends() == null) {
            user.setFriends(new ArrayList<>());
        }

        user.getFriends().add(newFriend.buildShareableUser());

        userRepository.save(user);
    }

    public List<UserSummaryDto> searchUsers(String term) {
        List<MongoUser> users = userRepository.searchUsers(term);
        List<UserSummaryDto> usersToBeReturned = new ArrayList<>();

        users.forEach(user ->
                usersToBeReturned.add(new UserSummaryDto(user.getId(), user.getFirstName(), user.getLastName()))
        );

        return usersToBeReturned;
    }

    public UserDto signUp(SignUpDto userDto) {
        Optional<MongoUser> optionalUser = userRepository.findByLogin(userDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new RuntimeException("Login already exists");
        }

        MongoUser user = new MongoUser(
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getLogin(),
                passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())),
                Collections.emptyList(),
                LocalDateTime.now()
                );

        MongoUser savedUser = userRepository.save(user);

        return new UserDto(savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getLogin());
    }

    private MongoUser getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
