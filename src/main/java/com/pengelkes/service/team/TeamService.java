package com.pengelkes.service.team;

import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.util.List;
import java.util.Optional;

/**
 * Created by pengelkes on 02.12.2016.
 */
@Service
public interface TeamService
{
    Optional<Team> create(Team team) throws ServletException;
    Optional<Team> findByName(String name);
    Optional<Team> findById(int id);
    List<Team> getAllTeams();
}
