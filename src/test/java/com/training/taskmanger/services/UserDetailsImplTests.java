package com.training.taskmanger.services;

import com.training.taskmanger.service.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDetailsImplTests {
    @InjectMocks
    UserDetailsImpl userDetails;

    @Test
    void callGetters() {
        userDetails.getUsername();
        userDetails.getId();
        userDetails.getEmail();
        userDetails.getName();
        userDetails.getAuthorities();
        userDetails.getPassword();
        userDetails.setName("test");
    }

    @Test
    void unimplementedMethods() {
        userDetails.getAuthorities();
        userDetails.isAccountNonExpired();
        userDetails.isAccountNonLocked();
        userDetails.isCredentialsNonExpired();
        userDetails.isEnabled();
        userDetails.equals(null);
    }
}
