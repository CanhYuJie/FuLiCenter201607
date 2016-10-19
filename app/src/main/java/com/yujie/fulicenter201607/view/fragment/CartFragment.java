package com.yujie.fulicenter201607.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.presenter.CartPre;
import com.yujie.fulicenter201607.view.activity.PayActivity;
import com.yujie.fulicenter201607.view.interface_group.ICartView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartFragment extends Fragment implements ICartView{
    public static final String TAG = CartFragment.class.getSimpleName();
    @Bind(R.id.sumPrice)
    TextView sumPrice;
    @Bind(R.id.saveMorey)
    TextView saveMorey;
    @Bind(R.id.btn_buy)
    Button btnBuy;
    @Bind(R.id.cart_recyclerview)
    RecyclerView cartRecyclerview;
    @Bind(R.id.cart_refresh_layout)
    SwipeRefreshLayout cartRefreshLayout;
    private Context mContext = getActivity();
    private CartPre pre;
    public CartFragment() {
    }
    private String payMoney;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        pre = new CartPre(getActivity(),this,cartRecyclerview,cartRefreshLayout);
        pre.findCarts(CartPre.CODE_INIT);
        initRefresh();
        return view;
    }

    private void initRefresh() {
        cartRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pre.findCarts(CartPre.CODE_REFRESH);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_buy)
    public void onClick() {
        startActivity(new Intent(getActivity(),PayActivity.class).putExtra("pay",payMoney));
    }

    @Override
    public void changeCount(String sum, String save) {
        if (sum!=null & save!=null){
            payMoney = sum;
            sumPrice.setText("合计  ￥"+sum);
            saveMorey.setText("节省  ￥"+save);
        }

    }
}
