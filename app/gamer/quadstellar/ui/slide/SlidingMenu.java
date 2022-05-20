package app.gamer.quadstellar.ui.slide;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import app.gamer.quadstellar.R;
import app.gamer.quadstellar.utils.LogUtil;

public class SlidingMenu extends RelativeLayout {
  public static final int LEFT = 0;
  
  public static final int LEFT_RIGHT = 2;
  
  public static final int RIGHT = 1;
  
  public static final int SLIDING_CONTENT = 1;
  
  public static final int SLIDING_WINDOW = 0;
  
  private static final String TAG = SlidingMenu.class.getSimpleName();
  
  public static final int TOUCHMODE_FULLSCREEN = 1;
  
  public static final int TOUCHMODE_MARGIN = 0;
  
  public static final int TOUCHMODE_NONE = 2;
  
  private boolean mActionbarOverlay = false;
  
  private OnCloseListener mCloseListener;
  
  private OnOpenListener mOpenListener;
  
  private OnOpenListener mSecondaryOpenListner;
  
  private CustomViewAbove mViewAbove;
  
  private ImageView mViewBackground;
  
  private CustomViewBehind mViewBehind;
  
  public SlidingMenu(Activity paramActivity, int paramInt) {
    this((Context)paramActivity, (AttributeSet)null);
    attachToActivity(paramActivity, paramInt);
  }
  
  public SlidingMenu(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SlidingMenu(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SlidingMenu(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
    this.mViewBackground = new ImageView(paramContext);
    this.mViewBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
    addView((View)this.mViewBackground, (ViewGroup.LayoutParams)layoutParams);
    layoutParams = new RelativeLayout.LayoutParams(-1, -1);
    this.mViewBehind = new CustomViewBehind(paramContext);
    addView((View)this.mViewBehind, (ViewGroup.LayoutParams)layoutParams);
    layoutParams = new RelativeLayout.LayoutParams(-1, -1);
    this.mViewAbove = new CustomViewAbove(paramContext);
    addView((View)this.mViewAbove, (ViewGroup.LayoutParams)layoutParams);
    this.mViewAbove.setCustomViewBehind(this.mViewBehind);
    this.mViewBehind.setCustomViewAbove(this.mViewAbove);
    this.mViewAbove.setOnPageChangeListener(new CustomViewAbove.OnPageChangeListener() {
          public static final int POSITION_CLOSE = 1;
          
          public static final int POSITION_OPEN = 0;
          
          public static final int POSITION_SECONDARY_OPEN = 2;
          
          public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}
          
          public void onPageSelected(int param1Int) {
            if (param1Int == 0 && SlidingMenu.this.mOpenListener != null) {
              SlidingMenu.this.mOpenListener.onOpen();
              return;
            } 
            if (param1Int == 1 && SlidingMenu.this.mCloseListener != null) {
              SlidingMenu.this.mCloseListener.onClose();
              return;
            } 
            if (param1Int == 2 && SlidingMenu.this.mSecondaryOpenListner != null)
              SlidingMenu.this.mSecondaryOpenListner.onOpen(); 
          }
        });
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SlidingMenu);
    setMode(typedArray.getInt(0, 0));
    paramInt = typedArray.getResourceId(1, -1);
    if (paramInt != -1) {
      setContent(paramInt);
    } else {
      setContent((View)new FrameLayout(paramContext));
    } 
    paramInt = typedArray.getResourceId(2, -1);
    if (paramInt != -1) {
      setMenu(paramInt);
    } else {
      setMenu((View)new FrameLayout(paramContext));
    } 
    setTouchModeAbove(typedArray.getInt(6, 0));
    setTouchModeBehind(typedArray.getInt(7, 0));
    paramInt = (int)typedArray.getDimension(3, -1.0F);
    int i = (int)typedArray.getDimension(4, -1.0F);
    if (paramInt != -1 && i != -1)
      throw new IllegalStateException("Cannot set both behindOffset and behindWidth for a SlidingMenu"); 
    if (paramInt != -1) {
      setBehindOffset(paramInt);
    } else if (i != -1) {
      setBehindWidth(i);
    } else {
      setBehindOffset(0);
    } 
    setBehindScrollScale(typedArray.getFloat(5, 0.33F));
    paramInt = typedArray.getResourceId(8, -1);
    if (paramInt != -1)
      setShadowDrawable(paramInt); 
    setShadowWidth((int)typedArray.getDimension(9, 0.0F));
    setFadeEnabled(typedArray.getBoolean(10, true));
    setFadeDegree(typedArray.getFloat(11, 0.33F));
    setSelectorEnabled(typedArray.getBoolean(12, false));
    paramInt = typedArray.getResourceId(13, -1);
    if (paramInt != -1)
      setSelectorDrawable(paramInt); 
    typedArray.recycle();
  }
  
