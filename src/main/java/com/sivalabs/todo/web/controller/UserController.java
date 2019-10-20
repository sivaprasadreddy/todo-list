package com.sivalabs.todo.web.controller;

import com.sivalabs.todo.entity.User;
import com.sivalabs.todo.model.ChangePasswordRequest;
import com.sivalabs.todo.model.CreateUserRequest;
import com.sivalabs.todo.model.UserDTO;
import com.sivalabs.todo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info("process=get_user, user_id=$id");
        return userService.getUserById(id)
                .map(UserDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @ResponseStatus(CREATED)
    public UserDTO createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        log.info("process=create_user, user_email=${createUserRequest.email}");
        User user = new User();
        user.setEmail(createUserRequest.getEmail());
        user.setName(createUserRequest.getName());
        user.setPassword(createUserRequest.getPassword());
        return UserDTO.fromEntity(userService.createUser(user));
    }


    @PostMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String email = currentUser.getName();
        log.info("process=change_password, email=$email");
        userService.changePassword(email, changePasswordRequest);
    }
}
