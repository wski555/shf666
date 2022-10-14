package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.House;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import service.HouseService;

import java.io.Serializable;

@Service(interfaceClass = HouseService.class)
@Transactional
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {
    @Autowired
    private HouseDao houseDao;
    @Autowired
    private DictDao dictDao;
    @Override
    protected BaseDao<House> getEntityDao() {
        return this.houseDao;
    }

    @Override
    public House getById(Serializable id) {
        House house = houseDao.getById(id);
        if(null == house) return null;
        String houseTypeName = dictDao.getNameById(house.getHouseTypeId());
        String floorName = dictDao.getNameById(house.getFloorId());
        String buildStructureName = dictDao.getNameById(house.getBuildStructureId());
        String directionName = dictDao.getNameById(house.getDirectionId());
        String decorationName = dictDao.getNameById(house.getDecorationId());
        String houseUseName = dictDao.getNameById(house.getHouseUseId());
        house.setHouseTypeName(houseTypeName);
        house.setFloorName(floorName);
        house.setBuildStructureName(buildStructureName);
        house.setDirectionName(directionName);
        house.setDecorationName(decorationName);
        house.setHouseUseName(houseUseName);
        return house;
    }

    @Override
    public void publish(Long houseId, Integer status) {
        House house = new House();
        house.setId(houseId);
        house.setStatus(status);
        houseDao.update(house);
    }

    @Override
    public PageInfo<HouseVo> findPageList(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {
        PageHelper.startPage(pageNum,pageSize);
        Page<HouseVo> page = houseDao.findPageList(houseQueryVo);
        for (HouseVo houseVo : page) {
            String houseName = dictDao.getNameById(houseVo.getHouseTypeId());
            String floorName = dictDao.getNameById(houseVo.getFloorId());
            String directionName = dictDao.getNameById(houseVo.getDirectionId());
            houseVo.setHouseTypeName(houseName);
            houseVo.setFloorName(floorName);
            houseVo.setDirectionName(directionName);
        }
        return new PageInfo<>(page,5);
    }
}
