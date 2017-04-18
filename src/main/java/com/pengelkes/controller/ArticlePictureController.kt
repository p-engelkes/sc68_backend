package com.pengelkes.controller

import com.pengelkes.service.ArticlePicture
import com.pengelkes.service.ArticlePictureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.imageio.ImageIO

/**
 * Created by pengelkes on 18.04.2017.
 */
@RestController
@RequestMapping("/api")
class ArticlePictureController @Autowired constructor(private val articlePictureService: ArticlePictureService) {
    @RequestMapping(value = "/articlePictures/{articleId}/upload", method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE))
    fun upload(@RequestPart("file") file: MultipartFile, @PathVariable articleId: Int): Boolean {
        if (!file.isEmpty) {
            val bufferedImage = ImageIO.read(file.inputStream)
            val articlePicture = ArticlePicture(
                    picture = Base64.getEncoder().encodeToString(file.bytes),
                    width = bufferedImage.width,
                    height = bufferedImage.height,
                    ratio = bufferedImage.width.toFloat().div(bufferedImage.height.toFloat()),
                    articleId = articleId
            )

            articlePictureService.createByArticle(articlePicture)
            return true
        }

        return false
    }

    @RequestMapping(value = "/articlePictures/{articleId}", method = arrayOf(RequestMethod.GET))
    fun findByArticle(@PathVariable articleId: Int): List<ArticlePicture> =
            articlePictureService.findByArticle(articleId)

    @RequestMapping(value = "/articlePictures/{pictureId}", method = arrayOf(RequestMethod.DELETE))
    fun deleteById(@PathVariable pictureId: Int) =
            articlePictureService.deleteById(pictureId)
}