package com.blankj.utilcode.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils {
  private RegexUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static List<String> getMatches(String paramString, CharSequence paramCharSequence) {
    if (paramCharSequence == null)
      return null; 
    ArrayList<String> arrayList = new ArrayList();
    Matcher matcher = Pattern.compile(paramString).matcher(paramCharSequence);
    while (true) {
      ArrayList<String> arrayList1 = arrayList;
      if (matcher.find()) {
        arrayList.add(matcher.group());
        continue;
      } 
      return arrayList1;
    } 
  }
  
  public static String getReplaceAll(String paramString1, String paramString2, String paramString3) {
    return (paramString1 == null) ? null : Pattern.compile(paramString2).matcher(paramString1).replaceAll(paramString3);
  }
  
  public static String getReplaceFirst(String paramString1, String paramString2, String paramString3) {
    return (paramString1 == null) ? null : Pattern.compile(paramString2).matcher(paramString1).replaceFirst(paramString3);
  }
  
  public static String[] getSplits(String paramString1, String paramString2) {
    return (paramString1 == null) ? null : paramString1.split(paramString2);
  }
  
  public static boolean isDate(CharSequence paramCharSequence) {
    return isMatch("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$", paramCharSequence);
  }
  
  public static boolean isEmail(CharSequence paramCharSequence) {
    return isMatch("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", paramCharSequence);
  }
  
  public static boolean isIDCard15(CharSequence paramCharSequence) {
    return isMatch("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$", paramCharSequence);
  }
  
  public static boolean isIDCard18(CharSequence paramCharSequence) {
    return isMatch("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$", paramCharSequence);
  }
  
  public static boolean isIP(CharSequence paramCharSequence) {
    return isMatch("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)", paramCharSequence);
  }
  
  public static boolean isMatch(String paramString, CharSequence paramCharSequence) {
    return (paramCharSequence != null && paramCharSequence.length() > 0 && Pattern.matches(paramString, paramCharSequence));
  }
  
  public static boolean isMobileExact(CharSequence paramCharSequence) {
    return isMatch("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,1,3,5-8])|(18[0-9])|(147))\\d{8}$", paramCharSequence);
  }
  
  public static boolean isMobileSimple(CharSequence paramCharSequence) {
    return isMatch("^[1]\\d{10}$", paramCharSequence);
  }
  
  public static boolean isTel(CharSequence paramCharSequence) {
    return isMatch("^0\\d{2,3}[- ]?\\d{7,8}", paramCharSequence);
  }
  
  public static boolean isURL(CharSequence paramCharSequence) {
    return isMatch("[a-zA-z]+://[^\\s]*", paramCharSequence);
  }
  
  public static boolean isUsername(CharSequence paramCharSequence) {
    return isMatch("^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$", paramCharSequence);
  }
  
  public static boolean isZh(CharSequence paramCharSequence) {
    return isMatch("^[\\u4e00-\\u9fa5]+$", paramCharSequence);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/RegexUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */