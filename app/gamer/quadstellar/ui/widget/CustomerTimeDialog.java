package app.gamer.quadstellar.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import app.gamer.quadstellar.utils.LightTool;
import java.util.Date;

public class CustomerTimeDialog {
  public static void showTimeDialog(Context paramContext, MyTextView paramMyTextView, final TextView eidttext, final Button but) {
    View view = LayoutInflater.from(paramContext).inflate(2130903303, null);
    final TimePickerView timepicker = (TimePickerView)view.findViewById(2131757000);
    timePickerView.setDescendantFocusability(393216);
    timePickerView.setIs24HourView(Boolean.valueOf(true));
    final String oldValue = paramMyTextView.getText().toString();
    Date date = new Date();
    timePickerView.setCurrentHour(Integer.valueOf(date.getHours()));
    timePickerView.setCurrentMinute(Integer.valueOf(date.getMinutes()));
    AlertDialog.Builder builder = new AlertDialog.Builder(paramContext);
    builder.setTitle(2131297105);
    builder.setView(view);
    builder.setPositiveButton(2131297465, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            if (!(LightTool.get2Number(timepicker.getCurrentHour().intValue()) + ":" + LightTool.get2Number(timepicker.getCurrentMinute().intValue())).equals(oldValue))
              but.setVisibility(0); 
            eidttext.setText(LightTool.get2Number(timepicker.getCurrentHour().intValue()) + ":" + LightTool.get2Number(timepicker.getCurrentMinute().intValue()));
            param1DialogInterface.dismiss();
          }
        });
    builder.setNegativeButton(2131296459, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            param1DialogInterface.dismiss();
          }
        });
    builder.create().show();
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/ui/widget/CustomerTimeDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */