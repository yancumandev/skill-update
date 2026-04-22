package dev.sorokin.eventmanager.users;


import dev.sorokin.eventmanager.security.jwt.JwtAuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger log = LoggerFactory.getLogger(UsersController.class);

    private final UserService userService;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final UserDtoConvector userDtoConvector;

    public UsersController(UserService userService, JwtAuthenticationService jwtAuthenticationService, UserDtoConvector userDtoConvector) {
        this.userService = userService;
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.userDtoConvector = userDtoConvector;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody SingUpRequest singUpRequest) {
        log.info("Регистрация пользователя {}", singUpRequest.login());
        var user = userService.registerUser(singUpRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserDto(
                        user.id(),
                        user.login(),
                        user.age(),
                        UserRole.USER.name()
                ));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @RequestBody @Valid SingInRequest singInRequest
    ) {
        log.info("запрос на вход пользователя {}", singInRequest.login());
        var token = jwtAuthenticationService.authenticateUsers(singInRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new JwtTokenResponse(token));

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") Long id) {
        var user = userService.findById(id);
        var userDto = userDtoConvector.toDto(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }


}
