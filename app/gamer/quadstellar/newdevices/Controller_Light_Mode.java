package app.gamer.quadstellar.newdevices;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.db.ColorInfoDB;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.db.LightDeviceInfoDB;
import app.gamer.quadstellar.domain.ColorInfo;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.domain.LightDeviceInfo;
import app.gamer.quadstellar.newdevices.Base.TabController;
import app.gamer.quadstellar.newdevices.view.CycleWheelView;
import app.gamer.quadstellar.ui.widget.InterceptPercentRelativeLayout;
import app.gamer.quadstellar.ui.widget.SetingDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.larswerkman.holocolorpicker.MyColorPicker;
import com.larswerkman.holocolorpicker.MySaturationBar;
import com.zhy.android.percent.support.PercentRelativeLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

public class Controller_Light_Mode extends TabController implements View.OnLongClickListener, View.OnClickListener, MyColorPicker.OnColorChangedListener {
  public final int MIN_CLICK_DELAY_TIME = 200;
  
  private final int WIFI_LAMP_WARM_YELLOW = 4;
  
  @BindView(2131756604)
  Button btFive;
  
  @BindView(2131756603)
  Button btFour;
  
  @BindView(2131756600)
  Button btOne;
  
  @BindView(2131756605)
  Button btSix;
  
  @BindView(2131756602)
  Button btThree;
  
  @BindView(2131756601)
  Button btTwo;
  
  private Button bt_cancel;
  
  private Button bt_ok;
  
  private CircleImageView circleImageView1;
  
  private CircleImageView circleImageView2;
  
  private CircleImageView circleImageView3;
  
  private CircleImageView circleImageView4;
  
  private CircleImageView circleImageView5;
  
  private int colorTage = -100;
  
  private int currentColor;
  
  private int currentProgress;
  
  @BindView(2131756587)
  CycleWheelView cycleWheelView;
  
  private Dialog dialog;
  
  private FatherDeviceInfo info;
  
  private ColorInfo info3;
  
  private boolean isClickColorButtion = false;
  
  private boolean isControlAll;
  
  @BindView(2131756597)
  ImageView ivLocalControl;
  
  private long lastClickTime;
  
  private LightDeviceInfo lightDeviceInfo;
  
  private int[] lightDeviceInfoCurrent = new int[4];
  
  @BindView(2131756606)
  LinearLayout llColorSpeed;
  
  @BindView(2131756590)
  PercentRelativeLayout llLocalControl;
  
  @BindView(2131756607)
  SeekBar mBride;
  
  private FatherDeviceInfo mDevice;
  
  private Button mReset;
  
  private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
      public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {}
      
      public void onStartTrackingTouch(SeekBar param1SeekBar) {}
      
