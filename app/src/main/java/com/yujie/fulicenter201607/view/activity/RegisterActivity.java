package com.yujie.fulicenter201607.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.presenter.RegisterPre;
import com.yujie.fulicenter201607.view.interface_group.IRegisterView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements IRegisterView{
    public static final String TAG = RegisterActivity.class.getSimpleName();
    @Bind(R.id.register_uid)
    EditText registerUid;
    @Bind(R.id.register_pwd)
    EditText registerPwd;
    @Bind(R.id.register_user_name)
    EditText registerUserName;
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.btn_go_login)
    Button btnGoLogin;
    private RegisterPre registerPre;
    String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        registerPre = new RegisterPre(this,this);
    }

    @OnClick({R.id.btn_register, R.id.btn_go_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                register();
                break;
            case R.id.btn_go_login:
                finish();
                break;
        }
    }

    /**
     * 注册
     */
    private void register() {
        uuid = registerUid.getText().toString();
        String pwd = registerPwd.getText().toString();
        String user_name = registerUserName.getText().toString();
        if (ifEmpty(uuid,registerUid) & ifEmpty(pwd,registerPwd) &
                ifEmpty(user_name,registerUserName)){
            registerPre.register(uuid,pwd,user_name);
        }
    }


    /**
     * 判断是否为空并进行控件提示
     * @param str
     * @param weiget
     * @return
     */
    public boolean ifEmpty(String str,EditText weiget){
        if (str.isEmpty()){
            weiget.setError("不能为空");
            weiget.requestFocus();
            return false;
        }
        return true;
    }


    @Override
    public void registerSuccess() {
        Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra("uid",uuid);
            startActivity(intent);
            finish();
    }

    @Override
    public void registerFailed(String msg) {
        Toast.makeText(RegisterActivity.this,msg,Toast.LENGTH_LONG).show();
    }
}
