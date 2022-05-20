package app.gamer.quadstellar.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import app.gamer.quadstellar.mode.EFDeviceLight;
import java.util.List;

public class AllLightControlView extends ImageView implements View.OnLongClickListener {
  private View dragImageView = null;
  
  private List<EFDeviceLight> lightList;
  
  private OnMoveUpListener listener;
  
  private ScrollView mScrollView;
  
  private Vibrator mVibrator;
  
  private int win_view_x;
  
  private int win_view_y;
  
  private WindowManager windowManager = null;
  
  private WindowManager.LayoutParams windowParams = null;
  
  public AllLightControlView(Context paramContext) {
    super(paramContext);
    init(paramContext);
  }
  
  public AllLightControlView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public AllLightControlView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private void init(Context paramContext) {
    if (!isInEditMode()) {
      this.mVibrator = (Vibrator)paramContext.getSystemService("vibrator");
      setOnLongClickListener(this);
    } 
  }
  
  private void onDrag(int paramInt1, int paramInt2) {
    if (this.dragImageView != null) {
      this.windowParams.alpha = 0.6F;
      this.windowParams.x = paramInt1;
      this.windowParams.y = paramInt2;
      this.windowManager.updateViewLayout(this.dragImageView, (ViewGroup.LayoutParams)this.windowParams);
      int[] arrayOfInt = new int[2];
      this.dragImageView.getLocationOnScreen(arrayOfInt);
    } 
  }
  
  private void stopDrag() {
    if (this.dragImageView != null) {
      this.windowManager.removeView(this.dragImageView);
      this.dragImageView = null;
    } 
  }
  
  public List<EFDeviceLight> getLightList() {
    return this.lightList;
  }
  
  public ScrollView getScrollView() {
    return this.mScrollView;
  }
  
  public boolean onLongClick(View paramView) {
    paramView.destroyDrawingCache();
    paramView.setDrawingCacheEnabled(true);
    Bitmap bitmap = Bitmap.createBitmap(paramView.getDrawingCache());
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    this.win_view_x = arrayOfInt[0];
    this.win_view_y = arrayOfInt[1];
    this.mVibrator.vibrate(50L);
    startDrag(bitmap, this.win_view_x, this.win_view_y);
    return true;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (this.dragImageView != null) {
      switch (paramMotionEvent.getAction()) {
        default:
          return super.onTouchEvent(paramMotionEvent);
        case 2:
          onDrag((int)paramMotionEvent.getRawX(), (int)paramMotionEvent.getRawY());
          if (this.listener != null)
            this.listener.onMoving((int)paramMotionEvent.getRawX(), (int)paramMotionEvent.getRawY()); 
          this.mScrollView.requestDisallowInterceptTouchEvent(true);
        case 1:
          break;
      } 
      stopDrag();
      if (this.listener != null)
        this.listener.onUp((int)paramMotionEvent.getRawX(), (int)paramMotionEvent.getRawY()); 
      this.mScrollView.requestDisallowInterceptTouchEvent(false);
    } 
  }
  
  public void setLightList(List<EFDeviceLight> paramList) {
    this.lightList = paramList;
  }
  
  public void setOnMoveUpListener(OnMoveUpListener paramOnMoveUpListener) {
    this.listener = paramOnMoveUpListener;
  }
  
  public void setScrollView(ScrollView paramScrollView) {
    this.mScrollView = paramScrollView;
  }
  
  public void startDrag(Bitmap paramBitmap, int paramInt1, int paramInt2) {
    stopDrag();
    this.windowParams = new WindowManager.LayoutParams();
    this.windowParams.gravity = 51;
    this.windowParams.x = paramInt1;
    this.windowParams.y = paramInt2;
    this.windowParams.width = (int)(paramBitmap.getWidth() * 1.2D);
    this.windowParams.height = (int)(paramBitmap.getHeight() * 1.2D);
    this.windowParams.flags = 408;
    this.windowParams.format = -3;
    this.windowParams.windowAnimations = 0;
    ImageView imageView = new ImageView(getContext());
    imageView.setImageBitmap(paramBitmap);
    this.windowManager = (WindowManager)getContext().getSystemService("window");
    this.windowManager.addView((View)imageView, (ViewGroup.LayoutParams)this.windowParams);
    this.dragImageView = (View)imageView;
  }
  
  public static interface OnMoveUpListener {
    void onMoving(int param1Int1, int param1Int2);
    
    void onUp(int param1Int1, int param1Int2);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/AllLightControlView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */