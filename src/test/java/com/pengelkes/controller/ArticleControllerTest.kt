package com.pengelkes.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pengelkes.ControllerTestCase
import com.pengelkes.service.Article
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Created by pengelkes on 13.01.2017.
 */
class ArticleControllerTest : ControllerTestCase() {

    @Test
    fun testFindAll() {
        val mockResult = mockMvc.perform(get("/api/articles"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()

        val expected = listOf<Article>(databaseArticle, databaseArticleWithAuthor, databaseArticleWithTeam)
        val mapper = ObjectMapper()
        val json = mockResult.response.contentAsString
        val returnedArticles = mapper.readValue(json, Array<Article>::class.java)
        returnedArticles.asList().should.equal(expected)
    }

    @Test
    fun testFindByFilterWithTeamId() {
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/articles/filter?teamId=1"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()

        val expected = listOf<Article>(databaseArticleWithTeam)
        val json = mockResult.response.contentAsString
        val returnedArticles = ObjectMapper().readValue(json, Array<Article>::class.java)
        returnedArticles.asList().should.equal(expected)
    }

    @Test
    fun testFindByFilterWithAuthorId() {
        val mockResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/articles/filter?authorId=1"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()

        val expected = listOf<Article>(databaseArticleWithAuthor)
        val json = mockResult.response.contentAsString
        val returnedArticles = ObjectMapper().readValue(json, Array<Article>::class.java)
        returnedArticles.asList().should.equal(expected)
    }

    @Test
    fun testCreate() {
        val article = Article(title = "I am a new title", content = "I am a new content")
        val json = ObjectMapper().writeValueAsString(article)

        val mockResult = mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn()

        val createdArticle = Article(mockResult.response.contentAsString)
        createdArticle.title.should.equal(article.title)
        createdArticle.content.should.equal(article.content)
    }

    @Test
    fun testUpdate() {
        val updateArticle = databaseArticle
        databaseArticle.title = "I am an updated title"
        databaseArticle.content = "I am an updated content"
        val json = ObjectMapper().writeValueAsString(updateArticle)

        val mockResult = mockMvc.perform(post("/api/articles/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andDo(print())
                .andExpect(status().isOk)
                .andReturn()

        Article(mockResult.response.contentAsString).should.equal(updateArticle)
    }
}