package com.pengelkes.service

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import java.io.IOException
import java.util.*

/**
 * Created by pengelkes on 29.04.2017.
 */
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
        val orderNumber = nodes.get(Team.ORDER_NUMBER_JSON)

        val team = Team()
        id?.let { team.id = it.asInt() }
        team.name = teamNameNode.asText()
        team.soccerInfoId = soccerIdNode.asText()
        team.oldClassId = oldClassIdNode.asInt()
        team.orderNumber = orderNumber.asInt()
        team.trainingTimes = trainingTimes
        return team
    }
}

class UserDeserializer : JsonDeserializer<User>() {
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext?): User {
        val nodes = jsonParser.codec.readTree<ObjectNode>(jsonParser)
        val idNode = nodes.get(User.id)
        val userNameNode = nodes.get(User.userName)
        val firstNameNode = nodes.get(User.firstName)
        val lastNameNode = nodes.get(User.lastName)
        val passwordNode = nodes.get(User.password)
        val emailNode = nodes.get(User.email)
        val positionNode = nodes.get(User.position)
        val teamIdNode = nodes.get(User.teamId)
        val backNumberNode = nodes.get(User.backNumber)
        val articleWriterNode = nodes.get(User.articleWriter)

        val user = User()
        idNode?.let { user.id = it.asInt() }
        userNameNode?.let { user.userName = it.asText() }
        firstNameNode?.let { user.firstName = it.asText() }
        lastNameNode?.let { user.lastName = it.asText() }
        passwordNode?.let { user.password = it.asText() }
        emailNode?.let { user.email = it.asText() }
        positionNode?.let { Position.fromTranslation(it.asText())?.let { user.position = it } }
        teamIdNode?.let { user.teamId = it.asInt() }
        backNumberNode?.let { user.backNumber = it.asInt() }
        articleWriterNode?.let { user.articleWriter = it.asBoolean() }

        return user
    }
}

fun JsonNode?.asSafeInt(): Int? {
    this?.let {
        return it.asInt()
    }

    return null
}

fun JsonNode?.asSafeText(): String? {
    this?.let {
        return it.asText()
    }

    return null
}

fun JsonNode?.asSafeBoolean(): Boolean? {
    this?.let {
        return it.asBoolean()
    }

    return null
}