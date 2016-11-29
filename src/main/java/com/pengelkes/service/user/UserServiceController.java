package com.pengelkes.service.user;

import com.pengelkes.backend.jooq.tables.records.UserAccountRecord;
import org.jooq.DSLContext;
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

    public Optional<User> create(User user)
    {
        UserAccountRecord userAccountRecord = dsl.insertInto(USER_ACCOUNT)
                .set(USER_ACCOUNT.FIRST_NAME, user.getFirstName())
                .set(USER_ACCOUNT.LAST_NAME, user.getLastName())
                .set(USER_ACCOUNT.USER_NAME, user.getUserName())
                .set(USER_ACCOUNT.PASSWORD, user.getPassword())
                .returning(USER_ACCOUNT.ID)
                .fetchOne();

        user.setId(userAccountRecord.getId());
        return Optional.of(user);
    }
}
