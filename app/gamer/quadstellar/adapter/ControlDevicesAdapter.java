package app.gamer.quadstellar.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.db.FatherDeviceInfoDB;
import app.gamer.quadstellar.domain.ControlRenameEvent;
import app.gamer.quadstellar.domain.DeviceStateEvent;
import app.gamer.quadstellar.domain.FatherDeviceInfo;
import app.gamer.quadstellar.ui.BaseActivity;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ControlDevicesAdapter extends ArrayListAdapter<FatherDeviceInfo> {
  private int devicePosition = -1;
  
  private TextView deviceState;
  
  int index = -1;
  
  private boolean isOnline = true;
  
  private Activity mActicity;
  
  private TextView mDevice_name;
  
  String newName;
  
  public ControlDevicesAdapter(BaseActivity paramBaseActivity) {
    super(paramBaseActivity);
    this.mActicity = (Activity)paramBaseActivity;
    EventBus.getDefault().register(this);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null)
      view = View.inflate((Context)this.mContext, 2130903119, null); 
    this.mDevice_name = (TextView)getAdapterView(view, 2131756543);
    TextView textView = (TextView)getAdapterView(view, 2131756544);
    this.deviceState = (TextView)getAdapterView(view, 2131756546);
    FatherDeviceInfo fatherDeviceInfo = this.mList.get(paramInt);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.mContext.getString(2131297583));
    stringBuilder.append(String.valueOf(paramInt + 1));
    if (fatherDeviceInfo != null && fatherDeviceInfo.getName() != null) {
      this.mDevice_name.setText(fatherDeviceInfo.getName());
      if (this.index == paramInt) {
        this.mDevice_name.setText(this.newName);
        fatherDeviceInfo.setName(this.newName);
        FatherDeviceInfoDB.getInstance().updateUser(fatherDeviceInfo);
      } 
    } else if (fatherDeviceInfo != null && this.index == paramInt) {
      this.mDevice_name.setText(this.newName);
      fatherDeviceInfo.setName(this.newName);
      FatherDeviceInfoDB.getInstance().updateUser(fatherDeviceInfo);
    } else {
      this.mDevice_name.setText(stringBuilder.toString());
    } 
    Log.d("ControlDevicesAdapter", "执行了=========");
    textView.setText("MAC:" + fatherDeviceInfo.getMacAdrass());
    if (fatherDeviceInfo.getIsOnline()) {
      this.deviceState.setText(this.mActicity.getString(2131296933));
    } else {
      this.deviceState.setText(this.mActicity.getString(2131296922));
    } 
    if (this.devicePosition == paramInt) {
      if (this.isOnline) {
        this.deviceState.setText(this.mActicity.getString(2131296933));
        return view;
      } 
    } else {
      return view;
    } 
    this.deviceState.setText(this.mActicity.getString(2131296922));
    return view;
  }
  
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(DeviceStateEvent paramDeviceStateEvent) {
    List<FatherDeviceInfo> list = FatherDeviceInfoDB.getInstance().queryUserList();
    byte b = 0;
    while (true) {
      if (b < list.size())
        if (((FatherDeviceInfo)list.get(b)).getMacAdrass().equals(App.macDress)) {
          list.remove(b);
        } else {
          b++;
          continue;
        }  
      this.mList.clear();
      this.mList.addAll(list);
      notifyDataSetChanged();
      return;
    } 
  }
  
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEventMainThread(ControlRenameEvent paramControlRenameEvent) {
    this.index = paramControlRenameEvent.getPositon();
    this.newName = paramControlRenameEvent.getName();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/adapter/ControlDevicesAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */