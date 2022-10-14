package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.RolePermission;

import java.util.List;

public interface RolePermissionDao extends BaseDao<RolePermission> {
    List<Long> findPermissionIdsByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

    void insertBatch(List<RolePermission> rolePermissionList);
}
