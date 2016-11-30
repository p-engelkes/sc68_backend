package com.pengelkes.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by pengelkes on 29.11.2016.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService
{
    private UserServiceController userServiceController;

    @Autowired
    public UserServiceImpl(UserServiceController userServiceController)
    {
        this.userServiceController = userServiceController;
    }

    @Override
    public Optional<User> registerNewUser(User user)
    {
        return this.userServiceController.create(user);
    }

    @Override
    public Optional<User> findByName(String name)
    {
        return this.userServiceController.findByName(name);
    }
}
