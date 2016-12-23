package com.pengelkes.oauth

import com.pengelkes.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Created by pengelkes on 30.11.2016.
 */
@Service
@Transactional
open class SkiClubUserDetailsService
@Autowired
constructor(private val userService: UserService) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(userName: String): UserDetails {
        var user = userService.findByName(userName)
        if (user != null) {
            return login(user.userName!!, user.password!!)
        } else {
            user = userService.findByEmail(userName)
            if (user != null) {
                return login(user.email!!, user.password!!)
            } else {
                throw UsernameNotFoundException("Es existiert kein Benutzer mit dem Namen: " + userName)
            }
        }
    }

    private fun login(userName: String, password: String): UserDetails {
        return org.springframework.security.core.userdetails.User(
                userName,
                password,
                true,
                true,
                true,
                true,
                getAuthorities(ROLE_USER)
        )
    }

    private fun getAuthorities(role: String): Collection<GrantedAuthority> {
        return Arrays.asList(SimpleGrantedAuthority(role))
    }

    companion object {
        private val ROLE_USER = "ROLE_USER"
    }
}
