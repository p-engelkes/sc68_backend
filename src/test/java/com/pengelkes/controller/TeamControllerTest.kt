package com.pengelkes.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pengelkes.SpringTestCase
import com.pengelkes.service.Team
import com.winterbe.expekt.should
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
 * Created by patrickengelkes on 12/01/2017.
 */
class TeamControllerTest : SpringTestCase() {

    @Autowired
    lateinit var context: WebApplicationContext

    lateinit var mockMvc: MockMvc

    override fun setup() {
        super.setup()
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        setUserAuthenticationForTesting()
    }

    @Test
    fun testGetAllTeams() {
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/teams"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()

        val expected = listOf<Team>(databaseTeam)
        val mapper = ObjectMapper()
        val json = mockResult.response.contentAsString
        val returnedTeams = mapper.readValue(json, Array<Team>::class.java)
        returnedTeams.asList().should.equal(expected)
    }

    @Test
    @Ignore
            //TODO: Create team from json and reactivate this test
    fun testCreateTeam() {
        val team = Team(name = "Herren 2. Mannschaft",
                trainingTimes = hashMapOf(Pair("Friday", "17:30"), Pair("Wednesday", "19:00")))
        val teamJson = ObjectMapper().writeValueAsString(team)

        val mockResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(teamJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        mockResult.response.contentAsString.toInt().should.equal(2)
    }
}