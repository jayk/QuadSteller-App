package app.gamer.quadstellar.newdevices.Base;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.controler.DataControler;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.domain.LightToggleEvent;
import app.gamer.quadstellar.newdevices.Controller_Light_Color;
import app.gamer.quadstellar.newdevices.Controller_Light_Mode;
import app.gamer.quadstellar.newdevices.Controller_Light_Out_Control;
import app.gamer.quadstellar.ui.widget.NoScrollViewPager;
import app.gamer.quadstellar.utils.PreferenceHelper;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Light_Controller extends TabController implements ViewPager.OnPageChangeListener {
  public static int mCurrentTab;
  
  private RadioButton colorButtion;
  
  private int colorMode;
  
  private Controller_Light_Color controller_light_color;
  
  private Controller_Light_Mode controller_light_mode;
  
  private FatherDeviceInfo info;
  
  private boolean isContrall;
  
  private View light_toggle;
  
  private int mCheckedId;
  
  private List<BaseController> mPagerDatas;
  
  private RadioGroup mRadioGroup;
  
  private ContentPagerAdapter mViewAdapter;
  
  private NoScrollViewPager mViewPager;
  
  private String macAddress = "FFFFFFFFFFFF";
  
  private RadioButton modeButtion;
  
  private String name;
  
  public Light_Controller(Context paramContext, boolean paramBoolean, View paramView) {
    super(paramContext, paramBoolean);
    this.isContrall = paramBoolean;
    this.light_toggle = paramView;
    EventBus.getDefault().register(this);
  }
  
  protected View initContentView(Context paramContext) {
    View view = View.inflate(this.mContext, 2130903124, null);
    this.mViewPager = (NoScrollViewPager)view.findViewById(2131756567);
    this.mRadioGroup = (RadioGroup)view.findViewById(2131756568);
    this.modeButtion = (RadioButton)view.findViewById(2131756570);
    this.colorButtion = (RadioButton)view.findViewById(2131756569);
    return view;
  }
  
  public void initData(int paramInt) {
    Log.d("TabController", "isContrlAll:" + this.isContrlAll);
    if (this.isContrlAll) {
      this.info = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
    } else {
      this.info = FatherDeviceInfoDB.getInstance().queryUserList(App.getInstance().getCurrentDevice().getMacAdrass());
      this.macAddress = App.getInstance().getCurrentDevice().getMacAdrass();
    } 
    this.controller_light_mode = new Controller_Light_Mode(this.mContext, this.isContrall);
    this.controller_light_color = new Controller_Light_Color(this.mContext, this.isContrall);
    this.mPagerDatas = new ArrayList<BaseController>();
    this.mPagerDatas.add(this.controller_light_color);
    this.mPagerDatas.add(this.controller_light_mode);
    this.mPagerDatas.add(new Controller_Light_Out_Control(this.mContext, this.isContrlAll));
    this.mViewAdapter = new ContentPagerAdapter();
    this.mViewPager.setAdapter(this.mViewAdapter);
    this.mRadioGroup.setOnCheckedChangeListener(new TabCheckedListener());
    this.mViewPager.setOnPageChangeListener(this);
    paramInt = PreferenceHelper.readInt("CurrentTab", 0);
    this.mViewPager.setCurrentItem(paramInt);
    if (paramInt == 1) {
      this.mRadioGroup.check(2131756570);
      return;
    } 
    this.mRadioGroup.check(2131756569);
  }
  
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(LightToggleEvent paramLightToggleEvent) {
    if (this.controller_light_mode != null)
      this.controller_light_mode.setBackgroundColor(paramLightToggleEvent.isCheked()); 
    if (this.controller_light_color != null)
      this.controller_light_color.setBackgroundColor(paramLightToggleEvent.isCheked()); 
  }
  
  public void onPageScrollStateChanged(int paramInt) {}
  
  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}
  
  public void onPageSelected(int paramInt) {
    switch (this.mViewPager.getCurrentItem()) {
      default:
        return;
      case 0:
        this.mRadioGroup.check(2131756569);
        this.light_toggle.setVisibility(0);
      case 1:
        this.mRadioGroup.check(2131756570);
        this.light_toggle.setVisibility(0);
      case 2:
        break;
    } 
    this.mRadioGroup.check(2131756571);
    this.light_toggle.setVisibility(8);
  }
  
  private class ContentPagerAdapter extends PagerAdapter {
    private ContentPagerAdapter() {}
    
    public void destroyItem(ViewGroup param1ViewGroup, int param1Int, Object param1Object) {
      param1ViewGroup.removeView((View)param1Object);
    }
    
    public int getCount() {
      return (Light_Controller.this.mPagerDatas != null) ? Light_Controller.this.mPagerDatas.size() : 0;
    }
    
    public int getItemPosition(Object param1Object) {
      return -2;
    }
    
    public Object instantiateItem(ViewGroup param1ViewGroup, int param1Int) {
      BaseController baseController = Light_Controller.this.mPagerDatas.get(param1Int);
      View view = baseController.getRootView();
      param1ViewGroup.addView(view);
      baseController.initData(0);
      baseController.exitActivity();
      return view;
    }
    
    public boolean isViewFromObject(View param1View, Object param1Object) {
      return (param1View == param1Object);
    }
  }
  
  private class TabCheckedListener implements RadioGroup.OnCheckedChangeListener {
    private TabCheckedListener() {}
    
    public void onCheckedChanged(RadioGroup param1RadioGroup, int param1Int) {
      if (Light_Controller.this.mCheckedId != param1Int) {
        Light_Controller.access$202(Light_Controller.this, param1Int);
        switch (param1Int) {
          default:
            Light_Controller.this.mViewPager.setCurrentItem(Light_Controller.mCurrentTab);
            PreferenceHelper.write("CurrentTab", Light_Controller.mCurrentTab);
            return;
          case 2131756569:
            Light_Controller.mCurrentTab = 0;
            Light_Controller.access$302(Light_Controller.this, "灯光");
            if (Light_Controller.this.isContrlAll) {
              Light_Controller.access$402(Light_Controller.this, FatherDeviceInfoDB.getInstance().queryUserList(App.macDress));
            } else {
              Light_Controller.access$402(Light_Controller.this, FatherDeviceInfoDB.getInstance().queryUserList(App.getInstance().getCurrentDevice().getMacAdrass()));
            } 
            DataControler.getInstance().sendLightData(Light_Controller.this.info, Light_Controller.this.isContrall, false, -1);
          case 2131756570:
            Light_Controller.mCurrentTab = 1;
            Light_Controller.access$302(Light_Controller.this, "局部控制");
            if (Light_Controller.this.isContrlAll) {
              Light_Controller.access$402(Light_Controller.this, FatherDeviceInfoDB.getInstance().queryUserList(App.macDress));
            } else {
              Light_Controller.access$402(Light_Controller.this, FatherDeviceInfoDB.getInstance().queryUserList(App.getInstance().getCurrentDevice().getMacAdrass()));
            } 
            DataControler.getInstance().sendLightData(Light_Controller.this.info, Light_Controller.this.isContrall, true, -1);
          case 2131756571:
            break;
        } 
        Light_Controller.mCurrentTab = 2;
        Light_Controller.access$302(Light_Controller.this, "外部控制");
        byte[] arrayOfByte = new byte[15];
        arrayOfByte[0] = (byte)(byte)arrayOfByte.length;
        arrayOfByte[1] = (byte)1;
        arrayOfByte[2] = (byte)(byte)Light_Controller.this.info.getToggleState();
        arrayOfByte[3] = (byte)20;
        DataControler.getInstance().sendData(Light_Controller.this.info, Light_Controller.this.isContrlAll, arrayOfByte);
      } 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Base/Light_Controller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */