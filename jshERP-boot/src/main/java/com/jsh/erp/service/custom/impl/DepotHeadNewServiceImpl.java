package com.jsh.erp.service.custom.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.jsh.erp.datasource.entitie.custom.WarehouseManagement;
import com.jsh.erp.datasource.entities.DepotHead;
import com.jsh.erp.datasource.entities.vo.CommodityExportVO;
import com.jsh.erp.datasource.entities.vo.DepotHeadVO;
import com.jsh.erp.datasource.entities.vo.SalesManExportVO;
import com.jsh.erp.datasource.mappers.custom.DepotHeadNewMapper;
import com.jsh.erp.service.custom.DepotHeadNewService;
import com.jsh.erp.service.custom.WarehouseManagementService;
import com.jsh.erp.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 单据主表 服务实现类
 * </p>
 *
 * @author dawei
 * @since 2023-07-11 22:42:34
 */
@Service
public class DepotHeadNewServiceImpl extends ServiceImpl<DepotHeadNewMapper, DepotHead> implements DepotHeadNewService {

    @Autowired
    private WarehouseManagementService warehouseManagementService;

    @Autowired
    private DepotHeadNewMapper depotHeadNewMapper;

    @Override
    public String buildNumberForYsld(Date operTime) {
        if (operTime == null) {
            operTime = DateUtil.date();
        }
        String year = StrUtil.toString(DateUtil.year(operTime));
        String pre = "";
        if (StrUtil.equals("2022", year)) {
            pre = "J";
        }
        String ym = DateUtil.format(operTime, "yyyyMM");
        if (ym.compareTo("202308") >= 0 && year.compareTo("2024") < 0) {
            pre = "D";
        } else if (ym.compareTo("202308") < 0 && year.compareTo("2023") > 0) {
            pre = "K";
        } else if (StrUtil.equals("2024", year)) {
            pre = "E";
        } else if (StrUtil.equals("2025", year)) {
            pre = "F";
        } else if (StrUtil.equals("2026", year)) {
            pre = "G";
        } else if (StrUtil.equals("2027", year)) {
            pre = "H";
        } else if (StrUtil.equals("2028", year)) {
            pre = "I";
        }
        Map<String, Object> dh = getMap(new QueryWrapper<DepotHead>()
                .select(" IFNULL(SUBSTRING(MAX(number) from 2),0)+1 as maxNum")
                .lambda().isNotNull(DepotHead::getNumber)
                .apply("date_format(oper_time,'%Y')='" + year + "'")
                .like(DepotHead::getNumber, pre)
        );
        String str = String.format("%04d", MapUtil.getInt(dh, "maxNum"));
        return pre.concat(str);
    }

    @Override
    public Result checkNumberOutInfo(String type, String depotId) {
        DepotHead dh = getOne(new LambdaQueryWrapper<DepotHead>().eq(DepotHead::getNumber, depotId));
        List<WarehouseManagement> wmOutList = warehouseManagementService.list(new LambdaQueryWrapper<WarehouseManagement>()
                .eq(WarehouseManagement::getDepotId, depotId)
                .like(WarehouseManagement::getType, type)
        );
        // 无出库记录
        if (CollUtil.isEmpty(wmOutList)) {
            return Result.success(dh);
        } else {
            // 有出库记录，但已全部出库
            Long nms = 0L;
            if (StrUtil.equals(type, "发货")) {
                nms = wmOutList.stream().filter(Objects::nonNull).mapToLong(WarehouseManagement::getActualQuantity).sum();
            } else {
                nms = wmOutList.stream().filter(Objects::nonNull).mapToLong(WarehouseManagement::getNumbers).sum();
            }
            if (nms.intValue() == dh.getNumbers()) {
                if (StrUtil.contains(type, "出库")) {
                    dh.setPurchaseStatus("5");
                } else if (StrUtil.contains(type, "发货")) {
                    dh.setLogisticsStatus(2);
                }
                updateById(dh);
                return Result.success(dh);
            } else if (nms.intValue() > dh.getNumbers()) {
                return Result.failed("该订单号：【" + depotId + "】已全部" + type + "，如有问题，请联系管理员处理！");
            } else {
                if (StrUtil.contains(type, "出库")) {
                    dh.setPurchaseStatus("4");
                } else if (StrUtil.contains(type, "发货")) {
                    dh.setLogisticsStatus(1);
                }
                updateById(dh);
                // 有出库记录，但未全部出库
                DepotHead dhNew = new DepotHead();
                dhNew.setCommodityNo(dh.getCommodityNo());
                dhNew.setColor(dh.getColor());
                Long size1 = wmOutList.stream().filter(e -> e.getSize1() != null).mapToLong(WarehouseManagement::getSize1).sum();
                dhNew.setSize1(dh.getSize1() - size1.intValue());
                Long size2 = wmOutList.stream().filter(e -> e.getSize2() != null).mapToLong(WarehouseManagement::getSize2).sum();
                dhNew.setSize2(dh.getSize2() - size2.intValue());
                Long size3 = wmOutList.stream().filter(e -> e.getSize3() != null).mapToLong(WarehouseManagement::getSize3).sum();
                dhNew.setSize3(dh.getSize3() - size3.intValue());
                Long size4 = wmOutList.stream().filter(e -> e.getSize4() != null).mapToLong(WarehouseManagement::getSize4).sum();
                dhNew.setSize4(dh.getSize4() - size4.intValue());
                Long size5 = wmOutList.stream().filter(e -> e.getSize5() != null).mapToLong(WarehouseManagement::getSize5).sum();
                dhNew.setSize5(dh.getSize5() - size5.intValue());
                Long size6 = wmOutList.stream().filter(e -> e.getSize6() != null).mapToLong(WarehouseManagement::getSize6).sum();
                dhNew.setSize6(dh.getSize6() - size6.intValue());
                Long size7 = wmOutList.stream().filter(e -> e.getSize7() != null).mapToLong(WarehouseManagement::getSize7).sum();
                dhNew.setSize7(dh.getSize7() - size7.intValue());
                Long size8 = wmOutList.stream().filter(e -> e.getSize8() != null).mapToLong(WarehouseManagement::getSize8).sum();
                dhNew.setSize8(dh.getSize8() - size8.intValue());
                Long size9 = wmOutList.stream().filter(e -> e.getSize9() != null).mapToLong(WarehouseManagement::getSize9).sum();
                dhNew.setSize9(dh.getSize9() - size9.intValue());
                Long size10 = wmOutList.stream().filter(e -> e.getSize10() != null).mapToLong(WarehouseManagement::getSize10).sum();
                dhNew.setSize10(dh.getSize10() - size10.intValue());
                Long size11 = wmOutList.stream().filter(e -> e.getSize11() != null).mapToLong(WarehouseManagement::getSize11).sum();
                dhNew.setSize11(dh.getSize11() - size11.intValue());
                Long number = wmOutList.stream().filter(e -> e.getNumbers() != null).mapToLong(WarehouseManagement::getNumbers).sum();
                dhNew.setNumbers(dh.getNumbers() - number.intValue());
                return Result.success(dhNew);
            }
        }
    }

    @Override
    public void exportCommodityList(DepotHeadVO param, HttpServletResponse response) {
        try {
            ServletOutputStream out = response.getOutputStream();
            // 这里 需要指定写用哪个class去写
            ExcelWriter excelWriter = EasyExcel.write(out, CommodityExportVO.class).build();
            // 这里注意 如果同一个sheet只要创建一次
            String sheetName = "货品信息";
            if (StrUtil.isNotBlank(param.getBeginTime()) && StrUtil.isNotBlank(param.getEndTime())) {
                sheetName = sheetName.concat("(").concat(DateUtil.format(DateUtil.parse(param.getBeginTime()), "yyMMdd")).concat("-").concat(DateUtil.format(DateUtil.parse(param.getEndTime()), "yyMMdd")).concat(")");
            }
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            List<CommodityExportVO> projects = depotHeadNewMapper.exportCommodityList(param);
            if (CollUtil.isEmpty(projects)) {
                throw new ServiceException("暂无数据");
            } else {
                List<CommodityExportVO> list = Lists.newArrayList();
                int ids = 1;
                for (CommodityExportVO statistics : projects) {
                    CommodityExportVO vo = new CommodityExportVO();
                    BeanUtil.copyProperties(statistics, vo);
                    vo.setIds(ids++);
                    list.add(vo);
                }
//                list.forEach(this::setIndex);
                excelWriter.write(list, writeSheet);
                String fileName = "货品信息汇总清单.xlsx";
                // 文件名乱码解决：postman测会乱码，但浏览器下载就正常
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                // 发送一个报头，告诉浏览器当前页面不进行缓存，每次访问的时候必须从服务器上读取最新的数据
                response.setHeader("Cache-Control", "no-store");
                response.addHeader("Cache-Control", "max-age=0");
                excelWriter.finish();
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    @Override
    public List<DepotHead> mixList(LambdaQueryWrapper<DepotHead> wrapper) {
        return depotHeadNewMapper.mixList(wrapper);
    }

    @Override
    public void exportSalesManList(DepotHeadVO param, HttpServletResponse response) {
        try {
            ServletOutputStream out = response.getOutputStream();
            // 这里 需要指定写用哪个class去写
            ExcelWriter excelWriter = EasyExcel.write(out, SalesManExportVO.class).build();
            // 这里注意 如果同一个sheet只要创建一次
            String sheetName = "业务员";
            if (StrUtil.isNotBlank(param.getBeginTime()) && StrUtil.isNotBlank(param.getEndTime())) {
                sheetName = sheetName.concat("(").concat(DateUtil.format(DateUtil.parse(param.getBeginTime()), "yyMMdd")).concat("-").concat(DateUtil.format(DateUtil.parse(param.getEndTime()), "yyMMdd")).concat(")");
            }
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            List<SalesManExportVO> projects = depotHeadNewMapper.exportSalesManList(param);
            if (CollUtil.isEmpty(projects)) {
                throw new ServiceException("暂无数据");
            } else {
                List<SalesManExportVO> list = Lists.newArrayList();
                int ids = 1;
                for (SalesManExportVO statistics : projects) {
                    SalesManExportVO vo = new SalesManExportVO();
                    BeanUtil.copyProperties(statistics, vo);
                    vo.setIds(ids++);
                    list.add(vo);
                }
//                list.forEach(this::setIndex);
                excelWriter.write(list, writeSheet);
                String fileName = "业务员汇总清单.xlsx";
                // 文件名乱码解决：postman测会乱码，但浏览器下载就正常
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                // 发送一个报头，告诉浏览器当前页面不进行缓存，每次访问的时候必须从服务器上读取最新的数据
                response.setHeader("Cache-Control", "no-store");
                response.addHeader("Cache-Control", "max-age=0");
                excelWriter.finish();
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
}
