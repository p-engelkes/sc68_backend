package com.pengelkes

import com.pengelkes.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.ServletException

/**
 * Created by pengelkes on 29.11.2016.
 */
//TODO: Remove for production
@Component
open class SetupBean
@Autowired
constructor(private val userService: UserService,
            private val teamService: TeamService) {

    @PostConstruct
    fun setupUser() {
        val trainingTimes = HashMap<String, String>()
        trainingTimes.put("Wednesday", "19:00")
        trainingTimes.put("Friday", "19:00")
        var team = Team("1. Mannschaft", trainingTimes)
        try {
            teamService.create(team)?.let {
                val user = User("admin@fake.com", "adminpass", it)
                user.position = Position.MIDFIELD
                try {
                    userService.registerNewUser(user)
                } catch (e: ServletException) {
                    e.printStackTrace()
                }
            }
        } catch (e: ServletException) {
            e.printStackTrace()
        }

        team = Team("2. Mannschaft", trainingTimes)
        try {
            teamService.create(team)
        } catch (e: ServletException) {
            e.printStackTrace()
        }

        userService.findByEmail("admin@fake.com")?.let { println(it.position) }
    }

}
