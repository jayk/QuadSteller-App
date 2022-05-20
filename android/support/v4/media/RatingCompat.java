package android.support.v4.media;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class RatingCompat implements Parcelable {
  public static final Parcelable.Creator<RatingCompat> CREATOR = new Parcelable.Creator<RatingCompat>() {
      public RatingCompat createFromParcel(Parcel param1Parcel) {
        return new RatingCompat(param1Parcel.readInt(), param1Parcel.readFloat());
      }
      
      public RatingCompat[] newArray(int param1Int) {
        return new RatingCompat[param1Int];
      }
    };
  
  public static final int RATING_3_STARS = 3;
  
  public static final int RATING_4_STARS = 4;
  
  public static final int RATING_5_STARS = 5;
  
  public static final int RATING_HEART = 1;
  
  public static final int RATING_NONE = 0;
  
  private static final float RATING_NOT_RATED = -1.0F;
  
  public static final int RATING_PERCENTAGE = 6;
  
  public static final int RATING_THUMB_UP_DOWN = 2;
  
  private static final String TAG = "Rating";
  
  private Object mRatingObj;
  
  private final int mRatingStyle;
  
  private final float mRatingValue;
  
  RatingCompat(int paramInt, float paramFloat) {
    this.mRatingStyle = paramInt;
    this.mRatingValue = paramFloat;
  }
  
  public static RatingCompat fromRating(Object paramObject) {
    RatingCompat ratingCompat1 = null;
    RatingCompat ratingCompat2 = ratingCompat1;
    if (paramObject != null) {
      if (Build.VERSION.SDK_INT < 19)
        return ratingCompat1; 
    } else {
      return ratingCompat2;
    } 
    int i = RatingCompatKitkat.getRatingStyle(paramObject);
    if (RatingCompatKitkat.isRated(paramObject)) {
      switch (i) {
        default:
          return ratingCompat1;
        case 1:
          ratingCompat2 = newHeartRating(RatingCompatKitkat.hasHeart(paramObject));
          ratingCompat2.mRatingObj = paramObject;
          return ratingCompat2;
        case 2:
          ratingCompat2 = newThumbRating(RatingCompatKitkat.isThumbUp(paramObject));
          ratingCompat2.mRatingObj = paramObject;
          return ratingCompat2;
        case 3:
        case 4:
        case 5:
          ratingCompat2 = newStarRating(i, RatingCompatKitkat.getStarRating(paramObject));
          ratingCompat2.mRatingObj = paramObject;
          return ratingCompat2;
        case 6:
          break;
      } 
      ratingCompat2 = newPercentageRating(RatingCompatKitkat.getPercentRating(paramObject));
    } else {
      ratingCompat2 = newUnratedRating(i);
    } 
    ratingCompat2.mRatingObj = paramObject;
    return ratingCompat2;
  }
  
  public static RatingCompat newHeartRating(boolean paramBoolean) {
    if (paramBoolean) {
      float f1 = 1.0F;
      return new RatingCompat(1, f1);
    } 
    float f = 0.0F;
    return new RatingCompat(1, f);
  }
  
  public static RatingCompat newPercentageRating(float paramFloat) {
    if (paramFloat < 0.0F || paramFloat > 100.0F) {
      Log.e("Rating", "Invalid percentage-based rating value");
      return null;
    } 
    return new RatingCompat(6, paramFloat);
  }
  
  public static RatingCompat newStarRating(int paramInt, float paramFloat) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: iload_0
    //   3: tableswitch default -> 28, 3 -> 60, 4 -> 86, 5 -> 92
    //   28: ldc 'Rating'
    //   30: new java/lang/StringBuilder
    //   33: dup
    //   34: invokespecial <init> : ()V
    //   37: ldc 'Invalid rating style ('
    //   39: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   42: iload_0
    //   43: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   46: ldc ') for a star rating'
    //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: invokevirtual toString : ()Ljava/lang/String;
    //   54: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   57: pop
    //   58: aload_2
    //   59: areturn
    //   60: ldc 3.0
    //   62: fstore_3
    //   63: fload_1
    //   64: fconst_0
    //   65: fcmpg
    //   66: iflt -> 75
    //   69: fload_1
    //   70: fload_3
    //   71: fcmpl
    //   72: ifle -> 98
    //   75: ldc 'Rating'
    //   77: ldc 'Trying to set out of range star-based rating'
    //   79: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   82: pop
    //   83: goto -> 58
    //   86: ldc 4.0
    //   88: fstore_3
    //   89: goto -> 63
    //   92: ldc 5.0
    //   94: fstore_3
    //   95: goto -> 63
    //   98: new android/support/v4/media/RatingCompat
    //   101: dup
    //   102: iload_0
    //   103: fload_1
    //   104: invokespecial <init> : (IF)V
    //   107: astore_2
    //   108: goto -> 58
  }
  
  public static RatingCompat newThumbRating(boolean paramBoolean) {
    if (paramBoolean) {
      float f1 = 1.0F;
      return new RatingCompat(2, f1);
    } 
    float f = 0.0F;
    return new RatingCompat(2, f);
  }
  
  public static RatingCompat newUnratedRating(int paramInt) {
    switch (paramInt) {
      default:
        return null;
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
        break;
    } 
    return new RatingCompat(paramInt, -1.0F);
  }
  
  public int describeContents() {
    return this.mRatingStyle;
  }
  
  public float getPercentRating() {
    return (this.mRatingStyle != 6 || !isRated()) ? -1.0F : this.mRatingValue;
  }
  
  public Object getRating() {
    if (this.mRatingObj != null || Build.VERSION.SDK_INT < 19)
      return this.mRatingObj; 
    if (isRated()) {
      switch (this.mRatingStyle) {
        default:
          return null;
        case 1:
          this.mRatingObj = RatingCompatKitkat.newHeartRating(hasHeart());
          return this.mRatingObj;
        case 2:
          this.mRatingObj = RatingCompatKitkat.newThumbRating(isThumbUp());
          return this.mRatingObj;
        case 3:
        case 4:
        case 5:
          this.mRatingObj = RatingCompatKitkat.newStarRating(this.mRatingStyle, getStarRating());
          return this.mRatingObj;
        case 6:
          break;
      } 
      this.mRatingObj = RatingCompatKitkat.newPercentageRating(getPercentRating());
    } 
    this.mRatingObj = RatingCompatKitkat.newUnratedRating(this.mRatingStyle);
    return this.mRatingObj;
  }
  
  public int getRatingStyle() {
    return this.mRatingStyle;
  }
  
  public float getStarRating() {
    switch (this.mRatingStyle) {
      default:
        return -1.0F;
      case 3:
      case 4:
      case 5:
        break;
    } 
    if (isRated())
      return this.mRatingValue; 
  }
  
  public boolean hasHeart() {
    boolean bool1 = true;
    boolean bool2 = false;
    if (this.mRatingStyle == 1) {
      if (this.mRatingValue == 1.0F)
        return bool1; 
      bool2 = false;
    } 
    return bool2;
  }
  
  public boolean isRated() {
    return (this.mRatingValue >= 0.0F);
  }
  
  public boolean isThumbUp() {
    boolean bool = false;
    if (this.mRatingStyle == 2 && this.mRatingValue == 1.0F)
      bool = true; 
    return bool;
  }
  
  public String toString() {
    StringBuilder stringBuilder = (new StringBuilder()).append("Rating:style=").append(this.mRatingStyle).append(" rating=");
    if (this.mRatingValue < 0.0F) {
      String str1 = "unrated";
      return stringBuilder.append(str1).toString();
    } 
    String str = String.valueOf(this.mRatingValue);
    return stringBuilder.append(str).toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeInt(this.mRatingStyle);
    paramParcel.writeFloat(this.mRatingValue);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface StarStyle {}
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface Style {}
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/media/RatingCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */