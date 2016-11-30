package com.pengelkes.oauth;

import com.pengelkes.service.user.User;
import com.pengelkes.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by pengelkes on 30.11.2016.
 */
@Service
@Transactional
public class SkiClubUserDetailsService implements UserDetailsService
{
    private static final String ROLE_USER = "ROLE_USER";

    private final UserService userService;

    @Autowired
    public SkiClubUserDetailsService(UserService userService)
    {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
    {
        final Optional<User> userOptional = userService.findByName(userName);
        if (userOptional.isPresent())
        {
            User user = userOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    getAuthorities(ROLE_USER)
            );
        } else
        {
            throw new UsernameNotFoundException("Username was not found: " + userName);
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role)
    {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }
}
