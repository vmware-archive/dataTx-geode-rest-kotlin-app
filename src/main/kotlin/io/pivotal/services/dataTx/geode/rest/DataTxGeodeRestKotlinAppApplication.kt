package io.pivotal.services.dataTx.geode.rest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication

/**
 * @author Gregory Green
 */
@SpringBootApplication
@ClientCacheApplication
class DataTxGeodeRestKotlinAppApplication

fun main(args: Array<String>) {
	runApplication<DataTxGeodeRestKotlinAppApplication>(*args)
}
