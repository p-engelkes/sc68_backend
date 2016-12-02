package com.pengelkes.controller;

import com.pengelkes.service.team.Team;
import com.pengelkes.service.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.util.List;
import java.util.Optional;

/**
 * Created by pengelkes on 02.12.2016.
 */
@RestController
@RequestMapping("/api")
public class TeamController
{
    private TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService)
    {
        this.teamService = teamService;
    }

    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    public List<Team> getAllTeams()
    {
        return teamService.getAllTeams();
    }

    @RequestMapping(value = "/teams", method = RequestMethod.POST)
    public Optional<Team> createTeam(@RequestBody String json) throws ServletException
    {
        Optional<Team> teamOptional = Team.fromJson(json);
        if (teamOptional.isPresent())
        {
            teamOptional = teamService.create(teamOptional.get());
        }

        return teamOptional;
    }
}
