package com.yujie.fulicenter201607.presenter;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.CategoryChildBean;
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
    private TextView footText;
    private int DEFAULT_SORT_ID = 1;
    public static int METHOD_REFRESH = 101;
    public static int METHOD_LOADMORE = 102;
    public static int METHOD_INIT = 103;

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    private int cat_id;
    public NewGoodPre(INewGoodsView view,FragmentActivity activity,RecyclerView recycleView,int cat_id) {
        this.view = view;
        this.activity = activity;
        this.recycleView = recycleView;
        this.cat_id = cat_id;
        initAdater();
    }

    public void getNewGoods(final int page_id, int page_size, final int method, final SwipeRefreshLayout fragmentSwipeRefreshLayoutRefresh){
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,cat_id+"")
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
                    if (parent_id!=0){
                        getGoodsDetails(DEF_PAGE_ID,DEF_PAGE_SIZE,METHOD_INIT,null);
                    }else {
                        getNewGoods(DEF_PAGE_ID,DEF_PAGE_SIZE,METHOD_INIT, null);
                    }
                }else {
                    DEF_PAGE_ID ++;
                    if (parent_id!= 0){
                        getGoodsDetails(DEF_PAGE_ID,DEF_PAGE_SIZE,METHOD_LOADMORE,null);
                    }else {
                        getNewGoods(DEF_PAGE_ID,DEF_PAGE_SIZE,METHOD_LOADMORE, null);
                    }
                }
            }
        });

        recycleView.setAdapter(loadMoreWrapper);
    }

    public void refresh(SwipeRefreshLayout fragmentSwipeRefreshLayoutRefresh){
        list.clear();
        DEF_PAGE_ID = 1;
        if (parent_id!=0){
//            getGoodsDetails(DEF_PAGE_ID,DEF_PAGE_SIZE,METHOD_REFRESH,fragmentSwipeRefreshLayoutRefresh);
        }else {
            getNewGoods(DEF_PAGE_ID,DEF_PAGE_SIZE,METHOD_REFRESH,fragmentSwipeRefreshLayoutRefresh);
        }
    }

    public void sortData(int sort_id) {
        DEFAULT_SORT_ID = sort_id;
        list = ConvertUtils.sortData(DEFAULT_SORT_ID,list);
        loadMoreWrapper.notifyDataSetChanged();
    }


    /***
     *
     * CateGoryActivity
     *
     *
     *
     *
     *
     *
     */
    private PopupWindow mPopWindow;
    private View mPopList;
    private SwipeRefreshLayout popRefresh;
    private RecyclerView popRecycleView;
    private CommonAdapter<CategoryChildBean> categoryAdapter;
    private ArrayList<CategoryChildBean> childList;
    private LoadMoreWrapper childLoadMore;
    private int parent_id = 0;
    public void showPop(final int parentId) {
        childList = new ArrayList<>();
        View parentLayout = activity.findViewById(parentId);
        mPopList = View.inflate(activity,R.layout.category_item,null);
        popRefresh = (SwipeRefreshLayout) mPopList.findViewById(R.id.popwindow_SwipeRefreshLayout_refresh);
        popRecycleView = (RecyclerView) mPopList.findViewById(R.id.popwindow_RecyclerView_category);
        popRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                childList.clear();
                DEF_PAGE_ID = 1;
                downloadCategoryCHild(parent_id,DEF_PAGE_ID,METHOD_REFRESH);
            }
        });
        popRecycleView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        categoryAdapter = new CommonAdapter<CategoryChildBean>(activity,R.layout.category_txt_item,childList) {
            @Override
            protected void convert(ViewHolder holder, final CategoryChildBean categoryChildBean, int position) {
                holder.setText(R.id.category_txt_items,categoryChildBean.getName());
                holder.setOnClickListener(R.id.category_item_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.replaceData(categoryChildBean);
                    }
                });
            }
        };
        childLoadMore = new LoadMoreWrapper(categoryAdapter);
        footText = new TextView(activity);
        footText.setText("正在加载更多数据...");
        childLoadMore.setLoadMoreView(footText);
        childLoadMore.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (childList.size()==0){
                    DEF_PAGE_ID = 1;
                    downloadCategoryCHild(parent_id,DEF_PAGE_ID,METHOD_INIT);
                }else {
                    DEF_PAGE_ID ++;
                    downloadCategoryCHild(parent_id,DEF_PAGE_ID,METHOD_LOADMORE);
                }
            }
        });
        popRecycleView.setAdapter(childLoadMore);
        showPopupWindow(parentLayout);
    }

    private void showPopupWindow(View parentLayout) {
        mPopWindow = new PopupWindow(mPopList, 160,400);
        //设置触摸PopupWindow之外的区域能关闭PopupWindow
        mPopWindow.setOutsideTouchable(true);
        //设置PopupWindow可触摸
        mPopWindow.setTouchable(true);
        //设置PopupWindow获取焦点
        mPopWindow.setFocusable(true);
        //设置popuWindow的背景,必须设置，否则PopupWindow不能隐藏
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置popuWindow进入和隐藏的动画
        mPopWindow.setAnimationStyle(R.style.styles_pop_window);
        //设置PopuWindow从屏幕底部进入
        mPopWindow.showAtLocation(parentLayout, Gravity.TOP, 50, (int)(90*getScreenDisplay().density));
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                view.getDataFailed("dismiss");
            }
        });
    }

    private DisplayMetrics getScreenDisplay(){
        //创建用于获取屏幕尺寸、像素密度的对象
        Display defaultDisplay  = activity.getWindowManager().getDefaultDisplay();
        //创建用于获取屏幕尺寸、像素密度等信息的对象
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        return outMetrics;
    }


    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public void downloadCategoryCHild(final int id, int page_id, final int method) {
        OkHttpUtils<CategoryChildBean[]> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,String.valueOf(id))
                .addParam(I.PAGE_ID,String.valueOf(page_id))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CategoryChildBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
                    @Override
                    public void onSuccess(CategoryChildBean[] result) {
                        if (result!=null & result.length>0){
                            ArrayList<CategoryChildBean> childBeen = ConvertUtils.array2List(result);
                            if (method == METHOD_REFRESH){
                                childList.clear();
                                childList.addAll(childBeen);
                                childLoadMore.notifyDataSetChanged();
                                popRefresh.setRefreshing(false);
                            }else if (method == METHOD_LOADMORE){
                                childList.addAll(childBeen);
                                childLoadMore.notifyDataSetChanged();
                            }else if (method == METHOD_INIT){
                                childList.clear();
                                childList.addAll(childBeen);
                                childLoadMore.notifyDataSetChanged();
                            }

                        }else {
                            footText.setText("没有更多数据了...");
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    public void getGoodsDetails(final int page_id, int page_size, final int method, final SwipeRefreshLayout fragmentSwipeRefreshLayoutRefresh){
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,cat_id+"")
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
}
