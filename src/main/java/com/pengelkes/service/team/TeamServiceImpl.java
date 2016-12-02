package com.pengelkes.service.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import java.util.List;
import java.util.Optional;

/**
 * Created by pengelkes on 02.12.2016.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService
{
    private TeamServiceController teamServiceController;

    @Autowired
    public TeamServiceImpl(TeamServiceController teamServiceController)
    {
        this.teamServiceController = teamServiceController;
    }

    @Override
    public Optional<Team> create(Team team) throws ServletException
    {
        if (nameExists(team.getName()))
        {
            throw new ServletException("Es existiert bereits ein Team mit dem ausgewähten Namen");
        }

        return teamServiceController.create(team);
    }

    @Override
    public Optional<Team> findByName(String name)
    {
        return teamServiceController.findByName(name);
    }

    @Override
    public Optional<Team> findById(int id)
    {
        return teamServiceController.findById(id);
    }

    @Override
    public List<Team> getAllTeams()
    {
        return teamServiceController.getAllTeams();
    }

    private boolean nameExists(String name)
    {
        return teamServiceController.findByName(name).isPresent();
    }
}
