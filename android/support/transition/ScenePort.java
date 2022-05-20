package android.support.transition;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
final class ScenePort {
  private Context mContext;
  
  Runnable mEnterAction;
  
  Runnable mExitAction;
  
  private View mLayout;
  
  private int mLayoutId = -1;
  
  private ViewGroup mSceneRoot;
  
  public ScenePort(ViewGroup paramViewGroup) {
    this.mSceneRoot = paramViewGroup;
  }
  
  private ScenePort(ViewGroup paramViewGroup, int paramInt, Context paramContext) {
    this.mContext = paramContext;
    this.mSceneRoot = paramViewGroup;
    this.mLayoutId = paramInt;
  }
  
  public ScenePort(ViewGroup paramViewGroup, View paramView) {
    this.mSceneRoot = paramViewGroup;
    this.mLayout = paramView;
  }
  
  static ScenePort getCurrentScene(View paramView) {
    return (ScenePort)paramView.getTag(R.id.transition_current_scene);
  }
  
  public static ScenePort getSceneForLayout(ViewGroup paramViewGroup, int paramInt, Context paramContext) {
    return new ScenePort(paramViewGroup, paramInt, paramContext);
  }
  
  static void setCurrentScene(View paramView, ScenePort paramScenePort) {
    paramView.setTag(R.id.transition_current_scene, paramScenePort);
  }
  
  public void enter() {
    if (this.mLayoutId > 0 || this.mLayout != null) {
      getSceneRoot().removeAllViews();
      if (this.mLayoutId > 0) {
        LayoutInflater.from(this.mContext).inflate(this.mLayoutId, this.mSceneRoot);
      } else {
        this.mSceneRoot.addView(this.mLayout);
      } 
    } 
    if (this.mEnterAction != null)
      this.mEnterAction.run(); 
    setCurrentScene((View)this.mSceneRoot, this);
  }
  
  public void exit() {
    if (getCurrentScene((View)this.mSceneRoot) == this && this.mExitAction != null)
      this.mExitAction.run(); 
  }
  
  public ViewGroup getSceneRoot() {
    return this.mSceneRoot;
  }
  
  boolean isCreatedFromLayoutResource() {
    return (this.mLayoutId > 0);
  }
  
  public void setEnterAction(Runnable paramRunnable) {
    this.mEnterAction = paramRunnable;
  }
  
  public void setExitAction(Runnable paramRunnable) {
    this.mExitAction = paramRunnable;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/ScenePort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */