package com.sergio.socialnetwork.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {

    @GetMapping("/v1/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}
