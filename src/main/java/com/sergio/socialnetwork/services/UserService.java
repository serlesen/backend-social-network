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
import com.sergio.socialnetwork.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ProfileDto getProfile(Long userId) {
        User user = getUser(userId);
        return new ProfileDto(new UserSummaryDto(user.getId(), user.getFirstName(), user.getLastName()),
                null,
                null,
                null);
    }

    public void addFriend(UserDto userDto, Long friendId) {
        User user = getUser(userDto.getId());

        User newFriend = getUser(friendId);

        if (user.getFriends() == null) {
            user.setFriends(new ArrayList<>());
        }

        user.getFriends().add(newFriend);

        userRepository.save(user);
    }

    public List<UserSummaryDto> searchUsers(String term) {
        List<User> users = userRepository.search("%" + term + "%");
        List<UserSummaryDto> usersToBeReturned = new ArrayList<>();

        users.forEach(user ->
                usersToBeReturned.add(new UserSummaryDto(user.getId(), user.getFirstName(), user.getLastName()))
        );

        return usersToBeReturned;
    }

    public UserDto signUp(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new RuntimeException("Login already exists");
        }

        User user = new User(null,
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getLogin(),
                passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())),
                null,
                null,
                LocalDateTime.now()
                );

        User savedUser = userRepository.save(user);

        return new UserDto(savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getLogin());
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
