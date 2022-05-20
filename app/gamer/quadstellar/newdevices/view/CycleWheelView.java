package app.gamer.quadstellar.newdevices.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class CycleWheelView extends ListView {
  private static final int COLOR_DIVIDER_DEFALUT;
  
  private static final int COLOR_SOLID_DEFAULT;
  
  private static final int COLOR_SOLID_SELET_DEFAULT;
  
  private static final int HEIGHT_DIVIDER_DEFAULT = 2;
  
  public static final String TAG = CycleWheelView.class.getSimpleName();
  
  private static final int WHEEL_SIZE_DEFAULT = 5;
  
  private boolean cylceEnable;
  
  private int dividerColor = COLOR_DIVIDER_DEFALUT;
  
  private int dividerHeight = 2;
  
  private CycleWheelViewAdapter mAdapter;
  
  private float mAlphaGradual = 0.7F;
  
  private int mCurrentPositon;
  
  private Handler mHandler;
  
  private int mItemHeight;
  
  private int mItemLabelTvId;
  
  private int mItemLayoutId;
  
  private WheelItemSelectedListener mItemSelectedListener;
  
  private int mLabelColor = Color.parseColor("#5A5A5A");
  
  private int mLabelSelectColor = -1;
  
  private List<String> mLabels;
  
  private int mWheelSize = 5;
  
  private int selectIndex = -1;
  
  private int selectState;
  
  private int seletedSolidColor = COLOR_SOLID_SELET_DEFAULT;
  
  private int solidColor = COLOR_SOLID_DEFAULT;
  
  static {
    COLOR_DIVIDER_DEFALUT = Color.parseColor("#747474");
    COLOR_SOLID_DEFAULT = Color.parseColor("#232323");
    COLOR_SOLID_SELET_DEFAULT = Color.parseColor("#232323");
  }
  
  public CycleWheelView(Context paramContext) {
    super(paramContext);
  }
  
  public CycleWheelView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public CycleWheelView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private int getDistance(float paramFloat) {
    return (Math.abs(paramFloat) <= 2.0F) ? (int)paramFloat : ((Math.abs(paramFloat) < 12.0F) ? ((paramFloat > 0.0F) ? 2 : -2) : (int)(paramFloat / 6.0F));
  }
  
  private int getPosition(int paramInt) {
    if (this.mLabels == null || this.mLabels.size() == 0)
      return 0; 
    int i = paramInt;
    if (this.cylceEnable) {
      i = 1073741823 / this.mLabels.size();
      i = paramInt + this.mLabels.size() * i;
    } 
    return i;
  }
  
  private void init() {
    this.mHandler = new Handler();
    this.mItemLayoutId = 2130903216;
    this.mItemLabelTvId = 2131756837;
    this.mAdapter = new CycleWheelViewAdapter();
    setVerticalScrollBarEnabled(false);
    setScrollingCacheEnabled(false);
    setCacheColorHint(0);
    setFadingEdgeLength(0);
    setOverScrollMode(2);
    setDividerHeight(0);
    setAdapter((ListAdapter)this.mAdapter);
    setOnScrollListener(new AbsListView.OnScrollListener() {
          public void onScroll(AbsListView param1AbsListView, int param1Int1, int param1Int2, int param1Int3) {
            CycleWheelView.this.refreshItems();
          }
          
          public void onScrollStateChanged(AbsListView param1AbsListView, int param1Int) {
            Log.d(CycleWheelView.TAG, "scrollState:" + param1Int);
            CycleWheelView.access$002(CycleWheelView.this, param1Int);
            if (param1Int == 0) {
              View view = CycleWheelView.this.getChildAt(0);
              if (view != null) {
                float f = view.getY();
                if (f == 0.0F) {
                  if (CycleWheelView.this.selectIndex != CycleWheelView.this.getSelection()) {
                    if (CycleWheelView.this.mItemSelectedListener != null)
                      CycleWheelView.this.mItemSelectedListener.onItemSelected(CycleWheelView.this.getSelection(), CycleWheelView.this.getSelectLabel()); 
                    CycleWheelView.access$102(CycleWheelView.this, CycleWheelView.this.getSelection());
                  } 
                  return;
                } 
                if (Math.abs(f) < (CycleWheelView.this.mItemHeight / 2)) {
                  CycleWheelView.this.smoothScrollBy(CycleWheelView.this.getDistance(f), 50);
                } else {
                  CycleWheelView.this.smoothScrollBy(CycleWheelView.this.getDistance(CycleWheelView.this.mItemHeight + f), 50);
                } 
              } 
            } else {
              return;
            } 
            if (CycleWheelView.this.selectIndex != CycleWheelView.this.getSelection()) {
              if (CycleWheelView.this.mItemSelectedListener != null)
                CycleWheelView.this.mItemSelectedListener.onItemSelected(CycleWheelView.this.getSelection(), CycleWheelView.this.getSelectLabel()); 
              CycleWheelView.access$102(CycleWheelView.this, CycleWheelView.this.getSelection());
            } 
          }
        });
  }
  
  private void initView() {
    this.mItemHeight = measureHeight();
    (getLayoutParams()).height = this.mItemHeight * this.mWheelSize;
    this.mAdapter.setData(this.mLabels);
    this.mAdapter.notifyDataSetChanged();
    setBackgroundDrawable(new Drawable() {
          public void draw(Canvas param1Canvas) {
            int i = CycleWheelView.this.getWidth();
            Paint paint1 = new Paint();
            paint1.setColor(CycleWheelView.this.dividerColor);
            paint1.setStrokeWidth(CycleWheelView.this.dividerHeight);
            Paint paint2 = new Paint();
            paint2.setColor(CycleWheelView.this.seletedSolidColor);
            Paint paint3 = new Paint();
            paint3.setColor(CycleWheelView.this.solidColor);
            param1Canvas.drawRect(0.0F, 0.0F, i, (CycleWheelView.this.mItemHeight * CycleWheelView.this.mWheelSize / 2), paint3);
            param1Canvas.drawRect(0.0F, (CycleWheelView.this.mItemHeight * (CycleWheelView.this.mWheelSize / 2 + 1)), i, (CycleWheelView.this.mItemHeight * CycleWheelView.this.mWheelSize), paint3);
            param1Canvas.drawRect(0.0F, (CycleWheelView.this.mItemHeight * CycleWheelView.this.mWheelSize / 2), i, (CycleWheelView.this.mItemHeight * (CycleWheelView.this.mWheelSize / 2 + 1)), paint2);
            param1Canvas.drawLine(0.0F, (CycleWheelView.this.mItemHeight * CycleWheelView.this.mWheelSize / 2), i, (CycleWheelView.this.mItemHeight * CycleWheelView.this.mWheelSize / 2), paint1);
            param1Canvas.drawLine(0.0F, (CycleWheelView.this.mItemHeight * (CycleWheelView.this.mWheelSize / 2 + 1)), i, (CycleWheelView.this.mItemHeight * (CycleWheelView.this.mWheelSize / 2 + 1)), paint1);
          }
          
          public int getOpacity() {
            return 0;
          }
          
          public void setAlpha(int param1Int) {}
          
          public void setColorFilter(ColorFilter param1ColorFilter) {}
        });
  }
  
  private int measureHeight() {
    View view = LayoutInflater.from(getContext()).inflate(this.mItemLayoutId, null);
    view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    return view.getMeasuredHeight();
  }
  
  private void refreshItems() {
    int i = this.mWheelSize / 2;
    int j = getFirstVisiblePosition();
    if (getChildAt(0) != null) {
      int k;
      if (Math.abs(getChildAt(0).getY()) <= (this.mItemHeight / 2)) {
        k = j + i;
      } else {
        k = j + i + 1;
      } 
      if (k != this.mCurrentPositon) {
        this.mCurrentPositon = k;
        resetItems(j, k, i);
      } 
    } 
  }
  
  private void resetItems(int paramInt1, int paramInt2, int paramInt3) {
    for (int i = paramInt2 - paramInt3 - 1; i < paramInt2 + paramInt3 + 1; i++) {
      View view = getChildAt(i - paramInt1);
      if (view != null) {
        TextView textView = (TextView)view.findViewById(this.mItemLabelTvId);
        if (paramInt2 == i) {
          textView.setTextColor(this.mLabelSelectColor);
          textView.setTextSize(20.0F);
          view.setAlpha(1.0F);
        } else {
          textView.setTextColor(this.mLabelColor);
          textView.setTextSize(18.0F);
          int j = Math.abs(i - paramInt2);
          view.setAlpha((float)Math.pow(this.mAlphaGradual, j));
        } 
      } 
    } 
  }
  
  public List<String> getLabels() {
    return this.mLabels;
  }
  
  public String getSelectLabel() {
    String str;
    int i = getSelection();
    int j = i;
    if (i < 0)
      j = 0; 
    try {
      str = this.mLabels.get(j);
    } catch (Exception exception) {
      str = "";
    } 
    return str;
  }
  
  public int getSelection() {
    if (this.mCurrentPositon == 0)
      this.mCurrentPositon = this.mWheelSize / 2; 
    return (this.mCurrentPositon - this.mWheelSize / 2) % this.mLabels.size();
  }
  
  public void setAlphaGradual(float paramFloat) {
    this.mAlphaGradual = paramFloat;
    resetItems(getFirstVisiblePosition(), this.mCurrentPositon, this.mWheelSize / 2);
  }
  
  public void setCycleEnable(boolean paramBoolean) {
    if (this.cylceEnable != paramBoolean) {
      this.cylceEnable = paramBoolean;
      this.mAdapter.notifyDataSetChanged();
      setSelection(getSelection());
    } 
  }
  
  public void setDivider(int paramInt1, int paramInt2) {
    this.dividerColor = paramInt1;
    this.dividerHeight = paramInt2;
  }
  
  public void setLabelColor(int paramInt) {
    this.mLabelColor = paramInt;
    resetItems(getFirstVisiblePosition(), this.mCurrentPositon, this.mWheelSize / 2);
  }
  
  public void setLabelSelectColor(int paramInt) {
    this.mLabelSelectColor = paramInt;
    resetItems(getFirstVisiblePosition(), this.mCurrentPositon, this.mWheelSize / 2);
  }
  
  public void setLabels(List<String> paramList) {
    this.mLabels = paramList;
    this.mAdapter.setData(this.mLabels);
    this.mAdapter.notifyDataSetChanged();
    initView();
  }
  
  public void setOnWheelItemSelectedListener(WheelItemSelectedListener paramWheelItemSelectedListener) {
    this.mItemSelectedListener = paramWheelItemSelectedListener;
  }
  
  public void setSelection(final int position) {
    this.mHandler.post(new Runnable() {
          public void run() {
            CycleWheelView.this.setSelection(CycleWheelView.this.getPosition(position));
          }
        });
  }
  
  public void setSolid(int paramInt1, int paramInt2) {
    this.solidColor = paramInt1;
    this.seletedSolidColor = paramInt2;
    initView();
  }
  
  public void setWheelItemLayout(int paramInt1, int paramInt2) {
    this.mItemLayoutId = paramInt1;
    this.mItemLabelTvId = paramInt2;
    this.mAdapter = new CycleWheelViewAdapter();
    this.mAdapter.setData(this.mLabels);
    setAdapter((ListAdapter)this.mAdapter);
    initView();
  }
  
  public void setWheelSize(int paramInt) throws CycleWheelViewException {
    if (paramInt < 3 || paramInt % 2 != 1)
      throw new CycleWheelViewException("Wheel Size Error , Must Be 3,5,7,9..."); 
    this.mWheelSize = paramInt;
    initView();
  }
  
  public class CycleWheelViewAdapter extends BaseAdapter {
    private List<String> mData = new ArrayList<String>();
    
    public int getCount() {
      return CycleWheelView.this.cylceEnable ? Integer.MAX_VALUE : (this.mData.size() + CycleWheelView.this.mWheelSize - 1);
    }
    
    public Object getItem(int param1Int) {
      return "";
    }
    
    public long getItemId(int param1Int) {
      return param1Int;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      View view = param1View;
      if (param1View == null)
        view = LayoutInflater.from(CycleWheelView.this.getContext()).inflate(CycleWheelView.this.mItemLayoutId, null); 
      TextView textView = (TextView)view.findViewById(CycleWheelView.this.mItemLabelTvId);
      if (param1Int < CycleWheelView.this.mWheelSize / 2 || (!CycleWheelView.this.cylceEnable && param1Int >= this.mData.size() + CycleWheelView.this.mWheelSize / 2)) {
        textView.setText("");
        view.setVisibility(4);
        return view;
      } 
      textView.setText(this.mData.get((param1Int - CycleWheelView.this.mWheelSize / 2) % this.mData.size()));
      view.setVisibility(0);
      return view;
    }
    
    public boolean isEnabled(int param1Int) {
      return false;
    }
    
    public void setData(List<String> param1List) {
      this.mData.clear();
      this.mData.addAll(param1List);
    }
  }
  
  public class CycleWheelViewException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public CycleWheelViewException(String param1String) {
      super(param1String);
    }
  }
  
  public static interface WheelItemSelectedListener {
    void onItemSelected(int param1Int, String param1String);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/CycleWheelView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */