package app.gamer.quadstellar.utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import app.gamer.quadstellar.App;
import java.util.Locale;

public class UpdateLanguageUtils {
  public static void switchLanguage(int paramInt) {
    Resources resources = App.getAppResources();
    Configuration configuration = resources.getConfiguration();
    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
    if (paramInt == 0) {
      configuration.locale = Locale.getDefault();
    } else if (paramInt == 1) {
      configuration.locale = Locale.SIMPLIFIED_CHINESE;
    } else if (paramInt == 2) {
      configuration.locale = Locale.ENGLISH;
    } else if (paramInt == 3) {
      configuration.locale = Locale.JAPANESE;
    } 
    resources.updateConfiguration(configuration, displayMetrics);
    PreferenceHelper.write("language", paramInt);
  }
  
  public static void switchLanguage(String paramString) {
    byte b = 0;
    Resources resources = App.getAppResources();
    Configuration configuration = resources.getConfiguration();
    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
    if (paramString.equals(resources.getString(2131296801))) {
      b = 0;
    } else if (paramString.equals(resources.getString(2131296799))) {
      b = 1;
    } else if (paramString.equals(resources.getString(2131296802))) {
      b = 2;
    } else if (paramString.equals(resources.getString(2131296803))) {
      b = 3;
    } 
    if (b == 0) {
      configuration.locale = Locale.getDefault();
    } else if (b == 1) {
      configuration.locale = Locale.SIMPLIFIED_CHINESE;
    } else if (b == 2) {
      configuration.locale = Locale.ENGLISH;
    } else if (b == 3) {
      configuration.locale = Locale.JAPANESE;
    } 
    resources.updateConfiguration(configuration, displayMetrics);
    PreferenceHelper.write("language", b);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/UpdateLanguageUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */