package app.lib.pullToRefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PullToRefreshListView extends PullToRefreshAdapterViewBase<ListView> {
  private LoadingLayout mFooterLoading;
  
  private LoadingLayout mHeaderLoading;
  
  private boolean mListViewExtrasEnabled;
  
  private FrameLayout mLvFooterLoadingFrame;
  
  public PullToRefreshListView(Context paramContext) {
    super(paramContext);
  }
  
  public PullToRefreshListView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public PullToRefreshListView(Context paramContext, PullToRefreshBase.Mode paramMode) {
    super(paramContext, paramMode);
  }
  
  public PullToRefreshListView(Context paramContext, PullToRefreshBase.Mode paramMode, PullToRefreshBase.AnimationStyle paramAnimationStyle) {
    super(paramContext, paramMode, paramAnimationStyle);
  }
  
  protected ListView createListView(Context paramContext, AttributeSet paramAttributeSet) {
    return (Build.VERSION.SDK_INT >= 9) ? new InternalListViewSDK9(paramContext, paramAttributeSet) : new InternalListView(paramContext, paramAttributeSet);
  }
  
  protected LoadingLayoutProxy createLoadingLayoutProxy(boolean paramBoolean1, boolean paramBoolean2) {
    LoadingLayoutProxy loadingLayoutProxy = super.createLoadingLayoutProxy(paramBoolean1, paramBoolean2);
    if (this.mListViewExtrasEnabled) {
      PullToRefreshBase.Mode mode = getMode();
      if (paramBoolean1 && mode.showHeaderLoadingLayout())
        loadingLayoutProxy.addLayout(this.mHeaderLoading); 
      if (paramBoolean2 && mode.showFooterLoadingLayout())
        loadingLayoutProxy.addLayout(this.mFooterLoading); 
    } 
    return loadingLayoutProxy;
  }
  
  protected ListView createRefreshableView(Context paramContext, AttributeSet paramAttributeSet) {
    ListView listView = createListView(paramContext, paramAttributeSet);
    listView.setId(16908298);
    return listView;
  }
  
  public final PullToRefreshBase.Orientation getPullToRefreshScrollDirection() {
    return PullToRefreshBase.Orientation.VERTICAL;
  }
  
  protected void handleStyledAttributes(TypedArray paramTypedArray) {
    super.handleStyledAttributes(paramTypedArray);
    this.mListViewExtrasEnabled = paramTypedArray.getBoolean(14, true);
    if (this.mListViewExtrasEnabled) {
      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2, 1);
      FrameLayout frameLayout = new FrameLayout(getContext());
      this.mHeaderLoading = createLoadingLayout(getContext(), PullToRefreshBase.Mode.PULL_FROM_START, paramTypedArray);
      this.mHeaderLoading.setVisibility(8);
      frameLayout.addView((View)this.mHeaderLoading, (ViewGroup.LayoutParams)layoutParams);
      this.mRefreshableView.addHeaderView((View)frameLayout, null, false);
      this.mLvFooterLoadingFrame = new FrameLayout(getContext());
      this.mFooterLoading = createLoadingLayout(getContext(), PullToRefreshBase.Mode.PULL_FROM_END, paramTypedArray);
      this.mFooterLoading.setVisibility(8);
      this.mLvFooterLoadingFrame.addView((View)this.mFooterLoading, (ViewGroup.LayoutParams)layoutParams);
      if (!paramTypedArray.hasValue(13))
        setScrollingWhileRefreshingEnabled(true); 
    } 
  }
  
  protected void onRefreshing(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mRefreshableView : Landroid/view/View;
    //   4: checkcast android/widget/ListView
    //   7: invokevirtual getAdapter : ()Landroid/widget/ListAdapter;
    //   10: astore_2
    //   11: aload_0
    //   12: getfield mListViewExtrasEnabled : Z
    //   15: ifeq -> 38
    //   18: aload_0
    //   19: invokevirtual getShowViewWhileRefreshing : ()Z
    //   22: ifeq -> 38
    //   25: aload_2
    //   26: ifnull -> 38
    //   29: aload_2
    //   30: invokeinterface isEmpty : ()Z
    //   35: ifeq -> 44
    //   38: aload_0
    //   39: iload_1
    //   40: invokespecial onRefreshing : (Z)V
    //   43: return
    //   44: aload_0
    //   45: iconst_0
    //   46: invokespecial onRefreshing : (Z)V
    //   49: getstatic app/lib/pullToRefresh/PullToRefreshListView$1.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Mode : [I
    //   52: aload_0
    //   53: invokevirtual getCurrentMode : ()Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   56: invokevirtual ordinal : ()I
    //   59: iaload
    //   60: tableswitch default -> 84, 1 -> 172, 2 -> 172
    //   84: aload_0
    //   85: invokevirtual getHeaderLayout : ()Lapp/lib/pullToRefresh/LoadingLayout;
    //   88: astore_2
    //   89: aload_0
    //   90: getfield mHeaderLoading : Lapp/lib/pullToRefresh/LoadingLayout;
    //   93: astore_3
    //   94: aload_0
    //   95: getfield mFooterLoading : Lapp/lib/pullToRefresh/LoadingLayout;
    //   98: astore #4
    //   100: iconst_0
    //   101: istore #5
    //   103: aload_0
    //   104: invokevirtual getScrollY : ()I
    //   107: aload_0
    //   108: invokevirtual getHeaderSize : ()I
    //   111: iadd
    //   112: istore #6
    //   114: aload_2
    //   115: invokevirtual reset : ()V
    //   118: aload_2
    //   119: invokevirtual hideAllViews : ()V
    //   122: aload #4
    //   124: bipush #8
    //   126: invokevirtual setVisibility : (I)V
    //   129: aload_3
    //   130: iconst_0
    //   131: invokevirtual setVisibility : (I)V
    //   134: aload_3
    //   135: invokevirtual refreshing : ()V
    //   138: iload_1
    //   139: ifeq -> 43
    //   142: aload_0
    //   143: invokevirtual disableLoadingLayoutVisibilityChanges : ()V
    //   146: aload_0
    //   147: iload #6
    //   149: invokevirtual setHeaderScroll : (I)V
    //   152: aload_0
    //   153: getfield mRefreshableView : Landroid/view/View;
    //   156: checkcast android/widget/ListView
    //   159: iload #5
    //   161: invokevirtual setSelection : (I)V
    //   164: aload_0
    //   165: iconst_0
    //   166: invokevirtual smoothScrollTo : (I)V
    //   169: goto -> 43
    //   172: aload_0
    //   173: invokevirtual getFooterLayout : ()Lapp/lib/pullToRefresh/LoadingLayout;
    //   176: astore_2
    //   177: aload_0
    //   178: getfield mFooterLoading : Lapp/lib/pullToRefresh/LoadingLayout;
    //   181: astore_3
    //   182: aload_0
    //   183: getfield mHeaderLoading : Lapp/lib/pullToRefresh/LoadingLayout;
    //   186: astore #4
    //   188: aload_0
    //   189: getfield mRefreshableView : Landroid/view/View;
    //   192: checkcast android/widget/ListView
    //   195: invokevirtual getCount : ()I
    //   198: iconst_1
    //   199: isub
    //   200: istore #5
    //   202: aload_0
    //   203: invokevirtual getScrollY : ()I
    //   206: aload_0
    //   207: invokevirtual getFooterSize : ()I
    //   210: isub
    //   211: istore #6
    //   213: goto -> 114
  }
  
  protected void onReset() {
    // Byte code:
    //   0: iconst_1
    //   1: istore_1
    //   2: iconst_1
    //   3: istore_2
    //   4: aload_0
    //   5: getfield mListViewExtrasEnabled : Z
    //   8: ifne -> 16
    //   11: aload_0
    //   12: invokespecial onReset : ()V
    //   15: return
    //   16: getstatic app/lib/pullToRefresh/PullToRefreshListView$1.$SwitchMap$app$lib$pullToRefresh$PullToRefreshBase$Mode : [I
    //   19: aload_0
    //   20: invokevirtual getCurrentMode : ()Lapp/lib/pullToRefresh/PullToRefreshBase$Mode;
    //   23: invokevirtual ordinal : ()I
    //   26: iaload
    //   27: tableswitch default -> 48, 1 -> 146, 2 -> 146
    //   48: aload_0
    //   49: invokevirtual getHeaderLayout : ()Lapp/lib/pullToRefresh/LoadingLayout;
    //   52: astore_3
    //   53: aload_0
    //   54: getfield mHeaderLoading : Lapp/lib/pullToRefresh/LoadingLayout;
    //   57: astore #4
    //   59: aload_0
    //   60: invokevirtual getHeaderSize : ()I
    //   63: ineg
    //   64: istore #5
    //   66: iconst_0
    //   67: istore #6
    //   69: aload_0
    //   70: getfield mRefreshableView : Landroid/view/View;
    //   73: checkcast android/widget/ListView
    //   76: invokevirtual getFirstVisiblePosition : ()I
    //   79: iconst_0
    //   80: isub
    //   81: invokestatic abs : (I)I
    //   84: iconst_1
    //   85: if_icmpgt -> 207
    //   88: aload #4
    //   90: invokevirtual getVisibility : ()I
    //   93: ifne -> 139
    //   96: aload_3
    //   97: invokevirtual showInvisibleViews : ()V
    //   100: aload #4
    //   102: bipush #8
    //   104: invokevirtual setVisibility : (I)V
    //   107: iload_2
    //   108: ifeq -> 139
    //   111: aload_0
    //   112: invokevirtual getState : ()Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   115: getstatic app/lib/pullToRefresh/PullToRefreshBase$State.MANUAL_REFRESHING : Lapp/lib/pullToRefresh/PullToRefreshBase$State;
    //   118: if_acmpeq -> 139
    //   121: aload_0
    //   122: getfield mRefreshableView : Landroid/view/View;
    //   125: checkcast android/widget/ListView
    //   128: iload #6
    //   130: invokevirtual setSelection : (I)V
    //   133: aload_0
    //   134: iload #5
    //   136: invokevirtual setHeaderScroll : (I)V
    //   139: aload_0
    //   140: invokespecial onReset : ()V
    //   143: goto -> 15
    //   146: aload_0
    //   147: invokevirtual getFooterLayout : ()Lapp/lib/pullToRefresh/LoadingLayout;
    //   150: astore_3
    //   151: aload_0
    //   152: getfield mFooterLoading : Lapp/lib/pullToRefresh/LoadingLayout;
    //   155: astore #4
    //   157: aload_0
    //   158: getfield mRefreshableView : Landroid/view/View;
    //   161: checkcast android/widget/ListView
    //   164: invokevirtual getCount : ()I
    //   167: iconst_1
    //   168: isub
    //   169: istore #6
    //   171: aload_0
    //   172: invokevirtual getFooterSize : ()I
    //   175: istore #5
    //   177: aload_0
    //   178: getfield mRefreshableView : Landroid/view/View;
    //   181: checkcast android/widget/ListView
    //   184: invokevirtual getLastVisiblePosition : ()I
    //   187: iload #6
    //   189: isub
    //   190: invokestatic abs : (I)I
    //   193: iconst_1
    //   194: if_icmpgt -> 202
    //   197: iload_1
    //   198: istore_2
    //   199: goto -> 88
    //   202: iconst_0
    //   203: istore_2
    //   204: goto -> 199
    //   207: iconst_0
    //   208: istore_2
    //   209: goto -> 88
  }
  
  protected class InternalListView extends ListView implements EmptyViewMethodAccessor {
    private boolean mAddedLvFooter = false;
    
    public InternalListView(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    protected void dispatchDraw(Canvas param1Canvas) {
      try {
        super.dispatchDraw(param1Canvas);
      } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
        indexOutOfBoundsException.printStackTrace();
      } 
    }
    
    public boolean dispatchTouchEvent(MotionEvent param1MotionEvent) {
      boolean bool;
      try {
        bool = super.dispatchTouchEvent(param1MotionEvent);
      } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
        indexOutOfBoundsException.printStackTrace();
        bool = false;
      } 
      return bool;
    }
    
    public void setAdapter(ListAdapter param1ListAdapter) {
      if (PullToRefreshListView.this.mLvFooterLoadingFrame != null && !this.mAddedLvFooter) {
        addFooterView((View)PullToRefreshListView.this.mLvFooterLoadingFrame, null, false);
        this.mAddedLvFooter = true;
      } 
      super.setAdapter(param1ListAdapter);
    }
    
    public void setEmptyView(View param1View) {
      PullToRefreshListView.this.setEmptyView(param1View);
    }
    
    public void setEmptyViewInternal(View param1View) {
      super.setEmptyView(param1View);
    }
  }
  
  @TargetApi(9)
  final class InternalListViewSDK9 extends InternalListView {
    public InternalListViewSDK9(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    protected boolean overScrollBy(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8, boolean param1Boolean) {
      boolean bool = super.overScrollBy(param1Int1, param1Int2, param1Int3, param1Int4, param1Int5, param1Int6, param1Int7, param1Int8, param1Boolean);
      OverscrollHelper.overScrollBy(PullToRefreshListView.this, param1Int1, param1Int3, param1Int2, param1Int4, param1Boolean);
      return bool;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/PullToRefreshListView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */