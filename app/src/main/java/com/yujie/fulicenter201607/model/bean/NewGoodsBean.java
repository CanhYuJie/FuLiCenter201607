package com.yujie.fulicenter201607.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yujie on 16-10-13.
 */

public class NewGoodsBean {

    /**
     * id : 731
     * goodsId : 6895
     * catId : 345
     * goodsName : IRM05W 幻彩蛹 变色鼠
     * goodsEnglishName : i-rocks 艾芮克
     * goodsBrief : 传说中IG战队用过的鼠标（啊，别问我IG是什么鬼，真的不知道！）。提供更高的精确度及细致度，USB高速侦测模组技术。3D多彩背光DC技术，可依你喜好设定所需颜色。即插即用，无需安装任何软件。
     * shopPrice : ￥399
     * currencyPrice : ￥414
     * promotePrice : ￥0
     * rankPrice : ￥414
     * isPromote : false
     * goodsThumb : 201508/thumb_img/6895_thumb_G_1439355137855.jpg
     * goodsImg : 201508/thumb_img/6895_thumb_G_1439355137855.jpg
     * addTime : 1442419200000
     * shareUrl : http://m.fulishe.com/item/6895
     * properties : [{"id":8514,"goodsId":0,"colorId":4,"colorName":"绿色","colorCode":"#59d85c","colorImg":"201309/1380064997570506166.jpg","colorUrl":"https://cn.shopbop.com/alexa-chung-loretta-romper-ag/vp/v=1/1573999972.htm?fm=search-shopbysize&os=false","albums":[{"pid":6936,"imgId":26104,"imgUrl":"201508/goods_img/6936_P_1439535131675.png","thumbUrl":"no_picture.gif"}]},{"id":8514,"goodsId":0,"colorId":4,"colorName":"绿色","colorCode":"#59d85c","colorImg":"201309/1380064997570506166.jpg","colorUrl":"https://cn.shopbop.com/alexa-chung-loretta-romper-ag/vp/v=1/1573999972.htm?fm=search-shopbysize&os=false","albums":[{"pid":6936,"imgId":26104,"imgUrl":"201508/goods_img/6936_P_1439535131675.png","thumbUrl":"no_picture.gif"}]}]
     * promote : false
     */

    private int id;
    private int goodsId;
    private int catId;
    private String goodsName;
    private String goodsEnglishName;
    private String goodsBrief;
    private String shopPrice;
    private String currencyPrice;
    private String promotePrice;
    private String rankPrice;
    private boolean isPromote;
    private String goodsThumb;
    private String goodsImg;
    private long addTime;
    private String shareUrl;
    private boolean promote;
    /**
     * id : 8514
     * goodsId : 0
     * colorId : 4
     * colorName : 绿色
     * colorCode : #59d85c
     * colorImg : 201309/1380064997570506166.jpg
     * colorUrl : https://cn.shopbop.com/alexa-chung-loretta-romper-ag/vp/v=1/1573999972.htm?fm=search-shopbysize&os=false
     * albums : [{"pid":6936,"imgId":26104,"imgUrl":"201508/goods_img/6936_P_1439535131675.png","thumbUrl":"no_picture.gif"}]
     */

    private ArrayList<PropertiesBean> properties;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsEnglishName() {
        return goodsEnglishName;
    }

    public void setGoodsEnglishName(String goodsEnglishName) {
        this.goodsEnglishName = goodsEnglishName;
    }

    public String getGoodsBrief() {
        return goodsBrief;
    }

    public void setGoodsBrief(String goodsBrief) {
        this.goodsBrief = goodsBrief;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getCurrencyPrice() {
        return currencyPrice;
    }

    public void setCurrencyPrice(String currencyPrice) {
        this.currencyPrice = currencyPrice;
    }

    public String getPromotePrice() {
        return promotePrice;
    }

    public void setPromotePrice(String promotePrice) {
        this.promotePrice = promotePrice;
    }

    public String getRankPrice() {
        return rankPrice;
    }

    public void setRankPrice(String rankPrice) {
        this.rankPrice = rankPrice;
    }

    public boolean isIsPromote() {
        return isPromote;
    }

    public void setIsPromote(boolean isPromote) {
        this.isPromote = isPromote;
    }

    public String getGoodsThumb() {
        return goodsThumb;
    }

    public void setGoodsThumb(String goodsThumb) {
        this.goodsThumb = goodsThumb;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public boolean isPromote() {
        return promote;
    }

    public void setPromote(boolean promote) {
        this.promote = promote;
    }

    public ArrayList<PropertiesBean> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<PropertiesBean> properties) {
        this.properties = properties;
    }

}
