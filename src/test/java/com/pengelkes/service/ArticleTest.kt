package com.pengelkes.service

import com.pengelkes.SpringTestCase
import com.winterbe.expekt.should
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by patrickengelkes on 10/01/2017.
 */
class ArticleTest : SpringTestCase() {

    companion object {
        val authorId = 1
        val teamId = 1
        val article = Article(
                title = "Test Title",
                content = "Test Content"
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
    @Ignore
            //TODO: Test does not run: org.postgresql.util.PSQLException: FEHLER: gecachter Plan darf den Ergebnistyp nicht ändern
            //reson still unknown fix later
    fun testCreate() {
        articleService.create(article)
        articleService.findAll().size.should.equal(2)
    }

    @Test
    fun testFindAll() {
        articleService.findAll().size.should.equal(3)
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
