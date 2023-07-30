package com.etrusted.interview.demo.service;

import com.etrusted.interview.demo.entity.User;
import java.util.Optional;

public interface UserService {

    Optional<User> findUserByEmail(String email);

}
