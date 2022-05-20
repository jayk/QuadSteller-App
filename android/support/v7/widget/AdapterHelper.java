package android.support.v7.widget;

import android.support.v4.util.Pools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AdapterHelper implements OpReorderer.Callback {
  private static final boolean DEBUG = false;
  
  static final int POSITION_TYPE_INVISIBLE = 0;
  
  static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
  
  private static final String TAG = "AHT";
  
  final Callback mCallback;
  
  final boolean mDisableRecycler;
  
  private int mExistingUpdateTypes = 0;
  
  Runnable mOnItemProcessedCallback;
  
  final OpReorderer mOpReorderer;
  
  final ArrayList<UpdateOp> mPendingUpdates = new ArrayList<UpdateOp>();
  
  final ArrayList<UpdateOp> mPostponedList = new ArrayList<UpdateOp>();
  
  private Pools.Pool<UpdateOp> mUpdateOpPool = (Pools.Pool<UpdateOp>)new Pools.SimplePool(30);
  
  AdapterHelper(Callback paramCallback) {
    this(paramCallback, false);
  }
  
  AdapterHelper(Callback paramCallback, boolean paramBoolean) {
    this.mCallback = paramCallback;
    this.mDisableRecycler = paramBoolean;
    this.mOpReorderer = new OpReorderer(this);
  }
  
  private void applyAdd(UpdateOp paramUpdateOp) {
    postponeAndUpdateViewHolders(paramUpdateOp);
  }
  
  private void applyMove(UpdateOp paramUpdateOp) {
    postponeAndUpdateViewHolders(paramUpdateOp);
  }
  
  private void applyRemove(UpdateOp paramUpdateOp) {
    int i = paramUpdateOp.positionStart;
    int j = 0;
    int k = paramUpdateOp.positionStart + paramUpdateOp.itemCount;
    byte b = -1;
    int m = paramUpdateOp.positionStart;
    while (m < k) {
      int n = 0;
      byte b1 = 0;
      if (this.mCallback.findViewHolder(m) != null || canFindInPreLayout(m)) {
        if (b == 0) {
          dispatchAndUpdateViewHolders(obtainUpdateOp(2, i, j, null));
          b1 = 1;
        } 
        b = 1;
        n = b1;
        b1 = b;
      } else {
        if (b == 1) {
          postponeAndUpdateViewHolders(obtainUpdateOp(2, i, j, null));
          n = 1;
        } 
        b1 = 0;
      } 
      if (n != 0) {
        m -= j;
        k -= j;
        n = 1;
      } else {
        n = j + 1;
      } 
      m++;
      j = n;
      b = b1;
    } 
    UpdateOp updateOp = paramUpdateOp;
    if (j != paramUpdateOp.itemCount) {
      recycleUpdateOp(paramUpdateOp);
      updateOp = obtainUpdateOp(2, i, j, null);
    } 
    if (b == 0) {
      dispatchAndUpdateViewHolders(updateOp);
      return;
    } 
    postponeAndUpdateViewHolders(updateOp);
  }
  
  private void applyUpdate(UpdateOp paramUpdateOp) {
    int i = paramUpdateOp.positionStart;
    int j = 0;
    int k = paramUpdateOp.positionStart;
    int m = paramUpdateOp.itemCount;
    int n = -1;
    int i1 = paramUpdateOp.positionStart;
    while (i1 < k + m) {
      int i2;
      int i3;
      if (this.mCallback.findViewHolder(i1) != null || canFindInPreLayout(i1)) {
        i2 = j;
        int i4 = i;
        if (n == 0) {
          dispatchAndUpdateViewHolders(obtainUpdateOp(4, i, j, paramUpdateOp.payload));
          i2 = 0;
          i4 = i1;
        } 
        i3 = 1;
        i = i4;
      } else {
        i2 = j;
        i3 = i;
        if (n == 1) {
          postponeAndUpdateViewHolders(obtainUpdateOp(4, i, j, paramUpdateOp.payload));
          i2 = 0;
          i3 = i1;
        } 
        j = 0;
        i = i3;
        i3 = j;
      } 
      j = i2 + 1;
      i1++;
      n = i3;
    } 
    Object object = paramUpdateOp;
    if (j != paramUpdateOp.itemCount) {
      object = paramUpdateOp.payload;
      recycleUpdateOp(paramUpdateOp);
      object = obtainUpdateOp(4, i, j, object);
    } 
    if (n == 0) {
      dispatchAndUpdateViewHolders((UpdateOp)object);
      return;
    } 
    postponeAndUpdateViewHolders((UpdateOp)object);
  }
  
  private boolean canFindInPreLayout(int paramInt) {
    boolean bool = true;
    int i = this.mPostponedList.size();
    for (byte b = 0; b < i; b++) {
      UpdateOp updateOp = this.mPostponedList.get(b);
      if (updateOp.cmd == 8) {
        if (findPositionOffset(updateOp.itemCount, b + 1) == paramInt) {
          boolean bool1 = bool;
          continue;
        } 
      } else if (updateOp.cmd == 1) {
        int j = updateOp.positionStart;
        int k = updateOp.itemCount;
        int m = updateOp.positionStart;
        while (m < j + k) {
          boolean bool1 = bool;
          if (findPositionOffset(m, b + 1) != paramInt) {
            m++;
            continue;
          } 
          return bool1;
        } 
      } 
    } 
    return false;
  }
  
  private void dispatchAndUpdateViewHolders(UpdateOp paramUpdateOp) {
    // Byte code:
    //   0: aload_1
    //   1: getfield cmd : I
    //   4: iconst_1
    //   5: if_icmpeq -> 17
    //   8: aload_1
    //   9: getfield cmd : I
    //   12: bipush #8
    //   14: if_icmpne -> 27
    //   17: new java/lang/IllegalArgumentException
    //   20: dup
    //   21: ldc 'should not dispatch add or move for pre layout'
    //   23: invokespecial <init> : (Ljava/lang/String;)V
    //   26: athrow
    //   27: aload_0
    //   28: aload_1
    //   29: getfield positionStart : I
    //   32: aload_1
    //   33: getfield cmd : I
    //   36: invokespecial updatePositionWithPostponed : (II)I
    //   39: istore_2
    //   40: iconst_1
    //   41: istore_3
    //   42: aload_1
    //   43: getfield positionStart : I
    //   46: istore #4
    //   48: aload_1
    //   49: getfield cmd : I
    //   52: tableswitch default -> 80, 2 -> 203, 3 -> 80, 4 -> 107
    //   80: new java/lang/IllegalArgumentException
    //   83: dup
    //   84: new java/lang/StringBuilder
    //   87: dup
    //   88: invokespecial <init> : ()V
    //   91: ldc 'op should be remove or update.'
    //   93: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: aload_1
    //   97: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   100: invokevirtual toString : ()Ljava/lang/String;
    //   103: invokespecial <init> : (Ljava/lang/String;)V
    //   106: athrow
    //   107: iconst_1
    //   108: istore #5
    //   110: iconst_1
    //   111: istore #6
    //   113: iload #6
    //   115: aload_1
    //   116: getfield itemCount : I
    //   119: if_icmpge -> 310
    //   122: aload_0
    //   123: aload_1
    //   124: getfield positionStart : I
    //   127: iload #5
    //   129: iload #6
    //   131: imul
    //   132: iadd
    //   133: aload_1
    //   134: getfield cmd : I
    //   137: invokespecial updatePositionWithPostponed : (II)I
    //   140: istore #7
    //   142: iconst_0
    //   143: istore #8
    //   145: iload #8
    //   147: istore #9
    //   149: aload_1
    //   150: getfield cmd : I
    //   153: tableswitch default -> 180, 2 -> 229, 3 -> 184, 4 -> 209
    //   180: iload #8
    //   182: istore #9
    //   184: iload #9
    //   186: ifeq -> 247
    //   189: iload_3
    //   190: iconst_1
    //   191: iadd
    //   192: istore #9
    //   194: iinc #6, 1
    //   197: iload #9
    //   199: istore_3
    //   200: goto -> 113
    //   203: iconst_0
    //   204: istore #5
    //   206: goto -> 110
    //   209: iload #7
    //   211: iload_2
    //   212: iconst_1
    //   213: iadd
    //   214: if_icmpne -> 223
    //   217: iconst_1
    //   218: istore #9
    //   220: goto -> 184
    //   223: iconst_0
    //   224: istore #9
    //   226: goto -> 220
    //   229: iload #7
    //   231: iload_2
    //   232: if_icmpne -> 241
    //   235: iconst_1
    //   236: istore #9
    //   238: goto -> 184
    //   241: iconst_0
    //   242: istore #9
    //   244: goto -> 238
    //   247: aload_0
    //   248: aload_1
    //   249: getfield cmd : I
    //   252: iload_2
    //   253: iload_3
    //   254: aload_1
    //   255: getfield payload : Ljava/lang/Object;
    //   258: invokevirtual obtainUpdateOp : (IIILjava/lang/Object;)Landroid/support/v7/widget/AdapterHelper$UpdateOp;
    //   261: astore #10
    //   263: aload_0
    //   264: aload #10
    //   266: iload #4
    //   268: invokevirtual dispatchFirstPassAndUpdateViewHolders : (Landroid/support/v7/widget/AdapterHelper$UpdateOp;I)V
    //   271: aload_0
    //   272: aload #10
    //   274: invokevirtual recycleUpdateOp : (Landroid/support/v7/widget/AdapterHelper$UpdateOp;)V
    //   277: iload #4
    //   279: istore #9
    //   281: aload_1
    //   282: getfield cmd : I
    //   285: iconst_4
    //   286: if_icmpne -> 295
    //   289: iload #4
    //   291: iload_3
    //   292: iadd
    //   293: istore #9
    //   295: iload #7
    //   297: istore_2
    //   298: iconst_1
    //   299: istore_3
    //   300: iload #9
    //   302: istore #4
    //   304: iload_3
    //   305: istore #9
    //   307: goto -> 194
    //   310: aload_1
    //   311: getfield payload : Ljava/lang/Object;
    //   314: astore #10
    //   316: aload_0
    //   317: aload_1
    //   318: invokevirtual recycleUpdateOp : (Landroid/support/v7/widget/AdapterHelper$UpdateOp;)V
    //   321: iload_3
    //   322: ifle -> 350
    //   325: aload_0
    //   326: aload_1
    //   327: getfield cmd : I
    //   330: iload_2
    //   331: iload_3
    //   332: aload #10
    //   334: invokevirtual obtainUpdateOp : (IIILjava/lang/Object;)Landroid/support/v7/widget/AdapterHelper$UpdateOp;
    //   337: astore_1
    //   338: aload_0
    //   339: aload_1
    //   340: iload #4
    //   342: invokevirtual dispatchFirstPassAndUpdateViewHolders : (Landroid/support/v7/widget/AdapterHelper$UpdateOp;I)V
    //   345: aload_0
    //   346: aload_1
    //   347: invokevirtual recycleUpdateOp : (Landroid/support/v7/widget/AdapterHelper$UpdateOp;)V
    //   350: return
  }
  
  private void postponeAndUpdateViewHolders(UpdateOp paramUpdateOp) {
    this.mPostponedList.add(paramUpdateOp);
    switch (paramUpdateOp.cmd) {
      default:
        throw new IllegalArgumentException("Unknown update op type for " + paramUpdateOp);
      case 1:
        this.mCallback.offsetPositionsForAdd(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
        return;
      case 8:
        this.mCallback.offsetPositionsForMove(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
        return;
      case 2:
        this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
        return;
      case 4:
        break;
    } 
    this.mCallback.markViewHoldersUpdated(paramUpdateOp.positionStart, paramUpdateOp.itemCount, paramUpdateOp.payload);
  }
  
  private int updatePositionWithPostponed(int paramInt1, int paramInt2) {
    int i = this.mPostponedList.size() - 1;
    int j;
    for (j = paramInt1; i >= 0; j = paramInt1) {
      UpdateOp updateOp = this.mPostponedList.get(i);
      if (updateOp.cmd == 8) {
        int k;
        if (updateOp.positionStart < updateOp.itemCount) {
          k = updateOp.positionStart;
          paramInt1 = updateOp.itemCount;
        } else {
          k = updateOp.itemCount;
          paramInt1 = updateOp.positionStart;
        } 
        if (j >= k && j <= paramInt1) {
          if (k == updateOp.positionStart) {
            if (paramInt2 == 1) {
              updateOp.itemCount++;
            } else if (paramInt2 == 2) {
              updateOp.itemCount--;
            } 
            paramInt1 = j + 1;
          } else {
            if (paramInt2 == 1) {
              updateOp.positionStart++;
            } else if (paramInt2 == 2) {
              updateOp.positionStart--;
            } 
            paramInt1 = j - 1;
          } 
        } else {
          paramInt1 = j;
          if (j < updateOp.positionStart)
            if (paramInt2 == 1) {
              updateOp.positionStart++;
              updateOp.itemCount++;
              paramInt1 = j;
            } else {
              paramInt1 = j;
              if (paramInt2 == 2) {
                updateOp.positionStart--;
                updateOp.itemCount--;
                paramInt1 = j;
              } 
            }  
        } 
      } else if (updateOp.positionStart <= j) {
        if (updateOp.cmd == 1) {
          paramInt1 = j - updateOp.itemCount;
        } else {
          paramInt1 = j;
          if (updateOp.cmd == 2)
            paramInt1 = j + updateOp.itemCount; 
        } 
      } else if (paramInt2 == 1) {
        updateOp.positionStart++;
        paramInt1 = j;
      } else {
        paramInt1 = j;
        if (paramInt2 == 2) {
          updateOp.positionStart--;
          paramInt1 = j;
        } 
      } 
      i--;
    } 
    for (paramInt1 = this.mPostponedList.size() - 1; paramInt1 >= 0; paramInt1--) {
      UpdateOp updateOp = this.mPostponedList.get(paramInt1);
      if (updateOp.cmd == 8) {
        if (updateOp.itemCount == updateOp.positionStart || updateOp.itemCount < 0) {
          this.mPostponedList.remove(paramInt1);
          recycleUpdateOp(updateOp);
        } 
      } else if (updateOp.itemCount <= 0) {
        this.mPostponedList.remove(paramInt1);
        recycleUpdateOp(updateOp);
      } 
    } 
    return j;
  }
  
  AdapterHelper addUpdateOp(UpdateOp... paramVarArgs) {
    Collections.addAll(this.mPendingUpdates, paramVarArgs);
    return this;
  }
  
  public int applyPendingUpdatesToPosition(int paramInt) {
    int i = this.mPendingUpdates.size();
    byte b = 0;
    int j = paramInt;
    while (true) {
      paramInt = j;
      if (b < i) {
        UpdateOp updateOp = this.mPendingUpdates.get(b);
        switch (updateOp.cmd) {
          case 1:
            paramInt = j;
            if (updateOp.positionStart <= j)
              paramInt = j + updateOp.itemCount; 
            b++;
            j = paramInt;
            break;
          case 2:
            paramInt = j;
            if (updateOp.positionStart <= j) {
              if (updateOp.positionStart + updateOp.itemCount > j)
                return -1; 
              paramInt = j - updateOp.itemCount;
            } 
            b++;
            j = paramInt;
            break;
          case 8:
            if (updateOp.positionStart == j) {
              paramInt = updateOp.itemCount;
            } else {
              int k = j;
              if (updateOp.positionStart < j)
                k = j - 1; 
              paramInt = k;
              if (updateOp.itemCount <= k)
                paramInt = k + 1; 
            } 
            b++;
            j = paramInt;
            break;
        } 
        continue;
      } 
      return paramInt;
    } 
  }
  
  void consumePostponedUpdates() {
    int i = this.mPostponedList.size();
    for (byte b = 0; b < i; b++)
      this.mCallback.onDispatchSecondPass(this.mPostponedList.get(b)); 
    recycleUpdateOpsAndClearList(this.mPostponedList);
    this.mExistingUpdateTypes = 0;
  }
  
  void consumeUpdatesInOnePass() {
    consumePostponedUpdates();
    int i = this.mPendingUpdates.size();
    byte b = 0;
    while (b < i) {
      UpdateOp updateOp = this.mPendingUpdates.get(b);
      switch (updateOp.cmd) {
        case 1:
          this.mCallback.onDispatchSecondPass(updateOp);
          this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
          if (this.mOnItemProcessedCallback != null)
            this.mOnItemProcessedCallback.run(); 
          b++;
          break;
        case 2:
          this.mCallback.onDispatchSecondPass(updateOp);
          this.mCallback.offsetPositionsForRemovingInvisible(updateOp.positionStart, updateOp.itemCount);
          if (this.mOnItemProcessedCallback != null)
            this.mOnItemProcessedCallback.run(); 
          b++;
          break;
        case 4:
          this.mCallback.onDispatchSecondPass(updateOp);
          this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount, updateOp.payload);
          if (this.mOnItemProcessedCallback != null)
            this.mOnItemProcessedCallback.run(); 
          b++;
          break;
        case 8:
          this.mCallback.onDispatchSecondPass(updateOp);
          this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
          if (this.mOnItemProcessedCallback != null)
            this.mOnItemProcessedCallback.run(); 
          b++;
          break;
      } 
    } 
    recycleUpdateOpsAndClearList(this.mPendingUpdates);
    this.mExistingUpdateTypes = 0;
  }
  
  void dispatchFirstPassAndUpdateViewHolders(UpdateOp paramUpdateOp, int paramInt) {
    this.mCallback.onDispatchFirstPass(paramUpdateOp);
    switch (paramUpdateOp.cmd) {
      default:
        throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
      case 2:
        this.mCallback.offsetPositionsForRemovingInvisible(paramInt, paramUpdateOp.itemCount);
        return;
      case 4:
        break;
    } 
    this.mCallback.markViewHoldersUpdated(paramInt, paramUpdateOp.itemCount, paramUpdateOp.payload);
  }
  
  int findPositionOffset(int paramInt) {
    return findPositionOffset(paramInt, 0);
  }
  
  int findPositionOffset(int paramInt1, int paramInt2) {
    int i = this.mPostponedList.size();
    int j = paramInt2;
    paramInt2 = paramInt1;
    while (true) {
      paramInt1 = paramInt2;
      if (j < i) {
        UpdateOp updateOp = this.mPostponedList.get(j);
        if (updateOp.cmd == 8) {
          if (updateOp.positionStart == paramInt2) {
            paramInt1 = updateOp.itemCount;
          } else {
            int k = paramInt2;
            if (updateOp.positionStart < paramInt2)
              k = paramInt2 - 1; 
            paramInt1 = k;
            if (updateOp.itemCount <= k)
              paramInt1 = k + 1; 
          } 
        } else {
          paramInt1 = paramInt2;
          if (updateOp.positionStart <= paramInt2)
            if (updateOp.cmd == 2) {
              if (paramInt2 < updateOp.positionStart + updateOp.itemCount)
                return -1; 
              paramInt1 = paramInt2 - updateOp.itemCount;
            } else {
              paramInt1 = paramInt2;
              if (updateOp.cmd == 1)
                paramInt1 = paramInt2 + updateOp.itemCount; 
            }  
        } 
        j++;
        paramInt2 = paramInt1;
        continue;
      } 
      return paramInt1;
    } 
  }
  
  boolean hasAnyUpdateTypes(int paramInt) {
    return ((this.mExistingUpdateTypes & paramInt) != 0);
  }
  
  boolean hasPendingUpdates() {
    return (this.mPendingUpdates.size() > 0);
  }
  
  boolean hasUpdates() {
    return (!this.mPostponedList.isEmpty() && !this.mPendingUpdates.isEmpty());
  }
  
  public UpdateOp obtainUpdateOp(int paramInt1, int paramInt2, int paramInt3, Object paramObject) {
    UpdateOp updateOp = (UpdateOp)this.mUpdateOpPool.acquire();
    if (updateOp == null)
      return new UpdateOp(paramInt1, paramInt2, paramInt3, paramObject); 
    updateOp.cmd = paramInt1;
    updateOp.positionStart = paramInt2;
    updateOp.itemCount = paramInt3;
    updateOp.payload = paramObject;
    return updateOp;
  }
  
  boolean onItemRangeChanged(int paramInt1, int paramInt2, Object paramObject) {
    boolean bool1 = false;
    boolean bool2 = true;
    if (paramInt2 < 1)
      return bool1; 
    this.mPendingUpdates.add(obtainUpdateOp(4, paramInt1, paramInt2, paramObject));
    this.mExistingUpdateTypes |= 0x4;
    if (this.mPendingUpdates.size() != 1)
      bool2 = false; 
    return bool2;
  }
  
  boolean onItemRangeInserted(int paramInt1, int paramInt2) {
    boolean bool1 = false;
    boolean bool2 = true;
    if (paramInt2 < 1)
      return bool1; 
    this.mPendingUpdates.add(obtainUpdateOp(1, paramInt1, paramInt2, null));
    this.mExistingUpdateTypes |= 0x1;
    if (this.mPendingUpdates.size() != 1)
      bool2 = false; 
    return bool2;
  }
  
  boolean onItemRangeMoved(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool1 = false;
    boolean bool2 = true;
    if (paramInt1 != paramInt2) {
      if (paramInt3 != 1)
        throw new IllegalArgumentException("Moving more than 1 item is not supported yet"); 
      this.mPendingUpdates.add(obtainUpdateOp(8, paramInt1, paramInt2, null));
      this.mExistingUpdateTypes |= 0x8;
      if (this.mPendingUpdates.size() == 1)
        return bool2; 
      bool1 = false;
    } 
    return bool1;
  }
  
  boolean onItemRangeRemoved(int paramInt1, int paramInt2) {
    boolean bool1 = false;
    boolean bool2 = true;
    if (paramInt2 < 1)
      return bool1; 
    this.mPendingUpdates.add(obtainUpdateOp(2, paramInt1, paramInt2, null));
    this.mExistingUpdateTypes |= 0x2;
    if (this.mPendingUpdates.size() != 1)
      bool2 = false; 
    return bool2;
  }
  
  void preProcess() {
    this.mOpReorderer.reorderOps(this.mPendingUpdates);
    int i = this.mPendingUpdates.size();
    byte b = 0;
    while (b < i) {
      UpdateOp updateOp = this.mPendingUpdates.get(b);
      switch (updateOp.cmd) {
        case 1:
          applyAdd(updateOp);
          if (this.mOnItemProcessedCallback != null)
            this.mOnItemProcessedCallback.run(); 
          b++;
          break;
        case 2:
          applyRemove(updateOp);
          if (this.mOnItemProcessedCallback != null)
            this.mOnItemProcessedCallback.run(); 
          b++;
          break;
        case 4:
          applyUpdate(updateOp);
          if (this.mOnItemProcessedCallback != null)
            this.mOnItemProcessedCallback.run(); 
          b++;
          break;
        case 8:
          applyMove(updateOp);
          if (this.mOnItemProcessedCallback != null)
            this.mOnItemProcessedCallback.run(); 
          b++;
          break;
      } 
    } 
    this.mPendingUpdates.clear();
  }
  
  public void recycleUpdateOp(UpdateOp paramUpdateOp) {
    if (!this.mDisableRecycler) {
      paramUpdateOp.payload = null;
      this.mUpdateOpPool.release(paramUpdateOp);
    } 
  }
  
  void recycleUpdateOpsAndClearList(List<UpdateOp> paramList) {
    int i = paramList.size();
    for (byte b = 0; b < i; b++)
      recycleUpdateOp(paramList.get(b)); 
    paramList.clear();
  }
  
  void reset() {
    recycleUpdateOpsAndClearList(this.mPendingUpdates);
    recycleUpdateOpsAndClearList(this.mPostponedList);
    this.mExistingUpdateTypes = 0;
  }
  
  static interface Callback {
    RecyclerView.ViewHolder findViewHolder(int param1Int);
    
    void markViewHoldersUpdated(int param1Int1, int param1Int2, Object param1Object);
    
    void offsetPositionsForAdd(int param1Int1, int param1Int2);
    
    void offsetPositionsForMove(int param1Int1, int param1Int2);
    
    void offsetPositionsForRemovingInvisible(int param1Int1, int param1Int2);
    
    void offsetPositionsForRemovingLaidOutOrNewView(int param1Int1, int param1Int2);
    
    void onDispatchFirstPass(AdapterHelper.UpdateOp param1UpdateOp);
    
    void onDispatchSecondPass(AdapterHelper.UpdateOp param1UpdateOp);
  }
  
  static class UpdateOp {
    static final int ADD = 1;
    
    static final int MOVE = 8;
    
    static final int POOL_SIZE = 30;
    
    static final int REMOVE = 2;
    
    static final int UPDATE = 4;
    
    int cmd;
    
    int itemCount;
    
    Object payload;
    
    int positionStart;
    
    UpdateOp(int param1Int1, int param1Int2, int param1Int3, Object param1Object) {
      this.cmd = param1Int1;
      this.positionStart = param1Int2;
      this.itemCount = param1Int3;
      this.payload = param1Object;
    }
    
    String cmdToString() {
      switch (this.cmd) {
        default:
          return "??";
        case 1:
          return "add";
        case 2:
          return "rm";
        case 4:
          return "up";
        case 8:
          break;
      } 
      return "mv";
    }
    
    public boolean equals(Object param1Object) {
      // Byte code:
      //   0: iconst_1
      //   1: istore_2
      //   2: aload_0
      //   3: aload_1
      //   4: if_acmpne -> 11
      //   7: iload_2
      //   8: istore_3
      //   9: iload_3
      //   10: ireturn
      //   11: aload_1
      //   12: ifnull -> 26
      //   15: aload_0
      //   16: invokevirtual getClass : ()Ljava/lang/Class;
      //   19: aload_1
      //   20: invokevirtual getClass : ()Ljava/lang/Class;
      //   23: if_acmpeq -> 31
      //   26: iconst_0
      //   27: istore_3
      //   28: goto -> 9
      //   31: aload_1
      //   32: checkcast android/support/v7/widget/AdapterHelper$UpdateOp
      //   35: astore_1
      //   36: aload_0
      //   37: getfield cmd : I
      //   40: aload_1
      //   41: getfield cmd : I
      //   44: if_icmpeq -> 52
      //   47: iconst_0
      //   48: istore_3
      //   49: goto -> 9
      //   52: aload_0
      //   53: getfield cmd : I
      //   56: bipush #8
      //   58: if_icmpne -> 101
      //   61: aload_0
      //   62: getfield itemCount : I
      //   65: aload_0
      //   66: getfield positionStart : I
      //   69: isub
      //   70: invokestatic abs : (I)I
      //   73: iconst_1
      //   74: if_icmpne -> 101
      //   77: aload_0
      //   78: getfield itemCount : I
      //   81: aload_1
      //   82: getfield positionStart : I
      //   85: if_icmpne -> 101
      //   88: iload_2
      //   89: istore_3
      //   90: aload_0
      //   91: getfield positionStart : I
      //   94: aload_1
      //   95: getfield itemCount : I
      //   98: if_icmpeq -> 9
      //   101: aload_0
      //   102: getfield itemCount : I
      //   105: aload_1
      //   106: getfield itemCount : I
      //   109: if_icmpeq -> 117
      //   112: iconst_0
      //   113: istore_3
      //   114: goto -> 9
      //   117: aload_0
      //   118: getfield positionStart : I
      //   121: aload_1
      //   122: getfield positionStart : I
      //   125: if_icmpeq -> 133
      //   128: iconst_0
      //   129: istore_3
      //   130: goto -> 9
      //   133: aload_0
      //   134: getfield payload : Ljava/lang/Object;
      //   137: ifnull -> 161
      //   140: iload_2
      //   141: istore_3
      //   142: aload_0
      //   143: getfield payload : Ljava/lang/Object;
      //   146: aload_1
      //   147: getfield payload : Ljava/lang/Object;
      //   150: invokevirtual equals : (Ljava/lang/Object;)Z
      //   153: ifne -> 9
      //   156: iconst_0
      //   157: istore_3
      //   158: goto -> 9
      //   161: iload_2
      //   162: istore_3
      //   163: aload_1
      //   164: getfield payload : Ljava/lang/Object;
      //   167: ifnull -> 9
      //   170: iconst_0
      //   171: istore_3
      //   172: goto -> 9
    }
    
    public int hashCode() {
      return (this.cmd * 31 + this.positionStart) * 31 + this.itemCount;
    }
    
    public String toString() {
      return Integer.toHexString(System.identityHashCode(this)) + "[" + cmdToString() + ",s:" + this.positionStart + "c:" + this.itemCount + ",p:" + this.payload + "]";
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/AdapterHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */