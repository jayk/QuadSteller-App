package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import java.util.ArrayList;

@TargetApi(21)
@RequiresApi(21)
class NotificationCompatApi21 {
  public static final String CATEGORY_ALARM = "alarm";
  
  public static final String CATEGORY_CALL = "call";
  
  public static final String CATEGORY_EMAIL = "email";
  
  public static final String CATEGORY_ERROR = "err";
  
  public static final String CATEGORY_EVENT = "event";
  
  public static final String CATEGORY_MESSAGE = "msg";
  
  public static final String CATEGORY_PROGRESS = "progress";
  
  public static final String CATEGORY_PROMO = "promo";
  
  public static final String CATEGORY_RECOMMENDATION = "recommendation";
  
  public static final String CATEGORY_SERVICE = "service";
  
  public static final String CATEGORY_SOCIAL = "social";
  
  public static final String CATEGORY_STATUS = "status";
  
  public static final String CATEGORY_SYSTEM = "sys";
  
  public static final String CATEGORY_TRANSPORT = "transport";
  
  private static final String KEY_AUTHOR = "author";
  
  private static final String KEY_MESSAGES = "messages";
  
  private static final String KEY_ON_READ = "on_read";
  
  private static final String KEY_ON_REPLY = "on_reply";
  
  private static final String KEY_PARTICIPANTS = "participants";
  
  private static final String KEY_REMOTE_INPUT = "remote_input";
  
  private static final String KEY_TEXT = "text";
  
  private static final String KEY_TIMESTAMP = "timestamp";
  
  private static RemoteInput fromCompatRemoteInput(RemoteInputCompatBase.RemoteInput paramRemoteInput) {
    return (new RemoteInput.Builder(paramRemoteInput.getResultKey())).setLabel(paramRemoteInput.getLabel()).setChoices(paramRemoteInput.getChoices()).setAllowFreeFormInput(paramRemoteInput.getAllowFreeFormInput()).addExtras(paramRemoteInput.getExtras()).build();
  }
  
  static Bundle getBundleForUnreadConversation(NotificationCompatBase.UnreadConversation paramUnreadConversation) {
    if (paramUnreadConversation == null)
      return null; 
    Bundle bundle = new Bundle();
    String str1 = null;
    String str2 = str1;
    if (paramUnreadConversation.getParticipants() != null) {
      str2 = str1;
      if ((paramUnreadConversation.getParticipants()).length > 1)
        str2 = paramUnreadConversation.getParticipants()[0]; 
    } 
    Parcelable[] arrayOfParcelable = new Parcelable[(paramUnreadConversation.getMessages()).length];
    for (byte b = 0; b < arrayOfParcelable.length; b++) {
      Bundle bundle1 = new Bundle();
      bundle1.putString("text", paramUnreadConversation.getMessages()[b]);
      bundle1.putString("author", str2);
      arrayOfParcelable[b] = (Parcelable)bundle1;
    } 
    bundle.putParcelableArray("messages", arrayOfParcelable);
    RemoteInputCompatBase.RemoteInput remoteInput = paramUnreadConversation.getRemoteInput();
    if (remoteInput != null)
      bundle.putParcelable("remote_input", (Parcelable)fromCompatRemoteInput(remoteInput)); 
    bundle.putParcelable("on_reply", (Parcelable)paramUnreadConversation.getReplyPendingIntent());
    bundle.putParcelable("on_read", (Parcelable)paramUnreadConversation.getReadPendingIntent());
    bundle.putStringArray("participants", paramUnreadConversation.getParticipants());
    bundle.putLong("timestamp", paramUnreadConversation.getLatestTimestamp());
    return bundle;
  }
  
  public static String getCategory(Notification paramNotification) {
    return paramNotification.category;
  }
  
  static NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(Bundle paramBundle, NotificationCompatBase.UnreadConversation.Factory paramFactory, RemoteInputCompatBase.RemoteInput.Factory paramFactory1) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore #4
    //   5: aload_0
    //   6: ifnonnull -> 16
    //   9: aload #4
    //   11: astore #5
    //   13: aload #5
    //   15: areturn
    //   16: aload_0
    //   17: ldc 'messages'
    //   19: invokevirtual getParcelableArray : (Ljava/lang/String;)[Landroid/os/Parcelable;
    //   22: astore #5
    //   24: aconst_null
    //   25: astore #6
    //   27: aload #5
    //   29: ifnull -> 81
    //   32: aload #5
    //   34: arraylength
    //   35: anewarray java/lang/String
    //   38: astore #6
    //   40: iconst_1
    //   41: istore #7
    //   43: iconst_0
    //   44: istore #8
    //   46: iload #7
    //   48: istore #9
    //   50: iload #8
    //   52: aload #6
    //   54: arraylength
    //   55: if_icmpge -> 72
    //   58: aload #5
    //   60: iload #8
    //   62: aaload
    //   63: instanceof android/os/Bundle
    //   66: ifne -> 185
    //   69: iconst_0
    //   70: istore #9
    //   72: aload #4
    //   74: astore #5
    //   76: iload #9
    //   78: ifeq -> 13
    //   81: aload_0
    //   82: ldc 'on_read'
    //   84: invokevirtual getParcelable : (Ljava/lang/String;)Landroid/os/Parcelable;
    //   87: checkcast android/app/PendingIntent
    //   90: astore #10
    //   92: aload_0
    //   93: ldc 'on_reply'
    //   95: invokevirtual getParcelable : (Ljava/lang/String;)Landroid/os/Parcelable;
    //   98: checkcast android/app/PendingIntent
    //   101: astore #11
    //   103: aload_0
    //   104: ldc 'remote_input'
    //   106: invokevirtual getParcelable : (Ljava/lang/String;)Landroid/os/Parcelable;
    //   109: checkcast android/app/RemoteInput
    //   112: astore #12
    //   114: aload_0
    //   115: ldc 'participants'
    //   117: invokevirtual getStringArray : (Ljava/lang/String;)[Ljava/lang/String;
    //   120: astore #13
    //   122: aload #4
    //   124: astore #5
    //   126: aload #13
    //   128: ifnull -> 13
    //   131: aload #4
    //   133: astore #5
    //   135: aload #13
    //   137: arraylength
    //   138: iconst_1
    //   139: if_icmpne -> 13
    //   142: aload_3
    //   143: astore #5
    //   145: aload #12
    //   147: ifnull -> 158
    //   150: aload #12
    //   152: aload_2
    //   153: invokestatic toCompatRemoteInput : (Landroid/app/RemoteInput;Landroid/support/v4/app/RemoteInputCompatBase$RemoteInput$Factory;)Landroid/support/v4/app/RemoteInputCompatBase$RemoteInput;
    //   156: astore #5
    //   158: aload_1
    //   159: aload #6
    //   161: aload #5
    //   163: aload #11
    //   165: aload #10
    //   167: aload #13
    //   169: aload_0
    //   170: ldc 'timestamp'
    //   172: invokevirtual getLong : (Ljava/lang/String;)J
    //   175: invokeinterface build : ([Ljava/lang/String;Landroid/support/v4/app/RemoteInputCompatBase$RemoteInput;Landroid/app/PendingIntent;Landroid/app/PendingIntent;[Ljava/lang/String;J)Landroid/support/v4/app/NotificationCompatBase$UnreadConversation;
    //   180: astore #5
    //   182: goto -> 13
    //   185: aload #6
    //   187: iload #8
    //   189: aload #5
    //   191: iload #8
    //   193: aaload
    //   194: checkcast android/os/Bundle
    //   197: ldc 'text'
    //   199: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   202: aastore
    //   203: aload #6
    //   205: iload #8
    //   207: aaload
    //   208: ifnonnull -> 217
    //   211: iconst_0
    //   212: istore #9
    //   214: goto -> 72
    //   217: iinc #8, 1
    //   220: goto -> 46
  }
  
  private static RemoteInputCompatBase.RemoteInput toCompatRemoteInput(RemoteInput paramRemoteInput, RemoteInputCompatBase.RemoteInput.Factory paramFactory) {
    return paramFactory.build(paramRemoteInput.getResultKey(), paramRemoteInput.getLabel(), paramRemoteInput.getChoices(), paramRemoteInput.getAllowFreeFormInput(), paramRemoteInput.getExtras());
  }
  
  public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
    private Notification.Builder b;
    
    private RemoteViews mBigContentView;
    
    private RemoteViews mContentView;
    
    private Bundle mExtras;
    
    private RemoteViews mHeadsUpContentView;
    
    public Builder(Context param1Context, Notification param1Notification1, CharSequence param1CharSequence1, CharSequence param1CharSequence2, CharSequence param1CharSequence3, RemoteViews param1RemoteViews1, int param1Int1, PendingIntent param1PendingIntent1, PendingIntent param1PendingIntent2, Bitmap param1Bitmap, int param1Int2, int param1Int3, boolean param1Boolean1, boolean param1Boolean2, boolean param1Boolean3, int param1Int4, CharSequence param1CharSequence4, boolean param1Boolean4, String param1String1, ArrayList<String> param1ArrayList, Bundle param1Bundle, int param1Int5, int param1Int6, Notification param1Notification2, String param1String2, boolean param1Boolean5, String param1String3, RemoteViews param1RemoteViews2, RemoteViews param1RemoteViews3, RemoteViews param1RemoteViews4) {
      Notification.Builder builder = (new Notification.Builder(param1Context)).setWhen(param1Notification1.when).setShowWhen(param1Boolean2).setSmallIcon(param1Notification1.icon, param1Notification1.iconLevel).setContent(param1Notification1.contentView).setTicker(param1Notification1.tickerText, param1RemoteViews1).setSound(param1Notification1.sound, param1Notification1.audioStreamType).setVibrate(param1Notification1.vibrate).setLights(param1Notification1.ledARGB, param1Notification1.ledOnMS, param1Notification1.ledOffMS);
      if ((param1Notification1.flags & 0x2) != 0) {
        param1Boolean2 = true;
      } else {
        param1Boolean2 = false;
      } 
      builder = builder.setOngoing(param1Boolean2);
      if ((param1Notification1.flags & 0x8) != 0) {
        param1Boolean2 = true;
      } else {
        param1Boolean2 = false;
      } 
      builder = builder.setOnlyAlertOnce(param1Boolean2);
      if ((param1Notification1.flags & 0x10) != 0) {
        param1Boolean2 = true;
      } else {
        param1Boolean2 = false;
      } 
      builder = builder.setAutoCancel(param1Boolean2).setDefaults(param1Notification1.defaults).setContentTitle(param1CharSequence1).setContentText(param1CharSequence2).setSubText(param1CharSequence4).setContentInfo(param1CharSequence3).setContentIntent(param1PendingIntent1).setDeleteIntent(param1Notification1.deleteIntent);
      if ((param1Notification1.flags & 0x80) != 0) {
        param1Boolean2 = true;
      } else {
        param1Boolean2 = false;
      } 
      this.b = builder.setFullScreenIntent(param1PendingIntent2, param1Boolean2).setLargeIcon(param1Bitmap).setNumber(param1Int1).setUsesChronometer(param1Boolean3).setPriority(param1Int4).setProgress(param1Int2, param1Int3, param1Boolean1).setLocalOnly(param1Boolean4).setGroup(param1String2).setGroupSummary(param1Boolean5).setSortKey(param1String3).setCategory(param1String1).setColor(param1Int5).setVisibility(param1Int6).setPublicVersion(param1Notification2);
      this.mExtras = new Bundle();
      if (param1Bundle != null)
        this.mExtras.putAll(param1Bundle); 
      for (String str : param1ArrayList)
        this.b.addPerson(str); 
      this.mContentView = param1RemoteViews2;
      this.mBigContentView = param1RemoteViews3;
      this.mHeadsUpContentView = param1RemoteViews4;
    }
    
    public void addAction(NotificationCompatBase.Action param1Action) {
      NotificationCompatApi20.addAction(this.b, param1Action);
    }
    
    public Notification build() {
      this.b.setExtras(this.mExtras);
      Notification notification = this.b.build();
      if (this.mContentView != null)
        notification.contentView = this.mContentView; 
      if (this.mBigContentView != null)
        notification.bigContentView = this.mBigContentView; 
      if (this.mHeadsUpContentView != null)
        notification.headsUpContentView = this.mHeadsUpContentView; 
      return notification;
    }
    
    public Notification.Builder getBuilder() {
      return this.b;
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/app/NotificationCompatApi21.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */