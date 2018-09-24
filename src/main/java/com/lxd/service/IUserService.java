package com.lxd.service;

import com.lxd.common.ServiceResponse;
import com.lxd.pojo.User;

/**
 * @author :留仙洞
 * @create 2018-09-16 23:30
 **/
public interface IUserService {
    ServiceResponse<User> login(String username, String password);
    ServiceResponse<String>register(User user);
    ServiceResponse<String>checkvalid(String str ,String type);

    ServiceResponse<User> updateInformation(User user);
}
