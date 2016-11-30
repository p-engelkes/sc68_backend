package com.pengelkes.service.user;

import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.util.Optional;

/**
 * Created by pengelkes on 29.11.2016.
 */
@Service
public interface UserService
{
    Optional<User> registerNewUser(User user) throws ServletException;

    Optional<User> findByName(String name);
}
