package com.doctorwork.sword.gateway.admin.core;

import com.doctorwork.doctorwork.admin.api.req.*;
import com.doctorwork.doctorwork.admin.api.res.PayloadInfoRes;
import com.doctorwork.doctorwork.admin.api.res.PayloadRes;
import com.doctorwork.doctorwork.admin.api.res.RouteInfoRes;
import com.doctorwork.sword.gateway.common.BusinessException;
import com.doctorwork.sword.gateway.common.PageResult;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:19 2019/7/23
 * @Modified By:
 */
public interface GatewayPayloadService {
    PageResult<PayloadRes> payloadSearch(PayloadSearchReq req);

    PayloadInfoRes payloadGet(String lbMark) throws BusinessException;

    void payloadUpdate(PayloadEdit edit) throws BusinessException;

    void payloadAdd(PayloadEdit edit) throws BusinessException;

    void payloadDel(PayloadDel del) throws BusinessException;

    void payloadEnable(String lbMark) throws BusinessException;

    void payloadDisable(String lbMark) throws BusinessException;
}
