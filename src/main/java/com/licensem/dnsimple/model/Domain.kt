package com.licensem.dnsimple.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.Date
import com.fasterxml.jackson.annotation.JsonProperty

data class Domain(
    @JsonProperty("id") var domainId: Int?,
    @JsonProperty("account_id") var accountId: Int?,
    @JsonProperty("registrant_id") var registrantId: Int?,
    @JsonProperty("name") var name: String?,
    @JsonProperty("unicode_name") var unicodeName: String?,
    @JsonProperty("token") var token: String?,
    @JsonProperty("state") var state: String?,
    @JsonProperty("auto_renew") var autoRenew: Boolean?,
    @JsonProperty("private_whois") var privateWhoIs: Boolean?,
    @JsonProperty("expires_on") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Europe/London") var expirationDate: Date?,
    @JsonProperty("created_at") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Europe/London") var createdDate: Date?,
    @JsonProperty("updated_at") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Europe/London") var updatedDate: Date?
)