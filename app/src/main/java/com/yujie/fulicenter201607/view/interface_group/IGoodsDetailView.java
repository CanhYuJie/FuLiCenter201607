package com.yujie.fulicenter201607.view.interface_group;

import com.yujie.fulicenter201607.model.bean.GoodsDetailsBean;

/**
 * Created by yujie on 16-10-17.
 */

public interface IGoodsDetailView {
    void getDataSuccess(GoodsDetailsBean details);
    void getDataFailed(String msg);
    void get_collected(boolean collect_flag);
    void collect_good(boolean collect_flag);
    void share(GoodsDetailsBean goodsDetails);
    void carted(String cart_count);
}
