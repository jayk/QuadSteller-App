package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@TargetApi(14)
@RequiresApi(14)
class TransitionManagerPort {
  private static final String[] EMPTY_STRINGS = new String[0];
  
  private static String LOG_TAG = "TransitionManager";
  
  private static TransitionPort sDefaultTransition = new AutoTransitionPort();
  
  static ArrayList<ViewGroup> sPendingTransitions;
  
  private static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<TransitionPort>>>> sRunningTransitions = new ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<TransitionPort>>>>();
  
  ArrayMap<String, ArrayMap<ScenePort, TransitionPort>> mNameSceneTransitions = new ArrayMap();
  
  ArrayMap<ScenePort, ArrayMap<String, TransitionPort>> mSceneNameTransitions = new ArrayMap();
  
  ArrayMap<ScenePort, ArrayMap<ScenePort, TransitionPort>> mScenePairTransitions = new ArrayMap();
  
  ArrayMap<ScenePort, TransitionPort> mSceneTransitions = new ArrayMap();
  
  static {
    sPendingTransitions = new ArrayList<ViewGroup>();
  }
  
  public static void beginDelayedTransition(ViewGroup paramViewGroup) {
    beginDelayedTransition(paramViewGroup, null);
  }
  
  public static void beginDelayedTransition(ViewGroup paramViewGroup, TransitionPort paramTransitionPort) {
    if (!sPendingTransitions.contains(paramViewGroup) && ViewCompat.isLaidOut((View)paramViewGroup)) {
      sPendingTransitions.add(paramViewGroup);
      TransitionPort transitionPort = paramTransitionPort;
      if (paramTransitionPort == null)
        transitionPort = sDefaultTransition; 
      paramTransitionPort = transitionPort.clone();
      sceneChangeSetup(paramViewGroup, paramTransitionPort);
      ScenePort.setCurrentScene((View)paramViewGroup, null);
      sceneChangeRunTransition(paramViewGroup, paramTransitionPort);
    } 
  }
  
  private static void changeScene(ScenePort paramScenePort, TransitionPort paramTransitionPort) {
    ViewGroup viewGroup = paramScenePort.getSceneRoot();
    TransitionPort transitionPort = null;
    if (paramTransitionPort != null) {
      transitionPort = paramTransitionPort.clone();
      transitionPort.setSceneRoot(viewGroup);
    } 
    ScenePort scenePort = ScenePort.getCurrentScene((View)viewGroup);
    if (scenePort != null && scenePort.isCreatedFromLayoutResource())
      transitionPort.setCanRemoveViews(true); 
    sceneChangeSetup(viewGroup, transitionPort);
    paramScenePort.enter();
    sceneChangeRunTransition(viewGroup, transitionPort);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static TransitionPort getDefaultTransition() {
    return sDefaultTransition;
  }
  
  static ArrayMap<ViewGroup, ArrayList<TransitionPort>> getRunningTransitions() {
    WeakReference<ArrayMap> weakReference1 = (WeakReference)sRunningTransitions.get();
    if (weakReference1 != null) {
      WeakReference<ArrayMap> weakReference = weakReference1;
      if (weakReference1.get() == null) {
        weakReference = new WeakReference<ArrayMap>(new ArrayMap());
        sRunningTransitions.set(weakReference);
        return weakReference.get();
      } 
      return weakReference.get();
    } 
    WeakReference<ArrayMap> weakReference2 = new WeakReference<ArrayMap>(new ArrayMap());
    sRunningTransitions.set(weakReference2);
    return weakReference2.get();
  }
  
  private TransitionPort getTransition(ScenePort paramScenePort) {
    ViewGroup viewGroup = paramScenePort.getSceneRoot();
    if (viewGroup != null) {
      ScenePort scenePort = ScenePort.getCurrentScene((View)viewGroup);
      if (scenePort != null) {
        ArrayMap arrayMap = (ArrayMap)this.mScenePairTransitions.get(paramScenePort);
        if (arrayMap != null) {
          TransitionPort transitionPort1 = (TransitionPort)arrayMap.get(scenePort);
          if (transitionPort1 != null)
            return transitionPort1; 
        } 
      } 
    } 
    TransitionPort transitionPort = (TransitionPort)this.mSceneTransitions.get(paramScenePort);
    if (transitionPort == null)
      transitionPort = sDefaultTransition; 
    return transitionPort;
  }
  
  public static void go(ScenePort paramScenePort) {
    changeScene(paramScenePort, sDefaultTransition);
  }
  
  public static void go(ScenePort paramScenePort, TransitionPort paramTransitionPort) {
    changeScene(paramScenePort, paramTransitionPort);
  }
  
  private static void sceneChangeRunTransition(ViewGroup paramViewGroup, TransitionPort paramTransitionPort) {
    if (paramTransitionPort != null && paramViewGroup != null) {
      MultiListener multiListener = new MultiListener(paramTransitionPort, paramViewGroup);
      paramViewGroup.addOnAttachStateChangeListener(multiListener);
      paramViewGroup.getViewTreeObserver().addOnPreDrawListener(multiListener);
    } 
  }
  
  private static void sceneChangeSetup(ViewGroup paramViewGroup, TransitionPort paramTransitionPort) {
    ArrayList arrayList = (ArrayList)getRunningTransitions().get(paramViewGroup);
    if (arrayList != null && arrayList.size() > 0) {
      Iterator<TransitionPort> iterator = arrayList.iterator();
      while (iterator.hasNext())
        ((TransitionPort)iterator.next()).pause((View)paramViewGroup); 
    } 
    if (paramTransitionPort != null)
      paramTransitionPort.captureValues(paramViewGroup, true); 
    ScenePort scenePort = ScenePort.getCurrentScene((View)paramViewGroup);
    if (scenePort != null)
      scenePort.exit(); 
  }
  
  public TransitionPort getNamedTransition(ScenePort paramScenePort, String paramString) {
    null = (ArrayMap)this.mSceneNameTransitions.get(paramScenePort);
    return (null != null) ? (TransitionPort)null.get(paramString) : null;
  }
  
  public TransitionPort getNamedTransition(String paramString, ScenePort paramScenePort) {
    null = (ArrayMap)this.mNameSceneTransitions.get(paramString);
    return (null != null) ? (TransitionPort)null.get(paramScenePort) : null;
  }
  
  public String[] getTargetSceneNames(ScenePort paramScenePort) {
    ArrayMap arrayMap = (ArrayMap)this.mSceneNameTransitions.get(paramScenePort);
    if (arrayMap == null)
      return EMPTY_STRINGS; 
    int i = arrayMap.size();
    String[] arrayOfString = new String[i];
    byte b = 0;
    while (true) {
      String[] arrayOfString1 = arrayOfString;
      if (b < i) {
        arrayOfString[b] = (String)arrayMap.keyAt(b);
        b++;
        continue;
      } 
      return arrayOfString1;
    } 
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void setDefaultTransition(TransitionPort paramTransitionPort) {
    sDefaultTransition = paramTransitionPort;
  }
  
  public void setTransition(ScenePort paramScenePort1, ScenePort paramScenePort2, TransitionPort paramTransitionPort) {
    ArrayMap arrayMap1 = (ArrayMap)this.mScenePairTransitions.get(paramScenePort2);
    ArrayMap arrayMap2 = arrayMap1;
    if (arrayMap1 == null) {
      arrayMap2 = new ArrayMap();
      this.mScenePairTransitions.put(paramScenePort2, arrayMap2);
    } 
    arrayMap2.put(paramScenePort1, paramTransitionPort);
  }
  
  public void setTransition(ScenePort paramScenePort, TransitionPort paramTransitionPort) {
    this.mSceneTransitions.put(paramScenePort, paramTransitionPort);
  }
  
  public void setTransition(ScenePort paramScenePort, String paramString, TransitionPort paramTransitionPort) {
    ArrayMap arrayMap1 = (ArrayMap)this.mSceneNameTransitions.get(paramScenePort);
    ArrayMap arrayMap2 = arrayMap1;
    if (arrayMap1 == null) {
      arrayMap2 = new ArrayMap();
      this.mSceneNameTransitions.put(paramScenePort, arrayMap2);
    } 
    arrayMap2.put(paramString, paramTransitionPort);
  }
  
  public void setTransition(String paramString, ScenePort paramScenePort, TransitionPort paramTransitionPort) {
    ArrayMap arrayMap1 = (ArrayMap)this.mNameSceneTransitions.get(paramString);
    ArrayMap arrayMap2 = arrayMap1;
    if (arrayMap1 == null) {
      arrayMap2 = new ArrayMap();
      this.mNameSceneTransitions.put(paramString, arrayMap2);
    } 
    arrayMap2.put(paramScenePort, paramTransitionPort);
  }
  
  public void transitionTo(ScenePort paramScenePort) {
    changeScene(paramScenePort, getTransition(paramScenePort));
  }
  
  private static class MultiListener implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {
    ViewGroup mSceneRoot;
    
    TransitionPort mTransition;
    
    MultiListener(TransitionPort param1TransitionPort, ViewGroup param1ViewGroup) {
      this.mTransition = param1TransitionPort;
      this.mSceneRoot = param1ViewGroup;
    }
    
    private void removeListeners() {
      this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
      this.mSceneRoot.removeOnAttachStateChangeListener(this);
    }
    
    public boolean onPreDraw() {
      ArrayList<?> arrayList2;
      removeListeners();
      TransitionManagerPort.sPendingTransitions.remove(this.mSceneRoot);
      final ArrayMap<ViewGroup, ArrayList<TransitionPort>> runningTransitions = TransitionManagerPort.getRunningTransitions();
      ArrayList<?> arrayList1 = (ArrayList)arrayMap.get(this.mSceneRoot);
      ArrayList arrayList = null;
      if (arrayList1 == null) {
        arrayList2 = new ArrayList();
        arrayMap.put(this.mSceneRoot, arrayList2);
      } else {
        arrayList2 = arrayList1;
        if (arrayList1.size() > 0) {
          arrayList = new ArrayList(arrayList1);
          arrayList2 = arrayList1;
        } 
      } 
      arrayList2.add(this.mTransition);
      this.mTransition.addListener(new TransitionPort.TransitionListenerAdapter() {
            public void onTransitionEnd(TransitionPort param2TransitionPort) {
              ((ArrayList)runningTransitions.get(TransitionManagerPort.MultiListener.this.mSceneRoot)).remove(param2TransitionPort);
            }
          });
      this.mTransition.captureValues(this.mSceneRoot, false);
      if (arrayList != null) {
        Iterator<TransitionPort> iterator = arrayList.iterator();
        while (iterator.hasNext())
          ((TransitionPort)iterator.next()).resume((View)this.mSceneRoot); 
      } 
      this.mTransition.playTransition(this.mSceneRoot);
      return true;
    }
    
    public void onViewAttachedToWindow(View param1View) {}
    
    public void onViewDetachedFromWindow(View param1View) {
      removeListeners();
      TransitionManagerPort.sPendingTransitions.remove(this.mSceneRoot);
      ArrayList arrayList = (ArrayList)TransitionManagerPort.getRunningTransitions().get(this.mSceneRoot);
      if (arrayList != null && arrayList.size() > 0) {
        Iterator<TransitionPort> iterator = arrayList.iterator();
        while (iterator.hasNext())
          ((TransitionPort)iterator.next()).resume((View)this.mSceneRoot); 
      } 
      this.mTransition.clearValues(true);
    }
  }
  
  class null extends TransitionPort.TransitionListenerAdapter {
    public void onTransitionEnd(TransitionPort param1TransitionPort) {
      ((ArrayList)runningTransitions.get(this.this$0.mSceneRoot)).remove(param1TransitionPort);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionManagerPort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */