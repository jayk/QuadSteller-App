package android.support.v4.media;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

public final class MediaMetadataCompat implements Parcelable {
  public static final Parcelable.Creator<MediaMetadataCompat> CREATOR;
  
  static final ArrayMap<String, Integer> METADATA_KEYS_TYPE = new ArrayMap();
  
  public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
  
  public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
  
  public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
  
  public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
  
  public static final String METADATA_KEY_ART = "android.media.metadata.ART";
  
  public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
  
  public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
  
  public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
  
  public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
  
  public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
  
  public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
  
  public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
  
  public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
  
  public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
  
  public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
  
  public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
  
  public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
  
  public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
  
  public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
  
  public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
  
  public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
  
  public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
  
  public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
  
  public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
  
  public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
  
  public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
  
  public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
  
  public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
  
  public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
  
  static final int METADATA_TYPE_BITMAP = 2;
  
  static final int METADATA_TYPE_LONG = 0;
  
  static final int METADATA_TYPE_RATING = 3;
  
  static final int METADATA_TYPE_TEXT = 1;
  
  private static final String[] PREFERRED_BITMAP_ORDER;
  
  private static final String[] PREFERRED_DESCRIPTION_ORDER = new String[] { "android.media.metadata.TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.WRITER", "android.media.metadata.AUTHOR", "android.media.metadata.COMPOSER" };
  
  private static final String[] PREFERRED_URI_ORDER;
  
  private static final String TAG = "MediaMetadata";
  
  final Bundle mBundle;
  
  private MediaDescriptionCompat mDescription;
  
  private Object mMetadataObj;
  
  static {
    PREFERRED_BITMAP_ORDER = new String[] { "android.media.metadata.DISPLAY_ICON", "android.media.metadata.ART", "android.media.metadata.ALBUM_ART" };
    PREFERRED_URI_ORDER = new String[] { "android.media.metadata.DISPLAY_ICON_URI", "android.media.metadata.ART_URI", "android.media.metadata.ALBUM_ART_URI" };
    CREATOR = new Parcelable.Creator<MediaMetadataCompat>() {
        public MediaMetadataCompat createFromParcel(Parcel param1Parcel) {
          return new MediaMetadataCompat(param1Parcel);
        }
        
        public MediaMetadataCompat[] newArray(int param1Int) {
          return new MediaMetadataCompat[param1Int];
        }
      };
  }
  
  MediaMetadataCompat(Bundle paramBundle) {
    this.mBundle = new Bundle(paramBundle);
  }
  
  MediaMetadataCompat(Parcel paramParcel) {
    this.mBundle = paramParcel.readBundle();
  }
  
  public static MediaMetadataCompat fromMediaMetadata(Object paramObject) {
    if (paramObject == null || Build.VERSION.SDK_INT < 21)
      return null; 
    Parcel parcel = Parcel.obtain();
    MediaMetadataCompatApi21.writeToParcel(paramObject, parcel, 0);
    parcel.setDataPosition(0);
    MediaMetadataCompat mediaMetadataCompat = (MediaMetadataCompat)CREATOR.createFromParcel(parcel);
    parcel.recycle();
    mediaMetadataCompat.mMetadataObj = paramObject;
    return mediaMetadataCompat;
  }
  
  public boolean containsKey(String paramString) {
    return this.mBundle.containsKey(paramString);
  }
  
  public int describeContents() {
    return 0;
  }
  
  public Bitmap getBitmap(String paramString) {
    Exception exception2 = null;
    try {
      Bitmap bitmap = (Bitmap)this.mBundle.getParcelable(paramString);
    } catch (Exception exception1) {
      Log.w("MediaMetadata", "Failed to retrieve a key as Bitmap.", exception1);
      exception1 = exception2;
    } 
    return (Bitmap)exception1;
  }
  
  public Bundle getBundle() {
    return this.mBundle;
  }
  
