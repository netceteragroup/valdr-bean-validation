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
   * iteration over the set is in progress (except through the iterator's own <tt>remove</tt> operation,
   * or through the <tt>setValue</tt> operation on a map entry returned by the iterator) the results of
   * the iteration are undefined. The set supports element removal, which removes the corresponding
   * mapping from the map, via the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
   * <tt>retainAll</tt> and <tt>clear</tt> operations. It does not support the <tt>add</tt> or
   * <tt>addAll</tt> operations.
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
   * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for
   *         <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map previously associated <tt>null</tt>
   *         with
   *         <tt>key</tt>, if the implementation supports <tt>null</tt> values.)
   * @throws UnsupportedOperationException if the <tt>put</tt> operation
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
   * Returns the number of key-value mappings in this map. If the map contains more than <tt>Integer.MAX_VALUE</tt>
   * elements, returns <tt>Integer.MAX_VALUE</tt>.
   *
   * @return the number of key-value mappings in this map
   */
  int size();
}
