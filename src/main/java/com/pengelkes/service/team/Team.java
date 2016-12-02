package com.pengelkes.service.team;

import com.pengelkes.database.jooq.hstore.HstoreUserType;
import com.pengelkes.service.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pengelkes on 02.12.2016.
 */
@Entity
@TypeDef(name = "hstore", typeClass = HstoreUserType.class)
public class Team
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;


    @OneToMany(mappedBy = "team")
    private List<User> users;

    @Type(type = "hstore")
    @Column(name = "training_times")
    private HashMap<String, String> trainingTimes;

    @CreationTimestamp
    private Date created;

    public Team()
    {

    }

    public Team(String name, HashMap<String, String> trainingTimes)
    {
        this.name = name;
        this.trainingTimes = trainingTimes;
    }

    public Team(int id, String name)
    {
        this.id = id;
        this.name = name;
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
