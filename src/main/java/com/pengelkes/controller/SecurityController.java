package com.pengelkes.controller;

import com.pengelkes.service.user.User;
import com.pengelkes.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.util.Optional;

/**
 * Created by pengelkes on 30.11.2016.
 */
@RestController
@RequestMapping("/api/security")
public class SecurityController
{
    private UserService userService;

    @Autowired
    public SecurityController(UserService userService)
    {
        this.userService = userService;
    }

    @RequestMapping(value = "/verifyLogin/{email:.+}")
    public int verifyToken(@PathVariable String email) throws ServletException
    {
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent())
        {
            return userOptional.get().getId();
        }

        throw new ServletException("Ein Benutzer mit dieser Adresse existiert nicht");
    }

}
