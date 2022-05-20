package app.lib.slideexpandable;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import java.util.BitSet;

public abstract class AbstractSlideExpandableListAdapter extends WrapperListAdapterImpl {
  private int animationDuration = 330;
  
  private View lastOpen = null;
  
  private int lastOpenPosition = -1;
  
  private View lastToggle = null;
  
  private BitSet openItems = new BitSet();
  
  private final SparseIntArray viewHeights = new SparseIntArray(10);
  
  public AbstractSlideExpandableListAdapter(ListAdapter paramListAdapter) {
    super(paramListAdapter);
  }
  
  private void animateView(View paramView, int paramInt) {
    ExpandCollapseAnimation expandCollapseAnimation = new ExpandCollapseAnimation(paramView, paramInt);
    expandCollapseAnimation.setDuration(getAnimationDuration());
    paramView.startAnimation(expandCollapseAnimation);
  }
  
  private void enableFor(final View button, final View target, final int position) {
    if (target == this.lastOpen && position != this.lastOpenPosition) {
      this.lastOpen = null;
      this.lastToggle = null;
    } 
    if (position == this.lastOpenPosition) {
      this.lastOpen = target;
      this.lastToggle = button;
    } 
    if (this.viewHeights.get(position, -1) == -1) {
      this.viewHeights.put(position, target.getMeasuredHeight());
      updateExpandable(target, position);
    } else {
      updateExpandable(target, position);
    } 
    button.setOnClickListener(new View.OnClickListener() {
          public void onClick(final View view) {
            boolean bool;
            Animation animation = target.getAnimation();
            if (animation != null && animation.hasStarted() && !animation.hasEnded()) {
              animation.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationEnd(Animation param2Animation) {
                      view.performClick();
                    }
                    
                    public void onAnimationRepeat(Animation param2Animation) {}
                    
                    public void onAnimationStart(Animation param2Animation) {}
                  });
              return;
            } 
            target.setAnimation(null);
            if (target.getVisibility() == 0) {
              bool = true;
            } else {
              bool = false;
            } 
            if (!bool) {
              Log.e("my", "expand");
              AbstractSlideExpandableListAdapter.this.openItems.set(position, true);
              if (ActionSlideExpandableListView.onItemClickListener != null)
                ActionSlideExpandableListView.onItemClickListener.OnClick(position, 1); 
            } else {
              Log.e("my", "collapse");
              if (ActionSlideExpandableListView.onItemClickListener != null)
                ActionSlideExpandableListView.onItemClickListener.OnClick(position, 0); 
              AbstractSlideExpandableListAdapter.this.openItems.set(position, false);
            } 
            if (!bool) {
              if (AbstractSlideExpandableListAdapter.this.lastOpenPosition != -1 && AbstractSlideExpandableListAdapter.this.lastOpenPosition != position) {
                if (AbstractSlideExpandableListAdapter.this.lastOpen != null)
                  AbstractSlideExpandableListAdapter.this.animateView(AbstractSlideExpandableListAdapter.this.lastOpen, 1); 
                Log.e("my", "collapse222");
                AbstractSlideExpandableListAdapter.this.openItems.set(AbstractSlideExpandableListAdapter.this.lastOpenPosition, false);
              } 
              AbstractSlideExpandableListAdapter.access$202(AbstractSlideExpandableListAdapter.this, target);
              AbstractSlideExpandableListAdapter.access$402(AbstractSlideExpandableListAdapter.this, button);
              AbstractSlideExpandableListAdapter.access$102(AbstractSlideExpandableListAdapter.this, position);
            } else if (AbstractSlideExpandableListAdapter.this.lastOpenPosition == position) {
              AbstractSlideExpandableListAdapter.access$102(AbstractSlideExpandableListAdapter.this, -1);
            } 
            AbstractSlideExpandableListAdapter.this.animateView(target, bool);
          }
        });
  }
  
  private static BitSet readBitSet(Parcel paramParcel) {
    int i = paramParcel.readInt();
    BitSet bitSet = new BitSet();
    for (byte b = 0; b < i; b++)
      bitSet.set(paramParcel.readInt()); 
    return bitSet;
  }
  
  private void updateExpandable(View paramView, int paramInt) {
    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)paramView.getLayoutParams();
    if (this.openItems.get(paramInt)) {
      paramView.setVisibility(0);
      layoutParams.bottomMargin = 0;
      return;
    } 
    paramView.setVisibility(8);
    layoutParams.bottomMargin = 0 - this.viewHeights.get(paramInt);
  }
  
  private static void writeBitSet(Parcel paramParcel, BitSet paramBitSet) {
    int i = -1;
    paramParcel.writeInt(paramBitSet.cardinality());
    while (true) {
      i = paramBitSet.nextSetBit(i + 1);
      if (i != -1) {
        paramParcel.writeInt(i);
        continue;
      } 
      break;
    } 
  }
  
  public boolean collapseLastOpen() {
    null = true;
    if (isAnyItemExpanded()) {
      if (this.lastOpen != null)
        animateView(this.lastOpen, 1); 
      this.openItems.set(this.lastOpenPosition, false);
      this.lastOpenPosition = -1;
      return null;
    } 
    return false;
  }
  
  public void enableFor(View paramView, int paramInt) {
    View view1 = getExpandToggleButton(paramView);
    View view2 = getExpandableView(paramView);
    view2.measure(paramView.getWidth(), paramView.getHeight());
    enableFor(view1, view2, paramInt);
  }
  
  public int getAnimationDuration() {
    return this.animationDuration;
  }
  
  public abstract View getArrowView(View paramView);
  
  public abstract View getExpandToggleButton(View paramView);
  
  public abstract View getExpandableView(View paramView);
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    paramView = this.wrapped.getView(paramInt, paramView, paramViewGroup);
    enableFor(paramView, paramInt);
    return paramView;
  }
  
  public boolean isAnyItemExpanded() {
    return (this.lastOpenPosition != -1);
  }
  
  public void onRestoreInstanceState(SavedState paramSavedState) {
    this.lastOpenPosition = paramSavedState.lastOpenPosition;
    this.openItems = paramSavedState.openItems;
  }
  
  public Parcelable onSaveInstanceState(Parcelable paramParcelable) {
    SavedState savedState = new SavedState(paramParcelable);
    savedState.lastOpenPosition = this.lastOpenPosition;
    savedState.openItems = this.openItems;
    return (Parcelable)savedState;
  }
  
  public void setAnimationDuration(int paramInt) {
    if (paramInt < 0)
      throw new IllegalArgumentException("Duration is less than zero"); 
    this.animationDuration = paramInt;
  }
  
  static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public AbstractSlideExpandableListAdapter.SavedState createFromParcel(Parcel param2Parcel) {
          return new AbstractSlideExpandableListAdapter.SavedState(param2Parcel);
        }
        
        public AbstractSlideExpandableListAdapter.SavedState[] newArray(int param2Int) {
          return new AbstractSlideExpandableListAdapter.SavedState[param2Int];
        }
      };
    
    public int lastOpenPosition = -1;
    
    public BitSet openItems = null;
    
    private SavedState(Parcel param1Parcel) {
      super(param1Parcel);
      param1Parcel.writeInt(this.lastOpenPosition);
      AbstractSlideExpandableListAdapter.writeBitSet(param1Parcel, this.openItems);
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      this.lastOpenPosition = param1Parcel.readInt();
      this.openItems = AbstractSlideExpandableListAdapter.readBitSet(param1Parcel);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public AbstractSlideExpandableListAdapter.SavedState createFromParcel(Parcel param1Parcel) {
      return new AbstractSlideExpandableListAdapter.SavedState(param1Parcel);
    }
    
    public AbstractSlideExpandableListAdapter.SavedState[] newArray(int param1Int) {
      return new AbstractSlideExpandableListAdapter.SavedState[param1Int];
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/slideexpandable/AbstractSlideExpandableListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */