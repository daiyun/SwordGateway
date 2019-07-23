package com.doctorwork.sword.gateway.admin.dal.mapper;

import com.doctorwork.sword.gateway.admin.dal.model.DiscoverRegistryConfig;

import java.util.List;

public interface DiscoverRegistryConfigMapper {
    /**
     * insert one record into table from <tt>discover_registry_config</tt>
     *
     * @param record
     */
    int insert(DiscoverRegistryConfig record);

    /**
     *
     * @param record
     */
    int insertSelective(DiscoverRegistryConfig record);

    /**
     * get one record by primary key from <tt>discover_registry_config</tt>
     *
     * @param id
     */
    DiscoverRegistryConfig selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(DiscoverRegistryConfig record);

    /**
     * update one record by primary key from <tt>discover_registry_config</tt>
     *
     * @param record
     */
    int updateByPrimaryKey(DiscoverRegistryConfig record);

    /**
     * get all records from  <tt>discover_registry_config</tt>
     *
     */
    List<DiscoverRegistryConfig> selectAll();
}