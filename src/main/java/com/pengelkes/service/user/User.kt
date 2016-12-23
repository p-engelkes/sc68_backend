package com.pengelkes.service.user

import com.pengelkes.backend.jooq.tables.UserAccount
import com.pengelkes.backend.jooq.tables.records.UserAccountRecord
import com.pengelkes.service.team.Team
import com.pengelkes.service.team.TeamService
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.servlet.ServletException

/**
 * Created by pengelkes on 23.12.2016.
 */
class User {
    var id: Int = 0
    var firstName: String? = null
    var lastName: String? = null
    var userName: String? = null
    var password: String? = null
    var email: String? = null
    var position: Position? = null
    var team: Team? = null
    var teamId: Int = 0
    var created: Date? = null
    var backNumber: Int = 0

    constructor(email: String, password: String, team: Team) {
        this.email = email
        this.password = password
        this.team = team
    }

    constructor(userAccountRecord: UserAccountRecord) {
        this.id = userAccountRecord.getValue<Int>(UserAccount.USER_ACCOUNT.ID, Int::class.java)
        this.userName = userAccountRecord.getValue<String>(UserAccount.USER_ACCOUNT.USER_NAME, String::class.java)
        this.firstName = userAccountRecord.getValue<String>(UserAccount.USER_ACCOUNT.FIRST_NAME, String::class.java)
        this.lastName = userAccountRecord.getValue<String>(UserAccount.USER_ACCOUNT.LAST_NAME, String::class.java)
        this.password = userAccountRecord.getValue<String>(UserAccount.USER_ACCOUNT.PASSWORD, String::class.java)
        this.email = userAccountRecord.getValue<String>(UserAccount.USER_ACCOUNT.EMAIL, String::class.java)
        this.position = userAccountRecord.getValue<Position>(UserAccount.USER_ACCOUNT.POSITION, Position::class.java)
        this.teamId = userAccountRecord.getValue<Int>(UserAccount.USER_ACCOUNT.TEAM_ID, Int::class.java)
        this.backNumber = userAccountRecord.getValue<Int>(UserAccount.USER_ACCOUNT.BACKNUMBER, Int::class.java)
    }
}

@Service
interface UserService {
    @Throws(ServletException::class)
    fun registerNewUser(user: User): Int

    fun findByName(name: String): User?
    fun findByEmail(email: String): User?
    fun findById(id: Int): User?
    fun update(user: User): User?
}

@Service
@Transactional
open class UserServiceImpl
@Autowired
constructor(private val userServiceController: UserServiceController,
            private val passwordEncoder: PasswordEncoder) : UserService {

    @Throws(ServletException::class)
    override fun registerNewUser(user: User): Int {
        if (emailExists(user.email!!)) {
            throw ServletException("Ohhh, ein anderer Benutzer hat diesen Namen schon ausgewählt")
        }

        user.password = passwordEncoder.encode(user.password)
        return this.userServiceController.registerNewUser(user)
    }

    override fun findByName(name: String): User? {
        return this.userServiceController.findByName(name)
    }

    override fun findByEmail(email: String): User? {
        return this.userServiceController.findByEmail(email)
    }

    override fun findById(id: Int): User? {
        return this.userServiceController.findById(id)
    }

    override fun update(user: User): User? {
        return this.userServiceController.update(user)
    }

    private fun emailExists(email: String): Boolean {
        return userServiceController.findByEmail(email) != null
    }
}


@Component
open class UserServiceController @Autowired constructor(val dsl: DSLContext, val teamService: TeamService) {
    fun registerNewUser(user: User): Int {
        val userAccountRecord = dsl.insertInto(UserAccount.USER_ACCOUNT)
                .set(UserAccount.USER_ACCOUNT.EMAIL, user.email)
                .set(UserAccount.USER_ACCOUNT.PASSWORD, user.password)
                .returning(UserAccount.USER_ACCOUNT.ID)
                .fetchOne()

        return userAccountRecord.getValue(UserAccount.USER_ACCOUNT.ID, Int::class.java)
    }

    fun create(user: User): User? {
        val userAccountRecord: UserAccountRecord

        if (user.team != null && user.position != null) {
            userAccountRecord = dsl.insertInto(UserAccount.USER_ACCOUNT)
                    .set(UserAccount.USER_ACCOUNT.FIRST_NAME, user.firstName)
                    .set(UserAccount.USER_ACCOUNT.LAST_NAME, user.lastName)
                    .set(UserAccount.USER_ACCOUNT.USER_NAME, user.userName)
                    .set(UserAccount.USER_ACCOUNT.PASSWORD, user.password)
                    .set(UserAccount.USER_ACCOUNT.EMAIL, user.email)
                    .set(UserAccount.USER_ACCOUNT.TEAM_ID, user.team!!.id)
                    .set(UserAccount.USER_ACCOUNT.POSITION, user.position!!.toString())
                    .returning(UserAccount.USER_ACCOUNT.ID)
                    .fetchOne()
        } else {
            userAccountRecord = dsl.insertInto(UserAccount.USER_ACCOUNT)
                    .set(UserAccount.USER_ACCOUNT.FIRST_NAME, user.firstName)
                    .set(UserAccount.USER_ACCOUNT.LAST_NAME, user.lastName)
                    .set(UserAccount.USER_ACCOUNT.USER_NAME, user.userName)
                    .set(UserAccount.USER_ACCOUNT.PASSWORD, user.password)
                    .set(UserAccount.USER_ACCOUNT.EMAIL, user.email)
                    .returning(UserAccount.USER_ACCOUNT.ID)
                    .fetchOne()
        }


        user.id = userAccountRecord.id
        return user
    }

    fun findByName(name: String): User? {
        return getEntity(dsl.selectFrom(UserAccount.USER_ACCOUNT)
                .where(UserAccount.USER_ACCOUNT.USER_NAME.eq(name)).fetchOne())
    }

    fun findByEmail(email: String): User? {
        return getEntity(dsl.selectFrom(UserAccount.USER_ACCOUNT)
                .where(UserAccount.USER_ACCOUNT.EMAIL.eq(email)).fetchOne())
    }

    fun findById(id: Int): User? {
        return getEntity(dsl.selectFrom(UserAccount.USER_ACCOUNT)
                .where(UserAccount.USER_ACCOUNT.ID.eq(id)).fetchOne())
    }

    fun update(user: User): User? {
        dsl.update(UserAccount.USER_ACCOUNT)
                .set(UserAccount.USER_ACCOUNT.EMAIL, user.email)
                .set(UserAccount.USER_ACCOUNT.FIRST_NAME, user.firstName)
                .set(UserAccount.USER_ACCOUNT.LAST_NAME, user.lastName)
                .set(UserAccount.USER_ACCOUNT.POSITION, user.position!!.toString())
                .set(UserAccount.USER_ACCOUNT.TEAM_ID, user.teamId)
                .set(UserAccount.USER_ACCOUNT.BACKNUMBER, user.backNumber)
                .where(UserAccount.USER_ACCOUNT.ID.eq(user.id))

        return user
    }

    private fun getEntity(userAccountRecord: UserAccountRecord?): User? {
        if (userAccountRecord != null) {
            val user = User(userAccountRecord)
            val teamId = userAccountRecord.teamId!!
            if (teamId > 0) {
                val teamOptional = teamService.findById(teamId)
                teamOptional.ifPresent { user.team = it }
            }
            return user
        }

        return null
    }
}