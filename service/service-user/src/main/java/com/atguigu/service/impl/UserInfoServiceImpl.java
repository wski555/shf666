package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.UserInfoDao;
import com.atguigu.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import service.UserInfoService;
@Service(interfaceClass = UserInfoService.class)
@Transactional
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo> implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Override
    protected BaseDao<UserInfo> getEntityDao() {
        return this.userInfoDao;
    }

    @Override
    public UserInfo getUserInfoByPhone(String phone) {
        return userInfoDao.getUserInfoByPhone(phone);
    }
}
