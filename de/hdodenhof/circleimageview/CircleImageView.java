package de.hdodenhof.circleimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {
  private static final Bitmap.Config BITMAP_CONFIG;
  
  private static final int COLORDRAWABLE_DIMENSION = 2;
  
  private static final int DEFAULT_BORDER_COLOR = -16777216;
  
  private static final boolean DEFAULT_BORDER_OVERLAY = false;
  
  private static final int DEFAULT_BORDER_WIDTH = 0;
  
  private static final int DEFAULT_FILL_COLOR = 0;
  
  private static final ImageView.ScaleType SCALE_TYPE = ImageView.ScaleType.CENTER_CROP;
  
  private Bitmap mBitmap;
  
  private int mBitmapHeight;
  
  private final Paint mBitmapPaint = new Paint();
  
  private BitmapShader mBitmapShader;
  
  private int mBitmapWidth;
  
  private int mBorderColor = -16777216;
  
  private boolean mBorderOverlay;
  
  private final Paint mBorderPaint = new Paint();
  
  private float mBorderRadius;
  
  private final RectF mBorderRect = new RectF();
  
  private int mBorderWidth = 0;
  
  private ColorFilter mColorFilter;
  
  private boolean mDisableCircularTransformation;
  
  private float mDrawableRadius;
  
  private final RectF mDrawableRect = new RectF();
  
  private int mFillColor = 0;
  
  private final Paint mFillPaint = new Paint();
  
  private boolean mReady;
  
  private boolean mSetupPending;
  
  private final Matrix mShaderMatrix = new Matrix();
  
  static {
    BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
  }
  
  public CircleImageView(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public CircleImageView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public CircleImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CircleImageView, paramInt, 0);
    this.mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, 0);
    this.mBorderColor = typedArray.getColor(R.styleable.CircleImageView_civ_border_color, -16777216);
    this.mBorderOverlay = typedArray.getBoolean(R.styleable.CircleImageView_civ_border_overlay, false);
    this.mFillColor = typedArray.getColor(R.styleable.CircleImageView_civ_fill_color, 0);
    typedArray.recycle();
    init();
  }
  
  private void applyColorFilter() {
    if (this.mBitmapPaint != null)
      this.mBitmapPaint.setColorFilter(this.mColorFilter); 
  }
  
  private RectF calculateBounds() {
    int i = getWidth() - getPaddingLeft() - getPaddingRight();
    int j = getHeight() - getPaddingTop() - getPaddingBottom();
    int k = Math.min(i, j);
    float f1 = getPaddingLeft() + (i - k) / 2.0F;
    float f2 = getPaddingTop() + (j - k) / 2.0F;
    return new RectF(f1, f2, k + f1, k + f2);
  }
  
  private Bitmap getBitmapFromDrawable(Drawable paramDrawable) {
    if (paramDrawable == null)
      return null; 
    if (paramDrawable instanceof BitmapDrawable)
      return ((BitmapDrawable)paramDrawable).getBitmap(); 
    try {
      Bitmap bitmap2;
      if (paramDrawable instanceof android.graphics.drawable.ColorDrawable) {
        bitmap2 = Bitmap.createBitmap(2, 2, BITMAP_CONFIG);
      } else {
        bitmap2 = Bitmap.createBitmap(paramDrawable.getIntrinsicWidth(), paramDrawable.getIntrinsicHeight(), BITMAP_CONFIG);
      } 
      Canvas canvas = new Canvas();
      this(bitmap2);
      paramDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      paramDrawable.draw(canvas);
      Bitmap bitmap1 = bitmap2;
    } catch (Exception exception) {
      exception.printStackTrace();
      exception = null;
    } 
    return (Bitmap)exception;
  }
  
  private void init() {
    super.setScaleType(SCALE_TYPE);
    this.mReady = true;
    if (this.mSetupPending) {
      setup();
      this.mSetupPending = false;
    } 
  }
  
  private void initializeBitmap() {
    if (this.mDisableCircularTransformation) {
      this.mBitmap = null;
    } else {
      this.mBitmap = getBitmapFromDrawable(getDrawable());
    } 
    setup();
  }
  
  private void setup() {
    if (!this.mReady) {
      this.mSetupPending = true;
      return;
    } 
    if (getWidth() != 0 || getHeight() != 0) {
      if (this.mBitmap == null) {
        invalidate();
        return;
      } 
      this.mBitmapShader = new BitmapShader(this.mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
      this.mBitmapPaint.setAntiAlias(true);
      this.mBitmapPaint.setShader((Shader)this.mBitmapShader);
      this.mBorderPaint.setStyle(Paint.Style.STROKE);
      this.mBorderPaint.setAntiAlias(true);
      this.mBorderPaint.setColor(this.mBorderColor);
      this.mBorderPaint.setStrokeWidth(this.mBorderWidth);
      this.mFillPaint.setStyle(Paint.Style.FILL);
      this.mFillPaint.setAntiAlias(true);
      this.mFillPaint.setColor(this.mFillColor);
      this.mBitmapHeight = this.mBitmap.getHeight();
      this.mBitmapWidth = this.mBitmap.getWidth();
      this.mBorderRect.set(calculateBounds());
      this.mBorderRadius = Math.min((this.mBorderRect.height() - this.mBorderWidth) / 2.0F, (this.mBorderRect.width() - this.mBorderWidth) / 2.0F);
      this.mDrawableRect.set(this.mBorderRect);
      if (!this.mBorderOverlay && this.mBorderWidth > 0)
        this.mDrawableRect.inset(this.mBorderWidth - 1.0F, this.mBorderWidth - 1.0F); 
      this.mDrawableRadius = Math.min(this.mDrawableRect.height() / 2.0F, this.mDrawableRect.width() / 2.0F);
      applyColorFilter();
      updateShaderMatrix();
      invalidate();
    } 
  }
  
  private void updateShaderMatrix() {
    float f3;
    float f1 = 0.0F;
    float f2 = 0.0F;
    this.mShaderMatrix.set(null);
    if (this.mBitmapWidth * this.mDrawableRect.height() > this.mDrawableRect.width() * this.mBitmapHeight) {
      f3 = this.mDrawableRect.height() / this.mBitmapHeight;
      f1 = (this.mDrawableRect.width() - this.mBitmapWidth * f3) * 0.5F;
    } else {
      f3 = this.mDrawableRect.width() / this.mBitmapWidth;
      f2 = (this.mDrawableRect.height() - this.mBitmapHeight * f3) * 0.5F;
    } 
    this.mShaderMatrix.setScale(f3, f3);
    this.mShaderMatrix.postTranslate((int)(f1 + 0.5F) + this.mDrawableRect.left, (int)(f2 + 0.5F) + this.mDrawableRect.top);
    this.mBitmapShader.setLocalMatrix(this.mShaderMatrix);
  }
  
  public int getBorderColor() {
    return this.mBorderColor;
  }
  
  public int getBorderWidth() {
    return this.mBorderWidth;
  }
  
  public ColorFilter getColorFilter() {
    return this.mColorFilter;
  }
  
  @Deprecated
  public int getFillColor() {
    return this.mFillColor;
  }
  
  public ImageView.ScaleType getScaleType() {
    return SCALE_TYPE;
  }
  
  public boolean isBorderOverlay() {
    return this.mBorderOverlay;
  }
  
  public boolean isDisableCircularTransformation() {
    return this.mDisableCircularTransformation;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.mDisableCircularTransformation) {
      super.onDraw(paramCanvas);
      return;
    } 
    if (this.mBitmap != null) {
      if (this.mFillColor != 0)
        paramCanvas.drawCircle(this.mDrawableRect.centerX(), this.mDrawableRect.centerY(), this.mDrawableRadius, this.mFillPaint); 
      paramCanvas.drawCircle(this.mDrawableRect.centerX(), this.mDrawableRect.centerY(), this.mDrawableRadius, this.mBitmapPaint);
      if (this.mBorderWidth > 0)
        paramCanvas.drawCircle(this.mBorderRect.centerX(), this.mBorderRect.centerY(), this.mBorderRadius, this.mBorderPaint); 
    } 
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    setup();
  }
  
  public void setAdjustViewBounds(boolean paramBoolean) {
    if (paramBoolean)
      throw new IllegalArgumentException("adjustViewBounds not supported."); 
  }
  
  public void setBorderColor(@ColorInt int paramInt) {
    if (paramInt != this.mBorderColor) {
      this.mBorderColor = paramInt;
      this.mBorderPaint.setColor(this.mBorderColor);
      invalidate();
    } 
  }
  
  @Deprecated
  public void setBorderColorResource(@ColorRes int paramInt) {
    setBorderColor(getContext().getResources().getColor(paramInt));
  }
  
  public void setBorderOverlay(boolean paramBoolean) {
    if (paramBoolean != this.mBorderOverlay) {
      this.mBorderOverlay = paramBoolean;
      setup();
    } 
  }
  
  public void setBorderWidth(int paramInt) {
    if (paramInt != this.mBorderWidth) {
      this.mBorderWidth = paramInt;
      setup();
    } 
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    if (paramColorFilter != this.mColorFilter) {
      this.mColorFilter = paramColorFilter;
      applyColorFilter();
      invalidate();
    } 
  }
  
  public void setDisableCircularTransformation(boolean paramBoolean) {
    if (this.mDisableCircularTransformation != paramBoolean) {
      this.mDisableCircularTransformation = paramBoolean;
      initializeBitmap();
    } 
  }
  
  @Deprecated
  public void setFillColor(@ColorInt int paramInt) {
    if (paramInt != this.mFillColor) {
      this.mFillColor = paramInt;
      this.mFillPaint.setColor(paramInt);
      invalidate();
    } 
  }
  
  @Deprecated
  public void setFillColorResource(@ColorRes int paramInt) {
    setFillColor(getContext().getResources().getColor(paramInt));
  }
  
  public void setImageBitmap(Bitmap paramBitmap) {
    super.setImageBitmap(paramBitmap);
    initializeBitmap();
  }
  
  public void setImageDrawable(Drawable paramDrawable) {
    super.setImageDrawable(paramDrawable);
    initializeBitmap();
  }
  
  public void setImageResource(@DrawableRes int paramInt) {
    super.setImageResource(paramInt);
    initializeBitmap();
  }
  
  public void setImageURI(Uri paramUri) {
    super.setImageURI(paramUri);
    initializeBitmap();
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
    setup();
  }
  
  public void setPaddingRelative(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.setPaddingRelative(paramInt1, paramInt2, paramInt3, paramInt4);
    setup();
  }
  
  public void setScaleType(ImageView.ScaleType paramScaleType) {
    if (paramScaleType != SCALE_TYPE)
      throw new IllegalArgumentException(String.format("ScaleType %s not supported.", new Object[] { paramScaleType })); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/de/hdodenhof/circleimageview/CircleImageView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */