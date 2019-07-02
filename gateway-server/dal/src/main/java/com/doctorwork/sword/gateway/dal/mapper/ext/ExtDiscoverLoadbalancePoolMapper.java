package com.doctorwork.sword.gateway.dal.mapper.ext;

import com.doctorwork.sword.gateway.dal.model.DiscoverLoadbalancePool;

import java.util.List;
import java.util.Set;

public interface ExtDiscoverLoadbalancePoolMapper {
    DiscoverLoadbalancePool get(String lbMark);

    List<DiscoverLoadbalancePool> list(Set<String> list);
}