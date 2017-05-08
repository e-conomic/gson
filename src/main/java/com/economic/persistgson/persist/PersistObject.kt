package com.economic.persistgson.persist


/**
 * Created by Tudor Dragan on 03/05/2017.
 * Copyright © e-conomic.com
 */

/**
 * Defines a generic object that has persist functionality. The purpose of this class
 * is to construct a default instance of a class that can be used for storing unknown
 * JSON properties
 *
 * */
interface PersistObject {
    companion object {
        /**
         * The [_persistMapReflectiveFieldKey] is a key value that must correspond to the [PersistObject]
         * persist map field name.
         * */
        val _persistMapReflectiveFieldKey = "_persistMap"
    }

    /**
     * The [_persistMap] is a mutable map that will contain any unknown properties that could not be
     * mapped to the Model object
     * */
    val _persistMap: MutableMap<String, Any>
}
