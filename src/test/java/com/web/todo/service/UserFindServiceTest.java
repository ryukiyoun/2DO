package com.web.todo.service;

import com.web.todo.entity.User;
import com.web.todo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserFindServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserFindService userFindService;

    @Test
    public void loadUserByUsernameNotNullTest() {
        Optional<User> user = Optional.of(User.builder().id(1).name("user").password("pass").build());

        //given
        given(userRepository.findAllByName(anyString())).willReturn(user);

        //when
        UserDetails result = userFindService.loadUserByUsername(anyString());

        //then
        assertThat(result.getUsername(), is(user.get().getName()));
        assertThat(result.getPassword(), is(user.get().getPassword()));
    }

    @Test
    public void loadUserByUsernameNullTest() {
        //given
        given(userRepository.findAllByName(anyString())).willReturn(Optional.empty());

        //when, then
        assertThrows(UsernameNotFoundException.class, () -> userFindService.loadUserByUsername(anyString()));
    }
}


