package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.gamer.quadstellar.mode.EFChoice;

public class ChoiceItemView extends RelativeLayout implements Checkable, Handler.Callback {
  private static final int UPDATE_UI = 7070;
  
  private CheckBox box;
  
  private TextView content;
  
  private EFChoice entry;
  
  private Context mContext;
  
  private Handler mHandler;
  
  public ChoiceItemView(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  public ChoiceItemView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public ChoiceItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private void init(Context paramContext) {
    if (!isInEditMode()) {
      this.mContext = paramContext;
      View view = View.inflate(paramContext, 2130903116, (ViewGroup)this);
      this.content = (TextView)view.findViewById(2131756538);
      this.box = (CheckBox)view.findViewById(2131756539);
      this.mHandler = new Handler(this);
    } 
  }
  
  private void updateUI() {
    if (this.entry != null) {
      String str;
      TextView textView = this.content;
      if (this.entry.getKey() != null) {
        str = this.entry.getKey();
      } else {
        str = "";
      } 
      textView.setText(str);
    } 
  }
  
  public EFChoice getEntry() {
    return this.entry;
  }
  
  public boolean handleMessage(Message paramMessage) {
    switch (paramMessage.what) {
      default:
        return false;
      case 7070:
        break;
    } 
    updateUI();
  }
  
  public boolean isChecked() {
    return this.box.isChecked();
  }
  
  public void setChecked(boolean paramBoolean) {
    this.box.setChecked(paramBoolean);
  }
  
  public void setEntry(EFChoice paramEFChoice) {
    this.entry = paramEFChoice;
    this.mHandler.sendEmptyMessage(7070);
  }
  
  public void toggle() {
    this.box.toggle();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/ChoiceItemView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */