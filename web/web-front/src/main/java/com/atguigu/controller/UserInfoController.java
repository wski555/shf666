package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.util.MD5;
import com.atguigu.vo.LoginVo;
import com.atguigu.vo.RegisterVo;
import com.qiniu.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserInfoService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("userInfo")
public class UserInfoController {
    @Reference
    private UserInfoService userInfoService;


    @RequestMapping("/sendCode/{phoneNumber}")
    public Result sendCode(@PathVariable("phoneNumber") String phoneNum, HttpServletRequest request){
        String code = "8888";
        request.getSession().setAttribute("code",code);
        return Result.ok(code);
    }
    @RequestMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo,HttpServletRequest request){
        String phone = registerVo.getPhone();
        String password = registerVo.getPassword();
        String nickName = registerVo.getNickName();
        String code = registerVo.getCode();
        if(StringUtils.isNullOrEmpty(phone) || StringUtils.isNullOrEmpty(password) || StringUtils.isNullOrEmpty(nickName) || StringUtils.isNullOrEmpty(code)){
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        //判断验证码是否正确
        String sessionCode = (String)request.getSession().getAttribute("code");
        if(!code.equals(sessionCode)){
            return Result.build(null,ResultCodeEnum.CODE_ERROR);
        }
        //判断手机号是否被注册过
        UserInfo userInfoByPhone = userInfoService.getUserInfoByPhone(phone);
        if(userInfoByPhone != null){
            return Result.build(null,ResultCodeEnum.PHONE_REGISTER_ERROR);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setPhone(phone);
        userInfo.setNickName(nickName);
        userInfo.setPassword(MD5.encrypt(password));
        userInfo.setStatus(1);
        userInfoService.insert(userInfo);
        return Result.ok();
    }

    @RequestMapping("/login")
    public Result login(@RequestBody LoginVo loginVo,HttpServletRequest request){
        String phone = loginVo.getPhone();
        String password = loginVo.getPassword();
        if(StringUtils.isNullOrEmpty(phone) || StringUtils.isNullOrEmpty(password)){
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        UserInfo userInfoByPhone = userInfoService.getUserInfoByPhone(phone);
        if (userInfoByPhone == null) {
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        }
        if(!MD5.encrypt(password).equalsIgnoreCase(userInfoByPhone.getPassword())){
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }

        if(userInfoByPhone.getStatus() == 0){
            return Result.build(null,ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }

        //将用户放入session域中(后面关注房源的时候判断用户是否登录)
        request.getSession().setAttribute("userInfo",userInfoByPhone);

        Map<String,Object> map = new HashMap<>();
        map.put("nickName",userInfoByPhone.getNickName());
        map.put("phone",userInfoByPhone.getPhone());
        return Result.ok(map);
    }

    @RequestMapping("/logout")
    public Result logout(HttpServletRequest request){
        request.getSession().removeAttribute("userInfo");
        return Result.ok();
    }
}
