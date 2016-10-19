package com.yujie.fulicenter201607.presenter;

import android.support.v7.app.AppCompatActivity;

import com.yujie.fulicenter201607.FuLiCenterApplication;
import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.model.bean.GoodsDetailsBean;
import com.yujie.fulicenter201607.model.bean.MessageBean;
import com.yujie.fulicenter201607.utils.OkHttpUtils;
import com.yujie.fulicenter201607.view.interface_group.IGoodsDetailView;

/**
 * Created by yujie on 16-10-17.
 */

public class GoodsDetailPre {
    private IGoodsDetailView view;
    private AppCompatActivity activity;
    private GoodsDetailsBean currentGoods;
    public GoodsDetailPre(IGoodsDetailView view, AppCompatActivity activity) {
        this.view = view;
        this.activity = activity;
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
                            view.carted();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
}
