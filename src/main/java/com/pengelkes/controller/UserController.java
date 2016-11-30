package com.pengelkes.controller;

import com.pengelkes.service.user.User;
import com.pengelkes.service.user.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Created by pengelkes on 29.11.2016.
 */
@RestController()
@RequestMapping("/user")
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

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public boolean registerUser(@RequestBody User user) throws ServletException
    {
        return userService.registerNewUser(user).isPresent();

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody Map<String, String> json) throws ServletException
    {
        if (json.get("username") == null || json.get("password") == null)
        {
            throw new ServletException("Einen leeren Benutzernamen und Passwort können wir leider nicht verarbeiten.");
        }

        String userName = json.get("username");
        String password = passwordEncoder.encode(json.get("password"));

        Optional<User> userOptional = userService.findByName(userName);

        if (userOptional.isPresent())
        {
            User user = userOptional.get();
            String pwd = user.getPassword();

            if (!password.equals(pwd))
            {
                throw new ServletException("Uuups, dein Passwort passt nicht zum Benutzernamen");
            }

            return Jwts.builder()
                    .setSubject(userName)
                    .claim("roles", "user")
                    .setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS256, "secretkey")
                    .compact();
        } else
        {
            throw new ServletException("Wir konnten den angegebenen Benutzernamen leider nicht in unserer Datenbank finden");
        }
    }
}
