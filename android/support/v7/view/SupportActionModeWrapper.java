package android.support.v7.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.view.menu.MenuWrapperFactory;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;

@TargetApi(11)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SupportActionModeWrapper extends ActionMode {
  final Context mContext;
  
  final ActionMode mWrappedObject;
  
  public SupportActionModeWrapper(Context paramContext, ActionMode paramActionMode) {
    this.mContext = paramContext;
    this.mWrappedObject = paramActionMode;
  }
  
  public void finish() {
    this.mWrappedObject.finish();
  }
  
  public View getCustomView() {
    return this.mWrappedObject.getCustomView();
  }
  
  public Menu getMenu() {
    return MenuWrapperFactory.wrapSupportMenu(this.mContext, (SupportMenu)this.mWrappedObject.getMenu());
  }
  
  public MenuInflater getMenuInflater() {
    return this.mWrappedObject.getMenuInflater();
  }
  
  public CharSequence getSubtitle() {
    return this.mWrappedObject.getSubtitle();
  }
  
  public Object getTag() {
    return this.mWrappedObject.getTag();
  }
  
  public CharSequence getTitle() {
    return this.mWrappedObject.getTitle();
  }
  
  public boolean getTitleOptionalHint() {
    return this.mWrappedObject.getTitleOptionalHint();
  }
  
  public void invalidate() {
    this.mWrappedObject.invalidate();
  }
  
  public boolean isTitleOptional() {
    return this.mWrappedObject.isTitleOptional();
  }
  
  public void setCustomView(View paramView) {
    this.mWrappedObject.setCustomView(paramView);
  }
  
  public void setSubtitle(int paramInt) {
    this.mWrappedObject.setSubtitle(paramInt);
  }
  
  public void setSubtitle(CharSequence paramCharSequence) {
    this.mWrappedObject.setSubtitle(paramCharSequence);
  }
  
  public void setTag(Object paramObject) {
    this.mWrappedObject.setTag(paramObject);
  }
  
  public void setTitle(int paramInt) {
    this.mWrappedObject.setTitle(paramInt);
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    this.mWrappedObject.setTitle(paramCharSequence);
  }
  
  public void setTitleOptionalHint(boolean paramBoolean) {
    this.mWrappedObject.setTitleOptionalHint(paramBoolean);
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static class CallbackWrapper implements ActionMode.Callback {
    final ArrayList<SupportActionModeWrapper> mActionModes;
    
    final Context mContext;
    
    final SimpleArrayMap<Menu, Menu> mMenus;
    
    final ActionMode.Callback mWrappedCallback;
    
    public CallbackWrapper(Context param1Context, ActionMode.Callback param1Callback) {
      this.mContext = param1Context;
      this.mWrappedCallback = param1Callback;
      this.mActionModes = new ArrayList<SupportActionModeWrapper>();
      this.mMenus = new SimpleArrayMap();
    }
    
    private Menu getMenuWrapper(Menu param1Menu) {
      Menu menu1 = (Menu)this.mMenus.get(param1Menu);
      Menu menu2 = menu1;
      if (menu1 == null) {
        menu2 = MenuWrapperFactory.wrapSupportMenu(this.mContext, (SupportMenu)param1Menu);
        this.mMenus.put(param1Menu, menu2);
      } 
      return menu2;
    }
    
    public ActionMode getActionModeWrapper(ActionMode param1ActionMode) {
      // Byte code:
      //   0: iconst_0
      //   1: istore_2
      //   2: aload_0
      //   3: getfield mActionModes : Ljava/util/ArrayList;
      //   6: invokevirtual size : ()I
      //   9: istore_3
      //   10: iload_2
      //   11: iload_3
      //   12: if_icmpge -> 53
      //   15: aload_0
      //   16: getfield mActionModes : Ljava/util/ArrayList;
      //   19: iload_2
      //   20: invokevirtual get : (I)Ljava/lang/Object;
      //   23: checkcast android/support/v7/view/SupportActionModeWrapper
      //   26: astore #4
      //   28: aload #4
      //   30: ifnull -> 47
      //   33: aload #4
      //   35: getfield mWrappedObject : Landroid/support/v7/view/ActionMode;
      //   38: aload_1
      //   39: if_acmpne -> 47
      //   42: aload #4
      //   44: astore_1
      //   45: aload_1
      //   46: areturn
      //   47: iinc #2, 1
      //   50: goto -> 10
      //   53: new android/support/v7/view/SupportActionModeWrapper
      //   56: dup
      //   57: aload_0
      //   58: getfield mContext : Landroid/content/Context;
      //   61: aload_1
      //   62: invokespecial <init> : (Landroid/content/Context;Landroid/support/v7/view/ActionMode;)V
      //   65: astore_1
      //   66: aload_0
      //   67: getfield mActionModes : Ljava/util/ArrayList;
      //   70: aload_1
      //   71: invokevirtual add : (Ljava/lang/Object;)Z
      //   74: pop
      //   75: goto -> 45
    }
    
    public boolean onActionItemClicked(ActionMode param1ActionMode, MenuItem param1MenuItem) {
      return this.mWrappedCallback.onActionItemClicked(getActionModeWrapper(param1ActionMode), MenuWrapperFactory.wrapSupportMenuItem(this.mContext, (SupportMenuItem)param1MenuItem));
    }
    
    public boolean onCreateActionMode(ActionMode param1ActionMode, Menu param1Menu) {
      return this.mWrappedCallback.onCreateActionMode(getActionModeWrapper(param1ActionMode), getMenuWrapper(param1Menu));
    }
    
    public void onDestroyActionMode(ActionMode param1ActionMode) {
      this.mWrappedCallback.onDestroyActionMode(getActionModeWrapper(param1ActionMode));
    }
    
    public boolean onPrepareActionMode(ActionMode param1ActionMode, Menu param1Menu) {
      return this.mWrappedCallback.onPrepareActionMode(getActionModeWrapper(param1ActionMode), getMenuWrapper(param1Menu));
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v7/view/SupportActionModeWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */