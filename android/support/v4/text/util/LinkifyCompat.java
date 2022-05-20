package android.support.v4.text.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.PatternsCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.webkit.WebView;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkifyCompat {
  private static final Comparator<LinkSpec> COMPARATOR;
  
  private static final String[] EMPTY_STRING = new String[0];
  
  static {
    COMPARATOR = new Comparator<LinkSpec>() {
        public final int compare(LinkifyCompat.LinkSpec param1LinkSpec1, LinkifyCompat.LinkSpec param1LinkSpec2) {
          byte b = -1;
          if (param1LinkSpec1.start >= param1LinkSpec2.start) {
            if (param1LinkSpec1.start > param1LinkSpec2.start)
              return 1; 
            if (param1LinkSpec1.end < param1LinkSpec2.end)
              return 1; 
            if (param1LinkSpec1.end <= param1LinkSpec2.end)
              b = 0; 
          } 
          return b;
        }
      };
  }
  
  private static void addLinkMovementMethod(@NonNull TextView paramTextView) {
    MovementMethod movementMethod = paramTextView.getMovementMethod();
    if ((movementMethod == null || !(movementMethod instanceof LinkMovementMethod)) && paramTextView.getLinksClickable())
      paramTextView.setMovementMethod(LinkMovementMethod.getInstance()); 
  }
  
  public static final void addLinks(@NonNull TextView paramTextView, @NonNull Pattern paramPattern, @Nullable String paramString) {
    addLinks(paramTextView, paramPattern, paramString, (String[])null, (Linkify.MatchFilter)null, (Linkify.TransformFilter)null);
  }
  
  public static final void addLinks(@NonNull TextView paramTextView, @NonNull Pattern paramPattern, @Nullable String paramString, @Nullable Linkify.MatchFilter paramMatchFilter, @Nullable Linkify.TransformFilter paramTransformFilter) {
    addLinks(paramTextView, paramPattern, paramString, (String[])null, paramMatchFilter, paramTransformFilter);
  }
  
  public static final void addLinks(@NonNull TextView paramTextView, @NonNull Pattern paramPattern, @Nullable String paramString, @Nullable String[] paramArrayOfString, @Nullable Linkify.MatchFilter paramMatchFilter, @Nullable Linkify.TransformFilter paramTransformFilter) {
    SpannableString spannableString = SpannableString.valueOf(paramTextView.getText());
    if (addLinks((Spannable)spannableString, paramPattern, paramString, paramArrayOfString, paramMatchFilter, paramTransformFilter)) {
      paramTextView.setText((CharSequence)spannableString);
      addLinkMovementMethod(paramTextView);
    } 
  }
  
  public static final boolean addLinks(@NonNull Spannable paramSpannable, int paramInt) {
    if (paramInt == 0)
      return false; 
    URLSpan[] arrayOfURLSpan = (URLSpan[])paramSpannable.getSpans(0, paramSpannable.length(), URLSpan.class);
    for (int i = arrayOfURLSpan.length - 1; i >= 0; i--)
      paramSpannable.removeSpan(arrayOfURLSpan[i]); 
    if ((paramInt & 0x4) != 0)
      Linkify.addLinks(paramSpannable, 4); 
    ArrayList<LinkSpec> arrayList = new ArrayList();
    if ((paramInt & 0x1) != 0) {
      Pattern pattern = PatternsCompat.AUTOLINK_WEB_URL;
      Linkify.MatchFilter matchFilter = Linkify.sUrlMatchFilter;
      gatherLinks(arrayList, paramSpannable, pattern, new String[] { "http://", "https://", "rtsp://" }, matchFilter, null);
    } 
    if ((paramInt & 0x2) != 0)
      gatherLinks(arrayList, paramSpannable, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[] { "mailto:" }, null, null); 
    if ((paramInt & 0x8) != 0)
      gatherMapLinks(arrayList, paramSpannable); 
    pruneOverlaps(arrayList, paramSpannable);
    if (arrayList.size() == 0)
      return false; 
    for (LinkSpec linkSpec : arrayList) {
      if (linkSpec.frameworkAddedSpan == null)
        applyLink(linkSpec.url, linkSpec.start, linkSpec.end, paramSpannable); 
    } 
    return true;
  }
  
  public static final boolean addLinks(@NonNull Spannable paramSpannable, @NonNull Pattern paramPattern, @Nullable String paramString) {
    return addLinks(paramSpannable, paramPattern, paramString, (String[])null, (Linkify.MatchFilter)null, (Linkify.TransformFilter)null);
  }
  
  public static final boolean addLinks(@NonNull Spannable paramSpannable, @NonNull Pattern paramPattern, @Nullable String paramString, @Nullable Linkify.MatchFilter paramMatchFilter, @Nullable Linkify.TransformFilter paramTransformFilter) {
    return addLinks(paramSpannable, paramPattern, paramString, (String[])null, paramMatchFilter, paramTransformFilter);
  }
  
  public static final boolean addLinks(@NonNull Spannable paramSpannable, @NonNull Pattern paramPattern, @Nullable String paramString, @Nullable String[] paramArrayOfString, @Nullable Linkify.MatchFilter paramMatchFilter, @Nullable Linkify.TransformFilter paramTransformFilter) {
    // Byte code:
    //   0: aload_2
    //   1: astore #6
    //   3: aload_2
    //   4: ifnonnull -> 11
    //   7: ldc ''
    //   9: astore #6
    //   11: aload_3
    //   12: ifnull -> 23
    //   15: aload_3
    //   16: astore_2
    //   17: aload_3
    //   18: arraylength
    //   19: iconst_1
    //   20: if_icmpge -> 27
    //   23: getstatic android/support/v4/text/util/LinkifyCompat.EMPTY_STRING : [Ljava/lang/String;
    //   26: astore_2
    //   27: aload_2
    //   28: arraylength
    //   29: iconst_1
    //   30: iadd
    //   31: anewarray java/lang/String
    //   34: astore #7
    //   36: aload #7
    //   38: iconst_0
    //   39: aload #6
    //   41: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
    //   44: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   47: aastore
    //   48: iconst_0
    //   49: istore #8
    //   51: iload #8
    //   53: aload_2
    //   54: arraylength
    //   55: if_icmpge -> 95
    //   58: aload_2
    //   59: iload #8
    //   61: aaload
    //   62: astore_3
    //   63: aload_3
    //   64: ifnonnull -> 84
    //   67: ldc ''
    //   69: astore_3
    //   70: aload #7
    //   72: iload #8
    //   74: iconst_1
    //   75: iadd
    //   76: aload_3
    //   77: aastore
    //   78: iinc #8, 1
    //   81: goto -> 51
    //   84: aload_3
    //   85: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
    //   88: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   91: astore_3
    //   92: goto -> 70
    //   95: iconst_0
    //   96: istore #9
    //   98: aload_1
    //   99: aload_0
    //   100: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   103: astore_1
    //   104: aload_1
    //   105: invokevirtual find : ()Z
    //   108: ifeq -> 177
    //   111: aload_1
    //   112: invokevirtual start : ()I
    //   115: istore #8
    //   117: aload_1
    //   118: invokevirtual end : ()I
    //   121: istore #10
    //   123: iconst_1
    //   124: istore #11
    //   126: aload #4
    //   128: ifnull -> 145
    //   131: aload #4
    //   133: aload_0
    //   134: iload #8
    //   136: iload #10
    //   138: invokeinterface acceptMatch : (Ljava/lang/CharSequence;II)Z
    //   143: istore #11
    //   145: iload #11
    //   147: ifeq -> 104
    //   150: aload_1
    //   151: iconst_0
    //   152: invokevirtual group : (I)Ljava/lang/String;
    //   155: aload #7
    //   157: aload_1
    //   158: aload #5
    //   160: invokestatic makeUrl : (Ljava/lang/String;[Ljava/lang/String;Ljava/util/regex/Matcher;Landroid/text/util/Linkify$TransformFilter;)Ljava/lang/String;
    //   163: iload #8
    //   165: iload #10
    //   167: aload_0
    //   168: invokestatic applyLink : (Ljava/lang/String;IILandroid/text/Spannable;)V
    //   171: iconst_1
    //   172: istore #9
    //   174: goto -> 104
    //   177: iload #9
    //   179: ireturn
  }
  
  public static final boolean addLinks(@NonNull TextView paramTextView, int paramInt) {
    boolean bool = false;
    if (paramInt != 0) {
      CharSequence charSequence = paramTextView.getText();
      if (charSequence instanceof Spannable) {
        if (addLinks((Spannable)charSequence, paramInt)) {
          addLinkMovementMethod(paramTextView);
          bool = true;
        } 
        return bool;
      } 
      SpannableString spannableString = SpannableString.valueOf(charSequence);
      if (addLinks((Spannable)spannableString, paramInt)) {
        addLinkMovementMethod(paramTextView);
        paramTextView.setText((CharSequence)spannableString);
        bool = true;
      } 
    } 
    return bool;
  }
  
  private static void applyLink(String paramString, int paramInt1, int paramInt2, Spannable paramSpannable) {
    paramSpannable.setSpan(new URLSpan(paramString), paramInt1, paramInt2, 33);
  }
  
  private static void gatherLinks(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable, Pattern paramPattern, String[] paramArrayOfString, Linkify.MatchFilter paramMatchFilter, Linkify.TransformFilter paramTransformFilter) {
    Matcher matcher = paramPattern.matcher((CharSequence)paramSpannable);
    while (matcher.find()) {
      int i = matcher.start();
      int j = matcher.end();
      if (paramMatchFilter == null || paramMatchFilter.acceptMatch((CharSequence)paramSpannable, i, j)) {
        LinkSpec linkSpec = new LinkSpec();
        linkSpec.url = makeUrl(matcher.group(0), paramArrayOfString, matcher, paramTransformFilter);
        linkSpec.start = i;
        linkSpec.end = j;
        paramArrayList.add(linkSpec);
      } 
    } 
  }
  
  private static final void gatherMapLinks(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable) {
    String str = paramSpannable.toString();
    int i = 0;
    while (true) {
      try {
        String str1 = WebView.findAddress(str);
        if (str1 != null) {
          int j = str.indexOf(str1);
          if (j >= 0) {
            LinkSpec linkSpec = new LinkSpec();
            this();
            int k = j + str1.length();
            linkSpec.start = i + j;
            linkSpec.end = i + k;
            str = str.substring(k);
            i += k;
            try {
              String str2 = URLEncoder.encode(str1, "UTF-8");
              StringBuilder stringBuilder = new StringBuilder();
              this();
              linkSpec.url = stringBuilder.append("geo:0,0?q=").append(str2).toString();
              paramArrayList.add(linkSpec);
            } catch (UnsupportedEncodingException unsupportedEncodingException) {}
            continue;
          } 
        } 
      } catch (UnsupportedOperationException unsupportedOperationException) {}
      return;
    } 
  }
  
  private static String makeUrl(@NonNull String paramString, @NonNull String[] paramArrayOfString, Matcher paramMatcher, @Nullable Linkify.TransformFilter paramTransformFilter) {
    String str = paramString;
    if (paramTransformFilter != null)
      str = paramTransformFilter.transformUrl(paramMatcher, paramString); 
    boolean bool = false;
    byte b = 0;
    while (true) {
      boolean bool1 = bool;
      paramString = str;
      if (b < paramArrayOfString.length)
        if (str.regionMatches(true, 0, paramArrayOfString[b], 0, paramArrayOfString[b].length())) {
          bool = true;
          bool1 = bool;
          paramString = str;
          if (!str.regionMatches(false, 0, paramArrayOfString[b], 0, paramArrayOfString[b].length())) {
            paramString = paramArrayOfString[b] + str.substring(paramArrayOfString[b].length());
            bool1 = bool;
          } 
        } else {
          b++;
          continue;
        }  
      String str1 = paramString;
      if (!bool1) {
        str1 = paramString;
        if (paramArrayOfString.length > 0)
          str1 = paramArrayOfString[0] + paramString; 
      } 
      return str1;
    } 
  }
  
  private static final void pruneOverlaps(ArrayList<LinkSpec> paramArrayList, Spannable paramSpannable) {
    URLSpan[] arrayOfURLSpan = (URLSpan[])paramSpannable.getSpans(0, paramSpannable.length(), URLSpan.class);
    int i;
    for (i = 0; i < arrayOfURLSpan.length; i++) {
      LinkSpec linkSpec = new LinkSpec();
      linkSpec.frameworkAddedSpan = arrayOfURLSpan[i];
      linkSpec.start = paramSpannable.getSpanStart(arrayOfURLSpan[i]);
      linkSpec.end = paramSpannable.getSpanEnd(arrayOfURLSpan[i]);
      paramArrayList.add(linkSpec);
    } 
    Collections.sort(paramArrayList, COMPARATOR);
    int j = paramArrayList.size();
    for (byte b = 0; b < j - 1; b++) {
      LinkSpec linkSpec1 = paramArrayList.get(b);
      LinkSpec linkSpec2 = paramArrayList.get(b + 1);
      i = -1;
      if (linkSpec1.start <= linkSpec2.start && linkSpec1.end > linkSpec2.start) {
        if (linkSpec2.end <= linkSpec1.end) {
          i = b + 1;
        } else if (linkSpec1.end - linkSpec1.start > linkSpec2.end - linkSpec2.start) {
          i = b + 1;
        } else if (linkSpec1.end - linkSpec1.start < linkSpec2.end - linkSpec2.start) {
          i = b;
        } 
        if (i != -1) {
          URLSpan uRLSpan = ((LinkSpec)paramArrayList.get(i)).frameworkAddedSpan;
          if (uRLSpan != null)
            paramSpannable.removeSpan(uRLSpan); 
          paramArrayList.remove(i);
          j--;
          continue;
        } 
      } 
    } 
  }
  
  private static class LinkSpec {
    int end;
    
    URLSpan frameworkAddedSpan;
    
    int start;
    
    String url;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LinkifyMask {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/text/util/LinkifyCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */