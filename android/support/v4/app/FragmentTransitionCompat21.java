package android.support.v4.app;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TargetApi(21)
@RequiresApi(21)
class FragmentTransitionCompat21 {
  public static void addTarget(Object paramObject, View paramView) {
    if (paramObject != null)
      ((Transition)paramObject).addTarget(paramView); 
  }
  
  public static void addTargets(Object paramObject, ArrayList<View> paramArrayList) {
    paramObject = paramObject;
    if (paramObject != null) {
      if (paramObject instanceof TransitionSet) {
        paramObject = paramObject;
        int i = paramObject.getTransitionCount();
        byte b = 0;
        while (true) {
          if (b < i) {
            addTargets(paramObject.getTransitionAt(b), paramArrayList);
            b++;
            continue;
          } 
          return;
        } 
      } 
      if (!hasSimpleTarget((Transition)paramObject) && isNullOrEmpty(paramObject.getTargets())) {
        int i = paramArrayList.size();
        byte b = 0;
        while (true) {
          if (b < i) {
            paramObject.addTarget(paramArrayList.get(b));
            b++;
            continue;
          } 
          return;
        } 
      } 
    } 
  }
  
  public static void beginDelayedTransition(ViewGroup paramViewGroup, Object paramObject) {
    TransitionManager.beginDelayedTransition(paramViewGroup, (Transition)paramObject);
  }
  
  private static void bfsAddViewChildren(List<View> paramList, View paramView) {
    int i = paramList.size();
    if (!containedBeforeIndex(paramList, paramView, i)) {
      paramList.add(paramView);
      int j = i;
      while (true) {
        if (j < paramList.size()) {
          paramView = paramList.get(j);
          if (paramView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)paramView;
            int k = viewGroup.getChildCount();
            for (byte b = 0; b < k; b++) {
              View view = viewGroup.getChildAt(b);
              if (!containedBeforeIndex(paramList, view, i))
                paramList.add(view); 
            } 
          } 
          j++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public static void captureTransitioningViews(ArrayList<View> paramArrayList, View paramView) {
    ViewGroup viewGroup;
    if (paramView.getVisibility() == 0) {
      if (paramView instanceof ViewGroup) {
        viewGroup = (ViewGroup)paramView;
        if (viewGroup.isTransitionGroup()) {
          paramArrayList.add(viewGroup);
          return;
        } 
        int i = viewGroup.getChildCount();
        byte b = 0;
        while (true) {
          if (b < i) {
            captureTransitioningViews(paramArrayList, viewGroup.getChildAt(b));
            b++;
            continue;
          } 
          return;
        } 
      } 
    } else {
      return;
    } 
    paramArrayList.add(viewGroup);
  }
  
  public static Object cloneTransition(Object paramObject) {
    Transition transition = null;
    if (paramObject != null)
      transition = ((Transition)paramObject).clone(); 
    return transition;
  }
  
  private static boolean containedBeforeIndex(List<View> paramList, View paramView, int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: iload_3
    //   3: iload_2
    //   4: if_icmpge -> 30
    //   7: aload_0
    //   8: iload_3
    //   9: invokeinterface get : (I)Ljava/lang/Object;
    //   14: aload_1
    //   15: if_acmpne -> 24
    //   18: iconst_1
    //   19: istore #4
    //   21: iload #4
    //   23: ireturn
    //   24: iinc #3, 1
    //   27: goto -> 2
    //   30: iconst_0
    //   31: istore #4
    //   33: goto -> 21
  }
  
  private static String findKeyForValue(Map<String, String> paramMap, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: invokeinterface entrySet : ()Ljava/util/Set;
    //   6: invokeinterface iterator : ()Ljava/util/Iterator;
    //   11: astore_0
    //   12: aload_0
    //   13: invokeinterface hasNext : ()Z
    //   18: ifeq -> 56
    //   21: aload_0
    //   22: invokeinterface next : ()Ljava/lang/Object;
    //   27: checkcast java/util/Map$Entry
    //   30: astore_2
    //   31: aload_1
    //   32: aload_2
    //   33: invokeinterface getValue : ()Ljava/lang/Object;
    //   38: invokevirtual equals : (Ljava/lang/Object;)Z
    //   41: ifeq -> 12
    //   44: aload_2
    //   45: invokeinterface getKey : ()Ljava/lang/Object;
    //   50: checkcast java/lang/String
    //   53: astore_0
    //   54: aload_0
    //   55: areturn
    //   56: aconst_null
    //   57: astore_0
    //   58: goto -> 54
  }
  
  public static void findNamedViews(Map<String, View> paramMap, View paramView) {
    if (paramView.getVisibility() == 0) {
      String str = paramView.getTransitionName();
      if (str != null)
        paramMap.put(str, paramView); 
      if (paramView instanceof ViewGroup) {
        ViewGroup viewGroup = (ViewGroup)paramView;
        int i = viewGroup.getChildCount();
        for (byte b = 0; b < i; b++)
          findNamedViews(paramMap, viewGroup.getChildAt(b)); 
      } 
    } 
  }
  
  public static void getBoundsOnScreen(View paramView, Rect paramRect) {
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    paramRect.set(arrayOfInt[0], arrayOfInt[1], arrayOfInt[0] + paramView.getWidth(), arrayOfInt[1] + paramView.getHeight());
  }
  
  private static boolean hasSimpleTarget(Transition paramTransition) {
    return (!isNullOrEmpty(paramTransition.getTargetIds()) || !isNullOrEmpty(paramTransition.getTargetNames()) || !isNullOrEmpty(paramTransition.getTargetTypes()));
  }
  
  private static boolean isNullOrEmpty(List paramList) {
    return (paramList == null || paramList.isEmpty());
  }
  
  public static Object mergeTransitionsInSequence(Object paramObject1, Object paramObject2, Object paramObject3) {
    Object object = null;
    paramObject1 = paramObject1;
    paramObject2 = paramObject2;
    paramObject3 = paramObject3;
    if (paramObject1 != null && paramObject2 != null) {
      paramObject1 = (new TransitionSet()).addTransition((Transition)paramObject1).addTransition((Transition)paramObject2).setOrdering(1);
    } else if (paramObject1 == null) {
      paramObject1 = object;
      if (paramObject2 != null)
        paramObject1 = paramObject2; 
    } 
    if (paramObject3 != null) {
      paramObject2 = new TransitionSet();
      if (paramObject1 != null)
        paramObject2.addTransition((Transition)paramObject1); 
      paramObject2.addTransition((Transition)paramObject3);
      paramObject1 = paramObject2;
    } 
    return paramObject1;
  }
  
  public static Object mergeTransitionsTogether(Object paramObject1, Object paramObject2, Object paramObject3) {
    TransitionSet transitionSet = new TransitionSet();
    if (paramObject1 != null)
      transitionSet.addTransition((Transition)paramObject1); 
    if (paramObject2 != null)
      transitionSet.addTransition((Transition)paramObject2); 
    if (paramObject3 != null)
      transitionSet.addTransition((Transition)paramObject3); 
    return transitionSet;
  }
  
  public static ArrayList<String> prepareSetNameOverridesOptimized(ArrayList<View> paramArrayList) {
    ArrayList<String> arrayList = new ArrayList();
    int i = paramArrayList.size();
    for (byte b = 0; b < i; b++) {
      View view = paramArrayList.get(b);
      arrayList.add(view.getTransitionName());
      view.setTransitionName(null);
    } 
    return arrayList;
  }
  
  public static void removeTarget(Object paramObject, View paramView) {
    if (paramObject != null)
      ((Transition)paramObject).removeTarget(paramView); 
  }
  
  public static void replaceTargets(Object paramObject, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2) {
    Transition transition = (Transition)paramObject;
    if (transition instanceof TransitionSet) {
      paramObject = transition;
      int i = paramObject.getTransitionCount();
      for (byte b = 0; b < i; b++)
        replaceTargets(paramObject.getTransitionAt(b), paramArrayList1, paramArrayList2); 
    } else if (!hasSimpleTarget(transition)) {
      paramObject = transition.getTargets();
      if (paramObject != null && paramObject.size() == paramArrayList1.size() && paramObject.containsAll(paramArrayList1)) {
        if (paramArrayList2 == null) {
          i = 0;
        } else {
          i = paramArrayList2.size();
        } 
        for (byte b = 0; b < i; b++)
          transition.addTarget(paramArrayList2.get(b)); 
        for (int i = paramArrayList1.size() - 1; i >= 0; i--)
          transition.removeTarget(paramArrayList1.get(i)); 
      } 
    } 
  }
  
  public static void scheduleHideFragmentView(Object paramObject, final View fragmentView, final ArrayList<View> exitingViews) {
    ((Transition)paramObject).addListener(new Transition.TransitionListener() {
          public void onTransitionCancel(Transition param1Transition) {}
          
          public void onTransitionEnd(Transition param1Transition) {
            param1Transition.removeListener(this);
            fragmentView.setVisibility(8);
            int i = exitingViews.size();
            for (byte b = 0; b < i; b++)
              ((View)exitingViews.get(b)).setVisibility(0); 
          }
          
          public void onTransitionPause(Transition param1Transition) {}
          
          public void onTransitionResume(Transition param1Transition) {}
          
          public void onTransitionStart(Transition param1Transition) {}
        });
  }
  
  public static void scheduleNameReset(final ViewGroup sceneRoot, final ArrayList<View> sharedElementsIn, final Map<String, String> nameOverrides) {
    sceneRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          public boolean onPreDraw() {
            sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            int i = sharedElementsIn.size();
            for (byte b = 0; b < i; b++) {
              View view = sharedElementsIn.get(b);
              String str = view.getTransitionName();
              view.setTransitionName((String)nameOverrides.get(str));
            } 
            return true;
          }
        });
  }
  
  public static void scheduleRemoveTargets(Object paramObject1, final Object enterTransition, final ArrayList<View> enteringViews, final Object exitTransition, final ArrayList<View> exitingViews, final Object sharedElementTransition, final ArrayList<View> sharedElementsIn) {
    ((Transition)paramObject1).addListener(new Transition.TransitionListener() {
          public void onTransitionCancel(Transition param1Transition) {}
          
          public void onTransitionEnd(Transition param1Transition) {}
          
          public void onTransitionPause(Transition param1Transition) {}
          
          public void onTransitionResume(Transition param1Transition) {}
          
          public void onTransitionStart(Transition param1Transition) {
            if (enterTransition != null)
              FragmentTransitionCompat21.replaceTargets(enterTransition, enteringViews, null); 
            if (exitTransition != null)
              FragmentTransitionCompat21.replaceTargets(exitTransition, exitingViews, null); 
            if (sharedElementTransition != null)
              FragmentTransitionCompat21.replaceTargets(sharedElementTransition, sharedElementsIn, null); 
          }
        });
  }
  
  public static void setEpicenter(Object paramObject, final Rect epicenter) {
    if (paramObject != null)
      ((Transition)paramObject).setEpicenterCallback(new Transition.EpicenterCallback() {
            public Rect onGetEpicenter(Transition param1Transition) {
              return (epicenter == null || epicenter.isEmpty()) ? null : epicenter;
            }
          }); 
  }
  
  public static void setEpicenter(final Object epicenter, View paramView) {
    if (paramView != null) {
      Transition transition = (Transition)epicenter;
      epicenter = new Rect();
      getBoundsOnScreen(paramView, (Rect)epicenter);
      transition.setEpicenterCallback(new Transition.EpicenterCallback() {
            public Rect onGetEpicenter(Transition param1Transition) {
              return epicenter;
            }
          });
    } 
  }
  
  public static void setNameOverridesOptimized(final View sceneRoot, final ArrayList<View> sharedElementsOut, final ArrayList<View> sharedElementsIn, final ArrayList<String> inNames, Map<String, String> paramMap) {
    final int numSharedElements = sharedElementsIn.size();
    final ArrayList<String> outNames = new ArrayList();
    byte b = 0;
    label18: while (b < i) {
      View view = sharedElementsOut.get(b);
      String str2 = view.getTransitionName();
      arrayList.add(str2);
      if (str2 == null)
        continue; 
      view.setTransitionName(null);
      String str1 = paramMap.get(str2);
      byte b1 = 0;
      while (true) {
        if (b1 < i)
          if (str1.equals(inNames.get(b1))) {
            ((View)sharedElementsIn.get(b1)).setTransitionName(str2);
          } else {
            b1++;
            continue;
          }  
        b++;
        continue label18;
      } 
    } 
    sceneRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          public boolean onPreDraw() {
            sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            for (byte b = 0; b < numSharedElements; b++) {
              ((View)sharedElementsIn.get(b)).setTransitionName(inNames.get(b));
              ((View)sharedElementsOut.get(b)).setTransitionName(outNames.get(b));
            } 
            return true;
          }
        });
  }
  
  public static void setNameOverridesUnoptimized(final View sceneRoot, final ArrayList<View> sharedElementsIn, final Map<String, String> nameOverrides) {
    sceneRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          public boolean onPreDraw() {
            sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            int i = sharedElementsIn.size();
            for (byte b = 0; b < i; b++) {
              View view = sharedElementsIn.get(b);
              String str = view.getTransitionName();
              if (str != null)
                view.setTransitionName(FragmentTransitionCompat21.findKeyForValue(nameOverrides, str)); 
            } 
            return true;
          }
        });
  }
  
  public static void setSharedElementTargets(Object paramObject, View paramView, ArrayList<View> paramArrayList) {
    paramObject = paramObject;
    List<View> list = paramObject.getTargets();
    list.clear();
    int i = paramArrayList.size();
    for (byte b = 0; b < i; b++)
      bfsAddViewChildren(list, paramArrayList.get(b)); 
    list.add(paramView);
    paramArrayList.add(paramView);
    addTargets(paramObject, paramArrayList);
  }
  
  public static void swapSharedElementTargets(Object paramObject, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2) {
    paramObject = paramObject;
    if (paramObject != null) {
      paramObject.getTargets().clear();
      paramObject.getTargets().addAll(paramArrayList2);
      replaceTargets(paramObject, paramArrayList1, paramArrayList2);
    } 
  }
  
  public static Object wrapTransitionInSet(Object paramObject) {
    if (paramObject == null)
      return null; 
    TransitionSet transitionSet = new TransitionSet();
    transitionSet.addTransition((Transition)paramObject);
    return transitionSet;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/app/FragmentTransitionCompat21.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */