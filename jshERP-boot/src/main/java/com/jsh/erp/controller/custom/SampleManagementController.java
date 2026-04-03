package com.jsh.erp.controller.custom;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsh.erp.datasource.entitie.custom.SampleManagement;
import com.jsh.erp.service.custom.SampleManagementService;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.ExcelUtils;
import com.jsh.erp.utils.Result;
import com.jsh.erp.utils.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 样品记录表 前端控制器
 * </p>
 *
 * @author dawei
 * @since 2023-07-11 19:52:53
 */
@RestController
@RequestMapping("/sampleManagement")
public class SampleManagementController {

    @Autowired
    private SampleManagementService sampleManagementService;

    /**
     * 获取商品列表
     *
     * @return
     */
    @GetMapping(value = "/getSampleManagementList")
    @ApiOperation(value = "获取样品列表")
    public Result getSampleManagementList() {
        SampleManagement query = new SampleManagement();
        List<SampleManagement> commoditys = sampleManagementService.getSampleInOutFinalListNoPage(query);
        if (CollUtil.isNotEmpty(commoditys)) {
            return Result.success(commoditys);
        } else {
            return Result.failed("获取样品列表暂无数据");
        }
    }

    /**
     * 获取样衣入库库存
     *
     * @return
     */
    @GetMapping(value = "/getSampleInOutFinalList")
    @ApiOperation(value = "获取样衣入库库存")
    public Result getSampleInOutFinalList(@RequestParam(value = Constants.SEARCH, required = false) String search,
                                          @RequestParam("currentPage") Integer currentPage,
                                          @RequestParam("pageSize") Integer pageSize) {
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String color = StringUtil.getInfo(search, "color");
        String price = StringUtil.getInfo(search, "price");
        SampleManagement wm = new SampleManagement();
        wm.setCurrentPage(currentPage);
        wm.setPageSize(pageSize);
        wm.setCommodityNo(commodityNo);
        if (StrUtil.isNotBlank(price)) {
            wm.setPrice(Double.parseDouble(price));
        }
        wm.setColor(color);
        IPage<SampleManagement> commoditys = sampleManagementService.getSampleInOutFinalList(wm);
        return Result.success(commoditys);
    }

    /**
     * 导出样衣入库库存
     *
     * @return
     */
    @GetMapping(value = "/exportSampleInOutList")
    @ApiOperation(value = "导出样衣入库库存")
    public void exportSampleInOutList(@RequestParam(value = "commodityNo", required = false) String commodityNo,
                                      @RequestParam(value = "color", required = false) String color,
                                      @RequestParam(value = "price", required = false) String price,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        SampleManagement wm = new SampleManagement();
        wm.setCommodityNo(commodityNo);
        if (StrUtil.isNotBlank(price)) {
            wm.setPrice(Double.parseDouble(price));
        }
        wm.setColor(color);
        List<SampleManagement> samples = sampleManagementService.getSampleInOutFinalListNoPage(wm);
        String[] names = {
                "样衣编号",
                "货品信息",
                "颜色",
                "S",
                "M",
                "L",
                "XL",
                "2XL",
                "3XL",
                "4XL",
                "5XL",
                "6XL",
                "7XL",
                "XS",
                "数量",
                "到货时间",
                "样衣单价(元)",
                "备注"
        };
        String title = "样衣库存";
        List<String[]> objects = new ArrayList<String[]>();
        if (null != samples) {
            for (SampleManagement s : samples) {
                String[] objs = new String[18];
                objs[0] = StrUtil.toString(s.getId());
                objs[1] = StrUtil.isNotBlank(s.getCommodityName()) ? s.getCommodityName().concat("(").concat(s.getCommodityNo()).concat(")") : "";
                objs[2] = s.getColor();
                objs[3] = s.getSize1() == null || s.getSize1() == 0 ? "" : StrUtil.toString(s.getSize1());
                objs[4] = s.getSize2() == null || s.getSize2() == 0 ? "" : StrUtil.toString(s.getSize2());
                objs[5] = s.getSize3() == null || s.getSize3() == 0 ? "" : StrUtil.toString(s.getSize3());
                objs[6] = s.getSize4() == null || s.getSize4() == 0 ? "" : StrUtil.toString(s.getSize4());
                objs[7] = s.getSize5() == null || s.getSize5() == 0 ? "" : StrUtil.toString(s.getSize5());
                objs[8] = s.getSize6() == null || s.getSize6() == 0 ? "" : StrUtil.toString(s.getSize6());
                objs[9] = s.getSize7() == null || s.getSize7() == 0 ? "" : StrUtil.toString(s.getSize7());
                objs[10] = s.getSize8() == null || s.getSize8() == 0 ? "" : StrUtil.toString(s.getSize8());
                objs[11] = s.getSize9() == null || s.getSize9() == 0 ? "" : StrUtil.toString(s.getSize9());
                objs[12] = s.getSize10() == null || s.getSize10() == 0 ? "" : StrUtil.toString(s.getSize10());
                objs[13] = s.getSize11() == null || s.getSize11() == 0 ? "" : StrUtil.toString(s.getSize11());
                objs[14] = StrUtil.toString(s.getNumbers());
                objs[15] = DateUtil.format(s.getInTime(), "yyyy-MM-dd");
                objs[16] = s.getPrice() == null ? "0" : StrUtil.toString(s.getPrice());
                objs[17] = s.getRemark();
                objects.add(objs);
            }
        }
        File file = ExcelUtils.exportObjectsWithoutTitle(title, "", names, title, objects);
        ExcelUtils.downloadExcel(file, file.getName(), response);
    }

