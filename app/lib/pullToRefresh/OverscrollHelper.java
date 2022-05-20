package app.lib.pullToRefresh;

import android.annotation.TargetApi;
import android.view.View;

@TargetApi(9)
public final class OverscrollHelper {
  static final float DEFAULT_OVERSCROLL_SCALE = 1.0F;
  
  static final String LOG_TAG = "OverscrollHelper";
  
  static boolean isAndroidOverScrollEnabled(View paramView) {
    return (paramView.getOverScrollMode() != 2);
  }
  
  public static void overScrollBy(PullToRefreshBase<?> paramPullToRefreshBase, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, float paramFloat, boolean paramBoolean) {
    // Byte code:
    //   0: getstatic app/lib/pullToRefresh/OverscrollHelper$1.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Orientation : [I
    //   3: aload_0
    //   4: invokevirtual getPullToRefreshScrollDirection : ()Lapp/lib/pullToRefresh/PullToRefreshBase$Orientation;
    //   7: invokevirtual ordinal : ()I
    //   10: iaload
    //   11: tableswitch default -> 28, 1 -> 216
    //   28: iload_3
    //   29: istore #9
    //   31: iload #4
    //   33: istore #10
    //   35: aload_0
    //   36: invokevirtual getScrollY : ()I
    //   39: istore #11
    //   41: aload_0
    //   42: invokevirtual isPullToRefreshOverScrollEnabled : ()Z
    //   45: ifeq -> 215
    //   48: aload_0
    //   49: invokevirtual isRefreshing : ()Z
    //   52: ifne -> 215
    //   55: aload_0
    //   56: invokevirtual getMode : ()Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   59: astore #12
    //   61: aload #12
    //   63: invokevirtual permitsPullToRefresh : ()Z
    //   66: ifeq -> 320
    //   69: iload #8
    //   71: ifne -> 320
    //   74: iload #9
    //   76: ifeq -> 320
    //   79: iload #9
    //   81: iload #10
    //   83: iadd
    //   84: istore #10
    //   86: ldc 'OverscrollHelper'
    //   88: new java/lang/StringBuilder
    //   91: dup
    //   92: invokespecial <init> : ()V
    //   95: ldc 'OverScroll. DeltaX: '
    //   97: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: iload_1
    //   101: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   104: ldc ', ScrollX: '
    //   106: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: iload_2
    //   110: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   113: ldc ', DeltaY: '
    //   115: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: iload_3
    //   119: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   122: ldc ', ScrollY: '
    //   124: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   127: iload #4
    //   129: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   132: ldc ', NewY: '
    //   134: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: iload #10
    //   139: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   142: ldc ', ScrollRange: '
    //   144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   147: iload #5
    //   149: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   152: ldc ', CurrentScroll: '
    //   154: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: iload #11
    //   159: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   162: invokevirtual toString : ()Ljava/lang/String;
    //   165: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   168: pop
    //   169: iload #10
    //   171: iconst_0
    //   172: iload #6
    //   174: isub
    //   175: if_icmpge -> 231
    //   178: aload #12
    //   180: invokevirtual showHeaderLoadingLayout : ()Z
    //   183: ifeq -> 215
    //   186: iload #11
    //   188: ifne -> 201
    //   191: aload_0
    //   192: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.OVERSCROLLING : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   195: iconst_0
    //   196: newarray boolean
    //   198: invokevirtual setState : (Lapp/lib/pullToRefresh/PullToRefreshBase$State;[Z)V
    //   201: aload_0
    //   202: iload #11
    //   204: iload #10
    //   206: iadd
    //   207: i2f
    //   208: fload #7
    //   210: fmul
    //   211: f2i
    //   212: invokevirtual setHeaderScroll : (I)V
    //   215: return
    //   216: iload_1
    //   217: istore #9
    //   219: iload_2
    //   220: istore #10
    //   222: aload_0
    //   223: invokevirtual getScrollX : ()I
    //   226: istore #11
    //   228: goto -> 41
    //   231: iload #10
    //   233: iload #5
    //   235: iload #6
    //   237: iadd
    //   238: if_icmple -> 284
    //   241: aload #12
    //   243: invokevirtual showFooterLoadingLayout : ()Z
    //   246: ifeq -> 215
    //   249: iload #11
    //   251: ifne -> 264
    //   254: aload_0
    //   255: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.OVERSCROLLING : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   258: iconst_0
    //   259: newarray boolean
    //   261: invokevirtual setState : (Lapp/lib/pullToRefresh/PullToRefreshBase$State;[Z)V
    //   264: aload_0
    //   265: iload #11
    //   267: iload #10
    //   269: iadd
    //   270: iload #5
    //   272: isub
    //   273: i2f
    //   274: fload #7
    //   276: fmul
    //   277: f2i
    //   278: invokevirtual setHeaderScroll : (I)V
    //   281: goto -> 215
    //   284: iload #10
    //   286: invokestatic abs : (I)I
    //   289: iload #6
    //   291: if_icmple -> 307
    //   294: iload #10
    //   296: iload #5
    //   298: isub
    //   299: invokestatic abs : (I)I
    //   302: iload #6
    //   304: if_icmpgt -> 215
    //   307: aload_0
    //   308: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.RESET : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   311: iconst_0
    //   312: newarray boolean
    //   314: invokevirtual setState : (Lapp/lib/pullToRefresh/PullToRefreshBase$State;[Z)V
    //   317: goto -> 215
    //   320: iload #8
    //   322: ifeq -> 215
    //   325: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.OVERSCROLLING : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   328: aload_0
    //   329: invokevirtual getState : ()Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   332: if_acmpne -> 215
    //   335: aload_0
    //   336: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.RESET : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   339: iconst_0
    //   340: newarray boolean
    //   342: invokevirtual setState : (Lapp/lib/pullToRefresh/PullToRefreshBase$State;[Z)V
    //   345: goto -> 215
  }
  
  public static void overScrollBy(PullToRefreshBase<?> paramPullToRefreshBase, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean) {
    overScrollBy(paramPullToRefreshBase, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, 0, 1.0F, paramBoolean);
  }
  
  public static void overScrollBy(PullToRefreshBase<?> paramPullToRefreshBase, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
    overScrollBy(paramPullToRefreshBase, paramInt1, paramInt2, paramInt3, paramInt4, 0, paramBoolean);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/OverscrollHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */