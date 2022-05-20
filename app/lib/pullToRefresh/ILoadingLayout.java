package app.lib.pullToRefresh;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public interface ILoadingLayout {
  void setLastUpdatedLabel(CharSequence paramCharSequence);
  
  void setLoadingDrawable(Drawable paramDrawable);
  
  void setPullLabel(CharSequence paramCharSequence);
  
  void setRefreshingLabel(CharSequence paramCharSequence);
  
  void setReleaseLabel(CharSequence paramCharSequence);
  
  void setTextTypeface(Typeface paramTypeface);
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/lib/pullToRefresh/ILoadingLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */