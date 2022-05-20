package com.blankj.utilcode.util;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Xml;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlSerializer;

public final class PhoneUtils {
  private PhoneUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static void call(String paramString) {
    Utils.getApp().startActivity(IntentUtils.getCallIntent(paramString));
  }
  
  public static void dial(String paramString) {
    Utils.getApp().startActivity(IntentUtils.getDialIntent(paramString));
  }
  
  public static List<HashMap<String, String>> getAllContactInfo() {
    SystemClock.sleep(3000L);
    ArrayList<HashMap<Object, Object>> arrayList = new ArrayList();
    ContentResolver contentResolver = Utils.getApp().getContentResolver();
    Uri uri1 = Uri.parse("content://com.android.contacts/raw_contacts");
    Uri uri2 = Uri.parse("content://com.android.contacts/data");
    Cursor cursor = contentResolver.query(uri1, new String[] { "contact_id" }, null, null, null);
    if (cursor != null)
      while (true) {
        try {
          if (cursor.moveToNext()) {
            String str = cursor.getString(0);
            if (!StringUtils.isEmpty(str)) {
              Cursor cursor1 = contentResolver.query(uri2, new String[] { "data1", "mimetype" }, "raw_contact_id=?", new String[] { str }, null);
              HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
              this();
              if (cursor1 != null)
                while (cursor1.moveToNext()) {
                  String str1 = cursor1.getString(0);
                  String str2 = cursor1.getString(1);
                  if (str2.equals("vnd.android.cursor.item/phone_v2")) {
                    hashMap.put("phone", str1);
                    continue;
                  } 
                  if (str2.equals("vnd.android.cursor.item/name"))
                    hashMap.put("name", str1); 
                }  
              arrayList.add(hashMap);
              if (cursor1 != null)
                cursor1.close(); 
            } 
            continue;
          } 
        } finally {
          if (cursor != null)
            cursor.close(); 
        } 
      }  
    if (cursor != null)
      cursor.close(); 
    return (List)arrayList;
  }
  
  public static void getAllSMS() {
    Cursor cursor = Utils.getApp().getContentResolver().query(Uri.parse("content://sms"), new String[] { "address", "date", "type", "body" }, null, null, null);
    cursor.getCount();
    XmlSerializer xmlSerializer = Xml.newSerializer();
    try {
      FileOutputStream fileOutputStream = new FileOutputStream();
      File file = new File();
      this("/mnt/sdcard/backupsms.xml");
      this(file);
      xmlSerializer.setOutput(fileOutputStream, "utf-8");
      xmlSerializer.startDocument("utf-8", Boolean.valueOf(true));
      xmlSerializer.startTag(null, "smss");
      while (cursor.moveToNext()) {
        SystemClock.sleep(1000L);
        xmlSerializer.startTag(null, "sms");
        xmlSerializer.startTag(null, "address");
        String str2 = cursor.getString(0);
        xmlSerializer.text(str2);
        xmlSerializer.endTag(null, "address");
        xmlSerializer.startTag(null, "date");
        String str3 = cursor.getString(1);
        xmlSerializer.text(str3);
        xmlSerializer.endTag(null, "date");
        xmlSerializer.startTag(null, "type");
        String str4 = cursor.getString(2);
        xmlSerializer.text(str4);
        xmlSerializer.endTag(null, "type");
        xmlSerializer.startTag(null, "body");
        String str1 = cursor.getString(3);
        xmlSerializer.text(str1);
        xmlSerializer.endTag(null, "body");
        xmlSerializer.endTag(null, "sms");
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        this();
        printStream.println(stringBuilder.append("address:").append(str2).append("   date:").append(str3).append("  type:").append(str4).append("  body:").append(str1).toString());
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
      return;
    } 
    exception.endTag(null, "smss");
    exception.endDocument();
    exception.flush();
  }
  
  public static void getContactNum() {
    Log.d("tips", "U should copy the following code.");
  }
  
  @SuppressLint({"HardwareIds"})
  public static String getIMEI() {
    null = (TelephonyManager)Utils.getApp().getSystemService("phone");
    return (null != null) ? null.getDeviceId() : null;
  }
  
  @SuppressLint({"HardwareIds"})
  public static String getIMSI() {
    null = (TelephonyManager)Utils.getApp().getSystemService("phone");
    return (null != null) ? null.getSubscriberId() : null;
  }
  
  @SuppressLint({"HardwareIds"})
  public static String getPhoneStatus() {
    TelephonyManager telephonyManager = (TelephonyManager)Utils.getApp().getSystemService("phone");
    String str = "" + "DeviceId(IMEI) = " + telephonyManager.getDeviceId() + "\n";
    str = str + "DeviceSoftwareVersion = " + telephonyManager.getDeviceSoftwareVersion() + "\n";
    str = str + "Line1Number = " + telephonyManager.getLine1Number() + "\n";
    str = str + "NetworkCountryIso = " + telephonyManager.getNetworkCountryIso() + "\n";
    str = str + "NetworkOperator = " + telephonyManager.getNetworkOperator() + "\n";
    str = str + "NetworkOperatorName = " + telephonyManager.getNetworkOperatorName() + "\n";
    str = str + "NetworkType = " + telephonyManager.getNetworkType() + "\n";
    str = str + "PhoneType = " + telephonyManager.getPhoneType() + "\n";
    str = str + "SimCountryIso = " + telephonyManager.getSimCountryIso() + "\n";
    str = str + "SimOperator = " + telephonyManager.getSimOperator() + "\n";
    str = str + "SimOperatorName = " + telephonyManager.getSimOperatorName() + "\n";
    str = str + "SimSerialNumber = " + telephonyManager.getSimSerialNumber() + "\n";
    str = str + "SimState = " + telephonyManager.getSimState() + "\n";
    str = str + "SubscriberId(IMSI) = " + telephonyManager.getSubscriberId() + "\n";
    return str + "VoiceMailNumber = " + telephonyManager.getVoiceMailNumber() + "\n";
  }
  
  public static int getPhoneType() {
    TelephonyManager telephonyManager = (TelephonyManager)Utils.getApp().getSystemService("phone");
    return (telephonyManager != null) ? telephonyManager.getPhoneType() : -1;
  }
  
  public static String getSimOperatorByMnc() {
    TelephonyManager telephonyManager = (TelephonyManager)Utils.getApp().getSystemService("phone");
    if (telephonyManager != null) {
      String str1 = telephonyManager.getSimOperator();
    } else {
      telephonyManager = null;
    } 
    if (telephonyManager == null)
      telephonyManager = null; 
    byte b1 = -1;
    byte b2 = b1;
    switch (telephonyManager.hashCode()) {
      default:
        b2 = b1;
      case 49679474:
      case 49679475:
      case 49679476:
        switch (b2) {
          default:
            return (String)telephonyManager;
          case 0:
          case 1:
          case 2:
            str = "中国移动";
          case 3:
            str = "中国联通";
          case 4:
            break;
        } 
        break;
      case 49679470:
        b2 = b1;
        if (str.equals("46000"))
          b2 = 0; 
      case 49679472:
        b2 = b1;
        if (str.equals("46002"))
          b2 = 1; 
      case 49679477:
        b2 = b1;
        if (str.equals("46007"))
          b2 = 2; 
      case 49679471:
        b2 = b1;
        if (str.equals("46001"))
          b2 = 3; 
      case 49679473:
        b2 = b1;
        if (str.equals("46003"))
          b2 = 4; 
    } 
    String str = "中国电信";
  }
  
  public static String getSimOperatorName() {
    null = (TelephonyManager)Utils.getApp().getSystemService("phone");
    return (null != null) ? null.getSimOperatorName() : null;
  }
  
  public static boolean isPhone() {
    TelephonyManager telephonyManager = (TelephonyManager)Utils.getApp().getSystemService("phone");
    return (telephonyManager != null && telephonyManager.getPhoneType() != 0);
  }
  
  public static boolean isSimCardReady() {
    TelephonyManager telephonyManager = (TelephonyManager)Utils.getApp().getSystemService("phone");
    return (telephonyManager != null && telephonyManager.getSimState() == 5);
  }
  
  public static void sendSms(String paramString1, String paramString2) {
    Utils.getApp().startActivity(IntentUtils.getSendSmsIntent(paramString1, paramString2));
  }
  
  public static void sendSmsSilent(String paramString1, String paramString2) {
    if (!StringUtils.isEmpty(paramString2)) {
      Iterator<String> iterator;
      PendingIntent pendingIntent = PendingIntent.getBroadcast((Context)Utils.getApp(), 0, new Intent(), 0);
      SmsManager smsManager = SmsManager.getDefault();
      if (paramString2.length() >= 70) {
        iterator = smsManager.divideMessage(paramString2).iterator();
        while (true) {
          if (iterator.hasNext()) {
            smsManager.sendTextMessage(paramString1, null, iterator.next(), pendingIntent, null);
            continue;
          } 
          return;
        } 
      } 
      smsManager.sendTextMessage(paramString1, null, (String)iterator, pendingIntent, null);
    } 
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/PhoneUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */