package app.lib.pullToRefresh;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import java.util.HashSet;
import java.util.Iterator;

public class LoadingLayoutProxy implements ILoadingLayout {
  private final HashSet<LoadingLayout> mLoadingLayouts = new HashSet<LoadingLayout>();
  
  public void addLayout(LoadingLayout paramLoadingLayout) {
    if (paramLoadingLayout != null)
      this.mLoadingLayouts.add(paramLoadingLayout); 
  }
  
  public void setLastUpdatedLabel(CharSequence paramCharSequence) {
    Iterator<LoadingLayout> iterator = this.mLoadingLayouts.iterator();
    while (iterator.hasNext())
      ((LoadingLayout)iterator.next()).setLastUpdatedLabel(paramCharSequence); 
  }
  
  public void setLoadingDrawable(Drawable paramDrawable) {
    Iterator<LoadingLayout> iterator = this.mLoadingLayouts.iterator();
    while (iterator.hasNext())
      ((LoadingLayout)iterator.next()).setLoadingDrawable(paramDrawable); 
  }
  
  public void setPullLabel(CharSequence paramCharSequence) {
    Iterator<LoadingLayout> iterator = this.mLoadingLayouts.iterator();
    while (iterator.hasNext())
      ((LoadingLayout)iterator.next()).setPullLabel(paramCharSequence); 
  }
  
  public void setRefreshingLabel(CharSequence paramCharSequence) {
    Iterator<LoadingLayout> iterator = this.mLoadingLayouts.iterator();
    while (iterator.hasNext())
      ((LoadingLayout)iterator.next()).setRefreshingLabel(paramCharSequence); 
  }
  
  public void setReleaseLabel(CharSequence paramCharSequence) {
    Iterator<LoadingLayout> iterator = this.mLoadingLayouts.iterator();
    while (iterator.hasNext())
      ((LoadingLayout)iterator.next()).setReleaseLabel(paramCharSequence); 
  }
  
  public void setTextTypeface(Typeface paramTypeface) {
    Iterator<LoadingLayout> iterator = this.mLoadingLayouts.iterator();
    while (iterator.hasNext())
      ((LoadingLayout)iterator.next()).setTextTypeface(paramTypeface); 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/LoadingLayoutProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */