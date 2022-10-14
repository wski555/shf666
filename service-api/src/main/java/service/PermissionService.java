package service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends BaseService<Permission> {
    List<Map<String, Object>> findZNodesByRoleId(Long roleId);

    void saveRolePermissionRelationShip(Long roleId, Long[] permissionIds);

    List<Permission> findMenuPermissionByAdminId(Long adminId);
    List<Permission> findAllMenu();

    List<String> getCodeListByAdminId(Long id);
}
