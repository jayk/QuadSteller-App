package android.support.v7.util;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DiffUtil {
  private static final Comparator<Snake> SNAKE_COMPARATOR = new Comparator<Snake>() {
      public int compare(DiffUtil.Snake param1Snake1, DiffUtil.Snake param1Snake2) {
        int i = param1Snake1.x - param1Snake2.x;
        int j = i;
        if (i == 0)
          j = param1Snake1.y - param1Snake2.y; 
        return j;
      }
    };
  
  public static DiffResult calculateDiff(Callback paramCallback) {
    return calculateDiff(paramCallback, true);
  }
  
  public static DiffResult calculateDiff(Callback paramCallback, boolean paramBoolean) {
    int i = paramCallback.getOldListSize();
    int j = paramCallback.getNewListSize();
    ArrayList<Snake> arrayList = new ArrayList();
    ArrayList<Range> arrayList1 = new ArrayList();
    arrayList1.add(new Range(0, i, 0, j));
    i = i + j + Math.abs(i - j);
    int[] arrayOfInt1 = new int[i * 2];
    int[] arrayOfInt2 = new int[i * 2];
    ArrayList<Range> arrayList2 = new ArrayList();
    while (!arrayList1.isEmpty()) {
      Range range = arrayList1.remove(arrayList1.size() - 1);
      Snake snake = diffPartial(paramCallback, range.oldListStart, range.oldListEnd, range.newListStart, range.newListEnd, arrayOfInt1, arrayOfInt2, i);
      if (snake != null) {
        Range range1;
        if (snake.size > 0)
          arrayList.add(snake); 
        snake.x += range.oldListStart;
        snake.y += range.newListStart;
        if (arrayList2.isEmpty()) {
          range1 = new Range();
        } else {
          range1 = arrayList2.remove(arrayList2.size() - 1);
        } 
        range1.oldListStart = range.oldListStart;
        range1.newListStart = range.newListStart;
        if (snake.reverse) {
          range1.oldListEnd = snake.x;
          range1.newListEnd = snake.y;
        } else if (snake.removal) {
          range1.oldListEnd = snake.x - 1;
          range1.newListEnd = snake.y;
        } else {
          range1.oldListEnd = snake.x;
          range1.newListEnd = snake.y - 1;
        } 
        arrayList1.add(range1);
        if (snake.reverse) {
          if (snake.removal) {
            range.oldListStart = snake.x + snake.size + 1;
            range.newListStart = snake.y + snake.size;
          } else {
            range.oldListStart = snake.x + snake.size;
            range.newListStart = snake.y + snake.size + 1;
          } 
        } else {
          range.oldListStart = snake.x + snake.size;
          range.newListStart = snake.y + snake.size;
        } 
        arrayList1.add(range);
        continue;
      } 
      arrayList2.add(range);
    } 
    Collections.sort(arrayList, SNAKE_COMPARATOR);
    return new DiffResult(paramCallback, arrayList, arrayOfInt1, arrayOfInt2, paramBoolean);
  }
  
  private static Snake diffPartial(Callback paramCallback, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt5) {
    int i = paramInt2 - paramInt1;
    int j = paramInt4 - paramInt3;
    if (paramInt2 - paramInt1 < 1 || paramInt4 - paramInt3 < 1)
      return null; 
    int k = i - j;
    int m = (i + j + 1) / 2;
    Arrays.fill(paramArrayOfint1, paramInt5 - m - 1, paramInt5 + m + 1, 0);
    Arrays.fill(paramArrayOfint2, paramInt5 - m - 1 + k, paramInt5 + m + 1 + k, i);
    if (k % 2 != 0) {
      paramInt4 = 1;
    } else {
      paramInt4 = 0;
    } 
    for (byte b = 0; b <= m; b++) {
      Snake snake;
      byte b1;
      for (b1 = -b; b1 <= b; b1 += 2) {
        boolean bool;
        if (b1 == -b || (b1 != b && paramArrayOfint1[paramInt5 + b1 - 1] < paramArrayOfint1[paramInt5 + b1 + 1])) {
          paramInt2 = paramArrayOfint1[paramInt5 + b1 + 1];
          bool = false;
        } else {
          paramInt2 = paramArrayOfint1[paramInt5 + b1 - 1] + 1;
          bool = true;
        } 
        for (int n = paramInt2 - b1; paramInt2 < i && n < j && paramCallback.areItemsTheSame(paramInt1 + paramInt2, paramInt3 + n); n++)
          paramInt2++; 
        paramArrayOfint1[paramInt5 + b1] = paramInt2;
        if (paramInt4 != 0 && b1 >= k - b + 1 && b1 <= k + b - 1 && paramArrayOfint1[paramInt5 + b1] >= paramArrayOfint2[paramInt5 + b1]) {
          snake = new Snake();
          snake.x = paramArrayOfint2[paramInt5 + b1];
          snake.y = snake.x - b1;
          snake.size = paramArrayOfint1[paramInt5 + b1] - paramArrayOfint2[paramInt5 + b1];
          snake.removal = bool;
          snake.reverse = false;
          return snake;
        } 
      } 
      for (b1 = -b; b1 <= b; b1 += 2) {
        boolean bool;
        int i1 = b1 + k;
        if (i1 == b + k || (i1 != -b + k && paramArrayOfint2[paramInt5 + i1 - 1] < paramArrayOfint2[paramInt5 + i1 + 1])) {
          paramInt2 = paramArrayOfint2[paramInt5 + i1 - 1];
          bool = false;
        } else {
          paramInt2 = paramArrayOfint2[paramInt5 + i1 + 1] - 1;
          bool = true;
        } 
        for (int n = paramInt2 - i1; paramInt2 > 0 && n > 0 && snake.areItemsTheSame(paramInt1 + paramInt2 - 1, paramInt3 + n - 1); n--)
          paramInt2--; 
        paramArrayOfint2[paramInt5 + i1] = paramInt2;
        if (paramInt4 == 0 && b1 + k >= -b && b1 + k <= b && paramArrayOfint1[paramInt5 + i1] >= paramArrayOfint2[paramInt5 + i1]) {
          snake = new Snake();
          snake.x = paramArrayOfint2[paramInt5 + i1];
          snake.y = snake.x - i1;
          snake.size = paramArrayOfint1[paramInt5 + i1] - paramArrayOfint2[paramInt5 + i1];
          snake.removal = bool;
          snake.reverse = true;
          return snake;
        } 
      } 
    } 
    throw new IllegalStateException("DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation.");
  }
  
  public static abstract class Callback {
    public abstract boolean areContentsTheSame(int param1Int1, int param1Int2);
    
    public abstract boolean areItemsTheSame(int param1Int1, int param1Int2);
    
    @Nullable
    public Object getChangePayload(int param1Int1, int param1Int2) {
      return null;
    }
    
    public abstract int getNewListSize();
    
    public abstract int getOldListSize();
  }
  
  public static class DiffResult {
    private static final int FLAG_CHANGED = 2;
    
    private static final int FLAG_IGNORE = 16;
    
    private static final int FLAG_MASK = 31;
    
    private static final int FLAG_MOVED_CHANGED = 4;
    
    private static final int FLAG_MOVED_NOT_CHANGED = 8;
    
    private static final int FLAG_NOT_CHANGED = 1;
    
    private static final int FLAG_OFFSET = 5;
    
    private final DiffUtil.Callback mCallback;
    
    private final boolean mDetectMoves;
    
    private final int[] mNewItemStatuses;
    
    private final int mNewListSize;
    
    private final int[] mOldItemStatuses;
    
    private final int mOldListSize;
    
    private final List<DiffUtil.Snake> mSnakes;
    
    DiffResult(DiffUtil.Callback param1Callback, List<DiffUtil.Snake> param1List, int[] param1ArrayOfint1, int[] param1ArrayOfint2, boolean param1Boolean) {
      this.mSnakes = param1List;
      this.mOldItemStatuses = param1ArrayOfint1;
      this.mNewItemStatuses = param1ArrayOfint2;
      Arrays.fill(this.mOldItemStatuses, 0);
      Arrays.fill(this.mNewItemStatuses, 0);
      this.mCallback = param1Callback;
      this.mOldListSize = param1Callback.getOldListSize();
      this.mNewListSize = param1Callback.getNewListSize();
      this.mDetectMoves = param1Boolean;
      addRootSnake();
      findMatchingItems();
    }
    
    private void addRootSnake() {
      DiffUtil.Snake snake;
      if (this.mSnakes.isEmpty()) {
        snake = null;
      } else {
        snake = this.mSnakes.get(0);
      } 
      if (snake == null || snake.x != 0 || snake.y != 0) {
        snake = new DiffUtil.Snake();
        snake.x = 0;
        snake.y = 0;
        snake.removal = false;
        snake.size = 0;
        snake.reverse = false;
        this.mSnakes.add(0, snake);
      } 
    }
    
    private void dispatchAdditions(List<DiffUtil.PostponedUpdate> param1List, ListUpdateCallback param1ListUpdateCallback, int param1Int1, int param1Int2, int param1Int3) {
      if (!this.mDetectMoves) {
        param1ListUpdateCallback.onInserted(param1Int1, param1Int2);
        return;
      } 
      param1Int2--;
      while (true) {
        if (param1Int2 >= 0) {
          int j;
          int i = this.mNewItemStatuses[param1Int3 + param1Int2] & 0x1F;
          switch (i) {
            case 0:
              param1ListUpdateCallback.onInserted(param1Int1, 1);
              for (DiffUtil.PostponedUpdate postponedUpdate : param1List)
                postponedUpdate.currentPos++; 
              param1Int2--;
              break;
            case 4:
            case 8:
              j = this.mNewItemStatuses[param1Int3 + param1Int2] >> 5;
              param1ListUpdateCallback.onMoved((removePostponedUpdate(param1List, j, true)).currentPos, param1Int1);
              if (i == 4)
                param1ListUpdateCallback.onChanged(param1Int1, 1, this.mCallback.getChangePayload(j, param1Int3 + param1Int2)); 
              param1Int2--;
              break;
            case 16:
              param1List.add(new DiffUtil.PostponedUpdate(param1Int3 + param1Int2, param1Int1, false));
              param1Int2--;
              break;
          } 
          continue;
        } 
        return;
      } 
    }
    
    private void dispatchRemovals(List<DiffUtil.PostponedUpdate> param1List, ListUpdateCallback param1ListUpdateCallback, int param1Int1, int param1Int2, int param1Int3) {
      if (!this.mDetectMoves) {
        param1ListUpdateCallback.onRemoved(param1Int1, param1Int2);
        return;
      } 
      param1Int2--;
      while (true) {
        if (param1Int2 >= 0) {
          DiffUtil.PostponedUpdate postponedUpdate;
          int j;
          int i = this.mOldItemStatuses[param1Int3 + param1Int2] & 0x1F;
          switch (i) {
            case 0:
              param1ListUpdateCallback.onRemoved(param1Int1 + param1Int2, 1);
              for (DiffUtil.PostponedUpdate postponedUpdate1 : param1List)
                postponedUpdate1.currentPos--; 
              param1Int2--;
              break;
            case 4:
            case 8:
              j = this.mOldItemStatuses[param1Int3 + param1Int2] >> 5;
              postponedUpdate = removePostponedUpdate(param1List, j, false);
              param1ListUpdateCallback.onMoved(param1Int1 + param1Int2, postponedUpdate.currentPos - 1);
              if (i == 4)
                param1ListUpdateCallback.onChanged(postponedUpdate.currentPos - 1, 1, this.mCallback.getChangePayload(param1Int3 + param1Int2, j)); 
              param1Int2--;
              break;
            case 16:
              param1List.add(new DiffUtil.PostponedUpdate(param1Int3 + param1Int2, param1Int1 + param1Int2, true));
              param1Int2--;
              break;
          } 
          continue;
        } 
        return;
      } 
    }
    
    private void findAddition(int param1Int1, int param1Int2, int param1Int3) {
      if (this.mOldItemStatuses[param1Int1 - 1] == 0)
        findMatchingItem(param1Int1, param1Int2, param1Int3, false); 
    }
    
    private boolean findMatchingItem(int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {
      int i;
      int j;
      if (param1Boolean) {
        i = param1Int2 - 1;
        int k = param1Int1;
        j = param1Int2 - 1;
        param1Int2 = k;
      } else {
        i = param1Int1 - 1;
        int k = param1Int1 - 1;
        j = param1Int2;
        param1Int2 = k;
      } 
      while (param1Int3 >= 0) {
        DiffUtil.Snake snake = this.mSnakes.get(param1Int3);
        int m = snake.x;
        int n = snake.size;
        int i1 = snake.y;
        int k = snake.size;
        if (param1Boolean) {
          while (--param1Int2 >= m + n) {
            if (this.mCallback.areItemsTheSame(param1Int2, i)) {
              if (this.mCallback.areContentsTheSame(param1Int2, i)) {
                param1Int1 = 8;
              } else {
                param1Int1 = 4;
              } 
              this.mNewItemStatuses[i] = param1Int2 << 5 | 0x10;
              this.mOldItemStatuses[param1Int2] = i << 5 | param1Int1;
              return true;
            } 
            param1Int2--;
          } 
        } else {
          for (param1Int2 = j - 1; param1Int2 >= i1 + k; param1Int2--) {
            if (this.mCallback.areItemsTheSame(i, param1Int2)) {
              if (this.mCallback.areContentsTheSame(i, param1Int2)) {
                param1Int3 = 8;
              } else {
                param1Int3 = 4;
              } 
              this.mOldItemStatuses[param1Int1 - 1] = param1Int2 << 5 | 0x10;
              this.mNewItemStatuses[param1Int2] = param1Int1 - 1 << 5 | param1Int3;
              return true;
            } 
          } 
        } 
        param1Int2 = snake.x;
        j = snake.y;
        param1Int3--;
      } 
      return false;
    }
    
    private void findMatchingItems() {
      int i = this.mOldListSize;
      int j = this.mNewListSize;
      for (int k = this.mSnakes.size() - 1; k >= 0; k--) {
        DiffUtil.Snake snake = this.mSnakes.get(k);
        int m = snake.x;
        int n = snake.size;
        int i1 = snake.y;
        int i2 = snake.size;
        if (this.mDetectMoves) {
          int i3;
          while (true) {
            i3 = j;
            if (i > m + n) {
              findAddition(i, j, k);
              i--;
              continue;
            } 
            break;
          } 
          while (i3 > i1 + i2) {
            findRemoval(i, i3, k);
            i3--;
          } 
        } 
        for (j = 0; j < snake.size; j++) {
          int i3 = snake.x + j;
          i2 = snake.y + j;
          if (this.mCallback.areContentsTheSame(i3, i2)) {
            i = 1;
          } else {
            i = 2;
          } 
          this.mOldItemStatuses[i3] = i2 << 5 | i;
          this.mNewItemStatuses[i2] = i3 << 5 | i;
        } 
        i = snake.x;
        j = snake.y;
      } 
    }
    
    private void findRemoval(int param1Int1, int param1Int2, int param1Int3) {
      if (this.mNewItemStatuses[param1Int2 - 1] == 0)
        findMatchingItem(param1Int1, param1Int2, param1Int3, true); 
    }
    
    private static DiffUtil.PostponedUpdate removePostponedUpdate(List<DiffUtil.PostponedUpdate> param1List, int param1Int, boolean param1Boolean) {
      // Byte code:
      //   0: aload_0
      //   1: invokeinterface size : ()I
      //   6: iconst_1
      //   7: isub
      //   8: istore_3
      //   9: iload_3
      //   10: iflt -> 118
      //   13: aload_0
      //   14: iload_3
      //   15: invokeinterface get : (I)Ljava/lang/Object;
      //   20: checkcast android/support/v7/util/DiffUtil$PostponedUpdate
      //   23: astore #4
      //   25: aload #4
      //   27: getfield posInOwnerList : I
      //   30: iload_1
      //   31: if_icmpne -> 112
      //   34: aload #4
      //   36: getfield removal : Z
      //   39: iload_2
      //   40: if_icmpne -> 112
      //   43: aload_0
      //   44: iload_3
      //   45: invokeinterface remove : (I)Ljava/lang/Object;
      //   50: pop
      //   51: iload_3
      //   52: istore_1
      //   53: aload #4
      //   55: astore #5
      //   57: iload_1
      //   58: aload_0
      //   59: invokeinterface size : ()I
      //   64: if_icmpge -> 121
      //   67: aload_0
      //   68: iload_1
      //   69: invokeinterface get : (I)Ljava/lang/Object;
      //   74: checkcast android/support/v7/util/DiffUtil$PostponedUpdate
      //   77: astore #5
      //   79: aload #5
      //   81: getfield currentPos : I
      //   84: istore #6
      //   86: iload_2
      //   87: ifeq -> 107
      //   90: iconst_1
      //   91: istore_3
      //   92: aload #5
      //   94: iload_3
      //   95: iload #6
      //   97: iadd
      //   98: putfield currentPos : I
      //   101: iinc #1, 1
      //   104: goto -> 53
      //   107: iconst_m1
      //   108: istore_3
      //   109: goto -> 92
      //   112: iinc #3, -1
      //   115: goto -> 9
      //   118: aconst_null
      //   119: astore #5
      //   121: aload #5
      //   123: areturn
    }
    
    public void dispatchUpdatesTo(ListUpdateCallback param1ListUpdateCallback) {
      if (param1ListUpdateCallback instanceof BatchingListUpdateCallback) {
        param1ListUpdateCallback = param1ListUpdateCallback;
      } else {
        param1ListUpdateCallback = new BatchingListUpdateCallback(param1ListUpdateCallback);
      } 
      ArrayList<DiffUtil.PostponedUpdate> arrayList = new ArrayList();
      int i = this.mOldListSize;
      int j = this.mNewListSize;
      for (int k = this.mSnakes.size() - 1; k >= 0; k--) {
        DiffUtil.Snake snake = this.mSnakes.get(k);
        int m = snake.size;
        int n = snake.x + m;
        int i1 = snake.y + m;
        if (n < i)
          dispatchRemovals(arrayList, param1ListUpdateCallback, n, i - n, n); 
        if (i1 < j)
          dispatchAdditions(arrayList, param1ListUpdateCallback, n, j - i1, i1); 
        for (j = m - 1; j >= 0; j--) {
          if ((this.mOldItemStatuses[snake.x + j] & 0x1F) == 2)
            param1ListUpdateCallback.onChanged(snake.x + j, 1, this.mCallback.getChangePayload(snake.x + j, snake.y + j)); 
        } 
        i = snake.x;
        j = snake.y;
      } 
      param1ListUpdateCallback.dispatchLastEvent();
    }
    
    public void dispatchUpdatesTo(final RecyclerView.Adapter adapter) {
      dispatchUpdatesTo(new ListUpdateCallback() {
            public void onChanged(int param2Int1, int param2Int2, Object param2Object) {
              adapter.notifyItemRangeChanged(param2Int1, param2Int2, param2Object);
            }
            
            public void onInserted(int param2Int1, int param2Int2) {
              adapter.notifyItemRangeInserted(param2Int1, param2Int2);
            }
            
            public void onMoved(int param2Int1, int param2Int2) {
              adapter.notifyItemMoved(param2Int1, param2Int2);
            }
            
            public void onRemoved(int param2Int1, int param2Int2) {
              adapter.notifyItemRangeRemoved(param2Int1, param2Int2);
            }
          });
    }
    
    @VisibleForTesting
    List<DiffUtil.Snake> getSnakes() {
      return this.mSnakes;
    }
  }
  
  class null implements ListUpdateCallback {
    public void onChanged(int param1Int1, int param1Int2, Object param1Object) {
      adapter.notifyItemRangeChanged(param1Int1, param1Int2, param1Object);
    }
    
    public void onInserted(int param1Int1, int param1Int2) {
      adapter.notifyItemRangeInserted(param1Int1, param1Int2);
    }
    
    public void onMoved(int param1Int1, int param1Int2) {
      adapter.notifyItemMoved(param1Int1, param1Int2);
    }
    
    public void onRemoved(int param1Int1, int param1Int2) {
      adapter.notifyItemRangeRemoved(param1Int1, param1Int2);
    }
  }
  
  private static class PostponedUpdate {
    int currentPos;
    
    int posInOwnerList;
    
    boolean removal;
    
    public PostponedUpdate(int param1Int1, int param1Int2, boolean param1Boolean) {
      this.posInOwnerList = param1Int1;
      this.currentPos = param1Int2;
      this.removal = param1Boolean;
    }
  }
  
  static class Range {
    int newListEnd;
    
    int newListStart;
    
    int oldListEnd;
    
    int oldListStart;
    
    public Range() {}
    
    public Range(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      this.oldListStart = param1Int1;
      this.oldListEnd = param1Int2;
      this.newListStart = param1Int3;
      this.newListEnd = param1Int4;
    }
  }
  
  static class Snake {
    boolean removal;
    
    boolean reverse;
    
    int size;
    
    int x;
    
    int y;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/util/DiffUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */