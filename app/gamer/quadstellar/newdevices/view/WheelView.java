package app.gamer.quadstellar.newdevices.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class WheelView extends ScrollView {
  public static final int OFF_SET_DEFAULT = 1;
  
  private static final int SCROLL_DIRECTION_DOWN = 1;
  
  private static final int SCROLL_DIRECTION_UP = 0;
  
  public static final String TAG = WheelView.class.getSimpleName();
  
  private int colorPostion;
  
  private Context context;
  
  int displayItemCount;
  
  int initialY;
  
  int itemHeight = 0;
  
  List<String> items;
  
  int newCheck = 50;
  
  int offset = 1;
  
  private OnWheelViewListener onWheelViewListener;
  
  Paint paint;
  
  private int scrollDirection = -1;
  
  Runnable scrollerTask;
  
  int[] selectedAreaBorder;
  
  int selectedIndex = 1;
  
  int viewWidth;
  
  private LinearLayout views;
  
  public WheelView(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  public WheelView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public WheelView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private TextView createView(String paramString) {
    TextView textView = new TextView(this.context);
    textView.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, -2));
    textView.setSingleLine(true);
    textView.setTextSize(2, 20.0F);
    textView.setText(paramString);
    textView.setGravity(17);
    int i = dip2px(15.0F);
    textView.setPadding(i, i, i, i);
    if (this.itemHeight == 0) {
      this.itemHeight = getViewMeasuredHeight((View)textView);
      Log.d(TAG, "itemHeight: " + this.itemHeight);
      this.views.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, this.itemHeight * this.displayItemCount));
      setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(((LinearLayout.LayoutParams)getLayoutParams()).width, this.itemHeight * this.displayItemCount));
    } 
    return textView;
  }
  
  private int dip2px(float paramFloat) {
    return (int)(paramFloat * (this.context.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  private List<String> getItems() {
    return this.items;
  }
  
  private int getViewMeasuredHeight(View paramView) {
    paramView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(536870911, -2147483648));
    return paramView.getMeasuredHeight();
  }
  
  private void init(Context paramContext) {
    this.context = paramContext;
    Log.d(TAG, "parent: " + getParent());
    setVerticalScrollBarEnabled(false);
    this.views = new LinearLayout(paramContext);
    this.views.setOrientation(1);
    addView((View)this.views);
    this.scrollerTask = new Runnable() {
        public void run() {
          final int divided = WheelView.this.getScrollY();
          if (WheelView.this.initialY - i == 0) {
            final int remainder = WheelView.this.initialY % WheelView.this.itemHeight;
            i = WheelView.this.initialY / WheelView.this.itemHeight;
            if (j == 0) {
              WheelView.this.selectedIndex = WheelView.this.offset + i;
              WheelView.this.onSeletedCallBack();
              return;
            } 
            if (j > WheelView.this.itemHeight / 2) {
              WheelView.this.post(new Runnable() {
                    public void run() {
                      WheelView.this.smoothScrollTo(0, WheelView.this.initialY - remainder + WheelView.this.itemHeight);
                      WheelView.this.selectedIndex = divided + WheelView.this.offset + 1;
                      WheelView.this.onSeletedCallBack();
                    }
                  });
              return;
            } 
            WheelView.this.post(new Runnable() {
                  public void run() {
                    WheelView.this.smoothScrollTo(0, WheelView.this.initialY - remainder);
                    WheelView.this.selectedIndex = divided + WheelView.this.offset;
                    WheelView.this.onSeletedCallBack();
                  }
                });
            return;
          } 
          WheelView.this.initialY = WheelView.this.getScrollY();
          WheelView.this.postDelayed(WheelView.this.scrollerTask, WheelView.this.newCheck);
        }
      };
  }
  
  private void initData() {
    this.displayItemCount = this.offset * 2 + 1;
    for (String str : this.items)
      this.views.addView((View)createView(str)); 
    refreshItemView(0);
  }
  
  private int[] obtainSelectedAreaBorder() {
    if (this.selectedAreaBorder == null) {
      this.selectedAreaBorder = new int[2];
      this.selectedAreaBorder[0] = this.itemHeight * this.offset;
      this.selectedAreaBorder[1] = this.itemHeight * (this.offset + 1);
    } 
    return this.selectedAreaBorder;
  }
  
  private void onSeletedCallBack() {
    if (this.onWheelViewListener != null)
      this.onWheelViewListener.onSelected(this.selectedIndex, this.items.get(this.selectedIndex)); 
  }
  
  private void refreshItemView(int paramInt) {
    int i = paramInt / this.itemHeight;
    i = this.offset;
    i = paramInt % this.itemHeight;
    paramInt /= this.itemHeight;
    if (i == 0) {
      paramInt = this.offset;
    } else if (i > this.itemHeight / 2) {
      paramInt = this.offset;
    } 
    i = this.views.getChildCount();
    Log.d(TAG, "childSize:" + i);
    paramInt = 0;
    while (true) {
      if (paramInt < i) {
        TextView textView = (TextView)this.views.getChildAt(paramInt);
        if (textView != null) {
          if (this.selectedIndex == 1) {
            if (paramInt == 2) {
              textView.setTextColor(Color.parseColor("#01988A"));
            } else {
              textView.setTextColor(Color.parseColor("#5A5A5A"));
            } 
          } else if (this.selectedIndex == paramInt) {
            Log.d(TAG, "selectedIndex:" + this.selectedIndex);
            Log.d(TAG, "i:" + paramInt);
            textView.setTextColor(Color.parseColor("#01988A"));
          } else {
            textView.setTextColor(Color.parseColor("#5A5A5A"));
          } 
          paramInt++;
          continue;
        } 
      } 
      return;
    } 
  }
  
  public void fling(int paramInt) {
    super.fling(paramInt / 3);
  }
  
  public int getOffset() {
    return this.offset;
  }
  
  public OnWheelViewListener getOnWheelViewListener() {
    return this.onWheelViewListener;
  }
  
  public int getSeletedIndex() {
    return this.selectedIndex - this.offset;
  }
  
  public String getSeletedItem() {
    return this.items.get(this.selectedIndex);
  }
  
  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    refreshItemView(paramInt2);
    if (paramInt2 > paramInt4) {
      this.scrollDirection = 1;
      return;
    } 
    this.scrollDirection = 0;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    Log.d(TAG, "w: " + paramInt1 + ", h: " + paramInt2 + ", oldw: " + paramInt3 + ", oldh: " + paramInt4);
    this.viewWidth = paramInt1;
    setBackgroundDrawable((Drawable)null);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (paramMotionEvent.getAction() == 1)
      startScrollerTask(); 
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    if (this.viewWidth == 0) {
      this.viewWidth = ((Activity)this.context).getWindowManager().getDefaultDisplay().getWidth();
      Log.d(TAG, "viewWidth: " + this.viewWidth);
    } 
    if (this.paint == null) {
      this.paint = new Paint();
      this.paint.setColor(Color.parseColor("#83cde6"));
      this.paint.setStrokeWidth(dip2px(1.0F));
    } 
    super.setBackgroundDrawable(new Drawable() {
          public void draw(Canvas param1Canvas) {
            param1Canvas.drawLine((WheelView.this.viewWidth * 1 / 6), WheelView.this.obtainSelectedAreaBorder()[0], (WheelView.this.viewWidth * 5 / 6), WheelView.this.obtainSelectedAreaBorder()[0], WheelView.this.paint);
            param1Canvas.drawLine((WheelView.this.viewWidth * 1 / 6), WheelView.this.obtainSelectedAreaBorder()[1], (WheelView.this.viewWidth * 5 / 6), WheelView.this.obtainSelectedAreaBorder()[1], WheelView.this.paint);
          }
          
          public int getOpacity() {
            return 0;
          }
          
          public void setAlpha(int param1Int) {}
          
          public void setColorFilter(ColorFilter param1ColorFilter) {}
        });
  }
  
  public void setItems(List<String> paramList) {
    if (this.items == null)
      this.items = new ArrayList<String>(); 
    this.items.clear();
    this.items.addAll(paramList);
    for (byte b = 0; b < this.offset; b++) {
      this.items.add(0, "");
      this.items.add("");
    } 
    initData();
  }
  
  public void setOffset(int paramInt) {
    this.offset = paramInt;
  }
  
  public void setOnWheelViewListener(OnWheelViewListener paramOnWheelViewListener) {
    this.onWheelViewListener = paramOnWheelViewListener;
  }
  
  public void setSeletion(final int p) {
    this.colorPostion = p;
    Log.d(TAG, "position:" + p);
    this.selectedIndex = this.offset + p;
    post(new Runnable() {
          public void run() {
            WheelView.this.smoothScrollTo(0, p * WheelView.this.itemHeight);
          }
        });
  }
  
  public void startScrollerTask() {
    this.initialY = getScrollY();
    postDelayed(this.scrollerTask, this.newCheck);
  }
  
  public static class OnWheelViewListener {
    public void onSelected(int param1Int, String param1String) {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/view/WheelView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */