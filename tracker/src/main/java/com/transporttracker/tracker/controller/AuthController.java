package com.transporttracker.tracker.controller;

import com.transporttracker.tracker.dto.AuthenticationRequest;
import com.transporttracker.tracker.dto.AuthenticationResponse;
import com.transporttracker.tracker.dto.UserDTO;
import com.transporttracker.tracker.dto.UserRegisterDTO;
import com.transporttracker.tracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@Valid @RequestBody UserRegisterDTO dto) {
        return userService.register(dto);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        return userService.authenticate(request);
    }
}
