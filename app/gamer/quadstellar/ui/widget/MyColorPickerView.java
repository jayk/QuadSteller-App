package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import app.gamer.quadstellar.newdevices.view.AvatarImageView;
import java.util.ArrayList;
import java.util.List;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MyColorPickerView extends AbsoluteLayout {
  private Bitmap bitmap;
  
  private double bitmapHeight;
  
  private double bitmapWidth;
  
  private OnDoubleClickListener doubleClickListener;
  
  private boolean isGroup = false;
  
  private int lastX;
  
  private int lastY;
  
  private View.OnTouchListener listener = new View.OnTouchListener() {
      public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
        int j;
        switch (param1MotionEvent.getAction()) {
          default:
            return false;
          case 0:
            MyColorPickerView.access$002(MyColorPickerView.this, MyColorPickerView.access$102(MyColorPickerView.this, (int)param1MotionEvent.getRawX()));
            MyColorPickerView.access$202(MyColorPickerView.this, MyColorPickerView.access$302(MyColorPickerView.this, (int)param1MotionEvent.getRawY()));
            MyColorPickerView.access$402(MyColorPickerView.this, ((BitmapDrawable)MyColorPickerView.this.mPickerBackBg.getDrawable()).getBitmap());
            if (MyColorPickerView.this.bitmap != null) {
              MyColorPickerView.access$602(MyColorPickerView.this, MyColorPickerView.this.bitmap.getWidth());
              MyColorPickerView.access$702(MyColorPickerView.this, MyColorPickerView.this.bitmap.getHeight());
            } 
            if (param1View instanceof LightView) {
              MyColorPickerView.access$802(MyColorPickerView.this, false);
            } else if (param1View instanceof AllLightView) {
              MyColorPickerView.access$802(MyColorPickerView.this, true);
            } 
            MyColorPickerView.this.getParent().requestDisallowInterceptTouchEvent(true);
          case 2:
            i = (int)param1MotionEvent.getRawX() - MyColorPickerView.this.lastX;
            j = (int)param1MotionEvent.getRawY() - MyColorPickerView.this.lastY;
            k = param1View.getLeft() + i;
            m = param1View.getTop() + j;
            i = param1View.getRight() + i;
            j = param1View.getBottom() + j;
            if (param1View.getWidth() / 2 + k > 0 && i - param1View.getWidth() / 2 < MyColorPickerView.this.mFrameLayout.getWidth() && param1View.getHeight() / 2 + m > 0 && j - param1View.getHeight() / 2 < MyColorPickerView.this.mFrameLayout.getHeight()) {
              AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams)param1View.getLayoutParams();
              layoutParams.x = k;
              layoutParams.y = m;
              param1View.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
              i = MyColorPickerView.this.getImagePixel((i - param1View.getWidth() / 2), (j - param1View.getHeight() / 2));
              m = Color.red(i);
              k = Color.green(i);
              i = Color.blue(i);
              if (MyColorPickerView.this.mChangedListener != null)
                MyColorPickerView.this.mChangedListener.onMoveColor(m, k, i, param1View, MyColorPickerView.this.isGroup); 
            } 
            MyColorPickerView.access$102(MyColorPickerView.this, (int)param1MotionEvent.getRawX());
            MyColorPickerView.access$302(MyColorPickerView.this, (int)param1MotionEvent.getRawY());
            MyColorPickerView.this.getParent().requestDisallowInterceptTouchEvent(true);
          case 1:
            break;
        } 
        int i = MyColorPickerView.this.getImagePixel((param1View.getRight() - param1View.getWidth() / 2), (param1View.getBottom() - param1View.getHeight() / 2));
        int k = Color.red(i);
        int m = Color.green(i);
        i = Color.blue(i);
        if (MyColorPickerView.this.mChangedListener != null)
          MyColorPickerView.this.mChangedListener.onColorChanged(k, m, i, param1View, MyColorPickerView.this.isGroup); 
        MyColorPickerView.this.getParent().requestDisallowInterceptTouchEvent(true);
        if ((int)param1MotionEvent.getRawX() - MyColorPickerView.this.startX > 1 || (int)param1MotionEvent.getRawY() - MyColorPickerView.this.startY <= 1);
      }
    };
  
  private AllLightView mAllLightView = null;
  
  private OnColorChangedListener mChangedListener;
  
  @ViewInject(2131756541)
  private View mFrameLayout;
  
  private List<LightView> mLightViews = null;
  
  @ViewInject(2131756542)
  private AvatarImageView mPickerBackBg;
  
  private double pickerBackWidth;
  
  private double pickerBackheight;
  
  private int startX;
  
  private int startY;
  
  private boolean toRemove = true;
  
  public MyColorPickerView(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  public MyColorPickerView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public MyColorPickerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private void init(Context paramContext) {
    if (!isInEditMode()) {
      View view = View.inflate(paramContext, 2130903118, (ViewGroup)this);
      x.view().inject(view, (View)this);
      this.mLightViews = new ArrayList<LightView>();
    } 
  }
  
  public void addView(View paramView) {
    super.addView(paramView);
    paramView.setOnTouchListener(this.listener);
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    if (paramView instanceof LightView && !this.mLightViews.contains(paramView)) {
      paramView.setOnTouchListener(this.listener);
      paramView.setOnClickListener(new MyClickListener());
      this.mLightViews.add((LightView)paramView);
    } 
    if (paramView instanceof AllLightView) {
      if (this.mAllLightView != null)
        removeView((View)this.mAllLightView); 
      paramView.setOnTouchListener(this.listener);
      paramView.setOnClickListener(new MyClickListener());
      this.mAllLightView = (AllLightView)paramView;
    } 
    super.addView(paramView, paramLayoutParams);
  }
  
  public AllLightView getAllLightView() {
    return this.mAllLightView;
  }
  
  public int getImagePixel(float paramFloat1, float paramFloat2) {
    if (this.bitmap == null)
      return -1; 
    if (this.pickerBackWidth == 0.0D || this.pickerBackheight == 0.0D) {
      this.pickerBackWidth = this.mPickerBackBg.getWidth();
      this.pickerBackheight = this.mPickerBackBg.getHeight();
    } 
    double d1 = this.bitmapWidth / this.pickerBackWidth;
    double d2 = this.bitmapHeight / this.pickerBackheight;
    null = (int)Math.ceil(paramFloat1 * d1);
    int i = (int)Math.ceil(paramFloat2 * d2);
    int j = null;
    if (null < 0)
      j = 0; 
    null = i;
    if (i < 0)
      null = 0; 
    i = j;
    if (j >= this.bitmap.getWidth())
      i = this.bitmap.getWidth() - 1; 
    j = null;
    if (null >= this.bitmap.getHeight())
      j = this.bitmap.getHeight() - 1; 
    return this.bitmap.getPixel(i, j);
  }
  
  public List<LightView> getLightViews() {
    return this.mLightViews;
  }
  
  public boolean isToRemove() {
    return this.toRemove;
  }
  
  public void removeView(View paramView) {
    super.removeView(paramView);
    if (paramView instanceof LightView && this.mLightViews != null)
      this.mLightViews.remove(paramView); 
    if (paramView instanceof AllLightView)
      this.mAllLightView = null; 
  }
  
  public void setImageBitmap(Bitmap paramBitmap) {
    this.mPickerBackBg.setImageBitmap(paramBitmap);
  }
  
  public void setImageDrawable(Drawable paramDrawable) {
    this.bitmap = ((BitmapDrawable)paramDrawable).getBitmap();
    this.mPickerBackBg.setImageDrawable(paramDrawable);
  }
  
  public void setOnColorChangedListenner(OnColorChangedListener paramOnColorChangedListener) {
    this.mChangedListener = paramOnColorChangedListener;
  }
  
  public void setOnDoubleClickListener(OnDoubleClickListener paramOnDoubleClickListener) {
    this.doubleClickListener = paramOnDoubleClickListener;
  }
  
  public void setToRemove(boolean paramBoolean) {
    this.toRemove = paramBoolean;
  }
  
  class MyClickListener implements View.OnClickListener {
    public Handler mHandler = new Handler() {
        public void handleMessage(Message param2Message) {
          int i = param2Message.arg1;
          if (MyColorPickerView.MyClickListener.this.onclickTimes == i);
        }
      };
    
    long mLastTime = 0L;
    
    private int onclickTimes = 0;
    
    private void isSingClick(int param1Int) {
      Message message = this.mHandler.obtainMessage();
      message.arg1 = param1Int;
      this.mHandler.sendMessageDelayed(message, 500L);
    }
    
    public void onClick(View param1View) {
      this.onclickTimes++;
      long l = System.currentTimeMillis();
      if (this.mLastTime != 0L && l - this.mLastTime < 500L) {
        this.mLastTime = l - 500L;
        if (MyColorPickerView.this.toRemove)
          MyColorPickerView.this.removeView(param1View); 
        if (param1View instanceof LightView && MyColorPickerView.this.doubleClickListener != null)
          MyColorPickerView.this.doubleClickListener.doubleClick((LightView)param1View); 
        return;
      } 
      isSingClick(this.onclickTimes);
      this.mLastTime = l;
    }
  }
  
  class null extends Handler {
    public void handleMessage(Message param1Message) {
      int i = param1Message.arg1;
      if (this.this$1.onclickTimes == i);
    }
  }
  
  public static interface OnColorChangedListener {
    void onColorChanged(int param1Int1, int param1Int2, int param1Int3, View param1View, boolean param1Boolean);
    
    void onMoveColor(int param1Int1, int param1Int2, int param1Int3, View param1View, boolean param1Boolean);
  }
  
  public static interface OnDoubleClickListener {
    void doubleClick(LightView param1LightView);
    
    void sigleOnClick(LightView param1LightView);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/MyColorPickerView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */