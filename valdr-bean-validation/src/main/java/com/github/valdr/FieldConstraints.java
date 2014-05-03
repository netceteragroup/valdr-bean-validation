package com.github.valdr;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maps constraint name to constraint attributes (Bean Validation annotation attributes).
 */
public class FieldConstraints implements MinimalMap<MinimalObjectMap> {
  private final Map<String, MinimalObjectMap> map = new HashMap<>();

  @Override
  public Set<Map.Entry<String, MinimalObjectMap>> entrySet() {
    return map.entrySet();
  }

  @Override
  public MinimalObjectMap put(String key, MinimalObjectMap value) {
    return map.put(key, value);
  }

  @Override
  public int size() {
    return map.size();
  }
}
