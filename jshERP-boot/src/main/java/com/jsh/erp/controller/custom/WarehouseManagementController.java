package com.jsh.erp.controller.custom;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entitie.custom.SampleManagement;
import com.jsh.erp.datasource.entitie.custom.WarehouseManagement;
import com.jsh.erp.datasource.entities.Commodity;
import com.jsh.erp.datasource.entities.DepotHead;
import com.jsh.erp.datasource.entities.DepotHeadVo4Body;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.datasource.entities.vo.DepotHeadVO;
import com.jsh.erp.datasource.mappers.custom.WarehouseManagementMapper;
import com.jsh.erp.exception.BusinessRunTimeException;
import com.jsh.erp.service.custom.CommodityService;
import com.jsh.erp.service.custom.DepotHeadNewService;
import com.jsh.erp.service.custom.SampleManagementService;
import com.jsh.erp.service.custom.WarehouseManagementService;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.ExcelUtils;
import com.jsh.erp.utils.Result;
import com.jsh.erp.utils.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 入出库记录表 前端控制器
 * </p>
 *
 * @author dawei
 * @since 2023-07-12 21:59:29
 */
@RestController
@RequestMapping("/warehouseManagement")
public class WarehouseManagementController {

    @Autowired
    private WarehouseManagementService warehouseManagementService;

    @Autowired
    private WarehouseManagementMapper warehouseManagementMapper;

    @Autowired
    private SampleManagementService sampleManagementService;

    @Autowired
    private UserService userService;
    @Autowired
    private CommodityService commodityService;

    @Autowired
    private DepotHeadNewService depotHeadNewService;

    /**
     * 获取入库库存
     *
     * @return
     */
    @GetMapping(value = "/getInOutFinalList")
    @ApiOperation(value = "获取入库库存")
    public Result getInOutFinalList(@RequestParam(value = Constants.SEARCH, required = false) String search,
                                    @RequestParam("currentPage") Integer currentPage,
                                    @RequestParam("pageSize") Integer pageSize) {
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String commodityName = StringUtil.getInfo(search, "commodityName");
        String inTime = StringUtil.getInfo(search, "inTime");
        String color = StringUtil.getInfo(search, "color");
        WarehouseManagement wm = new WarehouseManagement();
        wm.setCurrentPage(currentPage);
        wm.setPageSize(pageSize);
        wm.setCommodityNo(commodityNo);
        wm.setCommodityName(commodityName);
        wm.setInTimeStr(inTime);
        wm.setColor(color);
        IPage<WarehouseManagement> commoditys = warehouseManagementService.getInOutFinalList(wm);
        WarehouseManagement dh = new WarehouseManagement();
        dh.setAction("total");
        List<WarehouseManagement> wms = commoditys.getRecords();
        if (CollUtil.isNotEmpty(wms)) {
            dh.setSize1(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize1).reduce(0, Integer::sum));
            dh.setSize2(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize2).reduce(0, Integer::sum));
            dh.setSize3(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize3).reduce(0, Integer::sum));
            dh.setSize4(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize4).reduce(0, Integer::sum));
            dh.setSize5(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize5).reduce(0, Integer::sum));
            dh.setSize6(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize6).reduce(0, Integer::sum));
            dh.setSize7(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize7).reduce(0, Integer::sum));
            dh.setSize8(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize8).reduce(0, Integer::sum));
            dh.setSize9(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize9).reduce(0, Integer::sum));
            dh.setSize10(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize10).reduce(0, Integer::sum));
            dh.setSize11(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize11).reduce(0, Integer::sum));
            dh.setNumbers(wms.stream().filter(Objects::nonNull).map(WarehouseManagement::getNumbers).reduce(0, Integer::sum));
            commoditys.getRecords().add(dh);
        }
        return Result.success(commoditys);
    }

    /**
     * 获取订单物流情况
     *
     * @return
     */
    @GetMapping(value = "/getWMByNumber")
    @ApiOperation(value = "获取订单物流情况")
    public Result getWMByNumber(@RequestParam("depotId") String depotId) {
        QueryWrapper<WarehouseManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("depot_id", depotId);
        queryWrapper.eq("delete_flag", 0);
        queryWrapper.like("type", "发货");
        List<WarehouseManagement> commoditys = warehouseManagementService.list(queryWrapper);
        return Result.success(commoditys);
    }

    /**
     * 获取订单物流费
     *
     * @return
     */
    @GetMapping(value = "/getDepotHeadByNumber")
    @ApiOperation(value = "获取订单物流费")
    public Result getDepotHeadByNumber(@RequestParam("depotId") String depotId) {
        QueryWrapper<WarehouseManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("IFNULL(sum(logistics_freight),0) as logisticsFreight");
        queryWrapper.eq("depot_id", depotId);
        queryWrapper.eq("delete_flag", 0);
        queryWrapper.like("type", "发货");
        Map<String, Object> commoditys = warehouseManagementService.getMap(queryWrapper);
        BigDecimal sumCount = (BigDecimal) commoditys.get("logisticsFreight");
        return Result.success(sumCount);
    }

//    /**
//     * 调拨到样品库
//     *
//     * @return
//     */
//    @GetMapping(value = "/sendToSampleById")
//    @ApiOperation(value = "调拨到样品库")
//    public Result sendToSampleById(@RequestParam("id") String id) throws Exception {
//        User userInfo = userService.getCurrentUser(); // TODO 出库先不考虑
//        WarehouseManagement wm = warehouseManagementService.getById(id);
//        List<SampleManagement> sms = sampleManagementService.list(new LambdaQueryWrapper<SampleManagement>().eq(SampleManagement::getCommodityNo, wm.getCommodityNo()));
//        if (CollUtil.isNotEmpty(sms)) {
//            SampleManagement newsm = sms.get(0);
//            if (wm.getSize1() != null) {
//                newsm.setSize1(newsm.getSize1() + wm.getSize1());
//            }
//            if (wm.getSize2() != null) {
//                newsm.setSize2(newsm.getSize2() + wm.getSize2());
//            }
//            if (wm.getSize3() != null) {
//                newsm.setSize3(newsm.getSize3() + wm.getSize3());
//            }
//            if (wm.getSize4() != null) {
//                newsm.setSize4(newsm.getSize4() + wm.getSize4());
//            }
//            if (wm.getSize5() != null) {
//                newsm.setSize5(newsm.getSize5() + wm.getSize5());
//            }
//            if (wm.getSize6() != null) {
//                newsm.setSize6(newsm.getSize6() + wm.getSize6());
//            }
//            if (wm.getSize7() != null) {
//                newsm.setSize7(newsm.getSize7() + wm.getSize7());
//            }
//            if (wm.getSize8() != null) {
//                newsm.setSize8(newsm.getSize8() + wm.getSize8());
//            }
//            if (wm.getSize9() != null) {
//                newsm.setSize9(newsm.getSize9() + wm.getSize9());
//            }
//            if (wm.getSize10() != null) {
//                newsm.setSize10(newsm.getSize10() + wm.getSize10());
//            }
//            if (wm.getSize11() != null) {
//                newsm.setSize11(newsm.getSize11() + wm.getSize11());
//            }
//            if (wm.getNumbers() != null) {
//                newsm.setNumbers(newsm.getNumbers() + wm.getNumbers());
//            }
//            newsm.setInTime(wm.getInTime());
//            newsm.setPrice(wm.getPrice());
//            newsm.setUpdateBy(userInfo.getUsername());
//            newsm.setUpdateTime(DateUtil.date());
//            sampleManagementService.updateById(newsm);
//        } else {
//            SampleManagement sm = new SampleManagement();
//            BeanUtil.copyProperties(wm, sm, "id");
//            sm.setCreateBy(userInfo.getUsername());
//            sm.setCreateTime(DateUtil.date());
//            sampleManagementService.save(sm);
//        }
//        warehouseManagementService.removeById(id);
//        return Result.success(true);
//    }

    /**
     * 调拨到样品库
     *
     * @return
     */
    @GetMapping(value = "/sendToSampleByCommodityNo")
    @ApiOperation(value = "调拨到样品库")
    public Result sendToSampleByCommodityNo(@RequestParam("commodityNo") String commodityNo, @RequestParam("color") String color) throws Exception {
        User userInfo = userService.getCurrentUser(); // TODO 出库先不考虑
        WarehouseManagement req = new WarehouseManagement();
        req.setCommodityNo(commodityNo);
        req.setColor(color);
        List<WarehouseManagement> wms = warehouseManagementMapper.getInOutFinalList(req);
        wms.forEach(wm -> {
            if (wm.getNumbers() != 0) {
                List<SampleManagement> sms = sampleManagementService.list(new LambdaQueryWrapper<SampleManagement>()
                        .eq(SampleManagement::getCommodityNo, commodityNo)
                        .eq(SampleManagement::getColor, color));
                if (CollUtil.isNotEmpty(sms)) {
                    SampleManagement newsm = sms.get(0);
                    if (wm.getSize1() != null) {
                        newsm.setSize1(newsm.getSize1() + wm.getSize1());
                    }
                    if (wm.getSize2() != null) {
                        newsm.setSize2(newsm.getSize2() + wm.getSize2());
                    }
                    if (wm.getSize3() != null) {
                        newsm.setSize3(newsm.getSize3() + wm.getSize3());
                    }
                    if (wm.getSize4() != null) {
                        newsm.setSize4(newsm.getSize4() + wm.getSize4());
                    }
                    if (wm.getSize5() != null) {
                        newsm.setSize5(newsm.getSize5() + wm.getSize5());
                    }
                    if (wm.getSize6() != null) {
                        newsm.setSize6(newsm.getSize6() + wm.getSize6());
                    }
                    if (wm.getSize7() != null) {
                        newsm.setSize7(newsm.getSize7() + wm.getSize7());
                    }
                    if (wm.getSize8() != null) {
                        newsm.setSize8(newsm.getSize8() + wm.getSize8());
                    }
                    if (wm.getSize9() != null) {
                        newsm.setSize9(newsm.getSize9() + wm.getSize9());
                    }
                    if (wm.getSize10() != null) {
                        newsm.setSize10(newsm.getSize10() + wm.getSize10());
                    }
                    if (wm.getSize11() != null) {
                        newsm.setSize11(newsm.getSize11() + wm.getSize11());
                    }
                    if (wm.getNumbers() != null) {
                        newsm.setNumbers(newsm.getNumbers() + wm.getNumbers());
                    }
                    newsm.setInTime(wm.getInTime());
                    newsm.setPrice(wm.getPrice());
                    newsm.setUpdateBy(userInfo.getUsername());
                    newsm.setUpdateTime(DateUtil.date());
                    sampleManagementService.updateById(newsm);
                } else {
                    SampleManagement sm = new SampleManagement();
                    BeanUtil.copyProperties(wm, sm, "id");
                    sm.setCreateBy(userInfo.getUsername());
                    sm.setCreateTime(DateUtil.date());
                    sampleManagementService.save(sm);
                }
            }
            warehouseManagementService.update(Wrappers.lambdaUpdate(WarehouseManagement.class)
                    .eq(WarehouseManagement::getType, "入库")
                    .eq(WarehouseManagement::getCommodityNo, commodityNo)
                    .eq(WarehouseManagement::getColor, color)
                    .set(WarehouseManagement::getDeleteFlag, "1")
                    .set(WarehouseManagement::getRemark, "调拨入样品库")
            );
        });
        return Result.success(true);
    }

    /**
     * 新增单据主表及单据子表信息
     *
     * @param body
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/addWarehouseManagementRows")
    @ApiOperation(value = "新增单据主表及单据子表信息")
    public Object addWarehouseManagementRows(@RequestBody DepotHeadVo4Body body) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        User userInfo = userService.getCurrentUser(); // TODO 出库先不考虑
        String rows = body.getRows();
        JSONArray rowArr = JSONArray.parseArray(rows);
        if (null != rowArr && rowArr.size() > 0) {
            List<WarehouseManagement> wms = Lists.newArrayList();
            for (int i = 0; i < rowArr.size(); i++) {
                WarehouseManagement wm = JSONUtil.toBean(rowArr.getString(i), WarehouseManagement.class);
                if (StrUtil.isEmpty(wm.getCommodityNo())) {
                    continue;
                }
                Commodity co = commodityService.getOne(new LambdaQueryWrapper<Commodity>().eq(Commodity::getCommodityNo, wm.getCommodityNo()));
//                AccountItem accountItem = new AccountItem();
//                JSONObject tempInsertedJson = JSONObject.parseObject(rowArr.getString(i));
                wm.setType("入库");
                wm.setCommodityName(co.getName());
                wm.setId(null);
                int numbers = 0;
                if (wm.getSize1() != null) {
                    numbers += wm.getSize1();
                }
                if (wm.getSize2() != null) {
                    numbers += wm.getSize2();
                }
                if (wm.getSize3() != null) {
                    numbers += wm.getSize3();
                }
                if (wm.getSize4() != null) {
                    numbers += wm.getSize4();
                }
                if (wm.getSize5() != null) {
                    numbers += wm.getSize5();
                }
                if (wm.getSize6() != null) {
                    numbers += wm.getSize6();
                }
                if (wm.getSize7() != null) {
                    numbers += wm.getSize7();
                }
                if (wm.getSize8() != null) {
                    numbers += wm.getSize8();
                }
                if (wm.getSize9() != null) {
                    numbers += wm.getSize9();
                }
                if (wm.getSize10() != null) {
                    numbers += wm.getSize10();
                }
                if (wm.getSize11() != null) {
                    numbers += wm.getSize11();
                }
                wm.setNumbers(numbers);
                wm.setCreateBy(userInfo.getUsername());
                wm.setCreateTime(DateUtil.date());
                wms.add(wm);
            }
            warehouseManagementService.saveBatch(wms);
        }
        return result;
    }

    /**
     * 批量出库
     *
     * @param body
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/warehouseManagementOutRows")
    @ApiOperation(value = "批量出库")
    public Object warehouseManagementOutRows(@RequestBody DepotHeadVo4Body body) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        User userInfo = userService.getCurrentUser();
        String rows = body.getRows();
        JSONArray rowArr = JSONArray.parseArray(rows);
//        List<WarehouseManagement> handleWms = Lists.newArrayList();
//        if (null != rowArr && rowArr.size() > 0) {
//            for (int i = 0; i < rowArr.size(); i++) {
//                WarehouseManagement wm = JSONUtil.toBean(rowArr.getString(i), WarehouseManagement.class);
//                handleWms.add(wm);
//            }
//        }
//        Long counts = handleWms.stream().filter(e -> "project_review_finance_center".equals(e.getNoteName()) && "2".equals(e.getHandleResult())).count();


        if (null != rowArr && rowArr.size() > 0) {
            List<WarehouseManagement> wms = Lists.newArrayList();
            for (int i = 0; i < rowArr.size(); i++) {
                WarehouseManagement wm = JSONUtil.toBean(rowArr.getString(i), WarehouseManagement.class);
                wm.setDepotId(wm.getDepotIds());
                if (StrUtil.isEmpty(wm.getCommodityNo())) {
                    continue;
                }
                Commodity co = commodityService.getOne(new LambdaQueryWrapper<Commodity>().eq(Commodity::getCommodityNo, wm.getCommodityNo()));
//                AccountItem accountItem = new AccountItem();
//                JSONObject tempInsertedJson = JSONObject.parseObject(rowArr.getString(i));
                DepotHead dh = depotHeadNewService.getOne(new LambdaQueryWrapper<DepotHead>().eq(DepotHead::getNumber, wm.getDepotIds()));
                Result check = depotHeadNewService.checkNumberOutInfo("出库", wm.getDepotIds());
                if (StrUtil.equals(check.getCode(), "0")) {
                    throw new BusinessRunTimeException(ExceptionConstants.DEPOT_ITEM_ADD_FAILED_CODE,
                            "第" + (i + 1) + "行记录异常：" + check.getError());
                }
                DepotHead dhLimit = (DepotHead) check.getData();
                if (StrUtil.equals("部分出库", wm.getType())) {
                    dh.setPurchaseStatus("4");
                } else if (StrUtil.equals("全部出库", wm.getType())) {
                    dh.setPurchaseStatus("5");
                    wm.setSize1(dhLimit.getSize1());
                    wm.setSize2(dhLimit.getSize2());
                    wm.setSize3(dhLimit.getSize3());
                    wm.setSize4(dhLimit.getSize4());
                    wm.setSize5(dhLimit.getSize5());
                    wm.setSize6(dhLimit.getSize6());
                    wm.setSize7(dhLimit.getSize7());
                    wm.setSize8(dhLimit.getSize8());
                    wm.setSize9(dhLimit.getSize9());
                    wm.setSize10(dhLimit.getSize10());
                    wm.setSize11(dhLimit.getSize11());
                }
                wm.setCommodityName(co.getName());
                wm.setId(null);
                int numbers = 0;
                if (wm.getSize1() != null) {
                    numbers += wm.getSize1();
                }
                if (wm.getSize2() != null) {
                    numbers += wm.getSize2();
                }
                if (wm.getSize3() != null) {
                    numbers += wm.getSize3();
                }
                if (wm.getSize4() != null) {
                    numbers += wm.getSize4();
                }
                if (wm.getSize5() != null) {
                    numbers += wm.getSize5();
                }
                if (wm.getSize6() != null) {
                    numbers += wm.getSize6();
                }
                if (wm.getSize7() != null) {
                    numbers += wm.getSize7();
                }
                if (wm.getSize8() != null) {
                    numbers += wm.getSize8();
                }
                if (wm.getSize9() != null) {
                    numbers += wm.getSize9();
                }
                if (wm.getSize10() != null) {
                    numbers += wm.getSize10();
                }
                if (wm.getSize11() != null) {
                    numbers += wm.getSize11();
                }
                if (numbers > dhLimit.getNumbers()) {
                    throw new BusinessRunTimeException(ExceptionConstants.DEPOT_ITEM_ADD_FAILED_CODE,
                            "第" + (i + 1) + "行记录异常：订单号【" + wm.getDepotIds() + "】出库数已超该订单剩余出库数量，请重新输入！");
                }
                depotHeadNewService.updateById(dh);
                wm.setNumbers(numbers);
                wm.setCreateBy(userInfo.getUsername());
                wm.setCreateTime(DateUtil.date());
                warehouseManagementService.save(wm);
//                wms.add(wm);
            }
//            warehouseManagementService.saveBatch(wms);
        }
        return result;
    }

    /**
     * 批量发货
     *
     * @param body
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/wmLogisticsOutRows")
    @ApiOperation(value = "批量发货")
    public Object wmLogisticsOutRows(@RequestBody DepotHeadVo4Body body) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        User userInfo = userService.getCurrentUser();
        String rows = body.getRows();
        JSONArray rowArr = JSONArray.parseArray(rows);
//        List<WarehouseManagement> handleWms = Lists.newArrayList();
//        if (null != rowArr && rowArr.size() > 0) {
//            for (int i = 0; i < rowArr.size(); i++) {
//                WarehouseManagement wm = JSONUtil.toBean(rowArr.getString(i), WarehouseManagement.class);
//                handleWms.add(wm);
//            }
//        }
//        Long counts = handleWms.stream().filter(e -> "project_review_finance_center".equals(e.getNoteName()) && "2".equals(e.getHandleResult())).count();


        if (null != rowArr && rowArr.size() > 0) {
            List<WarehouseManagement> wms = Lists.newArrayList();
            for (int i = 0; i < rowArr.size(); i++) {
                WarehouseManagement wm = JSONUtil.toBean(rowArr.getString(i), WarehouseManagement.class);
                wm.setDepotId(wm.getDepotIds());
                if (StrUtil.isEmpty(wm.getCommodityNo())) {
                    continue;
                }
                Commodity co = commodityService.getOne(new LambdaQueryWrapper<Commodity>().eq(Commodity::getCommodityNo, wm.getCommodityNo()));
//                AccountItem accountItem = new AccountItem();
//                JSONObject tempInsertedJson = JSONObject.parseObject(rowArr.getString(i));
                DepotHead dh = depotHeadNewService.getOne(new LambdaQueryWrapper<DepotHead>().eq(DepotHead::getNumber, wm.getDepotIds()));
                Result check = depotHeadNewService.checkNumberOutInfo("发货", wm.getDepotIds());
                if (StrUtil.equals(check.getCode(), "0")) {
                    throw new BusinessRunTimeException(ExceptionConstants.DEPOT_ITEM_ADD_FAILED_CODE,
                            "第" + (i + 1) + "行记录异常：" + check.getError());
                }
                DepotHead dhLimit = (DepotHead) check.getData();
                if (StrUtil.equals("部分发货", wm.getType())) {
                    dh.setLogisticsStatus(1);
                } else if (StrUtil.equals("全部发货", wm.getType())) {
                    dh.setLogisticsStatus(2);
                    wm.setSize1(dhLimit.getSize1());
                    wm.setSize2(dhLimit.getSize2());
                    wm.setSize3(dhLimit.getSize3());
                    wm.setSize4(dhLimit.getSize4());
                    wm.setSize5(dhLimit.getSize5());
                    wm.setSize6(dhLimit.getSize6());
                    wm.setSize7(dhLimit.getSize7());
                    wm.setSize8(dhLimit.getSize8());
                    wm.setSize9(dhLimit.getSize9());
                    wm.setSize10(dhLimit.getSize10());
                    wm.setSize11(dhLimit.getSize11());
                }
                wm.setCommodityName(co.getName());
                wm.setId(null);
                int numbers = 0;
                if (wm.getSize1() != null) {
                    numbers += wm.getSize1();
                }
                if (wm.getSize2() != null) {
                    numbers += wm.getSize2();
                }
                if (wm.getSize3() != null) {
                    numbers += wm.getSize3();
                }
                if (wm.getSize4() != null) {
                    numbers += wm.getSize4();
                }
                if (wm.getSize5() != null) {
                    numbers += wm.getSize5();
                }
                if (wm.getSize6() != null) {
                    numbers += wm.getSize6();
                }
                if (wm.getSize7() != null) {
                    numbers += wm.getSize7();
                }
                if (wm.getSize8() != null) {
                    numbers += wm.getSize8();
                }
                if (wm.getSize9() != null) {
                    numbers += wm.getSize9();
                }
                if (wm.getSize10() != null) {
                    numbers += wm.getSize10();
                }
                if (wm.getSize11() != null) {
                    numbers += wm.getSize11();
                }
                if (numbers > dhLimit.getNumbers()) {
                    throw new BusinessRunTimeException(ExceptionConstants.DEPOT_ITEM_ADD_FAILED_CODE,
                            "第" + (i + 1) + "行记录异常：订单号【" + wm.getDepotIds() + "】发货数已超该订单剩余发货数量，请重新输入！");
                }
                depotHeadNewService.updateById(dh);
                wm.setNumbers(numbers);
                wm.setCreateBy(userInfo.getUsername());
                wm.setCreateTime(DateUtil.date());
                warehouseManagementService.save(wm);
//                wms.add(wm);
            }
//            warehouseManagementService.saveBatch(wms);
        }
        return result;
    }


    /**
     * 导出excel
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/exportWarehouseManagementList")
    @ApiOperation(value = "导出销售订单物流excel")
    public void exportWarehouseManagementList(@RequestParam(value = "salesMan", required = false) String salesMan,
                                              @RequestParam(value = "number", required = false) String number,
                                              @RequestParam(value = "commodityNo", required = false) String commodityNo,
                                              @RequestParam(value = "commodityName", required = false) String commodityName,
                                              @RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "ids", required = false) String ids,
                                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        LambdaQueryWrapper<WarehouseManagement> query = new LambdaQueryWrapper<>();
        query.eq(StrUtil.isNotBlank(salesMan), WarehouseManagement::getSalesMan, salesMan);
        query.like(StrUtil.isNotBlank(type), WarehouseManagement::getType, type);
        query.eq(StrUtil.isNotBlank(number), WarehouseManagement::getDepotId, number);
        if (StrUtil.isNotBlank(ids)) {
            List<String> splitToList = Splitter.on(",").trimResults().splitToList(ids);
            query.in(splitToList != null, WarehouseManagement::getId, splitToList);
        }
        query.eq(StrUtil.isNotBlank(commodityNo), WarehouseManagement::getCommodityNo, commodityNo);
        query.like(StrUtil.isNotBlank(commodityName), WarehouseManagement::getCommodityName, commodityName);
        query.orderByAsc(WarehouseManagement::getDepotId, WarehouseManagement::getDeliveryDate);
        query.last("limit 20000");

        List<WarehouseManagement> wmsOut = warehouseManagementService.list(query);
        String[] names = {
                "单据编号",
                "货品编码",
                "货品名称",
                "业务员",
                "类型",
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
                "总数量",
                "发货日期",
                "物流运费",
                "物流方式",
                "实发数量",
                "发货单号",
                "备注"
        };
        String title = "销售订单物流信息";
        List<String[]> objects = new ArrayList<String[]>();
        if (null != wmsOut) {
            for (WarehouseManagement s : wmsOut) {
                String[] objs = new String[24];
                objs[0] = s.getDepotId();
                objs[1] = s.getCommodityNo();
                objs[2] = s.getCommodityName();
                objs[3] = s.getSalesMan();
                objs[4] = s.getType();
                objs[5] = s.getColor();
                objs[6] = s.getSize1() == null || s.getSize1() == 0 ? "" : StrUtil.toString(s.getSize1());
                objs[7] = s.getSize2() == null || s.getSize2() == 0 ? "" : StrUtil.toString(s.getSize2());
                objs[8] = s.getSize3() == null || s.getSize3() == 0 ? "" : StrUtil.toString(s.getSize3());
                objs[9] = s.getSize4() == null || s.getSize4() == 0 ? "" : StrUtil.toString(s.getSize4());
                objs[10] = s.getSize5() == null || s.getSize5() == 0 ? "" : StrUtil.toString(s.getSize5());
                objs[11] = s.getSize6() == null || s.getSize6() == 0 ? "" : StrUtil.toString(s.getSize6());
                objs[12] = s.getSize7() == null || s.getSize7() == 0 ? "" : StrUtil.toString(s.getSize7());
                objs[13] = s.getSize8() == null || s.getSize8() == 0 ? "" : StrUtil.toString(s.getSize8());
                objs[14] = s.getSize9() == null || s.getSize9() == 0 ? "" : StrUtil.toString(s.getSize9());
                objs[15] = s.getSize10() == null || s.getSize10() == 0 ? "" : StrUtil.toString(s.getSize10());
                objs[16] = s.getSize11() == null || s.getSize11() == 0 ? "" : StrUtil.toString(s.getSize11());
                objs[17] = StrUtil.toString(s.getNumbers());
                objs[18] = DateUtil.format(s.getDeliveryDate(), "yyyy-MM-dd");
                objs[19] = s.getLogisticsFreight() == null ? "0" : StrUtil.toString(s.getLogisticsFreight().setScale(4, BigDecimal.ROUND_UP));
                objs[20] = s.getLogisticsMode();
                objs[21] = StrUtil.toString(StrUtil.toString(s.getSize11()));
                objs[22] = s.getShippingOrderNumber();
                objs[23] = s.getRemark();
                objects.add(objs);
            }
        }
        File file = ExcelUtils.exportObjectsWithoutTitle(title, "", names, title, objects);
        ExcelUtils.downloadExcel(file, file.getName(), response);
    }

    /**
     * 导出入库记录
     */
    @RequestMapping("/exportWarehouse")
    public void exportWarehouse(@RequestParam(value = "commodityNo", required = false) String commodityNo,
                                @RequestParam(value = "color", required = false) String color,
                                @RequestParam(value = "inTime", required = false) String inTime,
                                HttpServletResponse response) {
        DepotHeadVO vo = new DepotHeadVO();
        vo.setInTime(inTime);
        vo.setColor(color);
        vo.setCommodityNo(commodityNo);
        warehouseManagementService.exportWarehouse(vo, response);
    }


    /**
     * 导出库存盘点
     */
    @RequestMapping("/exportWarehouseInOut")
    public void exportWarehouseInOut(@RequestParam(value = "commodityNo", required = false) String commodityNo,
                                     @RequestParam(value = "color", required = false) String color,
                                     @RequestParam(value = "inTime", required = false) String inTime,
                                     HttpServletResponse response) {
        DepotHeadVO vo = new DepotHeadVO();
        vo.setInTime(inTime);
        vo.setColor(color);
        vo.setCommodityNo(commodityNo);
        warehouseManagementService.exportWarehouseInOut(vo, response);
    }


    /**
     * 获取自有仓库货品编号颜色
     */
    @GetMapping("/getCommodityNoColors")
    @ApiOperation(value = "获取入库库存")
    public Result getCommodityNoColors(@RequestParam("commodityNo") String commodityNo) {
        List<Map<String, String>> commoditys = warehouseManagementService.getCommodityNoColors(commodityNo);
        return Result.success(commoditys);
    }
}

