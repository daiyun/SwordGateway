package com.doctorwork.sword.gateway.dal.mapper;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceServer;

import java.util.List;

public interface LoadbalanceServerMapper {
    /**
     * insert one record into table from <tt>loadbalance_server</tt>
     *
     * @param record
     */
    int insert(LoadbalanceServer record);

    /**
     *
     * @param record
     */
    int insertSelective(LoadbalanceServer record);

    /**
     * get one record by primary key from <tt>loadbalance_server</tt>
     *
     * @param id
     */
    LoadbalanceServer selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(LoadbalanceServer record);

    /**
     * update one record by primary key from <tt>loadbalance_server</tt>
     *
     * @param record
     */
    int updateByPrimaryKey(LoadbalanceServer record);

    /**
     * get all records from  <tt>loadbalance_server</tt>
     *
     */
    List<LoadbalanceServer> selectAll();
}