package app.gamer.quadstellar.newdevices;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.Prefix;
import app.gamer.quadstellar.db.DeviceInfoDB;
import app.gamer.quadstellar.domain.DeviceInfo;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.domain.LightFanEvent;
import app.gamer.quadstellar.mode.ControlDevice;
import app.gamer.quadstellar.net.DeviceManager;
import app.gamer.quadstellar.net.UdpClient;
import app.gamer.quadstellar.newdevices.Base.TabController;
import app.gamer.quadstellar.newdevices.view.CycleWheelView;
import app.gamer.quadstellar.newdevices.view.LoopView;
import app.gamer.quadstellar.newdevices.view.OnItemSelectedListener;
import app.gamer.quadstellar.ui.widget.SetingDialog;
import app.gamer.quadstellar.utils.ByteUtils;
import app.gamer.quadstellar.utils.PreferenceHelper;
import app.gamer.quadstellar.utils.SharedPreferencesUtils;
import app.gamer.quadstellar.utils.XlinkUtils;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Controller_Fan_Customize extends TabController implements View.OnClickListener, View.OnLongClickListener {
  private static final String TAG = "Controller_Fan_Customize";
  
  private int CUSTOMIZE_1 = 1;
  
  private int CUSTOMIZE_2 = 2;
  
  public final int MIN_CLICK_DELAY_TIME = 200;
  
  private Display d;
  
  private byte getmFan_now;
  
  private ImageView imageView;
  
  private DeviceInfo info;
  
  private boolean isContrall;
  
  private long lastClickTime;
  
  private Activity mActivity;
  
  private RadioButton mConstant;
  
  private RadioButton mCustomize;
  
  private RadioButton mCustomize_1;
  
  private FatherDeviceInfo mDevice;
  
  private EditText mEditName;
  
  private ArrayList<ArrayList<String>> mEndTime;
  
  private Handler mHandler;
  
  public RadioGroup mRadioGroup;
  
  private SetingDialog mRenameDialog;
  
  private TextView mSpeed;
  
  private ArrayList<String> mStarTime;
  
  private String macAddress;
  
  private RelativeLayout relativeLayout;
  
  private UdpClient udpClient;
  
  public Controller_Fan_Customize(Context paramContext, Activity paramActivity, boolean paramBoolean) {
    super(paramContext, paramBoolean);
    this.mActivity = paramActivity;
    this.isContrall = paramBoolean;
    if (!EventBus.getDefault().isRegistered(this))
      EventBus.getDefault().register(this); 
  }
  
  private ArrayList<String> addData() {
    this.mStarTime = new ArrayList<String>();
    this.mStarTime.add("500");
    this.mStarTime.add("600");
    this.mStarTime.add("700");
    this.mStarTime.add("800");
    this.mStarTime.add("900");
    this.mStarTime.add("1000");
    this.mStarTime.add("1100");
    this.mStarTime.add("1200");
    this.mStarTime.add("1300");
    this.mStarTime.add("1400");
    this.mStarTime.add("1500");
    this.mStarTime.add("1600");
    this.mStarTime.add("1700");
    this.mStarTime.add("1800");
    this.mStarTime.add("1900");
    this.mStarTime.add("2000");
    this.mStarTime.add("2100");
    this.mStarTime.add("2200");
    return this.mStarTime;
  }
  
  private void getmFanNum() {
    final ArrayList<String> mStarDatas = addData();
    final Dialog singel_speed_dialog = new Dialog(this.mContext, 2131427778);
    View view = View.inflate(this.mContext, 2130903160, null);
    LoopView loopView = (LoopView)view.findViewById(2131756691);
    Button button1 = (Button)view.findViewById(2131756661);
    Button button2 = (Button)view.findViewById(2131756662);
    dialog.setContentView(view);
    loopView.setItems(arrayList);
    loopView.setNotLoop();
    loopView.setTextSize(20.0F);
    loopView.setContextSzie(23.0F);
    int i = SharedPreferencesUtils.getData((Context)this.mActivity, this.macAddress, 10);
    loopView.setInitPosition(i);
    final String[] singel_speed = new String[1];
    arrayOfString[0] = arrayList.get(i);
    loopView.setListener(new OnItemSelectedListener() {
          public void onItemSelected(int param1Int) {
            singel_speed[0] = mStarDatas.get(param1Int);
            if (Controller_Fan_Customize.this.isContrall) {
              SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, Controller_Fan_Customize.this.macAddress, param1Int);
              for (byte b = 0; b < DeviceManager.getInstance().getDevices().size(); b++) {
                ControlDevice controlDevice = DeviceManager.getInstance().getDevices().get(b);
                SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, controlDevice.getMacAddress(), param1Int);
              } 
            } else {
              SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, Controller_Fan_Customize.this.macAddress, param1Int);
            } 
          }
        });
    button1.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            singel_speed_dialog.dismiss();
            Controller_Fan_Customize.this.sendSingleSpeedData(singel_speed);
          }
        });
    button2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            singel_speed_dialog.dismiss();
          }
        });
    WindowManager windowManager = this.mActivity.getWindowManager();
    dialog.getWindow().setBackgroundDrawable(null);
    i = (int)(windowManager.getDefaultDisplay().getWidth() * 0.7D);
    dialog.getWindow().setLayout(i, -2);
    dialog.show();
  }
  
  private void initAnimation() {
    Animation animation = AnimationUtils.loadAnimation(this.mContext, 2130968612);
    animation.setInterpolator((Interpolator)new LinearInterpolator());
    this.imageView.startAnimation(animation);
  }
  
  private void sendSingleSpeedData(String[] paramArrayOfString) {
    byte[] arrayOfByte1 = ByteUtils.getPhonenumberBytes();
    byte[] arrayOfByte2 = ByteUtils.int2OneByte(3);
    byte[] arrayOfByte3 = ByteUtils.int2OneByte(Integer.parseInt(paramArrayOfString[0]) / 100);
    PreferenceHelper.write("speedData", Integer.parseInt(paramArrayOfString[0]) / 100);
    if (this.isContrall) {
      DeviceInfo deviceInfo = DeviceInfoDB.getInstance((Context)this.mActivity).queryUserList(App.macDress);
      for (byte b = 0; b < DeviceManager.getInstance().getDevices().size(); b++) {
        ControlDevice controlDevice = DeviceManager.getInstance().getDevices().get(b);
        DeviceInfo deviceInfo1 = DeviceInfoDB.getInstance((Context)this.mActivity).queryUserList(controlDevice.getMacAddress());
        deviceInfo1.setFanLowSpeed(Integer.parseInt(paramArrayOfString[0]) / 100);
        deviceInfo1.setFanMaxSpeed(Integer.parseInt(paramArrayOfString[0]) / 100);
        deviceInfo.setFanLowSpeed(Integer.parseInt(paramArrayOfString[0]) / 100);
        deviceInfo.setFanMaxSpeed(Integer.parseInt(paramArrayOfString[0]) / 100);
        DeviceInfoDB.getInstance(this.mContext).updateUser(deviceInfo);
        DeviceInfoDB.getInstance(this.mContext).updateUser(deviceInfo1);
        controlDevice.setmLights(deviceInfo);
        byte[] arrayOfByte = ByteUtils.append(50, new byte[][] { Prefix.CONTROLLER_FAN, ByteUtils.getSystemTimeBytes(Long.valueOf(System.currentTimeMillis())), arrayOfByte1, ByteUtils.hexStringToBytes(App.macDress), arrayOfByte2, arrayOfByte3, arrayOfByte3 });
        this.udpClient = UdpClient.getInstance();
        this.udpClient.connectSocket();
        if (deviceInfo != null)
          this.udpClient.sendData(8063, deviceInfo.getDeviceIP(), arrayOfByte, null); 
      } 
    } else {
      arrayOfByte2 = ByteUtils.append(50, new byte[][] { Prefix.CONTROLLER_FAN, ByteUtils.getSystemTimeBytes(Long.valueOf(System.currentTimeMillis())), arrayOfByte1, ByteUtils.hexStringToBytes(this.mDevice.getMacAdrass()), arrayOfByte2, arrayOfByte3, arrayOfByte3 });
      DeviceInfo deviceInfo = DeviceInfoDB.getInstance((Context)this.mActivity).queryUserList(this.mDevice.getMacAdrass());
      this.udpClient = UdpClient.getInstance();
      this.udpClient.connectSocket();
      if (deviceInfo != null)
        this.udpClient.sendData(8063, deviceInfo.getDeviceIP(), arrayOfByte2, null); 
    } 
  }
  
  private void showReNameDialog(final RadioButton view) {
    this.mRenameDialog = new SetingDialog((Context)this.mActivity, 2131427778);
    View view = View.inflate((Context)this.mActivity, 2130903176, null);
    Button button1 = (Button)view.findViewById(2131756661);
    Button button2 = (Button)view.findViewById(2131756662);
    this.mEditName = (EditText)view.findViewById(2131756717);
    button1.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            String str = Controller_Fan_Customize.this.mEditName.getText().toString().trim();
            if (TextUtils.isEmpty(str)) {
              XlinkUtils.shortTips(Controller_Fan_Customize.this.mActivity.getString(2131296911));
              return;
            } 
            view.setText(str);
            if (Controller_Fan_Customize.this.isContrall) {
              SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, Controller_Fan_Customize.this.macAddress + view.getId(), str);
              for (byte b = 0; b < DeviceManager.getInstance().getDevices().size(); b++) {
                ControlDevice controlDevice = DeviceManager.getInstance().getDevices().get(b);
                SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, controlDevice.getMacAddress() + view.getId(), str);
              } 
            } else {
              SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, Controller_Fan_Customize.this.macAddress + view.getId(), str);
            } 
            Controller_Fan_Customize.this.mRenameDialog.dismiss();
          }
        });
    button2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (Controller_Fan_Customize.this.mRenameDialog != null)
              Controller_Fan_Customize.this.mRenameDialog.dismiss(); 
          }
        });
    this.mRenameDialog.setContentView(view);
    Display display = this.mActivity.getWindowManager().getDefaultDisplay();
    int i = (int)(display.getHeight() * 0.5D);
    i = (int)(display.getWidth() * 0.7D);
    this.mRenameDialog.getWindow().setLayout(i, -2);
    this.mRenameDialog.show();
  }
  
  protected void getTimePicker(final int tage) {
    ArrayList<String> arrayList1 = addData();
    final ArrayList<String> maxSpeedList = new ArrayList();
    final ArrayList<String> lowSpeedList = new ArrayList();
    arrayList1.remove("2200");
    arrayList3.addAll(arrayList1);
    arrayList1.remove("500");
    arrayList1.add("2200");
    arrayList2.addAll(arrayList1);
    final Dialog second_speed_dialog = new Dialog(this.mContext, 2131427778);
    View view = View.inflate(this.mContext, 2130903161, null);
    final CycleWheelView lv_two_speed1 = (CycleWheelView)view.findViewById(2131756692);
    final CycleWheelView lv_two_speed2 = (CycleWheelView)view.findViewById(2131756694);
    Button button1 = (Button)view.findViewById(2131756661);
    Button button2 = (Button)view.findViewById(2131756662);
    dialog.setContentView(view);
    cycleWheelView2.setLabels(arrayList2);
    cycleWheelView1.setLabels(arrayList3);
    cycleWheelView1.setCycleEnable(false);
    cycleWheelView2.setCycleEnable(false);
    cycleWheelView1.setSolid(Color.parseColor("#313236"), Color.parseColor("#313236"));
    cycleWheelView2.setSolid(Color.parseColor("#313236"), Color.parseColor("#313236"));
    cycleWheelView1.setLabelSelectColor(this.mContext.getResources().getColor(2131624061));
    cycleWheelView2.setLabelSelectColor(this.mContext.getResources().getColor(2131624061));
    int i = SharedPreferencesUtils.getData((Context)this.mActivity, tage + this.macAddress + "low", 5);
    int j = SharedPreferencesUtils.getData((Context)this.mActivity, tage + this.macAddress + "max", 9);
    cycleWheelView2.setSelection(j);
    cycleWheelView1.setSelection(i);
    final String[] lowSpeed = new String[1];
    arrayOfString1[0] = arrayList3.get(i);
    final String[] HgtSpeed = new String[1];
    arrayOfString2[0] = arrayList2.get(j);
    cycleWheelView2.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
          public void onItemSelected(int param1Int, String param1String) {
            HgtSpeed[0] = maxSpeedList.get(param1Int);
          }
        });
    cycleWheelView1.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
          public void onItemSelected(int param1Int, String param1String) {
            if (!Controller_Fan_Customize.this.isFastDoubleClick()) {
              lowSpeed[0] = lowSpeedList.get(param1Int);
              ArrayList arrayList = new ArrayList();
              List list = lv_two_speed2.getLabels();
              int i = lv_two_speed2.getSelection();
              while (param1Int < maxSpeedList.size()) {
                arrayList.add(maxSpeedList.get(param1Int));
                param1Int++;
              } 
              lv_two_speed2.setLabels(arrayList);
              if (list.size() < arrayList.size()) {
                lv_two_speed2.setSelection(arrayList.size() + i - list.size());
              } else {
                int j = arrayList.size() + i - list.size();
                param1Int = j;
                if (j < 0)
                  param1Int = 0; 
                if (Integer.parseInt(param1String) + 100 >= Integer.parseInt((String)arrayList.get(param1Int))) {
                  lv_two_speed2.setSelection(0);
                } else {
                  lv_two_speed2.setSelection(arrayList.size() + i - list.size());
                } 
              } 
              Log.d("hh", "lv_two_speed2.getSelectedItem():" + lv_two_speed2.getSelection());
              Controller_Fan_Customize.access$002(Controller_Fan_Customize.this, System.currentTimeMillis());
            } 
          }
        });
    button1.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            byte[] arrayOfByte1;
            second_speed_dialog.dismiss();
            int i = lv_two_speed2.getLabels().size();
            HgtSpeed[0] = lv_two_speed2.getSelectLabel();
            if (Controller_Fan_Customize.this.isContrall) {
              SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, tage + Controller_Fan_Customize.this.macAddress + "low", lv_two_speed1.getSelection());
              SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, tage + Controller_Fan_Customize.this.macAddress + "max", lv_two_speed2.getSelection() + maxSpeedList.size() - i);
              for (byte b = 0; b < DeviceManager.getInstance().getDevices().size(); b++) {
                ControlDevice controlDevice = DeviceManager.getInstance().getDevices().get(b);
                SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, tage + controlDevice.getMacAddress() + "low", lv_two_speed1.getSelection());
                SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, tage + controlDevice.getMacAddress() + "max", lv_two_speed2.getSelection() + maxSpeedList.size() - i);
              } 
            } else {
              SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, tage + Controller_Fan_Customize.this.macAddress + "low", lv_two_speed1.getSelection());
              SharedPreferencesUtils.putData((Context)Controller_Fan_Customize.this.mActivity, tage + Controller_Fan_Customize.this.macAddress + "max", lv_two_speed2.getSelection() + maxSpeedList.size() - i);
            } 
            byte[] arrayOfByte2 = ByteUtils.getPhonenumberBytes();
            if (tage == Controller_Fan_Customize.this.CUSTOMIZE_1) {
              arrayOfByte1 = ByteUtils.int2OneByte(4);
            } else {
              arrayOfByte1 = ByteUtils.int2OneByte(5);
            } 
            byte[] arrayOfByte3 = ByteUtils.int2OneByte(Integer.parseInt(lowSpeed[0]) / 100);
            byte[] arrayOfByte4 = ByteUtils.int2OneByte(Integer.parseInt(HgtSpeed[0]) / 100);
            PreferenceHelper.write("speedLowData", Integer.parseInt(lowSpeed[0]) / 100);
            PreferenceHelper.write("speedMostData", Integer.parseInt(HgtSpeed[0]) / 100);
            Log.d("Controller_Fan_Customiz", lowSpeed[0] + "***************" + HgtSpeed[0]);
            if (Controller_Fan_Customize.this.isContrall) {
              DeviceInfo deviceInfo = DeviceInfoDB.getInstance((Context)Controller_Fan_Customize.this.mActivity).queryUserList(App.macDress);
              for (byte b = 0; b < DeviceManager.getInstance().getDevices().size(); b++) {
                ControlDevice controlDevice = DeviceManager.getInstance().getDevices().get(b);
                DeviceInfo deviceInfo1 = DeviceInfoDB.getInstance((Context)Controller_Fan_Customize.this.mActivity).queryUserList(controlDevice.getMacAddress());
                deviceInfo1.setFanLowSpeed(Integer.parseInt(lowSpeed[0]) / 100);
                deviceInfo1.setFanMaxSpeed(Integer.parseInt(HgtSpeed[0]) / 100);
                deviceInfo.setFanLowSpeed(Integer.parseInt(lowSpeed[0]) / 100);
                deviceInfo.setFanMaxSpeed(Integer.parseInt(HgtSpeed[0]) / 100);
                DeviceInfoDB.getInstance(Controller_Fan_Customize.this.mContext).updateUser(deviceInfo);
                DeviceInfoDB.getInstance(Controller_Fan_Customize.this.mContext).updateUser(deviceInfo1);
                controlDevice.setmLights(deviceInfo);
                byte[] arrayOfByte = ByteUtils.append(50, new byte[][] { Prefix.CONTROLLER_FAN, ByteUtils.getSystemTimeBytes(Long.valueOf(System.currentTimeMillis())), arrayOfByte2, ByteUtils.hexStringToBytes(App.macDress), arrayOfByte1, arrayOfByte3, arrayOfByte4 });
                Controller_Fan_Customize.access$702(Controller_Fan_Customize.this, UdpClient.getInstance());
                Controller_Fan_Customize.this.udpClient.connectSocket();
                if (deviceInfo != null)
                  Controller_Fan_Customize.this.udpClient.sendData(8063, deviceInfo.getDeviceIP(), arrayOfByte, null); 
              } 
            } else {
              arrayOfByte1 = ByteUtils.append(50, new byte[][] { Prefix.CONTROLLER_FAN, ByteUtils.getSystemTimeBytes(Long.valueOf(System.currentTimeMillis())), arrayOfByte2, ByteUtils.hexStringToBytes(Controller_Fan_Customize.access$800(this.this$0).getMacAdrass()), arrayOfByte1, arrayOfByte3, arrayOfByte4 });
              DeviceInfo deviceInfo = DeviceInfoDB.getInstance((Context)Controller_Fan_Customize.this.mActivity).queryUserList(Controller_Fan_Customize.this.mDevice.getMacAdrass());
              Controller_Fan_Customize.access$702(Controller_Fan_Customize.this, UdpClient.getInstance());
              Controller_Fan_Customize.this.udpClient.connectSocket();
              if (deviceInfo != null)
                Controller_Fan_Customize.this.udpClient.sendData(8063, deviceInfo.getDeviceIP(), arrayOfByte1, null); 
            } 
          }
        });
    button2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            second_speed_dialog.dismiss();
          }
        });
    tage = (int)(this.mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.7D);
    dialog.getWindow().setLayout(tage, -2);
    dialog.show();
  }
  
  protected View initContentView(Context paramContext) {
    View view = View.inflate(this.mContext, 2130903122, null);
    this.mRadioGroup = (RadioGroup)view.findViewById(2131756558);
    this.mConstant = (RadioButton)view.findViewById(2131756559);
    this.mCustomize = (RadioButton)view.findViewById(2131756560);
    this.mCustomize_1 = (RadioButton)view.findViewById(2131756561);
    this.relativeLayout = (RelativeLayout)view.findViewById(2131756555);
    this.mSpeed = (TextView)view.findViewById(2131756557);
    this.imageView = (ImageView)view.findViewById(2131756556);
    return view;
  }
  
  public void initData(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield isContrall : Z
    //   4: ifeq -> 360
    //   7: aload_0
    //   8: getstatic app/gamer/quadstellar/App.macDress : Ljava/lang/String;
    //   11: putfield macAddress : Ljava/lang/String;
    //   14: aload_0
    //   15: getfield mActivity : Landroid/app/Activity;
    //   18: new java/lang/StringBuilder
    //   21: dup
    //   22: invokespecial <init> : ()V
    //   25: aload_0
    //   26: getfield macAddress : Ljava/lang/String;
    //   29: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   32: aload_0
    //   33: getfield mCustomize : Landroid/widget/RadioButton;
    //   36: invokevirtual getId : ()I
    //   39: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   42: invokevirtual toString : ()Ljava/lang/String;
    //   45: invokestatic getData : (Landroid/content/Context;Ljava/lang/String;)Ljava/lang/String;
    //   48: astore_2
    //   49: aload_0
    //   50: getfield mActivity : Landroid/app/Activity;
    //   53: new java/lang/StringBuilder
    //   56: dup
    //   57: invokespecial <init> : ()V
    //   60: aload_0
    //   61: getfield macAddress : Ljava/lang/String;
    //   64: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: aload_0
    //   68: getfield mCustomize_1 : Landroid/widget/RadioButton;
    //   71: invokevirtual getId : ()I
    //   74: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   77: invokevirtual toString : ()Ljava/lang/String;
    //   80: invokestatic getData : (Landroid/content/Context;Ljava/lang/String;)Ljava/lang/String;
    //   83: astore_3
    //   84: aload_3
    //   85: ifnull -> 96
    //   88: aload_0
    //   89: getfield mCustomize_1 : Landroid/widget/RadioButton;
    //   92: aload_3
    //   93: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   96: aload_2
    //   97: ifnull -> 108
    //   100: aload_0
    //   101: getfield mCustomize : Landroid/widget/RadioButton;
    //   104: aload_2
    //   105: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   108: aload_0
    //   109: getfield isContrall : Z
    //   112: ifeq -> 132
    //   115: aload_0
    //   116: aload_0
    //   117: getfield mContext : Landroid/content/Context;
    //   120: invokestatic getInstance : (Landroid/content/Context;)Lapp/gamer/quadstellar/db/DeviceInfoDB;
    //   123: getstatic app/gamer/quadstellar/App.macDress : Ljava/lang/String;
    //   126: invokevirtual queryUserList : (Ljava/lang/String;)Lapp/gamer/quadstellar/domain/DeviceInfo;
    //   129: putfield info : Lapp/gamer/quadstellar/domain/DeviceInfo;
    //   132: aload_0
    //   133: invokestatic getInstance : ()Lapp/gamer/quadstellar/App;
    //   136: invokevirtual getCurrentDevice : ()Lapp/gamer/quadstellar/domain/FatherDeviceInfo;
    //   139: putfield mDevice : Lapp/gamer/quadstellar/domain/FatherDeviceInfo;
    //   142: aload_0
    //   143: getfield mConstant : Landroid/widget/RadioButton;
    //   146: aload_0
    //   147: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   150: aload_0
    //   151: getfield mCustomize : Landroid/widget/RadioButton;
    //   154: aload_0
    //   155: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   158: aload_0
    //   159: getfield mCustomize_1 : Landroid/widget/RadioButton;
    //   162: aload_0
    //   163: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   166: aload_0
    //   167: getfield mCustomize : Landroid/widget/RadioButton;
    //   170: aload_0
    //   171: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   174: aload_0
    //   175: getfield mCustomize_1 : Landroid/widget/RadioButton;
    //   178: aload_0
    //   179: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   182: ldc_w 'fanSelectPostion'
    //   185: invokestatic readInt : (Ljava/lang/String;)I
    //   188: istore #4
    //   190: aconst_null
    //   191: astore_2
    //   192: iconst_0
    //   193: istore #5
    //   195: iconst_0
    //   196: istore_1
    //   197: iload #4
    //   199: tableswitch default -> 216, 0 -> 373
    //   216: iload_1
    //   217: istore #6
    //   219: iload #5
    //   221: istore #7
    //   223: invokestatic getPhonenumberBytes : ()[B
    //   226: astore #8
    //   228: aload_0
    //   229: getfield isContrall : Z
    //   232: ifeq -> 415
    //   235: aload_0
    //   236: getfield mActivity : Landroid/app/Activity;
    //   239: invokestatic getInstance : (Landroid/content/Context;)Lapp/gamer/quadstellar/db/DeviceInfoDB;
    //   242: getstatic app/gamer/quadstellar/App.macDress : Ljava/lang/String;
    //   245: invokevirtual queryUserList : (Ljava/lang/String;)Lapp/gamer/quadstellar/domain/DeviceInfo;
    //   248: astore_3
    //   249: bipush #50
    //   251: bipush #7
    //   253: anewarray [B
    //   256: dup
    //   257: iconst_0
    //   258: getstatic app/gamer/quadstellar/Prefix.CONTROLLER_FAN : [B
    //   261: aastore
    //   262: dup
    //   263: iconst_1
    //   264: invokestatic currentTimeMillis : ()J
    //   267: invokestatic valueOf : (J)Ljava/lang/Long;
    //   270: invokestatic getSystemTimeBytes : (Ljava/lang/Long;)[B
    //   273: aastore
    //   274: dup
    //   275: iconst_2
    //   276: aload #8
    //   278: aastore
    //   279: dup
    //   280: iconst_3
    //   281: aload_3
    //   282: invokevirtual getMacDrass : ()Ljava/lang/String;
    //   285: invokestatic hexStringToBytes : (Ljava/lang/String;)[B
    //   288: aastore
    //   289: dup
    //   290: iconst_4
    //   291: aload_2
    //   292: aastore
    //   293: dup
    //   294: iconst_5
    //   295: iconst_1
    //   296: newarray byte
    //   298: dup
    //   299: iconst_0
    //   300: iload #7
    //   302: bastore
    //   303: aastore
    //   304: dup
    //   305: bipush #6
    //   307: iconst_1
    //   308: newarray byte
    //   310: dup
    //   311: iconst_0
    //   312: iload #6
    //   314: bastore
    //   315: aastore
    //   316: invokestatic append : (I[[B)[B
    //   319: astore_2
    //   320: aload_0
    //   321: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/UdpClient;
    //   324: putfield udpClient : Lapp/gamer/quadstellar/net/UdpClient;
    //   327: aload_0
    //   328: getfield udpClient : Lapp/gamer/quadstellar/net/UdpClient;
    //   331: invokevirtual connectSocket : ()Z
    //   334: pop
    //   335: aload_3
    //   336: ifnull -> 355
    //   339: aload_0
    //   340: getfield udpClient : Lapp/gamer/quadstellar/net/UdpClient;
    //   343: sipush #8063
    //   346: aload_3
    //   347: invokevirtual getDeviceIP : ()Ljava/lang/String;
    //   350: aload_2
    //   351: aconst_null
    //   352: invokevirtual sendData : (ILjava/lang/String;[BLapp/gamer/quadstellar/net/UdpClient$ReceiveCallback;)V
    //   355: aload_0
    //   356: invokespecial initAnimation : ()V
    //   359: return
    //   360: aload_0
    //   361: invokestatic getControlDevices : ()Lapp/gamer/quadstellar/mode/ControlDevice;
    //   364: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   367: putfield macAddress : Ljava/lang/String;
    //   370: goto -> 14
    //   373: aload_0
    //   374: getfield mRadioGroup : Landroid/widget/RadioGroup;
    //   377: ldc_w 2131756559
    //   380: invokevirtual check : (I)V
    //   383: iconst_3
    //   384: invokestatic int2OneByte : (I)[B
    //   387: astore_2
    //   388: ldc_w 'speedData'
    //   391: invokestatic readInt : (Ljava/lang/String;)I
    //   394: istore #5
    //   396: iload #5
    //   398: i2b
    //   399: istore_1
    //   400: iload #5
    //   402: i2b
    //   403: istore #5
    //   405: iload_1
    //   406: istore #7
    //   408: iload #5
    //   410: istore #6
    //   412: goto -> 223
    //   415: invokestatic getInstance : ()Lapp/gamer/quadstellar/App;
    //   418: invokevirtual getCurrentDevice : ()Lapp/gamer/quadstellar/domain/FatherDeviceInfo;
    //   421: astore_3
    //   422: aload_3
    //   423: ifnull -> 359
    //   426: getstatic app/gamer/quadstellar/Prefix.CONTROLLER_FAN : [B
    //   429: astore #9
    //   431: invokestatic currentTimeMillis : ()J
    //   434: invokestatic valueOf : (J)Ljava/lang/Long;
    //   437: invokestatic getSystemTimeBytes : (Ljava/lang/Long;)[B
    //   440: astore #10
    //   442: aload_3
    //   443: invokevirtual getMacAdrass : ()Ljava/lang/String;
    //   446: invokestatic hexStringToBytes : (Ljava/lang/String;)[B
    //   449: astore #11
    //   451: iconst_1
    //   452: newarray byte
    //   454: dup
    //   455: iconst_0
    //   456: iload #6
    //   458: bastore
    //   459: astore #12
    //   461: bipush #50
    //   463: bipush #7
    //   465: anewarray [B
    //   468: dup
    //   469: iconst_0
    //   470: aload #9
    //   472: aastore
    //   473: dup
    //   474: iconst_1
    //   475: aload #10
    //   477: aastore
    //   478: dup
    //   479: iconst_2
    //   480: aload #8
    //   482: aastore
    //   483: dup
    //   484: iconst_3
    //   485: aload #11
    //   487: aastore
    //   488: dup
    //   489: iconst_4
    //   490: aload_2
    //   491: aastore
    //   492: dup
    //   493: iconst_5
    //   494: iconst_1
    //   495: newarray byte
    //   497: dup
    //   498: iconst_0
    //   499: iload #7
    //   501: bastore
    //   502: aastore
    //   503: dup
    //   504: bipush #6
    //   506: aload #12
    //   508: aastore
    //   509: invokestatic append : (I[[B)[B
    //   512: astore_2
    //   513: aload_0
    //   514: getfield mActivity : Landroid/app/Activity;
    //   517: invokestatic getInstance : (Landroid/content/Context;)Lapp/gamer/quadstellar/db/DeviceInfoDB;
    //   520: aload_3
    //   521: invokevirtual getMacAdrass : ()Ljava/lang/String;
    //   524: invokevirtual queryUserList : (Ljava/lang/String;)Lapp/gamer/quadstellar/domain/DeviceInfo;
    //   527: astore_3
    //   528: goto -> 320
  }
  
  public boolean isFastDoubleClick() {
    long l = System.currentTimeMillis() - this.lastClickTime;
    return (0L < l && l < 200L);
  }
  
  public void onClick(View paramView) {
    switch (paramView.getId()) {
      default:
        return;
      case 2131756559:
        PreferenceHelper.write("fanSelectPostion", 0);
        getmFanNum();
      case 2131756560:
        PreferenceHelper.write("fanSelectPostion", 1);
        getTimePicker(this.CUSTOMIZE_1);
      case 2131756561:
        break;
    } 
    PreferenceHelper.write("fanSelectPostion", 2);
    getTimePicker(this.CUSTOMIZE_2);
  }
  
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(LightFanEvent paramLightFanEvent) {
    byte b1 = paramLightFanEvent.getLowSpeed();
    byte b2 = paramLightFanEvent.getMaxSpeed();
    Log.d("Controller_Fan_Customiz", "event:****************" + this.getmFan_now);
    if (this.mRadioGroup.getCheckedRadioButtonId() == 2131756559) {
      String str1 = this.mActivity.getString(2131296344) + " \n" + b1 + "00\nRPM";
      this.mSpeed.setText(str1);
      return;
    } 
    String str = this.mActivity.getString(2131296344) + "\n" + b1 + "00-" + b2 + "00\nRPM";
    this.mSpeed.setText(str);
  }
  
  public boolean onLongClick(View paramView) {
    switch (paramView.getId()) {
      default:
        return false;
      case 2131756560:
        showReNameDialog((RadioButton)paramView);
      case 2131756561:
        break;
    } 
    showReNameDialog((RadioButton)paramView);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Controller_Fan_Customize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */