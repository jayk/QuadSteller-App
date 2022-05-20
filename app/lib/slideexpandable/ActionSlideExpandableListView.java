package app.lib.slideexpandable;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListAdapter;

public class ActionSlideExpandableListView extends SlideExpandableListView {
  public static OnItemClickListener onItemClickListener;
  
  private int[] buttonIds = null;
  
  private OnActionClickListener listener;
  
  public ActionSlideExpandableListView(Context paramContext) {
    super(paramContext);
  }
  
  public ActionSlideExpandableListView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public ActionSlideExpandableListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void setAdapter(ListAdapter paramListAdapter) {
    super.setAdapter((ListAdapter)new WrapperListAdapterImpl(paramListAdapter) {
          public View getView(final int position, View param1View, ViewGroup param1ViewGroup) {
            final View listView = this.wrapped.getView(position, param1View, param1ViewGroup);
            if (ActionSlideExpandableListView.this.buttonIds != null && view != null)
              for (int i : ActionSlideExpandableListView.this.buttonIds) {
                View view1 = view.findViewById(i);
                if (view1 != null)
                  view1.findViewById(i).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View param2View) {
                          if (ActionSlideExpandableListView.this.listener != null)
                            ActionSlideExpandableListView.this.listener.onClick(listView, param2View, position); 
                        }
                      }); 
              }  
            return view;
          }
        });
  }
  
  public void setItemActionListener(OnActionClickListener paramOnActionClickListener, int... paramVarArgs) {
    this.listener = paramOnActionClickListener;
    this.buttonIds = paramVarArgs;
  }
  
  public void setItemClickListener(OnItemClickListener paramOnItemClickListener) {
    onItemClickListener = paramOnItemClickListener;
  }
  
  public static interface OnActionClickListener {
    void onClick(View param1View1, View param1View2, int param1Int);
  }
  
  public static interface OnItemClickListener {
    void OnClick(int param1Int1, int param1Int2);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/slideexpandable/ActionSlideExpandableListView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */