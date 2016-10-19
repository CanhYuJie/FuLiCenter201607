package com.yujie.fulicenter201607.presenter;


import android.support.v7.app.AppCompatActivity;

import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.model.bean.Result;
import com.yujie.fulicenter201607.utils.OkHttpUtils;
import com.yujie.fulicenter201607.view.interface_group.IRegisterView;

/**
 * Created by yujie on 16-10-11.
 */

public class RegisterPre {
    public static final String TAG = RegisterPre.class.getSimpleName();
    private IRegisterView view;
    private AppCompatActivity activity;
    public RegisterPre(IRegisterView view,AppCompatActivity activity) {
        this.view = view;
        this.activity = activity;
    }

    public void register(String uid,String pwd,String user_name){
        OkHttpUtils<Result> util = new OkHttpUtils<>(activity);
        util.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,uid)
                .addParam(I.User.PASSWORD,pwd)
                .addParam(I.User.NICK,user_name)
                .post()
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null & result.isRetMsg()){
                            view.registerSuccess();
                        }else {
                            view.registerFailed("注册失败,该用户已经存在");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        view.registerFailed("网络不通畅，无法连接到服务器"+error);
                    }
                });
    }
}
