package com.pengelkes.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pengelkes.ControllerTestCase
import com.pengelkes.service.OldClass
import com.winterbe.expekt.should
import org.junit.Ignore
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by pengelkes on 10.03.2017.
 */
class OldClassControllerTest : ControllerTestCase() {

    @Test
    @Ignore
    fun testFindAllWithTeamsAndArticles() {
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/oldClasses?articles=true"))
                .andDo { MockMvcResultHandlers.print() }
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8) }
                .andReturn()

        val expected = listOf(oldClassOne)
        val mapper = ObjectMapper()
        val json = mockResult.response.contentAsString
        val returnedOldClasses = mapper.readValue(json, Array<OldClass>::class.java)
        returnedOldClasses.asList().should.equal(expected)
    }

    @Test
    fun testFindAllWithTeams() {
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/oldClasses"))
                .andDo { MockMvcResultHandlers.print() }
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8) }
                .andReturn()

        val expected = listOf(oldClassOne, oldClassTwo)
        val mapper = ObjectMapper()
        val json = mockResult.response.contentAsString
        val returnedOldClasses = mapper.readValue(json, Array<OldClass>::class.java)
        returnedOldClasses.asList().should.equal(expected)
    }
}