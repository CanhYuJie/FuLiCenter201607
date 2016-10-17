package com.yujie.fulicenter201607.presenter;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.NewGoodsBean;
import com.yujie.fulicenter201607.utils.ConvertUtils;
import com.yujie.fulicenter201607.utils.OkHttpUtils;
import com.yujie.fulicenter201607.view.interface_group.INewGoodsView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by yujie on 16-10-14.
 */

public class NewGoodPre {
    public static final String TAG = NewGoodPre.class.getSimpleName();
    private INewGoodsView view;
    private CommonAdapter<NewGoodsBean> adapter;
    private FragmentActivity activity;
    private int DEF_PAGE_ID = 1;
    private int DEF_PAGE_SIZE = 10;
    private ArrayList<NewGoodsBean> list;
    private GridLayoutManager manager;
    private RecyclerView recycleView;
    private LoadMoreWrapper loadMoreWrapper;
    private HeaderAndFooterWrapper wrapper;
    private TextView footText;
    private int DEFAULT_SORT_ID = 1;
    public static int METHOD_REFRESH = 101;
    public static int METHOD_LOADMORE = 102;
    public static int METHOD_INIT = 103;
    public NewGoodPre(INewGoodsView view,FragmentActivity activity,RecyclerView recycleView) {
        this.view = view;
        this.activity = activity;
        this.recycleView = recycleView;
        initAdater();
    }

    public void getNewGoods(final int page_id, int page_size, final int method, final SwipeRefreshLayout fragmentSwipeRefreshLayoutRefresh){
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,I.CAT_ID+"")
                .addParam(I.PAGE_ID,page_id+"")
                .addParam(I.PAGE_SIZE,page_size+"")
                .targetClass(NewGoodsBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {
                        if (result!=null & result.length>0){
                            ArrayList<NewGoodsBean> newGoodsBeen = ConvertUtils.array2List(result);
                            if (method == METHOD_REFRESH){
                                list.clear();
                                list.addAll(newGoodsBeen);
                                list = ConvertUtils.sortData(DEFAULT_SORT_ID,list);
                                loadMoreWrapper.notifyDataSetChanged();
                                fragmentSwipeRefreshLayoutRefresh.setRefreshing(false);
                            }else if (method == METHOD_LOADMORE){
                                list.addAll(newGoodsBeen);
                                list = ConvertUtils.sortData(DEFAULT_SORT_ID,list);
                                loadMoreWrapper.notifyDataSetChanged();
                            }else if (method == METHOD_INIT){
                                list.clear();
                                list.addAll(newGoodsBeen);
                                list = ConvertUtils.sortData(DEFAULT_SORT_ID,list);
                                loadMoreWrapper.notifyDataSetChanged();
                            }

                        }else {
                            footText.setText("没有更多数据了...");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        view.getDataFailed(error);
                        if (method == METHOD_REFRESH)
                        fragmentSwipeRefreshLayoutRefresh.setRefreshing(false);
                    }
                });
    }


    public void initAdater(){
        list = new ArrayList<>();
        manager = new GridLayoutManager(activity,2);
        recycleView.setLayoutManager(manager);
        adapter = new CommonAdapter<NewGoodsBean>(activity, R.layout.new_goods_item_layout, list) {
            @Override
            protected void convert(ViewHolder holder, final NewGoodsBean newGoodsBean, int position) {
                Picasso.with(activity).load(I.DOWNLOAD_IMG_URL+newGoodsBean.getGoodsThumb())
                        .placeholder(R.drawable.nopic)
                        .error(R.drawable.nopic)
                        .into((ImageView) holder.getView(R.id.adapter_item_new_goods_img));
                holder.setText(R.id.adapter_item_new_goods_name,newGoodsBean.getGoodsName());
                holder.setText(R.id.adapter_item_new_goods_price,newGoodsBean.getCurrencyPrice());
                holder.setOnClickListener(R.id.adapter_item_new_goods_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.goDetail(newGoodsBean.getGoodsId());
                    }
                });
            }
        };
        loadMoreWrapper = new LoadMoreWrapper(adapter);
        footText = new TextView(activity);
        footText.setText("正在加载更多数据...");
        loadMoreWrapper.setLoadMoreView(footText);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (list.size()==0){
                    DEF_PAGE_ID = 1;
                    getNewGoods(DEF_PAGE_ID,DEF_PAGE_SIZE,METHOD_LOADMORE, null);
                }else {
                    DEF_PAGE_ID ++;
                    getNewGoods(DEF_PAGE_ID,DEF_PAGE_SIZE,METHOD_LOADMORE, null);
                }
            }
        });

        recycleView.setAdapter(loadMoreWrapper);
    }

    public void refresh(SwipeRefreshLayout fragmentSwipeRefreshLayoutRefresh){
        list.clear();
        DEF_PAGE_ID = 1;
        getNewGoods(DEF_PAGE_ID,DEF_PAGE_SIZE,METHOD_REFRESH,fragmentSwipeRefreshLayoutRefresh);
    }

    public void sortData(int sort_id) {
        DEFAULT_SORT_ID = sort_id;
        list = ConvertUtils.sortData(DEFAULT_SORT_ID,list);
        loadMoreWrapper.notifyDataSetChanged();
    }
}
