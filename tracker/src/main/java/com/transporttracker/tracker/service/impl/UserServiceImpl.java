package com.transporttracker.tracker.service.impl;

import com.transporttracker.tracker.dto.AuthenticationRequest;
import com.transporttracker.tracker.dto.AuthenticationResponse;
import com.transporttracker.tracker.dto.UserDTO;
import com.transporttracker.tracker.dto.UserRegisterDTO;
import com.transporttracker.tracker.entity.UserEntity;
import com.transporttracker.tracker.repository.UserRepository;
import com.transporttracker.tracker.service.UserService;

import com.transporttracker.tracker.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO register(UserRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        UserEntity user = UserEntity.builder()
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();

        UserEntity saved = userRepository.save(user);

        return new UserDTO(saved.getId(), saved.getEmail(), saved.getRole());
    }

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        String jwt = jwtService.generateToken(user.getEmail());

        return new AuthenticationResponse(jwt);
    }
}
