package app.gamer.quadstellar.newdevices.Base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import app.gamer.quadstellar.newdevices.Controller_Fan_Intelligent;
import app.gamer.quadstellar.utils.PreferenceHelper;
import java.util.List;

public class Fan_Controller extends TabController {
  public static int mCurrentTab;
  
  private Controller_Fan_Intelligent controller_fan_intelligent;
  
  private RadioButton intelligentButton;
  
  private boolean isContrall;
  
  Activity mActivity;
  
  private List<BaseController> mPagerDatas;
  
  private RadioButton mRadioButton;
  
  private RadioGroup mRadioGroup;
  
  private FrameLayout mViewPager;
  
  public Fan_Controller(Context paramContext, Activity paramActivity, boolean paramBoolean) {
    super(paramContext, paramBoolean);
    this.mActivity = paramActivity;
    this.isContrall = paramBoolean;
  }
  
  protected View initContentView(Context paramContext) {
    View view = View.inflate(this.mContext, 2130903121, null);
    this.mViewPager = (FrameLayout)view.findViewById(2131756552);
    this.mRadioGroup = (RadioGroup)view.findViewById(2131756551);
    this.mRadioButton = (RadioButton)view.findViewById(2131756554);
    this.intelligentButton = (RadioButton)view.findViewById(2131756553);
    return view;
  }
  
  public void initData(int paramInt) {
    if (this.controller_fan_intelligent == null)
      this.controller_fan_intelligent = new Controller_Fan_Intelligent(this.mContext, this.isContrall, this.mActivity); 
    this.mViewPager.addView((View)this.controller_fan_intelligent);
    this.mRadioGroup.setOnCheckedChangeListener(new TabCheckedListener());
    if (PreferenceHelper.readInt("CurrentTabFan", 0) == 0)
      this.mRadioGroup.check(2131756553); 
  }
  
  private class TabCheckedListener implements RadioGroup.OnCheckedChangeListener {
    private TabCheckedListener() {}
    
    public void onCheckedChanged(RadioGroup param1RadioGroup, int param1Int) {
      switch (param1Int) {
        default:
          PreferenceHelper.write("CurrentTabFan", Fan_Controller.mCurrentTab);
          Fan_Controller.this.controller_fan_intelligent.initCheckButton();
          return;
        case 2131756553:
          TabController.fanContorlType = 0;
        case 2131756554:
          break;
      } 
      TabController.fanContorlType = 1;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Base/Fan_Controller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */