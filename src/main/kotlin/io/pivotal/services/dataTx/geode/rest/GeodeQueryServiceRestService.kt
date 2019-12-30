package io.pivotal.services.dataTx.geode.rest

import com.fasterxml.jackson.databind.ObjectMapper
import io.pivotal.services.dataTx.geode.io.Querier
import org.apache.geode.cache.RegionDestroyedException
import org.apache.geode.cache.client.ServerOperationException
import org.apache.geode.cache.query.Struct
import org.apache.geode.cache.query.types.StructType
import org.apache.geode.pdx.JSONFormatter
import org.apache.geode.pdx.PdxInstance
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * Query service REST wrapper
 */
@Suppress("UNCHECKED_CAST")
@RestController
@RequestMapping("/query")
open class GeodeQueryServiceRestService
{
    val objectMapper = ObjectMapper();

    companion object
    {
        const val EMPTY_JSON : String = "[]";
    }

    /**
     * The default query with no limit
     * @param query the query
     */
    @PostMapping(path = arrayOf("/"),produces = arrayOf("application/json"))
    fun query(@RequestBody query: String) : String
    {
        return queryLimit(0,query);
    }//-----------------------------------------------------------

    @PostMapping(path = arrayOf("{limit}"),produces = arrayOf("application/json"))
    fun queryLimit(@PathVariable limit : Int, @RequestBody query: String) : String
    {
        try
        {

            val oql = formatLimit(query,limit);

            var results : Collection<PdxInstance>? = Querier.query(oql);


            return toJson(results as Collection<Any>);
        }
        catch(e: Exception )
        {
            e.printStackTrace();
            throw e;
        }
    }//------------------------------------------------

    /**
     * @param query the OQL
     * @param limit the records limit counts
     */
    fun formatLimit(query: String, limit: Int): String {

        if(limit == 0)
            return query;

        var lowerCaseQuery = query.toLowerCase();
        if(lowerCaseQuery.contains(" limit "))
            return query;

        return query.plus(" limit ${limit}");
    }//-------------------------------------------

    /**
     * HAndling exceptions in general for REST responses
     * @param request the HTTP request
     * @param response the HTTP reponse
     * @param e the exception
     * @return Data Error details
     */
    @ExceptionHandler(Exception::class)
    fun  handleException(request : HttpServletRequest, response : HttpServletResponse, e : Exception) : DataError
    {
        val cause = e.cause;

        var dataError = DataError();
        dataError.path = request.getRequestURI();

        dataError.error = "Processing error";

        if(e is ServerOperationException)
        {
            dataError.error ="Server opertion error";

            if(cause is RegionDestroyedException)
            {
                var regionDestroyedException = cause;
                dataError.message = "Region region:"+regionDestroyedException.getRegionFullPath()+" not found";
            }
        }
        response.status = 500;

        dataError.message = e.message;

        return dataError;
    }//------------------------------------------------

    fun toJson(list: Collection<Any>): String {

        if(list.isEmpty() )
            return EMPTY_JSON;

        //get first element
        var firstElement = list.iterator().next();

        //if struct
        if(firstElement is Struct)
        {
            return toJsonFromStruct(list as Collection<Struct>,firstElement.structType);
        }
        else if(firstElement is PdxInstance)
        {
            //else normal

            var results = list as Collection<PdxInstance>;

            var responseJson = StringBuilder();

            var count = 0;


            for (pdxInstance : PdxInstance in results) {

                if(count > 0 )
                    responseJson.append(",");

                responseJson.append(JSONFormatter.toJSON(pdxInstance));

                count++;

            }

            var allResults = StringBuilder("[").append(responseJson).append("]");

            return allResults.toString();
        }


        //list of object (not Pdx or Struct)

        return objectMapper.writeValueAsString(list);


    }//-------------------------------------------

    /**
     *
     * @param result the collection of results
     * @param limit the results limit count
     */
    fun toJsonFromStruct(results:Collection<Struct>, structType:StructType): String
    {
        var fieldNames = structType.fieldNames;


        var listMaps : MutableList<Map<String,String>> = arrayListOf();


        for(struct in results){

            val values = struct.fieldValues;

            if(values == null)
                continue;

            var map: HashMap<String,String> = HashMap(values.size);

            for(i in 0 until values.size)
            {
                map.put(fieldNames[i],values[i].toString());
            }

            listMaps.add(map);

        }

        return objectMapper.writeValueAsString(listMaps);
    }
}