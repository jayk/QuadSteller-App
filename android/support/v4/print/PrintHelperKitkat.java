package android.support.v4.print;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@TargetApi(19)
@RequiresApi(19)
class PrintHelperKitkat {
  public static final int COLOR_MODE_COLOR = 2;
  
  public static final int COLOR_MODE_MONOCHROME = 1;
  
  private static final String LOG_TAG = "PrintHelperKitkat";
  
  private static final int MAX_PRINT_SIZE = 3500;
  
  public static final int ORIENTATION_LANDSCAPE = 1;
  
  public static final int ORIENTATION_PORTRAIT = 2;
  
  public static final int SCALE_MODE_FILL = 2;
  
  public static final int SCALE_MODE_FIT = 1;
  
  int mColorMode = 2;
  
  final Context mContext;
  
  BitmapFactory.Options mDecodeOptions = null;
  
  protected boolean mIsMinMarginsHandlingCorrect = true;
  
  private final Object mLock = new Object();
  
  int mOrientation;
  
  protected boolean mPrintActivityRespectsOrientation = true;
  
  int mScaleMode = 2;
  
  PrintHelperKitkat(Context paramContext) {
    this.mContext = paramContext;
  }
  
  private Bitmap convertBitmapForColorMode(Bitmap paramBitmap, int paramInt) {
    if (paramInt == 1) {
      Bitmap bitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap);
      Paint paint = new Paint();
      ColorMatrix colorMatrix = new ColorMatrix();
      colorMatrix.setSaturation(0.0F);
      paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
      canvas.drawBitmap(paramBitmap, 0.0F, 0.0F, paint);
      canvas.setBitmap(null);
      paramBitmap = bitmap;
    } 
    return paramBitmap;
  }
  
  private Matrix getMatrix(int paramInt1, int paramInt2, RectF paramRectF, int paramInt3) {
    Matrix matrix = new Matrix();
    float f = paramRectF.width() / paramInt1;
    if (paramInt3 == 2) {
      f = Math.max(f, paramRectF.height() / paramInt2);
      matrix.postScale(f, f);
      matrix.postTranslate((paramRectF.width() - paramInt1 * f) / 2.0F, (paramRectF.height() - paramInt2 * f) / 2.0F);
      return matrix;
    } 
    f = Math.min(f, paramRectF.height() / paramInt2);
    matrix.postScale(f, f);
    matrix.postTranslate((paramRectF.width() - paramInt1 * f) / 2.0F, (paramRectF.height() - paramInt2 * f) / 2.0F);
    return matrix;
  }
  
  private static boolean isPortrait(Bitmap paramBitmap) {
    return (paramBitmap.getWidth() <= paramBitmap.getHeight());
  }
  
  private Bitmap loadBitmap(Uri paramUri, BitmapFactory.Options paramOptions) throws FileNotFoundException {
    if (paramUri == null || this.mContext == null)
      throw new IllegalArgumentException("bad argument to loadBitmap"); 
    InputStream inputStream = null;
    try {
      InputStream inputStream1 = this.mContext.getContentResolver().openInputStream(paramUri);
      inputStream = inputStream1;
      return BitmapFactory.decodeStream(inputStream1, null, paramOptions);
    } finally {
      if (inputStream != null)
        try {
          inputStream.close();
        } catch (IOException iOException) {
          Log.w("PrintHelperKitkat", "close fail ", iOException);
        }  
    } 
  }
  
  private Bitmap loadConstrainedBitmap(Uri paramUri, int paramInt) throws FileNotFoundException {
    BitmapFactory.Options options1 = null;
    if (paramInt <= 0 || paramUri == null || this.mContext == null)
      throw new IllegalArgumentException("bad argument to getScaledBitmap"); 
    BitmapFactory.Options options2 = new BitmapFactory.Options();
    options2.inJustDecodeBounds = true;
    loadBitmap(paramUri, options2);
    int i = options2.outWidth;
    int j = options2.outHeight;
    options2 = options1;
    if (i > 0) {
      if (j <= 0)
        return (Bitmap)options1; 
    } else {
      return (Bitmap)options2;
    } 
    int k = Math.max(i, j);
    int m;
    for (m = 1; k > paramInt; m <<= 1)
      k >>>= 1; 
    options2 = options1;
    if (m > 0) {
      options2 = options1;
      if (Math.min(i, j) / m > 0)
        synchronized (this.mLock) {
          options1 = new BitmapFactory.Options();
          this();
          this.mDecodeOptions = options1;
          this.mDecodeOptions.inMutable = true;
          this.mDecodeOptions.inSampleSize = m;
          options1 = this.mDecodeOptions;
          try {
            null = loadBitmap(paramUri, options1);
            Object object = this.mLock;
          } finally {
            null = null;
            Object object = this.mLock;
          } 
        }  
    } 
    return (Bitmap)options2;
  }
  
  private void writeBitmap(final PrintAttributes attributes, final int fittingMode, final Bitmap bitmap, final ParcelFileDescriptor fileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
    final PrintAttributes pdfAttributes;
    if (this.mIsMinMarginsHandlingCorrect) {
      printAttributes = attributes;
    } else {
      printAttributes = copyAttributes(attributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
    } 
    (new AsyncTask<Void, Void, Throwable>() {
        protected Throwable doInBackground(Void... param1VarArgs) {
          // Byte code:
          //   0: aconst_null
          //   1: astore_2
          //   2: aload_0
          //   3: getfield val$cancellationSignal : Landroid/os/CancellationSignal;
          //   6: invokevirtual isCanceled : ()Z
          //   9: ifeq -> 16
          //   12: aload_2
          //   13: astore_1
          //   14: aload_1
          //   15: areturn
          //   16: new android/print/pdf/PrintedPdfDocument
          //   19: astore_3
          //   20: aload_3
          //   21: aload_0
          //   22: getfield this$0 : Landroid/support/v4/print/PrintHelperKitkat;
          //   25: getfield mContext : Landroid/content/Context;
          //   28: aload_0
          //   29: getfield val$pdfAttributes : Landroid/print/PrintAttributes;
          //   32: invokespecial <init> : (Landroid/content/Context;Landroid/print/PrintAttributes;)V
          //   35: aload_0
          //   36: getfield this$0 : Landroid/support/v4/print/PrintHelperKitkat;
          //   39: aload_0
          //   40: getfield val$bitmap : Landroid/graphics/Bitmap;
          //   43: aload_0
          //   44: getfield val$pdfAttributes : Landroid/print/PrintAttributes;
          //   47: invokevirtual getColorMode : ()I
          //   50: invokestatic access$100 : (Landroid/support/v4/print/PrintHelperKitkat;Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
          //   53: astore #4
          //   55: aload_0
          //   56: getfield val$cancellationSignal : Landroid/os/CancellationSignal;
          //   59: invokevirtual isCanceled : ()Z
          //   62: istore #5
          //   64: aload_2
          //   65: astore_1
          //   66: iload #5
          //   68: ifne -> 14
          //   71: aload_3
          //   72: iconst_1
          //   73: invokevirtual startPage : (I)Landroid/graphics/pdf/PdfDocument$Page;
          //   76: astore #6
          //   78: aload_0
          //   79: getfield this$0 : Landroid/support/v4/print/PrintHelperKitkat;
          //   82: getfield mIsMinMarginsHandlingCorrect : Z
          //   85: ifeq -> 216
          //   88: new android/graphics/RectF
          //   91: astore_1
          //   92: aload_1
          //   93: aload #6
          //   95: invokevirtual getInfo : ()Landroid/graphics/pdf/PdfDocument$PageInfo;
          //   98: invokevirtual getContentRect : ()Landroid/graphics/Rect;
          //   101: invokespecial <init> : (Landroid/graphics/Rect;)V
          //   104: aload_0
          //   105: getfield this$0 : Landroid/support/v4/print/PrintHelperKitkat;
          //   108: aload #4
          //   110: invokevirtual getWidth : ()I
          //   113: aload #4
          //   115: invokevirtual getHeight : ()I
          //   118: aload_1
          //   119: aload_0
          //   120: getfield val$fittingMode : I
          //   123: invokestatic access$200 : (Landroid/support/v4/print/PrintHelperKitkat;IILandroid/graphics/RectF;I)Landroid/graphics/Matrix;
          //   126: astore #7
          //   128: aload_0
          //   129: getfield this$0 : Landroid/support/v4/print/PrintHelperKitkat;
          //   132: getfield mIsMinMarginsHandlingCorrect : Z
          //   135: ifeq -> 313
          //   138: aload #6
          //   140: invokevirtual getCanvas : ()Landroid/graphics/Canvas;
          //   143: aload #4
          //   145: aload #7
          //   147: aconst_null
          //   148: invokevirtual drawBitmap : (Landroid/graphics/Bitmap;Landroid/graphics/Matrix;Landroid/graphics/Paint;)V
          //   151: aload_3
          //   152: aload #6
          //   154: invokevirtual finishPage : (Landroid/graphics/pdf/PdfDocument$Page;)V
          //   157: aload_0
          //   158: getfield val$cancellationSignal : Landroid/os/CancellationSignal;
          //   161: invokevirtual isCanceled : ()Z
          //   164: istore #5
          //   166: iload #5
          //   168: ifeq -> 340
          //   171: aload_3
          //   172: invokevirtual close : ()V
          //   175: aload_0
          //   176: getfield val$fileDescriptor : Landroid/os/ParcelFileDescriptor;
          //   179: astore_1
          //   180: aload_1
          //   181: ifnull -> 191
          //   184: aload_0
          //   185: getfield val$fileDescriptor : Landroid/os/ParcelFileDescriptor;
          //   188: invokevirtual close : ()V
          //   191: aload_2
          //   192: astore_1
          //   193: aload #4
          //   195: aload_0
          //   196: getfield val$bitmap : Landroid/graphics/Bitmap;
          //   199: if_acmpeq -> 14
          //   202: aload #4
          //   204: invokevirtual recycle : ()V
          //   207: aload_2
          //   208: astore_1
          //   209: goto -> 14
          //   212: astore_1
          //   213: goto -> 14
          //   216: new android/print/pdf/PrintedPdfDocument
          //   219: astore #7
          //   221: aload #7
          //   223: aload_0
          //   224: getfield this$0 : Landroid/support/v4/print/PrintHelperKitkat;
          //   227: getfield mContext : Landroid/content/Context;
          //   230: aload_0
          //   231: getfield val$attributes : Landroid/print/PrintAttributes;
          //   234: invokespecial <init> : (Landroid/content/Context;Landroid/print/PrintAttributes;)V
          //   237: aload #7
          //   239: iconst_1
          //   240: invokevirtual startPage : (I)Landroid/graphics/pdf/PdfDocument$Page;
          //   243: astore #8
          //   245: new android/graphics/RectF
          //   248: astore_1
          //   249: aload_1
          //   250: aload #8
          //   252: invokevirtual getInfo : ()Landroid/graphics/pdf/PdfDocument$PageInfo;
          //   255: invokevirtual getContentRect : ()Landroid/graphics/Rect;
          //   258: invokespecial <init> : (Landroid/graphics/Rect;)V
          //   261: aload #7
          //   263: aload #8
          //   265: invokevirtual finishPage : (Landroid/graphics/pdf/PdfDocument$Page;)V
          //   268: aload #7
          //   270: invokevirtual close : ()V
          //   273: goto -> 104
          //   276: astore_1
          //   277: aload_3
          //   278: invokevirtual close : ()V
          //   281: aload_0
          //   282: getfield val$fileDescriptor : Landroid/os/ParcelFileDescriptor;
          //   285: astore_2
          //   286: aload_2
          //   287: ifnull -> 297
          //   290: aload_0
          //   291: getfield val$fileDescriptor : Landroid/os/ParcelFileDescriptor;
          //   294: invokevirtual close : ()V
          //   297: aload #4
          //   299: aload_0
          //   300: getfield val$bitmap : Landroid/graphics/Bitmap;
          //   303: if_acmpeq -> 311
          //   306: aload #4
          //   308: invokevirtual recycle : ()V
          //   311: aload_1
          //   312: athrow
          //   313: aload #7
          //   315: aload_1
          //   316: getfield left : F
          //   319: aload_1
          //   320: getfield top : F
          //   323: invokevirtual postTranslate : (FF)Z
          //   326: pop
          //   327: aload #6
          //   329: invokevirtual getCanvas : ()Landroid/graphics/Canvas;
          //   332: aload_1
          //   333: invokevirtual clipRect : (Landroid/graphics/RectF;)Z
          //   336: pop
          //   337: goto -> 138
          //   340: new java/io/FileOutputStream
          //   343: astore_1
          //   344: aload_1
          //   345: aload_0
          //   346: getfield val$fileDescriptor : Landroid/os/ParcelFileDescriptor;
          //   349: invokevirtual getFileDescriptor : ()Ljava/io/FileDescriptor;
          //   352: invokespecial <init> : (Ljava/io/FileDescriptor;)V
          //   355: aload_3
          //   356: aload_1
          //   357: invokevirtual writeTo : (Ljava/io/OutputStream;)V
          //   360: aload_3
          //   361: invokevirtual close : ()V
          //   364: aload_0
          //   365: getfield val$fileDescriptor : Landroid/os/ParcelFileDescriptor;
          //   368: astore_1
          //   369: aload_1
          //   370: ifnull -> 380
          //   373: aload_0
          //   374: getfield val$fileDescriptor : Landroid/os/ParcelFileDescriptor;
          //   377: invokevirtual close : ()V
          //   380: aload_2
          //   381: astore_1
          //   382: aload #4
          //   384: aload_0
          //   385: getfield val$bitmap : Landroid/graphics/Bitmap;
          //   388: if_acmpeq -> 14
          //   391: aload #4
          //   393: invokevirtual recycle : ()V
          //   396: aload_2
          //   397: astore_1
          //   398: goto -> 14
          //   401: astore_2
          //   402: goto -> 297
          //   405: astore_1
          //   406: goto -> 380
          //   409: astore_1
          //   410: goto -> 191
          // Exception table:
          //   from	to	target	type
          //   2	12	212	java/lang/Throwable
          //   16	64	212	java/lang/Throwable
          //   71	104	276	finally
          //   104	138	276	finally
          //   138	166	276	finally
          //   171	180	212	java/lang/Throwable
          //   184	191	409	java/io/IOException
          //   184	191	212	java/lang/Throwable
          //   193	207	212	java/lang/Throwable
          //   216	273	276	finally
          //   277	286	212	java/lang/Throwable
          //   290	297	401	java/io/IOException
          //   290	297	212	java/lang/Throwable
          //   297	311	212	java/lang/Throwable
          //   311	313	212	java/lang/Throwable
          //   313	337	276	finally
          //   340	360	276	finally
          //   360	369	212	java/lang/Throwable
          //   373	380	405	java/io/IOException
          //   373	380	212	java/lang/Throwable
          //   382	396	212	java/lang/Throwable
        }
        
        protected void onPostExecute(Throwable param1Throwable) {
          if (cancellationSignal.isCanceled()) {
            writeResultCallback.onWriteCancelled();
            return;
          } 
          if (param1Throwable == null) {
            writeResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
            return;
          } 
          Log.e("PrintHelperKitkat", "Error writing printed content", param1Throwable);
          writeResultCallback.onWriteFailed(null);
        }
      }).execute((Object[])new Void[0]);
  }
  
  protected PrintAttributes.Builder copyAttributes(PrintAttributes paramPrintAttributes) {
    PrintAttributes.Builder builder = (new PrintAttributes.Builder()).setMediaSize(paramPrintAttributes.getMediaSize()).setResolution(paramPrintAttributes.getResolution()).setMinMargins(paramPrintAttributes.getMinMargins());
    if (paramPrintAttributes.getColorMode() != 0)
      builder.setColorMode(paramPrintAttributes.getColorMode()); 
    return builder;
  }
  
  public int getColorMode() {
    return this.mColorMode;
  }
  
  public int getOrientation() {
    return (this.mOrientation == 0) ? 1 : this.mOrientation;
  }
  
  public int getScaleMode() {
    return this.mScaleMode;
  }
  
  public void printBitmap(final String jobName, final Bitmap bitmap, final OnPrintFinishCallback callback) {
    if (bitmap != null) {
      PrintAttributes.MediaSize mediaSize;
      final int fittingMode = this.mScaleMode;
      PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
      if (isPortrait(bitmap)) {
        mediaSize = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
      } else {
        mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
      } 
      PrintAttributes printAttributes = (new PrintAttributes.Builder()).setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
      printManager.print(jobName, new PrintDocumentAdapter() {
            private PrintAttributes mAttributes;
            
            public void onFinish() {
              if (callback != null)
                callback.onFinish(); 
            }
            
            public void onLayout(PrintAttributes param1PrintAttributes1, PrintAttributes param1PrintAttributes2, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.LayoutResultCallback param1LayoutResultCallback, Bundle param1Bundle) {
              boolean bool = true;
              this.mAttributes = param1PrintAttributes2;
              PrintDocumentInfo printDocumentInfo = (new PrintDocumentInfo.Builder(jobName)).setContentType(1).setPageCount(1).build();
              if (param1PrintAttributes2.equals(param1PrintAttributes1))
                bool = false; 
              param1LayoutResultCallback.onLayoutFinished(printDocumentInfo, bool);
            }
            
            public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) {
              PrintHelperKitkat.this.writeBitmap(this.mAttributes, fittingMode, bitmap, param1ParcelFileDescriptor, param1CancellationSignal, param1WriteResultCallback);
            }
          }printAttributes);
    } 
  }
  
  public void printBitmap(final String jobName, final Uri imageFile, final OnPrintFinishCallback callback) throws FileNotFoundException {
    PrintDocumentAdapter printDocumentAdapter = new PrintDocumentAdapter() {
        private PrintAttributes mAttributes;
        
        Bitmap mBitmap = null;
        
        AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
        
        private void cancelLoad() {
          synchronized (PrintHelperKitkat.this.mLock) {
            if (PrintHelperKitkat.this.mDecodeOptions != null) {
              PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
              PrintHelperKitkat.this.mDecodeOptions = null;
            } 
            return;
          } 
        }
        
        public void onFinish() {
          super.onFinish();
          cancelLoad();
          if (this.mLoadBitmap != null)
            this.mLoadBitmap.cancel(true); 
          if (callback != null)
            callback.onFinish(); 
          if (this.mBitmap != null) {
            this.mBitmap.recycle();
            this.mBitmap = null;
          } 
        }
        
        public void onLayout(PrintAttributes param1PrintAttributes1, PrintAttributes param1PrintAttributes2, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.LayoutResultCallback param1LayoutResultCallback, Bundle param1Bundle) {
          // Byte code:
          //   0: iconst_1
          //   1: istore #6
          //   3: aload_0
          //   4: monitorenter
          //   5: aload_0
          //   6: aload_2
          //   7: putfield mAttributes : Landroid/print/PrintAttributes;
          //   10: aload_0
          //   11: monitorexit
          //   12: aload_3
          //   13: invokevirtual isCanceled : ()Z
          //   16: ifeq -> 30
          //   19: aload #4
          //   21: invokevirtual onLayoutCancelled : ()V
          //   24: return
          //   25: astore_1
          //   26: aload_0
          //   27: monitorexit
          //   28: aload_1
          //   29: athrow
          //   30: aload_0
          //   31: getfield mBitmap : Landroid/graphics/Bitmap;
          //   34: ifnull -> 85
          //   37: new android/print/PrintDocumentInfo$Builder
          //   40: dup
          //   41: aload_0
          //   42: getfield val$jobName : Ljava/lang/String;
          //   45: invokespecial <init> : (Ljava/lang/String;)V
          //   48: iconst_1
          //   49: invokevirtual setContentType : (I)Landroid/print/PrintDocumentInfo$Builder;
          //   52: iconst_1
          //   53: invokevirtual setPageCount : (I)Landroid/print/PrintDocumentInfo$Builder;
          //   56: invokevirtual build : ()Landroid/print/PrintDocumentInfo;
          //   59: astore_3
          //   60: aload_2
          //   61: aload_1
          //   62: invokevirtual equals : (Ljava/lang/Object;)Z
          //   65: ifne -> 79
          //   68: aload #4
          //   70: aload_3
          //   71: iload #6
          //   73: invokevirtual onLayoutFinished : (Landroid/print/PrintDocumentInfo;Z)V
          //   76: goto -> 24
          //   79: iconst_0
          //   80: istore #6
          //   82: goto -> 68
          //   85: aload_0
          //   86: new android/support/v4/print/PrintHelperKitkat$3$1
          //   89: dup
          //   90: aload_0
          //   91: aload_3
          //   92: aload_2
          //   93: aload_1
          //   94: aload #4
          //   96: invokespecial <init> : (Landroid/support/v4/print/PrintHelperKitkat$3;Landroid/os/CancellationSignal;Landroid/print/PrintAttributes;Landroid/print/PrintAttributes;Landroid/print/PrintDocumentAdapter$LayoutResultCallback;)V
          //   99: iconst_0
          //   100: anewarray android/net/Uri
          //   103: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
          //   106: putfield mLoadBitmap : Landroid/os/AsyncTask;
          //   109: goto -> 24
          // Exception table:
          //   from	to	target	type
          //   5	12	25	finally
          //   26	28	25	finally
        }
        
        public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) {
          PrintHelperKitkat.this.writeBitmap(this.mAttributes, fittingMode, this.mBitmap, param1ParcelFileDescriptor, param1CancellationSignal, param1WriteResultCallback);
        }
      };
    PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
    PrintAttributes.Builder builder = new PrintAttributes.Builder();
    builder.setColorMode(this.mColorMode);
    if (this.mOrientation == 1 || this.mOrientation == 0) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
    } else if (this.mOrientation == 2) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
    } 
    printManager.print(jobName, printDocumentAdapter, builder.build());
  }
  
  public void setColorMode(int paramInt) {
    this.mColorMode = paramInt;
  }
  
  public void setOrientation(int paramInt) {
    this.mOrientation = paramInt;
  }
  
  public void setScaleMode(int paramInt) {
    this.mScaleMode = paramInt;
  }
  
  public static interface OnPrintFinishCallback {
    void onFinish();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/v4/print/PrintHelperKitkat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */