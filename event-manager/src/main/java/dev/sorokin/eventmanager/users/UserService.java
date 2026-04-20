package dev.sorokin.eventmanager.users;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.xml.bind.ValidationException;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(SingUpRequest singUpRequest) {
        if (singUpRequest.age() < 18) {
            throw new IllegalArgumentException("возраст должен от 18 (включительно) ");
        }
        if (userRepository.existsByLogin(singUpRequest.login())) {
            throw new IllegalArgumentException("логин уже занят");
        }
        var hashedPass = passwordEncoder.encode(singUpRequest.password());
        var saveUserToTable = new UserEntity(
                null,
                singUpRequest.login(),
                hashedPass,
                singUpRequest.age(),
                UserRole.USER);

        var saved = userRepository.save(saveUserToTable);
        return mapToDomain(saved);
    }

    public User findByLogin(String login) {
        UserEntity userEntity = userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return mapToDomain(userEntity);
    }

    private static User mapToDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getLogin(),
                entity.getAge(),
                entity.getRole(),
                null
        );
    }

    public User findById(Long id) {
        var userEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return mapToDomain(userEntity);
    }

    public boolean isUserExistsByLogin(String login) {
        return userRepository.findByLogin(login)
                .isPresent();
    }

    public void initDefaultAdmin(User admin) {
        var saveAdmin = new UserEntity(
                null,
                admin.login(),
                admin.passwordHash(),
                admin.age(),
                admin.role()
        );
        userRepository.save(saveAdmin);
    }


}
