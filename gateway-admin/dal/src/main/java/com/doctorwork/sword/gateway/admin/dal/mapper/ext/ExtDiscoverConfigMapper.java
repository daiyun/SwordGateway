package com.doctorwork.sword.gateway.admin.dal.mapper.ext;

import com.doctorwork.sword.gateway.admin.dal.model.DiscoverConfig;

import java.util.List;

public interface ExtDiscoverConfigMapper {
    DiscoverConfig get(String dscrId);

    List<DiscoverConfig> preLoad();
}