package com.doctorwork.sword.gateway.dal.mapper;

import com.doctorwork.sword.gateway.dal.model.RouteInfo;

import java.util.List;

public interface RouteInfoMapper {
    /**
     * insert one record into table from <tt>route_info</tt>
     *
     * @param record
     */
    int insert(RouteInfo record);

    /**
     *
     * @param record
     */
    int insertSelective(RouteInfo record);

    /**
     * get one record by primary key from <tt>route_info</tt>
     *
     * @param id
     */
    RouteInfo selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(RouteInfo record);

    /**
     * update one record by primary key from <tt>route_info</tt>
     *
     * @param record
     */
    int updateByPrimaryKey(RouteInfo record);

    /**
     * get all records from  <tt>route_info</tt>
     *
     */
    List<RouteInfo> selectAll();
}