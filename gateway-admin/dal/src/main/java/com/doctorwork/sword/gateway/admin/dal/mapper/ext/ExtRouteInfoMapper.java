package com.doctorwork.sword.gateway.admin.dal.mapper.ext;

import com.doctorwork.sword.gateway.admin.dal.model.RouteInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtRouteInfoMapper {
    List<RouteInfo> list();

    RouteInfo get(String routeMark);

    int update(@Param("param") RouteInfo routeInfo);

    int insert(@Param("param") RouteInfo routeInfo);

    int updateStatus(@Param("routeMark") String routeMark, @Param("routeStatus") Integer routeStatus);

    int delete(String routeMark);
}