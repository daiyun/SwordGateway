package com.doctorwork.sword.gateway.dal.mapper;

import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;

import java.util.List;

public interface DiscoverConfigMapper {
    /**
     * insert one record into table from <tt>discover_config</tt>
     *
     * @param record
     */
    int insert(DiscoverConfig record);

    /**
     *
     * @param record
     */
    int insertSelective(DiscoverConfig record);

    /**
     * get one record by primary key from <tt>discover_config</tt>
     *
     * @param id
     */
    DiscoverConfig selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(DiscoverConfig record);

    /**
     * update one record by primary key from <tt>discover_config</tt>
     *
     * @param record
     */
    int updateByPrimaryKey(DiscoverConfig record);

    /**
     * get all records from  <tt>discover_config</tt>
     *
     */
    List<DiscoverConfig> selectAll();
}