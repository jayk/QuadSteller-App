package com.blankj.utilcode.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class TimeUtils {
  private static final String[] CHINESE_ZODIAC;
  
  private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
  
  private static final String[] ZODIAC;
  
  private static final int[] ZODIAC_FLAGS;
  
  static {
    CHINESE_ZODIAC = new String[] { 
        "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", 
        "马", "羊" };
    ZODIAC = new String[] { 
        "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", 
        "射手座", "魔羯座" };
    ZODIAC_FLAGS = new int[] { 
        20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 
        23, 22 };
  }
  
  private TimeUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }
  
  public static long date2Millis(Date paramDate) {
    return paramDate.getTime();
  }
  
  public static String date2String(Date paramDate) {
    return date2String(paramDate, DEFAULT_FORMAT);
  }
  
  public static String date2String(Date paramDate, DateFormat paramDateFormat) {
    return paramDateFormat.format(paramDate);
  }
  
  public static String getChineseWeek(long paramLong) {
    return getChineseWeek(new Date(paramLong));
  }
  
  public static String getChineseWeek(String paramString) {
    return getChineseWeek(string2Date(paramString, DEFAULT_FORMAT));
  }
  
  public static String getChineseWeek(String paramString, DateFormat paramDateFormat) {
    return getChineseWeek(string2Date(paramString, paramDateFormat));
  }
  
  public static String getChineseWeek(Date paramDate) {
    return (new SimpleDateFormat("E", Locale.CHINA)).format(paramDate);
  }
  
  public static String getChineseZodiac(int paramInt) {
    return CHINESE_ZODIAC[paramInt % 12];
  }
  
  public static String getChineseZodiac(long paramLong) {
    return getChineseZodiac(millis2Date(paramLong));
  }
  
  public static String getChineseZodiac(String paramString) {
    return getChineseZodiac(string2Date(paramString, DEFAULT_FORMAT));
  }
  
  public static String getChineseZodiac(String paramString, DateFormat paramDateFormat) {
    return getChineseZodiac(string2Date(paramString, paramDateFormat));
  }
  
  public static String getChineseZodiac(Date paramDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(paramDate);
    return CHINESE_ZODIAC[calendar.get(1) % 12];
  }
  
  public static Date getDate(long paramLong1, long paramLong2, int paramInt) {
    return millis2Date(timeSpan2Millis(paramLong2, paramInt) + paramLong1);
  }
  
  public static Date getDate(String paramString, long paramLong, int paramInt) {
    return getDate(paramString, DEFAULT_FORMAT, paramLong, paramInt);
  }
  
  public static Date getDate(String paramString, DateFormat paramDateFormat, long paramLong, int paramInt) {
    return millis2Date(string2Millis(paramString, paramDateFormat) + timeSpan2Millis(paramLong, paramInt));
  }
  
  public static Date getDate(Date paramDate, long paramLong, int paramInt) {
    return millis2Date(date2Millis(paramDate) + timeSpan2Millis(paramLong, paramInt));
  }
  
  public static Date getDateByNow(long paramLong, int paramInt) {
    return getDate(getNowMills(), paramLong, paramInt);
  }
  
  public static String getFitTimeSpan(long paramLong1, long paramLong2, int paramInt) {
    return millis2FitTimeSpan(Math.abs(paramLong1 - paramLong2), paramInt);
  }
  
  public static String getFitTimeSpan(String paramString1, String paramString2, int paramInt) {
    return millis2FitTimeSpan(Math.abs(string2Millis(paramString1, DEFAULT_FORMAT) - string2Millis(paramString2, DEFAULT_FORMAT)), paramInt);
  }
  
  public static String getFitTimeSpan(String paramString1, String paramString2, DateFormat paramDateFormat, int paramInt) {
    return millis2FitTimeSpan(Math.abs(string2Millis(paramString1, paramDateFormat) - string2Millis(paramString2, paramDateFormat)), paramInt);
  }
  
  public static String getFitTimeSpan(Date paramDate1, Date paramDate2, int paramInt) {
    return millis2FitTimeSpan(Math.abs(date2Millis(paramDate1) - date2Millis(paramDate2)), paramInt);
  }
  
  public static String getFitTimeSpanByNow(long paramLong, int paramInt) {
    return getFitTimeSpan(System.currentTimeMillis(), paramLong, paramInt);
  }
  
  public static String getFitTimeSpanByNow(String paramString, int paramInt) {
    return getFitTimeSpan(getNowString(), paramString, DEFAULT_FORMAT, paramInt);
  }
  
  public static String getFitTimeSpanByNow(String paramString, DateFormat paramDateFormat, int paramInt) {
    return getFitTimeSpan(getNowString(paramDateFormat), paramString, paramDateFormat, paramInt);
  }
  
  public static String getFitTimeSpanByNow(Date paramDate, int paramInt) {
    return getFitTimeSpan(getNowDate(), paramDate, paramInt);
  }
  
  public static String getFriendlyTimeSpanByNow(long paramLong) {
    long l = System.currentTimeMillis() - paramLong;
    if (l < 0L)
      return String.format("%tc", new Object[] { Long.valueOf(paramLong) }); 
    if (l < 1000L)
      return "刚刚"; 
    if (l < 60000L)
      return String.format(Locale.getDefault(), "%d秒前", new Object[] { Long.valueOf(l / 1000L) }); 
    if (l < 3600000L)
      return String.format(Locale.getDefault(), "%d分钟前", new Object[] { Long.valueOf(l / 60000L) }); 
    l = getWeeOfToday();
    return (paramLong >= l) ? String.format("今天%tR", new Object[] { Long.valueOf(paramLong) }) : ((paramLong >= l - 86400000L) ? String.format("昨天%tR", new Object[] { Long.valueOf(paramLong) }) : String.format("%tF", new Object[] { Long.valueOf(paramLong) }));
  }
  
  public static String getFriendlyTimeSpanByNow(String paramString) {
    return getFriendlyTimeSpanByNow(paramString, DEFAULT_FORMAT);
  }
  
  public static String getFriendlyTimeSpanByNow(String paramString, DateFormat paramDateFormat) {
    return getFriendlyTimeSpanByNow(string2Millis(paramString, paramDateFormat));
  }
  
  public static String getFriendlyTimeSpanByNow(Date paramDate) {
    return getFriendlyTimeSpanByNow(paramDate.getTime());
  }
  
  public static long getMillis(long paramLong1, long paramLong2, int paramInt) {
    return timeSpan2Millis(paramLong2, paramInt) + paramLong1;
  }
  
  public static long getMillis(String paramString, long paramLong, int paramInt) {
    return getMillis(paramString, DEFAULT_FORMAT, paramLong, paramInt);
  }
  
  public static long getMillis(String paramString, DateFormat paramDateFormat, long paramLong, int paramInt) {
    return string2Millis(paramString, paramDateFormat) + timeSpan2Millis(paramLong, paramInt);
  }
  
  public static long getMillis(Date paramDate, long paramLong, int paramInt) {
    return date2Millis(paramDate) + timeSpan2Millis(paramLong, paramInt);
  }
  
  public static long getMillisByNow(long paramLong, int paramInt) {
    return getMillis(getNowMills(), paramLong, paramInt);
  }
  
  public static Date getNowDate() {
    return new Date();
  }
  
  public static long getNowMills() {
    return System.currentTimeMillis();
  }
  
  public static String getNowString() {
    return millis2String(System.currentTimeMillis(), DEFAULT_FORMAT);
  }
  
  public static String getNowString(DateFormat paramDateFormat) {
    return millis2String(System.currentTimeMillis(), paramDateFormat);
  }
  
  public static String getString(long paramLong1, long paramLong2, int paramInt) {
    return getString(paramLong1, DEFAULT_FORMAT, paramLong2, paramInt);
  }
  
  public static String getString(long paramLong1, DateFormat paramDateFormat, long paramLong2, int paramInt) {
    return millis2String(timeSpan2Millis(paramLong2, paramInt) + paramLong1, paramDateFormat);
  }
  
  public static String getString(String paramString, long paramLong, int paramInt) {
    return getString(paramString, DEFAULT_FORMAT, paramLong, paramInt);
  }
  
  public static String getString(String paramString, DateFormat paramDateFormat, long paramLong, int paramInt) {
    return millis2String(string2Millis(paramString, paramDateFormat) + timeSpan2Millis(paramLong, paramInt), paramDateFormat);
  }
  
  public static String getString(Date paramDate, long paramLong, int paramInt) {
    return getString(paramDate, DEFAULT_FORMAT, paramLong, paramInt);
  }
  
  public static String getString(Date paramDate, DateFormat paramDateFormat, long paramLong, int paramInt) {
    return millis2String(date2Millis(paramDate) + timeSpan2Millis(paramLong, paramInt), paramDateFormat);
  }
  
  public static String getStringByNow(long paramLong, int paramInt) {
    return getStringByNow(paramLong, DEFAULT_FORMAT, paramInt);
  }
  
  public static String getStringByNow(long paramLong, DateFormat paramDateFormat, int paramInt) {
    return getString(getNowMills(), paramDateFormat, paramLong, paramInt);
  }
  
  public static long getTimeSpan(long paramLong1, long paramLong2, int paramInt) {
    return millis2TimeSpan(Math.abs(paramLong1 - paramLong2), paramInt);
  }
  
  public static long getTimeSpan(String paramString1, String paramString2, int paramInt) {
    return getTimeSpan(paramString1, paramString2, DEFAULT_FORMAT, paramInt);
  }
  
  public static long getTimeSpan(String paramString1, String paramString2, DateFormat paramDateFormat, int paramInt) {
    return millis2TimeSpan(Math.abs(string2Millis(paramString1, paramDateFormat) - string2Millis(paramString2, paramDateFormat)), paramInt);
  }
  
  public static long getTimeSpan(Date paramDate1, Date paramDate2, int paramInt) {
    return millis2TimeSpan(Math.abs(date2Millis(paramDate1) - date2Millis(paramDate2)), paramInt);
  }
  
  public static long getTimeSpanByNow(long paramLong, int paramInt) {
    return getTimeSpan(System.currentTimeMillis(), paramLong, paramInt);
  }
  
  public static long getTimeSpanByNow(String paramString, int paramInt) {
    return getTimeSpan(getNowString(), paramString, DEFAULT_FORMAT, paramInt);
  }
  
  public static long getTimeSpanByNow(String paramString, DateFormat paramDateFormat, int paramInt) {
    return getTimeSpan(getNowString(paramDateFormat), paramString, paramDateFormat, paramInt);
  }
  
  public static long getTimeSpanByNow(Date paramDate, int paramInt) {
    return getTimeSpan(new Date(), paramDate, paramInt);
  }
  
  public static String getUSWeek(long paramLong) {
    return getUSWeek(new Date(paramLong));
  }
  
  public static String getUSWeek(String paramString) {
    return getUSWeek(string2Date(paramString, DEFAULT_FORMAT));
  }
  
  public static String getUSWeek(String paramString, DateFormat paramDateFormat) {
    return getUSWeek(string2Date(paramString, paramDateFormat));
  }
  
  public static String getUSWeek(Date paramDate) {
    return (new SimpleDateFormat("EEEE", Locale.US)).format(paramDate);
  }
  
  private static long getWeeOfToday() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(11, 0);
    calendar.set(13, 0);
    calendar.set(12, 0);
    calendar.set(14, 0);
    return calendar.getTimeInMillis();
  }
  
  public static int getWeekIndex(long paramLong) {
    return getWeekIndex(millis2Date(paramLong));
  }
  
  public static int getWeekIndex(String paramString) {
    return getWeekIndex(string2Date(paramString, DEFAULT_FORMAT));
  }
  
  public static int getWeekIndex(String paramString, DateFormat paramDateFormat) {
    return getWeekIndex(string2Date(paramString, paramDateFormat));
  }
  
  public static int getWeekIndex(Date paramDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(paramDate);
    return calendar.get(7);
  }
  
  public static int getWeekOfMonth(long paramLong) {
    return getWeekOfMonth(millis2Date(paramLong));
  }
  
  public static int getWeekOfMonth(String paramString) {
    return getWeekOfMonth(string2Date(paramString, DEFAULT_FORMAT));
  }
  
  public static int getWeekOfMonth(String paramString, DateFormat paramDateFormat) {
    return getWeekOfMonth(string2Date(paramString, paramDateFormat));
  }
  
  public static int getWeekOfMonth(Date paramDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(paramDate);
    return calendar.get(4);
  }
  
  public static int getWeekOfYear(long paramLong) {
    return getWeekOfYear(millis2Date(paramLong));
  }
  
  public static int getWeekOfYear(String paramString) {
    return getWeekOfYear(string2Date(paramString, DEFAULT_FORMAT));
  }
  
  public static int getWeekOfYear(String paramString, DateFormat paramDateFormat) {
    return getWeekOfYear(string2Date(paramString, paramDateFormat));
  }
  
  public static int getWeekOfYear(Date paramDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(paramDate);
    return calendar.get(3);
  }
  
  public static String getZodiac(int paramInt1, int paramInt2) {
    String[] arrayOfString = ZODIAC;
    if (paramInt2 >= ZODIAC_FLAGS[paramInt1 - 1])
      return arrayOfString[--paramInt1]; 
    paramInt1 = (paramInt1 + 10) % 12;
    return arrayOfString[paramInt1];
  }
  
  public static String getZodiac(long paramLong) {
    return getZodiac(millis2Date(paramLong));
  }
  
  public static String getZodiac(String paramString) {
    return getZodiac(string2Date(paramString, DEFAULT_FORMAT));
  }
  
  public static String getZodiac(String paramString, DateFormat paramDateFormat) {
    return getZodiac(string2Date(paramString, paramDateFormat));
  }
  
  public static String getZodiac(Date paramDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(paramDate);
    return getZodiac(calendar.get(2) + 1, calendar.get(5));
  }
  
  public static boolean isLeapYear(int paramInt) {
    return ((paramInt % 4 == 0 && paramInt % 100 != 0) || paramInt % 400 == 0);
  }
  
  public static boolean isLeapYear(long paramLong) {
    return isLeapYear(millis2Date(paramLong));
  }
  
  public static boolean isLeapYear(String paramString) {
    return isLeapYear(string2Date(paramString, DEFAULT_FORMAT));
  }
  
  public static boolean isLeapYear(String paramString, DateFormat paramDateFormat) {
    return isLeapYear(string2Date(paramString, paramDateFormat));
  }
  
  public static boolean isLeapYear(Date paramDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(paramDate);
    return isLeapYear(calendar.get(1));
  }
  
  public static boolean isToday(long paramLong) {
    long l = getWeeOfToday();
    return (paramLong >= l && paramLong < 86400000L + l);
  }
  
  public static boolean isToday(String paramString) {
    return isToday(string2Millis(paramString, DEFAULT_FORMAT));
  }
  
  public static boolean isToday(String paramString, DateFormat paramDateFormat) {
    return isToday(string2Millis(paramString, paramDateFormat));
  }
  
  public static boolean isToday(Date paramDate) {
    return isToday(paramDate.getTime());
  }
  
  public static Date millis2Date(long paramLong) {
    return new Date(paramLong);
  }
  
  private static String millis2FitTimeSpan(long paramLong, int paramInt) {
    if (paramLong < 0L || paramInt <= 0)
      return null; 
    int i = Math.min(paramInt, 5);
    String[] arrayOfString = new String[5];
    arrayOfString[0] = "天";
    arrayOfString[1] = "小时";
    arrayOfString[2] = "分钟";
    arrayOfString[3] = "秒";
    arrayOfString[4] = "毫秒";
    if (paramLong == 0L)
      return Character.MIN_VALUE + arrayOfString[i - 1]; 
    StringBuilder stringBuilder = new StringBuilder();
    int[] arrayOfInt = new int[5];
    arrayOfInt[0] = 86400000;
    arrayOfInt[1] = 3600000;
    arrayOfInt[2] = 60000;
    arrayOfInt[3] = 1000;
    arrayOfInt[4] = 1;
    paramInt = 0;
    while (paramInt < i) {
      long l = paramLong;
      if (paramLong >= arrayOfInt[paramInt]) {
        long l1 = paramLong / arrayOfInt[paramInt];
        l = paramLong - arrayOfInt[paramInt] * l1;
        stringBuilder.append(l1).append(arrayOfString[paramInt]);
      } 
      paramInt++;
      paramLong = l;
    } 
    return stringBuilder.toString();
  }
  
  public static String millis2String(long paramLong) {
    return millis2String(paramLong, DEFAULT_FORMAT);
  }
  
  public static String millis2String(long paramLong, DateFormat paramDateFormat) {
    return paramDateFormat.format(new Date(paramLong));
  }
  
  private static long millis2TimeSpan(long paramLong, int paramInt) {
    return paramLong / paramInt;
  }
  
  public static Date string2Date(String paramString) {
    return string2Date(paramString, DEFAULT_FORMAT);
  }
  
  public static Date string2Date(String paramString, DateFormat paramDateFormat) {
    try {
      Date date = paramDateFormat.parse(paramString);
    } catch (ParseException parseException) {
      parseException.printStackTrace();
      parseException = null;
    } 
    return (Date)parseException;
  }
  
  public static long string2Millis(String paramString) {
    return string2Millis(paramString, DEFAULT_FORMAT);
  }
  
  public static long string2Millis(String paramString, DateFormat paramDateFormat) {
    long l;
    try {
      l = paramDateFormat.parse(paramString).getTime();
    } catch (ParseException parseException) {
      parseException.printStackTrace();
      l = -1L;
    } 
    return l;
  }
  
  private static long timeSpan2Millis(long paramLong, int paramInt) {
    return paramInt * paramLong;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/com/blankj/utilcode/util/TimeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */