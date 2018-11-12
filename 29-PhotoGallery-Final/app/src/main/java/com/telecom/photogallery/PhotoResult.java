package com.telecom.photogallery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojf on 18/1/8.
 */

public class PhotoResult {
    private int pageSize;
    private int pageNo = 0;
    private int pageCount;
    private int itemCount = -1;
    private List<PhotoItem> datas = new ArrayList<>();

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public List<PhotoItem> getDatas() {
        return datas;
    }

    public void setDatas(List<PhotoItem> datas) {
        this.datas = datas;
    }
}
