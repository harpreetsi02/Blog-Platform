package com.backend.blogplatform.security;

import com.backend.blogplatform.entity.Role;
import com.backend.blogplatform.entity.User;
import com.backend.blogplatform.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User(name, email, null, Set.of(Role.USER));
                    return userRepository.save(newUser);
                });

        String token = jwtService.generateToken(user.getEmail());

        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
    }
}
