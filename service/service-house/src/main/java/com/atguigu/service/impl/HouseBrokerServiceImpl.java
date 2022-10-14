package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.HouseBrokerDao;
import com.atguigu.entity.HouseBroker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import service.HouseBrokerService;

import java.util.List;

@Service(interfaceClass = HouseBrokerService.class)
@Transactional
public class HouseBrokerServiceImpl extends BaseServiceImpl<HouseBroker> implements HouseBrokerService {
    @Autowired
    private HouseBrokerDao houseBrokerDao;
    @Override
    protected BaseDao<HouseBroker> getEntityDao() {
        return this.houseBrokerDao;
    }

    @Override
    public List<HouseBroker> findListByHouseId(Long houseId) {
        return houseBrokerDao.findListByHouseId(houseId);
    }
}
