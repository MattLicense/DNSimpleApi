package com.licensem.dnsimple.model

import org.junit.Assert.*
import org.junit.Test

class RecordWrapperTest : WrapperTests() {

    @Test
    fun testRecordWrapper() {
        var recordList: RecordWrapper
                = jsonMapper.readValue(getResource("/testRecordWrapper.json"), RecordWrapper::class.java)

        assertEquals(14, recordList.records.size)

        var jenkinsRecord = recordList.records.first { it.name == "jenkins" }
        jenkinsRecord.apply {
            assertNotNull(this)
            assertEquals("46.101.9.31", content)
            assertEquals(dateFormat.parse("2015-04-19T16:43:15Z"), createdDate)
            assertEquals(3970351, id)
            assertNull(priority)
            assertEquals(listOf("global"), regions)
            assertFalse(system_record!!)
            assertEquals(3600, ttl)
            assertEquals(RecordType.A, type)
            assertEquals(dateFormat.parse("2015-04-19T16:43:23Z"), updatedDate)
            assertEquals("mattlicense.co.uk", zone_id)
        }

        var dlRecord = recordList.records.first { it.name == "dl" }
        dlRecord.apply {
            assertNotNull(this)
            assertEquals("212.108.77.50", content)
            assertEquals(dateFormat.parse("2017-08-31T15:27:12Z"), createdDate)
            assertEquals(12175527, id)
            assertNull(priority)
            assertEquals(listOf("global"), regions)
            assertFalse(system_record!!)
            assertEquals(3600, ttl)
            assertEquals(RecordType.A, type)
            assertEquals(dateFormat.parse("2017-09-01T09:04:16Z"), updatedDate)
            assertEquals("mattlicense.co.uk", zone_id)
        }
    }

}
