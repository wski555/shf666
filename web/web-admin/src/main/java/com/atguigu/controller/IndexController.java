package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.AdminService;
import service.PermissionService;

import java.util.List;

@Controller
public class IndexController {
    @Reference
    AdminService adminService;
    @Reference
    PermissionService permissionService;
    @GetMapping("/")
    public String index(ModelMap model) {
        //后续替换为当前登录用户id
//        Long adminId = 1L;
//        Admin admin = adminService.getById(adminId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Admin admin = adminService.getByUsername(user.getUsername());
        List<Permission> permissionList = permissionService.findMenuPermissionByAdminId(admin.getId());
        model.addAttribute("admin", admin);
        model.addAttribute("permissionList",permissionList);
        return "frame/index";
    }

    @RequestMapping("/main")
    public String main(){
        return "frame/main";
    }
    @RequestMapping("/login")
    public String login(){
        return "frame/login";
    }
    @GetMapping("/auth")
    public String auth() {
        return "frame/auth";
    }

}
