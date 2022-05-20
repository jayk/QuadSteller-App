package org.greenrobot.greendao.query;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.greenrobot.greendao.AbstractDao;

abstract class AbstractQueryData<T, Q extends AbstractQuery<T>> {
  final AbstractDao<T, ?> dao;
  
  final String[] initialValues;
  
  final Map<Long, WeakReference<Q>> queriesForThreads;
  
  final String sql;
  
  AbstractQueryData(AbstractDao<T, ?> paramAbstractDao, String paramString, String[] paramArrayOfString) {
    this.dao = paramAbstractDao;
    this.sql = paramString;
    this.initialValues = paramArrayOfString;
    this.queriesForThreads = new HashMap<Long, WeakReference<Q>>();
  }
  
  protected abstract Q createQuery();
  
  Q forCurrentThread() {
    long l = Thread.currentThread().getId();
    synchronized (this.queriesForThreads) {
      WeakReference<AbstractQuery> weakReference = this.queriesForThreads.get(Long.valueOf(l));
      if (weakReference != null) {
        AbstractQuery abstractQuery = weakReference.get();
      } else {
        weakReference = null;
      } 
      if (weakReference == null) {
        gc();
        weakReference = (WeakReference<AbstractQuery>)createQuery();
        Map<Long, WeakReference<Q>> map = this.queriesForThreads;
        WeakReference<Q> weakReference1 = new WeakReference();
        this((T)weakReference);
        map.put(Long.valueOf(l), weakReference1);
        return (Q)weakReference;
      } 
      System.arraycopy(this.initialValues, 0, ((AbstractQuery)weakReference).parameters, 0, this.initialValues.length);
      return (Q)weakReference;
    } 
  }
  
  Q forCurrentThread(Q paramQ) {
    if (Thread.currentThread() == ((AbstractQuery)paramQ).ownerThread) {
      System.arraycopy(this.initialValues, 0, ((AbstractQuery)paramQ).parameters, 0, this.initialValues.length);
      return paramQ;
    } 
    return forCurrentThread();
  }
  
  void gc() {
    synchronized (this.queriesForThreads) {
      Iterator<Map.Entry> iterator = this.queriesForThreads.entrySet().iterator();
      while (iterator.hasNext()) {
        if (((WeakReference)((Map.Entry)iterator.next()).getValue()).get() == null)
          iterator.remove(); 
      } 
    } 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_1} */
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/AbstractQueryData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */