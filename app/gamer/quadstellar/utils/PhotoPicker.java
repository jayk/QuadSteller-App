package app.gamer.quadstellar.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import app.gamer.quadstellar.App;
import java.io.File;

public class PhotoPicker {
  private static final String IMAGE_TYPE = "image/*";
  
  public static String getPhotoPathByLocalUri(Context paramContext, Intent paramIntent) {
    String str1;
    String str2 = null;
    String str3 = str2;
    try {
      Uri uri = paramIntent.getData();
      str3 = str2;
      String[] arrayOfString = new String[1];
      arrayOfString[0] = "_data";
      str3 = str2;
      Cursor cursor = paramContext.getContentResolver().query(uri, arrayOfString, null, null, null);
      str3 = str2;
      cursor.moveToFirst();
      str3 = str2;
      str1 = cursor.getString(cursor.getColumnIndex(arrayOfString[0]));
      str3 = str1;
      cursor.close();
    } catch (Exception exception) {
      exception.printStackTrace();
      str1 = str3;
    } 
    return str1;
  }
  
  private static boolean launch3partyBroswer(Activity paramActivity, int paramInt) {
    boolean bool = true;
    Toast.makeText((Context)paramActivity, "没有相册软件，运行文件浏览器", 1).show();
    Intent intent = new Intent("android.intent.action.GET_CONTENT");
    intent.setType("image/*");
    intent = Intent.createChooser(intent, null);
    try {
      paramActivity.startActivityForResult(intent, paramInt);
      bool = false;
    } catch (ActivityNotFoundException activityNotFoundException) {}
    return bool;
  }
  
  private static boolean launch3partyBroswer(Fragment paramFragment, int paramInt) {
    boolean bool = true;
    Toast.makeText(App.getAppContext(), "没有相册软件，运行文件浏览器", 1).show();
    Intent intent = new Intent("android.intent.action.GET_CONTENT");
    intent.setType("image/*");
    intent = Intent.createChooser(intent, null);
    try {
      paramFragment.startActivityForResult(intent, paramInt);
      bool = false;
    } catch (ActivityNotFoundException activityNotFoundException) {}
    return bool;
  }
  
  public static void launchCamera(Activity paramActivity, int paramInt, File paramFile) {
    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    intent.putExtra("output", (Parcelable)Uri.fromFile(paramFile));
    paramActivity.startActivityForResult(intent, paramInt);
  }
  
  public static void launchCamera(Fragment paramFragment, int paramInt, File paramFile) {
    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    intent.putExtra("output", (Parcelable)Uri.fromFile(paramFile));
    paramFragment.startActivityForResult(intent, paramInt);
  }
  
  private static boolean launchFinally(Activity paramActivity) {
    Toast.makeText((Context)paramActivity, "您的系统没有文件浏览器或则相册支持,请安装！", 1).show();
    return false;
  }
  
  public static void launchGallery(Activity paramActivity, int paramInt) {
    if (!launchSys(paramActivity, paramInt) || !launch3partyBroswer(paramActivity, paramInt) || launchFinally(paramActivity));
  }
  
  public static void launchGallery(Activity paramActivity, Fragment paramFragment, int paramInt) {
    if (!launchSys(paramFragment, paramInt) || !launch3partyBroswer(paramFragment, paramInt) || launchFinally(paramActivity));
  }
  
  private static boolean launchSys(Activity paramActivity, int paramInt) {
    boolean bool;
    Intent intent = new Intent("android.intent.action.PICK", null);
    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    try {
      paramActivity.startActivityForResult(intent, paramInt);
      bool = false;
    } catch (ActivityNotFoundException activityNotFoundException) {
      bool = true;
    } 
    return bool;
  }
  
  private static boolean launchSys(Fragment paramFragment, int paramInt) {
    boolean bool;
    Intent intent = new Intent("android.intent.action.PICK", null);
    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    try {
      paramFragment.startActivityForResult(intent, paramInt);
      bool = false;
    } catch (ActivityNotFoundException activityNotFoundException) {
      bool = true;
    } 
    return bool;
  }
  
  public static void startCrop(Activity paramActivity, String paramString, int paramInt, boolean paramBoolean) {
    char c;
    File file = new File(paramString);
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(Uri.fromFile(file), "image/*");
    intent.putExtra("crop", "true");
    intent.putExtra("aspectX", 4);
    intent.putExtra("aspectY", 3);
    if (paramBoolean) {
      c = 'Ɛ';
    } else {
      c = 'È';
    } 
    intent.putExtra("outputX", c);
    if (paramBoolean) {
      c = 'Ĭ';
    } else {
      c = '';
    } 
    intent.putExtra("outputY", c);
    intent.putExtra("scale", true);
    intent.putExtra("return-data", true);
    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    intent.putExtra("noFaceDetection", true);
    paramActivity.startActivityForResult(intent, paramInt);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/utils/PhotoPicker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */