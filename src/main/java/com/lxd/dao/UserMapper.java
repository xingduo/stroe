package com.lxd.dao;

import com.lxd.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);
    int checkEmail(String email);
    //如果后面的方法中有使用的参数与前面的有重复的，需要使用mybatis的@Param注解
    User selectLogin(@Param("username")String username,@Param("password")String password);
    int checkEmailByUserId(@Param(value="email")String email,@Param(value="userId")Integer userId);


}