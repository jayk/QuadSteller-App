package app.gamer.quadstellar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import app.gamer.quadstellar.mode.EFChoice;
import app.gamer.quadstellar.ui.BaseActivity;

public class PhoneAdapter extends ArrayListAdapter<EFChoice> {
  private int selectPosition;
  
  public PhoneAdapter(BaseActivity paramBaseActivity) {
    super(paramBaseActivity);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null)
      view = View.inflate((Context)this.mContext, 2130903274, null); 
    TextView textView2 = (TextView)getAdapterView(view, 2131756345);
    TextView textView1 = (TextView)getAdapterView(view, 2131756951);
    ImageView imageView = (ImageView)getAdapterView(view, 2131756952);
    if (paramInt == this.selectPosition) {
      imageView.setSelected(true);
      textView1.setText(2131296822);
      textView2.setText(((EFChoice)this.mList.get(paramInt)).getKey());
      return view;
    } 
    imageView.setSelected(false);
    textView1.setText(2131297525);
    textView2.setText(((EFChoice)this.mList.get(paramInt)).getKey());
    return view;
  }
  
  public void setSelectPosition(int paramInt) {
    this.selectPosition = paramInt;
    notifyDataSetChanged();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/adapter/PhoneAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */