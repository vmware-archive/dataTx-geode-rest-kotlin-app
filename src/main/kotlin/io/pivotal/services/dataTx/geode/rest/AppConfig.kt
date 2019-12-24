package io.pivotal.services.dataTx.geode.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.EnablePdx
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
public class AppConfig
{

    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
    }
    /**
     * GemFire connection object is a Client Cache
     */
    //@Autowired
    //lateinit var clientCache : ClientCache;

    /**
     * Defines the GemFire connection cache
     * @param environment the environment variables in application.yml, application.properties, system properties or evironment variables
     * @return
     */
    /*@Bean(name = arrayOf("client-cache"))
    fun getCache(@Autowired  environment : Environment) : ClientCache
    {
        var vcap : GeodeSettings = GeodeSettings.getInstance();

        var locatorList = vcap.getLocatorUrlList();
        var locatorURI : URI? = null;

        if(locatorList != null && !locatorList.isEmpty())
            locatorURI = locatorList.get(0);

        var port : Int = 0;

        var  host : String? = environment.getProperty("LOCATOR_HOST");

        if (host == null || host.trim().length == 0)
        {
            //check env
            if(locatorURI != null)
            {
                host = locatorURI.host;
                port = locatorURI.port;
            }
        }
        else
        {
            port = Integer.parseInt(environment.getProperty("LOCATOR_PORT"));
        }

        if (host == null || host.trim().length == 0)
            throw IllegalArgumentException("Environment property LOCATOR_HOST is required");

        var name : String? = environment.getProperty("name");

        if (name == null || name.trim().length == 0)
            throw IllegalArgumentException("Environment property name is required");

        try
        {
            var props : Properties = Properties();
            props.setProperty("security-client-auth-init", GeodeConfigAuthInitialize::class.java.name+".create");

            var factory : ClientCacheFactory = ClientCacheFactory(props)
                    .addPoolLocator(host, port)
                    .setPoolSubscriptionEnabled(true)
                    .setPdxReadSerialized(true)
                    .set("name", name);

            return factory.create();
        }
        catch ( e : NumberFormatException)
        {
            throw NumberFormatException("Environment variable LOCATOR_PORT is required");
        }
    }*/
    //------------------------------------------------
    /**
     * Wire the example GemFire client wrapper
     * @param clientCache the GemFire connection
     * @param factory the factory to create regions
     * @return
     */
    //@Bean
   // fun getGeode() : GeodeClient
   // {
   //     return GeodeClient.connect();
   // }//------------------------------------------------

    /**
     * GemFire factory to create regions
     * @return the factory with the ClientRegionShortcut.PROXY
     */
   /* @Bean
    @SafeVarargs
    fun <K,V> getProxyClientRegionFactory() : ClientRegionFactory<K,V>
    {
        // The options are PROX or Caching Proxy
        //Caching PROXY has a better response time performance
        //but requires you register interest (set getRegion)
        var factory : ClientRegionFactory<K,V> = ClientCacheFactory.getAnyInstance()
                .createClientRegionFactory<K,V>(ClientRegionShortcut.CACHING_PROXY_HEAP_LRU)
                .setEvictionAttributes(EvictionAttributes.createLRUHeapAttributes());


        return factory;
    }*/
    //------------------------------------------------

}