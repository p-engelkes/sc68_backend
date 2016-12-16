package com.pengelkes.service.team;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.pengelkes.service.user.User;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by pengelkes on 02.12.2016.
 */
public class Team
{
    public static final String TRAINING_TIMES_JSON = "training_times";
    public static final String TRAINING_DAY_JSON = "day";
    public static final String TRAINING_TIME_JSON = "time";
    public static final String NAME_JSON = "name";

    private int id;
    private String name;
    private List<User> users;
    private HashMap<String, String> trainingTimes;
    private Date created;

    public Team()
    {

    }

    public Team(String name, HashMap<String, String> trainingTimes)
    {
        this.name = name;
        this.trainingTimes = trainingTimes;
    }

    public Team(int id, String name, HashMap<String, String> trainingTimes)
    {
        this.id = id;
        this.name = name;
        this.trainingTimes = trainingTimes;
    }

    public static Optional<Team> fromJson(String json)
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Team.class, new TeamDeserializer());
        mapper.registerModule(module);
        try
        {
            return Optional.ofNullable(mapper.readValue(json, Team.class));
        } catch (IOException e)
        {
            return Optional.empty();
        }
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }

    public HashMap<String, String> getTrainingTimes()
    {
        return trainingTimes;
    }

    public void setTrainingTimes(HashMap<String, String> trainingTimes)
    {
        this.trainingTimes = trainingTimes;
    }
}
