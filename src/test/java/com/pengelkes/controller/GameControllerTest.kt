package com.pengelkes.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pengelkes.ControllerTestCase
import com.pengelkes.service.Game
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by pengelkes on 20.02.2017.
 */
class GameControllerTest : ControllerTestCase() {

    @Test
    fun testFindByTeamAndType() {
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/games?teamId=1&gameType=PREVIOUS"))
                .andDo { MockMvcResultHandlers.print() }
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8) }
                .andReturn()

        val expected = listOf(databaseGameOne, databaseGameTwo)
        val mapper = ObjectMapper()
        val json = mockResult.response.contentAsString
        val returnedGames = mapper.readValue(json, Array<Game>::class.java)
        returnedGames.asList().should.equal(expected)
    }
}