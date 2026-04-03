package com.jsh.erp.controller.custom;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsh.erp.datasource.entitie.custom.SampleManagement;
import com.jsh.erp.datasource.entitie.custom.SampleManagementLog;
import com.jsh.erp.service.custom.SampleManagementLogService;
import com.jsh.erp.service.custom.SampleManagementService;
import com.jsh.erp.utils.ExcelUtils;
import com.jsh.erp.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 样品出库日志表 前端控制器
 * </p>
 *
 * @author dawei
 * @since 2023-07-11 19:52:54
 */
@RestController
@RequestMapping("/sampleManagementLog")
public class SampleManagementLogController {
    @Autowired
    private SampleManagementLogService sampleManagementLogService;

    @Autowired
    private SampleManagementService sampleManagementService;

    /**
     * 反哺后删除出库记录
     *
     * @return
     */
    @GetMapping(value = "/delete")
    @ApiOperation(value = "反哺后删除出库记录")
    public Result delete(@RequestParam Long id) throws Exception {
        // 删除先（反哺）
        SampleManagementLog oldLog = sampleManagementLogService.getById(id);
        SampleManagement sm = sampleManagementService.getById(oldLog.getSampleId());
        sm.setSize1(new BigDecimal(sm.getSize1()).add(new BigDecimal(oldLog.getSize1())).intValue());
        sm.setSize2(new BigDecimal(sm.getSize2()).add(new BigDecimal(oldLog.getSize2())).intValue());
        sm.setSize3(new BigDecimal(sm.getSize3()).add(new BigDecimal(oldLog.getSize3())).intValue());
        sm.setSize4(new BigDecimal(sm.getSize4()).add(new BigDecimal(oldLog.getSize4())).intValue());
        sm.setSize5(new BigDecimal(sm.getSize5()).add(new BigDecimal(oldLog.getSize5())).intValue());
        sm.setSize6(new BigDecimal(sm.getSize6()).add(new BigDecimal(oldLog.getSize6())).intValue());
        sm.setSize7(new BigDecimal(sm.getSize7()).add(new BigDecimal(oldLog.getSize7())).intValue());
        sm.setSize8(new BigDecimal(sm.getSize8()).add(new BigDecimal(oldLog.getSize8())).intValue());
        sm.setSize9(new BigDecimal(sm.getSize9()).add(new BigDecimal(oldLog.getSize9())).intValue());
        sm.setSize10(new BigDecimal(sm.getSize10()).add(new BigDecimal(oldLog.getSize10())).intValue());
        sm.setSize11(new BigDecimal(sm.getSize11()).add(new BigDecimal(oldLog.getSize11())).intValue());
        sm.setNumbers(new BigDecimal(sm.getNumbers()).add(new BigDecimal(oldLog.getNumbers())).intValue());
        sampleManagementService.updateById(sm);
        sampleManagementLogService.removeById(id);
        return Result.success(true);
    }


    /**
     * 导出excel
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/exportSampleLogList")
    @ApiOperation(value = "导出样品出库记录excel")
    public void exportSampleLogList(@RequestParam(value = "commodityNo", required = false) String commodityNo,
                                    @RequestParam(value = "salesMan", required = false) String salesMan,
                                    @RequestParam(value = "createTime", required = false) String createTime,
                                    @RequestParam(value = "beginTime", required = false) String beginTime,
                                    @RequestParam(value = "endTime", required = false) String endTime,
                                    @RequestParam(value = "price", required = false) String price,
                                    @RequestParam(value = "totalPrice", required = false) String totalPrice,
                                    @RequestParam(value = "sampleId", required = false) String sampleId,
                                    @RequestParam(value = "sampleNumber", required = false) String sampleNumber,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        LambdaQueryWrapper<SampleManagementLog> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(commodityNo), SampleManagementLog::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(salesMan), SampleManagementLog::getMember, salesMan);
        if (StrUtil.isNotBlank(createTime)) {
            query.apply("date_format(create_time,'%Y-%m-%d')='" + createTime + "'");
        }
        query.eq(StrUtil.isNotBlank(price), SampleManagementLog::getPrice, price);
        query.eq(StrUtil.isNotBlank(totalPrice), SampleManagementLog::getTotalPrice, totalPrice);
        query.eq(StrUtil.isNotBlank(sampleId), SampleManagementLog::getSampleId, sampleId);
        query.eq(StrUtil.isNotBlank(sampleNumber), SampleManagementLog::getSampleNumber, sampleNumber);

        query.ge(StrUtil.isNotBlank(beginTime), SampleManagementLog::getCreateTime, beginTime.concat(" 00:00:00"));
        query.le(StrUtil.isNotBlank(endTime), SampleManagementLog::getCreateTime, endTime.concat(" 23:59:59"));
        query.orderByAsc(SampleManagementLog::getId);
        List<SampleManagementLog> samples = sampleManagementLogService.list(query);
        String[] names = {
                "货品信息",
                "样衣编号",
                "样衣单编号",
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
                "单价",
                "金额",
                "已收样衣费",
                "业务员",
                "领样日期",
                "快递费(元)",
                "备注"
        };
        String title = "样衣出库记录";
        List<String[]> objects = new ArrayList<String[]>();
        if (null != samples) {
            for (SampleManagementLog s : samples) {
                String[] objs = new String[22];
                objs[0] = StrUtil.isNotBlank(s.getCommodityName()) ? s.getCommodityName().concat("(").concat(s.getCommodityNo()).concat(")") : "";
                objs[1] = StrUtil.toString(s.getSampleId());
                objs[2] = s.getSampleNumber();
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
                objs[15] = s.getPrice() == null ? "0" : StrUtil.toString(s.getPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[16] = s.getTotalPrice() == null ? "0" : StrUtil.toString(s.getTotalPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[17] = StrUtil.toString(s.getSampleCharge().setScale(4, BigDecimal.ROUND_UP));
                objs[18] = s.getMember();
                objs[19] = DateUtil.format(s.getCreateTime(), "yyyy-MM-dd");
                objs[20] = StrUtil.toString(s.getExpressPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[21] = s.getRemark();
                objects.add(objs);
            }
        }
        File file = ExcelUtils.exportObjectsWithoutTitle(title, "", names, title, objects);
        ExcelUtils.downloadExcel(file, file.getName(), response);
    }
}