  public MediaDescriptionCompat getDescription() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mDescription : Landroid/support/v4/media/MediaDescriptionCompat;
    //   4: ifnull -> 14
    //   7: aload_0
    //   8: getfield mDescription : Landroid/support/v4/media/MediaDescriptionCompat;
    //   11: astore_1
    //   12: aload_1
    //   13: areturn
    //   14: aload_0
    //   15: ldc 'android.media.metadata.MEDIA_ID'
    //   17: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   20: astore_2
    //   21: iconst_3
    //   22: anewarray java/lang/CharSequence
    //   25: astore_3
    //   26: aconst_null
    //   27: astore #4
    //   29: aconst_null
    //   30: astore #5
    //   32: aload_0
    //   33: ldc 'android.media.metadata.DISPLAY_TITLE'
    //   35: invokevirtual getText : (Ljava/lang/String;)Ljava/lang/CharSequence;
    //   38: astore_1
    //   39: aload_1
    //   40: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   43: ifne -> 290
    //   46: aload_3
    //   47: iconst_0
    //   48: aload_1
    //   49: aastore
    //   50: aload_3
    //   51: iconst_1
    //   52: aload_0
    //   53: ldc 'android.media.metadata.DISPLAY_SUBTITLE'
    //   55: invokevirtual getText : (Ljava/lang/String;)Ljava/lang/CharSequence;
    //   58: aastore
    //   59: aload_3
    //   60: iconst_2
    //   61: aload_0
    //   62: ldc 'android.media.metadata.DISPLAY_DESCRIPTION'
    //   64: invokevirtual getText : (Ljava/lang/String;)Ljava/lang/CharSequence;
    //   67: aastore
    //   68: iconst_0
    //   69: istore #6
    //   71: aload #4
    //   73: astore_1
    //   74: iload #6
    //   76: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_BITMAP_ORDER : [Ljava/lang/String;
    //   79: arraylength
    //   80: if_icmpge -> 98
    //   83: aload_0
    //   84: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_BITMAP_ORDER : [Ljava/lang/String;
    //   87: iload #6
    //   89: aaload
    //   90: invokevirtual getBitmap : (Ljava/lang/String;)Landroid/graphics/Bitmap;
    //   93: astore_1
    //   94: aload_1
    //   95: ifnull -> 355
    //   98: iconst_0
    //   99: istore #6
    //   101: aload #5
    //   103: astore #4
    //   105: iload #6
    //   107: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_URI_ORDER : [Ljava/lang/String;
    //   110: arraylength
    //   111: if_icmpge -> 141
    //   114: aload_0
    //   115: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_URI_ORDER : [Ljava/lang/String;
    //   118: iload #6
    //   120: aaload
    //   121: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   124: astore #4
    //   126: aload #4
    //   128: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   131: ifne -> 361
    //   134: aload #4
    //   136: invokestatic parse : (Ljava/lang/String;)Landroid/net/Uri;
    //   139: astore #4
    //   141: aconst_null
    //   142: astore #5
    //   144: aload_0
    //   145: ldc 'android.media.metadata.MEDIA_URI'
    //   147: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   150: astore #7
    //   152: aload #7
    //   154: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   157: ifne -> 167
    //   160: aload #7
    //   162: invokestatic parse : (Ljava/lang/String;)Landroid/net/Uri;
    //   165: astore #5
    //   167: new android/support/v4/media/MediaDescriptionCompat$Builder
    //   170: dup
    //   171: invokespecial <init> : ()V
    //   174: astore #7
    //   176: aload #7
    //   178: aload_2
    //   179: invokevirtual setMediaId : (Ljava/lang/String;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   182: pop
    //   183: aload #7
    //   185: aload_3
    //   186: iconst_0
    //   187: aaload
    //   188: invokevirtual setTitle : (Ljava/lang/CharSequence;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   191: pop
    //   192: aload #7
    //   194: aload_3
    //   195: iconst_1
    //   196: aaload
    //   197: invokevirtual setSubtitle : (Ljava/lang/CharSequence;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   200: pop
    //   201: aload #7
    //   203: aload_3
    //   204: iconst_2
    //   205: aaload
    //   206: invokevirtual setDescription : (Ljava/lang/CharSequence;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   209: pop
    //   210: aload #7
    //   212: aload_1
    //   213: invokevirtual setIconBitmap : (Landroid/graphics/Bitmap;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   216: pop
    //   217: aload #7
    //   219: aload #4
    //   221: invokevirtual setIconUri : (Landroid/net/Uri;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   224: pop
    //   225: aload #7
    //   227: aload #5
    //   229: invokevirtual setMediaUri : (Landroid/net/Uri;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   232: pop
    //   233: aload_0
    //   234: getfield mBundle : Landroid/os/Bundle;
    //   237: ldc 'android.media.metadata.BT_FOLDER_TYPE'
    //   239: invokevirtual containsKey : (Ljava/lang/String;)Z
    //   242: ifeq -> 273
    //   245: new android/os/Bundle
    //   248: dup
    //   249: invokespecial <init> : ()V
    //   252: astore_1
    //   253: aload_1
    //   254: ldc_w 'android.media.extra.BT_FOLDER_TYPE'
    //   257: aload_0
    //   258: ldc 'android.media.metadata.BT_FOLDER_TYPE'
    //   260: invokevirtual getLong : (Ljava/lang/String;)J
    //   263: invokevirtual putLong : (Ljava/lang/String;J)V
    //   266: aload #7
    //   268: aload_1
    //   269: invokevirtual setExtras : (Landroid/os/Bundle;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   272: pop
    //   273: aload_0
    //   274: aload #7
    //   276: invokevirtual build : ()Landroid/support/v4/media/MediaDescriptionCompat;
    //   279: putfield mDescription : Landroid/support/v4/media/MediaDescriptionCompat;
    //   282: aload_0
    //   283: getfield mDescription : Landroid/support/v4/media/MediaDescriptionCompat;
    //   286: astore_1
    //   287: goto -> 12
    //   290: iconst_0
    //   291: istore #8
    //   293: iconst_0
    //   294: istore #9
    //   296: iload #8
    //   298: aload_3
    //   299: arraylength
    //   300: if_icmpge -> 68
    //   303: iload #9
    //   305: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_DESCRIPTION_ORDER : [Ljava/lang/String;
    //   308: arraylength
    //   309: if_icmpge -> 68
    //   312: aload_0
    //   313: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_DESCRIPTION_ORDER : [Ljava/lang/String;
    //   316: iload #9
    //   318: aaload
    //   319: invokevirtual getText : (Ljava/lang/String;)Ljava/lang/CharSequence;
    //   322: astore_1
    //   323: iload #8
    //   325: istore #6
    //   327: aload_1
    //   328: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   331: ifne -> 345
    //   334: aload_3
    //   335: iload #8
    //   337: aload_1
    //   338: aastore
    //   339: iload #8
    //   341: iconst_1
    //   342: iadd
    //   343: istore #6
    //   345: iinc #9, 1
    //   348: iload #6
    //   350: istore #8
    //   352: goto -> 296
    //   355: iinc #6, 1
    //   358: goto -> 71
    //   361: iinc #6, 1
    //   364: goto -> 101
  }
  
  public long getLong(String paramString) {
    return this.mBundle.getLong(paramString, 0L);
  }
  
  public Object getMediaMetadata() {
    if (this.mMetadataObj != null || Build.VERSION.SDK_INT < 21)
      return this.mMetadataObj; 
    Parcel parcel = Parcel.obtain();
    writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    this.mMetadataObj = MediaMetadataCompatApi21.createFromParcel(parcel);
    parcel.recycle();
    return this.mMetadataObj;
  }
  
  public RatingCompat getRating(String paramString) {
    Exception exception2 = null;
    try {
      if (Build.VERSION.SDK_INT >= 19)
        return RatingCompat.fromRating(this.mBundle.getParcelable(paramString)); 
      RatingCompat ratingCompat = (RatingCompat)this.mBundle.getParcelable(paramString);
    } catch (Exception exception1) {
      Log.w("MediaMetadata", "Failed to retrieve a key as Rating.", exception1);
      exception1 = exception2;
    } 
    return (RatingCompat)exception1;
  }
  
  public String getString(String paramString) {
    null = this.mBundle.getCharSequence(paramString);
    return (null != null) ? null.toString() : null;
  }
  
  public CharSequence getText(String paramString) {
    return this.mBundle.getCharSequence(paramString);
  }
  
  public Set<String> keySet() {
    return this.mBundle.keySet();
  }
  
  public int size() {
    return this.mBundle.size();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeBundle(this.mBundle);
  }
  
  static {
    METADATA_KEYS_TYPE.put("android.media.metadata.TITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ARTIST", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DURATION", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.AUTHOR", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.WRITER", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.COMPOSER", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.COMPILATION", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DATE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.YEAR", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.GENRE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.TRACK_NUMBER", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.NUM_TRACKS", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISC_NUMBER", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ARTIST", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ART", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.ART_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.USER_RATING", Integer.valueOf(3));
    METADATA_KEYS_TYPE.put("android.media.metadata.RATING", Integer.valueOf(3));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_TITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_SUBTITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_DESCRIPTION", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_ID", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.BT_FOLDER_TYPE", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_URI", Integer.valueOf(1));
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface BitmapKey {}
  
  public static final class Builder {
    private final Bundle mBundle = new Bundle();
    
    public Builder() {}
    
    public Builder(MediaMetadataCompat param1MediaMetadataCompat) {}
    
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public Builder(MediaMetadataCompat param1MediaMetadataCompat, int param1Int) {
      this(param1MediaMetadataCompat);
      for (String str : this.mBundle.keySet()) {
        Object object = this.mBundle.get(str);
        if (object != null && object instanceof Bitmap) {
          object = object;
          if (object.getHeight() > param1Int || object.getWidth() > param1Int) {
            putBitmap(str, scaleBitmap((Bitmap)object, param1Int));
            continue;
          } 
          if (Build.VERSION.SDK_INT >= 14 && (str.equals("android.media.metadata.ART") || str.equals("android.media.metadata.ALBUM_ART")))
            putBitmap(str, object.copy(object.getConfig(), false)); 
        } 
      } 
    }
    
    private Bitmap scaleBitmap(Bitmap param1Bitmap, int param1Int) {
      float f = param1Int;
      f = Math.min(f / param1Bitmap.getWidth(), f / param1Bitmap.getHeight());
      param1Int = (int)(param1Bitmap.getHeight() * f);
      return Bitmap.createScaledBitmap(param1Bitmap, (int)(param1Bitmap.getWidth() * f), param1Int, true);
    }
    
    public MediaMetadataCompat build() {
      return new MediaMetadataCompat(this.mBundle);
    }
    
    public Builder putBitmap(String param1String, Bitmap param1Bitmap) {
      if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(param1String) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(param1String)).intValue() != 2)
        throw new IllegalArgumentException("The " + param1String + " key cannot be used to put a Bitmap"); 
      this.mBundle.putParcelable(param1String, (Parcelable)param1Bitmap);
      return this;
    }
    
    public Builder putLong(String param1String, long param1Long) {
      if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(param1String) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(param1String)).intValue() != 0)
        throw new IllegalArgumentException("The " + param1String + " key cannot be used to put a long"); 
      this.mBundle.putLong(param1String, param1Long);
      return this;
    }
    
    public Builder putRating(String param1String, RatingCompat param1RatingCompat) {
      if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(param1String) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(param1String)).intValue() != 3)
        throw new IllegalArgumentException("The " + param1String + " key cannot be used to put a Rating"); 
      if (Build.VERSION.SDK_INT >= 19) {
        this.mBundle.putParcelable(param1String, (Parcelable)param1RatingCompat.getRating());
        return this;
      } 
      this.mBundle.putParcelable(param1String, param1RatingCompat);
      return this;
    }
    
    public Builder putString(String param1String1, String param1String2) {
      if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(param1String1) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(param1String1)).intValue() != 1)
        throw new IllegalArgumentException("The " + param1String1 + " key cannot be used to put a String"); 
      this.mBundle.putCharSequence(param1String1, param1String2);
      return this;
    }
    
    public Builder putText(String param1String, CharSequence param1CharSequence) {
      if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(param1String) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(param1String)).intValue() != 1)
        throw new IllegalArgumentException("The " + param1String + " key cannot be used to put a CharSequence"); 
      this.mBundle.putCharSequence(param1String, param1CharSequence);
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface LongKey {}
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface RatingKey {}
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface TextKey {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/media/MediaMetadataCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */