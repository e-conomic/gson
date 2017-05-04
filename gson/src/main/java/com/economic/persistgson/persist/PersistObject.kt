package com.economic.persistgson.persist

import java.util.HashMap

/**
 * Created by Tudor Dragan on 03/05/2017.
 * Copyright © e-conomic.com
 */

interface PersistObject {
    val persistMap: MutableMap<String, Any>
}
