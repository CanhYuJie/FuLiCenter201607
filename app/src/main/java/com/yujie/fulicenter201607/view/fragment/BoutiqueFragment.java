package com.yujie.fulicenter201607.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.presenter.BaotiquePre;
import com.yujie.fulicenter201607.view.interface_group.IBaotiqueView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BoutiqueFragment extends Fragment implements IBaotiqueView{
    public static final String TAG = BoutiqueFragment.class.getSimpleName();
    @Bind(R.id.fragment_RecyclerView_baotique)
    RecyclerView fragmentRecyclerViewBaotique;
    private BaotiquePre pre;
    public BoutiqueFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boutique, container, false);
        ButterKnife.bind(this, view);
        pre = new BaotiquePre(this,getActivity(),fragmentRecyclerViewBaotique);
        pre.getData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void getDataFaild(String msg) {

    }

    @Override
    public void go_boutique(String cat_id) {
        Log.e(TAG, "go_boutique: "+cat_id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
