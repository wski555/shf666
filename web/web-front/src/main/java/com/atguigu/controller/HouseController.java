package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.result.Result;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import service.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hosue")
public class HouseController {
    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseImageService houseImageService;

    @Reference
    private UserFollowService userFollowService;

    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result findPageList(@PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize,
                               @RequestBody HouseQueryVo houseQueryVo){
        PageInfo<HouseVo> pageInfo= houseService.findPageList(pageNum,pageSize,houseQueryVo);
        return Result.ok(pageInfo);
    }
    @GetMapping("info/{houseId}")
    public Result info(@PathVariable Long houseId, HttpSession session) {
        House house = houseService.getById(houseId);
        Community community = communityService.getById(house.getCommunityId());
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(houseId);
        List<HouseImage> houseImage1List = houseImageService.findListByHouseIdAndTypeId(houseId, 1);

        Map<String, Object> map = new HashMap<>();
        map.put("house",house);
        map.put("community",community);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseImage1List",houseImage1List);
        //设置一个Boolean变量
        Boolean isFollow = false;
        UserInfo userInfo = (UserInfo) session.getAttribute("user");
        String nickName = userInfo.getNickName();
        if(null != nickName){
            isFollow = userFollowService.selectCountByUserIdAndHouseId(userInfo.getId(),houseId);
        }
        //TODo 关注业务后续补充，当前默认返回未关注
        map.put("isFollow",isFollow);
        return Result.ok(map);
    }
}
