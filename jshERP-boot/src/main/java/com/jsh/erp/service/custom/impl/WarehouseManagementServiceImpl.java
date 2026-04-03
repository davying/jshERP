package com.jsh.erp.service.custom.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.jsh.erp.datasource.entitie.custom.WarehouseManagement;
import com.jsh.erp.datasource.entities.vo.DepotHeadVO;
import com.jsh.erp.datasource.entities.vo.WarehouseInExportVO;
import com.jsh.erp.datasource.entities.vo.WarehouseInOutExportVO;
import com.jsh.erp.datasource.mappers.custom.WarehouseManagementMapper;
import com.jsh.erp.service.custom.WarehouseManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 入出库记录表 服务实现类
 * </p>
 *
 * @author dawei
 * @since 2023-07-12 21:59:29
 */
@Service
public class WarehouseManagementServiceImpl extends ServiceImpl<WarehouseManagementMapper, WarehouseManagement> implements WarehouseManagementService {

    @Autowired
    private WarehouseManagementMapper warehouseManagementMapper;

    @Override
    public IPage<WarehouseManagement> getInOutFinalList(WarehouseManagement wm) {
        Page<WarehouseManagement> page = new Page<>(wm.getCurrentPage(), wm.getPageSize());
        IPage<WarehouseManagement> res = warehouseManagementMapper.getInOutFinalList(page, wm);
        return res;
    }


    @Override
    public List<WarehouseManagement> getInOutFinalListNoPage(WarehouseManagement wm) {
        List<WarehouseManagement> res = warehouseManagementMapper.getInOutFinalList(wm);
        return res;
    }

    @Override
    public List<Map<String, String>> getCommodityNoColors(String commodityNo) {
        List<Map<String, String>> res = warehouseManagementMapper.getCommodityNoColors(commodityNo);
        return res;
    }

    @Override
    public void exportWarehouse(DepotHeadVO param, HttpServletResponse response) {
        try {
            ServletOutputStream out = response.getOutputStream();
            // 这里 需要指定写用哪个class去写
            ExcelWriter excelWriter = EasyExcel.write(out, WarehouseInExportVO.class).build();
            // 这里注意 如果同一个sheet只要创建一次
            String sheetName = "入库记录";
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            LambdaQueryWrapper<WarehouseManagement> wm = new LambdaQueryWrapper<>();
//            0ptional.ofNullable(dto.getUserId()).isPresent()
            wm.like(StrUtil.isNotBlank(param.getCommodityNo()), WarehouseManagement::getCommodityNo, param.getCommodityNo());
            wm.like(StrUtil.isNotBlank(param.getColor()), WarehouseManagement::getColor, param.getColor());
            if (StrUtil.isNotBlank(param.getInTime())) {
                wm.apply("date_format(in_time,'%Y-%m-%d')='" + param.getInTime() + "'");
            }
            wm.eq(WarehouseManagement::getType, "入库");
            List<WarehouseManagement> projects = warehouseManagementMapper.selectList(wm);
//            List<WarehouseInExportVO> projects = warehouseManagementMapper.exportWarehouse(param);
            if (CollUtil.isEmpty(projects)) {
                throw new ServiceException("暂无数据");
            } else {
                List<WarehouseInExportVO> list = Lists.newArrayList();
                int ids = 1;
                for (WarehouseManagement statistics : projects) {
                    WarehouseInExportVO vo = new WarehouseInExportVO();
                    BeanUtil.copyProperties(statistics, vo);
                    vo.setInTime(DateUtil.format(statistics.getInTime(), "yyyy/MM/dd"));
                    vo.setCommodityNo(statistics.getCommodityNo().concat("(").concat(statistics.getCommodityName()).concat(")"));
                    vo.setIds(ids++);
                    list.add(vo);
                }
                WarehouseInExportVO voTotal = new WarehouseInExportVO();
                voTotal.setIds(ids++);
                voTotal.setCommodityNo("合计：");
                voTotal.setSize1(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize1).reduce(0, Integer::sum));
                voTotal.setSize2(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize2).reduce(0, Integer::sum));
                voTotal.setSize3(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize3).reduce(0, Integer::sum));
                voTotal.setSize4(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize4).reduce(0, Integer::sum));
                voTotal.setSize5(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize5).reduce(0, Integer::sum));
                voTotal.setSize6(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize6).reduce(0, Integer::sum));
                voTotal.setSize7(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize7).reduce(0, Integer::sum));
                voTotal.setSize8(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize8).reduce(0, Integer::sum));
                voTotal.setSize9(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize9).reduce(0, Integer::sum));
                voTotal.setSize10(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize10).reduce(0, Integer::sum));
                voTotal.setSize11(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize11).reduce(0, Integer::sum));
                voTotal.setNumbers(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getNumbers).reduce(0, Integer::sum));
                list.add(voTotal);
//                list.forEach(this::setIndex);
                excelWriter.write(list, writeSheet);
                String fileName = "入库记录清单.xlsx";
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
    public void exportWarehouseInOut(DepotHeadVO param, HttpServletResponse response) {
        try {
            ServletOutputStream out = response.getOutputStream();
            // 这里 需要指定写用哪个class去写
            ExcelWriter excelWriter = EasyExcel.write(out, WarehouseInOutExportVO.class).build();
            // 这里注意 如果同一个sheet只要创建一次
            String sheetName = "库存盘点";
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            WarehouseManagement vmDto = new WarehouseManagement();
            BeanUtil.copyProperties(param, vmDto);
//            0ptional.ofNullable(dto.getUserId()).isPresent()
            List<WarehouseManagement> projects = warehouseManagementMapper.getInOutFinalList(vmDto);
//            List<WarehouseInExportVO> projects = warehouseManagementMapper.exportWarehouse(param);
            if (CollUtil.isEmpty(projects)) {
                throw new ServiceException("暂无数据");
            } else {
                List<WarehouseInOutExportVO> list = Lists.newArrayList();
                int ids = 1;
                for (WarehouseManagement statistics : projects) {
                    WarehouseInOutExportVO vo = new WarehouseInOutExportVO();
                    BeanUtil.copyProperties(statistics, vo);
                    vo.setCommodityNo(statistics.getCommodityNo().concat("(").concat(statistics.getCommodityName()).concat(")"));
                    vo.setIds(ids++);
                    list.add(vo);
                }
                WarehouseInOutExportVO voTotal = new WarehouseInOutExportVO();
                voTotal.setIds(ids++);
                voTotal.setCommodityNo("合计：");
                voTotal.setSize1(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize1).reduce(0, Integer::sum));
                voTotal.setSize2(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize2).reduce(0, Integer::sum));
                voTotal.setSize3(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize3).reduce(0, Integer::sum));
                voTotal.setSize4(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize4).reduce(0, Integer::sum));
                voTotal.setSize5(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize5).reduce(0, Integer::sum));
                voTotal.setSize6(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize6).reduce(0, Integer::sum));
                voTotal.setSize7(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize7).reduce(0, Integer::sum));
                voTotal.setSize8(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize8).reduce(0, Integer::sum));
                voTotal.setSize9(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize9).reduce(0, Integer::sum));
                voTotal.setSize10(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize10).reduce(0, Integer::sum));
                voTotal.setSize11(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize11).reduce(0, Integer::sum));
                voTotal.setNumbers(projects.stream().filter(Objects::nonNull).map(WarehouseManagement::getNumbers).reduce(0, Integer::sum));
                list.add(voTotal);
//                list.forEach(this::setIndex);
                excelWriter.write(list, writeSheet);
                String fileName = "库存盘点清单.xlsx";
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
