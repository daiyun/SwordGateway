package com.doctorwork.sword.gateway.admin.dal.mapper.ext;

import com.doctorwork.sword.gateway.admin.dal.model.LoadbalanceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtLoadbalanceInfoMapper {
    List<LoadbalanceInfo> list();

    LoadbalanceInfo get(String lbMark);

    int update(@Param("param") LoadbalanceInfo info);

    int delete(String lbMark);

    int updateStatus(@Param("lbMark") String lbMark, @Param("lbStatus") Integer lbStatus);
}