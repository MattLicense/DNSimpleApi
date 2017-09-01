package com.licensem.dnsimple.model

import org.junit.Assert.*
import org.junit.Test

class DomainWrapperTest : WrapperTests() {

    @Test
    fun testDomainWrapper() {
        var domainList: DomainWrapper
                = jsonMapper.readValue(getResource("/testDomainWrapper.json"), DomainWrapper::class.java)

        assertEquals(1, domainList.domains.size)

        domainList.domains[0].apply {
            assertEquals(154070, domainId)
            assertEquals(31715, accountId)
            assertNull(registrantId)
            assertEquals("mattlicense.co.uk", name)
            assertEquals("mattlicense.co.uk", unicodeName)
            assertNotNull(token)
            assertEquals("hosted", state)
            assertFalse(autoRenew!!)
            assertFalse(privateWhoIs!!)
            assertEquals(dateFormat.parse("2014-10-10T17:43:04Z"), createdDate)
            assertEquals(dateFormat.parse("2015-04-19T16:44:02Z"), updatedDate)
            assertNull(expirationDate)
        }
    }

}
