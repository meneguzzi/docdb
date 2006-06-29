package org.soton.docdb.diff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

/**
 * A simple implementation of a <code>MultiMap</code> that allows 
 * multiple values to be stored for any single key. 
 * @author Felipe
 *
 * @param <K> The type for the keys in the map
 * @param <V> The type for the values in the map
 */
public class SimpleMultimap<K,V> implements Iterable{
	private HashMap<K, List<V>> hashMap;
	
	protected SimpleMultimap() {
		hashMap = new HashMap<K, List<V>>();
	}
	
	public void put(K key, V value) {
		if(hashMap.containsKey(key)) {
			hashMap.get(key).add(value);
		} else {
			List<V> list = new ArrayList<V>();
			list.add(value);
			hashMap.put(key, list);
		}
	}
	
	/**
	 * Returns an iterator to all of the files with the same individual 
	 * key, or null if no such key exists in the
	 * mapping.
	 * 
	 * @param key A key to the multimap
	 * @return An iterator over the values already stored
	 */
	public Iterator<V> get(K key) {
		if(hashMap.containsKey(key)) {
			return hashMap.get(key).iterator();
		} else {
			return null;
		}
	}
	
	public List<V> getValues(K key) {
		return hashMap.get(key);
	}

	/* (non-Javadoc)
	 * @see java.util.HashMap#entrySet()
	 */
	public Set<Entry<K, List<V>>> entrySet() {
		return hashMap.entrySet();
	}

	/* (non-Javadoc)
	 * @see java.util.HashMap#keySet()
	 */
	public Set<K> keySet() {
		return hashMap.keySet();
	}

	public Iterator iterator() {
		return this.entrySet().iterator();
	}
}