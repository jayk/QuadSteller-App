package com.google.gson.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public final class LinkedTreeMap<K, V> extends AbstractMap<K, V> implements Serializable {
  private static final Comparator<Comparable> NATURAL_ORDER = new Comparator<Comparable>() {
      public int compare(Comparable<Comparable> param1Comparable1, Comparable param1Comparable2) {
        return param1Comparable1.compareTo(param1Comparable2);
      }
    };
  
  Comparator<? super K> comparator;
  
  private EntrySet entrySet;
  
  final Node<K, V> header;
  
  private KeySet keySet;
  
  int modCount;
  
  Node<K, V> root;
  
  int size;
  
  public LinkedTreeMap() {
    this((Comparator)NATURAL_ORDER);
  }
  
  public LinkedTreeMap(Comparator<? super K> paramComparator) {
    Comparator<Comparable> comparator;
    this.size = 0;
    this.modCount = 0;
    this.header = new Node<K, V>();
    if (paramComparator == null)
      comparator = NATURAL_ORDER; 
    this.comparator = (Comparator)comparator;
  }
  
  private boolean equal(Object paramObject1, Object paramObject2) {
    return (paramObject1 == paramObject2 || (paramObject1 != null && paramObject1.equals(paramObject2)));
  }
  
  private void rebalance(Node<K, V> paramNode, boolean paramBoolean) {
    while (true) {
      Node<K, V> node;
      int i;
      byte b;
      int j;
      if (paramNode != null) {
        node = paramNode.left;
        Node<K, V> node1 = paramNode.right;
        if (node != null) {
          i = node.height;
        } else {
          i = 0;
        } 
        if (node1 != null) {
          b = node1.height;
        } else {
          b = 0;
        } 
        j = i - b;
        if (j == -2) {
          Node<K, V> node2 = node1.left;
          node = node1.right;
          if (node != null) {
            i = node.height;
          } else {
            i = 0;
          } 
          if (node2 != null) {
            b = node2.height;
          } else {
            b = 0;
          } 
          i = b - i;
          if (i == -1 || (i == 0 && !paramBoolean)) {
            rotateLeft(paramNode);
          } else {
            assert i == 1;
            rotateRight(node1);
            rotateLeft(paramNode);
          } 
          if (paramBoolean)
            return; 
          continue;
        } 
      } else {
        return;
      } 
      if (j == 2) {
        Node<K, V> node1 = node.left;
        Node<K, V> node2 = node.right;
        if (node2 != null) {
          i = node2.height;
        } else {
          i = 0;
        } 
        if (node1 != null) {
          b = node1.height;
        } else {
          b = 0;
        } 
        i = b - i;
        if (i == 1 || (i == 0 && !paramBoolean)) {
          rotateRight(paramNode);
        } else {
          assert i == -1;
          rotateLeft(node);
          rotateRight(paramNode);
        } 
        if (!paramBoolean)
          continue; 
        return;
      } 
      if (j == 0) {
        paramNode.height = i + 1;
        if (paramBoolean)
          return; 
      } else {
        assert j == -1 || j == 1;
        paramNode.height = Math.max(i, b) + 1;
        if (!paramBoolean)
          return; 
      } 
      continue;
      paramNode = paramNode.parent;
    } 
  }
  
  private void replaceInParent(Node<K, V> paramNode1, Node<K, V> paramNode2) {
    Node<K, V> node = paramNode1.parent;
    paramNode1.parent = null;
    if (paramNode2 != null)
      paramNode2.parent = node; 
    if (node != null) {
      if (node.left == paramNode1) {
        node.left = paramNode2;
        return;
      } 
      assert node.right == paramNode1;
      node.right = paramNode2;
      return;
    } 
    this.root = paramNode2;
  }
  
  private void rotateLeft(Node<K, V> paramNode) {
    byte b = 0;
    Node<K, V> node1 = paramNode.left;
    Node<K, V> node2 = paramNode.right;
    Node<K, V> node3 = node2.left;
    Node<K, V> node4 = node2.right;
    paramNode.right = node3;
    if (node3 != null)
      node3.parent = paramNode; 
    replaceInParent(paramNode, node2);
    node2.left = paramNode;
    paramNode.parent = node2;
    if (node1 != null) {
      i = node1.height;
    } else {
      i = 0;
    } 
    if (node3 != null) {
      j = node3.height;
    } else {
      j = 0;
    } 
    paramNode.height = Math.max(i, j) + 1;
    int j = paramNode.height;
    int i = b;
    if (node4 != null)
      i = node4.height; 
    node2.height = Math.max(j, i) + 1;
  }
  
  private void rotateRight(Node<K, V> paramNode) {
    byte b = 0;
    Node<K, V> node1 = paramNode.left;
    Node<K, V> node2 = paramNode.right;
    Node<K, V> node3 = node1.left;
    Node<K, V> node4 = node1.right;
    paramNode.left = node4;
    if (node4 != null)
      node4.parent = paramNode; 
    replaceInParent(paramNode, node1);
    node1.right = paramNode;
    paramNode.parent = node1;
    if (node2 != null) {
      i = node2.height;
    } else {
      i = 0;
    } 
    if (node4 != null) {
      j = node4.height;
    } else {
      j = 0;
    } 
    paramNode.height = Math.max(i, j) + 1;
    int j = paramNode.height;
    int i = b;
    if (node3 != null)
      i = node3.height; 
    node1.height = Math.max(j, i) + 1;
  }
  
  private Object writeReplace() throws ObjectStreamException {
    return new LinkedHashMap<Object, Object>(this);
  }
  
  public void clear() {
    this.root = null;
    this.size = 0;
    this.modCount++;
    Node<K, V> node = this.header;
    node.prev = node;
    node.next = node;
  }
  
  public boolean containsKey(Object paramObject) {
    return (findByObject(paramObject) != null);
  }
  
  public Set<Map.Entry<K, V>> entrySet() {
    EntrySet entrySet = this.entrySet;
    if (entrySet == null) {
      entrySet = new EntrySet();
      this.entrySet = entrySet;
    } 
    return entrySet;
  }
  
  Node<K, V> find(K paramK, boolean paramBoolean) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_0
    //   3: getfield comparator : Ljava/util/Comparator;
    //   6: astore #4
    //   8: aload_0
    //   9: getfield root : Lcom/google/gson/internal/LinkedTreeMap$Node;
    //   12: astore #5
    //   14: iconst_0
    //   15: istore #6
    //   17: aload #5
    //   19: astore #7
    //   21: aload #5
    //   23: ifnull -> 112
    //   26: aload #4
    //   28: getstatic com/google/gson/internal/LinkedTreeMap.NATURAL_ORDER : Ljava/util/Comparator;
    //   31: if_acmpne -> 67
    //   34: aload_1
    //   35: checkcast java/lang/Comparable
    //   38: astore #8
    //   40: aload #8
    //   42: ifnull -> 73
    //   45: aload #8
    //   47: aload #5
    //   49: getfield key : Ljava/lang/Object;
    //   52: invokeinterface compareTo : (Ljava/lang/Object;)I
    //   57: istore #6
    //   59: iload #6
    //   61: ifne -> 91
    //   64: aload #5
    //   66: areturn
    //   67: aconst_null
    //   68: astore #8
    //   70: goto -> 40
    //   73: aload #4
    //   75: aload_1
    //   76: aload #5
    //   78: getfield key : Ljava/lang/Object;
    //   81: invokeinterface compare : (Ljava/lang/Object;Ljava/lang/Object;)I
    //   86: istore #6
    //   88: goto -> 59
    //   91: iload #6
    //   93: ifge -> 178
    //   96: aload #5
    //   98: getfield left : Lcom/google/gson/internal/LinkedTreeMap$Node;
    //   101: astore #7
    //   103: aload #7
    //   105: ifnonnull -> 188
    //   108: aload #5
    //   110: astore #7
    //   112: aload_3
    //   113: astore #5
    //   115: iload_2
    //   116: ifeq -> 64
    //   119: aload_0
    //   120: getfield header : Lcom/google/gson/internal/LinkedTreeMap$Node;
    //   123: astore #5
    //   125: aload #7
    //   127: ifnonnull -> 244
    //   130: aload #4
    //   132: getstatic com/google/gson/internal/LinkedTreeMap.NATURAL_ORDER : Ljava/util/Comparator;
    //   135: if_acmpne -> 195
    //   138: aload_1
    //   139: instanceof java/lang/Comparable
    //   142: ifne -> 195
    //   145: new java/lang/ClassCastException
    //   148: dup
    //   149: new java/lang/StringBuilder
    //   152: dup
    //   153: invokespecial <init> : ()V
    //   156: aload_1
    //   157: invokevirtual getClass : ()Ljava/lang/Class;
    //   160: invokevirtual getName : ()Ljava/lang/String;
    //   163: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   166: ldc ' is not Comparable'
    //   168: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   171: invokevirtual toString : ()Ljava/lang/String;
    //   174: invokespecial <init> : (Ljava/lang/String;)V
    //   177: athrow
    //   178: aload #5
    //   180: getfield right : Lcom/google/gson/internal/LinkedTreeMap$Node;
    //   183: astore #7
    //   185: goto -> 103
    //   188: aload #7
    //   190: astore #5
    //   192: goto -> 40
    //   195: new com/google/gson/internal/LinkedTreeMap$Node
    //   198: dup
    //   199: aload #7
    //   201: aload_1
    //   202: aload #5
    //   204: aload #5
    //   206: getfield prev : Lcom/google/gson/internal/LinkedTreeMap$Node;
    //   209: invokespecial <init> : (Lcom/google/gson/internal/LinkedTreeMap$Node;Ljava/lang/Object;Lcom/google/gson/internal/LinkedTreeMap$Node;Lcom/google/gson/internal/LinkedTreeMap$Node;)V
    //   212: astore_1
    //   213: aload_0
    //   214: aload_1
    //   215: putfield root : Lcom/google/gson/internal/LinkedTreeMap$Node;
    //   218: aload_0
    //   219: aload_0
    //   220: getfield size : I
    //   223: iconst_1
    //   224: iadd
    //   225: putfield size : I
    //   228: aload_0
    //   229: aload_0
    //   230: getfield modCount : I
    //   233: iconst_1
    //   234: iadd
    //   235: putfield modCount : I
    //   238: aload_1
    //   239: astore #5
    //   241: goto -> 64
    //   244: new com/google/gson/internal/LinkedTreeMap$Node
    //   247: dup
    //   248: aload #7
    //   250: aload_1
    //   251: aload #5
    //   253: aload #5
    //   255: getfield prev : Lcom/google/gson/internal/LinkedTreeMap$Node;
    //   258: invokespecial <init> : (Lcom/google/gson/internal/LinkedTreeMap$Node;Ljava/lang/Object;Lcom/google/gson/internal/LinkedTreeMap$Node;Lcom/google/gson/internal/LinkedTreeMap$Node;)V
    //   261: astore_1
    //   262: iload #6
    //   264: ifge -> 283
    //   267: aload #7
    //   269: aload_1
    //   270: putfield left : Lcom/google/gson/internal/LinkedTreeMap$Node;
    //   273: aload_0
    //   274: aload #7
    //   276: iconst_1
    //   277: invokespecial rebalance : (Lcom/google/gson/internal/LinkedTreeMap$Node;Z)V
    //   280: goto -> 218
    //   283: aload #7
    //   285: aload_1
    //   286: putfield right : Lcom/google/gson/internal/LinkedTreeMap$Node;
    //   289: goto -> 273
  }
  
  Node<K, V> findByEntry(Map.Entry<?, ?> paramEntry) {
    boolean bool;
    Node<K, V> node = findByObject(paramEntry.getKey());
    if (node != null && equal(node.value, paramEntry.getValue())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool ? node : null;
  }
  
  Node<K, V> findByObject(Object paramObject) {
    Node<K, V> node1 = null;
    Node<K, V> node2 = node1;
    if (paramObject != null)
      try {
        node2 = find((K)paramObject, false);
      } catch (ClassCastException classCastException) {
        node2 = node1;
      }  
    return node2;
  }
  
  public V get(Object<K, V> paramObject) {
    paramObject = (Object<K, V>)findByObject(paramObject);
    return (paramObject != null) ? ((Node)paramObject).value : null;
  }
  
  public Set<K> keySet() {
    KeySet keySet = this.keySet;
    if (keySet == null) {
      keySet = new KeySet();
      this.keySet = keySet;
    } 
    return keySet;
  }
  
  public V put(K paramK, V paramV) {
    if (paramK == null)
      throw new NullPointerException("key == null"); 
    Node<K, V> node = find(paramK, true);
    V v = node.value;
    node.value = paramV;
    return v;
  }
  
  public V remove(Object<K, V> paramObject) {
    paramObject = (Object<K, V>)removeInternalByKey(paramObject);
    return (paramObject != null) ? ((Node)paramObject).value : null;
  }
  
  void removeInternal(Node<K, V> paramNode, boolean paramBoolean) {
    if (paramBoolean) {
      paramNode.prev.next = paramNode.next;
      paramNode.next.prev = paramNode.prev;
    } 
    Node<K, V> node1 = paramNode.left;
    Node<K, V> node2 = paramNode.right;
    Node<K, V> node3 = paramNode.parent;
    if (node1 != null && node2 != null) {
      if (node1.height > node2.height) {
        node1 = node1.last();
      } else {
        node1 = node2.first();
      } 
      removeInternal(node1, false);
      int i = 0;
      node2 = paramNode.left;
      if (node2 != null) {
        i = node2.height;
        node1.left = node2;
        node2.parent = node1;
        paramNode.left = null;
      } 
      int j = 0;
      node2 = paramNode.right;
      if (node2 != null) {
        j = node2.height;
        node1.right = node2;
        node2.parent = node1;
        paramNode.right = null;
      } 
      node1.height = Math.max(i, j) + 1;
      replaceInParent(paramNode, node1);
      return;
    } 
    if (node1 != null) {
      replaceInParent(paramNode, node1);
      paramNode.left = null;
    } else if (node2 != null) {
      replaceInParent(paramNode, node2);
      paramNode.right = null;
    } else {
      replaceInParent(paramNode, null);
    } 
    rebalance(node3, false);
    this.size--;
    this.modCount++;
  }
  
  Node<K, V> removeInternalByKey(Object<K, V> paramObject) {
    paramObject = (Object<K, V>)findByObject(paramObject);
    if (paramObject != null)
      removeInternal((Node<K, V>)paramObject, true); 
    return (Node<K, V>)paramObject;
  }
  
  public int size() {
    return this.size;
  }
  
  static {
    boolean bool;
    if (!LinkedTreeMap.class.desiredAssertionStatus()) {
      bool = true;
    } else {
      bool = false;
    } 
    $assertionsDisabled = bool;
  }
  
  class EntrySet extends AbstractSet<Map.Entry<K, V>> {
    public void clear() {
      LinkedTreeMap.this.clear();
    }
    
    public boolean contains(Object param1Object) {
      return (param1Object instanceof Map.Entry && LinkedTreeMap.this.findByEntry((Map.Entry<?, ?>)param1Object) != null);
    }
    
    public Iterator<Map.Entry<K, V>> iterator() {
      return new LinkedTreeMap<K, V>.LinkedTreeMapIterator<Map.Entry<K, V>>() {
          public Map.Entry<K, V> next() {
            return nextNode();
          }
        };
    }
    
    public boolean remove(Object param1Object) {
      boolean bool = false;
      if (param1Object instanceof Map.Entry) {
        param1Object = LinkedTreeMap.this.findByEntry((Map.Entry<?, ?>)param1Object);
        if (param1Object != null) {
          LinkedTreeMap.this.removeInternal((LinkedTreeMap.Node)param1Object, true);
          bool = true;
        } 
      } 
      return bool;
    }
    
    public int size() {
      return LinkedTreeMap.this.size;
    }
  }
  
  class null extends LinkedTreeMapIterator<Map.Entry<K, V>> {
    public Map.Entry<K, V> next() {
      return nextNode();
    }
  }
  
  class KeySet extends AbstractSet<K> {
    public void clear() {
      LinkedTreeMap.this.clear();
    }
    
    public boolean contains(Object param1Object) {
      return LinkedTreeMap.this.containsKey(param1Object);
    }
    
    public Iterator<K> iterator() {
      return new LinkedTreeMap<K, V>.LinkedTreeMapIterator<K>() {
          public K next() {
            return (nextNode()).key;
          }
        };
    }
    
    public boolean remove(Object param1Object) {
      return (LinkedTreeMap.this.removeInternalByKey(param1Object) != null);
    }
    
    public int size() {
      return LinkedTreeMap.this.size;
    }
  }
  
  class null extends LinkedTreeMapIterator<K> {
    public K next() {
      return (nextNode()).key;
    }
  }
  
  private abstract class LinkedTreeMapIterator<T> implements Iterator<T> {
    int expectedModCount = LinkedTreeMap.this.modCount;
    
    LinkedTreeMap.Node<K, V> lastReturned = null;
    
    LinkedTreeMap.Node<K, V> next = LinkedTreeMap.this.header.next;
    
    private LinkedTreeMapIterator() {}
    
    public final boolean hasNext() {
      return (this.next != LinkedTreeMap.this.header);
    }
    
    final LinkedTreeMap.Node<K, V> nextNode() {
      LinkedTreeMap.Node<K, V> node = this.next;
      if (node == LinkedTreeMap.this.header)
        throw new NoSuchElementException(); 
      if (LinkedTreeMap.this.modCount != this.expectedModCount)
        throw new ConcurrentModificationException(); 
      this.next = node.next;
      this.lastReturned = node;
      return node;
    }
    
    public final void remove() {
      if (this.lastReturned == null)
        throw new IllegalStateException(); 
      LinkedTreeMap.this.removeInternal(this.lastReturned, true);
      this.lastReturned = null;
      this.expectedModCount = LinkedTreeMap.this.modCount;
    }
  }
  
  static final class Node<K, V> implements Map.Entry<K, V> {
    int height;
    
    final K key;
    
    Node<K, V> left;
    
    Node<K, V> next;
    
    Node<K, V> parent;
    
    Node<K, V> prev;
    
    Node<K, V> right;
    
    V value;
    
    Node() {
      this.key = null;
      this.prev = this;
      this.next = this;
    }
    
    Node(Node<K, V> param1Node1, K param1K, Node<K, V> param1Node2, Node<K, V> param1Node3) {
      this.parent = param1Node1;
      this.key = param1K;
      this.height = 1;
      this.next = param1Node2;
      this.prev = param1Node3;
      param1Node3.next = this;
      param1Node2.prev = this;
    }
    
    public boolean equals(Object param1Object) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_2
      //   2: iload_2
      //   3: istore_3
      //   4: aload_1
      //   5: instanceof java/util/Map$Entry
      //   8: ifeq -> 54
      //   11: aload_1
      //   12: checkcast java/util/Map$Entry
      //   15: astore_1
      //   16: aload_0
      //   17: getfield key : Ljava/lang/Object;
      //   20: ifnonnull -> 56
      //   23: iload_2
      //   24: istore_3
      //   25: aload_1
      //   26: invokeinterface getKey : ()Ljava/lang/Object;
      //   31: ifnonnull -> 54
      //   34: aload_0
      //   35: getfield value : Ljava/lang/Object;
      //   38: ifnonnull -> 77
      //   41: iload_2
      //   42: istore_3
      //   43: aload_1
      //   44: invokeinterface getValue : ()Ljava/lang/Object;
      //   49: ifnonnull -> 54
      //   52: iconst_1
      //   53: istore_3
      //   54: iload_3
      //   55: ireturn
      //   56: iload_2
      //   57: istore_3
      //   58: aload_0
      //   59: getfield key : Ljava/lang/Object;
      //   62: aload_1
      //   63: invokeinterface getKey : ()Ljava/lang/Object;
      //   68: invokevirtual equals : (Ljava/lang/Object;)Z
      //   71: ifeq -> 54
      //   74: goto -> 34
      //   77: iload_2
      //   78: istore_3
      //   79: aload_0
      //   80: getfield value : Ljava/lang/Object;
      //   83: aload_1
      //   84: invokeinterface getValue : ()Ljava/lang/Object;
      //   89: invokevirtual equals : (Ljava/lang/Object;)Z
      //   92: ifeq -> 54
      //   95: goto -> 52
    }
    
    public Node<K, V> first() {
      Node<K, V> node1 = this;
      for (Node<K, V> node2 = node1.left; node2 != null; node2 = node1.left)
        node1 = node2; 
      return node1;
    }
    
    public K getKey() {
      return this.key;
    }
    
    public V getValue() {
      return this.value;
    }
    
    public int hashCode() {
      int j;
      int i = 0;
      if (this.key == null) {
        j = 0;
      } else {
        j = this.key.hashCode();
      } 
      if (this.value != null)
        i = this.value.hashCode(); 
      return j ^ i;
    }
    
    public Node<K, V> last() {
      Node<K, V> node1 = this;
      for (Node<K, V> node2 = node1.right; node2 != null; node2 = node1.right)
        node1 = node2; 
      return node1;
    }
    
    public V setValue(V param1V) {
      V v = this.value;
      this.value = param1V;
      return v;
    }
    
    public String toString() {
      return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/google/gson/internal/LinkedTreeMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */