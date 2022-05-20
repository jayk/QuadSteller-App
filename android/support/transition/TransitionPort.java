package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SimpleArrayMap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(14)
@RequiresApi(14)
abstract class TransitionPort implements Cloneable {
  static final boolean DBG = false;
  
  private static final String LOG_TAG = "Transition";
  
  private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal<ArrayMap<Animator, AnimationInfo>>();
  
  ArrayList<Animator> mAnimators = new ArrayList<Animator>();
  
  boolean mCanRemoveViews = false;
  
  ArrayList<Animator> mCurrentAnimators = new ArrayList<Animator>();
  
  long mDuration = -1L;
  
  private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
  
  private boolean mEnded = false;
  
  TimeInterpolator mInterpolator = null;
  
  ArrayList<TransitionListener> mListeners = null;
  
  private String mName = getClass().getName();
  
  int mNumInstances = 0;
  
  TransitionSetPort mParent = null;
  
  boolean mPaused = false;
  
  ViewGroup mSceneRoot = null;
  
  long mStartDelay = -1L;
  
  private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
  
  ArrayList<View> mTargetChildExcludes = null;
  
  ArrayList<View> mTargetExcludes = null;
  
  ArrayList<Integer> mTargetIdChildExcludes = null;
  
  ArrayList<Integer> mTargetIdExcludes = null;
  
  ArrayList<Integer> mTargetIds = new ArrayList<Integer>();
  
  ArrayList<Class> mTargetTypeChildExcludes = null;
  
  ArrayList<Class> mTargetTypeExcludes = null;
  
  ArrayList<View> mTargets = new ArrayList<View>();
  
  private void captureHierarchy(View paramView, boolean paramBoolean) {
    if (paramView != null) {
      byte b = 0;
      if (paramView.getParent() instanceof ListView)
        b = 1; 
      if (!b || ((ListView)paramView.getParent()).getAdapter().hasStableIds()) {
        int i = -1;
        long l = -1L;
        if (!b) {
          i = paramView.getId();
        } else {
          ListView listView = (ListView)paramView.getParent();
          l = listView.getItemIdAtPosition(listView.getPositionForView(paramView));
        } 
        if ((this.mTargetIdExcludes == null || !this.mTargetIdExcludes.contains(Integer.valueOf(i))) && (this.mTargetExcludes == null || !this.mTargetExcludes.contains(paramView))) {
          if (this.mTargetTypeExcludes != null && paramView != null) {
            int j = this.mTargetTypeExcludes.size();
            byte b1 = 0;
            while (b1 < j) {
              if (!((Class)this.mTargetTypeExcludes.get(b1)).isInstance(paramView)) {
                b1++;
                continue;
              } 
              return;
            } 
          } 
          TransitionValues transitionValues = new TransitionValues();
          transitionValues.view = paramView;
          if (paramBoolean) {
            captureStartValues(transitionValues);
          } else {
            captureEndValues(transitionValues);
          } 
          if (paramBoolean) {
            if (!b) {
              this.mStartValues.viewValues.put(paramView, transitionValues);
              if (i >= 0)
                this.mStartValues.idValues.put(i, transitionValues); 
            } else {
              this.mStartValues.itemIdValues.put(l, transitionValues);
            } 
          } else if (!b) {
            this.mEndValues.viewValues.put(paramView, transitionValues);
            if (i >= 0)
              this.mEndValues.idValues.put(i, transitionValues); 
          } else {
            this.mEndValues.itemIdValues.put(l, transitionValues);
          } 
          if (paramView instanceof ViewGroup && (this.mTargetIdChildExcludes == null || !this.mTargetIdChildExcludes.contains(Integer.valueOf(i))) && (this.mTargetChildExcludes == null || !this.mTargetChildExcludes.contains(paramView))) {
            if (this.mTargetTypeChildExcludes != null && paramView != null) {
              i = this.mTargetTypeChildExcludes.size();
              b = 0;
              while (b < i) {
                if (!((Class)this.mTargetTypeChildExcludes.get(b)).isInstance(paramView)) {
                  b++;
                  continue;
                } 
                return;
              } 
            } 
            ViewGroup viewGroup = (ViewGroup)paramView;
            b = 0;
            while (true) {
              if (b < viewGroup.getChildCount()) {
                captureHierarchy(viewGroup.getChildAt(b), paramBoolean);
                b++;
                continue;
              } 
              return;
            } 
          } 
        } 
      } 
    } 
  }
  
  private ArrayList<Integer> excludeId(ArrayList<Integer> paramArrayList, int paramInt, boolean paramBoolean) {
    null = paramArrayList;
    if (paramInt > 0) {
      if (paramBoolean)
        return ArrayListManager.add(paramArrayList, Integer.valueOf(paramInt)); 
    } else {
      return null;
    } 
    return ArrayListManager.remove(paramArrayList, Integer.valueOf(paramInt));
  }
  
