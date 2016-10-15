package com.yujie.fulicenter201607.utils;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;

import com.yujie.fulicenter201607.R;

/**
 * Created by yujie on 16-10-14.
 */

public class ToolbarUtil {
    public static void setToolbar(FragmentActivity activity,Toolbar toolbar, String title, int icon){
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(icon);
        toolbar.inflateMenu(R.menu.toolbar_menu);
    }
}
