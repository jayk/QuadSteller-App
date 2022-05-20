package app.gamer.quadstellar.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import app.esptouch.EsptouchTask;
import app.esptouch.IEsptouchListener;
import app.esptouch.IEsptouchResult;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.Config;
import app.gamer.quadstellar.Prefix;
import app.gamer.quadstellar.adapter.PopListAdapter;
import app.gamer.quadstellar.callback.DestroyCallback;
import app.gamer.quadstellar.dao.WifiMessageDao;
import app.gamer.quadstellar.db.FanDeviceInfoDB;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.db.LightDeviceInfoDB;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.domain.VersionUpgrade;
import app.gamer.quadstellar.domain.WifiMessageInfo;
import app.gamer.quadstellar.mode.ControlDevice;
import app.gamer.quadstellar.mode.EFDeviceLight;
import app.gamer.quadstellar.net.DeviceManager;
import app.gamer.quadstellar.net.ThreadManger;
import app.gamer.quadstellar.net.UdpClient;
import app.gamer.quadstellar.newdevices.activity.About_Activity;
import app.gamer.quadstellar.newdevices.activity.ControlerActicity;
import app.gamer.quadstellar.newdevices.activity.Help_Activity;
import app.gamer.quadstellar.observer.Observer;
import app.gamer.quadstellar.observer.Subject;
import app.gamer.quadstellar.ui.BaseActivity;
import app.gamer.quadstellar.ui.BaseFragment;
import app.gamer.quadstellar.ui.activity.ControlDeviceActivity;
import app.gamer.quadstellar.ui.widget.CustomDialog;
import app.gamer.quadstellar.ui.widget.SetingDialog;
import app.gamer.quadstellar.utils.ByteUtils;
import app.gamer.quadstellar.utils.LogUtil;
import app.gamer.quadstellar.utils.PreferenceHelper;
import app.gamer.quadstellar.utils.StringTools;
import app.gamer.quadstellar.utils.XlinkUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

public class HomeFragment extends BaseFragment implements Handler.Callback, Observer, DestroyCallback {
  private static final String TAG = "HomeFragment";
  
  private int ScanCount = 0;
  
  private TextView contentText;
  
  private int count = 30;
  
  int deviceCount = 0;
  
  int deviceSize;
  
  private List<FatherDeviceInfo> devices;
  
  private List<FatherDeviceInfo> devices1;
  
  private Dialog dialog;
  
  private List<String> diviceCountList = new ArrayList<String>();
  
  private int height;
  
  private int index;
  
  boolean isConfWifi = false;
  
  private boolean isMain = false;
  
  private String isSsidHiddenStr = "NO";
  
  private boolean isStop = false;
  
  private Boolean isThreadDisable = Boolean.valueOf(false);
  
  private ListView listView;
  
  private EsptouchTask mEsptouchTask;
  
  private Handler mHandler;
  
  private List<EFDeviceLight> mLightList;
  
  private PopListAdapter mPopAdapter;
  
  private PopupWindow mPopWin;
  
  private ProgressDialog mProgressDialog;
  
  private SetingDialog mScanningDiaog;
  
  private SeekBar mSeekBar;
  
  private Dialog mUpdataDialog;
  
  private WifiManager mWifiManager;
  
  private String mac;
  
  private String macDress;
  
  private ArrayList<String> msgList = new ArrayList<String>();
  
  private IEsptouchListener myListener = new IEsptouchListener() {
      public void onEsptouchResultAdded(IEsptouchResult param1IEsptouchResult) {
        HomeFragment.this.onEsptoucResultAddedPerform(param1IEsptouchResult);
      }
    };
  
  private TextView progressText;
  
  private String psw;
  
  private BroadcastReceiver receiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        if (param1Intent.getAction().equals(Config.BROADCAST_RECVPIPE_SCAN)) {
          String str = param1Intent.getStringExtra("mac");
          if (!HomeFragment.this.diviceCountList.contains(str))
            HomeFragment.this.diviceCountList.add(str); 
          HomeFragment.this.wifiNamberText.setText(HomeFragment.this.diviceCountList.size() + HomeFragment.this.getString(2131296611) + HomeFragment.this.diviceCountList.size() + HomeFragment.this.getString(2131296612));
          HomeFragment.this.wifi_number.setText(HomeFragment.this.getString(2131296280) + HomeFragment.this.diviceCountList.size() + HomeFragment.this.getString(2131296611) + HomeFragment.this.diviceCountList.size() + HomeFragment.this.getString(2131296612));
        } 
        int i = param1Intent.getIntExtra("request_code_key", -1);
        byte[] arrayOfByte = param1Intent.getByteArrayExtra("data");
        if (arrayOfByte != null) {
          if (arrayOfByte[0] == -1)
            HomeFragment.access$902(HomeFragment.this, ByteUtils.bytesToHexString(ByteUtils.arrayCopyBytes(arrayOfByte, 2, 6))); 
          if (arrayOfByte[0] == 15 && arrayOfByte[2] == 100)
            if (ByteUtils.bytesToHexString(ByteUtils.arrayCopyBytes(arrayOfByte, 7, 16)).trim().toLowerCase().equals(App.uniqueId)) {
              String str = ByteUtils.bytesToHexString(ByteUtils.arrayCopyBytes(arrayOfByte, 27, 6));
              HomeFragment.this.diviceCountList.remove(str);
              FatherDeviceInfoDB.getInstance().deleteUser(str);
              FanDeviceInfoDB.getInstance().deleteUser(str);
              LightDeviceInfoDB.getInstance().deleteUser(str);
              FatherDeviceInfo fatherDeviceInfo = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
              int j = FatherDeviceInfoDB.getInstance().queryUserList().size();
              if (fatherDeviceInfo == null) {
                HomeFragment.this.wifi_number.setText(HomeFragment.this.getString(2131296280) + j + HomeFragment.this.getString(2131296611));
              } else {
                HomeFragment.this.wifi_number.setText(HomeFragment.this.getString(2131296280) + (j - 1) + HomeFragment.this.getString(2131296611));
              } 
              if (j == 2 && fatherDeviceInfo != null) {
                FatherDeviceInfoDB.getInstance().deleteUser(App.macDress);
                FanDeviceInfoDB.getInstance().deleteUser(App.macDress);
                LightDeviceInfoDB.getInstance().deleteUser(App.macDress);
              } 
            } else {
              return;
            }  
          if (i == 7052) {
            Log.d("resultcodeidwhat", ByteUtils.chaeckMatchResult(arrayOfByte) + "" + arrayOfByte);
            i = arrayOfByte[32];
            if (ByteUtils.bytesToHexString(ByteUtils.arrayCopyBytes(arrayOfByte, 7, 16)).trim().toLowerCase().equals(App.uniqueId)) {
              int j = arrayOfByte[33] & 0xFF;
              if (i == 3 || (j >= 99 && j <= 100)) {
                if (HomeFragment.this.updataCompte) {
                  HomeFragment.access$1002(HomeFragment.this, false);
                  HomeFragment.this.mProgressDialog.dismiss();
                  XlinkUtils.shortTips(HomeFragment.this.getString(2131297532));
                } 
                return;
              } 
              if (i == 4) {
                HomeFragment.this.mProgressDialog.dismiss();
                XlinkUtils.shortTips(HomeFragment.this.getString(2131297530));
                return;
              } 
              if (i == 5) {
                HomeFragment.this.mProgressDialog.dismiss();
                XlinkUtils.shortTips(HomeFragment.this.getString(2131297550));
                return;
              } 
              if (j >= 0 && j < 99) {
                HomeFragment.this.mProgressDialog.setProgress(j);
                return;
              } 
              if (j == 101 && HomeFragment.this.mProgressDialog != null)
                HomeFragment.this.mProgressDialog.dismiss(); 
            } 
          } 
        } 
      }
    };
  
  private RelativeLayout rl_main_view;
  
  private RelativeLayout rl_mohu;
  
  private CustomDialog showView;
  
  private boolean stopTag;
  
  private Thread thread;
  
  private Timer timer;
  
  private TextView tv_mohu;
  
  private TextView tv_setting;
  
  private UdpClient udpClient;
  
  private TextView upDataProgressText;
  
  private boolean updataCompte;
  
  private SeekBar updataSeekBar;
  
  private int width;
  
  private WifiMessageDao wifiMessageDao;
  
  private TextView wifiNamberText;
  
  @ViewInject(2131756902)
  private TextView wifi_number;
  
  private void _showPop(View paramView) {
    if (this.mPopWin == null) {
      setPopList();
      this.listView = new ListView((Context)this.mActivity);
      this.mPopAdapter = new PopListAdapter(this.mActivity);
      this.listView.setVerticalScrollBarEnabled(false);
      this.mPopAdapter.setList(this.msgList);
      this.listView.setAdapter((ListAdapter)this.mPopAdapter);
      this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
              if (HomeFragment.this.mPopWin != null)
                HomeFragment.this.mPopWin.dismiss(); 
              switch (param1Int) {
                default:
                  return;
                case 0:
                  HomeFragment.this.showDialog();
                case 1:
                  HomeFragment.this.startActivity(new Intent((Context)HomeFragment.this.mActivity, About_Activity.class));
                case 2:
                  HomeFragment.this.startActivity(new Intent((Context)HomeFragment.this.mActivity, Help_Activity.class));
                case 3:
                  break;
              } 
              final SetingDialog homeDialog = new SetingDialog((Context)HomeFragment.this.mActivity, 2131427778);
              param1View = View.inflate((Context)HomeFragment.this.mActivity, 2130903163, null);
              Button button1 = (Button)param1View.findViewById(2131756661);
              Button button2 = (Button)param1View.findViewById(2131756662);
              ((TextView)param1View.findViewById(2131756698)).setText(2131296617);
              button2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View param2View) {
                      homeDialog.dismiss();
                    }
                  });
              button1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View param2View) {
                      homeDialog.dismiss();
                      for (FatherDeviceInfo fatherDeviceInfo : FatherDeviceInfoDB.getInstance().queryUserList()) {
                        if (!fatherDeviceInfo.getMacAdrass().equals(App.macDress)) {
                          SystemClock.sleep(10L);
                          byte[] arrayOfByte = ByteUtils.appendAuto(new byte[][] { Prefix.RESTORE_FACTORY, ByteUtils.getSystemTimeBytes(Long.valueOf(System.currentTimeMillis())), ByteUtils.getPhonenumberBytes(), ByteUtils.hexStringToBytes(fatherDeviceInfo.getMacAdrass()) });
                          UdpClient.getInstance().sendData(7049, fatherDeviceInfo.getDeviceIP(), arrayOfByte, null);
                        } 
                      } 
                    }
                  });
              setingDialog.setContentView(param1View);
              setingDialog.getWindow().setLayout(HomeFragment.this.width, -2);
              setingDialog.show();
            }
          });
      this.mPopWin = new PopupWindow((Context)this.mActivity);
      this.mPopWin.setBackgroundDrawable((Drawable)new ColorDrawable(getResources().getColor(2131624072)));
      this.mPopWin.setWidth(getResources().getDimensionPixelSize(2131361948));
      this.mPopWin.setHeight(-2);
      this.mPopWin.setContentView((View)this.listView);
      this.mPopWin.setOutsideTouchable(true);
      this.mPopWin.showAsDropDown(paramView, 0, 0);
      this.mPopWin.update();
      return;
    } 
    if (this.mPopAdapter != null) {
      this.msgList.clear();
      setPopList();
      this.mPopAdapter.notifyDataSetChanged();
    } 
    this.mPopWin.showAsDropDown(paramView, 0, 0);
  }
  
  private void confWifi() {
    this.mHandler.sendEmptyMessage(3);
    this.isThreadDisable = Boolean.valueOf(false);
    this.mHandler.sendEmptyMessage(1);
  }
  
  private void esptouch(String paramString1, String paramString2) {
    String str1 = getWifiConnectedBssid();
    String str2 = Integer.toString(-1);
    if (Boolean.valueOf(false).booleanValue())
      this.isSsidHiddenStr = "YES"; 
    Log.d("HomeFragment", "mBtnConfirm is clicked, mEdtApSsid = " + paramString1 + ",  mEdtApPassword = " + paramString2 + "APBSSID" + str1 + "isSsidHiddenStr" + this.isSsidHiddenStr + "taskResultCountStr" + str2);
    ThreadManger.execute(new EsptouchAsyncTasks(paramString1, str1, paramString2, this.isSsidHiddenStr, str2));
  }
  
  private int getAndroidSDKVersion() {
    int i = 0;
    try {
      int j = Integer.valueOf(Build.VERSION.SDK_INT).intValue();
      i = j;
    } catch (NumberFormatException numberFormatException) {
      Log.e(numberFormatException.toString(), numberFormatException.getMessage());
    } 
    return i;
  }
  
  private WifiInfo getConnectionInfo() {
    return this.mWifiManager.getConnectionInfo();
  }
  
  private void getUpDataUrl(final ControlDevice device) {
    ThreadManger.execute(new Runnable() {
          public void run() {
            // Byte code:
            //   0: aconst_null
            //   1: astore_1
            //   2: new org/apache/http/client/methods/HttpGet
            //   5: astore_2
            //   6: aload_2
            //   7: ldc 'http://www.lingansmart.cn/ESP/FAN/version.txt'
            //   9: invokespecial <init> : (Ljava/lang/String;)V
            //   12: new org/apache/http/impl/client/DefaultHttpClient
            //   15: astore_3
            //   16: aload_3
            //   17: invokespecial <init> : ()V
            //   20: aload_3
            //   21: aload_2
            //   22: invokevirtual execute : (Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;
            //   25: astore_2
            //   26: aload_1
            //   27: astore_3
            //   28: aload_2
            //   29: invokeinterface getStatusLine : ()Lorg/apache/http/StatusLine;
            //   34: invokeinterface getStatusCode : ()I
            //   39: sipush #200
            //   42: if_icmpne -> 58
            //   45: aload_2
            //   46: invokeinterface getEntity : ()Lorg/apache/http/HttpEntity;
            //   51: invokestatic toString : (Lorg/apache/http/HttpEntity;)Ljava/lang/String;
            //   54: invokevirtual trim : ()Ljava/lang/String;
            //   57: astore_3
            //   58: ldc 'HomeFragment'
            //   60: new java/lang/StringBuilder
            //   63: dup
            //   64: invokespecial <init> : ()V
            //   67: ldc 'doInBackground: '
            //   69: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   72: aload_3
            //   73: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   76: invokevirtual toString : ()Ljava/lang/String;
            //   79: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
            //   82: pop
            //   83: aload_3
            //   84: ldc '\.'
            //   86: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
            //   89: astore_1
            //   90: aload_1
            //   91: arraylength
            //   92: newarray byte
            //   94: astore_3
            //   95: iconst_0
            //   96: istore #4
            //   98: iload #4
            //   100: aload_1
            //   101: arraylength
            //   102: if_icmpge -> 173
            //   105: aload_3
            //   106: iload #4
            //   108: aload_1
            //   109: iload #4
            //   111: aaload
            //   112: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Byte;
            //   115: invokevirtual byteValue : ()B
            //   118: bastore
            //   119: ldc 'HomeFragment'
            //   121: new java/lang/StringBuilder
            //   124: dup
            //   125: invokespecial <init> : ()V
            //   128: ldc 'verBytes[i]:'
            //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   133: aload_3
            //   134: iload #4
            //   136: baload
            //   137: invokevirtual append : (I)Ljava/lang/StringBuilder;
            //   140: invokevirtual toString : ()Ljava/lang/String;
            //   143: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
            //   146: pop
            //   147: iinc #4, 1
            //   150: goto -> 98
            //   153: astore_3
            //   154: aload_3
            //   155: invokevirtual printStackTrace : ()V
            //   158: aload_1
            //   159: astore_3
            //   160: goto -> 58
            //   163: astore_3
            //   164: aload_3
            //   165: invokevirtual printStackTrace : ()V
            //   168: aload_1
            //   169: astore_3
            //   170: goto -> 58
            //   173: invokestatic getPhonenumberBytes : ()[B
            //   176: astore_1
            //   177: bipush #50
            //   179: iconst_5
            //   180: anewarray [B
            //   183: dup
            //   184: iconst_0
            //   185: getstatic app/gamer/quadstellar/Prefix.WIFI_FAN : [B
            //   188: aastore
            //   189: dup
            //   190: iconst_1
            //   191: invokestatic currentTimeMillis : ()J
            //   194: invokestatic valueOf : (J)Ljava/lang/Long;
            //   197: invokestatic getSystemTimeBytes : (Ljava/lang/Long;)[B
            //   200: aastore
            //   201: dup
            //   202: iconst_2
            //   203: aload_1
            //   204: aastore
            //   205: dup
            //   206: iconst_3
            //   207: aload_0
            //   208: getfield val$device : Lapp/gamer/quadstellar/mode/ControlDevice;
            //   211: invokevirtual getMacAddress : ()Ljava/lang/String;
            //   214: invokestatic hexStringToBytes : (Ljava/lang/String;)[B
            //   217: aastore
            //   218: dup
            //   219: iconst_4
            //   220: aload_3
            //   221: aastore
            //   222: invokestatic append : (I[[B)[B
            //   225: astore_1
            //   226: aload_0
            //   227: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
            //   230: invokestatic getInstance : ()Lapp/gamer/quadstellar/net/UdpClient;
            //   233: invokestatic access$402 : (Lapp/gamer/quadstellar/ui/fragment/HomeFragment;Lapp/gamer/quadstellar/net/UdpClient;)Lapp/gamer/quadstellar/net/UdpClient;
            //   236: pop
            //   237: aload_0
            //   238: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
            //   241: invokestatic access$400 : (Lapp/gamer/quadstellar/ui/fragment/HomeFragment;)Lapp/gamer/quadstellar/net/UdpClient;
            //   244: invokevirtual connectSocket : ()Z
            //   247: pop
            //   248: invokestatic getInstance : ()Lapp/gamer/quadstellar/db/FatherDeviceInfoDB;
            //   251: aload_0
            //   252: getfield val$device : Lapp/gamer/quadstellar/mode/ControlDevice;
            //   255: invokevirtual getMacAddress : ()Ljava/lang/String;
            //   258: invokevirtual queryUserList : (Ljava/lang/String;)Lapp/gamer/quadstellar/domain/FatherDeviceInfo;
            //   261: astore_3
            //   262: aload_3
            //   263: ifnull -> 309
            //   266: aload_0
            //   267: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
            //   270: invokestatic access$400 : (Lapp/gamer/quadstellar/ui/fragment/HomeFragment;)Lapp/gamer/quadstellar/net/UdpClient;
            //   273: sipush #7052
            //   276: aload_3
            //   277: invokevirtual getDeviceIP : ()Ljava/lang/String;
            //   280: aload_1
            //   281: aconst_null
            //   282: invokevirtual sendData : (ILjava/lang/String;[BLapp/gamer/quadstellar/net/UdpClient$ReceiveCallback;)V
            //   285: aload_0
            //   286: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
            //   289: iconst_1
            //   290: invokestatic access$1002 : (Lapp/gamer/quadstellar/ui/fragment/HomeFragment;Z)Z
            //   293: pop
            //   294: aload_0
            //   295: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
            //   298: aload_0
            //   299: getfield val$device : Lapp/gamer/quadstellar/mode/ControlDevice;
            //   302: invokevirtual getMacAddress : ()Ljava/lang/String;
            //   305: invokestatic access$1202 : (Lapp/gamer/quadstellar/ui/fragment/HomeFragment;Ljava/lang/String;)Ljava/lang/String;
            //   308: pop
            //   309: return
            // Exception table:
            //   from	to	target	type
            //   2	26	153	org/apache/http/client/ClientProtocolException
            //   2	26	163	java/io/IOException
            //   28	58	153	org/apache/http/client/ClientProtocolException
            //   28	58	163	java/io/IOException
          }
        });
  }
  
  private NetworkInfo getWifiNetworkInfo() {
    return ((ConnectivityManager)getActivity().getSystemService("connectivity")).getNetworkInfo(1);
  }
  
  private void initReceiver() {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(Config.BROADCAST_RECVPIPE);
    intentFilter.addAction(Config.BROADCAST_RECVPIPE_SCAN);
    this.mActivity.registerReceiver(this.receiver, intentFilter);
  }
  
  private boolean isWifiConnected() {
    NetworkInfo networkInfo = getWifiNetworkInfo();
    boolean bool = false;
    if (networkInfo != null)
      bool = networkInfo.isConnected(); 
    return bool;
  }
  
  private void mAddWifi() {
    if (!this.mWifiManager.isWifiEnabled()) {
      XlinkUtils.shortTips(getString(2131296523));
      return;
    } 
    final Dialog scannDialog = new Dialog((Context)this.mActivity, 2131427778);
    new ArrayList();
    new HashMap<Object, Object>();
    View view = View.inflate((Context)this.mActivity, 2130903152, null);
    new ListView((Context)this.mActivity);
    dialog.setContentView(view);
    Button button1 = (Button)view.findViewById(2131756661);
    Button button2 = (Button)view.findViewById(2131756662);
    final EditText wifiName = (EditText)view.findViewById(2131756659);
    final EditText wifiPassword = (EditText)view.findViewById(2131756649);
    final ImageButton password_visibility = (ImageButton)view.findViewById(2131756660);
    imageButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            int i;
            boolean bool;
            ImageButton imageButton = password_visibility;
            if (ischeked[0]) {
              i = 2130838118;
            } else {
              i = 2130838105;
            } 
            imageButton.setImageResource(i);
            EditText editText = wifiPassword;
            if (ischeked[0]) {
              i = 145;
            } else {
              i = 129;
            } 
            editText.setInputType(i);
            boolean[] arrayOfBoolean = ischeked;
            if (ischeked[0]) {
              bool = false;
            } else {
              bool = true;
            } 
            arrayOfBoolean[0] = bool;
          }
        });
    try {
      if (this.mWifiManager.isWifiEnabled()) {
        String str1;
        String str2;
        WifiInfo wifiInfo = this.mWifiManager.getConnectionInfo();
        imageButton = null;
        if (wifiInfo != null) {
          str2 = wifiInfo.getSSID();
          str1 = str2;
          if (getAndroidSDKVersion() > 16) {
            str1 = str2;
            if (str2.startsWith("\"")) {
              str1 = str2;
              if (str2.endsWith("\""))
                str1 = str2.substring(1, str2.length() - 1); 
            } 
          } 
        } 
        Log.d("HomeFragment", str1);
        if (str1 == null) {
          str2 = "null";
        } else {
          str2 = str1;
        } 
        editText1.setText(str2);
        editText2.setText(this.wifiMessageDao.queryType(str1));
      } else {
        XlinkUtils.shortTips(getString(2131296893));
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    button1.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            HomeFragment.access$2302(HomeFragment.this, false);
            HomeFragment.access$2402(HomeFragment.this, wifiPassword.getText().toString().trim());
            if (HomeFragment.this.psw.isEmpty()) {
              XlinkUtils.shortTips(HomeFragment.this.getString(2131296653));
              return;
            } 
            if (HomeFragment.this.psw.length() < 8) {
              final SetingDialog homeDialog = new SetingDialog((Context)HomeFragment.this.mActivity, 2131427778);
              View view = View.inflate((Context)HomeFragment.this.mActivity, 2130903178, null);
              Button button1 = (Button)view.findViewById(2131756661);
              TextView textView2 = (TextView)view.findViewById(2131756663);
              TextView textView1 = (TextView)view.findViewById(2131756721);
              textView2.setText(HomeFragment.this.getString(2131296298));
              textView1.setText(HomeFragment.this.getString(2131296645));
              button1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View param2View) {
                      homeDialog.dismiss();
                    }
                  });
              setingDialog.setContentView(view);
              setingDialog.getWindow().setLayout(HomeFragment.this.width, -2);
              setingDialog.show();
              return;
            } 
            HomeFragment.access$2702(HomeFragment.this, new SetingDialog((Context)HomeFragment.this.mActivity, 2131427778));
            param1View = View.inflate((Context)HomeFragment.this.mActivity, 2130903153, null);
            HomeFragment.access$3002(HomeFragment.this, (TextView)param1View.findViewById(2131756664));
            HomeFragment.access$702(HomeFragment.this, (TextView)param1View.findViewById(2131756666));
            Button button = (Button)param1View.findViewById(2131756662);
            HomeFragment.access$3102(HomeFragment.this, (SeekBar)param1View.findViewById(2131756665));
            HomeFragment.this.mScanningDiaog.setContentView(param1View);
            HomeFragment.this.mScanningDiaog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                  public void onDismiss(DialogInterface param2DialogInterface) {
                    HomeFragment.this.tv_mohu.setText(2131296306);
                    if (HomeFragment.this.diviceCountList.size() > 0) {
                      PreferenceHelper.write("app.gamer.quadstellar_isFist", false);
                      HomeFragment.this.rl_mohu.setVisibility(8);
                      HomeFragment.this.rl_main_view.setVisibility(0);
                      HomeFragment.this.tv_setting.setVisibility(0);
                    } 
                    HomeFragment.this.diviceCountList.clear();
                    HomeFragment.this.deviceCount = 0;
                    HomeFragment.access$3602(HomeFragment.this, 30);
                    HomeFragment.access$3702(HomeFragment.this, 0);
                    HomeFragment.access$502(HomeFragment.this, true);
                    HomeFragment.this.mHandler.removeMessages(5);
                    if (HomeFragment.this.thread != null) {
                      HomeFragment.this.thread.interrupt();
                      HomeFragment.access$3902(HomeFragment.this, (Thread)null);
                    } 
                    HomeFragment.this.mHandler.removeMessages(2);
                    HomeFragment.this.mHandler.sendEmptyMessage(3);
                    HomeFragment.this.mHandler.sendEmptyMessage(0);
                    if (HomeFragment.this.mEsptouchTask != null)
                      HomeFragment.this.mEsptouchTask.interrupt(); 
                  }
                });
            button.setOnClickListener(new View.OnClickListener() {
                  public void onClick(View param2View) {
                    HomeFragment.access$4102(HomeFragment.this, Boolean.valueOf(true));
                    HomeFragment.this.mScanningDiaog.dismiss();
                    HomeFragment.this.mEsptouchTask.interrupt();
                  }
                });
            HomeFragment.this.mScanningDiaog.getWindow().setLayout(HomeFragment.this.width, -2);
            HomeFragment.this.mScanningDiaog.show();
            HomeFragment.this.mScanningDiaog.setCanceledOnTouchOutside(false);
            WifiMessageInfo wifiMessageInfo = new WifiMessageInfo();
            wifiMessageInfo.name = wifiName.getText().toString();
            wifiMessageInfo.passWord = HomeFragment.this.psw;
            if (HomeFragment.this.wifiMessageDao.queryType(wifiMessageInfo.name) == null) {
              HomeFragment.this.wifiMessageDao.insert(wifiMessageInfo);
            } else {
              HomeFragment.this.wifiMessageDao.updata(wifiMessageInfo);
            } 
            scannDialog.dismiss();
            HomeFragment.this.confWifi();
            HomeFragment.this.esptouch(wifiName.getText().toString(), HomeFragment.this.psw);
          }
        });
    button2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            scannDialog.dismiss();
            if (HomeFragment.this.mEsptouchTask != null)
              HomeFragment.this.mEsptouchTask.interrupt(); 
            HomeFragment.this.tv_mohu.setText(2131296306);
          }
        });
    Display display = this.mActivity.getWindowManager().getDefaultDisplay();
    int i = (int)(display.getHeight() * 0.5D);
    i = (int)(display.getWidth() * 0.9D);
    dialog.getWindow().setLayout(i, -2);
    dialog.show();
    dialog.setCanceledOnTouchOutside(false);
    this.tv_mohu.setText(2131296307);
  }
  
  @Event({2131756898})
  private void mAddWifiDevices(View paramView) {
    mAddWifi();
  }
  
  @Event({2131756903})
  private void mContro_All(View paramView) {
    List list = FatherDeviceInfoDB.getInstance().queryUserList();
    Log.d("HomeFragment", "devices.size():" + list.size());
    if (list.size() != 0) {
      Intent intent = new Intent((Context)this.mActivity, ControlerActicity.class);
      intent.putExtra("isContrlALl", true);
      startActivity(intent);
      return;
    } 
    XlinkUtils.shortTips(getString(2131296541));
  }
  
  @Event({2131756904})
  private void mContro_Single(View paramView) {
    showActivityForResult((Activity)this.mActivity, ControlDeviceActivity.class, 8053);
  }
  
  @Event({2131756897})
  private void mSettingOnclick(View paramView) {
    _showPop(paramView);
  }
  
  private void onEsptoucResultAddedPerform(IEsptouchResult paramIEsptouchResult) {
    getActivity().runOnUiThread(new Runnable() {
          public void run() {}
        });
  }
  
  private void scan() {
    Log.e("HomeFragment", "scan: ScanCount  = " + this.ScanCount);
    if (this.ScanCount < 5) {
      this.ScanCount++;
      if (this.ScanCount == 3) {
        byte[] arrayOfByte = ByteUtils.append(50, new byte[][] { Prefix.SCAN_WIFI_DEVICE, ByteUtils.getPhonenumberBytes(), { 74 } });
        this.udpClient.sendData(1, "255.255.255.255", arrayOfByte, null);
      } 
      this.mHandler.sendEmptyMessageDelayed(12, 1000L);
      Log.e("HomeFragment", "scan ====" + this.ScanCount);
      if (90 - this.count * 2 >= 88) {
        this.mSeekBar.setProgress(this.ScanCount * 2 + 90);
        this.progressText.setText((this.ScanCount * 2 + 90) + "%");
      } 
      return;
    } 
    this.ScanCount = 0;
  }
  
  private void scanDevices() {
    this.mHandler.sendEmptyMessage(12);
    this.mHandler.sendEmptyMessageDelayed(11, 5000L);
  }
  
  private void setPopList() {
    this.msgList.add(getString(2131296342));
    this.msgList.add(getString(2131296277));
    this.msgList.add(getString(2131296308));
    this.msgList.add(getString(2131297048));
  }
  
  private void showDialog() {
    (new OkHttpClient()).newCall((new Request.Builder()).url(Config.Check_APP_Updata_Version_Url).build()).enqueue(new Callback() {
          public void onFailure(Call param1Call, IOException param1IOException) {}
          
          public void onResponse(Call param1Call, Response param1Response) throws IOException {
            if (param1Response.code() == 200) {
              String str = param1Response.body().string();
              if (str != null) {
                VersionUpgrade versionUpgrade = (VersionUpgrade)(new Gson()).fromJson(str, VersionUpgrade.class);
                String[] arrayOfString = versionUpgrade.getVersion().substring(1).split("\\.");
                int i = Integer.parseInt(arrayOfString[0]);
                int j = Integer.parseInt(arrayOfString[1]);
                try {
                  arrayOfString = (HomeFragment.this.getActivity().getPackageManager().getPackageInfo(HomeFragment.this.getActivity().getPackageName(), 0)).versionName.substring(1).split("\\.");
                  int k = Integer.parseInt(arrayOfString[0]);
                  int m = Integer.parseInt(arrayOfString[1]);
                  FragmentActivity fragmentActivity = HomeFragment.this.getActivity();
                  Runnable runnable = new Runnable() {
                      public void run() {
                        final SetingDialog renameDialog = new SetingDialog((Context)HomeFragment.this.mActivity, 2131427778);
                        View view = View.inflate((Context)HomeFragment.this.mActivity, 2130903178, null);
                        TextView textView1 = (TextView)view.findViewById(2131756663);
                        TextView textView2 = (TextView)view.findViewById(2131756721);
                        Button button1 = (Button)view.findViewById(2131756661);
                        Button button2 = (Button)view.findViewById(2131756722);
                        if (mub > mub2) {
                          textView1.setText(2131296888);
                          Locale locale = (App.getAppResources().getConfiguration()).locale;
                          if (locale.getCountry().equals("CN") || locale.getCountry().equals("zh")) {
                            textView2.setText(versionUpgrade.getZh() + "\n" + HomeFragment.this.getString(2131296889));
                          } else {
                            textView2.setText(versionUpgrade.getEn() + "\n" + HomeFragment.this.getString(2131296889));
                          } 
                          button2.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View param3View) {
                                  renameDialog.dismiss();
                                }
                              });
                        } else {
                          button2.setVisibility(8);
                        } 
                        button1.setOnClickListener(new View.OnClickListener() {
                              public void onClick(View param3View) {
                                renameDialog.dismiss();
                                if (mub > mub2) {
                                  Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(Config.APP_Updata_Url));
                                  HomeFragment.this.startActivity(intent);
                                } 
                              }
                            });
                        setingDialog.setContentView(view);
                        Display display = HomeFragment.this.mActivity.getWindowManager().getDefaultDisplay();
                        int i = (int)(display.getHeight() * 0.5D);
                        i = (int)(display.getWidth() * 0.7D);
                        setingDialog.getWindow().setLayout(i, -2);
                        setingDialog.setCanceledOnTouchOutside(true);
                        setingDialog.show();
                      }
                    };
                  super(this, i * 10 + j, k * 10 + m, versionUpgrade);
                  fragmentActivity.runOnUiThread(runnable);
                } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
                  nameNotFoundException.printStackTrace();
                } 
              } 
            } 
          }
        });
  }
  
  @Event({2131756905})
  private void toWeb(View paramView) {
    final SetingDialog homeDialog = new SetingDialog((Context)this.mActivity, 2131427778);
    View view = View.inflate((Context)this.mActivity, 2130903163, null);
    Button button = (Button)view.findViewById(2131756661);
    ((Button)view.findViewById(2131756662)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            homeDialog.dismiss();
          }
        });
    button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.gamerstorm.com/"));
            HomeFragment.this.startActivity(intent);
            homeDialog.dismiss();
          }
        });
    setingDialog.setContentView(view);
    setingDialog.getWindow().setLayout(this.width, -2);
    setingDialog.show();
  }
  
  public void destroy() {
    if (this.mHandler != null)
      this.mHandler.removeCallbacksAndMessages(null); 
    if (this.receiver != null && this.mActivity != null)
      this.mActivity.unregisterReceiver(this.receiver); 
  }
  
  public String getWifiConnectedBssid() {
    WifiInfo wifiInfo = getConnectionInfo();
    String str1 = null;
    String str2 = str1;
    if (wifiInfo != null) {
      str2 = str1;
      if (isWifiConnected())
        str2 = wifiInfo.getBSSID(); 
    } 
    return str2;
  }
  
  public boolean handleMessage(Message paramMessage) {
    switch (paramMessage.what) {
      default:
        return false;
      case 1:
        this.mHandler.sendEmptyMessageDelayed(2, 1000L);
        this.mHandler.sendEmptyMessageDelayed(5, 14000L);
      case 2:
        this.count--;
        if (this.count < 1) {
          this.mHandler.sendEmptyMessage(3);
          this.isConfWifi = true;
          this.isThreadDisable = Boolean.valueOf(true);
        } 
        if (this.count % 3 == 0) {
          this.udpClient = UdpClient.getInstance();
          this.udpClient.connectSocket();
          byte[] arrayOfByte = ByteUtils.append(50, new byte[][] { Prefix.SCAN_WIFI_DEVICE, ByteUtils.getPhonenumberBytes(), { 74 } });
          this.udpClient.sendData(1, "255.255.255.255", arrayOfByte, null);
        } 
        this.mSeekBar.setProgress(90 - this.count * 3);
        this.progressText.setText((90 - this.count * 3) + "%");
        this.mHandler.sendEmptyMessageDelayed(2, 1000L);
      case 3:
        if (this.isConfWifi) {
          scanDevices();
          this.isConfWifi = false;
        } 
      case 4:
        this.isStop = true;
      case 5:
        this.thread = new Thread() {
            public void run() {
              super.run();
              int i = (HomeFragment.this.mWifiManager.getDhcpInfo()).ipAddress;
              HomeFragment.access$402(HomeFragment.this, UdpClient.getInstance());
              HomeFragment.this.udpClient.connectSocket();
              byte[] arrayOfByte = ByteUtils.append(50, new byte[][] { Prefix.SCAN_WIFI_DEVICE, ByteUtils.getPhonenumberBytes(), { 74 } });
              HomeFragment.access$502(HomeFragment.this, false);
              label42: while (!HomeFragment.this.stopTag) {
                byte b1 = 1;
                byte b = -1;
                for (byte b2 = 1;; b2++) {
                  if (b2 > 'È' || HomeFragment.this.stopTag) {
                    try {
                      Thread.sleep(2000L);
                    } catch (InterruptedException interruptedException) {
                      interruptedException.printStackTrace();
                    } 
                    continue label42;
                  } 
                  StringBuffer stringBuffer = new StringBuffer();
                  stringBuffer.append(String.valueOf(i & 0xFF));
                  stringBuffer.append(".");
                  stringBuffer.append(String.valueOf(i >> 8 & 0xFF));
                  stringBuffer.append(".");
                  int j = i >> 24 & 0xFF;
                  int k = i >> 16 & 0xFF;
                  if (b2 % 2 == 0) {
                    if (j + b <= 0) {
                      if (k - 1 <= 0) {
                        k = 0;
                      } else {
                        k--;
                      } 
                      stringBuffer.append(k).append(".").append(-(j + b));
                    } else {
                      stringBuffer.append(k).append(".").append(j + b);
                    } 
                    b--;
                    HomeFragment.this.udpClient.sendData(1, stringBuffer.toString(), arrayOfByte, null);
                  } else {
                    if (j + b1 >= 255) {
                      if (k + 1 >= 255) {
                        k = 255;
                      } else {
                        k++;
                      } 
                      stringBuffer.append(k).append(".").append(j + b1 - 255);
                    } else {
                      stringBuffer.append(k).append(".").append(j + b1);
                    } 
                    HomeFragment.this.udpClient.sendData(1, stringBuffer.toString(), arrayOfByte, null);
                    b1++;
                  } 
                  try {
                    Thread.sleep(20L);
                  } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                  } 
                } 
              } 
            }
          };
        this.thread.start();
      case 11:
        this.mSeekBar.setProgress(100);
        this.devices = FatherDeviceInfoDB.getInstance().queryUserList();
        if (this.devices == null || this.devices.size() == 0)
          XlinkUtils.shortTips(getString(2131296662)); 
        if (this.mScanningDiaog != null)
          this.mScanningDiaog.dismiss(); 
        this.stopTag = true;
        this.mHandler.removeMessages(5);
        if (this.thread != null) {
          this.thread.interrupt();
          this.thread = null;
        } 
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(12);
      case 12:
        scan();
      case 14:
        if (this.mProgressDialog != null || this.mProgressDialog.isShowing())
          this.mProgressDialog.dismiss(); 
      case 15:
        this.upDataProgressText.setText("0%");
        this.updataSeekBar.setProgress(0);
        getUpDataUrl(DeviceManager.getInstance().getDevices().get(this.index));
      case 16:
        this.upDataProgressText.setText("100%");
        this.updataSeekBar.setProgress(100);
        this.mUpdataDialog.dismiss();
        XlinkUtils.shortTips(2131297532);
      case 17:
        break;
    } 
    if (this.devices1 != null) {
      int i = 0;
      byte b = 0;
      while (b < this.devices1.size()) {
        int j = i;
        if (((FatherDeviceInfo)this.devices1.get(b)).getIsOnline())
          j = i + 1; 
        b++;
        i = j;
      } 
      this.wifi_number.setText(this.devices1.size() + getString(2131296611) + i + getString(2131296612) + "17");
    } 
  }
  
  protected void initUI(View paramView) {
    initReceiver();
    this.showView = new CustomDialog();
    this.mHandler = new Handler(this);
    App.getInstance().getLanguageSubject().attach(this);
    App.getInstance().getAllLampSubject().attach(this);
    FatherDeviceInfo fatherDeviceInfo = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
    int i = FatherDeviceInfoDB.getInstance().queryUserList().size();
    if (fatherDeviceInfo == null) {
      this.wifi_number.setText(getString(2131296280) + i + getString(2131296611));
    } else {
      this.wifi_number.setText(getString(2131296280) + (i - 1) + getString(2131296611));
    } 
    Log.d("HomeFragment", "DeviceManager.getInstance()" + DeviceManager.getInstance().getDevices().size());
    if (this.wifiMessageDao == null)
      this.wifiMessageDao = new WifiMessageDao((Context)this.mActivity); 
    UdpClient.getInstance().connectSocket();
    this.rl_main_view = (RelativeLayout)paramView.findViewById(2131756901);
    this.rl_mohu = (RelativeLayout)paramView.findViewById(2131756899);
    this.tv_mohu = (TextView)paramView.findViewById(2131756900);
    this.tv_setting = (TextView)paramView.findViewById(2131756897);
    if (PreferenceHelper.readBoolean("app.gamer.quadstellar_isFist", true)) {
      Bitmap bitmap = ImageUtils.fastBlur(ImageUtils.getBitmap(2130838091), 0.5F, 20.0F, true);
      this.rl_main_view.setVisibility(8);
      this.rl_mohu.setVisibility(0);
      this.rl_mohu.setBackground((Drawable)new BitmapDrawable(bitmap));
      this.tv_setting.setVisibility(8);
    } 
  }
  
  protected void lazyLoad() {}
  
  public void onActivityCreated(@Nullable Bundle paramBundle) {
    super.onActivityCreated(paramBundle);
    this.mWifiManager = (WifiManager)App.getAppContext().getApplicationContext().getSystemService("wifi");
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt2 != -1);
    LogUtil.e("requestCode", "requestCode====" + paramInt1);
    switch (paramInt1) {
      case 8053:
      case 8054:
      case 0:
        return;
      case 8057:
        break;
    } 
    App.getInstance().getLampSubject().detach(this);
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    return initView(paramLayoutInflater, 2130903247, paramViewGroup);
  }
  
  public void onDestroy() {
    super.onDestroy();
    this.mHandler.sendEmptyMessage(0);
  }
  
  public void onResume() {
    super.onResume();
    Display display = this.mActivity.getWindowManager().getDefaultDisplay();
    this.height = (int)(display.getHeight() * 0.5D);
    this.width = (int)(display.getWidth() * 0.7D);
    Log.d("扫描-扫描", StringTools.ISGETDEVICE + "");
    if (StringTools.ISGETDEVICE != 0) {
      this.dialog = this.showView.tipsDialog((Activity)this.mActivity, getString(2131297503), getString(2131297045), new View.OnClickListener() {
            public void onClick(View param1View) {
              HomeFragment.this.showActivityForResult((Activity)HomeFragment.this.mActivity, ControlDeviceActivity.class, 8053);
              HomeFragment.this.dialog.dismiss();
            }
          });
      this.dialog.show();
      StringTools.ISGETDEVICE = 0;
    } 
    FatherDeviceInfo fatherDeviceInfo = FatherDeviceInfoDB.getInstance().queryUserList(App.macDress);
    int i = FatherDeviceInfoDB.getInstance().queryUserList().size();
    this.devices1 = FatherDeviceInfoDB.getInstance().queryUserList();
    if (fatherDeviceInfo == null) {
      this.wifi_number.setText(getString(2131296280) + i + getString(2131296611));
    } else {
      this.wifi_number.setText(getString(2131296280) + (i - 1) + getString(2131296611));
      this.devices1.remove(fatherDeviceInfo);
    } 
    if (this.devices1 != null) {
      int j = 0;
      byte b = 0;
      while (b < this.devices1.size()) {
        int k = j;
        if (((FatherDeviceInfo)this.devices1.get(b)).getIsOnline())
          k = j + 1; 
        b++;
        j = k;
      } 
      if (fatherDeviceInfo == null) {
        this.wifi_number.setText(getString(2131296280) + i + getString(2131296611) + j + getString(2131296612));
      } else {
        this.wifi_number.setText(getString(2131296280) + (i - 1) + getString(2131296611) + j + getString(2131296612));
      } 
    } 
    Log.d("HomeFragment", "DeviceManager.getInstance()" + DeviceManager.getInstance().getDevices().size());
  }
  
  public void update(Subject paramSubject, Object... paramVarArgs) {}
  
  private class EsptouchAsyncTasks implements Runnable {
    String apBssid;
    
    String apPassword;
    
    String apSsid;
    
    String isSsidHiddenStr;
    
    String taskResultCountStr;
    
    public EsptouchAsyncTasks(String param1String1, String param1String2, String param1String3, String param1String4, String param1String5) {
      this.apSsid = param1String1;
      this.apBssid = param1String2;
      this.apPassword = param1String3;
      this.isSsidHiddenStr = param1String4;
      this.taskResultCountStr = param1String5;
    }
    
    public void run() {
      // Byte code:
      //   0: aload_0
      //   1: getfield apBssid : Ljava/lang/String;
      //   4: ifnonnull -> 24
      //   7: aload_0
      //   8: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
      //   11: invokevirtual getWifiConnectedBssid : ()Ljava/lang/String;
      //   14: astore_1
      //   15: aload_1
      //   16: ifnull -> 7
      //   19: aload_0
      //   20: aload_1
      //   21: putfield apBssid : Ljava/lang/String;
      //   24: ldc app/gamer/quadstellar/ui/fragment/HomeFragment
      //   26: monitorenter
      //   27: iconst_0
      //   28: istore_2
      //   29: aload_0
      //   30: getfield isSsidHiddenStr : Ljava/lang/String;
      //   33: ldc 'YES'
      //   35: invokevirtual equals : (Ljava/lang/Object;)Z
      //   38: ifeq -> 43
      //   41: iconst_1
      //   42: istore_2
      //   43: new java/lang/StringBuilder
      //   46: astore_1
      //   47: aload_1
      //   48: invokespecial <init> : ()V
      //   51: ldc 'HomeFragment'
      //   53: aload_1
      //   54: ldc '扫描第三方sdk'
      //   56: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   59: aload_0
      //   60: getfield apSsid : Ljava/lang/String;
      //   63: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   66: ldc '密码'
      //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   71: aload_0
      //   72: getfield apPassword : Ljava/lang/String;
      //   75: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   78: iload_2
      //   79: invokevirtual append : (Z)Ljava/lang/StringBuilder;
      //   82: invokevirtual toString : ()Ljava/lang/String;
      //   85: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
      //   88: pop
      //   89: aload_0
      //   90: getfield taskResultCountStr : Ljava/lang/String;
      //   93: invokestatic parseInt : (Ljava/lang/String;)I
      //   96: istore_3
      //   97: aload_0
      //   98: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
      //   101: astore_1
      //   102: new app/esptouch/EsptouchTask
      //   105: astore #4
      //   107: aload #4
      //   109: aload_0
      //   110: getfield apSsid : Ljava/lang/String;
      //   113: aload_0
      //   114: getfield apBssid : Ljava/lang/String;
      //   117: aload_0
      //   118: getfield apPassword : Ljava/lang/String;
      //   121: iload_2
      //   122: aload_0
      //   123: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
      //   126: invokevirtual getActivity : ()Landroid/support/v4/app/FragmentActivity;
      //   129: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLandroid/content/Context;)V
      //   132: aload_1
      //   133: aload #4
      //   135: invokestatic access$4002 : (Lapp/gamer/quadstellar/ui/fragment/HomeFragment;Lapp/esptouch/EsptouchTask;)Lapp/esptouch/EsptouchTask;
      //   138: pop
      //   139: aload_0
      //   140: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
      //   143: invokestatic access$4000 : (Lapp/gamer/quadstellar/ui/fragment/HomeFragment;)Lapp/esptouch/EsptouchTask;
      //   146: aload_0
      //   147: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
      //   150: invokestatic access$4600 : (Lapp/gamer/quadstellar/ui/fragment/HomeFragment;)Lapp/esptouch/IEsptouchListener;
      //   153: invokevirtual setEsptouchListener : (Lapp/esptouch/IEsptouchListener;)V
      //   156: ldc app/gamer/quadstellar/ui/fragment/HomeFragment
      //   158: monitorexit
      //   159: aload_0
      //   160: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
      //   163: invokestatic access$4000 : (Lapp/gamer/quadstellar/ui/fragment/HomeFragment;)Lapp/esptouch/EsptouchTask;
      //   166: iload_3
      //   167: invokevirtual executeForResults : (I)Ljava/util/List;
      //   170: astore_1
      //   171: aload_0
      //   172: getfield this$0 : Lapp/gamer/quadstellar/ui/fragment/HomeFragment;
      //   175: invokestatic access$4700 : (Lapp/gamer/quadstellar/ui/fragment/HomeFragment;)Lapp/gamer/quadstellar/ui/BaseActivity;
      //   178: new app/gamer/quadstellar/ui/fragment/HomeFragment$EsptouchAsyncTasks$1
      //   181: dup
      //   182: aload_0
      //   183: aload_1
      //   184: invokespecial <init> : (Lapp/gamer/quadstellar/ui/fragment/HomeFragment$EsptouchAsyncTasks;Ljava/util/List;)V
      //   187: invokevirtual runOnUiThread : (Ljava/lang/Runnable;)V
      //   190: return
      //   191: astore_1
      //   192: ldc app/gamer/quadstellar/ui/fragment/HomeFragment
      //   194: monitorexit
      //   195: aload_1
      //   196: athrow
      // Exception table:
      //   from	to	target	type
      //   29	41	191	finally
      //   43	159	191	finally
      //   192	195	191	finally
    }
  }
  
  class null implements Runnable {
    public void run() {
      IEsptouchResult iEsptouchResult = resultList.get(0);
      if (!iEsptouchResult.isCancelled() && !iEsptouchResult.isSuc())
        XlinkUtils.shortTips("Esptouch fail"); 
    }
  }
  
  class LoadTask extends AsyncTask<ControlDevice, Void, Void> {
    protected Void doInBackground(ControlDevice... param1VarArgs) {
      return null;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/fragment/HomeFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */