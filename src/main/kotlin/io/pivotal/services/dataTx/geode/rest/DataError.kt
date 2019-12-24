package io.pivotal.services.dataTx.geode.rest

import java.util.*

/**
 * @author Gregory Greens
 */
data class DataError (var timestamp : String = Calendar.getInstance().time.toString(),
                      var message : String? = null,
                      var stackTrace : String? = null,
                      var error : String? = null,
                      var  path : String? = null)
