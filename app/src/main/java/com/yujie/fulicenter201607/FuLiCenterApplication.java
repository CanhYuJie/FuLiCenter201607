package com.yujie.fulicenter201607;

import android.app.Application;
import android.content.Context;

/**
 * Created by yujie on 16-10-13.
 */

public class FuLiCenterApplication extends Application {
    public static final String TAG = FuLiCenterApplication.class.getSimpleName();
    private static FuLiCenterApplication instance = null;
    private FuLiCenterApplication(){}
    public static FuLiCenterApplication getInstance() {
        synchronized (FuLiCenterApplication.class) {
            if (instance == null) {
                instance = new FuLiCenterApplication();
            }
        }
        return instance;
    }
}
