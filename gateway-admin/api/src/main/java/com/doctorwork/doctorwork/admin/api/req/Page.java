package com.doctorwork.doctorwork.admin.api.req;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:29 2019/7/23
 * @Modified By:
 */
public class Page {
    private int pageNum = 1;
    private int pageSize = 10;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
