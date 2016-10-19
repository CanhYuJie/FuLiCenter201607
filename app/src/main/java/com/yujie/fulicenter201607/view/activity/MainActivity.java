package com.yujie.fulicenter201607.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yujie.fulicenter201607.FuLiCenterApplication;
import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.presenter.MainPre;
import com.yujie.fulicenter201607.view.fragment.BoutiqueFragment;
import com.yujie.fulicenter201607.view.fragment.CartFragment;
import com.yujie.fulicenter201607.view.fragment.CategoryFragment;
import com.yujie.fulicenter201607.view.fragment.NewGoodFragment;
import com.yujie.fulicenter201607.view.fragment.Personal_CenterFragment;
import com.yujie.fulicenter201607.view.interface_group.IMainView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMainView{
    public static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.activity_main_ViewPager_fragmentViewpager)
    ViewPager activityMainViewPagerFragmentViewpager;
    @Bind(R.id.activity_main_RadioGroup_group)
    RadioGroup activityMainRadioGroupGroup;
    @Bind(R.id.activity_main_TextView_cart_number)
    TextView activityMainTextViewCartNumber;
    private Context mContext = MainActivity.this;
    private MainPre pre;
    private int[] radioButtonArray = {
            R.id.activity_main_RadioButton_new_good,
            R.id.activity_main_RadioButton_boutique,
            R.id.activity_main_RadioButton_category,
            R.id.activity_main_RadioButton_cart,
            R.id.activity_main_RadioButton_personal_center
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initChose();
        initPre();
    }

    private void initPre() {
        pre = new MainPre(this);
        pre.setReceiver(this);
    }


    private void initChose() {
        ((RadioButton)findViewById(radioButtonArray[0])).setChecked(true);
        activityMainRadioGroupGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.activity_main_RadioButton_new_good:
                        setViewPagerIndex(0);
                        break;
                    case R.id.activity_main_RadioButton_boutique:
                        setViewPagerIndex(1);
                        break;
                    case R.id.activity_main_RadioButton_category:
                        setViewPagerIndex(2);
                        break;
                    case R.id.activity_main_RadioButton_cart:
                        if (FuLiCenterApplication.getInstance().getCurrentUser()!=null){
                            setViewPagerIndex(3);
                        }else {
                            startActivity(new Intent(mContext,LoginActivity.class));
                        }
                        break;
                    case R.id.activity_main_RadioButton_personal_center:
                        if (FuLiCenterApplication.getInstance().getCurrentUser()!=null){
                            setViewPagerIndex(4);
                        }else {
                            startActivity(new Intent(mContext,LoginActivity.class));
                        }
                        break;
                }
            }
        });
        activityMainViewPagerFragmentViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return new NewGoodFragment();
                    case 1:
                        return new BoutiqueFragment();
                    case 2:
                        return new CategoryFragment();
                    case 3:
                        return new CartFragment();
                    case 4:
                        return new Personal_CenterFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 5;
            }
        });
        activityMainViewPagerFragmentViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==3 | position ==4){
                    if (FuLiCenterApplication.getInstance().getCurrentUser()!=null){
                        ((RadioButton)findViewById(radioButtonArray[position])).setChecked(true);
                    }else {
                        startActivity(new Intent(mContext,LoginActivity.class));
                    }
                }else {
                    ((RadioButton)findViewById(radioButtonArray[position])).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setViewPagerIndex(int index) {
        activityMainViewPagerFragmentViewpager.setCurrentItem(index);
    }

    @Override
    public void setReceiver(String msg) {
        activityMainTextViewCartNumber.setText(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pre.unBindReceiver(this);
        ButterKnife.unbind(this);
    }
}
