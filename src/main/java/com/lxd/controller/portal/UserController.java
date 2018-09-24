package com.lxd.controller.portal;

import com.lxd.common.Const;
import com.lxd.common.ServiceResponse;
import com.lxd.pojo.User;
import com.lxd.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
/**
 * @author :留仙洞
 * @create 2018-09-16 23:30
 **/
@Controller
@RequestMapping("/user/")
public class UserController {
//    注入userservice
    @Autowired
    private IUserService iUserService;//属性的名字与userservice注解的名字一致
    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> login(String username, String password, HttpSession session) {
//        调用service层，service层调用mybatis的dao层dao层与mapper映射有关

        ServiceResponse<User> response = iUserService.login(username,password);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    @RequestMapping(value="logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServiceResponse.createBySuccess();
    }
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    //这里可传user，当然可以传user里面的其他填写的字段
    public ServiceResponse<String> register(User user){
        return iUserService.register(user);
    }
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> checkvalid(String str,String type){
      return iUserService.checkvalid(str, type);
    }
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> getaUserInfo(HttpSession httpSession){
        User user =(User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user != null){
            return ServiceResponse.createBySuccess(user);
        }
        return ServiceResponse.createByErrorMessage("用户未登录，无法获取用户信息");
    }
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> update_information(HttpSession httpSession,User user){
        User currentUser = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServiceResponse.createByErrorMessage("用户未登录!");
        }
        //由于修改的账号是当前账号的信息，所以获取的userID也是当前ID，这是前提，也是隐形条件。
        //下面是获取当前的userID
        user.setId(currentUser.getId());
        user.setUername(currentUser.getUername());
        ServiceResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){
            response.getData().setUername(currentUser.getUername());
            httpSession.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
}
