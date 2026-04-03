package com.jsh.erp.controller.custom;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.jsh.erp.datasource.entities.Commodity;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.datasource.entities.vo.CommodityVO;
import com.jsh.erp.service.custom.CommodityService;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.utils.ExcelUtils;
import com.jsh.erp.utils.Result;
import com.jsh.erp.utils.ResultForPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author dawei
 * @since 2023-07-08 19:35:18
 */
@RestController
@RequestMapping(value = "/commodity")
@Slf4j
@Api(tags = {"商品表"})
public class CommodityController {

    @Autowired
    private CommodityService commodityService;
    @Autowired
    private UserService userService;


    /**
     * 获取商品列表
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/getCommodityPage")
    @ApiOperation(value = "获取商品列表")
    public ResultForPage getCommodityPage(CommodityVO vo) {
        Page<Commodity> page = new Page<>(vo.getPageNo(), vo.getPageSize());
        LambdaQueryWrapper<Commodity> query = new LambdaQueryWrapper<>();
        Page<Commodity> commoditys = commodityService.page(page, query);
        if (CollUtil.isNotEmpty(commoditys.getRecords())) {
            return ResultForPage.buildSuccess(commoditys);
        } else {
            return ResultForPage.buildFailed("获取商品列表暂无数据");
        }
    }

    /**
     * 获取商品列表
     *
     * @return
     */
    @GetMapping(value = "/getCommodityList")
    @ApiOperation(value = "获取商品列表")
    public Result getCommodityList() {
        LambdaQueryWrapper<Commodity> query = new LambdaQueryWrapper<>();
        List<Commodity> commoditys = commodityService.list(query);
        if (CollUtil.isNotEmpty(commoditys)) {
            return Result.success(commoditys);
        } else {
            return Result.failed("获取商品列表暂无数据");
        }
    }

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/deleteCommodity")
    @ApiOperation(value = "删除商品")
    public Result deleteCommodity(@RequestParam("id") String id) throws Exception {
        User userInfo = userService.getCurrentUser();
        Commodity co = commodityService.getById(id);
        co.setUpdateBy(userInfo.getUsername());
        co.setUpdateTime(DateUtil.date());
        commodityService.removeById(co);
        return Result.success(true);
    }

    /**
     * 增加商品
     *
     * @param commodity
     * @return
     */
    @PostMapping(value = "/updCommodity")
    @ApiOperation(value = "删除商品")
    public Result deleteCommodity(Commodity commodity) throws Exception {
        User userInfo = userService.getCurrentUser();
        if (commodity.getId() == null) {
            commodity.setUpdateBy(userInfo.getUsername());
            commodity.setUpdateTime(DateUtil.date());
            commodity.setCreateTime(DateUtil.date());
            commodity.setCreateBy(userInfo.getUsername());
        } else {
            commodity.setUpdateBy(userInfo.getUsername());
            commodity.setUpdateTime(DateUtil.date());
        }
        commodityService.saveOrUpdate(commodity);
        return Result.success(true);
    }

    /**
     * 压缩图片
     *
     * @param oldPath
     * @param newPath
     * @param smallFactor
     * @return
     */
    @GetMapping(value = "/generateThumbnail")
    @ApiOperation(value = "压缩图片")
    public boolean generateThumbnail(@RequestParam String oldPath, @RequestParam String newPath, @RequestParam double smallFactor) {
        try (FileInputStream fis = new FileInputStream(oldPath)) {
            BufferedImage originalImage = ImageIO.read(fis);
            if (originalImage == null) {
                System.err.println("Failed to read the original image: " + oldPath);
                return false;
            }
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            int thumbnailWidth = (int) (width * smallFactor);
            int thumbnailHeight = (int) (height * smallFactor);
            BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = thumbnail.createGraphics();

            // 设置高质量的插值方法
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(originalImage, 0, 0, thumbnailWidth, thumbnailHeight, null);
            g2d.dispose();
            ImageIO.write(thumbnail, "jpg", new File(newPath));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 导出货品信息excel
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/exportCommodityList")
    @ApiOperation(value = "导出货品信息")
    public void exportCommodityList(@RequestParam(value = "commodityNo", required = false) String commodityNo,
                                    @RequestParam(value = "name", required = false) String name,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        LambdaQueryWrapper<Commodity> query = new LambdaQueryWrapper<>();
        query.eq(StrUtil.isNotBlank(commodityNo), Commodity::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(name), Commodity::getName, name);
        query.eq(Commodity::getDeleteFlag, 0);
        List<Commodity> depots = commodityService.list(query);
        //会员
        String[] names = {
                "序号",
                "名称",
                "货品编号",
                "备注",
                "单价(元)",
                "录入人员",
                "录入时间",
                "更新人员",
                "更新时间",
        };
        String title = "货品信息";
        List<String[]> objects = Lists.newArrayList();
        if (null != depots) {
            int index = 1;
            for (Commodity s : depots) {
                String[] objs = new String[9];
                objs[0] = StrUtil.toString(index++);
                objs[1] = s.getName();
                objs[2] = s.getCommodityNo();
                objs[3] = s.getRemark();
                objs[4] = StrUtil.toString(s.getPrice());
                objs[5] = s.getCreateBy();
                objs[6] = DateUtil.format(s.getCreateTime(), "yyyy-MM-dd");
                objs[7] = s.getUpdateBy();
                objs[8] = DateUtil.format(s.getUpdateTime(), "yyyy-MM-dd");
                objects.add(objs);
            }
        }
        File file = ExcelUtils.exportObjectsWithoutTitle(title, "", names, title, objects);
        ExcelUtils.downloadExcel(file, file.getName(), response);
    }
}

