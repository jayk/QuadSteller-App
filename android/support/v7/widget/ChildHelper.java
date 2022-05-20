package android.support.v7.widget;

import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

class ChildHelper {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "ChildrenHelper";
  
  final Bucket mBucket;
  
  final Callback mCallback;
  
  final List<View> mHiddenViews;
  
  ChildHelper(Callback paramCallback) {
    this.mCallback = paramCallback;
    this.mBucket = new Bucket();
    this.mHiddenViews = new ArrayList<View>();
  }
  
  private int getOffset(int paramInt) {
    if (paramInt < 0)
      return -1; 
    int i = this.mCallback.getChildCount();
    for (int j = paramInt; j < i; j += k) {
      int k = paramInt - j - this.mBucket.countOnesBefore(j);
      if (k == 0)
        while (true) {
          paramInt = j;
          if (this.mBucket.get(j)) {
            j++;
            continue;
          } 
          return paramInt;
        }  
    } 
    return -1;
  }
  
  private void hideViewInternal(View paramView) {
    this.mHiddenViews.add(paramView);
    this.mCallback.onEnteredHiddenState(paramView);
  }
  
  private boolean unhideViewInternal(View paramView) {
    if (this.mHiddenViews.remove(paramView)) {
      this.mCallback.onLeftHiddenState(paramView);
      return true;
    } 
    return false;
  }
  
  void addView(View paramView, int paramInt, boolean paramBoolean) {
    if (paramInt < 0) {
      paramInt = this.mCallback.getChildCount();
    } else {
      paramInt = getOffset(paramInt);
    } 
    this.mBucket.insert(paramInt, paramBoolean);
    if (paramBoolean)
      hideViewInternal(paramView); 
    this.mCallback.addView(paramView, paramInt);
  }
  
  void addView(View paramView, boolean paramBoolean) {
    addView(paramView, -1, paramBoolean);
  }
  
  void attachViewToParent(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams, boolean paramBoolean) {
    if (paramInt < 0) {
      paramInt = this.mCallback.getChildCount();
    } else {
      paramInt = getOffset(paramInt);
    } 
    this.mBucket.insert(paramInt, paramBoolean);
    if (paramBoolean)
      hideViewInternal(paramView); 
    this.mCallback.attachViewToParent(paramView, paramInt, paramLayoutParams);
  }
  
  void detachViewFromParent(int paramInt) {
    paramInt = getOffset(paramInt);
    this.mBucket.remove(paramInt);
    this.mCallback.detachViewFromParent(paramInt);
  }
  
  View findHiddenNonRemovedView(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mHiddenViews : Ljava/util/List;
    //   4: invokeinterface size : ()I
    //   9: istore_2
    //   10: iconst_0
    //   11: istore_3
    //   12: iload_3
    //   13: iload_2
    //   14: if_icmpge -> 79
    //   17: aload_0
    //   18: getfield mHiddenViews : Ljava/util/List;
    //   21: iload_3
    //   22: invokeinterface get : (I)Ljava/lang/Object;
    //   27: checkcast android/view/View
    //   30: astore #4
    //   32: aload_0
    //   33: getfield mCallback : Landroid/support/v7/widget/ChildHelper$Callback;
    //   36: aload #4
    //   38: invokeinterface getChildViewHolder : (Landroid/view/View;)Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   43: astore #5
    //   45: aload #5
    //   47: invokevirtual getLayoutPosition : ()I
    //   50: iload_1
    //   51: if_icmpne -> 73
    //   54: aload #5
    //   56: invokevirtual isInvalid : ()Z
    //   59: ifne -> 73
    //   62: aload #5
    //   64: invokevirtual isRemoved : ()Z
    //   67: ifne -> 73
    //   70: aload #4
    //   72: areturn
    //   73: iinc #3, 1
    //   76: goto -> 12
    //   79: aconst_null
    //   80: astore #4
    //   82: goto -> 70
  }
  
  View getChildAt(int paramInt) {
    paramInt = getOffset(paramInt);
    return this.mCallback.getChildAt(paramInt);
  }
  
  int getChildCount() {
    return this.mCallback.getChildCount() - this.mHiddenViews.size();
  }
  
  View getUnfilteredChildAt(int paramInt) {
    return this.mCallback.getChildAt(paramInt);
  }
  
  int getUnfilteredChildCount() {
    return this.mCallback.getChildCount();
  }
  
  void hide(View paramView) {
    int i = this.mCallback.indexOfChild(paramView);
    if (i < 0)
      throw new IllegalArgumentException("view is not a child, cannot hide " + paramView); 
    this.mBucket.set(i);
    hideViewInternal(paramView);
  }
  
  int indexOfChild(View paramView) {
    int i = -1;
    int j = this.mCallback.indexOfChild(paramView);
    if (j != -1 && !this.mBucket.get(j))
      i = j - this.mBucket.countOnesBefore(j); 
    return i;
  }
  
  boolean isHidden(View paramView) {
    return this.mHiddenViews.contains(paramView);
  }
  
  void removeAllViewsUnfiltered() {
    this.mBucket.reset();
    for (int i = this.mHiddenViews.size() - 1; i >= 0; i--) {
      this.mCallback.onLeftHiddenState(this.mHiddenViews.get(i));
      this.mHiddenViews.remove(i);
    } 
    this.mCallback.removeAllViews();
  }
  
  void removeView(View paramView) {
    int i = this.mCallback.indexOfChild(paramView);
    if (i >= 0) {
      if (this.mBucket.remove(i))
        unhideViewInternal(paramView); 
      this.mCallback.removeViewAt(i);
    } 
  }
  
  void removeViewAt(int paramInt) {
    paramInt = getOffset(paramInt);
    View view = this.mCallback.getChildAt(paramInt);
    if (view != null) {
      if (this.mBucket.remove(paramInt))
        unhideViewInternal(view); 
      this.mCallback.removeViewAt(paramInt);
    } 
  }
  
  boolean removeViewIfHidden(View paramView) {
    null = true;
    int i = this.mCallback.indexOfChild(paramView);
    if (i == -1) {
      if (unhideViewInternal(paramView));
      return null;
    } 
    if (this.mBucket.get(i)) {
      this.mBucket.remove(i);
      if (!unhideViewInternal(paramView));
      this.mCallback.removeViewAt(i);
      return null;
    } 
    return false;
  }
  
  public String toString() {
    return this.mBucket.toString() + ", hidden list:" + this.mHiddenViews.size();
  }
  
  void unhide(View paramView) {
    int i = this.mCallback.indexOfChild(paramView);
    if (i < 0)
      throw new IllegalArgumentException("view is not a child, cannot hide " + paramView); 
    if (!this.mBucket.get(i))
      throw new RuntimeException("trying to unhide a view that was not hidden" + paramView); 
    this.mBucket.clear(i);
    unhideViewInternal(paramView);
  }
  
  static class Bucket {
    static final int BITS_PER_WORD = 64;
    
    static final long LAST_BIT = -9223372036854775808L;
    
    long mData = 0L;
    
    Bucket next;
    
    private void ensureNext() {
      if (this.next == null)
        this.next = new Bucket(); 
    }
    
    void clear(int param1Int) {
      if (param1Int >= 64) {
        if (this.next != null)
          this.next.clear(param1Int - 64); 
        return;
      } 
      this.mData &= 1L << param1Int ^ 0xFFFFFFFFFFFFFFFFL;
    }
    
    int countOnesBefore(int param1Int) {
      return (this.next == null) ? ((param1Int >= 64) ? Long.bitCount(this.mData) : Long.bitCount(this.mData & (1L << param1Int) - 1L)) : ((param1Int < 64) ? Long.bitCount(this.mData & (1L << param1Int) - 1L) : (this.next.countOnesBefore(param1Int - 64) + Long.bitCount(this.mData)));
    }
    
    boolean get(int param1Int) {
      if (param1Int >= 64) {
        ensureNext();
        return this.next.get(param1Int - 64);
      } 
      return ((this.mData & 1L << param1Int) != 0L);
    }
    
    void insert(int param1Int, boolean param1Boolean) {
      boolean bool;
      if (param1Int >= 64) {
        ensureNext();
        this.next.insert(param1Int - 64, param1Boolean);
        return;
      } 
      if ((this.mData & Long.MIN_VALUE) != 0L) {
        bool = true;
      } else {
        bool = false;
      } 
      long l = (1L << param1Int) - 1L;
      this.mData = this.mData & l | (this.mData & (0xFFFFFFFFFFFFFFFFL ^ l)) << 1L;
      if (param1Boolean) {
        set(param1Int);
      } else {
        clear(param1Int);
      } 
      if (bool || this.next != null) {
        ensureNext();
        this.next.insert(0, bool);
      } 
    }
    
    boolean remove(int param1Int) {
      boolean bool2;
      if (param1Int >= 64) {
        ensureNext();
        return this.next.remove(param1Int - 64);
      } 
      long l = 1L << param1Int;
      if ((this.mData & l) != 0L) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.mData &= 0xFFFFFFFFFFFFFFFFL ^ l;
      l--;
      this.mData = this.mData & l | Long.rotateRight(this.mData & (0xFFFFFFFFFFFFFFFFL ^ l), 1);
      boolean bool1 = bool2;
      if (this.next != null) {
        if (this.next.get(0))
          set(63); 
        this.next.remove(0);
        bool1 = bool2;
      } 
      return bool1;
    }
    
    void reset() {
      this.mData = 0L;
      if (this.next != null)
        this.next.reset(); 
    }
    
    void set(int param1Int) {
      if (param1Int >= 64) {
        ensureNext();
        this.next.set(param1Int - 64);
        return;
      } 
      this.mData |= 1L << param1Int;
    }
    
    public String toString() {
      return (this.next == null) ? Long.toBinaryString(this.mData) : (this.next.toString() + "xx" + Long.toBinaryString(this.mData));
    }
  }
  
  static interface Callback {
    void addView(View param1View, int param1Int);
    
    void attachViewToParent(View param1View, int param1Int, ViewGroup.LayoutParams param1LayoutParams);
    
    void detachViewFromParent(int param1Int);
    
    View getChildAt(int param1Int);
    
    int getChildCount();
    
    RecyclerView.ViewHolder getChildViewHolder(View param1View);
    
    int indexOfChild(View param1View);
    
    void onEnteredHiddenState(View param1View);
    
    void onLeftHiddenState(View param1View);
    
    void removeAllViews();
    
    void removeViewAt(int param1Int);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/ChildHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */