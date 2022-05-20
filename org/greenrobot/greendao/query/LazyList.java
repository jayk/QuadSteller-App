package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.InternalQueryDaoAccess;

public class LazyList<E> implements List<E>, Closeable {
  private final Cursor cursor;
  
  private final InternalQueryDaoAccess<E> daoAccess;
  
  private final List<E> entities;
  
  private volatile int loadedCount;
  
  private final ReentrantLock lock;
  
  private final int size;
  
  LazyList(InternalQueryDaoAccess<E> paramInternalQueryDaoAccess, Cursor paramCursor, boolean paramBoolean) {
    this.cursor = paramCursor;
    this.daoAccess = paramInternalQueryDaoAccess;
    this.size = paramCursor.getCount();
    if (paramBoolean) {
      this.entities = new ArrayList<E>(this.size);
      for (byte b = 0; b < this.size; b++)
        this.entities.add(null); 
    } else {
      this.entities = null;
    } 
    if (this.size == 0)
      paramCursor.close(); 
    this.lock = new ReentrantLock();
  }
  
  public void add(int paramInt, E paramE) {
    throw new UnsupportedOperationException();
  }
  
  public boolean add(E paramE) {
    throw new UnsupportedOperationException();
  }
  
  public boolean addAll(int paramInt, Collection<? extends E> paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  public boolean addAll(Collection<? extends E> paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  protected void checkCached() {
    if (this.entities == null)
      throw new DaoException("This operation only works with cached lazy lists"); 
  }
  
  public void clear() {
    throw new UnsupportedOperationException();
  }
  
  public void close() {
    this.cursor.close();
  }
  
  public boolean contains(Object paramObject) {
    loadRemaining();
    return this.entities.contains(paramObject);
  }
  
  public boolean containsAll(Collection<?> paramCollection) {
    loadRemaining();
    return this.entities.containsAll(paramCollection);
  }
  
  public E get(int paramInt) {
    if (this.entities != null) {
      E e1 = this.entities.get(paramInt);
      E e2 = e1;
      if (e1 == null) {
        this.lock.lock();
        try {
          E e3;
          e1 = this.entities.get(paramInt);
          E e4 = e1;
          if (e1 == null) {
            e1 = loadEntity(paramInt);
            this.entities.set(paramInt, e1);
            this.loadedCount++;
            E e = e1;
            if (this.loadedCount == this.size) {
              this.cursor.close();
              e3 = e1;
            } 
          } 
          return e3;
        } finally {
          this.lock.unlock();
        } 
      } 
      return e2;
    } 
    this.lock.lock();
    try {
      E e = loadEntity(paramInt);
    } finally {
      this.lock.unlock();
    } 
    return (E)SYNTHETIC_LOCAL_VARIABLE_3;
  }
  
  public int getLoadedCount() {
    return this.loadedCount;
  }
  
  public int indexOf(Object paramObject) {
    loadRemaining();
    return this.entities.indexOf(paramObject);
  }
  
  public boolean isClosed() {
    return this.cursor.isClosed();
  }
  
  public boolean isEmpty() {
    return (this.size == 0);
  }
  
  public boolean isLoadedCompletely() {
    return (this.loadedCount == this.size);
  }
  
  public Iterator<E> iterator() {
    return new LazyIterator(0, false);
  }
  
  public int lastIndexOf(Object paramObject) {
    loadRemaining();
    return this.entities.lastIndexOf(paramObject);
  }
  
  public ListIterator<E> listIterator(int paramInt) {
    return new LazyIterator(paramInt, false);
  }
  
  public CloseableListIterator<E> listIterator() {
    return new LazyIterator(0, false);
  }
  
  public CloseableListIterator<E> listIteratorAutoClose() {
    return new LazyIterator(0, true);
  }
  
  protected E loadEntity(int paramInt) {
    if (!this.cursor.moveToPosition(paramInt))
      throw new DaoException("Could not move to cursor location " + paramInt); 
    Object object = this.daoAccess.loadCurrent(this.cursor, 0, true);
    if (object == null)
      throw new DaoException("Loading of entity failed (null) at position " + paramInt); 
    return (E)object;
  }
  
  public void loadRemaining() {
    checkCached();
    int i = this.entities.size();
    for (byte b = 0; b < i; b++)
      get(b); 
  }
  
  public E peak(int paramInt) {
    return (this.entities != null) ? this.entities.get(paramInt) : null;
  }
  
  public E remove(int paramInt) {
    throw new UnsupportedOperationException();
  }
  
  public boolean remove(Object paramObject) {
    throw new UnsupportedOperationException();
  }
  
  public boolean removeAll(Collection<?> paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  public boolean retainAll(Collection<?> paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  public E set(int paramInt, E paramE) {
    throw new UnsupportedOperationException();
  }
  
  public int size() {
    return this.size;
  }
  
  public List<E> subList(int paramInt1, int paramInt2) {
    checkCached();
    for (int i = paramInt1; i < paramInt2; i++)
      get(i); 
    return this.entities.subList(paramInt1, paramInt2);
  }
  
  public Object[] toArray() {
    loadRemaining();
    return this.entities.toArray();
  }
  
  public <T> T[] toArray(T[] paramArrayOfT) {
    loadRemaining();
    return this.entities.toArray(paramArrayOfT);
  }
  
  protected class LazyIterator implements CloseableListIterator<E> {
    private final boolean closeWhenDone;
    
    private int index;
    
    public LazyIterator(int param1Int, boolean param1Boolean) {
      this.index = param1Int;
      this.closeWhenDone = param1Boolean;
    }
    
    public void add(E param1E) {
      throw new UnsupportedOperationException();
    }
    
    public void close() {
      LazyList.this.close();
    }
    
    public boolean hasNext() {
      return (this.index < LazyList.this.size);
    }
    
    public boolean hasPrevious() {
      return (this.index > 0);
    }
    
    public E next() {
      if (this.index >= LazyList.this.size)
        throw new NoSuchElementException(); 
      E e = (E)LazyList.this.get(this.index);
      this.index++;
      if (this.index == LazyList.this.size && this.closeWhenDone)
        close(); 
      return e;
    }
    
    public int nextIndex() {
      return this.index;
    }
    
    public E previous() {
      if (this.index <= 0)
        throw new NoSuchElementException(); 
      this.index--;
      return LazyList.this.get(this.index);
    }
    
    public int previousIndex() {
      return this.index - 1;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
    
    public void set(E param1E) {
      throw new UnsupportedOperationException();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/greenrobot/greendao/query/LazyList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */