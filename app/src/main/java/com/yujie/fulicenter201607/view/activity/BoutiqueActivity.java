package com.yujie.fulicenter201607.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.BoutiqueBean;
import com.yujie.fulicenter201607.presenter.NewGoodPre;
import com.yujie.fulicenter201607.utils.ToolbarUtil;
import com.yujie.fulicenter201607.view.interface_group.INewGoodsView;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BoutiqueActivity extends AppCompatActivity implements INewGoodsView {
    public static final String TAG = BoutiqueActivity.class.getSimpleName();
    @Bind(R.id.fragment_Toolbar_titlebar)
    Toolbar fragmentToolbarTitlebar;
    @Bind(R.id.fragment_RecyclerView_new_goods)
    RecyclerView fragmentRecyclerViewNewGoods;
    @Bind(R.id.fragment_SwipeRefreshLayout_refresh)
    SwipeRefreshLayout fragmentSwipeRefreshLayoutRefresh;
    private NewGoodPre pre;
    private boolean moneyDesc = false;
    private boolean timeDesc = false;
    BoutiqueBean boutiqueBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_good);
        ButterKnife.bind(this);
        if (initIntent()==null){
            Toast.makeText(BoutiqueActivity.this,"没有获取到商品id",Toast.LENGTH_LONG).show();
            return;
        }
        initToolbar();
        initRefresh();
        pre = new NewGoodPre(this,this,fragmentRecyclerViewNewGoods,boutiqueBean.getId());
        pre.getNewGoods(1,10,NewGoodPre.METHOD_INIT, null);
    }

    private BoutiqueBean initIntent() {
        boutiqueBean = (BoutiqueBean) getIntent().getSerializableExtra("boutique");
        return boutiqueBean;
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
        ToolbarUtil.setToolbar(this, fragmentToolbarTitlebar, boutiqueBean.getTitle(), R.mipmap.menu_item_boutique_normal);
        fragmentToolbarTitlebar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.money_sort:
                        if (moneyDesc){
                            pre.sortData(I.SORT_BY_PRICE_DESC);
                            moneyDesc = false;
                        }else {
                            pre.sortData(I.SORT_BY_PRICE_ASC);
                            moneyDesc = true;
                        }
                        break;
                    case R.id.time_sort:
                        if (timeDesc){
                            pre.sortData(I.SORT_BY_ADDTIME_DESC);
                            timeDesc = false;
                        }else {
                            pre.sortData(I.SORT_BY_ADDTIME_ASC);
                            timeDesc = true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void goDetail(int goods_id) {
        startActivity(new Intent(this, GoodsDetailActivity.class).putExtra("goods_id",goods_id+""));
    }

    @Override
    public void getDataFailed(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
