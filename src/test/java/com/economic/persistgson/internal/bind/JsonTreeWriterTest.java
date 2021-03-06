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

import com.economic.persistgson.JsonNull;

import java.io.IOException;
import junit.framework.TestCase;

@SuppressWarnings("resource")
public final class JsonTreeWriterTest extends TestCase {
  public void testArray() throws IOException {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();
    assertEquals("[1,2,3]", writer.get().toString());
  }

  public void testNestedArray() throws IOException {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
    writer.endArray();
    writer.beginArray();
    writer.beginArray();
    writer.endArray();
    writer.endArray();
    writer.endArray();
    assertEquals("[[],[[]]]", writer.get().toString());
  }

  public void testObject() throws IOException {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    writer.beginObject();
    writer.name("A").value(1);
    writer.name("B").value(2);
    writer.endObject();
    assertEquals("{\"A\":1,\"B\":2}", writer.get().toString());
  }

  public void testNestedObject() throws IOException {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    writer.beginObject();
    writer.name("A");
    writer.beginObject();
    writer.name("B");
    writer.beginObject();
    writer.endObject();
    writer.endObject();
    writer.name("C");
    writer.beginObject();
    writer.endObject();
    writer.endObject();
    assertEquals("{\"A\":{\"B\":{}},\"C\":{}}", writer.get().toString());
  }

  public void testWriteAfterClose() throws Exception {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    writer.setLenient(true);
    writer.beginArray();
    writer.value("A");
    writer.endArray();
    writer.close();
    try {
      writer.beginArray();
      fail();
    } catch (IllegalStateException expected) {
    }
  }

  public void testPrematureClose() throws Exception {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    writer.setLenient(true);
    writer.beginArray();
    try {
      writer.close();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testSerializeNullsFalse() throws IOException {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    writer.setSerializeNulls(false);
    writer.beginObject();
    writer.name("A");
    writer.nullValue();
    writer.endObject();
    assertEquals("{}", writer.get().toString());
  }

  public void testSerializeNullsTrue() throws IOException {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    writer.setSerializeNulls(true);
    writer.beginObject();
    writer.name("A");
    writer.nullValue();
    writer.endObject();
    assertEquals("{\"A\":null}", writer.get().toString());
  }

  public void testEmptyWriter() {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    assertEquals(JsonNull.INSTANCE, writer.get());
  }

  public void testLenientNansAndInfinities() throws IOException {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    writer.setLenient(true);
    writer.beginArray();
    writer.value(Double.NaN);
    writer.value(Double.NEGATIVE_INFINITY);
    writer.value(Double.POSITIVE_INFINITY);
    writer.endArray();
    assertEquals("[NaN,-Infinity,Infinity]", writer.get().toString());
  }

  public void testStrictNansAndInfinities() throws IOException {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    writer.setLenient(false);
    writer.beginArray();
    try {
      writer.value(Double.NaN);
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      writer.value(Double.NEGATIVE_INFINITY);
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      writer.value(Double.POSITIVE_INFINITY);
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testStrictBoxedNansAndInfinities() throws IOException {
    com.economic.persistgson.internal.bind.JsonTreeWriter writer = new com.economic.persistgson.internal.bind.JsonTreeWriter();
    writer.setLenient(false);
    writer.beginArray();
    try {
      writer.value(new Double(Double.NaN));
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      writer.value(new Double(Double.NEGATIVE_INFINITY));
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      writer.value(new Double(Double.POSITIVE_INFINITY));
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }
}
