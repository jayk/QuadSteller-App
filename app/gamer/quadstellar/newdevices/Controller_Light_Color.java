package app.gamer.quadstellar.newdevices;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.db.ColorInfoDB;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.domain.ColorInfo;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.newdevices.Base.TabController;
import app.gamer.quadstellar.newdevices.view.CycleWheelView;
import app.gamer.quadstellar.ui.widget.InterceptLinearLayout;
import app.gamer.quadstellar.ui.widget.InterceptPercentRelativeLayout;
import app.gamer.quadstellar.ui.widget.SetingDialog;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.MyColorPicker;
import com.larswerkman.holocolorpicker.MySaturationBar;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

public class Controller_Light_Color extends TabController implements ColorPicker.OnColorChangedListener, MyColorPicker.OnColorChangedListener, View.OnLongClickListener, View.OnClickListener {
  public final int MIN_CLICK_DELAY_TIME;
  
  private CircleImageView circleImageView1;
  
  private CircleImageView circleImageView2;
  
  private CircleImageView circleImageView3;
  
  private CircleImageView circleImageView4;
  
  private CircleImageView circleImageView5;
  
  private int colorTage;
  
  private int currentColor;
  
  private int currentProgress;
  
  private CycleWheelView cycleWheelView;
  
  private FatherDeviceInfo info;
  
  private ColorInfo info3;
  
  private boolean isClickColorButtion;
  
  private boolean isControlAll;
  
  private long lastClickTime;
  
  private InterceptPercentRelativeLayout light_whole;
  
  private InterceptLinearLayout ll_circle_bg;
  
  private SeekBar mBride;
  
  private Button mReset;
  
  private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener;
  
  private SeekBar mSpeed;
  
  private MyColorPicker myColorPicker;
  
  private MySaturationBar mySaturationBar;
  
  private int postion;
  
  int saveColor;
  
  private int seletColor;
  
