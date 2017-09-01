package com.licensem.dnsimple.model

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URL
import java.text.SimpleDateFormat

abstract class WrapperTests {

    protected val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    protected val jsonMapper = ObjectMapper()

    fun getResource(resourceName: String) : URL {
        return WrapperTests::class.java.getResource(resourceName)
    }

}