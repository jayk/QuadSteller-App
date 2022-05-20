package app.gamer.quadstellar.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.Prefix;
import app.gamer.quadstellar.adapter.ControlDevicesAdapter;
import app.gamer.quadstellar.db.ColorInfoDB;
import app.gamer.quadstellar.db.FanDeviceInfoDB;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.db.LightDeviceInfoDB;
import app.gamer.quadstellar.domain.ControlRenameEvent;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.domain.UdpEvent;
import app.gamer.quadstellar.mode.ControlDevice;
import app.gamer.quadstellar.net.UdpClient;
import app.gamer.quadstellar.newdevices.activity.ControlerActicity;
import app.gamer.quadstellar.ui.BaseActivity;
import app.gamer.quadstellar.ui.widget.SetingDialog;
import app.gamer.quadstellar.utils.ByteUtils;
import app.gamer.quadstellar.utils.LightTool;
import app.gamer.quadstellar.utils.StringTools;
import app.gamer.quadstellar.utils.XlinkUtils;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

public class ControlDeviceActivity extends BaseActivity {
  private static final String TAG = "ControlDeviceActivity";
  
  @ViewInject(2131756399)
  private TextView back;
  
  private ControlDevicesAdapter controlDevicesAdapter;
  
  private List<ControlDevice> devices;
  
  @ViewInject(2131756400)
  private SwipeMenuListView devicesList;
  
  private List<FatherDeviceInfo> mControlDevices;
  
  private EditText mEditName;
  
  private SetingDialog mRenameDialog;
  
  ReNameOnClickListener reNameOnClickListener;
  
  private UdpClient udpClient;
  
