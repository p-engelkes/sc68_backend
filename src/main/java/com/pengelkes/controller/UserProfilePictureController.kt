package com.pengelkes.controller

import com.pengelkes.service.ProfilePicture
import com.pengelkes.service.ProfilePictureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.imageio.ImageIO

/**
 * Created by pengelkes on 04.01.2017.
 */
@RestController
@RequestMapping("/api")
class UserProfilePictureController @Autowired constructor(private val profilePictureService: ProfilePictureService) {

    @RequestMapping(value = "/profilePictures/{id}/upload", method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE))
    fun uplaodProfilePicture(
            @RequestPart("file") file: MultipartFile,
            @PathVariable id: Int): Boolean {
        if (!file.isEmpty) {
            val bufferedImage = ImageIO.read(file.inputStream)
            val profilePicture = ProfilePicture(
                    id,
                    Base64.getEncoder().encodeToString(file.bytes),
                    bufferedImage.width,
                    bufferedImage.height,
                    bufferedImage.width.toFloat().div(bufferedImage.height.toFloat())
            )

            profilePictureService.add(profilePicture)
            return true;
        } else {
            return false;
        }
    }
}