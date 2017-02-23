package com.pengelkes.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.pengelkes.backend.jooq.tables.Article.ARTICLE
import com.pengelkes.backend.jooq.tables.records.ArticleRecord
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Created by pengelkes on 30.12.2016.
 */
class Article {
    var id: Int = 0;
    var title: String? = null
    var content: String? = null
    var authorId: Int? = null
    var author: User? = null
    var teamId: Int? = null
    var team: Team? = null
    var created: Date? = null

    //empty constructor needed for jackson
    constructor()

    constructor(json: String) {
        val article = ObjectMapper().readValue(json, Article::class.java)
        this.id = article.id
        this.title = article.title
        this.content = article.content
        this.authorId = article.authorId
        this.author = article.author
        this.teamId = article.teamId
        this.team = article.team
        this.created = article.created
    }

    constructor(id: Int = 0, title: String, content: String, authorId: Int? = null, teamId: Int? = null) {
        this.id = id
        this.title = title
        this.content = content
        this.authorId = authorId
        this.teamId = teamId
    }

    constructor(articleRecord: ArticleRecord) {
        this.id = articleRecord.id
        this.title = articleRecord.title
        this.content = articleRecord.content
        this.authorId = articleRecord.authorId
        this.teamId = articleRecord.teamId
        this.created = articleRecord.createdAt
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Article

        if (id != other.id) return false
        if (title != other.title) return false
        if (content != other.content) return false
        if (authorId != other.authorId) return false
        if (author != other.author) return false
        if (teamId != other.teamId) return false
        if (team != other.team) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (authorId ?: 0)
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + (teamId ?: 0)
        result = 31 * result + (team?.hashCode() ?: 0)
        return result
    }
}

@Service
interface ArticleService {
    fun create(article: Article): Article
    fun update(article: Article): Article
    fun findAll(): List<Article>
    fun findByAuthor(authorId: Int): List<Article>
    fun findByTeam(teamId: Int): List<Article>
    fun getDistinctTeamsWithAnArticle(): List<Team>
}

@Service
@Transactional
open class ArticleServiceImpl @Autowired constructor(val articleServiceController: ArticleServiceController) : ArticleService {
    override fun create(article: Article): Article = articleServiceController.create(article)
    override fun update(article: Article): Article = articleServiceController.update(article)
    override fun findAll(): List<Article> = articleServiceController.findAll()
    override fun findByAuthor(authorId: Int): List<Article> = articleServiceController.findByAuthor(authorId)
    override fun findByTeam(teamId: Int): List<Article> = articleServiceController.findByTeam(teamId)
    override fun getDistinctTeamsWithAnArticle(): List<Team> = articleServiceController.getTeamsWithAnArticle()

}

@Component
open class ArticleServiceController @Autowired constructor(val dsl: DSLContext,
                                                           val userService: UserService,
                                                           val teamService: TeamService) {

    fun create(article: Article): Article {
        val articleRecord = dsl.insertInto(ARTICLE)
                .set(ARTICLE.TITLE, article.title)
                .set(ARTICLE.CONTENT, article.content)
                .set(ARTICLE.AUTHOR_ID, article.authorId)
                .set(ARTICLE.TEAM_ID, article.teamId)
                .returning(ARTICLE.ID)
                .fetchOne()
        article.id = articleRecord.getValue(ARTICLE.ID, Int::class.java)

        return article
    }

    fun update(article: Article): Article {
        dsl.update(ARTICLE)
                .set(ARTICLE.TITLE, article.title)
                .set(ARTICLE.CONTENT, article.content)
                .set(ARTICLE.AUTHOR_ID, article.authorId)
                .set(ARTICLE.TEAM_ID, article.teamId)
                .where(ARTICLE.ID.eq(article.id))
                .execute()

        return article
    }

    fun findAll(): List<Article> = getEntities(dsl.selectFrom(ARTICLE).fetch())

    fun findByAuthor(authorId: Int): List<Article> = getEntities(dsl.selectFrom(ARTICLE)
            .where(ARTICLE.AUTHOR_ID.eq(authorId)).fetch())

    fun findByTeam(teamId: Int): List<Article> = getEntities(dsl.selectFrom(ARTICLE)
            .where(ARTICLE.TEAM_ID.eq(teamId)).fetch())

    fun getTeamsWithAnArticle(): List<Team> {
        println("getting distinct teams with an article")
        val teamsWithAnArticle = mutableListOf<Team>()
        val distinctRecords = dsl.selectDistinct(ARTICLE.TEAM_ID).from(ARTICLE).where(ARTICLE.TEAM_ID.isNotNull)
                .fetch()
        distinctRecords.forEach {
            teamService.findById(it.value1())?.let { teamsWithAnArticle.add(it) }
        }

        return teamsWithAnArticle
    }

    private fun getEntities(result: Result<ArticleRecord>): List<Article> {
        val allArticles = mutableListOf<Article>()
        result.forEach { getEntity(it)?.let { allArticles.add(it) } }

        return allArticles
    }

    private fun getEntity(articleRecord: ArticleRecord?): Article? {
        if (articleRecord != null) {
            val article = Article(articleRecord)
            articleRecord.teamId?.let { teamService.findById(it)?.let { article.team = it } }
            articleRecord.authorId?.let { userService.findById(it)?.let { article.author = it } }
            return article
        }

        return null
    }
}