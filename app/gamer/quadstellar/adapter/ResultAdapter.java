package app.gamer.quadstellar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ResultAdapter extends ArrayAdapter {
  private LayoutInflater layoutInflater = null;
  
  private List<String> lstMac;
  
  public ResultAdapter(Context paramContext, int paramInt, List<String> paramList) {
    super(paramContext, paramInt, paramList);
    this.layoutInflater = LayoutInflater.from(paramContext);
    this.lstMac = paramList;
  }
  
  public int getCount() {
    return this.lstMac.size();
  }
  
  public Object getItem(int paramInt) {
    return this.lstMac.get(paramInt);
  }
  
  public long getItemId(int paramInt) {
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    if (paramView == null) {
      paramView = this.layoutInflater.inflate(2130903110, null);
      ViewHolder viewHolder1 = new ViewHolder();
      ViewHolder.access$102(viewHolder1, (TextView)paramView.findViewById(2131756524));
      ViewHolder.access$202(viewHolder1, (TextView)paramView.findViewById(2131756526));
      ViewHolder.access$302(viewHolder1, (TextView)paramView.findViewById(2131756525));
      paramView.setTag(viewHolder1);
      String[] arrayOfString1 = ((String)this.lstMac.get(paramInt)).split(";");
      viewHolder1.id.setText((paramInt + 1) + "");
      viewHolder1.mac.setText(arrayOfString1[0]);
      viewHolder1.ip.setText(arrayOfString1[1]);
      return paramView;
    } 
    ViewHolder viewHolder = (ViewHolder)paramView.getTag();
    String[] arrayOfString = ((String)this.lstMac.get(paramInt)).split(";");
    viewHolder.id.setText((paramInt + 1) + "");
    viewHolder.mac.setText(arrayOfString[0]);
    viewHolder.ip.setText(arrayOfString[1]);
    return paramView;
  }
  
  private static class ViewHolder {
    private TextView id;
    
    private TextView ip;
    
    private TextView mac;
    
    private ViewHolder() {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/adapter/ResultAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */