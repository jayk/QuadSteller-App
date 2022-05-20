package android.support.v7.widget;

import java.util.List;

class OpReorderer {
  final Callback mCallback;
  
  public OpReorderer(Callback paramCallback) {
    this.mCallback = paramCallback;
  }
  
  private int getLastMoveOutOfOrder(List<AdapterHelper.UpdateOp> paramList) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_1
    //   3: invokeinterface size : ()I
    //   8: iconst_1
    //   9: isub
    //   10: istore_3
    //   11: iload_3
    //   12: iflt -> 54
    //   15: aload_1
    //   16: iload_3
    //   17: invokeinterface get : (I)Ljava/lang/Object;
    //   22: checkcast android/support/v7/widget/AdapterHelper$UpdateOp
    //   25: getfield cmd : I
    //   28: bipush #8
    //   30: if_icmpne -> 42
    //   33: iload_2
    //   34: istore #4
    //   36: iload_2
    //   37: ifeq -> 45
    //   40: iload_3
    //   41: ireturn
    //   42: iconst_1
    //   43: istore #4
    //   45: iinc #3, -1
    //   48: iload #4
    //   50: istore_2
    //   51: goto -> 11
    //   54: iconst_m1
    //   55: istore_3
    //   56: goto -> 40
  }
  
  private void swapMoveAdd(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    int i = 0;
    if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart)
      i = 0 - 1; 
    int j = i;
    if (paramUpdateOp1.positionStart < paramUpdateOp2.positionStart)
      j = i + 1; 
    if (paramUpdateOp2.positionStart <= paramUpdateOp1.positionStart)
      paramUpdateOp1.positionStart += paramUpdateOp2.itemCount; 
    if (paramUpdateOp2.positionStart <= paramUpdateOp1.itemCount)
      paramUpdateOp1.itemCount += paramUpdateOp2.itemCount; 
    paramUpdateOp2.positionStart += j;
    paramList.set(paramInt1, paramUpdateOp2);
    paramList.set(paramInt2, paramUpdateOp1);
  }
  
  private void swapMoveOp(List<AdapterHelper.UpdateOp> paramList, int paramInt1, int paramInt2) {
    AdapterHelper.UpdateOp updateOp1 = paramList.get(paramInt1);
    AdapterHelper.UpdateOp updateOp2 = paramList.get(paramInt2);
    switch (updateOp2.cmd) {
      default:
        return;
      case 2:
        swapMoveRemove(paramList, paramInt1, updateOp1, paramInt2, updateOp2);
      case 1:
        swapMoveAdd(paramList, paramInt1, updateOp1, paramInt2, updateOp2);
      case 4:
        break;
    } 
    swapMoveUpdate(paramList, paramInt1, updateOp1, paramInt2, updateOp2);
  }
  
  void reorderOps(List<AdapterHelper.UpdateOp> paramList) {
    while (true) {
      int i = getLastMoveOutOfOrder(paramList);
      if (i != -1) {
        swapMoveOp(paramList, i, i + 1);
        continue;
      } 
      break;
    } 
  }
  
  void swapMoveRemove(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    boolean bool;
    int j;
    AdapterHelper.UpdateOp updateOp = null;
    int i = 0;
    if (paramUpdateOp1.positionStart < paramUpdateOp1.itemCount) {
      boolean bool1 = false;
      bool = bool1;
      j = i;
      if (paramUpdateOp2.positionStart == paramUpdateOp1.positionStart) {
        bool = bool1;
        j = i;
        if (paramUpdateOp2.itemCount == paramUpdateOp1.itemCount - paramUpdateOp1.positionStart) {
          j = 1;
          bool = bool1;
        } 
      } 
    } else {
      boolean bool1 = true;
      bool = bool1;
      j = i;
      if (paramUpdateOp2.positionStart == paramUpdateOp1.itemCount + 1) {
        bool = bool1;
        j = i;
        if (paramUpdateOp2.itemCount == paramUpdateOp1.positionStart - paramUpdateOp1.itemCount) {
          j = 1;
          bool = bool1;
        } 
      } 
    } 
    if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart) {
      paramUpdateOp2.positionStart--;
    } else if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart + paramUpdateOp2.itemCount) {
      paramUpdateOp2.itemCount--;
      paramUpdateOp1.cmd = 2;
      paramUpdateOp1.itemCount = 1;
      if (paramUpdateOp2.itemCount == 0) {
        paramList.remove(paramInt2);
        this.mCallback.recycleUpdateOp(paramUpdateOp2);
      } 
      return;
    } 
    if (paramUpdateOp1.positionStart <= paramUpdateOp2.positionStart) {
      paramUpdateOp2.positionStart++;
    } else if (paramUpdateOp1.positionStart < paramUpdateOp2.positionStart + paramUpdateOp2.itemCount) {
      i = paramUpdateOp2.positionStart;
      int k = paramUpdateOp2.itemCount;
      int m = paramUpdateOp1.positionStart;
      updateOp = this.mCallback.obtainUpdateOp(2, paramUpdateOp1.positionStart + 1, i + k - m, null);
      paramUpdateOp2.itemCount = paramUpdateOp1.positionStart - paramUpdateOp2.positionStart;
    } 
    if (j != 0) {
      paramList.set(paramInt1, paramUpdateOp2);
      paramList.remove(paramInt2);
      this.mCallback.recycleUpdateOp(paramUpdateOp1);
      return;
    } 
    if (bool) {
      if (updateOp != null) {
        if (paramUpdateOp1.positionStart > updateOp.positionStart)
          paramUpdateOp1.positionStart -= updateOp.itemCount; 
        if (paramUpdateOp1.itemCount > updateOp.positionStart)
          paramUpdateOp1.itemCount -= updateOp.itemCount; 
      } 
      if (paramUpdateOp1.positionStart > paramUpdateOp2.positionStart)
        paramUpdateOp1.positionStart -= paramUpdateOp2.itemCount; 
      if (paramUpdateOp1.itemCount > paramUpdateOp2.positionStart)
        paramUpdateOp1.itemCount -= paramUpdateOp2.itemCount; 
    } else {
      if (updateOp != null) {
        if (paramUpdateOp1.positionStart >= updateOp.positionStart)
          paramUpdateOp1.positionStart -= updateOp.itemCount; 
        if (paramUpdateOp1.itemCount >= updateOp.positionStart)
          paramUpdateOp1.itemCount -= updateOp.itemCount; 
      } 
      if (paramUpdateOp1.positionStart >= paramUpdateOp2.positionStart)
        paramUpdateOp1.positionStart -= paramUpdateOp2.itemCount; 
      if (paramUpdateOp1.itemCount >= paramUpdateOp2.positionStart)
        paramUpdateOp1.itemCount -= paramUpdateOp2.itemCount; 
    } 
    paramList.set(paramInt1, paramUpdateOp2);
    if (paramUpdateOp1.positionStart != paramUpdateOp1.itemCount) {
      paramList.set(paramInt2, paramUpdateOp1);
    } else {
      paramList.remove(paramInt2);
    } 
    if (updateOp != null)
      paramList.add(paramInt1, updateOp); 
  }
  
  void swapMoveUpdate(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    AdapterHelper.UpdateOp updateOp1 = null;
    AdapterHelper.UpdateOp updateOp2 = null;
    if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart) {
      paramUpdateOp2.positionStart--;
    } else if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart + paramUpdateOp2.itemCount) {
      paramUpdateOp2.itemCount--;
      updateOp1 = this.mCallback.obtainUpdateOp(4, paramUpdateOp1.positionStart, 1, paramUpdateOp2.payload);
    } 
    if (paramUpdateOp1.positionStart <= paramUpdateOp2.positionStart) {
      paramUpdateOp2.positionStart++;
    } else if (paramUpdateOp1.positionStart < paramUpdateOp2.positionStart + paramUpdateOp2.itemCount) {
      int i = paramUpdateOp2.positionStart + paramUpdateOp2.itemCount - paramUpdateOp1.positionStart;
      updateOp2 = this.mCallback.obtainUpdateOp(4, paramUpdateOp1.positionStart + 1, i, paramUpdateOp2.payload);
      paramUpdateOp2.itemCount -= i;
    } 
    paramList.set(paramInt2, paramUpdateOp1);
    if (paramUpdateOp2.itemCount > 0) {
      paramList.set(paramInt1, paramUpdateOp2);
    } else {
      paramList.remove(paramInt1);
      this.mCallback.recycleUpdateOp(paramUpdateOp2);
    } 
    if (updateOp1 != null)
      paramList.add(paramInt1, updateOp1); 
    if (updateOp2 != null)
      paramList.add(paramInt1, updateOp2); 
  }
  
  static interface Callback {
    AdapterHelper.UpdateOp obtainUpdateOp(int param1Int1, int param1Int2, int param1Int3, Object param1Object);
    
    void recycleUpdateOp(AdapterHelper.UpdateOp param1UpdateOp);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/OpReorderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */