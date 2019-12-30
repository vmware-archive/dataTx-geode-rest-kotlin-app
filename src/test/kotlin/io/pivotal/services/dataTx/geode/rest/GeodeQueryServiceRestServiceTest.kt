package io.pivotal.services.dataTx.geode.rest

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import nyla.solutions.core.util.Organizer
import org.apache.geode.cache.query.Struct
import org.apache.geode.cache.query.types.StructType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.util.*

class GeodeQueryServiceRestServiceTest {


    @Test
    fun testQueryWithStruct()
    {
        var service = GeodeQueryServiceRestService();
        var r1 = Mockito.mock(Struct::class.java);
        var structType = Mockito.mock(StructType::class.java);
        var list: List<Struct> = Organizer.toList(r1);


        var json = service.toJsonFromStruct(list,structType);


        var objectMapper = ObjectMapper();
        var tree :JsonNode = objectMapper.readTree(json);

        println("json: ${json}")

        Assertions.assertTrue(tree.isArray);


    }//-------------------------------------------

    @Test
    fun testQueryWithObject()
    {
        var service = GeodeQueryServiceRestService();
        var r1 = "hellow"
        var list: List<String> = Organizer.toList(r1);


        var json = service.toJson(list);


        var objectMapper = ObjectMapper();
        var tree :JsonNode = objectMapper.readTree(json);

        println("json: ${json}")

        Assertions.assertTrue(tree.isArray);


    }//-------------------------------------------
    fun testFormatLimit()
    {
        var service = GeodeQueryServiceRestService();


        Assertions.assertTrue(
                !service.formatLimit("",0)
                .toLowerCase().contains(" limit "));


        Assertions.assertEquals(
                service.formatLimit("select limit 5",0),
                "select limit 5");


        var limit = 4;
        Assertions.assertEquals(
                service.formatLimit("select *",limit),
                "select * limit ${limit}");
    }

}