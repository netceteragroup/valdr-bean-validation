package com.github.valdr.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.valdr.AttributeMap;

import java.io.IOException;
import java.util.Map;

/**
 * Serializes an {@link AttributeMap} like any other JSON map. All values are serialized by iterating over the entry
 * set.
 */
public class AttributeMapSerializer extends JsonSerializer<AttributeMap> {
  @Override
  public void serialize(AttributeMap attributes, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    throws IOException {
    jsonGenerator.writeStartObject();
    for (Map.Entry<String, Object> stringObjectEntry : attributes.entrySet()) {
      jsonGenerator.writeObjectField(stringObjectEntry.getKey(), stringObjectEntry.getValue());
    }
    jsonGenerator.writeEndObject();
  }
}
