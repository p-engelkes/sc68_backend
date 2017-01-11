package com.pengelkes.service

import com.pengelkes.DatabaseTestCase
import com.winterbe.expekt.should
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by patrickengelkes on 10/01/2017.
 */
class ArticleTest : DatabaseTestCase() {

    companion object {
        val authorId = 1
        val teamId = 1
        val article = Article(
                "Test Title",
                "Test Content"
        )
    }

    @Before
    override fun setup() {
        super.setup()
        article.authorId = null
        article.teamId = null
    }

    @Autowired
    lateinit var articleService: ArticleService

    @Test
    fun testCreate() {
        articleService.create(article)
        articleService.findAll().size.should.equal(2)
    }

    @Test
    fun testFindAll() {
        articleService.findAll().size.should.equal(1)
    }

    @Test
    fun testFindByAuthor() {
        article.authorId = authorId
        articleService.create(article)
        articleService.findByAuthor(authorId).should.not.be.`null`
    }

    @Test
    fun testFindByTeam() {
        article.teamId = teamId
        articleService.create(article)
        articleService.findByTeam(teamId).should.not.be.`null`
    }
}
