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
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                .addParam(I.Collect.GOODS_ID,goods_id)
                .addParam(I.Collect.USER_NAME, FuLiCenterApplication.User)
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
}
