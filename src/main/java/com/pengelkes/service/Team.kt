package com.pengelkes.service

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ObjectNode
import com.pengelkes.backend.jooq.Tables.USER_ACCOUNT
import com.pengelkes.backend.jooq.tables.Article.ARTICLE
import com.pengelkes.backend.jooq.tables.Team.TEAM
import com.pengelkes.backend.jooq.tables.records.TeamRecord
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.util.*
import javax.servlet.ServletException

/**
 * Created by pengelkes on 23.12.2016.
 */
class Team {

    var id: Int = 0
    var name: String? = null
    var trainingTimes: HashMap<String, String>? = null
    var soccerInfoId: String? = null
    var orderNumber: Int? = null
    var oldClassId: Int? = null
    var articles: MutableList<Article> = mutableListOf()
    var created: Date? = null

    constructor()

    constructor(id: Int = 0, name: String, trainingTimes: HashMap<String, String>, teamId: String? = null,
                orderNumber: Int? = null, oldClassId: Int? = null) {
        this.id = id
        this.name = name
        this.trainingTimes = trainingTimes
        this.soccerInfoId = teamId
        this.orderNumber = orderNumber
        this.oldClassId = oldClassId
    }

    constructor(teamRecord: TeamRecord) {
        this.id = teamRecord.id
        this.name = teamRecord.name
        this.trainingTimes = teamRecord.trainingTimes
        this.soccerInfoId = teamRecord.teamId
        this.orderNumber = teamRecord.orderNumber
        this.oldClassId = teamRecord.oldClassId
    }

    companion object {
        val ID_JSON = "id"
        val TRAINING_TIMES_JSON = "trainingTimes"
        val TRAINING_DAY_JSON = "day"
        val TRAINING_TIME_JSON = "time"
        val NAME_JSON = "name"
        val SOCCER_ID_JSON = "soccerId"
        val OLD_CLASS_ID_JSON = "oldClass"

        fun fromJson(json: String): Team? {
            val mapper = ObjectMapper()
            val module = SimpleModule()
            module.addDeserializer(Team::class.java, TeamDeserializer())
            mapper.registerModule(module)
            try {
                return mapper.readValue(json, Team::class.java)
            } catch (e: IOException) {
                return null
            }

        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Team

        if (id != other.id) return false
        if (name != other.name) return false
        if (trainingTimes != other.trainingTimes) return false
        if (soccerInfoId != other.soccerInfoId) return false
        if (orderNumber != other.orderNumber) return false
        if (oldClassId != other.oldClassId) return false
        if (created != other.created) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (trainingTimes?.hashCode() ?: 0)
        result = 31 * result + (soccerInfoId?.hashCode() ?: 0)
        result = 31 * result + (orderNumber ?: 0)
        result = 31 * result + (oldClassId ?: 0)
        result = 31 * result + (created?.hashCode() ?: 0)
        return result
    }


}

class TeamDeserializer : JsonDeserializer<Team>() {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Team {
        val trainingTimes = HashMap<String, String>()
        val nodes = jsonParser.codec.readTree<ObjectNode>(jsonParser)
        val trainingTimesNode = nodes.get(Team.TRAINING_TIMES_JSON)
        for (i in 0..trainingTimesNode.size() - 1) {
            val dayNode = trainingTimesNode.get(i)
            val key = dayNode.get(Team.TRAINING_DAY_JSON).asText()
            val value = dayNode.get(Team.TRAINING_TIME_JSON).asText()
            trainingTimes.put(key, value)
        }
        val teamNameNode = nodes.get(Team.NAME_JSON)
        val soccerIdNode = nodes.get(Team.SOCCER_ID_JSON)
        val id = nodes.get(Team.ID_JSON)
        val oldClassIdNode = nodes.get(Team.OLD_CLASS_ID_JSON)

        val team = Team()
        id?.let { team.id = it.asInt() }
        team.name = teamNameNode.asText()
        team.soccerInfoId = soccerIdNode.asText()
        team.oldClassId = oldClassIdNode.asInt()
        team.trainingTimes = trainingTimes
        return team
    }
}

@Service
interface TeamService {
    @Throws(ServletException::class)
    fun create(team: Team): Team

    fun findByName(name: String): Team?
    fun findById(id: Int): Team?
    fun findByOldClass(oldCLassId: Int): List<Team>
    fun findByOldClassWithArticle(oldCLassId: Int): List<Team>
    fun findAll(): List<Team>
    fun findAllPlayersByTeam(teamId: Int): List<User>
    fun update(team: Team): Team
}

@Service
@Transactional
open class TeamServiceImpl
@Autowired
constructor(private val teamServiceController: TeamServiceController) : TeamService {
    @Throws(ServletException::class)
    override fun create(team: Team): Team {
        if (nameExists(team.name)) {
            throw ServletException("Es existiert bereits ein Team mit dem ausgewähten Namen")
        }

        return teamServiceController.create(team)
    }

    override fun findByName(name: String) = teamServiceController.findByName(name)

    override fun findById(id: Int) = teamServiceController.findById(id)

    override fun findAll() = teamServiceController.findAll()

    override fun findByOldClass(oldCLassId: Int): List<Team> = teamServiceController.findByOldClass(oldCLassId)

    override fun findByOldClassWithArticle(oldCLassId: Int): List<Team> = teamServiceController.findByOldClassWithArticle(oldCLassId)

    override fun findAllPlayersByTeam(teamId: Int): List<User> = teamServiceController.findAllPlayersByTeam(teamId)

    override fun update(team: Team): Team = teamServiceController.update(team);

    private fun nameExists(name: String?): Boolean {
        if (name != null) {
            return teamServiceController.findByName(name) != null
        }
        return false
    }
}

@Component
open class TeamServiceController
@Autowired
constructor(private val dsl: DSLContext,
            private val profilePictureService: ProfilePictureService) {
    fun create(team: Team): Team {
        val record = dsl.insertInto(TEAM)
                .set(TEAM.NAME, team.name)
                .set(TEAM.TRAINING_TIMES, team.trainingTimes)
                .set(TEAM.OLD_CLASS_ID, team.oldClassId)
                .set(TEAM.TEAM_ID, team.soccerInfoId)
                .returning(TEAM.ID)
                .fetchOne()

        team.id = record.id;

        return team
    }

    fun update(team: Team): Team {
        dsl.update(TEAM)
                .set(TEAM.NAME, team.name)
                .set(TEAM.TRAINING_TIMES, team.trainingTimes)
                .set(TEAM.OLD_CLASS_ID, team.oldClassId)
                .set(TEAM.TEAM_ID, team.soccerInfoId)
                .where(TEAM.ID.eq(team.id))
                .execute()

        return team
    }

    fun findByName(name: String) = getEntity(dsl.selectFrom(TEAM).where(TEAM.NAME.eq(name)).fetchOne())

    fun findById(id: Int) = getEntity(dsl.selectFrom(TEAM).where(TEAM.ID.eq(id)).fetchOne())

    fun findByOldClass(oldCLassId: Int): List<Team> {
        return getEntities(dsl.selectFrom(TEAM)
                .where(TEAM.OLD_CLASS_ID.eq(oldCLassId))
                .orderBy(TEAM.ORDER_NUMBER.asc())
                .fetch())
    }

    fun findByOldClassWithArticle(oldCLassId: Int): List<Team> {
        val allTeams = mutableListOf<Team>()
        val allTeamsMap = mutableMapOf<Int, Team>()
        val allArticles = mutableListOf<Article>()

        val teamIdsWithAnArticle = dsl.selectDistinct(TEAM.ID).from(
                TEAM
                        .innerJoin(ARTICLE)
                        .on(TEAM.ID.eq(ARTICLE.TEAM_ID))
        ).where(TEAM.OLD_CLASS_ID.eq(oldCLassId)).fetch()

        teamIdsWithAnArticle.forEach { findById(it.value1())?.let { allTeamsMap.put(it.id, it) } }

        val allArticleRecords = dsl.select().from(
                TEAM
                        .innerJoin(ARTICLE)
                        .on(TEAM.ID.eq(ARTICLE.TEAM_ID))
        ).where(TEAM.OLD_CLASS_ID.eq(oldCLassId)).fetch()

        allArticleRecords.forEach { allArticles.add(Article(it)) }
        allArticles.forEach { article ->
            allTeamsMap[article.teamId]?.articles?.add(article)
        }

        allTeamsMap.forEach { allTeams.add(it.value) }
        allTeams.sortBy { it.orderNumber }

        return allTeams
    }


    fun findAll(): List<Team> {
        return getEntities(dsl.selectFrom(TEAM).fetch())
    }

    fun findAllPlayersByTeam(teamId: Int): List<User> {
        val players = mutableListOf<User>()

        dsl.selectFrom(USER_ACCOUNT).where(USER_ACCOUNT.TEAM_ID.eq(teamId)).forEach {
            it.getEntity(profilePictureService = profilePictureService)?.let { user ->
                findById(teamId)?.let { user.team = it }
                players.add(user)
            }
        }

        return players
    }

    private fun getEntity(teamRecord: TeamRecord?): Team? {
        if (teamRecord != null) {
            return Team(teamRecord)
        }

        return null
    }

    private fun getEntities(result: Result<TeamRecord>): List<Team> {
        val allTeams = mutableListOf<Team>()
        result.forEach { getEntity(it)?.let { allTeams.add(it) } }

        return allTeams
    }
}
