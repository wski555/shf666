package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.HouseUserService;

@Controller
@RequestMapping("/houseUser")
public class HouseUserController extends BaseController {
    @Reference
    private HouseUserService houseUserService;

    @RequestMapping("/create")
    public String goAddPage(@RequestParam("houseId") Long houseId, Model model){
        model.addAttribute("houseId",houseId);
        return "houseUser/create";
    }

    @RequestMapping("/save")
    public String save(HouseUser houseUser){
        houseUserService.insert(houseUser);
        return "common/successPage";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        HouseUser houseUser = houseUserService.getById(id);
        model.addAttribute("houseUser",houseUser);
        return "houseUser/edit";
    }
    @RequestMapping("/update")
    public String update(HouseUser houseUser){
        houseUserService.update(houseUser);
        return "common/successPage";
    }

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("id") Long id){
        houseUserService.delete(id);
        return "redirect:/house/"+houseId;
    }

}
