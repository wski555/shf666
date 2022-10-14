package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.AdminService;
import service.HouseBrokerService;

import java.util.List;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {

    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private AdminService adminService;

    @RequestMapping("/create")
    public String goAddPage(@RequestParam("houseId") Long houseId, Model model){
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList",adminList);
        model.addAttribute("houseId",houseId);
        return "houseBroker/create";
    }

    @RequestMapping("/save")
    public String save(HouseBroker houseBroker){
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.insert(houseBroker);
        return "common/successPage";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        HouseBroker houseBroker = houseBrokerService.getById(id);
        model.addAttribute("houseBroker",houseBroker);
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList",adminList);
        return "houseBroker/edit";
    }
    @RequestMapping("/update")
    public String update(HouseBroker houseBroker){
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.update(houseBroker);
        return "common/successPage";
    }

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("id") Long id){

        houseBrokerService.delete(id);
        return "redirect:/house/"+houseId;
    }

}
