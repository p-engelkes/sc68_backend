package com.pengelkes.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
{
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private TokenStore tokenStore;
    private AccessTokenConverter accessTokenConverter;

    @Autowired
    public AuthorizationServerConfiguration(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            TokenStore tokenStore,
            AccessTokenConverter accessTokenConverter
    )
    {
        super();
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
        this.accessTokenConverter = accessTokenConverter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices()
    {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        return tokenServices;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
    {
        clients.inMemory()
                .withClient("sc68")
                .secret("$2a$10$OuUzfzyBbpmHn9tR9Mz7R.D0GqO45SGrwn4BPetvJm6kgcgM18beq")
                .authorizedGrantTypes("password", "refresh_token")
                .refreshTokenValiditySeconds(3600 * 24)
                .scopes("sc68")
                .accessTokenValiditySeconds(3600);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
    {
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                .accessTokenConverter(accessTokenConverter);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception
    {
        security.checkTokenAccess("permitAll()");
        security.passwordEncoder(passwordEncoder);
        super.configure(security);
    }
}