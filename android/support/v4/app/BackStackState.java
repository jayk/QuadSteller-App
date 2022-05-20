package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

final class BackStackState implements Parcelable {
  public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator<BackStackState>() {
      public BackStackState createFromParcel(Parcel param1Parcel) {
        return new BackStackState(param1Parcel);
      }
      
      public BackStackState[] newArray(int param1Int) {
        return new BackStackState[param1Int];
      }
    };
  
  final boolean mAllowOptimization;
  
  final int mBreadCrumbShortTitleRes;
  
  final CharSequence mBreadCrumbShortTitleText;
  
  final int mBreadCrumbTitleRes;
  
  final CharSequence mBreadCrumbTitleText;
  
  final int mIndex;
  
  final String mName;
  
  final int[] mOps;
  
  final ArrayList<String> mSharedElementSourceNames;
  
  final ArrayList<String> mSharedElementTargetNames;
  
  final int mTransition;
  
  final int mTransitionStyle;
  
  public BackStackState(Parcel paramParcel) {
    boolean bool;
    this.mOps = paramParcel.createIntArray();
    this.mTransition = paramParcel.readInt();
    this.mTransitionStyle = paramParcel.readInt();
    this.mName = paramParcel.readString();
    this.mIndex = paramParcel.readInt();
    this.mBreadCrumbTitleRes = paramParcel.readInt();
    this.mBreadCrumbTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mBreadCrumbShortTitleRes = paramParcel.readInt();
    this.mBreadCrumbShortTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mSharedElementSourceNames = paramParcel.createStringArrayList();
    this.mSharedElementTargetNames = paramParcel.createStringArrayList();
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mAllowOptimization = bool;
  }
  
  public BackStackState(BackStackRecord paramBackStackRecord) {
    int i = paramBackStackRecord.mOps.size();
    this.mOps = new int[i * 6];
    if (!paramBackStackRecord.mAddToBackStack)
      throw new IllegalStateException("Not on back stack"); 
    byte b = 0;
    int j = 0;
    while (b < i) {
      BackStackRecord.Op op = paramBackStackRecord.mOps.get(b);
      int[] arrayOfInt = this.mOps;
      int k = j + 1;
      arrayOfInt[j] = op.cmd;
      arrayOfInt = this.mOps;
      int m = k + 1;
      if (op.fragment != null) {
        j = op.fragment.mIndex;
      } else {
        j = -1;
      } 
      arrayOfInt[k] = j;
      arrayOfInt = this.mOps;
      j = m + 1;
      arrayOfInt[m] = op.enterAnim;
      arrayOfInt = this.mOps;
      m = j + 1;
      arrayOfInt[j] = op.exitAnim;
      arrayOfInt = this.mOps;
      k = m + 1;
      arrayOfInt[m] = op.popEnterAnim;
      arrayOfInt = this.mOps;
      j = k + 1;
      arrayOfInt[k] = op.popExitAnim;
      b++;
    } 
    this.mTransition = paramBackStackRecord.mTransition;
    this.mTransitionStyle = paramBackStackRecord.mTransitionStyle;
    this.mName = paramBackStackRecord.mName;
    this.mIndex = paramBackStackRecord.mIndex;
    this.mBreadCrumbTitleRes = paramBackStackRecord.mBreadCrumbTitleRes;
    this.mBreadCrumbTitleText = paramBackStackRecord.mBreadCrumbTitleText;
    this.mBreadCrumbShortTitleRes = paramBackStackRecord.mBreadCrumbShortTitleRes;
    this.mBreadCrumbShortTitleText = paramBackStackRecord.mBreadCrumbShortTitleText;
    this.mSharedElementSourceNames = paramBackStackRecord.mSharedElementSourceNames;
    this.mSharedElementTargetNames = paramBackStackRecord.mSharedElementTargetNames;
    this.mAllowOptimization = paramBackStackRecord.mAllowOptimization;
  }
  
  public int describeContents() {
    return 0;
  }
  
  public BackStackRecord instantiate(FragmentManagerImpl paramFragmentManagerImpl) {
    BackStackRecord backStackRecord = new BackStackRecord(paramFragmentManagerImpl);
    int i = 0;
    for (byte b = 0; i < this.mOps.length; b++) {
      BackStackRecord.Op op = new BackStackRecord.Op();
      int[] arrayOfInt = this.mOps;
      int j = i + 1;
      op.cmd = arrayOfInt[i];
      if (FragmentManagerImpl.DEBUG)
        Log.v("FragmentManager", "Instantiate " + backStackRecord + " op #" + b + " base fragment #" + this.mOps[j]); 
      arrayOfInt = this.mOps;
      i = j + 1;
      j = arrayOfInt[j];
      if (j >= 0) {
        op.fragment = paramFragmentManagerImpl.mActive.get(j);
      } else {
        op.fragment = null;
      } 
      arrayOfInt = this.mOps;
      j = i + 1;
      op.enterAnim = arrayOfInt[i];
      arrayOfInt = this.mOps;
      i = j + 1;
      op.exitAnim = arrayOfInt[j];
      arrayOfInt = this.mOps;
      j = i + 1;
      op.popEnterAnim = arrayOfInt[i];
      arrayOfInt = this.mOps;
      i = j + 1;
      op.popExitAnim = arrayOfInt[j];
      backStackRecord.mEnterAnim = op.enterAnim;
      backStackRecord.mExitAnim = op.exitAnim;
      backStackRecord.mPopEnterAnim = op.popEnterAnim;
      backStackRecord.mPopExitAnim = op.popExitAnim;
      backStackRecord.addOp(op);
    } 
    backStackRecord.mTransition = this.mTransition;
    backStackRecord.mTransitionStyle = this.mTransitionStyle;
    backStackRecord.mName = this.mName;
    backStackRecord.mIndex = this.mIndex;
    backStackRecord.mAddToBackStack = true;
    backStackRecord.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
    backStackRecord.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
    backStackRecord.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
    backStackRecord.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
    backStackRecord.mSharedElementSourceNames = this.mSharedElementSourceNames;
    backStackRecord.mSharedElementTargetNames = this.mSharedElementTargetNames;
    backStackRecord.mAllowOptimization = this.mAllowOptimization;
    backStackRecord.bumpBackStackNesting(1);
    return backStackRecord;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramInt = 0;
    paramParcel.writeIntArray(this.mOps);
    paramParcel.writeInt(this.mTransition);
    paramParcel.writeInt(this.mTransitionStyle);
    paramParcel.writeString(this.mName);
    paramParcel.writeInt(this.mIndex);
    paramParcel.writeInt(this.mBreadCrumbTitleRes);
    TextUtils.writeToParcel(this.mBreadCrumbTitleText, paramParcel, 0);
    paramParcel.writeInt(this.mBreadCrumbShortTitleRes);
    TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, paramParcel, 0);
    paramParcel.writeStringList(this.mSharedElementSourceNames);
    paramParcel.writeStringList(this.mSharedElementTargetNames);
    if (this.mAllowOptimization)
      paramInt = 1; 
    paramParcel.writeInt(paramInt);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/app/BackStackState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */