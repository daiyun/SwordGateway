package com.doctorwork.doctorwork.admin.api.req;

/**
 * @author chenzhiqiang
 * @date 2019/7/27
 */
public class PayloadServerSearchReq extends Page {
    private String lbMark;

    public String getLbMark() {
        return lbMark;
    }

    public void setLbMark(String lbMark) {
        this.lbMark = lbMark;
    }
}
