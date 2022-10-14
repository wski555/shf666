package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.PermissionDao;
import com.atguigu.dao.RolePermissionDao;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import com.atguigu.helper.PermissionHelper;
import com.qiniu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import service.PermissionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Override
    protected BaseDao<Permission> getEntityDao() {
        return permissionDao;
    }

    @Override
    public List<Map<String, Object>> findZNodesByRoleId(Long roleId) {
        List<Permission> permissionList = permissionDao.findAll();
        List<Long> permissionIds = rolePermissionDao.findPermissionIdsByRoleId(roleId);
        List<Map<String, Object>> zNodes = new ArrayList<>();
        for (Permission permission : permissionList) {
                Map map = new HashMap();
                map.put("id",permission.getId());
                map.put("pId",permission.getParentId());
                map.put("name",permission.getName());
            if (permissionIds.contains(permission.getId())) {
                map.put("checked",true);
            }
            zNodes.add(map);
        }
        return zNodes;
    }

    @Override
    public void saveRolePermissionRelationShip(Long roleId, Long[] permissionIds) {
        rolePermissionDao.deleteByRoleId(roleId);
        List<RolePermission> rolePermissionList = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            if (StringUtils.isNullOrEmpty(String.valueOf(permissionId))) {
                continue;
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionList.add(rolePermission);
        }
        rolePermissionDao.insertBatch(rolePermissionList);
    }

    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        List<Permission> permissionList = null;
        //admin账号id为：1
        if(adminId.longValue() == 1) {
            //如果是超级管理员，获取所有菜单
            permissionList = permissionDao.findAll();
        } else {
            permissionList = permissionDao.findListByAdminId(adminId);
        }
        //把权限数据构建成树形结构数据
        List<Permission> result = PermissionHelper.bulid(permissionList);
        return result;
    }

    @Override
    public List<Permission> findAllMenu() {
        List<Permission> permissionList = permissionDao.findAll();
        if(CollectionUtils.isEmpty(permissionList)) return null;

        //构建树形数据,总共三级
        //把权限数据构建成树形结构数据
        List<Permission> result = PermissionHelper.bulid(permissionList);
        return result;
    }

    @Override
    public List<String> getCodeListByAdminId(Long id) {
        if(id == 1){
           return permissionDao.findAllCodeList();
        }
        return permissionDao.findCodeListByAdminId(id);
    }
}
