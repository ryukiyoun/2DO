package com.web.todo.service;

import com.web.todo.dto.UserDTO;
import com.web.todo.entity.User;
import com.web.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFindService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findAllByName(username).orElse(null);

        if (user != null) {
            return UserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .password(user.getPassword())
                    .build();
        } else
            throw new UsernameNotFoundException("User Not Found");
    }
}
