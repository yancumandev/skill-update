package dev.sorokin.eventmanager.security;

import dev.sorokin.eventmanager.users.UserEntity;
import dev.sorokin.eventmanager.users.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException(" пользователь с таким именем не найден"));

        return User.withUsername(username)
                .password(userEntity.getPassword())
                .authorities(userEntity.getRole().name())
                .build();
    }
}
