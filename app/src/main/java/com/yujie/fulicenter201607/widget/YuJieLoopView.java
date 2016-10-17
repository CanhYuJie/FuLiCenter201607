package com.yujie.fulicenter201607.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.R;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yujie on 16-10-17.
 */

public class YuJieLoopView extends FrameLayout {
    private Context mContext;
    private ImageView[] imageViews;
    private ArrayList<String> imageUrls;
    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private ImageView[] dots;
    private boolean autoPlay = true;
    private boolean displayIndex = true;
    private int currentIndex = 0;
    private ScheduledExecutorService service;
    private Handler viewpagerHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem((currentIndex+1));
        }
    };
    public YuJieLoopView(Context context) {
        this(context,null);
    }

    public YuJieLoopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YuJieLoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    private void initLoopView(){
        if (imageUrls == null | imageUrls.size() == 0){
            return;
        }
        LayoutInflater.from(mContext).inflate(R.layout.loop_view_yujie,this,true);
        viewPager = (ViewPager) findViewById(R.id.loop_viewpager);
        dotLayout = (LinearLayout) findViewById(R.id.loop_dotlayout);
        dotLayout.setGravity(Gravity.CENTER);
        imageViews = new ImageView[imageUrls.size()];
        for (int i = 0; i < imageUrls.size(); i ++){
            ImageView img = new ImageView(mContext);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(mContext).load(I.DOWNLOAD_IMG_URL+imageUrls.get(i))
                    .placeholder(R.drawable.nopic)
                    .error(R.drawable.nopic)
                    .into(img);
            imageViews[i] = img;
        }
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageUrls==null?0:imageUrls.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return (view == object);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViews[position]);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(imageViews[position]);
                return imageViews[position];
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private boolean isScrolled;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case 1:
                        isScrolled = false;
                        break;
                    case 2:
                        isScrolled = true;
                        break;
                    case 0:
                        if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount()-1
                                && isScrolled){
                            viewpagerHander.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    viewPager.setCurrentItem(0);
                                }
                            },2000);
                        }
                        break;
                }
            }
        });
        dots = new ImageView[imageViews.length];
        if (!displayIndex){
            return;
        }else {
            for (int i = 0; i < imageViews.length; i ++){
                dots[i] = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(5,0,5,0);
                dots[i].setLayoutParams(params);
                dots[i].setBackgroundResource(R.drawable.loop_view_dot_normal_bg);
                dotLayout.addView(dots[i]);
            }
            dots[0].setBackgroundResource(R.drawable.loop_view_dot_chose_bg);
        }
    }

    private void setCurrentDot(int position) {
        currentIndex = position;
        if (!displayIndex){
            return;
        }
        for (int i = 0; i < dotLayout.getChildCount(); i ++){
            if (i == currentIndex){
                dotLayout.getChildAt(i).setBackgroundResource(R.drawable.loop_view_dot_chose_bg);
            }else {
                dotLayout.getChildAt(i).setBackgroundResource(R.drawable.loop_view_dot_normal_bg);
            }
        }
    }

    public void startPlay(long delay,long period){
        initLoopView();
        if (autoPlay & imageUrls.size() > 0){
            service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(new ShowImgTask(),delay,period, TimeUnit.SECONDS);
        }
    }

    public void startPlay(){
        startPlay(3,2);
    }

    public void stopPlay(){
        if (service != null){
            service.shutdown();
        }
        if (viewpagerHander != null){
            viewpagerHander.removeCallbacksAndMessages(null);
        }
    }

    private class ShowImgTask implements Runnable{

        @Override
        public void run() {
            synchronized (viewPager){
                viewpagerHander.obtainMessage().sendToTarget();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPlay();
    }


    public YuJieLoopView setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
        return this;
    }

    public YuJieLoopView setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
        return this;
    }

    public YuJieLoopView setDisplayIndex(boolean displayIndex) {
        this.displayIndex = displayIndex;
        return this;
    }
}
