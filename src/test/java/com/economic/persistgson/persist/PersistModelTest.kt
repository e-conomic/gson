package com.economic.persistgson.persist

import com.economic.ecmfoundation.extensions.create
import com.economic.persistgson.Gson
import com.economic.persistgson.annotations.SerializedName
import junit.framework.Assert
import junit.framework.TestCase

/**
 * Created by Tudor Dragan on 04/05/2017.
 * Copyright © e-conomic.com
 */

class PersistModelTest : TestCase() {

    data class Customer(var customerNumber: Int? = null,
                        var customerContact: List<CustomerContact>? = null,
                        override val persistMap: MutableMap<String, Any> = mutableMapOf()) : PersistObject

    data class CustomerContact(var contactName: String? = "Generic Name",
                               var details: List<ContactDetails>? = null,
                               override val persistMap: MutableMap<String, Any> = mutableMapOf()) : PersistObject

    data class ContactDetails(@SerializedName("contactDetails") var contactInfo: String? = null,
                              override val persistMap: MutableMap<String, Any> = mutableMapOf()) : PersistObject

    val defaultCustomerJson = "{ \"customerNumber\": 1, \"unknownProperty\": 12, \"balance\": 123, \"demo\": true, \"customerContact\": [ { \"id\": 12345, \"contactName\": \"John Doe\", \"phones\": { \"home\": \"800-123-4567\", \"mobile\": \"877-123-1234\" }, \"creationDate\": \"1980-01-02\", \"email\": [ \"jd@example.com\", \"jd@example.org\" ], \"details\": [ { \"contactDetails\": \"Some contact details\" }, { \"contactDetails\": \"Some contact details\" }, { \"hello\": \"there!\", \"contactDetails\": \"Some contact details\" } ], \"emergencyContacts\": [ { \"name\": \"Jane Doe\", \"phone\": \"888-555-1212\", \"relationship\": \"spouse\" }, { \"name\": \"Justin Doe\", \"phone\": \"877-123-1212\", \"relationship\": \"parent\" } ] }, { \"id\": 12345, \"contactName\": \"John Doe\", \"phones\": { \"home\": \"800-123-4567\", \"mobile\": \"877-123-1234\" }, \"creationDate\": \"1980-01-02\", \"email\": [ \"jd@example.com\", \"jd@example.org\" ], \"details\": [ { \"contactDetails\": \"Some contact details\" }, { \"contactDetails\": \"Some contact details\" }, { \"hello\": \"there!\", \"contactDetails\": \"Some contact details\" } ], \"emergencyContacts\": [ { \"name\": \"Jane Doe\", \"phone\": \"888-555-1212\", \"relationship\": \"spouse\" }, { \"name\": \"Justin Doe\", \"phone\": \"877-123-1212\", \"relationship\": \"parent\" } ] } ], \"layout\": [ { \"layoutNumber\": 21, \"someOtherProperty\": [ { \"name\": \"Michael\" }, { \"name\": \"Michael\" } ] }, { \"layoutNumber\": 21, \"someOtherProperty\": [ { \"name\": \"Michael\" }, { \"name\": \"Michael\" } ] } ] }"
    val gson = Gson()

    fun testReadComplexJson() {
        val customer = gson.create<Customer>(defaultCustomerJson)

        Assert.assertNotNull(customer?.customerContact)
        Assert.assertTrue(customer?.persistMap?.values?.size != 0)
        Assert.assertEquals(1, customer?.customerNumber)
        Assert.assertEquals(12.0, customer!!.persistMap["unknownProperty"])
    }

    fun testWriteModelToJson() {
        val customer = Customer()

        customer.customerNumber = 27
        customer.customerContact = listOf(CustomerContact())
        customer.persistMap["unknownProperty"] = "hello"

        val expectedJson = "{\"customerNumber\":27,\"customerContact\":[{\"contactName\":\"Generic Name\"}],\"unknownProperty\":\"hello\"}"
        val json = gson.toJson(customer)

        Assert.assertEquals(expectedJson, json)
    }

    fun testJsonToModelToJsonConversionShouldBeSame() {
        val customer = gson.create<Customer>(defaultCustomerJson)
        val json = gson.toJson(customer)
        val customer2 = gson.create<Customer>(json)
        Assert.assertEquals(customer, customer2)
    }

    fun testOverwritingProperty() {
        val customer = gson.create<Customer>(defaultCustomerJson)

        customer?.customerContact = listOf(CustomerContact(contactName = "Some guy"))

        val expectedJson = "{\"customerNumber\":1,\"customerContact\":[{\"contactName\":\"Some guy\"}],\"unknownProperty\":12.0,\"balance\":123.0,\"demo\":true,\"layout\":[{\"layoutNumber\":21.0,\"someOtherProperty\":[{\"name\":\"Michael\"},{\"name\":\"Michael\"}]},{\"layoutNumber\":21.0,\"someOtherProperty\":[{\"name\":\"Michael\"},{\"name\":\"Michael\"}]}]}"
        val json = gson.toJson(customer)

        Assert.assertEquals(expectedJson, json)
    }

    fun testNestedUnknownPropertyShouldBePresent() {
        val customer = gson.create<Customer>(defaultCustomerJson)

        Assert.assertEquals("there!", customer!!.customerContact!![0].details!![2].persistMap["hello"])
        Assert.assertEquals("800-123-4567", (customer.customerContact!![0].persistMap["phones"] as Map<*, *>)["home"])
        Assert.assertEquals("jd@example.com", (customer.customerContact!![0].persistMap["email"] as List<*>)[0])
    }

    fun testAllPropertiesWithToManyReferenceSetFieldToNil() {
        val customer = gson.create<Customer>(defaultCustomerJson)

        customer?.customerContact = null

        val expectedJson = "{\"customerNumber\":1,\"unknownProperty\":12.0,\"balance\":123.0,\"demo\":true,\"layout\":[{\"layoutNumber\":21.0,\"someOtherProperty\":[{\"name\":\"Michael\"},{\"name\":\"Michael\"}]},{\"layoutNumber\":21.0,\"someOtherProperty\":[{\"name\":\"Michael\"},{\"name\":\"Michael\"}]}]}"
        val json = gson.toJson(customer)

        Assert.assertEquals(expectedJson, json)
    }

    fun testOverwriteNestedProperty() {
        val customer = gson.create<Customer>(defaultCustomerJson)

        customer?.customerContact = customer?.customerContact?.plus(CustomerContact(details = listOf(ContactDetails(contactInfo = "nothing"))))

        val expectedJson = "{\"customerNumber\":1,\"customerContact\":[{\"contactName\":\"John Doe\",\"details\":[{\"contactDetails\":\"Some contact details\"},{\"contactDetails\":\"Some contact details\"},{\"contactDetails\":\"Some contact details\",\"hello\":\"there!\"}],\"id\":12345.0,\"phones\":{\"home\":\"800-123-4567\",\"mobile\":\"877-123-1234\"},\"creationDate\":\"1980-01-02\",\"email\":[\"jd@example.com\",\"jd@example.org\"],\"emergencyContacts\":[{\"name\":\"Jane Doe\",\"phone\":\"888-555-1212\",\"relationship\":\"spouse\"},{\"name\":\"Justin Doe\",\"phone\":\"877-123-1212\",\"relationship\":\"parent\"}]},{\"contactName\":\"John Doe\",\"details\":[{\"contactDetails\":\"Some contact details\"},{\"contactDetails\":\"Some contact details\"},{\"contactDetails\":\"Some contact details\",\"hello\":\"there!\"}],\"id\":12345.0,\"phones\":{\"home\":\"800-123-4567\",\"mobile\":\"877-123-1234\"},\"creationDate\":\"1980-01-02\",\"email\":[\"jd@example.com\",\"jd@example.org\"],\"emergencyContacts\":[{\"name\":\"Jane Doe\",\"phone\":\"888-555-1212\",\"relationship\":\"spouse\"},{\"name\":\"Justin Doe\",\"phone\":\"877-123-1212\",\"relationship\":\"parent\"}]},{\"contactName\":\"Generic Name\",\"details\":[{\"contactDetails\":\"nothing\"}]}],\"unknownProperty\":12.0,\"balance\":123.0,\"demo\":true,\"layout\":[{\"layoutNumber\":21.0,\"someOtherProperty\":[{\"name\":\"Michael\"},{\"name\":\"Michael\"}]},{\"layoutNumber\":21.0,\"someOtherProperty\":[{\"name\":\"Michael\"},{\"name\":\"Michael\"}]}]}"
        val json = gson.toJson(customer)

        Assert.assertEquals(expectedJson, json)
    }

    fun testSerializedNameAnnotationWhenJsonAndObjectPropertiesMatch() {
        val contactDetailsJson = "{\"contactInfo\":\"Some contact details\",\"hello\":\"there!\"}"

        val contactDetails = gson.create<ContactDetails>(contactDetailsJson)

        Assert.assertNotNull(contactDetails)
        Assert.assertNull(contactDetails?.contactInfo)
        Assert.assertEquals("Some contact details", contactDetails?.persistMap!!["contactInfo"])
    }
}

