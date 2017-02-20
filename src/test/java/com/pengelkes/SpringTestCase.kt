package com.pengelkes

import com.pengelkes.database.Migrator
import com.pengelkes.service.*
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
 * Created by pengelkes on 10.01.2017.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = arrayOf("classpath:test.properties"))
abstract class SpringTestCase {

    @Autowired
    lateinit var migrator: Migrator

    companion object {
        lateinit var databasePicture: ProfilePicture
        lateinit var databaseUser: com.pengelkes.service.User
        lateinit var databaseArticle: Article
        lateinit var databaseArticleWithAuthor: Article
        lateinit var databaseArticleWithTeam: Article
        lateinit var databaseTeam: Team
        lateinit var databaseGameOne: Game
        lateinit var databaseGameTwo: Game
        lateinit var databaseGameThree: Game
        lateinit var databaseTableTeamOne: TableTeam
        lateinit var databaseTableTeamTwo: TableTeam
        lateinit var databaseTableTeamThree: TableTeam
        lateinit var databaseTableTeamFour: TableTeam
        lateinit var databaseTable: Table
    }

    @Before
    open fun setup() {
        migrator.cleanAndMigrate()
        databaseTeam = Team(1, "Herren 1. Mannschaft", hashMapOf(Pair("Friday", "19:00")),
                "011MICFJLG000000VTVG0001VTR8C1K7")
        databasePicture = ProfilePicture(
                1,
                "iVBORw0KGgoAAAANSUhEUgAABdwAAAH0CAIAAACo53h7AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAA0TSURBVHhe7dgxAYBAEMCwAx2M+HeGB5bXcF2SpR56fc87AAAAAOy6TwEAAABYZMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAICAKQMAAAAQMGUAAAAAAqYMAAAAQMCUAQAAAAiYMgAAAAABUwYAAAAgYMoAAAAABEwZAAAAgIApAwAAABAwZQAAAAACpgwAAABAwJQBAAAACJgyAAAAAAFTBgAAACBgygAAAAAETBkAAACAgCkDAAAAEDBlAAAAAAKmDAAAAEDAlAEAAAAImDIAAAAAAVMGAAAAIGDKAAAAAARMGQAAAIB1Mz/m/QUVg8igwwAAAABJRU5ErkJggg==",
                1500,
                500,
                3.toFloat()
        )
        databaseUser = com.pengelkes.service.User(id = 1, email = "test@test.com", password = "test",
                profilePicture = databasePicture)
        databaseArticle = Article(1, "Test Title", "Test Content")
        databaseArticleWithAuthor = Article(2, "I have an author title", "I have an author content", authorId = 1)
        databaseArticleWithAuthor.author = databaseUser
        databaseArticleWithTeam = Article(3, "I have a team title", "I have a team content", teamId = 1)
        databaseArticleWithTeam.team = databaseTeam
        databaseGameOne = Game(gameTime = "Sonntag, 19.02.2017 - 12:00 Uhr", homeTeamName = "Skiclub Nordwest Rheine",
                awayTeamName = "Portu Rheine", score = Score(6, 0), gameType = GameType.PREVIOUS, id = 1)
        databaseGameTwo = Game(gameTime = "Sonntag, 12.02.2017 - 12:00 Uhr", homeTeamName = "Skiclub Nordwest Rheine",
                awayTeamName = "Portu Rheine", score = Score(6, 0), gameType = GameType.PREVIOUS, id = 2)
        databaseGameThree = Game(gameTime = "Sonntag, 5.02.2017 - 12:00 Uhr", homeTeamName = "Skiclub Nordwest Rheine",
                awayTeamName = "Portu Rheine", score = Score(6, 0), gameType = GameType.PAST, id = 3)

        databaseTableTeamOne = TableTeam(1, 1, "Skiclub Rheine", "Icon", 15, 15, 0, 0, "50:0", 50, 45)
        databaseTableTeamTwo = TableTeam(2, 2, "Emsdetten 05", "Icon", 15, 14, 0, 1, "50:10", 40, 42)
        databaseTableTeamThree = TableTeam(3, 3, "Tus St. Arnold", "Icon", 15, 13, 0, 2, "50:20", 30, 39)
        databaseTableTeamFour = TableTeam(4, 4, "SF Gellendorf", "Icon", 15, 12, 0, 3, "50:30", 20, 36)
        databaseTable = Table(
                listOf(databaseTableTeamOne, databaseTableTeamTwo, databaseTableTeamThree, databaseTableTeamFour),
                databaseTeam.id
        )
    }

    fun setUserAuthenticationForTesting() {
        val user = User("testUser", "", listOf(SimpleGrantedAuthority("ROLE_USER")))
        val testingAuthenticationToken = TestingAuthenticationToken(user, null)
        SecurityContextHolder.getContext().authentication = testingAuthenticationToken
    }
}

abstract class ControllerTestCase : SpringTestCase() {
    @Autowired
    lateinit var context: WebApplicationContext

    lateinit var mockMvc: MockMvc

    override fun setup() {
        super.setup()
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        setUserAuthenticationForTesting()
    }
}