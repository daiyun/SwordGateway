package com.doctorwork.sword.gateway.dal.mapper.ext;

import com.doctorwork.sword.gateway.dal.model.DiscoverLoadbalancePool;

public interface ExtDiscoverLoadbalancePoolMapper {
    DiscoverLoadbalancePool get(String lbMark);
}