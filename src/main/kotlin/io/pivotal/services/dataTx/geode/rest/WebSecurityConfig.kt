package io.pivotal.services.dataTx.geode.rest

import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.SecurityExpressionHandler
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class WebSecurityConfig :  WebSecurityConfigurerAdapter()
{
    @Override
    override fun configure(http: HttpSecurity) {
        http.csrf().disable();

        http.httpBasic().realmName("default");

        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .logout()
                .permitAll();
    }


    fun  registerAuthentication( authManagerBuilder : AuthenticationManagerBuilder)
    {
        authManagerBuilder.inMemoryAuthentication()

                .withUser("admin")
                .password("admin").roles("USER", "ADMIN", "ROLE_ADMIN");

    }
}