  public Controller_Light_Color(Context paramContext, boolean paramBoolean) {
    super(paramContext, paramBoolean);
    FatherDeviceInfo fatherDeviceInfo;
    this.colorTage = -100;
    this.postion = -1;
    this.MIN_CLICK_DELAY_TIME = 200;
    this.mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {}
        
        public void onStartTrackingTouch(SeekBar param1SeekBar) {}
        
        public void onStopTrackingTouch(SeekBar param1SeekBar) {
          Controller_Light_Color controller_Light_Color;
          int i;
          switch (param1SeekBar.getId()) {
            default:
              return;
            case 2131756578:
              controller_Light_Color = Controller_Light_Color.this;
              if (param1SeekBar.getProgress() < 3) {
                i = 2;
              } else {
                i = param1SeekBar.getProgress();
              } 
              controller_Light_Color.mControl_Light_Mode(i, Controller_Light_Color.this.isControlAll, 8, false);
            case 2131756579:
              break;
          } 
          Controller_Light_Color.this.mControl_Light_Mode(param1SeekBar.getProgress(), Controller_Light_Color.this.isControlAll, 32, false);
        }
      };
    this.isClickColorButtion = false;
    this.isControlAll = paramBoolean;
    if (paramBoolean) {
      fatherDeviceInfo = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
    } else {
      fatherDeviceInfo = App.getInstance().getCurrentDevice();
    } 
    if (fatherDeviceInfo != null) {
      paramBoolean = bool;
      if (fatherDeviceInfo.getToggleState() != 0)
        paramBoolean = true; 
      setBackgroundColor(paramBoolean);
    } 
  }
  
  private void initModename() {
    ArrayList<String> arrayList = new ArrayList();
    arrayList.add(this.mContext.getString(2131296345));
    arrayList.add(this.mContext.getString(2131296282));
    arrayList.add(this.mContext.getString(2131296305));
    arrayList.add(this.mContext.getString(2131296420));
    arrayList.add(this.mContext.getString(2131296419));
    arrayList.add(this.mContext.getString(2131297058));
    arrayList.add(this.mContext.getString(2131297057));
    arrayList.add(this.mContext.getString(2131296492));
    arrayList.add(this.mContext.getString(2131297136));
    arrayList.add(this.mContext.getString(2131296320));
    arrayList.add(this.mContext.getString(2131296861));
    arrayList.add(this.mContext.getString(2131296862));
    final int[] ints = new int[12];
    arrayOfInt[0] = 0;
    arrayOfInt[1] = 1;
    arrayOfInt[2] = 6;
    arrayOfInt[3] = 4;
    arrayOfInt[4] = 9;
    arrayOfInt[5] = 5;
    arrayOfInt[6] = 10;
    arrayOfInt[7] = 18;
    arrayOfInt[8] = 3;
    arrayOfInt[9] = 16;
    arrayOfInt[10] = 21;
    arrayOfInt[11] = 22;
    this.cycleWheelView.setLabels(arrayList);
    byte b = 0;
    while (true) {
      if (b < arrayOfInt.length)
        if (arrayOfInt[b] == this.info.getLigtMode()) {
          setEnableClick(b);
          this.cycleWheelView.setSelection(b);
        } else {
          b++;
          continue;
        }  
      this.cycleWheelView.setLabelSelectColor(this.mContext.getResources().getColor(2131623987));
      this.cycleWheelView.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            public void onItemSelected(int param1Int, String param1String) {
              Controller_Light_Color.this.setEnableClick(param1Int);
              Controller_Light_Color.this.mControl_Light_Mode(ints[Controller_Light_Color.this.cycleWheelView.getSelection()], Controller_Light_Color.this.isControlAll, 2, false);
            }
          });
      return;
    } 
  }
  
  private void setEnableClick(int paramInt) {
    if (paramInt > 7) {
      this.ll_circle_bg.setAlpha(0.2F);
      this.mReset.setAlpha(0.2F);
      this.myColorPicker.setAlpha(0.2F);
      this.mySaturationBar.setAlpha(0.2F);
      this.ll_circle_bg.setChildClickable(false);
      this.mReset.setClickable(false);
      this.myColorPicker.setClickable(false);
      this.mySaturationBar.setClickable(false);
      return;
    } 
    this.ll_circle_bg.setAlpha(1.0F);
    this.mReset.setAlpha(1.0F);
    this.myColorPicker.setAlpha(1.0F);
    this.mySaturationBar.setAlpha(1.0F);
    this.ll_circle_bg.setChildClickable(true);
    this.mReset.setClickable(true);
    this.myColorPicker.setClickable(true);
    this.mySaturationBar.setClickable(true);
  }
  
  private static String toBrowserHexValue(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder(Integer.toHexString(paramInt & 0xFF));
    while (stringBuilder.length() < 2)
      stringBuilder.append("0"); 
    return stringBuilder.toString().toUpperCase();
  }
  
  public static String toHex(int paramInt1, int paramInt2, int paramInt3) {
    return "#" + toBrowserHexValue(paramInt1) + toBrowserHexValue(paramInt2) + toBrowserHexValue(paramInt3);
  }
  
  protected View initContentView(Context paramContext) {
    View view = View.inflate(this.mContext, 2130903125, null);
    this.light_whole = (InterceptPercentRelativeLayout)view.findViewById(2131756572);
    this.mSpeed = (SeekBar)view.findViewById(2131756579);
    this.mBride = (SeekBar)view.findViewById(2131756578);
    this.myColorPicker = (MyColorPicker)view.findViewById(2131756575);
    this.mySaturationBar = (MySaturationBar)view.findViewById(2131756577);
    this.myColorPicker.addSaturationBar(this.mySaturationBar);
    this.cycleWheelView = (CycleWheelView)view.findViewById(2131756587);
    this.mReset = (Button)view.findViewById(2131756586);
    this.circleImageView1 = (CircleImageView)view.findViewById(2131756581);
    this.circleImageView2 = (CircleImageView)view.findViewById(2131756582);
    this.circleImageView3 = (CircleImageView)view.findViewById(2131756583);
    this.circleImageView4 = (CircleImageView)view.findViewById(2131756584);
    this.circleImageView5 = (CircleImageView)view.findViewById(2131756585);
    this.ll_circle_bg = (InterceptLinearLayout)view.findViewById(2131756580);
    return view;
  }
  
  public void initData(int paramInt) {
    this.myColorPicker.setOnColorChangedListener(this);
    this.myColorPicker.setOnTouchListener(new View.OnTouchListener() {
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            switch (param1MotionEvent.getAction()) {
              default:
                return false;
              case 1:
                break;
            } 
            Controller_Light_Color.this.setColor(Controller_Light_Color.this.currentColor, -1);
          }
        });
    this.mySaturationBar.setOnSaturationChangedListener(new MySaturationBar.OnSaturationChangedListener() {
          public void onSaturationChanged(int param1Int1, int param1Int2) {
            Controller_Light_Color.access$002(Controller_Light_Color.this, param1Int1);
            Controller_Light_Color.access$102(Controller_Light_Color.this, param1Int2);
            Log.d("Controller_Light_Color", "saturation:" + param1Int1);
            if (!Controller_Light_Color.this.isFastDoubleClick()) {
              Controller_Light_Color.this.setColor(param1Int1, param1Int2);
              Controller_Light_Color.access$202(Controller_Light_Color.this, System.currentTimeMillis());
            } 
          }
        });
    this.mySaturationBar.setOnTouchListener(new View.OnTouchListener() {
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            switch (param1MotionEvent.getAction()) {
              default:
                return false;
              case 1:
                break;
            } 
            Controller_Light_Color.this.setColor(Controller_Light_Color.this.currentColor, Controller_Light_Color.this.currentProgress);
          }
        });
    if (this.isControlAll) {
      this.info = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
      for (FatherDeviceInfo fatherDeviceInfo : FatherDeviceInfoDB.getInstance().queryUserList()) {
        if (ColorInfoDB.getInstance().queryUserList(fatherDeviceInfo.getMacAdrass(), true) == null) {
          ColorInfo colorInfo = new ColorInfo();
          colorInfo.setIsWhole(true);
          colorInfo.setMacDrass(fatherDeviceInfo.getMacAdrass());
          ColorInfoDB.getInstance().insertUser(colorInfo);
        } 
      } 
    } else {
      this.info = App.getInstance().getCurrentDevice();
      if (ColorInfoDB.getInstance().queryUserList(this.info.getMacAdrass(), true) == null) {
        ColorInfo colorInfo = new ColorInfo();
        colorInfo.setIsWhole(true);
        colorInfo.setMacDrass(this.info.getMacAdrass());
        ColorInfoDB.getInstance().insertUser(colorInfo);
      } 
    } 
    if (this.info != null) {
      this.mBride.setProgress(this.info.getColorBrightness());
      this.mSpeed.setProgress(this.info.getColorSpeed());
      this.myColorPicker.setColor(Color.rgb(this.info.getColorR(), this.info.getColorG(), this.info.getColorB()));
    } 
    initModename();
    this.mBride.setMax(100);
    this.mSpeed.setMax(100);
    this.mBride.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
    this.mSpeed.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
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
    this.mReset.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Controller_Light_Color.this.circleImageView1.setImageDrawable((Drawable)new ColorDrawable(App.getAppContext().getResources().getColor(2131624097)));
            Controller_Light_Color.this.circleImageView2.setImageDrawable((Drawable)new ColorDrawable(App.getAppContext().getResources().getColor(2131624098)));
            Controller_Light_Color.this.circleImageView3.setImageDrawable((Drawable)new ColorDrawable(App.getAppContext().getResources().getColor(2131624099)));
            Controller_Light_Color.this.circleImageView4.setImageDrawable((Drawable)new ColorDrawable(App.getAppContext().getResources().getColor(2131624100)));
            Controller_Light_Color.this.circleImageView5.setImageDrawable((Drawable)new ColorDrawable(App.getAppContext().getResources().getColor(2131624101)));
            ColorInfo colorInfo = ColorInfoDB.getInstance().queryUserList(Controller_Light_Color.this.info.getMacAdrass(), true);
            colorInfo.setColorOne(-65536);
            colorInfo.setColorTwo(Color.parseColor("#FFFF00"));
            colorInfo.setColorThree(-16776961);
            colorInfo.setColorFour(-14483712);
            colorInfo.setColorFive(-1);
            ColorInfoDB.getInstance().updateUser(colorInfo);
          }
        });
  }
  
  public boolean isFastDoubleClick() {
    long l = System.currentTimeMillis() - this.lastClickTime;
    return (0L < l && l < 200L);
  }
  
  public void onClick(View paramView) {
    this.isClickColorButtion = true;
    if (this.isControlAll) {
      this.info3 = ColorInfoDB.getInstance().queryUserList(App.macDress, true);
      switch (paramView.getId()) {
        default:
          this.isClickColorButtion = false;
          return;
        case 2131756581:
          this.myColorPicker.setColor(this.info3.getColorOne());
          setColor(this.info3.getColorOne(), 100);
        case 2131756582:
          this.myColorPicker.setColor(this.info3.getColorTwo());
          setColor(this.info3.getColorTwo(), 100);
        case 2131756583:
          this.myColorPicker.setColor(this.info3.getColorThree());
          setColor(this.info3.getColorThree(), 100);
        case 2131756584:
          this.myColorPicker.setColor(this.info3.getColorFour());
          setColor(this.info3.getColorFour(), 100);
        case 2131756585:
          break;
      } 
      this.myColorPicker.setColor(this.info3.getColorFive());
      setColor(this.info3.getColorFive(), 100);
    } 
    ColorInfo colorInfo = ColorInfoDB.getInstance().queryUserList(App.getInstance().getCurrentDevice().getMacAdrass(), true);
    switch (paramView.getId()) {
      default:
      
      case 2131756581:
        Log.d("Controller_Light_Color", "selectColor.getOne():" + colorInfo.getColorOne());
        this.myColorPicker.setColor(colorInfo.getColorOne());
        setColor(colorInfo.getColorOne(), 100);
      case 2131756582:
        this.myColorPicker.setColor(colorInfo.getColorTwo());
        setColor(colorInfo.getColorTwo(), 100);
      case 2131756583:
        this.myColorPicker.setColor(colorInfo.getColorThree());
        setColor(colorInfo.getColorThree(), 100);
      case 2131756584:
        this.myColorPicker.setColor(colorInfo.getColorFour());
        setColor(colorInfo.getColorFour(), 100);
      case 2131756585:
        break;
    } 
    this.myColorPicker.setColor(colorInfo.getColorFive());
    setColor(colorInfo.getColorFive(), 100);
  }
  
  public void onColorChanged(int paramInt) {
    this.currentColor = paramInt;
    if (!isFastDoubleClick()) {
      if (!this.isClickColorButtion)
        setColor(paramInt, -1); 
      this.lastClickTime = System.currentTimeMillis();
    } 
  }
  
  public boolean onLongClick(final View v) {
    switch (v.getId()) {
      default:
        return false;
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
            CircleImageView circleImageView = (CircleImageView)v;
            if (Controller_Light_Color.this.saveColor == 0)
              Controller_Light_Color.this.saveColor = Controller_Light_Color.this.myColorPicker.getColor(); 
            circleImageView.setImageDrawable((Drawable)new ColorDrawable(Controller_Light_Color.this.saveColor));
            Log.d("Controller_Light_Color", "saveColor:" + Controller_Light_Color.this.saveColor);
            if (Controller_Light_Color.this.isControlAll) {
              List<FatherDeviceInfo> list = FatherDeviceInfoDB.getInstance().queryUserList();
              byte b = 0;
              while (b < list.size()) {
                ColorInfo colorInfo = ColorInfoDB.getInstance().queryUserList(((FatherDeviceInfo)list.get(b)).getMacAdrass(), true);
                switch (v.getId()) {
                  case 2131756581:
                    colorInfo.setColorOne(Controller_Light_Color.this.saveColor);
                    ColorInfoDB.getInstance().updateUser(colorInfo);
                    b++;
                    break;
                  case 2131756582:
                    colorInfo.setColorTwo(Controller_Light_Color.this.saveColor);
                    ColorInfoDB.getInstance().updateUser(colorInfo);
                    b++;
                    break;
                  case 2131756583:
                    colorInfo.setColorThree(Controller_Light_Color.this.saveColor);
                    ColorInfoDB.getInstance().updateUser(colorInfo);
                    b++;
                    break;
                  case 2131756584:
                    colorInfo.setColorFour(Controller_Light_Color.this.saveColor);
                    ColorInfoDB.getInstance().updateUser(colorInfo);
                    b++;
                    break;
                  case 2131756585:
                    colorInfo.setColorFive(Controller_Light_Color.this.saveColor);
                    ColorInfoDB.getInstance().updateUser(colorInfo);
                    b++;
                    break;
                } 
              } 
            } else {
              ColorInfo colorInfo = ColorInfoDB.getInstance().queryUserList(App.getInstance().getCurrentDevice().getMacAdrass(), true);
              switch (v.getId()) {
                default:
                  ColorInfoDB.getInstance().updateUser(colorInfo);
                  Log.d("Controller_Light_Color", "saveColor:" + colorInfo.getColorOne() + "mac地址" + colorInfo.getMacDrass());
                  return;
                case 2131756581:
                  colorInfo.setColorOne(Controller_Light_Color.this.saveColor);
                case 2131756582:
                  colorInfo.setColorTwo(Controller_Light_Color.this.saveColor);
                case 2131756583:
                  colorInfo.setColorThree(Controller_Light_Color.this.saveColor);
                case 2131756584:
                  colorInfo.setColorFour(Controller_Light_Color.this.saveColor);
                case 2131756585:
                  break;
              } 
              colorInfo.setColorFive(Controller_Light_Color.this.saveColor);
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
  
  public void setBackgroundColor(boolean paramBoolean) {
    if (paramBoolean) {
      this.light_whole.setAlpha(1.0F);
      this.light_whole.setChildClickable(true);
      return;
    } 
    this.light_whole.setAlpha(0.8F);
    this.light_whole.setChildClickable(false);
  }
  
  public void setColor(int paramInt1, int paramInt2) {
    this.saveColor = paramInt1;
    int i = (0xFF0000 & paramInt1) >> 16;
    if (Math.abs(this.colorTage - i) > 3) {
      mControl_Light_Color(i, (0xFF00 & paramInt1) >> 8, paramInt1 & 0xFF, this.isControlAll, false, 255, paramInt2);
      this.colorTage = paramInt1;
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Controller_Light_Color.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */