package com.pengelkes.service.user;

import com.pengelkes.backend.jooq.tables.records.UserAccountRecord;
import com.pengelkes.service.team.TeamService;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.pengelkes.backend.jooq.tables.UserAccount.USER_ACCOUNT;

/**
 * Created by pengelkes on 29.11.2016.
 */
@Service
@Transactional
public class UserServiceController
{
    private DSLContext dsl;
    private TeamService teamService;

    @Autowired
    public UserServiceController(DSLContext dsl,
                                 TeamService teamService)
    {
        this.dsl = dsl;
        this.teamService = teamService;
    }

    public int registerNewUser(User user)
    {
        UserAccountRecord userAccountRecord = dsl.insertInto(USER_ACCOUNT)
                .set(USER_ACCOUNT.EMAIL, user.getEmail())
                .set(USER_ACCOUNT.PASSWORD, user.getPassword())
                .returning(USER_ACCOUNT.ID)
                .fetchOne();

        return userAccountRecord.getValue(USER_ACCOUNT.ID, Integer.class);
    }

    public Optional<User> create(User user)
    {
        UserAccountRecord userAccountRecord;

        if (user.getTeam() != null && user.getPosition() != null)
        {
            userAccountRecord = dsl.insertInto(USER_ACCOUNT)
                    .set(USER_ACCOUNT.FIRST_NAME, user.getFirstName())
                    .set(USER_ACCOUNT.LAST_NAME, user.getLastName())
                    .set(USER_ACCOUNT.USER_NAME, user.getUserName())
                    .set(USER_ACCOUNT.PASSWORD, user.getPassword())
                    .set(USER_ACCOUNT.EMAIL, user.getEmail())
                    .set(USER_ACCOUNT.TEAM_ID, user.getTeam().getId())
                    .set(USER_ACCOUNT.POSITION, user.getPosition().toString())
                    .returning(USER_ACCOUNT.ID)
                    .fetchOne();
        } else
        {
            userAccountRecord = dsl.insertInto(USER_ACCOUNT)
                    .set(USER_ACCOUNT.FIRST_NAME, user.getFirstName())
                    .set(USER_ACCOUNT.LAST_NAME, user.getLastName())
                    .set(USER_ACCOUNT.USER_NAME, user.getUserName())
                    .set(USER_ACCOUNT.PASSWORD, user.getPassword())
                    .set(USER_ACCOUNT.EMAIL, user.getEmail())
                    .returning(USER_ACCOUNT.ID)
                    .fetchOne();
        }


        user.setId(userAccountRecord.getId());
        return Optional.of(user);
    }

    public Optional<User> findByName(String name)
    {
        return getEntity(dsl.selectFrom(USER_ACCOUNT)
                .where(USER_ACCOUNT.USER_NAME.eq(name)).fetchOne());
    }

    public Optional<User> findByEmail(String email)
    {
        return getEntity(dsl.selectFrom(USER_ACCOUNT)
                .where(USER_ACCOUNT.EMAIL.eq(email)).fetchOne());
    }

    public Optional<User> update(User user)
    {
        dsl.update(USER_ACCOUNT)
                .set(USER_ACCOUNT.POSITION, user.getPosition().toString())
                .set(USER_ACCOUNT.TEAM_ID, user.getTeamId())
                .where(USER_ACCOUNT.ID.eq(user.getId()));

        return Optional.of(user);
    }

    private Optional<User> getEntity(Record record)
    {
        if (record != null)
        {
            String userName = record.getValue(USER_ACCOUNT.USER_NAME, String.class);
            String firstName = record.getValue(USER_ACCOUNT.FIRST_NAME, String.class);
            String lastName = record.getValue(USER_ACCOUNT.LAST_NAME, String.class);
            String password = record.getValue(USER_ACCOUNT.PASSWORD, String.class);
            String email = record.getValue(USER_ACCOUNT.EMAIL, String.class);
            Position position = record.getValue(USER_ACCOUNT.POSITION, Position.class);
            Integer teamId = record.getValue(USER_ACCOUNT.TEAM_ID, Integer.class);

            User user = new User(userName, password, email, firstName, lastName, position);
            if (teamId != null)
            {
                teamService.findById(teamId).ifPresent(user::setTeam);
            }

            return Optional.of(user);
        }

        return Optional.empty();
    }
}
