package app.gamer.quadstellar.newdevices.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;

public class ScripActivity_ViewBinding implements Unbinder {
  private ScripActivity target;
  
  private View view2131756472;
  
  @UiThread
  public ScripActivity_ViewBinding(ScripActivity paramScripActivity) {
    this(paramScripActivity, paramScripActivity.getWindow().getDecorView());
  }
  
  @UiThread
  public ScripActivity_ViewBinding(final ScripActivity target, View paramView) {
    this.target = target;
    target.viewPager = (ViewPager)Utils.findRequiredViewAsType(paramView, 2131756471, "field 'viewPager'", ViewPager.class);
    View view = Utils.findRequiredView(paramView, 2131756472, "field 'startBtn' and method 'onViewClicked'");
    target.startBtn = (Button)Utils.castView(view, 2131756472, "field 'startBtn'", Button.class);
    this.view2131756472 = view;
    view.setOnClickListener((View.OnClickListener)new DebouncingOnClickListener() {
          public void doClick(View param1View) {
            target.onViewClicked();
          }
        });
    target.llContainer = (LinearLayout)Utils.findRequiredViewAsType(paramView, 2131756473, "field 'llContainer'", LinearLayout.class);
    target.ivRed = (ImageView)Utils.findRequiredViewAsType(paramView, 2131756474, "field 'ivRed'", ImageView.class);
  }
  
  @CallSuper
  public void unbind() {
    ScripActivity scripActivity = this.target;
    if (scripActivity == null)
      throw new IllegalStateException("Bindings already cleared."); 
    this.target = null;
    scripActivity.viewPager = null;
    scripActivity.startBtn = null;
    scripActivity.llContainer = null;
    scripActivity.ivRed = null;
    this.view2131756472.setOnClickListener(null);
    this.view2131756472 = null;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/activity/ScripActivity_ViewBinding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */