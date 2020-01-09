package io.pivotal.services.dataTx.geode.rest

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class WebSecurityConfig :  WebSecurityConfigurerAdapter()
{
    @Value("\${spring.security.user.name}")
    private lateinit var userName : String;

    @Value("\${spring.security.user.password}")
    private lateinit var password : String;

    @Override
    override fun configure(http: HttpSecurity) {
        http.csrf().disable();

        http.httpBasic().realmName("default");

        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .permitAll();
    }//-------------------------------------------

    fun  registerAuthentication( authManagerBuilder : AuthenticationManagerBuilder)
    {
        authManagerBuilder.inMemoryAuthentication()

                .withUser(userName)
                .password( password)
                .roles("USER", "ADMIN", "ROLE_ADMIN");

    }
}