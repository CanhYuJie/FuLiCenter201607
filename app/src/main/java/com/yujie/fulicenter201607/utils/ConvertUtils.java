package com.yujie.fulicenter201607.utils;

import android.content.Context;

import com.yujie.fulicenter201607.I;
import com.yujie.fulicenter201607.model.bean.NewGoodsBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 各种转换方法的工具类
 * Created by yao on 2016/10/2.
 */

public class ConvertUtils {
    public static <T> ArrayList<T> array2List(T[] array) {
        List<T> list = Arrays.asList(array);
        ArrayList<T> arrayList = new ArrayList<>(list);
        return arrayList;
    }

    public static int px2dp(Context context, int px){
        int density = (int) context.getResources().getDisplayMetrics().density;
        return px/density;
    }

    public static ArrayList<NewGoodsBean> sortData(int sortType, ArrayList<NewGoodsBean> list) {
        switch (sortType){
            case I.SORT_BY_PRICE_ASC:
                Collections.sort(list, new Comparator<NewGoodsBean>() {
                    @Override
                    public int compare(NewGoodsBean lhs, NewGoodsBean rhs) {
                        String lhsCurrencyPrice = lhs.getCurrencyPrice();
                        String rhsCurrentyPrice = rhs.getCurrencyPrice();
                        long lhsPrice = Long.parseLong(lhsCurrencyPrice.substring(1, lhsCurrencyPrice.length()));
                        long rhsPrice = Long.parseLong(rhsCurrentyPrice.substring(1,rhsCurrentyPrice.length()));
                        if(lhsPrice>rhsPrice){
                            return 1;
                        }
                        return -1;
                    }
                });
                break;
            case I.SORT_BY_PRICE_DESC:
                Collections.sort(list, new Comparator<NewGoodsBean>() {
                    @Override
                    public int compare(NewGoodsBean lhs, NewGoodsBean rhs) {
                        String lhsCurrencyPrice = lhs.getCurrencyPrice();
                        String rhsCurrentyPrice = rhs.getCurrencyPrice();
                        long lhsPrice = Long.parseLong(lhsCurrencyPrice.substring(1, lhsCurrencyPrice.length()));
                        long rhsPrice = Long.parseLong(rhsCurrentyPrice.substring(1,rhsCurrentyPrice.length()));
                        if(lhsPrice<rhsPrice){
                            return 1;
                        }
                        return -1;
                    }
                });
                break;
            case I.SORT_BY_ADDTIME_ASC:
                Collections.sort(list, new Comparator<NewGoodsBean>() {
                    @Override
                    public int compare(NewGoodsBean lhs, NewGoodsBean rhs) {
                        long lhsaddTime = lhs.getAddTime();
                        long rhsaddTime = rhs.getAddTime();
                        if(lhsaddTime>rhsaddTime){
                            return 1;
                        }
                        return -1;
                    }
                });
                break;
            case I.SORT_BY_ADDTIME_DESC:
                Collections.sort(list, new Comparator<NewGoodsBean>() {
                    @Override
                    public int compare(NewGoodsBean lhs, NewGoodsBean rhs) {
                        long lhsaddTime = lhs.getAddTime();
                        long rhsaddTime = rhs.getAddTime();
                        if(lhsaddTime<rhsaddTime){
                            return 1;
                        }
                        return -1;
                    }
                });
                break;
        }
        return list;
    }



    public   static   ArrayList<?>  removeDuplicateWithOrder(ArrayList<?> list)   {
        Set set  =   new HashSet();
        ArrayList newList  =   new  ArrayList();
        for  (Iterator iter = list.iterator(); iter.hasNext();)   {
            Object element  =  iter.next();
            if  (set.add(element))
                newList.add(element);
        }
        return newList;
    }
}
