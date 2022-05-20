package app.gamer.quadstellar.newdevices.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;

public class Help_Activity_ViewBinding implements Unbinder {
  private Help_Activity target;
  
  private View view2131756401;
  
  @UiThread
  public Help_Activity_ViewBinding(Help_Activity paramHelp_Activity) {
    this(paramHelp_Activity, paramHelp_Activity.getWindow().getDecorView());
  }
  
  @UiThread
  public Help_Activity_ViewBinding(final Help_Activity target, View paramView) {
    this.target = target;
    paramView = Utils.findRequiredView(paramView, 2131756401, "field 'btHelp' and method 'onViewClicked'");
    target.btHelp = (Button)Utils.castView(paramView, 2131756401, "field 'btHelp'", Button.class);
    this.view2131756401 = paramView;
    paramView.setOnClickListener((View.OnClickListener)new DebouncingOnClickListener() {
          public void doClick(View param1View) {
            target.onViewClicked();
          }
        });
  }
  
  @CallSuper
  public void unbind() {
    Help_Activity help_Activity = this.target;
    if (help_Activity == null)
      throw new IllegalStateException("Bindings already cleared."); 
    this.target = null;
    help_Activity.btHelp = null;
    this.view2131756401.setOnClickListener(null);
    this.view2131756401 = null;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/activity/Help_Activity_ViewBinding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */