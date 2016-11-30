package com.pengelkes;

import com.pengelkes.service.user.User;
import com.pengelkes.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;

/**
 * Created by pengelkes on 29.11.2016.
 */
//TODO: Remove for production
@Component
public class SetupBean
{
    private UserService userService;

    @Autowired
    public SetupBean(UserService userService)
    {
        this.userService = userService;
    }

    @PostConstruct
    public void setupUser()
    {
        final User user = new User("admin@fake.com", "adminpass");
        try
        {
            userService.registerNewUser(user);
        } catch (ServletException e)
        {
            e.printStackTrace();
        }
    }

}
