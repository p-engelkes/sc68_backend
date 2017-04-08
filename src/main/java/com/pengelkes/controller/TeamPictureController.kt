package com.pengelkes.controller

import com.pengelkes.service.TeamPicture
import com.pengelkes.service.TeamPictureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.imageio.ImageIO

/**
 * Created by patrickengelkes on 27/03/2017.
 */
@RestController
@RequestMapping("/api")
class TeamPictureController @Autowired constructor(private val teamPictureService: TeamPictureService) {
    @RequestMapping(value = "/teamPictures/{teamId}/upload", method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE))
    fun upload(@RequestPart("file") file: MultipartFile, @PathVariable teamId: Int): Boolean {
        if (!file.isEmpty) {
            val bufferedImage = ImageIO.read(file.inputStream)
            val teamPicture = TeamPicture(
                    picture = Base64.getEncoder().encodeToString(file.bytes),
                    width = bufferedImage.width,
                    height = bufferedImage.height,
                    ratio = bufferedImage.width.toFloat().div(bufferedImage.height.toFloat()),
                    teamId = teamId
            )

            teamPictureService.createByTeam(teamPicture)
            return true
        }

        return false
    }

    @RequestMapping(value = "/teamPictures/{teamId}", method = arrayOf(RequestMethod.GET))
    fun findByTeam(@PathVariable teamId: Int): List<TeamPicture> {
        return teamPictureService.findByTeam(teamId)
    }

    @RequestMapping(value = "/teamPictures/{pictureId}", method = arrayOf(RequestMethod.DELETE))
    fun deleteById(@PathVariable pictureId: Int) {
        return teamPictureService.deleteById(pictureId)
    }
}