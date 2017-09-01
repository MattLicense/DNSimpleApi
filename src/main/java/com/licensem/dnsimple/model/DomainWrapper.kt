package com.licensem.dnsimple.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class DomainWrapper(@JsonProperty("data") var domains: List<Domain> = emptyList())