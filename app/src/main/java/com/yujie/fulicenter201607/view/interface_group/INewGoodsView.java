package com.yujie.fulicenter201607.view.interface_group;


import com.yujie.fulicenter201607.model.bean.CategoryChildBean;

/**
 * Created by yujie on 16-10-14.
 */

public interface INewGoodsView {
    void goDetail(int goods_id);
    void getDataFailed(String msg);
    void replaceData(CategoryChildBean child);
}
