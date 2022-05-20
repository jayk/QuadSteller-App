package app.gamer.quadstellar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import app.gamer.quadstellar.mode.EFDeviceSwitch;
import app.gamer.quadstellar.ui.BaseActivity;

public class EFSwitchAdapter extends ArrayListAdapter<EFDeviceSwitch> {
  public EFSwitchAdapter(BaseActivity paramBaseActivity) {
    super(paramBaseActivity);
  }
  
  private int getIconRes(int paramInt) {
    switch (paramInt) {
      default:
        return 2130838274;
      case -1:
        return 2130838301;
      case 0:
        break;
    } 
    return 2130838273;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null)
      view = View.inflate((Context)this.mContext, 2130903273, null); 
    TextView textView = (TextView)getAdapterView(view, 2131756950);
    ImageView imageView1 = (ImageView)getAdapterView(view, 2131756949);
    ImageView imageView2 = (ImageView)getAdapterView(view, 2131756948);
    EFDeviceSwitch eFDeviceSwitch = this.mList.get(paramInt);
    imageView1.setVisibility(4);
    if (eFDeviceSwitch.getDeviceName() == null) {
      String str1 = (paramInt + 1) + "";
      textView.setText(str1);
      imageView2.setImageResource(getIconRes(eFDeviceSwitch.getDeviceState()));
      return view;
    } 
    String str = eFDeviceSwitch.getDeviceName();
    textView.setText(str);
    imageView2.setImageResource(getIconRes(eFDeviceSwitch.getDeviceState()));
    return view;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/adapter/EFSwitchAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */