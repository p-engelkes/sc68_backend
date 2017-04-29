package com.pengelkes.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.pengelkes.backend.jooq.tables.UserAccount.USER_ACCOUNT
import com.pengelkes.backend.jooq.tables.records.UserAccountRecord
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
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
    var positionTranslation: String? = null
    var team: Team? = null
    var teamId: Int? = null
    var created: Date? = null
    var backNumber: Int? = null
    var profilePicture: ProfilePicture? = null
    var articleWriter: Boolean = false

    //empty constructor needed for jackson
    constructor() {
    }

    constructor(id: Int = 0, firstName: String? = null, lastName: String? = null, userName: String? = null,
                password: String? = null, email: String? = null, position: Position? = null, team: Team? = null,
                teamId: Int? = null, created: Date? = null, backNumber: Int? = null, profilePicture: ProfilePicture? = null) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.userName = userName
        this.password = password
        this.email = email
        this.position = position
        this.team = team
        this.teamId = teamId
        this.created = created
        this.backNumber = backNumber
        this.profilePicture = profilePicture
    }

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }

    constructor(json: String) {
        val mapper = ObjectMapper()
        val user = mapper.readValue(json, User::class.java)
        this.id = user.id
        this.firstName = user.firstName
        this.lastName = user.lastName
        this.password = user.password
        this.email = user.email
        this.position = user.position
        this.team = user.team
        this.teamId = user.teamId
        this.created = user.created
        this.backNumber = user.backNumber
        this.profilePicture = user.profilePicture
    }

    constructor(userAccountRecord: UserAccountRecord) {
        this.id = userAccountRecord.id
        this.userName = userAccountRecord.userName
        this.firstName = userAccountRecord.firstName
        this.lastName = userAccountRecord.lastName
        this.password = userAccountRecord.password
        this.email = userAccountRecord.email
        this.position = userAccountRecord.getValue<Position>(USER_ACCOUNT.POSITION, Position::class.java)
        this.teamId = userAccountRecord.teamId
        this.backNumber = userAccountRecord.backnumber
        this.articleWriter = userAccountRecord.articleWriter
    }

    companion object {
        val id = "id"
        val userName = "userName"
        val firstName = "firstName"
        val lastName = "lastName"
        val password = "password"
        val email = "email"
        val position = "position"
        val teamId = "teamId"
        val backNumber = "backNumber"
        val articleWriter = "articleWriter"

        fun fromJson(json: String): User? {
            val mapper = ObjectMapper()
            val module = SimpleModule()
            module.addDeserializer(User::class.java, UserDeserializer())
            mapper.registerModule(module)
            try {
                return mapper.readValue(json, User::class.java)
            } catch (e: IOException) {
                return null
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as User

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (userName != other.userName) return false
        if (password != other.password) return false
        if (email != other.email) return false
        if (position != other.position) return false
        if (team != other.team) return false
        if (teamId != other.teamId) return false
        if (backNumber != other.backNumber) return false
        if (profilePicture != other.profilePicture) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + (userName?.hashCode() ?: 0)
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (position?.hashCode() ?: 0)
        result = 31 * result + (team?.hashCode() ?: 0)
        result = 31 * result + (teamId ?: 0)
        result = 31 * result + (backNumber ?: 0)
        result = 31 * result + (profilePicture?.hashCode() ?: 0)
        return result
    }
}

@Service
interface UserService {
    @Throws(ServletException::class)
    fun registerNewUser(user: User): Int

    fun findAll(): List<User>
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

    override fun findAll() = this.userServiceController.findAll()
    override fun findByName(name: String) = this.userServiceController.findByName(name)
    override fun findByEmail(email: String) = this.userServiceController.findByEmail(email)
    override fun findById(id: Int) = this.userServiceController.findById(id)
    override fun update(user: User) = this.userServiceController.update(user)
    private fun emailExists(email: String) = userServiceController.findByEmail(email) != null
}


@Component
open class UserServiceController @Autowired constructor(val dsl: DSLContext,
                                                        val teamService: TeamService,
                                                        val profilePictureService: ProfilePictureService) {
    fun registerNewUser(user: User): Int {
        val userAccountRecord = dsl.insertInto(USER_ACCOUNT)
                .set(USER_ACCOUNT.EMAIL, user.email)
                .set(USER_ACCOUNT.PASSWORD, user.password)
                .returning(USER_ACCOUNT.ID)
                .fetchOne()

        return userAccountRecord.id
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

    fun findAll() = getEntities(dsl.selectFrom(USER_ACCOUNT).fetch())

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
                .set(USER_ACCOUNT.POSITION, user.position?.toString())
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

    private fun getEntities(result: Result<UserAccountRecord>): List<User> {
        val allUsers = mutableListOf<User>()
        result.forEach { getEntity(it)?.let { allUsers.add(it) } }

        return allUsers
    }
}

fun UserAccountRecord?.getEntity(teamService: TeamService? = null, profilePictureService: ProfilePictureService): User? {
    if (this != null) {
        val user = User(this)
        teamService?.let { this.teamId?.let { teamService.findById(it)?.let { user.team = it } } }
        profilePictureService.findById(user.id)?.let { user.profilePicture = it }
        return user
    }

    return null
}