  private ArrayList<Class> excludeType(ArrayList<Class> paramArrayList, Class<?> paramClass, boolean paramBoolean) {
    null = paramArrayList;
    if (paramClass != null) {
      if (paramBoolean)
        return ArrayListManager.add(paramArrayList, paramClass); 
    } else {
      return null;
    } 
    return ArrayListManager.remove(paramArrayList, paramClass);
  }
  
  private ArrayList<View> excludeView(ArrayList<View> paramArrayList, View paramView, boolean paramBoolean) {
    null = paramArrayList;
    if (paramView != null) {
      if (paramBoolean)
        return ArrayListManager.add(paramArrayList, paramView); 
    } else {
      return null;
    } 
    return ArrayListManager.remove(paramArrayList, paramView);
  }
  
  private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
    ArrayMap<Animator, AnimationInfo> arrayMap1 = sRunningAnimators.get();
    ArrayMap<Animator, AnimationInfo> arrayMap2 = arrayMap1;
    if (arrayMap1 == null) {
      arrayMap2 = new ArrayMap();
      sRunningAnimators.set(arrayMap2);
    } 
    return arrayMap2;
  }
  
  private void runAnimator(Animator paramAnimator, final ArrayMap<Animator, AnimationInfo> runningAnimators) {
    if (paramAnimator != null) {
      paramAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator param1Animator) {
              runningAnimators.remove(param1Animator);
              TransitionPort.this.mCurrentAnimators.remove(param1Animator);
            }
            
            public void onAnimationStart(Animator param1Animator) {
              TransitionPort.this.mCurrentAnimators.add(param1Animator);
            }
          });
      animate(paramAnimator);
    } 
  }
  
  public TransitionPort addListener(TransitionListener paramTransitionListener) {
    if (this.mListeners == null)
      this.mListeners = new ArrayList<TransitionListener>(); 
    this.mListeners.add(paramTransitionListener);
    return this;
  }
  
  public TransitionPort addTarget(int paramInt) {
    if (paramInt > 0)
      this.mTargetIds.add(Integer.valueOf(paramInt)); 
    return this;
  }
  
  public TransitionPort addTarget(View paramView) {
    this.mTargets.add(paramView);
    return this;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected void animate(Animator paramAnimator) {
    if (paramAnimator == null) {
      end();
      return;
    } 
    if (getDuration() >= 0L)
      paramAnimator.setDuration(getDuration()); 
    if (getStartDelay() >= 0L)
      paramAnimator.setStartDelay(getStartDelay()); 
    if (getInterpolator() != null)
      paramAnimator.setInterpolator(getInterpolator()); 
    paramAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          public void onAnimationEnd(Animator param1Animator) {
            TransitionPort.this.end();
            param1Animator.removeListener((Animator.AnimatorListener)this);
          }
        });
    paramAnimator.start();
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected void cancel() {
    int i;
    for (i = this.mCurrentAnimators.size() - 1; i >= 0; i--)
      ((Animator)this.mCurrentAnimators.get(i)).cancel(); 
    if (this.mListeners != null && this.mListeners.size() > 0) {
      ArrayList<TransitionListener> arrayList = (ArrayList)this.mListeners.clone();
      int j = arrayList.size();
      for (i = 0; i < j; i++)
        ((TransitionListener)arrayList.get(i)).onTransitionCancel(this); 
    } 
  }
  
  public abstract void captureEndValues(TransitionValues paramTransitionValues);
  
  public abstract void captureStartValues(TransitionValues paramTransitionValues);
  
  void captureValues(ViewGroup paramViewGroup, boolean paramBoolean) {
    TransitionValues transitionValues;
    clearValues(paramBoolean);
    if (this.mTargetIds.size() > 0 || this.mTargets.size() > 0) {
      if (this.mTargetIds.size() > 0)
        for (byte b = 0; b < this.mTargetIds.size(); b++) {
          int i = ((Integer)this.mTargetIds.get(b)).intValue();
          View view = paramViewGroup.findViewById(i);
          if (view != null) {
            TransitionValues transitionValues1 = new TransitionValues();
            transitionValues1.view = view;
            if (paramBoolean) {
              captureStartValues(transitionValues1);
            } else {
              captureEndValues(transitionValues1);
            } 
            if (paramBoolean) {
              this.mStartValues.viewValues.put(view, transitionValues1);
              if (i >= 0)
                this.mStartValues.idValues.put(i, transitionValues1); 
            } else {
              this.mEndValues.viewValues.put(view, transitionValues1);
              if (i >= 0)
                this.mEndValues.idValues.put(i, transitionValues1); 
            } 
          } 
        }  
      if (this.mTargets.size() > 0)
        for (byte b = 0; b < this.mTargets.size(); b++) {
          View view = this.mTargets.get(b);
          if (view != null) {
            transitionValues = new TransitionValues();
            transitionValues.view = view;
            if (paramBoolean) {
              captureStartValues(transitionValues);
            } else {
              captureEndValues(transitionValues);
            } 
            if (paramBoolean) {
              this.mStartValues.viewValues.put(view, transitionValues);
            } else {
              this.mEndValues.viewValues.put(view, transitionValues);
            } 
          } 
        }  
    } else {
      captureHierarchy((View)transitionValues, paramBoolean);
    } 
  }
  
  void clearValues(boolean paramBoolean) {
    if (paramBoolean) {
      this.mStartValues.viewValues.clear();
      this.mStartValues.idValues.clear();
      this.mStartValues.itemIdValues.clear();
      return;
    } 
    this.mEndValues.viewValues.clear();
    this.mEndValues.idValues.clear();
    this.mEndValues.itemIdValues.clear();
  }
  
  public TransitionPort clone() {
    TransitionPort transitionPort = null;
    try {
      TransitionPort transitionPort1 = (TransitionPort)super.clone();
      transitionPort = transitionPort1;
      ArrayList<Animator> arrayList = new ArrayList();
      transitionPort = transitionPort1;
      this();
      transitionPort = transitionPort1;
      transitionPort1.mAnimators = arrayList;
      transitionPort = transitionPort1;
      TransitionValuesMaps transitionValuesMaps = new TransitionValuesMaps();
      transitionPort = transitionPort1;
      this();
      transitionPort = transitionPort1;
      transitionPort1.mStartValues = transitionValuesMaps;
      transitionPort = transitionPort1;
      transitionValuesMaps = new TransitionValuesMaps();
      transitionPort = transitionPort1;
      this();
      transitionPort = transitionPort1;
      transitionPort1.mEndValues = transitionValuesMaps;
      transitionPort = transitionPort1;
    } catch (CloneNotSupportedException cloneNotSupportedException) {}
    return transitionPort;
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return null;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected void createAnimators(ViewGroup paramViewGroup, TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2) {
    ArrayMap arrayMap = new ArrayMap((SimpleArrayMap)paramTransitionValuesMaps2.viewValues);
    SparseArray sparseArray = new SparseArray(paramTransitionValuesMaps2.idValues.size());
    int i;
    for (i = 0; i < paramTransitionValuesMaps2.idValues.size(); i++)
      sparseArray.put(paramTransitionValuesMaps2.idValues.keyAt(i), paramTransitionValuesMaps2.idValues.valueAt(i)); 
    LongSparseArray longSparseArray = new LongSparseArray(paramTransitionValuesMaps2.itemIdValues.size());
    for (i = 0; i < paramTransitionValuesMaps2.itemIdValues.size(); i++)
      longSparseArray.put(paramTransitionValuesMaps2.itemIdValues.keyAt(i), paramTransitionValuesMaps2.itemIdValues.valueAt(i)); 
    ArrayList<TransitionValues> arrayList1 = new ArrayList();
    ArrayList<TransitionValues> arrayList2 = new ArrayList();
    for (View view : paramTransitionValuesMaps1.viewValues.keySet()) {
      TransitionValues transitionValues = null;
      i = 0;
      if (view.getParent() instanceof ListView)
        i = 1; 
      if (i == 0) {
        TransitionValues transitionValues1;
        i = view.getId();
        if (paramTransitionValuesMaps1.viewValues.get(view) != null) {
          transitionValues1 = (TransitionValues)paramTransitionValuesMaps1.viewValues.get(view);
        } else {
          transitionValues1 = (TransitionValues)paramTransitionValuesMaps1.idValues.get(i);
        } 
        if (paramTransitionValuesMaps2.viewValues.get(view) != null) {
          transitionValues = (TransitionValues)paramTransitionValuesMaps2.viewValues.get(view);
          arrayMap.remove(view);
        } else if (i != -1) {
          TransitionValues transitionValues2 = (TransitionValues)paramTransitionValuesMaps2.idValues.get(i);
          View view1 = null;
          for (View view2 : arrayMap.keySet()) {
            if (view2.getId() == i)
              view1 = view2; 
          } 
          transitionValues = transitionValues2;
          if (view1 != null) {
            arrayMap.remove(view1);
            transitionValues = transitionValues2;
          } 
        } 
        sparseArray.remove(i);
        if (isValidTarget(view, i)) {
          arrayList1.add(transitionValues1);
          arrayList2.add(transitionValues);
        } 
        continue;
      } 
      ListView listView = (ListView)view.getParent();
      if (listView.getAdapter().hasStableIds()) {
        long l = listView.getItemIdAtPosition(listView.getPositionForView(view));
        TransitionValues transitionValues1 = (TransitionValues)paramTransitionValuesMaps1.itemIdValues.get(l);
        longSparseArray.remove(l);
        arrayList1.add(transitionValues1);
        arrayList2.add(null);
      } 
    } 
    int j = paramTransitionValuesMaps1.itemIdValues.size();
    for (i = 0; i < j; i++) {
      long l = paramTransitionValuesMaps1.itemIdValues.keyAt(i);
      if (isValidTarget(null, l)) {
        TransitionValues transitionValues1 = (TransitionValues)paramTransitionValuesMaps1.itemIdValues.get(l);
        TransitionValues transitionValues2 = (TransitionValues)paramTransitionValuesMaps2.itemIdValues.get(l);
        longSparseArray.remove(l);
        arrayList1.add(transitionValues1);
        arrayList2.add(transitionValues2);
      } 
    } 
    for (View view : arrayMap.keySet()) {
      i = view.getId();
      if (isValidTarget(view, i)) {
        TransitionValues transitionValues1;
        if (paramTransitionValuesMaps1.viewValues.get(view) != null) {
          transitionValues1 = (TransitionValues)paramTransitionValuesMaps1.viewValues.get(view);
        } else {
          transitionValues1 = (TransitionValues)paramTransitionValuesMaps1.idValues.get(i);
        } 
        TransitionValues transitionValues2 = (TransitionValues)arrayMap.get(view);
        sparseArray.remove(i);
        arrayList1.add(transitionValues1);
        arrayList2.add(transitionValues2);
      } 
    } 
    j = sparseArray.size();
    for (i = 0; i < j; i++) {
      int k = sparseArray.keyAt(i);
      if (isValidTarget(null, k)) {
        TransitionValues transitionValues2 = (TransitionValues)paramTransitionValuesMaps1.idValues.get(k);
        TransitionValues transitionValues1 = (TransitionValues)sparseArray.get(k);
        arrayList1.add(transitionValues2);
        arrayList2.add(transitionValues1);
      } 
    } 
    j = longSparseArray.size();
    for (i = 0; i < j; i++) {
      long l = longSparseArray.keyAt(i);
      TransitionValues transitionValues1 = (TransitionValues)paramTransitionValuesMaps1.itemIdValues.get(l);
      TransitionValues transitionValues2 = (TransitionValues)longSparseArray.get(l);
      arrayList1.add(transitionValues1);
      arrayList2.add(transitionValues2);
    } 
    ArrayMap<Animator, AnimationInfo> arrayMap1 = getRunningAnimators();
    i = 0;
    label123: while (i < arrayList1.size()) {
      TransitionValues transitionValues2 = arrayList1.get(i);
      TransitionValues transitionValues1 = arrayList2.get(i);
      if (transitionValues2 != null || transitionValues1 != null) {
        if (transitionValues2 == null || !transitionValues2.equals(transitionValues1)) {
          Animator animator = createAnimator(paramViewGroup, transitionValues2, transitionValues1);
          if (animator != null) {
            TransitionValues transitionValues;
            longSparseArray = null;
            if (transitionValues1 != null) {
              View view1 = transitionValues1.view;
              String[] arrayOfString = getTransitionProperties();
              Animator animator2 = animator;
              LongSparseArray longSparseArray1 = longSparseArray;
              view = view1;
              if (view1 != null) {
                animator2 = animator;
                longSparseArray1 = longSparseArray;
                view = view1;
                if (arrayOfString != null) {
                  animator2 = animator;
                  longSparseArray1 = longSparseArray;
                  view = view1;
                  if (arrayOfString.length > 0) {
                    transitionValues = new TransitionValues();
                    transitionValues.view = view1;
                    TransitionValues transitionValues3 = (TransitionValues)paramTransitionValuesMaps2.viewValues.get(view1);
                    if (transitionValues3 != null)
                      for (j = 0; j < arrayOfString.length; j++)
                        transitionValues.values.put(arrayOfString[j], transitionValues3.values.get(arrayOfString[j]));  
                    int k = arrayMap1.size();
                    j = 0;
                    while (true) {
                      TransitionValues transitionValues4;
                      animator2 = animator;
                      transitionValues3 = transitionValues;
                      view = view1;
                      if (j < k) {
                        AnimationInfo animationInfo = (AnimationInfo)arrayMap1.get(arrayMap1.keyAt(j));
                        if (animationInfo.values != null && animationInfo.view == view1 && ((animationInfo.name == null && getName() == null) || animationInfo.name.equals(getName())) && animationInfo.values.equals(transitionValues)) {
                          animator2 = null;
                          view = view1;
                          transitionValues4 = transitionValues;
                        } else {
                          j++;
                          continue;
                        } 
                      } 
                      if (animator2 != null) {
                        arrayMap1.put(animator2, new AnimationInfo(view, getName(), WindowIdPort.getWindowId((View)paramViewGroup), transitionValues4));
                        this.mAnimators.add(animator2);
                      } 
                      i++;
                      continue label123;
                    } 
                  } 
                  continue;
                } 
                continue;
              } 
              continue;
            } 
            View view = ((TransitionValues)view).view;
            Animator animator1 = animator;
            transitionValues1 = transitionValues;
            continue;
          } 
          continue;
        } 
        continue;
      } 
      continue;
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected void end() {
    this.mNumInstances--;
    if (this.mNumInstances == 0) {
      if (this.mListeners != null && this.mListeners.size() > 0) {
        ArrayList<TransitionListener> arrayList = (ArrayList)this.mListeners.clone();
        int i = arrayList.size();
        for (byte b1 = 0; b1 < i; b1++)
          ((TransitionListener)arrayList.get(b1)).onTransitionEnd(this); 
      } 
      byte b;
      for (b = 0; b < this.mStartValues.itemIdValues.size(); b++)
        View view = ((TransitionValues)this.mStartValues.itemIdValues.valueAt(b)).view; 
      for (b = 0; b < this.mEndValues.itemIdValues.size(); b++)
        View view = ((TransitionValues)this.mEndValues.itemIdValues.valueAt(b)).view; 
      this.mEnded = true;
    } 
  }
  
  public TransitionPort excludeChildren(int paramInt, boolean paramBoolean) {
    this.mTargetIdChildExcludes = excludeId(this.mTargetIdChildExcludes, paramInt, paramBoolean);
    return this;
  }
  
  public TransitionPort excludeChildren(View paramView, boolean paramBoolean) {
    this.mTargetChildExcludes = excludeView(this.mTargetChildExcludes, paramView, paramBoolean);
    return this;
  }
  
  public TransitionPort excludeChildren(Class paramClass, boolean paramBoolean) {
    this.mTargetTypeChildExcludes = excludeType(this.mTargetTypeChildExcludes, paramClass, paramBoolean);
    return this;
  }
  
  public TransitionPort excludeTarget(int paramInt, boolean paramBoolean) {
    this.mTargetIdExcludes = excludeId(this.mTargetIdExcludes, paramInt, paramBoolean);
    return this;
  }
  
  public TransitionPort excludeTarget(View paramView, boolean paramBoolean) {
    this.mTargetExcludes = excludeView(this.mTargetExcludes, paramView, paramBoolean);
    return this;
  }
  
  public TransitionPort excludeTarget(Class paramClass, boolean paramBoolean) {
    this.mTargetTypeExcludes = excludeType(this.mTargetTypeExcludes, paramClass, paramBoolean);
    return this;
  }
  
  public long getDuration() {
    return this.mDuration;
  }
  
  public TimeInterpolator getInterpolator() {
    return this.mInterpolator;
  }
  
  public String getName() {
    return this.mName;
  }
  
  public long getStartDelay() {
    return this.mStartDelay;
  }
  
  public List<Integer> getTargetIds() {
    return this.mTargetIds;
  }
  
  public List<View> getTargets() {
    return this.mTargets;
  }
  
  public String[] getTransitionProperties() {
    return null;
  }
  
  public TransitionValues getTransitionValues(View paramView, boolean paramBoolean) {
    TransitionValuesMaps transitionValuesMaps;
    if (this.mParent != null)
      return this.mParent.getTransitionValues(paramView, paramBoolean); 
    if (paramBoolean) {
      transitionValuesMaps = this.mStartValues;
    } else {
      transitionValuesMaps = this.mEndValues;
    } 
    TransitionValues transitionValues2 = (TransitionValues)transitionValuesMaps.viewValues.get(paramView);
    TransitionValues transitionValues1 = transitionValues2;
    if (transitionValues2 == null) {
      int i = paramView.getId();
      if (i >= 0)
        transitionValues2 = (TransitionValues)transitionValuesMaps.idValues.get(i); 
      transitionValues1 = transitionValues2;
      if (transitionValues2 == null) {
        transitionValues1 = transitionValues2;
        if (paramView.getParent() instanceof ListView) {
          ListView listView = (ListView)paramView.getParent();
          long l = listView.getItemIdAtPosition(listView.getPositionForView(paramView));
          transitionValues1 = (TransitionValues)transitionValuesMaps.itemIdValues.get(l);
        } 
      } 
    } 
    return transitionValues1;
  }
  
  boolean isValidTarget(View paramView, long paramLong) {
    byte b;
    if (this.mTargetIdExcludes != null && this.mTargetIdExcludes.contains(Integer.valueOf((int)paramLong)))
      return false; 
    if (this.mTargetExcludes != null && this.mTargetExcludes.contains(paramView))
      return false; 
    if (this.mTargetTypeExcludes != null && paramView != null) {
      int i = this.mTargetTypeExcludes.size();
      for (b = 0; b < i; b++) {
        if (((Class)this.mTargetTypeExcludes.get(b)).isInstance(paramView))
          return false; 
      } 
    } 
    if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0)
      return true; 
    if (this.mTargetIds.size() > 0)
      for (b = 0; b < this.mTargetIds.size(); b++) {
        if (((Integer)this.mTargetIds.get(b)).intValue() == paramLong)
          return true; 
      }  
    if (paramView != null && this.mTargets.size() > 0)
      for (b = 0; b < this.mTargets.size(); b++) {
        if (this.mTargets.get(b) == paramView)
          return true; 
      }  
    boolean bool = false;
    while (b < this.mTargets.size()) {
      if (this.mTargets.get(b) == paramView)
        return true; 
      b++;
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void pause(View paramView) {
    if (!this.mEnded) {
      ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
      int i = arrayMap.size();
      WindowIdPort windowIdPort = WindowIdPort.getWindowId(paramView);
      while (--i >= 0) {
        AnimationInfo animationInfo = (AnimationInfo)arrayMap.valueAt(i);
        if (animationInfo.view != null && windowIdPort.equals(animationInfo.windowId))
          ((Animator)arrayMap.keyAt(i)).cancel(); 
        i--;
      } 
      if (this.mListeners != null && this.mListeners.size() > 0) {
        ArrayList<TransitionListener> arrayList = (ArrayList)this.mListeners.clone();
        int j = arrayList.size();
        for (i = 0; i < j; i++)
          ((TransitionListener)arrayList.get(i)).onTransitionPause(this); 
      } 
      this.mPaused = true;
    } 
  }
  
  void playTransition(ViewGroup paramViewGroup) {
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    for (int i = arrayMap.size() - 1; i >= 0; i--) {
      Animator animator = (Animator)arrayMap.keyAt(i);
      if (animator != null) {
        AnimationInfo animationInfo = (AnimationInfo)arrayMap.get(animator);
        if (animationInfo != null && animationInfo.view != null && animationInfo.view.getContext() == paramViewGroup.getContext()) {
          TransitionValues transitionValues2;
          boolean bool1 = false;
          TransitionValues transitionValues1 = animationInfo.values;
          View view = animationInfo.view;
          if (this.mEndValues.viewValues != null) {
            TransitionValues transitionValues = (TransitionValues)this.mEndValues.viewValues.get(view);
          } else {
            animationInfo = null;
          } 
          AnimationInfo animationInfo1 = animationInfo;
          if (animationInfo == null)
            transitionValues2 = (TransitionValues)this.mEndValues.idValues.get(view.getId()); 
          boolean bool2 = bool1;
          if (transitionValues1 != null) {
            bool2 = bool1;
            if (transitionValues2 != null) {
              Iterator<String> iterator = transitionValues1.values.keySet().iterator();
              while (true) {
                bool2 = bool1;
                if (iterator.hasNext()) {
                  String str = iterator.next();
                  animationInfo = (AnimationInfo)transitionValues1.values.get(str);
                  str = (String)transitionValues2.values.get(str);
                  if (animationInfo != null && str != null && !animationInfo.equals(str)) {
                    bool2 = true;
                    break;
                  } 
                  continue;
                } 
                break;
              } 
            } 
          } 
          if (bool2)
            if (animator.isRunning() || animator.isStarted()) {
              animator.cancel();
            } else {
              arrayMap.remove(animator);
            }  
        } 
      } 
    } 
    createAnimators(paramViewGroup, this.mStartValues, this.mEndValues);
    runAnimators();
  }
  
  public TransitionPort removeListener(TransitionListener paramTransitionListener) {
    if (this.mListeners != null) {
      this.mListeners.remove(paramTransitionListener);
      if (this.mListeners.size() == 0)
        this.mListeners = null; 
    } 
    return this;
  }
  
  public TransitionPort removeTarget(int paramInt) {
    if (paramInt > 0)
      this.mTargetIds.remove(Integer.valueOf(paramInt)); 
    return this;
  }
  
  public TransitionPort removeTarget(View paramView) {
    if (paramView != null)
      this.mTargets.remove(paramView); 
    return this;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void resume(View paramView) {
    if (this.mPaused) {
      if (!this.mEnded) {
        ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
        int i = arrayMap.size();
        WindowIdPort windowIdPort = WindowIdPort.getWindowId(paramView);
        while (--i >= 0) {
          AnimationInfo animationInfo = (AnimationInfo)arrayMap.valueAt(i);
          if (animationInfo.view != null && windowIdPort.equals(animationInfo.windowId))
            ((Animator)arrayMap.keyAt(i)).end(); 
          i--;
        } 
        if (this.mListeners != null && this.mListeners.size() > 0) {
          ArrayList<TransitionListener> arrayList = (ArrayList)this.mListeners.clone();
          int j = arrayList.size();
          for (i = 0; i < j; i++)
            ((TransitionListener)arrayList.get(i)).onTransitionResume(this); 
        } 
      } 
      this.mPaused = false;
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected void runAnimators() {
    start();
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    for (Animator animator : this.mAnimators) {
      if (arrayMap.containsKey(animator)) {
        start();
        runAnimator(animator, arrayMap);
      } 
    } 
    this.mAnimators.clear();
    end();
  }
  
  void setCanRemoveViews(boolean paramBoolean) {
    this.mCanRemoveViews = paramBoolean;
  }
  
  public TransitionPort setDuration(long paramLong) {
    this.mDuration = paramLong;
    return this;
  }
  
  public TransitionPort setInterpolator(TimeInterpolator paramTimeInterpolator) {
    this.mInterpolator = paramTimeInterpolator;
    return this;
  }
  
  TransitionPort setSceneRoot(ViewGroup paramViewGroup) {
    this.mSceneRoot = paramViewGroup;
    return this;
  }
  
  public TransitionPort setStartDelay(long paramLong) {
    this.mStartDelay = paramLong;
    return this;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected void start() {
    if (this.mNumInstances == 0) {
      if (this.mListeners != null && this.mListeners.size() > 0) {
        ArrayList<TransitionListener> arrayList = (ArrayList)this.mListeners.clone();
        int i = arrayList.size();
        for (byte b = 0; b < i; b++)
          ((TransitionListener)arrayList.get(b)).onTransitionStart(this); 
      } 
      this.mEnded = false;
    } 
    this.mNumInstances++;
  }
  
  public String toString() {
    return toString("");
  }
  
  String toString(String paramString) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: aload_1
    //   8: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   11: aload_0
    //   12: invokevirtual getClass : ()Ljava/lang/Class;
    //   15: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   18: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: ldc_w '@'
    //   24: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: aload_0
    //   28: invokevirtual hashCode : ()I
    //   31: invokestatic toHexString : (I)Ljava/lang/String;
    //   34: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   37: ldc_w ': '
    //   40: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: invokevirtual toString : ()Ljava/lang/String;
    //   46: astore_2
    //   47: aload_2
    //   48: astore_1
    //   49: aload_0
    //   50: getfield mDuration : J
    //   53: ldc2_w -1
    //   56: lcmp
    //   57: ifeq -> 94
    //   60: new java/lang/StringBuilder
    //   63: dup
    //   64: invokespecial <init> : ()V
    //   67: aload_2
    //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: ldc_w 'dur('
    //   74: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: aload_0
    //   78: getfield mDuration : J
    //   81: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   84: ldc_w ') '
    //   87: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: astore_1
    //   94: aload_1
    //   95: astore_2
    //   96: aload_0
    //   97: getfield mStartDelay : J
    //   100: ldc2_w -1
    //   103: lcmp
    //   104: ifeq -> 141
    //   107: new java/lang/StringBuilder
    //   110: dup
    //   111: invokespecial <init> : ()V
    //   114: aload_1
    //   115: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: ldc_w 'dly('
    //   121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: aload_0
    //   125: getfield mStartDelay : J
    //   128: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   131: ldc_w ') '
    //   134: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: invokevirtual toString : ()Ljava/lang/String;
    //   140: astore_2
    //   141: aload_2
    //   142: astore_1
    //   143: aload_0
    //   144: getfield mInterpolator : Landroid/animation/TimeInterpolator;
    //   147: ifnull -> 184
    //   150: new java/lang/StringBuilder
    //   153: dup
    //   154: invokespecial <init> : ()V
    //   157: aload_2
    //   158: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   161: ldc_w 'interp('
    //   164: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   167: aload_0
    //   168: getfield mInterpolator : Landroid/animation/TimeInterpolator;
    //   171: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   174: ldc_w ') '
    //   177: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: invokevirtual toString : ()Ljava/lang/String;
    //   183: astore_1
    //   184: aload_0
    //   185: getfield mTargetIds : Ljava/util/ArrayList;
    //   188: invokevirtual size : ()I
    //   191: ifgt -> 206
    //   194: aload_1
    //   195: astore_2
    //   196: aload_0
    //   197: getfield mTargets : Ljava/util/ArrayList;
    //   200: invokevirtual size : ()I
    //   203: ifle -> 420
    //   206: new java/lang/StringBuilder
    //   209: dup
    //   210: invokespecial <init> : ()V
    //   213: aload_1
    //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: ldc_w 'tgts('
    //   220: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   223: invokevirtual toString : ()Ljava/lang/String;
    //   226: astore_2
    //   227: aload_2
    //   228: astore_1
    //   229: aload_0
    //   230: getfield mTargetIds : Ljava/util/ArrayList;
    //   233: invokevirtual size : ()I
    //   236: ifle -> 313
    //   239: iconst_0
    //   240: istore_3
    //   241: aload_2
    //   242: astore_1
    //   243: iload_3
    //   244: aload_0
    //   245: getfield mTargetIds : Ljava/util/ArrayList;
    //   248: invokevirtual size : ()I
    //   251: if_icmpge -> 313
    //   254: aload_2
    //   255: astore_1
    //   256: iload_3
    //   257: ifle -> 281
    //   260: new java/lang/StringBuilder
    //   263: dup
    //   264: invokespecial <init> : ()V
    //   267: aload_2
    //   268: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   271: ldc_w ', '
    //   274: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   277: invokevirtual toString : ()Ljava/lang/String;
    //   280: astore_1
    //   281: new java/lang/StringBuilder
    //   284: dup
    //   285: invokespecial <init> : ()V
    //   288: aload_1
    //   289: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   292: aload_0
    //   293: getfield mTargetIds : Ljava/util/ArrayList;
    //   296: iload_3
    //   297: invokevirtual get : (I)Ljava/lang/Object;
    //   300: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   303: invokevirtual toString : ()Ljava/lang/String;
    //   306: astore_2
    //   307: iinc #3, 1
    //   310: goto -> 241
    //   313: aload_1
    //   314: astore_2
    //   315: aload_0
    //   316: getfield mTargets : Ljava/util/ArrayList;
    //   319: invokevirtual size : ()I
    //   322: ifle -> 399
    //   325: iconst_0
    //   326: istore_3
    //   327: aload_1
    //   328: astore_2
    //   329: iload_3
    //   330: aload_0
    //   331: getfield mTargets : Ljava/util/ArrayList;
    //   334: invokevirtual size : ()I
    //   337: if_icmpge -> 399
    //   340: aload_1
    //   341: astore_2
    //   342: iload_3
    //   343: ifle -> 367
    //   346: new java/lang/StringBuilder
    //   349: dup
    //   350: invokespecial <init> : ()V
    //   353: aload_1
    //   354: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   357: ldc_w ', '
    //   360: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   363: invokevirtual toString : ()Ljava/lang/String;
    //   366: astore_2
    //   367: new java/lang/StringBuilder
    //   370: dup
    //   371: invokespecial <init> : ()V
    //   374: aload_2
    //   375: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   378: aload_0
    //   379: getfield mTargets : Ljava/util/ArrayList;
    //   382: iload_3
    //   383: invokevirtual get : (I)Ljava/lang/Object;
    //   386: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   389: invokevirtual toString : ()Ljava/lang/String;
    //   392: astore_1
    //   393: iinc #3, 1
    //   396: goto -> 327
    //   399: new java/lang/StringBuilder
    //   402: dup
    //   403: invokespecial <init> : ()V
    //   406: aload_2
    //   407: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   410: ldc_w ')'
    //   413: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   416: invokevirtual toString : ()Ljava/lang/String;
    //   419: astore_2
    //   420: aload_2
    //   421: areturn
  }
  
  private static class AnimationInfo {
    String name;
    
    TransitionValues values;
    
    View view;
    
    WindowIdPort windowId;
    
    AnimationInfo(View param1View, String param1String, WindowIdPort param1WindowIdPort, TransitionValues param1TransitionValues) {
      this.view = param1View;
      this.name = param1String;
      this.values = param1TransitionValues;
      this.windowId = param1WindowIdPort;
    }
  }
  
  private static class ArrayListManager {
    static <T> ArrayList<T> add(ArrayList<T> param1ArrayList, T param1T) {
      ArrayList<T> arrayList = param1ArrayList;
      if (param1ArrayList == null)
        arrayList = new ArrayList<T>(); 
      if (!arrayList.contains(param1T))
        arrayList.add(param1T); 
      return arrayList;
    }
    
    static <T> ArrayList<T> remove(ArrayList<T> param1ArrayList, T param1T) {
      ArrayList<T> arrayList = param1ArrayList;
      if (param1ArrayList != null) {
        param1ArrayList.remove(param1T);
        arrayList = param1ArrayList;
        if (param1ArrayList.isEmpty())
          arrayList = null; 
      } 
      return arrayList;
    }
  }
  
  public static interface TransitionListener {
    void onTransitionCancel(TransitionPort param1TransitionPort);
    
    void onTransitionEnd(TransitionPort param1TransitionPort);
    
    void onTransitionPause(TransitionPort param1TransitionPort);
    
    void onTransitionResume(TransitionPort param1TransitionPort);
    
    void onTransitionStart(TransitionPort param1TransitionPort);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static class TransitionListenerAdapter implements TransitionListener {
    public void onTransitionCancel(TransitionPort param1TransitionPort) {}
    
    public void onTransitionEnd(TransitionPort param1TransitionPort) {}
    
    public void onTransitionPause(TransitionPort param1TransitionPort) {}
    
    public void onTransitionResume(TransitionPort param1TransitionPort) {}
    
    public void onTransitionStart(TransitionPort param1TransitionPort) {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionPort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */