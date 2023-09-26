package com.github.valdr;

import java.util.Map;
import java.util.Set;

/**
 * Max-reduced map interface to allow for POJOs that delegate to an internal {@link Map} rather than extend one. The
 * map key must a {@link String}.
 *
 * @param <V> map value type
 */
public interface MinimalMap<V> {
  /**
   * Returns a {@link Set} view of the mappings contained in this map. The set is backed by the map,
   * so changes to the map are reflected in the set, and vice-versa. If the map is modified while an
   * iteration over the set is in progress (except through the iterator's own <code>remove</code> operation,
   * or through the <code>setValue</code> operation on a map entry returned by the iterator) the results of
   * the iteration are undefined. The set supports element removal, which removes the corresponding
   * mapping from the map, via the <code>Iterator.remove</code>, <code>Set.remove</code>, <code>removeAll</code>,
   * <code>retainAll</code> and <code>clear</code> operations. It does not support the <code>add</code> or
   * <code>addAll</code> operations.
   *
   * @return a set view of the mappings contained in this map
   */
  Set<Map.Entry<String, V>> entrySet();

  /**
   * Associates the specified value with the specified key in this map.  If the map previously contained a mapping for
   * the key, the old value is replaced by the specified value.
   *
   * @param key   key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return the previous value associated with <code>key</code>, or <code>null</code> if there was no mapping for
   * <code>key</code>. (A <code>null</code> return can also indicate that the map previously associated <code>null</code>
   * with
   * <code>key</code>, if the implementation supports <code>null</code> values.)
   * @throws UnsupportedOperationException if the <code>put</code> operation
   *                                       is not supported by this map
   * @throws ClassCastException            if the class of the specified key or value
   *                                       prevents it from being stored in this map
   * @throws NullPointerException          if the specified key or value is null
   *                                       and this map does not permit null keys or values
   * @throws IllegalArgumentException      if some property of the specified key
   *                                       or value prevents it from being stored in this map
   */
  V put(String key, V value);

  /**
   * Returns the number of key-value mappings in this map. If the map contains more than <code>Integer.MAX_VALUE</code>
   * elements, returns <code>Integer.MAX_VALUE</code>.
   *
   * @return the number of key-value mappings in this map
   */
  int size();
}
