package com.yujie.fulicenter201607.presenter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.BoutiqueBean;
import com.yujie.fulicenter201607.utils.ConvertUtils;
import com.yujie.fulicenter201607.utils.OkHttpUtils;
import com.yujie.fulicenter201607.view.interface_group.IBaotiqueView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

/**
 * Created by yujie on 16-10-17.
 */

public class BaotiquePre {
    private IBaotiqueView view;
    private FragmentActivity activity;
    private RecyclerView recyclerView;
    private CommonAdapter<BoutiqueBean> adapter;
    public BaotiquePre(IBaotiqueView view,FragmentActivity activity,RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.view = view;
        this.activity = activity;
    }

    public void getData(){
        OkHttpUtils<BoutiqueBean[]> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
                    @Override
                    public void onSuccess(BoutiqueBean[] result) {
                        if (result != null & result.length>0){
                            ArrayList<BoutiqueBean> boutiqueBeen = ConvertUtils.array2List(result);
                            setAdapter(boutiqueBeen);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void setAdapter(ArrayList<BoutiqueBean> boutiqueBeen) {
        adapter = new CommonAdapter<BoutiqueBean>(activity, R.layout.boutique_item,boutiqueBeen) {
            @Override
            protected void convert(ViewHolder holder, final BoutiqueBean boutiqueBean, int position) {
                holder.setText(R.id.adapter_item_boutique_title,boutiqueBean.getTitle());
                holder.setText(R.id.adapter_item_boutique_desc,boutiqueBean.getDescription());
                holder.setText(R.id.adapter_item_boutique_name,boutiqueBean.getName());
                Picasso.with(activity).load(I.DOWNLOAD_IMG_URL+boutiqueBean.getImageurl())
                        .placeholder(R.drawable.nopic)
                        .error(R.drawable.nopic)
                        .into((ImageView) holder.getView(R.id.adapter_item_boutique_img));
                holder.setOnClickListener(R.id.adapter_item_boutique_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.go_boutique(boutiqueBean.getId()+"");
                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }
}
