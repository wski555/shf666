package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.util.QiniuUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import service.AdminService;
import service.RoleService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    @Reference
    private AdminService adminService;
    @Reference
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @RequestMapping
    public String findPage(HttpServletRequest request,Map map){
        Map<String, Object> filters = getFilters(request);
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        map.put("filters",filters);
        map.put("page",pageInfo);
        return "admin/index";
    }

    @RequestMapping("/create")
    public String goAddPage(){
        return "admin/create";
    }

    @RequestMapping("/save")
    public String save(Admin admin){
        admin.setHeadUrl("http://47.93.148.192:8080/group1/M00/03/F0/rBHu8mHqbpSAU0jVAAAgiJmKg0o148.jpg");
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminService.insert(admin);
        return "common/successPage";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Map map){
        Admin admin = adminService.getById(id);
        map.put("admin",admin);
        return "admin/edit";
    }

    @RequestMapping("/update")
    public String update(Admin admin){
        adminService.update(admin);
        return "common/successPage";
    }
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        adminService.delete(id);
        return "redirect:/admin";
    }

    @RequestMapping("/uploadShow/{id}")
    public String goUploadPage(@PathVariable("id") Long id, Model model){
        model.addAttribute("id",id);
        return "admin/upload";
    }

    @RequestMapping("/upload/{id}")
    @ResponseBody
    public String upload(@PathVariable("id") Long id,
                         @RequestParam("file") MultipartFile file) throws IOException {
        String filename= UUID.randomUUID().toString();
        QiniuUtil.upload2Qiniu(file.getBytes(),filename);
        String url ="http://rjfnv4c55.hn-bkt.clouddn.com/"+filename;
        Admin admin = adminService.getById(id);
        admin.setHeadUrl(url);
        adminService.insert(admin);
        return "common/successPage";
    }

    @RequestMapping("/assignShow/{id}")
    public String goAssignShowPage(@PathVariable("id") Long id, Model model){
        model.addAttribute("id",id);
        Map<String, Object> roleMap = roleService.findRolesByAdminId(id);
        model.addAllAttributes(roleMap);
        return "admin/assignShow";
    }
    @RequestMapping("/assignRole")
    public String assignRole(@RequestParam Long adminId, @RequestParam Long[] roleIds){
        roleService.addRoleIdsAndAdminId(adminId,roleIds);
        return "common/successPage";
    }
}
