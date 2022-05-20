package app.gamer.quadstellar.newdevices.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import app.gamer.quadstellar.App;
import app.gamer.quadstellar.ui.activity.HomeActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ScripActivity extends AppCompatActivity {
  @BindView(2131756474)
  ImageView ivRed;
  
  @BindView(2131756473)
  LinearLayout llContainer;
  
  private int[] mImageIds;
  
  private int[] mImageIds_en = new int[] { 2130838353, 2130838354, 2130838355, 2130838356, 2130838357 };
  
  private int[] mImageIds_zh = new int[] { 2130838348, 2130838349, 2130838350, 2130838351, 2130838352 };
  
  private ArrayList<ImageView> mImageViewList;
  
  private int mPaintDis;
  
  @BindView(2131756472)
  Button startBtn;
  
  @BindView(2131756471)
  ViewPager viewPager;
  
  private void initData() {
    String str = (App.getAppResources().getConfiguration()).locale.getCountry();
    if (str.equals("CN") || str.equals("zh")) {
      this.mImageIds = this.mImageIds_zh;
    } else {
      this.mImageIds = this.mImageIds_en;
    } 
    this.mImageViewList = new ArrayList<ImageView>();
    for (byte b = 0; b < this.mImageIds.length; b++) {
      ImageView imageView = new ImageView((Context)this);
      Picasso.with((Context)this).load(this.mImageIds[b]).into(imageView);
      this.mImageViewList.add(imageView);
      imageView = new ImageView((Context)this);
      imageView.setImageResource(2130838253);
      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
      if (b > 0)
        layoutParams.leftMargin = 10; 
      imageView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      this.llContainer.addView((View)imageView);
    } 
    AdapterViewpager adapterViewpager = new AdapterViewpager();
    this.viewPager.setAdapter(adapterViewpager);
  }
  
  private void initListener() {
    this.ivRed.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          public void onGlobalLayout() {
            ScripActivity.this.ivRed.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            ScripActivity.access$002(ScripActivity.this, ScripActivity.this.llContainer.getChildAt(1).getLeft() - ScripActivity.this.llContainer.getChildAt(0).getLeft());
            System.out.println("距离：" + ScripActivity.this.mPaintDis);
          }
        });
    this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          public void onPageScrollStateChanged(int param1Int) {
            System.out.println("state:" + param1Int);
          }
          
          public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {
            param1Int2 = (int)(ScripActivity.this.mPaintDis * param1Float);
            int i = ScripActivity.this.mPaintDis;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)ScripActivity.this.ivRed.getLayoutParams();
            layoutParams.leftMargin = param1Int2 + i * param1Int1;
            ScripActivity.this.ivRed.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
          }
          
          public void onPageSelected(int param1Int) {
            System.out.println("position:" + param1Int);
            if (param1Int == ScripActivity.this.mImageViewList.size() - 1) {
              ScripActivity.this.startBtn.setVisibility(0);
              return;
            } 
            ScripActivity.this.startBtn.setVisibility(8);
          }
        });
  }
  
  protected void onCreate(@Nullable Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903095);
    ButterKnife.bind((Activity)this);
    initData();
    initListener();
  }
  
  @OnClick({2131756472})
  public void onViewClicked() {
    startActivity(new Intent((Context)this, HomeActivity.class));
  }
  
  public class AdapterViewpager extends PagerAdapter {
    public void destroyItem(ViewGroup param1ViewGroup, int param1Int, Object param1Object) {
      param1ViewGroup.removeView((View)param1Object);
    }
    
    public int getCount() {
      return ScripActivity.this.mImageViewList.size();
    }
    
    public Object instantiateItem(ViewGroup param1ViewGroup, int param1Int) {
      ImageView imageView = ScripActivity.this.mImageViewList.get(param1Int);
      param1ViewGroup.addView((View)imageView);
      return imageView;
    }
    
    public boolean isViewFromObject(View param1View, Object param1Object) {
      return (param1View == param1Object);
    }
  }
}


/* Location:              /opt/home/jayk-storage/Downloads/quadstellar/classes-dex2jar.jar!/app/gamer/quadstellar/newdevices/activity/ScripActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */