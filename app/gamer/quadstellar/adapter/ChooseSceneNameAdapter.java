package app.gamer.quadstellar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import app.gamer.quadstellar.ui.BaseActivity;

public class ChooseSceneNameAdapter extends ArrayListAdapter<String> {
  public ChooseSceneNameAdapter(BaseActivity paramBaseActivity) {
    super(paramBaseActivity);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null)
      view = View.inflate((Context)this.mContext, 2130903117, null); 
    ((TextView)getAdapterView(view, 2131756540)).setText(this.mList.get(paramInt));
    return view;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/adapter/ChooseSceneNameAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */