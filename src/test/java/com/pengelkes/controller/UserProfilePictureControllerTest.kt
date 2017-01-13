package com.pengelkes.controller

import com.pengelkes.SpringTestCase
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


/**
 * Created by pengelkes on 13.01.2017.
 */
class UserProfilePictureControllerTest : SpringTestCase() {
    @Autowired
    lateinit var context: WebApplicationContext

    lateinit var mockMvc: MockMvc

    override fun setup() {
        super.setup()
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        setUserAuthenticationForTesting()
    }

    @Test
    fun testUploadProfilePicture() {
        val testImage = javaClass.classLoader.getResource("files/1to1.png")
        val mockImage = MockMultipartFile("file", "1to1.png", MediaType.MULTIPART_FORM_DATA_VALUE, testImage.readBytes())
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/profilePictures/1/upload")
                .file(mockImage))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        mockResult.response.contentAsString.toBoolean().should.be.`true`
    }
}
