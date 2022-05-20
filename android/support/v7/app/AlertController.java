package android.support.v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.appcompat.R;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.lang.ref.WeakReference;

class AlertController {
  ListAdapter mAdapter;
  
  private int mAlertDialogLayout;
  
  private final View.OnClickListener mButtonHandler = new View.OnClickListener() {
      public void onClick(View param1View) {
        Message message;
        if (param1View == AlertController.this.mButtonPositive && AlertController.this.mButtonPositiveMessage != null) {
          message = Message.obtain(AlertController.this.mButtonPositiveMessage);
        } else if (message == AlertController.this.mButtonNegative && AlertController.this.mButtonNegativeMessage != null) {
          message = Message.obtain(AlertController.this.mButtonNegativeMessage);
        } else if (message == AlertController.this.mButtonNeutral && AlertController.this.mButtonNeutralMessage != null) {
          message = Message.obtain(AlertController.this.mButtonNeutralMessage);
        } else {
          message = null;
        } 
        if (message != null)
          message.sendToTarget(); 
        AlertController.this.mHandler.obtainMessage(1, AlertController.this.mDialog).sendToTarget();
      }
    };
  
  Button mButtonNegative;
  
  Message mButtonNegativeMessage;
  
  private CharSequence mButtonNegativeText;
  
  Button mButtonNeutral;
  
  Message mButtonNeutralMessage;
  
  private CharSequence mButtonNeutralText;
  
  private int mButtonPanelLayoutHint = 0;
  
  private int mButtonPanelSideLayout;
  
  Button mButtonPositive;
  
  Message mButtonPositiveMessage;
  
  private CharSequence mButtonPositiveText;
  
  int mCheckedItem = -1;
  
  private final Context mContext;
  
  private View mCustomTitleView;
  
  final AppCompatDialog mDialog;
  
  Handler mHandler;
  
  private Drawable mIcon;
  
  private int mIconId = 0;
  
  private ImageView mIconView;
  
  int mListItemLayout;
  
  int mListLayout;
  
  ListView mListView;
  
  private CharSequence mMessage;
  
  private TextView mMessageView;
  
  int mMultiChoiceItemLayout;
  
  NestedScrollView mScrollView;
  
  private boolean mShowTitle;
  
  int mSingleChoiceItemLayout;
  
  private CharSequence mTitle;
  
  private TextView mTitleView;
  
  private View mView;
  
  private int mViewLayoutResId;
  
  private int mViewSpacingBottom;
  
  private int mViewSpacingLeft;
  
  private int mViewSpacingRight;
  
  private boolean mViewSpacingSpecified = false;
  
  private int mViewSpacingTop;
  
  private final Window mWindow;
  
  public AlertController(Context paramContext, AppCompatDialog paramAppCompatDialog, Window paramWindow) {
    this.mContext = paramContext;
    this.mDialog = paramAppCompatDialog;
    this.mWindow = paramWindow;
    this.mHandler = new ButtonHandler((DialogInterface)paramAppCompatDialog);
    TypedArray typedArray = paramContext.obtainStyledAttributes(null, R.styleable.AlertDialog, R.attr.alertDialogStyle, 0);
    this.mAlertDialogLayout = typedArray.getResourceId(R.styleable.AlertDialog_android_layout, 0);
    this.mButtonPanelSideLayout = typedArray.getResourceId(R.styleable.AlertDialog_buttonPanelSideLayout, 0);
    this.mListLayout = typedArray.getResourceId(R.styleable.AlertDialog_listLayout, 0);
    this.mMultiChoiceItemLayout = typedArray.getResourceId(R.styleable.AlertDialog_multiChoiceItemLayout, 0);
    this.mSingleChoiceItemLayout = typedArray.getResourceId(R.styleable.AlertDialog_singleChoiceItemLayout, 0);
    this.mListItemLayout = typedArray.getResourceId(R.styleable.AlertDialog_listItemLayout, 0);
    this.mShowTitle = typedArray.getBoolean(R.styleable.AlertDialog_showTitle, true);
    typedArray.recycle();
    paramAppCompatDialog.supportRequestWindowFeature(1);
  }
  
  static boolean canTextInput(View paramView) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_1
    //   2: aload_0
    //   3: invokevirtual onCheckIsTextEditor : ()Z
    //   6: ifeq -> 11
    //   9: iload_1
    //   10: ireturn
    //   11: aload_0
    //   12: instanceof android/view/ViewGroup
    //   15: ifne -> 23
    //   18: iconst_0
    //   19: istore_1
    //   20: goto -> 9
    //   23: aload_0
    //   24: checkcast android/view/ViewGroup
    //   27: astore_0
    //   28: aload_0
    //   29: invokevirtual getChildCount : ()I
    //   32: istore_2
    //   33: iload_2
    //   34: ifle -> 57
    //   37: iload_2
    //   38: iconst_1
    //   39: isub
    //   40: istore_3
    //   41: iload_3
    //   42: istore_2
    //   43: aload_0
    //   44: iload_3
    //   45: invokevirtual getChildAt : (I)Landroid/view/View;
    //   48: invokestatic canTextInput : (Landroid/view/View;)Z
    //   51: ifeq -> 33
    //   54: goto -> 9
    //   57: iconst_0
    //   58: istore_1
    //   59: goto -> 9
  }
  
  private void centerButton(Button paramButton) {
    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)paramButton.getLayoutParams();
    layoutParams.gravity = 1;
    layoutParams.weight = 0.5F;
    paramButton.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
  }
  
  static void manageScrollIndicators(View paramView1, View paramView2, View paramView3) {
    boolean bool = false;
    if (paramView2 != null) {
      byte b;
      if (ViewCompat.canScrollVertically(paramView1, -1)) {
        b = 0;
      } else {
        b = 4;
      } 
      paramView2.setVisibility(b);
    } 
    if (paramView3 != null) {
      byte b;
      if (ViewCompat.canScrollVertically(paramView1, 1)) {
        b = bool;
      } else {
        b = 4;
      } 
      paramView3.setVisibility(b);
    } 
  }
  
  @Nullable
  private ViewGroup resolvePanel(@Nullable View paramView1, @Nullable View paramView2) {
    if (paramView1 == null) {
      paramView1 = paramView2;
      if (paramView2 instanceof ViewStub)
        paramView1 = ((ViewStub)paramView2).inflate(); 
      return (ViewGroup)paramView1;
    } 
    if (paramView2 != null) {
      ViewParent viewParent = paramView2.getParent();
      if (viewParent instanceof ViewGroup)
        ((ViewGroup)viewParent).removeView(paramView2); 
    } 
    paramView2 = paramView1;
    if (paramView1 instanceof ViewStub)
      paramView2 = ((ViewStub)paramView1).inflate(); 
    return (ViewGroup)paramView2;
  }
  
  private int selectContentView() {
    return (this.mButtonPanelSideLayout == 0) ? this.mAlertDialogLayout : ((this.mButtonPanelLayoutHint == 1) ? this.mButtonPanelSideLayout : this.mAlertDialogLayout);
  }
  
  private void setScrollIndicators(ViewGroup paramViewGroup, final View top, int paramInt1, int paramInt2) {
    final View bottom = this.mWindow.findViewById(R.id.scrollIndicatorUp);
    View view2 = this.mWindow.findViewById(R.id.scrollIndicatorDown);
    if (Build.VERSION.SDK_INT >= 23) {
      ViewCompat.setScrollIndicators(top, paramInt1, paramInt2);
      if (view1 != null)
        paramViewGroup.removeView(view1); 
      if (view2 != null)
        paramViewGroup.removeView(view2); 
      return;
    } 
    top = view1;
    if (view1 != null) {
      top = view1;
      if ((paramInt1 & 0x1) == 0) {
        paramViewGroup.removeView(view1);
        top = null;
      } 
    } 
    view1 = view2;
    if (view2 != null) {
      view1 = view2;
      if ((paramInt1 & 0x2) == 0) {
        paramViewGroup.removeView(view2);
        view1 = null;
      } 
    } 
    if (top != null || view1 != null) {
      if (this.mMessage != null) {
        this.mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
              public void onScrollChange(NestedScrollView param1NestedScrollView, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
                AlertController.manageScrollIndicators((View)param1NestedScrollView, top, bottom);
              }
            });
        this.mScrollView.post(new Runnable() {
              public void run() {
                AlertController.manageScrollIndicators((View)AlertController.this.mScrollView, top, bottom);
              }
            });
        return;
      } 
      if (this.mListView != null) {
        this.mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
              public void onScroll(AbsListView param1AbsListView, int param1Int1, int param1Int2, int param1Int3) {
                AlertController.manageScrollIndicators((View)param1AbsListView, top, bottom);
              }
              
              public void onScrollStateChanged(AbsListView param1AbsListView, int param1Int) {}
            });
        this.mListView.post(new Runnable() {
              public void run() {
                AlertController.manageScrollIndicators((View)AlertController.this.mListView, top, bottom);
              }
            });
        return;
      } 
      if (top != null)
        paramViewGroup.removeView(top); 
      if (view1 != null)
        paramViewGroup.removeView(view1); 
    } 
  }
  
  private void setupButtons(ViewGroup paramViewGroup) {
    boolean bool = false;
    int i = 0;
    this.mButtonPositive = (Button)paramViewGroup.findViewById(16908313);
    this.mButtonPositive.setOnClickListener(this.mButtonHandler);
    if (TextUtils.isEmpty(this.mButtonPositiveText)) {
      this.mButtonPositive.setVisibility(8);
    } else {
      this.mButtonPositive.setText(this.mButtonPositiveText);
      this.mButtonPositive.setVisibility(0);
      i = false | true;
    } 
    this.mButtonNegative = (Button)paramViewGroup.findViewById(16908314);
    this.mButtonNegative.setOnClickListener(this.mButtonHandler);
    if (TextUtils.isEmpty(this.mButtonNegativeText)) {
      this.mButtonNegative.setVisibility(8);
    } else {
      this.mButtonNegative.setText(this.mButtonNegativeText);
      this.mButtonNegative.setVisibility(0);
      i |= 0x2;
    } 
    this.mButtonNeutral = (Button)paramViewGroup.findViewById(16908315);
    this.mButtonNeutral.setOnClickListener(this.mButtonHandler);
    if (TextUtils.isEmpty(this.mButtonNeutralText)) {
      this.mButtonNeutral.setVisibility(8);
    } else {
      this.mButtonNeutral.setText(this.mButtonNeutralText);
      this.mButtonNeutral.setVisibility(0);
      i |= 0x4;
    } 
    if (shouldCenterSingleButton(this.mContext))
      if (i == 1) {
        centerButton(this.mButtonPositive);
      } else if (i == 2) {
        centerButton(this.mButtonNegative);
      } else if (i == 4) {
        centerButton(this.mButtonNeutral);
      }  
    if (i != 0)
      bool = true; 
    if (!bool)
      paramViewGroup.setVisibility(8); 
  }
  
  private void setupContent(ViewGroup paramViewGroup) {
    this.mScrollView = (NestedScrollView)this.mWindow.findViewById(R.id.scrollView);
    this.mScrollView.setFocusable(false);
    this.mScrollView.setNestedScrollingEnabled(false);
    this.mMessageView = (TextView)paramViewGroup.findViewById(16908299);
    if (this.mMessageView != null) {
      if (this.mMessage != null) {
        this.mMessageView.setText(this.mMessage);
        return;
      } 
      this.mMessageView.setVisibility(8);
      this.mScrollView.removeView((View)this.mMessageView);
      if (this.mListView != null) {
        paramViewGroup = (ViewGroup)this.mScrollView.getParent();
        int i = paramViewGroup.indexOfChild((View)this.mScrollView);
        paramViewGroup.removeViewAt(i);
        paramViewGroup.addView((View)this.mListView, i, new ViewGroup.LayoutParams(-1, -1));
        return;
      } 
      paramViewGroup.setVisibility(8);
    } 
  }
  
  private void setupCustomContent(ViewGroup paramViewGroup) {
    View view;
    boolean bool = false;
    if (this.mView != null) {
      view = this.mView;
    } else if (this.mViewLayoutResId != 0) {
      view = LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, paramViewGroup, false);
    } else {
      view = null;
    } 
    if (view != null)
      bool = true; 
    if (!bool || !canTextInput(view))
      this.mWindow.setFlags(131072, 131072); 
    if (bool) {
      FrameLayout frameLayout = (FrameLayout)this.mWindow.findViewById(R.id.custom);
      frameLayout.addView(view, new ViewGroup.LayoutParams(-1, -1));
      if (this.mViewSpacingSpecified)
        frameLayout.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom); 
      if (this.mListView != null)
        ((LinearLayout.LayoutParams)paramViewGroup.getLayoutParams()).weight = 0.0F; 
      return;
    } 
    paramViewGroup.setVisibility(8);
  }
  
  private void setupTitle(ViewGroup paramViewGroup) {
    boolean bool = false;
    if (this.mCustomTitleView != null) {
      ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -2);
      paramViewGroup.addView(this.mCustomTitleView, 0, layoutParams);
      this.mWindow.findViewById(R.id.title_template).setVisibility(8);
      return;
    } 
    this.mIconView = (ImageView)this.mWindow.findViewById(16908294);
    if (!TextUtils.isEmpty(this.mTitle))
      bool = true; 
    if (bool && this.mShowTitle) {
      this.mTitleView = (TextView)this.mWindow.findViewById(R.id.alertTitle);
      this.mTitleView.setText(this.mTitle);
      if (this.mIconId != 0) {
        this.mIconView.setImageResource(this.mIconId);
        return;
      } 
      if (this.mIcon != null) {
        this.mIconView.setImageDrawable(this.mIcon);
        return;
      } 
      this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
      this.mIconView.setVisibility(8);
      return;
    } 
    this.mWindow.findViewById(R.id.title_template).setVisibility(8);
    this.mIconView.setVisibility(8);
    paramViewGroup.setVisibility(8);
  }
  
  private void setupView() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mWindow : Landroid/view/Window;
    //   4: getstatic android/support/v7/appcompat/R$id.parentPanel : I
    //   7: invokevirtual findViewById : (I)Landroid/view/View;
    //   10: astore_1
    //   11: aload_1
    //   12: getstatic android/support/v7/appcompat/R$id.topPanel : I
    //   15: invokevirtual findViewById : (I)Landroid/view/View;
    //   18: astore_2
    //   19: aload_1
    //   20: getstatic android/support/v7/appcompat/R$id.contentPanel : I
    //   23: invokevirtual findViewById : (I)Landroid/view/View;
    //   26: astore_3
    //   27: aload_1
    //   28: getstatic android/support/v7/appcompat/R$id.buttonPanel : I
    //   31: invokevirtual findViewById : (I)Landroid/view/View;
    //   34: astore #4
    //   36: aload_1
    //   37: getstatic android/support/v7/appcompat/R$id.customPanel : I
    //   40: invokevirtual findViewById : (I)Landroid/view/View;
    //   43: checkcast android/view/ViewGroup
    //   46: astore_1
    //   47: aload_0
    //   48: aload_1
    //   49: invokespecial setupCustomContent : (Landroid/view/ViewGroup;)V
    //   52: aload_1
    //   53: getstatic android/support/v7/appcompat/R$id.topPanel : I
    //   56: invokevirtual findViewById : (I)Landroid/view/View;
    //   59: astore #5
    //   61: aload_1
    //   62: getstatic android/support/v7/appcompat/R$id.contentPanel : I
    //   65: invokevirtual findViewById : (I)Landroid/view/View;
    //   68: astore #6
    //   70: aload_1
    //   71: getstatic android/support/v7/appcompat/R$id.buttonPanel : I
    //   74: invokevirtual findViewById : (I)Landroid/view/View;
    //   77: astore #7
    //   79: aload_0
    //   80: aload #5
    //   82: aload_2
    //   83: invokespecial resolvePanel : (Landroid/view/View;Landroid/view/View;)Landroid/view/ViewGroup;
    //   86: astore_2
    //   87: aload_0
    //   88: aload #6
    //   90: aload_3
    //   91: invokespecial resolvePanel : (Landroid/view/View;Landroid/view/View;)Landroid/view/ViewGroup;
    //   94: astore_3
    //   95: aload_0
    //   96: aload #7
    //   98: aload #4
    //   100: invokespecial resolvePanel : (Landroid/view/View;Landroid/view/View;)Landroid/view/ViewGroup;
    //   103: astore #4
    //   105: aload_0
    //   106: aload_3
    //   107: invokespecial setupContent : (Landroid/view/ViewGroup;)V
    //   110: aload_0
    //   111: aload #4
    //   113: invokespecial setupButtons : (Landroid/view/ViewGroup;)V
    //   116: aload_0
    //   117: aload_2
    //   118: invokespecial setupTitle : (Landroid/view/ViewGroup;)V
    //   121: aload_1
    //   122: ifnull -> 403
    //   125: aload_1
    //   126: invokevirtual getVisibility : ()I
    //   129: bipush #8
    //   131: if_icmpeq -> 403
    //   134: iconst_1
    //   135: istore #8
    //   137: aload_2
    //   138: ifnull -> 409
    //   141: aload_2
    //   142: invokevirtual getVisibility : ()I
    //   145: bipush #8
    //   147: if_icmpeq -> 409
    //   150: iconst_1
    //   151: istore #9
    //   153: aload #4
    //   155: ifnull -> 415
    //   158: aload #4
    //   160: invokevirtual getVisibility : ()I
    //   163: bipush #8
    //   165: if_icmpeq -> 415
    //   168: iconst_1
    //   169: istore #10
    //   171: iload #10
    //   173: ifne -> 200
    //   176: aload_3
    //   177: ifnull -> 200
    //   180: aload_3
    //   181: getstatic android/support/v7/appcompat/R$id.textSpacerNoButtons : I
    //   184: invokevirtual findViewById : (I)Landroid/view/View;
    //   187: astore #4
    //   189: aload #4
    //   191: ifnull -> 200
    //   194: aload #4
    //   196: iconst_0
    //   197: invokevirtual setVisibility : (I)V
    //   200: iload #9
    //   202: ifeq -> 421
    //   205: aload_0
    //   206: getfield mScrollView : Landroid/support/v4/widget/NestedScrollView;
    //   209: ifnull -> 220
    //   212: aload_0
    //   213: getfield mScrollView : Landroid/support/v4/widget/NestedScrollView;
    //   216: iconst_1
    //   217: invokevirtual setClipToPadding : (Z)V
    //   220: aconst_null
    //   221: astore_1
    //   222: aload_0
    //   223: getfield mMessage : Ljava/lang/CharSequence;
    //   226: ifnonnull -> 244
    //   229: aload_0
    //   230: getfield mListView : Landroid/widget/ListView;
    //   233: ifnonnull -> 244
    //   236: aload_1
    //   237: astore #4
    //   239: iload #8
    //   241: ifeq -> 261
    //   244: aload_1
    //   245: astore #4
    //   247: iload #8
    //   249: ifne -> 261
    //   252: aload_2
    //   253: getstatic android/support/v7/appcompat/R$id.titleDividerNoCustom : I
    //   256: invokevirtual findViewById : (I)Landroid/view/View;
    //   259: astore #4
    //   261: aload #4
    //   263: ifnull -> 272
    //   266: aload #4
    //   268: iconst_0
    //   269: invokevirtual setVisibility : (I)V
    //   272: aload_0
    //   273: getfield mListView : Landroid/widget/ListView;
    //   276: instanceof android/support/v7/app/AlertController$RecycleListView
    //   279: ifeq -> 296
    //   282: aload_0
    //   283: getfield mListView : Landroid/widget/ListView;
    //   286: checkcast android/support/v7/app/AlertController$RecycleListView
    //   289: iload #9
    //   291: iload #10
    //   293: invokevirtual setHasDecor : (ZZ)V
    //   296: iload #8
    //   298: ifne -> 348
    //   301: aload_0
    //   302: getfield mListView : Landroid/widget/ListView;
    //   305: ifnull -> 448
    //   308: aload_0
    //   309: getfield mListView : Landroid/widget/ListView;
    //   312: astore #4
    //   314: aload #4
    //   316: ifnull -> 348
    //   319: iload #9
    //   321: ifeq -> 457
    //   324: iconst_1
    //   325: istore #8
    //   327: iload #10
    //   329: ifeq -> 463
    //   332: iconst_2
    //   333: istore #11
    //   335: aload_0
    //   336: aload_3
    //   337: aload #4
    //   339: iload #8
    //   341: iload #11
    //   343: ior
    //   344: iconst_3
    //   345: invokespecial setScrollIndicators : (Landroid/view/ViewGroup;Landroid/view/View;II)V
    //   348: aload_0
    //   349: getfield mListView : Landroid/widget/ListView;
    //   352: astore #4
    //   354: aload #4
    //   356: ifnull -> 402
    //   359: aload_0
    //   360: getfield mAdapter : Landroid/widget/ListAdapter;
    //   363: ifnull -> 402
    //   366: aload #4
    //   368: aload_0
    //   369: getfield mAdapter : Landroid/widget/ListAdapter;
    //   372: invokevirtual setAdapter : (Landroid/widget/ListAdapter;)V
    //   375: aload_0
    //   376: getfield mCheckedItem : I
    //   379: istore #8
    //   381: iload #8
    //   383: iconst_m1
    //   384: if_icmple -> 402
    //   387: aload #4
    //   389: iload #8
    //   391: iconst_1
    //   392: invokevirtual setItemChecked : (IZ)V
    //   395: aload #4
    //   397: iload #8
    //   399: invokevirtual setSelection : (I)V
    //   402: return
    //   403: iconst_0
    //   404: istore #8
    //   406: goto -> 137
    //   409: iconst_0
    //   410: istore #9
    //   412: goto -> 153
    //   415: iconst_0
    //   416: istore #10
    //   418: goto -> 171
    //   421: aload_3
    //   422: ifnull -> 272
    //   425: aload_3
    //   426: getstatic android/support/v7/appcompat/R$id.textSpacerNoTitle : I
    //   429: invokevirtual findViewById : (I)Landroid/view/View;
    //   432: astore #4
    //   434: aload #4
    //   436: ifnull -> 272
    //   439: aload #4
    //   441: iconst_0
    //   442: invokevirtual setVisibility : (I)V
    //   445: goto -> 272
    //   448: aload_0
    //   449: getfield mScrollView : Landroid/support/v4/widget/NestedScrollView;
    //   452: astore #4
    //   454: goto -> 314
    //   457: iconst_0
    //   458: istore #8
    //   460: goto -> 327
    //   463: iconst_0
    //   464: istore #11
    //   466: goto -> 335
  }
  
  private static boolean shouldCenterSingleButton(Context paramContext) {
    boolean bool = true;
    TypedValue typedValue = new TypedValue();
    paramContext.getTheme().resolveAttribute(R.attr.alertDialogCenterButtons, typedValue, true);
    if (typedValue.data == 0)
      bool = false; 
    return bool;
  }
  
  public Button getButton(int paramInt) {
    switch (paramInt) {
      default:
        return null;
      case -1:
        return this.mButtonPositive;
      case -2:
        return this.mButtonNegative;
      case -3:
        break;
    } 
    return this.mButtonNeutral;
  }
  
  public int getIconAttributeResId(int paramInt) {
    TypedValue typedValue = new TypedValue();
    this.mContext.getTheme().resolveAttribute(paramInt, typedValue, true);
    return typedValue.resourceId;
  }
  
  public ListView getListView() {
    return this.mListView;
  }
  
  public void installContent() {
    int i = selectContentView();
    this.mDialog.setContentView(i);
    setupView();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    return (this.mScrollView != null && this.mScrollView.executeKeyEvent(paramKeyEvent));
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    return (this.mScrollView != null && this.mScrollView.executeKeyEvent(paramKeyEvent));
  }
  
  public void setButton(int paramInt, CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener, Message paramMessage) {
    Message message = paramMessage;
    if (paramMessage == null) {
      message = paramMessage;
      if (paramOnClickListener != null)
        message = this.mHandler.obtainMessage(paramInt, paramOnClickListener); 
    } 
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("Button does not exist");
      case -1:
        this.mButtonPositiveText = paramCharSequence;
        this.mButtonPositiveMessage = message;
        return;
      case -2:
        this.mButtonNegativeText = paramCharSequence;
        this.mButtonNegativeMessage = message;
        return;
      case -3:
        break;
    } 
    this.mButtonNeutralText = paramCharSequence;
    this.mButtonNeutralMessage = message;
  }
  
  public void setButtonPanelLayoutHint(int paramInt) {
    this.mButtonPanelLayoutHint = paramInt;
  }
  
  public void setCustomTitle(View paramView) {
    this.mCustomTitleView = paramView;
  }
  
  public void setIcon(int paramInt) {
    this.mIcon = null;
    this.mIconId = paramInt;
    if (this.mIconView != null) {
      if (paramInt != 0) {
        this.mIconView.setVisibility(0);
        this.mIconView.setImageResource(this.mIconId);
        return;
      } 
    } else {
      return;
    } 
    this.mIconView.setVisibility(8);
  }
  
  public void setIcon(Drawable paramDrawable) {
    this.mIcon = paramDrawable;
    this.mIconId = 0;
    if (this.mIconView != null) {
      if (paramDrawable != null) {
        this.mIconView.setVisibility(0);
        this.mIconView.setImageDrawable(paramDrawable);
        return;
      } 
    } else {
      return;
    } 
    this.mIconView.setVisibility(8);
  }
  
  public void setMessage(CharSequence paramCharSequence) {
    this.mMessage = paramCharSequence;
    if (this.mMessageView != null)
      this.mMessageView.setText(paramCharSequence); 
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    this.mTitle = paramCharSequence;
    if (this.mTitleView != null)
      this.mTitleView.setText(paramCharSequence); 
  }
  
  public void setView(int paramInt) {
    this.mView = null;
    this.mViewLayoutResId = paramInt;
    this.mViewSpacingSpecified = false;
  }
  
  public void setView(View paramView) {
    this.mView = paramView;
    this.mViewLayoutResId = 0;
    this.mViewSpacingSpecified = false;
  }
  
  public void setView(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mView = paramView;
    this.mViewLayoutResId = 0;
    this.mViewSpacingSpecified = true;
    this.mViewSpacingLeft = paramInt1;
    this.mViewSpacingTop = paramInt2;
    this.mViewSpacingRight = paramInt3;
    this.mViewSpacingBottom = paramInt4;
  }
  
  public static class AlertParams {
    public ListAdapter mAdapter;
    
    public boolean mCancelable;
    
    public int mCheckedItem = -1;
    
    public boolean[] mCheckedItems;
    
    public final Context mContext;
    
    public Cursor mCursor;
    
    public View mCustomTitleView;
    
    public boolean mForceInverseBackground;
    
    public Drawable mIcon;
    
    public int mIconAttrId = 0;
    
    public int mIconId = 0;
    
    public final LayoutInflater mInflater;
    
    public String mIsCheckedColumn;
    
    public boolean mIsMultiChoice;
    
    public boolean mIsSingleChoice;
    
    public CharSequence[] mItems;
    
    public String mLabelColumn;
    
    public CharSequence mMessage;
    
    public DialogInterface.OnClickListener mNegativeButtonListener;
    
    public CharSequence mNegativeButtonText;
    
    public DialogInterface.OnClickListener mNeutralButtonListener;
    
    public CharSequence mNeutralButtonText;
    
    public DialogInterface.OnCancelListener mOnCancelListener;
    
    public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
    
    public DialogInterface.OnClickListener mOnClickListener;
    
    public DialogInterface.OnDismissListener mOnDismissListener;
    
    public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    
    public DialogInterface.OnKeyListener mOnKeyListener;
    
    public OnPrepareListViewListener mOnPrepareListViewListener;
    
    public DialogInterface.OnClickListener mPositiveButtonListener;
    
    public CharSequence mPositiveButtonText;
    
    public boolean mRecycleOnMeasure = true;
    
    public CharSequence mTitle;
    
    public View mView;
    
    public int mViewLayoutResId;
    
    public int mViewSpacingBottom;
    
    public int mViewSpacingLeft;
    
    public int mViewSpacingRight;
    
    public boolean mViewSpacingSpecified = false;
    
    public int mViewSpacingTop;
    
    public AlertParams(Context param1Context) {
      this.mContext = param1Context;
      this.mCancelable = true;
      this.mInflater = (LayoutInflater)param1Context.getSystemService("layout_inflater");
    }
    
    private void createListView(final AlertController dialog) {
      ArrayAdapter<CharSequence> arrayAdapter;
      final AlertController.RecycleListView listView = (AlertController.RecycleListView)this.mInflater.inflate(dialog.mListLayout, null);
      if (this.mIsMultiChoice) {
        if (this.mCursor == null) {
          arrayAdapter = new ArrayAdapter<CharSequence>(this.mContext, dialog.mMultiChoiceItemLayout, 16908308, this.mItems) {
              public View getView(int param2Int, View param2View, ViewGroup param2ViewGroup) {
                param2View = super.getView(param2Int, param2View, param2ViewGroup);
                if (AlertController.AlertParams.this.mCheckedItems != null && AlertController.AlertParams.this.mCheckedItems[param2Int])
                  listView.setItemChecked(param2Int, true); 
                return param2View;
              }
            };
        } else {
          CursorAdapter cursorAdapter = new CursorAdapter(this.mContext, this.mCursor, false) {
              private final int mIsCheckedIndex;
              
              private final int mLabelIndex;
              
              public void bindView(View param2View, Context param2Context, Cursor param2Cursor) {
                boolean bool = true;
                ((CheckedTextView)param2View.findViewById(16908308)).setText(param2Cursor.getString(this.mLabelIndex));
                AlertController.RecycleListView recycleListView = listView;
                int i = param2Cursor.getPosition();
                if (param2Cursor.getInt(this.mIsCheckedIndex) != 1)
                  bool = false; 
                recycleListView.setItemChecked(i, bool);
              }
              
              public View newView(Context param2Context, Cursor param2Cursor, ViewGroup param2ViewGroup) {
                return AlertController.AlertParams.this.mInflater.inflate(dialog.mMultiChoiceItemLayout, param2ViewGroup, false);
              }
            };
        } 
      } else {
        int i;
        if (this.mIsSingleChoice) {
          i = dialog.mSingleChoiceItemLayout;
        } else {
          i = dialog.mListItemLayout;
        } 
        if (this.mCursor != null) {
          SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this.mContext, i, this.mCursor, new String[] { this.mLabelColumn }, new int[] { 16908308 });
        } else if (this.mAdapter != null) {
          ListAdapter listAdapter = this.mAdapter;
        } else {
          arrayAdapter = new AlertController.CheckedItemAdapter(this.mContext, i, 16908308, this.mItems);
        } 
      } 
      if (this.mOnPrepareListViewListener != null)
        this.mOnPrepareListViewListener.onPrepareListView(recycleListView); 
      dialog.mAdapter = (ListAdapter)arrayAdapter;
      dialog.mCheckedItem = this.mCheckedItem;
      if (this.mOnClickListener != null) {
        recycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              public void onItemClick(AdapterView<?> param2AdapterView, View param2View, int param2Int, long param2Long) {
                AlertController.AlertParams.this.mOnClickListener.onClick((DialogInterface)dialog.mDialog, param2Int);
                if (!AlertController.AlertParams.this.mIsSingleChoice)
                  dialog.mDialog.dismiss(); 
              }
            });
      } else if (this.mOnCheckboxClickListener != null) {
        recycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              public void onItemClick(AdapterView<?> param2AdapterView, View param2View, int param2Int, long param2Long) {
                if (AlertController.AlertParams.this.mCheckedItems != null)
                  AlertController.AlertParams.this.mCheckedItems[param2Int] = listView.isItemChecked(param2Int); 
                AlertController.AlertParams.this.mOnCheckboxClickListener.onClick((DialogInterface)dialog.mDialog, param2Int, listView.isItemChecked(param2Int));
              }
            });
      } 
      if (this.mOnItemSelectedListener != null)
        recycleListView.setOnItemSelectedListener(this.mOnItemSelectedListener); 
      if (this.mIsSingleChoice) {
        recycleListView.setChoiceMode(1);
      } else if (this.mIsMultiChoice) {
        recycleListView.setChoiceMode(2);
      } 
      dialog.mListView = recycleListView;
    }
    
    public void apply(AlertController param1AlertController) {
      if (this.mCustomTitleView != null) {
        param1AlertController.setCustomTitle(this.mCustomTitleView);
      } else {
        if (this.mTitle != null)
          param1AlertController.setTitle(this.mTitle); 
        if (this.mIcon != null)
          param1AlertController.setIcon(this.mIcon); 
        if (this.mIconId != 0)
          param1AlertController.setIcon(this.mIconId); 
        if (this.mIconAttrId != 0)
          param1AlertController.setIcon(param1AlertController.getIconAttributeResId(this.mIconAttrId)); 
      } 
      if (this.mMessage != null)
        param1AlertController.setMessage(this.mMessage); 
      if (this.mPositiveButtonText != null)
        param1AlertController.setButton(-1, this.mPositiveButtonText, this.mPositiveButtonListener, null); 
      if (this.mNegativeButtonText != null)
        param1AlertController.setButton(-2, this.mNegativeButtonText, this.mNegativeButtonListener, null); 
      if (this.mNeutralButtonText != null)
        param1AlertController.setButton(-3, this.mNeutralButtonText, this.mNeutralButtonListener, null); 
      if (this.mItems != null || this.mCursor != null || this.mAdapter != null)
        createListView(param1AlertController); 
      if (this.mView != null) {
        if (this.mViewSpacingSpecified) {
          param1AlertController.setView(this.mView, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
          return;
        } 
        param1AlertController.setView(this.mView);
        return;
      } 
      if (this.mViewLayoutResId != 0)
        param1AlertController.setView(this.mViewLayoutResId); 
    }
    
    public static interface OnPrepareListViewListener {
      void onPrepareListView(ListView param2ListView);
    }
  }
  
  class null extends ArrayAdapter<CharSequence> {
    null(Context param1Context, int param1Int1, int param1Int2, CharSequence[] param1ArrayOfCharSequence) {
      super(param1Context, param1Int1, param1Int2, (Object[])param1ArrayOfCharSequence);
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      param1View = super.getView(param1Int, param1View, param1ViewGroup);
      if (this.this$0.mCheckedItems != null && this.this$0.mCheckedItems[param1Int])
        listView.setItemChecked(param1Int, true); 
      return param1View;
    }
  }
  
  class null extends CursorAdapter {
    private final int mIsCheckedIndex;
    
    private final int mLabelIndex;
    
    null(Context param1Context, Cursor param1Cursor, boolean param1Boolean) {
      super(param1Context, param1Cursor, param1Boolean);
      Cursor cursor = getCursor();
      this.mLabelIndex = cursor.getColumnIndexOrThrow(this.this$0.mLabelColumn);
      this.mIsCheckedIndex = cursor.getColumnIndexOrThrow(this.this$0.mIsCheckedColumn);
    }
    
    public void bindView(View param1View, Context param1Context, Cursor param1Cursor) {
      boolean bool = true;
      ((CheckedTextView)param1View.findViewById(16908308)).setText(param1Cursor.getString(this.mLabelIndex));
      AlertController.RecycleListView recycleListView = listView;
      int i = param1Cursor.getPosition();
      if (param1Cursor.getInt(this.mIsCheckedIndex) != 1)
        bool = false; 
      recycleListView.setItemChecked(i, bool);
    }
    
    public View newView(Context param1Context, Cursor param1Cursor, ViewGroup param1ViewGroup) {
      return this.this$0.mInflater.inflate(dialog.mMultiChoiceItemLayout, param1ViewGroup, false);
    }
  }
  
  class null implements AdapterView.OnItemClickListener {
    public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      this.this$0.mOnClickListener.onClick((DialogInterface)dialog.mDialog, param1Int);
      if (!this.this$0.mIsSingleChoice)
        dialog.mDialog.dismiss(); 
    }
  }
  
  class null implements AdapterView.OnItemClickListener {
    public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      if (this.this$0.mCheckedItems != null)
        this.this$0.mCheckedItems[param1Int] = listView.isItemChecked(param1Int); 
      this.this$0.mOnCheckboxClickListener.onClick((DialogInterface)dialog.mDialog, param1Int, listView.isItemChecked(param1Int));
    }
  }
  
  public static interface OnPrepareListViewListener {
    void onPrepareListView(ListView param1ListView);
  }
  
  private static final class ButtonHandler extends Handler {
    private static final int MSG_DISMISS_DIALOG = 1;
    
    private WeakReference<DialogInterface> mDialog;
    
    public ButtonHandler(DialogInterface param1DialogInterface) {
      this.mDialog = new WeakReference<DialogInterface>(param1DialogInterface);
    }
    
    public void handleMessage(Message param1Message) {
      switch (param1Message.what) {
        default:
          return;
        case -3:
        case -2:
        case -1:
          ((DialogInterface.OnClickListener)param1Message.obj).onClick(this.mDialog.get(), param1Message.what);
        case 1:
          break;
      } 
      ((DialogInterface)param1Message.obj).dismiss();
    }
  }
  
  private static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {
    public CheckedItemAdapter(Context param1Context, int param1Int1, int param1Int2, CharSequence[] param1ArrayOfCharSequence) {
      super(param1Context, param1Int1, param1Int2, (Object[])param1ArrayOfCharSequence);
    }
    
    public long getItemId(int param1Int) {
      return param1Int;
    }
    
    public boolean hasStableIds() {
      return true;
    }
  }
  
  public static class RecycleListView extends ListView {
    private final int mPaddingBottomNoButtons;
    
    private final int mPaddingTopNoTitle;
    
    public RecycleListView(Context param1Context) {
      this(param1Context, (AttributeSet)null);
    }
    
    public RecycleListView(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.RecycleListView);
      this.mPaddingBottomNoButtons = typedArray.getDimensionPixelOffset(R.styleable.RecycleListView_paddingBottomNoButtons, -1);
      this.mPaddingTopNoTitle = typedArray.getDimensionPixelOffset(R.styleable.RecycleListView_paddingTopNoTitle, -1);
    }
    
    public void setHasDecor(boolean param1Boolean1, boolean param1Boolean2) {
      if (!param1Boolean2 || !param1Boolean1) {
        int j;
        int m;
        int i = getPaddingLeft();
        if (param1Boolean1) {
          j = getPaddingTop();
        } else {
          j = this.mPaddingTopNoTitle;
        } 
        int k = getPaddingRight();
        if (param1Boolean2) {
          m = getPaddingBottom();
        } else {
          m = this.mPaddingBottomNoButtons;
        } 
        setPadding(i, j, k, m);
      } 
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/app/AlertController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */