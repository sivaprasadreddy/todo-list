package com.sivalabs.todo.service;

import com.sivalabs.todo.entity.Role;
import com.sivalabs.todo.entity.User;
import com.sivalabs.todo.exception.TodoApplicationException;
import com.sivalabs.todo.model.ChangePasswordRequest;
import com.sivalabs.todo.repository.RoleRepository;
import com.sivalabs.todo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new TodoApplicationException("Email ${user.email} is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> role_user = roleRepository.findByName("ROLE_USER");
        user.setRoles(new HashSet<>(Collections.singletonList(role_user.orElse(null))));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        Optional<User> byId = userRepository.findById(user.getId());
        if(!byId.isPresent()) {
            throw new TodoApplicationException("User with id ${user.id} not found");
        }
        user.setPassword(byId.get().getPassword());
        user.setRoles(byId.get().getRoles());
        return userRepository.save(user);

    }

    public void deleteUser(Long userId) {
        Optional<User> byId = userRepository.findById(userId);
        byId.ifPresent(userRepository::delete);
    }

    public void changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        Optional<User> userByEmail = this.getUserByEmail(email);
        if(!userByEmail.isPresent()) {
            throw new TodoApplicationException("User with email $email not found");
        }
        User user = userByEmail.get();
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new TodoApplicationException("Current password doesn't match");
        }
    }
}
