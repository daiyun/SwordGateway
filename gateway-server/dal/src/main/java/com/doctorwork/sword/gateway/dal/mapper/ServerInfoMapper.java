package com.doctorwork.sword.gateway.dal.mapper;

import com.doctorwork.sword.gateway.dal.model.ServerInfo;

import java.util.List;

public interface ServerInfoMapper {
    /**
     * insert one record into table from <tt>server_info</tt>
     *
     * @param record
     */
    int insert(ServerInfo record);

    /**
     *
     * @param record
     */
    int insertSelective(ServerInfo record);

    /**
     * get one record by primary key from <tt>server_info</tt>
     *
     * @param id
     */
    ServerInfo selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(ServerInfo record);

    /**
     * update one record by primary key from <tt>server_info</tt>
     *
     * @param record
     */
    int updateByPrimaryKey(ServerInfo record);

    /**
     * get all records from  <tt>server_info</tt>
     *
     */
    List<ServerInfo> selectAll();
}