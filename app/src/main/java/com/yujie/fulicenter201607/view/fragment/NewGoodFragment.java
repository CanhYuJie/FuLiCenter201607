package com.yujie.fulicenter201607.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.NewGoodsBean;
import com.yujie.fulicenter201607.presenter.NewGoodPre;
import com.yujie.fulicenter201607.utils.ToolbarUtil;
import com.yujie.fulicenter201607.view.interface_group.INewGoodsView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewGoodFragment extends Fragment implements INewGoodsView{
    public static final String TAG = NewGoodFragment.class.getSimpleName();
    @Bind(R.id.fragment_RecyclerView_new_goods)
    RecyclerView fragmentRecyclerViewNewGoods;
    @Bind(R.id.fragment_Toolbar_titlebar)
    Toolbar fragment_Toolbar_titlebar;
    @Bind(R.id.fragment_SwipeRefreshLayout_refresh)
    SwipeRefreshLayout fragmentSwipeRefreshLayoutRefresh;
    private NewGoodPre pre;
    public NewGoodFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_good, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        initRefresh();
        pre = new NewGoodPre(this,getActivity(),fragmentRecyclerViewNewGoods);
        pre.getNewGoods(1,10,NewGoodPre.METHOD_INIT, null);
        return view;
    }

    private void initRefresh() {
        fragmentSwipeRefreshLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pre.refresh(fragmentSwipeRefreshLayoutRefresh);
            }
        });
    }

    private void initToolbar() {
        ToolbarUtil.setToolbar(getActivity(), fragment_Toolbar_titlebar, "新品", R.mipmap.menu_item_new_good_normal);
        fragment_Toolbar_titlebar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.money_sort:

                        break;
                    case R.id.time_sort:

                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void getDataSuccess() {

    }

    @Override
    public void getDataFailed(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }
}
