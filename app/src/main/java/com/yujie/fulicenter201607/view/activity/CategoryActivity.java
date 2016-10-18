package com.yujie.fulicenter201607.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.CategoryChildBean;
import com.yujie.fulicenter201607.presenter.NewGoodPre;
import com.yujie.fulicenter201607.utils.ToolbarUtil;
import com.yujie.fulicenter201607.view.interface_group.INewGoodsView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryActivity extends AppCompatActivity implements INewGoodsView {
    public static final String TAG = CategoryActivity.class.getSimpleName();
    @Bind(R.id.fragment_Toolbar_titlebar)
    Toolbar fragmentToolbarTitlebar;
    @Bind(R.id.fragment_RecyclerView_new_goods)
    RecyclerView fragmentRecyclerViewNewGoods;
    @Bind(R.id.fragment_SwipeRefreshLayout_refresh)
    SwipeRefreshLayout fragmentSwipeRefreshLayoutRefresh;
    @Bind(R.id.category_name)
    TextView categoryName;
    @Bind(R.id.category_arrow)
    ImageView categoryArrow;
    @Bind(R.id.category_root)
    LinearLayout categoryRoot;
    private NewGoodPre pre;
    private boolean moneyDesc = false;
    private boolean timeDesc = false;
    CategoryChildBean childbean;
    private boolean showPop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_good);
        ButterKnife.bind(this);
        if (initIntent() == null) {
            Toast.makeText(CategoryActivity.this, "没有获取到商品id", Toast.LENGTH_LONG).show();
            return;
        }
        pre = new NewGoodPre(this, this, fragmentRecyclerViewNewGoods, childbean.getId());
        pre.setParent_id(childbean.getParentId());
        initToolbar();
        initRefresh();
    }

    private CategoryChildBean initIntent() {
        childbean = (CategoryChildBean) getIntent().getSerializableExtra("category");
        return childbean;
    }

    private void initRefresh() {
        fragmentSwipeRefreshLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragmentSwipeRefreshLayoutRefresh.setRefreshing(false);
            }
        });
    }

    private void initToolbar() {
        ToolbarUtil.setToolbar(this, fragmentToolbarTitlebar, null, R.drawable.selector_back_bg);
        fragmentToolbarTitlebar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.money_sort:
                        if (moneyDesc) {
                            pre.sortData(I.SORT_BY_PRICE_DESC);
                            moneyDesc = false;
                        } else {
                            pre.sortData(I.SORT_BY_PRICE_ASC);
                            moneyDesc = true;
                        }
                        break;
                    case R.id.time_sort:
                        if (timeDesc) {
                            pre.sortData(I.SORT_BY_ADDTIME_DESC);
                            timeDesc = false;
                        } else {
                            pre.sortData(I.SORT_BY_ADDTIME_ASC);
                            timeDesc = true;
                        }
                        break;
                }
                return false;
            }
        });

        fragmentToolbarTitlebar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        categoryRoot.setVisibility(View.VISIBLE);
        categoryName.setText(childbean.getName());

    }

    @Override
    public void goDetail(int goods_id) {
        startActivity(new Intent(this, GoodsDetailActivity.class).putExtra("goods_id", goods_id + ""));
    }

    @Override
    public void getDataFailed(String msg) {
        if (msg.equals("dismiss")){
            categoryArrow.setImageResource(R.mipmap.expand_on);
            showPop = false;
        }
    }

    @Override
    public void replaceData(CategoryChildBean child) {
        categoryName.setText(child.getName());
        pre.setCat_id(child.getId());
        pre.getGoodsDetails(1, 10, NewGoodPre.METHOD_INIT, null);
    }

    @OnClick(R.id.category_arrow)
    public void onClick() {
        if (showPop){
            categoryArrow.setImageResource(R.mipmap.expand_on);
            showPop = false;
        }else {
            pre.setParent_id(childbean.getParentId());
            pre.showPop(R.id.fragment_root);
            categoryArrow.setImageResource(R.mipmap.expand_off);
            showPop = true;
        }
    }
}