  public void addIgnoredView(View paramView) {
    this.mViewAbove.addIgnoredView(paramView);
  }
  
  public void attachToActivity(Activity paramActivity, int paramInt) {
    attachToActivity(paramActivity, paramInt, false);
  }
  
  public void attachToActivity(Activity paramActivity, int paramInt, boolean paramBoolean) {
    ViewGroup viewGroup1;
    if (paramInt != 0 && paramInt != 1)
      throw new IllegalArgumentException("slideStyle must be either SLIDING_WINDOW or SLIDING_CONTENT"); 
    if (getParent() != null)
      throw new IllegalStateException("This SlidingMenu appears to already be attached"); 
    TypedArray typedArray = paramActivity.getTheme().obtainStyledAttributes(new int[] { 16842836 });
    int i = typedArray.getResourceId(0, 0);
    typedArray.recycle();
    switch (paramInt) {
      default:
        return;
      case 0:
        this.mActionbarOverlay = false;
        viewGroup1 = (ViewGroup)paramActivity.getWindow().getDecorView();
        viewGroup2 = (ViewGroup)viewGroup1.getChildAt(0);
        viewGroup2.setBackgroundResource(i);
        viewGroup1.removeView((View)viewGroup2);
        viewGroup1.addView((View)this);
        setContent((View)viewGroup2);
      case 1:
        break;
    } 
    this.mActionbarOverlay = paramBoolean;
    ViewGroup viewGroup2 = (ViewGroup)viewGroup1.findViewById(16908290);
    View view = viewGroup2.getChildAt(0);
    viewGroup2.removeView(view);
    viewGroup2.addView((View)this);
    setContent(view);
    if (view.getBackground() == null)
      view.setBackgroundResource(i); 
  }
  
  public void clearIgnoredViews() {
    this.mViewAbove.clearIgnoredViews();
  }
  
  @SuppressLint({"NewApi"})
  protected boolean fitSystemWindows(Rect paramRect) {
    int i = paramRect.left;
    int j = paramRect.right;
    int k = paramRect.top;
    int m = paramRect.bottom;
    if (!this.mActionbarOverlay) {
      LogUtil.v(TAG, "setting padding!");
      setPadding(i, k, j, m);
    } 
    return true;
  }
  
  public int getBehindOffset() {
    return ((RelativeLayout.LayoutParams)this.mViewBehind.getLayoutParams()).rightMargin;
  }
  
  public float getBehindScrollScale() {
    return this.mViewBehind.getScrollScale();
  }
  
  public View getContent() {
    return this.mViewAbove.getContent();
  }
  
  public View getMenu() {
    return this.mViewBehind.getContent();
  }
  
  public int getMode() {
    return this.mViewBehind.getMode();
  }
  
  public View getSecondaryMenu() {
    return this.mViewBehind.getSecondaryContent();
  }
  
  public int getTouchModeAbove() {
    return this.mViewAbove.getTouchMode();
  }
  
  public int getTouchmodeMarginThreshold() {
    return this.mViewBehind.getMarginThreshold();
  }
  
  public boolean isMenuShowing() {
    return (this.mViewAbove.getCurrentItem() == 0 || this.mViewAbove.getCurrentItem() == 2);
  }
  
  public boolean isSecondaryMenuShowing() {
    return (this.mViewAbove.getCurrentItem() == 2);
  }
  
  public boolean isSlidingEnabled() {
    return this.mViewAbove.isSlidingEnabled();
  }
  
  @TargetApi(11)
  public void manageLayers(float paramFloat) {
    final byte layerType = 0;
    if (Build.VERSION.SDK_INT >= 11) {
      boolean bool;
      if (paramFloat > 0.0F && paramFloat < 1.0F) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool)
        b = 2; 
      if (b != getContent().getLayerType())
        getHandler().post(new Runnable() {
              public void run() {
                boolean bool;
                String str = SlidingMenu.TAG;
                StringBuilder stringBuilder = (new StringBuilder()).append("changing layerType. hardware? ");
                if (layerType == 2) {
                  bool = true;
                } else {
                  bool = false;
                } 
                LogUtil.v(str, stringBuilder.append(bool).toString());
                SlidingMenu.this.getContent().setLayerType(layerType, null);
                SlidingMenu.this.getMenu().setLayerType(layerType, null);
                if (SlidingMenu.this.getSecondaryMenu() != null)
                  SlidingMenu.this.getSecondaryMenu().setLayerType(layerType, null); 
              }
            }); 
    } 
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.mViewAbove.setCurrentItem(savedState.getItem());
  }
  
  protected Parcelable onSaveInstanceState() {
    return (Parcelable)new SavedState(super.onSaveInstanceState(), this.mViewAbove.getCurrentItem());
  }
  
  public void removeIgnoredView(View paramView) {
    this.mViewAbove.removeIgnoredView(paramView);
  }
  
  public void setAboveCanvasTransformer(CanvasTransformer paramCanvasTransformer) {
    this.mViewAbove.setCanvasTransformer(paramCanvasTransformer);
  }
  
  public void setAboveOffset(int paramInt) {
    this.mViewAbove.setAboveOffset(paramInt);
  }
  
  public void setAboveOffsetRes(int paramInt) {
    setAboveOffset((int)getContext().getResources().getDimension(paramInt));
  }
  
  public void setBackgroundImage(int paramInt) {
    this.mViewBackground.setBackgroundResource(paramInt);
  }
  
  public void setBehindCanvasTransformer(CanvasTransformer paramCanvasTransformer) {
    this.mViewBehind.setCanvasTransformer(paramCanvasTransformer);
  }
  
  public void setBehindOffset(int paramInt) {
    this.mViewBehind.setWidthOffset(paramInt);
  }
  
  public void setBehindOffsetRes(int paramInt) {
    setBehindOffset((int)getContext().getResources().getDimension(paramInt));
  }
  
  public void setBehindScrollScale(float paramFloat) {
    if (paramFloat < 0.0F && paramFloat > 1.0F)
      throw new IllegalStateException("ScrollScale must be between 0 and 1"); 
    this.mViewBehind.setScrollScale(paramFloat);
  }
  
  public void setBehindWidth(int paramInt) {
    int i;
    Display display = ((WindowManager)getContext().getSystemService("window")).getDefaultDisplay();
    try {
      Point point = new Point();
      this();
      Display.class.getMethod("getSize", new Class[] { Point.class }).invoke(display, new Object[] { point });
      i = point.x;
    } catch (Exception exception) {
      i = display.getWidth();
    } 
    setBehindOffset(i - paramInt);
  }
  
  public void setBehindWidthRes(int paramInt) {
    setBehindWidth((int)getContext().getResources().getDimension(paramInt));
  }
  
  public void setContent(int paramInt) {
    setContent(LayoutInflater.from(getContext()).inflate(paramInt, null));
  }
  
  public void setContent(View paramView) {
    this.mViewAbove.setContent(paramView);
    showContent();
  }
  
  public void setFadeDegree(float paramFloat) {
    this.mViewBehind.setFadeDegree(paramFloat);
  }
  
  public void setFadeEnabled(boolean paramBoolean) {
    this.mViewBehind.setFadeEnabled(paramBoolean);
  }
  
  public void setMenu(int paramInt) {
    setMenu(LayoutInflater.from(getContext()).inflate(paramInt, null));
  }
  
  public void setMenu(View paramView) {
    this.mViewBehind.setContent(paramView);
  }
  
  public void setMode(int paramInt) {
    if (paramInt != 0 && paramInt != 1 && paramInt != 2)
      throw new IllegalStateException("SlidingMenu mode must be LEFT, RIGHT, or LEFT_RIGHT"); 
    this.mViewBehind.setMode(paramInt);
  }
  
  public void setOnCloseListener(OnCloseListener paramOnCloseListener) {
    this.mCloseListener = paramOnCloseListener;
  }
  
  public void setOnClosedListener(OnClosedListener paramOnClosedListener) {
    this.mViewAbove.setOnClosedListener(paramOnClosedListener);
  }
  
  public void setOnOpenListener(OnOpenListener paramOnOpenListener) {
    this.mOpenListener = paramOnOpenListener;
  }
  
  public void setOnOpenedListener(OnOpenedListener paramOnOpenedListener) {
    this.mViewAbove.setOnOpenedListener(paramOnOpenedListener);
  }
  
  public void setSecondaryMenu(int paramInt) {
    setSecondaryMenu(LayoutInflater.from(getContext()).inflate(paramInt, null));
  }
  
  public void setSecondaryMenu(View paramView) {
    this.mViewBehind.setSecondaryContent(paramView);
  }
  
  public void setSecondaryOnOpenListner(OnOpenListener paramOnOpenListener) {
    this.mSecondaryOpenListner = paramOnOpenListener;
  }
  
  public void setSecondaryShadowDrawable(int paramInt) {
    setSecondaryShadowDrawable(getContext().getResources().getDrawable(paramInt));
  }
  
  public void setSecondaryShadowDrawable(Drawable paramDrawable) {
    this.mViewBehind.setSecondaryShadowDrawable(paramDrawable);
  }
  
  public void setSelectedView(View paramView) {
    this.mViewBehind.setSelectedView(paramView);
  }
  
  public void setSelectorBitmap(Bitmap paramBitmap) {
    this.mViewBehind.setSelectorBitmap(paramBitmap);
  }
  
  public void setSelectorDrawable(int paramInt) {
    this.mViewBehind.setSelectorBitmap(BitmapFactory.decodeResource(getResources(), paramInt));
  }
  
  public void setSelectorEnabled(boolean paramBoolean) {
    this.mViewBehind.setSelectorEnabled(true);
  }
  
  public void setShadowDrawable(int paramInt) {
    setShadowDrawable(getContext().getResources().getDrawable(paramInt));
  }
  
  public void setShadowDrawable(Drawable paramDrawable) {
    this.mViewBehind.setShadowDrawable(paramDrawable);
  }
  
  public void setShadowWidth(int paramInt) {
    this.mViewBehind.setShadowWidth(paramInt);
  }
  
  public void setShadowWidthRes(int paramInt) {
    setShadowWidth((int)getResources().getDimension(paramInt));
  }
  
  public void setSlidingEnabled(boolean paramBoolean) {
    this.mViewAbove.setSlidingEnabled(paramBoolean);
  }
  
  public void setStatic(boolean paramBoolean) {
    if (paramBoolean) {
      setSlidingEnabled(false);
      this.mViewAbove.setCustomViewBehind(null);
      this.mViewAbove.setCurrentItem(1);
      return;
    } 
    this.mViewAbove.setCurrentItem(1);
    this.mViewAbove.setCustomViewBehind(this.mViewBehind);
    setSlidingEnabled(true);
  }
  
  public void setTouchModeAbove(int paramInt) {
    if (paramInt != 1 && paramInt != 0 && paramInt != 2)
      throw new IllegalStateException("TouchMode must be set to eitherTOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN or TOUCHMODE_NONE."); 
    this.mViewAbove.setTouchMode(paramInt);
  }
  
  public void setTouchModeBehind(int paramInt) {
    if (paramInt != 1 && paramInt != 0 && paramInt != 2)
      throw new IllegalStateException("TouchMode must be set to eitherTOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN or TOUCHMODE_NONE."); 
    this.mViewBehind.setTouchMode(paramInt);
  }
  
  public void setTouchmodeMarginThreshold(int paramInt) {
    this.mViewBehind.setMarginThreshold(paramInt);
  }
  
  public void showContent() {
    showContent(true);
  }
  
  public void showContent(boolean paramBoolean) {
    this.mViewAbove.setCurrentItem(1, paramBoolean);
  }
  
  public void showMenu() {
    showMenu(true);
  }
  
  public void showMenu(boolean paramBoolean) {
    this.mViewAbove.setCurrentItem(0, paramBoolean);
  }
  
  public void showSecondaryMenu() {
    showSecondaryMenu(true);
  }
  
  public void showSecondaryMenu(boolean paramBoolean) {
    this.mViewAbove.setCurrentItem(2, paramBoolean);
  }
  
  public void toggle() {
    toggle(true);
  }
  
  public void toggle(boolean paramBoolean) {
    if (isMenuShowing()) {
      showContent(paramBoolean);
      return;
    } 
    showMenu(paramBoolean);
  }
  
  public static interface CanvasTransformer {
    void transformCanvas(Canvas param1Canvas, float param1Float);
  }
  
  public static interface OnCloseListener {
    void onClose();
  }
  
  public static interface OnClosedListener {
    void onClosed();
  }
  
  public static interface OnOpenListener {
    void onOpen();
  }
  
  public static interface OnOpenedListener {
    void onOpened();
  }
  
  public static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public SlidingMenu.SavedState createFromParcel(Parcel param2Parcel) {
          return new SlidingMenu.SavedState(param2Parcel);
        }
        
        public SlidingMenu.SavedState[] newArray(int param2Int) {
          return new SlidingMenu.SavedState[param2Int];
        }
      };
    
    private final int mItem;
    
    private SavedState(Parcel param1Parcel) {
      super(param1Parcel);
      this.mItem = param1Parcel.readInt();
    }
    
    public SavedState(Parcelable param1Parcelable, int param1Int) {
      super(param1Parcelable);
      this.mItem = param1Int;
    }
    
    public int getItem() {
      return this.mItem;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.mItem);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public SlidingMenu.SavedState createFromParcel(Parcel param1Parcel) {
      return new SlidingMenu.SavedState(param1Parcel);
    }
    
    public SlidingMenu.SavedState[] newArray(int param1Int) {
      return new SlidingMenu.SavedState[param1Int];
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/slide/SlidingMenu.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */