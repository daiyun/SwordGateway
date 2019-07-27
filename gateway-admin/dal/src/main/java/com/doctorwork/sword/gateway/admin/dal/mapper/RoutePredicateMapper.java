package com.doctorwork.sword.gateway.admin.dal.mapper;

import com.doctorwork.sword.gateway.admin.dal.model.RoutePredicate;
import java.util.List;

public interface RoutePredicateMapper {
    /**
     * insert one record into table from <tt>route_predicate</tt>
     *
     * @param record
     */
    int insert(RoutePredicate record);

    /**
     *
     * @param record
     */
    int insertSelective(RoutePredicate record);

    /**
     * getByLbMark one record by primary key from <tt>route_predicate</tt>
     *
     * @param id
     */
    RoutePredicate selectByPrimaryKey(Long id);

    /**
     *
     * @param record
     */
    int updateByPrimaryKeySelective(RoutePredicate record);

    /**
     * update one record by primary key from <tt>route_predicate</tt>
     *
     * @param record
     */
    int updateByPrimaryKey(RoutePredicate record);

    /**
     * getByLbMark all records from  <tt>route_predicate</tt>
     *
     */
    List<RoutePredicate> selectAll();
}