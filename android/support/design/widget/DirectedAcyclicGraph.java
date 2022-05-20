package android.support.design.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pools;
import android.support.v4.util.SimpleArrayMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

final class DirectedAcyclicGraph<T> {
  private final SimpleArrayMap<T, ArrayList<T>> mGraph = new SimpleArrayMap();
  
  private final Pools.Pool<ArrayList<T>> mListPool = (Pools.Pool<ArrayList<T>>)new Pools.SimplePool(10);
  
  private final ArrayList<T> mSortResult = new ArrayList<T>();
  
  private final HashSet<T> mSortTmpMarked = new HashSet<T>();
  
  private void dfs(T paramT, ArrayList<T> paramArrayList, HashSet<T> paramHashSet) {
    if (!paramArrayList.contains(paramT)) {
      if (paramHashSet.contains(paramT))
        throw new RuntimeException("This graph contains cyclic dependencies"); 
      paramHashSet.add(paramT);
      ArrayList<T> arrayList = (ArrayList)this.mGraph.get(paramT);
      if (arrayList != null) {
        byte b = 0;
        int i = arrayList.size();
        while (b < i) {
          dfs(arrayList.get(b), paramArrayList, paramHashSet);
          b++;
        } 
      } 
      paramHashSet.remove(paramT);
      paramArrayList.add(paramT);
    } 
  }
  
  @NonNull
  private ArrayList<T> getEmptyList() {
    ArrayList<T> arrayList1 = (ArrayList)this.mListPool.acquire();
    ArrayList<T> arrayList2 = arrayList1;
    if (arrayList1 == null)
      arrayList2 = new ArrayList(); 
    return arrayList2;
  }
  
  private void poolList(@NonNull ArrayList<T> paramArrayList) {
    paramArrayList.clear();
    this.mListPool.release(paramArrayList);
  }
  
  void addEdge(@NonNull T paramT1, @NonNull T paramT2) {
    if (!this.mGraph.containsKey(paramT1) || !this.mGraph.containsKey(paramT2))
      throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge"); 
    ArrayList<T> arrayList1 = (ArrayList)this.mGraph.get(paramT1);
    ArrayList<T> arrayList2 = arrayList1;
    if (arrayList1 == null) {
      arrayList2 = getEmptyList();
      this.mGraph.put(paramT1, arrayList2);
    } 
    arrayList2.add(paramT2);
  }
  
  void addNode(@NonNull T paramT) {
    if (!this.mGraph.containsKey(paramT))
      this.mGraph.put(paramT, null); 
  }
  
  void clear() {
    byte b = 0;
    int i = this.mGraph.size();
    while (b < i) {
      ArrayList<T> arrayList = (ArrayList)this.mGraph.valueAt(b);
      if (arrayList != null)
        poolList(arrayList); 
      b++;
    } 
    this.mGraph.clear();
  }
  
  boolean contains(@NonNull T paramT) {
    return this.mGraph.containsKey(paramT);
  }
  
  @Nullable
  List getIncomingEdges(@NonNull T paramT) {
    return (List)this.mGraph.get(paramT);
  }
  
  @Nullable
  List getOutgoingEdges(@NonNull T paramT) {
    ArrayList<Object> arrayList = null;
    byte b = 0;
    int i = this.mGraph.size();
    while (b < i) {
      ArrayList arrayList1 = (ArrayList)this.mGraph.valueAt(b);
      ArrayList<Object> arrayList2 = arrayList;
      if (arrayList1 != null) {
        arrayList2 = arrayList;
        if (arrayList1.contains(paramT)) {
          arrayList2 = arrayList;
          if (arrayList == null)
            arrayList2 = new ArrayList(); 
          arrayList2.add(this.mGraph.keyAt(b));
        } 
      } 
      b++;
      arrayList = arrayList2;
    } 
    return arrayList;
  }
  
  @NonNull
  ArrayList<T> getSortedList() {
    this.mSortResult.clear();
    this.mSortTmpMarked.clear();
    byte b = 0;
    int i = this.mGraph.size();
    while (b < i) {
      dfs((T)this.mGraph.keyAt(b), this.mSortResult, this.mSortTmpMarked);
      b++;
    } 
    return this.mSortResult;
  }
  
  boolean hasOutgoingEdges(@NonNull T paramT) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: getfield mGraph : Landroid/support/v4/util/SimpleArrayMap;
    //   6: invokevirtual size : ()I
    //   9: istore_3
    //   10: iload_2
    //   11: iload_3
    //   12: if_icmpge -> 54
    //   15: aload_0
    //   16: getfield mGraph : Landroid/support/v4/util/SimpleArrayMap;
    //   19: iload_2
    //   20: invokevirtual valueAt : (I)Ljava/lang/Object;
    //   23: checkcast java/util/ArrayList
    //   26: astore #4
    //   28: aload #4
    //   30: ifnull -> 48
    //   33: aload #4
    //   35: aload_1
    //   36: invokevirtual contains : (Ljava/lang/Object;)Z
    //   39: ifeq -> 48
    //   42: iconst_1
    //   43: istore #5
    //   45: iload #5
    //   47: ireturn
    //   48: iinc #2, 1
    //   51: goto -> 10
    //   54: iconst_0
    //   55: istore #5
    //   57: goto -> 45
  }
  
  int size() {
    return this.mGraph.size();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/DirectedAcyclicGraph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */