package com.doctorwork.sword.gateway.admin.dal.mapper;

import com.doctorwork.sword.gateway.admin.dal.model.DiscoverConfig;
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
     * getByLbMark one record by primary key from <tt>discover_config</tt>
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
     * getByLbMark all records from  <tt>discover_config</tt>
     *
     */
    List<DiscoverConfig> selectAll();
}