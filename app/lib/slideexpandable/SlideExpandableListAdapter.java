package app.lib.slideexpandable;

import android.view.View;
import android.widget.ListAdapter;

public class SlideExpandableListAdapter extends AbstractSlideExpandableListAdapter {
  private int expandable_view_id;
  
  private int toggle_button_id;
  
  public SlideExpandableListAdapter(ListAdapter paramListAdapter) {
    this(paramListAdapter, 2131755020, 2131755019);
  }
  
  public SlideExpandableListAdapter(ListAdapter paramListAdapter, int paramInt1, int paramInt2) {
    super(paramListAdapter);
    this.toggle_button_id = paramInt1;
    this.expandable_view_id = paramInt2;
  }
  
  public View getArrowView(View paramView) {
    return null;
  }
  
  public View getExpandToggleButton(View paramView) {
    return paramView.findViewById(this.toggle_button_id);
  }
  
  public View getExpandableView(View paramView) {
    return paramView.findViewById(this.expandable_view_id);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/slideexpandable/SlideExpandableListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */