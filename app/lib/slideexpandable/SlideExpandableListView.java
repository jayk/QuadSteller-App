package app.lib.slideexpandable;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

class SlideExpandableListView extends ListView {
  private SlideExpandableListAdapter adapter;
  
  public SlideExpandableListView(Context paramContext) {
    super(paramContext);
  }
  
  public SlideExpandableListView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public SlideExpandableListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public boolean collapse() {
    return (this.adapter != null) ? this.adapter.collapseLastOpen() : false;
  }
  
  public void enableExpandOnItemClick() {
    setOnItemClickListener(new AdapterView.OnItemClickListener() {
          public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            ((SlideExpandableListAdapter)SlideExpandableListView.this.getAdapter()).getExpandToggleButton(param1View).performClick();
          }
        });
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof AbstractSlideExpandableListAdapter.SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    AbstractSlideExpandableListAdapter.SavedState savedState = (AbstractSlideExpandableListAdapter.SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.adapter.onRestoreInstanceState(savedState);
  }
  
  public Parcelable onSaveInstanceState() {
    return this.adapter.onSaveInstanceState(super.onSaveInstanceState());
  }
  
  public void setAdapter(ListAdapter paramListAdapter) {
    this.adapter = new SlideExpandableListAdapter(paramListAdapter);
    super.setAdapter((ListAdapter)this.adapter);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/slideexpandable/SlideExpandableListView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */