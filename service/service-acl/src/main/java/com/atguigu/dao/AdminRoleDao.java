package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.AdminRole;

import java.util.List;

public interface AdminRoleDao extends BaseDao<AdminRole> {
    List<Long> findRolesByAdminId(Long id);

    void deleteByAdminId(Long adminId);

    void insertBatch(List<AdminRole> adminRoleList);
}
