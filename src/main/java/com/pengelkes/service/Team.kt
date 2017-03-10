package com.pengelkes.service

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ObjectNode
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
        val TRAINING_TIMES_JSON = "training_times"
        val TRAINING_DAY_JSON = "day"
        val TRAINING_TIME_JSON = "time"
        val NAME_JSON = "name"

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


        val team = Team()
        team.name = teamNameNode.asText()
        team.trainingTimes = trainingTimes
        return team
    }
}

@Service
interface TeamService {
    @Throws(ServletException::class)
    fun create(team: Team): Int
    fun findByName(name: String): Team?
    fun findById(id: Int): Team?
    fun findByOldClass(oldCLassId: Int): List<Team>
    fun findAll(): List<Team>
}

@Service
@Transactional
open class TeamServiceImpl
@Autowired
constructor(private val teamServiceController: TeamServiceController) : TeamService {
    @Throws(ServletException::class)
    override fun create(team: Team): Int {
        if (nameExists(team.name)) {
            throw ServletException("Es existiert bereits ein Team mit dem ausgewähten Namen")
        }

        return teamServiceController.create(team)
    }

    override fun findByName(name: String) = teamServiceController.findByName(name)

    override fun findById(id: Int) = teamServiceController.findById(id)

    override fun findAll() = teamServiceController.findAll()

    override fun findByOldClass(oldCLassId: Int): List<Team> = teamServiceController.findByOldClass(oldCLassId)

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
constructor(private val dsl: DSLContext) {

    fun create(team: Team): Int {
        val record = dsl.insertInto(TEAM)
                .set(TEAM.NAME, team.name)
                .set(TEAM.TRAINING_TIMES, team.trainingTimes)
                .returning(TEAM.ID)
                .fetchOne()

        return record.id
    }

    fun findByName(name: String) = getEntity(dsl.selectFrom(TEAM).where(TEAM.NAME.eq(name)).fetchOne())

    fun findById(id: Int) = getEntity(dsl.selectFrom(TEAM).where(TEAM.ID.eq(id)).fetchOne())

    fun findByOldClass(oldCLassId: Int?): List<Team> {
        return getEntities(dsl.selectFrom(TEAM)
                .where(TEAM.OLD_CLASS_ID.eq(oldCLassId))
                .orderBy(TEAM.ORDER_NUMBER.asc())
                .fetch())
    }

    fun findAll(): List<Team> {
        return getEntities(dsl.selectFrom(TEAM).fetch())
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
