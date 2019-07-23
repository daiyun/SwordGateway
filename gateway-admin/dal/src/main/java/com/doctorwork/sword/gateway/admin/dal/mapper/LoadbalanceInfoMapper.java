package com.doctorwork.sword.gateway.admin.dal.mapper;

import com.doctorwork.sword.gateway.admin.dal.model.LoadbalanceInfo;
import java.util.List;

public interface LoadbalanceInfoMapper {
    /**
     * insert one record into table from <tt>loadbalance_info</tt>
     *
     * @param record
     */
    int insert(LoadbalanceInfo record);

    /**
     *
     * @param record
     */
    int insertSelective(LoadbalanceInfo record);

    /**
     * get one record by primary key from <tt>loadbalance_info</tt>
     *
     * @param id
     */
    LoadbalanceInfo selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(LoadbalanceInfo record);

    /**
     * update one record by primary key from <tt>loadbalance_info</tt>
     *
     * @param record
     */
    int updateByPrimaryKey(LoadbalanceInfo record);

    /**
     * get all records from  <tt>loadbalance_info</tt>
     *
     */
    List<LoadbalanceInfo> selectAll();
}