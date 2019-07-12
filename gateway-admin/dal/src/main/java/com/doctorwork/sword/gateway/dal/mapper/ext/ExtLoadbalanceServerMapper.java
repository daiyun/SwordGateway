package com.doctorwork.sword.gateway.dal.mapper.ext;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceServer;

import java.util.List;

public interface ExtLoadbalanceServerMapper {
    List<LoadbalanceServer> get(String lbMark);
}