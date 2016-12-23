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
import org.jooq.Record
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
    var users: List<User>? = null
    var trainingTimes: HashMap<String, String>? = null
    var created: Date? = null

    constructor() {

    }

    constructor(name: String, trainingTimes: HashMap<String, String>) {
        this.name = name
        this.trainingTimes = trainingTimes
    }

    constructor(id: Int, name: String, trainingTimes: HashMap<String, String>) {
        this.id = id
        this.name = name
        this.trainingTimes = trainingTimes
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
    fun create(team: Team): Team?

    fun findByName(name: String): Team?
    fun findById(id: Int): Team?
    fun getAllTeams(): List<Team>
}

@Service
@Transactional
open class TeamServiceImpl
@Autowired
constructor(private val teamServiceController: TeamServiceController) : TeamService {

    @Throws(ServletException::class)
    override fun create(team: Team): Team? {
        if (nameExists(team.name)) {
            throw ServletException("Es existiert bereits ein Team mit dem ausgewähten Namen")
        }

        return teamServiceController.create(team)
    }

    override fun findByName(name: String): Team? {
        return teamServiceController.findByName(name)
    }

    override fun findById(id: Int): Team? {
        return teamServiceController.findById(id)
    }

    override fun getAllTeams(): List<Team> = teamServiceController.getAllTeams()

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

    fun create(team: Team): Team? {
        val record = dsl.insertInto(TEAM)
                .set(TEAM.NAME, team.name)
                .set(TEAM.TRAINING_TIMES, team.trainingTimes)
                .returning(TEAM.ID)
                .fetchOne()

        team.id = record.id
        return team
    }

    fun findByName(name: String): Team? {
        return getEntity(dsl.selectFrom(TEAM).where(TEAM.NAME.eq(name)).fetchOne())
    }

    fun findById(id: Int): Team? {
        return getEntity(dsl.selectFrom(TEAM).where(TEAM.ID.eq(id)).fetchOne())
    }

    fun getAllTeams(): List<Team> = getEntities(dsl.selectFrom(TEAM).fetch())

    private fun getEntity(record: Record?): Team? {
        if (record != null) {
            val id = record.getValue(TEAM.ID, Int::class.java)
            val name = record.getValue(TEAM.NAME, String::class.java)
            val trainingTimes: HashMap<String, String> = record.getValue(TEAM.TRAINING_TIMES, HashMap::class.java) as HashMap<String, String>

            return Team(id, name, trainingTimes)
        }

        return null
    }

    private fun getEntities(result: Result<TeamRecord>): List<Team> {
        val allTeams = mutableListOf<Team>()
        result.forEach { getEntity(it)?.let { allTeams.add(it) } }

        return allTeams
    }
}
