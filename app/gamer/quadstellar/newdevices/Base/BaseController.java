package app.gamer.quadstellar.newdevices.Base;

import android.content.Context;
import android.view.View;

public abstract class BaseController extends View {
  protected Context mContext;
  
  protected View mRootView;
  
  public BaseController(Context paramContext) {
    super(paramContext);
    this.mContext = paramContext;
    this.mRootView = initView(paramContext);
  }
  
  public void exitActivity() {}
  
  public View getRootView() {
    return this.mRootView;
  }
  
  public void initData(int paramInt) {}
  
  protected abstract View initView(Context paramContext);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Base/BaseController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */