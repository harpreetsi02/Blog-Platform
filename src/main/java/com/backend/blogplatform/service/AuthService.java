package com.backend.blogplatform.service;

import com.backend.blogplatform.dto.request.LoginRequest;
import com.backend.blogplatform.dto.request.RegisterRequest;
import com.backend.blogplatform.dto.response.AuthResponse;
import com.backend.blogplatform.dto.response.UserResponse;
import com.backend.blogplatform.entity.User;
import com.backend.blogplatform.mapper.UserMapper;
import com.backend.blogplatform.repository.UserRepository;
import com.backend.blogplatform.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            UserMapper userMapper,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UserResponse register(RegisterRequest request){

        if (userRepository.existsByEmail(request.getEmail())){
            throw new IllegalStateException("Email already registered!");
        }

        String hashPassword = passwordEncoder.encode(request.getPassword());
        User user = userMapper.toUserEntity(request, hashPassword);
        User savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "User not fount with email: " + request.getEmail()
                        )
                );

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, userMapper.toUserResponse(user));
    }
}
