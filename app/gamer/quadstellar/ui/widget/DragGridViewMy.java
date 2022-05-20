package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import app.gamer.quadstellar.utils.DensityUtils;

public class DragGridViewMy extends GridView {
  private MotionEvent downEv;
  
  public int downX;
  
  public int downY;
  
  private Bitmap dragBitmap;
  
  private View dragImageView = null;
  
  private ViewGroup dragItemView = null;
  
  int dragOffsetX;
  
  int dragOffsetY;
  
  public int dragPosition;
  
  private double dragScale = 1.0D;
  
  private boolean isMoving = false;
  
  public int lastY;
  
  private DragGridViewListener listener;
  
  private int mHorizontalSpacing = 15;
  
  private DragState mState = DragState.TOUCH_DRAG;
  
  private Vibrator mVibrator;
  
  private int win_view_x;
  
  private int win_view_y;
  
  private WindowManager windowManager = null;
  
  private WindowManager.LayoutParams windowParams = null;
  
  public int windowX;
  
  public int windowY;
  
  public DragGridViewMy(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  public DragGridViewMy(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public DragGridViewMy(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private void onDrag(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.dragImageView != null) {
      this.windowParams.alpha = 0.6F;
      this.windowParams.x = paramInt3 - this.win_view_x;
      this.windowParams.y = paramInt4 - this.win_view_y;
      this.windowManager.updateViewLayout(this.dragImageView, (ViewGroup.LayoutParams)this.windowParams);
    } 
  }
  
  private void setTouchDragStart(MotionEvent paramMotionEvent) {
    int i = (int)paramMotionEvent.getX();
    int j = (int)paramMotionEvent.getY();
    this.dragPosition = pointToPosition(i, j);
    if (this.dragPosition != -1) {
      this.dragItemView = (ViewGroup)getChildAt(this.dragPosition - getFirstVisiblePosition());
      this.win_view_x = this.windowX - this.dragItemView.getLeft();
      this.win_view_y = this.windowY - this.dragItemView.getTop();
      this.dragOffsetX = (int)(paramMotionEvent.getRawX() - i);
      this.dragOffsetY = (int)(paramMotionEvent.getRawY() - j);
      this.dragItemView.destroyDrawingCache();
      this.dragItemView.setDrawingCacheEnabled(true);
      this.dragBitmap = Bitmap.createBitmap(this.dragItemView.getDrawingCache());
      startDrag(this.dragBitmap, (int)paramMotionEvent.getRawX(), (int)paramMotionEvent.getRawY());
      this.dragItemView.setVisibility(4);
      this.isMoving = true;
      this.lastY = (int)paramMotionEvent.getRawY();
      requestDisallowInterceptTouchEvent(false);
    } 
  }
  
  private void stopDrag() {
    if (this.dragImageView != null) {
      this.windowManager.removeView(this.dragImageView);
      this.dragImageView = null;
    } 
    if (this.dragItemView != null)
      this.dragItemView.setVisibility(0); 
  }
  
  public boolean getMovingState() {
    return this.isMoving;
  }
  
  public DragState getState() {
    return this.mState;
  }
  
  public void init(Context paramContext) {
    if (!isInEditMode()) {
      this.mVibrator = (Vibrator)paramContext.getSystemService("vibrator");
      this.mHorizontalSpacing = DensityUtils.dip2px(paramContext, this.mHorizontalSpacing);
    } 
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    if (paramMotionEvent.getAction() == 0) {
      this.downX = (int)paramMotionEvent.getX();
      this.downY = (int)paramMotionEvent.getY();
      this.windowX = (int)paramMotionEvent.getX();
      this.windowY = (int)paramMotionEvent.getY();
      this.downEv = paramMotionEvent;
    } 
    return super.onInterceptTouchEvent(paramMotionEvent);
  }
  
  public void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(536870911, -2147483648));
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool = false;
    if (this.mState == DragState.TOUCH_DRAG) {
      int i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      switch (paramMotionEvent.getAction()) {
        default:
          return super.onTouchEvent(paramMotionEvent);
        case 0:
          this.downX = (int)paramMotionEvent.getX();
          this.windowX = (int)paramMotionEvent.getX();
          this.downY = (int)paramMotionEvent.getY();
          this.windowY = (int)paramMotionEvent.getY();
        case 2:
          if (this.dragImageView == null)
            setTouchDragStart(paramMotionEvent); 
          if (this.dragPosition != -1 && this.dragPosition == 0) {
            int k = (int)paramMotionEvent.getRawX();
            int m = (int)paramMotionEvent.getRawY();
            onDrag(i, j, k, m);
            if (this.listener != null && this.windowParams != null && this.dragImageView != null) {
              if (m < this.lastY)
                bool = true; 
              j = this.lastY;
              this.listener.onMoving(this.windowParams.x, this.windowParams.y, bool, m - j);
            } 
            this.lastY = m;
            requestDisallowInterceptTouchEvent(true);
          } 
        case 1:
        case 3:
          break;
      } 
      this.isMoving = false;
      if (this.listener != null && this.windowParams != null && this.dragImageView != null) {
        DragGridViewListener dragGridViewListener = this.listener;
        j = this.windowParams.x;
        int m = this.dragImageView.getWidth() / 2;
        int k = this.windowParams.y;
        dragGridViewListener.onDrop(this, m + j, this.dragImageView.getHeight() / 2 + k, getItemAtPosition(this.dragPosition), this.dragBitmap);
      } 
      stopDrag();
      requestDisallowInterceptTouchEvent(false);
    } 
  }
  
  public void setDragGridViewListener(DragGridViewListener paramDragGridViewListener) {
    this.listener = paramDragGridViewListener;
  }
  
  public void setOnItemClickListener(final MotionEvent ev) {
    setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          public boolean onItemLongClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            int i = (int)ev.getX();
            int j = (int)ev.getY();
            DragGridViewMy.this.dragPosition = param1Int;
            if (DragGridViewMy.this.dragPosition != -1) {
              DragGridViewMy.access$002(DragGridViewMy.this, (ViewGroup)DragGridViewMy.this.getChildAt(DragGridViewMy.this.dragPosition - DragGridViewMy.this.getFirstVisiblePosition()));
              DragGridViewMy.access$102(DragGridViewMy.this, DragGridViewMy.this.windowX - DragGridViewMy.this.dragItemView.getLeft());
              DragGridViewMy.access$202(DragGridViewMy.this, DragGridViewMy.this.windowY - DragGridViewMy.this.dragItemView.getTop());
              DragGridViewMy.this.dragOffsetX = (int)(ev.getRawX() - i);
              DragGridViewMy.this.dragOffsetY = (int)(ev.getRawY() - j);
              DragGridViewMy.this.dragItemView.destroyDrawingCache();
              DragGridViewMy.this.dragItemView.setDrawingCacheEnabled(true);
              DragGridViewMy.access$302(DragGridViewMy.this, Bitmap.createBitmap(DragGridViewMy.this.dragItemView.getDrawingCache()));
              DragGridViewMy.this.mVibrator.vibrate(50L);
              DragGridViewMy.this.startDrag(DragGridViewMy.this.dragBitmap, (int)ev.getRawX(), (int)ev.getRawY());
              DragGridViewMy.this.dragItemView.setVisibility(4);
              DragGridViewMy.access$502(DragGridViewMy.this, true);
              DragGridViewMy.this.lastY = (int)ev.getRawY();
              DragGridViewMy.this.requestDisallowInterceptTouchEvent(true);
              return true;
            } 
            return false;
          }
        });
  }
  
  public void setState(DragState paramDragState) {
    this.mState = paramDragState;
  }
  
  public void startDrag(Bitmap paramBitmap, int paramInt1, int paramInt2) {
    stopDrag();
    this.windowParams = new WindowManager.LayoutParams();
    this.windowParams.gravity = 51;
    this.windowParams.x = paramInt1 - this.win_view_x;
    this.windowParams.y = paramInt2 - this.win_view_y;
    this.windowParams.width = (int)(this.dragScale * paramBitmap.getWidth());
    this.windowParams.height = (int)(this.dragScale * paramBitmap.getHeight());
    this.windowParams.flags = 408;
    this.windowParams.format = -3;
    this.windowParams.windowAnimations = 0;
    ImageView imageView = new ImageView(getContext());
    imageView.setImageBitmap(paramBitmap);
    this.windowManager = (WindowManager)getContext().getSystemService("window");
    this.windowManager.addView((View)imageView, (ViewGroup.LayoutParams)this.windowParams);
    this.dragImageView = (View)imageView;
  }
  
  public static interface DragGridViewListener {
    void onDrop(DragGridViewMy param1DragGridViewMy, int param1Int1, int param1Int2, Object param1Object, Bitmap param1Bitmap);
    
    void onMoving(int param1Int1, int param1Int2, boolean param1Boolean, int param1Int3);
  }
  
  public enum DragState {
    LONG_CLICK_DRAG, NONE, TOUCH_DRAG;
    
    static {
      $VALUES = new DragState[] { NONE, LONG_CLICK_DRAG, TOUCH_DRAG };
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/DragGridViewMy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */