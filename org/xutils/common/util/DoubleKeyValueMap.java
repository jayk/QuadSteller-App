package org.xutils.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DoubleKeyValueMap<K1, K2, V> {
  private final ConcurrentHashMap<K1, ConcurrentHashMap<K2, V>> k1_k2V_map = new ConcurrentHashMap<K1, ConcurrentHashMap<K2, V>>();
  
  public void clear() {
    if (this.k1_k2V_map.size() > 0) {
      Iterator<ConcurrentHashMap> iterator = this.k1_k2V_map.values().iterator();
      while (iterator.hasNext())
        ((ConcurrentHashMap)iterator.next()).clear(); 
      this.k1_k2V_map.clear();
    } 
  }
  
  public boolean containsKey(K1 paramK1) {
    return this.k1_k2V_map.containsKey(paramK1);
  }
  
  public boolean containsKey(K1 paramK1, K2 paramK2) {
    return this.k1_k2V_map.containsKey(paramK1) ? ((ConcurrentHashMap)this.k1_k2V_map.get(paramK1)).containsKey(paramK2) : false;
  }
  
  public V get(K1 paramK1, K2 paramK2) {
    null = this.k1_k2V_map.get(paramK1);
    return (V)((null == null) ? null : null.get(paramK2));
  }
  
  public ConcurrentHashMap<K2, V> get(K1 paramK1) {
    return this.k1_k2V_map.get(paramK1);
  }
  
  public Collection<V> getAllValues() {
    Collection<V> collection = null;
    Set set = this.k1_k2V_map.keySet();
    if (set != null) {
      ArrayList arrayList = new ArrayList();
      Iterator<ArrayList> iterator = set.iterator();
      while (true) {
        collection = arrayList;
        if (iterator.hasNext()) {
          collection = iterator.next();
          collection = ((ConcurrentHashMap)this.k1_k2V_map.get(collection)).values();
          if (collection != null)
            arrayList.addAll(collection); 
          continue;
        } 
        break;
      } 
    } 
    return collection;
  }
  
  public Collection<V> getAllValues(K1 paramK1) {
    ConcurrentHashMap concurrentHashMap = this.k1_k2V_map.get(paramK1);
    return (concurrentHashMap == null) ? null : concurrentHashMap.values();
  }
  
  public Set<K1> getFirstKeys() {
    return this.k1_k2V_map.keySet();
  }
  
  public void put(K1 paramK1, K2 paramK2, V paramV) {
    if (paramK1 != null && paramK2 != null && paramV != null) {
      if (this.k1_k2V_map.containsKey(paramK1)) {
        ConcurrentHashMap<K2, V> concurrentHashMap1 = this.k1_k2V_map.get(paramK1);
        if (concurrentHashMap1 != null) {
          concurrentHashMap1.put(paramK2, paramV);
          return;
        } 
        concurrentHashMap1 = new ConcurrentHashMap<K2, V>();
        concurrentHashMap1.put(paramK2, paramV);
        this.k1_k2V_map.put(paramK1, concurrentHashMap1);
        return;
      } 
      ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<Object, Object>();
      concurrentHashMap.put(paramK2, paramV);
      this.k1_k2V_map.put(paramK1, concurrentHashMap);
    } 
  }
  
  public void remove(K1 paramK1) {
    this.k1_k2V_map.remove(paramK1);
  }
  
  public void remove(K1 paramK1, K2 paramK2) {
    ConcurrentHashMap concurrentHashMap = this.k1_k2V_map.get(paramK1);
    if (concurrentHashMap != null)
      concurrentHashMap.remove(paramK2); 
    if (concurrentHashMap == null || concurrentHashMap.isEmpty())
      this.k1_k2V_map.remove(paramK1); 
  }
  
  public int size() {
    if (this.k1_k2V_map.size() == 0)
      return 0; 
    int i = 0;
    Iterator<ConcurrentHashMap> iterator = this.k1_k2V_map.values().iterator();
    while (true) {
      int j = i;
      if (iterator.hasNext()) {
        i += ((ConcurrentHashMap)iterator.next()).size();
        continue;
      } 
      return j;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/common/util/DoubleKeyValueMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */