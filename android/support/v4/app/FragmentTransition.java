package android.support.v4.app;

import android.graphics.Rect;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.util.ArrayList;
import java.util.Map;

class FragmentTransition {
  private static final int[] INVERSE_OPS = new int[] { 0, 3, 0, 1, 5, 4, 7, 6 };
  
  private static void addToFirstInLastOut(BackStackRecord paramBackStackRecord, BackStackRecord.Op paramOp, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aload_1
    //   1: getfield fragment : Landroid/support/v4/app/Fragment;
    //   4: astore #5
    //   6: aload #5
    //   8: getfield mContainerId : I
    //   11: istore #6
    //   13: iload #6
    //   15: ifne -> 19
    //   18: return
    //   19: iload_3
    //   20: ifeq -> 325
    //   23: getstatic android/support/v4/app/FragmentTransition.INVERSE_OPS : [I
    //   26: aload_1
    //   27: getfield cmd : I
    //   30: iaload
    //   31: istore #7
    //   33: iconst_0
    //   34: istore #8
    //   36: iconst_0
    //   37: istore #9
    //   39: iconst_0
    //   40: istore #10
    //   42: iconst_0
    //   43: istore #11
    //   45: iload #10
    //   47: istore #12
    //   49: iload #8
    //   51: istore #13
    //   53: iload #11
    //   55: istore #14
    //   57: iload #9
    //   59: istore #15
    //   61: iload #7
    //   63: tableswitch default -> 104, 1 -> 396, 2 -> 120, 3 -> 530, 4 -> 450, 5 -> 334, 6 -> 530, 7 -> 396
    //   104: iload #9
    //   106: istore #15
    //   108: iload #11
    //   110: istore #14
    //   112: iload #8
    //   114: istore #13
    //   116: iload #10
    //   118: istore #12
    //   120: aload_2
    //   121: iload #6
    //   123: invokevirtual get : (I)Ljava/lang/Object;
    //   126: checkcast android/support/v4/app/FragmentTransition$FragmentContainerTransition
    //   129: astore #16
    //   131: aload #16
    //   133: astore_1
    //   134: iload #13
    //   136: ifeq -> 164
    //   139: aload #16
    //   141: aload_2
    //   142: iload #6
    //   144: invokestatic ensureContainer : (Landroid/support/v4/app/FragmentTransition$FragmentContainerTransition;Landroid/util/SparseArray;I)Landroid/support/v4/app/FragmentTransition$FragmentContainerTransition;
    //   147: astore_1
    //   148: aload_1
    //   149: aload #5
    //   151: putfield lastIn : Landroid/support/v4/app/Fragment;
    //   154: aload_1
    //   155: iload_3
    //   156: putfield lastInIsPop : Z
    //   159: aload_1
    //   160: aload_0
    //   161: putfield lastInTransaction : Landroid/support/v4/app/BackStackRecord;
    //   164: iload #4
    //   166: ifne -> 241
    //   169: iload #14
    //   171: ifeq -> 241
    //   174: aload_1
    //   175: ifnull -> 192
    //   178: aload_1
    //   179: getfield firstOut : Landroid/support/v4/app/Fragment;
    //   182: aload #5
    //   184: if_acmpne -> 192
    //   187: aload_1
    //   188: aconst_null
    //   189: putfield firstOut : Landroid/support/v4/app/Fragment;
    //   192: aload_0
    //   193: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   196: astore #16
    //   198: aload #5
    //   200: getfield mState : I
    //   203: iconst_1
    //   204: if_icmpge -> 241
    //   207: aload #16
    //   209: getfield mCurState : I
    //   212: iconst_1
    //   213: if_icmplt -> 241
    //   216: aload_0
    //   217: getfield mAllowOptimization : Z
    //   220: ifne -> 241
    //   223: aload #16
    //   225: aload #5
    //   227: invokevirtual makeActive : (Landroid/support/v4/app/Fragment;)V
    //   230: aload #16
    //   232: aload #5
    //   234: iconst_1
    //   235: iconst_0
    //   236: iconst_0
    //   237: iconst_0
    //   238: invokevirtual moveToState : (Landroid/support/v4/app/Fragment;IIIZ)V
    //   241: aload_1
    //   242: astore #16
    //   244: iload #12
    //   246: ifeq -> 291
    //   249: aload_1
    //   250: ifnull -> 263
    //   253: aload_1
    //   254: astore #16
    //   256: aload_1
    //   257: getfield firstOut : Landroid/support/v4/app/Fragment;
    //   260: ifnonnull -> 291
    //   263: aload_1
    //   264: aload_2
    //   265: iload #6
    //   267: invokestatic ensureContainer : (Landroid/support/v4/app/FragmentTransition$FragmentContainerTransition;Landroid/util/SparseArray;I)Landroid/support/v4/app/FragmentTransition$FragmentContainerTransition;
    //   270: astore #16
    //   272: aload #16
    //   274: aload #5
    //   276: putfield firstOut : Landroid/support/v4/app/Fragment;
    //   279: aload #16
    //   281: iload_3
    //   282: putfield firstOutIsPop : Z
    //   285: aload #16
    //   287: aload_0
    //   288: putfield firstOutTransaction : Landroid/support/v4/app/BackStackRecord;
    //   291: iload #4
    //   293: ifne -> 18
    //   296: iload #15
    //   298: ifeq -> 18
    //   301: aload #16
    //   303: ifnull -> 18
    //   306: aload #16
    //   308: getfield lastIn : Landroid/support/v4/app/Fragment;
    //   311: aload #5
    //   313: if_acmpne -> 18
    //   316: aload #16
    //   318: aconst_null
    //   319: putfield lastIn : Landroid/support/v4/app/Fragment;
    //   322: goto -> 18
    //   325: aload_1
    //   326: getfield cmd : I
    //   329: istore #7
    //   331: goto -> 33
    //   334: iload #4
    //   336: ifeq -> 386
    //   339: aload #5
    //   341: getfield mHiddenChanged : Z
    //   344: ifeq -> 380
    //   347: aload #5
    //   349: getfield mHidden : Z
    //   352: ifne -> 380
    //   355: aload #5
    //   357: getfield mAdded : Z
    //   360: ifeq -> 380
    //   363: iconst_1
    //   364: istore #13
    //   366: iconst_1
    //   367: istore #14
    //   369: iload #10
    //   371: istore #12
    //   373: iload #9
    //   375: istore #15
    //   377: goto -> 120
    //   380: iconst_0
    //   381: istore #13
    //   383: goto -> 366
    //   386: aload #5
    //   388: getfield mHidden : Z
    //   391: istore #13
    //   393: goto -> 366
    //   396: iload #4
    //   398: ifeq -> 422
    //   401: aload #5
    //   403: getfield mIsNewlyAdded : Z
    //   406: istore #13
    //   408: iconst_1
    //   409: istore #14
    //   411: iload #10
    //   413: istore #12
    //   415: iload #9
    //   417: istore #15
    //   419: goto -> 120
    //   422: aload #5
    //   424: getfield mAdded : Z
    //   427: ifne -> 444
    //   430: aload #5
    //   432: getfield mHidden : Z
    //   435: ifne -> 444
    //   438: iconst_1
    //   439: istore #13
    //   441: goto -> 408
    //   444: iconst_0
    //   445: istore #13
    //   447: goto -> 441
    //   450: iload #4
    //   452: ifeq -> 502
    //   455: aload #5
    //   457: getfield mHiddenChanged : Z
    //   460: ifeq -> 496
    //   463: aload #5
    //   465: getfield mAdded : Z
    //   468: ifeq -> 496
    //   471: aload #5
    //   473: getfield mHidden : Z
    //   476: ifeq -> 496
    //   479: iconst_1
    //   480: istore #12
    //   482: iconst_1
    //   483: istore #15
    //   485: iload #8
    //   487: istore #13
    //   489: iload #11
    //   491: istore #14
    //   493: goto -> 120
    //   496: iconst_0
    //   497: istore #12
    //   499: goto -> 482
    //   502: aload #5
    //   504: getfield mAdded : Z
    //   507: ifeq -> 524
    //   510: aload #5
    //   512: getfield mHidden : Z
    //   515: ifne -> 524
    //   518: iconst_1
    //   519: istore #12
    //   521: goto -> 482
    //   524: iconst_0
    //   525: istore #12
    //   527: goto -> 521
    //   530: iload #4
    //   532: ifeq -> 585
    //   535: aload #5
    //   537: getfield mAdded : Z
    //   540: ifne -> 579
    //   543: aload #5
    //   545: getfield mView : Landroid/view/View;
    //   548: ifnull -> 579
    //   551: aload #5
    //   553: getfield mView : Landroid/view/View;
    //   556: invokevirtual getVisibility : ()I
    //   559: ifne -> 579
    //   562: iconst_1
    //   563: istore #12
    //   565: iconst_1
    //   566: istore #15
    //   568: iload #8
    //   570: istore #13
    //   572: iload #11
    //   574: istore #14
    //   576: goto -> 120
    //   579: iconst_0
    //   580: istore #12
    //   582: goto -> 565
    //   585: aload #5
    //   587: getfield mAdded : Z
    //   590: ifeq -> 607
    //   593: aload #5
    //   595: getfield mHidden : Z
    //   598: ifne -> 607
    //   601: iconst_1
    //   602: istore #12
    //   604: goto -> 565
    //   607: iconst_0
    //   608: istore #12
    //   610: goto -> 604
  }
  
  public static void calculateFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean) {
    int i = paramBackStackRecord.mOps.size();
    for (byte b = 0; b < i; b++)
      addToFirstInLastOut(paramBackStackRecord, paramBackStackRecord.mOps.get(b), paramSparseArray, false, paramBoolean); 
  }
  
  private static ArrayMap<String, String> calculateNameOverrides(int paramInt1, ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt2, int paramInt3) {
    ArrayMap<String, String> arrayMap = new ArrayMap();
    label27: while (--paramInt3 >= paramInt2) {
      BackStackRecord backStackRecord = paramArrayList.get(paramInt3);
      if (!backStackRecord.interactsWith(paramInt1))
        continue; 
      boolean bool = ((Boolean)paramArrayList1.get(paramInt3)).booleanValue();
      if (backStackRecord.mSharedElementSourceNames != null) {
        ArrayList<String> arrayList1;
        ArrayList<String> arrayList2;
        int i = backStackRecord.mSharedElementSourceNames.size();
        if (bool) {
          arrayList1 = backStackRecord.mSharedElementSourceNames;
          arrayList2 = backStackRecord.mSharedElementTargetNames;
        } else {
          arrayList2 = backStackRecord.mSharedElementSourceNames;
          arrayList1 = backStackRecord.mSharedElementTargetNames;
        } 
        byte b = 0;
        while (true) {
          if (b < i) {
            String str2 = arrayList2.get(b);
            String str3 = arrayList1.get(b);
            String str1 = (String)arrayMap.remove(str3);
            if (str1 != null) {
              arrayMap.put(str2, str1);
            } else {
              arrayMap.put(str2, str3);
            } 
            b++;
            continue;
          } 
          paramInt3--;
          continue label27;
        } 
      } 
      continue;
    } 
    return arrayMap;
  }
  
  public static void calculatePopFragments(BackStackRecord paramBackStackRecord, SparseArray<FragmentContainerTransition> paramSparseArray, boolean paramBoolean) {
    if (paramBackStackRecord.mManager.mContainer.onHasView()) {
      int i = paramBackStackRecord.mOps.size() - 1;
      while (true) {
        if (i >= 0) {
          addToFirstInLastOut(paramBackStackRecord, paramBackStackRecord.mOps.get(i), paramSparseArray, true, paramBoolean);
          i--;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private static void callSharedElementStartEnd(Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean1, ArrayMap<String, View> paramArrayMap, boolean paramBoolean2) {
    SharedElementCallback sharedElementCallback;
    ArrayList<Object> arrayList1;
    ArrayList<Object> arrayList2;
    if (paramBoolean1) {
      sharedElementCallback = paramFragment2.getEnterTransitionCallback();
    } else {
      sharedElementCallback = sharedElementCallback.getEnterTransitionCallback();
    } 
    if (sharedElementCallback != null) {
      int i;
      arrayList1 = new ArrayList();
      arrayList2 = new ArrayList();
      if (paramArrayMap == null) {
        i = 0;
      } else {
        i = paramArrayMap.size();
      } 
      for (byte b = 0; b < i; b++) {
        arrayList2.add(paramArrayMap.keyAt(b));
        arrayList1.add(paramArrayMap.valueAt(b));
      } 
      if (paramBoolean2) {
        sharedElementCallback.onSharedElementStart(arrayList2, arrayList1, null);
        return;
      } 
    } else {
      return;
    } 
    sharedElementCallback.onSharedElementEnd(arrayList2, arrayList1, null);
  }
  
  private static ArrayMap<String, View> captureInSharedElements(ArrayMap<String, String> paramArrayMap, Object<String> paramObject, FragmentContainerTransition paramFragmentContainerTransition) {
    ArrayList<String> arrayList;
    SharedElementCallback sharedElementCallback;
    Fragment fragment = paramFragmentContainerTransition.lastIn;
    View view = fragment.getView();
    if (paramArrayMap.isEmpty() || paramObject == null || view == null) {
      paramArrayMap.clear();
      return null;
    } 
    ArrayMap<String, View> arrayMap = new ArrayMap();
    FragmentTransitionCompat21.findNamedViews((Map<String, View>)arrayMap, view);
    paramObject = (Object<String>)paramFragmentContainerTransition.lastInTransaction;
    if (paramFragmentContainerTransition.lastInIsPop) {
      sharedElementCallback = fragment.getExitTransitionCallback();
      paramObject = (Object<String>)((BackStackRecord)paramObject).mSharedElementSourceNames;
    } else {
      sharedElementCallback = fragment.getEnterTransitionCallback();
      arrayList = ((BackStackRecord)paramObject).mSharedElementTargetNames;
    } 
    arrayMap.retainAll(arrayList);
    if (sharedElementCallback != null) {
      sharedElementCallback.onMapSharedElements(arrayList, (Map<String, View>)arrayMap);
      int i = arrayList.size() - 1;
      while (true) {
        String str;
        ArrayMap<String, View> arrayMap1 = arrayMap;
        if (i >= 0) {
          String str1 = arrayList.get(i);
          View view1 = (View)arrayMap.get(str1);
          if (view1 == null) {
            str = findKeyForValue(paramArrayMap, str1);
            if (str != null)
              paramArrayMap.remove(str); 
          } else if (!str1.equals(ViewCompat.getTransitionName((View)str))) {
            str1 = findKeyForValue(paramArrayMap, str1);
            if (str1 != null)
              paramArrayMap.put(str1, ViewCompat.getTransitionName((View)str)); 
          } 
          i--;
          continue;
        } 
        return (ArrayMap<String, View>)str;
      } 
    } 
    retainValues(paramArrayMap, arrayMap);
    return arrayMap;
  }
  
  private static ArrayMap<String, View> captureOutSharedElements(ArrayMap<String, String> paramArrayMap, Object<String> paramObject, FragmentContainerTransition paramFragmentContainerTransition) {
    ArrayList<String> arrayList;
    SharedElementCallback sharedElementCallback;
    if (paramArrayMap.isEmpty() || paramObject == null) {
      paramArrayMap.clear();
      return null;
    } 
    Fragment fragment = paramFragmentContainerTransition.firstOut;
    ArrayMap<String, View> arrayMap = new ArrayMap();
    FragmentTransitionCompat21.findNamedViews((Map<String, View>)arrayMap, fragment.getView());
    paramObject = (Object<String>)paramFragmentContainerTransition.firstOutTransaction;
    if (paramFragmentContainerTransition.firstOutIsPop) {
      sharedElementCallback = fragment.getEnterTransitionCallback();
      paramObject = (Object<String>)((BackStackRecord)paramObject).mSharedElementTargetNames;
    } else {
      sharedElementCallback = fragment.getExitTransitionCallback();
      arrayList = ((BackStackRecord)paramObject).mSharedElementSourceNames;
    } 
    arrayMap.retainAll(arrayList);
    if (sharedElementCallback != null) {
      sharedElementCallback.onMapSharedElements(arrayList, (Map<String, View>)arrayMap);
      int i = arrayList.size() - 1;
      while (true) {
        View view;
        ArrayMap<String, View> arrayMap1 = arrayMap;
        if (i >= 0) {
          String str = arrayList.get(i);
          view = (View)arrayMap.get(str);
          if (view == null) {
            paramArrayMap.remove(str);
          } else if (!str.equals(ViewCompat.getTransitionName(view))) {
            str = (String)paramArrayMap.remove(str);
            paramArrayMap.put(ViewCompat.getTransitionName(view), str);
          } 
          i--;
          continue;
        } 
        return (ArrayMap<String, View>)view;
      } 
    } 
    paramArrayMap.retainAll(arrayMap.keySet());
    return arrayMap;
  }
  
  private static ArrayList<View> configureEnteringExitingViews(Object paramObject, Fragment paramFragment, ArrayList<View> paramArrayList, View paramView) {
    ArrayList<View> arrayList = null;
    if (paramObject != null) {
      ArrayList<View> arrayList1 = new ArrayList();
      FragmentTransitionCompat21.captureTransitioningViews(arrayList1, paramFragment.getView());
      if (paramArrayList != null)
        arrayList1.removeAll(paramArrayList); 
      arrayList = arrayList1;
      if (!arrayList1.isEmpty()) {
        arrayList1.add(paramView);
        FragmentTransitionCompat21.addTargets(paramObject, arrayList1);
        arrayList = arrayList1;
      } 
    } 
    return arrayList;
  }
  
  private static Object configureSharedElementsOptimized(final ViewGroup sceneRoot, final View epicenterView, ArrayMap<String, String> paramArrayMap, final FragmentContainerTransition epicenter, ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2, Object paramObject1, Object paramObject2) {
    Object object1;
    Object object2;
    final Fragment inFragment = epicenter.lastIn;
    final Fragment outFragment = epicenter.firstOut;
    if (fragment1 != null)
      fragment1.getView().setVisibility(0); 
    if (fragment1 == null || fragment2 == null)
      return null; 
    final boolean inIsPop = epicenter.lastInIsPop;
    if (paramArrayMap.isEmpty()) {
      object2 = null;
    } else {
      object2 = getSharedElementTransition(fragment1, fragment2, bool);
    } 
    ArrayMap<String, View> arrayMap1 = captureOutSharedElements(paramArrayMap, object2, epicenter);
    final ArrayMap<String, View> inSharedElements = captureInSharedElements(paramArrayMap, object2, epicenter);
    if (paramArrayMap.isEmpty()) {
      paramArrayMap = null;
    } else {
      paramArrayList1.addAll(arrayMap1.values());
      paramArrayList2.addAll(arrayMap2.values());
      object1 = object2;
    } 
    if (paramObject1 == null && paramObject2 == null && object1 == null)
      return null; 
    callSharedElementStartEnd(fragment1, fragment2, bool, arrayMap1, true);
    if (object1 != null) {
      paramArrayList2.add(epicenterView);
      FragmentTransitionCompat21.setSharedElementTargets(object1, epicenterView, paramArrayList1);
      setOutEpicenter(object1, paramObject2, arrayMap1, epicenter.firstOutIsPop, epicenter.firstOutTransaction);
      Rect rect2 = new Rect();
      View view = getInEpicenterView(arrayMap2, epicenter, paramObject1, bool);
      epicenterView = view;
      Rect rect1 = rect2;
      if (view != null) {
        FragmentTransitionCompat21.setEpicenter(paramObject1, rect2);
        rect1 = rect2;
        epicenterView = view;
      } 
    } else {
      epicenter = null;
      epicenterView = null;
    } 
    sceneRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          public boolean onPreDraw() {
            sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, inSharedElements, false);
            if (epicenterView != null)
              FragmentTransitionCompat21.getBoundsOnScreen(epicenterView, epicenter); 
            return true;
          }
        });
    return object1;
  }
  
  private static Object configureSharedElementsUnoptimized(final ViewGroup sceneRoot, final View nonExistentView, final ArrayMap<String, String> nameOverrides, final FragmentContainerTransition fragments, final ArrayList<View> sharedElementsOut, final ArrayList<View> sharedElementsIn, final Object enterTransition, final Object inEpicenter) {
    final Object finalSharedElementTransition;
    final Fragment inFragment = fragments.lastIn;
    final Fragment outFragment = fragments.firstOut;
    if (fragment1 == null || fragment2 == null)
      return null; 
    final boolean inIsPop = fragments.lastInIsPop;
    if (nameOverrides.isEmpty()) {
      object = null;
    } else {
      object = getSharedElementTransition(fragment1, fragment2, bool);
    } 
    ArrayMap<String, View> arrayMap = captureOutSharedElements(nameOverrides, object, fragments);
    if (nameOverrides.isEmpty()) {
      object = null;
    } else {
      sharedElementsOut.addAll(arrayMap.values());
    } 
    if (enterTransition == null && inEpicenter == null && object == null)
      return null; 
    callSharedElementStartEnd(fragment1, fragment2, bool, arrayMap, true);
    if (object != null) {
      Rect rect = new Rect();
      FragmentTransitionCompat21.setSharedElementTargets(object, nonExistentView, sharedElementsOut);
      setOutEpicenter(object, inEpicenter, arrayMap, fragments.firstOutIsPop, fragments.firstOutTransaction);
      inEpicenter = rect;
      if (enterTransition != null) {
        FragmentTransitionCompat21.setEpicenter(enterTransition, rect);
        inEpicenter = rect;
      } 
    } else {
      inEpicenter = null;
    } 
    sceneRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          public boolean onPreDraw() {
            sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            ArrayMap arrayMap = FragmentTransition.captureInSharedElements(nameOverrides, finalSharedElementTransition, fragments);
            if (arrayMap != null) {
              sharedElementsIn.addAll(arrayMap.values());
              sharedElementsIn.add(nonExistentView);
            } 
            FragmentTransition.callSharedElementStartEnd(inFragment, outFragment, inIsPop, arrayMap, false);
            if (finalSharedElementTransition != null) {
              FragmentTransitionCompat21.swapSharedElementTargets(finalSharedElementTransition, sharedElementsOut, sharedElementsIn);
              View view = FragmentTransition.getInEpicenterView(arrayMap, fragments, enterTransition, inIsPop);
              if (view != null)
                FragmentTransitionCompat21.getBoundsOnScreen(view, inEpicenter); 
            } 
            return true;
          }
        });
    return object;
  }
  
  private static void configureTransitionsOptimized(FragmentManagerImpl paramFragmentManagerImpl, int paramInt, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap) {
    ViewGroup viewGroup = (ViewGroup)paramFragmentManagerImpl.mContainer.onFindViewById(paramInt);
    if (viewGroup != null) {
      Fragment fragment1 = paramFragmentContainerTransition.lastIn;
      Fragment fragment2 = paramFragmentContainerTransition.firstOut;
      boolean bool1 = paramFragmentContainerTransition.lastInIsPop;
      boolean bool2 = paramFragmentContainerTransition.firstOutIsPop;
      ArrayList<View> arrayList1 = new ArrayList();
      ArrayList<View> arrayList2 = new ArrayList();
      Object object2 = getEnterTransition(fragment1, bool1);
      Object object1 = getExitTransition(fragment2, bool2);
      Object object3 = configureSharedElementsOptimized(viewGroup, paramView, paramArrayMap, paramFragmentContainerTransition, arrayList2, arrayList1, object2, object1);
      if (object2 != null || object3 != null || object1 != null) {
        ArrayList<View> arrayList3 = configureEnteringExitingViews(object1, fragment2, arrayList2, paramView);
        ArrayList<View> arrayList4 = configureEnteringExitingViews(object2, fragment1, arrayList1, paramView);
        setViewVisibility(arrayList4, 4);
        Object object = mergeTransitions(object2, object1, object3, fragment1, bool1);
        if (object != null) {
          replaceHide(object1, fragment2, arrayList3);
          ArrayList<String> arrayList = FragmentTransitionCompat21.prepareSetNameOverridesOptimized(arrayList1);
          FragmentTransitionCompat21.scheduleRemoveTargets(object, object2, arrayList4, object1, arrayList3, object3, arrayList1);
          FragmentTransitionCompat21.beginDelayedTransition(viewGroup, object);
          FragmentTransitionCompat21.setNameOverridesOptimized((View)viewGroup, arrayList2, arrayList1, arrayList, (Map<String, String>)paramArrayMap);
          setViewVisibility(arrayList4, 0);
          FragmentTransitionCompat21.swapSharedElementTargets(object3, arrayList2, arrayList1);
        } 
      } 
    } 
  }
  
  private static void configureTransitionsUnoptimized(FragmentManagerImpl paramFragmentManagerImpl, int paramInt, FragmentContainerTransition paramFragmentContainerTransition, View paramView, ArrayMap<String, String> paramArrayMap) {
    ViewGroup viewGroup = (ViewGroup)paramFragmentManagerImpl.mContainer.onFindViewById(paramInt);
    if (viewGroup != null) {
      Fragment fragment1 = paramFragmentContainerTransition.lastIn;
      Fragment fragment2 = paramFragmentContainerTransition.firstOut;
      boolean bool1 = paramFragmentContainerTransition.lastInIsPop;
      boolean bool2 = paramFragmentContainerTransition.firstOutIsPop;
      Object object2 = getEnterTransition(fragment1, bool1);
      Object object1 = getExitTransition(fragment2, bool2);
      ArrayList<View> arrayList1 = new ArrayList();
      ArrayList<View> arrayList2 = new ArrayList();
      Object object3 = configureSharedElementsUnoptimized(viewGroup, paramView, paramArrayMap, paramFragmentContainerTransition, arrayList1, arrayList2, object2, object1);
      if (object2 != null || object3 != null || object1 != null) {
        arrayList1 = configureEnteringExitingViews(object1, fragment2, arrayList1, paramView);
        if (arrayList1 == null || arrayList1.isEmpty())
          object1 = null; 
        FragmentTransitionCompat21.addTarget(object2, paramView);
        Object object = mergeTransitions(object2, object1, object3, fragment1, paramFragmentContainerTransition.lastInIsPop);
        if (object != null) {
          ArrayList<View> arrayList = new ArrayList();
          FragmentTransitionCompat21.scheduleRemoveTargets(object, object2, arrayList, object1, arrayList1, object3, arrayList2);
          scheduleTargetChange(viewGroup, fragment1, paramView, arrayList2, object2, arrayList, object1, arrayList1);
          FragmentTransitionCompat21.setNameOverridesUnoptimized((View)viewGroup, arrayList2, (Map<String, String>)paramArrayMap);
          FragmentTransitionCompat21.beginDelayedTransition(viewGroup, object);
          FragmentTransitionCompat21.scheduleNameReset(viewGroup, arrayList2, (Map<String, String>)paramArrayMap);
        } 
      } 
    } 
  }
  
  private static FragmentContainerTransition ensureContainer(FragmentContainerTransition paramFragmentContainerTransition, SparseArray<FragmentContainerTransition> paramSparseArray, int paramInt) {
    FragmentContainerTransition fragmentContainerTransition = paramFragmentContainerTransition;
    if (paramFragmentContainerTransition == null) {
      fragmentContainerTransition = new FragmentContainerTransition();
      paramSparseArray.put(paramInt, fragmentContainerTransition);
    } 
    return fragmentContainerTransition;
  }
  
  private static String findKeyForValue(ArrayMap<String, String> paramArrayMap, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual size : ()I
    //   4: istore_2
    //   5: iconst_0
    //   6: istore_3
    //   7: iload_3
    //   8: iload_2
    //   9: if_icmpge -> 41
    //   12: aload_1
    //   13: aload_0
    //   14: iload_3
    //   15: invokevirtual valueAt : (I)Ljava/lang/Object;
    //   18: invokevirtual equals : (Ljava/lang/Object;)Z
    //   21: ifeq -> 35
    //   24: aload_0
    //   25: iload_3
    //   26: invokevirtual keyAt : (I)Ljava/lang/Object;
    //   29: checkcast java/lang/String
    //   32: astore_0
    //   33: aload_0
    //   34: areturn
    //   35: iinc #3, 1
    //   38: goto -> 7
    //   41: aconst_null
    //   42: astore_0
    //   43: goto -> 33
  }
  
  private static Object getEnterTransition(Fragment paramFragment, boolean paramBoolean) {
    if (paramFragment == null)
      return null; 
    if (paramBoolean) {
      null = paramFragment.getReenterTransition();
    } else {
      null = null.getEnterTransition();
    } 
    return FragmentTransitionCompat21.cloneTransition(null);
  }
  
  private static Object getExitTransition(Fragment paramFragment, boolean paramBoolean) {
    if (paramFragment == null)
      return null; 
    if (paramBoolean) {
      null = paramFragment.getReturnTransition();
    } else {
      null = null.getExitTransition();
    } 
    return FragmentTransitionCompat21.cloneTransition(null);
  }
  
  private static View getInEpicenterView(ArrayMap<String, View> paramArrayMap, FragmentContainerTransition paramFragmentContainerTransition, Object paramObject, boolean paramBoolean) {
    BackStackRecord backStackRecord = paramFragmentContainerTransition.lastInTransaction;
    if (paramObject != null && backStackRecord.mSharedElementSourceNames != null && !backStackRecord.mSharedElementSourceNames.isEmpty()) {
      String str;
      if (paramBoolean) {
        str = backStackRecord.mSharedElementSourceNames.get(0);
      } else {
        str = ((BackStackRecord)str).mSharedElementTargetNames.get(0);
      } 
      return (View)paramArrayMap.get(str);
    } 
    return null;
  }
  
  private static Object getSharedElementTransition(Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean) {
    if (paramFragment1 == null || paramFragment2 == null)
      return null; 
    if (paramBoolean) {
      null = paramFragment2.getSharedElementReturnTransition();
    } else {
      null = null.getSharedElementEnterTransition();
    } 
    return FragmentTransitionCompat21.wrapTransitionInSet(FragmentTransitionCompat21.cloneTransition(null));
  }
  
  private static Object mergeTransitions(Object paramObject1, Object paramObject2, Object paramObject3, Fragment paramFragment, boolean paramBoolean) {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramObject1 != null) {
      bool2 = bool1;
      if (paramObject2 != null) {
        bool2 = bool1;
        if (paramFragment != null)
          if (paramBoolean) {
            bool2 = paramFragment.getAllowReturnTransitionOverlap();
          } else {
            bool2 = paramFragment.getAllowEnterTransitionOverlap();
          }  
      } 
    } 
    return bool2 ? FragmentTransitionCompat21.mergeTransitionsTogether(paramObject2, paramObject1, paramObject3) : FragmentTransitionCompat21.mergeTransitionsInSequence(paramObject2, paramObject1, paramObject3);
  }
  
  private static void replaceHide(final Object container, Fragment paramFragment, final ArrayList<View> exitingViews) {
    if (paramFragment != null && container != null && paramFragment.mAdded && paramFragment.mHidden && paramFragment.mHiddenChanged) {
      paramFragment.setHideReplaced(true);
      FragmentTransitionCompat21.scheduleHideFragmentView(container, paramFragment.getView(), exitingViews);
      container = paramFragment.mContainer;
      container.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
              container.getViewTreeObserver().removeOnPreDrawListener(this);
              FragmentTransition.setViewVisibility(exitingViews, 4);
              return true;
            }
          });
    } 
  }
  
  private static void retainValues(ArrayMap<String, String> paramArrayMap, ArrayMap<String, View> paramArrayMap1) {
    for (int i = paramArrayMap.size() - 1; i >= 0; i--) {
      if (!paramArrayMap1.containsKey(paramArrayMap.valueAt(i)))
        paramArrayMap.removeAt(i); 
    } 
  }
  
  private static void scheduleTargetChange(final ViewGroup sceneRoot, final Fragment inFragment, final View nonExistentView, final ArrayList<View> sharedElementsIn, final Object enterTransition, final ArrayList<View> enteringViews, final Object exitTransition, final ArrayList<View> exitingViews) {
    sceneRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          public boolean onPreDraw() {
            sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            if (enterTransition != null) {
              FragmentTransitionCompat21.removeTarget(enterTransition, nonExistentView);
              ArrayList arrayList = FragmentTransition.configureEnteringExitingViews(enterTransition, inFragment, sharedElementsIn, nonExistentView);
              enteringViews.addAll(arrayList);
            } 
            if (exitingViews != null) {
              ArrayList<View> arrayList = new ArrayList();
              arrayList.add(nonExistentView);
              FragmentTransitionCompat21.replaceTargets(exitTransition, exitingViews, arrayList);
              exitingViews.clear();
              exitingViews.add(nonExistentView);
            } 
            return true;
          }
        });
  }
  
  private static void setOutEpicenter(Object paramObject1, Object paramObject2, ArrayMap<String, View> paramArrayMap, boolean paramBoolean, BackStackRecord paramBackStackRecord) {
    if (paramBackStackRecord.mSharedElementSourceNames != null && !paramBackStackRecord.mSharedElementSourceNames.isEmpty()) {
      String str;
      if (paramBoolean) {
        str = paramBackStackRecord.mSharedElementTargetNames.get(0);
      } else {
        str = ((BackStackRecord)str).mSharedElementSourceNames.get(0);
      } 
      View view = (View)paramArrayMap.get(str);
      FragmentTransitionCompat21.setEpicenter(paramObject1, view);
      if (paramObject2 != null)
        FragmentTransitionCompat21.setEpicenter(paramObject2, view); 
    } 
  }
  
  private static void setViewVisibility(ArrayList<View> paramArrayList, int paramInt) {
    if (paramArrayList != null) {
      int i = paramArrayList.size() - 1;
      while (true) {
        if (i >= 0) {
          ((View)paramArrayList.get(i)).setVisibility(paramInt);
          i--;
          continue;
        } 
        return;
      } 
    } 
  }
  
  static void startTransitions(FragmentManagerImpl paramFragmentManagerImpl, ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (paramFragmentManagerImpl.mCurState >= 1 && Build.VERSION.SDK_INT >= 21) {
      SparseArray<FragmentContainerTransition> sparseArray = new SparseArray();
      int i;
      for (i = paramInt1; i < paramInt2; i++) {
        BackStackRecord backStackRecord = paramArrayList.get(i);
        if (((Boolean)paramArrayList1.get(i)).booleanValue()) {
          calculatePopFragments(backStackRecord, sparseArray, paramBoolean);
        } else {
          calculateFragments(backStackRecord, sparseArray, paramBoolean);
        } 
      } 
      if (sparseArray.size() != 0) {
        View view = new View(paramFragmentManagerImpl.mHost.getContext());
        int j = sparseArray.size();
        i = 0;
        while (true) {
          if (i < j) {
            int k = sparseArray.keyAt(i);
            ArrayMap<String, String> arrayMap = calculateNameOverrides(k, paramArrayList, paramArrayList1, paramInt1, paramInt2);
            FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition)sparseArray.valueAt(i);
            if (paramBoolean) {
              configureTransitionsOptimized(paramFragmentManagerImpl, k, fragmentContainerTransition, view, arrayMap);
            } else {
              configureTransitionsUnoptimized(paramFragmentManagerImpl, k, fragmentContainerTransition, view, arrayMap);
            } 
            i++;
            continue;
          } 
          return;
        } 
      } 
    } 
  }
  
  static class FragmentContainerTransition {
    public Fragment firstOut;
    
    public boolean firstOutIsPop;
    
    public BackStackRecord firstOutTransaction;
    
    public Fragment lastIn;
    
    public boolean lastInIsPop;
    
    public BackStackRecord lastInTransaction;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/app/FragmentTransition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */