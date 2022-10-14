package com.atguigu.dao;

import com.atguigu.entity.Dict;

import java.util.List;

public interface DictDao {
    List<Dict> findListByParentId(Long parentId);
    Integer isParentNode(Long parentId);
    String getNameById(Long id);

    Dict getByDictCode(String dictCode);
}
