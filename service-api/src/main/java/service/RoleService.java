package service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role> {
    List<Role> findAll();

    Map<String, Object> findRolesByAdminId(Long id);

    void addRoleIdsAndAdminId(Long adminId, Long[] roleIds);
}
