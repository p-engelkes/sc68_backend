package com.pengelkes.controller

import com.pengelkes.SpringTestCase
import com.pengelkes.service.User
import com.winterbe.expekt.should
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
 * Created by pengelkes on 12.01.2017.
 */
class UserControllerTest : SpringTestCase() {

    @Autowired
    lateinit var context: WebApplicationContext

    lateinit var mockMvc: MockMvc

    override fun setup() {
        super.setup()
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        setUserAuthenticationForTesting()
    }

    @Test
    fun testGetUserById() {
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()

        User(mockResult.response.contentAsString).should.equal(databaseUser)
    }
}