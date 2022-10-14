package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.qiniu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import service.RoleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private AdminRoleDao adminRoleDao;

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }


    @Override
    public Map<String, Object> findRolesByAdminId(Long id) {
        List<Role> roleList = roleDao.findAll();
        List<Long> roleIds = adminRoleDao.findRolesByAdminId(id);
        List<Role> noAssignRoleList = new ArrayList<>();
        List<Role> assignRoleList = new ArrayList<>();
        for (Role role : roleList) {
            if (roleIds.contains(role.getId())) {
                assignRoleList.add(role);
            } else {
                noAssignRoleList.add(role);
            }
        }
        Map roleMap = new HashMap();
        roleMap.put("noAssignRoleList", noAssignRoleList);
        roleMap.put("noAssignRoleList", noAssignRoleList);
        return roleMap;
    }

    @Override
    public void addRoleIdsAndAdminId(Long adminId, Long[] roleIds) {
        adminRoleDao.deleteByAdminId(adminId);
        List<AdminRole> adminRoleList = new ArrayList<>();
        for (Long roleId : roleIds) {
            if (StringUtils.isNullOrEmpty(String.valueOf(roleId))) {
                continue;
            }
            AdminRole adminRole = new AdminRole();
            adminRole.setRoleId(roleId);
            adminRole.setAdminId(adminId);
            adminRoleList.add(adminRole);
        }
        adminRoleDao.insertBatch(adminRoleList);
    }

        @Override
        protected BaseDao<Role> getEntityDao () {
            return this.roleDao;
        }
}
