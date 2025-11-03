package com.fps.back.users.service.users;

import com.fps.back.users.model.dto.Consume.ConsumeJsonLogin;
import com.fps.back.users.model.dto.Consume.ConsumeJsonString;
import com.fps.back.users.model.dto.Response.ResponseJsonLogin;
import com.fps.back.users.model.dto.Response.ResponseJsonString;
import com.fps.back.users.model.entity.Permission;
import com.fps.back.users.model.entity.User;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
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
