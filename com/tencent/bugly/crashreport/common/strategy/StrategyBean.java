package com.tencent.bugly.crashreport.common.strategy;

import android.os.Parcel;
import android.os.Parcelable;
import com.tencent.bugly.proguard.z;
import java.util.Map;

public class StrategyBean implements Parcelable {
  public static final Parcelable.Creator<StrategyBean> CREATOR;
  
  public static String a = "http://rqd.uu.qq.com/rqd/sync";
  
  public static String b = "http://android.bugly.qq.com/rqd/async";
  
  public static String c = "http://android.bugly.qq.com/rqd/async";
  
  public static String d;
  
  public long e = -1L;
  
  public long f = -1L;
  
  public boolean g = true;
  
  public boolean h = true;
  
  public boolean i = true;
  
  public boolean j = true;
  
  public boolean k = false;
  
  public boolean l = true;
  
  public boolean m = true;
  
  public boolean n = true;
  
  public boolean o = true;
  
  public long p;
  
  public long q = 30000L;
  
  public String r = b;
  
  public String s = c;
  
  public String t = a;
  
  public String u;
  
  public Map<String, String> v;
  
  public int w = 10;
  
  public long x = 300000L;
  
  public long y = -1L;
  
  static {
    CREATOR = new Parcelable.Creator<StrategyBean>() {
      
      };
  }
  
  public StrategyBean() {
    this.f = System.currentTimeMillis();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("S(@L@L").append("@)");
    d = stringBuilder.toString();
    stringBuilder.setLength(0);
    stringBuilder.append("*^@K#K").append("@!");
    this.u = stringBuilder.toString();
  }
  
  public StrategyBean(Parcel paramParcel) {
    try {
      boolean bool1;
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("S(@L@L").append("@)");
      d = stringBuilder.toString();
      this.f = paramParcel.readLong();
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.g = bool1;
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.h = bool1;
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.i = bool1;
      this.r = paramParcel.readString();
      this.s = paramParcel.readString();
      this.u = paramParcel.readString();
      this.v = z.b(paramParcel);
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.j = bool1;
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.k = bool1;
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.n = bool1;
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.o = bool1;
      this.q = paramParcel.readLong();
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.l = bool1;
      if (paramParcel.readByte() == 1) {
        bool1 = bool;
      } else {
        bool1 = false;
      } 
      this.m = bool1;
      this.p = paramParcel.readLong();
      this.w = paramParcel.readInt();
      this.x = paramParcel.readLong();
      this.y = paramParcel.readLong();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public int describeContents() {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    boolean bool = true;
    paramParcel.writeLong(this.f);
    if (this.g) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeByte((byte)paramInt);
    if (this.h) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeByte((byte)paramInt);
    if (this.i) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeByte((byte)paramInt);
    paramParcel.writeString(this.r);
    paramParcel.writeString(this.s);
    paramParcel.writeString(this.u);
    z.b(paramParcel, this.v);
    if (this.j) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeByte((byte)paramInt);
    if (this.k) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeByte((byte)paramInt);
    if (this.n) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeByte((byte)paramInt);
    if (this.o) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeByte((byte)paramInt);
    paramParcel.writeLong(this.q);
    if (this.l) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeByte((byte)paramInt);
    if (this.m) {
      paramInt = bool;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeByte((byte)paramInt);
    paramParcel.writeLong(this.p);
    paramParcel.writeInt(this.w);
    paramParcel.writeLong(this.x);
    paramParcel.writeLong(this.y);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/common/strategy/StrategyBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */