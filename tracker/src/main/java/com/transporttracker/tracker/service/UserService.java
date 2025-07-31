package com.transporttracker.tracker.service;

import com.transporttracker.tracker.dto.AuthenticationRequest;
import com.transporttracker.tracker.dto.AuthenticationResponse;
import com.transporttracker.tracker.dto.UserRegisterDTO;
import com.transporttracker.tracker.dto.UserDTO;

public interface UserService {
    UserDTO register(UserRegisterDTO userDto);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}