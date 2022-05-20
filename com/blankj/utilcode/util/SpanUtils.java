package com.blankj.utilcode.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineHeightSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ReplacementSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.text.style.UpdateAppearance;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public final class SpanUtils {
  public static final int ALIGN_BASELINE = 1;
  
  public static final int ALIGN_BOTTOM = 0;
  
  public static final int ALIGN_CENTER = 2;
  
  public static final int ALIGN_TOP = 3;
  
  private static final int COLOR_DEFAULT = -16777217;
  
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  
  private int alignIconMargin;
  
  private int alignImage;
  
  private int alignLine;
  
  private Layout.Alignment alignment;
  
  private int backgroundColor;
  
  private float blurRadius;
  
  private int bulletColor;
  
  private int bulletGapWidth;
  
  private int bulletRadius;
  
  private ClickableSpan clickSpan;
  
  private int first;
  
  private int flag;
  
  private String fontFamily;
  
  private int fontSize;
  
  private boolean fontSizeIsDp;
  
  private int foregroundColor;
  
  private Bitmap iconMarginBitmap;
  
  private Drawable iconMarginDrawable;
  
  private int iconMarginGapWidth;
  
  private int iconMarginResourceId;
  
  private Uri iconMarginUri;
  
  private Bitmap imageBitmap;
  
  private Drawable imageDrawable;
  
  private int imageResourceId;
  
  private Uri imageUri;
  
  private boolean isBold;
  
  private boolean isBoldItalic;
  
  private boolean isItalic;
  
  private boolean isStrikethrough;
  
  private boolean isSubscript;
  
  private boolean isSuperscript;
  
  private boolean isUnderline;
  
  private int lineHeight;
  
  private SpannableStringBuilder mBuilder = new SpannableStringBuilder();
  
  private CharSequence mText = "";
  
  private int mType;
  
  private final int mTypeCharSequence = 0;
  
  private final int mTypeImage = 1;
  
  private final int mTypeSpace = 2;
  
  private float proportion;
  
  private int quoteColor;
  
  private int quoteGapWidth;
  
  private int rest;
  
  private Shader shader;
  
  private int shadowColor;
  
  private float shadowDx;
  
  private float shadowDy;
  
  private float shadowRadius;
  
  private int spaceColor;
  
  private int spaceSize;
  
  private Object[] spans;
  
  private int stripeWidth;
  
  private BlurMaskFilter.Blur style;
  
  private Typeface typeface;
  
  private String url;
  
  private float xProportion;
  
  public SpanUtils() {
    setDefault();
  }
  
  private void apply(int paramInt) {
    applyLast();
    this.mType = paramInt;
  }
  
  private void applyLast() {
    if (this.mType == 0) {
      updateCharCharSequence();
    } else if (this.mType == 1) {
      updateImage();
    } else if (this.mType == 2) {
      updateSpace();
    } 
    setDefault();
  }
  
  private void setDefault() {
    this.flag = 33;
    this.foregroundColor = -16777217;
    this.backgroundColor = -16777217;
    this.lineHeight = -1;
    this.quoteColor = -16777217;
    this.first = -1;
    this.bulletColor = -16777217;
    this.iconMarginBitmap = null;
    this.iconMarginDrawable = null;
    this.iconMarginUri = null;
    this.iconMarginResourceId = -1;
    this.iconMarginGapWidth = -1;
    this.fontSize = -1;
    this.proportion = -1.0F;
    this.xProportion = -1.0F;
    this.isStrikethrough = false;
    this.isUnderline = false;
    this.isSuperscript = false;
    this.isSubscript = false;
    this.isBold = false;
    this.isItalic = false;
    this.isBoldItalic = false;
    this.fontFamily = null;
    this.typeface = null;
    this.alignment = null;
    this.clickSpan = null;
    this.url = null;
    this.blurRadius = -1.0F;
    this.shader = null;
    this.shadowRadius = -1.0F;
    this.spans = null;
    this.imageBitmap = null;
    this.imageDrawable = null;
    this.imageUri = null;
    this.imageResourceId = -1;
    this.spaceSize = -1;
  }
  
  private void updateCharCharSequence() {
    if (this.mText.length() != 0) {
      int i = this.mBuilder.length();
      this.mBuilder.append(this.mText);
      int j = this.mBuilder.length();
      if (this.foregroundColor != -16777217)
        this.mBuilder.setSpan(new ForegroundColorSpan(this.foregroundColor), i, j, this.flag); 
      if (this.backgroundColor != -16777217)
        this.mBuilder.setSpan(new BackgroundColorSpan(this.backgroundColor), i, j, this.flag); 
      if (this.first != -1)
        this.mBuilder.setSpan(new LeadingMarginSpan.Standard(this.first, this.rest), i, j, this.flag); 
      if (this.quoteColor != -16777217)
        this.mBuilder.setSpan(new CustomQuoteSpan(this.quoteColor, this.stripeWidth, this.quoteGapWidth), i, j, this.flag); 
      if (this.bulletColor != -16777217)
        this.mBuilder.setSpan(new CustomBulletSpan(this.bulletColor, this.bulletRadius, this.bulletGapWidth), i, j, this.flag); 
      if (this.iconMarginGapWidth != -1)
        if (this.iconMarginBitmap != null) {
          this.mBuilder.setSpan(new CustomIconMarginSpan(this.iconMarginBitmap, this.iconMarginGapWidth, this.alignIconMargin), i, j, this.flag);
        } else if (this.iconMarginDrawable != null) {
          this.mBuilder.setSpan(new CustomIconMarginSpan(this.iconMarginDrawable, this.iconMarginGapWidth, this.alignIconMargin), i, j, this.flag);
        } else if (this.iconMarginUri != null) {
          this.mBuilder.setSpan(new CustomIconMarginSpan(this.iconMarginUri, this.iconMarginGapWidth, this.alignIconMargin), i, j, this.flag);
        } else if (this.iconMarginResourceId != -1) {
          this.mBuilder.setSpan(new CustomIconMarginSpan(this.iconMarginResourceId, this.iconMarginGapWidth, this.alignIconMargin), i, j, this.flag);
        }  
      if (this.fontSize != -1)
        this.mBuilder.setSpan(new AbsoluteSizeSpan(this.fontSize, this.fontSizeIsDp), i, j, this.flag); 
      if (this.proportion != -1.0F)
        this.mBuilder.setSpan(new RelativeSizeSpan(this.proportion), i, j, this.flag); 
      if (this.xProportion != -1.0F)
        this.mBuilder.setSpan(new ScaleXSpan(this.xProportion), i, j, this.flag); 
      if (this.lineHeight != -1)
        this.mBuilder.setSpan(new CustomLineHeightSpan(this.lineHeight, this.alignLine), i, j, this.flag); 
      if (this.isStrikethrough)
        this.mBuilder.setSpan(new StrikethroughSpan(), i, j, this.flag); 
      if (this.isUnderline)
        this.mBuilder.setSpan(new UnderlineSpan(), i, j, this.flag); 
      if (this.isSuperscript)
        this.mBuilder.setSpan(new SuperscriptSpan(), i, j, this.flag); 
      if (this.isSubscript)
        this.mBuilder.setSpan(new SubscriptSpan(), i, j, this.flag); 
      if (this.isBold)
        this.mBuilder.setSpan(new StyleSpan(1), i, j, this.flag); 
      if (this.isItalic)
        this.mBuilder.setSpan(new StyleSpan(2), i, j, this.flag); 
      if (this.isBoldItalic)
        this.mBuilder.setSpan(new StyleSpan(3), i, j, this.flag); 
      if (this.fontFamily != null)
        this.mBuilder.setSpan(new TypefaceSpan(this.fontFamily), i, j, this.flag); 
      if (this.typeface != null)
        this.mBuilder.setSpan(new CustomTypefaceSpan(this.typeface), i, j, this.flag); 
      if (this.alignment != null)
        this.mBuilder.setSpan(new AlignmentSpan.Standard(this.alignment), i, j, this.flag); 
      if (this.clickSpan != null)
        this.mBuilder.setSpan(this.clickSpan, i, j, this.flag); 
      if (this.url != null)
        this.mBuilder.setSpan(new URLSpan(this.url), i, j, this.flag); 
      if (this.blurRadius != -1.0F)
        this.mBuilder.setSpan(new MaskFilterSpan((MaskFilter)new BlurMaskFilter(this.blurRadius, this.style)), i, j, this.flag); 
      if (this.shader != null)
        this.mBuilder.setSpan(new ShaderSpan(this.shader), i, j, this.flag); 
      if (this.shadowRadius != -1.0F)
        this.mBuilder.setSpan(new ShadowSpan(this.shadowRadius, this.shadowDx, this.shadowDy, this.shadowColor), i, j, this.flag); 
      if (this.spans != null) {
        Object[] arrayOfObject = this.spans;
        int k = arrayOfObject.length;
        byte b = 0;
        while (true) {
          if (b < k) {
            Object object = arrayOfObject[b];
            this.mBuilder.setSpan(object, i, j, this.flag);
            b++;
            continue;
          } 
          return;
        } 
      } 
    } 
  }
  
  private void updateImage() {
    int i = this.mBuilder.length();
    this.mBuilder.append("<img>");
    int j = i + 5;
    if (this.imageBitmap != null) {
      this.mBuilder.setSpan(new CustomImageSpan(this.imageBitmap, this.alignImage), i, j, this.flag);
      return;
    } 
    if (this.imageDrawable != null) {
      this.mBuilder.setSpan(new CustomImageSpan(this.imageDrawable, this.alignImage), i, j, this.flag);
      return;
    } 
    if (this.imageUri != null) {
      this.mBuilder.setSpan(new CustomImageSpan(this.imageUri, this.alignImage), i, j, this.flag);
      return;
    } 
    if (this.imageResourceId != -1)
      this.mBuilder.setSpan(new CustomImageSpan(this.imageResourceId, this.alignImage), i, j, this.flag); 
  }
  
  private void updateSpace() {
    int i = this.mBuilder.length();
    this.mBuilder.append("< >");
    this.mBuilder.setSpan(new SpaceSpan(this.spaceSize, this.spaceColor), i, i + 3, this.flag);
  }
  
  public SpanUtils append(@NonNull CharSequence paramCharSequence) {
    apply(0);
    this.mText = paramCharSequence;
    return this;
  }
  
  public SpanUtils appendImage(@DrawableRes int paramInt) {
    return appendImage(paramInt, 0);
  }
  
  public SpanUtils appendImage(@DrawableRes int paramInt1, int paramInt2) {
    apply(1);
    this.imageResourceId = paramInt1;
    this.alignImage = paramInt2;
    return this;
  }
  
  public SpanUtils appendImage(@NonNull Bitmap paramBitmap) {
    return appendImage(paramBitmap, 0);
  }
  
  public SpanUtils appendImage(@NonNull Bitmap paramBitmap, int paramInt) {
    apply(1);
    this.imageBitmap = paramBitmap;
    this.alignImage = paramInt;
    return this;
  }
  
  public SpanUtils appendImage(@NonNull Drawable paramDrawable) {
    return appendImage(paramDrawable, 0);
  }
  
  public SpanUtils appendImage(@NonNull Drawable paramDrawable, int paramInt) {
    apply(1);
    this.imageDrawable = paramDrawable;
    this.alignImage = paramInt;
    return this;
  }
  
  public SpanUtils appendImage(@NonNull Uri paramUri) {
    return appendImage(paramUri, 0);
  }
  
  public SpanUtils appendImage(@NonNull Uri paramUri, int paramInt) {
    apply(1);
    this.imageUri = paramUri;
    this.alignImage = paramInt;
    return this;
  }
  
  public SpanUtils appendLine() {
    apply(0);
    this.mText = LINE_SEPARATOR;
    return this;
  }
  
  public SpanUtils appendLine(@NonNull CharSequence paramCharSequence) {
    apply(0);
    this.mText = paramCharSequence + LINE_SEPARATOR;
    return this;
  }
  
  public SpanUtils appendSpace(@IntRange(from = 0L) int paramInt) {
    return appendSpace(paramInt, 0);
  }
  
  public SpanUtils appendSpace(@IntRange(from = 0L) int paramInt1, @ColorInt int paramInt2) {
    apply(2);
    this.spaceSize = paramInt1;
    this.spaceColor = paramInt2;
    return this;
  }
  
  public SpannableStringBuilder create() {
    applyLast();
    return this.mBuilder;
  }
  
  public SpanUtils setAlign(@NonNull Layout.Alignment paramAlignment) {
    this.alignment = paramAlignment;
    return this;
  }
  
  public SpanUtils setBackgroundColor(@ColorInt int paramInt) {
    this.backgroundColor = paramInt;
    return this;
  }
  
  public SpanUtils setBlur(@FloatRange(from = 0.0D, fromInclusive = false) float paramFloat, BlurMaskFilter.Blur paramBlur) {
    this.blurRadius = paramFloat;
    this.style = paramBlur;
    return this;
  }
  
  public SpanUtils setBold() {
    this.isBold = true;
    return this;
  }
  
  public SpanUtils setBoldItalic() {
    this.isBoldItalic = true;
    return this;
  }
  
  public SpanUtils setBullet(@IntRange(from = 0L) int paramInt) {
    return setBullet(0, 3, paramInt);
  }
  
  public SpanUtils setBullet(@ColorInt int paramInt1, @IntRange(from = 0L) int paramInt2, @IntRange(from = 0L) int paramInt3) {
    this.bulletColor = paramInt1;
    this.bulletRadius = paramInt2;
    this.bulletGapWidth = paramInt3;
    return this;
  }
  
  public SpanUtils setClickSpan(@NonNull ClickableSpan paramClickableSpan) {
    this.clickSpan = paramClickableSpan;
    return this;
  }
  
  public SpanUtils setFlag(int paramInt) {
    this.flag = paramInt;
    return this;
  }
  
  public SpanUtils setFontFamily(@NonNull String paramString) {
    this.fontFamily = paramString;
    return this;
  }
  
  public SpanUtils setFontProportion(@FloatRange(from = 0.0D, fromInclusive = false) float paramFloat) {
    this.proportion = paramFloat;
    return this;
  }
  
  public SpanUtils setFontSize(@IntRange(from = 0L) int paramInt) {
    return setFontSize(paramInt, false);
  }
  
  public SpanUtils setFontSize(@IntRange(from = 0L) int paramInt, boolean paramBoolean) {
    this.fontSize = paramInt;
    this.fontSizeIsDp = paramBoolean;
    return this;
  }
  
  public SpanUtils setFontXProportion(@FloatRange(from = 0.0D, fromInclusive = false) float paramFloat) {
    this.xProportion = paramFloat;
    return this;
  }
  
  public SpanUtils setForegroundColor(@ColorInt int paramInt) {
    this.foregroundColor = paramInt;
    return this;
  }
  
  public SpanUtils setIconMargin(@DrawableRes int paramInt) {
    return setIconMargin(paramInt, 0, 2);
  }
  
  public SpanUtils setIconMargin(@DrawableRes int paramInt1, int paramInt2, int paramInt3) {
    this.iconMarginResourceId = paramInt1;
    this.iconMarginGapWidth = paramInt2;
    this.alignIconMargin = paramInt3;
    return this;
  }
  
  public SpanUtils setIconMargin(Bitmap paramBitmap) {
    return setIconMargin(paramBitmap, 0, 2);
  }
  
  public SpanUtils setIconMargin(Bitmap paramBitmap, int paramInt1, int paramInt2) {
    this.iconMarginBitmap = paramBitmap;
    this.iconMarginGapWidth = paramInt1;
    this.alignIconMargin = paramInt2;
    return this;
  }
  
  public SpanUtils setIconMargin(Drawable paramDrawable) {
    return setIconMargin(paramDrawable, 0, 2);
  }
  
  public SpanUtils setIconMargin(Drawable paramDrawable, int paramInt1, int paramInt2) {
    this.iconMarginDrawable = paramDrawable;
    this.iconMarginGapWidth = paramInt1;
    this.alignIconMargin = paramInt2;
    return this;
  }
  
  public SpanUtils setIconMargin(Uri paramUri) {
    return setIconMargin(paramUri, 0, 2);
  }
  
  public SpanUtils setIconMargin(Uri paramUri, int paramInt1, int paramInt2) {
    this.iconMarginUri = paramUri;
    this.iconMarginGapWidth = paramInt1;
    this.alignIconMargin = paramInt2;
    return this;
  }
  
  public SpanUtils setItalic() {
    this.isItalic = true;
    return this;
  }
  
  public SpanUtils setLeadingMargin(@IntRange(from = 0L) int paramInt1, @IntRange(from = 0L) int paramInt2) {
    this.first = paramInt1;
    this.rest = paramInt2;
    return this;
  }
  
  public SpanUtils setLineHeight(@IntRange(from = 0L) int paramInt) {
    return setLineHeight(paramInt, 2);
  }
  
  public SpanUtils setLineHeight(@IntRange(from = 0L) int paramInt1, int paramInt2) {
    this.lineHeight = paramInt1;
    this.alignLine = paramInt2;
    return this;
  }
  
  public SpanUtils setQuoteColor(@ColorInt int paramInt) {
    return setQuoteColor(paramInt, 2, 2);
  }
  
  public SpanUtils setQuoteColor(@ColorInt int paramInt1, @IntRange(from = 1L) int paramInt2, @IntRange(from = 0L) int paramInt3) {
    this.quoteColor = paramInt1;
    this.stripeWidth = paramInt2;
    this.quoteGapWidth = paramInt3;
    return this;
  }
  
  public SpanUtils setShader(@NonNull Shader paramShader) {
    this.shader = paramShader;
    return this;
  }
  
  public SpanUtils setShadow(@FloatRange(from = 0.0D, fromInclusive = false) float paramFloat1, float paramFloat2, float paramFloat3, int paramInt) {
    this.shadowRadius = paramFloat1;
    this.shadowDx = paramFloat2;
    this.shadowDy = paramFloat3;
    this.shadowColor = paramInt;
    return this;
  }
  
  public SpanUtils setSpans(@NonNull Object... paramVarArgs) {
    if (paramVarArgs.length > 0)
      this.spans = paramVarArgs; 
    return this;
  }
  
  public SpanUtils setStrikethrough() {
    this.isStrikethrough = true;
    return this;
  }
  
  public SpanUtils setSubscript() {
    this.isSubscript = true;
    return this;
  }
  
  public SpanUtils setSuperscript() {
    this.isSuperscript = true;
    return this;
  }
  
  public SpanUtils setTypeface(@NonNull Typeface paramTypeface) {
    this.typeface = paramTypeface;
    return this;
  }
  
  public SpanUtils setUnderline() {
    this.isUnderline = true;
    return this;
  }
  
  public SpanUtils setUrl(@NonNull String paramString) {
    this.url = paramString;
    return this;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Align {}
  
  class CustomBulletSpan implements LeadingMarginSpan {
    private final int color;
    
    private final int gapWidth;
    
    private final int radius;
    
    private Path sBulletPath = null;
    
    private CustomBulletSpan(int param1Int1, int param1Int2, int param1Int3) {
      this.color = param1Int1;
      this.radius = param1Int2;
      this.gapWidth = param1Int3;
    }
    
    public void drawLeadingMargin(Canvas param1Canvas, Paint param1Paint, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, CharSequence param1CharSequence, int param1Int6, int param1Int7, boolean param1Boolean, Layout param1Layout) {
      if (((Spanned)param1CharSequence).getSpanStart(this) == param1Int6) {
        Paint.Style style = param1Paint.getStyle();
        param1Int4 = param1Paint.getColor();
        param1Paint.setColor(this.color);
        param1Paint.setStyle(Paint.Style.FILL);
        if (param1Canvas.isHardwareAccelerated()) {
          if (this.sBulletPath == null) {
            this.sBulletPath = new Path();
            this.sBulletPath.addCircle(0.0F, 0.0F, this.radius, Path.Direction.CW);
          } 
          param1Canvas.save();
          param1Canvas.translate((this.radius * param1Int2 + param1Int1), (param1Int3 + param1Int5) / 2.0F);
          param1Canvas.drawPath(this.sBulletPath, param1Paint);
          param1Canvas.restore();
        } else {
          param1Canvas.drawCircle((this.radius * param1Int2 + param1Int1), (param1Int3 + param1Int5) / 2.0F, this.radius, param1Paint);
        } 
        param1Paint.setColor(param1Int4);
        param1Paint.setStyle(style);
      } 
    }
    
    public int getLeadingMargin(boolean param1Boolean) {
      return this.radius * 2 + this.gapWidth;
    }
  }
  
  abstract class CustomDynamicDrawableSpan extends ReplacementSpan {
    static final int ALIGN_BASELINE = 1;
    
    static final int ALIGN_BOTTOM = 0;
    
    static final int ALIGN_CENTER = 2;
    
    static final int ALIGN_TOP = 3;
    
    private WeakReference<Drawable> mDrawableRef;
    
    final int mVerticalAlignment = 0;
    
    private CustomDynamicDrawableSpan() {}
    
    private CustomDynamicDrawableSpan(int param1Int) {}
    
    private Drawable getCachedDrawable() {
      WeakReference<Drawable> weakReference = this.mDrawableRef;
      Drawable drawable = null;
      if (weakReference != null)
        drawable = weakReference.get(); 
      if (drawable == null)
        this.mDrawableRef = new WeakReference<Drawable>(getDrawable()); 
      return getDrawable();
    }
    
    public void draw(@NonNull Canvas param1Canvas, CharSequence param1CharSequence, int param1Int1, int param1Int2, float param1Float, int param1Int3, int param1Int4, int param1Int5, @NonNull Paint param1Paint) {
      Drawable drawable = getCachedDrawable();
      Rect rect = drawable.getBounds();
      param1Canvas.save();
      float f = (param1Paint.getFontMetrics()).descent - (param1Paint.getFontMetrics()).ascent;
      param1Int2 = param1Int5 - rect.bottom;
      param1Int1 = param1Int2;
      if (rect.height() < f)
        if (this.mVerticalAlignment == 1) {
          param1Int1 = param1Int2 - (param1Paint.getFontMetricsInt()).descent;
        } else if (this.mVerticalAlignment == 2) {
          param1Int1 = (int)(param1Int2 - (f - rect.height()) / 2.0F);
        } else {
          param1Int1 = param1Int2;
          if (this.mVerticalAlignment == 3)
            param1Int1 = (int)(param1Int2 - f - rect.height()); 
        }  
      param1Canvas.translate(param1Float, param1Int1);
      drawable.draw(param1Canvas);
      param1Canvas.restore();
    }
    
    public abstract Drawable getDrawable();
    
    public int getSize(@NonNull Paint param1Paint, CharSequence param1CharSequence, int param1Int1, int param1Int2, Paint.FontMetricsInt param1FontMetricsInt) {
      Rect rect = getCachedDrawable().getBounds();
      param1Int1 = (int)((param1Paint.getFontMetrics()).descent - (param1Paint.getFontMetrics()).ascent);
      if (param1FontMetricsInt != null && rect.height() > param1Int1) {
        if (this.mVerticalAlignment == 3) {
          param1FontMetricsInt.descent += rect.height() - param1Int1;
          return rect.right;
        } 
      } else {
        return rect.right;
      } 
      if (this.mVerticalAlignment == 2) {
        param1FontMetricsInt.ascent -= (rect.height() - param1Int1) / 2;
        param1FontMetricsInt.descent += (rect.height() - param1Int1) / 2;
        return rect.right;
      } 
      param1FontMetricsInt.ascent -= rect.height() - param1Int1;
      return rect.right;
    }
  }
  
  class CustomIconMarginSpan implements LeadingMarginSpan, LineHeightSpan {
    static final int ALIGN_CENTER = 2;
    
    static final int ALIGN_TOP = 3;
    
    private boolean flag;
    
    private int lineHeight;
    
    Bitmap mBitmap;
    
    private int mPad;
    
    final int mVerticalAlignment;
    
    private int need0;
    
    private int need1;
    
    private int totalHeight;
    
    private CustomIconMarginSpan(int param1Int1, int param1Int2, int param1Int3) {
      this.mBitmap = resource2Bitmap(param1Int1);
      this.mPad = param1Int2;
      this.mVerticalAlignment = param1Int3;
    }
    
    private CustomIconMarginSpan(Bitmap param1Bitmap, int param1Int1, int param1Int2) {
      this.mBitmap = param1Bitmap;
      this.mPad = param1Int1;
      this.mVerticalAlignment = param1Int2;
    }
    
    private CustomIconMarginSpan(Drawable param1Drawable, int param1Int1, int param1Int2) {
      this.mBitmap = drawable2Bitmap(param1Drawable);
      this.mPad = param1Int1;
      this.mVerticalAlignment = param1Int2;
    }
    
    private CustomIconMarginSpan(Uri param1Uri, int param1Int1, int param1Int2) {
      this.mBitmap = uri2Bitmap(param1Uri);
      this.mPad = param1Int1;
      this.mVerticalAlignment = param1Int2;
    }
    
    private Bitmap drawable2Bitmap(Drawable param1Drawable) {
      Bitmap bitmap;
      if (param1Drawable instanceof BitmapDrawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable)param1Drawable;
        if (bitmapDrawable.getBitmap() != null)
          return bitmapDrawable.getBitmap(); 
      } 
      if (param1Drawable.getIntrinsicWidth() <= 0 || param1Drawable.getIntrinsicHeight() <= 0) {
        Bitmap.Config config;
        if (param1Drawable.getOpacity() != -1) {
          config = Bitmap.Config.ARGB_8888;
        } else {
          config = Bitmap.Config.RGB_565;
        } 
        bitmap = Bitmap.createBitmap(1, 1, config);
      } else {
        Bitmap.Config config;
        int i = param1Drawable.getIntrinsicWidth();
        int j = param1Drawable.getIntrinsicHeight();
        if (param1Drawable.getOpacity() != -1) {
          config = Bitmap.Config.ARGB_8888;
        } else {
          config = Bitmap.Config.RGB_565;
        } 
        bitmap = Bitmap.createBitmap(i, j, config);
      } 
      Canvas canvas = new Canvas(bitmap);
      param1Drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      param1Drawable.draw(canvas);
      return bitmap;
    }
    
    private Bitmap resource2Bitmap(int param1Int) {
      Drawable drawable = ContextCompat.getDrawable((Context)Utils.getApp(), param1Int);
      Canvas canvas = new Canvas();
      Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
      canvas.setBitmap(bitmap);
      drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
      drawable.draw(canvas);
      return bitmap;
    }
    
    private Bitmap uri2Bitmap(Uri param1Uri) {
      Bitmap bitmap;
      try {
        bitmap = MediaStore.Images.Media.getBitmap(Utils.getApp().getContentResolver(), param1Uri);
      } catch (IOException iOException) {
        iOException.printStackTrace();
        bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
      } 
      return bitmap;
    }
    
    public void chooseHeight(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3, int param1Int4, Paint.FontMetricsInt param1FontMetricsInt) {
      if (this.lineHeight == 0)
        this.lineHeight = param1Int4 - param1Int3; 
      if (this.need0 == 0 && param1Int2 == ((Spanned)param1CharSequence).getSpanEnd(this)) {
        param1Int1 = this.mBitmap.getHeight();
        this.need0 = param1Int1 - param1FontMetricsInt.descent + param1Int4 - param1FontMetricsInt.ascent - param1Int3;
        this.need1 = param1Int1 - param1FontMetricsInt.bottom + param1Int4 - param1FontMetricsInt.top - param1Int3;
        this.totalHeight = param1Int4 - param1Int3 + this.lineHeight;
        return;
      } 
      if (this.need0 > 0 || this.need1 > 0) {
        if (this.mVerticalAlignment == 3) {
          if (param1Int2 == ((Spanned)param1CharSequence).getSpanEnd(this)) {
            if (this.need0 > 0)
              param1FontMetricsInt.descent += this.need0; 
            if (this.need1 > 0)
              param1FontMetricsInt.bottom += this.need1; 
          } 
          return;
        } 
        if (this.mVerticalAlignment == 2) {
          if (param1Int1 == ((Spanned)param1CharSequence).getSpanStart(this)) {
            if (this.need0 > 0)
              param1FontMetricsInt.ascent -= this.need0 / 2; 
            if (this.need1 > 0)
              param1FontMetricsInt.top -= this.need1 / 2; 
          } else if (!this.flag) {
            if (this.need0 > 0)
              param1FontMetricsInt.ascent += this.need0 / 2; 
            if (this.need1 > 0)
              param1FontMetricsInt.top += this.need1 / 2; 
            this.flag = true;
          } 
          if (param1Int2 == ((Spanned)param1CharSequence).getSpanEnd(this)) {
            if (this.need0 > 0)
              param1FontMetricsInt.descent += this.need0 / 2; 
            if (this.need1 > 0)
              param1FontMetricsInt.bottom += this.need1 / 2; 
          } 
          return;
        } 
        if (param1Int1 == ((Spanned)param1CharSequence).getSpanStart(this)) {
          if (this.need0 > 0)
            param1FontMetricsInt.ascent -= this.need0; 
          if (this.need1 > 0)
            param1FontMetricsInt.top -= this.need1; 
          return;
        } 
        if (!this.flag) {
          if (this.need0 > 0)
            param1FontMetricsInt.ascent += this.need0; 
          if (this.need1 > 0)
            param1FontMetricsInt.top += this.need1; 
          this.flag = true;
        } 
      } 
    }
    
    public void drawLeadingMargin(Canvas param1Canvas, Paint param1Paint, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, CharSequence param1CharSequence, int param1Int6, int param1Int7, boolean param1Boolean, Layout param1Layout) {
      param1Int4 = param1Layout.getLineTop(param1Layout.getLineForOffset(((Spanned)param1CharSequence).getSpanStart(this)));
      param1Int3 = param1Int1;
      if (param1Int2 < 0)
        param1Int3 = param1Int1 - this.mBitmap.getWidth(); 
      param1Int1 = this.totalHeight - this.mBitmap.getHeight();
      if (param1Int1 > 0) {
        if (this.mVerticalAlignment == 3) {
          param1Canvas.drawBitmap(this.mBitmap, param1Int3, param1Int4, param1Paint);
          return;
        } 
        if (this.mVerticalAlignment == 2) {
          param1Canvas.drawBitmap(this.mBitmap, param1Int3, (param1Int1 / 2 + param1Int4), param1Paint);
          return;
        } 
        param1Canvas.drawBitmap(this.mBitmap, param1Int3, (param1Int4 + param1Int1), param1Paint);
        return;
      } 
      param1Canvas.drawBitmap(this.mBitmap, param1Int3, param1Int4, param1Paint);
    }
    
    public int getLeadingMargin(boolean param1Boolean) {
      return this.mBitmap.getWidth() + this.mPad;
    }
  }
  
  class CustomImageSpan extends CustomDynamicDrawableSpan {
    private Uri mContentUri;
    
    private Drawable mDrawable;
    
    private int mResourceId;
    
    private CustomImageSpan(int param1Int1, int param1Int2) {
      super(param1Int2);
      this.mResourceId = param1Int1;
    }
    
    private CustomImageSpan(Bitmap param1Bitmap, int param1Int) {
      super(param1Int);
      this.mDrawable = (Drawable)new BitmapDrawable(Utils.getApp().getResources(), param1Bitmap);
      this.mDrawable.setBounds(0, 0, this.mDrawable.getIntrinsicWidth(), this.mDrawable.getIntrinsicHeight());
    }
    
    private CustomImageSpan(Drawable param1Drawable, int param1Int) {
      super(param1Int);
      this.mDrawable = param1Drawable;
      this.mDrawable.setBounds(0, 0, this.mDrawable.getIntrinsicWidth(), this.mDrawable.getIntrinsicHeight());
    }
    
    private CustomImageSpan(Uri param1Uri, int param1Int) {
      super(param1Int);
      this.mContentUri = param1Uri;
    }
    
    public Drawable getDrawable() {
      Drawable drawable1 = null;
      Drawable drawable2 = null;
      if (this.mDrawable != null)
        return this.mDrawable; 
      if (this.mContentUri != null) {
        try {
          InputStream inputStream = Utils.getApp().getContentResolver().openInputStream(this.mContentUri);
          Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
          BitmapDrawable bitmapDrawable = new BitmapDrawable();
          this(Utils.getApp().getResources(), bitmap);
          try {
            bitmapDrawable.setBounds(0, 0, bitmapDrawable.getIntrinsicWidth(), bitmapDrawable.getIntrinsicHeight());
            if (inputStream != null)
              inputStream.close(); 
          } catch (Exception exception) {}
        } catch (Exception exception) {
          drawable1 = drawable2;
        } 
        return drawable1;
      } 
      try {
        Drawable drawable = ContextCompat.getDrawable((Context)Utils.getApp(), this.mResourceId);
        drawable1 = drawable;
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable1 = drawable;
      } catch (Exception exception) {
        Log.e("sms", "Unable to find resource: " + this.mResourceId);
      } 
      return drawable1;
    }
  }
  
  class CustomLineHeightSpan extends CharacterStyle implements LineHeightSpan {
    static final int ALIGN_CENTER = 2;
    
    static final int ALIGN_TOP = 3;
    
    private final int height;
    
    final int mVerticalAlignment;
    
    CustomLineHeightSpan(int param1Int1, int param1Int2) {
      this.height = param1Int1;
      this.mVerticalAlignment = param1Int2;
    }
    
    public void chooseHeight(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3, int param1Int4, Paint.FontMetricsInt param1FontMetricsInt) {
      param1Int1 = this.height - param1FontMetricsInt.descent + param1Int4 - param1FontMetricsInt.ascent - param1Int3;
      if (param1Int1 > 0)
        if (this.mVerticalAlignment == 3) {
          param1FontMetricsInt.descent += param1Int1;
        } else if (this.mVerticalAlignment == 2) {
          param1FontMetricsInt.descent += param1Int1 / 2;
          param1FontMetricsInt.ascent -= param1Int1 / 2;
        } else {
          param1FontMetricsInt.ascent -= param1Int1;
        }  
      param1Int1 = this.height - param1FontMetricsInt.bottom + param1Int4 - param1FontMetricsInt.top - param1Int3;
      if (param1Int1 > 0) {
        if (this.mVerticalAlignment == 3) {
          param1FontMetricsInt.top += param1Int1;
          return;
        } 
      } else {
        return;
      } 
      if (this.mVerticalAlignment == 2) {
        param1FontMetricsInt.bottom += param1Int1 / 2;
        param1FontMetricsInt.top -= param1Int1 / 2;
        return;
      } 
      param1FontMetricsInt.top -= param1Int1;
    }
    
    public void updateDrawState(TextPaint param1TextPaint) {}
  }
  
  class CustomQuoteSpan implements LeadingMarginSpan {
    private final int color;
    
    private final int gapWidth;
    
    private final int stripeWidth;
    
    private CustomQuoteSpan(int param1Int1, int param1Int2, int param1Int3) {
      this.color = param1Int1;
      this.stripeWidth = param1Int2;
      this.gapWidth = param1Int3;
    }
    
    public void drawLeadingMargin(Canvas param1Canvas, Paint param1Paint, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, CharSequence param1CharSequence, int param1Int6, int param1Int7, boolean param1Boolean, Layout param1Layout) {
      Paint.Style style = param1Paint.getStyle();
      param1Int4 = param1Paint.getColor();
      param1Paint.setStyle(Paint.Style.FILL);
      param1Paint.setColor(this.color);
      param1Canvas.drawRect(param1Int1, param1Int3, (this.stripeWidth * param1Int2 + param1Int1), param1Int5, param1Paint);
      param1Paint.setStyle(style);
      param1Paint.setColor(param1Int4);
    }
    
    public int getLeadingMargin(boolean param1Boolean) {
      return this.stripeWidth + this.gapWidth;
    }
  }
  
  @SuppressLint({"ParcelCreator"})
  class CustomTypefaceSpan extends TypefaceSpan {
    private final Typeface newType;
    
    private CustomTypefaceSpan(Typeface param1Typeface) {
      super("");
      this.newType = param1Typeface;
    }
    
    private void apply(Paint param1Paint, Typeface param1Typeface) {
      int i;
      Typeface typeface = param1Paint.getTypeface();
      if (typeface == null) {
        i = 0;
      } else {
        i = typeface.getStyle();
      } 
      i &= param1Typeface.getStyle() ^ 0xFFFFFFFF;
      if ((i & 0x1) != 0)
        param1Paint.setFakeBoldText(true); 
      if ((i & 0x2) != 0)
        param1Paint.setTextSkewX(-0.25F); 
      param1Paint.getShader();
      param1Paint.setTypeface(param1Typeface);
    }
    
    public void updateDrawState(TextPaint param1TextPaint) {
      apply((Paint)param1TextPaint, this.newType);
    }
    
    public void updateMeasureState(TextPaint param1TextPaint) {
      apply((Paint)param1TextPaint, this.newType);
    }
  }
  
  class ShaderSpan extends CharacterStyle implements UpdateAppearance {
    private Shader mShader;
    
    private ShaderSpan(Shader param1Shader) {
      this.mShader = param1Shader;
    }
    
    public void updateDrawState(TextPaint param1TextPaint) {
      param1TextPaint.setShader(this.mShader);
    }
  }
  
  class ShadowSpan extends CharacterStyle implements UpdateAppearance {
    private float dx;
    
    private float dy;
    
    private float radius;
    
    private int shadowColor;
    
    private ShadowSpan(float param1Float1, float param1Float2, float param1Float3, int param1Int) {
      this.radius = param1Float1;
      this.dx = param1Float2;
      this.dy = param1Float3;
      this.shadowColor = param1Int;
    }
    
    public void updateDrawState(TextPaint param1TextPaint) {
      param1TextPaint.setShadowLayer(this.radius, this.dx, this.dy, this.shadowColor);
    }
  }
  
  class SpaceSpan extends ReplacementSpan {
    private final int color;
    
    private final int width;
    
    private SpaceSpan(int param1Int) {
      this(param1Int, 0);
    }
    
    private SpaceSpan(int param1Int1, int param1Int2) {
      this.width = param1Int1;
      this.color = param1Int2;
    }
    
    public void draw(@NonNull Canvas param1Canvas, CharSequence param1CharSequence, @IntRange(from = 0L) int param1Int1, @IntRange(from = 0L) int param1Int2, float param1Float, int param1Int3, int param1Int4, int param1Int5, @NonNull Paint param1Paint) {
      Paint.Style style = param1Paint.getStyle();
      param1Int1 = param1Paint.getColor();
      param1Paint.setStyle(Paint.Style.FILL);
      param1Paint.setColor(this.color);
      param1Canvas.drawRect(param1Float, param1Int3, param1Float + this.width, param1Int5, param1Paint);
      param1Paint.setStyle(style);
      param1Paint.setColor(param1Int1);
    }
    
    public int getSize(@NonNull Paint param1Paint, CharSequence param1CharSequence, @IntRange(from = 0L) int param1Int1, @IntRange(from = 0L) int param1Int2, @Nullable Paint.FontMetricsInt param1FontMetricsInt) {
      return this.width;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/SpanUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */