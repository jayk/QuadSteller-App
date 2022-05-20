package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.ArrayList;
import java.util.HashMap;

public final class LocalBroadcastManager {
  private static final boolean DEBUG = false;
  
  static final int MSG_EXEC_PENDING_BROADCASTS = 1;
  
  private static final String TAG = "LocalBroadcastManager";
  
  private static LocalBroadcastManager mInstance;
  
  private static final Object mLock = new Object();
  
  private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap<String, ArrayList<ReceiverRecord>>();
  
  private final Context mAppContext;
  
  private final Handler mHandler;
  
  private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList<BroadcastRecord>();
  
  private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = new HashMap<BroadcastReceiver, ArrayList<IntentFilter>>();
  
  private LocalBroadcastManager(Context paramContext) {
    this.mAppContext = paramContext;
    this.mHandler = new Handler(paramContext.getMainLooper()) {
        public void handleMessage(Message param1Message) {
          switch (param1Message.what) {
            default:
              super.handleMessage(param1Message);
              return;
            case 1:
              break;
          } 
          LocalBroadcastManager.this.executePendingBroadcasts();
        }
      };
  }
  
  private void executePendingBroadcasts() {
    while (true) {
      HashMap<BroadcastReceiver, ArrayList<IntentFilter>> hashMap;
      BroadcastRecord broadcastRecord;
      synchronized (this.mReceivers) {
        int i = this.mPendingBroadcasts.size();
        if (i <= 0)
          return; 
        BroadcastRecord[] arrayOfBroadcastRecord = new BroadcastRecord[i];
        this.mPendingBroadcasts.toArray(arrayOfBroadcastRecord);
        this.mPendingBroadcasts.clear();
        for (i = 0; i < arrayOfBroadcastRecord.length; i++) {
          broadcastRecord = arrayOfBroadcastRecord[i];
          for (byte b = 0; b < broadcastRecord.receivers.size(); b++)
            ((ReceiverRecord)broadcastRecord.receivers.get(b)).receiver.onReceive(this.mAppContext, broadcastRecord.intent); 
        } 
      } 
    } 
  }
  
  public static LocalBroadcastManager getInstance(Context paramContext) {
    synchronized (mLock) {
      if (mInstance == null) {
        LocalBroadcastManager localBroadcastManager = new LocalBroadcastManager();
        this(paramContext.getApplicationContext());
        mInstance = localBroadcastManager;
      } 
      return mInstance;
    } 
  }
  
  public void registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter) {
    synchronized (this.mReceivers) {
      ReceiverRecord receiverRecord = new ReceiverRecord();
      this(paramIntentFilter, paramBroadcastReceiver);
      ArrayList<IntentFilter> arrayList1 = this.mReceivers.get(paramBroadcastReceiver);
      ArrayList<IntentFilter> arrayList2 = arrayList1;
      if (arrayList1 == null) {
        arrayList2 = new ArrayList();
        this(1);
        this.mReceivers.put(paramBroadcastReceiver, arrayList2);
      } 
      arrayList2.add(paramIntentFilter);
      for (byte b = 0; b < paramIntentFilter.countActions(); b++) {
        String str = paramIntentFilter.getAction(b);
        arrayList2 = (ArrayList<IntentFilter>)this.mActions.get(str);
        ArrayList<IntentFilter> arrayList = arrayList2;
        if (arrayList2 == null) {
          arrayList = new ArrayList<IntentFilter>();
          this(1);
          this.mActions.put(str, arrayList);
        } 
        arrayList.add(receiverRecord);
      } 
      return;
    } 
  }
  
  public boolean sendBroadcast(Intent paramIntent) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mReceivers : Ljava/util/HashMap;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_1
    //   8: invokevirtual getAction : ()Ljava/lang/String;
    //   11: astore_3
    //   12: aload_1
    //   13: aload_0
    //   14: getfield mAppContext : Landroid/content/Context;
    //   17: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   20: invokevirtual resolveTypeIfNeeded : (Landroid/content/ContentResolver;)Ljava/lang/String;
    //   23: astore #4
    //   25: aload_1
    //   26: invokevirtual getData : ()Landroid/net/Uri;
    //   29: astore #5
    //   31: aload_1
    //   32: invokevirtual getScheme : ()Ljava/lang/String;
    //   35: astore #6
    //   37: aload_1
    //   38: invokevirtual getCategories : ()Ljava/util/Set;
    //   41: astore #7
    //   43: aload_1
    //   44: invokevirtual getFlags : ()I
    //   47: bipush #8
    //   49: iand
    //   50: ifeq -> 274
    //   53: iconst_1
    //   54: istore #8
    //   56: iload #8
    //   58: ifeq -> 111
    //   61: new java/lang/StringBuilder
    //   64: astore #9
    //   66: aload #9
    //   68: invokespecial <init> : ()V
    //   71: ldc 'LocalBroadcastManager'
    //   73: aload #9
    //   75: ldc 'Resolving type '
    //   77: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: aload #4
    //   82: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: ldc ' scheme '
    //   87: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: aload #6
    //   92: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   95: ldc ' of intent '
    //   97: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: aload_1
    //   101: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   104: invokevirtual toString : ()Ljava/lang/String;
    //   107: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   110: pop
    //   111: aload_0
    //   112: getfield mActions : Ljava/util/HashMap;
    //   115: aload_1
    //   116: invokevirtual getAction : ()Ljava/lang/String;
    //   119: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   122: checkcast java/util/ArrayList
    //   125: astore #10
    //   127: aload #10
    //   129: ifnull -> 591
    //   132: iload #8
    //   134: ifeq -> 168
    //   137: new java/lang/StringBuilder
    //   140: astore #9
    //   142: aload #9
    //   144: invokespecial <init> : ()V
    //   147: ldc 'LocalBroadcastManager'
    //   149: aload #9
    //   151: ldc 'Action list: '
    //   153: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   156: aload #10
    //   158: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   161: invokevirtual toString : ()Ljava/lang/String;
    //   164: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   167: pop
    //   168: aconst_null
    //   169: astore #11
    //   171: iconst_0
    //   172: istore #12
    //   174: iload #12
    //   176: aload #10
    //   178: invokevirtual size : ()I
    //   181: if_icmpge -> 498
    //   184: aload #10
    //   186: iload #12
    //   188: invokevirtual get : (I)Ljava/lang/Object;
    //   191: checkcast android/support/v4/content/LocalBroadcastManager$ReceiverRecord
    //   194: astore #13
    //   196: iload #8
    //   198: ifeq -> 235
    //   201: new java/lang/StringBuilder
    //   204: astore #9
    //   206: aload #9
    //   208: invokespecial <init> : ()V
    //   211: ldc 'LocalBroadcastManager'
    //   213: aload #9
    //   215: ldc 'Matching against filter '
    //   217: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: aload #13
    //   222: getfield filter : Landroid/content/IntentFilter;
    //   225: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   228: invokevirtual toString : ()Ljava/lang/String;
    //   231: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   234: pop
    //   235: aload #13
    //   237: getfield broadcasting : Z
    //   240: ifeq -> 280
    //   243: aload #11
    //   245: astore #9
    //   247: iload #8
    //   249: ifeq -> 264
    //   252: ldc 'LocalBroadcastManager'
    //   254: ldc '  Filter's target already added'
    //   256: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   259: pop
    //   260: aload #11
    //   262: astore #9
    //   264: iinc #12, 1
    //   267: aload #9
    //   269: astore #11
    //   271: goto -> 174
    //   274: iconst_0
    //   275: istore #8
    //   277: goto -> 56
    //   280: aload #13
    //   282: getfield filter : Landroid/content/IntentFilter;
    //   285: aload_3
    //   286: aload #4
    //   288: aload #6
    //   290: aload #5
    //   292: aload #7
    //   294: ldc 'LocalBroadcastManager'
    //   296: invokevirtual match : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/net/Uri;Ljava/util/Set;Ljava/lang/String;)I
    //   299: istore #14
    //   301: iload #14
    //   303: iflt -> 386
    //   306: iload #8
    //   308: ifeq -> 345
    //   311: new java/lang/StringBuilder
    //   314: astore #9
    //   316: aload #9
    //   318: invokespecial <init> : ()V
    //   321: ldc 'LocalBroadcastManager'
    //   323: aload #9
    //   325: ldc '  Filter matched!  match=0x'
    //   327: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   330: iload #14
    //   332: invokestatic toHexString : (I)Ljava/lang/String;
    //   335: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   338: invokevirtual toString : ()Ljava/lang/String;
    //   341: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   344: pop
    //   345: aload #11
    //   347: astore #9
    //   349: aload #11
    //   351: ifnonnull -> 364
    //   354: new java/util/ArrayList
    //   357: astore #9
    //   359: aload #9
    //   361: invokespecial <init> : ()V
    //   364: aload #9
    //   366: aload #13
    //   368: invokevirtual add : (Ljava/lang/Object;)Z
    //   371: pop
    //   372: aload #13
    //   374: iconst_1
    //   375: putfield broadcasting : Z
    //   378: goto -> 264
    //   381: astore_1
    //   382: aload_2
    //   383: monitorexit
    //   384: aload_1
    //   385: athrow
    //   386: aload #11
    //   388: astore #9
    //   390: iload #8
    //   392: ifeq -> 264
    //   395: iload #14
    //   397: tableswitch default -> 428, -4 -> 477, -3 -> 470, -2 -> 484, -1 -> 491
    //   428: ldc 'unknown reason'
    //   430: astore #9
    //   432: new java/lang/StringBuilder
    //   435: astore #13
    //   437: aload #13
    //   439: invokespecial <init> : ()V
    //   442: ldc 'LocalBroadcastManager'
    //   444: aload #13
    //   446: ldc '  Filter did not match: '
    //   448: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   451: aload #9
    //   453: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   456: invokevirtual toString : ()Ljava/lang/String;
    //   459: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   462: pop
    //   463: aload #11
    //   465: astore #9
    //   467: goto -> 264
    //   470: ldc 'action'
    //   472: astore #9
    //   474: goto -> 432
    //   477: ldc 'category'
    //   479: astore #9
    //   481: goto -> 432
    //   484: ldc 'data'
    //   486: astore #9
    //   488: goto -> 432
    //   491: ldc 'type'
    //   493: astore #9
    //   495: goto -> 432
    //   498: aload #11
    //   500: ifnull -> 591
    //   503: iconst_0
    //   504: istore #8
    //   506: iload #8
    //   508: aload #11
    //   510: invokevirtual size : ()I
    //   513: if_icmpge -> 536
    //   516: aload #11
    //   518: iload #8
    //   520: invokevirtual get : (I)Ljava/lang/Object;
    //   523: checkcast android/support/v4/content/LocalBroadcastManager$ReceiverRecord
    //   526: iconst_0
    //   527: putfield broadcasting : Z
    //   530: iinc #8, 1
    //   533: goto -> 506
    //   536: aload_0
    //   537: getfield mPendingBroadcasts : Ljava/util/ArrayList;
    //   540: astore #4
    //   542: new android/support/v4/content/LocalBroadcastManager$BroadcastRecord
    //   545: astore #9
    //   547: aload #9
    //   549: aload_1
    //   550: aload #11
    //   552: invokespecial <init> : (Landroid/content/Intent;Ljava/util/ArrayList;)V
    //   555: aload #4
    //   557: aload #9
    //   559: invokevirtual add : (Ljava/lang/Object;)Z
    //   562: pop
    //   563: aload_0
    //   564: getfield mHandler : Landroid/os/Handler;
    //   567: iconst_1
    //   568: invokevirtual hasMessages : (I)Z
    //   571: ifne -> 583
    //   574: aload_0
    //   575: getfield mHandler : Landroid/os/Handler;
    //   578: iconst_1
    //   579: invokevirtual sendEmptyMessage : (I)Z
    //   582: pop
    //   583: iconst_1
    //   584: istore #15
    //   586: aload_2
    //   587: monitorexit
    //   588: iload #15
    //   590: ireturn
    //   591: aload_2
    //   592: monitorexit
    //   593: iconst_0
    //   594: istore #15
    //   596: goto -> 588
    // Exception table:
    //   from	to	target	type
    //   7	53	381	finally
    //   61	111	381	finally
    //   111	127	381	finally
    //   137	168	381	finally
    //   174	196	381	finally
    //   201	235	381	finally
    //   235	243	381	finally
    //   252	260	381	finally
    //   280	301	381	finally
    //   311	345	381	finally
    //   354	364	381	finally
    //   364	378	381	finally
    //   382	384	381	finally
    //   432	463	381	finally
    //   506	530	381	finally
    //   536	583	381	finally
    //   586	588	381	finally
    //   591	593	381	finally
  }
  
  public void sendBroadcastSync(Intent paramIntent) {
    if (sendBroadcast(paramIntent))
      executePendingBroadcasts(); 
  }
  
  public void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver) {
    synchronized (this.mReceivers) {
      ArrayList<IntentFilter> arrayList = this.mReceivers.remove(paramBroadcastReceiver);
      if (arrayList == null)
        return; 
      for (byte b = 0; b < arrayList.size(); b++) {
        IntentFilter intentFilter = arrayList.get(b);
        for (byte b1 = 0; b1 < intentFilter.countActions(); b1++) {
          String str = intentFilter.getAction(b1);
          ArrayList arrayList1 = this.mActions.get(str);
          if (arrayList1 != null) {
            int i;
            for (i = 0; i < arrayList1.size(); i = j + 1) {
              int j = i;
              if (((ReceiverRecord)arrayList1.get(i)).receiver == paramBroadcastReceiver) {
                arrayList1.remove(i);
                j = i - 1;
              } 
            } 
            if (arrayList1.size() <= 0)
              this.mActions.remove(str); 
          } 
        } 
      } 
      return;
    } 
  }
  
  private static class BroadcastRecord {
    final Intent intent;
    
    final ArrayList<LocalBroadcastManager.ReceiverRecord> receivers;
    
    BroadcastRecord(Intent param1Intent, ArrayList<LocalBroadcastManager.ReceiverRecord> param1ArrayList) {
      this.intent = param1Intent;
      this.receivers = param1ArrayList;
    }
  }
  
  private static class ReceiverRecord {
    boolean broadcasting;
    
    final IntentFilter filter;
    
    final BroadcastReceiver receiver;
    
    ReceiverRecord(IntentFilter param1IntentFilter, BroadcastReceiver param1BroadcastReceiver) {
      this.filter = param1IntentFilter;
      this.receiver = param1BroadcastReceiver;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder(128);
      stringBuilder.append("Receiver{");
      stringBuilder.append(this.receiver);
      stringBuilder.append(" filter=");
      stringBuilder.append(this.filter);
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/content/LocalBroadcastManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */