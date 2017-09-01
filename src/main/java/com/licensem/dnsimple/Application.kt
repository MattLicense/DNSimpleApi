package com.licensem.dnsimple

import com.licensem.dnsimple.model.RecordType
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.InputStreamReader
import java.io.BufferedReader
import java.net.URL
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
import org.springframework.scheduling.TaskScheduler
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) {
    AnnotationConfigApplicationContext(Application::class.java)
}

@Configuration
@ComponentScan(basePackages = arrayOf("com.licensem.dnsimple") )
@PropertySource("classpath:application.properties")
@EnableScheduling
open class Application(
    @Autowired private var api: DNSimpleApi,
    @Value("\${dnsimple.api.domain}") private val domainName: String,
    @Value("\${dnsimple.api.subDomain}") private val subDomain: String
) {
    private val LOG = LogManager.getLogger(this.javaClass)!!

    @Bean
    open fun taskScheduler(): TaskScheduler {
        return ConcurrentTaskScheduler()
    }

    /**
     * Runs every hour to check whether the external IP address has changed. If it has, updates the A record for the
     * specified domain / subDomain
     */
    @Scheduled(fixedDelay = 3600000L)
    fun checkIpAddress() {
        val domains = api.getDomains()
        val domain = domains.firstOrNull { it.name == domainName }
        if(domain != null) {
            LOG.info("Found details for ${domain.name}")
            val records = api.getRecords(domain.name!!)

            // Get the external IP address
            val ip = BufferedReader(InputStreamReader(URL("http://checkip.amazonaws.com").openStream())).readLine()
            LOG.info("Current external IP address is $ip")

            val downloadsRecord = records.firstOrNull { it.name == subDomain }
            if (downloadsRecord == null) {
                LOG.info("$subDomain.$domainName doesn't exist, creating it")
                api.createRecord(domain.name!!, RecordType.A, subDomain, ip)
            } else {
                if (downloadsRecord.content != ip) {
                    LOG.info("IP changed from ${downloadsRecord.content} to $ip on $subDomain.$domainName")
                    api.updateRecord(domain.name!!, downloadsRecord.id!!, ip)
                } else {
                    LOG.info("No IP change detected")
                }
            }
        } else {
            LOG.warn("Couldn't find domain $domainName")
        }
    }
}
