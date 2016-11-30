package com.pengelkes.service.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by pengelkes on 29.11.2016.
 */
@Service
public interface UserService
{
    Optional<User> registerNewUser(User user);

    Optional<User> findByName(String name);
}
