package com.etrusted.interview.demo.service;

import com.etrusted.interview.demo.entity.User;
import com.etrusted.interview.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getsUserByEmailAndVerifiesEmail(){
        final String userEmail = "john.doe@example.com";
        User expectedUserResponse = createUserResponse();
        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(expectedUserResponse));
        Optional<User> actualUserResponse = userService.findUserByEmail(userEmail);
        assertNotNull(actualUserResponse);
        assertEquals(expectedUserResponse.getEmail(),actualUserResponse.get().getEmail());
    }

    @Test
    void returnEmptyUserWhenUserIsNotPresent(){
        User expectedUserResponse = new User();
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(expectedUserResponse));
        Optional<User> actualUserResponse = userService.findUserByEmail(anyString());
        assertNull(actualUserResponse.get().getEmail());
        assertEquals(0,actualUserResponse.get().getId());
    }

    private User createUserResponse() {
        return User.builder()
                .id(1)
                .firstName("john")
                .lastName("doe")
                .email("john.doe@example.com")
                .address("USA")
                .build();
    }

}
