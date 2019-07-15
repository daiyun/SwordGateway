package com.doctorwork.sword.gateway.dal.mapper.ext;

import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;

import java.util.List;

public interface ExtDiscoverConfigMapper {
    DiscoverConfig get(String dscrId);

    List<DiscoverConfig> all();
}