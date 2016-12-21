package com.pengelkes.service.user;

import com.pengelkes.backend.jooq.tables.records.UserAccountRecord;
import com.pengelkes.service.team.Team;

import java.util.Date;

import static com.pengelkes.backend.jooq.tables.UserAccount.USER_ACCOUNT;

/**
 * Created by pengelkes on 29.11.2016.
 */
public class User
{
    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private Position position;
    private Team team;
    private int teamId;
    private Date created;
    private int backNumber;

    public User()
    {

    }

    public User(String email, String password, Team team)
    {
        this.email = email;
        this.password = password;
        this.team = team;
    }

    public User(UserAccountRecord userAccountRecord)
    {
        this.id = userAccountRecord.getValue(USER_ACCOUNT.ID, Integer.class);
        this.userName = userAccountRecord.getValue(USER_ACCOUNT.USER_NAME, String.class);
        this.firstName = userAccountRecord.getValue(USER_ACCOUNT.FIRST_NAME, String.class);
        this.lastName = userAccountRecord.getValue(USER_ACCOUNT.LAST_NAME, String.class);
        this.password = userAccountRecord.getValue(USER_ACCOUNT.PASSWORD, String.class);
        this.email = userAccountRecord.getValue(USER_ACCOUNT.EMAIL, String.class);
        this.position = userAccountRecord.getValue(USER_ACCOUNT.POSITION, Position.class);
        this.teamId = userAccountRecord.getValue(USER_ACCOUNT.TEAM_ID, Integer.class);
        this.backNumber = userAccountRecord.getValue(USER_ACCOUNT.BACKNUMBER, Integer.class);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    public Team getTeam()
    {
        return team;
    }

    public void setTeam(Team team)
    {
        this.team = team;
    }

    public int getTeamId()
    {
        return teamId;
    }

    public void setTeamId(int teamId)
    {
        this.teamId = teamId;
    }

    public int getBackNumber()
    {
        return backNumber;
    }

    public void setBackNumber(int backNumber)
    {
        this.backNumber = backNumber;
    }
}
