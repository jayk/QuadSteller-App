package app.gamer.quadstellar.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import app.gamer.quadstellar.adapter.ChooseSceneNameAdapter;
import app.gamer.quadstellar.mode.EFChoice;
import app.gamer.quadstellar.ui.BaseActivity;
import app.gamer.quadstellar.utils.StringTools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomDialog {
  private Button click;
  
  @SuppressLint({"HandlerLeak"})
  private Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        CustomDialog.this.click.setEnabled(true);
      }
    };
  
  View myView;
  
  public StringCallBack stringCallBack;
  
  public static void exitApp(final Context context) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context, 2131427648);
    builder.setTitle(context.getString(2131297503));
    builder.setMessage(context.getString(2131296512));
    builder.setPositiveButton(context.getString(2131296506), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.setFlags(268435456);
            context.startActivity(intent);
            Process.killProcess(Process.myPid());
          }
        });
    builder.setNegativeButton(context.getString(2131296459), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            param1DialogInterface.dismiss();
          }
        });
    builder.create().show();
  }
  
  public Dialog choosePictureDialog(Activity paramActivity, View.OnClickListener paramOnClickListener1, View.OnClickListener paramOnClickListener2, View.OnClickListener paramOnClickListener3) {
    this.myView = LayoutInflater.from((Context)paramActivity).inflate(2130903151, null);
    TextView textView1 = (TextView)this.myView.findViewById(2131756654);
    TextView textView2 = (TextView)this.myView.findViewById(2131756655);
    TextView textView3 = (TextView)this.myView.findViewById(2131756657);
    TextView textView4 = (TextView)this.myView.findViewById(2131756656);
    textView1.setOnClickListener(paramOnClickListener1);
    textView2.setOnClickListener(paramOnClickListener2);
    textView4.setOnClickListener(paramOnClickListener3);
    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    layoutParams.x = -80;
    layoutParams.y = -60;
    final ClearDialogBlackBg dialog = new ClearDialogBlackBg((Context)paramActivity, 2131427740);
    clearDialogBlackBg.addContentView(this.myView, (ViewGroup.LayoutParams)layoutParams);
    clearDialogBlackBg.setCanceledOnTouchOutside(false);
    textView3.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            dialog.dismiss();
          }
        });
    return clearDialogBlackBg;
  }
  
  public Dialog editClickDialog(BaseActivity paramBaseActivity, String paramString1, String paramString2, boolean paramBoolean, final StringCallBack back) {
    this.myView = LayoutInflater.from((Context)paramBaseActivity).inflate(2130903155, null);
    TextView textView1 = (TextView)this.myView.findViewById(2131756669);
    final EditText inputEt = (EditText)this.myView.findViewById(2131756671);
    GridView gridView = (GridView)this.myView.findViewById(2131756670);
    TextView textView2 = (TextView)this.myView.findViewById(2131756672);
    TextView textView3 = (TextView)this.myView.findViewById(2131756673);
    textView1.setText(paramString1);
    List<String> list = Arrays.asList(paramBaseActivity.getResources().getStringArray(2131689483));
    final ArrayList<String> mPic = new ArrayList();
    for (byte b = 0; b < list.size(); b++)
      arrayList.add("assets://scenepic/sceneimage" + (b + 100) + ".jpg"); 
    ChooseSceneNameAdapter chooseSceneNameAdapter = new ChooseSceneNameAdapter(paramBaseActivity);
    chooseSceneNameAdapter.setList(list);
    gridView.setAdapter((ListAdapter)chooseSceneNameAdapter);
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            inputEt.setText((String)param1AdapterView.getItemAtPosition(param1Int));
            StringTools.SCENEPIC = mPic.get(param1Int);
          }
        });
    if (paramBoolean) {
      editText.setText(paramString2);
      textView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              if (back != null)
                back.passValue(inputEt.getText().toString()); 
            }
          });
      editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable param1Editable) {}
            
            public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
            
            public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          });
      WindowManager.LayoutParams layoutParams1 = new WindowManager.LayoutParams();
      layoutParams1.x = -80;
      layoutParams1.y = -60;
      clearDialogBlackBg = new ClearDialogBlackBg((Context)paramBaseActivity, 2131427740);
      clearDialogBlackBg.addContentView(this.myView, (ViewGroup.LayoutParams)layoutParams1);
      clearDialogBlackBg.setCanceledOnTouchOutside(false);
      textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              dialog.dismiss();
            }
          });
      return clearDialogBlackBg;
    } 
    editText.setHint(paramString2);
    textView3.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (back != null)
              back.passValue(inputEt.getText().toString()); 
          }
        });
    editText.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {}
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
        });
    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    layoutParams.x = -80;
    layoutParams.y = -60;
    final ClearDialogBlackBg dialog = new ClearDialogBlackBg((Context)clearDialogBlackBg, 2131427740);
    clearDialogBlackBg.addContentView(this.myView, (ViewGroup.LayoutParams)layoutParams);
    clearDialogBlackBg.setCanceledOnTouchOutside(false);
    textView2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            dialog.dismiss();
          }
        });
    return clearDialogBlackBg;
  }
  
  public Dialog editDialog(Activity paramActivity, String paramString1, String paramString2, boolean paramBoolean, final StringCallBack back) {
    this.myView = LayoutInflater.from((Context)paramActivity).inflate(2130903157, null);
    TextView textView1 = (TextView)this.myView.findViewById(2131756679);
    final EditText inputEt = (EditText)this.myView.findViewById(2131756680);
    TextView textView2 = (TextView)this.myView.findViewById(2131756681);
    TextView textView3 = (TextView)this.myView.findViewById(2131756682);
    textView1.setText(paramString1);
    if (paramBoolean) {
      editText.setText(paramString2);
      textView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              if (back != null)
                back.passValue(inputEt.getText().toString()); 
            }
          });
      editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable param1Editable) {}
            
            public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
            
            public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          });
      WindowManager.LayoutParams layoutParams1 = new WindowManager.LayoutParams();
      layoutParams1.x = -80;
      layoutParams1.y = -60;
      clearDialogBlackBg = new ClearDialogBlackBg((Context)paramActivity, 2131427740);
      clearDialogBlackBg.addContentView(this.myView, (ViewGroup.LayoutParams)layoutParams1);
      clearDialogBlackBg.setCanceledOnTouchOutside(false);
      textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              dialog.dismiss();
            }
          });
      return clearDialogBlackBg;
    } 
    editText.setHint(paramString2);
    textView3.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (back != null)
              back.passValue(inputEt.getText().toString()); 
          }
        });
    editText.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {}
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
        });
    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    layoutParams.x = -80;
    layoutParams.y = -60;
    final ClearDialogBlackBg dialog = new ClearDialogBlackBg((Context)clearDialogBlackBg, 2131427740);
    clearDialogBlackBg.addContentView(this.myView, (ViewGroup.LayoutParams)layoutParams);
    clearDialogBlackBg.setCanceledOnTouchOutside(false);
    textView2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            dialog.dismiss();
          }
        });
    return clearDialogBlackBg;
  }
  
  public Dialog editLightDialog(Activity paramActivity, String paramString1, String paramString2, boolean paramBoolean, final StringCallBack back, final View.OnClickListener clickListener) {
    this.myView = LayoutInflater.from((Context)paramActivity).inflate(2130903164, null);
    TextView textView1 = (TextView)this.myView.findViewById(2131756679);
    this.click = (Button)this.myView.findViewById(2131756699);
    EditText editText = (EditText)this.myView.findViewById(2131756680);
    TextView textView2 = (TextView)this.myView.findViewById(2131756681);
    TextView textView3 = (TextView)this.myView.findViewById(2131756682);
    textView1.setText(paramString1);
    if (paramBoolean) {
      editText.setText(paramString2);
      textView3.setOnClickListener(clickListener);
      this.click.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View arg0) {
              CustomDialog.this.click.setEnabled(false);
              CustomDialog.this.mHandler.sendEmptyMessageDelayed(0, 6000L);
              CustomDialog.this.mHandler.post(new Runnable() {
                    public void run() {
                      clickListener.onClick(arg0);
                    }
                  });
            }
          });
      editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable param1Editable) {}
            
            public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
            
            public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
              back.passValue(param1CharSequence.toString());
            }
          });
      WindowManager.LayoutParams layoutParams1 = new WindowManager.LayoutParams();
      layoutParams1.x = -80;
      layoutParams1.y = -60;
      clearDialogBlackBg = new ClearDialogBlackBg((Context)paramActivity, 2131427740);
      clearDialogBlackBg.addContentView(this.myView, (ViewGroup.LayoutParams)layoutParams1);
      clearDialogBlackBg.setCanceledOnTouchOutside(false);
      textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              dialog.dismiss();
            }
          });
      return clearDialogBlackBg;
    } 
    editText.setHint(paramString2);
    textView3.setOnClickListener(clickListener);
    this.click.setOnClickListener(new View.OnClickListener() {
          public void onClick(final View arg0) {
            CustomDialog.this.click.setEnabled(false);
            CustomDialog.this.mHandler.sendEmptyMessageDelayed(0, 6000L);
            CustomDialog.this.mHandler.post(new Runnable() {
                  public void run() {
                    clickListener.onClick(arg0);
                  }
                });
          }
        });
    editText.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {}
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
            back.passValue(param1CharSequence.toString());
          }
        });
    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    layoutParams.x = -80;
    layoutParams.y = -60;
    final ClearDialogBlackBg dialog = new ClearDialogBlackBg((Context)clearDialogBlackBg, 2131427740);
    clearDialogBlackBg.addContentView(this.myView, (ViewGroup.LayoutParams)layoutParams);
    clearDialogBlackBg.setCanceledOnTouchOutside(false);
    textView2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            dialog.dismiss();
          }
        });
    return clearDialogBlackBg;
  }
  
  public void getStringValue(StringCallBack paramStringCallBack) {
    this.stringCallBack = paramStringCallBack;
  }
  
  public ProgressDialog loadingDialog(Context paramContext, String paramString1, String paramString2) {
    ProgressDialog progressDialog = new ProgressDialog(paramContext, 2131427648);
    progressDialog.setTitle(paramString1);
    progressDialog.setMessage(paramString2);
    progressDialog.setIndeterminate(true);
    progressDialog.setCancelable(true);
    progressDialog.setCanceledOnTouchOutside(false);
    return progressDialog;
  }
  
  public Dialog seekDialog(Activity paramActivity, String paramString, SeekBar.OnSeekBarChangeListener paramOnSeekBarChangeListener) {
    this.myView = LayoutInflater.from((Context)paramActivity).inflate(2130903173, null);
    TextView textView1 = (TextView)this.myView.findViewById(2131756713);
    SeekBar seekBar = (SeekBar)this.myView.findViewById(2131756714);
    TextView textView2 = (TextView)this.myView.findViewById(2131756715);
    textView1.setText(paramString);
    seekBar.setOnSeekBarChangeListener(paramOnSeekBarChangeListener);
    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    layoutParams.x = -80;
    layoutParams.y = -60;
    final ClearDialogBlackBg dialog = new ClearDialogBlackBg((Context)paramActivity, 2131427740);
    clearDialogBlackBg.addContentView(this.myView, (ViewGroup.LayoutParams)layoutParams);
    clearDialogBlackBg.setCanceledOnTouchOutside(false);
    textView2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            dialog.dismiss();
          }
        });
    return clearDialogBlackBg;
  }
  
  public Dialog tipsDialog(Activity paramActivity, String paramString1, String paramString2, View.OnClickListener paramOnClickListener) {
    this.myView = LayoutInflater.from((Context)paramActivity).inflate(2130903147, null);
    TextView textView1 = (TextView)this.myView.findViewById(2131756641);
    TextView textView2 = (TextView)this.myView.findViewById(2131756642);
    TextView textView3 = (TextView)this.myView.findViewById(2131756643);
    TextView textView4 = (TextView)this.myView.findViewById(2131756644);
    textView1.setText(paramString1);
    textView2.setText(paramString2);
    textView4.setOnClickListener(paramOnClickListener);
    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    layoutParams.x = -80;
    layoutParams.y = -60;
    final ClearDialogBlackBg dialog = new ClearDialogBlackBg((Context)paramActivity, 2131427740);
    clearDialogBlackBg.addContentView(this.myView, (ViewGroup.LayoutParams)layoutParams);
    clearDialogBlackBg.setCanceledOnTouchOutside(false);
    textView3.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            dialog.dismiss();
          }
        });
    return clearDialogBlackBg;
  }
  
  public Dialog upTipsDialog(Activity paramActivity, String paramString1, String paramString2, View.OnClickListener paramOnClickListener) {
    this.myView = LayoutInflater.from((Context)paramActivity).inflate(2130903148, null);
    TextView textView1 = (TextView)this.myView.findViewById(2131756641);
    TextView textView2 = (TextView)this.myView.findViewById(2131756642);
    TextView textView3 = (TextView)this.myView.findViewById(2131756645);
    TextView textView4 = (TextView)this.myView.findViewById(2131756646);
    TextView textView5 = (TextView)this.myView.findViewById(2131756647);
    textView1.setText(paramString1);
    textView2.setText(paramString2);
    textView4.setOnClickListener(paramOnClickListener);
    textView5.setOnClickListener(paramOnClickListener);
    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    layoutParams.x = -80;
    layoutParams.y = -60;
    final ClearDialogBlackBg dialog = new ClearDialogBlackBg((Context)paramActivity, 2131427740);
    clearDialogBlackBg.addContentView(this.myView, (ViewGroup.LayoutParams)layoutParams);
    clearDialogBlackBg.setCanceledOnTouchOutside(false);
    textView3.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            dialog.dismiss();
          }
        });
    return clearDialogBlackBg;
  }
  
  public static interface ChoiceCallback {
    void onItemClick(int param1Int, EFChoice param1EFChoice);
  }
  
  public static interface StringCallBack {
    void passValue(String param1String);
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/CustomDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */