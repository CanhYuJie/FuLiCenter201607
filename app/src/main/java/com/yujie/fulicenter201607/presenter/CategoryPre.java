package com.yujie.fulicenter201607.presenter;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.R;
import com.yujie.fulicenter201607.model.bean.CategoryChildBean;
import com.yujie.fulicenter201607.model.bean.CategoryGroupBean;
import com.yujie.fulicenter201607.utils.ConvertUtils;
import com.yujie.fulicenter201607.utils.OkHttpUtils;
import com.yujie.fulicenter201607.view.interface_group.ICategoryView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yujie on 16-10-18.
 */

public class CategoryPre {
    public static final String TAG = CategoryPre.class.getSimpleName();
    private FragmentActivity activity;
    private BaseExpandableListAdapter adapter;
    private ExpandableListView listView;
    private ArrayList<CategoryGroupBean> groupBeen;
    private HashMap<Integer, ArrayList<CategoryChildBean>> childgroup;
    private int DEF_PAGE_ID = 1;
    private int METHOD_INIT = 101;
    private int METHOD_LOADMORE = 102;
    private int mVisibleItemCount = 0;
    private ICategoryView view;
    public CategoryPre(FragmentActivity activity, ExpandableListView listView, ICategoryView view) {
        this.activity = activity;
        this.listView = listView;
        this.view = view;
    }

    public void downloadCategoryGroup() {
        groupBeen = new ArrayList<>();
        childgroup = new HashMap<>();
        OkHttpUtils<CategoryGroupBean[]> utils = new OkHttpUtils<>(activity);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
                    @Override
                    public void onSuccess(CategoryGroupBean[] result) {
                        if (result != null & result.length > 0) {
                            groupBeen = ConvertUtils.array2List(result);
                            initAdapter();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initAdapter() {
        adapter = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return groupBeen == null ? 0 : groupBeen.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                if (groupBeen == null || childgroup == null) {
                    return 0;
                } else {
                    ArrayList<CategoryChildBean> childlist = childgroup.get(groupBeen.get(groupPosition).getId());
                    return childlist == null ? 0 : childlist.size();
                }
            }

            @Override
            public CategoryGroupBean getGroup(int groupPosition) {
                return groupBeen.get(groupPosition);
            }

            @Override
            public CategoryChildBean getChild(int groupPosition, int childPosition) {
                if (groupBeen != null || childgroup != null) {
                    ArrayList<CategoryChildBean> childlist = childgroup.get(groupBeen.get(groupPosition).getId());
                    return childlist.get(childPosition);
                }
                return null;
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                GroupViewHolder holder = null;
                if (convertView == null){
                    convertView = LayoutInflater.from(activity).inflate(R.layout.category_group_layout,null);
                    holder = new GroupViewHolder(convertView);
                    convertView.setTag(holder);
                }else {
                    holder = (GroupViewHolder) convertView.getTag();
                }
                CategoryGroupBean group = getGroup(groupPosition);
                if (isExpanded){
                    holder.adapterItemCategoryGroupArrow.setImageResource(R.mipmap.expand_off);
                }else {
                    holder.adapterItemCategoryGroupArrow.setImageResource(R.mipmap.expand_on);
                }
                holder.adapterItemCategoryGroupName.setText(group.getName());
                Picasso.with(activity).load(I.DOWNLOAD_IMG_URL+group.getImageUrl())
                        .placeholder(R.drawable.nopic)
                        .error(R.drawable.nopic)
                        .into(holder.adapterItemCategoryGroupImg);
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                ChildViewHolder holder = null;
                if (convertView == null){
                    convertView = LayoutInflater.from(activity).inflate(R.layout.category_child_layout,null);
                    holder = new ChildViewHolder(convertView);
                    convertView.setTag(holder);
                }else {
                    holder = (ChildViewHolder) convertView.getTag();
                }
                CategoryChildBean child = getChild(groupPosition, childPosition);
                if (child!=null & (child.getParentId() == getGroup(groupPosition).getId())){
                    holder.adapterItemCategoryChildName.setText(child.getName());
                    Picasso.with(activity).load(I.DOWNLOAD_IMG_URL+child.getImageUrl())
                            .placeholder(R.drawable.nopic)
                            .error(R.drawable.nopic)
                            .into(holder.adapterItemCategoryChildImg);
                }
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        };

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                boolean isExpand = parent.isGroupExpanded(groupPosition);
                if (isExpand){
                    listView.collapseGroup(groupPosition);
                    collapseItem(groupPosition);
                    DEF_PAGE_ID = 1;
                }else {
                    if (childgroup.get(groupBeen.get(groupPosition).getId())!=null){
                        listView.expandGroup(groupPosition);
                        collapseItem(groupPosition);
                        return true;
                    }else {
                        downloadCategoryCHild(groupBeen.get(groupPosition).getId(),
                                groupPosition,DEF_PAGE_ID,METHOD_INIT);
                    }
                }
                return false;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ArrayList<CategoryChildBean> childgrouplist = childgroup.get(groupBeen.get(groupPosition).getId());
                CategoryChildBean categoryChildBean = childgrouplist.get(childPosition);
                view.childClick(categoryChildBean);
                return false;
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    ArrayList<CategoryChildBean> enableChildGroup = getEnableGroup();
                    if (enableChildGroup!=null){
                        if (mVisibleItemCount-enableChildGroup.size() <= (groupBeen.size())){
                            int id = enableChildGroup.get(0).getParentId();
                            DEF_PAGE_ID ++;
                            downloadCategoryCHild(id,0,DEF_PAGE_ID,METHOD_LOADMORE);
                        }
                    }else {
                        Log.e(TAG, "onScrollStateChanged: "+"No EnableChildGroup" );
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mVisibleItemCount = visibleItemCount;
            }
        });
        listView.setAdapter(adapter);
    }


    public ArrayList<CategoryChildBean> getEnableGroup(){
        for (int i = 0; i < groupBeen.size(); i ++){
            if (listView.isGroupExpanded(i)){
                return childgroup.get(groupBeen.get(i).getId());
            }
        }
        return null;
    }

    private void collapseItem(int groupPosition) {
        for (int i = 0; i < groupBeen.size(); i ++){
            if (i == groupPosition)
                continue;
            listView.collapseGroup(i);
        }
    }

    private void downloadCategoryCHild(final int id, final int group_id, int page_id, final int method) {
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
                            if (method == METHOD_INIT){
                                childgroup.put(id,childBeen);
                                adapter.notifyDataSetChanged();
                                listView.expandGroup(group_id);
                                collapseItem(group_id);
                            }else if (method == METHOD_LOADMORE){
                                ArrayList<CategoryChildBean> list = childgroup.get(id);
                                for (CategoryChildBean c:list){
                                    for (CategoryChildBean l:childBeen){
                                        if (c.getName().equals(l.getName())){
                                            Log.e(TAG, "onSuccess: "+l.getName()+"is same to"+
                                                    c.getName()+"is killed");
                                            return;
                                        }
                                    }
                                }
                                list.addAll(childBeen);
                                adapter.notifyDataSetChanged();
                            }
                        }else {

                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }


    static class GroupViewHolder {
        @Bind(R.id.adapter_item_category_group_img)
        ImageView adapterItemCategoryGroupImg;
        @Bind(R.id.adapter_item_category_group_name)
        TextView adapterItemCategoryGroupName;
        @Bind(R.id.adapter_item_category_group_arrow)
        ImageView adapterItemCategoryGroupArrow;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @Bind(R.id.adapter_item_category_child_img)
        ImageView adapterItemCategoryChildImg;
        @Bind(R.id.adapter_item_category_child_name)
        TextView adapterItemCategoryChildName;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
