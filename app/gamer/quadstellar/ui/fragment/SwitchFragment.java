package app.gamer.quadstellar.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.Prefix;
import app.gamer.quadstellar.callback.ISendCallback;
import app.gamer.quadstellar.callback.SendCallback;
import app.gamer.quadstellar.mode.ControlDevice;
import app.gamer.quadstellar.mode.EFDeviceLight;
import app.gamer.quadstellar.mode.dao.DaoUtil;
import app.gamer.quadstellar.net.DeviceManager;
import app.gamer.quadstellar.net.XlinkClient;
import app.gamer.quadstellar.observer.Observer;
import app.gamer.quadstellar.observer.Subject;
import app.gamer.quadstellar.ui.BaseActivity;
import app.gamer.quadstellar.ui.BaseFragment;
import app.gamer.quadstellar.ui.widget.LoadingDialog;
import app.gamer.quadstellar.utils.ByteUtils;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.view.annotation.ViewInject;

public class SwitchFragment extends BaseFragment implements Observer {
  private ControlDevice mDevice;
  
  private LoadingDialog mLoadingDialog;
  
  @ViewInject(2131756360)
  private TextView mTxtTitle;
  
  private void off(View paramView) {
    this.mDevice = DeviceManager.getInstance().getRecentNorDev();
    byte[] arrayOfByte = ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, ByteUtils.getPhonenumberBytes(), Prefix.BROACAST_DATA, Prefix.COLOR_OFF, ByteUtils.int2OneByte(0) });
    XlinkClient.getInstance().sendPipe(this.mDevice, arrayOfByte, -2, (ISendCallback)new SendCallback() {
          public void onSendEnd(boolean param1Boolean) {
            if (SwitchFragment.this.mLoadingDialog != null)
              SwitchFragment.this.mLoadingDialog.dismiss(); 
            if (param1Boolean) {
              SwitchFragment.this.updateLampStatus(0);
              App.getInstance().getAllLampSubject().lampStateChanege(0);
            } 
          }
          
          public void onSendStart() {
            if (SwitchFragment.this.mLoadingDialog == null)
              SwitchFragment.access$002(SwitchFragment.this, new LoadingDialog((Context)SwitchFragment.this.mActivity)); 
            SwitchFragment.this.mLoadingDialog.show();
          }
        });
  }
  
  private void on(View paramView) {
    this.mDevice = DeviceManager.getInstance().getRecentNorDev();
    byte[] arrayOfByte = ByteUtils.append(50, new byte[][] { Prefix.CHANGE_COLOR_DEVICES, ByteUtils.getPhonenumberBytes(), Prefix.BROACAST_DATA, Prefix.COLOR_ON, ByteUtils.int2OneByte(0) });
    XlinkClient.getInstance().sendPipe(this.mDevice, arrayOfByte, -2, (ISendCallback)new SendCallback() {
          public void onSendEnd(boolean param1Boolean) {
            if (SwitchFragment.this.mLoadingDialog != null)
              SwitchFragment.this.mLoadingDialog.dismiss(); 
            if (param1Boolean) {
              SwitchFragment.this.updateLampStatus(1);
              App.getInstance().getAllLampSubject().lampStateChanege(1);
            } 
          }
          
          public void onSendStart() {
            if (SwitchFragment.this.mLoadingDialog == null)
              SwitchFragment.access$002(SwitchFragment.this, new LoadingDialog((Context)SwitchFragment.this.mActivity)); 
            SwitchFragment.this.mLoadingDialog.show();
          }
        });
  }
  
  protected void initUI(View paramView) {
    App.getInstance().getLanguageSubject().attach(this);
  }
  
  protected void lazyLoad() {}
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    return null;
  }
  
  public void update(Subject paramSubject, Object... paramVarArgs) {
    if (paramSubject instanceof app.gamer.quadstellar.observer.LanguageSubject)
      this.mTxtTitle.setText(2131296405); 
  }
  
  protected void updateLampStatus(int paramInt) {
    KeyValue keyValue = new KeyValue("deviceState", Integer.valueOf(paramInt));
    DaoUtil.update(EFDeviceLight.class, WhereBuilder.b("deviceType", "=", Integer.valueOf(1)).and("parentMac", "=", this.mDevice.getMacAddress()), keyValue);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/fragment/SwitchFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */