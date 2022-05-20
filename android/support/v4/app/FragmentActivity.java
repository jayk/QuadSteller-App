package android.support.v4.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class FragmentActivity extends BaseFragmentActivityJB implements ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompatApi23.RequestPermissionsRequestCodeValidator {
  static final String ALLOCATED_REQUEST_INDICIES_TAG = "android:support:request_indicies";
  
  static final String FRAGMENTS_TAG = "android:support:fragments";
  
  static final int MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS = 65534;
  
  static final int MSG_REALLY_STOPPED = 1;
  
  static final int MSG_RESUME_PENDING = 2;
  
  static final String NEXT_CANDIDATE_REQUEST_INDEX_TAG = "android:support:next_request_index";
  
  static final String REQUEST_FRAGMENT_WHO_TAG = "android:support:request_fragment_who";
  
  private static final String TAG = "FragmentActivity";
  
  boolean mCreated;
  
  final FragmentController mFragments = FragmentController.createController(new HostCallbacks());
  
  final Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        switch (param1Message.what) {
          default:
            super.handleMessage(param1Message);
            return;
          case 1:
            if (FragmentActivity.this.mStopped)
              FragmentActivity.this.doReallyStop(false); 
            return;
          case 2:
            break;
        } 
        FragmentActivity.this.onResumeFragments();
        FragmentActivity.this.mFragments.execPendingActions();
      }
    };
  
  int mNextCandidateRequestIndex;
  
  boolean mOptionsMenuInvalidated;
  
  SparseArrayCompat<String> mPendingFragmentActivityResults;
  
  boolean mReallyStopped;
  
  boolean mRequestedPermissionsFromFragment;
  
  boolean mResumed;
  
  boolean mRetaining;
  
  boolean mStopped;
  
  private int allocateRequestIndex(Fragment paramFragment) {
    if (this.mPendingFragmentActivityResults.size() >= 65534)
      throw new IllegalStateException("Too many pending Fragment activity results."); 
    while (this.mPendingFragmentActivityResults.indexOfKey(this.mNextCandidateRequestIndex) >= 0)
      this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % 65534; 
    int i = this.mNextCandidateRequestIndex;
    this.mPendingFragmentActivityResults.put(i, paramFragment.mWho);
    this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % 65534;
    return i;
  }
  
  private void dumpViewHierarchy(String paramString, PrintWriter paramPrintWriter, View paramView) {
    paramPrintWriter.print(paramString);
    if (paramView == null) {
      paramPrintWriter.println("null");
      return;
    } 
    paramPrintWriter.println(viewToString(paramView));
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int i = viewGroup.getChildCount();
      if (i > 0) {
        paramString = paramString + "  ";
        byte b = 0;
        while (true) {
          if (b < i) {
            dumpViewHierarchy(paramString, paramPrintWriter, viewGroup.getChildAt(b));
            b++;
            continue;
          } 
          return;
        } 
      } 
    } 
  }
  
  private static String viewToString(View paramView) {
    // Byte code:
    //   0: bipush #70
    //   2: istore_1
    //   3: bipush #46
    //   5: istore_2
    //   6: new java/lang/StringBuilder
    //   9: dup
    //   10: sipush #128
    //   13: invokespecial <init> : (I)V
    //   16: astore_3
    //   17: aload_3
    //   18: aload_0
    //   19: invokevirtual getClass : ()Ljava/lang/Class;
    //   22: invokevirtual getName : ()Ljava/lang/String;
    //   25: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: pop
    //   29: aload_3
    //   30: bipush #123
    //   32: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   35: pop
    //   36: aload_3
    //   37: aload_0
    //   38: invokestatic identityHashCode : (Ljava/lang/Object;)I
    //   41: invokestatic toHexString : (I)Ljava/lang/String;
    //   44: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   47: pop
    //   48: aload_3
    //   49: bipush #32
    //   51: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   54: pop
    //   55: aload_0
    //   56: invokevirtual getVisibility : ()I
    //   59: lookupswitch default -> 92, 0 -> 533, 4 -> 543, 8 -> 553
    //   92: aload_3
    //   93: bipush #46
    //   95: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: aload_0
    //   100: invokevirtual isFocusable : ()Z
    //   103: ifeq -> 563
    //   106: bipush #70
    //   108: istore #4
    //   110: iload #4
    //   112: istore #5
    //   114: aload_3
    //   115: iload #5
    //   117: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   120: pop
    //   121: aload_0
    //   122: invokevirtual isEnabled : ()Z
    //   125: ifeq -> 574
    //   128: bipush #69
    //   130: istore #4
    //   132: iload #4
    //   134: istore #5
    //   136: aload_3
    //   137: iload #5
    //   139: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   142: pop
    //   143: aload_0
    //   144: invokevirtual willNotDraw : ()Z
    //   147: ifeq -> 585
    //   150: bipush #46
    //   152: istore #4
    //   154: iload #4
    //   156: istore #5
    //   158: aload_3
    //   159: iload #5
    //   161: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   164: pop
    //   165: aload_0
    //   166: invokevirtual isHorizontalScrollBarEnabled : ()Z
    //   169: ifeq -> 596
    //   172: bipush #72
    //   174: istore #4
    //   176: iload #4
    //   178: istore #5
    //   180: aload_3
    //   181: iload #5
    //   183: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: aload_0
    //   188: invokevirtual isVerticalScrollBarEnabled : ()Z
    //   191: ifeq -> 607
    //   194: bipush #86
    //   196: istore #4
    //   198: iload #4
    //   200: istore #5
    //   202: aload_3
    //   203: iload #5
    //   205: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   208: pop
    //   209: aload_0
    //   210: invokevirtual isClickable : ()Z
    //   213: ifeq -> 618
    //   216: bipush #67
    //   218: istore #4
    //   220: iload #4
    //   222: istore #5
    //   224: aload_3
    //   225: iload #5
    //   227: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   230: pop
    //   231: aload_0
    //   232: invokevirtual isLongClickable : ()Z
    //   235: ifeq -> 629
    //   238: bipush #76
    //   240: istore #4
    //   242: iload #4
    //   244: istore #5
    //   246: aload_3
    //   247: iload #5
    //   249: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   252: pop
    //   253: aload_3
    //   254: bipush #32
    //   256: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   259: pop
    //   260: aload_0
    //   261: invokevirtual isFocused : ()Z
    //   264: ifeq -> 640
    //   267: iload_1
    //   268: istore #5
    //   270: aload_3
    //   271: iload #5
    //   273: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   276: pop
    //   277: aload_0
    //   278: invokevirtual isSelected : ()Z
    //   281: ifeq -> 649
    //   284: bipush #83
    //   286: istore_1
    //   287: iload_1
    //   288: istore #5
    //   290: aload_3
    //   291: iload #5
    //   293: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   296: pop
    //   297: iload_2
    //   298: istore #5
    //   300: aload_0
    //   301: invokevirtual isPressed : ()Z
    //   304: ifeq -> 313
    //   307: bipush #80
    //   309: istore_2
    //   310: iload_2
    //   311: istore #5
    //   313: aload_3
    //   314: iload #5
    //   316: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   319: pop
    //   320: aload_3
    //   321: bipush #32
    //   323: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   326: pop
    //   327: aload_3
    //   328: aload_0
    //   329: invokevirtual getLeft : ()I
    //   332: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   335: pop
    //   336: aload_3
    //   337: bipush #44
    //   339: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   342: pop
    //   343: aload_3
    //   344: aload_0
    //   345: invokevirtual getTop : ()I
    //   348: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   351: pop
    //   352: aload_3
    //   353: bipush #45
    //   355: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   358: pop
    //   359: aload_3
    //   360: aload_0
    //   361: invokevirtual getRight : ()I
    //   364: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   367: pop
    //   368: aload_3
    //   369: bipush #44
    //   371: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   374: pop
    //   375: aload_3
    //   376: aload_0
    //   377: invokevirtual getBottom : ()I
    //   380: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   383: pop
    //   384: aload_0
    //   385: invokevirtual getId : ()I
    //   388: istore_2
    //   389: iload_2
    //   390: iconst_m1
    //   391: if_icmpeq -> 520
    //   394: aload_3
    //   395: ldc ' #'
    //   397: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   400: pop
    //   401: aload_3
    //   402: iload_2
    //   403: invokestatic toHexString : (I)Ljava/lang/String;
    //   406: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   409: pop
    //   410: aload_0
    //   411: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   414: astore #6
    //   416: iload_2
    //   417: ifeq -> 520
    //   420: aload #6
    //   422: ifnull -> 520
    //   425: ldc -16777216
    //   427: iload_2
    //   428: iand
    //   429: lookupswitch default -> 456, 16777216 -> 665, 2130706432 -> 658
    //   456: aload #6
    //   458: iload_2
    //   459: invokevirtual getResourcePackageName : (I)Ljava/lang/String;
    //   462: astore_0
    //   463: aload #6
    //   465: iload_2
    //   466: invokevirtual getResourceTypeName : (I)Ljava/lang/String;
    //   469: astore #7
    //   471: aload #6
    //   473: iload_2
    //   474: invokevirtual getResourceEntryName : (I)Ljava/lang/String;
    //   477: astore #6
    //   479: aload_3
    //   480: ldc ' '
    //   482: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   485: pop
    //   486: aload_3
    //   487: aload_0
    //   488: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   491: pop
    //   492: aload_3
    //   493: ldc ':'
    //   495: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   498: pop
    //   499: aload_3
    //   500: aload #7
    //   502: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   505: pop
    //   506: aload_3
    //   507: ldc '/'
    //   509: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   512: pop
    //   513: aload_3
    //   514: aload #6
    //   516: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   519: pop
    //   520: aload_3
    //   521: ldc_w '}'
    //   524: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   527: pop
    //   528: aload_3
    //   529: invokevirtual toString : ()Ljava/lang/String;
    //   532: areturn
    //   533: aload_3
    //   534: bipush #86
    //   536: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   539: pop
    //   540: goto -> 99
    //   543: aload_3
    //   544: bipush #73
    //   546: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   549: pop
    //   550: goto -> 99
    //   553: aload_3
    //   554: bipush #71
    //   556: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   559: pop
    //   560: goto -> 99
    //   563: bipush #46
    //   565: istore #4
    //   567: iload #4
    //   569: istore #5
    //   571: goto -> 114
    //   574: bipush #46
    //   576: istore #4
    //   578: iload #4
    //   580: istore #5
    //   582: goto -> 136
    //   585: bipush #68
    //   587: istore #4
    //   589: iload #4
    //   591: istore #5
    //   593: goto -> 158
    //   596: bipush #46
    //   598: istore #4
    //   600: iload #4
    //   602: istore #5
    //   604: goto -> 180
    //   607: bipush #46
    //   609: istore #4
    //   611: iload #4
    //   613: istore #5
    //   615: goto -> 202
    //   618: bipush #46
    //   620: istore #4
    //   622: iload #4
    //   624: istore #5
    //   626: goto -> 224
    //   629: bipush #46
    //   631: istore #4
    //   633: iload #4
    //   635: istore #5
    //   637: goto -> 246
    //   640: bipush #46
    //   642: istore_1
    //   643: iload_1
    //   644: istore #5
    //   646: goto -> 270
    //   649: bipush #46
    //   651: istore_1
    //   652: iload_1
    //   653: istore #5
    //   655: goto -> 290
    //   658: ldc_w 'app'
    //   661: astore_0
    //   662: goto -> 463
    //   665: ldc_w 'android'
    //   668: astore_0
    //   669: goto -> 463
    //   672: astore_0
    //   673: goto -> 520
    // Exception table:
    //   from	to	target	type
    //   456	463	672	android/content/res/Resources$NotFoundException
    //   463	520	672	android/content/res/Resources$NotFoundException
  }
  
  final View dispatchFragmentsOnCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    return this.mFragments.onCreateView(paramView, paramString, paramContext, paramAttributeSet);
  }
  
  void doReallyStop(boolean paramBoolean) {
    if (!this.mReallyStopped) {
      this.mReallyStopped = true;
      this.mRetaining = paramBoolean;
      this.mHandler.removeMessages(1);
      onReallyStop();
      return;
    } 
    if (paramBoolean) {
      this.mFragments.doLoaderStart();
      this.mFragments.doLoaderStop(true);
    } 
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    if (Build.VERSION.SDK_INT >= 11);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Local FragmentActivity ");
    paramPrintWriter.print(Integer.toHexString(System.identityHashCode(this)));
    paramPrintWriter.println(" State:");
    String str = paramString + "  ";
    paramPrintWriter.print(str);
    paramPrintWriter.print("mCreated=");
    paramPrintWriter.print(this.mCreated);
    paramPrintWriter.print("mResumed=");
    paramPrintWriter.print(this.mResumed);
    paramPrintWriter.print(" mStopped=");
    paramPrintWriter.print(this.mStopped);
    paramPrintWriter.print(" mReallyStopped=");
    paramPrintWriter.println(this.mReallyStopped);
    this.mFragments.dumpLoaders(str, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    this.mFragments.getSupportFragmentManager().dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("View Hierarchy:");
    dumpViewHierarchy(paramString + "  ", paramPrintWriter, getWindow().getDecorView());
  }
  
  public Object getLastCustomNonConfigurationInstance() {
    null = (NonConfigurationInstances)getLastNonConfigurationInstance();
    return (null != null) ? null.custom : null;
  }
  
  public FragmentManager getSupportFragmentManager() {
    return this.mFragments.getSupportFragmentManager();
  }
  
  public LoaderManager getSupportLoaderManager() {
    return this.mFragments.getSupportLoaderManager();
  }
  
  @Deprecated
  public final MediaControllerCompat getSupportMediaController() {
    return MediaControllerCompat.getMediaController(this);
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    this.mFragments.noteStateNotSaved();
    int i = paramInt1 >> 16;
    if (i != 0) {
      String str = (String)this.mPendingFragmentActivityResults.get(--i);
      this.mPendingFragmentActivityResults.remove(i);
      if (str == null) {
        Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
        return;
      } 
      Fragment fragment = this.mFragments.findFragmentByWho(str);
      if (fragment == null) {
        Log.w("FragmentActivity", "Activity result no fragment exists for who: " + str);
        return;
      } 
      fragment.onActivityResult(0xFFFF & paramInt1, paramInt2, paramIntent);
      return;
    } 
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }
  
  public void onAttachFragment(Fragment paramFragment) {}
  
  public void onBackPressed() {
    if (!this.mFragments.getSupportFragmentManager().popBackStackImmediate())
      super.onBackPressed(); 
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    this.mFragments.dispatchConfigurationChanged(paramConfiguration);
  }
  
  protected void onCreate(@Nullable Bundle paramBundle) {
    FragmentManagerNonConfig fragmentManagerNonConfig = null;
    this.mFragments.attachHost(null);
    super.onCreate(paramBundle);
    NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
    if (nonConfigurationInstances != null)
      this.mFragments.restoreLoaderNonConfig(nonConfigurationInstances.loaders); 
    if (paramBundle != null) {
      Parcelable parcelable = paramBundle.getParcelable("android:support:fragments");
      FragmentController fragmentController = this.mFragments;
      if (nonConfigurationInstances != null)
        fragmentManagerNonConfig = nonConfigurationInstances.fragments; 
      fragmentController.restoreAllState(parcelable, fragmentManagerNonConfig);
      if (paramBundle.containsKey("android:support:next_request_index")) {
        this.mNextCandidateRequestIndex = paramBundle.getInt("android:support:next_request_index");
        int[] arrayOfInt = paramBundle.getIntArray("android:support:request_indicies");
        String[] arrayOfString = paramBundle.getStringArray("android:support:request_fragment_who");
        if (arrayOfInt == null || arrayOfString == null || arrayOfInt.length != arrayOfString.length) {
          Log.w("FragmentActivity", "Invalid requestCode mapping in savedInstanceState.");
        } else {
          this.mPendingFragmentActivityResults = new SparseArrayCompat(arrayOfInt.length);
          byte b = 0;
          while (true) {
            if (b < arrayOfInt.length) {
              this.mPendingFragmentActivityResults.put(arrayOfInt[b], arrayOfString[b]);
              b++;
              continue;
            } 
            if (this.mPendingFragmentActivityResults == null) {
              this.mPendingFragmentActivityResults = new SparseArrayCompat();
              this.mNextCandidateRequestIndex = 0;
            } 
            this.mFragments.dispatchCreate();
            return;
          } 
        } 
      } 
    } 
    if (this.mPendingFragmentActivityResults == null) {
      this.mPendingFragmentActivityResults = new SparseArrayCompat();
      this.mNextCandidateRequestIndex = 0;
    } 
    this.mFragments.dispatchCreate();
  }
  
  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu) {
    if (paramInt == 0) {
      int i = super.onCreatePanelMenu(paramInt, paramMenu) | this.mFragments.dispatchCreateOptionsMenu(paramMenu, getMenuInflater());
      if (Build.VERSION.SDK_INT < 11)
        i = 1; 
      return i;
    } 
    return super.onCreatePanelMenu(paramInt, paramMenu);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    doReallyStop(false);
    this.mFragments.dispatchDestroy();
    this.mFragments.doLoaderDestroy();
  }
  
  public void onLowMemory() {
    super.onLowMemory();
    this.mFragments.dispatchLowMemory();
  }
  
  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem) {
    if (super.onMenuItemSelected(paramInt, paramMenuItem))
      return true; 
    switch (paramInt) {
      default:
        return false;
      case 0:
        return this.mFragments.dispatchOptionsItemSelected(paramMenuItem);
      case 6:
        break;
    } 
    return this.mFragments.dispatchContextItemSelected(paramMenuItem);
  }
  
  @CallSuper
  public void onMultiWindowModeChanged(boolean paramBoolean) {
    this.mFragments.dispatchMultiWindowModeChanged(paramBoolean);
  }
  
  protected void onNewIntent(Intent paramIntent) {
    super.onNewIntent(paramIntent);
    this.mFragments.noteStateNotSaved();
  }
  
  public void onPanelClosed(int paramInt, Menu paramMenu) {
    switch (paramInt) {
      default:
        super.onPanelClosed(paramInt, paramMenu);
        return;
      case 0:
        break;
    } 
    this.mFragments.dispatchOptionsMenuClosed(paramMenu);
  }
  
  protected void onPause() {
    super.onPause();
    this.mResumed = false;
    if (this.mHandler.hasMessages(2)) {
      this.mHandler.removeMessages(2);
      onResumeFragments();
    } 
    this.mFragments.dispatchPause();
  }
  
  @CallSuper
  public void onPictureInPictureModeChanged(boolean paramBoolean) {
    this.mFragments.dispatchPictureInPictureModeChanged(paramBoolean);
  }
  
  protected void onPostResume() {
    super.onPostResume();
    this.mHandler.removeMessages(2);
    onResumeFragments();
    this.mFragments.execPendingActions();
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  protected boolean onPrepareOptionsPanel(View paramView, Menu paramMenu) {
    return super.onPreparePanel(0, paramView, paramMenu);
  }
  
  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu) {
    if (paramInt == 0 && paramMenu != null) {
      if (this.mOptionsMenuInvalidated) {
        this.mOptionsMenuInvalidated = false;
        paramMenu.clear();
        onCreatePanelMenu(paramInt, paramMenu);
      } 
      return onPrepareOptionsPanel(paramView, paramMenu) | this.mFragments.dispatchPrepareOptionsMenu(paramMenu);
    } 
    return super.onPreparePanel(paramInt, paramView, paramMenu);
  }
  
  void onReallyStop() {
    this.mFragments.doLoaderStop(this.mRetaining);
    this.mFragments.dispatchReallyStop();
  }
  
  public void onRequestPermissionsResult(int paramInt, @NonNull String[] paramArrayOfString, @NonNull int[] paramArrayOfint) {
    String str;
    int i = paramInt >> 16 & 0xFFFF;
    if (i != 0) {
      str = (String)this.mPendingFragmentActivityResults.get(--i);
      this.mPendingFragmentActivityResults.remove(i);
      if (str == null) {
        Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
        return;
      } 
    } else {
      return;
    } 
    Fragment fragment = this.mFragments.findFragmentByWho(str);
    if (fragment == null) {
      Log.w("FragmentActivity", "Activity result no fragment exists for who: " + str);
      return;
    } 
    fragment.onRequestPermissionsResult(paramInt & 0xFFFF, paramArrayOfString, paramArrayOfint);
  }
  
  protected void onResume() {
    super.onResume();
    this.mHandler.sendEmptyMessage(2);
    this.mResumed = true;
    this.mFragments.execPendingActions();
  }
  
  protected void onResumeFragments() {
    this.mFragments.dispatchResume();
  }
  
  public Object onRetainCustomNonConfigurationInstance() {
    return null;
  }
  
  public final Object onRetainNonConfigurationInstance() {
    if (this.mStopped)
      doReallyStop(true); 
    Object object = onRetainCustomNonConfigurationInstance();
    FragmentManagerNonConfig fragmentManagerNonConfig = this.mFragments.retainNestedNonConfig();
    SimpleArrayMap<String, LoaderManager> simpleArrayMap = this.mFragments.retainLoaderNonConfig();
    if (fragmentManagerNonConfig == null && simpleArrayMap == null && object == null)
      return null; 
    NonConfigurationInstances nonConfigurationInstances = new NonConfigurationInstances();
    nonConfigurationInstances.custom = object;
    nonConfigurationInstances.fragments = fragmentManagerNonConfig;
    nonConfigurationInstances.loaders = simpleArrayMap;
    return nonConfigurationInstances;
  }
  
  protected void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    Parcelable parcelable = this.mFragments.saveAllState();
    if (parcelable != null)
      paramBundle.putParcelable("android:support:fragments", parcelable); 
    if (this.mPendingFragmentActivityResults.size() > 0) {
      paramBundle.putInt("android:support:next_request_index", this.mNextCandidateRequestIndex);
      int[] arrayOfInt = new int[this.mPendingFragmentActivityResults.size()];
      String[] arrayOfString = new String[this.mPendingFragmentActivityResults.size()];
      for (byte b = 0; b < this.mPendingFragmentActivityResults.size(); b++) {
        arrayOfInt[b] = this.mPendingFragmentActivityResults.keyAt(b);
        arrayOfString[b] = (String)this.mPendingFragmentActivityResults.valueAt(b);
      } 
      paramBundle.putIntArray("android:support:request_indicies", arrayOfInt);
      paramBundle.putStringArray("android:support:request_fragment_who", arrayOfString);
    } 
  }
  
  protected void onStart() {
    super.onStart();
    this.mStopped = false;
    this.mReallyStopped = false;
    this.mHandler.removeMessages(1);
    if (!this.mCreated) {
      this.mCreated = true;
      this.mFragments.dispatchActivityCreated();
    } 
    this.mFragments.noteStateNotSaved();
    this.mFragments.execPendingActions();
    this.mFragments.doLoaderStart();
    this.mFragments.dispatchStart();
    this.mFragments.reportLoaderStart();
  }
  
  public void onStateNotSaved() {
    this.mFragments.noteStateNotSaved();
  }
  
  protected void onStop() {
    super.onStop();
    this.mStopped = true;
    this.mHandler.sendEmptyMessage(1);
    this.mFragments.dispatchStop();
  }
  
  void requestPermissionsFromFragment(Fragment paramFragment, String[] paramArrayOfString, int paramInt) {
    if (paramInt == -1) {
      ActivityCompat.requestPermissions(this, paramArrayOfString, paramInt);
      return;
    } 
    checkForValidRequestCode(paramInt);
    try {
      this.mRequestedPermissionsFromFragment = true;
      ActivityCompat.requestPermissions(this, paramArrayOfString, (allocateRequestIndex(paramFragment) + 1 << 16) + (0xFFFF & paramInt));
      return;
    } finally {
      this.mRequestedPermissionsFromFragment = false;
    } 
  }
  
  public void setEnterSharedElementCallback(SharedElementCallback paramSharedElementCallback) {
    ActivityCompat.setEnterSharedElementCallback(this, paramSharedElementCallback);
  }
  
  public void setExitSharedElementCallback(SharedElementCallback paramSharedElementCallback) {
    ActivityCompat.setExitSharedElementCallback(this, paramSharedElementCallback);
  }
  
  @Deprecated
  public final void setSupportMediaController(MediaControllerCompat paramMediaControllerCompat) {
    MediaControllerCompat.setMediaController(this, paramMediaControllerCompat);
  }
  
  public void startActivityForResult(Intent paramIntent, int paramInt) {
    if (!this.mStartedActivityFromFragment && paramInt != -1)
      checkForValidRequestCode(paramInt); 
    super.startActivityForResult(paramIntent, paramInt);
  }
  
  public void startActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt) {
    startActivityFromFragment(paramFragment, paramIntent, paramInt, (Bundle)null);
  }
  
  public void startActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt, @Nullable Bundle paramBundle) {
    this.mStartedActivityFromFragment = true;
    if (paramInt == -1)
      try {
        ActivityCompat.startActivityForResult(this, paramIntent, -1, paramBundle);
        return;
      } finally {
        this.mStartedActivityFromFragment = false;
      }  
    checkForValidRequestCode(paramInt);
    ActivityCompat.startActivityForResult(this, paramIntent, (allocateRequestIndex(paramFragment) + 1 << 16) + (0xFFFF & paramInt), paramBundle);
    this.mStartedActivityFromFragment = false;
  }
  
  public void startIntentSenderFromFragment(Fragment paramFragment, IntentSender paramIntentSender, int paramInt1, @Nullable Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle) throws IntentSender.SendIntentException {
    this.mStartedIntentSenderFromFragment = true;
    if (paramInt1 == -1)
      try {
        ActivityCompat.startIntentSenderForResult(this, paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
        return;
      } finally {
        this.mStartedIntentSenderFromFragment = false;
      }  
    checkForValidRequestCode(paramInt1);
    ActivityCompat.startIntentSenderForResult(this, paramIntentSender, (allocateRequestIndex(paramFragment) + 1 << 16) + (0xFFFF & paramInt1), paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
    this.mStartedIntentSenderFromFragment = false;
  }
  
  public void supportFinishAfterTransition() {
    ActivityCompat.finishAfterTransition(this);
  }
  
  public void supportInvalidateOptionsMenu() {
    if (Build.VERSION.SDK_INT >= 11) {
      ActivityCompatHoneycomb.invalidateOptionsMenu(this);
      return;
    } 
    this.mOptionsMenuInvalidated = true;
  }
  
  public void supportPostponeEnterTransition() {
    ActivityCompat.postponeEnterTransition(this);
  }
  
  public void supportStartPostponedEnterTransition() {
    ActivityCompat.startPostponedEnterTransition(this);
  }
  
  public final void validateRequestPermissionsRequestCode(int paramInt) {
    if (!this.mRequestedPermissionsFromFragment && paramInt != -1)
      checkForValidRequestCode(paramInt); 
  }
  
  class HostCallbacks extends FragmentHostCallback<FragmentActivity> {
    public HostCallbacks() {
      super(FragmentActivity.this);
    }
    
    public void onAttachFragment(Fragment param1Fragment) {
      FragmentActivity.this.onAttachFragment(param1Fragment);
    }
    
    @SuppressLint({"NewApi"})
    public void onDump(String param1String, FileDescriptor param1FileDescriptor, PrintWriter param1PrintWriter, String[] param1ArrayOfString) {
      FragmentActivity.this.dump(param1String, param1FileDescriptor, param1PrintWriter, param1ArrayOfString);
    }
    
    @Nullable
    public View onFindViewById(int param1Int) {
      return FragmentActivity.this.findViewById(param1Int);
    }
    
    public FragmentActivity onGetHost() {
      return FragmentActivity.this;
    }
    
    public LayoutInflater onGetLayoutInflater() {
      return FragmentActivity.this.getLayoutInflater().cloneInContext((Context)FragmentActivity.this);
    }
    
    public int onGetWindowAnimations() {
      Window window = FragmentActivity.this.getWindow();
      return (window == null) ? 0 : (window.getAttributes()).windowAnimations;
    }
    
    public boolean onHasView() {
      Window window = FragmentActivity.this.getWindow();
      return (window != null && window.peekDecorView() != null);
    }
    
    public boolean onHasWindowAnimations() {
      return (FragmentActivity.this.getWindow() != null);
    }
    
    public void onRequestPermissionsFromFragment(@NonNull Fragment param1Fragment, @NonNull String[] param1ArrayOfString, int param1Int) {
      FragmentActivity.this.requestPermissionsFromFragment(param1Fragment, param1ArrayOfString, param1Int);
    }
    
    public boolean onShouldSaveFragmentState(Fragment param1Fragment) {
      return !FragmentActivity.this.isFinishing();
    }
    
    public boolean onShouldShowRequestPermissionRationale(@NonNull String param1String) {
      return ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this, param1String);
    }
    
    public void onStartActivityFromFragment(Fragment param1Fragment, Intent param1Intent, int param1Int) {
      FragmentActivity.this.startActivityFromFragment(param1Fragment, param1Intent, param1Int);
    }
    
    public void onStartActivityFromFragment(Fragment param1Fragment, Intent param1Intent, int param1Int, @Nullable Bundle param1Bundle) {
      FragmentActivity.this.startActivityFromFragment(param1Fragment, param1Intent, param1Int, param1Bundle);
    }
    
    public void onStartIntentSenderFromFragment(Fragment param1Fragment, IntentSender param1IntentSender, int param1Int1, @Nullable Intent param1Intent, int param1Int2, int param1Int3, int param1Int4, Bundle param1Bundle) throws IntentSender.SendIntentException {
      FragmentActivity.this.startIntentSenderFromFragment(param1Fragment, param1IntentSender, param1Int1, param1Intent, param1Int2, param1Int3, param1Int4, param1Bundle);
    }
    
    public void onSupportInvalidateOptionsMenu() {
      FragmentActivity.this.supportInvalidateOptionsMenu();
    }
  }
  
  static final class NonConfigurationInstances {
    Object custom;
    
    FragmentManagerNonConfig fragments;
    
    SimpleArrayMap<String, LoaderManager> loaders;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/app/FragmentActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */