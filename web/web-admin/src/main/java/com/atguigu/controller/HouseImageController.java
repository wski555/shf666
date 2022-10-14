package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.util.QiniuUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.HouseImageService;
import service.HouseService;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController{
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseService houseService;

    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String goUploadPage(@PathVariable("houseId") Long houseId,
                               @PathVariable("type") Integer type,
                               Model model){
        model.addAttribute("houseId",houseId);
        model.addAttribute("type",type);
        return "house/upload";
    }


    @RequestMapping("/upload/{houseId}/{type}")
    @ResponseBody
    public Result upload(
            @PathVariable("houseId") Long  houseId,
            @PathVariable("type") Integer  type,
            @RequestParam("file") MultipartFile[] multipartFiles,
            Model model
    ) throws IOException {
        for (int i = 0; i < multipartFiles.length; i++) {

            MultipartFile multipartFile =multipartFiles[i];
            String filename= UUID.randomUUID().toString();
            QiniuUtil.upload2Qiniu(multipartFile.getBytes(),filename);
            String url ="http://rjfnv4c55.hn-bkt.clouddn.com/"+filename;

            HouseImage houseImage  =new HouseImage();
            houseImage.setHouseId(houseId);
            houseImage.setImageName(filename);
            houseImage.setType(type);
            houseImage.setImageUrl(url);
            houseImageService.insert(houseImage);
            // 默认本次上传的第一个房源屠天设置为house表格中的默认图片
            if (i==0  && type==1){
                // 更新房源默认图片
                House house =new House();
                house.setId(houseId);
                house.setDefaultImageUrl(url);
                houseService.update(house);
            }


        }
        return Result.ok();
    }

    /**
     * 删除
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(ModelMap model, @PathVariable Long houseId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        HouseImage houseImage = houseImageService.getById(id);
        houseImageService.delete(id);
        QiniuUtil.deleteFileFromQiniu(houseImage.getImageName());
        return "redirect:/house/" + houseId;
    }
}


