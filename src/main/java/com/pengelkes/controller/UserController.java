package com.pengelkes.controller;

import com.pengelkes.service.user.Position;
import com.pengelkes.service.user.User;
import com.pengelkes.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pengelkes on 29.11.2016.
 */
@RestController()
@RequestMapping("/api")
public class UserController
{
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService,
                          PasswordEncoder passwordEncoder)
    {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public boolean registerUser(@RequestBody User user) throws ServletException
    {
        return userService.registerNewUser(user).isPresent();
    }

    @RequestMapping(value = "/positions", method = RequestMethod.GET)
    public List<Position> getAllPositions()
    {
        return Arrays.asList(Position.values());
    }
}
