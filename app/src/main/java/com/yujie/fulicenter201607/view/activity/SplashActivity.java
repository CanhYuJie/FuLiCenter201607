package com.yujie.fulicenter201607.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.yujie.fulicenter201607.FuLiCenterApplication;
import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.GoodsDetailsBean;
import com.yujie.fulicenter201607.model.bean.Result;
import com.yujie.fulicenter201607.presenter.GoodsDetailPre;
import com.yujie.fulicenter201607.utils.MFGT;
import com.yujie.fulicenter201607.utils.SpUtils;
import com.yujie.fulicenter201607.view.interface_group.IGoodsDetailView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity{
    public static final String TAG = SplashActivity.class.getSimpleName();
    @Bind(R.id.activity_splash)
    RelativeLayout activitySplash;
    private Context mContext = SplashActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Animation alphaAnimation = AnimationUtils.loadAnimation(mContext, R.anim.splash_anim);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(mContext,MainActivity.class));
                MFGT.finish(SplashActivity.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        activitySplash.startAnimation(alphaAnimation);
        Object user = SpUtils.readObject(this, "current_user");
        if (user instanceof Result){
            FuLiCenterApplication.getInstance().setCurrentUser((Result) user);
        }else {
            Log.e(TAG, "onCreate: 转换失败" +user);
        }
    }
}
