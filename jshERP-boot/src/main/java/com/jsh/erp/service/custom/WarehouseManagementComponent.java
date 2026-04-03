package com.jsh.erp.service.custom;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsh.erp.datasource.entitie.custom.SampleManagement;
import com.jsh.erp.datasource.entitie.custom.WarehouseManagement;
import com.jsh.erp.datasource.entities.Commodity;
import com.jsh.erp.datasource.entities.DepotHead;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.QueryUtils;
import com.jsh.erp.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service(value = "warehouseManagement_component")
@WarehouseManagementResource
public class WarehouseManagementComponent implements ICommonQuery {
    @Autowired
    private WarehouseManagementService warehouseManagementService;
    @Autowired
    private DepotHeadNewService depotHeadNewService;
    @Resource
    private UserService userService;
    @Autowired
    private SampleManagementService sampleManagementService;
    @Autowired
    private CommodityService commodityService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return warehouseManagementService.getById(id);
    }

    @Override
    public List<?> select(Map<String, String> map) throws Exception {
        return getMaterialList(map);
    }

    private List<?> getMaterialList(Map<String, String> map) throws Exception {
        String search = map.get(Constants.SEARCH);
        String type = StringUtil.getInfo(search, "type");
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String commodityName = StringUtil.getInfo(search, "commodityName");
        String color = StringUtil.getInfo(search, "color");
        String inTime = StringUtil.getInfo(search, "inTime");
        String number = StringUtil.getInfo(search, "number");
        String salesMan = StringUtil.getInfo(search, "salesMan");
//        String price = StringUtil.getInfo(search, "price");
        LambdaQueryWrapper<WarehouseManagement> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(type), WarehouseManagement::getType, type);
        if (StrUtil.isNotBlank(commodityNo)) {
            query.and(i -> i.eq(WarehouseManagement::getCommodityNo, commodityNo)
                    .or().eq(WarehouseManagement::getCommodityName, commodityNo));
        }
        query.eq(StrUtil.isNotBlank(salesMan), WarehouseManagement::getSalesMan, salesMan);
        query.eq(StrUtil.isNotBlank(number), WarehouseManagement::getDepotId, number);
        query.eq(StrUtil.isNotBlank(commodityNo), WarehouseManagement::getCommodityNo, commodityNo);
        query.like(StrUtil.isNotBlank(commodityName), WarehouseManagement::getCommodityName, commodityName);
        query.likeLeft(StrUtil.isNotBlank(color), WarehouseManagement::getColor, color);
        if (StrUtil.isNotBlank(inTime)) {
            query.apply("date_format(in_time,'%Y-%m-%d')='" + inTime + "'");
        }
//        query.like(StrUtil.isNotBlank(price), SampleManagementLog::getPrice, price);
        if (StrUtil.contains(type, "发货") || StrUtil.contains(type, "出库")) {
            query.orderByAsc(WarehouseManagement::getDepotId);
        } else if (StrUtil.contains(type, "入库")) {
            query.orderByAsc(WarehouseManagement::getInTime);
        } else {
            query.orderByAsc(WarehouseManagement::getCreateTime);
        }
        query.last("limit ".concat(StrUtil.toString(QueryUtils.offset(map))).concat(",").concat(StrUtil.toString(QueryUtils.rows(map))));
        List<WarehouseManagement> warehouseManagements = warehouseManagementService.list(query);
        if (StrUtil.contains(type, "入库") || StrUtil.contains(type, "出库")) {
            WarehouseManagement dh = new WarehouseManagement();
            dh.setAction("total");
            dh.setSize1(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize1).reduce(0, Integer::sum));
            dh.setSize2(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize2).reduce(0, Integer::sum));
            dh.setSize3(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize3).reduce(0, Integer::sum));
            dh.setSize4(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize4).reduce(0, Integer::sum));
            dh.setSize5(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize5).reduce(0, Integer::sum));
            dh.setSize6(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize6).reduce(0, Integer::sum));
            dh.setSize7(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize7).reduce(0, Integer::sum));
            dh.setSize8(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize8).reduce(0, Integer::sum));
            dh.setSize9(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize9).reduce(0, Integer::sum));
            dh.setSize10(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize10).reduce(0, Integer::sum));
            dh.setSize11(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getSize11).reduce(0, Integer::sum));
            dh.setNumbers(warehouseManagements.stream().filter(Objects::nonNull).map(WarehouseManagement::getNumbers).reduce(0, Integer::sum));
            warehouseManagements.add(dh);
        }
        return warehouseManagements;
    }

    @Override
    public Long counts(Map<String, String> map) throws Exception {
        String search = map.get(Constants.SEARCH);
        String type = StringUtil.getInfo(search, "type");
        String number = StringUtil.getInfo(search, "number");
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String commodityName = StringUtil.getInfo(search, "commodityName");
        String color = StringUtil.getInfo(search, "color");
//        String price = StringUtil.getInfo(search, "price");
        LambdaQueryWrapper<WarehouseManagement> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(type), WarehouseManagement::getType, type);
        query.eq(StrUtil.isNotBlank(number), WarehouseManagement::getNumbers, number);
        query.eq(StrUtil.isNotBlank(commodityNo), WarehouseManagement::getCommodityNo, commodityNo);
        query.like(StrUtil.isNotBlank(commodityName), WarehouseManagement::getCommodityName, commodityName);
//        query.like(StrUtil.isNotBlank(commodityNo), SampleManagement::getCommodityNo, commodityNo);
        query.likeLeft(StrUtil.isNotBlank(color), WarehouseManagement::getColor, color);
//        query.like(StrUtil.isNotBlank(price), SampleManagement::getPrice, price);
        return warehouseManagementService.count(query);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception {
        WarehouseManagement commodity = JSONObject.parseObject(obj.toJSONString(), WarehouseManagement.class);
        User userInfo = userService.getCurrentUser(); // TODO 出库先不考虑
        Commodity co = commodityService.getOne(new LambdaQueryWrapper<Commodity>().eq(Commodity::getCommodityNo, commodity.getCommodityNo()));
//        WarehouseManagement wmSearch = new WarehouseManagement();
//        wmSearch.setCommodityNo(commodity.getCommodityNo());
//        wmSearch.setCommodityName(commodity.getCommodityName());
//        wmSearch.setColor(commodity.getColor());
//        List<WarehouseManagement> wms = warehouseManagementService.getInOutFinalListNoPage(wmSearch);
//        if (CollUtil.isEmpty(wms)) {
//            throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                    "该货品库存不足，无法出样！");
//        }
//        WarehouseManagement wm = wms.get(0);
//        if (commodity.getSize1() != null) {
//            int size1 = new BigDecimal(wm.getSize1()).subtract(new BigDecimal(commodity.getSize1())).intValue();
//            if (size1 < 0) {
//                throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                        "S库存不足，无法出样！");
//            }
//        }
//        if (StrUtil.isNotBlank(commodity.getSize2())) {
//            int size2 = new BigDecimal(sm.getSize2()).subtract(new BigDecimal(commodity.getSize2())).intValue();
//            if (size2 < 0) {
//                throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                        "M库存不足，无法出样！");
//            } else {
//                sm.setSize2(new BigDecimal(sm.getSize2()).subtract(new BigDecimal(commodity.getSize2())).toString());
//            }
//        }
//        if (StrUtil.isNotBlank(commodity.getSize3())) {
//            int size3 = new BigDecimal(sm.getSize3()).subtract(new BigDecimal(commodity.getSize3())).intValue();
//            if (size3 < 0) {
//                throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                        "L库存不足，无法出样！");
//            } else {
//                sm.setSize3(new BigDecimal(sm.getSize3()).subtract(new BigDecimal(commodity.getSize3())).toString());
//            }
//        }
//        if (StrUtil.isNotBlank(commodity.getSize4())) {
//            int size4 = new BigDecimal(sm.getSize4()).subtract(new BigDecimal(commodity.getSize4())).intValue();
//            if (size4 < 0) {
//                throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                        "XL库存不足，无法出样！");
//            } else {
//                sm.setSize4(new BigDecimal(sm.getSize4()).subtract(new BigDecimal(commodity.getSize4())).toString());
//            }
//        }
//        if (StrUtil.isNotBlank(commodity.getSize5())) {
//            int size5 = new BigDecimal(sm.getSize5()).subtract(new BigDecimal(commodity.getSize5())).intValue();
//            if (size5 < 0) {
//                throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                        "2XL库存不足，无法出样！");
//            } else {
//                sm.setSize5(new BigDecimal(sm.getSize5()).subtract(new BigDecimal(commodity.getSize5())).toString());
//            }
//        }
//        if (StrUtil.isNotBlank(commodity.getSize6())) {
//            int size6 = new BigDecimal(sm.getSize6()).subtract(new BigDecimal(commodity.getSize6())).intValue();
//            if (size6 < 0) {
//                throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                        "3XL库存不足，无法出样！");
//            } else {
//                sm.setSize6(new BigDecimal(sm.getSize6()).subtract(new BigDecimal(commodity.getSize6())).toString());
//            }
//        }
//        if (StrUtil.isNotBlank(commodity.getSize7())) {
//            int size7 = new BigDecimal(sm.getSize7()).subtract(new BigDecimal(commodity.getSize7())).intValue();
//            if (size7 < 0) {
//                throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                        "4XL库存不足，无法出样！");
//            } else {
//                sm.setSize7(new BigDecimal(sm.getSize7()).subtract(new BigDecimal(commodity.getSize7())).toString());
//            }
//        }
//        if (StrUtil.isNotBlank(commodity.getSize8())) {
//            int size8 = new BigDecimal(sm.getSize8()).subtract(new BigDecimal(commodity.getSize8())).intValue();
//            if (size8 < 0) {
//                throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                        "5XL库存不足，无法出样！");
//            } else {
//                sm.setSize8(new BigDecimal(sm.getSize8()).subtract(new BigDecimal(commodity.getSize8())).toString());
//            }
//        }
//        if (StrUtil.isNotBlank(commodity.getSize9())) {
//            int size9 = new BigDecimal(sm.getSize9()).subtract(new BigDecimal(commodity.getSize9())).intValue();
//            if (size9 < 0) {
//                throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                        "6XL库存不足，无法出样！");
//            } else {
//                sm.setSize9(new BigDecimal(sm.getSize9()).subtract(new BigDecimal(commodity.getSize9())).toString());
//            }
//        }
//        if (StrUtil.isNotBlank(commodity.getSize10())) {
//            int size10 = new BigDecimal(sm.getSize10()).subtract(new BigDecimal(commodity.getSize10())).intValue();
//            if (size10 < 0) {
//                throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                        "7XL库存不足，无法出样！");
//            } else {
//                sm.setSize10(new BigDecimal(sm.getSize10()).subtract(new BigDecimal(commodity.getSize10())).toString());
//            }
//        }
//        if (StrUtil.isNotBlank(commodity.getSize11())) {
//            int size11 = new BigDecimal(sm.getSize11()).subtract(new BigDecimal(commodity.getSize11())).intValue();
//            if (size11 < 0) {
//                throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
//                        "XS库存不足，无法出样！");
//            } else {
//                sm.setSize11(new BigDecimal(sm.getSize11()).subtract(new BigDecimal(commodity.getSize11())).toString());
//            }
//        }
//        sm.setNumbers(new BigDecimal(sm.getNumbers()).subtract(new BigDecimal(commodity.getNumbers())).toString());
//        sampleManagementService.updateById(sm);
        DepotHead dh = depotHeadNewService.getOne(new LambdaQueryWrapper<DepotHead>().eq(DepotHead::getNumber, commodity.getDepotId()));
        if (StrUtil.equals("部分出库", commodity.getType())) {
            dh.setPurchaseStatus("4");
            depotHeadNewService.updateById(dh);
        } else if (StrUtil.equals("全部出库", commodity.getType())) {
            dh.setPurchaseStatus("5");
            depotHeadNewService.updateById(dh);
        } else if (StrUtil.equals("全部发货", commodity.getType())) {
            dh.setLogisticsStatus(2);
            depotHeadNewService.updateById(dh);
        } else if (StrUtil.equals("部分发货", commodity.getType())) {
            dh.setLogisticsStatus(1);
            depotHeadNewService.updateById(dh);
        } else if (StrUtil.equals("调拨", commodity.getType())) {
            SampleManagement sm = new SampleManagement();
            BeanUtil.copyProperties(commodity, sm);
            sm.setCreateTime(DateUtil.date());
            sm.setInTime(DateUtil.date());
            sm.setPrice(co.getPrice());
            sm.setCreateBy("系统调拨_".concat(userInfo.getUsername()));
            sampleManagementService.save(sm);
        }
        if (commodity.getId() == null) {
//            List<WarehouseManagement> wms = warehouseManagementService.list(new LambdaQueryWrapper<WarehouseManagement>().eq(WarehouseManagement::getCommodityNo, commodity.getCommodityNo()));
//            if (CollUtil.isNotEmpty(wms)) {
//                WarehouseManagement wm = wms.get(0);
//                if (commodity.getSize1() != null) {
//                    wm.setSize1(wm.getSize1() + commodity.getSize1());
//                }
//                if (commodity.getSize2() != null) {
//                    wm.setSize2(wm.getSize2() + commodity.getSize2());
//                }
//                if (commodity.getSize3() != null) {
//                    wm.setSize3(wm.getSize3() + commodity.getSize3());
//                }
//                if (commodity.getSize4() != null) {
//                    wm.setSize4(wm.getSize4() + commodity.getSize4());
//                }
//                if (commodity.getSize5() != null) {
//                    wm.setSize5(wm.getSize5() + commodity.getSize5());
//                }
//                if (commodity.getSize6() != null) {
//                    wm.setSize6(wm.getSize6() + commodity.getSize6());
//                }
//                if (commodity.getSize7() != null) {
//                    wm.setSize7(wm.getSize7() + commodity.getSize7());
//                }
//                if (commodity.getSize8() != null) {
//                    wm.setSize8(wm.getSize8() + commodity.getSize8());
//                }
//                if (commodity.getSize9() != null) {
//                    wm.setSize9(wm.getSize9() + commodity.getSize9());
//                }
//                if (commodity.getSize10() != null) {
//                    wm.setSize10(wm.getSize10() + commodity.getSize10());
//                }
//                if (commodity.getSize11() != null) {
//                    wm.setSize11(wm.getSize11() + commodity.getSize11());
//                }
//                if (commodity.getNumbers() != null) {
//                    wm.setNumbers(wm.getNumbers() + commodity.getNumbers());
//                }
//                wm.setInTime(commodity.getInTime());
//                wm.setPrice(commodity.getPrice());
//                wm.setUpdateBy(userInfo.getUsername());
//                wm.setUpdateTime(DateUtil.date());
//                warehouseManagementService.updateById(wm);
//                return 1;
//            } else {
            commodity.setUpdateBy(userInfo.getUsername());
            commodity.setUpdateTime(DateUtil.date());
            commodity.setCreateTime(DateUtil.date());
            commodity.setCreateBy(userInfo.getUsername());
//            }
        } else {
            commodity.setUpdateBy(userInfo.getUsername());
            commodity.setUpdateTime(DateUtil.date());
        }
        warehouseManagementService.saveOrUpdate(commodity);
        return 1;
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request) throws Exception {
        WarehouseManagement commodity = JSONObject.parseObject(obj.toJSONString(), WarehouseManagement.class);
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
        warehouseManagementService.saveOrUpdate(commodity);
        return 1;
    }

    @Override
    public int delete(Long id, HttpServletRequest request) throws Exception {
        warehouseManagementService.removeById(id);
        return 1;
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request) throws Exception {
        List<String> list = Arrays.asList(ids.split(","));
        warehouseManagementService.removeByIds(list);
        return 1;
    }

    @Override
    public int checkIsNameExist(Long id, String name) throws Exception {
        return 1;
    }

//    public int checkIsExist(Long id, String name)throws Exception {
//        return commodityService.checkIsExist(id, name, model, color, standard, mfrs, otherField1,
//                otherField2, otherField3, unit, unitId);
//    }

}
