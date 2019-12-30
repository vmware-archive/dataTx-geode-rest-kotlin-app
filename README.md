 # DataTx Geode kotlin REST API

This is an Spring Boot based app that provides the following [Apache Geode](https://geode.apache.org/) features 
- POST region with region key and value to put data into a [region](https://geode.apache.org/docs/guide/basic_config/data_regions/chapter_overview.html)
- GET region name and key to read

When starting the application you must provide the ENVironment

- spring_data_gemfire_pool_locators=host1[port],host2[port]   (comma separated)
- spring_security_user_name=Authentication user
- spring_security_user_password=Authentication password
- spring_data_gemfire_name =**Client Name**


Example 
	
	export spring_data_gemfire_pool_locators=localhost[10334]
	export spring_security_user_name=admin
	export spring_security_user_password=admin
	export spring_data_gemfire_name=GEODE_REST_CLIENT
	

**Secured Apache Geode Authentication**

When connecting to a Apache Geode cluster with security enabled, set the following spring
properties. Note that is can be different then the spring_security_user_name/spring_security_user_password
properties.

    spring.data.gemfire.security.username=...
    spring.data.gemfire.security.password=...


# REST Region Service

The URL http://**root**/region prefix exposes a REST interface to preform READ/WRITE 
operations on a Apache Geode region.


# POST region

Put a  key/value entry into a given region.

**NOTE** The value is expected to be a JSON string


**Format** 

	http://<root>/region/<regionName>/<key>
	
	POST BODY
		<value>

*Example*

HTTP POST

http://**host**/region/Test/hello

RESPONSE

{ "name" : "world"}



# GET region

Get a region value based on a given key

**Format** 

	http://<root>/region/<regionName>/<key>
	
*Example*

HTTP GET

http://localhost:8080/region/Test/hello

RESPONSE

{ "name" : "world"}



# Query

Perform a Apache Geode
http://host:port/query 

* Note the select assumes the results of PdxInstances 

HTTP POST 

http://localhost:8080/query

    
    select * from /region 


Appending a number the limit the number of results returned. 

    http://host:port/query/limitNumber
	

HTTP POST 

http://localhost:8080/query/10

    
    select * from /region 