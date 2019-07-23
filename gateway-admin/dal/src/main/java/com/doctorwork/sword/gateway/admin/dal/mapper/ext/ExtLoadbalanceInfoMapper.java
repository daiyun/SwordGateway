package com.doctorwork.sword.gateway.admin.dal.mapper.ext;

import com.doctorwork.sword.gateway.admin.dal.model.LoadbalanceInfo;

public interface ExtLoadbalanceInfoMapper {
    LoadbalanceInfo get(String lbMark);
}