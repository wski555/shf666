package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import service.DictService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController {
    @Reference
     DictService dictService;

    /**
     * 根据上级id获取子节点数据列表
     * @param id
     * @return
     */
    @RequestMapping(value = "/findZnodes")
    @ResponseBody
    public Result findByParentId(@RequestParam(value = "id", defaultValue = "0") Long id) {
        List<Map<String,Object>> zNodes = dictService.findZnodes(id);
        return Result.ok(zNodes);
    }


    @RequestMapping(value = "findListByDictCode/{dictCode}")
    @ResponseBody
    public Result<List<Dict>> findListByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findListByDictCode(dictCode);
        return Result.ok(list);
    }
    @RequestMapping(value = "findListByParentId/{parentId}")
    @ResponseBody
    public Result<List<Dict>> findListByParentId(@PathVariable Long parentId) {
        List<Dict> list = dictService.findListByParentId(parentId);
        return Result.ok(list);
    }
    @RequestMapping
    public String index(ModelMap model) {
        return "dict/index";
    }
}
