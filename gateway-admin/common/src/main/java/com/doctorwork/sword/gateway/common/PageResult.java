package com.doctorwork.sword.gateway.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxingle on 2017/12/20.
 */
public class PageResult<T> implements Serializable {
    private long totalCount;
    private long totalPage;
    private int pageNum;
    private int pageSize;
    private List<T> list;

    public PageResult() {
    }

    public PageResult(long totalCount, long totalPage, int pageNum, int pageSize, List<T> list) {
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.list = list;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

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

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

