package com.yujie.fulicenter201607.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.AlbumsBean;
import com.yujie.fulicenter201607.model.bean.GoodsDetailsBean;
import com.yujie.fulicenter201607.model.bean.PropertiesBean;
import com.yujie.fulicenter201607.presenter.GoodsDetailPre;
import com.yujie.fulicenter201607.utils.ShareUtils;
import com.yujie.fulicenter201607.view.interface_group.IGoodsDetailView;
import com.yujie.fulicenter201607.widget.YuJieLoopView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsDetailActivity extends AppCompatActivity implements IGoodsDetailView {
    public static final String TAG = GoodsDetailActivity.class.getSimpleName();
    @Bind(R.id.activity_goods_detail_ImageView_back)
    ImageView activityGoodsDetailImageViewBack;
    @Bind(R.id.activity_goods_detail_ImageView_share)
    ImageView activityGoodsDetailImageViewShare;
    @Bind(R.id.activity_goods_detail_ImageView_collect)
    CheckBox activityGoodsDetailImageViewCollect;
    @Bind(R.id.activity_goods_detail_ImageView_add_cart)
    ImageView activityGoodsDetailImageViewAddCart;
    @Bind(R.id.activity_goods_detail_TextView_cart_number)
    TextView activityGoodsDetailTextViewCartNumber;
    @Bind(R.id.activity_goods_detail_TextView_goods_english_name)
    TextView activityGoodsDetailTextViewGoodsEnglishName;
    @Bind(R.id.activity_goods_detail_TextView_goods_name)
    TextView activityGoodsDetailTextViewGoodsName;
    @Bind(R.id.activity_goods_detail_TextView_goods_price)
    TextView activityGoodsDetailTextViewGoodsPrice;
    @Bind(R.id.activity_goods_detail_TextView_goods_detail)
    TextView activityGoodsDetailTextViewGoodsDetail;
    @Bind(R.id.activity_goods_detail_YuJieLoopView_looper)
    YuJieLoopView activityGoodsDetailYuJieLoopViewLooper;
    private String goods_id;
    private GoodsDetailPre pre;
    private ArrayList<String> imageUrls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        initGoodId();
        initPre();
        initCheckBoxCollect();
    }

    private void initCheckBoxCollect() {
        activityGoodsDetailImageViewCollect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //collect goods;
                    pre.collect_goods(goods_id);
                }else {
                    pre.delete_collect(goods_id);
                }
            }
        });
    }

    private void initPre() {
        pre = new GoodsDetailPre(this, this);
        pre.getDetails(goods_id);
        pre.getCollect(goods_id);
    }

    private void initGoodId() {
        goods_id = getIntent().getStringExtra("goods_id");
    }

    @OnClick({R.id.activity_goods_detail_ImageView_back, R.id.activity_goods_detail_ImageView_share, R.id.activity_goods_detail_ImageView_add_cart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_goods_detail_ImageView_back:
                finish();
                break;
            case R.id.activity_goods_detail_ImageView_share:
                pre.share();
                break;
            case R.id.activity_goods_detail_ImageView_add_cart:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void getDataSuccess(GoodsDetailsBean details) {
        Log.e(TAG, "getDataSuccess: " + details.getProperties().get(0).getAlbums());
        activityGoodsDetailTextViewGoodsEnglishName.setText(details.getGoodsEnglishName());
        activityGoodsDetailTextViewGoodsName.setText(details.getGoodsName());
        initLooperView(details);
        activityGoodsDetailTextViewGoodsPrice.setText(details.getCurrencyPrice());
        activityGoodsDetailTextViewGoodsDetail.setText(details.getGoodsBrief());
    }

    private void initLooperView(GoodsDetailsBean details) {
        imageUrls = new ArrayList<>();
        ArrayList<PropertiesBean> properties = details.getProperties();
        for (PropertiesBean p:properties){
            ArrayList<AlbumsBean> albums = p.getAlbums();
            for (AlbumsBean a:albums){
                String imgUrl = a.getImgUrl();
                imageUrls.add(imgUrl);
            }
        }
        activityGoodsDetailYuJieLoopViewLooper.setImageUrls(imageUrls)
                .startPlay();
    }

    @Override
    public void getDataFailed(String msg) {
        Toast.makeText(GoodsDetailActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void get_collected(boolean collect_flag) {
        activityGoodsDetailImageViewCollect.setChecked(collect_flag);
    }

    @Override
    public void collect_good(boolean collect_flag) {
        activityGoodsDetailImageViewCollect.setChecked(collect_flag);
    }

    @Override
    public void share(GoodsDetailsBean goodsDetails) {
        ShareUtils.showShare(this,goodsDetails.getGoodsName(),
                goodsDetails.getShareUrl(),goodsDetails.getGoodsBrief(),
                goodsDetails.getGoodsImg(),goodsDetails.getShareUrl(),
                goodsDetails.getGoodsBrief(),null,null);
    }
}
