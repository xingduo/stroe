package com.lxd.service.Impl;

import com.lxd.common.Const;
import com.lxd.common.ServiceResponse;
import com.lxd.dao.UserMapper;
import com.lxd.pojo.User;
import com.lxd.service.IUserService;
import com.lxd.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：留仙洞
 * @create 2018-09-16 23:34
 **/
@Service("iUserService")
//注入到controller里面
public class UserServiceImpl implements IUserService {
    @Autowired
//    将usermapper注入进来
    private UserMapper userMapper;

    @Override
    public ServiceResponse<User> login(String username, String password) {
        //调用数据库的username数据，拿过来
        int resultCount = userMapper.checkUsername(username);
//        如果验证都没有数据
        if (resultCount == 0) {
            return ServiceResponse.createByErrorMessage("用户名不存在！");
        }
        //密码登录MD5,用加密的md5密码进行比较
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServiceResponse.createByErrorMessage("密码错误！");
        }
        user.setPasswrod(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess("登录成功！", user);
    }

    /**
     * 先校验用户名是否存在，验证email是否存在
     *
     * @param user
     * @return
     */
    public ServiceResponse<String> register(User user) {
        int resultCount = userMapper.checkUsername(user.getUername());
        if (resultCount > 0) {
            return ServiceResponse.createByErrorMessage("用户名已经存在！");
        }
        resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount > 0) {
            return ServiceResponse.createByErrorMessage("email已经存在！");
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密,将密码加密
        user.setPasswrod(MD5Util.MD5EncodeUtf8(user.getPasswrod()));
        resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServiceResponse.createByErrorMessage("注册失败！");
        }
        return ServiceResponse.createBySuccessMessage("注册成功！");
    }

    public ServiceResponse<String> checkvalid(String str, String type) {
        /**
         判断type是否为空，要使用该功能，前提是type不能为空，
         当type不为空的前提下：需要判断username与email都分别是否与type相等
         如果相等了，证明用户存在。
         **/
        if (StringUtils.isNotBlank(type)) {
            //校验用户名是否相等
            if (Const.USERNAME.equals(type)) {
                //search from the db's table data
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    //resultCount >0,the data haven included in user table,so the user haven exist.
                    return ServiceResponse.createByErrorMessage("用户已经存在!");
                }
            }
            if (Const.EMAIL.equals(type)){
                //varify that email is equal.
                int resultConut = userMapper.checkEmail(str);
                if (resultConut > 0){
                    //resultCount > 0 ,the email haven exist in the user table.
                    return ServiceResponse.createByErrorMessage("email已经存在!");
                }
            }
        }
        return ServiceResponse.createBySuccessMessage("校验成功！");
    }
    /**
    更新用户信息的业务逻辑：用户的email是不是已经存在，并且如果email相同的话，不能是当前用户的。

     **/
    public ServiceResponse<User> updateInformation(User user){
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount > 0){
            return ServiceResponse.createByErrorMessage("用户email已经存在");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0){
            return ServiceResponse.createBySuccess("个人信息更新成功",updateUser);
        }
        return ServiceResponse.createByErrorMessage("更新个人信息失败");
    }

}
