package dev.sorokin.eventmanager.security.jwt;

import dev.sorokin.eventmanager.users.SingInRequest;
import dev.sorokin.eventmanager.users.User;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;

    public JwtAuthenticationService(AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String authenticateUsers(@Valid SingInRequest singInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        singInRequest.login(),
                        singInRequest.password()
                )
        );
        return jwtTokenManager.generateToken(singInRequest.login());
    }


    public User getCurrentAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Error Authentication");
        }
        return (User) authentication.getPrincipal();
    }


}
