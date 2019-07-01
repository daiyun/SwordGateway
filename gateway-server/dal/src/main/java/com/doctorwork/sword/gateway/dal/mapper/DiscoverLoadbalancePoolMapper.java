package com.doctorwork.sword.gateway.dal.mapper;

import com.doctorwork.sword.gateway.dal.model.DiscoverLoadbalancePool;
import java.util.List;

public interface DiscoverLoadbalancePoolMapper {
    /**
     * insert one record into table from <tt>discover_loadbalance_pool</tt>
     *
     * @param record
     */
    int insert(DiscoverLoadbalancePool record);

    /**
     *
     * @param record
     */
    int insertSelective(DiscoverLoadbalancePool record);

    /**
     * get one record by primary key from <tt>discover_loadbalance_pool</tt>
     *
     * @param id
     */
    DiscoverLoadbalancePool selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(DiscoverLoadbalancePool record);

    /**
     * update one record by primary key from <tt>discover_loadbalance_pool</tt>
     *
     * @param record
     */
    int updateByPrimaryKey(DiscoverLoadbalancePool record);

    /**
     * get all records from  <tt>discover_loadbalance_pool</tt>
     *
     */
    List<DiscoverLoadbalancePool> selectAll();
}