package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.PermissionService;
import service.RoleService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {


    @Reference
    RoleService roleService;
    @Reference
    private PermissionService permissionService;

    @PreAuthorize("hasAuthority('role.show')")
    @RequestMapping
    public String findPage(HttpServletRequest request, Model model){
        Map<String, Object> filters = getFilters(request);
        PageInfo<Role> pageInfo = roleService.findPage(filters);
        model.addAttribute("filters",filters);
        model.addAttribute("page",pageInfo);
        return "role/index";
    }
    //去添加角色页面
    @PreAuthorize("hasAuthority('role.create')")
    @RequestMapping("/create")
    public String goAddPage(){
        return "role/create";
    }
    @PreAuthorize("hasAuthority('role.create')")
    @RequestMapping("/save")
    public String save(Role role){
        roleService.insert(role);
        return "common/successPage";
    }
    @PreAuthorize("hasAuthority('role.edit')")
    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id") Integer id,Map map){
       Role role = roleService.getById(id);
       map.put("role",role);
       return "role/edit";
    }
    @PreAuthorize("hasAuthority('role.edit')")
    @RequestMapping("/update")
    public String update(Role role){
        roleService.update(role);
        return "common/successPage";
    }
    @PreAuthorize("hasAuthority('role.delete')")
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        roleService.delete(id);
        return "redirect/role";
    }
    @PreAuthorize("hasAuthority('role.assgin')")
    @GetMapping("/assignShow/{roleId}")
    public String assignShow(ModelMap model, @PathVariable Long roleId) {
        List<Map<String,Object>> zNodes = permissionService.findZNodesByRoleId(roleId);
        model.addAttribute("zNodes", JSON.toJSONString(zNodes));
        model.addAttribute("roleId", roleId);
        return "role/assignShow";
    }
    @PreAuthorize("hasAuthority('role.assgin')")
    @PostMapping("/assignPermission")
    public String assignPermission(Long roleId,Long[] permissionIds) {
        permissionService.saveRolePermissionRelationShip(roleId, permissionIds);
        return "common/successPage";
    }

}