    /**
     * 导出excel
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/exportSampleList")
    @ApiOperation(value = "导出样品入库记录excel")
    public void exportSampleList(@RequestParam(value = "commodityNo", required = false) String commodityNo,
                                 @RequestParam(value = "color", required = false) String color,
                                 @RequestParam(value = "price", required = false) String price,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        LambdaQueryWrapper<SampleManagement> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(commodityNo), SampleManagement::getCommodityNo, commodityNo);
        query.like(StrUtil.isNotBlank(color), SampleManagement::getColor, color);
        query.eq(StrUtil.isNotBlank(price), SampleManagement::getPrice, price);
        query.orderByAsc(SampleManagement::getId);
        List<SampleManagement> samples = sampleManagementService.list(query);
        String[] names = {
                "样衣编号",
                "货品信息",
                "颜色",
                "S",
                "M",
                "L",
                "XL",
                "2XL",
                "3XL",
                "4XL",
                "5XL",
                "6XL",
                "7XL",
                "XS",
                "数量",
                "到货时间",
                "样衣单价(元)",
                "备注"
        };
        String title = "样衣入库记录";
        List<String[]> objects = new ArrayList<String[]>();
        if (null != samples) {
            for (SampleManagement s : samples) {
                String[] objs = new String[18];
                objs[0] = StrUtil.toString(s.getId());
                objs[1] = StrUtil.isNotBlank(s.getCommodityName()) ? s.getCommodityName().concat("(").concat(s.getCommodityNo()).concat(")") : "";
                objs[2] = s.getColor();
                objs[3] = s.getSize1() == null || s.getSize1() == 0 ? "" : StrUtil.toString(s.getSize1());
                objs[4] = s.getSize2() == null || s.getSize2() == 0 ? "" : StrUtil.toString(s.getSize2());
                objs[5] = s.getSize3() == null || s.getSize3() == 0 ? "" : StrUtil.toString(s.getSize3());
                objs[6] = s.getSize4() == null || s.getSize4() == 0 ? "" : StrUtil.toString(s.getSize4());
                objs[7] = s.getSize5() == null || s.getSize5() == 0 ? "" : StrUtil.toString(s.getSize5());
                objs[8] = s.getSize6() == null || s.getSize6() == 0 ? "" : StrUtil.toString(s.getSize6());
                objs[9] = s.getSize7() == null || s.getSize7() == 0 ? "" : StrUtil.toString(s.getSize7());
                objs[10] = s.getSize8() == null || s.getSize8() == 0 ? "" : StrUtil.toString(s.getSize8());
                objs[11] = s.getSize9() == null || s.getSize9() == 0 ? "" : StrUtil.toString(s.getSize9());
                objs[12] = s.getSize10() == null || s.getSize10() == 0 ? "" : StrUtil.toString(s.getSize10());
                objs[13] = s.getSize11() == null || s.getSize11() == 0 ? "" : StrUtil.toString(s.getSize11());
                objs[14] = StrUtil.toString(s.getNumbers());
                objs[15] = DateUtil.format(s.getInTime(), "yyyy-MM-dd");
                objs[16] = s.getPrice() == null ? "0" : StrUtil.toString(s.getPrice());
                objs[17] = s.getRemark();
                objects.add(objs);
            }
        }
        File file = ExcelUtils.exportObjectsWithoutTitle(title, "", names, title, objects);
        ExcelUtils.downloadExcel(file, file.getName(), response);
    }

}

