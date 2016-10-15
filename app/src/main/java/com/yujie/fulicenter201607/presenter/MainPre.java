package com.yujie.fulicenter201607.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;

import com.yujie.fulicenter201607.view.activity.MainActivity;
import com.yujie.fulicenter201607.view.interface_group.IMainView;

/**
 * Created by yujie on 16-10-14.
 */

public class MainPre {
    public static final String TAG = MainPre.class.getSimpleName();
    private IMainView view;
    private CartHintBroadcastReceiver receiver;
    public MainPre(IMainView view) {
        this.view = view;
    }

    public void setReceiver(AppCompatActivity activity){
        receiver = new CartHintBroadcastReceiver();
        IntentFilter filter = new IntentFilter("receiver_update_cart_hint");
        activity.registerReceiver(receiver,filter);
    }

    public void unBindReceiver(AppCompatActivity activity) {
        activity.unregisterReceiver(receiver);
    }

    class CartHintBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String cart_hint = intent.getStringExtra("cart_hint");
            view.setReceiver(cart_hint);
        }
    }
}
