package com.tencent.bugly.crashreport.crash;

import android.os.Parcel;
import android.os.Parcelable;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import com.tencent.bugly.proguard.z;
import java.util.Map;
import java.util.UUID;

public class CrashDetailBean implements Parcelable, Comparable<CrashDetailBean> {
  public static final Parcelable.Creator<CrashDetailBean> CREATOR = new Parcelable.Creator<CrashDetailBean>() {
    
    };
  
  public String A;
  
  public long B;
  
  public long C;
  
  public long D;
  
  public long E;
  
  public long F;
  
  public long G;
  
  public String H;
  
  public String I;
  
  public String J;
  
  public String K;
  
  public long L;
  
  public boolean M;
  
  public Map<String, String> N;
  
  public int O;
  
  public int P;
  
  public Map<String, String> Q;
  
  public Map<String, String> R;
  
  public byte[] S;
  
  public String T;
  
  public String U;
  
  private String V;
  
  public long a;
  
  public int b;
  
  public String c;
  
  public boolean d;
  
  public String e;
  
  public String f;
  
  public String g;
  
  public Map<String, PlugInBean> h;
  
  public Map<String, PlugInBean> i;
  
  public boolean j;
  
  public boolean k;
  
  public int l;
  
  public String m;
  
  public String n;
  
  public String o;
  
  public String p;
  
  public String q;
  
  public long r;
  
  public String s;
  
  public int t;
  
  public String u;
  
  public String v;
  
  public String w;
  
  public byte[] x;
  
  public Map<String, String> y;
  
  public String z;
  
  public CrashDetailBean() {
    this.a = -1L;
    this.b = 0;
    this.c = UUID.randomUUID().toString();
    this.d = false;
    this.e = "";
    this.f = "";
    this.g = "";
    this.h = null;
    this.i = null;
    this.j = false;
    this.k = false;
    this.l = 0;
    this.m = "";
    this.n = "";
    this.o = "";
    this.p = "";
    this.q = "";
    this.r = -1L;
    this.s = null;
    this.t = 0;
    this.u = "";
    this.v = "";
    this.w = null;
    this.x = null;
    this.y = null;
    this.z = "";
    this.A = "";
    this.B = -1L;
    this.C = -1L;
    this.D = -1L;
    this.E = -1L;
    this.F = -1L;
    this.G = -1L;
    this.H = "";
    this.V = "";
    this.I = "";
    this.J = "";
    this.K = "";
    this.L = -1L;
    this.M = false;
    this.N = null;
    this.O = -1;
    this.P = -1;
    this.Q = null;
    this.R = null;
    this.S = null;
    this.T = null;
    this.U = null;
  }
  
  public CrashDetailBean(Parcel paramParcel) {
    boolean bool2;
    this.a = -1L;
    this.b = 0;
    this.c = UUID.randomUUID().toString();
    this.d = false;
    this.e = "";
    this.f = "";
    this.g = "";
    this.h = null;
    this.i = null;
    this.j = false;
    this.k = false;
    this.l = 0;
    this.m = "";
    this.n = "";
    this.o = "";
    this.p = "";
    this.q = "";
    this.r = -1L;
    this.s = null;
    this.t = 0;
    this.u = "";
    this.v = "";
    this.w = null;
    this.x = null;
    this.y = null;
    this.z = "";
    this.A = "";
    this.B = -1L;
    this.C = -1L;
    this.D = -1L;
    this.E = -1L;
    this.F = -1L;
    this.G = -1L;
    this.H = "";
    this.V = "";
    this.I = "";
    this.J = "";
    this.K = "";
    this.L = -1L;
    this.M = false;
    this.N = null;
    this.O = -1;
    this.P = -1;
    this.Q = null;
    this.R = null;
    this.S = null;
    this.T = null;
    this.U = null;
    this.b = paramParcel.readInt();
    this.c = paramParcel.readString();
    if (paramParcel.readByte() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    this.d = bool2;
    this.e = paramParcel.readString();
    this.f = paramParcel.readString();
    this.g = paramParcel.readString();
    if (paramParcel.readByte() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    this.j = bool2;
    if (paramParcel.readByte() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    this.k = bool2;
    this.l = paramParcel.readInt();
    this.m = paramParcel.readString();
    this.n = paramParcel.readString();
    this.o = paramParcel.readString();
    this.p = paramParcel.readString();
    this.q = paramParcel.readString();
    this.r = paramParcel.readLong();
    this.s = paramParcel.readString();
    this.t = paramParcel.readInt();
    this.u = paramParcel.readString();
    this.v = paramParcel.readString();
    this.w = paramParcel.readString();
    this.y = z.b(paramParcel);
    this.z = paramParcel.readString();
    this.A = paramParcel.readString();
    this.B = paramParcel.readLong();
    this.C = paramParcel.readLong();
    this.D = paramParcel.readLong();
    this.E = paramParcel.readLong();
    this.F = paramParcel.readLong();
    this.G = paramParcel.readLong();
    this.H = paramParcel.readString();
    this.V = paramParcel.readString();
    this.I = paramParcel.readString();
    this.J = paramParcel.readString();
    this.K = paramParcel.readString();
    this.L = paramParcel.readLong();
    if (paramParcel.readByte() == 1) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    this.M = bool2;
    this.N = z.b(paramParcel);
    this.h = z.a(paramParcel);
    this.i = z.a(paramParcel);
    this.O = paramParcel.readInt();
    this.P = paramParcel.readInt();
    this.Q = z.b(paramParcel);
    this.R = z.b(paramParcel);
    this.S = paramParcel.createByteArray();
    this.x = paramParcel.createByteArray();
    this.T = paramParcel.readString();
    this.U = paramParcel.readString();
  }
  
  public int describeContents() {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    boolean bool = true;
    paramParcel.writeInt(this.b);
    paramParcel.writeString(this.c);
    if (this.d) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeByte((byte)paramInt);
    paramParcel.writeString(this.e);
    paramParcel.writeString(this.f);
    paramParcel.writeString(this.g);
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
    paramParcel.writeInt(this.l);
    paramParcel.writeString(this.m);
    paramParcel.writeString(this.n);
    paramParcel.writeString(this.o);
    paramParcel.writeString(this.p);
    paramParcel.writeString(this.q);
    paramParcel.writeLong(this.r);
    paramParcel.writeString(this.s);
    paramParcel.writeInt(this.t);
    paramParcel.writeString(this.u);
    paramParcel.writeString(this.v);
    paramParcel.writeString(this.w);
    z.b(paramParcel, this.y);
    paramParcel.writeString(this.z);
    paramParcel.writeString(this.A);
    paramParcel.writeLong(this.B);
    paramParcel.writeLong(this.C);
    paramParcel.writeLong(this.D);
    paramParcel.writeLong(this.E);
    paramParcel.writeLong(this.F);
    paramParcel.writeLong(this.G);
    paramParcel.writeString(this.H);
    paramParcel.writeString(this.V);
    paramParcel.writeString(this.I);
    paramParcel.writeString(this.J);
    paramParcel.writeString(this.K);
    paramParcel.writeLong(this.L);
    if (this.M) {
      paramInt = bool;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeByte((byte)paramInt);
    z.b(paramParcel, this.N);
    z.a(paramParcel, this.h);
    z.a(paramParcel, this.i);
    paramParcel.writeInt(this.O);
    paramParcel.writeInt(this.P);
    z.b(paramParcel, this.Q);
    z.b(paramParcel, this.R);
    paramParcel.writeByteArray(this.S);
    paramParcel.writeByteArray(this.x);
    paramParcel.writeString(this.T);
    paramParcel.writeString(this.U);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/tencent/bugly/crashreport/crash/CrashDetailBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */