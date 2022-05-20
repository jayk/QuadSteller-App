package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.v4.os.BuildCompat;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.util.Pair;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflaterFactory {
  static final Interpolator ACCELERATE_CUBIC;
  
  static final Interpolator ACCELERATE_QUINT;
  
  static final int ANIM_DUR = 220;
  
  public static final int ANIM_STYLE_CLOSE_ENTER = 3;
  
  public static final int ANIM_STYLE_CLOSE_EXIT = 4;
  
  public static final int ANIM_STYLE_FADE_ENTER = 5;
  
  public static final int ANIM_STYLE_FADE_EXIT = 6;
  
  public static final int ANIM_STYLE_OPEN_ENTER = 1;
  
  public static final int ANIM_STYLE_OPEN_EXIT = 2;
  
  static boolean DEBUG = false;
  
  static final Interpolator DECELERATE_CUBIC;
  
  static final Interpolator DECELERATE_QUINT;
  
  static final boolean HONEYCOMB;
  
  static final String TAG = "FragmentManager";
  
  static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
  
  static final String TARGET_STATE_TAG = "android:target_state";
  
  static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
  
  static final String VIEW_STATE_TAG = "android:view_state";
  
  static Field sAnimationListenerField = null;
  
  ArrayList<Fragment> mActive;
  
  ArrayList<Fragment> mAdded;
  
  ArrayList<Integer> mAvailBackStackIndices;
  
  ArrayList<Integer> mAvailIndices;
  
  ArrayList<BackStackRecord> mBackStack;
  
  ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
  
  ArrayList<BackStackRecord> mBackStackIndices;
  
  FragmentContainer mContainer;
  
  ArrayList<Fragment> mCreatedMenus;
  
  int mCurState = 0;
  
  boolean mDestroyed;
  
  Runnable mExecCommit = new Runnable() {
      public void run() {
        FragmentManagerImpl.this.execPendingActions();
      }
    };
  
  boolean mExecutingActions;
  
  boolean mHavePendingDeferredStart;
  
  FragmentHostCallback mHost;
  
  private CopyOnWriteArrayList<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks;
  
  boolean mNeedMenuInvalidate;
  
  String mNoTransactionsBecause;
  
  Fragment mParent;
  
  ArrayList<OpGenerator> mPendingActions;
  
  ArrayList<StartEnterTransitionListener> mPostponedTransactions;
  
  SparseArray<Parcelable> mStateArray = null;
  
  Bundle mStateBundle = null;
  
  boolean mStateSaved;
  
  Runnable[] mTmpActions;
  
  ArrayList<Fragment> mTmpAddedFragments;
  
  ArrayList<Boolean> mTmpIsPop;
  
  ArrayList<BackStackRecord> mTmpRecords;
  
  static {
    DECELERATE_QUINT = (Interpolator)new DecelerateInterpolator(2.5F);
    DECELERATE_CUBIC = (Interpolator)new DecelerateInterpolator(1.5F);
    ACCELERATE_QUINT = (Interpolator)new AccelerateInterpolator(2.5F);
    ACCELERATE_CUBIC = (Interpolator)new AccelerateInterpolator(1.5F);
  }
  
  private void checkStateLoss() {
    if (this.mStateSaved)
      throw new IllegalStateException("Can not perform this action after onSaveInstanceState"); 
    if (this.mNoTransactionsBecause != null)
      throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause); 
  }
  
  private void cleanupExec() {
    this.mExecutingActions = false;
    this.mTmpIsPop.clear();
    this.mTmpRecords.clear();
  }
  
  private void completeExecute(BackStackRecord paramBackStackRecord, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    ArrayList<BackStackRecord> arrayList = new ArrayList(1);
    ArrayList<Boolean> arrayList1 = new ArrayList(1);
    arrayList.add(paramBackStackRecord);
    arrayList1.add(Boolean.valueOf(paramBoolean1));
    executeOps(arrayList, arrayList1, 0, 1);
    if (paramBoolean2)
      FragmentTransition.startTransitions(this, arrayList, arrayList1, 0, 1, true); 
    if (paramBoolean3) {
      moveToState(this.mCurState, true);
      return;
    } 
    if (this.mActive != null) {
      int i = this.mActive.size();
      byte b = 0;
      while (true) {
        if (b < i) {
          Fragment fragment = this.mActive.get(b);
          if (fragment.mView != null && fragment.mIsNewlyAdded && paramBackStackRecord.interactsWith(fragment.mContainerId))
            fragment.mIsNewlyAdded = false; 
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private void endAnimatingAwayFragments() {
    int i;
    if (this.mActive == null) {
      i = 0;
    } else {
      i = this.mActive.size();
    } 
    for (byte b = 0; b < i; b++) {
      Fragment fragment = this.mActive.get(b);
      if (fragment != null && fragment.getAnimatingAway() != null) {
        int j = fragment.getStateAfterAnimating();
        View view = fragment.getAnimatingAway();
        fragment.setAnimatingAway(null);
        view.clearAnimation();
        moveToState(fragment, j, 0, 0, false);
      } 
    } 
  }
  
  private void ensureExecReady(boolean paramBoolean) {
    if (this.mExecutingActions)
      throw new IllegalStateException("FragmentManager is already executing transactions"); 
    if (Looper.myLooper() != this.mHost.getHandler().getLooper())
      throw new IllegalStateException("Must be called from main thread of fragment host"); 
    if (!paramBoolean)
      checkStateLoss(); 
    if (this.mTmpRecords == null) {
      this.mTmpRecords = new ArrayList<BackStackRecord>();
      this.mTmpIsPop = new ArrayList<Boolean>();
    } 
    executePostponedTransaction(null, null);
  }
  
  private static void executeOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2) {
    while (paramInt1 < paramInt2) {
      BackStackRecord backStackRecord = paramArrayList.get(paramInt1);
      if (((Boolean)paramArrayList1.get(paramInt1)).booleanValue()) {
        backStackRecord.executePopOps();
      } else {
        backStackRecord.executeOps();
      } 
      paramInt1++;
    } 
  }
  
  private void executeOpsTogether(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2) {
    boolean bool = ((BackStackRecord)paramArrayList.get(paramInt1)).mAllowOptimization;
    boolean bool1 = false;
    if (this.mTmpAddedFragments == null) {
      this.mTmpAddedFragments = new ArrayList<Fragment>();
    } else {
      this.mTmpAddedFragments.clear();
    } 
    if (this.mAdded != null)
      this.mTmpAddedFragments.addAll(this.mAdded); 
    int i;
    for (i = paramInt1; i < paramInt2; i++) {
      boolean bool3;
      BackStackRecord backStackRecord = paramArrayList.get(i);
      boolean bool2 = ((Boolean)paramArrayList1.get(i)).booleanValue();
      if (!bool2)
        backStackRecord.expandReplaceOps(this.mTmpAddedFragments); 
      if (bool2) {
        bool3 = true;
      } else {
        bool3 = true;
      } 
      backStackRecord.bumpBackStackNesting(bool3);
      if (bool1 || backStackRecord.mAddToBackStack) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
    } 
    this.mTmpAddedFragments.clear();
    if (!bool)
      FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, paramInt2, false); 
    executeOps(paramArrayList, paramArrayList1, paramInt1, paramInt2);
    i = paramInt2;
    if (bool) {
      moveFragmentsToInvisible();
      i = postponePostponableTransactions(paramArrayList, paramArrayList1, paramInt1, paramInt2);
    } 
    if (i != paramInt1 && bool) {
      FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, i, true);
      moveToState(this.mCurState, true);
    } 
    while (paramInt1 < paramInt2) {
      BackStackRecord backStackRecord = paramArrayList.get(paramInt1);
      if (((Boolean)paramArrayList1.get(paramInt1)).booleanValue() && backStackRecord.mIndex >= 0) {
        freeBackStackIndex(backStackRecord.mIndex);
        backStackRecord.mIndex = -1;
      } 
      paramInt1++;
    } 
    if (bool1)
      reportBackStackChanged(); 
  }
  
  private void executePostponedTransaction(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   4: ifnonnull -> 105
    //   7: iconst_0
    //   8: istore_3
    //   9: iconst_0
    //   10: istore #4
    //   12: iload_3
    //   13: istore #5
    //   15: iload #4
    //   17: istore_3
    //   18: iload_3
    //   19: iload #5
    //   21: if_icmpge -> 236
    //   24: aload_0
    //   25: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   28: iload_3
    //   29: invokevirtual get : (I)Ljava/lang/Object;
    //   32: checkcast android/support/v4/app/FragmentManagerImpl$StartEnterTransitionListener
    //   35: astore #6
    //   37: aload_1
    //   38: ifnull -> 116
    //   41: aload #6
    //   43: invokestatic access$000 : (Landroid/support/v4/app/FragmentManagerImpl$StartEnterTransitionListener;)Z
    //   46: ifne -> 116
    //   49: aload_1
    //   50: aload #6
    //   52: invokestatic access$100 : (Landroid/support/v4/app/FragmentManagerImpl$StartEnterTransitionListener;)Landroid/support/v4/app/BackStackRecord;
    //   55: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   58: istore #4
    //   60: iload #4
    //   62: iconst_m1
    //   63: if_icmpeq -> 116
    //   66: aload_2
    //   67: iload #4
    //   69: invokevirtual get : (I)Ljava/lang/Object;
    //   72: checkcast java/lang/Boolean
    //   75: invokevirtual booleanValue : ()Z
    //   78: ifeq -> 116
    //   81: aload #6
    //   83: invokevirtual cancelTransaction : ()V
    //   86: iload #5
    //   88: istore #4
    //   90: iload_3
    //   91: istore #7
    //   93: iload #7
    //   95: iconst_1
    //   96: iadd
    //   97: istore_3
    //   98: iload #4
    //   100: istore #5
    //   102: goto -> 18
    //   105: aload_0
    //   106: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   109: invokevirtual size : ()I
    //   112: istore_3
    //   113: goto -> 9
    //   116: aload #6
    //   118: invokevirtual isReady : ()Z
    //   121: ifne -> 159
    //   124: iload_3
    //   125: istore #7
    //   127: iload #5
    //   129: istore #4
    //   131: aload_1
    //   132: ifnull -> 93
    //   135: iload_3
    //   136: istore #7
    //   138: iload #5
    //   140: istore #4
    //   142: aload #6
    //   144: invokestatic access$100 : (Landroid/support/v4/app/FragmentManagerImpl$StartEnterTransitionListener;)Landroid/support/v4/app/BackStackRecord;
    //   147: aload_1
    //   148: iconst_0
    //   149: aload_1
    //   150: invokevirtual size : ()I
    //   153: invokevirtual interactsWith : (Ljava/util/ArrayList;II)Z
    //   156: ifeq -> 93
    //   159: aload_0
    //   160: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   163: iload_3
    //   164: invokevirtual remove : (I)Ljava/lang/Object;
    //   167: pop
    //   168: iload_3
    //   169: iconst_1
    //   170: isub
    //   171: istore #7
    //   173: iload #5
    //   175: iconst_1
    //   176: isub
    //   177: istore #4
    //   179: aload_1
    //   180: ifnull -> 228
    //   183: aload #6
    //   185: invokestatic access$000 : (Landroid/support/v4/app/FragmentManagerImpl$StartEnterTransitionListener;)Z
    //   188: ifne -> 228
    //   191: aload_1
    //   192: aload #6
    //   194: invokestatic access$100 : (Landroid/support/v4/app/FragmentManagerImpl$StartEnterTransitionListener;)Landroid/support/v4/app/BackStackRecord;
    //   197: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   200: istore_3
    //   201: iload_3
    //   202: iconst_m1
    //   203: if_icmpeq -> 228
    //   206: aload_2
    //   207: iload_3
    //   208: invokevirtual get : (I)Ljava/lang/Object;
    //   211: checkcast java/lang/Boolean
    //   214: invokevirtual booleanValue : ()Z
    //   217: ifeq -> 228
    //   220: aload #6
    //   222: invokevirtual cancelTransaction : ()V
    //   225: goto -> 93
    //   228: aload #6
    //   230: invokevirtual completeTransaction : ()V
    //   233: goto -> 93
    //   236: return
  }
  
  private Fragment findFragmentUnder(Fragment paramFragment) {
    // Byte code:
    //   0: aload_1
    //   1: getfield mContainer : Landroid/view/ViewGroup;
    //   4: astore_2
    //   5: aload_1
    //   6: getfield mView : Landroid/view/View;
    //   9: astore_3
    //   10: aload_2
    //   11: ifnull -> 18
    //   14: aload_3
    //   15: ifnonnull -> 22
    //   18: aconst_null
    //   19: astore_1
    //   20: aload_1
    //   21: areturn
    //   22: aload_0
    //   23: getfield mAdded : Ljava/util/ArrayList;
    //   26: aload_1
    //   27: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   30: iconst_1
    //   31: isub
    //   32: istore #4
    //   34: iload #4
    //   36: iflt -> 75
    //   39: aload_0
    //   40: getfield mAdded : Ljava/util/ArrayList;
    //   43: iload #4
    //   45: invokevirtual get : (I)Ljava/lang/Object;
    //   48: checkcast android/support/v4/app/Fragment
    //   51: astore_3
    //   52: aload_3
    //   53: getfield mContainer : Landroid/view/ViewGroup;
    //   56: aload_2
    //   57: if_acmpne -> 69
    //   60: aload_3
    //   61: astore_1
    //   62: aload_3
    //   63: getfield mView : Landroid/view/View;
    //   66: ifnonnull -> 20
    //   69: iinc #4, -1
    //   72: goto -> 34
    //   75: aconst_null
    //   76: astore_1
    //   77: goto -> 20
  }
  
  private void forcePostponedTransactions() {
    if (this.mPostponedTransactions != null)
      while (!this.mPostponedTransactions.isEmpty())
        ((StartEnterTransitionListener)this.mPostponedTransactions.remove(0)).completeTransaction();  
  }
  
  private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mPendingActions : Ljava/util/ArrayList;
    //   6: ifnull -> 19
    //   9: aload_0
    //   10: getfield mPendingActions : Ljava/util/ArrayList;
    //   13: invokevirtual size : ()I
    //   16: ifne -> 25
    //   19: aload_0
    //   20: monitorexit
    //   21: iconst_0
    //   22: istore_3
    //   23: iload_3
    //   24: ireturn
    //   25: aload_0
    //   26: getfield mPendingActions : Ljava/util/ArrayList;
    //   29: invokevirtual size : ()I
    //   32: istore #4
    //   34: iconst_0
    //   35: istore #5
    //   37: iload #5
    //   39: iload #4
    //   41: if_icmpge -> 70
    //   44: aload_0
    //   45: getfield mPendingActions : Ljava/util/ArrayList;
    //   48: iload #5
    //   50: invokevirtual get : (I)Ljava/lang/Object;
    //   53: checkcast android/support/v4/app/FragmentManagerImpl$OpGenerator
    //   56: aload_1
    //   57: aload_2
    //   58: invokeinterface generateOps : (Ljava/util/ArrayList;Ljava/util/ArrayList;)Z
    //   63: pop
    //   64: iinc #5, 1
    //   67: goto -> 37
    //   70: aload_0
    //   71: getfield mPendingActions : Ljava/util/ArrayList;
    //   74: invokevirtual clear : ()V
    //   77: aload_0
    //   78: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   81: invokevirtual getHandler : ()Landroid/os/Handler;
    //   84: aload_0
    //   85: getfield mExecCommit : Ljava/lang/Runnable;
    //   88: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   91: aload_0
    //   92: monitorexit
    //   93: iload #4
    //   95: ifle -> 108
    //   98: iconst_1
    //   99: istore_3
    //   100: goto -> 23
    //   103: astore_1
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_1
    //   107: athrow
    //   108: iconst_0
    //   109: istore_3
    //   110: goto -> 23
    // Exception table:
    //   from	to	target	type
    //   2	19	103	finally
    //   19	21	103	finally
    //   25	34	103	finally
    //   44	64	103	finally
    //   70	93	103	finally
    //   104	106	103	finally
  }
  
  static Animation makeFadeAnimation(Context paramContext, float paramFloat1, float paramFloat2) {
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat1, paramFloat2);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    return (Animation)alphaAnimation;
  }
  
  static Animation makeOpenCloseAnimation(Context paramContext, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    AnimationSet animationSet = new AnimationSet(false);
    ScaleAnimation scaleAnimation = new ScaleAnimation(paramFloat1, paramFloat2, paramFloat1, paramFloat2, 1, 0.5F, 1, 0.5F);
    scaleAnimation.setInterpolator(DECELERATE_QUINT);
    scaleAnimation.setDuration(220L);
    animationSet.addAnimation((Animation)scaleAnimation);
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat3, paramFloat4);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    animationSet.addAnimation((Animation)alphaAnimation);
    return (Animation)animationSet;
  }
  
  static boolean modifiesAlpha(Animation paramAnimation) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_1
    //   2: aload_0
    //   3: instanceof android/view/animation/AlphaAnimation
    //   6: ifeq -> 13
    //   9: iload_1
    //   10: istore_2
    //   11: iload_2
    //   12: ireturn
    //   13: aload_0
    //   14: instanceof android/view/animation/AnimationSet
    //   17: ifeq -> 61
    //   20: aload_0
    //   21: checkcast android/view/animation/AnimationSet
    //   24: invokevirtual getAnimations : ()Ljava/util/List;
    //   27: astore_0
    //   28: iconst_0
    //   29: istore_3
    //   30: iload_3
    //   31: aload_0
    //   32: invokeinterface size : ()I
    //   37: if_icmpge -> 61
    //   40: iload_1
    //   41: istore_2
    //   42: aload_0
    //   43: iload_3
    //   44: invokeinterface get : (I)Ljava/lang/Object;
    //   49: instanceof android/view/animation/AlphaAnimation
    //   52: ifne -> 11
    //   55: iinc #3, 1
    //   58: goto -> 30
    //   61: iconst_0
    //   62: istore_2
    //   63: goto -> 11
  }
  
  private void moveFragmentsToInvisible() {
    if (this.mCurState >= 1) {
      int j;
      int i = Math.min(this.mCurState, 4);
      if (this.mAdded == null) {
        j = 0;
      } else {
        j = this.mAdded.size();
      } 
      byte b = 0;
      while (true) {
        if (b < j) {
          Fragment fragment = this.mAdded.get(b);
          if (fragment.mState < i) {
            moveToState(fragment, i, fragment.getNextAnim(), fragment.getNextTransition(), false);
            if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded)
              fragment.mView.setVisibility(4); 
          } 
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private void optimizeAndExecuteOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    if (paramArrayList != null && !paramArrayList.isEmpty()) {
      if (paramArrayList1 == null || paramArrayList.size() != paramArrayList1.size())
        throw new IllegalStateException("Internal error with the back stack records"); 
      executePostponedTransaction(paramArrayList, paramArrayList1);
      int i = paramArrayList.size();
      int j = 0;
      int k = 0;
      label26: while (k < i) {
        int m = k;
        int n = j;
        if (!((BackStackRecord)paramArrayList.get(k)).mAllowOptimization) {
          if (j != k)
            executeOpsTogether(paramArrayList, paramArrayList1, j, k); 
          for (n = k + 1;; n++) {
            if (n >= i || ((BackStackRecord)paramArrayList.get(n)).mAllowOptimization) {
              executeOpsTogether(paramArrayList, paramArrayList1, k, n);
              k = n;
              m = n - 1;
              n = k;
              k = m + 1;
              j = n;
              continue label26;
            } 
          } 
        } 
        continue;
      } 
      if (j != i)
        executeOpsTogether(paramArrayList, paramArrayList1, j, i); 
    } 
  }
  
  private boolean popBackStackImmediate(String paramString, int paramInt1, int paramInt2) {
    execPendingActions();
    ensureExecReady(true);
    boolean bool = popBackStackState(this.mTmpRecords, this.mTmpIsPop, paramString, paramInt1, paramInt2);
    if (bool) {
      this.mExecutingActions = true;
      try {
        optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
        cleanupExec();
        return bool;
      } finally {
        cleanupExec();
      } 
    } 
    doPendingDeferredStart();
    return bool;
  }
  
  private int postponePostponableTransactions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2) {
    int i = paramInt2;
    int j = paramInt2 - 1;
    while (j >= paramInt1) {
      boolean bool1;
      BackStackRecord backStackRecord = paramArrayList.get(j);
      boolean bool = ((Boolean)paramArrayList1.get(j)).booleanValue();
      if (backStackRecord.isPostponed() && !backStackRecord.interactsWith(paramArrayList, j + 1, paramInt2)) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      int k = i;
      if (bool1) {
        if (this.mPostponedTransactions == null)
          this.mPostponedTransactions = new ArrayList<StartEnterTransitionListener>(); 
        StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, bool);
        this.mPostponedTransactions.add(startEnterTransitionListener);
        backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
        if (bool) {
          backStackRecord.executeOps();
        } else {
          backStackRecord.executePopOps();
        } 
        k = i - 1;
        if (j != k) {
          paramArrayList.remove(j);
          paramArrayList.add(k, backStackRecord);
        } 
        moveFragmentsToInvisible();
      } 
      j--;
      i = k;
    } 
    return i;
  }
  
  public static int reverseTransit(int paramInt) {
    boolean bool = false;
    switch (paramInt) {
      default:
        return bool;
      case 4097:
        return 8194;
      case 8194:
        return 4097;
      case 4099:
        break;
    } 
    return 4099;
  }
  
  private void scheduleCommit() {
    // Byte code:
    //   0: iconst_1
    //   1: istore_1
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   8: ifnull -> 81
    //   11: aload_0
    //   12: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   15: invokevirtual isEmpty : ()Z
    //   18: ifne -> 81
    //   21: iconst_1
    //   22: istore_2
    //   23: aload_0
    //   24: getfield mPendingActions : Ljava/util/ArrayList;
    //   27: ifnull -> 86
    //   30: aload_0
    //   31: getfield mPendingActions : Ljava/util/ArrayList;
    //   34: invokevirtual size : ()I
    //   37: iconst_1
    //   38: if_icmpne -> 86
    //   41: iload_2
    //   42: ifne -> 49
    //   45: iload_1
    //   46: ifeq -> 78
    //   49: aload_0
    //   50: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   53: invokevirtual getHandler : ()Landroid/os/Handler;
    //   56: aload_0
    //   57: getfield mExecCommit : Ljava/lang/Runnable;
    //   60: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   63: aload_0
    //   64: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   67: invokevirtual getHandler : ()Landroid/os/Handler;
    //   70: aload_0
    //   71: getfield mExecCommit : Ljava/lang/Runnable;
    //   74: invokevirtual post : (Ljava/lang/Runnable;)Z
    //   77: pop
    //   78: aload_0
    //   79: monitorexit
    //   80: return
    //   81: iconst_0
    //   82: istore_2
    //   83: goto -> 23
    //   86: iconst_0
    //   87: istore_1
    //   88: goto -> 41
    //   91: astore_3
    //   92: aload_0
    //   93: monitorexit
    //   94: aload_3
    //   95: athrow
    // Exception table:
    //   from	to	target	type
    //   4	21	91	finally
    //   23	41	91	finally
    //   49	78	91	finally
    //   78	80	91	finally
    //   92	94	91	finally
  }
  
  private void setHWLayerAnimListenerIfAlpha(View paramView, Animation paramAnimation) {
    if (paramView != null && paramAnimation != null && shouldRunOnHWLayer(paramView, paramAnimation)) {
      Animation.AnimationListener animationListener = null;
      try {
        if (sAnimationListenerField == null) {
          sAnimationListenerField = Animation.class.getDeclaredField("mListener");
          sAnimationListenerField.setAccessible(true);
        } 
        Animation.AnimationListener animationListener1 = (Animation.AnimationListener)sAnimationListenerField.get(paramAnimation);
        animationListener = animationListener1;
      } catch (NoSuchFieldException noSuchFieldException) {
        Log.e("FragmentManager", "No field with the name mListener is found in Animation class", noSuchFieldException);
      } catch (IllegalAccessException illegalAccessException) {
        Log.e("FragmentManager", "Cannot access Animation's mListener field", illegalAccessException);
      } 
      ViewCompat.setLayerType(paramView, 2, null);
      paramAnimation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(paramView, paramAnimation, animationListener));
    } 
  }
  
  static boolean shouldRunOnHWLayer(View paramView, Animation paramAnimation) {
    return (Build.VERSION.SDK_INT >= 19 && ViewCompat.getLayerType(paramView) == 0 && ViewCompat.hasOverlappingRendering(paramView) && modifiesAlpha(paramAnimation));
  }
  
  private void throwException(RuntimeException paramRuntimeException) {
    Log.e("FragmentManager", paramRuntimeException.getMessage());
    Log.e("FragmentManager", "Activity state:");
    PrintWriter printWriter = new PrintWriter((Writer)new LogWriter("FragmentManager"));
    if (this.mHost != null) {
      try {
        this.mHost.onDump("  ", null, printWriter, new String[0]);
      } catch (Exception exception) {
        Log.e("FragmentManager", "Failed dumping state", exception);
      } 
      throw paramRuntimeException;
    } 
    try {
      dump("  ", null, (PrintWriter)exception, new String[0]);
    } catch (Exception exception1) {
      Log.e("FragmentManager", "Failed dumping state", exception1);
    } 
    throw paramRuntimeException;
  }
  
  public static int transitToStyleIndex(int paramInt, boolean paramBoolean) {
    byte b = -1;
    switch (paramInt) {
      default:
        return b;
      case 4097:
        return paramBoolean ? 1 : 2;
      case 8194:
        return paramBoolean ? 3 : 4;
      case 4099:
        break;
    } 
    return paramBoolean ? 5 : 6;
  }
  
  void addBackStackState(BackStackRecord paramBackStackRecord) {
    if (this.mBackStack == null)
      this.mBackStack = new ArrayList<BackStackRecord>(); 
    this.mBackStack.add(paramBackStackRecord);
    reportBackStackChanged();
  }
  
  public void addFragment(Fragment paramFragment, boolean paramBoolean) {
    if (this.mAdded == null)
      this.mAdded = new ArrayList<Fragment>(); 
    if (DEBUG)
      Log.v("FragmentManager", "add: " + paramFragment); 
    makeActive(paramFragment);
    if (!paramFragment.mDetached) {
      if (this.mAdded.contains(paramFragment))
        throw new IllegalStateException("Fragment already added: " + paramFragment); 
      this.mAdded.add(paramFragment);
      paramFragment.mAdded = true;
      paramFragment.mRemoving = false;
      if (paramFragment.mView == null)
        paramFragment.mHiddenChanged = false; 
      if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
        this.mNeedMenuInvalidate = true; 
      if (paramBoolean)
        moveToState(paramFragment); 
    } 
  }
  
  public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    if (this.mBackStackChangeListeners == null)
      this.mBackStackChangeListeners = new ArrayList<FragmentManager.OnBackStackChangedListener>(); 
    this.mBackStackChangeListeners.add(paramOnBackStackChangedListener);
  }
  
  public int allocBackStackIndex(BackStackRecord paramBackStackRecord) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnull -> 19
    //   9: aload_0
    //   10: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   13: invokevirtual size : ()I
    //   16: ifgt -> 104
    //   19: aload_0
    //   20: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   23: ifnonnull -> 39
    //   26: new java/util/ArrayList
    //   29: astore_2
    //   30: aload_2
    //   31: invokespecial <init> : ()V
    //   34: aload_0
    //   35: aload_2
    //   36: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   39: aload_0
    //   40: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   43: invokevirtual size : ()I
    //   46: istore_3
    //   47: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   50: ifeq -> 91
    //   53: new java/lang/StringBuilder
    //   56: astore_2
    //   57: aload_2
    //   58: invokespecial <init> : ()V
    //   61: ldc 'FragmentManager'
    //   63: aload_2
    //   64: ldc_w 'Setting back stack index '
    //   67: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   70: iload_3
    //   71: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   74: ldc_w ' to '
    //   77: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: aload_1
    //   81: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   84: invokevirtual toString : ()Ljava/lang/String;
    //   87: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   90: pop
    //   91: aload_0
    //   92: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   95: aload_1
    //   96: invokevirtual add : (Ljava/lang/Object;)Z
    //   99: pop
    //   100: aload_0
    //   101: monitorexit
    //   102: iload_3
    //   103: ireturn
    //   104: aload_0
    //   105: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   108: aload_0
    //   109: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   112: invokevirtual size : ()I
    //   115: iconst_1
    //   116: isub
    //   117: invokevirtual remove : (I)Ljava/lang/Object;
    //   120: checkcast java/lang/Integer
    //   123: invokevirtual intValue : ()I
    //   126: istore_3
    //   127: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   130: ifeq -> 171
    //   133: new java/lang/StringBuilder
    //   136: astore_2
    //   137: aload_2
    //   138: invokespecial <init> : ()V
    //   141: ldc 'FragmentManager'
    //   143: aload_2
    //   144: ldc_w 'Adding back stack index '
    //   147: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: iload_3
    //   151: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   154: ldc_w ' with '
    //   157: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: aload_1
    //   161: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   164: invokevirtual toString : ()Ljava/lang/String;
    //   167: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   170: pop
    //   171: aload_0
    //   172: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   175: iload_3
    //   176: aload_1
    //   177: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   180: pop
    //   181: aload_0
    //   182: monitorexit
    //   183: goto -> 102
    //   186: astore_1
    //   187: aload_0
    //   188: monitorexit
    //   189: aload_1
    //   190: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	186	finally
    //   19	39	186	finally
    //   39	91	186	finally
    //   91	102	186	finally
    //   104	171	186	finally
    //   171	183	186	finally
    //   187	189	186	finally
  }
  
  public void attachController(FragmentHostCallback paramFragmentHostCallback, FragmentContainer paramFragmentContainer, Fragment paramFragment) {
    if (this.mHost != null)
      throw new IllegalStateException("Already attached"); 
    this.mHost = paramFragmentHostCallback;
    this.mContainer = paramFragmentContainer;
    this.mParent = paramFragment;
  }
  
  public void attachFragment(Fragment paramFragment) {
    if (DEBUG)
      Log.v("FragmentManager", "attach: " + paramFragment); 
    if (paramFragment.mDetached) {
      paramFragment.mDetached = false;
      if (!paramFragment.mAdded) {
        if (this.mAdded == null)
          this.mAdded = new ArrayList<Fragment>(); 
        if (this.mAdded.contains(paramFragment))
          throw new IllegalStateException("Fragment already added: " + paramFragment); 
        if (DEBUG)
          Log.v("FragmentManager", "add from attach: " + paramFragment); 
        this.mAdded.add(paramFragment);
        paramFragment.mAdded = true;
        if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
          this.mNeedMenuInvalidate = true; 
      } 
    } 
  }
  
  public FragmentTransaction beginTransaction() {
    return new BackStackRecord(this);
  }
  
  void completeShowHideFragment(Fragment paramFragment) {
    if (paramFragment.mView != null) {
      boolean bool;
      int i = paramFragment.getNextTransition();
      if (!paramFragment.mHidden) {
        bool = true;
      } else {
        bool = false;
      } 
      Animation animation = loadAnimation(paramFragment, i, bool, paramFragment.getNextTransitionStyle());
      if (animation != null) {
        setHWLayerAnimListenerIfAlpha(paramFragment.mView, animation);
        paramFragment.mView.startAnimation(animation);
        setHWLayerAnimListenerIfAlpha(paramFragment.mView, animation);
        animation.start();
      } 
      if (paramFragment.mHidden && !paramFragment.isHideReplaced()) {
        i = 8;
      } else {
        i = 0;
      } 
      paramFragment.mView.setVisibility(i);
      if (paramFragment.isHideReplaced())
        paramFragment.setHideReplaced(false); 
    } 
    if (paramFragment.mAdded && paramFragment.mHasMenu && paramFragment.mMenuVisible)
      this.mNeedMenuInvalidate = true; 
    paramFragment.mHiddenChanged = false;
    paramFragment.onHiddenChanged(paramFragment.mHidden);
  }
  
  public void detachFragment(Fragment paramFragment) {
    if (DEBUG)
      Log.v("FragmentManager", "detach: " + paramFragment); 
    if (!paramFragment.mDetached) {
      paramFragment.mDetached = true;
      if (paramFragment.mAdded) {
        if (this.mAdded != null) {
          if (DEBUG)
            Log.v("FragmentManager", "remove from detach: " + paramFragment); 
          this.mAdded.remove(paramFragment);
        } 
        if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
          this.mNeedMenuInvalidate = true; 
        paramFragment.mAdded = false;
      } 
    } 
  }
  
  public void dispatchActivityCreated() {
    this.mStateSaved = false;
    moveToState(2, false);
  }
  
  public void dispatchConfigurationChanged(Configuration paramConfiguration) {
    if (this.mAdded != null)
      for (byte b = 0; b < this.mAdded.size(); b++) {
        Fragment fragment = this.mAdded.get(b);
        if (fragment != null)
          fragment.performConfigurationChanged(paramConfiguration); 
      }  
  }
  
  public boolean dispatchContextItemSelected(MenuItem paramMenuItem) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mAdded : Ljava/util/ArrayList;
    //   4: ifnull -> 56
    //   7: iconst_0
    //   8: istore_2
    //   9: iload_2
    //   10: aload_0
    //   11: getfield mAdded : Ljava/util/ArrayList;
    //   14: invokevirtual size : ()I
    //   17: if_icmpge -> 56
    //   20: aload_0
    //   21: getfield mAdded : Ljava/util/ArrayList;
    //   24: iload_2
    //   25: invokevirtual get : (I)Ljava/lang/Object;
    //   28: checkcast android/support/v4/app/Fragment
    //   31: astore_3
    //   32: aload_3
    //   33: ifnull -> 50
    //   36: aload_3
    //   37: aload_1
    //   38: invokevirtual performContextItemSelected : (Landroid/view/MenuItem;)Z
    //   41: ifeq -> 50
    //   44: iconst_1
    //   45: istore #4
    //   47: iload #4
    //   49: ireturn
    //   50: iinc #2, 1
    //   53: goto -> 9
    //   56: iconst_0
    //   57: istore #4
    //   59: goto -> 47
  }
  
  public void dispatchCreate() {
    this.mStateSaved = false;
    moveToState(1, false);
  }
  
  public boolean dispatchCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {
    boolean bool1 = false;
    boolean bool2 = false;
    ArrayList<Fragment> arrayList1 = null;
    ArrayList<Fragment> arrayList2 = null;
    if (this.mAdded != null) {
      byte b = 0;
      while (true) {
        arrayList1 = arrayList2;
        bool1 = bool2;
        if (b < this.mAdded.size()) {
          Fragment fragment = this.mAdded.get(b);
          arrayList1 = arrayList2;
          bool1 = bool2;
          if (fragment != null) {
            arrayList1 = arrayList2;
            bool1 = bool2;
            if (fragment.performCreateOptionsMenu(paramMenu, paramMenuInflater)) {
              bool1 = true;
              arrayList1 = arrayList2;
              if (arrayList2 == null)
                arrayList1 = new ArrayList(); 
              arrayList1.add(fragment);
            } 
          } 
          b++;
          arrayList2 = arrayList1;
          bool2 = bool1;
          continue;
        } 
        break;
      } 
    } 
    if (this.mCreatedMenus != null)
      for (byte b = 0; b < this.mCreatedMenus.size(); b++) {
        Fragment fragment = this.mCreatedMenus.get(b);
        if (arrayList1 == null || !arrayList1.contains(fragment))
          fragment.onDestroyOptionsMenu(); 
      }  
    this.mCreatedMenus = arrayList1;
    return bool1;
  }
  
  public void dispatchDestroy() {
    this.mDestroyed = true;
    execPendingActions();
    moveToState(0, false);
    this.mHost = null;
    this.mContainer = null;
    this.mParent = null;
  }
  
  public void dispatchDestroyView() {
    moveToState(1, false);
  }
  
  public void dispatchLowMemory() {
    if (this.mAdded != null)
      for (byte b = 0; b < this.mAdded.size(); b++) {
        Fragment fragment = this.mAdded.get(b);
        if (fragment != null)
          fragment.performLowMemory(); 
      }  
  }
  
  public void dispatchMultiWindowModeChanged(boolean paramBoolean) {
    if (this.mAdded != null) {
      int i = this.mAdded.size() - 1;
      while (true) {
        if (i >= 0) {
          Fragment fragment = this.mAdded.get(i);
          if (fragment != null)
            fragment.performMultiWindowModeChanged(paramBoolean); 
          i--;
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentActivityCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentActivityCreated(paramFragment, paramBundle, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentActivityCreated(this, paramFragment, paramBundle); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentAttached(paramFragment, paramContext, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentAttached(this, paramFragment, paramContext); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentCreated(paramFragment, paramBundle, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentCreated(this, paramFragment, paramBundle); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentDestroyed(Fragment paramFragment, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDestroyed(paramFragment, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentDestroyed(this, paramFragment); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentDetached(Fragment paramFragment, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDetached(paramFragment, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentDetached(this, paramFragment); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentPaused(Fragment paramFragment, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPaused(paramFragment, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentPaused(this, paramFragment); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentPreAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreAttached(paramFragment, paramContext, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentPreAttached(this, paramFragment, paramContext); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentResumed(Fragment paramFragment, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentResumed(paramFragment, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentResumed(this, paramFragment); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentSaveInstanceState(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentSaveInstanceState(paramFragment, paramBundle, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentSaveInstanceState(this, paramFragment, paramBundle); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentStarted(Fragment paramFragment, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStarted(paramFragment, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentStarted(this, paramFragment); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentStopped(Fragment paramFragment, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStopped(paramFragment, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentStopped(this, paramFragment); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentViewCreated(Fragment paramFragment, View paramView, Bundle paramBundle, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewCreated(paramFragment, paramView, paramBundle, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentViewCreated(this, paramFragment, paramView, paramBundle); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  void dispatchOnFragmentViewDestroyed(Fragment paramFragment, boolean paramBoolean) {
    if (this.mParent != null) {
      FragmentManager fragmentManager = this.mParent.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewDestroyed(paramFragment, true); 
    } 
    if (this.mLifecycleCallbacks != null) {
      Iterator<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> iterator = this.mLifecycleCallbacks.iterator();
      while (true) {
        if (iterator.hasNext()) {
          Pair pair = iterator.next();
          if (!paramBoolean || ((Boolean)pair.second).booleanValue())
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentViewDestroyed(this, paramFragment); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  public boolean dispatchOptionsItemSelected(MenuItem paramMenuItem) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mAdded : Ljava/util/ArrayList;
    //   4: ifnull -> 56
    //   7: iconst_0
    //   8: istore_2
    //   9: iload_2
    //   10: aload_0
    //   11: getfield mAdded : Ljava/util/ArrayList;
    //   14: invokevirtual size : ()I
    //   17: if_icmpge -> 56
    //   20: aload_0
    //   21: getfield mAdded : Ljava/util/ArrayList;
    //   24: iload_2
    //   25: invokevirtual get : (I)Ljava/lang/Object;
    //   28: checkcast android/support/v4/app/Fragment
    //   31: astore_3
    //   32: aload_3
    //   33: ifnull -> 50
    //   36: aload_3
    //   37: aload_1
    //   38: invokevirtual performOptionsItemSelected : (Landroid/view/MenuItem;)Z
    //   41: ifeq -> 50
    //   44: iconst_1
    //   45: istore #4
    //   47: iload #4
    //   49: ireturn
    //   50: iinc #2, 1
    //   53: goto -> 9
    //   56: iconst_0
    //   57: istore #4
    //   59: goto -> 47
  }
  
  public void dispatchOptionsMenuClosed(Menu paramMenu) {
    if (this.mAdded != null)
      for (byte b = 0; b < this.mAdded.size(); b++) {
        Fragment fragment = this.mAdded.get(b);
        if (fragment != null)
          fragment.performOptionsMenuClosed(paramMenu); 
      }  
  }
  
  public void dispatchPause() {
    moveToState(4, false);
  }
  
  public void dispatchPictureInPictureModeChanged(boolean paramBoolean) {
    if (this.mAdded != null) {
      int i = this.mAdded.size() - 1;
      while (true) {
        if (i >= 0) {
          Fragment fragment = this.mAdded.get(i);
          if (fragment != null)
            fragment.performPictureInPictureModeChanged(paramBoolean); 
          i--;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public boolean dispatchPrepareOptionsMenu(Menu paramMenu) {
    boolean bool1 = false;
    boolean bool2 = false;
    if (this.mAdded != null) {
      byte b = 0;
      while (true) {
        bool1 = bool2;
        if (b < this.mAdded.size()) {
          Fragment fragment = this.mAdded.get(b);
          bool1 = bool2;
          if (fragment != null) {
            bool1 = bool2;
            if (fragment.performPrepareOptionsMenu(paramMenu))
              bool1 = true; 
          } 
          b++;
          bool2 = bool1;
          continue;
        } 
        break;
      } 
    } 
    return bool1;
  }
  
  public void dispatchReallyStop() {
    moveToState(2, false);
  }
  
  public void dispatchResume() {
    this.mStateSaved = false;
    moveToState(5, false);
  }
  
  public void dispatchStart() {
    this.mStateSaved = false;
    moveToState(4, false);
  }
  
  public void dispatchStop() {
    this.mStateSaved = true;
    moveToState(3, false);
  }
  
  void doPendingDeferredStart() {
    if (this.mHavePendingDeferredStart) {
      boolean bool = false;
      byte b = 0;
      while (b < this.mActive.size()) {
        Fragment fragment = this.mActive.get(b);
        boolean bool1 = bool;
        if (fragment != null) {
          bool1 = bool;
          if (fragment.mLoaderManager != null)
            bool1 = bool | fragment.mLoaderManager.hasRunningLoaders(); 
        } 
        b++;
        bool = bool1;
      } 
      if (!bool) {
        this.mHavePendingDeferredStart = false;
        startPendingDeferredFragments();
      } 
    } 
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: aload_1
    //   8: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   11: ldc_w '    '
    //   14: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   17: invokevirtual toString : ()Ljava/lang/String;
    //   20: astore #5
    //   22: aload_0
    //   23: getfield mActive : Ljava/util/ArrayList;
    //   26: ifnull -> 150
    //   29: aload_0
    //   30: getfield mActive : Ljava/util/ArrayList;
    //   33: invokevirtual size : ()I
    //   36: istore #6
    //   38: iload #6
    //   40: ifle -> 150
    //   43: aload_3
    //   44: aload_1
    //   45: invokevirtual print : (Ljava/lang/String;)V
    //   48: aload_3
    //   49: ldc_w 'Active Fragments in '
    //   52: invokevirtual print : (Ljava/lang/String;)V
    //   55: aload_3
    //   56: aload_0
    //   57: invokestatic identityHashCode : (Ljava/lang/Object;)I
    //   60: invokestatic toHexString : (I)Ljava/lang/String;
    //   63: invokevirtual print : (Ljava/lang/String;)V
    //   66: aload_3
    //   67: ldc_w ':'
    //   70: invokevirtual println : (Ljava/lang/String;)V
    //   73: iconst_0
    //   74: istore #7
    //   76: iload #7
    //   78: iload #6
    //   80: if_icmpge -> 150
    //   83: aload_0
    //   84: getfield mActive : Ljava/util/ArrayList;
    //   87: iload #7
    //   89: invokevirtual get : (I)Ljava/lang/Object;
    //   92: checkcast android/support/v4/app/Fragment
    //   95: astore #8
    //   97: aload_3
    //   98: aload_1
    //   99: invokevirtual print : (Ljava/lang/String;)V
    //   102: aload_3
    //   103: ldc_w '  #'
    //   106: invokevirtual print : (Ljava/lang/String;)V
    //   109: aload_3
    //   110: iload #7
    //   112: invokevirtual print : (I)V
    //   115: aload_3
    //   116: ldc_w ': '
    //   119: invokevirtual print : (Ljava/lang/String;)V
    //   122: aload_3
    //   123: aload #8
    //   125: invokevirtual println : (Ljava/lang/Object;)V
    //   128: aload #8
    //   130: ifnull -> 144
    //   133: aload #8
    //   135: aload #5
    //   137: aload_2
    //   138: aload_3
    //   139: aload #4
    //   141: invokevirtual dump : (Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V
    //   144: iinc #7, 1
    //   147: goto -> 76
    //   150: aload_0
    //   151: getfield mAdded : Ljava/util/ArrayList;
    //   154: ifnull -> 247
    //   157: aload_0
    //   158: getfield mAdded : Ljava/util/ArrayList;
    //   161: invokevirtual size : ()I
    //   164: istore #6
    //   166: iload #6
    //   168: ifle -> 247
    //   171: aload_3
    //   172: aload_1
    //   173: invokevirtual print : (Ljava/lang/String;)V
    //   176: aload_3
    //   177: ldc_w 'Added Fragments:'
    //   180: invokevirtual println : (Ljava/lang/String;)V
    //   183: iconst_0
    //   184: istore #7
    //   186: iload #7
    //   188: iload #6
    //   190: if_icmpge -> 247
    //   193: aload_0
    //   194: getfield mAdded : Ljava/util/ArrayList;
    //   197: iload #7
    //   199: invokevirtual get : (I)Ljava/lang/Object;
    //   202: checkcast android/support/v4/app/Fragment
    //   205: astore #8
    //   207: aload_3
    //   208: aload_1
    //   209: invokevirtual print : (Ljava/lang/String;)V
    //   212: aload_3
    //   213: ldc_w '  #'
    //   216: invokevirtual print : (Ljava/lang/String;)V
    //   219: aload_3
    //   220: iload #7
    //   222: invokevirtual print : (I)V
    //   225: aload_3
    //   226: ldc_w ': '
    //   229: invokevirtual print : (Ljava/lang/String;)V
    //   232: aload_3
    //   233: aload #8
    //   235: invokevirtual toString : ()Ljava/lang/String;
    //   238: invokevirtual println : (Ljava/lang/String;)V
    //   241: iinc #7, 1
    //   244: goto -> 186
    //   247: aload_0
    //   248: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   251: ifnull -> 344
    //   254: aload_0
    //   255: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   258: invokevirtual size : ()I
    //   261: istore #6
    //   263: iload #6
    //   265: ifle -> 344
    //   268: aload_3
    //   269: aload_1
    //   270: invokevirtual print : (Ljava/lang/String;)V
    //   273: aload_3
    //   274: ldc_w 'Fragments Created Menus:'
    //   277: invokevirtual println : (Ljava/lang/String;)V
    //   280: iconst_0
    //   281: istore #7
    //   283: iload #7
    //   285: iload #6
    //   287: if_icmpge -> 344
    //   290: aload_0
    //   291: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   294: iload #7
    //   296: invokevirtual get : (I)Ljava/lang/Object;
    //   299: checkcast android/support/v4/app/Fragment
    //   302: astore #8
    //   304: aload_3
    //   305: aload_1
    //   306: invokevirtual print : (Ljava/lang/String;)V
    //   309: aload_3
    //   310: ldc_w '  #'
    //   313: invokevirtual print : (Ljava/lang/String;)V
    //   316: aload_3
    //   317: iload #7
    //   319: invokevirtual print : (I)V
    //   322: aload_3
    //   323: ldc_w ': '
    //   326: invokevirtual print : (Ljava/lang/String;)V
    //   329: aload_3
    //   330: aload #8
    //   332: invokevirtual toString : ()Ljava/lang/String;
    //   335: invokevirtual println : (Ljava/lang/String;)V
    //   338: iinc #7, 1
    //   341: goto -> 283
    //   344: aload_0
    //   345: getfield mBackStack : Ljava/util/ArrayList;
    //   348: ifnull -> 452
    //   351: aload_0
    //   352: getfield mBackStack : Ljava/util/ArrayList;
    //   355: invokevirtual size : ()I
    //   358: istore #6
    //   360: iload #6
    //   362: ifle -> 452
    //   365: aload_3
    //   366: aload_1
    //   367: invokevirtual print : (Ljava/lang/String;)V
    //   370: aload_3
    //   371: ldc_w 'Back Stack:'
    //   374: invokevirtual println : (Ljava/lang/String;)V
    //   377: iconst_0
    //   378: istore #7
    //   380: iload #7
    //   382: iload #6
    //   384: if_icmpge -> 452
    //   387: aload_0
    //   388: getfield mBackStack : Ljava/util/ArrayList;
    //   391: iload #7
    //   393: invokevirtual get : (I)Ljava/lang/Object;
    //   396: checkcast android/support/v4/app/BackStackRecord
    //   399: astore #8
    //   401: aload_3
    //   402: aload_1
    //   403: invokevirtual print : (Ljava/lang/String;)V
    //   406: aload_3
    //   407: ldc_w '  #'
    //   410: invokevirtual print : (Ljava/lang/String;)V
    //   413: aload_3
    //   414: iload #7
    //   416: invokevirtual print : (I)V
    //   419: aload_3
    //   420: ldc_w ': '
    //   423: invokevirtual print : (Ljava/lang/String;)V
    //   426: aload_3
    //   427: aload #8
    //   429: invokevirtual toString : ()Ljava/lang/String;
    //   432: invokevirtual println : (Ljava/lang/String;)V
    //   435: aload #8
    //   437: aload #5
    //   439: aload_2
    //   440: aload_3
    //   441: aload #4
    //   443: invokevirtual dump : (Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V
    //   446: iinc #7, 1
    //   449: goto -> 380
    //   452: aload_0
    //   453: monitorenter
    //   454: aload_0
    //   455: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   458: ifnull -> 546
    //   461: aload_0
    //   462: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   465: invokevirtual size : ()I
    //   468: istore #6
    //   470: iload #6
    //   472: ifle -> 546
    //   475: aload_3
    //   476: aload_1
    //   477: invokevirtual print : (Ljava/lang/String;)V
    //   480: aload_3
    //   481: ldc_w 'Back Stack Indices:'
    //   484: invokevirtual println : (Ljava/lang/String;)V
    //   487: iconst_0
    //   488: istore #7
    //   490: iload #7
    //   492: iload #6
    //   494: if_icmpge -> 546
    //   497: aload_0
    //   498: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   501: iload #7
    //   503: invokevirtual get : (I)Ljava/lang/Object;
    //   506: checkcast android/support/v4/app/BackStackRecord
    //   509: astore_2
    //   510: aload_3
    //   511: aload_1
    //   512: invokevirtual print : (Ljava/lang/String;)V
    //   515: aload_3
    //   516: ldc_w '  #'
    //   519: invokevirtual print : (Ljava/lang/String;)V
    //   522: aload_3
    //   523: iload #7
    //   525: invokevirtual print : (I)V
    //   528: aload_3
    //   529: ldc_w ': '
    //   532: invokevirtual print : (Ljava/lang/String;)V
    //   535: aload_3
    //   536: aload_2
    //   537: invokevirtual println : (Ljava/lang/Object;)V
    //   540: iinc #7, 1
    //   543: goto -> 490
    //   546: aload_0
    //   547: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   550: ifnull -> 589
    //   553: aload_0
    //   554: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   557: invokevirtual size : ()I
    //   560: ifle -> 589
    //   563: aload_3
    //   564: aload_1
    //   565: invokevirtual print : (Ljava/lang/String;)V
    //   568: aload_3
    //   569: ldc_w 'mAvailBackStackIndices: '
    //   572: invokevirtual print : (Ljava/lang/String;)V
    //   575: aload_3
    //   576: aload_0
    //   577: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   580: invokevirtual toArray : ()[Ljava/lang/Object;
    //   583: invokestatic toString : ([Ljava/lang/Object;)Ljava/lang/String;
    //   586: invokevirtual println : (Ljava/lang/String;)V
    //   589: aload_0
    //   590: monitorexit
    //   591: aload_0
    //   592: getfield mPendingActions : Ljava/util/ArrayList;
    //   595: ifnull -> 688
    //   598: aload_0
    //   599: getfield mPendingActions : Ljava/util/ArrayList;
    //   602: invokevirtual size : ()I
    //   605: istore #6
    //   607: iload #6
    //   609: ifle -> 688
    //   612: aload_3
    //   613: aload_1
    //   614: invokevirtual print : (Ljava/lang/String;)V
    //   617: aload_3
    //   618: ldc_w 'Pending Actions:'
    //   621: invokevirtual println : (Ljava/lang/String;)V
    //   624: iconst_0
    //   625: istore #7
    //   627: iload #7
    //   629: iload #6
    //   631: if_icmpge -> 688
    //   634: aload_0
    //   635: getfield mPendingActions : Ljava/util/ArrayList;
    //   638: iload #7
    //   640: invokevirtual get : (I)Ljava/lang/Object;
    //   643: checkcast android/support/v4/app/FragmentManagerImpl$OpGenerator
    //   646: astore_2
    //   647: aload_3
    //   648: aload_1
    //   649: invokevirtual print : (Ljava/lang/String;)V
    //   652: aload_3
    //   653: ldc_w '  #'
    //   656: invokevirtual print : (Ljava/lang/String;)V
    //   659: aload_3
    //   660: iload #7
    //   662: invokevirtual print : (I)V
    //   665: aload_3
    //   666: ldc_w ': '
    //   669: invokevirtual print : (Ljava/lang/String;)V
    //   672: aload_3
    //   673: aload_2
    //   674: invokevirtual println : (Ljava/lang/Object;)V
    //   677: iinc #7, 1
    //   680: goto -> 627
    //   683: astore_1
    //   684: aload_0
    //   685: monitorexit
    //   686: aload_1
    //   687: athrow
    //   688: aload_3
    //   689: aload_1
    //   690: invokevirtual print : (Ljava/lang/String;)V
    //   693: aload_3
    //   694: ldc_w 'FragmentManager misc state:'
    //   697: invokevirtual println : (Ljava/lang/String;)V
    //   700: aload_3
    //   701: aload_1
    //   702: invokevirtual print : (Ljava/lang/String;)V
    //   705: aload_3
    //   706: ldc_w '  mHost='
    //   709: invokevirtual print : (Ljava/lang/String;)V
    //   712: aload_3
    //   713: aload_0
    //   714: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   717: invokevirtual println : (Ljava/lang/Object;)V
    //   720: aload_3
    //   721: aload_1
    //   722: invokevirtual print : (Ljava/lang/String;)V
    //   725: aload_3
    //   726: ldc_w '  mContainer='
    //   729: invokevirtual print : (Ljava/lang/String;)V
    //   732: aload_3
    //   733: aload_0
    //   734: getfield mContainer : Landroid/support/v4/app/FragmentContainer;
    //   737: invokevirtual println : (Ljava/lang/Object;)V
    //   740: aload_0
    //   741: getfield mParent : Landroid/support/v4/app/Fragment;
    //   744: ifnull -> 767
    //   747: aload_3
    //   748: aload_1
    //   749: invokevirtual print : (Ljava/lang/String;)V
    //   752: aload_3
    //   753: ldc_w '  mParent='
    //   756: invokevirtual print : (Ljava/lang/String;)V
    //   759: aload_3
    //   760: aload_0
    //   761: getfield mParent : Landroid/support/v4/app/Fragment;
    //   764: invokevirtual println : (Ljava/lang/Object;)V
    //   767: aload_3
    //   768: aload_1
    //   769: invokevirtual print : (Ljava/lang/String;)V
    //   772: aload_3
    //   773: ldc_w '  mCurState='
    //   776: invokevirtual print : (Ljava/lang/String;)V
    //   779: aload_3
    //   780: aload_0
    //   781: getfield mCurState : I
    //   784: invokevirtual print : (I)V
    //   787: aload_3
    //   788: ldc_w ' mStateSaved='
    //   791: invokevirtual print : (Ljava/lang/String;)V
    //   794: aload_3
    //   795: aload_0
    //   796: getfield mStateSaved : Z
    //   799: invokevirtual print : (Z)V
    //   802: aload_3
    //   803: ldc_w ' mDestroyed='
    //   806: invokevirtual print : (Ljava/lang/String;)V
    //   809: aload_3
    //   810: aload_0
    //   811: getfield mDestroyed : Z
    //   814: invokevirtual println : (Z)V
    //   817: aload_0
    //   818: getfield mNeedMenuInvalidate : Z
    //   821: ifeq -> 844
    //   824: aload_3
    //   825: aload_1
    //   826: invokevirtual print : (Ljava/lang/String;)V
    //   829: aload_3
    //   830: ldc_w '  mNeedMenuInvalidate='
    //   833: invokevirtual print : (Ljava/lang/String;)V
    //   836: aload_3
    //   837: aload_0
    //   838: getfield mNeedMenuInvalidate : Z
    //   841: invokevirtual println : (Z)V
    //   844: aload_0
    //   845: getfield mNoTransactionsBecause : Ljava/lang/String;
    //   848: ifnull -> 871
    //   851: aload_3
    //   852: aload_1
    //   853: invokevirtual print : (Ljava/lang/String;)V
    //   856: aload_3
    //   857: ldc_w '  mNoTransactionsBecause='
    //   860: invokevirtual print : (Ljava/lang/String;)V
    //   863: aload_3
    //   864: aload_0
    //   865: getfield mNoTransactionsBecause : Ljava/lang/String;
    //   868: invokevirtual println : (Ljava/lang/String;)V
    //   871: aload_0
    //   872: getfield mAvailIndices : Ljava/util/ArrayList;
    //   875: ifnull -> 914
    //   878: aload_0
    //   879: getfield mAvailIndices : Ljava/util/ArrayList;
    //   882: invokevirtual size : ()I
    //   885: ifle -> 914
    //   888: aload_3
    //   889: aload_1
    //   890: invokevirtual print : (Ljava/lang/String;)V
    //   893: aload_3
    //   894: ldc_w '  mAvailIndices: '
    //   897: invokevirtual print : (Ljava/lang/String;)V
    //   900: aload_3
    //   901: aload_0
    //   902: getfield mAvailIndices : Ljava/util/ArrayList;
    //   905: invokevirtual toArray : ()[Ljava/lang/Object;
    //   908: invokestatic toString : ([Ljava/lang/Object;)Ljava/lang/String;
    //   911: invokevirtual println : (Ljava/lang/String;)V
    //   914: return
    // Exception table:
    //   from	to	target	type
    //   454	470	683	finally
    //   475	487	683	finally
    //   497	540	683	finally
    //   546	589	683	finally
    //   589	591	683	finally
    //   684	686	683	finally
  }
  
  public void enqueueAction(OpGenerator paramOpGenerator, boolean paramBoolean) {
    // Byte code:
    //   0: iload_2
    //   1: ifne -> 8
    //   4: aload_0
    //   5: invokespecial checkStateLoss : ()V
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield mDestroyed : Z
    //   14: ifne -> 24
    //   17: aload_0
    //   18: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   21: ifnonnull -> 42
    //   24: new java/lang/IllegalStateException
    //   27: astore_1
    //   28: aload_1
    //   29: ldc_w 'Activity has been destroyed'
    //   32: invokespecial <init> : (Ljava/lang/String;)V
    //   35: aload_1
    //   36: athrow
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    //   42: aload_0
    //   43: getfield mPendingActions : Ljava/util/ArrayList;
    //   46: ifnonnull -> 62
    //   49: new java/util/ArrayList
    //   52: astore_3
    //   53: aload_3
    //   54: invokespecial <init> : ()V
    //   57: aload_0
    //   58: aload_3
    //   59: putfield mPendingActions : Ljava/util/ArrayList;
    //   62: aload_0
    //   63: getfield mPendingActions : Ljava/util/ArrayList;
    //   66: aload_1
    //   67: invokevirtual add : (Ljava/lang/Object;)Z
    //   70: pop
    //   71: aload_0
    //   72: invokespecial scheduleCommit : ()V
    //   75: aload_0
    //   76: monitorexit
    //   77: return
    // Exception table:
    //   from	to	target	type
    //   10	24	37	finally
    //   24	37	37	finally
    //   38	40	37	finally
    //   42	62	37	finally
    //   62	77	37	finally
  }
  
  public boolean execPendingActions() {
    ensureExecReady(true);
    boolean bool = false;
    while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
        cleanupExec();
      } finally {
        cleanupExec();
      } 
    } 
    doPendingDeferredStart();
    return bool;
  }
  
  public void execSingleAction(OpGenerator paramOpGenerator, boolean paramBoolean) {
    ensureExecReady(paramBoolean);
    if (paramOpGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
        cleanupExec();
        return;
      } finally {
        cleanupExec();
      } 
    } 
    doPendingDeferredStart();
  }
  
  public boolean executePendingTransactions() {
    boolean bool = execPendingActions();
    forcePostponedTransactions();
    return bool;
  }
  
  public Fragment findFragmentById(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mAdded : Ljava/util/ArrayList;
    //   4: ifnull -> 53
    //   7: aload_0
    //   8: getfield mAdded : Ljava/util/ArrayList;
    //   11: invokevirtual size : ()I
    //   14: iconst_1
    //   15: isub
    //   16: istore_2
    //   17: iload_2
    //   18: iflt -> 53
    //   21: aload_0
    //   22: getfield mAdded : Ljava/util/ArrayList;
    //   25: iload_2
    //   26: invokevirtual get : (I)Ljava/lang/Object;
    //   29: checkcast android/support/v4/app/Fragment
    //   32: astore_3
    //   33: aload_3
    //   34: ifnull -> 47
    //   37: aload_3
    //   38: getfield mFragmentId : I
    //   41: iload_1
    //   42: if_icmpne -> 47
    //   45: aload_3
    //   46: areturn
    //   47: iinc #2, -1
    //   50: goto -> 17
    //   53: aload_0
    //   54: getfield mActive : Ljava/util/ArrayList;
    //   57: ifnull -> 110
    //   60: aload_0
    //   61: getfield mActive : Ljava/util/ArrayList;
    //   64: invokevirtual size : ()I
    //   67: iconst_1
    //   68: isub
    //   69: istore_2
    //   70: iload_2
    //   71: iflt -> 110
    //   74: aload_0
    //   75: getfield mActive : Ljava/util/ArrayList;
    //   78: iload_2
    //   79: invokevirtual get : (I)Ljava/lang/Object;
    //   82: checkcast android/support/v4/app/Fragment
    //   85: astore #4
    //   87: aload #4
    //   89: ifnull -> 104
    //   92: aload #4
    //   94: astore_3
    //   95: aload #4
    //   97: getfield mFragmentId : I
    //   100: iload_1
    //   101: if_icmpeq -> 45
    //   104: iinc #2, -1
    //   107: goto -> 70
    //   110: aconst_null
    //   111: astore_3
    //   112: goto -> 45
  }
  
  public Fragment findFragmentByTag(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mAdded : Ljava/util/ArrayList;
    //   4: ifnull -> 60
    //   7: aload_1
    //   8: ifnull -> 60
    //   11: aload_0
    //   12: getfield mAdded : Ljava/util/ArrayList;
    //   15: invokevirtual size : ()I
    //   18: iconst_1
    //   19: isub
    //   20: istore_2
    //   21: iload_2
    //   22: iflt -> 60
    //   25: aload_0
    //   26: getfield mAdded : Ljava/util/ArrayList;
    //   29: iload_2
    //   30: invokevirtual get : (I)Ljava/lang/Object;
    //   33: checkcast android/support/v4/app/Fragment
    //   36: astore_3
    //   37: aload_3
    //   38: ifnull -> 54
    //   41: aload_1
    //   42: aload_3
    //   43: getfield mTag : Ljava/lang/String;
    //   46: invokevirtual equals : (Ljava/lang/Object;)Z
    //   49: ifeq -> 54
    //   52: aload_3
    //   53: areturn
    //   54: iinc #2, -1
    //   57: goto -> 21
    //   60: aload_0
    //   61: getfield mActive : Ljava/util/ArrayList;
    //   64: ifnull -> 124
    //   67: aload_1
    //   68: ifnull -> 124
    //   71: aload_0
    //   72: getfield mActive : Ljava/util/ArrayList;
    //   75: invokevirtual size : ()I
    //   78: iconst_1
    //   79: isub
    //   80: istore_2
    //   81: iload_2
    //   82: iflt -> 124
    //   85: aload_0
    //   86: getfield mActive : Ljava/util/ArrayList;
    //   89: iload_2
    //   90: invokevirtual get : (I)Ljava/lang/Object;
    //   93: checkcast android/support/v4/app/Fragment
    //   96: astore #4
    //   98: aload #4
    //   100: ifnull -> 118
    //   103: aload #4
    //   105: astore_3
    //   106: aload_1
    //   107: aload #4
    //   109: getfield mTag : Ljava/lang/String;
    //   112: invokevirtual equals : (Ljava/lang/Object;)Z
    //   115: ifne -> 52
    //   118: iinc #2, -1
    //   121: goto -> 81
    //   124: aconst_null
    //   125: astore_3
    //   126: goto -> 52
  }
  
  public Fragment findFragmentByWho(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mActive : Ljava/util/ArrayList;
    //   4: ifnull -> 61
    //   7: aload_1
    //   8: ifnull -> 61
    //   11: aload_0
    //   12: getfield mActive : Ljava/util/ArrayList;
    //   15: invokevirtual size : ()I
    //   18: iconst_1
    //   19: isub
    //   20: istore_2
    //   21: iload_2
    //   22: iflt -> 61
    //   25: aload_0
    //   26: getfield mActive : Ljava/util/ArrayList;
    //   29: iload_2
    //   30: invokevirtual get : (I)Ljava/lang/Object;
    //   33: checkcast android/support/v4/app/Fragment
    //   36: astore_3
    //   37: aload_3
    //   38: ifnull -> 55
    //   41: aload_3
    //   42: aload_1
    //   43: invokevirtual findFragmentByWho : (Ljava/lang/String;)Landroid/support/v4/app/Fragment;
    //   46: astore_3
    //   47: aload_3
    //   48: ifnull -> 55
    //   51: aload_3
    //   52: astore_1
    //   53: aload_1
    //   54: areturn
    //   55: iinc #2, -1
    //   58: goto -> 21
    //   61: aconst_null
    //   62: astore_1
    //   63: goto -> 53
  }
  
  public void freeBackStackIndex(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: iload_1
    //   7: aconst_null
    //   8: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   11: pop
    //   12: aload_0
    //   13: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   16: ifnonnull -> 32
    //   19: new java/util/ArrayList
    //   22: astore_2
    //   23: aload_2
    //   24: invokespecial <init> : ()V
    //   27: aload_0
    //   28: aload_2
    //   29: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   32: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   35: ifeq -> 66
    //   38: new java/lang/StringBuilder
    //   41: astore_2
    //   42: aload_2
    //   43: invokespecial <init> : ()V
    //   46: ldc 'FragmentManager'
    //   48: aload_2
    //   49: ldc_w 'Freeing back stack index '
    //   52: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   55: iload_1
    //   56: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   59: invokevirtual toString : ()Ljava/lang/String;
    //   62: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   65: pop
    //   66: aload_0
    //   67: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   70: iload_1
    //   71: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   74: invokevirtual add : (Ljava/lang/Object;)Z
    //   77: pop
    //   78: aload_0
    //   79: monitorexit
    //   80: return
    //   81: astore_2
    //   82: aload_0
    //   83: monitorexit
    //   84: aload_2
    //   85: athrow
    // Exception table:
    //   from	to	target	type
    //   2	32	81	finally
    //   32	66	81	finally
    //   66	80	81	finally
    //   82	84	81	finally
  }
  
  public FragmentManager.BackStackEntry getBackStackEntryAt(int paramInt) {
    return this.mBackStack.get(paramInt);
  }
  
  public int getBackStackEntryCount() {
    return (this.mBackStack != null) ? this.mBackStack.size() : 0;
  }
  
  public Fragment getFragment(Bundle paramBundle, String paramString) {
    int i = paramBundle.getInt(paramString, -1);
    if (i == -1)
      return null; 
    if (i >= this.mActive.size())
      throwException(new IllegalStateException("Fragment no longer exists for key " + paramString + ": index " + i)); 
    Fragment fragment2 = this.mActive.get(i);
    Fragment fragment1 = fragment2;
    if (fragment2 == null) {
      throwException(new IllegalStateException("Fragment no longer exists for key " + paramString + ": index " + i));
      fragment1 = fragment2;
    } 
    return fragment1;
  }
  
  public List<Fragment> getFragments() {
    return this.mActive;
  }
  
  LayoutInflaterFactory getLayoutInflaterFactory() {
    return this;
  }
  
  public void hideFragment(Fragment paramFragment) {
    boolean bool = true;
    if (DEBUG)
      Log.v("FragmentManager", "hide: " + paramFragment); 
    if (!paramFragment.mHidden) {
      paramFragment.mHidden = true;
      if (paramFragment.mHiddenChanged)
        bool = false; 
      paramFragment.mHiddenChanged = bool;
    } 
  }
  
  public boolean isDestroyed() {
    return this.mDestroyed;
  }
  
  boolean isStateAtLeast(int paramInt) {
    return (this.mCurState >= paramInt);
  }
  
  Animation loadAnimation(Fragment paramFragment, int paramInt1, boolean paramBoolean, int paramInt2) {
    Animation animation = paramFragment.onCreateAnimation(paramInt1, paramBoolean, paramFragment.getNextAnim());
    if (animation != null)
      return animation; 
    if (paramFragment.getNextAnim() != 0) {
      Animation animation1 = AnimationUtils.loadAnimation(this.mHost.getContext(), paramFragment.getNextAnim());
      if (animation1 != null)
        return animation1; 
    } 
    if (paramInt1 == 0)
      return null; 
    paramInt1 = transitToStyleIndex(paramInt1, paramBoolean);
    if (paramInt1 < 0)
      return null; 
    switch (paramInt1) {
      default:
        paramInt1 = paramInt2;
        if (paramInt2 == 0) {
          paramInt1 = paramInt2;
          if (this.mHost.onHasWindowAnimations())
            paramInt1 = this.mHost.onGetWindowAnimations(); 
        } 
        if (paramInt1 == 0)
          return null; 
        break;
      case 1:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.125F, 1.0F, 0.0F, 1.0F);
      case 2:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 0.975F, 1.0F, 0.0F);
      case 3:
        return makeOpenCloseAnimation(this.mHost.getContext(), 0.975F, 1.0F, 0.0F, 1.0F);
      case 4:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 1.075F, 1.0F, 0.0F);
      case 5:
        return makeFadeAnimation(this.mHost.getContext(), 0.0F, 1.0F);
      case 6:
        return makeFadeAnimation(this.mHost.getContext(), 1.0F, 0.0F);
    } 
    return null;
  }
  
  void makeActive(Fragment paramFragment) {
    if (paramFragment.mIndex < 0) {
      if (this.mAvailIndices == null || this.mAvailIndices.size() <= 0) {
        if (this.mActive == null)
          this.mActive = new ArrayList<Fragment>(); 
        paramFragment.setIndex(this.mActive.size(), this.mParent);
        this.mActive.add(paramFragment);
      } else {
        paramFragment.setIndex(((Integer)this.mAvailIndices.remove(this.mAvailIndices.size() - 1)).intValue(), this.mParent);
        this.mActive.set(paramFragment.mIndex, paramFragment);
      } 
      if (DEBUG)
        Log.v("FragmentManager", "Allocated fragment index " + paramFragment); 
    } 
  }
  
  void makeInactive(Fragment paramFragment) {
    if (paramFragment.mIndex >= 0) {
      if (DEBUG)
        Log.v("FragmentManager", "Freeing fragment index " + paramFragment); 
      this.mActive.set(paramFragment.mIndex, null);
      if (this.mAvailIndices == null)
        this.mAvailIndices = new ArrayList<Integer>(); 
      this.mAvailIndices.add(Integer.valueOf(paramFragment.mIndex));
      this.mHost.inactivateFragment(paramFragment.mWho);
      paramFragment.initState();
    } 
  }
  
  void moveFragmentToExpectedState(Fragment paramFragment) {
    if (paramFragment != null) {
      int i = this.mCurState;
      int j = i;
      if (paramFragment.mRemoving)
        if (paramFragment.isInBackStack()) {
          j = Math.min(i, 1);
        } else {
          j = Math.min(i, 0);
        }  
      moveToState(paramFragment, j, paramFragment.getNextTransition(), paramFragment.getNextTransitionStyle(), false);
      if (paramFragment.mView != null) {
        Fragment fragment = findFragmentUnder(paramFragment);
        if (fragment != null) {
          View view = fragment.mView;
          ViewGroup viewGroup = paramFragment.mContainer;
          j = viewGroup.indexOfChild(view);
          i = viewGroup.indexOfChild(paramFragment.mView);
          if (i < j) {
            viewGroup.removeViewAt(i);
            viewGroup.addView(paramFragment.mView, j);
          } 
        } 
        if (paramFragment.mIsNewlyAdded && paramFragment.mContainer != null) {
          paramFragment.mView.setVisibility(0);
          paramFragment.mIsNewlyAdded = false;
          Animation animation = loadAnimation(paramFragment, paramFragment.getNextTransition(), true, paramFragment.getNextTransitionStyle());
          if (animation != null) {
            setHWLayerAnimListenerIfAlpha(paramFragment.mView, animation);
            paramFragment.mView.startAnimation(animation);
          } 
        } 
      } 
      if (paramFragment.mHiddenChanged)
        completeShowHideFragment(paramFragment); 
    } 
  }
  
  void moveToState(int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   4: ifnonnull -> 22
    //   7: iload_1
    //   8: ifeq -> 22
    //   11: new java/lang/IllegalStateException
    //   14: dup
    //   15: ldc_w 'No activity'
    //   18: invokespecial <init> : (Ljava/lang/String;)V
    //   21: athrow
    //   22: iload_2
    //   23: ifne -> 35
    //   26: iload_1
    //   27: aload_0
    //   28: getfield mCurState : I
    //   31: if_icmpne -> 35
    //   34: return
    //   35: aload_0
    //   36: iload_1
    //   37: putfield mCurState : I
    //   40: aload_0
    //   41: getfield mActive : Ljava/util/ArrayList;
    //   44: ifnull -> 34
    //   47: iconst_0
    //   48: istore_1
    //   49: iconst_0
    //   50: istore_3
    //   51: aload_0
    //   52: getfield mAdded : Ljava/util/ArrayList;
    //   55: ifnull -> 128
    //   58: aload_0
    //   59: getfield mAdded : Ljava/util/ArrayList;
    //   62: invokevirtual size : ()I
    //   65: istore #4
    //   67: iconst_0
    //   68: istore #5
    //   70: iload_3
    //   71: istore_1
    //   72: iload #5
    //   74: iload #4
    //   76: if_icmpge -> 128
    //   79: aload_0
    //   80: getfield mAdded : Ljava/util/ArrayList;
    //   83: iload #5
    //   85: invokevirtual get : (I)Ljava/lang/Object;
    //   88: checkcast android/support/v4/app/Fragment
    //   91: astore #6
    //   93: aload_0
    //   94: aload #6
    //   96: invokevirtual moveFragmentToExpectedState : (Landroid/support/v4/app/Fragment;)V
    //   99: iload_3
    //   100: istore_1
    //   101: aload #6
    //   103: getfield mLoaderManager : Landroid/support/v4/app/LoaderManagerImpl;
    //   106: ifnull -> 120
    //   109: iload_3
    //   110: aload #6
    //   112: getfield mLoaderManager : Landroid/support/v4/app/LoaderManagerImpl;
    //   115: invokevirtual hasRunningLoaders : ()Z
    //   118: ior
    //   119: istore_1
    //   120: iinc #5, 1
    //   123: iload_1
    //   124: istore_3
    //   125: goto -> 70
    //   128: aload_0
    //   129: getfield mActive : Ljava/util/ArrayList;
    //   132: invokevirtual size : ()I
    //   135: istore #4
    //   137: iconst_0
    //   138: istore_3
    //   139: iload_1
    //   140: istore #5
    //   142: iload_3
    //   143: istore_1
    //   144: iload_1
    //   145: iload #4
    //   147: if_icmpge -> 239
    //   150: aload_0
    //   151: getfield mActive : Ljava/util/ArrayList;
    //   154: iload_1
    //   155: invokevirtual get : (I)Ljava/lang/Object;
    //   158: checkcast android/support/v4/app/Fragment
    //   161: astore #6
    //   163: iload #5
    //   165: istore_3
    //   166: aload #6
    //   168: ifnull -> 230
    //   171: aload #6
    //   173: getfield mRemoving : Z
    //   176: ifne -> 190
    //   179: iload #5
    //   181: istore_3
    //   182: aload #6
    //   184: getfield mDetached : Z
    //   187: ifeq -> 230
    //   190: iload #5
    //   192: istore_3
    //   193: aload #6
    //   195: getfield mIsNewlyAdded : Z
    //   198: ifne -> 230
    //   201: aload_0
    //   202: aload #6
    //   204: invokevirtual moveFragmentToExpectedState : (Landroid/support/v4/app/Fragment;)V
    //   207: iload #5
    //   209: istore_3
    //   210: aload #6
    //   212: getfield mLoaderManager : Landroid/support/v4/app/LoaderManagerImpl;
    //   215: ifnull -> 230
    //   218: iload #5
    //   220: aload #6
    //   222: getfield mLoaderManager : Landroid/support/v4/app/LoaderManagerImpl;
    //   225: invokevirtual hasRunningLoaders : ()Z
    //   228: ior
    //   229: istore_3
    //   230: iinc #1, 1
    //   233: iload_3
    //   234: istore #5
    //   236: goto -> 144
    //   239: iload #5
    //   241: ifne -> 248
    //   244: aload_0
    //   245: invokevirtual startPendingDeferredFragments : ()V
    //   248: aload_0
    //   249: getfield mNeedMenuInvalidate : Z
    //   252: ifeq -> 34
    //   255: aload_0
    //   256: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   259: ifnull -> 34
    //   262: aload_0
    //   263: getfield mCurState : I
    //   266: iconst_5
    //   267: if_icmpne -> 34
    //   270: aload_0
    //   271: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   274: invokevirtual onSupportInvalidateOptionsMenu : ()V
    //   277: aload_0
    //   278: iconst_0
    //   279: putfield mNeedMenuInvalidate : Z
    //   282: goto -> 34
  }
  
  void moveToState(Fragment paramFragment) {
    moveToState(paramFragment, this.mCurState, 0, 0, false);
  }
  
  void moveToState(Fragment paramFragment, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    // Byte code:
    //   0: aload_1
    //   1: getfield mAdded : Z
    //   4: ifeq -> 17
    //   7: iload_2
    //   8: istore #6
    //   10: aload_1
    //   11: getfield mDetached : Z
    //   14: ifeq -> 28
    //   17: iload_2
    //   18: istore #6
    //   20: iload_2
    //   21: iconst_1
    //   22: if_icmple -> 28
    //   25: iconst_1
    //   26: istore #6
    //   28: iload #6
    //   30: istore #7
    //   32: aload_1
    //   33: getfield mRemoving : Z
    //   36: ifeq -> 58
    //   39: iload #6
    //   41: istore #7
    //   43: iload #6
    //   45: aload_1
    //   46: getfield mState : I
    //   49: if_icmple -> 58
    //   52: aload_1
    //   53: getfield mState : I
    //   56: istore #7
    //   58: iload #7
    //   60: istore_2
    //   61: aload_1
    //   62: getfield mDeferStart : Z
    //   65: ifeq -> 90
    //   68: iload #7
    //   70: istore_2
    //   71: aload_1
    //   72: getfield mState : I
    //   75: iconst_4
    //   76: if_icmpge -> 90
    //   79: iload #7
    //   81: istore_2
    //   82: iload #7
    //   84: iconst_3
    //   85: if_icmple -> 90
    //   88: iconst_3
    //   89: istore_2
    //   90: aload_1
    //   91: getfield mState : I
    //   94: iload_2
    //   95: if_icmpge -> 1304
    //   98: aload_1
    //   99: getfield mFromLayout : Z
    //   102: ifeq -> 113
    //   105: aload_1
    //   106: getfield mInLayout : Z
    //   109: ifne -> 113
    //   112: return
    //   113: aload_1
    //   114: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   117: ifnull -> 137
    //   120: aload_1
    //   121: aconst_null
    //   122: invokevirtual setAnimatingAway : (Landroid/view/View;)V
    //   125: aload_0
    //   126: aload_1
    //   127: aload_1
    //   128: invokevirtual getStateAfterAnimating : ()I
    //   131: iconst_0
    //   132: iconst_0
    //   133: iconst_1
    //   134: invokevirtual moveToState : (Landroid/support/v4/app/Fragment;IIIZ)V
    //   137: iload_2
    //   138: istore_3
    //   139: iload_2
    //   140: istore #7
    //   142: iload_2
    //   143: istore #6
    //   145: iload_2
    //   146: istore #4
    //   148: aload_1
    //   149: getfield mState : I
    //   152: tableswitch default -> 188, 0 -> 265, 1 -> 697, 2 -> 1076, 3 -> 1095, 4 -> 1151
    //   188: iload_2
    //   189: istore #7
    //   191: aload_1
    //   192: getfield mState : I
    //   195: iload #7
    //   197: if_icmpeq -> 112
    //   200: ldc 'FragmentManager'
    //   202: new java/lang/StringBuilder
    //   205: dup
    //   206: invokespecial <init> : ()V
    //   209: ldc_w 'moveToState: Fragment state for '
    //   212: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: aload_1
    //   216: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   219: ldc_w ' not updated inline; '
    //   222: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: ldc_w 'expected state '
    //   228: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   231: iload #7
    //   233: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   236: ldc_w ' found '
    //   239: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   242: aload_1
    //   243: getfield mState : I
    //   246: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   249: invokevirtual toString : ()Ljava/lang/String;
    //   252: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   255: pop
    //   256: aload_1
    //   257: iload #7
    //   259: putfield mState : I
    //   262: goto -> 112
    //   265: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   268: ifeq -> 297
    //   271: ldc 'FragmentManager'
    //   273: new java/lang/StringBuilder
    //   276: dup
    //   277: invokespecial <init> : ()V
    //   280: ldc_w 'moveto CREATED: '
    //   283: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   286: aload_1
    //   287: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   290: invokevirtual toString : ()Ljava/lang/String;
    //   293: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   296: pop
    //   297: iload_2
    //   298: istore #4
    //   300: aload_1
    //   301: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   304: ifnull -> 412
    //   307: aload_1
    //   308: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   311: aload_0
    //   312: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   315: invokevirtual getContext : ()Landroid/content/Context;
    //   318: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   321: invokevirtual setClassLoader : (Ljava/lang/ClassLoader;)V
    //   324: aload_1
    //   325: aload_1
    //   326: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   329: ldc 'android:view_state'
    //   331: invokevirtual getSparseParcelableArray : (Ljava/lang/String;)Landroid/util/SparseArray;
    //   334: putfield mSavedViewState : Landroid/util/SparseArray;
    //   337: aload_1
    //   338: aload_0
    //   339: aload_1
    //   340: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   343: ldc 'android:target_state'
    //   345: invokevirtual getFragment : (Landroid/os/Bundle;Ljava/lang/String;)Landroid/support/v4/app/Fragment;
    //   348: putfield mTarget : Landroid/support/v4/app/Fragment;
    //   351: aload_1
    //   352: getfield mTarget : Landroid/support/v4/app/Fragment;
    //   355: ifnull -> 372
    //   358: aload_1
    //   359: aload_1
    //   360: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   363: ldc 'android:target_req_state'
    //   365: iconst_0
    //   366: invokevirtual getInt : (Ljava/lang/String;I)I
    //   369: putfield mTargetRequestCode : I
    //   372: aload_1
    //   373: aload_1
    //   374: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   377: ldc 'android:user_visible_hint'
    //   379: iconst_1
    //   380: invokevirtual getBoolean : (Ljava/lang/String;Z)Z
    //   383: putfield mUserVisibleHint : Z
    //   386: iload_2
    //   387: istore #4
    //   389: aload_1
    //   390: getfield mUserVisibleHint : Z
    //   393: ifne -> 412
    //   396: aload_1
    //   397: iconst_1
    //   398: putfield mDeferStart : Z
    //   401: iload_2
    //   402: istore #4
    //   404: iload_2
    //   405: iconst_3
    //   406: if_icmple -> 412
    //   409: iconst_3
    //   410: istore #4
    //   412: aload_1
    //   413: aload_0
    //   414: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   417: putfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   420: aload_1
    //   421: aload_0
    //   422: getfield mParent : Landroid/support/v4/app/Fragment;
    //   425: putfield mParentFragment : Landroid/support/v4/app/Fragment;
    //   428: aload_0
    //   429: getfield mParent : Landroid/support/v4/app/Fragment;
    //   432: ifnull -> 520
    //   435: aload_0
    //   436: getfield mParent : Landroid/support/v4/app/Fragment;
    //   439: getfield mChildFragmentManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   442: astore #8
    //   444: aload_1
    //   445: aload #8
    //   447: putfield mFragmentManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   450: aload_0
    //   451: aload_1
    //   452: aload_0
    //   453: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   456: invokevirtual getContext : ()Landroid/content/Context;
    //   459: iconst_0
    //   460: invokevirtual dispatchOnFragmentPreAttached : (Landroid/support/v4/app/Fragment;Landroid/content/Context;Z)V
    //   463: aload_1
    //   464: iconst_0
    //   465: putfield mCalled : Z
    //   468: aload_1
    //   469: aload_0
    //   470: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   473: invokevirtual getContext : ()Landroid/content/Context;
    //   476: invokevirtual onAttach : (Landroid/content/Context;)V
    //   479: aload_1
    //   480: getfield mCalled : Z
    //   483: ifne -> 532
    //   486: new android/support/v4/app/SuperNotCalledException
    //   489: dup
    //   490: new java/lang/StringBuilder
    //   493: dup
    //   494: invokespecial <init> : ()V
    //   497: ldc_w 'Fragment '
    //   500: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   503: aload_1
    //   504: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   507: ldc_w ' did not call through to super.onAttach()'
    //   510: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   513: invokevirtual toString : ()Ljava/lang/String;
    //   516: invokespecial <init> : (Ljava/lang/String;)V
    //   519: athrow
    //   520: aload_0
    //   521: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   524: invokevirtual getFragmentManagerImpl : ()Landroid/support/v4/app/FragmentManagerImpl;
    //   527: astore #8
    //   529: goto -> 444
    //   532: aload_1
    //   533: getfield mParentFragment : Landroid/support/v4/app/Fragment;
    //   536: ifnonnull -> 1220
    //   539: aload_0
    //   540: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   543: aload_1
    //   544: invokevirtual onAttachFragment : (Landroid/support/v4/app/Fragment;)V
    //   547: aload_0
    //   548: aload_1
    //   549: aload_0
    //   550: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   553: invokevirtual getContext : ()Landroid/content/Context;
    //   556: iconst_0
    //   557: invokevirtual dispatchOnFragmentAttached : (Landroid/support/v4/app/Fragment;Landroid/content/Context;Z)V
    //   560: aload_1
    //   561: getfield mRetaining : Z
    //   564: ifne -> 1231
    //   567: aload_1
    //   568: aload_1
    //   569: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   572: invokevirtual performCreate : (Landroid/os/Bundle;)V
    //   575: aload_0
    //   576: aload_1
    //   577: aload_1
    //   578: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   581: iconst_0
    //   582: invokevirtual dispatchOnFragmentCreated : (Landroid/support/v4/app/Fragment;Landroid/os/Bundle;Z)V
    //   585: aload_1
    //   586: iconst_0
    //   587: putfield mRetaining : Z
    //   590: iload #4
    //   592: istore_3
    //   593: aload_1
    //   594: getfield mFromLayout : Z
    //   597: ifeq -> 697
    //   600: aload_1
    //   601: aload_1
    //   602: aload_1
    //   603: aload_1
    //   604: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   607: invokevirtual getLayoutInflater : (Landroid/os/Bundle;)Landroid/view/LayoutInflater;
    //   610: aconst_null
    //   611: aload_1
    //   612: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   615: invokevirtual performCreateView : (Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)Landroid/view/View;
    //   618: putfield mView : Landroid/view/View;
    //   621: aload_1
    //   622: getfield mView : Landroid/view/View;
    //   625: ifnull -> 1261
    //   628: aload_1
    //   629: aload_1
    //   630: getfield mView : Landroid/view/View;
    //   633: putfield mInnerView : Landroid/view/View;
    //   636: getstatic android/os/Build$VERSION.SDK_INT : I
    //   639: bipush #11
    //   641: if_icmplt -> 1247
    //   644: aload_1
    //   645: getfield mView : Landroid/view/View;
    //   648: iconst_0
    //   649: invokestatic setSaveFromParentEnabled : (Landroid/view/View;Z)V
    //   652: aload_1
    //   653: getfield mHidden : Z
    //   656: ifeq -> 668
    //   659: aload_1
    //   660: getfield mView : Landroid/view/View;
    //   663: bipush #8
    //   665: invokevirtual setVisibility : (I)V
    //   668: aload_1
    //   669: aload_1
    //   670: getfield mView : Landroid/view/View;
    //   673: aload_1
    //   674: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   677: invokevirtual onViewCreated : (Landroid/view/View;Landroid/os/Bundle;)V
    //   680: aload_0
    //   681: aload_1
    //   682: aload_1
    //   683: getfield mView : Landroid/view/View;
    //   686: aload_1
    //   687: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   690: iconst_0
    //   691: invokevirtual dispatchOnFragmentViewCreated : (Landroid/support/v4/app/Fragment;Landroid/view/View;Landroid/os/Bundle;Z)V
    //   694: iload #4
    //   696: istore_3
    //   697: iload_3
    //   698: istore #7
    //   700: iload_3
    //   701: iconst_1
    //   702: if_icmple -> 1076
    //   705: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   708: ifeq -> 737
    //   711: ldc 'FragmentManager'
    //   713: new java/lang/StringBuilder
    //   716: dup
    //   717: invokespecial <init> : ()V
    //   720: ldc_w 'moveto ACTIVITY_CREATED: '
    //   723: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   726: aload_1
    //   727: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   730: invokevirtual toString : ()Ljava/lang/String;
    //   733: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   736: pop
    //   737: aload_1
    //   738: getfield mFromLayout : Z
    //   741: ifne -> 1035
    //   744: aconst_null
    //   745: astore #8
    //   747: aload_1
    //   748: getfield mContainerId : I
    //   751: ifeq -> 910
    //   754: aload_1
    //   755: getfield mContainerId : I
    //   758: iconst_m1
    //   759: if_icmpne -> 799
    //   762: aload_0
    //   763: new java/lang/IllegalArgumentException
    //   766: dup
    //   767: new java/lang/StringBuilder
    //   770: dup
    //   771: invokespecial <init> : ()V
    //   774: ldc_w 'Cannot create fragment '
    //   777: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   780: aload_1
    //   781: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   784: ldc_w ' for a container view with no id'
    //   787: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   790: invokevirtual toString : ()Ljava/lang/String;
    //   793: invokespecial <init> : (Ljava/lang/String;)V
    //   796: invokespecial throwException : (Ljava/lang/RuntimeException;)V
    //   799: aload_0
    //   800: getfield mContainer : Landroid/support/v4/app/FragmentContainer;
    //   803: aload_1
    //   804: getfield mContainerId : I
    //   807: invokevirtual onFindViewById : (I)Landroid/view/View;
    //   810: checkcast android/view/ViewGroup
    //   813: astore #9
    //   815: aload #9
    //   817: astore #8
    //   819: aload #9
    //   821: ifnonnull -> 910
    //   824: aload #9
    //   826: astore #8
    //   828: aload_1
    //   829: getfield mRestored : Z
    //   832: ifne -> 910
    //   835: aload_1
    //   836: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   839: aload_1
    //   840: getfield mContainerId : I
    //   843: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   846: astore #8
    //   848: aload_0
    //   849: new java/lang/IllegalArgumentException
    //   852: dup
    //   853: new java/lang/StringBuilder
    //   856: dup
    //   857: invokespecial <init> : ()V
    //   860: ldc_w 'No view found for id 0x'
    //   863: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   866: aload_1
    //   867: getfield mContainerId : I
    //   870: invokestatic toHexString : (I)Ljava/lang/String;
    //   873: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   876: ldc_w ' ('
    //   879: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   882: aload #8
    //   884: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   887: ldc_w ') for fragment '
    //   890: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   893: aload_1
    //   894: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   897: invokevirtual toString : ()Ljava/lang/String;
    //   900: invokespecial <init> : (Ljava/lang/String;)V
    //   903: invokespecial throwException : (Ljava/lang/RuntimeException;)V
    //   906: aload #9
    //   908: astore #8
    //   910: aload_1
    //   911: aload #8
    //   913: putfield mContainer : Landroid/view/ViewGroup;
    //   916: aload_1
    //   917: aload_1
    //   918: aload_1
    //   919: aload_1
    //   920: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   923: invokevirtual getLayoutInflater : (Landroid/os/Bundle;)Landroid/view/LayoutInflater;
    //   926: aload #8
    //   928: aload_1
    //   929: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   932: invokevirtual performCreateView : (Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)Landroid/view/View;
    //   935: putfield mView : Landroid/view/View;
    //   938: aload_1
    //   939: getfield mView : Landroid/view/View;
    //   942: ifnull -> 1296
    //   945: aload_1
    //   946: aload_1
    //   947: getfield mView : Landroid/view/View;
    //   950: putfield mInnerView : Landroid/view/View;
    //   953: getstatic android/os/Build$VERSION.SDK_INT : I
    //   956: bipush #11
    //   958: if_icmplt -> 1282
    //   961: aload_1
    //   962: getfield mView : Landroid/view/View;
    //   965: iconst_0
    //   966: invokestatic setSaveFromParentEnabled : (Landroid/view/View;Z)V
    //   969: aload #8
    //   971: ifnull -> 988
    //   974: aload #8
    //   976: aload_1
    //   977: getfield mView : Landroid/view/View;
    //   980: invokevirtual addView : (Landroid/view/View;)V
    //   983: aload_1
    //   984: iconst_1
    //   985: putfield mIsNewlyAdded : Z
    //   988: aload_1
    //   989: getfield mHidden : Z
    //   992: ifeq -> 1009
    //   995: aload_1
    //   996: getfield mView : Landroid/view/View;
    //   999: bipush #8
    //   1001: invokevirtual setVisibility : (I)V
    //   1004: aload_1
    //   1005: iconst_0
    //   1006: putfield mIsNewlyAdded : Z
    //   1009: aload_1
    //   1010: aload_1
    //   1011: getfield mView : Landroid/view/View;
    //   1014: aload_1
    //   1015: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1018: invokevirtual onViewCreated : (Landroid/view/View;Landroid/os/Bundle;)V
    //   1021: aload_0
    //   1022: aload_1
    //   1023: aload_1
    //   1024: getfield mView : Landroid/view/View;
    //   1027: aload_1
    //   1028: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1031: iconst_0
    //   1032: invokevirtual dispatchOnFragmentViewCreated : (Landroid/support/v4/app/Fragment;Landroid/view/View;Landroid/os/Bundle;Z)V
    //   1035: aload_1
    //   1036: aload_1
    //   1037: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1040: invokevirtual performActivityCreated : (Landroid/os/Bundle;)V
    //   1043: aload_0
    //   1044: aload_1
    //   1045: aload_1
    //   1046: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1049: iconst_0
    //   1050: invokevirtual dispatchOnFragmentActivityCreated : (Landroid/support/v4/app/Fragment;Landroid/os/Bundle;Z)V
    //   1053: aload_1
    //   1054: getfield mView : Landroid/view/View;
    //   1057: ifnull -> 1068
    //   1060: aload_1
    //   1061: aload_1
    //   1062: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1065: invokevirtual restoreViewState : (Landroid/os/Bundle;)V
    //   1068: aload_1
    //   1069: aconst_null
    //   1070: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   1073: iload_3
    //   1074: istore #7
    //   1076: iload #7
    //   1078: istore #6
    //   1080: iload #7
    //   1082: iconst_2
    //   1083: if_icmple -> 1095
    //   1086: aload_1
    //   1087: iconst_3
    //   1088: putfield mState : I
    //   1091: iload #7
    //   1093: istore #6
    //   1095: iload #6
    //   1097: istore #4
    //   1099: iload #6
    //   1101: iconst_3
    //   1102: if_icmple -> 1151
    //   1105: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1108: ifeq -> 1137
    //   1111: ldc 'FragmentManager'
    //   1113: new java/lang/StringBuilder
    //   1116: dup
    //   1117: invokespecial <init> : ()V
    //   1120: ldc_w 'moveto STARTED: '
    //   1123: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1126: aload_1
    //   1127: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1130: invokevirtual toString : ()Ljava/lang/String;
    //   1133: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1136: pop
    //   1137: aload_1
    //   1138: invokevirtual performStart : ()V
    //   1141: aload_0
    //   1142: aload_1
    //   1143: iconst_0
    //   1144: invokevirtual dispatchOnFragmentStarted : (Landroid/support/v4/app/Fragment;Z)V
    //   1147: iload #6
    //   1149: istore #4
    //   1151: iload #4
    //   1153: istore #7
    //   1155: iload #4
    //   1157: iconst_4
    //   1158: if_icmple -> 191
    //   1161: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1164: ifeq -> 1193
    //   1167: ldc 'FragmentManager'
    //   1169: new java/lang/StringBuilder
    //   1172: dup
    //   1173: invokespecial <init> : ()V
    //   1176: ldc_w 'moveto RESUMED: '
    //   1179: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1182: aload_1
    //   1183: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1186: invokevirtual toString : ()Ljava/lang/String;
    //   1189: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1192: pop
    //   1193: aload_1
    //   1194: invokevirtual performResume : ()V
    //   1197: aload_0
    //   1198: aload_1
    //   1199: iconst_0
    //   1200: invokevirtual dispatchOnFragmentResumed : (Landroid/support/v4/app/Fragment;Z)V
    //   1203: aload_1
    //   1204: aconst_null
    //   1205: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   1208: aload_1
    //   1209: aconst_null
    //   1210: putfield mSavedViewState : Landroid/util/SparseArray;
    //   1213: iload #4
    //   1215: istore #7
    //   1217: goto -> 191
    //   1220: aload_1
    //   1221: getfield mParentFragment : Landroid/support/v4/app/Fragment;
    //   1224: aload_1
    //   1225: invokevirtual onAttachFragment : (Landroid/support/v4/app/Fragment;)V
    //   1228: goto -> 547
    //   1231: aload_1
    //   1232: aload_1
    //   1233: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1236: invokevirtual restoreChildFragmentState : (Landroid/os/Bundle;)V
    //   1239: aload_1
    //   1240: iconst_1
    //   1241: putfield mState : I
    //   1244: goto -> 585
    //   1247: aload_1
    //   1248: aload_1
    //   1249: getfield mView : Landroid/view/View;
    //   1252: invokestatic wrap : (Landroid/view/View;)Landroid/view/ViewGroup;
    //   1255: putfield mView : Landroid/view/View;
    //   1258: goto -> 652
    //   1261: aload_1
    //   1262: aconst_null
    //   1263: putfield mInnerView : Landroid/view/View;
    //   1266: iload #4
    //   1268: istore_3
    //   1269: goto -> 697
    //   1272: astore #8
    //   1274: ldc_w 'unknown'
    //   1277: astore #8
    //   1279: goto -> 848
    //   1282: aload_1
    //   1283: aload_1
    //   1284: getfield mView : Landroid/view/View;
    //   1287: invokestatic wrap : (Landroid/view/View;)Landroid/view/ViewGroup;
    //   1290: putfield mView : Landroid/view/View;
    //   1293: goto -> 969
    //   1296: aload_1
    //   1297: aconst_null
    //   1298: putfield mInnerView : Landroid/view/View;
    //   1301: goto -> 1035
    //   1304: iload_2
    //   1305: istore #7
    //   1307: aload_1
    //   1308: getfield mState : I
    //   1311: iload_2
    //   1312: if_icmple -> 191
    //   1315: aload_1
    //   1316: getfield mState : I
    //   1319: tableswitch default -> 1352, 1 -> 1358, 2 -> 1549, 3 -> 1508, 4 -> 1461, 5 -> 1414
    //   1352: iload_2
    //   1353: istore #7
    //   1355: goto -> 191
    //   1358: iload_2
    //   1359: istore #7
    //   1361: iload_2
    //   1362: iconst_1
    //   1363: if_icmpge -> 191
    //   1366: aload_0
    //   1367: getfield mDestroyed : Z
    //   1370: ifeq -> 1396
    //   1373: aload_1
    //   1374: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1377: ifnull -> 1396
    //   1380: aload_1
    //   1381: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1384: astore #8
    //   1386: aload_1
    //   1387: aconst_null
    //   1388: invokevirtual setAnimatingAway : (Landroid/view/View;)V
    //   1391: aload #8
    //   1393: invokevirtual clearAnimation : ()V
    //   1396: aload_1
    //   1397: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1400: ifnull -> 1766
    //   1403: aload_1
    //   1404: iload_2
    //   1405: invokevirtual setStateAfterAnimating : (I)V
    //   1408: iconst_1
    //   1409: istore #7
    //   1411: goto -> 191
    //   1414: iload_2
    //   1415: iconst_5
    //   1416: if_icmpge -> 1461
    //   1419: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1422: ifeq -> 1451
    //   1425: ldc 'FragmentManager'
    //   1427: new java/lang/StringBuilder
    //   1430: dup
    //   1431: invokespecial <init> : ()V
    //   1434: ldc_w 'movefrom RESUMED: '
    //   1437: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1440: aload_1
    //   1441: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1444: invokevirtual toString : ()Ljava/lang/String;
    //   1447: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1450: pop
    //   1451: aload_1
    //   1452: invokevirtual performPause : ()V
    //   1455: aload_0
    //   1456: aload_1
    //   1457: iconst_0
    //   1458: invokevirtual dispatchOnFragmentPaused : (Landroid/support/v4/app/Fragment;Z)V
    //   1461: iload_2
    //   1462: iconst_4
    //   1463: if_icmpge -> 1508
    //   1466: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1469: ifeq -> 1498
    //   1472: ldc 'FragmentManager'
    //   1474: new java/lang/StringBuilder
    //   1477: dup
    //   1478: invokespecial <init> : ()V
    //   1481: ldc_w 'movefrom STARTED: '
    //   1484: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1487: aload_1
    //   1488: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1491: invokevirtual toString : ()Ljava/lang/String;
    //   1494: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1497: pop
    //   1498: aload_1
    //   1499: invokevirtual performStop : ()V
    //   1502: aload_0
    //   1503: aload_1
    //   1504: iconst_0
    //   1505: invokevirtual dispatchOnFragmentStopped : (Landroid/support/v4/app/Fragment;Z)V
    //   1508: iload_2
    //   1509: iconst_3
    //   1510: if_icmpge -> 1549
    //   1513: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1516: ifeq -> 1545
    //   1519: ldc 'FragmentManager'
    //   1521: new java/lang/StringBuilder
    //   1524: dup
    //   1525: invokespecial <init> : ()V
    //   1528: ldc_w 'movefrom STOPPED: '
    //   1531: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1534: aload_1
    //   1535: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1538: invokevirtual toString : ()Ljava/lang/String;
    //   1541: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1544: pop
    //   1545: aload_1
    //   1546: invokevirtual performReallyStop : ()V
    //   1549: iload_2
    //   1550: iconst_2
    //   1551: if_icmpge -> 1358
    //   1554: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1557: ifeq -> 1586
    //   1560: ldc 'FragmentManager'
    //   1562: new java/lang/StringBuilder
    //   1565: dup
    //   1566: invokespecial <init> : ()V
    //   1569: ldc_w 'movefrom ACTIVITY_CREATED: '
    //   1572: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1575: aload_1
    //   1576: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1579: invokevirtual toString : ()Ljava/lang/String;
    //   1582: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1585: pop
    //   1586: aload_1
    //   1587: getfield mView : Landroid/view/View;
    //   1590: ifnull -> 1616
    //   1593: aload_0
    //   1594: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   1597: aload_1
    //   1598: invokevirtual onShouldSaveFragmentState : (Landroid/support/v4/app/Fragment;)Z
    //   1601: ifeq -> 1616
    //   1604: aload_1
    //   1605: getfield mSavedViewState : Landroid/util/SparseArray;
    //   1608: ifnonnull -> 1616
    //   1611: aload_0
    //   1612: aload_1
    //   1613: invokevirtual saveFragmentViewState : (Landroid/support/v4/app/Fragment;)V
    //   1616: aload_1
    //   1617: invokevirtual performDestroyView : ()V
    //   1620: aload_0
    //   1621: aload_1
    //   1622: iconst_0
    //   1623: invokevirtual dispatchOnFragmentViewDestroyed : (Landroid/support/v4/app/Fragment;Z)V
    //   1626: aload_1
    //   1627: getfield mView : Landroid/view/View;
    //   1630: ifnull -> 1748
    //   1633: aload_1
    //   1634: getfield mContainer : Landroid/view/ViewGroup;
    //   1637: ifnull -> 1748
    //   1640: aconst_null
    //   1641: astore #9
    //   1643: aload #9
    //   1645: astore #8
    //   1647: aload_0
    //   1648: getfield mCurState : I
    //   1651: ifle -> 1690
    //   1654: aload #9
    //   1656: astore #8
    //   1658: aload_0
    //   1659: getfield mDestroyed : Z
    //   1662: ifne -> 1690
    //   1665: aload #9
    //   1667: astore #8
    //   1669: aload_1
    //   1670: getfield mView : Landroid/view/View;
    //   1673: invokevirtual getVisibility : ()I
    //   1676: ifne -> 1690
    //   1679: aload_0
    //   1680: aload_1
    //   1681: iload_3
    //   1682: iconst_0
    //   1683: iload #4
    //   1685: invokevirtual loadAnimation : (Landroid/support/v4/app/Fragment;IZI)Landroid/view/animation/Animation;
    //   1688: astore #8
    //   1690: aload #8
    //   1692: ifnull -> 1737
    //   1695: aload_1
    //   1696: aload_1
    //   1697: getfield mView : Landroid/view/View;
    //   1700: invokevirtual setAnimatingAway : (Landroid/view/View;)V
    //   1703: aload_1
    //   1704: iload_2
    //   1705: invokevirtual setStateAfterAnimating : (I)V
    //   1708: aload #8
    //   1710: new android/support/v4/app/FragmentManagerImpl$2
    //   1713: dup
    //   1714: aload_0
    //   1715: aload_1
    //   1716: getfield mView : Landroid/view/View;
    //   1719: aload #8
    //   1721: aload_1
    //   1722: invokespecial <init> : (Landroid/support/v4/app/FragmentManagerImpl;Landroid/view/View;Landroid/view/animation/Animation;Landroid/support/v4/app/Fragment;)V
    //   1725: invokevirtual setAnimationListener : (Landroid/view/animation/Animation$AnimationListener;)V
    //   1728: aload_1
    //   1729: getfield mView : Landroid/view/View;
    //   1732: aload #8
    //   1734: invokevirtual startAnimation : (Landroid/view/animation/Animation;)V
    //   1737: aload_1
    //   1738: getfield mContainer : Landroid/view/ViewGroup;
    //   1741: aload_1
    //   1742: getfield mView : Landroid/view/View;
    //   1745: invokevirtual removeView : (Landroid/view/View;)V
    //   1748: aload_1
    //   1749: aconst_null
    //   1750: putfield mContainer : Landroid/view/ViewGroup;
    //   1753: aload_1
    //   1754: aconst_null
    //   1755: putfield mView : Landroid/view/View;
    //   1758: aload_1
    //   1759: aconst_null
    //   1760: putfield mInnerView : Landroid/view/View;
    //   1763: goto -> 1358
    //   1766: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1769: ifeq -> 1798
    //   1772: ldc 'FragmentManager'
    //   1774: new java/lang/StringBuilder
    //   1777: dup
    //   1778: invokespecial <init> : ()V
    //   1781: ldc_w 'movefrom CREATED: '
    //   1784: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1787: aload_1
    //   1788: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1791: invokevirtual toString : ()Ljava/lang/String;
    //   1794: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1797: pop
    //   1798: aload_1
    //   1799: getfield mRetaining : Z
    //   1802: ifne -> 1851
    //   1805: aload_1
    //   1806: invokevirtual performDestroy : ()V
    //   1809: aload_0
    //   1810: aload_1
    //   1811: iconst_0
    //   1812: invokevirtual dispatchOnFragmentDestroyed : (Landroid/support/v4/app/Fragment;Z)V
    //   1815: aload_1
    //   1816: invokevirtual performDetach : ()V
    //   1819: aload_0
    //   1820: aload_1
    //   1821: iconst_0
    //   1822: invokevirtual dispatchOnFragmentDetached : (Landroid/support/v4/app/Fragment;Z)V
    //   1825: iload_2
    //   1826: istore #7
    //   1828: iload #5
    //   1830: ifne -> 191
    //   1833: aload_1
    //   1834: getfield mRetaining : Z
    //   1837: ifne -> 1859
    //   1840: aload_0
    //   1841: aload_1
    //   1842: invokevirtual makeInactive : (Landroid/support/v4/app/Fragment;)V
    //   1845: iload_2
    //   1846: istore #7
    //   1848: goto -> 191
    //   1851: aload_1
    //   1852: iconst_0
    //   1853: putfield mState : I
    //   1856: goto -> 1815
    //   1859: aload_1
    //   1860: aconst_null
    //   1861: putfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   1864: aload_1
    //   1865: aconst_null
    //   1866: putfield mParentFragment : Landroid/support/v4/app/Fragment;
    //   1869: aload_1
    //   1870: aconst_null
    //   1871: putfield mFragmentManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   1874: iload_2
    //   1875: istore #7
    //   1877: goto -> 191
    // Exception table:
    //   from	to	target	type
    //   835	848	1272	android/content/res/Resources$NotFoundException
  }
  
  public void noteStateNotSaved() {
    this.mStateSaved = false;
  }
  
  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    View view;
    String str1 = null;
    if (!"fragment".equals(paramString))
      return (View)str1; 
    paramString = paramAttributeSet.getAttributeValue(null, "class");
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, FragmentTag.Fragment);
    String str2 = paramString;
    if (paramString == null)
      str2 = typedArray.getString(0); 
    int i = typedArray.getResourceId(1, -1);
    String str3 = typedArray.getString(2);
    typedArray.recycle();
    paramString = str1;
    if (Fragment.isSupportFragmentClass(this.mHost.getContext(), str2)) {
      Fragment fragment2;
      boolean bool;
      if (paramView != null) {
        bool = paramView.getId();
      } else {
        bool = false;
      } 
      if (bool == -1 && i == -1 && str3 == null)
        throw new IllegalArgumentException(paramAttributeSet.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + str2); 
      if (i != -1) {
        Fragment fragment = findFragmentById(i);
      } else {
        paramView = null;
      } 
      View view1 = paramView;
      if (paramView == null) {
        view1 = paramView;
        if (str3 != null)
          fragment2 = findFragmentByTag(str3); 
      } 
      Fragment fragment1 = fragment2;
      if (fragment2 == null) {
        fragment1 = fragment2;
        if (bool != -1)
          fragment1 = findFragmentById(bool); 
      } 
      if (DEBUG)
        Log.v("FragmentManager", "onCreateView: id=0x" + Integer.toHexString(i) + " fname=" + str2 + " existing=" + fragment1); 
      if (fragment1 == null) {
        boolean bool1;
        fragment2 = Fragment.instantiate(paramContext, str2);
        fragment2.mFromLayout = true;
        if (i != 0) {
          bool1 = i;
        } else {
          bool1 = bool;
        } 
        fragment2.mFragmentId = bool1;
        fragment2.mContainerId = bool;
        fragment2.mTag = str3;
        fragment2.mInLayout = true;
        fragment2.mFragmentManager = this;
        fragment2.mHost = this.mHost;
        fragment2.onInflate(this.mHost.getContext(), paramAttributeSet, fragment2.mSavedFragmentState);
        addFragment(fragment2, true);
      } else {
        if (fragment1.mInLayout)
          throw new IllegalArgumentException(paramAttributeSet.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(i) + ", tag " + str3 + ", or parent id 0x" + Integer.toHexString(bool) + " with another fragment for " + str2); 
        fragment1.mInLayout = true;
        fragment1.mHost = this.mHost;
        fragment2 = fragment1;
        if (!fragment1.mRetaining) {
          fragment1.onInflate(this.mHost.getContext(), paramAttributeSet, fragment1.mSavedFragmentState);
          fragment2 = fragment1;
        } 
      } 
      if (this.mCurState < 1 && fragment2.mFromLayout) {
        moveToState(fragment2, 1, 0, 0, false);
      } else {
        moveToState(fragment2);
      } 
      if (fragment2.mView == null)
        throw new IllegalStateException("Fragment " + str2 + " did not create a view."); 
      if (i != 0)
        fragment2.mView.setId(i); 
      if (fragment2.mView.getTag() == null)
        fragment2.mView.setTag(str3); 
      view = fragment2.mView;
    } 
    return view;
  }
  
  public void performPendingDeferredStart(Fragment paramFragment) {
    if (paramFragment.mDeferStart) {
      if (this.mExecutingActions) {
        this.mHavePendingDeferredStart = true;
        return;
      } 
    } else {
      return;
    } 
    paramFragment.mDeferStart = false;
    moveToState(paramFragment, this.mCurState, 0, 0, false);
  }
  
  public void popBackStack() {
    enqueueAction(new PopBackStackState(null, -1, 0), false);
  }
  
  public void popBackStack(int paramInt1, int paramInt2) {
    if (paramInt1 < 0)
      throw new IllegalArgumentException("Bad id: " + paramInt1); 
    enqueueAction(new PopBackStackState(null, paramInt1, paramInt2), false);
  }
  
  public void popBackStack(String paramString, int paramInt) {
    enqueueAction(new PopBackStackState(paramString, -1, paramInt), false);
  }
  
  public boolean popBackStackImmediate() {
    checkStateLoss();
    return popBackStackImmediate(null, -1, 0);
  }
  
  public boolean popBackStackImmediate(int paramInt1, int paramInt2) {
    checkStateLoss();
    execPendingActions();
    if (paramInt1 < 0)
      throw new IllegalArgumentException("Bad id: " + paramInt1); 
    return popBackStackImmediate(null, paramInt1, paramInt2);
  }
  
  public boolean popBackStackImmediate(String paramString, int paramInt) {
    checkStateLoss();
    return popBackStackImmediate(paramString, -1, paramInt);
  }
  
  boolean popBackStackState(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, String paramString, int paramInt1, int paramInt2) {
    boolean bool = false;
    if (this.mBackStack == null)
      return bool; 
    if (paramString == null && paramInt1 < 0 && (paramInt2 & 0x1) == 0) {
      paramInt1 = this.mBackStack.size() - 1;
      boolean bool1 = bool;
      if (paramInt1 >= 0) {
        paramArrayList.add(this.mBackStack.remove(paramInt1));
        paramArrayList1.add(Boolean.valueOf(true));
      } else {
        return bool1;
      } 
    } else {
      int i = -1;
      if (paramString != null || paramInt1 >= 0) {
        int j = this.mBackStack.size() - 1;
        while (true) {
          if (j >= 0) {
            BackStackRecord backStackRecord = this.mBackStack.get(j);
            if ((paramString == null || !paramString.equals(backStackRecord.getName())) && (paramInt1 < 0 || paramInt1 != backStackRecord.mIndex)) {
              j--;
              continue;
            } 
          } 
          boolean bool2 = bool;
          if (j >= 0) {
            i = j;
            if ((paramInt2 & 0x1) != 0)
              for (paramInt2 = j - 1;; paramInt2--) {
                i = paramInt2;
                if (paramInt2 >= 0) {
                  BackStackRecord backStackRecord = this.mBackStack.get(paramInt2);
                  if (paramString == null || !paramString.equals(backStackRecord.getName())) {
                    i = paramInt2;
                    if (paramInt1 >= 0) {
                      i = paramInt2;
                      if (paramInt1 == backStackRecord.mIndex)
                        continue; 
                    } 
                    break;
                  } 
                  continue;
                } 
                break;
              }  
            break;
          } 
          return bool2;
        } 
      } 
      boolean bool1 = bool;
      if (i != this.mBackStack.size() - 1) {
        paramInt1 = this.mBackStack.size() - 1;
        while (true) {
          if (paramInt1 > i) {
            paramArrayList.add(this.mBackStack.remove(paramInt1));
            paramArrayList1.add(Boolean.valueOf(true));
            paramInt1--;
            continue;
          } 
          return true;
        } 
      } 
      return bool1;
    } 
    return true;
  }
  
  public void putFragment(Bundle paramBundle, String paramString, Fragment paramFragment) {
    if (paramFragment.mIndex < 0)
      throwException(new IllegalStateException("Fragment " + paramFragment + " is not currently in the FragmentManager")); 
    paramBundle.putInt(paramString, paramFragment.mIndex);
  }
  
  public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks, boolean paramBoolean) {
    if (this.mLifecycleCallbacks == null)
      this.mLifecycleCallbacks = new CopyOnWriteArrayList<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>>(); 
    this.mLifecycleCallbacks.add(new Pair(paramFragmentLifecycleCallbacks, Boolean.valueOf(paramBoolean)));
  }
  
  public void removeFragment(Fragment paramFragment) {
    boolean bool;
    if (DEBUG)
      Log.v("FragmentManager", "remove: " + paramFragment + " nesting=" + paramFragment.mBackStackNesting); 
    if (!paramFragment.isInBackStack()) {
      bool = true;
    } else {
      bool = false;
    } 
    if (!paramFragment.mDetached || bool) {
      if (this.mAdded != null)
        this.mAdded.remove(paramFragment); 
      if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
        this.mNeedMenuInvalidate = true; 
      paramFragment.mAdded = false;
      paramFragment.mRemoving = true;
    } 
  }
  
  public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    if (this.mBackStackChangeListeners != null)
      this.mBackStackChangeListeners.remove(paramOnBackStackChangedListener); 
  }
  
  void reportBackStackChanged() {
    if (this.mBackStackChangeListeners != null)
      for (byte b = 0; b < this.mBackStackChangeListeners.size(); b++)
        ((FragmentManager.OnBackStackChangedListener)this.mBackStackChangeListeners.get(b)).onBackStackChanged();  
  }
  
  void restoreAllState(Parcelable paramParcelable, FragmentManagerNonConfig paramFragmentManagerNonConfig) {
    if (paramParcelable != null) {
      FragmentManagerState fragmentManagerState = (FragmentManagerState)paramParcelable;
      if (fragmentManagerState.mActive != null) {
        Fragment fragment;
        paramParcelable = null;
        if (paramFragmentManagerNonConfig != null) {
          byte b1;
          List<Fragment> list = paramFragmentManagerNonConfig.getFragments();
          List<FragmentManagerNonConfig> list1 = paramFragmentManagerNonConfig.getChildNonConfigs();
          if (list != null) {
            b1 = list.size();
          } else {
            b1 = 0;
          } 
          byte b2 = 0;
          while (true) {
            List<FragmentManagerNonConfig> list2 = list1;
            if (b2 < b1) {
              fragment = list.get(b2);
              if (DEBUG)
                Log.v("FragmentManager", "restoreAllState: re-attaching retained " + fragment); 
              FragmentState fragmentState = fragmentManagerState.mActive[fragment.mIndex];
              fragmentState.mInstance = fragment;
              fragment.mSavedViewState = null;
              fragment.mBackStackNesting = 0;
              fragment.mInLayout = false;
              fragment.mAdded = false;
              fragment.mTarget = null;
              if (fragmentState.mSavedFragmentState != null) {
                fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                fragment.mSavedViewState = fragmentState.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                fragment.mSavedFragmentState = fragmentState.mSavedFragmentState;
              } 
              b2++;
              continue;
            } 
            break;
          } 
        } 
        this.mActive = new ArrayList<Fragment>(fragmentManagerState.mActive.length);
        if (this.mAvailIndices != null)
          this.mAvailIndices.clear(); 
        int i;
        for (i = 0; i < fragmentManagerState.mActive.length; i++) {
          FragmentState fragmentState = fragmentManagerState.mActive[i];
          if (fragmentState != null) {
            FragmentManagerNonConfig fragmentManagerNonConfig1 = null;
            FragmentManagerNonConfig fragmentManagerNonConfig2 = fragmentManagerNonConfig1;
            if (fragment != null) {
              fragmentManagerNonConfig2 = fragmentManagerNonConfig1;
              if (i < fragment.size())
                fragmentManagerNonConfig2 = fragment.get(i); 
            } 
            Fragment fragment1 = fragmentState.instantiate(this.mHost, this.mParent, fragmentManagerNonConfig2);
            if (DEBUG)
              Log.v("FragmentManager", "restoreAllState: active #" + i + ": " + fragment1); 
            this.mActive.add(fragment1);
            fragmentState.mInstance = null;
          } else {
            this.mActive.add(null);
            if (this.mAvailIndices == null)
              this.mAvailIndices = new ArrayList<Integer>(); 
            if (DEBUG)
              Log.v("FragmentManager", "restoreAllState: avail #" + i); 
            this.mAvailIndices.add(Integer.valueOf(i));
          } 
        } 
        if (paramFragmentManagerNonConfig != null) {
          List<Fragment> list = paramFragmentManagerNonConfig.getFragments();
          if (list != null) {
            i = list.size();
          } else {
            i = 0;
          } 
          for (byte b = 0; b < i; b++) {
            Fragment fragment1 = list.get(b);
            if (fragment1.mTargetIndex >= 0)
              if (fragment1.mTargetIndex < this.mActive.size()) {
                fragment1.mTarget = this.mActive.get(fragment1.mTargetIndex);
              } else {
                Log.w("FragmentManager", "Re-attaching retained fragment " + fragment1 + " target no longer exists: " + fragment1.mTargetIndex);
                fragment1.mTarget = null;
              }  
          } 
        } 
        if (fragmentManagerState.mAdded != null) {
          this.mAdded = new ArrayList<Fragment>(fragmentManagerState.mAdded.length);
          for (i = 0; i < fragmentManagerState.mAdded.length; i++) {
            fragment = this.mActive.get(fragmentManagerState.mAdded[i]);
            if (fragment == null)
              throwException(new IllegalStateException("No instantiated fragment for index #" + fragmentManagerState.mAdded[i])); 
            fragment.mAdded = true;
            if (DEBUG)
              Log.v("FragmentManager", "restoreAllState: added #" + i + ": " + fragment); 
            if (this.mAdded.contains(fragment))
              throw new IllegalStateException("Already added!"); 
            this.mAdded.add(fragment);
          } 
        } else {
          this.mAdded = null;
        } 
        if (fragmentManagerState.mBackStack != null) {
          this.mBackStack = new ArrayList<BackStackRecord>(fragmentManagerState.mBackStack.length);
          i = 0;
          while (true) {
            if (i < fragmentManagerState.mBackStack.length) {
              BackStackRecord backStackRecord = fragmentManagerState.mBackStack[i].instantiate(this);
              if (DEBUG) {
                Log.v("FragmentManager", "restoreAllState: back stack #" + i + " (index " + backStackRecord.mIndex + "): " + backStackRecord);
                backStackRecord.dump("  ", new PrintWriter((Writer)new LogWriter("FragmentManager")), false);
              } 
              this.mBackStack.add(backStackRecord);
              if (backStackRecord.mIndex >= 0)
                setBackStackIndex(backStackRecord.mIndex, backStackRecord); 
              i++;
              continue;
            } 
            return;
          } 
        } 
        this.mBackStack = null;
      } 
    } 
  }
  
  FragmentManagerNonConfig retainNonConfig() {
    ArrayList<Fragment> arrayList1 = null;
    ArrayList<Fragment> arrayList2 = null;
    ArrayList<Fragment> arrayList3 = null;
    ArrayList<Fragment> arrayList4 = null;
    if (this.mActive != null) {
      byte b = 0;
      while (true) {
        arrayList3 = arrayList4;
        arrayList1 = arrayList2;
        if (b < this.mActive.size()) {
          Fragment fragment = this.mActive.get(b);
          ArrayList<Fragment> arrayList = arrayList4;
          arrayList1 = arrayList2;
          if (fragment != null) {
            arrayList3 = arrayList2;
            if (fragment.mRetainInstance) {
              byte b3;
              arrayList1 = arrayList2;
              if (arrayList2 == null)
                arrayList1 = new ArrayList(); 
              arrayList1.add(fragment);
              fragment.mRetaining = true;
              if (fragment.mTarget != null) {
                b3 = fragment.mTarget.mIndex;
              } else {
                b3 = -1;
              } 
              fragment.mTargetIndex = b3;
              arrayList3 = arrayList1;
              if (DEBUG) {
                Log.v("FragmentManager", "retainNonConfig: keeping retained " + fragment);
                arrayList3 = arrayList1;
              } 
            } 
            byte b2 = 0;
            byte b1 = b2;
            arrayList2 = arrayList4;
            if (fragment.mChildFragmentManager != null) {
              FragmentManagerNonConfig fragmentManagerNonConfig = fragment.mChildFragmentManager.retainNonConfig();
              b1 = b2;
              arrayList2 = arrayList4;
              if (fragmentManagerNonConfig != null) {
                arrayList2 = arrayList4;
                if (arrayList4 == null) {
                  arrayList4 = new ArrayList<Fragment>();
                  b1 = 0;
                  while (true) {
                    arrayList2 = arrayList4;
                    if (b1 < b) {
                      arrayList4.add(null);
                      b1++;
                      continue;
                    } 
                    break;
                  } 
                } 
                arrayList2.add(fragmentManagerNonConfig);
                b1 = 1;
              } 
            } 
            arrayList = arrayList2;
            arrayList1 = arrayList3;
            if (arrayList2 != null) {
              arrayList = arrayList2;
              arrayList1 = arrayList3;
              if (b1 == 0) {
                arrayList2.add(null);
                arrayList1 = arrayList3;
                arrayList = arrayList2;
              } 
            } 
          } 
          b++;
          arrayList4 = arrayList;
          arrayList2 = arrayList1;
          continue;
        } 
        break;
      } 
    } 
    return (arrayList1 == null && arrayList3 == null) ? null : new FragmentManagerNonConfig(arrayList1, (List)arrayList3);
  }
  
  Parcelable saveAllState() {
    BackStackState[] arrayOfBackStackState;
    int[] arrayOfInt;
    Parcelable parcelable1 = null;
    forcePostponedTransactions();
    endAnimatingAwayFragments();
    execPendingActions();
    if (HONEYCOMB)
      this.mStateSaved = true; 
    Parcelable parcelable2 = parcelable1;
    if (this.mActive != null) {
      if (this.mActive.size() <= 0)
        return parcelable1; 
    } else {
      return parcelable2;
    } 
    int i = this.mActive.size();
    FragmentState[] arrayOfFragmentState = new FragmentState[i];
    int j = 0;
    byte b;
    for (b = 0; b < i; b++) {
      Fragment fragment = this.mActive.get(b);
      if (fragment != null) {
        if (fragment.mIndex < 0)
          throwException(new IllegalStateException("Failure saving state: active " + fragment + " has cleared index: " + fragment.mIndex)); 
        byte b1 = 1;
        FragmentState fragmentState = new FragmentState(fragment);
        arrayOfFragmentState[b] = fragmentState;
        if (fragment.mState > 0 && fragmentState.mSavedFragmentState == null) {
          fragmentState.mSavedFragmentState = saveFragmentBasicState(fragment);
          if (fragment.mTarget != null) {
            if (fragment.mTarget.mIndex < 0)
              throwException(new IllegalStateException("Failure saving state: " + fragment + " has target not in fragment manager: " + fragment.mTarget)); 
            if (fragmentState.mSavedFragmentState == null)
              fragmentState.mSavedFragmentState = new Bundle(); 
            putFragment(fragmentState.mSavedFragmentState, "android:target_state", fragment.mTarget);
            if (fragment.mTargetRequestCode != 0)
              fragmentState.mSavedFragmentState.putInt("android:target_req_state", fragment.mTargetRequestCode); 
          } 
        } else {
          fragmentState.mSavedFragmentState = fragment.mSavedFragmentState;
        } 
        j = b1;
        if (DEBUG) {
          Log.v("FragmentManager", "Saved state of " + fragment + ": " + fragmentState.mSavedFragmentState);
          j = b1;
        } 
      } 
    } 
    if (!j) {
      parcelable2 = parcelable1;
      if (DEBUG) {
        Log.v("FragmentManager", "saveAllState: no fragments!");
        parcelable2 = parcelable1;
      } 
      return parcelable2;
    } 
    parcelable1 = null;
    Parcelable parcelable3 = null;
    parcelable2 = parcelable1;
    if (this.mAdded != null) {
      j = this.mAdded.size();
      parcelable2 = parcelable1;
      if (j > 0) {
        int[] arrayOfInt1 = new int[j];
        b = 0;
        while (true) {
          arrayOfInt = arrayOfInt1;
          if (b < j) {
            arrayOfInt1[b] = ((Fragment)this.mAdded.get(b)).mIndex;
            if (arrayOfInt1[b] < 0)
              throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(b) + " has cleared index: " + arrayOfInt1[b])); 
            if (DEBUG)
              Log.v("FragmentManager", "saveAllState: adding fragment #" + b + ": " + this.mAdded.get(b)); 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    parcelable1 = parcelable3;
    if (this.mBackStack != null) {
      j = this.mBackStack.size();
      parcelable1 = parcelable3;
      if (j > 0) {
        BackStackState[] arrayOfBackStackState1 = new BackStackState[j];
        b = 0;
        while (true) {
          arrayOfBackStackState = arrayOfBackStackState1;
          if (b < j) {
            arrayOfBackStackState1[b] = new BackStackState(this.mBackStack.get(b));
            if (DEBUG)
              Log.v("FragmentManager", "saveAllState: adding back stack #" + b + ": " + this.mBackStack.get(b)); 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    parcelable3 = new FragmentManagerState();
    ((FragmentManagerState)parcelable3).mActive = arrayOfFragmentState;
    ((FragmentManagerState)parcelable3).mAdded = arrayOfInt;
    ((FragmentManagerState)parcelable3).mBackStack = arrayOfBackStackState;
    return parcelable3;
  }
  
  Bundle saveFragmentBasicState(Fragment paramFragment) {
    Bundle bundle1 = null;
    if (this.mStateBundle == null)
      this.mStateBundle = new Bundle(); 
    paramFragment.performSaveInstanceState(this.mStateBundle);
    dispatchOnFragmentSaveInstanceState(paramFragment, this.mStateBundle, false);
    if (!this.mStateBundle.isEmpty()) {
      bundle1 = this.mStateBundle;
      this.mStateBundle = null;
    } 
    if (paramFragment.mView != null)
      saveFragmentViewState(paramFragment); 
    Bundle bundle2 = bundle1;
    if (paramFragment.mSavedViewState != null) {
      bundle2 = bundle1;
      if (bundle1 == null)
        bundle2 = new Bundle(); 
      bundle2.putSparseParcelableArray("android:view_state", paramFragment.mSavedViewState);
    } 
    bundle1 = bundle2;
    if (!paramFragment.mUserVisibleHint) {
      bundle1 = bundle2;
      if (bundle2 == null)
        bundle1 = new Bundle(); 
      bundle1.putBoolean("android:user_visible_hint", paramFragment.mUserVisibleHint);
    } 
    return bundle1;
  }
  
  public Fragment.SavedState saveFragmentInstanceState(Fragment paramFragment) {
    Fragment.SavedState savedState1 = null;
    if (paramFragment.mIndex < 0)
      throwException(new IllegalStateException("Fragment " + paramFragment + " is not currently in the FragmentManager")); 
    Fragment.SavedState savedState2 = savedState1;
    if (paramFragment.mState > 0) {
      Bundle bundle = saveFragmentBasicState(paramFragment);
      savedState2 = savedState1;
      if (bundle != null)
        savedState2 = new Fragment.SavedState(bundle); 
    } 
    return savedState2;
  }
  
  void saveFragmentViewState(Fragment paramFragment) {
    if (paramFragment.mInnerView != null) {
      if (this.mStateArray == null) {
        this.mStateArray = new SparseArray();
      } else {
        this.mStateArray.clear();
      } 
      paramFragment.mInnerView.saveHierarchyState(this.mStateArray);
      if (this.mStateArray.size() > 0) {
        paramFragment.mSavedViewState = this.mStateArray;
        this.mStateArray = null;
      } 
    } 
  }
  
  public void setBackStackIndex(int paramInt, BackStackRecord paramBackStackRecord) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnonnull -> 22
    //   9: new java/util/ArrayList
    //   12: astore_3
    //   13: aload_3
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: aload_3
    //   19: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   22: aload_0
    //   23: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   26: invokevirtual size : ()I
    //   29: istore #4
    //   31: iload #4
    //   33: istore #5
    //   35: iload_1
    //   36: iload #4
    //   38: if_icmpge -> 98
    //   41: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   44: ifeq -> 85
    //   47: new java/lang/StringBuilder
    //   50: astore_3
    //   51: aload_3
    //   52: invokespecial <init> : ()V
    //   55: ldc 'FragmentManager'
    //   57: aload_3
    //   58: ldc_w 'Setting back stack index '
    //   61: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: iload_1
    //   65: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   68: ldc_w ' to '
    //   71: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: aload_2
    //   75: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   78: invokevirtual toString : ()Ljava/lang/String;
    //   81: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   84: pop
    //   85: aload_0
    //   86: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   89: iload_1
    //   90: aload_2
    //   91: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   94: pop
    //   95: aload_0
    //   96: monitorexit
    //   97: return
    //   98: iload #5
    //   100: iload_1
    //   101: if_icmpge -> 187
    //   104: aload_0
    //   105: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   108: aconst_null
    //   109: invokevirtual add : (Ljava/lang/Object;)Z
    //   112: pop
    //   113: aload_0
    //   114: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   117: ifnonnull -> 133
    //   120: new java/util/ArrayList
    //   123: astore_3
    //   124: aload_3
    //   125: invokespecial <init> : ()V
    //   128: aload_0
    //   129: aload_3
    //   130: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   133: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   136: ifeq -> 168
    //   139: new java/lang/StringBuilder
    //   142: astore_3
    //   143: aload_3
    //   144: invokespecial <init> : ()V
    //   147: ldc 'FragmentManager'
    //   149: aload_3
    //   150: ldc_w 'Adding available back stack index '
    //   153: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   156: iload #5
    //   158: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   161: invokevirtual toString : ()Ljava/lang/String;
    //   164: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   167: pop
    //   168: aload_0
    //   169: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   172: iload #5
    //   174: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   177: invokevirtual add : (Ljava/lang/Object;)Z
    //   180: pop
    //   181: iinc #5, 1
    //   184: goto -> 98
    //   187: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   190: ifeq -> 231
    //   193: new java/lang/StringBuilder
    //   196: astore_3
    //   197: aload_3
    //   198: invokespecial <init> : ()V
    //   201: ldc 'FragmentManager'
    //   203: aload_3
    //   204: ldc_w 'Adding back stack index '
    //   207: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: iload_1
    //   211: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   214: ldc_w ' with '
    //   217: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: aload_2
    //   221: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   224: invokevirtual toString : ()Ljava/lang/String;
    //   227: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   230: pop
    //   231: aload_0
    //   232: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   235: aload_2
    //   236: invokevirtual add : (Ljava/lang/Object;)Z
    //   239: pop
    //   240: goto -> 95
    //   243: astore_2
    //   244: aload_0
    //   245: monitorexit
    //   246: aload_2
    //   247: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	243	finally
    //   22	31	243	finally
    //   41	85	243	finally
    //   85	95	243	finally
    //   95	97	243	finally
    //   104	133	243	finally
    //   133	168	243	finally
    //   168	181	243	finally
    //   187	231	243	finally
    //   231	240	243	finally
    //   244	246	243	finally
  }
  
  public void showFragment(Fragment paramFragment) {
    boolean bool = false;
    if (DEBUG)
      Log.v("FragmentManager", "show: " + paramFragment); 
    if (paramFragment.mHidden) {
      paramFragment.mHidden = false;
      if (!paramFragment.mHiddenChanged)
        bool = true; 
      paramFragment.mHiddenChanged = bool;
    } 
  }
  
  void startPendingDeferredFragments() {
    if (this.mActive != null) {
      byte b = 0;
      while (true) {
        if (b < this.mActive.size()) {
          Fragment fragment = this.mActive.get(b);
          if (fragment != null)
            performPendingDeferredStart(fragment); 
          b++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append("FragmentManager{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append(" in ");
    if (this.mParent != null) {
      DebugUtils.buildShortClassTag(this.mParent, stringBuilder);
      stringBuilder.append("}}");
      return stringBuilder.toString();
    } 
    DebugUtils.buildShortClassTag(this.mHost, stringBuilder);
    stringBuilder.append("}}");
    return stringBuilder.toString();
  }
  
  public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   4: ifnonnull -> 8
    //   7: return
    //   8: aload_0
    //   9: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   12: astore_2
    //   13: aload_2
    //   14: monitorenter
    //   15: iconst_0
    //   16: istore_3
    //   17: aload_0
    //   18: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   21: invokevirtual size : ()I
    //   24: istore #4
    //   26: iload_3
    //   27: iload #4
    //   29: if_icmpge -> 59
    //   32: aload_0
    //   33: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   36: iload_3
    //   37: invokevirtual get : (I)Ljava/lang/Object;
    //   40: checkcast android/support/v4/util/Pair
    //   43: getfield first : Ljava/lang/Object;
    //   46: aload_1
    //   47: if_acmpne -> 69
    //   50: aload_0
    //   51: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   54: iload_3
    //   55: invokevirtual remove : (I)Ljava/lang/Object;
    //   58: pop
    //   59: aload_2
    //   60: monitorexit
    //   61: goto -> 7
    //   64: astore_1
    //   65: aload_2
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    //   69: iinc #3, 1
    //   72: goto -> 26
    // Exception table:
    //   from	to	target	type
    //   17	26	64	finally
    //   32	59	64	finally
    //   59	61	64	finally
    //   65	67	64	finally
  }
  
  static {
    boolean bool = false;
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 11)
      bool = true; 
    HONEYCOMB = bool;
  }
  
  static class AnimateOnHWLayerIfNeededListener implements Animation.AnimationListener {
    private Animation.AnimationListener mOriginalListener;
    
    private boolean mShouldRunOnHWLayer;
    
    View mView;
    
    public AnimateOnHWLayerIfNeededListener(View param1View, Animation param1Animation) {
      if (param1View != null && param1Animation != null)
        this.mView = param1View; 
    }
    
    public AnimateOnHWLayerIfNeededListener(View param1View, Animation param1Animation, Animation.AnimationListener param1AnimationListener) {
      if (param1View != null && param1Animation != null) {
        this.mOriginalListener = param1AnimationListener;
        this.mView = param1View;
        this.mShouldRunOnHWLayer = true;
      } 
    }
    
    @CallSuper
    public void onAnimationEnd(Animation param1Animation) {
      if (this.mView != null && this.mShouldRunOnHWLayer)
        if (ViewCompat.isAttachedToWindow(this.mView) || BuildCompat.isAtLeastN()) {
          this.mView.post(new Runnable() {
                public void run() {
                  ViewCompat.setLayerType(FragmentManagerImpl.AnimateOnHWLayerIfNeededListener.this.mView, 0, null);
                }
              });
        } else {
          ViewCompat.setLayerType(this.mView, 0, null);
        }  
      if (this.mOriginalListener != null)
        this.mOriginalListener.onAnimationEnd(param1Animation); 
    }
    
    public void onAnimationRepeat(Animation param1Animation) {
      if (this.mOriginalListener != null)
        this.mOriginalListener.onAnimationRepeat(param1Animation); 
    }
    
    @CallSuper
    public void onAnimationStart(Animation param1Animation) {
      if (this.mOriginalListener != null)
        this.mOriginalListener.onAnimationStart(param1Animation); 
    }
  }
  
  class null implements Runnable {
    public void run() {
      ViewCompat.setLayerType(this.this$0.mView, 0, null);
    }
  }
  
  static class FragmentTag {
    public static final int[] Fragment = new int[] { 16842755, 16842960, 16842961 };
    
    public static final int Fragment_id = 1;
    
    public static final int Fragment_name = 0;
    
    public static final int Fragment_tag = 2;
  }
  
  static interface OpGenerator {
    boolean generateOps(ArrayList<BackStackRecord> param1ArrayList, ArrayList<Boolean> param1ArrayList1);
  }
  
  private class PopBackStackState implements OpGenerator {
    final int mFlags;
    
    final int mId;
    
    final String mName;
    
    PopBackStackState(String param1String, int param1Int1, int param1Int2) {
      this.mName = param1String;
      this.mId = param1Int1;
      this.mFlags = param1Int2;
    }
    
    public boolean generateOps(ArrayList<BackStackRecord> param1ArrayList, ArrayList<Boolean> param1ArrayList1) {
      return FragmentManagerImpl.this.popBackStackState(param1ArrayList, param1ArrayList1, this.mName, this.mId, this.mFlags);
    }
  }
  
  static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
    private final boolean mIsBack;
    
    private int mNumPostponed;
    
    private final BackStackRecord mRecord;
    
    StartEnterTransitionListener(BackStackRecord param1BackStackRecord, boolean param1Boolean) {
      this.mIsBack = param1Boolean;
      this.mRecord = param1BackStackRecord;
    }
    
    public void cancelTransaction() {
      this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
    }
    
    public void completeTransaction() {
      boolean bool2;
      boolean bool1 = false;
      if (this.mNumPostponed > 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
      int i = fragmentManagerImpl.mAdded.size();
      for (byte b = 0; b < i; b++) {
        Fragment fragment = fragmentManagerImpl.mAdded.get(b);
        fragment.setOnStartEnterTransitionListener(null);
        if (bool2 && fragment.isPostponed())
          fragment.startPostponedEnterTransition(); 
      } 
      fragmentManagerImpl = this.mRecord.mManager;
      BackStackRecord backStackRecord = this.mRecord;
      boolean bool = this.mIsBack;
      if (!bool2)
        bool1 = true; 
      fragmentManagerImpl.completeExecute(backStackRecord, bool, bool1, true);
    }
    
    public boolean isReady() {
      return (this.mNumPostponed == 0);
    }
    
    public void onStartEnterTransition() {
      this.mNumPostponed--;
      if (this.mNumPostponed == 0)
        this.mRecord.mManager.scheduleCommit(); 
    }
    
    public void startListening() {
      this.mNumPostponed++;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/app/FragmentManagerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */