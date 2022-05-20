package android.support.design.widget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.TintTypedArray;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Interpolator;

final class CollapsingTextHelper {
  private static final boolean DEBUG_DRAW = false;
  
  private static final Paint DEBUG_DRAW_PAINT;
  
  private static final boolean USE_SCALING_TEXTURE;
  
  private boolean mBoundsChanged;
  
  private final Rect mCollapsedBounds;
  
  private float mCollapsedDrawX;
  
  private float mCollapsedDrawY;
  
  private int mCollapsedShadowColor;
  
  private float mCollapsedShadowDx;
  
  private float mCollapsedShadowDy;
  
  private float mCollapsedShadowRadius;
  
  private ColorStateList mCollapsedTextColor;
  
  private int mCollapsedTextGravity = 16;
  
  private float mCollapsedTextSize = 15.0F;
  
  private Typeface mCollapsedTypeface;
  
  private final RectF mCurrentBounds;
  
  private float mCurrentDrawX;
  
  private float mCurrentDrawY;
  
  private float mCurrentTextSize;
  
  private Typeface mCurrentTypeface;
  
  private boolean mDrawTitle;
  
  private final Rect mExpandedBounds;
  
  private float mExpandedDrawX;
  
  private float mExpandedDrawY;
  
  private float mExpandedFraction;
  
  private int mExpandedShadowColor;
  
  private float mExpandedShadowDx;
  
  private float mExpandedShadowDy;
  
  private float mExpandedShadowRadius;
  
  private ColorStateList mExpandedTextColor;
  
  private int mExpandedTextGravity = 16;
  
  private float mExpandedTextSize = 15.0F;
  
  private Bitmap mExpandedTitleTexture;
  
  private Typeface mExpandedTypeface;
  
  private boolean mIsRtl;
  
  private Interpolator mPositionInterpolator;
  
  private float mScale;
  
  private int[] mState;
  
  private CharSequence mText;
  
  private final TextPaint mTextPaint;
  
  private Interpolator mTextSizeInterpolator;
  
  private CharSequence mTextToDraw;
  
  private float mTextureAscent;
  
  private float mTextureDescent;
  
  private Paint mTexturePaint;
  
  private boolean mUseTexture;
  
  private final View mView;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT < 18) {
      bool = true;
    } else {
      bool = false;
    } 
    USE_SCALING_TEXTURE = bool;
    DEBUG_DRAW_PAINT = null;
    if (DEBUG_DRAW_PAINT != null) {
      DEBUG_DRAW_PAINT.setAntiAlias(true);
      DEBUG_DRAW_PAINT.setColor(-65281);
    } 
  }
  
  public CollapsingTextHelper(View paramView) {
    this.mView = paramView;
    this.mTextPaint = new TextPaint(129);
    this.mCollapsedBounds = new Rect();
    this.mExpandedBounds = new Rect();
    this.mCurrentBounds = new RectF();
  }
  
  private static int blendColors(int paramInt1, int paramInt2, float paramFloat) {
    float f1 = 1.0F - paramFloat;
    float f2 = Color.alpha(paramInt1);
    float f3 = Color.alpha(paramInt2);
    float f4 = Color.red(paramInt1);
    float f5 = Color.red(paramInt2);
    float f6 = Color.green(paramInt1);
    float f7 = Color.green(paramInt2);
    float f8 = Color.blue(paramInt1);
    float f9 = Color.blue(paramInt2);
    return Color.argb((int)(f2 * f1 + f3 * paramFloat), (int)(f4 * f1 + f5 * paramFloat), (int)(f6 * f1 + f7 * paramFloat), (int)(f8 * f1 + f9 * paramFloat));
  }
  
  private void calculateBaseOffsets() {
    // Byte code:
    //   0: iconst_1
    //   1: istore_1
    //   2: aload_0
    //   3: getfield mCurrentTextSize : F
    //   6: fstore_2
    //   7: aload_0
    //   8: aload_0
    //   9: getfield mCollapsedTextSize : F
    //   12: invokespecial calculateUsingTextSize : (F)V
    //   15: aload_0
    //   16: getfield mTextToDraw : Ljava/lang/CharSequence;
    //   19: ifnull -> 378
    //   22: aload_0
    //   23: getfield mTextPaint : Landroid/text/TextPaint;
    //   26: aload_0
    //   27: getfield mTextToDraw : Ljava/lang/CharSequence;
    //   30: iconst_0
    //   31: aload_0
    //   32: getfield mTextToDraw : Ljava/lang/CharSequence;
    //   35: invokeinterface length : ()I
    //   40: invokevirtual measureText : (Ljava/lang/CharSequence;II)F
    //   43: fstore_3
    //   44: aload_0
    //   45: getfield mCollapsedTextGravity : I
    //   48: istore #4
    //   50: aload_0
    //   51: getfield mIsRtl : Z
    //   54: ifeq -> 383
    //   57: iconst_1
    //   58: istore #5
    //   60: iload #4
    //   62: iload #5
    //   64: invokestatic getAbsoluteGravity : (II)I
    //   67: istore #5
    //   69: iload #5
    //   71: bipush #112
    //   73: iand
    //   74: lookupswitch default -> 100, 48 -> 404, 80 -> 389
    //   100: aload_0
    //   101: getfield mTextPaint : Landroid/text/TextPaint;
    //   104: invokevirtual descent : ()F
    //   107: aload_0
    //   108: getfield mTextPaint : Landroid/text/TextPaint;
    //   111: invokevirtual ascent : ()F
    //   114: fsub
    //   115: fconst_2
    //   116: fdiv
    //   117: fstore #6
    //   119: aload_0
    //   120: getfield mTextPaint : Landroid/text/TextPaint;
    //   123: invokevirtual descent : ()F
    //   126: fstore #7
    //   128: aload_0
    //   129: aload_0
    //   130: getfield mCollapsedBounds : Landroid/graphics/Rect;
    //   133: invokevirtual centerY : ()I
    //   136: i2f
    //   137: fload #6
    //   139: fload #7
    //   141: fsub
    //   142: fadd
    //   143: putfield mCollapsedDrawY : F
    //   146: iload #5
    //   148: ldc 8388615
    //   150: iand
    //   151: lookupswitch default -> 176, 1 -> 427, 5 -> 446
    //   176: aload_0
    //   177: aload_0
    //   178: getfield mCollapsedBounds : Landroid/graphics/Rect;
    //   181: getfield left : I
    //   184: i2f
    //   185: putfield mCollapsedDrawX : F
    //   188: aload_0
    //   189: aload_0
    //   190: getfield mExpandedTextSize : F
    //   193: invokespecial calculateUsingTextSize : (F)V
    //   196: aload_0
    //   197: getfield mTextToDraw : Ljava/lang/CharSequence;
    //   200: ifnull -> 463
    //   203: aload_0
    //   204: getfield mTextPaint : Landroid/text/TextPaint;
    //   207: aload_0
    //   208: getfield mTextToDraw : Ljava/lang/CharSequence;
    //   211: iconst_0
    //   212: aload_0
    //   213: getfield mTextToDraw : Ljava/lang/CharSequence;
    //   216: invokeinterface length : ()I
    //   221: invokevirtual measureText : (Ljava/lang/CharSequence;II)F
    //   224: fstore_3
    //   225: aload_0
    //   226: getfield mExpandedTextGravity : I
    //   229: istore #4
    //   231: aload_0
    //   232: getfield mIsRtl : Z
    //   235: ifeq -> 468
    //   238: iload_1
    //   239: istore #5
    //   241: iload #4
    //   243: iload #5
    //   245: invokestatic getAbsoluteGravity : (II)I
    //   248: istore #5
    //   250: iload #5
    //   252: bipush #112
    //   254: iand
    //   255: lookupswitch default -> 280, 48 -> 489, 80 -> 474
    //   280: aload_0
    //   281: getfield mTextPaint : Landroid/text/TextPaint;
    //   284: invokevirtual descent : ()F
    //   287: aload_0
    //   288: getfield mTextPaint : Landroid/text/TextPaint;
    //   291: invokevirtual ascent : ()F
    //   294: fsub
    //   295: fconst_2
    //   296: fdiv
    //   297: fstore #7
    //   299: aload_0
    //   300: getfield mTextPaint : Landroid/text/TextPaint;
    //   303: invokevirtual descent : ()F
    //   306: fstore #6
    //   308: aload_0
    //   309: aload_0
    //   310: getfield mExpandedBounds : Landroid/graphics/Rect;
    //   313: invokevirtual centerY : ()I
    //   316: i2f
    //   317: fload #7
    //   319: fload #6
    //   321: fsub
    //   322: fadd
    //   323: putfield mExpandedDrawY : F
    //   326: iload #5
    //   328: ldc 8388615
    //   330: iand
    //   331: lookupswitch default -> 356, 1 -> 512, 5 -> 531
    //   356: aload_0
    //   357: aload_0
    //   358: getfield mExpandedBounds : Landroid/graphics/Rect;
    //   361: getfield left : I
    //   364: i2f
    //   365: putfield mExpandedDrawX : F
    //   368: aload_0
    //   369: invokespecial clearTexture : ()V
    //   372: aload_0
    //   373: fload_2
    //   374: invokespecial setInterpolatedTextSize : (F)V
    //   377: return
    //   378: fconst_0
    //   379: fstore_3
    //   380: goto -> 44
    //   383: iconst_0
    //   384: istore #5
    //   386: goto -> 60
    //   389: aload_0
    //   390: aload_0
    //   391: getfield mCollapsedBounds : Landroid/graphics/Rect;
    //   394: getfield bottom : I
    //   397: i2f
    //   398: putfield mCollapsedDrawY : F
    //   401: goto -> 146
    //   404: aload_0
    //   405: aload_0
    //   406: getfield mCollapsedBounds : Landroid/graphics/Rect;
    //   409: getfield top : I
    //   412: i2f
    //   413: aload_0
    //   414: getfield mTextPaint : Landroid/text/TextPaint;
    //   417: invokevirtual ascent : ()F
    //   420: fsub
    //   421: putfield mCollapsedDrawY : F
    //   424: goto -> 146
    //   427: aload_0
    //   428: aload_0
    //   429: getfield mCollapsedBounds : Landroid/graphics/Rect;
    //   432: invokevirtual centerX : ()I
    //   435: i2f
    //   436: fload_3
    //   437: fconst_2
    //   438: fdiv
    //   439: fsub
    //   440: putfield mCollapsedDrawX : F
    //   443: goto -> 188
    //   446: aload_0
    //   447: aload_0
    //   448: getfield mCollapsedBounds : Landroid/graphics/Rect;
    //   451: getfield right : I
    //   454: i2f
    //   455: fload_3
    //   456: fsub
    //   457: putfield mCollapsedDrawX : F
    //   460: goto -> 188
    //   463: fconst_0
    //   464: fstore_3
    //   465: goto -> 225
    //   468: iconst_0
    //   469: istore #5
    //   471: goto -> 241
    //   474: aload_0
    //   475: aload_0
    //   476: getfield mExpandedBounds : Landroid/graphics/Rect;
    //   479: getfield bottom : I
    //   482: i2f
    //   483: putfield mExpandedDrawY : F
    //   486: goto -> 326
    //   489: aload_0
    //   490: aload_0
    //   491: getfield mExpandedBounds : Landroid/graphics/Rect;
    //   494: getfield top : I
    //   497: i2f
    //   498: aload_0
    //   499: getfield mTextPaint : Landroid/text/TextPaint;
    //   502: invokevirtual ascent : ()F
    //   505: fsub
    //   506: putfield mExpandedDrawY : F
    //   509: goto -> 326
    //   512: aload_0
    //   513: aload_0
    //   514: getfield mExpandedBounds : Landroid/graphics/Rect;
    //   517: invokevirtual centerX : ()I
    //   520: i2f
    //   521: fload_3
    //   522: fconst_2
    //   523: fdiv
    //   524: fsub
    //   525: putfield mExpandedDrawX : F
    //   528: goto -> 368
    //   531: aload_0
    //   532: aload_0
    //   533: getfield mExpandedBounds : Landroid/graphics/Rect;
    //   536: getfield right : I
    //   539: i2f
    //   540: fload_3
    //   541: fsub
    //   542: putfield mExpandedDrawX : F
    //   545: goto -> 368
  }
  
  private void calculateCurrentOffsets() {
    calculateOffsets(this.mExpandedFraction);
  }
  
  private boolean calculateIsRtl(CharSequence paramCharSequence) {
    boolean bool = true;
    if (ViewCompat.getLayoutDirection(this.mView) != 1)
      bool = false; 
    if (bool) {
      TextDirectionHeuristicCompat textDirectionHeuristicCompat1 = TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL;
      return textDirectionHeuristicCompat1.isRtl(paramCharSequence, 0, paramCharSequence.length());
    } 
    TextDirectionHeuristicCompat textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
    return textDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
  }
  
  private void calculateOffsets(float paramFloat) {
    interpolateBounds(paramFloat);
    this.mCurrentDrawX = lerp(this.mExpandedDrawX, this.mCollapsedDrawX, paramFloat, this.mPositionInterpolator);
    this.mCurrentDrawY = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, paramFloat, this.mPositionInterpolator);
    setInterpolatedTextSize(lerp(this.mExpandedTextSize, this.mCollapsedTextSize, paramFloat, this.mTextSizeInterpolator));
    if (this.mCollapsedTextColor != this.mExpandedTextColor) {
      this.mTextPaint.setColor(blendColors(getCurrentExpandedTextColor(), getCurrentCollapsedTextColor(), paramFloat));
    } else {
      this.mTextPaint.setColor(getCurrentCollapsedTextColor());
    } 
    this.mTextPaint.setShadowLayer(lerp(this.mExpandedShadowRadius, this.mCollapsedShadowRadius, paramFloat, null), lerp(this.mExpandedShadowDx, this.mCollapsedShadowDx, paramFloat, null), lerp(this.mExpandedShadowDy, this.mCollapsedShadowDy, paramFloat, null), blendColors(this.mExpandedShadowColor, this.mCollapsedShadowColor, paramFloat));
    ViewCompat.postInvalidateOnAnimation(this.mView);
  }
  
  private void calculateUsingTextSize(float paramFloat) {
    boolean bool = true;
    if (this.mText != null) {
      float f3;
      float f1 = this.mCollapsedBounds.width();
      float f2 = this.mExpandedBounds.width();
      boolean bool1 = false;
      boolean bool2 = false;
      if (isClose(paramFloat, this.mCollapsedTextSize)) {
        f3 = this.mCollapsedTextSize;
        this.mScale = 1.0F;
        if (this.mCurrentTypeface != this.mCollapsedTypeface) {
          this.mCurrentTypeface = this.mCollapsedTypeface;
          bool2 = true;
        } 
        paramFloat = f1;
      } else {
        f3 = this.mExpandedTextSize;
        bool2 = bool1;
        if (this.mCurrentTypeface != this.mExpandedTypeface) {
          this.mCurrentTypeface = this.mExpandedTypeface;
          bool2 = true;
        } 
        if (isClose(paramFloat, this.mExpandedTextSize)) {
          this.mScale = 1.0F;
        } else {
          this.mScale = paramFloat / this.mExpandedTextSize;
        } 
        paramFloat = this.mCollapsedTextSize / this.mExpandedTextSize;
        if (f2 * paramFloat > f1) {
          paramFloat = Math.min(f1 / paramFloat, f2);
        } else {
          paramFloat = f2;
        } 
      } 
      bool1 = bool2;
      if (paramFloat > 0.0F) {
        if (this.mCurrentTextSize != f3 || this.mBoundsChanged || bool2) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        this.mCurrentTextSize = f3;
        this.mBoundsChanged = false;
        bool1 = bool2;
      } 
      if (this.mTextToDraw == null || bool1) {
        this.mTextPaint.setTextSize(this.mCurrentTextSize);
        this.mTextPaint.setTypeface(this.mCurrentTypeface);
        TextPaint textPaint = this.mTextPaint;
        if (this.mScale == 1.0F)
          bool = false; 
        textPaint.setLinearText(bool);
        CharSequence charSequence = TextUtils.ellipsize(this.mText, this.mTextPaint, paramFloat, TextUtils.TruncateAt.END);
        if (!TextUtils.equals(charSequence, this.mTextToDraw)) {
          this.mTextToDraw = charSequence;
          this.mIsRtl = calculateIsRtl(this.mTextToDraw);
        } 
      } 
    } 
  }
  
  private void clearTexture() {
    if (this.mExpandedTitleTexture != null) {
      this.mExpandedTitleTexture.recycle();
      this.mExpandedTitleTexture = null;
    } 
  }
  
  private void ensureExpandedTexture() {
    if (this.mExpandedTitleTexture == null && !this.mExpandedBounds.isEmpty() && !TextUtils.isEmpty(this.mTextToDraw)) {
      calculateOffsets(0.0F);
      this.mTextureAscent = this.mTextPaint.ascent();
      this.mTextureDescent = this.mTextPaint.descent();
      int i = Math.round(this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length()));
      int j = Math.round(this.mTextureDescent - this.mTextureAscent);
      if (i > 0 && j > 0) {
        this.mExpandedTitleTexture = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
        (new Canvas(this.mExpandedTitleTexture)).drawText(this.mTextToDraw, 0, this.mTextToDraw.length(), 0.0F, j - this.mTextPaint.descent(), (Paint)this.mTextPaint);
        if (this.mTexturePaint == null)
          this.mTexturePaint = new Paint(3); 
      } 
    } 
  }
  
  @ColorInt
  private int getCurrentCollapsedTextColor() {
    return (this.mState != null) ? this.mCollapsedTextColor.getColorForState(this.mState, 0) : this.mCollapsedTextColor.getDefaultColor();
  }
  
  @ColorInt
  private int getCurrentExpandedTextColor() {
    return (this.mState != null) ? this.mExpandedTextColor.getColorForState(this.mState, 0) : this.mExpandedTextColor.getDefaultColor();
  }
  
  private void interpolateBounds(float paramFloat) {
    this.mCurrentBounds.left = lerp(this.mExpandedBounds.left, this.mCollapsedBounds.left, paramFloat, this.mPositionInterpolator);
    this.mCurrentBounds.top = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, paramFloat, this.mPositionInterpolator);
    this.mCurrentBounds.right = lerp(this.mExpandedBounds.right, this.mCollapsedBounds.right, paramFloat, this.mPositionInterpolator);
    this.mCurrentBounds.bottom = lerp(this.mExpandedBounds.bottom, this.mCollapsedBounds.bottom, paramFloat, this.mPositionInterpolator);
  }
  
  private static boolean isClose(float paramFloat1, float paramFloat2) {
    return (Math.abs(paramFloat1 - paramFloat2) < 0.001F);
  }
  
  private static float lerp(float paramFloat1, float paramFloat2, float paramFloat3, Interpolator paramInterpolator) {
    float f = paramFloat3;
    if (paramInterpolator != null)
      f = paramInterpolator.getInterpolation(paramFloat3); 
    return AnimationUtils.lerp(paramFloat1, paramFloat2, f);
  }
  
  private Typeface readFontFamilyTypeface(int paramInt) {
    TypedArray typedArray = this.mView.getContext().obtainStyledAttributes(paramInt, new int[] { 16843692 });
    try {
      null = typedArray.getString(0);
      if (null != null)
        return Typeface.create(null, 0); 
      return null;
    } finally {
      typedArray.recycle();
    } 
  }
  
  private static boolean rectEquals(Rect paramRect, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return (paramRect.left == paramInt1 && paramRect.top == paramInt2 && paramRect.right == paramInt3 && paramRect.bottom == paramInt4);
  }
  
  private void setInterpolatedTextSize(float paramFloat) {
    boolean bool;
    calculateUsingTextSize(paramFloat);
    if (USE_SCALING_TEXTURE && this.mScale != 1.0F) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mUseTexture = bool;
    if (this.mUseTexture)
      ensureExpandedTexture(); 
    ViewCompat.postInvalidateOnAnimation(this.mView);
  }
  
  public void draw(Canvas paramCanvas) {
    int i = paramCanvas.save();
    if (this.mTextToDraw != null && this.mDrawTitle) {
      boolean bool;
      float f3;
      float f1 = this.mCurrentDrawX;
      float f2 = this.mCurrentDrawY;
      if (this.mUseTexture && this.mExpandedTitleTexture != null) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool) {
        f3 = this.mTextureAscent * this.mScale;
        float f = this.mTextureDescent;
        f = this.mScale;
      } else {
        f3 = this.mTextPaint.ascent() * this.mScale;
        this.mTextPaint.descent();
        float f = this.mScale;
      } 
      float f4 = f2;
      if (bool)
        f4 = f2 + f3; 
      if (this.mScale != 1.0F)
        paramCanvas.scale(this.mScale, this.mScale, f1, f4); 
      if (bool) {
        paramCanvas.drawBitmap(this.mExpandedTitleTexture, f1, f4, this.mTexturePaint);
      } else {
        paramCanvas.drawText(this.mTextToDraw, 0, this.mTextToDraw.length(), f1, f4, (Paint)this.mTextPaint);
      } 
    } 
    paramCanvas.restoreToCount(i);
  }
  
  ColorStateList getCollapsedTextColor() {
    return this.mCollapsedTextColor;
  }
  
  int getCollapsedTextGravity() {
    return this.mCollapsedTextGravity;
  }
  
  float getCollapsedTextSize() {
    return this.mCollapsedTextSize;
  }
  
  Typeface getCollapsedTypeface() {
    return (this.mCollapsedTypeface != null) ? this.mCollapsedTypeface : Typeface.DEFAULT;
  }
  
  ColorStateList getExpandedTextColor() {
    return this.mExpandedTextColor;
  }
  
  int getExpandedTextGravity() {
    return this.mExpandedTextGravity;
  }
  
  float getExpandedTextSize() {
    return this.mExpandedTextSize;
  }
  
  Typeface getExpandedTypeface() {
    return (this.mExpandedTypeface != null) ? this.mExpandedTypeface : Typeface.DEFAULT;
  }
  
  float getExpansionFraction() {
    return this.mExpandedFraction;
  }
  
  CharSequence getText() {
    return this.mText;
  }
  
  final boolean isStateful() {
    return ((this.mCollapsedTextColor != null && this.mCollapsedTextColor.isStateful()) || (this.mExpandedTextColor != null && this.mExpandedTextColor.isStateful()));
  }
  
  void onBoundsChanged() {
    boolean bool;
    if (this.mCollapsedBounds.width() > 0 && this.mCollapsedBounds.height() > 0 && this.mExpandedBounds.width() > 0 && this.mExpandedBounds.height() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mDrawTitle = bool;
  }
  
  public void recalculate() {
    if (this.mView.getHeight() > 0 && this.mView.getWidth() > 0) {
      calculateBaseOffsets();
      calculateCurrentOffsets();
    } 
  }
  
  void setCollapsedBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!rectEquals(this.mCollapsedBounds, paramInt1, paramInt2, paramInt3, paramInt4)) {
      this.mCollapsedBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
      this.mBoundsChanged = true;
      onBoundsChanged();
    } 
  }
  
  void setCollapsedTextAppearance(int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramInt, R.styleable.TextAppearance);
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor))
      this.mCollapsedTextColor = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor); 
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize))
      this.mCollapsedTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.mCollapsedTextSize); 
    this.mCollapsedShadowColor = tintTypedArray.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
    this.mCollapsedShadowDx = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0F);
    this.mCollapsedShadowDy = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0F);
    this.mCollapsedShadowRadius = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0F);
    tintTypedArray.recycle();
    if (Build.VERSION.SDK_INT >= 16)
      this.mCollapsedTypeface = readFontFamilyTypeface(paramInt); 
    recalculate();
  }
  
  void setCollapsedTextColor(ColorStateList paramColorStateList) {
    if (this.mCollapsedTextColor != paramColorStateList) {
      this.mCollapsedTextColor = paramColorStateList;
      recalculate();
    } 
  }
  
  void setCollapsedTextGravity(int paramInt) {
    if (this.mCollapsedTextGravity != paramInt) {
      this.mCollapsedTextGravity = paramInt;
      recalculate();
    } 
  }
  
  void setCollapsedTextSize(float paramFloat) {
    if (this.mCollapsedTextSize != paramFloat) {
      this.mCollapsedTextSize = paramFloat;
      recalculate();
    } 
  }
  
  void setCollapsedTypeface(Typeface paramTypeface) {
    if (this.mCollapsedTypeface != paramTypeface) {
      this.mCollapsedTypeface = paramTypeface;
      recalculate();
    } 
  }
  
  void setExpandedBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!rectEquals(this.mExpandedBounds, paramInt1, paramInt2, paramInt3, paramInt4)) {
      this.mExpandedBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
      this.mBoundsChanged = true;
      onBoundsChanged();
    } 
  }
  
  void setExpandedTextAppearance(int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramInt, R.styleable.TextAppearance);
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor))
      this.mExpandedTextColor = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor); 
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize))
      this.mExpandedTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.mExpandedTextSize); 
    this.mExpandedShadowColor = tintTypedArray.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
    this.mExpandedShadowDx = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0F);
    this.mExpandedShadowDy = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0F);
    this.mExpandedShadowRadius = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0F);
    tintTypedArray.recycle();
    if (Build.VERSION.SDK_INT >= 16)
      this.mExpandedTypeface = readFontFamilyTypeface(paramInt); 
    recalculate();
  }
  
  void setExpandedTextColor(ColorStateList paramColorStateList) {
    if (this.mExpandedTextColor != paramColorStateList) {
      this.mExpandedTextColor = paramColorStateList;
      recalculate();
    } 
  }
  
  void setExpandedTextGravity(int paramInt) {
    if (this.mExpandedTextGravity != paramInt) {
      this.mExpandedTextGravity = paramInt;
      recalculate();
    } 
  }
  
  void setExpandedTextSize(float paramFloat) {
    if (this.mExpandedTextSize != paramFloat) {
      this.mExpandedTextSize = paramFloat;
      recalculate();
    } 
  }
  
  void setExpandedTypeface(Typeface paramTypeface) {
    if (this.mExpandedTypeface != paramTypeface) {
      this.mExpandedTypeface = paramTypeface;
      recalculate();
    } 
  }
  
  void setExpansionFraction(float paramFloat) {
    paramFloat = MathUtils.constrain(paramFloat, 0.0F, 1.0F);
    if (paramFloat != this.mExpandedFraction) {
      this.mExpandedFraction = paramFloat;
      calculateCurrentOffsets();
    } 
  }
  
  void setPositionInterpolator(Interpolator paramInterpolator) {
    this.mPositionInterpolator = paramInterpolator;
    recalculate();
  }
  
  final boolean setState(int[] paramArrayOfint) {
    this.mState = paramArrayOfint;
    if (isStateful()) {
      recalculate();
      return true;
    } 
    return false;
  }
  
  void setText(CharSequence paramCharSequence) {
    if (paramCharSequence == null || !paramCharSequence.equals(this.mText)) {
      this.mText = paramCharSequence;
      this.mTextToDraw = null;
      clearTexture();
      recalculate();
    } 
  }
  
  void setTextSizeInterpolator(Interpolator paramInterpolator) {
    this.mTextSizeInterpolator = paramInterpolator;
    recalculate();
  }
  
  void setTypefaces(Typeface paramTypeface) {
    this.mExpandedTypeface = paramTypeface;
    this.mCollapsedTypeface = paramTypeface;
    recalculate();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/CollapsingTextHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */