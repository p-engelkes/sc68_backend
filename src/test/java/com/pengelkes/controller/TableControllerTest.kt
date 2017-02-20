package com.pengelkes.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pengelkes.ControllerTestCase
import com.pengelkes.service.Table
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by pengelkes on 20.02.2017.
 */
class TableControllerTest : ControllerTestCase() {

    @Test
    fun testFindByTeam() {
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/table?teamId=1"))
                .andDo { MockMvcResultHandlers.print() }
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8) }
                .andReturn()

        val expected = databaseTable
        val mapper = ObjectMapper()
        val json = mockResult.response.contentAsString
        val returnedTable = mapper.readValue(json, Table::class.java)
        returnedTable.should.equal(expected)
    }
}