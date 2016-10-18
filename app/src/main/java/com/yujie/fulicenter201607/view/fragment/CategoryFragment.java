package com.yujie.fulicenter201607.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.CategoryChildBean;
import com.yujie.fulicenter201607.presenter.CategoryPre;
import com.yujie.fulicenter201607.view.interface_group.ICategoryView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CategoryFragment extends Fragment implements ICategoryView{
    public static final String TAG = CategoryFragment.class.getSimpleName();
    @Bind(R.id.fragment_ExpandableListView_category)
    ExpandableListView fragmentExpandableListViewCategory;
    private CategoryPre pre;
    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        pre = new CategoryPre(getActivity(),fragmentExpandableListViewCategory,this);
        pre.downloadCategoryGroup();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void childClick(CategoryChildBean child) {
        Log.e(TAG, "childClick: "+child.getParentId()+"\n"+child.getId()+"\n"+child.getName() );
    }
}