      public void onStopTrackingTouch(SeekBar param1SeekBar) {
        Controller_Light_Mode controller_Light_Mode;
        int i;
        switch (param1SeekBar.getId()) {
          default:
            return;
          case 2131756607:
            controller_Light_Mode = Controller_Light_Mode.this;
            if (param1SeekBar.getProgress() < 3) {
              i = 2;
            } else {
              i = param1SeekBar.getProgress();
            } 
            controller_Light_Mode.mControl_Light_Mode(i, Controller_Light_Mode.this.isControlAll, 8, true);
          case 2131756608:
            break;
        } 
        Controller_Light_Mode.this.mControl_Light_Mode(param1SeekBar.getProgress(), Controller_Light_Mode.this.isControlAll, 32, true);
      }
    };
  
  @BindView(2131756608)
  SeekBar mSpeed;
  
  private MyColorPicker myColor;
  
  private MySaturationBar mySaturationbar;
  
  private int postion = -1;
  
  @BindView(2131756588)
  InterceptPercentRelativeLayout prlLightLocal;
  
  @BindView(2131756589)
  PercentRelativeLayout rlLocalControl;
  
  private int saveColor;
  
  private int sonControlNmber;
  
  private long timeMilli;
  
  @BindView(2131756591)
  View vLightControlFive;
  
  @BindView(2131756592)
  View vLightControlFour;
  
  @BindView(2131756594)
  View vLightControlOne;
  
  @BindView(2131756596)
  View vLightControlSix;
  
  @BindView(2131756595)
  View vLightControlThree;
  
  @BindView(2131756593)
  View vLightControlTwo;
  
  public Controller_Light_Mode(Context paramContext, boolean paramBoolean) {
    super(paramContext, paramBoolean);
    this.isControlAll = paramBoolean;
  }
  
  private void initModename() {
    ArrayList<String> arrayList = new ArrayList();
    arrayList.add(this.mContext.getString(2131296345));
    arrayList.add(this.mContext.getString(2131296401));
    arrayList.add(this.mContext.getString(2131296400));
    arrayList.add(this.mContext.getString(2131296420));
    arrayList.add(this.mContext.getString(2131296419));
    arrayList.add(this.mContext.getString(2131297058));
    arrayList.add(this.mContext.getString(2131297057));
    arrayList.add(this.mContext.getString(2131296493));
    arrayList.add(this.mContext.getString(2131296494));
    final int[] ints = new int[9];
    arrayOfInt[0] = 0;
    arrayOfInt[1] = 1;
    arrayOfInt[2] = 6;
    arrayOfInt[3] = 4;
    arrayOfInt[4] = 9;
    arrayOfInt[5] = 5;
    arrayOfInt[6] = 10;
    arrayOfInt[7] = 18;
    arrayOfInt[8] = 19;
    this.mSpeed.setProgress(this.info.getPartLightSpeed());
    this.mBride.setProgress(this.info.getPartLightBrigtness());
    this.cycleWheelView.setLabels(arrayList);
    this.cycleWheelView.setLabelSelectColor(this.mContext.getResources().getColor(2131623987));
    byte b = 0;
    while (true) {
      if (b < arrayOfInt.length)
        if (arrayOfInt[b] == this.info.getPartLightMode()) {
          this.cycleWheelView.setSelection(b);
        } else {
          b++;
          continue;
        }  
      this.cycleWheelView.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            public void onItemSelected(int param1Int, String param1String) {
              Controller_Light_Mode.this.mControl_Light_Mode(ints[Controller_Light_Mode.this.cycleWheelView.getSelection()], Controller_Light_Mode.this.isControlAll, 2, true);
              Log.d("Controller_Light_Mode", "index:" + Controller_Light_Mode.this.cycleWheelView.getSelection());
            }
          });
      return;
    } 
  }
  
  private void initViewColor(int paramInt) {
    switch (this.sonControlNmber) {
      default:
        return;
      case 0:
        this.vLightControlOne.setBackgroundColor(paramInt);
        this.btOne.setBackgroundColor(paramInt);
      case 1:
        this.vLightControlTwo.setBackgroundColor(paramInt);
        this.btTwo.setBackgroundColor(paramInt);
      case 2:
        this.vLightControlThree.setBackgroundColor(paramInt);
        this.btThree.setBackgroundColor(paramInt);
      case 3:
        this.vLightControlFour.setBackgroundColor(paramInt);
        this.btFour.setBackgroundColor(paramInt);
      case 4:
        this.vLightControlFive.setBackgroundColor(paramInt);
        this.btFive.setBackgroundColor(paramInt);
      case 5:
        break;
    } 
    this.vLightControlSix.setBackgroundColor(paramInt);
    this.btSix.setBackgroundColor(paramInt);
  }
  
  private void setSelectColor(int paramInt1, int paramInt2) {
    this.saveColor = paramInt1;
    int i = (0xFF0000 & paramInt1) >> 16;
    int j = (0xFF00 & paramInt1) >> 8;
    int k = paramInt1 & 0xFF;
    Log.d("colorselect", "rgb" + i + "-" + j + "-" + k);
    Log.d("colorselect", "int" + paramInt1);
    if (paramInt1 != 0) {
      setViewColor();
      mControl_Light_Color(i, j, k, this.isControlAll, true, this.sonControlNmber, paramInt2);
    } 
  }
  
  private void setViewColor() {
    int i;
    int j;
    int k;
    switch (this.sonControlNmber) {
      default:
        i = this.saveColor;
        j = this.saveColor;
        k = this.saveColor;
        this.lightDeviceInfo.setColorR((i & 0xFF0000) >> 16);
        this.lightDeviceInfo.setColorG((j & 0xFF00) >> 8);
        this.lightDeviceInfo.setColorB(k & 0xFF);
        return;
      case 0:
        this.vLightControlOne.setBackgroundColor(this.saveColor);
        this.btOne.setBackgroundColor(this.saveColor);
      case 1:
        this.vLightControlTwo.setBackgroundColor(this.saveColor);
        this.btTwo.setBackgroundColor(this.saveColor);
      case 2:
        this.vLightControlThree.setBackgroundColor(this.saveColor);
        this.btThree.setBackgroundColor(this.saveColor);
      case 3:
        this.vLightControlFour.setBackgroundColor(this.saveColor);
        this.btFour.setBackgroundColor(this.saveColor);
      case 4:
        this.vLightControlFive.setBackgroundColor(this.saveColor);
        this.btFive.setBackgroundColor(this.saveColor);
      case 5:
        break;
    } 
    this.vLightControlSix.setBackgroundColor(this.saveColor);
    this.btSix.setBackgroundColor(this.saveColor);
  }
  
  protected View initContentView(Context paramContext) {
    View view = View.inflate(this.mContext, 2130903126, null);
    ButterKnife.bind(this, view);
    return view;
  }
  
  public void initData(int paramInt) {
    if (this.isControlAll) {
      this.info = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
      for (FatherDeviceInfo fatherDeviceInfo : FatherDeviceInfoDB.getInstance().queryUserList()) {
        if (ColorInfoDB.getInstance().queryUserList(fatherDeviceInfo.getMacAdrass(), false) == null) {
          ColorInfo colorInfo = new ColorInfo();
          colorInfo.setIsWhole(false);
          colorInfo.setMacDrass(fatherDeviceInfo.getMacAdrass());
          ColorInfoDB.getInstance().insertUser(colorInfo);
        } 
      } 
    } else {
      this.info = App.getInstance().getCurrentDevice();
      if (ColorInfoDB.getInstance().queryUserList(this.info.getMacAdrass(), false) == null) {
        ColorInfo colorInfo = new ColorInfo();
        colorInfo.setIsWhole(false);
        colorInfo.setMacDrass(this.info.getMacAdrass());
        ColorInfoDB.getInstance().insertUser(colorInfo);
      } 
    } 
    for (paramInt = LightDeviceInfoDB.getInstance().queryUserList(this.info.getMacAdrass()).size() - 1; paramInt > -1; paramInt--) {
      this.sonControlNmber = paramInt;
      this.lightDeviceInfo = LightDeviceInfoDB.getInstance().queryUserList(this.info.getMacAdrass(), this.sonControlNmber);
      initViewColor(Color.rgb(this.lightDeviceInfo.getColorR(), this.lightDeviceInfo.getColorG(), this.lightDeviceInfo.getColorB()));
    } 
    this.mBride.setMax(100);
    this.mSpeed.setMax(100);
    initModename();
    this.mBride.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
    this.mSpeed.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
  }
  
  public boolean isFastDoubleClick() {
    long l = System.currentTimeMillis() - this.lastClickTime;
    return (0L < l && l < 200L);
  }
  
  public void onClick(View paramView) {
    this.isClickColorButtion = true;
    if (this.isControlAll) {
      this.info3 = ColorInfoDB.getInstance().queryUserList(App.macDress, false);
      switch (paramView.getId()) {
        default:
          this.isClickColorButtion = false;
          return;
        case 2131756581:
          this.myColor.setColor(this.info3.getColorOne());
          setSelectColor(this.info3.getColorOne(), 100);
        case 2131756582:
          this.myColor.setColor(this.info3.getColorTwo());
          setSelectColor(this.info3.getColorTwo(), 100);
        case 2131756583:
          this.myColor.setColor(this.info3.getColorThree());
          setSelectColor(this.info3.getColorThree(), 100);
        case 2131756584:
          this.myColor.setColor(this.info3.getColorFour());
          setSelectColor(this.info3.getColorFour(), 100);
        case 2131756585:
          break;
      } 
      this.myColor.setColor(this.info3.getColorFive());
      setSelectColor(this.info3.getColorFive(), 100);
    } 
    ColorInfo colorInfo = ColorInfoDB.getInstance().queryUserList(App.getInstance().getCurrentDevice().getMacAdrass(), false);
    switch (paramView.getId()) {
      default:
      
      case 2131756581:
        Log.d("Controller_Light_Color", "selectColor.getOne():" + colorInfo.getColorOne());
        this.myColor.setColor(colorInfo.getColorOne());
        setSelectColor(colorInfo.getColorOne(), 100);
      case 2131756582:
        this.myColor.setColor(colorInfo.getColorTwo());
        setSelectColor(colorInfo.getColorTwo(), 100);
      case 2131756583:
        this.myColor.setColor(colorInfo.getColorThree());
        setSelectColor(colorInfo.getColorThree(), 100);
      case 2131756584:
        this.myColor.setColor(colorInfo.getColorFour());
        setSelectColor(colorInfo.getColorFour(), 100);
      case 2131756585:
        break;
    } 
    this.myColor.setColor(colorInfo.getColorFive());
    setSelectColor(colorInfo.getColorFive(), 100);
  }
  
  public void onColorChanged(int paramInt) {
    this.currentColor = paramInt;
    if (!isFastDoubleClick()) {
      if (!this.isClickColorButtion)
        setColor(paramInt, -1); 
      this.lastClickTime = System.currentTimeMillis();
    } 
  }
  
  public boolean onLongClick(final View rb) {
    switch (rb.getId()) {
      default:
        return true;
      case 2131756581:
      case 2131756582:
      case 2131756583:
      case 2131756584:
      case 2131756585:
        break;
    } 
    final SetingDialog mRenameDialog = new SetingDialog(this.mContext, 2131427778);
    View view = View.inflate(this.mContext, 2130903183, null);
    Button button1 = (Button)view.findViewById(2131756661);
    Button button2 = (Button)view.findViewById(2131756662);
    TextView textView1 = (TextView)view.findViewById(2131756725);
    TextView textView2 = (TextView)view.findViewById(2131756726);
    textView1.setText(this.mContext.getString(2131296322));
    textView2.setText(this.mContext.getString(2131297052));
    button1.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            mRenameDialog.dismiss();
            CircleImageView circleImageView = (CircleImageView)rb;
            if (Controller_Light_Mode.this.saveColor == 0)
              Controller_Light_Mode.access$1202(Controller_Light_Mode.this, Controller_Light_Mode.this.myColor.getColor()); 
            circleImageView.setImageDrawable((Drawable)new ColorDrawable(Controller_Light_Mode.this.saveColor));
            Log.d("Controller_Light_Color", "saveColor:" + Controller_Light_Mode.this.saveColor);
            if (Controller_Light_Mode.this.isControlAll) {
              List<FatherDeviceInfo> list = FatherDeviceInfoDB.getInstance().queryUserList();
              byte b = 0;
              while (b < list.size()) {
                ColorInfo colorInfo = ColorInfoDB.getInstance().queryUserList(((FatherDeviceInfo)list.get(b)).getMacAdrass(), false);
                switch (rb.getId()) {
                  case 2131756581:
                    colorInfo.setColorOne(Controller_Light_Mode.this.saveColor);
                    Controller_Light_Mode.this.info3.setColorOne(Controller_Light_Mode.this.saveColor);
                    ColorInfoDB.getInstance().updateUser(colorInfo);
                    ColorInfoDB.getInstance().updateUser(Controller_Light_Mode.this.info3);
                    b++;
                    break;
                  case 2131756582:
                    colorInfo.setColorTwo(Controller_Light_Mode.this.saveColor);
                    Controller_Light_Mode.this.info3.setColorTwo(Controller_Light_Mode.this.saveColor);
                    ColorInfoDB.getInstance().updateUser(colorInfo);
                    ColorInfoDB.getInstance().updateUser(Controller_Light_Mode.this.info3);
                    b++;
                    break;
                  case 2131756583:
                    colorInfo.setColorThree(Controller_Light_Mode.this.saveColor);
                    Controller_Light_Mode.this.info3.setColorThree(Controller_Light_Mode.this.saveColor);
                    ColorInfoDB.getInstance().updateUser(colorInfo);
                    ColorInfoDB.getInstance().updateUser(Controller_Light_Mode.this.info3);
                    b++;
                    break;
                  case 2131756584:
                    colorInfo.setColorFour(Controller_Light_Mode.this.saveColor);
                    Controller_Light_Mode.this.info3.setColorFour(Controller_Light_Mode.this.saveColor);
                    ColorInfoDB.getInstance().updateUser(colorInfo);
                    ColorInfoDB.getInstance().updateUser(Controller_Light_Mode.this.info3);
                    b++;
                    break;
                  case 2131756585:
                    colorInfo.setColorFive(Controller_Light_Mode.this.saveColor);
                    Controller_Light_Mode.this.info3.setColorFive(Controller_Light_Mode.this.saveColor);
                    ColorInfoDB.getInstance().updateUser(colorInfo);
                    ColorInfoDB.getInstance().updateUser(Controller_Light_Mode.this.info3);
                    b++;
                    break;
                } 
              } 
            } else {
              ColorInfo colorInfo = ColorInfoDB.getInstance().queryUserList(App.getInstance().getCurrentDevice().getMacAdrass(), false);
              switch (rb.getId()) {
                default:
                  ColorInfoDB.getInstance().updateUser(colorInfo);
                  Log.d("Controller_Light_Color", "saveColor:" + colorInfo.getColorOne() + "mac地址" + colorInfo.getMacDrass());
                  return;
                case 2131756581:
                  colorInfo.setColorOne(Controller_Light_Mode.this.saveColor);
                case 2131756582:
                  colorInfo.setColorTwo(Controller_Light_Mode.this.saveColor);
                case 2131756583:
                  colorInfo.setColorThree(Controller_Light_Mode.this.saveColor);
                case 2131756584:
                  colorInfo.setColorFour(Controller_Light_Mode.this.saveColor);
                case 2131756585:
                  break;
              } 
              colorInfo.setColorFive(Controller_Light_Mode.this.saveColor);
            } 
          }
        });
    button2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            mRenameDialog.cancel();
          }
        });
    setingDialog.setContentView(view);
    int i = this.mContext.getResources().getDimensionPixelOffset(2131362237);
    setingDialog.getWindow().setLayout(i, -2);
    setingDialog.show();
  }
  
  @OnClick({2131756600, 2131756601, 2131756602, 2131756603, 2131756604, 2131756605})
  public void onViewClicked(View paramView) {
    switch (paramView.getId()) {
      default:
        if (this.dialog == null)
          this.dialog = new Dialog(this.mContext, 2131427778); 
        paramView = View.inflate(this.mContext, 2130903165, null);
        this.myColor = (MyColorPicker)paramView.findViewById(2131756700);
        this.mySaturationbar = (MySaturationBar)paramView.findViewById(2131756701);
        this.mReset = (Button)paramView.findViewById(2131756586);
        this.circleImageView1 = (CircleImageView)paramView.findViewById(2131756581);
        this.circleImageView2 = (CircleImageView)paramView.findViewById(2131756582);
        this.circleImageView3 = (CircleImageView)paramView.findViewById(2131756583);
        this.circleImageView4 = (CircleImageView)paramView.findViewById(2131756584);
        this.circleImageView5 = (CircleImageView)paramView.findViewById(2131756585);
        this.bt_ok = (Button)paramView.findViewById(2131756688);
        this.bt_cancel = (Button)paramView.findViewById(2131756689);
        this.circleImageView1.setOnLongClickListener(this);
        this.circleImageView2.setOnLongClickListener(this);
        this.circleImageView3.setOnLongClickListener(this);
        this.circleImageView4.setOnLongClickListener(this);
        this.circleImageView5.setOnLongClickListener(this);
        this.circleImageView1.setOnClickListener(this);
        this.circleImageView2.setOnClickListener(this);
        this.circleImageView3.setOnClickListener(this);
        this.circleImageView4.setOnClickListener(this);
        this.circleImageView5.setOnClickListener(this);
        this.myColor.addSaturationBar(this.mySaturationbar);
        this.myColor.setOnColorChangedListener(this);
        this.myColor.setOnTouchListener(new View.OnTouchListener() {
              public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
                switch (param1MotionEvent.getAction()) {
                  default:
                    return false;
                  case 1:
                    break;
                } 
                Controller_Light_Mode.this.setColor(Controller_Light_Mode.this.currentColor, -1);
              }
            });
        this.mReset.setOnClickListener(new View.OnClickListener() {
              public void onClick(View param1View) {
                Controller_Light_Mode.this.circleImageView1.setImageDrawable((Drawable)new ColorDrawable(App.getAppContext().getResources().getColor(2131624097)));
                Controller_Light_Mode.this.circleImageView2.setImageDrawable((Drawable)new ColorDrawable(App.getAppContext().getResources().getColor(2131624098)));
                Controller_Light_Mode.this.circleImageView3.setImageDrawable((Drawable)new ColorDrawable(App.getAppContext().getResources().getColor(2131624099)));
                Controller_Light_Mode.this.circleImageView4.setImageDrawable((Drawable)new ColorDrawable(App.getAppContext().getResources().getColor(2131624100)));
                Controller_Light_Mode.this.circleImageView5.setImageDrawable((Drawable)new ColorDrawable(App.getAppContext().getResources().getColor(2131624101)));
                ColorInfo colorInfo = ColorInfoDB.getInstance().queryUserList(Controller_Light_Mode.this.info.getMacAdrass(), false);
                colorInfo.setColorOne(-65536);
                colorInfo.setColorTwo(Color.parseColor("#FFFF00"));
                colorInfo.setColorThree(-16776961);
                colorInfo.setColorFour(-14483712);
                colorInfo.setColorFive(-1);
                ColorInfoDB.getInstance().updateUser(colorInfo);
              }
            });
        if (this.isControlAll) {
          this.lightDeviceInfo = LightDeviceInfoDB.getInstance().queryUserList(this.info.getMacAdrass(), this.sonControlNmber);
        } else {
          break;
        } 
        this.lightDeviceInfoCurrent[0] = this.lightDeviceInfo.getColorR();
        this.lightDeviceInfoCurrent[1] = this.lightDeviceInfo.getColorG();
        this.lightDeviceInfoCurrent[2] = this.lightDeviceInfo.getColorB();
        this.myColor.setColor(Color.rgb(this.lightDeviceInfo.getColorR(), this.lightDeviceInfo.getColorG(), this.lightDeviceInfo.getColorB()));
        this.mySaturationbar.setOnSaturationChangedListener(new MySaturationBar.OnSaturationChangedListener() {
              public void onSaturationChanged(int param1Int1, int param1Int2) {
                Controller_Light_Mode.access$402(Controller_Light_Mode.this, param1Int1);
                Controller_Light_Mode.access$1102(Controller_Light_Mode.this, param1Int2);
                Log.d("Controller_Light_Color", "saturation:" + param1Int1);
                if (!Controller_Light_Mode.this.isFastDoubleClick()) {
                  Controller_Light_Mode.access$1202(Controller_Light_Mode.this, param1Int1);
                  Controller_Light_Mode.this.setColor(Controller_Light_Mode.this.saveColor, param1Int2);
                  Controller_Light_Mode.access$1302(Controller_Light_Mode.this, System.currentTimeMillis());
                } 
              }
            });
        this.mySaturationbar.setOnTouchListener(new View.OnTouchListener() {
              public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
                switch (param1MotionEvent.getAction()) {
                  default:
                    return false;
                  case 1:
                    break;
                } 
                Controller_Light_Mode.this.setColor(Controller_Light_Mode.this.currentColor, Controller_Light_Mode.this.currentProgress);
              }
            });
        this.bt_ok.setOnClickListener(new View.OnClickListener() {
              public void onClick(View param1View) {
                if (Controller_Light_Mode.this.dialog != null && Controller_Light_Mode.this.dialog.isShowing()) {
                  Controller_Light_Mode.this.dialog.dismiss();
                  Controller_Light_Mode.this.lightDeviceInfo.setSonLightColor(Controller_Light_Mode.this.saveColor);
                  LightDeviceInfoDB.getInstance().updateUser(Controller_Light_Mode.this.lightDeviceInfo);
                } 
              }
            });
        this.bt_cancel.setOnClickListener(new View.OnClickListener() {
              public void onClick(View param1View) {
                if (Controller_Light_Mode.this.dialog != null && Controller_Light_Mode.this.dialog.isShowing()) {
                  Controller_Light_Mode.this.dialog.dismiss();
                  Controller_Light_Mode.access$1202(Controller_Light_Mode.this, Color.argb(255, Controller_Light_Mode.this.lightDeviceInfoCurrent[0], Controller_Light_Mode.this.lightDeviceInfoCurrent[1], Controller_Light_Mode.this.lightDeviceInfoCurrent[2]));
                  Controller_Light_Mode.this.setViewColor();
                  Controller_Light_Mode.this.mControl_Light_Color(Controller_Light_Mode.this.lightDeviceInfoCurrent[0], Controller_Light_Mode.this.lightDeviceInfoCurrent[1], Controller_Light_Mode.this.lightDeviceInfoCurrent[2], Controller_Light_Mode.this.isControlAll, true, Controller_Light_Mode.this.sonControlNmber, -1);
                } 
              }
            });
        this.dialog.setContentView(paramView);
        this.dialog.show();
        return;
      case 2131756600:
        this.sonControlNmber = 0;
      case 2131756601:
        this.sonControlNmber = 1;
      case 2131756602:
        this.sonControlNmber = 2;
      case 2131756603:
        this.sonControlNmber = 3;
      case 2131756604:
        this.sonControlNmber = 4;
      case 2131756605:
        this.sonControlNmber = 5;
    } 
    this.lightDeviceInfo = LightDeviceInfoDB.getInstance().queryUserList(this.info.getMacAdrass(), this.sonControlNmber);
    this.lightDeviceInfoCurrent[0] = this.lightDeviceInfo.getColorR();
    this.lightDeviceInfoCurrent[1] = this.lightDeviceInfo.getColorG();
    this.lightDeviceInfoCurrent[2] = this.lightDeviceInfo.getColorB();
    this.myColor.setColor(Color.rgb(this.lightDeviceInfo.getColorR(), this.lightDeviceInfo.getColorG(), this.lightDeviceInfo.getColorB()));
    this.mySaturationbar.setOnSaturationChangedListener(new MySaturationBar.OnSaturationChangedListener() {
          public void onSaturationChanged(int param1Int1, int param1Int2) {
            Controller_Light_Mode.access$402(Controller_Light_Mode.this, param1Int1);
            Controller_Light_Mode.access$1102(Controller_Light_Mode.this, param1Int2);
            Log.d("Controller_Light_Color", "saturation:" + param1Int1);
            if (!Controller_Light_Mode.this.isFastDoubleClick()) {
              Controller_Light_Mode.access$1202(Controller_Light_Mode.this, param1Int1);
              Controller_Light_Mode.this.setColor(Controller_Light_Mode.this.saveColor, param1Int2);
              Controller_Light_Mode.access$1302(Controller_Light_Mode.this, System.currentTimeMillis());
            } 
          }
        });
    this.mySaturationbar.setOnTouchListener(new View.OnTouchListener() {
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            switch (param1MotionEvent.getAction()) {
              default:
                return false;
              case 1:
                break;
            } 
            Controller_Light_Mode.this.setColor(Controller_Light_Mode.this.currentColor, Controller_Light_Mode.this.currentProgress);
          }
        });
    this.bt_ok.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (Controller_Light_Mode.this.dialog != null && Controller_Light_Mode.this.dialog.isShowing()) {
              Controller_Light_Mode.this.dialog.dismiss();
              Controller_Light_Mode.this.lightDeviceInfo.setSonLightColor(Controller_Light_Mode.this.saveColor);
              LightDeviceInfoDB.getInstance().updateUser(Controller_Light_Mode.this.lightDeviceInfo);
            } 
          }
        });
    this.bt_cancel.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (Controller_Light_Mode.this.dialog != null && Controller_Light_Mode.this.dialog.isShowing()) {
              Controller_Light_Mode.this.dialog.dismiss();
              Controller_Light_Mode.access$1202(Controller_Light_Mode.this, Color.argb(255, Controller_Light_Mode.this.lightDeviceInfoCurrent[0], Controller_Light_Mode.this.lightDeviceInfoCurrent[1], Controller_Light_Mode.this.lightDeviceInfoCurrent[2]));
              Controller_Light_Mode.this.setViewColor();
              Controller_Light_Mode.this.mControl_Light_Color(Controller_Light_Mode.this.lightDeviceInfoCurrent[0], Controller_Light_Mode.this.lightDeviceInfoCurrent[1], Controller_Light_Mode.this.lightDeviceInfoCurrent[2], Controller_Light_Mode.this.isControlAll, true, Controller_Light_Mode.this.sonControlNmber, -1);
            } 
          }
        });
    this.dialog.setContentView(paramView);
    this.dialog.show();
  }
  
  public void setBackgroundColor(boolean paramBoolean) {
    if (paramBoolean) {
      this.prlLightLocal.setAlpha(1.0F);
      this.prlLightLocal.setChildClickable(true);
      return;
    } 
    this.prlLightLocal.setAlpha(0.8F);
    this.prlLightLocal.setChildClickable(false);
  }
  
  public void setColor(int paramInt1, int paramInt2) {
    this.saveColor = paramInt1;
    int i = (0xFF0000 & paramInt1) >> 16;
    if (Math.abs(this.colorTage - i) > 3) {
      setViewColor();
      mControl_Light_Color(i, (0xFF00 & paramInt1) >> 8, paramInt1 & 0xFF, this.isControlAll, true, this.sonControlNmber, paramInt2);
      this.colorTage = paramInt1;
      this.lightDeviceInfo.setSonLightColor(this.saveColor);
      LightDeviceInfoDB.getInstance().updateUser(this.lightDeviceInfo);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Controller_Light_Mode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */