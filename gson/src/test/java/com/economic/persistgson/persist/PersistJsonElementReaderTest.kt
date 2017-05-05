package com.economic.persistgson.persist

import com.economic.ecmfoundation.extensions.create
import com.economic.persistgson.Gson
import junit.framework.TestCase

/**
 * Created by Tudor Dragan on 04/05/2017.
 * Copyright © e-conomic.com
 */

class PersistJsonElementReaderTest : TestCase() {

    data class Customer(var customerNumber: Int? = null,
                        var customerContact: CustomerContact? = null) : PersistObject {
        override val persistMap: MutableMap<String, Any> = mutableMapOf()
    }

    data class CustomerContact(var contactName: String? = null) : PersistObject {
        override val persistMap: MutableMap<String, Any> = mutableMapOf()
    }

    data class ContactDetails(var contactDetails: String? = null) : PersistObject{
        override val persistMap: MutableMap<String, Any> = mutableMapOf()
    }


    fun testComplexJson() {
        val jsonString = "{ \"customerNumber\": 1, \"unknownProperty\": 12, \"balance\": 123, \"demo\": true, \"customerContact\": { \"id\": 12345, \"contactName\": \"John Doe\", \"phones\": { \"home\": \"800-123-4567\", \"mobile\": \"877-123-1234\" }, \"creationDate\": \"1980-01-02\", \"email\": [ \"jd@example.com\", \"jd@example.org\" ], \"details\": [{ \"contactDetails\": \"sadasdsadadasdasd\" }, { \"contactDetails\": \"sadasdsadadasdasd\" }, { \"hello\": \"there!\", \"contactDetails\": \"sadasdsadadasdasd\" } ], \"emergencyContacts\": [{ \"name\": \"Jane Doe\", \"phone\": \"888-555-1212\", \"relationship\": \"spouse\" }, { \"name\": \"Justin Doe\", \"phone\": \"877-123-1212\", \"relationship\": \"parent\" } ] }, \"layout\": [{ \"layoutNumber\": 21, \"someOtherProperty\": [{ \"name\": \"safa\" }, { \"name\": \"safa\" } ] }, { \"layoutNumber\": 21, \"someOtherProperty\": [{ \"name\": \"safa\" }, { \"name\": \"safa\" } ] } ] }"

        val customer = Gson().create<Customer>(jsonString)
        customer
    }
}

