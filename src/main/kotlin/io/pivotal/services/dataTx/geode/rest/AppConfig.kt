package io.pivotal.services.dataTx.geode.rest

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.EnablePdx
import org.springframework.data.gemfire.config.annotation.EnableSecurity
import org.springframework.data.gemfire.config.annotation.EnableStatistics
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * @author Gregory Green
 */
@Configuration
@EnableSwagger2
@EnablePdx(readSerialized = true)
@EnableStatistics
@EnableSecurity
public class AppConfig
{
    @Value("\${spring.data.gemfire.pool.locators}")
    lateinit var locators: String;


    @Value("\${spring.security.user.name}")
    lateinit var userName: String;

    @Value("\${spring.security.user.password}")
    lateinit var password: String;


    //spring.data.gemfire.name
    @Value("\${spring.data.gemfire.name}")
    lateinit var name: String;


    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
    }
}