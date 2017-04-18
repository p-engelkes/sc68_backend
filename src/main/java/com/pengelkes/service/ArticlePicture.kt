package com.pengelkes.service

import com.pengelkes.backend.jooq.tables.ArticlePicture.ARTICLE_PICTURE
import com.pengelkes.backend.jooq.tables.records.ArticlePictureRecord
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Created by pengelkes on 18.04.2017.
 */
class ArticlePicture {
    var id: Int? = null
    var picture: String? = null
    var width: Int? = null
    var height: Int? = null
    var ratio: Float? = null
    var createdAt: Date? = null
    var articleId: Int? = null

    constructor(id: Int? = null, picture: String? = null, width: Int? = null, height: Int? = null,
                ratio: Float? = null, createdAt: Date? = null, articleId: Int? = null) {
        this.id = id
        this.picture = picture
        this.width = width
        this.height = height
        this.ratio = ratio
        this.createdAt = createdAt
        this.articleId = articleId
    }

    constructor(articlePictureRecord: ArticlePictureRecord) {
        this.id = articlePictureRecord.id
        this.picture = articlePictureRecord.picture
        this.width = articlePictureRecord.width
        this.height = articlePictureRecord.height
        this.createdAt = articlePictureRecord.createdAt
        this.articleId = articleId
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as ArticlePicture

        if (id != other.id) return false
        if (picture != other.picture) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (ratio != other.ratio) return false
        if (createdAt != other.createdAt) return false
        if (articleId != other.articleId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (picture?.hashCode() ?: 0)
        result = 31 * result + (width ?: 0)
        result = 31 * result + (height ?: 0)
        result = 31 * result + (ratio?.hashCode() ?: 0)
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + (articleId ?: 0)
        return result
    }
}

@Service
interface ArticlePictureService {
    fun createByArticle(articlePicture: ArticlePicture)
    fun findByArticle(articleId: Int): List<ArticlePicture>
    fun deleteById(pictureId: Int)
}

@Service
@Transactional
open class ArticlePictureServiceImpl
@Autowired constructor(private val articlePictureServiceController: ArticlePictureServiceController) : ArticlePictureService {
    override fun createByArticle(articlePicture: ArticlePicture) =
            articlePictureServiceController.create(articlePicture)

    override fun findByArticle(articleId: Int): List<ArticlePicture> =
            articlePictureServiceController.findByArticle(articleId)

    override fun deleteById(pictureId: Int) =
            articlePictureServiceController.deleteById(pictureId)
}

@Component
open class ArticlePictureServiceController @Autowired constructor(val dsl: DSLContext) {
    fun create(articlePicture: ArticlePicture) {
        dsl.insertInto(ARTICLE_PICTURE)
                .set(ARTICLE_PICTURE.PICTURE, articlePicture.picture)
                .set(ARTICLE_PICTURE.WIDTH, articlePicture.width)
                .set(ARTICLE_PICTURE.HEIGHT, articlePicture.height)
                .set(ARTICLE_PICTURE.RATIO, articlePicture.ratio)
                .set(ARTICLE_PICTURE.ARTICLE_ID, articlePicture.articleId)
                .execute()
    }

    fun findByArticle(articleId: Int): List<ArticlePicture> {
        return getEntities(dsl.selectFrom(ARTICLE_PICTURE)
                .where(ARTICLE_PICTURE.ARTICLE_ID.eq(articleId))
                .fetch())
    }

    fun deleteById(pictureId: Int) {
        dsl.deleteFrom(ARTICLE_PICTURE)
                .where(ARTICLE_PICTURE.ID.eq(pictureId))
                .execute()
    }

    private fun getEntities(result: Result<ArticlePictureRecord>): List<ArticlePicture> {
        val allArticlePictures = mutableListOf<ArticlePicture>()
        result.forEach { getEntity(it)?.let { allArticlePictures.add(it) } }

        return allArticlePictures
    }

    private fun getEntity(articlePictureRecord: ArticlePictureRecord?): ArticlePicture? {
        if (articlePictureRecord != null) return ArticlePicture(articlePictureRecord) else return null
    }
}