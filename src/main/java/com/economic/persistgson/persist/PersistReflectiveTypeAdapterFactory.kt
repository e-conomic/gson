package com.economic.persistgson.persist

import com.economic.persistgson.*
import com.economic.persistgson.internal.ConstructorConstructor
import com.economic.persistgson.internal.Excluder
import com.economic.persistgson.internal.ObjectConstructor
import com.economic.persistgson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory
import com.economic.persistgson.internal.bind.ReflectiveTypeAdapterFactory
import com.economic.persistgson.reflect.TypeToken
import com.economic.persistgson.stream.JsonReader
import com.economic.persistgson.stream.JsonToken
import com.economic.persistgson.stream.JsonWriter

import org.json.JSONObject

import java.io.IOException
import java.util.HashMap

/**
 * Created by Tudor Dragan on 03/05/2017.
 * Copyright © e-conomic.com
 */

class PersistReflectiveTypeAdapterFactory(constructorConstructor: ConstructorConstructor, fieldNamingPolicy: FieldNamingStrategy, excluder: Excluder, jsonAdapterFactory: JsonAdapterAnnotationTypeAdapterFactory) : ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder, jsonAdapterFactory) {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val raw = type.rawType

        if (!Any::class.java.isAssignableFrom(raw)) {
            return null // it's a primitive!
        }

        val constructor = constructorConstructor.get(type)
        return Adapter(gson, constructor, getBoundFields(gson, type, raw))
    }

    class Adapter<T> internal constructor(context: Gson, private val constructor: ObjectConstructor<T>, private val boundFields: Map<String, ReflectiveTypeAdapterFactory.BoundField>) : TypeAdapter<T>() {

        val defaultReadAdapter: TypeAdapter<Any> = context.getAdapter(TypeToken.get(Any::class.java).rawType)

        @Throws(IOException::class)
        override fun read(`in`: JsonReader): T? {
            if (`in`.peek() == JsonToken.NULL) {
                `in`.nextNull()
                return null
            }

            val instance = constructor.construct()

            try {
                `in`.beginObject()
                while (`in`.hasNext()) {
                    val name = `in`.nextName()
                    val field = boundFields[name]
                    if (field == null || !field.deserialized) {
                        // if instance is of type PersisObject
                        if (instance is PersistObject) {
                            try {
                                val processedJson = defaultReadAdapter.read(`in`)
                                if (processedJson != null) {
                                    instance.persistMap.put(name, processedJson)
                                } else {
                                    `in`.skipValue()
                                }
                            } catch (exception: Exception) {
                                throw exception
                            }
                        } else {
                            `in`.skipValue()
                        }
                    } else {
                        field.read(`in`, instance)
                    }
                }
            } catch (e: IllegalStateException) {
                throw JsonSyntaxException(e)
            } catch (e: IllegalAccessException) {
                throw AssertionError(e)
            }

            `in`.endObject()
            return instance
        }

        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: T?) {
            if (value == null) {
                out.nullValue()
                return
            }

            out.beginObject()
            try {
                for (boundField in boundFields.values) {
                    writeValueToBoundField(boundField, value, out)
                }
            } catch (e: IllegalAccessException) {
                throw AssertionError(e)
            }

            out.endObject()
        }

        private fun writeValueToBoundField(boundField: BoundField, value: T?, out: JsonWriter) {
            if (boundField.writeField(value)) {
                boundField.write(out, value)
            }
        }
    }
}
