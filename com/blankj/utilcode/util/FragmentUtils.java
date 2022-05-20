package com.blankj.utilcode.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class FragmentUtils {
  private static final String ARGS_ID = "args_id";
  
  private static final String ARGS_IS_ADD_STACK = "args_is_add_stack";
  
  private static final String ARGS_IS_HIDE = "args_is_hide";
  
  private static final int TYPE_ADD_FRAGMENT = 1;
  
  private static final int TYPE_HIDE_FRAGMENT = 4;
  
  private static final int TYPE_REMOVE_FRAGMENT = 32;
  
  private static final int TYPE_REMOVE_TO_FRAGMENT = 64;
  
  private static final int TYPE_REPLACE_FRAGMENT = 16;
  
  private static final int TYPE_SHOW_FRAGMENT = 2;
  
  private static final int TYPE_SHOW_HIDE_FRAGMENT = 8;
  
  private FragmentUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static void add(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt) {
    add(paramFragmentManager, paramFragment, paramInt, false, false);
  }
  
  public static void add(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt1, @AnimRes int paramInt2, @AnimRes int paramInt3) {
    add(paramFragmentManager, paramFragment, paramInt1, false, paramInt2, paramInt3, 0, 0);
  }
  
  public static void add(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt1, @AnimRes int paramInt2, @AnimRes int paramInt3, @AnimRes int paramInt4, @AnimRes int paramInt5) {
    add(paramFragmentManager, paramFragment, paramInt1, false, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  public static void add(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt, boolean paramBoolean) {
    add(paramFragmentManager, paramFragment, paramInt, paramBoolean, false);
  }
  
  public static void add(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt1, boolean paramBoolean, @AnimRes int paramInt2, @AnimRes int paramInt3) {
    add(paramFragmentManager, paramFragment, paramInt1, paramBoolean, paramInt2, paramInt3, 0, 0);
  }
  
  public static void add(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt1, boolean paramBoolean, @AnimRes int paramInt2, @AnimRes int paramInt3, @AnimRes int paramInt4, @AnimRes int paramInt5) {
    FragmentTransaction fragmentTransaction = paramFragmentManager.beginTransaction();
    putArgs(paramFragment, new Args(paramInt1, false, paramBoolean));
    addAnim(fragmentTransaction, paramInt2, paramInt3, paramInt4, paramInt5);
    operate(1, paramFragmentManager, fragmentTransaction, null, new Fragment[] { paramFragment });
  }
  
  public static void add(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    putArgs(paramFragment, new Args(paramInt, paramBoolean1, paramBoolean2));
    operateNoAnim(paramFragmentManager, 1, null, new Fragment[] { paramFragment });
  }
  
  public static void add(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt, boolean paramBoolean, @NonNull View... paramVarArgs) {
    FragmentTransaction fragmentTransaction = paramFragmentManager.beginTransaction();
    putArgs(paramFragment, new Args(paramInt, false, paramBoolean));
    addSharedElement(fragmentTransaction, paramVarArgs);
    operate(1, paramFragmentManager, fragmentTransaction, null, new Fragment[] { paramFragment });
  }
  
  public static void add(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt, @NonNull View... paramVarArgs) {
    add(paramFragmentManager, paramFragment, paramInt, false, paramVarArgs);
  }
  
  public static void add(@NonNull FragmentManager paramFragmentManager, @NonNull List<Fragment> paramList, @IdRes int paramInt1, int paramInt2) {
    add(paramFragmentManager, paramList.<Fragment>toArray(new Fragment[paramList.size()]), paramInt1, paramInt2);
  }
  
  public static void add(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment[] paramArrayOfFragment, @IdRes int paramInt1, int paramInt2) {
    byte b = 0;
    int i = paramArrayOfFragment.length;
    while (b < i) {
      boolean bool;
      Fragment fragment = paramArrayOfFragment[b];
      if (paramInt2 != b) {
        bool = true;
      } else {
        bool = false;
      } 
      putArgs(fragment, new Args(paramInt1, bool, false));
      b++;
    } 
    operateNoAnim(paramFragmentManager, 1, null, paramArrayOfFragment);
  }
  
  private static void addAnim(FragmentTransaction paramFragmentTransaction, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramFragmentTransaction.setCustomAnimations(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  private static void addSharedElement(FragmentTransaction paramFragmentTransaction, View... paramVarArgs) {
    if (Build.VERSION.SDK_INT >= 21) {
      int i = paramVarArgs.length;
      for (byte b = 0; b < i; b++) {
        View view = paramVarArgs[b];
        paramFragmentTransaction.addSharedElement(view, view.getTransitionName());
      } 
    } 
  }
  
  public static boolean dispatchBackPress(@NonNull Fragment paramFragment) {
    return (paramFragment.isResumed() && paramFragment.isVisible() && paramFragment.getUserVisibleHint() && paramFragment instanceof OnBackClickListener && ((OnBackClickListener)paramFragment).onBackClick());
  }
  
  public static boolean dispatchBackPress(@NonNull FragmentManager paramFragmentManager) {
    boolean bool1 = false;
    List<Fragment> list = getFragments(paramFragmentManager);
    boolean bool2 = bool1;
    if (list != null) {
      if (list.isEmpty())
        return bool1; 
    } else {
      return bool2;
    } 
    int i = list.size() - 1;
    while (true) {
      bool2 = bool1;
      if (i >= 0) {
        Fragment fragment = list.get(i);
        if (fragment != null && fragment.isResumed() && fragment.isVisible() && fragment.getUserVisibleHint() && fragment instanceof OnBackClickListener && ((OnBackClickListener)fragment).onBackClick())
          return true; 
        i--;
        continue;
      } 
      return bool2;
    } 
  }
  
  public static Fragment findFragment(@NonNull FragmentManager paramFragmentManager, Class<? extends Fragment> paramClass) {
    return paramFragmentManager.findFragmentByTag(paramClass.getName());
  }
  
  public static List<FragmentNode> getAllFragments(@NonNull FragmentManager paramFragmentManager) {
    return getAllFragments(paramFragmentManager, new ArrayList<FragmentNode>());
  }
  
  private static List<FragmentNode> getAllFragments(@NonNull FragmentManager paramFragmentManager, List<FragmentNode> paramList) {
    List<Fragment> list = getFragments(paramFragmentManager);
    for (int i = list.size() - 1; i >= 0; i--) {
      Fragment fragment = list.get(i);
      if (fragment != null)
        paramList.add(new FragmentNode(fragment, getAllFragments(fragment.getChildFragmentManager(), new ArrayList<FragmentNode>()))); 
    } 
    return paramList;
  }
  
  public static List<FragmentNode> getAllFragmentsInStack(@NonNull FragmentManager paramFragmentManager) {
    return getAllFragmentsInStack(paramFragmentManager, new ArrayList<FragmentNode>());
  }
  
  private static List<FragmentNode> getAllFragmentsInStack(@NonNull FragmentManager paramFragmentManager, List<FragmentNode> paramList) {
    List<Fragment> list = getFragments(paramFragmentManager);
    for (int i = list.size() - 1; i >= 0; i--) {
      Fragment fragment = list.get(i);
      if (fragment != null && fragment.getArguments().getBoolean("args_is_add_stack"))
        paramList.add(new FragmentNode(fragment, getAllFragmentsInStack(fragment.getChildFragmentManager(), new ArrayList<FragmentNode>()))); 
    } 
    return paramList;
  }
  
  private static Args getArgs(Fragment paramFragment) {
    Bundle bundle = paramFragment.getArguments();
    return new Args(bundle.getInt("args_id", paramFragment.getId()), bundle.getBoolean("args_is_hide"), bundle.getBoolean("args_is_add_stack"));
  }
  
  public static List<Fragment> getFragments(@NonNull FragmentManager paramFragmentManager) {
    List<?> list = paramFragmentManager.getFragments();
    if (list != null) {
      List<?> list1 = list;
      return (List)(list.isEmpty() ? Collections.emptyList() : list1);
    } 
    return (List)Collections.emptyList();
  }
  
  public static List<Fragment> getFragmentsInStack(@NonNull FragmentManager paramFragmentManager) {
    List<Fragment> list = getFragments(paramFragmentManager);
    ArrayList<Fragment> arrayList = new ArrayList();
    for (Fragment fragment : list) {
      if (fragment != null && fragment.getArguments().getBoolean("args_is_add_stack"))
        arrayList.add(fragment); 
    } 
    return arrayList;
  }
  
  public static String getSimpleName(Fragment paramFragment) {
    return (paramFragment == null) ? "null" : paramFragment.getClass().getSimpleName();
  }
  
  public static Fragment getTop(@NonNull FragmentManager paramFragmentManager) {
    return getTopIsInStack(paramFragmentManager, false);
  }
  
  public static Fragment getTopInStack(@NonNull FragmentManager paramFragmentManager) {
    return getTopIsInStack(paramFragmentManager, true);
  }
  
  private static Fragment getTopIsInStack(@NonNull FragmentManager paramFragmentManager, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic getFragments : (Landroid/support/v4/app/FragmentManager;)Ljava/util/List;
    //   4: astore_2
    //   5: aload_2
    //   6: invokeinterface size : ()I
    //   11: iconst_1
    //   12: isub
    //   13: istore_3
    //   14: iload_3
    //   15: iflt -> 66
    //   18: aload_2
    //   19: iload_3
    //   20: invokeinterface get : (I)Ljava/lang/Object;
    //   25: checkcast android/support/v4/app/Fragment
    //   28: astore #4
    //   30: aload #4
    //   32: ifnull -> 60
    //   35: aload #4
    //   37: astore_0
    //   38: iload_1
    //   39: ifeq -> 58
    //   42: aload #4
    //   44: invokevirtual getArguments : ()Landroid/os/Bundle;
    //   47: ldc 'args_is_add_stack'
    //   49: invokevirtual getBoolean : (Ljava/lang/String;)Z
    //   52: ifeq -> 60
    //   55: aload #4
    //   57: astore_0
    //   58: aload_0
    //   59: areturn
    //   60: iinc #3, -1
    //   63: goto -> 14
    //   66: aconst_null
    //   67: astore_0
    //   68: goto -> 58
  }
  
  public static Fragment getTopShow(@NonNull FragmentManager paramFragmentManager) {
    return getTopShowIsInStack(paramFragmentManager, false);
  }
  
  public static Fragment getTopShowInStack(@NonNull FragmentManager paramFragmentManager) {
    return getTopShowIsInStack(paramFragmentManager, true);
  }
  
  private static Fragment getTopShowIsInStack(@NonNull FragmentManager paramFragmentManager, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic getFragments : (Landroid/support/v4/app/FragmentManager;)Ljava/util/List;
    //   4: astore_2
    //   5: aload_2
    //   6: invokeinterface size : ()I
    //   11: iconst_1
    //   12: isub
    //   13: istore_3
    //   14: iload_3
    //   15: iflt -> 90
    //   18: aload_2
    //   19: iload_3
    //   20: invokeinterface get : (I)Ljava/lang/Object;
    //   25: checkcast android/support/v4/app/Fragment
    //   28: astore #4
    //   30: aload #4
    //   32: ifnull -> 84
    //   35: aload #4
    //   37: invokevirtual isResumed : ()Z
    //   40: ifeq -> 84
    //   43: aload #4
    //   45: invokevirtual isVisible : ()Z
    //   48: ifeq -> 84
    //   51: aload #4
    //   53: invokevirtual getUserVisibleHint : ()Z
    //   56: ifeq -> 84
    //   59: aload #4
    //   61: astore_0
    //   62: iload_1
    //   63: ifeq -> 82
    //   66: aload #4
    //   68: invokevirtual getArguments : ()Landroid/os/Bundle;
    //   71: ldc 'args_is_add_stack'
    //   73: invokevirtual getBoolean : (Ljava/lang/String;)Z
    //   76: ifeq -> 84
    //   79: aload #4
    //   81: astore_0
    //   82: aload_0
    //   83: areturn
    //   84: iinc #3, -1
    //   87: goto -> 14
    //   90: aconst_null
    //   91: astore_0
    //   92: goto -> 82
  }
  
  public static void hide(@NonNull Fragment paramFragment) {
    putArgs(paramFragment, true);
    operateNoAnim(paramFragment.getFragmentManager(), 4, null, new Fragment[] { paramFragment });
  }
  
  public static void hide(@NonNull FragmentManager paramFragmentManager) {
    List<Fragment> list = getFragments(paramFragmentManager);
    Iterator<Fragment> iterator = list.iterator();
    while (iterator.hasNext())
      putArgs(iterator.next(), true); 
    operateNoAnim(paramFragmentManager, 4, null, list.<Fragment>toArray(new Fragment[list.size()]));
  }
  
  private static void operate(int paramInt, FragmentManager paramFragmentManager, FragmentTransaction paramFragmentTransaction, Fragment paramFragment, Fragment... paramVarArgs) {
    Bundle bundle1;
    Bundle bundle2;
    String str;
    int i = 0;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    int j = 0;
    if (paramFragment != null && paramFragment.isRemoving()) {
      Log.e("FragmentUtils", paramFragment.getClass().getName() + " is isRemoving");
      return;
    } 
    switch (paramInt) {
      default:
        paramFragmentTransaction.commitAllowingStateLoss();
        return;
      case 1:
        i = paramVarArgs.length;
        paramInt = j;
        while (true) {
          if (paramInt < i) {
            Fragment fragment1 = paramVarArgs[paramInt];
            String str1 = fragment1.getClass().getName();
            bundle2 = fragment1.getArguments();
            Fragment fragment2 = paramFragmentManager.findFragmentByTag(str1);
            if (fragment2 != null && fragment2.isAdded())
              paramFragmentTransaction.remove(fragment2); 
            paramFragmentTransaction.add(bundle2.getInt("args_id"), fragment1, str1);
            if (bundle2.getBoolean("args_is_hide"))
              paramFragmentTransaction.hide(fragment1); 
            if (bundle2.getBoolean("args_is_add_stack"))
              paramFragmentTransaction.addToBackStack(str1); 
            paramInt++;
          } 
        } 
      case 4:
        j = paramVarArgs.length;
        paramInt = i;
        while (true) {
          if (paramInt < j) {
            paramFragmentTransaction.hide(paramVarArgs[paramInt]);
            paramInt++;
          } 
        } 
      case 2:
        j = paramVarArgs.length;
        paramInt = bool1;
        while (true) {
          if (paramInt < j) {
            paramFragmentTransaction.show(paramVarArgs[paramInt]);
            paramInt++;
          } 
        } 
      case 8:
        paramFragmentTransaction.show((Fragment)bundle2);
        j = paramVarArgs.length;
        paramInt = bool2;
        while (true) {
          if (paramInt < j) {
            Fragment fragment = paramVarArgs[paramInt];
            if (fragment != bundle2)
              paramFragmentTransaction.hide(fragment); 
            paramInt++;
          } 
        } 
      case 16:
        str = paramVarArgs[0].getClass().getName();
        bundle1 = paramVarArgs[0].getArguments();
        paramFragmentTransaction.replace(bundle1.getInt("args_id"), paramVarArgs[0], str);
        if (bundle1.getBoolean("args_is_add_stack"))
          paramFragmentTransaction.addToBackStack(str); 
      case 32:
        j = paramVarArgs.length;
        paramInt = bool3;
        while (true) {
          if (paramInt < j) {
            Fragment fragment = paramVarArgs[paramInt];
            if (fragment != str)
              paramFragmentTransaction.remove(fragment); 
            paramInt++;
          } 
        } 
      case 64:
        break;
    } 
    paramInt = paramVarArgs.length - 1;
    while (true) {
      if (paramInt >= 0) {
        Fragment fragment = paramVarArgs[paramInt];
        if (fragment == paramVarArgs[0])
          if (str != null)
            paramFragmentTransaction.remove(fragment);  
        paramFragmentTransaction.remove(fragment);
        paramInt--;
      } 
    } 
  }
  
  private static void operateNoAnim(FragmentManager paramFragmentManager, int paramInt, Fragment paramFragment, Fragment... paramVarArgs) {
    operate(paramInt, paramFragmentManager, paramFragmentManager.beginTransaction(), paramFragment, paramVarArgs);
  }
  
  public static void pop(@NonNull FragmentManager paramFragmentManager) {
    pop(paramFragmentManager, true);
  }
  
  public static void pop(@NonNull FragmentManager paramFragmentManager, boolean paramBoolean) {
    if (paramBoolean) {
      paramFragmentManager.popBackStackImmediate();
      return;
    } 
    paramFragmentManager.popBackStack();
  }
  
  public static void popAll(@NonNull FragmentManager paramFragmentManager) {
    popAll(paramFragmentManager, true);
  }
  
  public static void popAll(@NonNull FragmentManager paramFragmentManager, boolean paramBoolean) {
    while (paramFragmentManager.getBackStackEntryCount() > 0) {
      if (paramBoolean) {
        paramFragmentManager.popBackStackImmediate();
        continue;
      } 
      paramFragmentManager.popBackStack();
    } 
  }
  
  public static void popTo(@NonNull FragmentManager paramFragmentManager, Class<? extends Fragment> paramClass, boolean paramBoolean) {
    popTo(paramFragmentManager, paramClass, paramBoolean, true);
  }
  
  public static void popTo(@NonNull FragmentManager paramFragmentManager, Class<? extends Fragment> paramClass, boolean paramBoolean1, boolean paramBoolean2) {
    boolean bool1 = true;
    boolean bool2 = true;
    if (paramBoolean2) {
      str = paramClass.getName();
      if (!paramBoolean1)
        bool2 = false; 
      paramFragmentManager.popBackStackImmediate(str, bool2);
      return;
    } 
    String str = str.getName();
    if (paramBoolean1) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    paramFragmentManager.popBackStack(str, bool2);
  }
  
  private static void putArgs(Fragment paramFragment, Args paramArgs) {
    Bundle bundle1 = paramFragment.getArguments();
    Bundle bundle2 = bundle1;
    if (bundle1 == null) {
      bundle2 = new Bundle();
      paramFragment.setArguments(bundle2);
    } 
    bundle2.putInt("args_id", paramArgs.id);
    bundle2.putBoolean("args_is_hide", paramArgs.isHide);
    bundle2.putBoolean("args_is_add_stack", paramArgs.isAddStack);
  }
  
  private static void putArgs(Fragment paramFragment, boolean paramBoolean) {
    Bundle bundle1 = paramFragment.getArguments();
    Bundle bundle2 = bundle1;
    if (bundle1 == null) {
      bundle2 = new Bundle();
      paramFragment.setArguments(bundle2);
    } 
    bundle2.putBoolean("args_is_hide", paramBoolean);
  }
  
  public static void remove(@NonNull Fragment paramFragment) {
    operateNoAnim(paramFragment.getFragmentManager(), 32, null, new Fragment[] { paramFragment });
  }
  
  public static void removeAll(@NonNull FragmentManager paramFragmentManager) {
    List<Fragment> list = getFragments(paramFragmentManager);
    operateNoAnim(paramFragmentManager, 32, null, list.<Fragment>toArray(new Fragment[list.size()]));
  }
  
  public static void removeTo(@NonNull Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment;
    FragmentManager fragmentManager = paramFragment.getFragmentManager();
    if (paramBoolean) {
      fragment = paramFragment;
    } else {
      fragment = null;
    } 
    operateNoAnim(fragmentManager, 64, fragment, new Fragment[] { paramFragment });
  }
  
  public static void replace(@NonNull Fragment paramFragment1, @NonNull Fragment paramFragment2) {
    replace(paramFragment1, paramFragment2, false);
  }
  
  public static void replace(@NonNull Fragment paramFragment1, @NonNull Fragment paramFragment2, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    replace(paramFragment1, paramFragment2, false, paramInt1, paramInt2, 0, 0);
  }
  
  public static void replace(@NonNull Fragment paramFragment1, @NonNull Fragment paramFragment2, @AnimRes int paramInt1, @AnimRes int paramInt2, @AnimRes int paramInt3, @AnimRes int paramInt4) {
    replace(paramFragment1, paramFragment2, false, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static void replace(@NonNull Fragment paramFragment1, @NonNull Fragment paramFragment2, boolean paramBoolean) {
    Args args = getArgs(paramFragment1);
    replace(paramFragment1.getFragmentManager(), paramFragment2, args.id, paramBoolean);
  }
  
  public static void replace(@NonNull Fragment paramFragment1, @NonNull Fragment paramFragment2, boolean paramBoolean, @AnimRes int paramInt1, @AnimRes int paramInt2) {
    replace(paramFragment1, paramFragment2, paramBoolean, paramInt1, paramInt2, 0, 0);
  }
  
  public static void replace(@NonNull Fragment paramFragment1, @NonNull Fragment paramFragment2, boolean paramBoolean, @AnimRes int paramInt1, @AnimRes int paramInt2, @AnimRes int paramInt3, @AnimRes int paramInt4) {
    Args args = getArgs(paramFragment1);
    replace(paramFragment1.getFragmentManager(), paramFragment2, args.id, paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static void replace(@NonNull Fragment paramFragment1, @NonNull Fragment paramFragment2, boolean paramBoolean, View... paramVarArgs) {
    Args args = getArgs(paramFragment1);
    replace(paramFragment1.getFragmentManager(), paramFragment2, args.id, paramBoolean, paramVarArgs);
  }
  
  public static void replace(@NonNull Fragment paramFragment1, @NonNull Fragment paramFragment2, View... paramVarArgs) {
    replace(paramFragment1, paramFragment2, false, paramVarArgs);
  }
  
  public static void replace(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt) {
    replace(paramFragmentManager, paramFragment, paramInt, false);
  }
  
  public static void replace(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt1, @AnimRes int paramInt2, @AnimRes int paramInt3) {
    replace(paramFragmentManager, paramFragment, paramInt1, false, paramInt2, paramInt3, 0, 0);
  }
  
  public static void replace(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt1, @AnimRes int paramInt2, @AnimRes int paramInt3, @AnimRes int paramInt4, @AnimRes int paramInt5) {
    replace(paramFragmentManager, paramFragment, paramInt1, false, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  public static void replace(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt, boolean paramBoolean) {
    FragmentTransaction fragmentTransaction = paramFragmentManager.beginTransaction();
    putArgs(paramFragment, new Args(paramInt, false, paramBoolean));
    operate(16, paramFragmentManager, fragmentTransaction, null, new Fragment[] { paramFragment });
  }
  
  public static void replace(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt1, boolean paramBoolean, @AnimRes int paramInt2, @AnimRes int paramInt3) {
    replace(paramFragmentManager, paramFragment, paramInt1, paramBoolean, paramInt2, paramInt3, 0, 0);
  }
  
  public static void replace(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt1, boolean paramBoolean, @AnimRes int paramInt2, @AnimRes int paramInt3, @AnimRes int paramInt4, @AnimRes int paramInt5) {
    FragmentTransaction fragmentTransaction = paramFragmentManager.beginTransaction();
    putArgs(paramFragment, new Args(paramInt1, false, paramBoolean));
    addAnim(fragmentTransaction, paramInt2, paramInt3, paramInt4, paramInt5);
    operate(16, paramFragmentManager, fragmentTransaction, null, new Fragment[] { paramFragment });
  }
  
  public static void replace(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt, boolean paramBoolean, View... paramVarArgs) {
    FragmentTransaction fragmentTransaction = paramFragmentManager.beginTransaction();
    putArgs(paramFragment, new Args(paramInt, false, paramBoolean));
    addSharedElement(fragmentTransaction, paramVarArgs);
    operate(16, paramFragmentManager, fragmentTransaction, null, new Fragment[] { paramFragment });
  }
  
  public static void replace(@NonNull FragmentManager paramFragmentManager, @NonNull Fragment paramFragment, @IdRes int paramInt, View... paramVarArgs) {
    replace(paramFragmentManager, paramFragment, paramInt, false, paramVarArgs);
  }
  
  public static void setBackground(@NonNull Fragment paramFragment, Drawable paramDrawable) {
    ViewCompat.setBackground(paramFragment.getView(), paramDrawable);
  }
  
  public static void setBackgroundColor(@NonNull Fragment paramFragment, @ColorInt int paramInt) {
    View view = paramFragment.getView();
    if (view != null)
      view.setBackgroundColor(paramInt); 
  }
  
  public static void setBackgroundResource(@NonNull Fragment paramFragment, @DrawableRes int paramInt) {
    View view = paramFragment.getView();
    if (view != null)
      view.setBackgroundResource(paramInt); 
  }
  
  public static void show(@NonNull Fragment paramFragment) {
    putArgs(paramFragment, false);
    operateNoAnim(paramFragment.getFragmentManager(), 2, null, new Fragment[] { paramFragment });
  }
  
  public static void show(@NonNull FragmentManager paramFragmentManager) {
    List<Fragment> list = getFragments(paramFragmentManager);
    Iterator<Fragment> iterator = list.iterator();
    while (iterator.hasNext())
      putArgs(iterator.next(), false); 
    operateNoAnim(paramFragmentManager, 2, null, list.<Fragment>toArray(new Fragment[list.size()]));
  }
  
  public static void showHide(int paramInt, @NonNull List<Fragment> paramList) {
    showHide(paramList.get(paramInt), paramList);
  }
  
  public static void showHide(int paramInt, @NonNull Fragment... paramVarArgs) {
    showHide(paramVarArgs[paramInt], paramVarArgs);
  }
  
  public static void showHide(@NonNull Fragment paramFragment1, @NonNull Fragment paramFragment2) {
    putArgs(paramFragment1, false);
    putArgs(paramFragment2, true);
    operateNoAnim(paramFragment1.getFragmentManager(), 8, paramFragment1, new Fragment[] { paramFragment2 });
  }
  
  public static void showHide(@NonNull Fragment paramFragment, @NonNull List<Fragment> paramList) {
    for (Fragment fragment : paramList) {
      boolean bool;
      if (fragment != paramFragment) {
        bool = true;
      } else {
        bool = false;
      } 
      putArgs(fragment, bool);
    } 
    operateNoAnim(paramFragment.getFragmentManager(), 8, paramFragment, paramList.<Fragment>toArray(new Fragment[paramList.size()]));
  }
  
  public static void showHide(@NonNull Fragment paramFragment, @NonNull Fragment... paramVarArgs) {
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++) {
      boolean bool;
      Fragment fragment = paramVarArgs[b];
      if (fragment != paramFragment) {
        bool = true;
      } else {
        bool = false;
      } 
      putArgs(fragment, bool);
    } 
    operateNoAnim(paramFragment.getFragmentManager(), 8, paramFragment, paramVarArgs);
  }
  
  private static class Args {
    int id;
    
    boolean isAddStack;
    
    boolean isHide;
    
    private Args(int param1Int, boolean param1Boolean1, boolean param1Boolean2) {
      this.id = param1Int;
      this.isHide = param1Boolean1;
      this.isAddStack = param1Boolean2;
    }
  }
  
  public static class FragmentNode {
    Fragment fragment;
    
    List<FragmentNode> next;
    
    public FragmentNode(Fragment param1Fragment, List<FragmentNode> param1List) {
      this.fragment = param1Fragment;
      this.next = param1List;
    }
    
    public String toString() {
      StringBuilder stringBuilder = (new StringBuilder()).append(this.fragment.getClass().getSimpleName()).append("->");
      if (this.next == null || this.next.isEmpty()) {
        String str1 = "no child";
        return stringBuilder.append(str1).toString();
      } 
      String str = this.next.toString();
      return stringBuilder.append(str).toString();
    }
  }
  
  public static interface OnBackClickListener {
    boolean onBackClick();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/FragmentUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */