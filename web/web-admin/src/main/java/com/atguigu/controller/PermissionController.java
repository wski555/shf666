package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.PermissionService;

import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController {
    @Reference
    PermissionService permissionService;
    @GetMapping
    public String index(ModelMap model) {
        List<Permission> list = permissionService.findAllMenu();
        model.addAttribute("list", list);
        return "permission/index";
    }

    /**
     * 进入新增
     * @param model
     * @param permission
     * @return
     */
    @GetMapping("/create")
    public String create(ModelMap model, Permission permission) {
        model.addAttribute("permission",permission);
        return "permission/create";
    }

    /**
     * 保存新增
     * @param permission
     * @return
     */
    @PostMapping("/save")
    public String save(Permission permission) {

        permissionService.insert(permission);

        return "common/successPage";
    }

    /**
     * 编辑
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(ModelMap model,@PathVariable Long id) {
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission",permission);
        return "permission/edit";
    }

    /**
     * 保存更新
     * @param permission
     * @return
     */
    @PostMapping(value="/update")
    public String update(Permission permission) {
        permissionService.update(permission);
        return "common/successPage";
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        permissionService.delete(id);
        return "redirect:/permission";
    }
}