  private void initSwipeList() {
    this.mControlDevices = FatherDeviceInfoDB.getInstance().queryUserList();
    byte b = 0;
    while (true) {
      if (b < this.mControlDevices.size())
        if (((FatherDeviceInfo)this.mControlDevices.get(b)).getMacAdrass().equals(App.macDress)) {
          this.mControlDevices.remove(b);
        } else {
          b++;
          continue;
        }  
      updataDevice(this.mControlDevices);
      SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
          public void create(SwipeMenu param1SwipeMenu) {
            SwipeMenuItem swipeMenuItem1 = new SwipeMenuItem(ControlDeviceActivity.this.getApplicationContext());
            SwipeMenuItem swipeMenuItem2 = new SwipeMenuItem(ControlDeviceActivity.this.getApplicationContext());
            swipeMenuItem1.setWidth(LightTool.dip2px((Context)ControlDeviceActivity.this, 70.0F));
            swipeMenuItem2.setWidth(LightTool.dip2px((Context)ControlDeviceActivity.this, 70.0F));
            swipeMenuItem1.setTitle(ControlDeviceActivity.this.getString(2131296634));
            swipeMenuItem1.setBackground(2130837802);
            swipeMenuItem1.setTitleColor(-1);
            swipeMenuItem2.setTitleColor(-1);
            swipeMenuItem1.setTitleSize(18);
            swipeMenuItem2.setTitle(ControlDeviceActivity.this.getString(2131296580));
            swipeMenuItem2.setBackground(2130837798);
            swipeMenuItem2.setTitleSize(18);
            param1SwipeMenu.addMenuItem(swipeMenuItem1);
            param1SwipeMenu.addMenuItem(swipeMenuItem2);
          }
        };
      this.devicesList.setMenuCreator(swipeMenuCreator);
      this.devicesList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            public boolean onMenuItemClick(final int position, SwipeMenu param1SwipeMenu, int param1Int2) {
              Button button1;
              Display display;
              View view;
              Button button2;
              FatherDeviceInfo fatherDeviceInfo;
              Log.d("ControlDeviceActivity", "position:" + position);
              switch (param1Int2) {
                default:
                  return true;
                case 0:
                  ControlDeviceActivity.access$202(ControlDeviceActivity.this, new SetingDialog((Context)ControlDeviceActivity.this, 2131427778));
                  view = View.inflate((Context)ControlDeviceActivity.this, 2130903176, null);
                  button1 = (Button)view.findViewById(2131756661);
                  button2 = (Button)view.findViewById(2131756662);
                  ControlDeviceActivity.access$302(ControlDeviceActivity.this, (EditText)view.findViewById(2131756717));
                  fatherDeviceInfo = ControlDeviceActivity.this.controlDevicesAdapter.getList().get(position);
                  if (fatherDeviceInfo.getName() == null) {
                    ControlDeviceActivity.this.mEditName.setText(ControlDeviceActivity.this.getString(2131297583) + (position + 1));
                  } else {
                    ControlDeviceActivity.this.mEditName.setText(fatherDeviceInfo.getName());
                  } 
                  button1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View param2View) {
                          String str = ControlDeviceActivity.this.mEditName.getText().toString().trim();
                          if (TextUtils.isEmpty(str)) {
                            XlinkUtils.shortTips(ControlDeviceActivity.this.getString(2131296911));
                            return;
                          } 
                          ControlRenameEvent controlRenameEvent = new ControlRenameEvent();
                          controlRenameEvent.setName(str);
                          controlRenameEvent.setPositon(position);
                          EventBus.getDefault().post(controlRenameEvent);
                          ControlDeviceActivity.this.controlDevicesAdapter.notifyDataSetChanged();
                          ControlDeviceActivity.this.mRenameDialog.dismiss();
                        }
                      });
                  button2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View param2View) {
                          if (ControlDeviceActivity.this.mRenameDialog != null)
                            ControlDeviceActivity.this.mRenameDialog.dismiss(); 
                        }
                      });
                  ControlDeviceActivity.this.mRenameDialog.setContentView(view);
                  display = ControlDeviceActivity.this.getWindowManager().getDefaultDisplay();
                  position = (int)(display.getHeight() * 0.5D);
                  position = (int)(display.getWidth() * 0.7D);
                  ControlDeviceActivity.this.mRenameDialog.getWindow().setLayout(position, -2);
                  ControlDeviceActivity.this.mRenameDialog.show();
                case 1:
                  break;
              } 
              String str = ((FatherDeviceInfo)ControlDeviceActivity.this.controlDevicesAdapter.getList().get(position)).getMacAdrass();
              FatherDeviceInfoDB.getInstance().deleteUser(str);
              FanDeviceInfoDB.getInstance().deleteUser(str);
              LightDeviceInfoDB.getInstance().deleteUser(str);
              ColorInfoDB.getInstance().deleteUser(str);
              ControlDeviceActivity.this.controlDevicesAdapter.remove(position);
              if (ControlDeviceActivity.this.controlDevicesAdapter.getList().size() == 0) {
                FatherDeviceInfoDB.getInstance().deleteUser(App.macDress);
                FanDeviceInfoDB.getInstance().deleteUser(App.macDress);
                LightDeviceInfoDB.getInstance().deleteUser(App.macDress);
                ColorInfoDB.getInstance().deleteUser(App.macDress);
              } 
            }
          });
      return;
    } 
  }
  
  private void toControlDeviceActivity(FatherDeviceInfo paramFatherDeviceInfo) {
    App.getInstance().setCurrentDevice(paramFatherDeviceInfo);
    toGetDevices(paramFatherDeviceInfo);
  }
  
  private void toGetDevices(FatherDeviceInfo paramFatherDeviceInfo) {
    byte[] arrayOfByte = ByteUtils.append(50, new byte[][] { Prefix.OC_TYPE, ByteUtils.getSystemTimeBytes(Long.valueOf(System.currentTimeMillis())), ByteUtils.getPhonenumberBytes(), ByteUtils.hexStringToBytes(paramFatherDeviceInfo.getMacAdrass()), { 74 } });
    this.udpClient = UdpClient.getInstance();
    if (!this.udpClient.connectSocket())
      XlinkUtils.shortTips(getString(2131296644)); 
    if (paramFatherDeviceInfo != null && paramFatherDeviceInfo.getIsOnline()) {
      this.udpClient.sendData(7074, paramFatherDeviceInfo.getDeviceIP(), arrayOfByte, null);
      App.getInstance().setCurrentDevice(paramFatherDeviceInfo);
      Intent intent = new Intent((Context)this, ControlerActicity.class);
      intent.putExtra("isContrlALl", false);
      startActivity(intent);
    } 
  }
  
  private void updataDevice(List<FatherDeviceInfo> paramList) {
    Log.d("ControlDeviceActivity", "devices.size():" + paramList.size());
    if (paramList != null && paramList.size() >= 0) {
      if (this.controlDevicesAdapter == null) {
        this.controlDevicesAdapter = new ControlDevicesAdapter(this);
        this.devicesList.setAdapter((ListAdapter)this.controlDevicesAdapter);
      } 
      this.controlDevicesAdapter.setList(paramList);
    } 
  }
  
  protected void init() {
    StringTools.ISGETDEVICE = 0;
    initSwipeList();
    this.devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            ControlDeviceActivity.this.toControlDeviceActivity(ControlDeviceActivity.this.mControlDevices.get(param1Int));
          }
        });
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    EventBus.getDefault().register(this);
    setContentView(2130903076);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
  
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEventMainThread(UdpEvent paramUdpEvent) {
    if (paramUdpEvent.getState() == -1)
      this.controlDevicesAdapter.notifyDataSetChanged(); 
  }
  
  protected void onPause() {
    super.onPause();
  }
  
  protected void onStart() {
    super.onStart();
    this.back.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            ControlDeviceActivity.this.finish();
          }
        });
  }
  
  public void setReNameOnClickListener(ReNameOnClickListener paramReNameOnClickListener) {
    this.reNameOnClickListener = paramReNameOnClickListener;
  }
  
  public static interface ReNameOnClickListener {
    void ReNameOnClick(String param1String, int param1Int);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/activity/ControlDeviceActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */