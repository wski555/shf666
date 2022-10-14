package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import service.CommunityService;
import service.DictService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {
    @Reference
     CommunityService communityService;
    @Reference
     DictService dictService;

    @RequestMapping
    public String findPage(HttpServletRequest request, Model model){
        Map<String, Object> filters = getFilters(request);
        PageInfo<Community> pageInfo = communityService.findPage(filters);
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        model.addAttribute("areaList",areaList);
        model.addAttribute("filters",filters);
        model.addAttribute("page",pageInfo);
        return "community/index";
    }

    @RequestMapping("/create")
    public String goAddPage(Model model){
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        model.addAttribute("areaList",areaList);
        return "community/create";
    }

    @RequestMapping("/save")
    public String save(Community community){
        communityService.insert(community);
        return "common/successPage";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Map map){
        Community community = communityService.getById(id);
        map.put("community",community);
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        map.put("areaList",areaList);
        return "community/edit";
    }
    @RequestMapping("/update")
    public String update(Community community){
        communityService.update(community);
        return "common/successPage";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        communityService.delete(id);
        return "redirect:/community";
    }

}
