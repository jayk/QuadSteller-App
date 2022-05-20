package app.gamer.quadstellar.newdevices;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.db.FanDeviceInfoDB;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.domain.FanDeviceInfo;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.domain.RefreshDataEvent;
import app.gamer.quadstellar.newdevices.Base.TabController;
import app.gamer.quadstellar.utils.PreferenceHelper;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Controller_Fan_Intelligent extends TabController implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
  public static int mCurrentTab;
  
  public final int MIN_CLICK_DELAY_TIME = 200;
  
  private Dialog dialog;
  
  private ImageView imageView;
  
  private FatherDeviceInfo info;
  
  private RadioButton intelligentButton;
  
  private boolean isContrall;
  
  private long lastClickTime;
  
  private Activity mActivity;
  
  private RadioButton mRadioButton;
  
  public RadioGroup mRadioGroup;
  
  public RadioGroup mRadioGroup2;
  
  private RadioGroup mRadioGroupButtom;
  
  private TextView mSpeed;
  
  private RadioButton radioButton;
  
  private RadioButton radioButtonConstant;
  
  private RadioButton rb_fan_performance;
  
  private RadioButton rb_fan_quite;
  
  private RefreshDataEvent refreshDataEvent;
  
  private SeekBar speed;
  
  private TextView tv_speed;
  
  private View view;
  
  public Controller_Fan_Intelligent(Context paramContext, boolean paramBoolean, Activity paramActivity) {
    super(paramContext, paramBoolean);
    this.isContrall = paramBoolean;
    this.mActivity = paramActivity;
    EventBus.getDefault().register(this);
  }
  
  private void initAnimation() {
    Animation animation = AnimationUtils.loadAnimation(this.mContext, 2130968612);
    animation.setInterpolator((Interpolator)new LinearInterpolator());
    this.imageView.startAnimation(animation);
  }
  
  private void initListener() {
    this.mRadioGroup.setOnCheckedChangeListener(this);
    this.mRadioGroup2.setOnCheckedChangeListener(this);
    this.radioButton.setOnClickListener(this);
    this.rb_fan_performance.setOnClickListener(this);
    this.rb_fan_quite.setOnClickListener(this);
    this.radioButtonConstant.setOnClickListener(this);
    this.mRadioGroupButtom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          public void onCheckedChanged(RadioGroup param1RadioGroup, @IdRes int param1Int) {
            switch (param1Int) {
              default:
                if (Controller_Fan_Intelligent.this.refreshDataEvent != null)
                  if (TabController.fanContorlType == 0) {
                    if (Controller_Fan_Intelligent.this.refreshDataEvent.getInFanSpeed() <= 0) {
                      Controller_Fan_Intelligent.this.mSpeed.setText(2131296897);
                    } else {
                      Controller_Fan_Intelligent.this.mSpeed.setText(Controller_Fan_Intelligent.this.mContext.getString(2131296567) + Controller_Fan_Intelligent.this.refreshDataEvent.getInFanSpeed() + "\nRPM");
                    } 
                  } else {
                    break;
                  }  
                PreferenceHelper.write("CurrentTabFan", Controller_Fan_Intelligent.mCurrentTab);
                Controller_Fan_Intelligent.this.initCheckButton();
                return;
              case 2131756553:
                TabController.fanContorlType = 0;
              case 2131756554:
                TabController.fanContorlType = 1;
            } 
            if (Controller_Fan_Intelligent.this.refreshDataEvent.getOutFanSpeed() <= 0) {
              Controller_Fan_Intelligent.this.mSpeed.setText(2131296897);
            } else {
              Controller_Fan_Intelligent.this.mSpeed.setText(Controller_Fan_Intelligent.this.mContext.getString(2131296567) + Controller_Fan_Intelligent.this.refreshDataEvent.getOutFanSpeed() + "\nRPM");
            } 
            PreferenceHelper.write("CurrentTabFan", Controller_Fan_Intelligent.mCurrentTab);
            Controller_Fan_Intelligent.this.initCheckButton();
          }
        });
    if (PreferenceHelper.readInt("CurrentTabFan", 0) == 0)
      this.mRadioGroupButtom.check(2131756553); 
  }
  
  private void mCheck() {
    if (this.isContrall) {
      this.info = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
      return;
    } 
    this.info = App.getInstance().getCurrentDevice();
    if (TabController.fanContorlType == 0) {
      FanDeviceInfo fanDeviceInfo1 = FanDeviceInfoDB.getInstance().queryUserList(this.info.getMacAdrass(), 0);
      this.mSpeed.setText(this.mContext.getString(2131296567) + fanDeviceInfo1.getRealitySonFanSpeed() + "\nRPM");
      return;
    } 
    FanDeviceInfo fanDeviceInfo = FanDeviceInfoDB.getInstance().queryUserList(this.info.getMacAdrass(), 1);
    this.mSpeed.setText(this.mContext.getString(2131296567) + fanDeviceInfo.getRealitySonFanSpeed() + "\nRPM");
    if (this.tv_speed != null)
      this.tv_speed.setText(fanDeviceInfo.getRealitySonFanSpeed() + ""); 
  }
  
  public void initCheckButton() {
    FanDeviceInfo fanDeviceInfo = FanDeviceInfoDB.getInstance().queryUserList(this.info.getMacAdrass(), TabController.fanContorlType);
    if (fanDeviceInfo != null) {
      switch (fanDeviceInfo.getSonFanMode()) {
        default:
          return;
        case 0:
          this.mRadioGroup.check(2131756564);
          mControl_Fan_changemode(0, 0, this.isContrall);
        case 1:
          this.mRadioGroup.check(2131756565);
          mControl_Fan_changemode(1, 0, this.isContrall);
        case 2:
          this.mRadioGroup.check(2131756566);
          mControl_Fan_changemode(2, 0, this.isContrall);
        case 3:
          break;
      } 
      this.mRadioGroup2.check(2131756559);
      mControl_Fan_changemode(3, fanDeviceInfo.getSetSonFanSpeed(), this.isContrall);
    } 
  }
  
  protected View initContentView(Context paramContext) {
    this.view = View.inflate(this.mContext, 2130903123, null);
    this.mRadioGroup = (RadioGroup)this.view.findViewById(2131756562);
    this.mRadioGroup2 = (RadioGroup)this.view.findViewById(2131756563);
    this.radioButton = (RadioButton)this.view.findViewById(2131756565);
    this.rb_fan_performance = (RadioButton)this.view.findViewById(2131756564);
    this.rb_fan_quite = (RadioButton)this.view.findViewById(2131756566);
    this.radioButtonConstant = (RadioButton)this.view.findViewById(2131756559);
    this.mSpeed = (TextView)this.view.findViewById(2131756557);
    this.imageView = (ImageView)this.view.findViewById(2131756556);
    this.mRadioGroupButtom = (RadioGroup)this.view.findViewById(2131756551);
    this.mRadioButton = (RadioButton)this.view.findViewById(2131756554);
    this.intelligentButton = (RadioButton)this.view.findViewById(2131756553);
    return this.view;
  }
  
  public void initData(int paramInt) {
    mCheck();
    initListener();
    initAnimation();
    initCheckButton();
  }
  
  public boolean isFastDoubleClick() {
    long l = System.currentTimeMillis() - this.lastClickTime;
    return (0L < l && l < 200L);
  }
  
  public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt) {
    if (paramRadioGroup != null && paramInt > 0) {
      if (paramRadioGroup == this.mRadioGroup) {
        this.mRadioGroup2.clearCheck();
      } else if (paramRadioGroup == this.mRadioGroup2) {
        this.mRadioGroup.clearCheck();
      } 
      paramRadioGroup.check(paramInt);
    } 
  }
  
  public void onClick(View paramView) {
    switch (paramView.getId()) {
      default:
        return;
      case 2131756564:
        mControl_Fan_changemode(0, 0, this.isContrall);
      case 2131756565:
        mControl_Fan_changemode(1, 0, this.isContrall);
      case 2131756566:
        mControl_Fan_changemode(2, 0, this.isContrall);
      case 2131756559:
        break;
    } 
    if (this.dialog == null)
      this.dialog = new Dialog(this.mContext, 2131427778); 
    View view = View.inflate(this.mContext, 2130903159, null);
    this.tv_speed = (TextView)view.findViewById(2131756686);
    Button button2 = (Button)view.findViewById(2131756688);
    Button button1 = (Button)view.findViewById(2131756689);
    this.speed = (SeekBar)view.findViewById(2131756687);
    FanDeviceInfo fanDeviceInfo = FanDeviceInfoDB.getInstance().queryUserList(this.info.getMacAdrass(), TabController.fanContorlType);
    if (this.refreshDataEvent != null) {
      if (TabController.fanContorlType == 0) {
        if (this.refreshDataEvent.getInFanSpeed() <= 0) {
          this.tv_speed.setText(2131296897);
        } else {
          this.tv_speed.setText(this.refreshDataEvent.getInFanSpeed() + "");
        } 
      } else if (this.refreshDataEvent.getOutFanSpeed() <= 0) {
        this.tv_speed.setText(2131296897);
      } else {
        this.tv_speed.setText(this.refreshDataEvent.getOutFanSpeed() + "");
      } 
    } else {
      this.tv_speed.setText(Integer.toString(fanDeviceInfo.getRealitySonFanSpeed()));
    } 
    this.speed.setProgress(fanDeviceInfo.getSetSonFanSpeed());
    final int progress = fanDeviceInfo.getSetSonFanSpeed();
    this.speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
            if (param1Boolean && !Controller_Fan_Intelligent.this.isFastDoubleClick()) {
              Controller_Fan_Intelligent.this.mControl_Fan_changemode(3, param1Int, Controller_Fan_Intelligent.this.isContrall);
              Controller_Fan_Intelligent.access$602(Controller_Fan_Intelligent.this, System.currentTimeMillis());
            } 
          }
          
          public void onStartTrackingTouch(SeekBar param1SeekBar) {}
          
          public void onStopTrackingTouch(SeekBar param1SeekBar) {
            Controller_Fan_Intelligent.this.mControl_Fan_changemode(3, param1SeekBar.getProgress(), Controller_Fan_Intelligent.this.isContrall);
          }
        });
    button2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (Controller_Fan_Intelligent.this.dialog != null && Controller_Fan_Intelligent.this.dialog.isShowing())
              Controller_Fan_Intelligent.this.dialog.dismiss(); 
          }
        });
    button1.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (Controller_Fan_Intelligent.this.dialog != null && Controller_Fan_Intelligent.this.dialog.isShowing()) {
              Controller_Fan_Intelligent.this.dialog.dismiss();
              Controller_Fan_Intelligent.this.mControl_Fan_changemode(3, progress, Controller_Fan_Intelligent.this.isContrall);
              Controller_Fan_Intelligent.this.speed.setProgress(progress);
            } 
          }
        });
    this.dialog.setContentView(view);
    i = (int)(this.mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.9D);
    this.dialog.getWindow().setLayout(i, -2);
    this.dialog.show();
  }
  
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(RefreshDataEvent paramRefreshDataEvent) {
    this.refreshDataEvent = paramRefreshDataEvent;
    if (TabController.fanContorlType == 0) {
      if (paramRefreshDataEvent.getInFanSpeed() <= 0) {
        this.mSpeed.setText(2131296897);
      } else {
        this.mSpeed.setText(this.mContext.getString(2131296567) + paramRefreshDataEvent.getInFanSpeed() + "\nRPM");
      } 
      if (this.tv_speed != null) {
        if (paramRefreshDataEvent.getInFanSpeed() <= 0) {
          this.tv_speed.setText(2131296897);
          return;
        } 
      } else {
        return;
      } 
      this.tv_speed.setText(paramRefreshDataEvent.getInFanSpeed() + "");
      return;
    } 
    if (this.tv_speed != null)
      if (paramRefreshDataEvent.getOutFanSpeed() <= 0) {
        this.tv_speed.setText(2131296897);
      } else {
        this.tv_speed.setText(paramRefreshDataEvent.getOutFanSpeed() + "");
      }  
    if (paramRefreshDataEvent.getOutFanSpeed() <= 0) {
      this.mSpeed.setText(2131296897);
      return;
    } 
    this.mSpeed.setText(this.mContext.getString(2131296567) + paramRefreshDataEvent.getOutFanSpeed() + "\nRPM");
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Controller_Fan_Intelligent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */