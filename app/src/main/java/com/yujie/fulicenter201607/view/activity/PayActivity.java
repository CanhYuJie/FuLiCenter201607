package com.yujie.fulicenter201607.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.utils.ConvertUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends AppCompatActivity {
    public static final String TAG = PayActivity.class.getSimpleName();
    @Bind(R.id.edit_user_name)
    EditText editUserName;
    @Bind(R.id.edit_phone)
    EditText editPhone;
    @Bind(R.id.spinner_area)
    Spinner spinnerArea;
    @Bind(R.id.edit_street)
    EditText editStreet;
    @Bind(R.id.pay_btn)
    Button payBtn;
    private Context mContext = PayActivity.this;
    private String payMoney = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        if (initIntent()==null){
            return;
        }
    }

    private String initIntent() {
        payMoney = getIntent().getStringExtra("pay");
        return payMoney;
    }

    @OnClick(R.id.pay_btn)
    public void onClick() {
        String user = editUserName.getText().toString();
        String phone = editPhone.getText().toString();
        String street = editStreet.getText().toString();
        //所在地区获取还没有
        if (ConvertUtils.ifEmpty(user,editUserName)
                &ConvertUtils.ifEmpty(phone,editPhone)
                &ConvertUtils.ifEmpty(street,editStreet)){
            Log.e(TAG, "onClick: "+"支付..." );
        }
    }
}
