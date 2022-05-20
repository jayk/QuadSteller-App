package android.support.v7.widget;

import android.support.annotation.NonNull;
import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultItemAnimator extends SimpleItemAnimator {
  private static final boolean DEBUG = false;
  
  ArrayList<RecyclerView.ViewHolder> mAddAnimations = new ArrayList<RecyclerView.ViewHolder>();
  
  ArrayList<ArrayList<RecyclerView.ViewHolder>> mAdditionsList = new ArrayList<ArrayList<RecyclerView.ViewHolder>>();
  
  ArrayList<RecyclerView.ViewHolder> mChangeAnimations = new ArrayList<RecyclerView.ViewHolder>();
  
  ArrayList<ArrayList<ChangeInfo>> mChangesList = new ArrayList<ArrayList<ChangeInfo>>();
  
  ArrayList<RecyclerView.ViewHolder> mMoveAnimations = new ArrayList<RecyclerView.ViewHolder>();
  
  ArrayList<ArrayList<MoveInfo>> mMovesList = new ArrayList<ArrayList<MoveInfo>>();
  
  private ArrayList<RecyclerView.ViewHolder> mPendingAdditions = new ArrayList<RecyclerView.ViewHolder>();
  
  private ArrayList<ChangeInfo> mPendingChanges = new ArrayList<ChangeInfo>();
  
  private ArrayList<MoveInfo> mPendingMoves = new ArrayList<MoveInfo>();
  
  private ArrayList<RecyclerView.ViewHolder> mPendingRemovals = new ArrayList<RecyclerView.ViewHolder>();
  
  ArrayList<RecyclerView.ViewHolder> mRemoveAnimations = new ArrayList<RecyclerView.ViewHolder>();
  
  private void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
    final ViewPropertyAnimatorCompat animation = ViewCompat.animate(holder.itemView);
    this.mRemoveAnimations.add(holder);
    viewPropertyAnimatorCompat.setDuration(getRemoveDuration()).alpha(0.0F).setListener(new VpaListenerAdapter() {
          public void onAnimationEnd(View param1View) {
            animation.setListener(null);
            ViewCompat.setAlpha(param1View, 1.0F);
            DefaultItemAnimator.this.dispatchRemoveFinished(holder);
            DefaultItemAnimator.this.mRemoveAnimations.remove(holder);
            DefaultItemAnimator.this.dispatchFinishedWhenDone();
          }
          
          public void onAnimationStart(View param1View) {
            DefaultItemAnimator.this.dispatchRemoveStarting(holder);
          }
        }).start();
  }
  
  private void endChangeAnimation(List<ChangeInfo> paramList, RecyclerView.ViewHolder paramViewHolder) {
    for (int i = paramList.size() - 1; i >= 0; i--) {
      ChangeInfo changeInfo = paramList.get(i);
      if (endChangeAnimationIfNecessary(changeInfo, paramViewHolder) && changeInfo.oldHolder == null && changeInfo.newHolder == null)
        paramList.remove(changeInfo); 
    } 
  }
  
  private void endChangeAnimationIfNecessary(ChangeInfo paramChangeInfo) {
    if (paramChangeInfo.oldHolder != null)
      endChangeAnimationIfNecessary(paramChangeInfo, paramChangeInfo.oldHolder); 
    if (paramChangeInfo.newHolder != null)
      endChangeAnimationIfNecessary(paramChangeInfo, paramChangeInfo.newHolder); 
  }
  
  private boolean endChangeAnimationIfNecessary(ChangeInfo paramChangeInfo, RecyclerView.ViewHolder paramViewHolder) {
    null = false;
    if (paramChangeInfo.newHolder == paramViewHolder) {
      paramChangeInfo.newHolder = null;
    } else if (paramChangeInfo.oldHolder == paramViewHolder) {
      paramChangeInfo.oldHolder = null;
      null = true;
    } else {
      return false;
    } 
    ViewCompat.setAlpha(paramViewHolder.itemView, 1.0F);
    ViewCompat.setTranslationX(paramViewHolder.itemView, 0.0F);
    ViewCompat.setTranslationY(paramViewHolder.itemView, 0.0F);
    dispatchChangeFinished(paramViewHolder, null);
    return true;
  }
  
  private void resetAnimation(RecyclerView.ViewHolder paramViewHolder) {
    AnimatorCompatHelper.clearInterpolator(paramViewHolder.itemView);
    endAnimation(paramViewHolder);
  }
  
  public boolean animateAdd(RecyclerView.ViewHolder paramViewHolder) {
    resetAnimation(paramViewHolder);
    ViewCompat.setAlpha(paramViewHolder.itemView, 0.0F);
    this.mPendingAdditions.add(paramViewHolder);
    return true;
  }
  
  void animateAddImpl(final RecyclerView.ViewHolder holder) {
    final ViewPropertyAnimatorCompat animation = ViewCompat.animate(holder.itemView);
    this.mAddAnimations.add(holder);
    viewPropertyAnimatorCompat.alpha(1.0F).setDuration(getAddDuration()).setListener(new VpaListenerAdapter() {
          public void onAnimationCancel(View param1View) {
            ViewCompat.setAlpha(param1View, 1.0F);
          }
          
          public void onAnimationEnd(View param1View) {
            animation.setListener(null);
            DefaultItemAnimator.this.dispatchAddFinished(holder);
            DefaultItemAnimator.this.mAddAnimations.remove(holder);
            DefaultItemAnimator.this.dispatchFinishedWhenDone();
          }
          
          public void onAnimationStart(View param1View) {
            DefaultItemAnimator.this.dispatchAddStarting(holder);
          }
        }).start();
  }
  
  public boolean animateChange(RecyclerView.ViewHolder paramViewHolder1, RecyclerView.ViewHolder paramViewHolder2, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramViewHolder1 == paramViewHolder2)
      return animateMove(paramViewHolder1, paramInt1, paramInt2, paramInt3, paramInt4); 
    float f1 = ViewCompat.getTranslationX(paramViewHolder1.itemView);
    float f2 = ViewCompat.getTranslationY(paramViewHolder1.itemView);
    float f3 = ViewCompat.getAlpha(paramViewHolder1.itemView);
    resetAnimation(paramViewHolder1);
    int i = (int)((paramInt3 - paramInt1) - f1);
    int j = (int)((paramInt4 - paramInt2) - f2);
    ViewCompat.setTranslationX(paramViewHolder1.itemView, f1);
    ViewCompat.setTranslationY(paramViewHolder1.itemView, f2);
    ViewCompat.setAlpha(paramViewHolder1.itemView, f3);
    if (paramViewHolder2 != null) {
      resetAnimation(paramViewHolder2);
      ViewCompat.setTranslationX(paramViewHolder2.itemView, -i);
      ViewCompat.setTranslationY(paramViewHolder2.itemView, -j);
      ViewCompat.setAlpha(paramViewHolder2.itemView, 0.0F);
    } 
    this.mPendingChanges.add(new ChangeInfo(paramViewHolder1, paramViewHolder2, paramInt1, paramInt2, paramInt3, paramInt4));
    return true;
  }
  
  void animateChangeImpl(final ChangeInfo changeInfo) {
    View view;
    RecyclerView.ViewHolder viewHolder1 = changeInfo.oldHolder;
    if (viewHolder1 == null) {
      viewHolder1 = null;
    } else {
      view = viewHolder1.itemView;
    } 
    final RecyclerView.ViewHolder newView = changeInfo.newHolder;
    if (viewHolder2 != null) {
      View view1 = viewHolder2.itemView;
    } else {
      viewHolder2 = null;
    } 
    if (view != null) {
      final ViewPropertyAnimatorCompat newViewAnimation = ViewCompat.animate(view).setDuration(getChangeDuration());
      this.mChangeAnimations.add(changeInfo.oldHolder);
      viewPropertyAnimatorCompat.translationX((changeInfo.toX - changeInfo.fromX));
      viewPropertyAnimatorCompat.translationY((changeInfo.toY - changeInfo.fromY));
      viewPropertyAnimatorCompat.alpha(0.0F).setListener(new VpaListenerAdapter() {
            public void onAnimationEnd(View param1View) {
              oldViewAnim.setListener(null);
              ViewCompat.setAlpha(param1View, 1.0F);
              ViewCompat.setTranslationX(param1View, 0.0F);
              ViewCompat.setTranslationY(param1View, 0.0F);
              DefaultItemAnimator.this.dispatchChangeFinished(changeInfo.oldHolder, true);
              DefaultItemAnimator.this.mChangeAnimations.remove(changeInfo.oldHolder);
              DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }
            
            public void onAnimationStart(View param1View) {
              DefaultItemAnimator.this.dispatchChangeStarting(changeInfo.oldHolder, true);
            }
          }).start();
    } 
    if (viewHolder2 != null) {
      final ViewPropertyAnimatorCompat newViewAnimation = ViewCompat.animate((View)viewHolder2);
      this.mChangeAnimations.add(changeInfo.newHolder);
      viewPropertyAnimatorCompat.translationX(0.0F).translationY(0.0F).setDuration(getChangeDuration()).alpha(1.0F).setListener(new VpaListenerAdapter() {
            public void onAnimationEnd(View param1View) {
              newViewAnimation.setListener(null);
              ViewCompat.setAlpha(newView, 1.0F);
              ViewCompat.setTranslationX(newView, 0.0F);
              ViewCompat.setTranslationY(newView, 0.0F);
              DefaultItemAnimator.this.dispatchChangeFinished(changeInfo.newHolder, false);
              DefaultItemAnimator.this.mChangeAnimations.remove(changeInfo.newHolder);
              DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }
            
            public void onAnimationStart(View param1View) {
              DefaultItemAnimator.this.dispatchChangeStarting(changeInfo.newHolder, false);
            }
          }).start();
    } 
  }
  
  public boolean animateMove(RecyclerView.ViewHolder paramViewHolder, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    View view = paramViewHolder.itemView;
    paramInt1 = (int)(paramInt1 + ViewCompat.getTranslationX(paramViewHolder.itemView));
    int i = (int)(paramInt2 + ViewCompat.getTranslationY(paramViewHolder.itemView));
    resetAnimation(paramViewHolder);
    paramInt2 = paramInt3 - paramInt1;
    int j = paramInt4 - i;
    if (paramInt2 == 0 && j == 0) {
      dispatchMoveFinished(paramViewHolder);
      return false;
    } 
    if (paramInt2 != 0)
      ViewCompat.setTranslationX(view, -paramInt2); 
    if (j != 0)
      ViewCompat.setTranslationY(view, -j); 
    this.mPendingMoves.add(new MoveInfo(paramViewHolder, paramInt1, i, paramInt3, paramInt4));
    return true;
  }
  
  void animateMoveImpl(final RecyclerView.ViewHolder holder, final int deltaX, final int deltaY, int paramInt3, int paramInt4) {
    View view = holder.itemView;
    deltaX = paramInt3 - deltaX;
    deltaY = paramInt4 - deltaY;
    if (deltaX != 0)
      ViewCompat.animate(view).translationX(0.0F); 
    if (deltaY != 0)
      ViewCompat.animate(view).translationY(0.0F); 
    final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
    this.mMoveAnimations.add(holder);
    viewPropertyAnimatorCompat.setDuration(getMoveDuration()).setListener(new VpaListenerAdapter() {
          public void onAnimationCancel(View param1View) {
            if (deltaX != 0)
              ViewCompat.setTranslationX(param1View, 0.0F); 
            if (deltaY != 0)
              ViewCompat.setTranslationY(param1View, 0.0F); 
          }
          
          public void onAnimationEnd(View param1View) {
            animation.setListener(null);
            DefaultItemAnimator.this.dispatchMoveFinished(holder);
            DefaultItemAnimator.this.mMoveAnimations.remove(holder);
            DefaultItemAnimator.this.dispatchFinishedWhenDone();
          }
          
          public void onAnimationStart(View param1View) {
            DefaultItemAnimator.this.dispatchMoveStarting(holder);
          }
        }).start();
  }
  
  public boolean animateRemove(RecyclerView.ViewHolder paramViewHolder) {
    resetAnimation(paramViewHolder);
    this.mPendingRemovals.add(paramViewHolder);
    return true;
  }
  
  public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder paramViewHolder, @NonNull List<Object> paramList) {
    return (!paramList.isEmpty() || super.canReuseUpdatedViewHolder(paramViewHolder, paramList));
  }
  
  void cancelAll(List<RecyclerView.ViewHolder> paramList) {
    for (int i = paramList.size() - 1; i >= 0; i--)
      ViewCompat.animate(((RecyclerView.ViewHolder)paramList.get(i)).itemView).cancel(); 
  }
  
  void dispatchFinishedWhenDone() {
    if (!isRunning())
      dispatchAnimationsFinished(); 
  }
  
  public void endAnimation(RecyclerView.ViewHolder paramViewHolder) {
    // Byte code:
    //   0: aload_1
    //   1: getfield itemView : Landroid/view/View;
    //   4: astore_2
    //   5: aload_2
    //   6: invokestatic animate : (Landroid/view/View;)Landroid/support/v4/view/ViewPropertyAnimatorCompat;
    //   9: invokevirtual cancel : ()V
    //   12: aload_0
    //   13: getfield mPendingMoves : Ljava/util/ArrayList;
    //   16: invokevirtual size : ()I
    //   19: iconst_1
    //   20: isub
    //   21: istore_3
    //   22: iload_3
    //   23: iflt -> 74
    //   26: aload_0
    //   27: getfield mPendingMoves : Ljava/util/ArrayList;
    //   30: iload_3
    //   31: invokevirtual get : (I)Ljava/lang/Object;
    //   34: checkcast android/support/v7/widget/DefaultItemAnimator$MoveInfo
    //   37: getfield holder : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   40: aload_1
    //   41: if_acmpne -> 68
    //   44: aload_2
    //   45: fconst_0
    //   46: invokestatic setTranslationY : (Landroid/view/View;F)V
    //   49: aload_2
    //   50: fconst_0
    //   51: invokestatic setTranslationX : (Landroid/view/View;F)V
    //   54: aload_0
    //   55: aload_1
    //   56: invokevirtual dispatchMoveFinished : (Landroid/support/v7/widget/RecyclerView$ViewHolder;)V
    //   59: aload_0
    //   60: getfield mPendingMoves : Ljava/util/ArrayList;
    //   63: iload_3
    //   64: invokevirtual remove : (I)Ljava/lang/Object;
    //   67: pop
    //   68: iinc #3, -1
    //   71: goto -> 22
    //   74: aload_0
    //   75: aload_0
    //   76: getfield mPendingChanges : Ljava/util/ArrayList;
    //   79: aload_1
    //   80: invokespecial endChangeAnimation : (Ljava/util/List;Landroid/support/v7/widget/RecyclerView$ViewHolder;)V
    //   83: aload_0
    //   84: getfield mPendingRemovals : Ljava/util/ArrayList;
    //   87: aload_1
    //   88: invokevirtual remove : (Ljava/lang/Object;)Z
    //   91: ifeq -> 104
    //   94: aload_2
    //   95: fconst_1
    //   96: invokestatic setAlpha : (Landroid/view/View;F)V
    //   99: aload_0
    //   100: aload_1
    //   101: invokevirtual dispatchRemoveFinished : (Landroid/support/v7/widget/RecyclerView$ViewHolder;)V
    //   104: aload_0
    //   105: getfield mPendingAdditions : Ljava/util/ArrayList;
    //   108: aload_1
    //   109: invokevirtual remove : (Ljava/lang/Object;)Z
    //   112: ifeq -> 125
    //   115: aload_2
    //   116: fconst_1
    //   117: invokestatic setAlpha : (Landroid/view/View;F)V
    //   120: aload_0
    //   121: aload_1
    //   122: invokevirtual dispatchAddFinished : (Landroid/support/v7/widget/RecyclerView$ViewHolder;)V
    //   125: aload_0
    //   126: getfield mChangesList : Ljava/util/ArrayList;
    //   129: invokevirtual size : ()I
    //   132: iconst_1
    //   133: isub
    //   134: istore_3
    //   135: iload_3
    //   136: iflt -> 182
    //   139: aload_0
    //   140: getfield mChangesList : Ljava/util/ArrayList;
    //   143: iload_3
    //   144: invokevirtual get : (I)Ljava/lang/Object;
    //   147: checkcast java/util/ArrayList
    //   150: astore #4
    //   152: aload_0
    //   153: aload #4
    //   155: aload_1
    //   156: invokespecial endChangeAnimation : (Ljava/util/List;Landroid/support/v7/widget/RecyclerView$ViewHolder;)V
    //   159: aload #4
    //   161: invokevirtual isEmpty : ()Z
    //   164: ifeq -> 176
    //   167: aload_0
    //   168: getfield mChangesList : Ljava/util/ArrayList;
    //   171: iload_3
    //   172: invokevirtual remove : (I)Ljava/lang/Object;
    //   175: pop
    //   176: iinc #3, -1
    //   179: goto -> 135
    //   182: aload_0
    //   183: getfield mMovesList : Ljava/util/ArrayList;
    //   186: invokevirtual size : ()I
    //   189: iconst_1
    //   190: isub
    //   191: istore_3
    //   192: iload_3
    //   193: iflt -> 292
    //   196: aload_0
    //   197: getfield mMovesList : Ljava/util/ArrayList;
    //   200: iload_3
    //   201: invokevirtual get : (I)Ljava/lang/Object;
    //   204: checkcast java/util/ArrayList
    //   207: astore #4
    //   209: aload #4
    //   211: invokevirtual size : ()I
    //   214: iconst_1
    //   215: isub
    //   216: istore #5
    //   218: iload #5
    //   220: iflt -> 280
    //   223: aload #4
    //   225: iload #5
    //   227: invokevirtual get : (I)Ljava/lang/Object;
    //   230: checkcast android/support/v7/widget/DefaultItemAnimator$MoveInfo
    //   233: getfield holder : Landroid/support/v7/widget/RecyclerView$ViewHolder;
    //   236: aload_1
    //   237: if_acmpne -> 286
    //   240: aload_2
    //   241: fconst_0
    //   242: invokestatic setTranslationY : (Landroid/view/View;F)V
    //   245: aload_2
    //   246: fconst_0
    //   247: invokestatic setTranslationX : (Landroid/view/View;F)V
    //   250: aload_0
    //   251: aload_1
    //   252: invokevirtual dispatchMoveFinished : (Landroid/support/v7/widget/RecyclerView$ViewHolder;)V
    //   255: aload #4
    //   257: iload #5
    //   259: invokevirtual remove : (I)Ljava/lang/Object;
    //   262: pop
    //   263: aload #4
    //   265: invokevirtual isEmpty : ()Z
    //   268: ifeq -> 280
    //   271: aload_0
    //   272: getfield mMovesList : Ljava/util/ArrayList;
    //   275: iload_3
    //   276: invokevirtual remove : (I)Ljava/lang/Object;
    //   279: pop
    //   280: iinc #3, -1
    //   283: goto -> 192
    //   286: iinc #5, -1
    //   289: goto -> 218
    //   292: aload_0
    //   293: getfield mAdditionsList : Ljava/util/ArrayList;
    //   296: invokevirtual size : ()I
    //   299: iconst_1
    //   300: isub
    //   301: istore_3
    //   302: iload_3
    //   303: iflt -> 361
    //   306: aload_0
    //   307: getfield mAdditionsList : Ljava/util/ArrayList;
    //   310: iload_3
    //   311: invokevirtual get : (I)Ljava/lang/Object;
    //   314: checkcast java/util/ArrayList
    //   317: astore #4
    //   319: aload #4
    //   321: aload_1
    //   322: invokevirtual remove : (Ljava/lang/Object;)Z
    //   325: ifeq -> 355
    //   328: aload_2
    //   329: fconst_1
    //   330: invokestatic setAlpha : (Landroid/view/View;F)V
    //   333: aload_0
    //   334: aload_1
    //   335: invokevirtual dispatchAddFinished : (Landroid/support/v7/widget/RecyclerView$ViewHolder;)V
    //   338: aload #4
    //   340: invokevirtual isEmpty : ()Z
    //   343: ifeq -> 355
    //   346: aload_0
    //   347: getfield mAdditionsList : Ljava/util/ArrayList;
    //   350: iload_3
    //   351: invokevirtual remove : (I)Ljava/lang/Object;
    //   354: pop
    //   355: iinc #3, -1
    //   358: goto -> 302
    //   361: aload_0
    //   362: getfield mRemoveAnimations : Ljava/util/ArrayList;
    //   365: aload_1
    //   366: invokevirtual remove : (Ljava/lang/Object;)Z
    //   369: ifeq -> 372
    //   372: aload_0
    //   373: getfield mAddAnimations : Ljava/util/ArrayList;
    //   376: aload_1
    //   377: invokevirtual remove : (Ljava/lang/Object;)Z
    //   380: ifeq -> 383
    //   383: aload_0
    //   384: getfield mChangeAnimations : Ljava/util/ArrayList;
    //   387: aload_1
    //   388: invokevirtual remove : (Ljava/lang/Object;)Z
    //   391: ifeq -> 394
    //   394: aload_0
    //   395: getfield mMoveAnimations : Ljava/util/ArrayList;
    //   398: aload_1
    //   399: invokevirtual remove : (Ljava/lang/Object;)Z
    //   402: ifeq -> 405
    //   405: aload_0
    //   406: invokevirtual dispatchFinishedWhenDone : ()V
    //   409: return
  }
  
  public void endAnimations() {
    int i;
    for (i = this.mPendingMoves.size() - 1; i >= 0; i--) {
      MoveInfo moveInfo = this.mPendingMoves.get(i);
      View view = moveInfo.holder.itemView;
      ViewCompat.setTranslationY(view, 0.0F);
      ViewCompat.setTranslationX(view, 0.0F);
      dispatchMoveFinished(moveInfo.holder);
      this.mPendingMoves.remove(i);
    } 
    for (i = this.mPendingRemovals.size() - 1; i >= 0; i--) {
      dispatchRemoveFinished(this.mPendingRemovals.get(i));
      this.mPendingRemovals.remove(i);
    } 
    for (i = this.mPendingAdditions.size() - 1; i >= 0; i--) {
      RecyclerView.ViewHolder viewHolder = this.mPendingAdditions.get(i);
      ViewCompat.setAlpha(viewHolder.itemView, 1.0F);
      dispatchAddFinished(viewHolder);
      this.mPendingAdditions.remove(i);
    } 
    for (i = this.mPendingChanges.size() - 1; i >= 0; i--)
      endChangeAnimationIfNecessary(this.mPendingChanges.get(i)); 
    this.mPendingChanges.clear();
    if (isRunning()) {
      for (i = this.mMovesList.size() - 1; i >= 0; i--) {
        ArrayList<MoveInfo> arrayList = this.mMovesList.get(i);
        for (int j = arrayList.size() - 1; j >= 0; j--) {
          MoveInfo moveInfo = arrayList.get(j);
          View view = moveInfo.holder.itemView;
          ViewCompat.setTranslationY(view, 0.0F);
          ViewCompat.setTranslationX(view, 0.0F);
          dispatchMoveFinished(moveInfo.holder);
          arrayList.remove(j);
          if (arrayList.isEmpty())
            this.mMovesList.remove(arrayList); 
        } 
      } 
      for (i = this.mAdditionsList.size() - 1; i >= 0; i--) {
        ArrayList<RecyclerView.ViewHolder> arrayList = this.mAdditionsList.get(i);
        for (int j = arrayList.size() - 1; j >= 0; j--) {
          RecyclerView.ViewHolder viewHolder = arrayList.get(j);
          ViewCompat.setAlpha(viewHolder.itemView, 1.0F);
          dispatchAddFinished(viewHolder);
          arrayList.remove(j);
          if (arrayList.isEmpty())
            this.mAdditionsList.remove(arrayList); 
        } 
      } 
      for (i = this.mChangesList.size() - 1; i >= 0; i--) {
        ArrayList<ChangeInfo> arrayList = this.mChangesList.get(i);
        for (int j = arrayList.size() - 1; j >= 0; j--) {
          endChangeAnimationIfNecessary(arrayList.get(j));
          if (arrayList.isEmpty())
            this.mChangesList.remove(arrayList); 
        } 
      } 
      cancelAll(this.mRemoveAnimations);
      cancelAll(this.mMoveAnimations);
      cancelAll(this.mAddAnimations);
      cancelAll(this.mChangeAnimations);
      dispatchAnimationsFinished();
    } 
  }
  
  public boolean isRunning() {
    return (!this.mPendingAdditions.isEmpty() || !this.mPendingChanges.isEmpty() || !this.mPendingMoves.isEmpty() || !this.mPendingRemovals.isEmpty() || !this.mMoveAnimations.isEmpty() || !this.mRemoveAnimations.isEmpty() || !this.mAddAnimations.isEmpty() || !this.mChangeAnimations.isEmpty() || !this.mMovesList.isEmpty() || !this.mAdditionsList.isEmpty() || !this.mChangesList.isEmpty());
  }
  
  public void runPendingAnimations() {
    boolean bool1;
    boolean bool2;
    boolean bool3;
    boolean bool4;
    if (!this.mPendingRemovals.isEmpty()) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (!this.mPendingMoves.isEmpty()) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (!this.mPendingChanges.isEmpty()) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    if (!this.mPendingAdditions.isEmpty()) {
      bool4 = true;
    } else {
      bool4 = false;
    } 
    if (bool1 || bool2 || bool4 || bool3) {
      Iterator<RecyclerView.ViewHolder> iterator = this.mPendingRemovals.iterator();
      while (iterator.hasNext())
        animateRemoveImpl(iterator.next()); 
      this.mPendingRemovals.clear();
      if (bool2) {
        final ArrayList<MoveInfo> additions = new ArrayList();
        arrayList.addAll(this.mPendingMoves);
        this.mMovesList.add(arrayList);
        this.mPendingMoves.clear();
        Runnable runnable = new Runnable() {
            public void run() {
              for (DefaultItemAnimator.MoveInfo moveInfo : moves)
                DefaultItemAnimator.this.animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY, moveInfo.toX, moveInfo.toY); 
              moves.clear();
              DefaultItemAnimator.this.mMovesList.remove(moves);
            }
          };
        if (bool1) {
          ViewCompat.postOnAnimationDelayed(((MoveInfo)arrayList.get(0)).holder.itemView, runnable, getRemoveDuration());
        } else {
          runnable.run();
        } 
      } 
      if (bool3) {
        final ArrayList<ChangeInfo> additions = new ArrayList();
        arrayList.addAll(this.mPendingChanges);
        this.mChangesList.add(arrayList);
        this.mPendingChanges.clear();
        Runnable runnable = new Runnable() {
            public void run() {
              for (DefaultItemAnimator.ChangeInfo changeInfo : changes)
                DefaultItemAnimator.this.animateChangeImpl(changeInfo); 
              changes.clear();
              DefaultItemAnimator.this.mChangesList.remove(changes);
            }
          };
        if (bool1) {
          ViewCompat.postOnAnimationDelayed(((ChangeInfo)arrayList.get(0)).oldHolder.itemView, runnable, getRemoveDuration());
        } else {
          runnable.run();
        } 
      } 
      if (bool4) {
        final ArrayList<RecyclerView.ViewHolder> additions = new ArrayList();
        arrayList.addAll(this.mPendingAdditions);
        this.mAdditionsList.add(arrayList);
        this.mPendingAdditions.clear();
        Runnable runnable = new Runnable() {
            public void run() {
              for (RecyclerView.ViewHolder viewHolder : additions)
                DefaultItemAnimator.this.animateAddImpl(viewHolder); 
              additions.clear();
              DefaultItemAnimator.this.mAdditionsList.remove(additions);
            }
          };
        if (bool1 || bool2 || bool3) {
          long l1;
          long l3;
          if (bool1) {
            l1 = getRemoveDuration();
          } else {
            l1 = 0L;
          } 
          if (bool2) {
            l2 = getMoveDuration();
          } else {
            l2 = 0L;
          } 
          if (bool3) {
            l3 = getChangeDuration();
          } else {
            l3 = 0L;
          } 
          long l2 = Math.max(l2, l3);
          ViewCompat.postOnAnimationDelayed(((RecyclerView.ViewHolder)arrayList.get(0)).itemView, runnable, l1 + l2);
          return;
        } 
        runnable.run();
      } 
    } 
  }
  
  private static class ChangeInfo {
    public int fromX;
    
    public int fromY;
    
    public RecyclerView.ViewHolder newHolder;
    
    public RecyclerView.ViewHolder oldHolder;
    
    public int toX;
    
    public int toY;
    
    private ChangeInfo(RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2) {
      this.oldHolder = param1ViewHolder1;
      this.newHolder = param1ViewHolder2;
    }
    
    ChangeInfo(RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      this(param1ViewHolder1, param1ViewHolder2);
      this.fromX = param1Int1;
      this.fromY = param1Int2;
      this.toX = param1Int3;
      this.toY = param1Int4;
    }
    
    public String toString() {
      return "ChangeInfo{oldHolder=" + this.oldHolder + ", newHolder=" + this.newHolder + ", fromX=" + this.fromX + ", fromY=" + this.fromY + ", toX=" + this.toX + ", toY=" + this.toY + '}';
    }
  }
  
  private static class MoveInfo {
    public int fromX;
    
    public int fromY;
    
    public RecyclerView.ViewHolder holder;
    
    public int toX;
    
    public int toY;
    
    MoveInfo(RecyclerView.ViewHolder param1ViewHolder, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      this.holder = param1ViewHolder;
      this.fromX = param1Int1;
      this.fromY = param1Int2;
      this.toX = param1Int3;
      this.toY = param1Int4;
    }
  }
  
  private static class VpaListenerAdapter implements ViewPropertyAnimatorListener {
    public void onAnimationCancel(View param1View) {}
    
    public void onAnimationEnd(View param1View) {}
    
    public void onAnimationStart(View param1View) {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/widget/DefaultItemAnimator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */