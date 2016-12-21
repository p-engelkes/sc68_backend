package com.pengelkes;

import com.pengelkes.service.team.Team;
import com.pengelkes.service.team.TeamService;
import com.pengelkes.service.user.Position;
import com.pengelkes.service.user.User;
import com.pengelkes.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by pengelkes on 29.11.2016.
 */
//TODO: Remove for production
@Component
public class SetupBean
{
    private UserService userService;
    private TeamService teamService;

    @Autowired
    public SetupBean(UserService userService,
                     TeamService teamService)
    {
        this.userService = userService;
        this.teamService = teamService;
    }

    @PostConstruct
    public void setupUser()
    {
        HashMap<String, String> trainingTimes = new HashMap<>();
        trainingTimes.put("Wednesday", "19:00");
        trainingTimes.put("Friday", "19:00");
        Team team = new Team("1. Mannschaft", trainingTimes);
        try
        {
            Optional<Team> teamOptional = teamService.create(team);
            if (teamOptional.isPresent())
            {
                final User user = new User("admin@fake.com", "adminpass", teamOptional.get());
                user.setPosition(Position.MIDFIELD);
                try
                {
                    userService.registerNewUser(user);
                } catch (ServletException e)
                {
                    e.printStackTrace();
                }
            }
        } catch (ServletException e)
        {
            e.printStackTrace();
        }

        team = new Team("2. Mannschaft", trainingTimes);
        try
        {
            teamService.create(team);
        } catch (ServletException e)
        {
            e.printStackTrace();
        }

        userService.findByEmail("admin@fake.com").ifPresent(user -> System.out.println(user.getPosition()));
    }

}
