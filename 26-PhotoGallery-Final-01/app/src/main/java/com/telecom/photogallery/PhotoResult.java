package com.telecom.photogallery;

import java.util.List;

/**
 * Created by xiaojf on 18/1/8.
 */

public class PhotoResult {
    private int pageSize;
    private int pageNo = 0;
    private int pageCount;
    private int itemCount = -1;
    private List<PhotoItem> datas;

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public List<PhotoItem> getDatas() {
        return datas;
    }

}
