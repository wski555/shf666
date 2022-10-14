package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import service.DictService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = DictService.class)
@Transactional
public class DictServiceImpl implements DictService {
    @Autowired
    private DictDao dictDao;

    @Override
    public List<Dict> findListByParentId(Long id) {
        return dictDao.findListByParentId(id);
    }

    @Override
    public List<Dict> findListByDictCode(String dictCode) {
        Dict dict = dictDao.getByDictCode(dictCode);
        return dictDao.findListByParentId(dict.getId());
    }

    @Override
    public String getNameById(Long id) {
        return dictDao.getNameById(id);
    }

    @Override
    public List<Map<String, Object>> findZnodes(Long id) {
        List<Map<String, Object>> zNodes = new ArrayList<>();
        List<Dict> dictList = dictDao.findListByParentId(id);
        for (Dict dict : dictList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id",dict.getId());
            map.put("name",dict.getName());
            map.put("isParent",dictDao.isParentNode(dict.getId()) > 0? true:false);
            zNodes.add(map);
        }

        return zNodes;
    }
}
