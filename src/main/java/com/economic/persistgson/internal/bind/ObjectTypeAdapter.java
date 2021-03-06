/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.economic.persistgson.internal.bind;

import com.economic.persistgson.Gson;
import com.economic.persistgson.JsonElement;
import com.economic.persistgson.JsonParser;
import com.economic.persistgson.TypeAdapterFactory;
import com.economic.persistgson.stream.JsonReader;
import com.economic.persistgson.stream.JsonToken;
import com.economic.persistgson.stream.JsonWriter;
import com.economic.persistgson.TypeAdapter;
import com.economic.persistgson.internal.LinkedTreeMap;
import com.economic.persistgson.reflect.TypeToken;
import com.economic.persistgson.stream.MalformedJsonException;
import com.google.common.math.DoubleMath;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Adapts types whose static type is only 'Object'. Uses getClass() on
 * serialization and a primitive/Map/List on deserialization.
 */
public final class ObjectTypeAdapter extends TypeAdapter<Object> {
  public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
    @SuppressWarnings("unchecked")
    @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      if (type.getRawType() == Object.class) {
        return (TypeAdapter<T>) new ObjectTypeAdapter(gson);
      }
      return null;
    }
  };

  private final Gson gson;
  private final JsonParser jsonParser = new JsonParser();

  ObjectTypeAdapter(Gson gson) {
    this.gson = gson;
  }

  @Override public Object read(JsonReader in) throws IOException {
    JsonToken token = in.peek();
    switch (token) {
    case BEGIN_ARRAY:
      List<Object> list = new ArrayList<Object>();
      in.beginArray();
      while (in.hasNext()) {
        list.add(read(in));
      }
      in.endArray();
      return list;

    case BEGIN_OBJECT:
      Map<String, Object> map = new LinkedTreeMap<String, Object>();
      in.beginObject();
      while (in.hasNext()) {
        map.put(in.nextName(), read(in));
      }
      in.endObject();
      return map;

    case STRING:
      return in.nextString();

    case NUMBER:
      JsonElement element = jsonParser.parse(in);
      String elementString = element.getAsString();
      double asDouble = Double.parseDouble(elementString); // don't catch this NumberFormatException.
      if ((Double.isNaN(asDouble) || Double.isInfinite(asDouble))) {
        throw new com.economic.persistgson.stream.MalformedJsonException(
                "JSON forbids NaN and infinities: " + asDouble);
      }
      if (DoubleMath.isMathematicalInteger(asDouble)) {
        return (long) asDouble;
      } else {
        return asDouble;
      }
    case BOOLEAN:
      return in.nextBoolean();

    case NULL:
      in.nextNull();
      return null;

    default:
      throw new IllegalStateException();
    }
  }

  @SuppressWarnings("unchecked")
  @Override public void write(JsonWriter out, Object value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }

    TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) gson.getAdapter(value.getClass());
    if (typeAdapter instanceof ObjectTypeAdapter) {
      out.beginObject();
      out.endObject();
      return;
    }

    typeAdapter.write(out, value);
  }
}
