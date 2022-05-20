package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import java.util.ArrayList;

public class FlowRadioGroup extends LinearLayout {
  private int mCheckedId = -1;
  
  private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
  
  private OnCheckedChangeListener mOnCheckedChangeListener;
  
  private PassThroughHierarchyChangeListener mPassThroughListener;
  
  private boolean mProtectFromCheckedChange = false;
  
  private ArrayList<RadioButton> radioButtons;
  
  public FlowRadioGroup(Context paramContext) {
    super(paramContext);
    setOrientation(1);
    init();
  }
  
  public FlowRadioGroup(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  private void init() {
    this.mChildOnCheckedChangeListener = new CheckedStateTracker();
    this.mPassThroughListener = new PassThroughHierarchyChangeListener();
    super.setOnHierarchyChangeListener(this.mPassThroughListener);
    this.radioButtons = new ArrayList<RadioButton>();
  }
  
  private void setCheckedId(int paramInt) {
    this.mCheckedId = paramInt;
    if (this.mOnCheckedChangeListener != null)
      this.mOnCheckedChangeListener.onCheckedChanged(this, this.mCheckedId); 
  }
  
  private void setCheckedId(ViewGroup paramViewGroup) {
    int i = paramViewGroup.getChildCount();
    for (byte b = 0; b < i; b++) {
      if (paramViewGroup.getChildAt(b) instanceof RadioButton) {
        RadioButton radioButton = (RadioButton)paramViewGroup.getChildAt(b);
        if (radioButton.getId() == -1)
          radioButton.setId(radioButton.hashCode()); 
        radioButton.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
      } else if (paramViewGroup.getChildAt(b) instanceof ViewGroup) {
        setCheckedId((ViewGroup)paramViewGroup.getChildAt(b));
      } 
    } 
  }
  
  private void setCheckedStateForView(int paramInt, boolean paramBoolean) {
    View view = findViewById(paramInt);
    if (view != null && view instanceof RadioButton)
      ((RadioButton)view).setChecked(paramBoolean); 
  }
  
  private void setCheckedView(ViewGroup paramViewGroup) {
    int i = paramViewGroup.getChildCount();
    for (byte b = 0; b < i; b++) {
      if (paramViewGroup.getChildAt(b) instanceof RadioButton) {
        RadioButton radioButton = (RadioButton)paramViewGroup.getChildAt(b);
        this.radioButtons.add(radioButton);
        if (radioButton.isChecked()) {
          this.mProtectFromCheckedChange = true;
          if (this.mCheckedId != -1)
            setCheckedStateForView(this.mCheckedId, false); 
          this.mProtectFromCheckedChange = false;
          setCheckedId(radioButton.getId());
        } 
      } else if (paramViewGroup.getChildAt(b) instanceof ViewGroup) {
        setCheckedView((ViewGroup)paramViewGroup.getChildAt(b));
      } 
    } 
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    if (paramView instanceof RadioButton) {
      RadioButton radioButton = (RadioButton)paramView;
      this.radioButtons.add(radioButton);
      if (radioButton.isChecked()) {
        this.mProtectFromCheckedChange = true;
        if (this.mCheckedId != -1)
          setCheckedStateForView(this.mCheckedId, false); 
        this.mProtectFromCheckedChange = false;
        setCheckedId(radioButton.getId());
      } 
    } else if (paramView instanceof ViewGroup) {
      setCheckedView((ViewGroup)paramView);
    } 
    super.addView(paramView, paramInt, paramLayoutParams);
  }
  
  public void check(int paramInt) {
    if (paramInt == -1 || paramInt != this.mCheckedId) {
      if (this.mCheckedId != -1)
        setCheckedStateForView(this.mCheckedId, false); 
      if (paramInt != -1)
        setCheckedStateForView(paramInt, true); 
      setCheckedId(paramInt);
    } 
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void clearCheck() {
    check(-1);
  }
  
  public RadioButton findRadioButton(ViewGroup paramViewGroup) {
    RadioButton radioButton2;
    RadioButton radioButton1 = null;
    int i = paramViewGroup.getChildCount();
    byte b = 0;
    while (true) {
      radioButton2 = radioButton1;
      if (b < i) {
        if (paramViewGroup.getChildAt(b) instanceof RadioButton) {
          radioButton1 = (RadioButton)paramViewGroup.getChildAt(b);
        } else if (paramViewGroup.getChildAt(b) instanceof ViewGroup) {
          radioButton2 = findRadioButton((ViewGroup)paramViewGroup.getChildAt(b));
          findRadioButton((ViewGroup)paramViewGroup.getChildAt(b));
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    return radioButton2;
  }
  
  protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public int getCheckedRadioButtonId() {
    return this.mCheckedId;
  }
  
  public RadioButton getRadioButton(int paramInt) {
    return this.radioButtons.get(paramInt);
  }
  
  public int getRadioButtonCount() {
    return this.radioButtons.size();
  }
  
  protected void onFinishInflate() {
    super.onFinishInflate();
    if (this.mCheckedId != -1) {
      this.mProtectFromCheckedChange = true;
      setCheckedStateForView(this.mCheckedId, true);
      this.mProtectFromCheckedChange = false;
      setCheckedId(this.mCheckedId);
    } 
  }
  
  public void setOnCheckedChangeListener(OnCheckedChangeListener paramOnCheckedChangeListener) {
    this.mOnCheckedChangeListener = paramOnCheckedChangeListener;
  }
  
  public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener paramOnHierarchyChangeListener) {
    PassThroughHierarchyChangeListener.access$202(this.mPassThroughListener, paramOnHierarchyChangeListener);
  }
  
  private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
    private CheckedStateTracker() {}
    
    public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
      if (!FlowRadioGroup.this.mProtectFromCheckedChange) {
        FlowRadioGroup.access$302(FlowRadioGroup.this, true);
        if (FlowRadioGroup.this.mCheckedId != -1)
          FlowRadioGroup.this.setCheckedStateForView(FlowRadioGroup.this.mCheckedId, false); 
        FlowRadioGroup.access$302(FlowRadioGroup.this, false);
        int i = param1CompoundButton.getId();
        FlowRadioGroup.this.setCheckedId(i);
      } 
    }
  }
  
  public static class LayoutParams extends LinearLayout.LayoutParams {
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(int param1Int1, int param1Int2, float param1Float) {
      super(param1Int1, param1Int2, param1Float);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    protected void setBaseAttributes(TypedArray param1TypedArray, int param1Int1, int param1Int2) {
      if (param1TypedArray.hasValue(param1Int1)) {
        this.width = param1TypedArray.getLayoutDimension(param1Int1, "layout_width");
      } else {
        this.width = -2;
      } 
      if (param1TypedArray.hasValue(param1Int2)) {
        this.height = param1TypedArray.getLayoutDimension(param1Int2, "layout_height");
        return;
      } 
      this.height = -2;
    }
  }
  
  public static interface OnCheckedChangeListener {
    void onCheckedChanged(FlowRadioGroup param1FlowRadioGroup, int param1Int);
  }
  
  private class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
    private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
    
    private PassThroughHierarchyChangeListener() {}
    
    public void onChildViewAdded(View param1View1, View param1View2) {
      if (param1View1 == FlowRadioGroup.this && param1View2 instanceof RadioButton) {
        if (param1View2.getId() == -1)
          param1View2.setId(param1View2.hashCode()); 
        ((RadioButton)param1View2).setOnCheckedChangeListener(FlowRadioGroup.this.mChildOnCheckedChangeListener);
      } else if (param1View1 == FlowRadioGroup.this && param1View2 instanceof ViewGroup) {
        FlowRadioGroup.this.setCheckedId((ViewGroup)param1View2);
      } 
      if (this.mOnHierarchyChangeListener != null)
        this.mOnHierarchyChangeListener.onChildViewAdded(param1View1, param1View2); 
    }
    
    public void onChildViewRemoved(View param1View1, View param1View2) {
      if (param1View1 == FlowRadioGroup.this && param1View2 instanceof RadioButton) {
        ((RadioButton)param1View2).setOnCheckedChangeListener(null);
      } else if (param1View1 == FlowRadioGroup.this && param1View2 instanceof ViewGroup) {
        FlowRadioGroup.this.findRadioButton((ViewGroup)param1View2).setOnCheckedChangeListener(null);
      } 
      if (this.mOnHierarchyChangeListener != null)
        this.mOnHierarchyChangeListener.onChildViewRemoved(param1View1, param1View2); 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/FlowRadioGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */