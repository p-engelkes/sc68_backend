package com.pengelkes.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pengelkes on 30.11.2016.
 */
@RestController
@RequestMapping("/api/security")
public class SecurityController
{

    @RequestMapping(value = "/verifyLogin")
    public String verifyToken()
    {
        return "Login Successful";
    }

}
