package com.example.RideShare.service;

import com.example.RideShare.dto.RegisterRequest;
import com.example.RideShare.dto.LoginRequest;
import com.example.RideShare.dto.AuthResponse;
import com.example.RideShare.model.User;
import com.example.RideShare.repository.UserRepository;
import com.example.RideShare.util.JwtUtil;
import com.example.RideShare.exception.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole(req.getRole());
        userRepository.save(u);
    }

    public AuthResponse login(LoginRequest req) {
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(u.getUsername(), u.getRole());
        return new AuthResponse(token, u.getUsername(), u.getRole());
    }
}
