package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.UserFollowDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import service.DictService;
import service.UserFollowService;
import service.UserInfoService;

@Service(interfaceClass = UserInfoService.class)
@Transactional
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {

    @Autowired
    private UserFollowDao userFollowDao;
    @Reference
    private DictService dictService;
    @Override
    protected BaseDao<UserFollow> getEntityDao() {
        return userFollowDao;
    }

    @Override
    public Boolean selectCountByUserIdAndHouseId(Long id, Long houseId) {
        Integer count = userFollowDao.selectCountByUserIdAndHouseId(id, houseId);
//        if(count > 0){
//            return true;
//        }
//        return false;
        return count > 0;
    }

    @Override
    public PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long userId) {
        PageHelper.startPage(pageNum,pageSize);
        Page<UserFollowVo> page = userFollowDao.findListPage(userId);
        for (UserFollowVo userFollowVo : page) {
            String houseTypeName = dictService.getNameById(userFollowVo.getHouseTypeId());
            String floorName = dictService.getNameById(userFollowVo.getFloorId());
            String directionName = dictService.getNameById(userFollowVo.getDirectionId());
            userFollowVo.setHouseTypeName(houseTypeName);
            userFollowVo.setFloorName(floorName);
            userFollowVo.setDirectionName(directionName);
        }
        return new PageInfo<>(page,5);
    }

    @Override
    public void follow(Long id, Long houseId) {
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(id);
        userFollow.setHouseId(houseId);
        userFollowDao.insert(userFollow);
    }
}
