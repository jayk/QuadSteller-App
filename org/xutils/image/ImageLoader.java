package org.xutils.image;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import org.xutils.cache.LruCache;
import org.xutils.cache.LruDiskCache;
import org.xutils.common.Callback;
import org.xutils.common.task.Priority;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

final class ImageLoader implements Callback.PrepareCallback<File, Drawable>, Callback.CacheCallback<Drawable>, Callback.ProgressCallback<Drawable>, Callback.TypedCallback<Drawable>, Callback.Cancelable {
  private static final String DISK_CACHE_DIR_NAME = "xUtils_img";
  
  private static final Executor EXECUTOR;
  
  private static final HashMap<String, FakeImageView> FAKE_IMG_MAP;
  
  private static final LruCache<MemCacheKey, Drawable> MEM_CACHE;
  
  private static final int MEM_CACHE_MIN_SIZE = 4194304;
  
  private static final AtomicLong SEQ_SEEK = new AtomicLong(0L);
  
  private static final Type loadType;
  
  private Callback.CacheCallback<Drawable> cacheCallback;
  
  private Callback.CommonCallback<Drawable> callback;
  
  private Callback.Cancelable cancelable;
  
  private volatile boolean cancelled = false;
  
  private boolean hasCache = false;
  
  private MemCacheKey key;
  
  private ImageOptions options;
  
  private Callback.PrepareCallback<File, Drawable> prepareCallback;
  
  private Callback.ProgressCallback<Drawable> progressCallback;
  
  private final long seq = SEQ_SEEK.incrementAndGet();
  
  private volatile boolean stopped = false;
  
  private WeakReference<ImageView> viewRef;
  
  static {
    EXECUTOR = (Executor)new PriorityExecutor(10, false);
    MEM_CACHE = new LruCache<MemCacheKey, Drawable>(4194304) {
        private boolean deepClear = false;
        
        protected void entryRemoved(boolean param1Boolean, MemCacheKey param1MemCacheKey, Drawable param1Drawable1, Drawable param1Drawable2) {
          super.entryRemoved(param1Boolean, param1MemCacheKey, param1Drawable1, param1Drawable2);
          if (param1Boolean && this.deepClear && param1Drawable1 instanceof ReusableDrawable)
            ((ReusableDrawable)param1Drawable1).setMemCacheKey(null); 
        }
        
        protected int sizeOf(MemCacheKey param1MemCacheKey, Drawable param1Drawable) {
          Bitmap bitmap;
          if (param1Drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable)param1Drawable).getBitmap();
            return (bitmap == null) ? 0 : bitmap.getByteCount();
          } 
          return (param1Drawable instanceof GifDrawable) ? ((GifDrawable)param1Drawable).getByteCount() : super.sizeOf(bitmap, param1Drawable);
        }
        
