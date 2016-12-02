package com.pengelkes.service.team;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;

import static com.pengelkes.service.team.Team.*;

public class TeamDeserializer extends JsonDeserializer<Team>
{

    @Override
    public Team deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException
    {
        HashMap<String, String> trainingTimes = new HashMap<>();
        ObjectNode nodes = jsonParser.getCodec().readTree(jsonParser);
        JsonNode trainingTimesNode = nodes.get(TRAINING_TIMES_JSON);
        for (int i = 0; i < trainingTimesNode.size(); i++)
        {
            JsonNode dayNode = trainingTimesNode.get(i);
            String key = dayNode.get(TRAINING_DAY_JSON).asText();
            String value = dayNode.get(TRAINING_TIME_JSON).asText();
            trainingTimes.put(key, value);
        }
        JsonNode teamNameNode = nodes.get(NAME_JSON);


        final Team team = new Team();
        team.setName(teamNameNode.asText());
        team.setTrainingTimes(trainingTimes);
        return team;
    }
}