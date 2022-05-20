package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@SuppressLint({"NewApi"})
public class AnimatedVectorDrawableCompat extends VectorDrawableCommon implements Animatable {
  private static final String ANIMATED_VECTOR = "animated-vector";
  
  private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
  
  private static final String LOGTAG = "AnimatedVDCompat";
  
  private static final String TARGET = "target";
  
  private AnimatedVectorDrawableCompatState mAnimatedVectorState;
  
  private ArgbEvaluator mArgbEvaluator = null;
  
  AnimatedVectorDrawableDelegateState mCachedConstantStateDelegate;
  
  final Drawable.Callback mCallback = new Drawable.Callback() {
      public void invalidateDrawable(Drawable param1Drawable) {
        AnimatedVectorDrawableCompat.this.invalidateSelf();
      }
      
      public void scheduleDrawable(Drawable param1Drawable, Runnable param1Runnable, long param1Long) {
        AnimatedVectorDrawableCompat.this.scheduleSelf(param1Runnable, param1Long);
      }
      
      public void unscheduleDrawable(Drawable param1Drawable, Runnable param1Runnable) {
        AnimatedVectorDrawableCompat.this.unscheduleSelf(param1Runnable);
      }
    };
  
  private Context mContext;
  
  AnimatedVectorDrawableCompat() {
    this((Context)null, (AnimatedVectorDrawableCompatState)null, (Resources)null);
  }
  
  private AnimatedVectorDrawableCompat(@Nullable Context paramContext) {
    this(paramContext, (AnimatedVectorDrawableCompatState)null, (Resources)null);
  }
  
  private AnimatedVectorDrawableCompat(@Nullable Context paramContext, @Nullable AnimatedVectorDrawableCompatState paramAnimatedVectorDrawableCompatState, @Nullable Resources paramResources) {
    this.mContext = paramContext;
    if (paramAnimatedVectorDrawableCompatState != null) {
      this.mAnimatedVectorState = paramAnimatedVectorDrawableCompatState;
      return;
    } 
    this.mAnimatedVectorState = new AnimatedVectorDrawableCompatState(paramContext, paramAnimatedVectorDrawableCompatState, this.mCallback, paramResources);
  }
  
  @Nullable
  public static AnimatedVectorDrawableCompat create(@NonNull Context paramContext, @DrawableRes int paramInt) {
    if (Build.VERSION.SDK_INT >= 24) {
      AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat(paramContext);
      animatedVectorDrawableCompat.mDelegateDrawable = ResourcesCompat.getDrawable(paramContext.getResources(), paramInt, paramContext.getTheme());
      animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
      animatedVectorDrawableCompat.mCachedConstantStateDelegate = new AnimatedVectorDrawableDelegateState(animatedVectorDrawableCompat.mDelegateDrawable.getConstantState());
      return animatedVectorDrawableCompat;
    } 
    Resources resources = paramContext.getResources();
    try {
      XmlPullParserException xmlPullParserException;
      XmlResourceParser xmlResourceParser = resources.getXml(paramInt);
      AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xmlResourceParser);
      do {
        paramInt = xmlResourceParser.next();
      } while (paramInt != 2 && paramInt != 1);
      if (paramInt != 2) {
        xmlPullParserException = new XmlPullParserException();
        this("No start tag found");
        throw xmlPullParserException;
      } 
      AnimatedVectorDrawableCompat animatedVectorDrawableCompat = createFromXmlInner((Context)xmlPullParserException, xmlPullParserException.getResources(), (XmlPullParser)xmlResourceParser, attributeSet, xmlPullParserException.getTheme());
    } catch (XmlPullParserException xmlPullParserException) {
      Log.e("AnimatedVDCompat", "parser error", (Throwable)xmlPullParserException);
      xmlPullParserException = null;
    } catch (IOException iOException) {
      Log.e("AnimatedVDCompat", "parser error", iOException);
    } 
    return (AnimatedVectorDrawableCompat)iOException;
  }
  
  public static AnimatedVectorDrawableCompat createFromXmlInner(Context paramContext, Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat(paramContext);
    animatedVectorDrawableCompat.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    return animatedVectorDrawableCompat;
  }
  
  private boolean isStarted() {
    boolean bool = false;
    ArrayList<Animator> arrayList = this.mAnimatedVectorState.mAnimators;
    if (arrayList == null)
      return bool; 
    int i = arrayList.size();
    byte b = 0;
    while (true) {
      boolean bool1 = bool;
      if (b < i) {
        if (((Animator)arrayList.get(b)).isRunning())
          return true; 
        b++;
        continue;
      } 
      return bool1;
    } 
  }
  
  private void setupAnimatorsForTarget(String paramString, Animator paramAnimator) {
    paramAnimator.setTarget(this.mAnimatedVectorState.mVectorDrawable.getTargetByName(paramString));
    if (Build.VERSION.SDK_INT < 21)
      setupColorAnimator(paramAnimator); 
    if (this.mAnimatedVectorState.mAnimators == null) {
      this.mAnimatedVectorState.mAnimators = new ArrayList<Animator>();
      this.mAnimatedVectorState.mTargetNameMap = new ArrayMap();
    } 
    this.mAnimatedVectorState.mAnimators.add(paramAnimator);
    this.mAnimatedVectorState.mTargetNameMap.put(paramAnimator, paramString);
  }
  
  private void setupColorAnimator(Animator paramAnimator) {
    if (paramAnimator instanceof AnimatorSet) {
      ArrayList<Animator> arrayList = ((AnimatorSet)paramAnimator).getChildAnimations();
      if (arrayList != null)
        for (byte b = 0; b < arrayList.size(); b++)
          setupColorAnimator(arrayList.get(b));  
    } 
    if (paramAnimator instanceof ObjectAnimator) {
      ObjectAnimator objectAnimator = (ObjectAnimator)paramAnimator;
      String str = objectAnimator.getPropertyName();
      if ("fillColor".equals(str) || "strokeColor".equals(str)) {
        if (this.mArgbEvaluator == null)
          this.mArgbEvaluator = new ArgbEvaluator(); 
        objectAnimator.setEvaluator((TypeEvaluator)this.mArgbEvaluator);
      } 
    } 
  }
  
  public void applyTheme(Resources.Theme paramTheme) {
    if (this.mDelegateDrawable != null)
      DrawableCompat.applyTheme(this.mDelegateDrawable, paramTheme); 
  }
  
  public boolean canApplyTheme() {
    return (this.mDelegateDrawable != null) ? DrawableCompat.canApplyTheme(this.mDelegateDrawable) : false;
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.draw(paramCanvas);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.draw(paramCanvas);
    if (isStarted())
      invalidateSelf(); 
  }
  
  public int getAlpha() {
    return (this.mDelegateDrawable != null) ? DrawableCompat.getAlpha(this.mDelegateDrawable) : this.mAnimatedVectorState.mVectorDrawable.getAlpha();
  }
  
  public int getChangingConfigurations() {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getChangingConfigurations() : (super.getChangingConfigurations() | this.mAnimatedVectorState.mChangingConfigurations);
  }
  
  public Drawable.ConstantState getConstantState() {
    return (this.mDelegateDrawable != null) ? new AnimatedVectorDrawableDelegateState(this.mDelegateDrawable.getConstantState()) : null;
  }
  
  public int getIntrinsicHeight() {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getIntrinsicHeight() : this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
  }
  
  public int getIntrinsicWidth() {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getIntrinsicWidth() : this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
  }
  
  public int getOpacity() {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.getOpacity() : this.mAnimatedVectorState.mVectorDrawable.getOpacity();
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet) throws XmlPullParserException, IOException {
    inflate(paramResources, paramXmlPullParser, paramAttributeSet, (Resources.Theme)null);
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme) throws XmlPullParserException, IOException {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.inflate(this.mDelegateDrawable, paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
      return;
    } 
    int i = paramXmlPullParser.getEventType();
    int j = paramXmlPullParser.getDepth();
    while (true) {
      if (i != 1 && (paramXmlPullParser.getDepth() >= j + 1 || i != 3)) {
        if (i == 2) {
          TypedArray typedArray;
          String str = paramXmlPullParser.getName();
          if ("animated-vector".equals(str)) {
            typedArray = VectorDrawableCommon.obtainAttributes(paramResources, paramTheme, paramAttributeSet, AndroidResources.styleable_AnimatedVectorDrawable);
            i = typedArray.getResourceId(0, 0);
            if (i != 0) {
              VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(paramResources, i, paramTheme);
              vectorDrawableCompat.setAllowCaching(false);
              vectorDrawableCompat.setCallback(this.mCallback);
              if (this.mAnimatedVectorState.mVectorDrawable != null)
                this.mAnimatedVectorState.mVectorDrawable.setCallback(null); 
              this.mAnimatedVectorState.mVectorDrawable = vectorDrawableCompat;
            } 
            typedArray.recycle();
          } else if ("target".equals(typedArray)) {
            typedArray = paramResources.obtainAttributes(paramAttributeSet, AndroidResources.styleable_AnimatedVectorDrawableTarget);
            String str1 = typedArray.getString(0);
            i = typedArray.getResourceId(1, 0);
            if (i != 0)
              if (this.mContext != null) {
                setupAnimatorsForTarget(str1, AnimatorInflater.loadAnimator(this.mContext, i));
              } else {
                throw new IllegalStateException("Context can't be null when inflating animators");
              }  
            typedArray.recycle();
          } 
        } 
        i = paramXmlPullParser.next();
        continue;
      } 
      return;
    } 
  }
  
  public boolean isAutoMirrored() {
    return (this.mDelegateDrawable != null) ? DrawableCompat.isAutoMirrored(this.mDelegateDrawable) : this.mAnimatedVectorState.mVectorDrawable.isAutoMirrored();
  }
  
  public boolean isRunning() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mDelegateDrawable : Landroid/graphics/drawable/Drawable;
    //   4: ifnull -> 20
    //   7: aload_0
    //   8: getfield mDelegateDrawable : Landroid/graphics/drawable/Drawable;
    //   11: checkcast android/graphics/drawable/AnimatedVectorDrawable
    //   14: invokevirtual isRunning : ()Z
    //   17: istore_1
    //   18: iload_1
    //   19: ireturn
    //   20: aload_0
    //   21: getfield mAnimatedVectorState : Landroid/support/graphics/drawable/AnimatedVectorDrawableCompat$AnimatedVectorDrawableCompatState;
    //   24: getfield mAnimators : Ljava/util/ArrayList;
    //   27: astore_2
    //   28: aload_2
    //   29: invokevirtual size : ()I
    //   32: istore_3
    //   33: iconst_0
    //   34: istore #4
    //   36: iload #4
    //   38: iload_3
    //   39: if_icmpge -> 68
    //   42: aload_2
    //   43: iload #4
    //   45: invokevirtual get : (I)Ljava/lang/Object;
    //   48: checkcast android/animation/Animator
    //   51: invokevirtual isRunning : ()Z
    //   54: ifeq -> 62
    //   57: iconst_1
    //   58: istore_1
    //   59: goto -> 18
    //   62: iinc #4, 1
    //   65: goto -> 36
    //   68: iconst_0
    //   69: istore_1
    //   70: goto -> 18
  }
  
  public boolean isStateful() {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.isStateful() : this.mAnimatedVectorState.mVectorDrawable.isStateful();
  }
  
  public Drawable mutate() {
    if (this.mDelegateDrawable != null)
      this.mDelegateDrawable.mutate(); 
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setBounds(paramRect);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setBounds(paramRect);
  }
  
  protected boolean onLevelChange(int paramInt) {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.setLevel(paramInt) : this.mAnimatedVectorState.mVectorDrawable.setLevel(paramInt);
  }
  
  protected boolean onStateChange(int[] paramArrayOfint) {
    return (this.mDelegateDrawable != null) ? this.mDelegateDrawable.setState(paramArrayOfint) : this.mAnimatedVectorState.mVectorDrawable.setState(paramArrayOfint);
  }
  
  public void setAlpha(int paramInt) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setAlpha(paramInt);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setAlpha(paramInt);
  }
  
  public void setAutoMirrored(boolean paramBoolean) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setAutoMirrored(paramBoolean);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setAutoMirrored(paramBoolean);
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setColorFilter(paramColorFilter);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setColorFilter(paramColorFilter);
  }
  
  public void setTint(int paramInt) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setTint(this.mDelegateDrawable, paramInt);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setTint(paramInt);
  }
  
  public void setTintList(ColorStateList paramColorStateList) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setTintList(this.mDelegateDrawable, paramColorStateList);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setTintList(paramColorStateList);
  }
  
  public void setTintMode(PorterDuff.Mode paramMode) {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setTintMode(this.mDelegateDrawable, paramMode);
      return;
    } 
    this.mAnimatedVectorState.mVectorDrawable.setTintMode(paramMode);
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2) {
    if (this.mDelegateDrawable != null)
      return this.mDelegateDrawable.setVisible(paramBoolean1, paramBoolean2); 
    this.mAnimatedVectorState.mVectorDrawable.setVisible(paramBoolean1, paramBoolean2);
    return super.setVisible(paramBoolean1, paramBoolean2);
  }
  
  public void start() {
    if (this.mDelegateDrawable != null) {
      ((AnimatedVectorDrawable)this.mDelegateDrawable).start();
      return;
    } 
    if (!isStarted()) {
      ArrayList<Animator> arrayList = this.mAnimatedVectorState.mAnimators;
      int i = arrayList.size();
      for (byte b = 0; b < i; b++)
        ((Animator)arrayList.get(b)).start(); 
      invalidateSelf();
    } 
  }
  
  public void stop() {
    if (this.mDelegateDrawable != null) {
      ((AnimatedVectorDrawable)this.mDelegateDrawable).stop();
      return;
    } 
    ArrayList<Animator> arrayList = this.mAnimatedVectorState.mAnimators;
    int i = arrayList.size();
    byte b = 0;
    while (true) {
      if (b < i) {
        ((Animator)arrayList.get(b)).end();
        b++;
        continue;
      } 
      return;
    } 
  }
  
  private static class AnimatedVectorDrawableCompatState extends Drawable.ConstantState {
    ArrayList<Animator> mAnimators;
    
    int mChangingConfigurations;
    
    ArrayMap<Animator, String> mTargetNameMap;
    
    VectorDrawableCompat mVectorDrawable;
    
    public AnimatedVectorDrawableCompatState(Context param1Context, AnimatedVectorDrawableCompatState param1AnimatedVectorDrawableCompatState, Drawable.Callback param1Callback, Resources param1Resources) {
      if (param1AnimatedVectorDrawableCompatState != null) {
        this.mChangingConfigurations = param1AnimatedVectorDrawableCompatState.mChangingConfigurations;
        if (param1AnimatedVectorDrawableCompatState.mVectorDrawable != null) {
          Drawable.ConstantState constantState = param1AnimatedVectorDrawableCompatState.mVectorDrawable.getConstantState();
          if (param1Resources != null) {
            this.mVectorDrawable = (VectorDrawableCompat)constantState.newDrawable(param1Resources);
          } else {
            this.mVectorDrawable = (VectorDrawableCompat)constantState.newDrawable();
          } 
          this.mVectorDrawable = (VectorDrawableCompat)this.mVectorDrawable.mutate();
          this.mVectorDrawable.setCallback(param1Callback);
          this.mVectorDrawable.setBounds(param1AnimatedVectorDrawableCompatState.mVectorDrawable.getBounds());
          this.mVectorDrawable.setAllowCaching(false);
        } 
        if (param1AnimatedVectorDrawableCompatState.mAnimators != null) {
          int i = param1AnimatedVectorDrawableCompatState.mAnimators.size();
          this.mAnimators = new ArrayList<Animator>(i);
          this.mTargetNameMap = new ArrayMap(i);
          for (byte b = 0; b < i; b++) {
            Animator animator2 = param1AnimatedVectorDrawableCompatState.mAnimators.get(b);
            Animator animator1 = animator2.clone();
            String str = (String)param1AnimatedVectorDrawableCompatState.mTargetNameMap.get(animator2);
            animator1.setTarget(this.mVectorDrawable.getTargetByName(str));
            this.mAnimators.add(animator1);
            this.mTargetNameMap.put(animator1, str);
          } 
        } 
      } 
    }
    
    public int getChangingConfigurations() {
      return this.mChangingConfigurations;
    }
    
    public Drawable newDrawable() {
      throw new IllegalStateException("No constant state support for SDK < 24.");
    }
    
    public Drawable newDrawable(Resources param1Resources) {
      throw new IllegalStateException("No constant state support for SDK < 24.");
    }
  }
  
  private static class AnimatedVectorDrawableDelegateState extends Drawable.ConstantState {
    private final Drawable.ConstantState mDelegateState;
    
    public AnimatedVectorDrawableDelegateState(Drawable.ConstantState param1ConstantState) {
      this.mDelegateState = param1ConstantState;
    }
    
    public boolean canApplyTheme() {
      return this.mDelegateState.canApplyTheme();
    }
    
    public int getChangingConfigurations() {
      return this.mDelegateState.getChangingConfigurations();
    }
    
    public Drawable newDrawable() {
      AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
      animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable();
      animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
      return animatedVectorDrawableCompat;
    }
    
    public Drawable newDrawable(Resources param1Resources) {
      AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
      animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(param1Resources);
      animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
      return animatedVectorDrawableCompat;
    }
    
    public Drawable newDrawable(Resources param1Resources, Resources.Theme param1Theme) {
      AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
      animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(param1Resources, param1Theme);
      animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
      return animatedVectorDrawableCompat;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/graphics/drawable/AnimatedVectorDrawableCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */