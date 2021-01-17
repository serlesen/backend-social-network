package com.sergio.socialnetwork.services;

import com.sergio.socialnetwork.dto.UserDto;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public UserDto findOrCreateByLogin(OAuth2User principal) {
        return new UserDto(1L, principal.getAttribute("name"), principal.getAttribute("login"));
    }
}
