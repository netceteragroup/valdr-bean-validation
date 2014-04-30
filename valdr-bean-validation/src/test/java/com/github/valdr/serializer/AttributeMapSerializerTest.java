package com.github.valdr.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.valdr.AttributeMap;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link AttributeMapSerializer}.
 */
public class AttributeMapSerializerTest {
  private static final String LS = System.getProperty("line.separator");
  private TestAttributeMap attributeMap;

  /**
   * Initializes the map.
   */
  @Before
  public void setup() {
    attributeMap = new TestAttributeMap();
  }

  /**
   * See method name.
   */
  @Test
  public void shouldProduceEmptyJsonForEmptyMap() {
    // given empty attributeMap
    // when
    String json = getJson();
    // then
    assertThat(json, is("{ }"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldProduceFullJsonForNonEmptyMap() {
    // given
    attributeMap.map.put("foo", "bar");
    attributeMap.map.put("super-hero", Boolean.TRUE);
    // when
    String json = getJson();
    // then
    assertThat(json, is("{" + LS + "  \"foo\" : \"bar\"," + LS + "  \"super-hero\" : true" + LS + "}"));
  }

  /**
   * See method name.
   */
  @Test
  public void shouldSupportObjectMapValue() {
    // given
    attributeMap.map.put("foo", new ClassWithString("bar"));
    // when
    String json = getJson();
    // then
    assertThat(json, is("{" + LS + "  \"foo\" : {" + LS + "    \"string\" : \"bar\"" + LS + "  }" + LS + "}"));
  }

  private String getJson() {
    ObjectMapper objectMapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(AttributeMap.class, new AttributeMapSerializer());
    objectMapper.registerModule(module);

    ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
    try {
      return ow.writeValueAsString(attributeMap);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Value
  private static final class ClassWithString {
    private final String string;
  }

  private static final class TestAttributeMap implements AttributeMap {

    private final Map<String, Object> map = new LinkedHashMap<>(4);

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
      return map.entrySet();
    }
  }
}
