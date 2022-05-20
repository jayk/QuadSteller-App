package app.gamer.quadstellar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import app.gamer.quadstellar.mode.EFSwitchTimer;
import app.gamer.quadstellar.ui.BaseActivity;

public class EFSwitchTimerAdapter extends ArrayListAdapter<EFSwitchTimer> {
  private int deviceType = 3;
  
  private SwitchTimerStateChangeListener listener = null;
  
  private boolean show = true;
  
  public EFSwitchTimerAdapter(BaseActivity paramBaseActivity) {
    super(paramBaseActivity);
  }
  
  public EFSwitchTimerAdapter(BaseActivity paramBaseActivity, int paramInt) {
    super(paramBaseActivity);
    this.deviceType = paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null)
      view = View.inflate((Context)this.mContext, 2130903304, null); 
    TextView textView = (TextView)getAdapterView(view, 2131757001);
    ImageView imageView = (ImageView)getAdapterView(view, 2131757002);
    final EFSwitchTimer timer = this.mList.get(paramInt);
    textView.setText(eFSwitchTimer.getAllText(this.deviceType));
    imageView.setSelected(eFSwitchTimer.getState());
    if (this.show) {
      paramInt = 0;
      imageView.setVisibility(paramInt);
      imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              boolean bool2;
              boolean bool1 = true;
              EFSwitchTimer eFSwitchTimer = timer;
              if (!param1View.isSelected()) {
                bool2 = true;
              } else {
                bool2 = false;
              } 
              eFSwitchTimer.setState(bool2);
              if (!param1View.isSelected()) {
                bool2 = bool1;
              } else {
                bool2 = false;
              } 
              param1View.setSelected(bool2);
              if (EFSwitchTimerAdapter.this.listener != null)
                EFSwitchTimerAdapter.this.listener.stateChange(timer, timer.getState()); 
            }
          });
      return view;
    } 
    paramInt = 4;
    imageView.setVisibility(paramInt);
    imageView.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            boolean bool2;
            boolean bool1 = true;
            EFSwitchTimer eFSwitchTimer = timer;
            if (!param1View.isSelected()) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            eFSwitchTimer.setState(bool2);
            if (!param1View.isSelected()) {
              bool2 = bool1;
            } else {
              bool2 = false;
            } 
            param1View.setSelected(bool2);
            if (EFSwitchTimerAdapter.this.listener != null)
              EFSwitchTimerAdapter.this.listener.stateChange(timer, timer.getState()); 
          }
        });
    return view;
  }
  
  public void setBoxState(boolean paramBoolean) {
    this.show = paramBoolean;
  }
  
  public void setSwitchTimerStateChangeListener(SwitchTimerStateChangeListener paramSwitchTimerStateChangeListener) {
    this.listener = paramSwitchTimerStateChangeListener;
  }
  
  public static interface SwitchTimerStateChangeListener {
    void stateChange(EFSwitchTimer param1EFSwitchTimer, boolean param1Boolean);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/adapter/EFSwitchTimerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */