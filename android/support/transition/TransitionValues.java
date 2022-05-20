package android.support.transition;

import android.view.View;
import java.util.HashMap;
import java.util.Map;

public class TransitionValues {
  public final Map<String, Object> values = new HashMap<String, Object>();
  
  public View view;
  
  public boolean equals(Object paramObject) {
    return (paramObject instanceof TransitionValues && this.view == ((TransitionValues)paramObject).view && this.values.equals(((TransitionValues)paramObject).values));
  }
  
  public int hashCode() {
    return this.view.hashCode() * 31 + this.values.hashCode();
  }
  
  public String toString() {
    String str = "TransitionValues@" + Integer.toHexString(hashCode()) + ":\n";
    str = str + "    view = " + this.view + "\n";
    str = str + "    values:";
    for (String str1 : this.values.keySet())
      str = str + "    " + str1 + ": " + this.values.get(str1) + "\n"; 
    return str;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/transition/TransitionValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */