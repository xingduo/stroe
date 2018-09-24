package com.lxd.dao;

import com.lxd.pojo.PayInfo;

public interface PayInfoMapper {

    int deleteByPrimaryKey(Integer id);


    int insertSelective(PayInfo record);

    PayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayInfo record);

    int updateByPrimaryKey(PayInfo record);
}