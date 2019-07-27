package com.doctorwork.sword.gateway.admin.dal.mapper;

import com.doctorwork.sword.gateway.admin.dal.model.RouteFilter;
import java.util.List;

public interface RouteFilterMapper {
    /**
     * insert one record into table from <tt>route_filter</tt>
     *
     * @param record
     */
    int insert(RouteFilter record);

    /**
     *
     * @param record
     */
    int insertSelective(RouteFilter record);

    /**
     * getByLbMark one record by primary key from <tt>route_filter</tt>
     *
     * @param id
     */
    RouteFilter selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(RouteFilter record);

    /**
     * update one record by primary key from <tt>route_filter</tt>
     *
     * @param record
     */
    int updateByPrimaryKey(RouteFilter record);

    /**
     * getByLbMark all records from  <tt>route_filter</tt>
     *
     */
    List<RouteFilter> selectAll();
}