package android.support.v4.widget;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class FocusStrategy {
  private static boolean beamBeats(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2, @NonNull Rect paramRect3) {
    boolean bool = true;
    boolean bool1 = beamsOverlap(paramInt, paramRect1, paramRect2);
    if (beamsOverlap(paramInt, paramRect1, paramRect3) || !bool1)
      return false; 
    bool1 = bool;
    if (isToDirectionOf(paramInt, paramRect1, paramRect3)) {
      bool1 = bool;
      if (paramInt != 17) {
        bool1 = bool;
        if (paramInt != 66) {
          bool1 = bool;
          if (majorAxisDistance(paramInt, paramRect1, paramRect2) >= majorAxisDistanceToFarEdge(paramInt, paramRect1, paramRect3))
            bool1 = false; 
        } 
      } 
    } 
    return bool1;
  }
  
  private static boolean beamsOverlap(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) {
    boolean bool = true;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 17:
      case 66:
        if (paramRect2.bottom < paramRect1.top || paramRect2.top > paramRect1.bottom)
          bool = false; 
        return bool;
      case 33:
      case 130:
        break;
    } 
    if (paramRect2.right < paramRect1.left || paramRect2.left > paramRect1.right)
      bool = false; 
    return bool;
  }
  
  public static <L, T> T findNextFocusInAbsoluteDirection(@NonNull L paramL, @NonNull CollectionAdapter<L, T> paramCollectionAdapter, @NonNull BoundsAdapter<T> paramBoundsAdapter, @Nullable T paramT, @NonNull Rect paramRect, int paramInt) {
    // Byte code:
    //   0: new android/graphics/Rect
    //   3: dup
    //   4: aload #4
    //   6: invokespecial <init> : (Landroid/graphics/Rect;)V
    //   9: astore #6
    //   11: iload #5
    //   13: lookupswitch default -> 56, 17 -> 66, 33 -> 150, 66 -> 133, 130 -> 166
    //   56: new java/lang/IllegalArgumentException
    //   59: dup
    //   60: ldc 'direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.'
    //   62: invokespecial <init> : (Ljava/lang/String;)V
    //   65: athrow
    //   66: aload #6
    //   68: aload #4
    //   70: invokevirtual width : ()I
    //   73: iconst_1
    //   74: iadd
    //   75: iconst_0
    //   76: invokevirtual offset : (II)V
    //   79: aconst_null
    //   80: astore #7
    //   82: aload_1
    //   83: aload_0
    //   84: invokeinterface size : (Ljava/lang/Object;)I
    //   89: istore #8
    //   91: new android/graphics/Rect
    //   94: dup
    //   95: invokespecial <init> : ()V
    //   98: astore #9
    //   100: iconst_0
    //   101: istore #10
    //   103: iload #10
    //   105: iload #8
    //   107: if_icmpge -> 221
    //   110: aload_1
    //   111: aload_0
    //   112: iload #10
    //   114: invokeinterface get : (Ljava/lang/Object;I)Ljava/lang/Object;
    //   119: astore #11
    //   121: aload #11
    //   123: aload_3
    //   124: if_acmpne -> 183
    //   127: iinc #10, 1
    //   130: goto -> 103
    //   133: aload #6
    //   135: aload #4
    //   137: invokevirtual width : ()I
    //   140: iconst_1
    //   141: iadd
    //   142: ineg
    //   143: iconst_0
    //   144: invokevirtual offset : (II)V
    //   147: goto -> 79
    //   150: aload #6
    //   152: iconst_0
    //   153: aload #4
    //   155: invokevirtual height : ()I
    //   158: iconst_1
    //   159: iadd
    //   160: invokevirtual offset : (II)V
    //   163: goto -> 79
    //   166: aload #6
    //   168: iconst_0
    //   169: aload #4
    //   171: invokevirtual height : ()I
    //   174: iconst_1
    //   175: iadd
    //   176: ineg
    //   177: invokevirtual offset : (II)V
    //   180: goto -> 79
    //   183: aload_2
    //   184: aload #11
    //   186: aload #9
    //   188: invokeinterface obtainBounds : (Ljava/lang/Object;Landroid/graphics/Rect;)V
    //   193: iload #5
    //   195: aload #4
    //   197: aload #9
    //   199: aload #6
    //   201: invokestatic isBetterCandidate : (ILandroid/graphics/Rect;Landroid/graphics/Rect;Landroid/graphics/Rect;)Z
    //   204: ifeq -> 127
    //   207: aload #6
    //   209: aload #9
    //   211: invokevirtual set : (Landroid/graphics/Rect;)V
    //   214: aload #11
    //   216: astore #7
    //   218: goto -> 127
    //   221: aload #7
    //   223: areturn
  }
  
  public static <L, T> T findNextFocusInRelativeDirection(@NonNull L paramL, @NonNull CollectionAdapter<L, T> paramCollectionAdapter, @NonNull BoundsAdapter<T> paramBoundsAdapter, @Nullable T paramT, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    int i = paramCollectionAdapter.size(paramL);
    ArrayList<?> arrayList = new ArrayList(i);
    for (byte b = 0; b < i; b++)
      arrayList.add(paramCollectionAdapter.get(paramL, b)); 
    Collections.sort(arrayList, new SequentialComparator(paramBoolean1, paramBoundsAdapter));
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
      case 2:
        return (T)getNextFocusable((L)paramT, (ArrayList)arrayList, paramBoolean2);
      case 1:
        break;
    } 
    return (T)getPreviousFocusable((L)paramT, (ArrayList)arrayList, paramBoolean2);
  }
  
  private static <T> T getNextFocusable(T paramT, ArrayList<T> paramArrayList, boolean paramBoolean) {
    int j;
    int i = paramArrayList.size();
    if (paramT == null) {
      j = -1;
    } else {
      j = paramArrayList.lastIndexOf(paramT);
    } 
    return (++j < i) ? paramArrayList.get(j) : ((paramBoolean && i > 0) ? paramArrayList.get(0) : null);
  }
  
  private static <T> T getPreviousFocusable(T paramT, ArrayList<T> paramArrayList, boolean paramBoolean) {
    int j;
    int i = paramArrayList.size();
    if (paramT == null) {
      j = i;
    } else {
      j = paramArrayList.indexOf(paramT);
    } 
    return (--j >= 0) ? paramArrayList.get(j) : ((paramBoolean && i > 0) ? paramArrayList.get(i - 1) : null);
  }
  
  private static int getWeightedDistanceFor(int paramInt1, int paramInt2) {
    return paramInt1 * 13 * paramInt1 + paramInt2 * paramInt2;
  }
  
  private static boolean isBetterCandidate(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2, @NonNull Rect paramRect3) {
    boolean bool1 = true;
    if (!isCandidate(paramRect1, paramRect2, paramInt))
      return false; 
    boolean bool2 = bool1;
    if (isCandidate(paramRect1, paramRect3, paramInt)) {
      bool2 = bool1;
      if (!beamBeats(paramInt, paramRect1, paramRect2, paramRect3)) {
        if (beamBeats(paramInt, paramRect1, paramRect3, paramRect2))
          return false; 
        bool2 = bool1;
        if (getWeightedDistanceFor(majorAxisDistance(paramInt, paramRect1, paramRect2), minorAxisDistance(paramInt, paramRect1, paramRect2)) >= getWeightedDistanceFor(majorAxisDistance(paramInt, paramRect1, paramRect3), minorAxisDistance(paramInt, paramRect1, paramRect3)))
          bool2 = false; 
      } 
    } 
    return bool2;
  }
  
  private static boolean isCandidate(@NonNull Rect paramRect1, @NonNull Rect paramRect2, int paramInt) {
    boolean bool = true;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 17:
        if ((paramRect1.right <= paramRect2.right && paramRect1.left < paramRect2.right) || paramRect1.left <= paramRect2.left)
          bool = false; 
        return bool;
      case 66:
        if ((paramRect1.left >= paramRect2.left && paramRect1.right > paramRect2.left) || paramRect1.right >= paramRect2.right)
          bool = false; 
        return bool;
      case 33:
        if ((paramRect1.bottom <= paramRect2.bottom && paramRect1.top < paramRect2.bottom) || paramRect1.top <= paramRect2.top)
          bool = false; 
        return bool;
      case 130:
        break;
    } 
    if ((paramRect1.top >= paramRect2.top && paramRect1.bottom > paramRect2.top) || paramRect1.bottom >= paramRect2.bottom)
      bool = false; 
    return bool;
  }
  
  private static boolean isToDirectionOf(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) {
    boolean bool = true;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 17:
        if (paramRect1.left < paramRect2.right)
          bool = false; 
        return bool;
      case 66:
        if (paramRect1.right > paramRect2.left)
          bool = false; 
        return bool;
      case 33:
        if (paramRect1.top < paramRect2.bottom)
          bool = false; 
        return bool;
      case 130:
        break;
    } 
    if (paramRect1.bottom > paramRect2.top)
      bool = false; 
    return bool;
  }
  
  private static int majorAxisDistance(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) {
    return Math.max(0, majorAxisDistanceRaw(paramInt, paramRect1, paramRect2));
  }
  
  private static int majorAxisDistanceRaw(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) {
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 17:
        return paramRect1.left - paramRect2.right;
      case 66:
        return paramRect2.left - paramRect1.right;
      case 33:
        return paramRect1.top - paramRect2.bottom;
      case 130:
        break;
    } 
    return paramRect2.top - paramRect1.bottom;
  }
  
  private static int majorAxisDistanceToFarEdge(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) {
    return Math.max(1, majorAxisDistanceToFarEdgeRaw(paramInt, paramRect1, paramRect2));
  }
  
  private static int majorAxisDistanceToFarEdgeRaw(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) {
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 17:
        return paramRect1.left - paramRect2.left;
      case 66:
        return paramRect2.right - paramRect1.right;
      case 33:
        return paramRect1.top - paramRect2.top;
      case 130:
        break;
    } 
    return paramRect2.bottom - paramRect1.bottom;
  }
  
  private static int minorAxisDistance(int paramInt, @NonNull Rect paramRect1, @NonNull Rect paramRect2) {
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      case 17:
      case 66:
        return Math.abs(paramRect1.top + paramRect1.height() / 2 - paramRect2.top + paramRect2.height() / 2);
      case 33:
      case 130:
        break;
    } 
    return Math.abs(paramRect1.left + paramRect1.width() / 2 - paramRect2.left + paramRect2.width() / 2);
  }
  
  public static interface BoundsAdapter<T> {
    void obtainBounds(T param1T, Rect param1Rect);
  }
  
  public static interface CollectionAdapter<T, V> {
    V get(T param1T, int param1Int);
    
    int size(T param1T);
  }
  
  private static class SequentialComparator<T> implements Comparator<T> {
    private final FocusStrategy.BoundsAdapter<T> mAdapter;
    
    private final boolean mIsLayoutRtl;
    
    private final Rect mTemp1 = new Rect();
    
    private final Rect mTemp2 = new Rect();
    
    SequentialComparator(boolean param1Boolean, FocusStrategy.BoundsAdapter<T> param1BoundsAdapter) {
      this.mIsLayoutRtl = param1Boolean;
      this.mAdapter = param1BoundsAdapter;
    }
    
    public int compare(T param1T1, T param1T2) {
      boolean bool = true;
      byte b1 = 1;
      byte b2 = -1;
      Rect rect1 = this.mTemp1;
      Rect rect2 = this.mTemp2;
      this.mAdapter.obtainBounds(param1T1, rect1);
      this.mAdapter.obtainBounds(param1T2, rect2);
      if (rect1.top < rect2.top)
        return b2; 
      if (rect1.top > rect2.top)
        return 1; 
      if (rect1.left < rect2.left) {
        if (!this.mIsLayoutRtl)
          b1 = -1; 
        return b1;
      } 
      if (rect1.left > rect2.left) {
        b1 = b2;
        if (!this.mIsLayoutRtl)
          b1 = 1; 
        return b1;
      } 
      b1 = b2;
      if (rect1.bottom >= rect2.bottom) {
        if (rect1.bottom > rect2.bottom)
          return 1; 
        if (rect1.right < rect2.right)
          return this.mIsLayoutRtl ? bool : -1; 
        if (rect1.right > rect2.right) {
          b1 = b2;
          if (!this.mIsLayoutRtl)
            b1 = 1; 
          return b1;
        } 
        b1 = 0;
      } 
      return b1;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/widget/FocusStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */