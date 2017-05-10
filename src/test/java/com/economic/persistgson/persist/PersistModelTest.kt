package com.economic.persistgson.persist

import com.economic.ecmfoundation.extensions.create
import com.economic.persistgson.Gson
import com.economic.persistgson.GsonBuilder
import com.economic.persistgson.annotations.SerializedName
import junit.framework.Assert
import junit.framework.TestCase
import org.apache.commons.lang3.mutable.Mutable
import java.util.*

/**
 * Created by Tudor Dragan on 04/05/2017.
 * Copyright © e-conomic.com
 */

class PersistModelTest : TestCase() {

    data class Customer(var customerNumber: Int? = null,
                        var doubleCustomerNumber: Double? = null,
                        var customerContact: List<CustomerContact>? = null,
                        override val _persistMap: MutableMap<String, Any> = mutableMapOf()) : PersistObject

    data class CustomerContact(var contactName: String? = "Generic Name",
                               var details: List<ContactDetails>? = null,
                               override val _persistMap: MutableMap<String, Any> = mutableMapOf()) : PersistObject

    data class ContactDetails(@SerializedName("contactDetails") var contactInfo: String? = null,
                              override val _persistMap: MutableMap<String, Any> = mutableMapOf()) : PersistObject

    val defaultCustomerJson = "{\"customerNumber\": 1, \"doubleCustomerNumber\": 2.02, \"unknownProperty\": 12, \"balance\": 123, \"demo\": true, \"customerContact\": [{ \"id\": 12345, \"contactName\": \"John Doe\", \"phones\": { \"home\": \"800-123-4567\", \"mobile\": \"877-123-1234\" }, \"creationDate\": \"1980-01-02\", \"email\": [\"jd@example.com\", \"jd@example.org\"], \"details\": [{ \"contactDetails\": \"Some contact details\" }, { \"contactDetails\": \"Some contact details\" }, { \"hello\": \"there!\", \"contactDetails\": \"Some contact details\" }], \"emergencyContacts\": [{ \"name\": \"Jane Doe\", \"phone\": \"888-555-1212\", \"relationship\": \"spouse\" }, { \"name\": \"Justin Doe\", \"phone\": \"877-123-1212\", \"relationship\": \"parent\" }] }, { \"id\": 12345, \"contactName\": \"John Doe\", \"phones\": { \"home\": \"800-123-4567\", \"mobile\": \"877-123-1234\" }, \"creationDate\": \"1980-01-02\", \"email\": [\"jd@example.com\", \"jd@example.org\"], \"details\": [{ \"contactDetails\": \"Some contact details\" }, { \"contactDetails\": \"Some contact details\" }, { \"hello\": \"there!\", \"contactDetails\": \"Some contact details\" }], \"emergencyContacts\": [{ \"name\": \"Jane Doe\", \"phone\": \"888-555-1212\", \"relationship\": \"spouse\" }, { \"name\": \"Justin Doe\", \"phone\": \"877-123-1212\", \"relationship\": \"parent\" }] }], \"layout\": [{ \"layoutNumber\": 21, \"someOtherProperty\": [{ \"name\": \"Michael\" }, { \"name\": \"Michael\" }] }, { \"layoutNumber\": 21, \"someOtherProperty\": [{ \"name\": \"Michael\" }, { \"name\": \"Michael\" }] }] }"
    val defaultCustomerMap = mapOf(
            "customerNumber" to 1,
            "doubleCustomerNumber" to 2.02,
            "unknownProperty" to 12,
            "balance" to 123,
            "demo" to true,
            "customerContact" to arrayListOf(
                    mapOf(
                            "id" to 12345,
                            "contactName" to "John Doe",
                            "phones" to mapOf(
                                    "home" to "800-123-4567",
                                    "mobile" to "877-123-1234"
                            ),
                            "creationDate" to "1980-01-02",
                            "email" to arrayListOf(
                                    "jd@example.com",
                                    "jd@example.org"
                            ),
                            "details" to arrayListOf(
                                    mapOf(
                                            "contactDetails" to "Some contact details"
                                    ),
                                    mapOf(
                                            "contactDetails" to "Some contact details"
                                    ),
                                    mapOf(
                                            "contactDetails" to "Some contact details",
                                            "hello" to "there!"
                                    )
                            ),
                            "emergencyContacts" to arrayListOf(
                                    mapOf(
                                            "name" to "Jane Doe",
                                            "phone" to "888-555-1212",
                                            "relationship" to "spouse"
                                    ),
                                    mapOf(
                                            "name" to "Justin Doe",
                                            "phone" to "877-123-1212",
                                            "relationship" to "parent"
                                    )
                            )
                    ),
                    mapOf(
                            "id" to 12345,
                            "contactName" to "John Doe",
                            "phones" to mapOf(
                                    "home" to "800-123-4567",
                                    "mobile" to "877-123-1234"
                            ),
                            "creationDate" to "1980-01-02",
                            "email" to arrayListOf(
                                    "jd@example.com",
                                    "jd@example.org"
                            ),
                            "details" to arrayListOf(
                                    mapOf(
                                            "contactDetails" to "Some contact details"
                                    ),
                                    mapOf(
                                            "contactDetails" to "Some contact details"
                                    ),
                                    mapOf(
                                            "contactDetails" to "Some contact details",
                                            "hello" to "there!"
                                    )
                            ),
                            "emergencyContacts" to arrayListOf(
                                    mapOf(
                                            "name" to "Jane Doe",
                                            "phone" to "888-555-1212",
                                            "relationship" to "spouse"
                                    ),
                                    mapOf(
                                            "name" to "Justin Doe",
                                            "phone" to "877-123-1212",
                                            "relationship" to "parent"
                                    )
                            )
                    )
            ),
            "layout" to arrayListOf(
                    mapOf(
                            "layoutNumber" to 21,
                            "someOtherProperty" to arrayListOf(
                                    mapOf(
                                            "name" to "Michael"
                                    ),
                                    mapOf(
                                            "name" to "Michael"
                                    )
                            )
                    ),
                    mapOf(
                            "layoutNumber" to 21,
                            "someOtherProperty" to arrayListOf(
                                    mapOf(
                                            "name" to "Michael"
                                    ),
                                    mapOf(
                                            "name" to "Michael"
                                    )
                            )
                    )
            )
    )
    val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()

    fun testReadComplexJson() {
        val customer = gson.create<Customer>(defaultCustomerJson)

        Assert.assertNotNull(customer?.customerContact)
        Assert.assertTrue(customer?._persistMap?.values?.size != 0)
        Assert.assertEquals(1, customer?.customerNumber)
        Assert.assertEquals(12, customer!!._persistMap["unknownProperty"])
    }

    fun testWriteModelToJson() {
        val customer = Customer()

        customer.customerNumber = 27
        customer.doubleCustomerNumber = 12.0242
        customer.customerContact = listOf(CustomerContact())
        customer._persistMap["unknownProperty"] = "hello"

        val expectedJson = mapOf<String, Any>(
                "customerNumber" to 27,
                "doubleCustomerNumber" to 12.0242,
                "customerContact" to arrayListOf(
                        mapOf("contactName" to "Generic Name")
                ),
                "unknownProperty" to "hello"
                )

        val json = gson.create<Map<String, Any>>(gson.toJson(customer))

        Assert.assertEquals(expectedJson, json)
    }

    fun testJsonToModelToJsonConversionShouldBeSame() {
        val customer = gson.create<Customer>(gson.toJson(defaultCustomerMap))
        val json = gson.toJson(customer)
        val customer2 = gson.create<Customer>(json)
        Assert.assertEquals(customer, customer2)
    }

    fun testOverwritingProperty() {
        val customer = gson.create<Customer>(defaultCustomerJson)

        customer?.customerContact = listOf(CustomerContact(contactName = "Some Guy"))
        customer?.doubleCustomerNumber = 2.021421241

        val expectedCustomerMap = mapOf(
                "customerNumber" to 1,
                "doubleCustomerNumber" to 2.021421241,
                "unknownProperty" to 12,
                "balance" to 123,
                "demo" to true,
                "customerContact" to arrayListOf(
                        mapOf(
                                "contactName" to "Some Guy"
                        )
                ),
                "layout" to arrayListOf(
                        mapOf(
                                "layoutNumber" to 21,
                                "someOtherProperty" to arrayListOf(
                                        mapOf(
                                                "name" to "Michael"
                                        ),
                                        mapOf(
                                                "name" to "Michael"
                                        )
                                )
                        ),
                        mapOf(
                                "layoutNumber" to 21,
                                "someOtherProperty" to arrayListOf(
                                        mapOf(
                                                "name" to "Michael"
                                        ),
                                        mapOf(
                                                "name" to "Michael"
                                        )
                                )
                        )
                )
        )

        val jsonMap = gson.create<Map<String, Any>>(gson.toJson(customer))
        Assert.assertEquals(expectedCustomerMap, jsonMap)
    }

    fun testNestedUnknownPropertyShouldBePresent() {
        val customer = gson.create<Customer>(defaultCustomerJson)

        Assert.assertEquals("there!", customer!!.customerContact!![0].details!![2]._persistMap["hello"])
        Assert.assertEquals("800-123-4567", (customer.customerContact!![0]._persistMap["phones"] as Map<*, *>)["home"])
        Assert.assertEquals("jd@example.com", (customer.customerContact!![0]._persistMap["email"] as List<*>)[0])
    }

    fun testAllPropertiesWithToManyReferenceSetFieldToNil() {
        val customer = gson.create<Customer>(defaultCustomerJson)

        customer?.customerContact = null

        val expectedCustomerMap = mapOf(
                "customerNumber" to 1,
                "doubleCustomerNumber" to 2.02,
                "unknownProperty" to 12,
                "balance" to 123,
                "demo" to true,
                "layout" to arrayListOf(
                        mapOf(
                                "layoutNumber" to 21,
                                "someOtherProperty" to arrayListOf(
                                        mapOf(
                                                "name" to "Michael"
                                        ),
                                        mapOf(
                                                "name" to "Michael"
                                        )
                                )
                        ),
                        mapOf(
                                "layoutNumber" to 21,
                                "someOtherProperty" to arrayListOf(
                                        mapOf(
                                                "name" to "Michael"
                                        ),
                                        mapOf(
                                                "name" to "Michael"
                                        )
                                )
                        )
                )
        )

        val jsonMap = gson.create<Map<String, Any>>(gson.toJson(customer))
        Assert.assertEquals(expectedCustomerMap, jsonMap)
    }

    fun testOverwriteNestedProperty() {
        val customer = gson.create<Customer>(defaultCustomerJson)

        customer?.customerContact = customer?.customerContact?.plus(CustomerContact(details = listOf(ContactDetails(contactInfo = "nothing"))))

        val expectedCustomerMap = mapOf(
                "customerNumber" to 1,
                "doubleCustomerNumber" to 2.02,
                "unknownProperty" to 12,
                "balance" to 123,
                "demo" to true,
                "customerContact" to arrayListOf(
                        mapOf(
                                "id" to 12345,
                                "contactName" to "John Doe",
                                "phones" to mapOf(
                                        "home" to "800-123-4567",
                                        "mobile" to "877-123-1234"
                                ),
                                "creationDate" to "1980-01-02",
                                "email" to arrayListOf(
                                        "jd@example.com",
                                        "jd@example.org"
                                ),
                                "details" to arrayListOf(
                                        mapOf(
                                                "contactDetails" to "Some contact details"
                                        ),
                                        mapOf(
                                                "contactDetails" to "Some contact details"
                                        ),
                                        mapOf(
                                                "contactDetails" to "Some contact details",
                                                "hello" to "there!"
                                        )
                                ),
                                "emergencyContacts" to arrayListOf(
                                        mapOf(
                                                "name" to "Jane Doe",
                                                "phone" to "888-555-1212",
                                                "relationship" to "spouse"
                                        ),
                                        mapOf(
                                                "name" to "Justin Doe",
                                                "phone" to "877-123-1212",
                                                "relationship" to "parent"
                                        )
                                )
                        ),
                        mapOf(
                                "id" to 12345,
                                "contactName" to "John Doe",
                                "phones" to mapOf(
                                        "home" to "800-123-4567",
                                        "mobile" to "877-123-1234"
                                ),
                                "creationDate" to "1980-01-02",
                                "email" to arrayListOf(
                                        "jd@example.com",
                                        "jd@example.org"
                                ),
                                "details" to arrayListOf(
                                        mapOf(
                                                "contactDetails" to "Some contact details"
                                        ),
                                        mapOf(
                                                "contactDetails" to "Some contact details"
                                        ),
                                        mapOf(
                                                "contactDetails" to "Some contact details",
                                                "hello" to "there!"
                                        )
                                ),
                                "emergencyContacts" to arrayListOf(
                                        mapOf(
                                                "name" to "Jane Doe",
                                                "phone" to "888-555-1212",
                                                "relationship" to "spouse"
                                        ),
                                        mapOf(
                                                "name" to "Justin Doe",
                                                "phone" to "877-123-1212",
                                                "relationship" to "parent"
                                        )
                                )
                        ),
                        mapOf(
                                "contactName" to "Generic Name",
                                "details" to arrayListOf(
                                        mapOf(
                                                "contactDetails" to "nothing"
                                        )
                                )
                        )
                ),
                "layout" to arrayListOf(
                        mapOf(
                                "layoutNumber" to 21,
                                "someOtherProperty" to arrayListOf(
                                        mapOf(
                                                "name" to "Michael"
                                        ),
                                        mapOf(
                                                "name" to "Michael"
                                        )
                                )
                        ),
                        mapOf(
                                "layoutNumber" to 21,
                                "someOtherProperty" to arrayListOf(
                                        mapOf(
                                                "name" to "Michael"
                                        ),
                                        mapOf(
                                                "name" to "Michael"
                                        )
                                )
                        )
                )
        )

        val jsonMap = gson.create<Map<String, Any>>(gson.toJson(customer))
        Assert.assertEquals(expectedCustomerMap, jsonMap)
    }

    fun testSerializedNameAnnotationWhenJsonAndObjectPropertiesMatch() {
        val contactDetailsJson = "{\"contactInfo\":\"Some contact details\",\"hello\":\"there!\"}"

        val contactDetails = gson.create<ContactDetails>(contactDetailsJson)

        Assert.assertNotNull(contactDetails)
        Assert.assertNull(contactDetails?.contactInfo)
        Assert.assertEquals("Some contact details", contactDetails?._persistMap!!["contactInfo"])
    }
}

