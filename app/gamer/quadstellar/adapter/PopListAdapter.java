package app.gamer.quadstellar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import app.gamer.quadstellar.ui.BaseActivity;

public class PopListAdapter extends ArrayListAdapter<String> {
  public PopListAdapter(BaseActivity paramBaseActivity) {
    super(paramBaseActivity);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null)
      view = View.inflate((Context)this.mContext, 2130903233, null); 
    TextView textView = (TextView)getAdapterView(view, 2131756867);
    textView.setText(this.mList.get(paramInt));
    textView.setTag(Integer.valueOf(paramInt));
    view.setBackgroundResource(2130838147);
    return view;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/adapter/PopListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */