package com.sivalabs.todo.config.security;

import com.sivalabs.todo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("userDetailsService")
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.sivalabs.todo.entity.User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            //user.get().getRoles();
            return new SecurityUser(user.get());
        } else {
            throw new UsernameNotFoundException("No user found with username $username.");
        }
    }
}
