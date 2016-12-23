package com.pengelkes

import com.pengelkes.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.AccessTokenConverter
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.util.*

/**
 * Created by pengelkes on 23.12.2016.
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
        return User(
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


@Configuration
open class SecurityBeans {
    @Value("\${signing-key:oui214hmui23o4hm1pui3o2hp4m1o3h2m1o43}")
    private val signingKey: String? = null

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(10, SecureRandom())
    }

    @Bean
    open fun accessTokenConverter(): JwtAccessTokenConverter {
        val jwtAccessTokenConverter = JwtAccessTokenConverter()
        jwtAccessTokenConverter.setSigningKey(signingKey!!)
        return jwtAccessTokenConverter
    }

    @Bean
    open fun tokenStore(): TokenStore {
        return JwtTokenStore(accessTokenConverter())
    }
}

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
open class ResourceServerConfiguration
@Autowired
constructor(
        private val userDetailsService: UserDetailsService,
        private val passwordEncoder: PasswordEncoder,
        private val tokenStore: TokenStore
) : ResourceServerConfigurerAdapter() {

    @Bean
    open fun authProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder)
        return authProvider
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authProvider())
    }

    @Throws(Exception::class)
    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.tokenStore(tokenStore)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/api/user/register").permitAll()
                .antMatchers("/api/teams").permitAll()
                .antMatchers("/api/positions").permitAll()
                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable()
    }
}

@Configuration
@EnableAuthorizationServer
open class AuthorizationServerConfiguration
@Autowired
constructor(
        private val authenticationManager: AuthenticationManager,
        private val userDetailsService: UserDetailsService,
        private val passwordEncoder: PasswordEncoder,
        private val tokenStore: TokenStore,
        private val accessTokenConverter: AccessTokenConverter
) : AuthorizationServerConfigurerAdapter() {

    @Bean
    @Primary
    open fun tokenServices(): DefaultTokenServices {
        val tokenServices = DefaultTokenServices()
        tokenServices.setTokenStore(tokenStore)
        return tokenServices
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients!!.inMemory()
                .withClient("sc68")
                .secret("$2a$10\$OuUzfzyBbpmHn9tR9Mz7R.D0GqO45SGrwn4BPetvJm6kgcgM18beq")
                .authorizedGrantTypes("password", "refresh_token")
                .refreshTokenValiditySeconds(3600 * 24)
                .scopes("sc68")
                .accessTokenValiditySeconds(3600)
    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints!!
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                .accessTokenConverter(accessTokenConverter)
    }

    @Throws(Exception::class)
    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security!!.checkTokenAccess("permitAll()")
        security.passwordEncoder(passwordEncoder)
        super.configure(security)
    }
}
