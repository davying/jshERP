package com.jsh.erp.service.depotHead;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.jsh.erp.datasource.entitie.custom.WarehouseManagement;
import com.jsh.erp.datasource.entities.DepotHead;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.datasource.mappers.UserMapperEx;
import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.service.custom.DepotHeadNewService;
import com.jsh.erp.service.custom.WarehouseManagementService;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.QueryUtils;
import com.jsh.erp.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service(value = "depotHead_component")
@DepotHeadResource
public class DepotHeadComponent implements ICommonQuery {

    @Resource
    private DepotHeadService depotHeadService;

    @Autowired
    private DepotHeadNewService depotHeadNewService;

    @Resource
    private UserService userService;

    @Autowired
    private WarehouseManagementService warehouseManagementService;

    @Autowired
    private UserMapperEx userMapperEx;

    @Override
    public Object selectOne(Long id) throws Exception {
        return depotHeadNewService.getById(id);
    }

    @Override
    public List<?> select(Map<String, String> map) throws Exception {
        return getDepotHeadList(map);
    }

    private List<?> getDepotHeadList(Map<String, String> map) throws Exception {
        String search = map.get(Constants.SEARCH);
        User userInfo = userService.getCurrentUser();
        List<Map<String, String>> users = Lists.newArrayList();
        String deptName = StringUtil.getInfo(search, "deptName");
        if (StrUtil.equals(deptName, "全部") || StrUtil.isBlank(deptName)) {
            users = userMapperEx.selectConditionUserList(null);
        } else if (StrUtil.contains(deptName, "销售")) {
            users = userMapperEx.selectConditionUserList(deptName);
        } else if (StrUtil.equals(deptName, "财务仓储")) {
            List<String> deptNames = Lists.newArrayList();
            deptNames.add("仓库管理员");
            deptNames.add("财务部");
            users = userMapperEx.selectUserList(deptNames);
        }
        if (CollUtil.isEmpty(users)) {
            return new ArrayList<DepotHead>();
        }
        List<String> userIds = users.stream().map(o -> StrUtil.toString(o.get("id"))).collect(Collectors.toList());
        String type = StringUtil.getInfo(search, "type");
        String salesMan = StringUtil.getInfo(search, "salesMan");
        String operTime = StringUtil.getInfo(search, "operTime");
        String isFinalPay = StringUtil.getInfo(search, "isFinalPay");
        String roleType = StringUtil.getInfo(search, "roleType");
        String status = StringUtil.getInfo(search, "status");
        String color = StringUtil.getInfo(search, "color");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        String logisticsStatus = StringUtil.getInfo(search, "logisticsStatus");
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String customer = StringUtil.getInfo(search, "customer");
        String depotId = StringUtil.getInfo(search, "depotId");
        String creator = StringUtil.getInfo(search, "creator");
        String printer = StringUtil.getInfo(search, "printer");
        String ironingStaff = StringUtil.getInfo(search, "ironingStaff");
        String stackStaff = StringUtil.getInfo(search, "stackStaff");
        String rustStaff = StringUtil.getInfo(search, "rustStaff");
        String printingReconciliationDate = StringUtil.getInfo(search, "printingReconciliationDate");
        String stampingReconciliationDate = StringUtil.getInfo(search, "stampingReconciliationDate");
        String embroideryReconciliationDate = StringUtil.getInfo(search, "embroideryReconciliationDate");
        LambdaQueryWrapper<DepotHead> query = new LambdaQueryWrapper<>();
        String numbers = StringUtil.getInfo(search, "number");
        if (StrUtil.isNotBlank(numbers)) {
            List<String> numberList = Splitter.on("，").trimResults().splitToList(numbers.replaceAll(",", "，"));
            query.in(DepotHead::getNumber, numberList);
        }
        query.eq(DepotHead::getDeleteFlag, 0);
        if (StrUtil.equals(isFinalPay, "是")) {
            query.apply("IFNULL(end_staff,'')='是'");
        } else if (StrUtil.equals(isFinalPay, "否")) {
            query.apply("IFNULL(end_staff,'')!='是'");
        }
        if (CollUtil.isNotEmpty(userIds)) {
            query.in(DepotHead::getCreator, userIds);
        }
        if (StrUtil.contains(userInfo.getRoleName(), "业务员") || StrUtil.contains(roleType, "个人")) {
            query.eq(DepotHead::getCreator, userInfo.getId());
        } else if (StrUtil.contains(userInfo.getRoleName(), "财务")) {
            query.isNotNull(DepotHead::getNumber);
        } else if (StrUtil.contains(userInfo.getRoleName(), "仓库")) {
            query.in(DepotHead::getStatus, 9, 4, 1);
            query.notLike(DepotHead::getNumber, "D");
        } else {
            query.eq(StrUtil.isNotBlank(creator), DepotHead::getCreator, creator);
        }
        query.eq(StrUtil.isNotBlank(logisticsStatus), DepotHead::getLogisticsStatus, logisticsStatus);
        query.eq(StrUtil.isNotBlank(salesMan), DepotHead::getSalesMan, salesMan);
        query.eq(StrUtil.isNotBlank(type), DepotHead::getType, type);
        query.eq(StrUtil.isNotBlank(color), DepotHead::getColor, color);
        query.like(StrUtil.isNotBlank(commodityNo), DepotHead::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(printer), DepotHead::getPrinter, printer);
        query.eq(StrUtil.isNotBlank(ironingStaff), DepotHead::getIroningStaff, ironingStaff);
        query.eq(StrUtil.isNotBlank(status), DepotHead::getStatus, status);
        query.like(StrUtil.isNotBlank(customer), DepotHead::getCustomer, customer);
        query.eq(StrUtil.isNotBlank(depotId), DepotHead::getId, depotId);
        // 修改后
        if (StrUtil.isNotBlank(beginTime)) {
            query.ge(DepotHead::getOperTime, beginTime.concat(" 00:00:00"));
        }
        if (StrUtil.isNotBlank(endTime)) {
            query.le(DepotHead::getOperTime, endTime.concat(" 23:59:59"));
        }

        query.eq(StrUtil.isNotBlank(stackStaff), DepotHead::getStackStaff, stackStaff);
        query.eq(StrUtil.isNotBlank(rustStaff), DepotHead::getRustStaff, rustStaff);
        if (StrUtil.isNotBlank(operTime)) {
            query.apply("date_format(oper_time,'%Y-%m-%d')='" + operTime + "'");
        }
        if (StrUtil.isNotBlank(printingReconciliationDate)) {
            query.apply("date_format(printing_reconciliation_date,'%Y-%m-%d')='" + printingReconciliationDate + "'");
        }
        if (StrUtil.isNotBlank(stampingReconciliationDate)) {
            query.apply("date_format(stamping_reconciliation_date,'%Y-%m-%d')='" + stampingReconciliationDate + "'");
        }
        if (StrUtil.isNotBlank(embroideryReconciliationDate)) {
            query.apply("date_format(embroidery_reconciliation_date,'%Y-%m-%d')='" + embroideryReconciliationDate + "'");
        }
        if (StrUtil.contains(userInfo.getRoleName(), "业务员") || StrUtil.contains(roleType, "个人")) {
            query.orderByDesc(DepotHead::getOperTime);
        } else {
            query.orderByAsc(DepotHead::getNumber);
        }
        String rows = StrUtil.toString(QueryUtils.rows(map));
        if (StrUtil.endWith(rows, "1")) {
            query.last("limit ".concat(StrUtil.toString(QueryUtils.offset(map))).concat(",").concat(StrUtil.toString(QueryUtils.rows(map) - 1)));
        } else {
            query.last("limit ".concat(StrUtil.toString(QueryUtils.offset(map))).concat(",").concat(StrUtil.toString(QueryUtils.rows(map))));
        }
        List<DepotHead> depots = depotHeadNewService.list(query);
        depots.forEach(e -> {
            // 订单物流费
            QueryWrapper<WarehouseManagement> queryWarehouseWrapper = new QueryWrapper<>();
            queryWarehouseWrapper.select("IFNULL(sum(logistics_freight),0) as logisticsFreight");
            queryWarehouseWrapper.eq("depot_id", e.getNumber());
            queryWarehouseWrapper.eq("delete_flag", 0);
            queryWarehouseWrapper.like("type", "发货");
            Map<String, Object> orderExpressFee = warehouseManagementService.getMap(queryWarehouseWrapper);
            BigDecimal logisticsFreight = (BigDecimal) orderExpressFee.get("logisticsFreight");
            e.setLogisticsFreight(logisticsFreight);
            BigDecimal rate = null;
            if (e.getFinancialTaxAmount().compareTo(new BigDecimal(0)) <= 0) {
                if (StrUtil.equals("增票11个点", e.getBillingRate())) {
                    rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.11"));
                } else if (StrUtil.equals("普票2个点", e.getBillingRate())) {
                    rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.02"));
                } else if (StrUtil.equals("普票3个点", e.getBillingRate())) {
                    rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.03"));
                }
                e.setFinancialTaxAmount(rate == null ? new BigDecimal(0) : rate);
            } else {
                rate = e.getFinancialTaxAmount();
            }


            BigDecimal ocm = (e.getTotalFinancialPrice()
                    .subtract(rate != null ? rate : new BigDecimal(0))
                    .subtract(e.getTotalPrintingPrice())
                    .subtract(e.getHotStampingPaintingTotalPrice())
                    .subtract(e.getTotalStackPrice())
                    .subtract(e.getEmbroideryTotalPrice())
                    .subtract(e.getFinancialDeductionsFee())
                    .subtract(e.getFinancialRebate())
                    .subtract(e.getFinancialCostAmount())
                    .subtract(e.getOtherExpensesInFinance())
                    .subtract(e.getOtherExpensesInFinance2())
//                    .subtract(e.getFinancialTaxAmount())
                    .subtract(e.getLogisticsFreight())).setScale(4, BigDecimal.ROUND_UP);
            if (e.getDivideType() == 1) {
                ocm = ocm.multiply(new BigDecimal("0.5"));
            } else if (e.getDivideType() == 2) {
                ocm = ocm.multiply(new BigDecimal("0.4"));
            }
            e.setOrderCommission(ocm);
        });
//        if (!StrUtil.contains(roleType, "个人")) {
        if (StrUtil.contains(roleType, "个人") ||
                StrUtil.contains(userInfo.getRoleName(), "财务") || StrUtil.contains(userInfo.getRoleName(), "管理员")) {
            DepotHead dh = new DepotHead();
            dh.setAction("total");
//            long projectTotal = list.stream().filter(e -> e.getConstYear().equals(item.getConstYear()))
//                    .mapToLong(BhPrProjectInfo::getProjectCount).sum();
            Long nms = depots.stream().filter(Objects::nonNull).mapToLong(DepotHead::getNumbers).sum();
            dh.setSize1(depots.stream().filter(Objects::nonNull).map(DepotHead::getSize1).reduce(0, Integer::sum));
            dh.setSize2(depots.stream().filter(Objects::nonNull).map(DepotHead::getSize2).reduce(0, Integer::sum));
            dh.setSize3(depots.stream().filter(Objects::nonNull).map(DepotHead::getSize3).reduce(0, Integer::sum));
            dh.setSize4(depots.stream().filter(Objects::nonNull).map(DepotHead::getSize4).reduce(0, Integer::sum));
            dh.setSize5(depots.stream().filter(Objects::nonNull).map(DepotHead::getSize5).reduce(0, Integer::sum));
            dh.setSize6(depots.stream().filter(Objects::nonNull).map(DepotHead::getSize6).reduce(0, Integer::sum));
            dh.setSize7(depots.stream().filter(Objects::nonNull).map(DepotHead::getSize7).reduce(0, Integer::sum));
            dh.setSize8(depots.stream().filter(Objects::nonNull).map(DepotHead::getSize8).reduce(0, Integer::sum));
            dh.setSize9(depots.stream().filter(Objects::nonNull).map(DepotHead::getSize9).reduce(0, Integer::sum));
            dh.setSize10(depots.stream().filter(Objects::nonNull).map(DepotHead::getSize10).reduce(0, Integer::sum));
            dh.setSize11(depots.stream().filter(Objects::nonNull).map(DepotHead::getSize11).reduce(0, Integer::sum));

            dh.setNumbers(nms.intValue());
            dh.setTotalFinancialPrice(depots.stream().filter(Objects::nonNull).map(DepotHead::getTotalFinancialPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setTotalPrintingPrice(depots.stream().filter(Objects::nonNull).map(DepotHead::getTotalPrintingPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setHotStampingPaintingTotalPrice(depots.stream().filter(Objects::nonNull).map(DepotHead::getHotStampingPaintingTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setTotalStackPrice(depots.stream().filter(Objects::nonNull).map(DepotHead::getTotalStackPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setEmbroideryTotalPrice(depots.stream().filter(Objects::nonNull).map(DepotHead::getEmbroideryTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setFinancialDeposit(depots.stream().filter(Objects::nonNull).map(DepotHead::getFinancialDeposit).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setFinancialBalance(depots.stream().filter(Objects::nonNull).map(DepotHead::getFinancialBalance).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setFinancialStaff(depots.stream().filter(Objects::nonNull).map(DepotHead::getFinancialStaff).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setFinancialDeductionsFee(depots.stream().filter(Objects::nonNull).map(DepotHead::getFinancialDeductionsFee).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setFinancialRebate(depots.stream().filter(Objects::nonNull).map(DepotHead::getFinancialRebate).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setOtherExpensesInFinance(depots.stream().filter(Objects::nonNull).map(DepotHead::getOtherExpensesInFinance).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setOtherExpensesInFinance2(depots.stream().filter(Objects::nonNull).map(DepotHead::getOtherExpensesInFinance2).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setFinancialTaxAmount(depots.stream().filter(Objects::nonNull).map(DepotHead::getFinancialTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setFinancialCostAmount(depots.stream().filter(Objects::nonNull).map(DepotHead::getFinancialCostAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setLogisticsFreight(depots.stream().filter(Objects::nonNull).map(DepotHead::getLogisticsFreight).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setOrderCommission(depots.stream().filter(Objects::nonNull).map(DepotHead::getOrderCommission).reduce(BigDecimal.ZERO, BigDecimal::add));
            dh.setPrinter(" ");
            dh.setStackStaff(" ");
            dh.setIroningStaff(" ");
            dh.setStackStaff(" ");
            depots.add(dh);
        }
//        }
        return depots;
    }

    @Override
    public Long counts(Map<String, String> map) throws Exception {
        String search = map.get(Constants.SEARCH);
        User userInfo = userService.getCurrentUser();
        String isFinalPay = StringUtil.getInfo(search, "isFinalPay");
        String type = StringUtil.getInfo(search, "type");
        String salesMan = StringUtil.getInfo(search, "salesMan");
        String operTime = StringUtil.getInfo(search, "operTime");
        String roleType = StringUtil.getInfo(search, "roleType");

        List<Map<String, String>> users = Lists.newArrayList();
        String deptName = StringUtil.getInfo(search, "deptName");
        if (StrUtil.equals(deptName, "全部") || StrUtil.isBlank(deptName)) {
            users = userMapperEx.selectConditionUserList(null);
        } else if (StrUtil.contains(deptName, "销售")) {
            users = userMapperEx.selectConditionUserList(deptName);
        } else if (StrUtil.equals(deptName, "财务仓储")) {
            List<String> deptNames = Lists.newArrayList();
            deptNames.add("仓库管理员");
            deptNames.add("财务部");
            users = userMapperEx.selectUserList(deptNames);
        }
        if (CollUtil.isEmpty(users)) {
            return 0L;
        }
        List<String> userIds = users.stream().map(o -> StrUtil.toString(o.get("id"))).collect(Collectors.toList());
        String status = StringUtil.getInfo(search, "status");
        String color = StringUtil.getInfo(search, "color");
//        String linkNumber = StringUtil.getInfo(search, "linkNumber");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String customer = StringUtil.getInfo(search, "customer");
        String depotId = StringUtil.getInfo(search, "depotId");
        String creator = StringUtil.getInfo(search, "creator");
        String printer = StringUtil.getInfo(search, "printer");
        String ironingStaff = StringUtil.getInfo(search, "ironingStaff");
        String stackStaff = StringUtil.getInfo(search, "stackStaff");
        String rustStaff = StringUtil.getInfo(search, "rustStaff");
        String printingReconciliationDate = StringUtil.getInfo(search, "printingReconciliationDate");
        String stampingReconciliationDate = StringUtil.getInfo(search, "stampingReconciliationDate");
        String embroideryReconciliationDate = StringUtil.getInfo(search, "embroideryReconciliationDate");
        String logisticsStatus = StringUtil.getInfo(search, "logisticsStatus");
        LambdaQueryWrapper<DepotHead> query = new LambdaQueryWrapper<>();
        String numbers = StringUtil.getInfo(search, "number");
        if (StrUtil.isNotBlank(numbers)) {
            List<String> numberList = Splitter.on("，").trimResults().splitToList(numbers.replaceAll(",", "，"));
            query.in(DepotHead::getNumber, numberList);
        }
        query.eq(DepotHead::getDeleteFlag, 0);
        query.eq(StrUtil.isNotBlank(logisticsStatus), DepotHead::getLogisticsStatus, logisticsStatus);
        if (StrUtil.equals(isFinalPay, "是")) {
            query.apply("IFNULL(end_staff,'')='是'");
        } else if (StrUtil.equals(isFinalPay, "否")) {
            query.apply("IFNULL(end_staff,'')!='是'");
        }
        if (CollUtil.isNotEmpty(userIds)) {
            query.in(DepotHead::getCreator, userIds);
        }
        if (StrUtil.contains(userInfo.getRoleName(), "业务员") || StrUtil.contains(roleType, "个人")) {
            query.eq(DepotHead::getCreator, userInfo.getId());
        } else if (StrUtil.contains(userInfo.getRoleName(), "财务")) {
            query.isNotNull(DepotHead::getNumber);
        } else if (StrUtil.contains(userInfo.getRoleName(), "仓库")) {
            query.in(DepotHead::getStatus, 9, 4, 1);
            query.notLike(DepotHead::getNumber, "D");
        } else {
            query.eq(StrUtil.isNotBlank(creator), DepotHead::getCreator, creator);
        }
        query.eq(StrUtil.isNotBlank(salesMan), DepotHead::getSalesMan, salesMan);
        query.eq(StrUtil.isNotBlank(type), DepotHead::getType, type);
        query.eq(StrUtil.isNotBlank(color), DepotHead::getColor, color);
        query.like(StrUtil.isNotBlank(commodityNo), DepotHead::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(printer), DepotHead::getPrinter, printer);
        query.eq(StrUtil.isNotBlank(ironingStaff), DepotHead::getIroningStaff, ironingStaff);
        query.eq(StrUtil.isNotBlank(status), DepotHead::getStatus, status);
        query.like(StrUtil.isNotBlank(customer), DepotHead::getCustomer, customer);
        query.eq(StrUtil.isNotBlank(depotId), DepotHead::getId, depotId);
        if (StrUtil.isNotBlank(beginTime)) {
            query.ge(DepotHead::getOperTime, beginTime.concat(" 00:00:00"));
        }
        if (StrUtil.isNotBlank(endTime)) {
            query.le(DepotHead::getOperTime, endTime.concat(" 23:59:59"));
        }
        query.eq(StrUtil.isNotBlank(stackStaff), DepotHead::getStackStaff, stackStaff);
        query.eq(StrUtil.isNotBlank(rustStaff), DepotHead::getRustStaff, rustStaff);
        if (StrUtil.isNotBlank(operTime)) {
            query.apply("date_format(oper_time,'%Y-%m-%d')='" + operTime + "'");
        }
        if (StrUtil.isNotBlank(printingReconciliationDate)) {
            query.apply("date_format(printing_reconciliation_date,'%Y-%m-%d')='" + printingReconciliationDate + "'");
        }
        if (StrUtil.isNotBlank(stampingReconciliationDate)) {
            query.apply("date_format(stamping_reconciliation_date,'%Y-%m-%d')='" + stampingReconciliationDate + "'");
        }
        if (StrUtil.isNotBlank(embroideryReconciliationDate)) {
            query.apply("date_format(embroidery_reconciliation_date,'%Y-%m-%d')='" + embroideryReconciliationDate + "'");
        }
        return depotHeadNewService.count(query);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception {
        DepotHead dh = JSONObject.parseObject(obj.toJSONString(), DepotHead.class);
        BigDecimal rate = new BigDecimal(0);
        if (StrUtil.equals("增票11个点", dh.getBillingRate())) {
            rate = dh.getTotalFinancialPrice().multiply(new BigDecimal("0.11")).setScale(4, BigDecimal.ROUND_UP);
        } else if (StrUtil.equals("普票2个点", dh.getBillingRate())) {
            rate = dh.getTotalFinancialPrice().multiply(new BigDecimal("0.02")).setScale(4, BigDecimal.ROUND_UP);
        } else if (StrUtil.equals("普票3个点", dh.getBillingRate())) {
            rate = dh.getTotalFinancialPrice().multiply(new BigDecimal("0.03")).setScale(4, BigDecimal.ROUND_UP);
        }
        dh.setFinancialTaxAmount(rate);
        depotHeadNewService.saveOrUpdate(dh);
        return 0;
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request) throws Exception {
        DepotHead commodity = JSONObject.parseObject(obj.toJSONString(), DepotHead.class);
        BigDecimal rate = new BigDecimal(0);
        if (StrUtil.equals("增票11个点", commodity.getBillingRate())) {
            rate = commodity.getTotalFinancialPrice().multiply(new BigDecimal("0.11")).setScale(4, BigDecimal.ROUND_UP);
        } else if (StrUtil.equals("普票2个点", commodity.getBillingRate())) {
            rate = commodity.getTotalFinancialPrice().multiply(new BigDecimal("0.02")).setScale(4, BigDecimal.ROUND_UP);
        } else if (StrUtil.equals("普票3个点", commodity.getBillingRate())) {
            rate = commodity.getTotalFinancialPrice().multiply(new BigDecimal("0.03")).setScale(4, BigDecimal.ROUND_UP);
        }
        commodity.setFinancialTaxAmount(rate);
        depotHeadNewService.saveOrUpdate(commodity);
        return 0;
    }

    @Override
    public int delete(Long id, HttpServletRequest request) throws Exception {
        DepotHead dh = depotHeadNewService.getById(id);
        if (dh == null) {
            return 1;
        } else {
            dh.setDeleteFlag("1");
            depotHeadNewService.updateById(dh);
        }
        return 0;
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request) throws Exception {
        return depotHeadService.batchDeleteDepotHead(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name) throws Exception {
        return 0;
    }

}
