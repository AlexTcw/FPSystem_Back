package com.fps.back.users.service.users;

import com.fps.back.shared.model.exception.ResourceNotFoundException;
import com.fps.back.users.model.dto.Consume.ConsumeJsonLogin;
import com.fps.back.users.model.dto.Consume.ConsumeJsonString;
import com.fps.back.users.model.dto.Consume.ConsumeJsonUser;
import com.fps.back.users.model.dto.Response.ResponseJsonNumberGeneric;
import com.fps.back.users.model.dto.Response.ResponseJsonLogin;
import com.fps.back.users.model.dto.Response.ResponseJsonString;
import com.fps.back.users.model.dto.Response.ResponseJsonUser;
import com.fps.back.users.model.entity.Role;
import com.fps.back.users.model.entity.User;
import com.fps.back.users.repository.RoleRepository;
import com.fps.back.users.repository.UserRepository;
import com.fps.back.users.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authManager;

    @Override
    public ResponseJsonString bcrypt(ConsumeJsonString consume){
        return new ResponseJsonString(this.bcrypt(consume.key()));
    }

    @Override
    public ResponseJsonLogin login(ConsumeJsonLogin consume){
        if (consume == null){
            throw new IllegalArgumentException("Consume is null");
        }
        if (consume.username() == null || consume.password() == null){
            throw new IllegalArgumentException("Consume username or password is null");
        }

        if (!userRepository.existsUserByUsername(consume.username())){
            throw new UsernameNotFoundException(consume.username());
        }

        User user = userRepository.findUserByUsername(consume.username());
        String jwtToken = authenticate(consume.username(), consume.password())
                .orElseThrow(() -> new RuntimeException("Authentication failed"));

        Set<String> roles = user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
        Set<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(p -> p.getName().name())
                .collect(Collectors.toSet());
        return new ResponseJsonLogin(user.getUserId(),permissions,roles,user.getUsername(), jwtToken);
    }

    @Override
    public ResponseJsonNumberGeneric getRoles(){
        return new ResponseJsonNumberGeneric(roleRepository.findAll().stream()
                .collect(Collectors.toMap(Role::getRoleId, role -> role.getName().name())));
    }

    @Override
    public ResponseJsonUser createOrUpdateUser(ConsumeJsonUser consume, Long userId) {
        if (userId == null && consume == null){
            throw new IllegalArgumentException("Missing data for user");
        }
        if (roleRepository.existsRoleByRoleId(consume.rolID())){
            throw new IllegalArgumentException("role not found with id " + consume.rolID());
        }
        User user;
        if (userId != null){
            if (userId <= 0){
                throw new IllegalArgumentException("Invalid user id");
            }
            user = userRepository.findUserByUserId(userId);
            user = updateUser(user,consume);
        } else {
            user = createUser(consume);
        }
        userRepository.save(user);
        return new ResponseJsonUser(
                user.getUserId(),
                user.getRoles().stream().flatMap(role -> role.getPermissions().stream().map(permission -> permission.getName().name())).collect(Collectors.toSet()),
                user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName()+" "+user.getLastName()
        );
    }

    public User createUser(ConsumeJsonUser consume){
        validData(consume.email(), "email"); validData(consume.password(),  "password");
        return User.builder()
                .firstName(consume.firstName())
                .lastName(consume.lastName())
                .phone(consume.phone())
                .address(consume.address())
                .email(consume.email())
                .username(Optional.ofNullable(consume.username()).filter(s -> !s.isEmpty()).orElseGet(() -> usernameGenerator(consume.email())))
                .password(bcrypt(consume.password()))
                .roles(roleRepository.findRoleByRoleId(consume.rolID()))
                .build();
    }

    public User updateUser(User updatedUser, ConsumeJsonUser consume) {
        return updatedUser.toBuilder()
                .firstName(getOrDefault(consume.firstName(), updatedUser.getFirstName()))
                .lastName(getOrDefault(consume.lastName(), updatedUser.getLastName()))
                .phone(getOrDefault(consume.phone(), updatedUser.getPhone()))
                .address(getOrDefault(consume.address(), updatedUser.getAddress()))
                .email(getOrDefault(consume.email(), updatedUser.getEmail()))
                .username(getOrDefault(consume.username(), updatedUser.getUsername()))
                .password(Optional.ofNullable(consume.password()).filter(s -> !s.isEmpty()).map(this::bcrypt).orElse(updatedUser.getPassword()))
                .roles(roleRepository.findRoleByRoleId(consume.rolID()))
                .build();
    }


    private String usernameGenerator(String email) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String base = email.split("@")[0];
        String username;

        do {
            boolean upperPrefix = random.nextBoolean();
            boolean upperSuffix = random.nextBoolean();

            char prefix = (char) random.nextInt(
                    upperPrefix ? 'A' : 'a',
                    (upperPrefix ? 'Z' : 'z') + 1
            );

            char suffix = (char) random.nextInt(
                    upperSuffix ? 'A' : 'a',
                    (upperSuffix ? 'Z' : 'z') + 1
            );

            int number = random.nextInt(1, 1001);

            username = prefix + base + suffix + number;

        } while (userRepository.existsUserByUsername(username));

        return username;
    }



    private static void validData(String data,String field) {
        if (data == null){
            throw new IllegalArgumentException(field + " cannot be null");
        }
        if (data.isEmpty()){
            throw new IllegalArgumentException(field + " cannot be empty");
        }
    }


    private static String getOrDefault(String newValue, String currentValue) {
        return Optional.ofNullable(newValue)
                .filter(s -> !s.isEmpty())
                .orElse(currentValue);
    }

    private String bcrypt(String password){
        return new BCryptPasswordEncoder().encode(password);
    }

    private Optional<String> authenticate(String username, String password) {
        try {
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return Optional.of(jwtService.getToken(userDetails));
        } catch (AuthenticationException e) {
            return Optional.empty();
        }
    }


}
