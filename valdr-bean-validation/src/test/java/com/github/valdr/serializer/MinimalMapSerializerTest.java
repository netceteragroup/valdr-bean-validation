package com.github.valdr.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.valdr.MinimalMap;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests {@link MinimalMapSerializer}.
 */
public class MinimalMapSerializerTest {
  private static final String LS = System.getProperty("line.separator");
  private TestMinimalMap attributeMap;

  /**
   * Initializes the map.
   */
  @BeforeEach
  public void setup() {
    attributeMap = new TestMinimalMap();
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
    module.addSerializer(MinimalMap.class, new MinimalMapSerializer());
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

  private static final class TestMinimalMap implements MinimalMap<Object> {

    private final Map<String, Object> map = new LinkedHashMap<>(4);

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
      return map.entrySet();
    }

    @Override
    public int size() {
      return map.size();
    }

    @Override
    public Object put(String key, Object value) {
      return map.put(key, value);
    }
  }
}
