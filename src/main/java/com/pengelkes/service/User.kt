package com.pengelkes.service

import com.pengelkes.backend.jooq.tables.UserAccount.USER_ACCOUNT
import com.pengelkes.backend.jooq.tables.records.UserAccountRecord
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
    var teamId: Int? = null
    var created: Date? = null
    var backNumber: Int = 0
    var profilePicture: ProfilePicture? = null

    //empty constructor needed for jackson
    constructor() {}

    constructor(email: String, password: String, team: Team) {
        this.email = email
        this.password = password
        this.team = team
    }

    constructor(userAccountRecord: UserAccountRecord) {
        this.id = userAccountRecord.getValue<Int>(USER_ACCOUNT.ID, Int::class.java)
        this.userName = userAccountRecord.getValue<String>(USER_ACCOUNT.USER_NAME, String::class.java)
        this.firstName = userAccountRecord.getValue<String>(USER_ACCOUNT.FIRST_NAME, String::class.java)
        this.lastName = userAccountRecord.getValue<String>(USER_ACCOUNT.LAST_NAME, String::class.java)
        this.password = userAccountRecord.getValue<String>(USER_ACCOUNT.PASSWORD, String::class.java)
        this.email = userAccountRecord.getValue<String>(USER_ACCOUNT.EMAIL, String::class.java)
        this.position = userAccountRecord.getValue<Position>(USER_ACCOUNT.POSITION, Position::class.java)
        this.teamId = userAccountRecord.getValue<Int>(USER_ACCOUNT.TEAM_ID, Int::class.java)
        this.backNumber = userAccountRecord.getValue<Int>(USER_ACCOUNT.BACKNUMBER, Int::class.java)
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

    override fun findByName(name: String) = this.userServiceController.findByName(name)
    override fun findByEmail(email: String) = this.userServiceController.findByEmail(email)
    override fun findById(id: Int) = this.userServiceController.findById(id)
    override fun update(user: User) = this.userServiceController.update(user)
    private fun emailExists(email: String) = userServiceController.findByEmail(email) != null
}


@Component
open class UserServiceController @Autowired constructor(
        val dsl: DSLContext,
        val teamService: TeamService,
        val profilePictureService: ProfilePictureService) {
    fun registerNewUser(user: User): Int {
        val userAccountRecord = dsl.insertInto(USER_ACCOUNT)
                .set(USER_ACCOUNT.EMAIL, user.email)
                .set(USER_ACCOUNT.PASSWORD, user.password)
                .returning(USER_ACCOUNT.ID)
                .fetchOne()

        return userAccountRecord.getValue(USER_ACCOUNT.ID, Int::class.java)
    }

    fun create(user: User): User? {
        val userAccountRecord: UserAccountRecord

        if (user.team != null && user.position != null) {
            userAccountRecord = dsl.insertInto(USER_ACCOUNT)
                    .set(USER_ACCOUNT.FIRST_NAME, user.firstName)
                    .set(USER_ACCOUNT.LAST_NAME, user.lastName)
                    .set(USER_ACCOUNT.USER_NAME, user.userName)
                    .set(USER_ACCOUNT.PASSWORD, user.password)
                    .set(USER_ACCOUNT.EMAIL, user.email)
                    .set(USER_ACCOUNT.TEAM_ID, user.team!!.id)
                    .set(USER_ACCOUNT.POSITION, user.position!!.toString())
                    .returning(USER_ACCOUNT.ID)
                    .fetchOne()
        } else {
            userAccountRecord = dsl.insertInto(USER_ACCOUNT)
                    .set(USER_ACCOUNT.FIRST_NAME, user.firstName)
                    .set(USER_ACCOUNT.LAST_NAME, user.lastName)
                    .set(USER_ACCOUNT.USER_NAME, user.userName)
                    .set(USER_ACCOUNT.PASSWORD, user.password)
                    .set(USER_ACCOUNT.EMAIL, user.email)
                    .returning(USER_ACCOUNT.ID)
                    .fetchOne()
        }


        user.id = userAccountRecord.id
        return user
    }

    fun findByName(name: String) = getEntity(dsl.selectFrom(USER_ACCOUNT)
            .where(USER_ACCOUNT.USER_NAME.eq(name)).fetchOne())

    fun findByEmail(email: String) = getEntity(dsl.selectFrom(USER_ACCOUNT)
                .where(USER_ACCOUNT.EMAIL.eq(email)).fetchOne())

    fun findById(id: Int) = getEntity(dsl.selectFrom(USER_ACCOUNT)
                .where(USER_ACCOUNT.ID.eq(id)).fetchOne())

    fun update(user: User): User? {
        dsl.update(USER_ACCOUNT)
                .set(USER_ACCOUNT.EMAIL, user.email)
                .set(USER_ACCOUNT.FIRST_NAME, user.firstName)
                .set(USER_ACCOUNT.LAST_NAME, user.lastName)
                .set(USER_ACCOUNT.POSITION, user.position?.toString() )
                .set(USER_ACCOUNT.TEAM_ID, user.teamId)
                .set(USER_ACCOUNT.BACKNUMBER, user.backNumber)
                .where(USER_ACCOUNT.ID.eq(user.id))
                .execute()

        return user
    }

    private fun getEntity(userAccountRecord: UserAccountRecord?): User? {
        if (userAccountRecord != null) {
            val user = User(userAccountRecord)
            userAccountRecord.teamId?.let { teamService.findById(it)?.let { user.team = it } }
            profilePictureService.findById(user.id)?.let { user.profilePicture = it }
            return user
        }

        return null
    }
}