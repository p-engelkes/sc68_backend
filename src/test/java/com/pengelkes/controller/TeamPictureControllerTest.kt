package com.pengelkes.controller

import com.pengelkes.ControllerTestCase
import com.pengelkes.service.TeamPicture
import com.pengelkes.service.TeamPictureService
import com.winterbe.expekt.should
import org.codehaus.jackson.map.ObjectMapper
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by pengelkes on 28.03.2017.
 */
class TeamPictureControllerTest : ControllerTestCase() {

    @Autowired
    lateinit var teamPictureService: TeamPictureService

    @Test
    fun testUploadProfilePicture() {
        val testImage = javaClass.classLoader.getResource("files/1to1.png")
        val mockImage = MockMultipartFile("file", "1to1.png", MediaType.MULTIPART_FORM_DATA_VALUE, testImage.readBytes())
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/teamPictures/1/upload")
                .file(mockImage))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        mockResult.response.contentAsString.toBoolean().should.be.`true`
        teamPictureService.findByTeam(1).size.should.equal(2)
    }

    @Test
    fun testFindByTeam() {
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/teamPictures/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()

        val expected = listOf(teamPicture)
        val json = mockResult.response.contentAsString
        val returnedTeamPictures = ObjectMapper().readValue(json, Array<TeamPicture>::class.java)
        returnedTeamPictures.asList().should.equal(expected)
    }
}
