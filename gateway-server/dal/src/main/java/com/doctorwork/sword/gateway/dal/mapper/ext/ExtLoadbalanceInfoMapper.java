package com.doctorwork.sword.gateway.dal.mapper.ext;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;

public interface ExtLoadbalanceInfoMapper {
    LoadbalanceInfo get(String lbMark);
}