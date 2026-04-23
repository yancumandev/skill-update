package dev.sorokin.eventmanager.security;

import dev.sorokin.eventmanager.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler, JwtTokenFilter jwtTokenFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(login -> login.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/auth").permitAll()

                                .requestMatchers(HttpMethod.GET, "/locations/**").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/locations").hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/locations/**").hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/locations/**").hasAnyAuthority("ADMIN")

                                .requestMatchers(HttpMethod.POST, "/events").hasAuthority("USER")
                                .requestMatchers(HttpMethod.DELETE, "/events/{locationId}").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/events/**").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/events/{locationId}").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/events/search").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/events/my").hasAuthority("USER")

                                .requestMatchers(HttpMethod.POST, "/events/registrations/").hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.DELETE, "/events/registrations/cancel/").hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.GET,"/events/registrations/my").hasAnyAuthority("USER")

                                .anyRequest().authenticated()
                )
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
