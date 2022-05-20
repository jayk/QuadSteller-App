package android.support.v4.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public final class GestureDetectorCompat {
  private final GestureDetectorCompatImpl mImpl;
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener) {
    this(paramContext, paramOnGestureListener, null);
  }
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler) {
    if (Build.VERSION.SDK_INT > 17) {
      this.mImpl = new GestureDetectorCompatImplJellybeanMr2(paramContext, paramOnGestureListener, paramHandler);
      return;
    } 
    this.mImpl = new GestureDetectorCompatImplBase(paramContext, paramOnGestureListener, paramHandler);
  }
  
  public boolean isLongpressEnabled() {
    return this.mImpl.isLongpressEnabled();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    return this.mImpl.onTouchEvent(paramMotionEvent);
  }
  
  public void setIsLongpressEnabled(boolean paramBoolean) {
    this.mImpl.setIsLongpressEnabled(paramBoolean);
  }
  
  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener) {
    this.mImpl.setOnDoubleTapListener(paramOnDoubleTapListener);
  }
  
  static interface GestureDetectorCompatImpl {
    boolean isLongpressEnabled();
    
    boolean onTouchEvent(MotionEvent param1MotionEvent);
    
    void setIsLongpressEnabled(boolean param1Boolean);
    
    void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener);
  }
  
  static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl {
    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    
    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    
    private static final int LONG_PRESS = 2;
    
    private static final int SHOW_PRESS = 1;
    
    private static final int TAP = 3;
    
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    
    private boolean mAlwaysInBiggerTapRegion;
    
    private boolean mAlwaysInTapRegion;
    
    MotionEvent mCurrentDownEvent;
    
    boolean mDeferConfirmSingleTap;
    
    GestureDetector.OnDoubleTapListener mDoubleTapListener;
    
    private int mDoubleTapSlopSquare;
    
    private float mDownFocusX;
    
    private float mDownFocusY;
    
    private final Handler mHandler;
    
    private boolean mInLongPress;
    
    private boolean mIsDoubleTapping;
    
    private boolean mIsLongpressEnabled;
    
    private float mLastFocusX;
    
    private float mLastFocusY;
    
    final GestureDetector.OnGestureListener mListener;
    
    private int mMaximumFlingVelocity;
    
    private int mMinimumFlingVelocity;
    
    private MotionEvent mPreviousUpEvent;
    
    boolean mStillDown;
    
    private int mTouchSlopSquare;
    
    private VelocityTracker mVelocityTracker;
    
    static {
    
    }
    
    public GestureDetectorCompatImplBase(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) {
      if (param1Handler != null) {
        this.mHandler = new GestureHandler(param1Handler);
      } else {
        this.mHandler = new GestureHandler();
      } 
      this.mListener = param1OnGestureListener;
      if (param1OnGestureListener instanceof GestureDetector.OnDoubleTapListener)
        setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)param1OnGestureListener); 
      init(param1Context);
    }
    
    private void cancel() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
      this.mIsDoubleTapping = false;
      this.mStillDown = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void cancelTaps() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mIsDoubleTapping = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void init(Context param1Context) {
      if (param1Context == null)
        throw new IllegalArgumentException("Context must not be null"); 
      if (this.mListener == null)
        throw new IllegalArgumentException("OnGestureListener must not be null"); 
      this.mIsLongpressEnabled = true;
      ViewConfiguration viewConfiguration = ViewConfiguration.get(param1Context);
      int i = viewConfiguration.getScaledTouchSlop();
      int j = viewConfiguration.getScaledDoubleTapSlop();
      this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
      this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
      this.mTouchSlopSquare = i * i;
      this.mDoubleTapSlopSquare = j * j;
    }
    
    private boolean isConsideredDoubleTap(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, MotionEvent param1MotionEvent3) {
      boolean bool1 = false;
      if (!this.mAlwaysInBiggerTapRegion)
        return bool1; 
      boolean bool2 = bool1;
      if (param1MotionEvent3.getEventTime() - param1MotionEvent2.getEventTime() <= DOUBLE_TAP_TIMEOUT) {
        int i = (int)param1MotionEvent1.getX() - (int)param1MotionEvent3.getX();
        int j = (int)param1MotionEvent1.getY() - (int)param1MotionEvent3.getY();
        bool2 = bool1;
        if (i * i + j * j < this.mDoubleTapSlopSquare)
          bool2 = true; 
      } 
      return bool2;
    }
    
    void dispatchLongPress() {
      this.mHandler.removeMessages(3);
      this.mDeferConfirmSingleTap = false;
      this.mInLongPress = true;
      this.mListener.onLongPress(this.mCurrentDownEvent);
    }
    
    public boolean isLongpressEnabled() {
      return this.mIsLongpressEnabled;
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getAction : ()I
      //   4: istore_2
      //   5: aload_0
      //   6: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   9: ifnonnull -> 19
      //   12: aload_0
      //   13: invokestatic obtain : ()Landroid/view/VelocityTracker;
      //   16: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   19: aload_0
      //   20: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   23: aload_1
      //   24: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
      //   27: iload_2
      //   28: sipush #255
      //   31: iand
      //   32: bipush #6
      //   34: if_icmpne -> 100
      //   37: iconst_1
      //   38: istore_3
      //   39: iload_3
      //   40: ifeq -> 105
      //   43: aload_1
      //   44: invokestatic getActionIndex : (Landroid/view/MotionEvent;)I
      //   47: istore #4
      //   49: fconst_0
      //   50: fstore #5
      //   52: fconst_0
      //   53: fstore #6
      //   55: aload_1
      //   56: invokevirtual getPointerCount : ()I
      //   59: istore #7
      //   61: iconst_0
      //   62: istore #8
      //   64: iload #8
      //   66: iload #7
      //   68: if_icmpge -> 140
      //   71: iload #4
      //   73: iload #8
      //   75: if_icmpne -> 111
      //   78: fload #6
      //   80: fstore #9
      //   82: fload #5
      //   84: fstore #6
      //   86: iinc #8, 1
      //   89: fload #6
      //   91: fstore #5
      //   93: fload #9
      //   95: fstore #6
      //   97: goto -> 64
      //   100: iconst_0
      //   101: istore_3
      //   102: goto -> 39
      //   105: iconst_m1
      //   106: istore #4
      //   108: goto -> 49
      //   111: fload #5
      //   113: aload_1
      //   114: iload #8
      //   116: invokevirtual getX : (I)F
      //   119: fadd
      //   120: fstore #5
      //   122: fload #6
      //   124: aload_1
      //   125: iload #8
      //   127: invokevirtual getY : (I)F
      //   130: fadd
      //   131: fstore #9
      //   133: fload #5
      //   135: fstore #6
      //   137: goto -> 86
      //   140: iload_3
      //   141: ifeq -> 235
      //   144: iload #7
      //   146: iconst_1
      //   147: isub
      //   148: istore_3
      //   149: fload #5
      //   151: iload_3
      //   152: i2f
      //   153: fdiv
      //   154: fstore #5
      //   156: fload #6
      //   158: iload_3
      //   159: i2f
      //   160: fdiv
      //   161: fstore #9
      //   163: iconst_0
      //   164: istore #4
      //   166: iconst_0
      //   167: istore #10
      //   169: iconst_0
      //   170: istore #11
      //   172: iconst_0
      //   173: istore #12
      //   175: iload #12
      //   177: istore #13
      //   179: iload_2
      //   180: sipush #255
      //   183: iand
      //   184: tableswitch default -> 228, 0 -> 423, 1 -> 937, 2 -> 692, 3 -> 1219, 4 -> 232, 5 -> 241, 6 -> 276
      //   228: iload #12
      //   230: istore #13
      //   232: iload #13
      //   234: ireturn
      //   235: iload #7
      //   237: istore_3
      //   238: goto -> 149
      //   241: aload_0
      //   242: fload #5
      //   244: putfield mLastFocusX : F
      //   247: aload_0
      //   248: fload #5
      //   250: putfield mDownFocusX : F
      //   253: aload_0
      //   254: fload #9
      //   256: putfield mLastFocusY : F
      //   259: aload_0
      //   260: fload #9
      //   262: putfield mDownFocusY : F
      //   265: aload_0
      //   266: invokespecial cancelTaps : ()V
      //   269: iload #12
      //   271: istore #13
      //   273: goto -> 232
      //   276: aload_0
      //   277: fload #5
      //   279: putfield mLastFocusX : F
      //   282: aload_0
      //   283: fload #5
      //   285: putfield mDownFocusX : F
      //   288: aload_0
      //   289: fload #9
      //   291: putfield mLastFocusY : F
      //   294: aload_0
      //   295: fload #9
      //   297: putfield mDownFocusY : F
      //   300: aload_0
      //   301: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   304: sipush #1000
      //   307: aload_0
      //   308: getfield mMaximumFlingVelocity : I
      //   311: i2f
      //   312: invokevirtual computeCurrentVelocity : (IF)V
      //   315: aload_1
      //   316: invokestatic getActionIndex : (Landroid/view/MotionEvent;)I
      //   319: istore #4
      //   321: aload_1
      //   322: iload #4
      //   324: invokevirtual getPointerId : (I)I
      //   327: istore_3
      //   328: aload_0
      //   329: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   332: iload_3
      //   333: invokestatic getXVelocity : (Landroid/view/VelocityTracker;I)F
      //   336: fstore #6
      //   338: aload_0
      //   339: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   342: iload_3
      //   343: invokestatic getYVelocity : (Landroid/view/VelocityTracker;I)F
      //   346: fstore #5
      //   348: iconst_0
      //   349: istore_3
      //   350: iload #12
      //   352: istore #13
      //   354: iload_3
      //   355: iload #7
      //   357: if_icmpge -> 232
      //   360: iload_3
      //   361: iload #4
      //   363: if_icmpne -> 372
      //   366: iinc #3, 1
      //   369: goto -> 350
      //   372: aload_1
      //   373: iload_3
      //   374: invokevirtual getPointerId : (I)I
      //   377: istore #8
      //   379: fload #6
      //   381: aload_0
      //   382: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   385: iload #8
      //   387: invokestatic getXVelocity : (Landroid/view/VelocityTracker;I)F
      //   390: fmul
      //   391: fload #5
      //   393: aload_0
      //   394: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   397: iload #8
      //   399: invokestatic getYVelocity : (Landroid/view/VelocityTracker;I)F
      //   402: fmul
      //   403: fadd
      //   404: fconst_0
      //   405: fcmpg
      //   406: ifge -> 366
      //   409: aload_0
      //   410: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   413: invokevirtual clear : ()V
      //   416: iload #12
      //   418: istore #13
      //   420: goto -> 232
      //   423: iload #4
      //   425: istore_3
      //   426: aload_0
      //   427: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   430: ifnull -> 523
      //   433: aload_0
      //   434: getfield mHandler : Landroid/os/Handler;
      //   437: iconst_3
      //   438: invokevirtual hasMessages : (I)Z
      //   441: istore #13
      //   443: iload #13
      //   445: ifeq -> 456
      //   448: aload_0
      //   449: getfield mHandler : Landroid/os/Handler;
      //   452: iconst_3
      //   453: invokevirtual removeMessages : (I)V
      //   456: aload_0
      //   457: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   460: ifnull -> 673
      //   463: aload_0
      //   464: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   467: ifnull -> 673
      //   470: iload #13
      //   472: ifeq -> 673
      //   475: aload_0
      //   476: aload_0
      //   477: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   480: aload_0
      //   481: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   484: aload_1
      //   485: invokespecial isConsideredDoubleTap : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;Landroid/view/MotionEvent;)Z
      //   488: ifeq -> 673
      //   491: aload_0
      //   492: iconst_1
      //   493: putfield mIsDoubleTapping : Z
      //   496: iconst_0
      //   497: aload_0
      //   498: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   501: aload_0
      //   502: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   505: invokeinterface onDoubleTap : (Landroid/view/MotionEvent;)Z
      //   510: ior
      //   511: aload_0
      //   512: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   515: aload_1
      //   516: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   521: ior
      //   522: istore_3
      //   523: aload_0
      //   524: fload #5
      //   526: putfield mLastFocusX : F
      //   529: aload_0
      //   530: fload #5
      //   532: putfield mDownFocusX : F
      //   535: aload_0
      //   536: fload #9
      //   538: putfield mLastFocusY : F
      //   541: aload_0
      //   542: fload #9
      //   544: putfield mDownFocusY : F
      //   547: aload_0
      //   548: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   551: ifnull -> 561
      //   554: aload_0
      //   555: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   558: invokevirtual recycle : ()V
      //   561: aload_0
      //   562: aload_1
      //   563: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   566: putfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   569: aload_0
      //   570: iconst_1
      //   571: putfield mAlwaysInTapRegion : Z
      //   574: aload_0
      //   575: iconst_1
      //   576: putfield mAlwaysInBiggerTapRegion : Z
      //   579: aload_0
      //   580: iconst_1
      //   581: putfield mStillDown : Z
      //   584: aload_0
      //   585: iconst_0
      //   586: putfield mInLongPress : Z
      //   589: aload_0
      //   590: iconst_0
      //   591: putfield mDeferConfirmSingleTap : Z
      //   594: aload_0
      //   595: getfield mIsLongpressEnabled : Z
      //   598: ifeq -> 635
      //   601: aload_0
      //   602: getfield mHandler : Landroid/os/Handler;
      //   605: iconst_2
      //   606: invokevirtual removeMessages : (I)V
      //   609: aload_0
      //   610: getfield mHandler : Landroid/os/Handler;
      //   613: iconst_2
      //   614: aload_0
      //   615: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   618: invokevirtual getDownTime : ()J
      //   621: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   624: i2l
      //   625: ladd
      //   626: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.LONGPRESS_TIMEOUT : I
      //   629: i2l
      //   630: ladd
      //   631: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   634: pop
      //   635: aload_0
      //   636: getfield mHandler : Landroid/os/Handler;
      //   639: iconst_1
      //   640: aload_0
      //   641: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   644: invokevirtual getDownTime : ()J
      //   647: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   650: i2l
      //   651: ladd
      //   652: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   655: pop
      //   656: iload_3
      //   657: aload_0
      //   658: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   661: aload_1
      //   662: invokeinterface onDown : (Landroid/view/MotionEvent;)Z
      //   667: ior
      //   668: istore #13
      //   670: goto -> 232
      //   673: aload_0
      //   674: getfield mHandler : Landroid/os/Handler;
      //   677: iconst_3
      //   678: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT : I
      //   681: i2l
      //   682: invokevirtual sendEmptyMessageDelayed : (IJ)Z
      //   685: pop
      //   686: iload #4
      //   688: istore_3
      //   689: goto -> 523
      //   692: iload #12
      //   694: istore #13
      //   696: aload_0
      //   697: getfield mInLongPress : Z
      //   700: ifne -> 232
      //   703: aload_0
      //   704: getfield mLastFocusX : F
      //   707: fload #5
      //   709: fsub
      //   710: fstore #6
      //   712: aload_0
      //   713: getfield mLastFocusY : F
      //   716: fload #9
      //   718: fsub
      //   719: fstore #14
      //   721: aload_0
      //   722: getfield mIsDoubleTapping : Z
      //   725: ifeq -> 745
      //   728: iconst_0
      //   729: aload_0
      //   730: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   733: aload_1
      //   734: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   739: ior
      //   740: istore #13
      //   742: goto -> 232
      //   745: aload_0
      //   746: getfield mAlwaysInTapRegion : Z
      //   749: ifeq -> 878
      //   752: fload #5
      //   754: aload_0
      //   755: getfield mDownFocusX : F
      //   758: fsub
      //   759: f2i
      //   760: istore_3
      //   761: fload #9
      //   763: aload_0
      //   764: getfield mDownFocusY : F
      //   767: fsub
      //   768: f2i
      //   769: istore #4
      //   771: iload_3
      //   772: iload_3
      //   773: imul
      //   774: iload #4
      //   776: iload #4
      //   778: imul
      //   779: iadd
      //   780: istore_3
      //   781: iload #10
      //   783: istore #12
      //   785: iload_3
      //   786: aload_0
      //   787: getfield mTouchSlopSquare : I
      //   790: if_icmple -> 854
      //   793: aload_0
      //   794: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   797: aload_0
      //   798: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   801: aload_1
      //   802: fload #6
      //   804: fload #14
      //   806: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   811: istore #12
      //   813: aload_0
      //   814: fload #5
      //   816: putfield mLastFocusX : F
      //   819: aload_0
      //   820: fload #9
      //   822: putfield mLastFocusY : F
      //   825: aload_0
      //   826: iconst_0
      //   827: putfield mAlwaysInTapRegion : Z
      //   830: aload_0
      //   831: getfield mHandler : Landroid/os/Handler;
      //   834: iconst_3
      //   835: invokevirtual removeMessages : (I)V
      //   838: aload_0
      //   839: getfield mHandler : Landroid/os/Handler;
      //   842: iconst_1
      //   843: invokevirtual removeMessages : (I)V
      //   846: aload_0
      //   847: getfield mHandler : Landroid/os/Handler;
      //   850: iconst_2
      //   851: invokevirtual removeMessages : (I)V
      //   854: iload #12
      //   856: istore #13
      //   858: iload_3
      //   859: aload_0
      //   860: getfield mTouchSlopSquare : I
      //   863: if_icmple -> 232
      //   866: aload_0
      //   867: iconst_0
      //   868: putfield mAlwaysInBiggerTapRegion : Z
      //   871: iload #12
      //   873: istore #13
      //   875: goto -> 232
      //   878: fload #6
      //   880: invokestatic abs : (F)F
      //   883: fconst_1
      //   884: fcmpl
      //   885: ifge -> 902
      //   888: iload #12
      //   890: istore #13
      //   892: fload #14
      //   894: invokestatic abs : (F)F
      //   897: fconst_1
      //   898: fcmpl
      //   899: iflt -> 232
      //   902: aload_0
      //   903: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   906: aload_0
      //   907: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   910: aload_1
      //   911: fload #6
      //   913: fload #14
      //   915: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   920: istore #13
      //   922: aload_0
      //   923: fload #5
      //   925: putfield mLastFocusX : F
      //   928: aload_0
      //   929: fload #9
      //   931: putfield mLastFocusY : F
      //   934: goto -> 232
      //   937: aload_0
      //   938: iconst_0
      //   939: putfield mStillDown : Z
      //   942: aload_1
      //   943: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   946: astore #15
      //   948: aload_0
      //   949: getfield mIsDoubleTapping : Z
      //   952: ifeq -> 1037
      //   955: iconst_0
      //   956: aload_0
      //   957: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   960: aload_1
      //   961: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   966: ior
      //   967: istore #13
      //   969: aload_0
      //   970: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   973: ifnull -> 983
      //   976: aload_0
      //   977: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   980: invokevirtual recycle : ()V
      //   983: aload_0
      //   984: aload #15
      //   986: putfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   989: aload_0
      //   990: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   993: ifnull -> 1008
      //   996: aload_0
      //   997: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   1000: invokevirtual recycle : ()V
      //   1003: aload_0
      //   1004: aconst_null
      //   1005: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   1008: aload_0
      //   1009: iconst_0
      //   1010: putfield mIsDoubleTapping : Z
      //   1013: aload_0
      //   1014: iconst_0
      //   1015: putfield mDeferConfirmSingleTap : Z
      //   1018: aload_0
      //   1019: getfield mHandler : Landroid/os/Handler;
      //   1022: iconst_1
      //   1023: invokevirtual removeMessages : (I)V
      //   1026: aload_0
      //   1027: getfield mHandler : Landroid/os/Handler;
      //   1030: iconst_2
      //   1031: invokevirtual removeMessages : (I)V
      //   1034: goto -> 232
      //   1037: aload_0
      //   1038: getfield mInLongPress : Z
      //   1041: ifeq -> 1064
      //   1044: aload_0
      //   1045: getfield mHandler : Landroid/os/Handler;
      //   1048: iconst_3
      //   1049: invokevirtual removeMessages : (I)V
      //   1052: aload_0
      //   1053: iconst_0
      //   1054: putfield mInLongPress : Z
      //   1057: iload #11
      //   1059: istore #13
      //   1061: goto -> 969
      //   1064: aload_0
      //   1065: getfield mAlwaysInTapRegion : Z
      //   1068: ifeq -> 1123
      //   1071: aload_0
      //   1072: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   1075: aload_1
      //   1076: invokeinterface onSingleTapUp : (Landroid/view/MotionEvent;)Z
      //   1081: istore #12
      //   1083: iload #12
      //   1085: istore #13
      //   1087: aload_0
      //   1088: getfield mDeferConfirmSingleTap : Z
      //   1091: ifeq -> 969
      //   1094: iload #12
      //   1096: istore #13
      //   1098: aload_0
      //   1099: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   1102: ifnull -> 969
      //   1105: aload_0
      //   1106: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   1109: aload_1
      //   1110: invokeinterface onSingleTapConfirmed : (Landroid/view/MotionEvent;)Z
      //   1115: pop
      //   1116: iload #12
      //   1118: istore #13
      //   1120: goto -> 969
      //   1123: aload_0
      //   1124: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   1127: astore #16
      //   1129: aload_1
      //   1130: iconst_0
      //   1131: invokevirtual getPointerId : (I)I
      //   1134: istore_3
      //   1135: aload #16
      //   1137: sipush #1000
      //   1140: aload_0
      //   1141: getfield mMaximumFlingVelocity : I
      //   1144: i2f
      //   1145: invokevirtual computeCurrentVelocity : (IF)V
      //   1148: aload #16
      //   1150: iload_3
      //   1151: invokestatic getYVelocity : (Landroid/view/VelocityTracker;I)F
      //   1154: fstore #5
      //   1156: aload #16
      //   1158: iload_3
      //   1159: invokestatic getXVelocity : (Landroid/view/VelocityTracker;I)F
      //   1162: fstore #6
      //   1164: fload #5
      //   1166: invokestatic abs : (F)F
      //   1169: aload_0
      //   1170: getfield mMinimumFlingVelocity : I
      //   1173: i2f
      //   1174: fcmpl
      //   1175: ifgt -> 1196
      //   1178: iload #11
      //   1180: istore #13
      //   1182: fload #6
      //   1184: invokestatic abs : (F)F
      //   1187: aload_0
      //   1188: getfield mMinimumFlingVelocity : I
      //   1191: i2f
      //   1192: fcmpl
      //   1193: ifle -> 969
      //   1196: aload_0
      //   1197: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   1200: aload_0
      //   1201: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1204: aload_1
      //   1205: fload #6
      //   1207: fload #5
      //   1209: invokeinterface onFling : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   1214: istore #13
      //   1216: goto -> 969
      //   1219: aload_0
      //   1220: invokespecial cancel : ()V
      //   1223: iload #12
      //   1225: istore #13
      //   1227: goto -> 232
    }
    
    public void setIsLongpressEnabled(boolean param1Boolean) {
      this.mIsLongpressEnabled = param1Boolean;
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) {
      this.mDoubleTapListener = param1OnDoubleTapListener;
    }
    
    private class GestureHandler extends Handler {
      GestureHandler() {}
      
      GestureHandler(Handler param2Handler) {
        super(param2Handler.getLooper());
      }
      
      public void handleMessage(Message param2Message) {
        switch (param2Message.what) {
          default:
            throw new RuntimeException("Unknown message " + param2Message);
          case 1:
            GestureDetectorCompat.GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
            return;
          case 2:
            GestureDetectorCompat.GestureDetectorCompatImplBase.this.dispatchLongPress();
            return;
          case 3:
            break;
        } 
        if (GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener != null) {
          if (!GestureDetectorCompat.GestureDetectorCompatImplBase.this.mStillDown) {
            GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
            return;
          } 
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
        } 
      }
    }
  }
  
  private class GestureHandler extends Handler {
    GestureHandler() {}
    
    GestureHandler(Handler param1Handler) {
      super(param1Handler.getLooper());
    }
    
    public void handleMessage(Message param1Message) {
      switch (param1Message.what) {
        default:
          throw new RuntimeException("Unknown message " + param1Message);
        case 1:
          this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
          return;
        case 2:
          this.this$0.dispatchLongPress();
          return;
        case 3:
          break;
      } 
      if (this.this$0.mDoubleTapListener != null) {
        if (!this.this$0.mStillDown) {
          this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
          return;
        } 
        this.this$0.mDeferConfirmSingleTap = true;
      } 
    }
  }
  
  static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl {
    private final GestureDetector mDetector;
    
    public GestureDetectorCompatImplJellybeanMr2(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) {
      this.mDetector = new GestureDetector(param1Context, param1OnGestureListener, param1Handler);
    }
    
    public boolean isLongpressEnabled() {
      return this.mDetector.isLongpressEnabled();
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      return this.mDetector.onTouchEvent(param1MotionEvent);
    }
    
    public void setIsLongpressEnabled(boolean param1Boolean) {
      this.mDetector.setIsLongpressEnabled(param1Boolean);
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) {
      this.mDetector.setOnDoubleTapListener(param1OnDoubleTapListener);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/view/GestureDetectorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */