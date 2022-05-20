package android.support.design.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class TextInputEditText extends AppCompatEditText {
  public TextInputEditText(Context paramContext) {
    super(paramContext);
  }
  
  public TextInputEditText(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public TextInputEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo) {
    InputConnection inputConnection = super.onCreateInputConnection(paramEditorInfo);
    if (inputConnection != null && paramEditorInfo.hintText == null)
      for (ViewParent viewParent = getParent();; viewParent = viewParent.getParent()) {
        if (viewParent instanceof android.view.View) {
          if (viewParent instanceof TextInputLayout) {
            paramEditorInfo.hintText = ((TextInputLayout)viewParent).getHint();
            return inputConnection;
          } 
        } else {
          return inputConnection;
        } 
      }  
    return inputConnection;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/android/support/design/widget/TextInputEditText.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */