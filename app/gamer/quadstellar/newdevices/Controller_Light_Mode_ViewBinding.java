package app.gamer.quadstellar.newdevices;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import app.gamer.quadstellar.newdevices.view.CycleWheelView;
import app.gamer.quadstellar.ui.widget.InterceptPercentRelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.zhy.android.percent.support.PercentRelativeLayout;

public class Controller_Light_Mode_ViewBinding implements Unbinder {
  private Controller_Light_Mode target;
  
  private View view2131756600;
  
  private View view2131756601;
  
  private View view2131756602;
  
  private View view2131756603;
  
  private View view2131756604;
  
  private View view2131756605;
  
  @UiThread
  public Controller_Light_Mode_ViewBinding(Controller_Light_Mode paramController_Light_Mode) {
    this(paramController_Light_Mode, (View)paramController_Light_Mode);
  }
  
  @UiThread
  public Controller_Light_Mode_ViewBinding(final Controller_Light_Mode target, View paramView) {
    this.target = target;
    View view = Utils.findRequiredView(paramView, 2131756600, "field 'btOne' and method 'onViewClicked'");
    target.btOne = (Button)Utils.castView(view, 2131756600, "field 'btOne'", Button.class);
    this.view2131756600 = view;
    view.setOnClickListener((View.OnClickListener)new DebouncingOnClickListener() {
          public void doClick(View param1View) {
            target.onViewClicked(param1View);
          }
        });
    view = Utils.findRequiredView(paramView, 2131756601, "field 'btTwo' and method 'onViewClicked'");
    target.btTwo = (Button)Utils.castView(view, 2131756601, "field 'btTwo'", Button.class);
    this.view2131756601 = view;
    view.setOnClickListener((View.OnClickListener)new DebouncingOnClickListener() {
          public void doClick(View param1View) {
            target.onViewClicked(param1View);
          }
        });
    view = Utils.findRequiredView(paramView, 2131756602, "field 'btThree' and method 'onViewClicked'");
    target.btThree = (Button)Utils.castView(view, 2131756602, "field 'btThree'", Button.class);
    this.view2131756602 = view;
    view.setOnClickListener((View.OnClickListener)new DebouncingOnClickListener() {
          public void doClick(View param1View) {
            target.onViewClicked(param1View);
          }
        });
    view = Utils.findRequiredView(paramView, 2131756603, "field 'btFour' and method 'onViewClicked'");
    target.btFour = (Button)Utils.castView(view, 2131756603, "field 'btFour'", Button.class);
    this.view2131756603 = view;
    view.setOnClickListener((View.OnClickListener)new DebouncingOnClickListener() {
          public void doClick(View param1View) {
            target.onViewClicked(param1View);
          }
        });
    view = Utils.findRequiredView(paramView, 2131756604, "field 'btFive' and method 'onViewClicked'");
    target.btFive = (Button)Utils.castView(view, 2131756604, "field 'btFive'", Button.class);
    this.view2131756604 = view;
    view.setOnClickListener((View.OnClickListener)new DebouncingOnClickListener() {
          public void doClick(View param1View) {
            target.onViewClicked(param1View);
          }
        });
    view = Utils.findRequiredView(paramView, 2131756605, "field 'btSix' and method 'onViewClicked'");
    target.btSix = (Button)Utils.castView(view, 2131756605, "field 'btSix'", Button.class);
    this.view2131756605 = view;
    view.setOnClickListener((View.OnClickListener)new DebouncingOnClickListener() {
          public void doClick(View param1View) {
            target.onViewClicked(param1View);
          }
        });
    target.rlLocalControl = (PercentRelativeLayout)Utils.findRequiredViewAsType(paramView, 2131756589, "field 'rlLocalControl'", PercentRelativeLayout.class);
    target.mBride = (SeekBar)Utils.findRequiredViewAsType(paramView, 2131756607, "field 'mBride'", SeekBar.class);
    target.mSpeed = (SeekBar)Utils.findRequiredViewAsType(paramView, 2131756608, "field 'mSpeed'", SeekBar.class);
    target.llColorSpeed = (LinearLayout)Utils.findRequiredViewAsType(paramView, 2131756606, "field 'llColorSpeed'", LinearLayout.class);
    target.cycleWheelView = (CycleWheelView)Utils.findRequiredViewAsType(paramView, 2131756587, "field 'cycleWheelView'", CycleWheelView.class);
    target.vLightControlFive = Utils.findRequiredView(paramView, 2131756591, "field 'vLightControlFive'");
    target.vLightControlSix = Utils.findRequiredView(paramView, 2131756596, "field 'vLightControlSix'");
    target.vLightControlFour = Utils.findRequiredView(paramView, 2131756592, "field 'vLightControlFour'");
    target.vLightControlTwo = Utils.findRequiredView(paramView, 2131756593, "field 'vLightControlTwo'");
    target.vLightControlOne = Utils.findRequiredView(paramView, 2131756594, "field 'vLightControlOne'");
    target.vLightControlThree = Utils.findRequiredView(paramView, 2131756595, "field 'vLightControlThree'");
    target.llLocalControl = (PercentRelativeLayout)Utils.findRequiredViewAsType(paramView, 2131756590, "field 'llLocalControl'", PercentRelativeLayout.class);
    target.prlLightLocal = (InterceptPercentRelativeLayout)Utils.findRequiredViewAsType(paramView, 2131756588, "field 'prlLightLocal'", InterceptPercentRelativeLayout.class);
    target.ivLocalControl = (ImageView)Utils.findRequiredViewAsType(paramView, 2131756597, "field 'ivLocalControl'", ImageView.class);
  }
  
  @CallSuper
  public void unbind() {
    Controller_Light_Mode controller_Light_Mode = this.target;
    if (controller_Light_Mode == null)
      throw new IllegalStateException("Bindings already cleared."); 
    this.target = null;
    controller_Light_Mode.btOne = null;
    controller_Light_Mode.btTwo = null;
    controller_Light_Mode.btThree = null;
    controller_Light_Mode.btFour = null;
    controller_Light_Mode.btFive = null;
    controller_Light_Mode.btSix = null;
    controller_Light_Mode.rlLocalControl = null;
    controller_Light_Mode.mBride = null;
    controller_Light_Mode.mSpeed = null;
    controller_Light_Mode.llColorSpeed = null;
    controller_Light_Mode.cycleWheelView = null;
    controller_Light_Mode.vLightControlFive = null;
    controller_Light_Mode.vLightControlSix = null;
    controller_Light_Mode.vLightControlFour = null;
    controller_Light_Mode.vLightControlTwo = null;
    controller_Light_Mode.vLightControlOne = null;
    controller_Light_Mode.vLightControlThree = null;
    controller_Light_Mode.llLocalControl = null;
    controller_Light_Mode.prlLightLocal = null;
    controller_Light_Mode.ivLocalControl = null;
    this.view2131756600.setOnClickListener(null);
    this.view2131756600 = null;
    this.view2131756601.setOnClickListener(null);
    this.view2131756601 = null;
    this.view2131756602.setOnClickListener(null);
    this.view2131756602 = null;
    this.view2131756603.setOnClickListener(null);
    this.view2131756603 = null;
    this.view2131756604.setOnClickListener(null);
    this.view2131756604 = null;
    this.view2131756605.setOnClickListener(null);
    this.view2131756605 = null;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Controller_Light_Mode_ViewBinding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */