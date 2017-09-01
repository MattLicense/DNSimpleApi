package com.licensem.dnsimple

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPatch
import com.github.kittinunf.fuel.httpPost
import com.licensem.dnsimple.model.*
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class DNSimpleApi(
    // Kotlin needs to escape the $ for Spring to pick up the variables
    @Value("\${dnsimple.api.url}") apiUrl : String,
    @Value("\${dnsimple.api.clientId}") clientId : Int,
    @Value("\${dnsimple.api.token}")private val bearerToken : String
) {

    private val jsonMapper = ObjectMapper()
    private val LOG = LogManager.getLogger(this.javaClass)!!

    // when initialised, set the Fuel basePath
    init {
        FuelManager.instance.basePath = apiUrl.plus(clientId)
        FuelManager.instance.baseHeaders = mapOf(
            "Authorization" to "Bearer $bearerToken",
            "Accept" to "application/json",
            "Content-Type" to "application/json"
        )
    }

    /**
     * Gets all the domains registered for the user logged in
     *
     * @return list of domains available for the authorised user
     */
    fun getDomains() : List<Domain> {
        var domains: List<Domain> = emptyList()
        val url = "/domains"
        LOG.info("Making HTTP call GET ${FuelManager.instance.basePath}$url")
        val result = url.httpGet().responseString().third

        result.fold(
            { data ->
                var domainWrapper = jsonMapper.readValue(data, DomainWrapper::class.java)
                domains = domainWrapper.domains
                LOG.info("Found ${domains.size} domains")
            }, { error ->
                LOG.error("Error retrieving domains: $error")
                domains = emptyList()
            }
        )

        return domains
    }

    /**
     * Gets all of the records for the given domain
     *
     * @param domain    domain (without any subdomain prefix)
     * @return list of records associated to that domain
     */
    fun getRecords(domain: String) : List<Record> {
        var records: List<Record> = emptyList()
        val url = "/zones/".plus(domain).plus("/records")
        LOG.info("Making HTTP call GET ${FuelManager.instance.basePath}$url")
        val result = url.httpGet().responseString().third

        result.fold(
            { data ->
                var recordWrapper = jsonMapper.readValue(data, RecordWrapper::class.java)
                records = recordWrapper.records
                LOG.info("Found ${records.size} records for domain $domain")
            }, { error ->
                LOG.error("Error retrieving records: $error")
                records = emptyList()
            }
        )

        return records
    }

    /**
     * Creates a record
     *
     * @param domain - Domain name of the record being created
     * @param type - record type (e.g. A, CNAME)
     * @param name - name (i.e. subdomain) for record
     * @param value - value of the record
     * @param ttl - time to live, default 3600 seconds (1 hour)
     * @return the newly created record, or null if failed.
     */
    fun createRecord(domain: String, type: RecordType, name: String, value: String, ttl: Int = 3600) : Record? {
        val jsonRequest = "{\"name\":\"$name\",\"type\":\"$type\",\"content\":\"$value\",\"ttl\":$ttl}"
        val url = "/zones/".plus(domain).plus("/records")
        LOG.info("Making HTTP call POST ${FuelManager.instance.basePath}$url")
        val result = url.httpPost().body(jsonRequest).responseString().third

        var createdRecord: RecordResponse? = null
        result.fold(
            { data ->
                createdRecord = jsonMapper.readValue(data, RecordResponse::class.java)
                LOG.info("Successfully created record: ${createdRecord?.record}")
            }, { error ->
                LOG.error("Error creating record: $error")
            }
        )

        return createdRecord?.record
    }

    /**
     * Updates a record based on it's ID
     *
     * @param domain - domain name of the record being updated
     * @param recordId - ID of the record being updated
     * @param newValue - updated value of the record
     * @param ttl - time to live, default 3600 seconds (1 hour)
     * @return the updated record, or null if the update failed
     */
    fun updateRecord(domain: String, recordId: Int, newValue: String, ttl: Int = 3600) : Record? {
        val jsonRequest = "{\"content\":\"$newValue\",\"ttl\":$ttl}"
        val url = "/zones/".plus(domain).plus("/records/").plus(recordId)
        LOG.info("Making HTTP call PATCH ${FuelManager.instance.basePath}$url")
        val result = url.httpPatch().body(jsonRequest).responseString().third

        var updatedRecord: RecordResponse? = null
        result.fold(
            { data ->
                updatedRecord = jsonMapper.readValue(data, RecordResponse::class.java)
                LOG.info("Successfully updated record: ${updatedRecord?.record}")
            }, { error ->
                LOG.error("Error updating record: $error")
            }
        )

        return updatedRecord?.record
    }

}