package app.gamer.quadstellar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import app.gamer.quadstellar.mode.EFTimer;
import app.gamer.quadstellar.ui.BaseActivity;

public class EFTimerAdapter extends ArrayListAdapter<EFTimer> {
  protected TimerStateChangeListener listener = null;
  
  protected boolean show = true;
  
  public EFTimerAdapter(BaseActivity paramBaseActivity) {
    super(paramBaseActivity);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null)
      view = View.inflate((Context)this.mContext, 2130903304, null); 
    TextView textView = (TextView)getAdapterView(view, 2131757001);
    ImageView imageView = (ImageView)getAdapterView(view, 2131757002);
    final EFTimer timer = this.mList.get(paramInt);
    textView.setText(eFTimer.getAllText(-1));
    imageView.setSelected(eFTimer.getState());
    if (this.show) {
      paramInt = 0;
      imageView.setVisibility(paramInt);
      imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              boolean bool2;
              boolean bool1 = true;
              EFTimer eFTimer = timer;
              if (!param1View.isSelected()) {
                bool2 = true;
              } else {
                bool2 = false;
              } 
              eFTimer.setState(bool2);
              if (!param1View.isSelected()) {
                bool2 = bool1;
              } else {
                bool2 = false;
              } 
              param1View.setSelected(bool2);
              if (EFTimerAdapter.this.listener != null)
                EFTimerAdapter.this.listener.stateChange(timer, timer.getState()); 
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
            EFTimer eFTimer = timer;
            if (!param1View.isSelected()) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            eFTimer.setState(bool2);
            if (!param1View.isSelected()) {
              bool2 = bool1;
            } else {
              bool2 = false;
            } 
            param1View.setSelected(bool2);
            if (EFTimerAdapter.this.listener != null)
              EFTimerAdapter.this.listener.stateChange(timer, timer.getState()); 
          }
        });
    return view;
  }
  
  public void setBoxState(boolean paramBoolean) {
    this.show = paramBoolean;
  }
  
  public void setTimerStateChangeListener(TimerStateChangeListener paramTimerStateChangeListener) {
    this.listener = paramTimerStateChangeListener;
  }
  
  public static interface TimerStateChangeListener {
    void stateChange(EFTimer param1EFTimer, boolean param1Boolean);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/adapter/EFTimerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */