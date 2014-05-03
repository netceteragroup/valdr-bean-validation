package com.github.valdr.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.valdr.MinimalMap;

import java.io.IOException;
import java.util.Map;

/**
 * Serializes an {@link com.github.valdr.MinimalMap} like any other JSON map. All values are serialized by iterating
 * over the entry set.
 *
 * @param <V> map value type
 */
public class MinimalMapSerializer<V> extends JsonSerializer<MinimalMap<V>> {
  @Override
  public void serialize(MinimalMap<V> attributes, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    throws IOException {
    jsonGenerator.writeStartObject();
    for (Map.Entry<String, V> stringObjectEntry : attributes.entrySet()) {
      jsonGenerator.writeObjectField(stringObjectEntry.getKey(), stringObjectEntry.getValue());
    }
    jsonGenerator.writeEndObject();
  }
}