        public void trimToSize(int param1Int) {
          if (param1Int < 0)
            this.deepClear = true; 
          super.trimToSize(param1Int);
          this.deepClear = false;
        }
      };
    int i = 1048576 * ((ActivityManager)x.app().getSystemService("activity")).getMemoryClass() / 8;
    int j = i;
    if (i < 4194304)
      j = 4194304; 
    MEM_CACHE.resize(j);
    FAKE_IMG_MAP = new HashMap<String, FakeImageView>();
    loadType = File.class;
  }
  
  static void clearCacheFiles() {
    LruDiskCache.getDiskCache("xUtils_img").clearCacheFiles();
  }
  
  static void clearMemCache() {
    MEM_CACHE.evictAll();
  }
  
  private static RequestParams createRequestParams(String paramString, ImageOptions paramImageOptions) {
    RequestParams requestParams2 = new RequestParams(paramString);
    requestParams2.setCacheDirName("xUtils_img");
    requestParams2.setConnectTimeout(8000);
    requestParams2.setPriority(Priority.BG_LOW);
    requestParams2.setExecutor(EXECUTOR);
    requestParams2.setCancelFast(true);
    requestParams2.setUseCookie(false);
    RequestParams requestParams1 = requestParams2;
    if (paramImageOptions != null) {
      ImageOptions.ParamsBuilder paramsBuilder = paramImageOptions.getParamsBuilder();
      requestParams1 = requestParams2;
      if (paramsBuilder != null)
        requestParams1 = paramsBuilder.buildParams(requestParams2, paramImageOptions); 
    } 
    return requestParams1;
  }
  
  static Callback.Cancelable doBind(ImageView paramImageView, String paramString, ImageOptions paramImageOptions, Callback.CommonCallback<Drawable> paramCommonCallback) {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull -> 16
    //   4: aconst_null
    //   5: aload_2
    //   6: ldc 'view is null'
    //   8: aload_3
    //   9: invokestatic postArgsException : (Landroid/widget/ImageView;Lorg/xutils/image/ImageOptions;Ljava/lang/String;Lorg/xutils/common/Callback$CommonCallback;)V
    //   12: aconst_null
    //   13: astore_0
    //   14: aload_0
    //   15: areturn
    //   16: aload_1
    //   17: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   20: ifeq -> 36
    //   23: aload_0
    //   24: aload_2
    //   25: ldc 'url is null'
    //   27: aload_3
    //   28: invokestatic postArgsException : (Landroid/widget/ImageView;Lorg/xutils/image/ImageOptions;Ljava/lang/String;Lorg/xutils/common/Callback$CommonCallback;)V
    //   31: aconst_null
    //   32: astore_0
    //   33: goto -> 14
    //   36: aload_2
    //   37: astore #4
    //   39: aload_2
    //   40: ifnonnull -> 48
    //   43: getstatic org/xutils/image/ImageOptions.DEFAULT : Lorg/xutils/image/ImageOptions;
    //   46: astore #4
    //   48: aload #4
    //   50: aload_0
    //   51: invokevirtual optimizeMaxSize : (Landroid/widget/ImageView;)V
    //   54: new org/xutils/image/MemCacheKey
    //   57: dup
    //   58: aload_1
    //   59: aload #4
    //   61: invokespecial <init> : (Ljava/lang/String;Lorg/xutils/image/ImageOptions;)V
    //   64: astore #5
    //   66: aload_0
    //   67: invokevirtual getDrawable : ()Landroid/graphics/drawable/Drawable;
    //   70: astore_2
    //   71: aload_2
    //   72: instanceof org/xutils/image/AsyncDrawable
    //   75: ifeq -> 387
    //   78: aload_2
    //   79: checkcast org/xutils/image/AsyncDrawable
    //   82: invokevirtual getImageLoader : ()Lorg/xutils/image/ImageLoader;
    //   85: astore_2
    //   86: aload_2
    //   87: ifnull -> 118
    //   90: aload_2
    //   91: getfield stopped : Z
    //   94: ifne -> 118
    //   97: aload #5
    //   99: aload_2
    //   100: getfield key : Lorg/xutils/image/MemCacheKey;
    //   103: invokevirtual equals : (Ljava/lang/Object;)Z
    //   106: ifeq -> 114
    //   109: aconst_null
    //   110: astore_0
    //   111: goto -> 14
    //   114: aload_2
    //   115: invokevirtual cancel : ()V
    //   118: aconst_null
    //   119: astore_2
    //   120: aload #4
    //   122: invokevirtual isUseMemCache : ()Z
    //   125: ifeq -> 180
    //   128: getstatic org/xutils/image/ImageLoader.MEM_CACHE : Lorg/xutils/cache/LruCache;
    //   131: aload #5
    //   133: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   136: checkcast android/graphics/drawable/Drawable
    //   139: astore #5
    //   141: aload #5
    //   143: astore_2
    //   144: aload #5
    //   146: instanceof android/graphics/drawable/BitmapDrawable
    //   149: ifeq -> 180
    //   152: aload #5
    //   154: checkcast android/graphics/drawable/BitmapDrawable
    //   157: invokevirtual getBitmap : ()Landroid/graphics/Bitmap;
    //   160: astore #6
    //   162: aload #6
    //   164: ifnull -> 178
    //   167: aload #5
    //   169: astore_2
    //   170: aload #6
    //   172: invokevirtual isRecycled : ()Z
    //   175: ifeq -> 180
    //   178: aconst_null
    //   179: astore_2
    //   180: aload_2
    //   181: ifnull -> 605
    //   184: iconst_0
    //   185: istore #7
    //   187: iconst_0
    //   188: istore #8
    //   190: iload #8
    //   192: istore #9
    //   194: iload #7
    //   196: istore #10
    //   198: aload_3
    //   199: instanceof org/xutils/common/Callback$ProgressCallback
    //   202: ifeq -> 222
    //   205: iload #8
    //   207: istore #9
    //   209: iload #7
    //   211: istore #10
    //   213: aload_3
    //   214: checkcast org/xutils/common/Callback$ProgressCallback
    //   217: invokeinterface onWaiting : ()V
    //   222: iload #8
    //   224: istore #9
    //   226: iload #7
    //   228: istore #10
    //   230: aload_0
    //   231: aload #4
    //   233: invokevirtual getImageScaleType : ()Landroid/widget/ImageView$ScaleType;
    //   236: invokevirtual setScaleType : (Landroid/widget/ImageView$ScaleType;)V
    //   239: iload #8
    //   241: istore #9
    //   243: iload #7
    //   245: istore #10
    //   247: aload_0
    //   248: aload_2
    //   249: invokevirtual setImageDrawable : (Landroid/graphics/drawable/Drawable;)V
    //   252: iconst_1
    //   253: istore #7
    //   255: iconst_1
    //   256: istore #11
    //   258: iconst_1
    //   259: istore #8
    //   261: iload #7
    //   263: istore #9
    //   265: iload #11
    //   267: istore #10
    //   269: aload_3
    //   270: instanceof org/xutils/common/Callback$CacheCallback
    //   273: ifeq -> 433
    //   276: iload #7
    //   278: istore #9
    //   280: iload #11
    //   282: istore #10
    //   284: aload_3
    //   285: checkcast org/xutils/common/Callback$CacheCallback
    //   288: aload_2
    //   289: invokeinterface onCache : (Ljava/lang/Object;)Z
    //   294: istore #8
    //   296: iload #8
    //   298: istore #10
    //   300: iload #8
    //   302: ifne -> 460
    //   305: iload #8
    //   307: istore #9
    //   309: iload #8
    //   311: istore #10
    //   313: new org/xutils/image/ImageLoader
    //   316: astore_2
    //   317: iload #8
    //   319: istore #9
    //   321: iload #8
    //   323: istore #10
    //   325: aload_2
    //   326: invokespecial <init> : ()V
    //   329: iload #8
    //   331: istore #9
    //   333: iload #8
    //   335: istore #10
    //   337: aload_2
    //   338: aload_0
    //   339: aload_1
    //   340: aload #4
    //   342: aload_3
    //   343: invokespecial doLoad : (Landroid/widget/ImageView;Ljava/lang/String;Lorg/xutils/image/ImageOptions;Lorg/xutils/common/Callback$CommonCallback;)Lorg/xutils/common/Callback$Cancelable;
    //   346: astore_2
    //   347: aload_2
    //   348: astore_1
    //   349: aload_1
    //   350: astore_0
    //   351: iload #8
    //   353: ifeq -> 14
    //   356: aload_1
    //   357: astore_0
    //   358: aload_3
    //   359: ifnull -> 14
    //   362: aload_3
    //   363: invokeinterface onFinished : ()V
    //   368: aload_1
    //   369: astore_0
    //   370: goto -> 14
    //   373: astore_0
    //   374: aload_0
    //   375: invokevirtual getMessage : ()Ljava/lang/String;
    //   378: aload_0
    //   379: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   382: aload_1
    //   383: astore_0
    //   384: goto -> 14
    //   387: aload_2
    //   388: instanceof org/xutils/image/ReusableDrawable
    //   391: ifeq -> 118
    //   394: aload_2
    //   395: checkcast org/xutils/image/ReusableDrawable
    //   398: invokeinterface getMemCacheKey : ()Lorg/xutils/image/MemCacheKey;
    //   403: astore #6
    //   405: aload #6
    //   407: ifnull -> 118
    //   410: aload #6
    //   412: aload #5
    //   414: invokevirtual equals : (Ljava/lang/Object;)Z
    //   417: ifeq -> 118
    //   420: getstatic org/xutils/image/ImageLoader.MEM_CACHE : Lorg/xutils/cache/LruCache;
    //   423: aload #5
    //   425: aload_2
    //   426: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   429: pop
    //   430: goto -> 118
    //   433: iload #8
    //   435: istore #10
    //   437: aload_3
    //   438: ifnull -> 460
    //   441: iload #7
    //   443: istore #9
    //   445: iload #11
    //   447: istore #10
    //   449: aload_3
    //   450: aload_2
    //   451: invokeinterface onSuccess : (Ljava/lang/Object;)V
    //   456: iload #8
    //   458: istore #10
    //   460: iload #10
    //   462: ifeq -> 475
    //   465: aload_3
    //   466: ifnull -> 475
    //   469: aload_3
    //   470: invokeinterface onFinished : ()V
    //   475: aconst_null
    //   476: astore_0
    //   477: goto -> 14
    //   480: astore_0
    //   481: aload_0
    //   482: invokevirtual getMessage : ()Ljava/lang/String;
    //   485: aload_0
    //   486: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   489: goto -> 475
    //   492: astore_2
    //   493: iload #9
    //   495: istore #10
    //   497: aload_2
    //   498: invokevirtual getMessage : ()Ljava/lang/String;
    //   501: aload_2
    //   502: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   505: iconst_0
    //   506: istore #9
    //   508: iload #9
    //   510: istore #10
    //   512: new org/xutils/image/ImageLoader
    //   515: astore_2
    //   516: iload #9
    //   518: istore #10
    //   520: aload_2
    //   521: invokespecial <init> : ()V
    //   524: iload #9
    //   526: istore #10
    //   528: aload_2
    //   529: aload_0
    //   530: aload_1
    //   531: aload #4
    //   533: aload_3
    //   534: invokespecial doLoad : (Landroid/widget/ImageView;Ljava/lang/String;Lorg/xutils/image/ImageOptions;Lorg/xutils/common/Callback$CommonCallback;)Lorg/xutils/common/Callback$Cancelable;
    //   537: astore_1
    //   538: aload_1
    //   539: astore_0
    //   540: iconst_0
    //   541: ifeq -> 14
    //   544: aload_1
    //   545: astore_0
    //   546: aload_3
    //   547: ifnull -> 14
    //   550: aload_3
    //   551: invokeinterface onFinished : ()V
    //   556: aload_1
    //   557: astore_0
    //   558: goto -> 14
    //   561: astore_0
    //   562: aload_0
    //   563: invokevirtual getMessage : ()Ljava/lang/String;
    //   566: aload_0
    //   567: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   570: aload_1
    //   571: astore_0
    //   572: goto -> 14
    //   575: astore_0
    //   576: iload #10
    //   578: ifeq -> 591
    //   581: aload_3
    //   582: ifnull -> 591
    //   585: aload_3
    //   586: invokeinterface onFinished : ()V
    //   591: aload_0
    //   592: athrow
    //   593: astore_1
    //   594: aload_1
    //   595: invokevirtual getMessage : ()Ljava/lang/String;
    //   598: aload_1
    //   599: invokestatic e : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   602: goto -> 591
    //   605: new org/xutils/image/ImageLoader
    //   608: dup
    //   609: invokespecial <init> : ()V
    //   612: aload_0
    //   613: aload_1
    //   614: aload #4
    //   616: aload_3
    //   617: invokespecial doLoad : (Landroid/widget/ImageView;Ljava/lang/String;Lorg/xutils/image/ImageOptions;Lorg/xutils/common/Callback$CommonCallback;)Lorg/xutils/common/Callback$Cancelable;
    //   620: astore_0
    //   621: goto -> 14
    // Exception table:
    //   from	to	target	type
    //   198	205	492	java/lang/Throwable
    //   198	205	575	finally
    //   213	222	492	java/lang/Throwable
    //   213	222	575	finally
    //   230	239	492	java/lang/Throwable
    //   230	239	575	finally
    //   247	252	492	java/lang/Throwable
    //   247	252	575	finally
    //   269	276	492	java/lang/Throwable
    //   269	276	575	finally
    //   284	296	492	java/lang/Throwable
    //   284	296	575	finally
    //   313	317	492	java/lang/Throwable
    //   313	317	575	finally
    //   325	329	492	java/lang/Throwable
    //   325	329	575	finally
    //   337	347	492	java/lang/Throwable
    //   337	347	575	finally
    //   362	368	373	java/lang/Throwable
    //   449	456	492	java/lang/Throwable
    //   449	456	575	finally
    //   469	475	480	java/lang/Throwable
    //   497	505	575	finally
    //   512	516	575	finally
    //   520	524	575	finally
    //   528	538	575	finally
    //   550	556	561	java/lang/Throwable
    //   585	591	593	java/lang/Throwable
  }
  
  private Callback.Cancelable doLoad(ImageView paramImageView, String paramString, ImageOptions paramImageOptions, Callback.CommonCallback<Drawable> paramCommonCallback) {
    this.viewRef = new WeakReference<ImageView>(paramImageView);
    this.options = paramImageOptions;
    this.key = new MemCacheKey(paramString, paramImageOptions);
    this.callback = paramCommonCallback;
    if (paramCommonCallback instanceof Callback.ProgressCallback)
      this.progressCallback = (Callback.ProgressCallback<Drawable>)paramCommonCallback; 
    if (paramCommonCallback instanceof Callback.PrepareCallback)
      this.prepareCallback = (Callback.PrepareCallback)paramCommonCallback; 
    if (paramCommonCallback instanceof Callback.CacheCallback)
      this.cacheCallback = (Callback.CacheCallback<Drawable>)paramCommonCallback; 
    if (paramImageOptions.isForceLoadingDrawable()) {
      Drawable drawable = paramImageOptions.getLoadingDrawable(paramImageView);
      paramImageView.setScaleType(paramImageOptions.getPlaceholderScaleType());
      paramImageView.setImageDrawable(new AsyncDrawable(this, drawable));
    } else {
      paramImageView.setImageDrawable(new AsyncDrawable(this, paramImageView.getDrawable()));
    } 
    RequestParams requestParams = createRequestParams(paramString, paramImageOptions);
    if (paramImageView instanceof FakeImageView)
      synchronized (FAKE_IMG_MAP) {
        FAKE_IMG_MAP.put(paramString, (FakeImageView)paramImageView);
        Callback.Cancelable cancelable1 = x.http().get(requestParams, (Callback.CommonCallback)this);
        this.cancelable = cancelable1;
        return cancelable1;
      }  
    Callback.Cancelable cancelable = x.http().get(requestParams, (Callback.CommonCallback)this);
    this.cancelable = cancelable;
    return cancelable;
  }
  
  static Callback.Cancelable doLoadDrawable(String paramString, ImageOptions paramImageOptions, Callback.CommonCallback<Drawable> paramCommonCallback) {
    String str = null;
    if (TextUtils.isEmpty(paramString)) {
      postArgsException(null, paramImageOptions, "url is null", paramCommonCallback);
      return (Callback.Cancelable)str;
    } 
    HashMap<String, FakeImageView> hashMap = FAKE_IMG_MAP;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/HashMap<[ObjectType{java/lang/String}, InnerObjectType{ObjectType{org/xutils/image/ImageLoader}.Lorg/xutils/image/ImageLoader$FakeImageView;}]>}, name=null} */
    try {
      FakeImageView fakeImageView2 = FAKE_IMG_MAP.get(paramString);
      FakeImageView fakeImageView1 = fakeImageView2;
      if (fakeImageView2 == null) {
        fakeImageView1 = new FakeImageView();
        this();
      } 
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/HashMap<[ObjectType{java/lang/String}, InnerObjectType{ObjectType{org/xutils/image/ImageLoader}.Lorg/xutils/image/ImageLoader$FakeImageView;}]>}, name=null} */
      Callback.Cancelable cancelable = doBind(fakeImageView1, paramString, paramImageOptions, paramCommonCallback);
    } finally {}
    return (Callback.Cancelable)paramString;
  }
  
  static Callback.Cancelable doLoadFile(String paramString, ImageOptions paramImageOptions, Callback.CacheCallback<File> paramCacheCallback) {
    String str = null;
    if (TextUtils.isEmpty(paramString)) {
      postArgsException(null, paramImageOptions, "url is null", (Callback.CommonCallback<?>)paramCacheCallback);
      return (Callback.Cancelable)str;
    } 
    RequestParams requestParams = createRequestParams(paramString, paramImageOptions);
    return x.http().get(requestParams, (Callback.CommonCallback)paramCacheCallback);
  }
  
  private static void postArgsException(final ImageView view, final ImageOptions options, final String exMsg, final Callback.CommonCallback<?> callback) {
    x.task().autoPost(new Runnable() {
          public void run() {
            try {
              if (callback instanceof Callback.ProgressCallback)
                ((Callback.ProgressCallback)callback).onWaiting(); 
              if (view != null && options != null) {
                view.setScaleType(options.getPlaceholderScaleType());
                view.setImageDrawable(options.getFailureDrawable(view));
              } 
              if (callback != null) {
                Callback.CommonCallback commonCallback = callback;
                IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
                this(exMsg);
                commonCallback.onError(illegalArgumentException, false);
              } 
              return;
            } catch (Throwable throwable) {
              Callback.CommonCallback commonCallback = callback;
              if (commonCallback != null)
                try {
                  callback.onError(throwable, true);
                } catch (Throwable throwable1) {} 
              return;
            } finally {
              if (callback != null)
                try {
                  callback.onFinished();
                } catch (Throwable throwable) {
                  LogUtil.e(throwable.getMessage(), throwable);
                }  
            } 
          }
        });
  }
  
  private void setErrorDrawable4Callback() {
    ImageView imageView = this.viewRef.get();
    if (imageView != null) {
      Drawable drawable = this.options.getFailureDrawable(imageView);
      imageView.setScaleType(this.options.getPlaceholderScaleType());
      imageView.setImageDrawable(drawable);
    } 
  }
  
  private void setSuccessDrawable4Callback(Drawable paramDrawable) {
    ImageView imageView = this.viewRef.get();
    if (imageView != null) {
      imageView.setScaleType(this.options.getImageScaleType());
      if (paramDrawable instanceof GifDrawable) {
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER)
          imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE); 
        imageView.setLayerType(1, null);
      } 
      if (this.options.getAnimation() != null) {
        ImageAnimationHelper.animationDisplay(imageView, paramDrawable, this.options.getAnimation());
        return;
      } 
    } else {
      return;
    } 
    if (this.options.isFadeIn()) {
      ImageAnimationHelper.fadeInDisplay(imageView, paramDrawable);
      return;
    } 
    imageView.setImageDrawable(paramDrawable);
  }
  
  private boolean validView4Callback(boolean paramBoolean) {
    boolean bool = false;
    ImageView imageView = this.viewRef.get();
    null = bool;
    if (imageView != null) {
      Drawable drawable = imageView.getDrawable();
      if (drawable instanceof AsyncDrawable) {
        ImageLoader imageLoader = ((AsyncDrawable)drawable).getImageLoader();
        if (imageLoader != null) {
          if (imageLoader == this) {
            if (imageView.getVisibility() != 0) {
              imageLoader.cancel();
              return bool;
            } 
            return true;
          } 
          if (this.seq > imageLoader.seq) {
            imageLoader.cancel();
            return true;
          } 
          cancel();
          return bool;
        } 
      } else if (paramBoolean) {
        cancel();
        return bool;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public void cancel() {
    this.stopped = true;
    this.cancelled = true;
    if (this.cancelable != null)
      this.cancelable.cancel(); 
  }
  
  public Type getLoadType() {
    return loadType;
  }
  
  public boolean isCancelled() {
    boolean bool = false;
    if (this.cancelled || !validView4Callback(false))
      bool = true; 
    return bool;
  }
  
  public boolean onCache(Drawable paramDrawable) {
    boolean bool = false;
    if (validView4Callback(true) && paramDrawable != null) {
      this.hasCache = true;
      setSuccessDrawable4Callback(paramDrawable);
      if (this.cacheCallback != null)
        return this.cacheCallback.onCache(paramDrawable); 
      if (this.callback != null) {
        this.callback.onSuccess(paramDrawable);
        return true;
      } 
      bool = true;
    } 
    return bool;
  }
  
  public void onCancelled(Callback.CancelledException paramCancelledException) {
    this.stopped = true;
    if (validView4Callback(false) && this.callback != null)
      this.callback.onCancelled(paramCancelledException); 
  }
  
  public void onError(Throwable paramThrowable, boolean paramBoolean) {
    this.stopped = true;
    if (validView4Callback(false)) {
      if (paramThrowable instanceof org.xutils.ex.FileLockedException) {
        LogUtil.d("ImageFileLocked: " + this.key.url);
        x.task().postDelayed(new Runnable() {
              public void run() {
                ImageLoader.doBind(ImageLoader.this.viewRef.get(), ImageLoader.this.key.url, ImageLoader.this.options, ImageLoader.this.callback);
              }
            }10L);
        return;
      } 
      LogUtil.e(this.key.url, paramThrowable);
      setErrorDrawable4Callback();
      if (this.callback != null)
        this.callback.onError(paramThrowable, paramBoolean); 
    } 
  }
  
  public void onFinished() {
    // Byte code:
    //   0: aload_0
    //   1: iconst_1
    //   2: putfield stopped : Z
    //   5: aload_0
    //   6: getfield viewRef : Ljava/lang/ref/WeakReference;
    //   9: invokevirtual get : ()Ljava/lang/Object;
    //   12: checkcast android/widget/ImageView
    //   15: instanceof org/xutils/image/ImageLoader$FakeImageView
    //   18: ifeq -> 43
    //   21: getstatic org/xutils/image/ImageLoader.FAKE_IMG_MAP : Ljava/util/HashMap;
    //   24: astore_1
    //   25: aload_1
    //   26: monitorenter
    //   27: getstatic org/xutils/image/ImageLoader.FAKE_IMG_MAP : Ljava/util/HashMap;
    //   30: aload_0
    //   31: getfield key : Lorg/xutils/image/MemCacheKey;
    //   34: getfield url : Ljava/lang/String;
    //   37: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   40: pop
    //   41: aload_1
    //   42: monitorexit
    //   43: aload_0
    //   44: iconst_0
    //   45: invokespecial validView4Callback : (Z)Z
    //   48: ifne -> 57
    //   51: return
    //   52: astore_2
    //   53: aload_1
    //   54: monitorexit
    //   55: aload_2
    //   56: athrow
    //   57: aload_0
    //   58: getfield callback : Lorg/xutils/common/Callback$CommonCallback;
    //   61: ifnull -> 51
    //   64: aload_0
    //   65: getfield callback : Lorg/xutils/common/Callback$CommonCallback;
    //   68: invokeinterface onFinished : ()V
    //   73: goto -> 51
    // Exception table:
    //   from	to	target	type
    //   27	43	52	finally
    //   53	55	52	finally
  }
  
  public void onLoading(long paramLong1, long paramLong2, boolean paramBoolean) {
    if (validView4Callback(true) && this.progressCallback != null)
      this.progressCallback.onLoading(paramLong1, paramLong2, paramBoolean); 
  }
  
  public void onStarted() {
    if (validView4Callback(true) && this.progressCallback != null)
      this.progressCallback.onStarted(); 
  }
  
  public void onSuccess(Drawable paramDrawable) {
    boolean bool;
    if (!this.hasCache) {
      bool = true;
    } else {
      bool = false;
    } 
    if (validView4Callback(bool) && paramDrawable != null) {
      setSuccessDrawable4Callback(paramDrawable);
      if (this.callback != null)
        this.callback.onSuccess(paramDrawable); 
    } 
  }
  
  public void onWaiting() {
    if (this.progressCallback != null)
      this.progressCallback.onWaiting(); 
  }
  
  public Drawable prepare(File paramFile) {
    if (!validView4Callback(true))
      return null; 
    Drawable drawable = null;
    try {
      if (this.prepareCallback != null)
        drawable = (Drawable)this.prepareCallback.prepare(paramFile); 
      Drawable drawable1 = drawable;
      if (drawable == null)
        drawable1 = ImageDecoder.decodeFileWithLock(paramFile, this.options, this); 
      drawable = drawable1;
      if (drawable1 != null) {
        drawable = drawable1;
        if (drawable1 instanceof ReusableDrawable) {
          ((ReusableDrawable)drawable1).setMemCacheKey(this.key);
          MEM_CACHE.put(this.key, drawable1);
          drawable = drawable1;
        } 
      } 
    } catch (IOException iOException) {
      IOUtil.deleteFileOrDir(paramFile);
      LogUtil.w(iOException.getMessage(), iOException);
      drawable = null;
    } 
    return drawable;
  }
  
  @SuppressLint({"ViewConstructor"})
  private static final class FakeImageView extends ImageView {
    private Drawable drawable;
    
    public FakeImageView() {
      super((Context)x.app());
    }
    
    public Drawable getDrawable() {
      return this.drawable;
    }
    
    public void setImageDrawable(Drawable param1Drawable) {
      this.drawable = param1Drawable;
    }
    
    public void setLayerType(int param1Int, Paint param1Paint) {}
    
    public void setScaleType(ImageView.ScaleType param1ScaleType) {}
    
    public void startAnimation(Animation param1Animation) {}
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/image/ImageLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */