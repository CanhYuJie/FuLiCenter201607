package com.yujie.fulicenter201607.presenter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yujie.fulicenter201607.FuLiCenterApplication;
import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.CartBean;
import com.yujie.fulicenter201607.model.bean.GoodsDetailsBean;
import com.yujie.fulicenter201607.model.bean.MessageBean;
import com.yujie.fulicenter201607.utils.ConvertUtils;
import com.yujie.fulicenter201607.utils.OkHttpUtils;
import com.yujie.fulicenter201607.view.interface_group.ICartView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yujie on 16-10-19.
 */

public class CartPre {
    public static final String TAG = CartPre.class.getSimpleName();
    private FragmentActivity activity;
    private ICartView view;
    private int DEF_PAGE_ID = 1;
    private int DEF_PAGE_SIZE = 10;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    public static final int CODE_INIT = 1001;
    public static final int CODE_REFRESH = 1002;
    public static final int CODE_LOADMORE = 1003;
    private CommonAdapter<CartBean> cartAdapter;
    private LoadMoreWrapper loadMoreWrapper;
    private ArrayList<CartBean> cartlist;
    private HashMap<Integer,GoodsDetailsBean> goodsDetailsList;
    private TextView footText;
    public CartPre(FragmentActivity activity, ICartView view,RecyclerView recyclerView, SwipeRefreshLayout refreshLayout) {
        this.activity = activity;
        this.view = view;
        this.recyclerView = recyclerView;
        this.refreshLayout = refreshLayout;
        cartlist = new ArrayList<>();
        goodsDetailsList = new HashMap<Integer, GoodsDetailsBean>();
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        cartAdapter = new CommonAdapter<CartBean>(activity, R.layout.cartlayout,cartlist) {
            @Override
            protected void convert(final ViewHolder holder, final CartBean cartBean, int position) {
                if (cartlist.size()!=goodsDetailsList.size()){
                    Log.e(TAG, "convert: "+cartlist.size()+"***"+goodsDetailsList.size());
                    return;
                }
                holder.setText(R.id.cart_goods_count,"("+String.valueOf(cartBean.getCount())+")");
                holder.setChecked(R.id.cart_check,cartBean.isChecked());
                if (cartBean.getGoodsId() == cartlist.get(position).getGoodsId()){
                    GoodsDetailsBean detailsBean = goodsDetailsList.get(cartBean.getGoodsId());
                    holder.setText(R.id.cart_goods_count,String.valueOf(cartBean.getCount()));
                    holder.setText(R.id.cart_goods_price,detailsBean.getShopPrice());
                    holder.setText(R.id.cart_goods_name,detailsBean.getGoodsName());
                    Picasso.with(activity).load(I.DOWNLOAD_IMG_URL+detailsBean.getGoodsThumb())
                            .placeholder(R.drawable.nopic)
                            .error(R.drawable.nopic)
                            .into((ImageView) holder.getView(R.id.cartgoods_img));
                    holder.setOnClickListener(R.id.btn_add_cart, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cartBean.setCount(cartBean.getCount()+1);
                            refreshCartCount(cartBean.getId(),cartBean.getCount(),
                                    ((CheckBox)holder.getView(R.id.cart_check)).isChecked(),holder);
                            initMainCount();
                        }
                    });
                    holder.setOnClickListener(R.id.btn_del_cart, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cartBean.setCount(cartBean.getCount()-1);
                            refreshCartCount(cartBean.getId(),cartBean.getCount(),
                                    ((CheckBox)holder.getView(R.id.cart_check)).isChecked(),holder);
                            initMainCount();
                        }
                    });

                    ((CheckBox)holder.getView(R.id.cart_check)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            cartBean.setChecked(isChecked);
                            initMainCount();
                            refreshCartCount(cartBean.getId(),cartBean.getCount(),isChecked,holder);
                        }
                    });
                }

            }
        };
        loadMoreWrapper = new LoadMoreWrapper(cartAdapter);
        footText = new TextView(activity);
        footText.setText("正在加载更多数据...");
        loadMoreWrapper.setLoadMoreView(footText);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (cartlist.size()<=DEF_PAGE_SIZE){
                    DEF_PAGE_ID = 1;
                    findCarts(CODE_INIT);
                }else {
                    DEF_PAGE_ID ++;
                    findCarts(CODE_LOADMORE);
                }
            }
        });
        recyclerView.setAdapter(loadMoreWrapper);
    }

    private void refreshCartCount(int cart_id, final int count, boolean checked, final ViewHolder holder) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID,String.valueOf(cart_id))
                .addParam(I.Cart.COUNT,String.valueOf(count))
                .addParam(I.Cart.IS_CHECKED,String.valueOf(checked))
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result!=null & result.isSuccess()){
                            holder.setText(R.id.cart_goods_count,String.valueOf(count));
                        }else {
                            Log.e(TAG, "onSuccess: 修改失败" );
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

    }

    private void initMainCount() {
        int sum = 0;
        int total = 0;
        for (CartBean c:cartlist){
            if (c.isChecked()){
                int count = c.getCount();
                GoodsDetailsBean goods = goodsDetailsList.get(c.getGoodsId());
                String shopPrice = goods.getShopPrice();
                sum+=count*Integer.parseInt(shopPrice.substring(1,shopPrice.length()));
                String currencyPrice = goods.getCurrencyPrice();
                total+=count*Integer.parseInt(currencyPrice.substring(1,currencyPrice.length()));
            }else {
                continue;
            }
        }
        view.changeCount(sum+"",total-sum+"");
    }

    public void findCarts(final int method){
        OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME,
                        FuLiCenterApplication.getInstance().getCurrentUser().getRetData()
                .getMuserName())
                .addParam(I.PAGE_ID,String.valueOf(DEF_PAGE_ID))
                .addParam(I.PAGE_SIZE,String.valueOf(DEF_PAGE_SIZE))
                .targetClass(CartBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] result) {
                        if (result!=null & result.length>0){
                            ArrayList<CartBean> cartBeen = ConvertUtils.array2List(result);
                            if (noRepeat(cartBeen)){
                                if (method == CODE_INIT){
                                    cartlist.addAll(cartBeen);
                                    downloadGoodsDetail(cartBeen,method);
                                }else if (method == CODE_REFRESH){
                                    cartlist.clear();
                                    goodsDetailsList.clear();
                                    cartlist.addAll(cartBeen);
                                    refreshLayout.setRefreshing(false);
                                    downloadGoodsDetail(cartBeen,method);
                                }else if (method == CODE_LOADMORE){
                                    cartlist.addAll(ConvertUtils.array2List(result));
                                    downloadGoodsDetail(cartBeen,method);
                                }
                            }else {
                                footText.setText("没有更多数据");
                                return;
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private boolean noRepeat(ArrayList<CartBean> cartBeen) {
        for (CartBean c:cartlist){
            for (CartBean b:cartBeen){
                if (c.getGoodsId()==b.getGoodsId()){
                    return false;
                }
            }
        }
        return true;
    }

    private void downloadGoodsDetail(ArrayList<CartBean> carts,final int method) {
        for(final CartBean cart:carts){
            OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(activity);
            utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                    .addParam(I.GoodsDetails.KEY_GOODS_ID,String.valueOf(cart.getGoodsId()))
                    .targetClass(GoodsDetailsBean.class)
                    .execute(new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
                        @Override
                        public void onSuccess(GoodsDetailsBean result) {
                            Log.e(TAG, "downloadGoodsDetail: "+result);
                            if (result!=null){
                                goodsDetailsList.put(cart.getGoodsId(),result);
                                if (goodsDetailsList.size()==cartlist.size()){
                                    if (method == CODE_INIT){
                                        initAdapter();
                                        initMainCount();
                                    }else if (method == CODE_REFRESH){
                                        loadMoreWrapper.notifyDataSetChanged();
                                        initMainCount();
                                    }else if (method == CODE_LOADMORE){
                                        loadMoreWrapper.notifyDataSetChanged();
                                        initMainCount();
                                    }
                                }
                            }else {
                                cartlist.remove(cart);
                                if (goodsDetailsList.size()==cartlist.size()){
                                    if (method == CODE_INIT){
                                        initAdapter();
                                        initMainCount();
                                    }else if (method == CODE_REFRESH){
                                        loadMoreWrapper.notifyDataSetChanged();
                                        initMainCount();
                                    }else if (method == CODE_LOADMORE){
                                        loadMoreWrapper.notifyDataSetChanged();
                                        initMainCount();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(String error) {
                        }
                    });
        }
    }
}
