package com.yujie.fulicenter201607.view.interface_group;


import com.yujie.fulicenter201607.model.bean.Result;

/**
 * Created by yujie on 16-10-11.
 */

public interface ILoginView {
    /** 登录成功*/
    void loginSuccess(Result result);
    /** 登录失败*/
    void loginFailed(String msg);
}
