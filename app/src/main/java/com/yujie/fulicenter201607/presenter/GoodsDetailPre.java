package com.yujie.fulicenter201607.presenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yujie.fulicenter201607.FuLiCenterApplication;
import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.CartBean;
import com.yujie.fulicenter201607.model.bean.GoodsDetailsBean;
import com.yujie.fulicenter201607.model.bean.MessageBean;
import com.yujie.fulicenter201607.utils.ConvertUtils;
import com.yujie.fulicenter201607.utils.OkHttpUtils;
import com.yujie.fulicenter201607.view.interface_group.IGoodsDetailView;

import java.util.ArrayList;

/**
 * Created by yujie on 16-10-17.
 */

public class GoodsDetailPre {
    public static final String TAG = GoodsDetailPre.class.getSimpleName();
    private IGoodsDetailView view;
    private AppCompatActivity activity;
    private GoodsDetailsBean currentGoods;
    private int currentCount = 0;
    private CartBean currentCart = null;
    public GoodsDetailPre(IGoodsDetailView view, AppCompatActivity activity) {
        this.view = view;
        this.activity = activity;
    }

    public void initCartCount(final String goods_id) {
        OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME,
                        FuLiCenterApplication.getInstance().getCurrentUser().getRetData()
                                .getMuserName())
                .targetClass(CartBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] result) {
                        if (result!=null & result.length>0){
                            ArrayList<CartBean> cartBeen = ConvertUtils.array2List(result);
                            for (CartBean cart:cartBeen){
                                int goodsId = cart.getGoodsId();
                                if (String.valueOf(goodsId).equals(goods_id)){
                                    currentCount = cart.getCount();
                                    currentCart = cart;
                                }
                            }
                            activity.sendBroadcast(new Intent("receiver_update_cart_hint").putExtra("cart_hint",cartBeen.size()+""));
                            if (view!=null)
                            view.carted(String.valueOf(currentCount));
                        }else {
                            if (view!=null)
                            view.carted(null);
                        }
                    }

                    @Override
                    public void onError(String error) {
                            view.carted(null);
                    }
                });
    }

    public void getDetails(String goods_id){
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,goods_id)
                .targetClass(GoodsDetailsBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
                    @Override
                    public void onSuccess(GoodsDetailsBean result) {
                        if (result!=null){
                            currentGoods = result;
                            view.getDataSuccess(result);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        view.getDataFailed(error);
                    }
                });
    }

    public void getCollect(String goods_id){
        if (FuLiCenterApplication.getInstance().getCurrentUser()!=null){
            OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(activity);
            utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                    .addParam(I.Collect.GOODS_ID,goods_id)
                    .addParam(I.Collect.USER_NAME,
                            FuLiCenterApplication.getInstance().getCurrentUser().getRetData().getMuserName())
                    .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null){
                                view.get_collected(result.isSuccess());
                            }
                        }

                        @Override
                        public void onError(String error) {
                            view.getDataFailed(error);
                        }
                    });
        }else {
            view.get_collected(false);
            return;
        }
    }

    public void collect_goods(String goods_id){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                .addParam(I.Collect.GOODS_ID,goods_id)
                .addParam(I.Collect.USER_NAME, FuLiCenterApplication.User)
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null){
                            view.collect_good(result.isSuccess());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        view.getDataFailed(error);
                    }
                });
    }

    public void delete_collect(String goods_id) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                .addParam(I.Collect.GOODS_ID,goods_id)
                .addParam(I.Collect.USER_NAME, FuLiCenterApplication.User)
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null){
                            view.collect_good(!result.isSuccess());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        view.getDataFailed(error);
                    }
                });
    }

    public void share() {
        view.share(currentGoods);
    }

    public void addToCart() {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.GOODS_ID,currentGoods.getGoodsId()+"")
                .addParam(I.Cart.USER_NAME,FuLiCenterApplication.getInstance().getCurrentUser().getRetData().getMuserName())
                .addParam(I.Cart.COUNT,1+"")
                .addParam(I.Cart.IS_CHECKED,String.valueOf(true))
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result!=null & result.isSuccess()){
                            currentCount=1;
                            view.carted("1");
                            initCartCount(currentGoods.getGoodsId()+"");
                        }else {
                            view.carted(null);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        view.carted(null);
                    }
                });
    }

    public void updateCart(CartBean cart){
        currentCount++;
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID,String.valueOf(cart.getId()))
                .addParam(I.Cart.COUNT,String.valueOf(currentCount))
                .addParam(I.Cart.IS_CHECKED,String.valueOf(cart.isChecked()))
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result!=null & result.isSuccess()){
                            view.carted(currentCount+"");
                        }else {
                            currentCount--;
                            view.carted(currentCount+"");
                            Log.e(TAG, "onSuccess: 修改失败" );
                        }
                    }

                    @Override
                    public void onError(String error) {
                        currentCount--;
                        view.carted(currentCount+"");
                    }
                });
    }

    public void checkCart() {
        if (currentCount>0 & currentCart!=null){
            updateCart(currentCart);
        }else {
            addToCart();
        }
    }
}
