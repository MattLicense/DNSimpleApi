package com.licensem.dnsimple.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Record(
    @JsonProperty("id") var id: Int?,
    @JsonProperty("zone_id") var zone_id: String?,
    @JsonProperty("name") var name: String?,
    @JsonProperty("content") var content: String?,
    @JsonProperty("ttl") var ttl: Int?,
    @JsonProperty("priority") var priority: Int?,
    @JsonProperty("type") var type: RecordType?,
    @JsonProperty("regions") var regions: List<String>?,
    @JsonProperty("system_record") var system_record: Boolean?,
    @JsonProperty("created_at") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Europe/London") var createdDate: Date?,
    @JsonProperty("updated_at") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Europe/London") var updatedDate: Date?
)