package app.gamer.quadstellar.newdevices.activity;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.Config;
import app.gamer.quadstellar.Prefix;
import app.gamer.quadstellar.controler.DataControler;
import app.gamer.quadstellar.db.FanDeviceInfoDB;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.db.LightDeviceInfoDB;
import app.gamer.quadstellar.domain.FanDeviceInfo;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.domain.LightDeviceInfo;
import app.gamer.quadstellar.domain.LightToggleEvent;
import app.gamer.quadstellar.net.UdpClient;
import app.gamer.quadstellar.newdevices.Base.BaseController;
import app.gamer.quadstellar.newdevices.Base.Light_Controller;
import app.gamer.quadstellar.newdevices.Base.Tem_Controller;
import app.gamer.quadstellar.newdevices.Controller_Fan_Intelligent;
import app.gamer.quadstellar.ui.BaseActivity;
import app.gamer.quadstellar.ui.widget.NoScrollViewPager;
import app.gamer.quadstellar.utils.ByteUtils;
import app.gamer.quadstellar.utils.SharedPreferencesUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;

public class ControlerActicity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
  public static int mCurrentTab;
  
  private LinearLayout back;
  
  private TimerTask connectTask;
  
  private boolean isCheked = false;
  
  public boolean isContrall;
  
  private List<BaseController> mPagerDatas;
  
  private RadioGroup mRadioGroup;
  
  private ContentPagerAdapter mViewAdapter;
  
  private NoScrollViewPager mViewPager;
  
  private String name;
  
  private RelativeLayout rl_light_toggle;
  
  private ImageView sb_default;
  
  private Timer timer;
  
  private boolean getIsOpen() {
    FatherDeviceInfo fatherDeviceInfo;
    boolean bool1 = false;
    if (this.isContrall) {
      fatherDeviceInfo = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
    } else {
      fatherDeviceInfo = App.getInstance().getCurrentDevice();
    } 
    boolean bool2 = bool1;
    if (fatherDeviceInfo != null) {
      bool2 = bool1;
      if (fatherDeviceInfo.getToggleState() != 0)
        bool2 = true; 
    } 
    return bool2;
  }
  
  private void initReceiver() {
    (new IntentFilter()).addAction(Config.BROADCAST_RECVPIPE);
  }
  
  private void mControl_Mode(int paramInt) {
    FatherDeviceInfo fatherDeviceInfo;
    boolean bool1;
    if (this.isContrall) {
      FatherDeviceInfo fatherDeviceInfo1 = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
      fatherDeviceInfo1.setToggleState(paramInt);
      fatherDeviceInfo1.setControlTag(1);
      List<FatherDeviceInfo> list = FatherDeviceInfoDB.getInstance().queryUserList();
      byte b = 0;
      while (true) {
        fatherDeviceInfo = fatherDeviceInfo1;
        if (b < list.size()) {
          ((FatherDeviceInfo)list.get(b)).setControlTag(1);
          ((FatherDeviceInfo)list.get(b)).setToggleState(paramInt);
          FatherDeviceInfoDB.getInstance().updateUser(list.get(b));
          b++;
          continue;
        } 
        break;
      } 
    } else {
      fatherDeviceInfo = App.getInstance().getCurrentDevice();
      fatherDeviceInfo.setToggleState((byte)paramInt);
      fatherDeviceInfo.setControlTag(1);
      FatherDeviceInfoDB.getInstance().updateUser(fatherDeviceInfo);
    } 
    DataControler dataControler = DataControler.getInstance();
    boolean bool = this.isContrall;
    if (Light_Controller.mCurrentTab == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    dataControler.sendLightData(fatherDeviceInfo, bool, bool1, 255);
  }
  
  private void startTask() {
    final FatherDeviceInfo fatherDeviceInfo;
    if (this.timer == null)
      this.timer = new Timer(); 
    if (this.connectTask != null) {
      this.connectTask.cancel();
      this.connectTask = null;
    } 
    if (this.isContrall) {
      fatherDeviceInfo = FatherDeviceInfoDB.getInstance().queryUserList().get(0);
    } else {
      fatherDeviceInfo = App.getInstance().getCurrentDevice();
    } 
    this.connectTask = new TimerTask() {
        public void run() {
          if (ControlerActicity.mCurrentTab != 0) {
            byte[] arrayOfByte = ByteUtils.append(50, new byte[][] { Prefix.OC_TYPE, ByteUtils.getSystemTimeBytes(Long.valueOf(System.currentTimeMillis())), ByteUtils.getPhonenumberBytes(), ByteUtils.hexStringToBytes(this.val$fatherDeviceInfo.getMacAdrass()), { 74 } });
            arrayOfByte[37] = (byte)(byte)(ControlerActicity.mCurrentTab + 1);
            UdpClient.getInstance().sendData(7074, fatherDeviceInfo.getDeviceIP(), arrayOfByte, null);
          } 
        }
      };
    if (this.timer != null && this.connectTask != null)
      this.timer.schedule(this.connectTask, 500L, 2500L); 
  }
  
  protected void init() {
    this.isContrall = getIntent().getExtras().getBoolean("isContrlALl", false);
    Log.d("ControlerActicity", "isContrall:" + this.isContrall);
    if (this.isContrall && FatherDeviceInfoDB.getInstance().queryUserList(App.macDress) == null) {
      FatherDeviceInfo fatherDeviceInfo = new FatherDeviceInfo();
      fatherDeviceInfo.setMacAdrass(App.macDress);
      fatherDeviceInfo.setDeviceIP(App.DEVICE_IP);
      FatherDeviceInfoDB.getInstance().insertUser(fatherDeviceInfo);
      byte b;
      for (b = 0; b < 2; b++) {
        FanDeviceInfo fanDeviceInfo = new FanDeviceInfo();
        fanDeviceInfo.setMacAdrass(App.macDress);
        fanDeviceInfo.setSonFanNumber(b);
        FanDeviceInfoDB.getInstance().insertUser(fanDeviceInfo);
      } 
      for (b = 0; b < 6; b++) {
        LightDeviceInfo lightDeviceInfo = new LightDeviceInfo();
        lightDeviceInfo.setMacAdrass(App.macDress);
        lightDeviceInfo.setSonLightNumber(b);
        LightDeviceInfoDB.getInstance().insertUser(lightDeviceInfo);
      } 
    } 
    initView();
    initData();
  }
  
  protected void initData() {
    this.isCheked = Boolean.valueOf(SharedPreferencesUtils.getData((Context)this, "toggle_isCheckde", false)).booleanValue();
    this.mPagerDatas = new ArrayList<BaseController>();
    this.mPagerDatas.add(new Light_Controller((Context)this, this.isContrall, (View)this.rl_light_toggle));
    this.mPagerDatas.add(new Controller_Fan_Intelligent((Context)this, this.isContrall, (Activity)this));
    this.mPagerDatas.add(new Tem_Controller((Context)this, this.isContrall));
    this.mViewAdapter = new ContentPagerAdapter();
    this.mViewPager.setAdapter(this.mViewAdapter);
    this.mRadioGroup.setOnCheckedChangeListener(new TabCheckedListener());
    this.mRadioGroup.check(2131756760);
    this.mViewPager.setOnPageChangeListener(this);
    this.back.setOnClickListener(this);
    if (getIsOpen()) {
      this.sb_default.setImageDrawable(getResources().getDrawable(2130838330));
      this.isCheked = true;
      EventBus.getDefault().post(new LightToggleEvent(true));
    } else {
      this.sb_default.setImageDrawable(getResources().getDrawable(2130838329));
      this.isCheked = false;
      EventBus.getDefault().post(new LightToggleEvent(false));
    } 
    this.sb_default.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            boolean bool;
            int i = 1;
            ControlerActicity controlerActicity = ControlerActicity.this;
            if (!ControlerActicity.this.isCheked) {
              bool = true;
            } else {
              bool = false;
            } 
            ControlerActicity.access$202(controlerActicity, bool);
            controlerActicity = ControlerActicity.this;
            if (!ControlerActicity.this.isCheked)
              i = 0; 
            controlerActicity.mControl_Mode(i);
            EventBus.getDefault().post(new LightToggleEvent(ControlerActicity.this.isCheked));
            ImageView imageView = ControlerActicity.this.sb_default;
            Resources resources = ControlerActicity.this.getResources();
            if (ControlerActicity.this.isCheked) {
              i = 2130838330;
            } else {
              i = 2130838329;
            } 
            imageView.setImageDrawable(resources.getDrawable(i));
          }
        });
    startTask();
  }
  
  public void initView() {
    this.mViewPager = (NoScrollViewPager)findViewById(2131756763);
    this.mRadioGroup = (RadioGroup)findViewById(2131756759);
    this.sb_default = (ImageView)findViewById(2131756756);
    this.rl_light_toggle = (RelativeLayout)findViewById(2131756755);
    this.back = (LinearLayout)findViewById(2131756758);
  }
  
  public void onClick(View paramView) {
    switch (paramView.getId()) {
      default:
        return;
      case 2131756758:
        break;
    } 
    finish();
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903194);
    initReceiver();
  }
  
  public void onDestroy() {
    super.onDestroy();
    stopTask();
  }
  
  public void onPageScrollStateChanged(int paramInt) {}
  
  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}
  
  public void onPageSelected(int paramInt) {
    switch (this.mViewPager.getCurrentItem()) {
      default:
        return;
      case 0:
        this.mRadioGroup.check(2131756760);
      case 1:
        this.mRadioGroup.check(2131756761);
      case 2:
        break;
    } 
    this.mRadioGroup.check(2131756762);
  }
  
  public void onStart() {
    super.onStart();
    this.isCheked = Boolean.valueOf(SharedPreferencesUtils.getData((Context)this, "toggle_isCheckde", false)).booleanValue();
  }
  
  public void onStop() {
    super.onStop();
    SharedPreferencesUtils.putData((Context)this, "toggle_isCheckde", this.isCheked);
    EventBus.getDefault().unregister(this);
  }
  
  public void stopTask() {
    if (this.timer != null) {
      this.timer.cancel();
      this.timer = null;
    } 
    if (this.connectTask != null) {
      this.connectTask.cancel();
      this.connectTask = null;
    } 
  }
  
  private class ContentPagerAdapter extends PagerAdapter {
    private ContentPagerAdapter() {}
    
    public void destroyItem(ViewGroup param1ViewGroup, int param1Int, Object param1Object) {
      param1ViewGroup.removeView((View)param1Object);
    }
    
    public int getCount() {
      return (ControlerActicity.this.mPagerDatas != null) ? ControlerActicity.this.mPagerDatas.size() : 0;
    }
    
    public int getItemPosition(Object param1Object) {
      return -2;
    }
    
    public Object instantiateItem(ViewGroup param1ViewGroup, int param1Int) {
      BaseController baseController = ControlerActicity.this.mPagerDatas.get(param1Int);
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
      switch (param1Int) {
        default:
          ControlerActicity.this.mViewPager.setCurrentItem(ControlerActicity.mCurrentTab);
          return;
        case 2131756760:
          ControlerActicity.mCurrentTab = 0;
          ControlerActicity.access$502(ControlerActicity.this, "灯光");
        case 2131756761:
          ControlerActicity.mCurrentTab = 1;
          ControlerActicity.access$502(ControlerActicity.this, "风扇");
        case 2131756762:
          break;
      } 
      ControlerActicity.mCurrentTab = 2;
      ControlerActicity.access$502(ControlerActicity.this, "温度");
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/activity/ControlerActicity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */