package com.doctorwork.sword.gateway.admin.dal.mapper.ext;

import com.doctorwork.sword.gateway.admin.dal.model.LoadbalanceServer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtLoadbalanceServerMapper {

    LoadbalanceServer get(Long id);

    List<LoadbalanceServer> getByLbMark(String lbMark);

    int update(@Param("param") LoadbalanceServer server);

    LoadbalanceServer getByIpPort(@Param("lbMark") String lbMark,
                                  @Param("ip") String ip,
                                  @Param("port") Integer port);

    int delete(Long id);

    int updateStatus(@Param("id") Long id, @Param("srvStatus") Integer srvStatus);

    int updateEnable(@Param("id") Long id, @Param("srvEnable") Integer srvEnable);
}