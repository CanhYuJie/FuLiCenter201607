package com.yujie.fulicenter201607.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.view.fragment.BoutiqueFragment;
import com.yujie.fulicenter201607.view.fragment.CartFragment;
import com.yujie.fulicenter201607.view.fragment.CategoryFragment;
import com.yujie.fulicenter201607.view.fragment.NewGoodFragment;
import com.yujie.fulicenter201607.view.fragment.Personal_CenterFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.activity_main_ViewPager_fragmentViewpager)
    ViewPager activityMainViewPagerFragmentViewpager;
    @Bind(R.id.activity_main_RadioGroup_group)
    RadioGroup activityMainRadioGroupGroup;
    @Bind(R.id.activity_main_TextView_cart_number)
    TextView activityMainTextViewCartNumber;
    private Context mContext = MainActivity.this;
    private CartHintBroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initChose();
        initReceiver();
    }

    private void initReceiver() {
        receiver = new CartHintBroadcastReceiver();
        IntentFilter filter = new IntentFilter("receiver_update_cart_hint");
        registerReceiver(receiver,filter);
    }

    private void initChose() {
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
                        setViewPagerIndex(3);
                        break;
                    case R.id.activity_main_RadioButton_personal_center:
                        setViewPagerIndex(4);
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
    }

    private void setViewPagerIndex(int index) {
        activityMainViewPagerFragmentViewpager.setCurrentItem(index);
    }

    class CartHintBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String cart_hint = getIntent().getStringExtra("cart_hint");
            activityMainTextViewCartNumber.setText(cart_hint);
        }
    }
}
