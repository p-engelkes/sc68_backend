package com.pengelkes.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import java.util.Optional;

/**
 * Created by pengelkes on 29.11.2016.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService
{
    private UserServiceController userServiceController;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserServiceController userServiceController,
                           PasswordEncoder passwordEncoder)
    {
        this.userServiceController = userServiceController;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> registerNewUser(User user) throws ServletException
    {
        if (userNameExists(user.getUserName()))
        {
            throw new ServletException("Ohhh, ein anderer Benutzer hat diesen Namen schon ausgewählt");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userServiceController.registerNewUser(user);
    }

    @Override
    public Optional<User> findByName(String name)
    {
        return this.userServiceController.findByName(name);
    }

    @Override
    public Optional<User> findByEmail(String email)
    {
        return this.userServiceController.findByEmail(email);
    }

    private boolean userNameExists(String name)
    {
        return userServiceController.findByName(name).isPresent();
    }
}
