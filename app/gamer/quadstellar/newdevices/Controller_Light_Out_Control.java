package app.gamer.quadstellar.newdevices;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import app.gamer.quadstellar.newdevices.Base.TabController;
import com.squareup.picasso.Picasso;

public class Controller_Light_Out_Control extends TabController {
  public Controller_Light_Out_Control(Context paramContext, boolean paramBoolean) {
    super(paramContext, paramBoolean);
  }
  
  protected View initContentView(Context paramContext) {
    View view = View.inflate(paramContext, 2130903127, null);
    ImageView imageView = (ImageView)view.findViewById(2131756597);
    Picasso.with(paramContext).load(2130838310).into(imageView);
    return view;
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/Controller_Light_Out_Control.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */