package com.yujie.fulicenter201607.presenter;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yujie.fulicenter201607.FuLiCenterApplication;
import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.model.bean.Result;
import com.yujie.fulicenter201607.utils.OkHttpUtils;
import com.yujie.fulicenter201607.view.interface_group.ILoginView;


/**
 * Created by yujie on 16-10-11.
 */

public class LoginPre {
    public static final String TAG = LoginPre.class.getSimpleName();
    private ILoginView view;
    private AppCompatActivity activity;
    public LoginPre(ILoginView view,AppCompatActivity activity) {
        this.view = view;
        this.activity = activity;
    }

    public void login(String userName,String password){
        OkHttpUtils<Result> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.PASSWORD,password)
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null & result.isRetMsg()){
                            FuLiCenterApplication.getInstance().setCurrentUser(result);
                            view.loginSuccess(result);
                        }else {
                            view.loginFailed("no user in database!");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        view.loginFailed("can't connect to server,login failed");
                    }
                });

    }
}
