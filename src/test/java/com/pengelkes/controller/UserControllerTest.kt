package com.pengelkes.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pengelkes.ControllerTestCase
import com.pengelkes.service.User
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by pengelkes on 12.01.2017.
 */
class UserControllerTest : ControllerTestCase() {

    @Test
    fun testGetUserById() {
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()

        User(mockResult.response.contentAsString).should.equal(databaseUser)
    }

    @Test
    fun testUpdateUser() {
        val updateUser = databaseUser
        updateUser.firstName = "Peter"
        updateUser.lastName = "Pan"
        updateUser.backNumber = 8
        val json = ObjectMapper().writeValueAsString(updateUser)

        val mockResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        User(mockResult.response.contentAsString).should.equal(updateUser)
    }

    @Test
    fun testRegisterUser() {
        val newUser = User("new@user.com", "test")
        val json = ObjectMapper().writeValueAsString(newUser)

        val mockResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        mockResult.response.contentAsString.toInt().should.equal(2)
    }

    @Test
    fun getAllPositions() {
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/positions"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        val expectedPositions = "[\"GOALKEEPER\",\"DEFENSE\",\"MIDFIELD\",\"OFFENSE\",\"COACH\",\"SUPERVISOR\"]"

        mockResult.response.contentAsString.should.equal(expectedPositions)
    }
}