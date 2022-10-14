package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseUserService houseUserService;
    @RequestMapping
    public String findPage(HttpServletRequest request, Model model){
        Map<String, Object> filters = getFilters(request);
        PageInfo<House> pageInfo = houseService.findPage(filters);
        getCommunitiesAndDicts(model);
        model.addAttribute("filters",filters);
        model.addAttribute("page",pageInfo);
        return "house/index";
    }

    @RequestMapping("/create")
    public String goAddPage(Model model){
        getCommunitiesAndDicts(model);
        return "house/create";
    }

    @RequestMapping("/save")
    public String save(House house){
        houseService.insert(house);
        return "common/successPage";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        House house = houseService.getById(id);
        model.addAttribute("house",house);
        getCommunitiesAndDicts(model);
        return "house/edit";
    }

    @RequestMapping("/update")
    public String update(House house){
        houseService.update(house);
        return "common/successPage";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        houseService.delete(id);
        return "redirect:/house";
    }

    @RequestMapping("/publish/{houseId}/{status}")
    public String publish(@PathVariable("houseId") Long houseId,
                          @PathVariable("status") Integer status){
        houseService.publish(houseId,status);
        return "redirect:/house";
    }
    public void getCommunitiesAndDicts(Model model){
        List<Community> communityList = communityService.findAll();
        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        List<Dict> floorList = dictService.findListByDictCode("floor");
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        List<Dict> directionList = dictService.findListByDictCode("direction");
        List<Dict> decorationList = dictService.findListByDictCode("decoration");
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");
        model.addAttribute("houseUseList",houseUseList);
        model.addAttribute("decorationList",decorationList);
        model.addAttribute("directionList",directionList);
        model.addAttribute("buildStructureList",buildStructureList);
        model.addAttribute("floorList",floorList);
        model.addAttribute("houseTypeList",houseTypeList);
        model.addAttribute("communityList",communityList);
    }

    @RequestMapping("/{houseId}")
    public String show(@PathVariable("houseId") Long houseId,Model model){
        House house = houseService.getById(houseId);
        Community community = communityService.getById(house.getCommunityId());
        model.addAttribute("house",house);
        model.addAttribute("community",community);
        List<HouseImage> houseImage1List = houseImageService.findListByHouseIdAndTypeId(houseId, 1);
        List<HouseImage> houseImage2List = houseImageService.findListByHouseIdAndTypeId(houseId, 2);
        model.addAttribute("houseImage1List",houseImage1List);
        model.addAttribute("houseImage2List",houseImage2List);
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(houseId);
        List<HouseUser> houseUserList = houseUserService.findListByHouseId(houseId);
        model.addAttribute("houseBrokerList",houseBrokerList);
        model.addAttribute("houseUserList",houseUserList);
        return "house/show";
    }
}