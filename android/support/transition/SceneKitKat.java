package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.transition.Scene;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TargetApi(19)
@RequiresApi(19)
class SceneKitKat extends SceneWrapper {
  private static Field sEnterAction;
  
  private static Method sSetCurrentScene;
  
  private View mLayout;
  
  private void invokeEnterAction() {
    // Byte code:
    //   0: getstatic android/support/transition/SceneKitKat.sEnterAction : Ljava/lang/reflect/Field;
    //   3: ifnonnull -> 23
    //   6: ldc android/transition/Scene
    //   8: ldc 'mEnterAction'
    //   10: invokevirtual getDeclaredField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   13: putstatic android/support/transition/SceneKitKat.sEnterAction : Ljava/lang/reflect/Field;
    //   16: getstatic android/support/transition/SceneKitKat.sEnterAction : Ljava/lang/reflect/Field;
    //   19: iconst_1
    //   20: invokevirtual setAccessible : (Z)V
    //   23: getstatic android/support/transition/SceneKitKat.sEnterAction : Ljava/lang/reflect/Field;
    //   26: aload_0
    //   27: getfield mScene : Landroid/transition/Scene;
    //   30: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   33: checkcast java/lang/Runnable
    //   36: astore_1
    //   37: aload_1
    //   38: ifnull -> 47
    //   41: aload_1
    //   42: invokeinterface run : ()V
    //   47: return
    //   48: astore_1
    //   49: new java/lang/RuntimeException
    //   52: dup
    //   53: aload_1
    //   54: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   57: athrow
    //   58: astore_1
    //   59: new java/lang/RuntimeException
    //   62: dup
    //   63: aload_1
    //   64: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   67: athrow
    // Exception table:
    //   from	to	target	type
    //   6	23	48	java/lang/NoSuchFieldException
    //   23	37	58	java/lang/IllegalAccessException
    //   41	47	58	java/lang/IllegalAccessException
  }
  
  private void updateCurrentScene(View paramView) {
    if (sSetCurrentScene == null)
      try {
        sSetCurrentScene = Scene.class.getDeclaredMethod("setCurrentScene", new Class[] { View.class, Scene.class });
        sSetCurrentScene.setAccessible(true);
        try {
          sSetCurrentScene.invoke((Object)null, new Object[] { paramView, this.mScene });
          return;
        } catch (IllegalAccessException illegalAccessException) {
        
        } catch (InvocationTargetException invocationTargetException) {}
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      }  
    try {
      sSetCurrentScene.invoke((Object)null, new Object[] { noSuchMethodException, this.mScene });
      return;
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
  }
  
  public void enter() {
    if (this.mLayout != null) {
      ViewGroup viewGroup = getSceneRoot();
      viewGroup.removeAllViews();
      viewGroup.addView(this.mLayout);
      invokeEnterAction();
      updateCurrentScene((View)viewGroup);
      return;
    } 
    this.mScene.enter();
  }
  
  public void init(ViewGroup paramViewGroup) {
    this.mScene = new Scene(paramViewGroup);
  }
  
  public void init(ViewGroup paramViewGroup, View paramView) {
    if (paramView instanceof ViewGroup) {
      this.mScene = new Scene(paramViewGroup, (ViewGroup)paramView);
      return;
    } 
    this.mScene = new Scene(paramViewGroup);
    this.mLayout = paramView;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/SceneKitKat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */