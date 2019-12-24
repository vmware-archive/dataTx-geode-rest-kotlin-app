package io.pivotal.services.dataTx.geode.rest

import io.pivotal.services.dataTx.geode.client.GeodeClient
import nyla.solutions.core.util.Debugger
import org.apache.geode.cache.Region
import org.apache.geode.cache.RegionDestroyedException
import org.apache.geode.cache.client.ServerOperationException
import org.apache.geode.pdx.JSONFormatter
import org.apache.geode.pdx.PdxInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import io.pivotal.services.dataTx.geode.serialization.PDX
import org.apache.geode.cache.client.ClientCache
import org.apache.geode.cache.client.ClientCacheFactory


/**
 * Service for manage region records
 * @author Gregory Green
 */
@RestController
@RequestMapping("/region")
open class GeodeRegionService
{
    //@Autowired
    //lateinit var cache : ClientCache

    /**
     * Put a new records
     * @param region the region name
     * @param key the region key
     * @param value the region value
     * @return previous Region values in JSON
     */
    @PostMapping(path = arrayOf("{region}/{key}"),produces = arrayOf("application/json"))
    fun putEntry(@PathVariable region : String?, @PathVariable key : String?, @RequestBody value: String?) : String?
    {

        if(region == null || region.length == 0 || key == null)
            return null;

        try {
            //Region<String, PdxInstance> gemRegion = geode.getRegion(region);
            var gemRegion: Region<String, PdxInstance> = GeodeClient.getRegion(
                    ClientCacheFactory.getAnyInstance(),region)

            System.out.println("Putting key $key in region $region")

            //var pdxInstance = JSONFormatter.fromJSON(value);

            var pdxInstance = PDX.fromJSON(value)

            var response = gemRegion.put(key, pdxInstance);

            if (response == null)
                return null;

            return JSONFormatter.toJSON(response);
        }
        catch(e: Exception )
        {
            e.printStackTrace();
            throw e;
        }
    }//------------------------------------------------

    /**
     * Delete a region entry by a key
     * @param regionName the region name
     * @param key the region key
     * @return the deleted region value in JSON format
     */
    @DeleteMapping(path = arrayOf("{regionName}/{key}"),produces = arrayOf("application/json"))
    fun delete(@PathVariable regionName : String,@PathVariable   key : String): String?
    {
        var region : Region<String,PdxInstance>  = GeodeClient.getRegion(
                ClientCacheFactory.getAnyInstance()
                ,regionName);

        var pdx : PdxInstance? = region.remove(key);

        if(pdx == null)
            return null;

        return JSONFormatter.toJSON(pdx);

    }//------------------------------------------------
    /**
     * Get a value by a given key
     * @param region the region name
     * @param key the region key
     * @return the value of the region in JSON format
     */
    @GetMapping(path = arrayOf("{region}/{key}"),produces = arrayOf("application/json"))
    fun getValueByKey(@PathVariable  region : String?,@PathVariable  key : String?) : String?
    {
        try
        {
            if(region == null || region.length == 0 || key == null)
                return null;

            var gemRegion : Region<String, Any> = GeodeClient.getRegion(ClientCacheFactory.getAnyInstance(),region);

            var value = gemRegion.get(key);

            if(value == null)
                return null;


            if(value is PdxInstance)
                return JSONFormatter.toJSON(value);
            return value as String;
        }
        catch( serverError : ServerOperationException)
        {
            var cause : Throwable? = serverError.getRootCause();
            if(cause is RegionDestroyedException)
            {
                throw DataServiceSystemException("Region \""+region+"\" not found");
            }
            throw DataServiceSystemException(serverError.message,serverError);
        }
        catch (e : RuntimeException)
        {

            e.printStackTrace();

            throw DataServiceSystemException(e.message,e);
        }
    }//------------------------------------------------
    /**
     * HAndling exceptions in general for REST responses
     * @param request the HTTP request
     * @param response the HTTP reponse
     * @param e the exception
     * @return Data Error details
     */
    @ExceptionHandler(Exception::class)
    fun  handleException(request : HttpServletRequest,  response : HttpServletResponse,  e : Exception) : DataError
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
        dataError.stackTrace = Debugger.stackTrace(e);

        return dataError;
    }//------------------------------------------------
}