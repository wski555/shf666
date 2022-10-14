package service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseImage;

import java.util.List;

public interface HouseImageService extends BaseService<HouseImage> {
    List<HouseImage> findListByHouseIdAndTypeId(Long houseId, Integer typeId);
}
