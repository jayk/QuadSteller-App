package org.xutils.http;

import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import org.xutils.common.Callback;
import org.xutils.common.task.AbsTask;
import org.xutils.common.task.Priority;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.IOUtil;
import org.xutils.common.util.ParameterizedTypeUtil;
import org.xutils.http.app.RequestInterceptListener;
import org.xutils.http.app.RequestTracker;
import org.xutils.http.request.UriRequest;
import org.xutils.http.request.UriRequestFactory;
import org.xutils.x;

public class HttpTask<ResultType> extends AbsTask<ResultType> implements ProgressHandler {
  private static final PriorityExecutor CACHE_EXECUTOR;
  
  private static final HashMap<String, WeakReference<HttpTask<?>>> DOWNLOAD_TASK;
  
  private static final int FLAG_CACHE = 2;
  
  private static final int FLAG_PROGRESS = 3;
  
  private static final int FLAG_REQUEST_CREATED = 1;
  
  private static final PriorityExecutor HTTP_EXECUTOR;
  
  private static final int MAX_FILE_LOAD_WORKER = 3;
  
  private static final AtomicInteger sCurrFileLoadCount = new AtomicInteger(0);
  
  private Callback.CacheCallback<ResultType> cacheCallback;
  
  private final Object cacheLock = new Object();
  
  private final Callback.CommonCallback<ResultType> callback;
  
  private final Executor executor;
  
  private volatile boolean hasException = false;
  
  private long lastUpdateTime;
  
  private Type loadType;
  
  private long loadingUpdateMaxTimeSpan = 300L;
  
  private RequestParams params;
  
  private Callback.PrepareCallback prepareCallback;
  
  private Callback.ProgressCallback progressCallback;
  
  private Object rawResult = null;
  
  private UriRequest request;
  
  private RequestInterceptListener requestInterceptListener;
  
  private RequestWorker requestWorker;
  
  private RequestTracker tracker;
  
  private volatile Boolean trustCache = null;
  
  static {
    DOWNLOAD_TASK = new HashMap<String, WeakReference<HttpTask<?>>>(1);
    HTTP_EXECUTOR = new PriorityExecutor(5, true);
    CACHE_EXECUTOR = new PriorityExecutor(5, true);
  }
  
  public HttpTask(RequestParams paramRequestParams, Callback.Cancelable paramCancelable, Callback.CommonCallback<ResultType> paramCommonCallback) {
    super(paramCancelable);
    assert paramRequestParams != null;
    assert paramCommonCallback != null;
    this.params = paramRequestParams;
    this.callback = paramCommonCallback;
    if (paramCommonCallback instanceof Callback.CacheCallback)
      this.cacheCallback = (Callback.CacheCallback<ResultType>)paramCommonCallback; 
    if (paramCommonCallback instanceof Callback.PrepareCallback)
      this.prepareCallback = (Callback.PrepareCallback)paramCommonCallback; 
    if (paramCommonCallback instanceof Callback.ProgressCallback)
      this.progressCallback = (Callback.ProgressCallback)paramCommonCallback; 
    if (paramCommonCallback instanceof RequestInterceptListener)
      this.requestInterceptListener = (RequestInterceptListener)paramCommonCallback; 
    RequestTracker requestTracker2 = paramRequestParams.getRequestTracker();
    RequestTracker requestTracker1 = requestTracker2;
    if (requestTracker2 == null)
      if (paramCommonCallback instanceof RequestTracker) {
        requestTracker1 = (RequestTracker)paramCommonCallback;
      } else {
        requestTracker1 = UriRequestFactory.getDefaultTracker();
      }  
    if (requestTracker1 != null)
      this.tracker = new RequestTrackerWrapper(requestTracker1); 
    if (paramRequestParams.getExecutor() != null) {
      this.executor = paramRequestParams.getExecutor();
      return;
    } 
    if (this.cacheCallback != null) {
      this.executor = (Executor)CACHE_EXECUTOR;
      return;
    } 
    this.executor = (Executor)HTTP_EXECUTOR;
  }
  
  private void checkDownloadTask() {
    if (File.class == this.loadType) {
      synchronized (DOWNLOAD_TASK) {
        String str = this.params.getSaveFilePath();
        if (!TextUtils.isEmpty(str)) {
          WeakReference<HttpTask> weakReference = (WeakReference)DOWNLOAD_TASK.get(str);
          if (weakReference != null) {
            HttpTask httpTask = weakReference.get();
            if (httpTask != null) {
              httpTask.cancel();
              httpTask.closeRequestSync();
            } 
            DOWNLOAD_TASK.remove(str);
          } 
          HashMap<String, WeakReference<HttpTask<?>>> hashMap = DOWNLOAD_TASK;
          weakReference = new WeakReference<HttpTask>();
          this(this);
          hashMap.put(str, weakReference);
        } 
        if (DOWNLOAD_TASK.size() > 3) {
          Iterator<Map.Entry> iterator = DOWNLOAD_TASK.entrySet().iterator();
          while (iterator.hasNext()) {
            WeakReference weakReference = (WeakReference)((Map.Entry)iterator.next()).getValue();
            if (weakReference == null || weakReference.get() == null)
              iterator.remove(); 
          } 
        } 
      } 
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_1} */
    } 
  }
  
  private void clearRawResult() {
    if (this.rawResult instanceof Closeable)
      IOUtil.closeQuietly((Closeable)this.rawResult); 
    this.rawResult = null;
  }
  
  private void closeRequestSync() {
    clearRawResult();
    IOUtil.closeQuietly((Closeable)this.request);
  }
  
  private UriRequest createNewRequest() throws Throwable {
    this.params.init();
    UriRequest uriRequest = UriRequestFactory.getUriRequest(this.params, this.loadType);
    uriRequest.setCallingClassLoader(this.callback.getClass().getClassLoader());
    uriRequest.setProgressHandler(this);
    this.loadingUpdateMaxTimeSpan = this.params.getLoadingUpdateMaxTimeSpan();
    update(1, new Object[] { uriRequest });
    return uriRequest;
  }
  
  private void resolveLoadType() {
    Class<?> clazz = this.callback.getClass();
    if (this.callback instanceof Callback.TypedCallback) {
      this.loadType = ((Callback.TypedCallback)this.callback).getLoadType();
      return;
    } 
    if (this.callback instanceof Callback.PrepareCallback) {
      this.loadType = ParameterizedTypeUtil.getParameterizedType(clazz, Callback.PrepareCallback.class, 0);
      return;
    } 
    this.loadType = ParameterizedTypeUtil.getParameterizedType(clazz, Callback.CommonCallback.class, 0);
  }
  
  protected void cancelWorks() {
    x.task().run(new Runnable() {
          public void run() {
            HttpTask.this.closeRequestSync();
          }
        });
  }
  
  protected ResultType doBackground() throws Throwable {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isCancelled : ()Z
    //   4: ifeq -> 18
    //   7: new org/xutils/common/Callback$CancelledException
    //   10: dup
    //   11: ldc_w 'cancelled before request'
    //   14: invokespecial <init> : (Ljava/lang/String;)V
    //   17: athrow
    //   18: aload_0
    //   19: invokespecial resolveLoadType : ()V
    //   22: aload_0
    //   23: aload_0
    //   24: invokespecial createNewRequest : ()Lorg/xutils/http/request/UriRequest;
    //   27: putfield request : Lorg/xutils/http/request/UriRequest;
    //   30: aload_0
    //   31: invokespecial checkDownloadTask : ()V
    //   34: iconst_0
    //   35: istore_1
    //   36: aconst_null
    //   37: astore_2
    //   38: aload_0
    //   39: getfield params : Lorg/xutils/http/RequestParams;
    //   42: invokevirtual getHttpRetryHandler : ()Lorg/xutils/http/app/HttpRetryHandler;
    //   45: astore_3
    //   46: aload_3
    //   47: astore #4
    //   49: aload_3
    //   50: ifnonnull -> 62
    //   53: new org/xutils/http/app/HttpRetryHandler
    //   56: dup
    //   57: invokespecial <init> : ()V
    //   60: astore #4
    //   62: aload #4
    //   64: aload_0
    //   65: getfield params : Lorg/xutils/http/RequestParams;
    //   68: invokevirtual getMaxRetryCount : ()I
    //   71: invokevirtual setMaxRetryCount : (I)V
    //   74: aload_0
    //   75: invokevirtual isCancelled : ()Z
    //   78: ifeq -> 92
    //   81: new org/xutils/common/Callback$CancelledException
    //   84: dup
    //   85: ldc_w 'cancelled before request'
    //   88: invokespecial <init> : (Ljava/lang/String;)V
    //   91: athrow
    //   92: aconst_null
    //   93: astore_3
    //   94: aload_3
    //   95: astore #5
    //   97: aload_0
    //   98: getfield cacheCallback : Lorg/xutils/common/Callback$CacheCallback;
    //   101: ifnull -> 383
    //   104: aload_3
    //   105: astore #5
    //   107: aload_0
    //   108: getfield params : Lorg/xutils/http/RequestParams;
    //   111: invokevirtual getMethod : ()Lorg/xutils/http/HttpMethod;
    //   114: invokestatic permitsCache : (Lorg/xutils/http/HttpMethod;)Z
    //   117: ifeq -> 383
    //   120: aload_0
    //   121: invokespecial clearRawResult : ()V
    //   124: new java/lang/StringBuilder
    //   127: astore #5
    //   129: aload #5
    //   131: invokespecial <init> : ()V
    //   134: aload #5
    //   136: ldc_w 'load cache: '
    //   139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: aload_0
    //   143: getfield request : Lorg/xutils/http/request/UriRequest;
    //   146: invokevirtual getRequestUri : ()Ljava/lang/String;
    //   149: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: invokevirtual toString : ()Ljava/lang/String;
    //   155: invokestatic d : (Ljava/lang/String;)V
    //   158: aload_0
    //   159: aload_0
    //   160: getfield request : Lorg/xutils/http/request/UriRequest;
    //   163: invokevirtual loadResultFromCache : ()Ljava/lang/Object;
    //   166: putfield rawResult : Ljava/lang/Object;
    //   169: aload_0
    //   170: invokevirtual isCancelled : ()Z
    //   173: ifeq -> 204
    //   176: aload_0
    //   177: invokespecial clearRawResult : ()V
    //   180: new org/xutils/common/Callback$CancelledException
    //   183: dup
    //   184: ldc_w 'cancelled before request'
    //   187: invokespecial <init> : (Ljava/lang/String;)V
    //   190: athrow
    //   191: astore #5
    //   193: ldc_w 'load disk cache error'
    //   196: aload #5
    //   198: invokestatic w : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   201: goto -> 169
    //   204: aload_3
    //   205: astore #5
    //   207: aload_0
    //   208: getfield rawResult : Ljava/lang/Object;
    //   211: ifnull -> 383
    //   214: aload_0
    //   215: getfield prepareCallback : Lorg/xutils/common/Callback$PrepareCallback;
    //   218: ifnull -> 283
    //   221: aload_0
    //   222: getfield prepareCallback : Lorg/xutils/common/Callback$PrepareCallback;
    //   225: aload_0
    //   226: getfield rawResult : Ljava/lang/Object;
    //   229: invokeinterface prepare : (Ljava/lang/Object;)Ljava/lang/Object;
    //   234: astore_3
    //   235: aload_0
    //   236: invokespecial clearRawResult : ()V
    //   239: aload_0
    //   240: invokevirtual isCancelled : ()Z
    //   243: ifeq -> 291
    //   246: new org/xutils/common/Callback$CancelledException
    //   249: dup
    //   250: ldc_w 'cancelled before request'
    //   253: invokespecial <init> : (Ljava/lang/String;)V
    //   256: athrow
    //   257: astore #5
    //   259: aconst_null
    //   260: astore_3
    //   261: ldc_w 'prepare disk cache error'
    //   264: aload #5
    //   266: invokestatic w : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   269: aload_0
    //   270: invokespecial clearRawResult : ()V
    //   273: goto -> 239
    //   276: astore_3
    //   277: aload_0
    //   278: invokespecial clearRawResult : ()V
    //   281: aload_3
    //   282: athrow
    //   283: aload_0
    //   284: getfield rawResult : Ljava/lang/Object;
    //   287: astore_3
    //   288: goto -> 239
    //   291: aload_3
    //   292: astore #5
    //   294: aload_3
    //   295: ifnull -> 383
    //   298: aload_0
    //   299: iconst_2
    //   300: iconst_1
    //   301: anewarray java/lang/Object
    //   304: dup
    //   305: iconst_0
    //   306: aload_3
    //   307: aastore
    //   308: invokevirtual update : (I[Ljava/lang/Object;)V
    //   311: aload_0
    //   312: getfield cacheLock : Ljava/lang/Object;
    //   315: astore #5
    //   317: aload #5
    //   319: monitorenter
    //   320: aload_0
    //   321: getfield trustCache : Ljava/lang/Boolean;
    //   324: astore #6
    //   326: aload #6
    //   328: ifnonnull -> 361
    //   331: aload_0
    //   332: getfield cacheLock : Ljava/lang/Object;
    //   335: invokevirtual wait : ()V
    //   338: goto -> 320
    //   341: astore_3
    //   342: new org/xutils/common/Callback$CancelledException
    //   345: astore_3
    //   346: aload_3
    //   347: ldc_w 'cancelled before request'
    //   350: invokespecial <init> : (Ljava/lang/String;)V
    //   353: aload_3
    //   354: athrow
    //   355: astore_3
    //   356: aload #5
    //   358: monitorexit
    //   359: aload_3
    //   360: athrow
    //   361: aload #5
    //   363: monitorexit
    //   364: aload_3
    //   365: astore #5
    //   367: aload_0
    //   368: getfield trustCache : Ljava/lang/Boolean;
    //   371: invokevirtual booleanValue : ()Z
    //   374: ifeq -> 383
    //   377: aconst_null
    //   378: astore #5
    //   380: aload #5
    //   382: areturn
    //   383: aload_0
    //   384: getfield trustCache : Ljava/lang/Boolean;
    //   387: ifnonnull -> 398
    //   390: aload_0
    //   391: iconst_0
    //   392: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   395: putfield trustCache : Ljava/lang/Boolean;
    //   398: aload #5
    //   400: ifnonnull -> 410
    //   403: aload_0
    //   404: getfield request : Lorg/xutils/http/request/UriRequest;
    //   407: invokevirtual clearCacheHeader : ()V
    //   410: aload_0
    //   411: getfield callback : Lorg/xutils/common/Callback$CommonCallback;
    //   414: instanceof org/xutils/common/Callback$ProxyCacheCallback
    //   417: ifeq -> 441
    //   420: aload_0
    //   421: getfield callback : Lorg/xutils/common/Callback$CommonCallback;
    //   424: checkcast org/xutils/common/Callback$ProxyCacheCallback
    //   427: invokeinterface onlyCache : ()Z
    //   432: ifeq -> 441
    //   435: aconst_null
    //   436: astore #5
    //   438: goto -> 380
    //   441: iconst_1
    //   442: istore #7
    //   444: aconst_null
    //   445: astore_3
    //   446: iload #7
    //   448: ifeq -> 944
    //   451: iconst_0
    //   452: istore #7
    //   454: aload_0
    //   455: invokevirtual isCancelled : ()Z
    //   458: ifeq -> 514
    //   461: new org/xutils/common/Callback$CancelledException
    //   464: astore #5
    //   466: aload #5
    //   468: ldc_w 'cancelled before request'
    //   471: invokespecial <init> : (Ljava/lang/String;)V
    //   474: aload #5
    //   476: athrow
    //   477: astore #5
    //   479: iconst_1
    //   480: istore #7
    //   482: new java/lang/StringBuilder
    //   485: dup
    //   486: invokespecial <init> : ()V
    //   489: ldc_w 'Http Redirect:'
    //   492: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   495: aload_0
    //   496: getfield params : Lorg/xutils/http/RequestParams;
    //   499: invokevirtual getUri : ()Ljava/lang/String;
    //   502: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   505: invokevirtual toString : ()Ljava/lang/String;
    //   508: invokestatic w : (Ljava/lang/String;)V
    //   511: goto -> 446
    //   514: aload_0
    //   515: getfield request : Lorg/xutils/http/request/UriRequest;
    //   518: invokevirtual close : ()V
    //   521: aload_0
    //   522: invokespecial clearRawResult : ()V
    //   525: new java/lang/StringBuilder
    //   528: astore #5
    //   530: aload #5
    //   532: invokespecial <init> : ()V
    //   535: aload #5
    //   537: ldc_w 'load: '
    //   540: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   543: aload_0
    //   544: getfield request : Lorg/xutils/http/request/UriRequest;
    //   547: invokevirtual getRequestUri : ()Ljava/lang/String;
    //   550: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   553: invokevirtual toString : ()Ljava/lang/String;
    //   556: invokestatic d : (Ljava/lang/String;)V
    //   559: new org/xutils/http/HttpTask$RequestWorker
    //   562: astore #5
    //   564: aload #5
    //   566: aload_0
    //   567: aconst_null
    //   568: invokespecial <init> : (Lorg/xutils/http/HttpTask;Lorg/xutils/http/HttpTask$1;)V
    //   571: aload_0
    //   572: aload #5
    //   574: putfield requestWorker : Lorg/xutils/http/HttpTask$RequestWorker;
    //   577: aload_0
    //   578: getfield requestWorker : Lorg/xutils/http/HttpTask$RequestWorker;
    //   581: invokevirtual request : ()V
    //   584: aload_0
    //   585: getfield requestWorker : Lorg/xutils/http/HttpTask$RequestWorker;
    //   588: getfield ex : Ljava/lang/Throwable;
    //   591: ifnull -> 743
    //   594: aload_0
    //   595: getfield requestWorker : Lorg/xutils/http/HttpTask$RequestWorker;
    //   598: getfield ex : Ljava/lang/Throwable;
    //   601: athrow
    //   602: astore #5
    //   604: aload_0
    //   605: invokespecial clearRawResult : ()V
    //   608: aload_0
    //   609: invokevirtual isCancelled : ()Z
    //   612: ifeq -> 784
    //   615: new org/xutils/common/Callback$CancelledException
    //   618: astore #5
    //   620: aload #5
    //   622: ldc_w 'cancelled during request'
    //   625: invokespecial <init> : (Ljava/lang/String;)V
    //   628: aload #5
    //   630: athrow
    //   631: astore #6
    //   633: aload_3
    //   634: astore #5
    //   636: aload #6
    //   638: astore_3
    //   639: aload_0
    //   640: getfield request : Lorg/xutils/http/request/UriRequest;
    //   643: invokevirtual getResponseCode : ()I
    //   646: lookupswitch default -> 680, 204 -> 938, 205 -> 938, 304 -> 938
    //   680: aload_3
    //   681: astore #6
    //   683: aload #6
    //   685: astore_3
    //   686: aload_0
    //   687: invokevirtual isCancelled : ()Z
    //   690: ifeq -> 715
    //   693: aload #6
    //   695: astore_3
    //   696: aload #6
    //   698: instanceof org/xutils/common/Callback$CancelledException
    //   701: ifne -> 715
    //   704: new org/xutils/common/Callback$CancelledException
    //   707: dup
    //   708: ldc_w 'canceled by user'
    //   711: invokespecial <init> : (Ljava/lang/String;)V
    //   714: astore_3
    //   715: aload_0
    //   716: getfield request : Lorg/xutils/http/request/UriRequest;
    //   719: astore #6
    //   721: iinc #1, 1
    //   724: aload #4
    //   726: aload #6
    //   728: aload_3
    //   729: iload_1
    //   730: invokevirtual canRetry : (Lorg/xutils/http/request/UriRequest;Ljava/lang/Throwable;I)Z
    //   733: istore #7
    //   735: aload_3
    //   736: astore_2
    //   737: aload #5
    //   739: astore_3
    //   740: goto -> 446
    //   743: aload_0
    //   744: aload_0
    //   745: getfield requestWorker : Lorg/xutils/http/HttpTask$RequestWorker;
    //   748: getfield result : Ljava/lang/Object;
    //   751: putfield rawResult : Ljava/lang/Object;
    //   754: aload_0
    //   755: getfield prepareCallback : Lorg/xutils/common/Callback$PrepareCallback;
    //   758: ifnull -> 923
    //   761: aload_0
    //   762: invokevirtual isCancelled : ()Z
    //   765: ifeq -> 787
    //   768: new org/xutils/common/Callback$CancelledException
    //   771: astore #5
    //   773: aload #5
    //   775: ldc_w 'cancelled before request'
    //   778: invokespecial <init> : (Ljava/lang/String;)V
    //   781: aload #5
    //   783: athrow
    //   784: aload #5
    //   786: athrow
    //   787: aload_0
    //   788: getfield prepareCallback : Lorg/xutils/common/Callback$PrepareCallback;
    //   791: aload_0
    //   792: getfield rawResult : Ljava/lang/Object;
    //   795: invokeinterface prepare : (Ljava/lang/Object;)Ljava/lang/Object;
    //   800: astore #5
    //   802: aload #5
    //   804: astore_3
    //   805: aload_3
    //   806: astore #6
    //   808: aload_3
    //   809: astore #5
    //   811: aload_0
    //   812: invokespecial clearRawResult : ()V
    //   815: aload_3
    //   816: astore #6
    //   818: aload_3
    //   819: astore #5
    //   821: aload_0
    //   822: getfield cacheCallback : Lorg/xutils/common/Callback$CacheCallback;
    //   825: ifnull -> 860
    //   828: aload_3
    //   829: astore #6
    //   831: aload_3
    //   832: astore #5
    //   834: aload_0
    //   835: getfield params : Lorg/xutils/http/RequestParams;
    //   838: invokevirtual getMethod : ()Lorg/xutils/http/HttpMethod;
    //   841: invokestatic permitsCache : (Lorg/xutils/http/HttpMethod;)Z
    //   844: ifeq -> 860
    //   847: aload_3
    //   848: astore #6
    //   850: aload_3
    //   851: astore #5
    //   853: aload_0
    //   854: getfield request : Lorg/xutils/http/request/UriRequest;
    //   857: invokevirtual save2Cache : ()V
    //   860: aload_3
    //   861: astore #6
    //   863: aload_3
    //   864: astore #5
    //   866: aload_0
    //   867: invokevirtual isCancelled : ()Z
    //   870: ifeq -> 935
    //   873: aload_3
    //   874: astore #6
    //   876: aload_3
    //   877: astore #5
    //   879: new org/xutils/common/Callback$CancelledException
    //   882: astore #8
    //   884: aload_3
    //   885: astore #6
    //   887: aload_3
    //   888: astore #5
    //   890: aload #8
    //   892: ldc_w 'cancelled after request'
    //   895: invokespecial <init> : (Ljava/lang/String;)V
    //   898: aload_3
    //   899: astore #6
    //   901: aload_3
    //   902: astore #5
    //   904: aload #8
    //   906: athrow
    //   907: astore_3
    //   908: aload #6
    //   910: astore_3
    //   911: goto -> 479
    //   914: astore #5
    //   916: aload_0
    //   917: invokespecial clearRawResult : ()V
    //   920: aload #5
    //   922: athrow
    //   923: aload_0
    //   924: getfield rawResult : Ljava/lang/Object;
    //   927: astore #5
    //   929: aload #5
    //   931: astore_3
    //   932: goto -> 815
    //   935: goto -> 446
    //   938: aconst_null
    //   939: astore #5
    //   941: goto -> 380
    //   944: aload_3
    //   945: astore #5
    //   947: aload_2
    //   948: ifnull -> 380
    //   951: aload_3
    //   952: astore #5
    //   954: aload_3
    //   955: ifnonnull -> 380
    //   958: aload_3
    //   959: astore #5
    //   961: aload_0
    //   962: getfield trustCache : Ljava/lang/Boolean;
    //   965: invokevirtual booleanValue : ()Z
    //   968: ifne -> 380
    //   971: aload_0
    //   972: iconst_1
    //   973: putfield hasException : Z
    //   976: aload_2
    //   977: athrow
    //   978: astore_3
    //   979: goto -> 639
    //   982: astore #6
    //   984: goto -> 320
    // Exception table:
    //   from	to	target	type
    //   120	169	191	java/lang/Throwable
    //   221	235	257	java/lang/Throwable
    //   221	235	276	finally
    //   261	269	276	finally
    //   320	326	355	finally
    //   331	338	341	java/lang/InterruptedException
    //   331	338	982	java/lang/Throwable
    //   331	338	355	finally
    //   342	355	355	finally
    //   356	359	355	finally
    //   361	364	355	finally
    //   454	477	477	org/xutils/ex/HttpRedirectException
    //   454	477	631	java/lang/Throwable
    //   514	521	477	org/xutils/ex/HttpRedirectException
    //   514	521	631	java/lang/Throwable
    //   521	602	602	java/lang/Throwable
    //   521	602	477	org/xutils/ex/HttpRedirectException
    //   604	631	477	org/xutils/ex/HttpRedirectException
    //   604	631	631	java/lang/Throwable
    //   743	754	602	java/lang/Throwable
    //   743	754	477	org/xutils/ex/HttpRedirectException
    //   754	784	477	org/xutils/ex/HttpRedirectException
    //   754	784	631	java/lang/Throwable
    //   784	787	477	org/xutils/ex/HttpRedirectException
    //   784	787	631	java/lang/Throwable
    //   787	802	914	finally
    //   811	815	907	org/xutils/ex/HttpRedirectException
    //   811	815	978	java/lang/Throwable
    //   821	828	907	org/xutils/ex/HttpRedirectException
    //   821	828	978	java/lang/Throwable
    //   834	847	907	org/xutils/ex/HttpRedirectException
    //   834	847	978	java/lang/Throwable
    //   853	860	907	org/xutils/ex/HttpRedirectException
    //   853	860	978	java/lang/Throwable
    //   866	873	907	org/xutils/ex/HttpRedirectException
    //   866	873	978	java/lang/Throwable
    //   879	884	907	org/xutils/ex/HttpRedirectException
    //   879	884	978	java/lang/Throwable
    //   890	898	907	org/xutils/ex/HttpRedirectException
    //   890	898	978	java/lang/Throwable
    //   904	907	907	org/xutils/ex/HttpRedirectException
    //   904	907	978	java/lang/Throwable
    //   916	923	477	org/xutils/ex/HttpRedirectException
    //   916	923	631	java/lang/Throwable
    //   923	929	477	org/xutils/ex/HttpRedirectException
    //   923	929	631	java/lang/Throwable
  }
  
  public Executor getExecutor() {
    return this.executor;
  }
  
  public Priority getPriority() {
    return this.params.getPriority();
  }
  
  protected boolean isCancelFast() {
    return this.params.isCancelFast();
  }
  
  protected void onCancelled(Callback.CancelledException paramCancelledException) {
    if (this.tracker != null)
      this.tracker.onCancelled(this.request); 
    this.callback.onCancelled(paramCancelledException);
  }
  
  protected void onError(Throwable paramThrowable, boolean paramBoolean) {
    if (this.tracker != null)
      this.tracker.onError(this.request, paramThrowable, paramBoolean); 
    this.callback.onError(paramThrowable, paramBoolean);
  }
  
  protected void onFinished() {
    if (this.tracker != null)
      this.tracker.onFinished(this.request); 
    x.task().run(new Runnable() {
          public void run() {
            HttpTask.this.closeRequestSync();
          }
        });
    this.callback.onFinished();
  }
  
  protected void onStarted() {
    if (this.tracker != null)
      this.tracker.onStart(this.params); 
    if (this.progressCallback != null)
      this.progressCallback.onStarted(); 
  }
  
  protected void onSuccess(ResultType paramResultType) {
    if (!this.hasException) {
      if (this.tracker != null)
        this.tracker.onSuccess(this.request, paramResultType); 
      this.callback.onSuccess(paramResultType);
    } 
  }
  
  protected void onUpdate(int paramInt, Object... paramVarArgs) {
    // Byte code:
    //   0: iload_1
    //   1: tableswitch default -> 28, 1 -> 29, 2 -> 54, 3 -> 160
    //   28: return
    //   29: aload_0
    //   30: getfield tracker : Lorg/xutils/http/app/RequestTracker;
    //   33: ifnull -> 28
    //   36: aload_0
    //   37: getfield tracker : Lorg/xutils/http/app/RequestTracker;
    //   40: aload_2
    //   41: iconst_0
    //   42: aaload
    //   43: checkcast org/xutils/http/request/UriRequest
    //   46: invokeinterface onRequestCreated : (Lorg/xutils/http/request/UriRequest;)V
    //   51: goto -> 28
    //   54: aload_0
    //   55: getfield cacheLock : Ljava/lang/Object;
    //   58: astore_3
    //   59: aload_3
    //   60: monitorenter
    //   61: aload_2
    //   62: iconst_0
    //   63: aaload
    //   64: astore_2
    //   65: aload_0
    //   66: getfield tracker : Lorg/xutils/http/app/RequestTracker;
    //   69: ifnull -> 86
    //   72: aload_0
    //   73: getfield tracker : Lorg/xutils/http/app/RequestTracker;
    //   76: aload_0
    //   77: getfield request : Lorg/xutils/http/request/UriRequest;
    //   80: aload_2
    //   81: invokeinterface onCache : (Lorg/xutils/http/request/UriRequest;Ljava/lang/Object;)V
    //   86: aload_0
    //   87: aload_0
    //   88: getfield cacheCallback : Lorg/xutils/common/Callback$CacheCallback;
    //   91: aload_2
    //   92: invokeinterface onCache : (Ljava/lang/Object;)Z
    //   97: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   100: putfield trustCache : Ljava/lang/Boolean;
    //   103: aload_0
    //   104: getfield cacheLock : Ljava/lang/Object;
    //   107: invokevirtual notifyAll : ()V
    //   110: aload_3
    //   111: monitorexit
    //   112: goto -> 28
    //   115: astore_2
    //   116: aload_3
    //   117: monitorexit
    //   118: aload_2
    //   119: athrow
    //   120: astore_2
    //   121: aload_0
    //   122: iconst_0
    //   123: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   126: putfield trustCache : Ljava/lang/Boolean;
    //   129: aload_0
    //   130: getfield callback : Lorg/xutils/common/Callback$CommonCallback;
    //   133: aload_2
    //   134: iconst_1
    //   135: invokeinterface onError : (Ljava/lang/Throwable;Z)V
    //   140: aload_0
    //   141: getfield cacheLock : Ljava/lang/Object;
    //   144: invokevirtual notifyAll : ()V
    //   147: goto -> 110
    //   150: astore_2
    //   151: aload_0
    //   152: getfield cacheLock : Ljava/lang/Object;
    //   155: invokevirtual notifyAll : ()V
    //   158: aload_2
    //   159: athrow
    //   160: aload_0
    //   161: getfield progressCallback : Lorg/xutils/common/Callback$ProgressCallback;
    //   164: ifnull -> 28
    //   167: aload_2
    //   168: arraylength
    //   169: iconst_3
    //   170: if_icmpne -> 28
    //   173: aload_0
    //   174: getfield progressCallback : Lorg/xutils/common/Callback$ProgressCallback;
    //   177: aload_2
    //   178: iconst_0
    //   179: aaload
    //   180: checkcast java/lang/Number
    //   183: invokevirtual longValue : ()J
    //   186: aload_2
    //   187: iconst_1
    //   188: aaload
    //   189: checkcast java/lang/Number
    //   192: invokevirtual longValue : ()J
    //   195: aload_2
    //   196: iconst_2
    //   197: aaload
    //   198: checkcast java/lang/Boolean
    //   201: invokevirtual booleanValue : ()Z
    //   204: invokeinterface onLoading : (JJZ)V
    //   209: goto -> 28
    //   212: astore_2
    //   213: aload_0
    //   214: getfield callback : Lorg/xutils/common/Callback$CommonCallback;
    //   217: aload_2
    //   218: iconst_1
    //   219: invokeinterface onError : (Ljava/lang/Throwable;Z)V
    //   224: goto -> 28
    // Exception table:
    //   from	to	target	type
    //   65	86	120	java/lang/Throwable
    //   65	86	150	finally
    //   86	103	120	java/lang/Throwable
    //   86	103	150	finally
    //   103	110	115	finally
    //   110	112	115	finally
    //   116	118	115	finally
    //   121	140	150	finally
    //   140	147	115	finally
    //   151	160	115	finally
    //   173	209	212	java/lang/Throwable
  }
  
  protected void onWaiting() {
    if (this.tracker != null)
      this.tracker.onWaiting(this.params); 
    if (this.progressCallback != null)
      this.progressCallback.onWaiting(); 
  }
  
  public String toString() {
    return this.params.toString();
  }
  
  public boolean updateProgress(long paramLong1, long paramLong2, boolean paramBoolean) {
    boolean bool = true;
    if (isCancelled() || isFinished())
      return false; 
    if (this.progressCallback != null && this.request != null && paramLong1 > 0L) {
      long l = paramLong1;
      if (paramLong1 < paramLong2)
        l = paramLong2; 
      if (paramBoolean) {
        this.lastUpdateTime = System.currentTimeMillis();
        update(3, new Object[] { Long.valueOf(l), Long.valueOf(paramLong2), Boolean.valueOf(this.request.isLoading()) });
      } else {
        paramLong1 = System.currentTimeMillis();
        if (paramLong1 - this.lastUpdateTime >= this.loadingUpdateMaxTimeSpan) {
          this.lastUpdateTime = paramLong1;
          update(3, new Object[] { Long.valueOf(l), Long.valueOf(paramLong2), Boolean.valueOf(this.request.isLoading()) });
        } 
      } 
    } 
    if (!isCancelled()) {
      paramBoolean = bool;
      return isFinished() ? false : paramBoolean;
    } 
    return false;
  }
  
  static {
    boolean bool;
    if (!HttpTask.class.desiredAssertionStatus()) {
      bool = true;
    } else {
      bool = false;
    } 
    $assertionsDisabled = bool;
  }
  
  private final class RequestWorker {
    Throwable ex;
    
    Object result;
    
    private RequestWorker() {}
    
    public void request() {
      // Byte code:
      //   0: iconst_0
      //   1: istore_1
      //   2: iconst_0
      //   3: istore_2
      //   4: ldc java/io/File
      //   6: aload_0
      //   7: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   10: invokestatic access$200 : (Lorg/xutils/http/HttpTask;)Ljava/lang/reflect/Type;
      //   13: if_acmpne -> 75
      //   16: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   19: astore_3
      //   20: aload_3
      //   21: monitorenter
      //   22: iload_2
      //   23: istore_1
      //   24: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   27: invokevirtual get : ()I
      //   30: iconst_3
      //   31: if_icmplt -> 66
      //   34: aload_0
      //   35: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   38: invokevirtual isCancelled : ()Z
      //   41: istore #4
      //   43: iload_2
      //   44: istore_1
      //   45: iload #4
      //   47: ifne -> 66
      //   50: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   53: ldc2_w 10
      //   56: invokevirtual wait : (J)V
      //   59: goto -> 22
      //   62: astore #5
      //   64: iconst_1
      //   65: istore_1
      //   66: aload_3
      //   67: monitorexit
      //   68: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   71: invokevirtual incrementAndGet : ()I
      //   74: pop
      //   75: iload_1
      //   76: ifne -> 89
      //   79: aload_0
      //   80: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   83: invokevirtual isCancelled : ()Z
      //   86: ifeq -> 374
      //   89: new org/xutils/common/Callback$CancelledException
      //   92: astore #5
      //   94: new java/lang/StringBuilder
      //   97: astore_3
      //   98: aload_3
      //   99: invokespecial <init> : ()V
      //   102: aload_3
      //   103: ldc 'cancelled before request'
      //   105: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   108: astore #6
      //   110: iload_1
      //   111: ifeq -> 368
      //   114: ldc '(interrupted)'
      //   116: astore_3
      //   117: aload #5
      //   119: aload #6
      //   121: aload_3
      //   122: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   125: invokevirtual toString : ()Ljava/lang/String;
      //   128: invokespecial <init> : (Ljava/lang/String;)V
      //   131: aload #5
      //   133: athrow
      //   134: astore_3
      //   135: aload_0
      //   136: aload_3
      //   137: putfield ex : Ljava/lang/Throwable;
      //   140: aload_3
      //   141: instanceof org/xutils/ex/HttpException
      //   144: ifeq -> 286
      //   147: aload_3
      //   148: checkcast org/xutils/ex/HttpException
      //   151: astore #5
      //   153: aload #5
      //   155: invokevirtual getCode : ()I
      //   158: istore_1
      //   159: iload_1
      //   160: sipush #301
      //   163: if_icmpeq -> 173
      //   166: iload_1
      //   167: sipush #302
      //   170: if_icmpne -> 286
      //   173: aload_0
      //   174: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   177: invokestatic access$600 : (Lorg/xutils/http/HttpTask;)Lorg/xutils/http/RequestParams;
      //   180: invokevirtual getRedirectHandler : ()Lorg/xutils/http/app/RedirectHandler;
      //   183: astore #6
      //   185: aload #6
      //   187: ifnull -> 286
      //   190: aload #6
      //   192: aload_0
      //   193: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   196: invokestatic access$500 : (Lorg/xutils/http/HttpTask;)Lorg/xutils/http/request/UriRequest;
      //   199: invokeinterface getRedirectParams : (Lorg/xutils/http/request/UriRequest;)Lorg/xutils/http/RequestParams;
      //   204: astore #6
      //   206: aload #6
      //   208: ifnull -> 286
      //   211: aload #6
      //   213: invokevirtual getMethod : ()Lorg/xutils/http/HttpMethod;
      //   216: ifnonnull -> 234
      //   219: aload #6
      //   221: aload_0
      //   222: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   225: invokestatic access$600 : (Lorg/xutils/http/HttpTask;)Lorg/xutils/http/RequestParams;
      //   228: invokevirtual getMethod : ()Lorg/xutils/http/HttpMethod;
      //   231: invokevirtual setMethod : (Lorg/xutils/http/HttpMethod;)V
      //   234: aload_0
      //   235: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   238: aload #6
      //   240: invokestatic access$602 : (Lorg/xutils/http/HttpTask;Lorg/xutils/http/RequestParams;)Lorg/xutils/http/RequestParams;
      //   243: pop
      //   244: aload_0
      //   245: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   248: aload_0
      //   249: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   252: invokestatic access$700 : (Lorg/xutils/http/HttpTask;)Lorg/xutils/http/request/UriRequest;
      //   255: invokestatic access$502 : (Lorg/xutils/http/HttpTask;Lorg/xutils/http/request/UriRequest;)Lorg/xutils/http/request/UriRequest;
      //   258: pop
      //   259: new org/xutils/ex/HttpRedirectException
      //   262: astore #6
      //   264: aload #6
      //   266: iload_1
      //   267: aload #5
      //   269: invokevirtual getMessage : ()Ljava/lang/String;
      //   272: aload #5
      //   274: invokevirtual getResult : ()Ljava/lang/String;
      //   277: invokespecial <init> : (ILjava/lang/String;Ljava/lang/String;)V
      //   280: aload_0
      //   281: aload #6
      //   283: putfield ex : Ljava/lang/Throwable;
      //   286: ldc java/io/File
      //   288: aload_0
      //   289: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   292: invokestatic access$200 : (Lorg/xutils/http/HttpTask;)Ljava/lang/reflect/Type;
      //   295: if_acmpne -> 322
      //   298: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   301: astore #5
      //   303: aload #5
      //   305: monitorenter
      //   306: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   309: invokevirtual decrementAndGet : ()I
      //   312: pop
      //   313: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   316: invokevirtual notifyAll : ()V
      //   319: aload #5
      //   321: monitorexit
      //   322: return
      //   323: astore #5
      //   325: aload_3
      //   326: monitorexit
      //   327: aload #5
      //   329: athrow
      //   330: astore #5
      //   332: ldc java/io/File
      //   334: aload_0
      //   335: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   338: invokestatic access$200 : (Lorg/xutils/http/HttpTask;)Ljava/lang/reflect/Type;
      //   341: if_acmpne -> 365
      //   344: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   347: astore_3
      //   348: aload_3
      //   349: monitorenter
      //   350: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   353: invokevirtual decrementAndGet : ()I
      //   356: pop
      //   357: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   360: invokevirtual notifyAll : ()V
      //   363: aload_3
      //   364: monitorexit
      //   365: aload #5
      //   367: athrow
      //   368: ldc ''
      //   370: astore_3
      //   371: goto -> 117
      //   374: aload_0
      //   375: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   378: invokestatic access$500 : (Lorg/xutils/http/HttpTask;)Lorg/xutils/http/request/UriRequest;
      //   381: aload_0
      //   382: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   385: invokestatic access$400 : (Lorg/xutils/http/HttpTask;)Lorg/xutils/http/app/RequestInterceptListener;
      //   388: invokevirtual setRequestInterceptListener : (Lorg/xutils/http/app/RequestInterceptListener;)V
      //   391: aload_0
      //   392: aload_0
      //   393: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   396: invokestatic access$500 : (Lorg/xutils/http/HttpTask;)Lorg/xutils/http/request/UriRequest;
      //   399: invokevirtual loadResult : ()Ljava/lang/Object;
      //   402: putfield result : Ljava/lang/Object;
      //   405: aload_0
      //   406: getfield ex : Ljava/lang/Throwable;
      //   409: ifnull -> 426
      //   412: aload_0
      //   413: getfield ex : Ljava/lang/Throwable;
      //   416: athrow
      //   417: astore_3
      //   418: aload_0
      //   419: aload_3
      //   420: putfield ex : Ljava/lang/Throwable;
      //   423: goto -> 405
      //   426: ldc java/io/File
      //   428: aload_0
      //   429: getfield this$0 : Lorg/xutils/http/HttpTask;
      //   432: invokestatic access$200 : (Lorg/xutils/http/HttpTask;)Ljava/lang/reflect/Type;
      //   435: if_acmpne -> 322
      //   438: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   441: astore_3
      //   442: aload_3
      //   443: monitorenter
      //   444: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   447: invokevirtual decrementAndGet : ()I
      //   450: pop
      //   451: invokestatic access$300 : ()Ljava/util/concurrent/atomic/AtomicInteger;
      //   454: invokevirtual notifyAll : ()V
      //   457: aload_3
      //   458: monitorexit
      //   459: goto -> 322
      //   462: astore #5
      //   464: aload_3
      //   465: monitorexit
      //   466: aload #5
      //   468: athrow
      //   469: astore #5
      //   471: aload_0
      //   472: aload_3
      //   473: putfield ex : Ljava/lang/Throwable;
      //   476: goto -> 286
      //   479: astore_3
      //   480: aload #5
      //   482: monitorexit
      //   483: aload_3
      //   484: athrow
      //   485: astore #5
      //   487: aload_3
      //   488: monitorexit
      //   489: aload #5
      //   491: athrow
      //   492: astore #5
      //   494: goto -> 22
      // Exception table:
      //   from	to	target	type
      //   4	22	134	java/lang/Throwable
      //   4	22	330	finally
      //   24	43	323	finally
      //   50	59	62	java/lang/InterruptedException
      //   50	59	492	java/lang/Throwable
      //   50	59	323	finally
      //   66	68	323	finally
      //   68	75	134	java/lang/Throwable
      //   68	75	330	finally
      //   79	89	134	java/lang/Throwable
      //   79	89	330	finally
      //   89	110	134	java/lang/Throwable
      //   89	110	330	finally
      //   117	134	134	java/lang/Throwable
      //   117	134	330	finally
      //   135	159	330	finally
      //   173	185	330	finally
      //   190	206	469	java/lang/Throwable
      //   190	206	330	finally
      //   211	234	469	java/lang/Throwable
      //   211	234	330	finally
      //   234	286	469	java/lang/Throwable
      //   234	286	330	finally
      //   306	322	479	finally
      //   325	327	323	finally
      //   327	330	134	java/lang/Throwable
      //   327	330	330	finally
      //   350	365	485	finally
      //   374	405	417	java/lang/Throwable
      //   374	405	330	finally
      //   405	417	134	java/lang/Throwable
      //   405	417	330	finally
      //   418	423	134	java/lang/Throwable
      //   418	423	330	finally
      //   444	459	462	finally
      //   464	466	462	finally
      //   471	476	330	finally
      //   480	483	479	finally
      //   487	489	485	finally
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/org/xutils/http/HttpTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */