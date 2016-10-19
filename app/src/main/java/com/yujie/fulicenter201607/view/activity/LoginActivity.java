package com.yujie.fulicenter201607.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.Result;
import com.yujie.fulicenter201607.presenter.LoginPre;
import com.yujie.fulicenter201607.utils.SpUtils;
import com.yujie.fulicenter201607.view.interface_group.ILoginView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ILoginView{
    public static final String TAG = LoginActivity.class.getSimpleName();
    @Bind(R.id.login_activity_EditText_inputPhone)
    EditText loginActivityEditTextInputPhone;
    @Bind(R.id.login_activity_EditText_inputPwd)
    EditText loginActivityEditTextInputPwd;
    @Bind(R.id.login_activity_Button_login)
    Button loginActivityButtonLogin;
    @Bind(R.id.login_activity_Button_register)
    Button loginActivityButtonRegister;
    private Context mContext = LoginActivity.this;
    String userName;
    String password;
    private LoginPre mLoginPre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginPre = new LoginPre(this,this);
    }

    @OnClick({R.id.login_activity_Button_login, R.id.login_activity_Button_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_activity_Button_login:
                if (invalidEmpty()) return;
                mLoginPre.login(userName,password);
                break;
            case R.id.login_activity_Button_register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
        }
    }


    private boolean invalidEmpty() {
        userName = loginActivityEditTextInputPhone.getText().toString();
        password = loginActivityEditTextInputPwd.getText().toString();
        if (userName.isEmpty()){
            loginActivityEditTextInputPhone.setError("no input,please input");
            loginActivityEditTextInputPhone.requestFocus();
            return true;
        }
        if (password.isEmpty()){
            loginActivityEditTextInputPwd.setError("no input,please input");
            loginActivityEditTextInputPwd.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        loginActivityEditTextInputPhone.setText(uid);
    }

    @Override
    public void loginSuccess(Result result) {
        SpUtils.saveObject(this,"current_user",result);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    public void loginFailed(String msg) {
        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();
    }
}
