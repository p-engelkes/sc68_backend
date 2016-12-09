package com.pengelkes.service.user;

import com.pengelkes.backend.jooq.tables.records.UserAccountRecord;
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

    @Autowired
    public UserServiceController(DSLContext dsl)
    {
        this.dsl = dsl;
    }

    public Optional<User> registerNewUser(User user)
    {
        UserAccountRecord userAccountRecord = dsl.insertInto(USER_ACCOUNT)
                .set(USER_ACCOUNT.EMAIL, user.getEmail())
                .set(USER_ACCOUNT.PASSWORD, user.getPassword())
                .returning(USER_ACCOUNT.ID)
                .fetchOne();

        return getEntity(userAccountRecord);
    }

    public Optional<User> create(User user)
    {
        UserAccountRecord userAccountRecord;

        if (user.getTeam().isPresent() && user.getPosition() != null)
        {
            userAccountRecord = dsl.insertInto(USER_ACCOUNT)
                    .set(USER_ACCOUNT.FIRST_NAME, user.getFirstName())
                    .set(USER_ACCOUNT.LAST_NAME, user.getLastName())
                    .set(USER_ACCOUNT.USER_NAME, user.getUserName())
                    .set(USER_ACCOUNT.PASSWORD, user.getPassword())
                    .set(USER_ACCOUNT.EMAIL, user.getEmail())
                    .set(USER_ACCOUNT.TEAM_ID, user.getTeam().get().getId())
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

            return Optional.of(new User(userName, password, email, firstName, lastName, position));
        }

        return Optional.empty();
    }
}
