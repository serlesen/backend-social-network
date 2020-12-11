package com.sergio.socialnetwork.controllers;

import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sergio.socialnetwork.config.CookieAuthenticationFilter;
import com.sergio.socialnetwork.config.UserAuthenticationProvider;
import com.sergio.socialnetwork.dto.SignUpDto;
import com.sergio.socialnetwork.dto.UserDto;
import com.sergio.socialnetwork.services.AuthenticationService;
import com.sergio.socialnetwork.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(UserService userService,
                                    AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserDto> signIn(@AuthenticationPrincipal UserDto user,
                                          HttpServletResponse servletResponse) {

        Cookie authCookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, authenticationService.createToken(user));
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setMaxAge((int) Duration.of(1, ChronoUnit.DAYS).toSeconds());
        authCookie.setPath("/");

        servletResponse.addCookie(authCookie);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid SignUpDto user) {
        UserDto createdUser = userService.signUp(user);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId() + "/profile")).body(createdUser);
    }

    @PostMapping("/signOut")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal UserDto user) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}
