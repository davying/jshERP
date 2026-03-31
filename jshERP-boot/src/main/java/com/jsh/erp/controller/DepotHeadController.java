package com.jsh.erp.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entitie.custom.WarehouseManagement;
import com.jsh.erp.datasource.entities.Commodity;
import com.jsh.erp.datasource.entities.DepotHead;
import com.jsh.erp.datasource.entities.DepotHeadVo4Body;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.datasource.entities.vo.DepotHeadVO;
import com.jsh.erp.datasource.mappers.UserMapperEx;
import com.jsh.erp.datasource.mappers.custom.DepotHeadNewMapper;
import com.jsh.erp.datasource.vo.DepotHeadVo4InDetail;
import com.jsh.erp.datasource.vo.DepotHeadVo4InOutMCount;
import com.jsh.erp.datasource.vo.DepotHeadVo4List;
import com.jsh.erp.datasource.vo.DepotHeadVo4StatementAccount;
import com.jsh.erp.service.custom.CommodityService;
import com.jsh.erp.service.custom.DepotHeadNewService;
import com.jsh.erp.service.custom.WarehouseManagementService;
import com.jsh.erp.service.depot.DepotService;
import com.jsh.erp.service.depotHead.DepotHeadService;
import com.jsh.erp.service.redis.RedisService;
import com.jsh.erp.service.systemConfig.SystemConfigService;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.jsh.erp.utils.ResponseJsonUtil.returnJson;

/**
 * @author ji-sheng-hua 752*718*920
 */
@RestController
@Slf4j
@RequestMapping(value = "/depotHead")
@Api(tags = {"单据管理"})
public class DepotHeadController {

    @Resource
    private DepotHeadService depotHeadService;
    @Resource
    private DepotService depotService;
    @Resource
    private SystemConfigService systemConfigService;
    @Resource
    private RedisService redisService;
    @Autowired
    private DepotHeadNewService depotHeadNewService;
    @Resource
    private UserService userService;
    @Autowired
    private WarehouseManagementService warehouseManagementService;
    @Resource
    private CommodityService commodityService;
    @Autowired
    private UserMapperEx userMapperEx;
    @Autowired
    private DepotHeadNewMapper depotHead2Map;

    private static List<Map<String, Object>> convertKeysToCamelCase(List<Map<String, Object>> originalList) {
        return originalList.stream().map(map -> {
            Map<String, Object> convertedMap = new HashMap<>();
            map.forEach((key, value) -> {
                String camelKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.toLowerCase());
                convertedMap.put(camelKey, value);
            });
            return convertedMap;
        }).collect(Collectors.toList());
    }

    /**
     * 更新业务员订单确认状态
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/updSalesConfirmed")
    public Result updSalesConfirmed(@RequestParam("id") Long id, @RequestParam("status") Integer status) throws Exception {
        DepotHead vo = depotHeadNewService.getById(id);
        vo.setSalesConfirmed(status);
        depotHeadNewService.updateById(vo);
        return Result.success("更新成功");
    }

    /**
     * 获取订单列表
     *
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/getDepotHeadList")
    @ApiOperation(value = "")
    public Result getDepotHeadList(@RequestBody JSONObject jsonObject) throws Exception {
        String status = jsonObject.getString("status");
        String type = jsonObject.getString("type");
        LambdaQueryWrapper<DepotHead> depot = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(status)) {
            List<String> statusList = Splitter.on(",").trimResults().splitToList(status);
            depot.in(DepotHead::getStatus, statusList);
        }
        if (StrUtil.contains(type, "出库")) {
            depot.eq(DepotHead::getSaleType, "自有仓库");
            depot.ne(DepotHead::getPurchaseStatus, "5");
            depot.last(" and numbers!=0 ");
        } else if (StrUtil.contains(type, "发货")) {
            depot.ne(DepotHead::getLogisticsStatus, "2");
            depot.ne(DepotHead::getNumbers, 0);
//            depot.notExists("select a.id from jsh_warehouse_management a where a.depot_id = jsh_depot_head.number and a.type like '%发货' ");
        }
//        if (StrUtil.equals("出库", type)) {
//            depot.last(" and sale_type='自有仓库' and date_format(create_time,'%Y')=date_format(now(),'%Y')");
//        }
        List<DepotHead> res = depotHeadNewService.list(depot);
        if (CollUtil.isNotEmpty(res)) {
            return Result.success(res);
        } else {
            return Result.failed("暂无数据");
        }
    }

    /**
     * 获取订单列表
     *
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/getDepotHeadIds")
    @ApiOperation(value = "获取订单列表")
    public Result getDepotHeadIds(@RequestBody JSONObject jsonObject) throws Exception {
        String status = jsonObject.getString("status");
        String type = jsonObject.getString("type");
        LambdaQueryWrapper<DepotHead> depot = new LambdaQueryWrapper<>();
        depot.select(DepotHead::getId, DepotHead::getNumber, DepotHead::getSalesMan, DepotHead::getColor, DepotHead::getCommodityNo, DepotHead::getCommodityName);
        if (StrUtil.isNotBlank(status)) {
            List<String> statusList = Splitter.on(",").trimResults().splitToList(status);
            depot.in(DepotHead::getStatus, statusList);
        }
        if (StrUtil.contains(type, "出库")) {
            depot.eq(DepotHead::getSaleType, "自有仓库");
            depot.ne(DepotHead::getPurchaseStatus, "5");
            depot.last(" and numbers!=0 ");
        } else if (StrUtil.contains(type, "发货")) {
            depot.ne(DepotHead::getLogisticsStatus, "2");
            depot.ne(DepotHead::getNumbers, 0);
//            depot.notExists("select a.id from jsh_warehouse_management a where a.depot_id = jsh_depot_head.number and a.type like '%发货' ");
        }
//        if (StrUtil.equals("出库", type)) {
//            depot.last(" and sale_type='自有仓库' and date_format(create_time,'%Y')=date_format(now(),'%Y')");
//        }
        List<Map<String, Object>> res = depotHeadNewService.listMaps(depot);
        List<Map<String, Object>> camelRes = convertKeysToCamelCase(res);
        if (CollUtil.isNotEmpty(camelRes)) {
            return Result.success(camelRes);
        } else {
            return Result.failed("暂无数据");
        }
    }

    /**
     * 批量设置状态-审核或者反审核
     *
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping(value = "/batchSetStatus")
    @ApiOperation(value = "批量设置状态-审核或者反审核")
    public String batchSetStatus(@RequestBody JSONObject jsonObject,
                                 HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        String status = jsonObject.getString("status");
        String ids = jsonObject.getString("ids");
        int res = depotHeadService.batchSetStatus(status, ids);
        if (res > 0) {
            return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else {
            return returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }

    /**
     * 入库出库明细接口
     *
     * @param currentPage
     * @param pageSize
     * @param oId
     * @param number
     * @param materialParam
     * @param depotId
     * @param beginTime
     * @param endTime
     * @param type
     * @param request
     * @return
     */
    @GetMapping(value = "/findInOutDetail")
    @ApiOperation(value = "入库出库明细接口")
    public BaseResponseInfo findInOutDetail(@RequestParam("currentPage") Integer currentPage,
                                            @RequestParam("pageSize") Integer pageSize,
                                            @RequestParam(value = "organId", required = false) Integer oId,
                                            @RequestParam("number") String number,
                                            @RequestParam("materialParam") String materialParam,
                                            @RequestParam(value = "depotId", required = false) Long depotId,
                                            @RequestParam("beginTime") String beginTime,
                                            @RequestParam("endTime") String endTime,
                                            @RequestParam(value = "roleType", required = false) String roleType,
                                            @RequestParam("type") String type,
                                            @RequestParam(value = "creator", required = false) Long creator,
                                            @RequestParam("remark") String remark,
                                            HttpServletRequest request) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Long> depotList = new ArrayList<>();
            if (depotId != null) {
                depotList.add(depotId);
            } else {
                //未选择仓库时默认为当前用户有权限的仓库
                JSONArray depotArr = depotService.findDepotByCurrentUser();
                for (Object obj : depotArr) {
                    JSONObject object = JSONObject.parseObject(obj.toString());
                    depotList.add(object.getLong("id"));
                }
            }
            List<DepotHeadVo4InDetail> resList = new ArrayList<DepotHeadVo4InDetail>();
            String[] creatorArray = depotHeadService.getCreatorArray(roleType);
            String subType = "出库".equals(type) ? "销售" : "";
            String[] organArray = depotHeadService.getOrganArray(subType, "");
            beginTime = Tools.parseDayToTime(beginTime, BusinessConstants.DAY_FIRST_TIME);
            endTime = Tools.parseDayToTime(endTime, BusinessConstants.DAY_LAST_TIME);
            Boolean forceFlag = systemConfigService.getForceApprovalFlag();
            List<DepotHeadVo4InDetail> list = depotHeadService.findInOutDetail(beginTime, endTime, type, creatorArray, organArray, forceFlag,
                    StringUtil.toNull(materialParam), depotList, oId, StringUtil.toNull(number), creator, remark, (currentPage - 1) * pageSize, pageSize);
            int total = depotHeadService.findInOutDetailCount(beginTime, endTime, type, creatorArray, organArray, forceFlag,
                    StringUtil.toNull(materialParam), depotList, oId, StringUtil.toNull(number), creator, remark);
            map.put("total", total);
            //存放数据json数组
            if (null != list) {
                for (DepotHeadVo4InDetail dhd : list) {
                    resList.add(dhd);
                }
            }
            map.put("rows", resList);
            res.code = 200;
            res.data = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 入库出库统计接口
     *
     * @param currentPage
     * @param pageSize
     * @param oId
     * @param materialParam
     * @param depotId
     * @param beginTime
     * @param endTime
     * @param type
     * @param request
     * @return
     */
    @GetMapping(value = "/findInOutMaterialCount")
    @ApiOperation(value = "入库出库统计接口")
    public BaseResponseInfo findInOutMaterialCount(@RequestParam("currentPage") Integer currentPage,
                                                   @RequestParam("pageSize") Integer pageSize,
                                                   @RequestParam(value = "organId", required = false) Integer oId,
                                                   @RequestParam("materialParam") String materialParam,
                                                   @RequestParam(value = "depotId", required = false) Long depotId,
                                                   @RequestParam("beginTime") String beginTime,
                                                   @RequestParam("endTime") String endTime,
                                                   @RequestParam("type") String type,
                                                   @RequestParam(value = "roleType", required = false) String roleType,
                                                   HttpServletRequest request) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Long> depotList = new ArrayList<>();
            if (depotId != null) {
                depotList.add(depotId);
            } else {
                //未选择仓库时默认为当前用户有权限的仓库
                JSONArray depotArr = depotService.findDepotByCurrentUser();
                for (Object obj : depotArr) {
                    JSONObject object = JSONObject.parseObject(obj.toString());
                    depotList.add(object.getLong("id"));
                }
            }
            beginTime = Tools.parseDayToTime(beginTime, BusinessConstants.DAY_FIRST_TIME);
            endTime = Tools.parseDayToTime(endTime, BusinessConstants.DAY_LAST_TIME);
            Boolean forceFlag = systemConfigService.getForceApprovalFlag();
            List<DepotHeadVo4InOutMCount> list = depotHeadService.findInOutMaterialCount(beginTime, endTime, type, forceFlag, StringUtil.toNull(materialParam),
                    depotList, oId, roleType, (currentPage - 1) * pageSize, pageSize);
            int total = depotHeadService.findInOutMaterialCountTotal(beginTime, endTime, type, forceFlag, StringUtil.toNull(materialParam),
                    depotList, oId, roleType);
            map.put("total", total);
            map.put("rows", list);
            res.code = 200;
            res.data = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 调拨明细统计
     *
     * @param currentPage
     * @param pageSize
     * @param number
     * @param materialParam
     * @param depotIdF      调出仓库
     * @param depotId       调入仓库
     * @param beginTime
     * @param endTime
     * @param subType
     * @param request
     * @return
     */
    @GetMapping(value = "/findAllocationDetail")
    @ApiOperation(value = "调拨明细统计")
    public BaseResponseInfo findallocationDetail(@RequestParam("currentPage") Integer currentPage,
                                                 @RequestParam("pageSize") Integer pageSize,
                                                 @RequestParam("number") String number,
                                                 @RequestParam("materialParam") String materialParam,
                                                 @RequestParam(value = "depotId", required = false) Long depotId,
                                                 @RequestParam(value = "depotIdF", required = false) Long depotIdF,
                                                 @RequestParam("beginTime") String beginTime,
                                                 @RequestParam("endTime") String endTime,
                                                 @RequestParam("subType") String subType,
                                                 @RequestParam(value = "roleType", required = false) String roleType,
                                                 @RequestParam("remark") String remark,
                                                 HttpServletRequest request) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Long> depotList = new ArrayList<>();
            List<Long> depotFList = new ArrayList<>();
            if (depotId != null) {
                depotList.add(depotId);
            } else {
                //未选择仓库时默认为当前用户有权限的仓库
                JSONArray depotArr = depotService.findDepotByCurrentUser();
                for (Object obj : depotArr) {
                    JSONObject object = JSONObject.parseObject(obj.toString());
                    depotList.add(object.getLong("id"));
                }
            }
            if (depotIdF != null) {
                depotFList.add(depotIdF);
            } else {
                //未选择仓库时默认为当前用户有权限的仓库
                JSONArray depotArr = depotService.findDepotByCurrentUser();
                for (Object obj : depotArr) {
                    JSONObject object = JSONObject.parseObject(obj.toString());
                    depotFList.add(object.getLong("id"));
                }
            }
            String[] creatorArray = depotHeadService.getCreatorArray(roleType);
            beginTime = Tools.parseDayToTime(beginTime, BusinessConstants.DAY_FIRST_TIME);
            endTime = Tools.parseDayToTime(endTime, BusinessConstants.DAY_LAST_TIME);
            Boolean forceFlag = systemConfigService.getForceApprovalFlag();
            List<DepotHeadVo4InDetail> list = depotHeadService.findAllocationDetail(beginTime, endTime, subType, StringUtil.toNull(number),
                    creatorArray, forceFlag, StringUtil.toNull(materialParam), depotList, depotFList, remark, (currentPage - 1) * pageSize, pageSize);
            int total = depotHeadService.findAllocationDetailCount(beginTime, endTime, subType, StringUtil.toNull(number),
                    creatorArray, forceFlag, StringUtil.toNull(materialParam), depotList, depotFList, remark);
            map.put("rows", list);
            map.put("total", total);
            res.code = 200;
            res.data = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 对账单接口
     *
     * @param currentPage
     * @param pageSize
     * @param beginTime
     * @param endTime
     * @param organId
     * @param supplierType
     * @param request
     * @return
     */
    @GetMapping(value = "/getStatementAccount")
    @ApiOperation(value = "对账单接口")
    public BaseResponseInfo getStatementAccount(@RequestParam("currentPage") Integer currentPage,
                                                @RequestParam("pageSize") Integer pageSize,
                                                @RequestParam("beginTime") String beginTime,
                                                @RequestParam("endTime") String endTime,
                                                @RequestParam(value = "organId", required = false) Integer organId,
                                                @RequestParam("supplierType") String supplierType,
                                                HttpServletRequest request) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String type = "";
            String subType = "";
            String typeBack = "";
            String subTypeBack = "";
            String billType = "";
            if (("供应商").equals(supplierType)) {
                type = "入库";
                subType = "采购";
                typeBack = "出库";
                subTypeBack = "采购退货";
                billType = "付款";
            } else if (("客户").equals(supplierType)) {
                type = "出库";
                subType = "销售";
                typeBack = "入库";
                subTypeBack = "销售退货";
                billType = "收款";
            }
            String[] organArray = depotHeadService.getOrganArray(subType, "");
            beginTime = Tools.parseDayToTime(beginTime, BusinessConstants.DAY_FIRST_TIME);
            endTime = Tools.parseDayToTime(endTime, BusinessConstants.DAY_LAST_TIME);
            List<DepotHeadVo4StatementAccount> list = depotHeadService.getStatementAccount(beginTime, endTime, organId, organArray,
                    supplierType, type, subType, typeBack, subTypeBack, billType, (currentPage - 1) * pageSize, pageSize);
            int total = depotHeadService.getStatementAccountCount(beginTime, endTime, organId, organArray,
                    supplierType, type, subType, typeBack, subTypeBack, billType);
            for (DepotHeadVo4StatementAccount item : list) {
                //期初 = 起始期初金额+上期欠款金额-上期退货的欠款金额-上期收付款
                BigDecimal preNeed = item.getBeginNeed().add(item.getPreDebtMoney()).subtract(item.getPreReturnDebtMoney()).subtract(item.getPreBackMoney());
                item.setPreNeed(preNeed);
                //实际欠款 = 本期欠款-本期退货的欠款金额
                BigDecimal realDebtMoney = item.getDebtMoney().subtract(item.getReturnDebtMoney());
                item.setDebtMoney(realDebtMoney);
                //期末 = 期初+实际欠款-本期收款
                BigDecimal allNeedGet = preNeed.add(realDebtMoney).subtract(item.getBackMoney());
                item.setAllNeed(allNeedGet);
            }
            map.put("rows", list);
            map.put("total", total);
            List<DepotHeadVo4StatementAccount> totalPayList = depotHeadService.getStatementAccountTotalPay(beginTime, endTime, organId, organArray,
                    supplierType, type, subType, typeBack, subTypeBack, billType);
            if (totalPayList.size() > 0) {
                DepotHeadVo4StatementAccount totalPayItem = totalPayList.get(0);
                BigDecimal firstMoney = BigDecimal.ZERO;
                BigDecimal lastMoney = BigDecimal.ZERO;
                if (totalPayItem != null) {
                    //期初 = 起始期初金额+上期欠款金额-上期退货的欠款金额-上期收付款
                    firstMoney = totalPayItem.getBeginNeed().add(totalPayItem.getPreDebtMoney()).subtract(totalPayItem.getPreReturnDebtMoney()).subtract(totalPayItem.getPreBackMoney());
                    //期末 = 期初+本期欠款-本期退货的欠款金额-本期收款
                    lastMoney = firstMoney.add(totalPayItem.getDebtMoney()).subtract(totalPayItem.getReturnDebtMoney()).subtract(totalPayItem.getBackMoney());
                }
                map.put("firstMoney", firstMoney); //期初
                map.put("lastMoney", lastMoney);  //期末
            }
            res.code = 200;
            res.data = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据编号查询单据信息
     *
     * @param number
     * @return
     */
    @GetMapping(value = "/getDetailByNumberNew")
    @ApiOperation(value = "根据编号查询单据信息")
    public Result getDetailByNumberNew(@RequestParam("number") String number) throws Exception {
        List<DepotHead> list = depotHeadNewService.list(new LambdaQueryWrapper<DepotHead>().eq(DepotHead::getNumber, number));
        if (CollUtil.isNotEmpty(list)) {
            DepotHead dh = list.get(0);
            // 订单物流费
            QueryWrapper<WarehouseManagement> queryWarehouseWrapper = new QueryWrapper<>();
            queryWarehouseWrapper.select("IFNULL(sum(logistics_freight),0) as logisticsFreight");
            queryWarehouseWrapper.eq("depot_id", dh.getNumber());
            queryWarehouseWrapper.eq("delete_flag", 0);
            queryWarehouseWrapper.like("type", "发货");
            Map<String, Object> orderExpressFee = warehouseManagementService.getMap(queryWarehouseWrapper);
            BigDecimal logisticsFreight = (BigDecimal) orderExpressFee.get("logisticsFreight");
            dh.setLogisticsFreight(logisticsFreight);
            return Result.success(dh);
        } else {
            return Result.failed("暂无数据");
        }
    }

    /**
     * 根据编号查询单据信息 老接口
     *
     * @param number
     * @param request
     * @return
     */
    @GetMapping(value = "/getDetailByNumber1")
    @ApiOperation(value = "根据编号查询单据信息")
    public BaseResponseInfo getDetailByNumber1(@RequestParam("number") String number,
                                               HttpServletRequest request) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        DepotHeadVo4List dhl = new DepotHeadVo4List();
        try {
            List<DepotHeadVo4List> list = depotHeadService.getDetailByNumber(number, request);
            if (list.size() > 0) {
                dhl = list.get(0);
            }
            res.code = 200;
            res.data = dhl;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据编号查询单据信息
     *
     * @param search
     * @param request
     * @return
     */
    @GetMapping(value = "/getDetailByNumber")
    @ApiOperation(value = "根据编号查询单据信息")
    public BaseResponseInfo getDetailByNumber(@RequestParam(value = "number", required = false) String number,
                                              HttpServletRequest request) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        DepotHead dhl = new DepotHead();
        try {
            List<DepotHead> list = Lists.newArrayList();
            list = depotHeadNewService.list(new LambdaQueryWrapper<DepotHead>().eq(DepotHead::getNumber, number).or().eq(DepotHead::getId, number));
            if (list.size() > 0) {
                dhl = list.get(0);
                dhl.setCommodityNoStr(dhl.getCommodityNo().concat("(").concat(dhl.getCommodityName()).concat(")"));
            }
            res.code = 200;
            res.data = dhl;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据原单号查询关联的单据列表
     *
     * @param number
     * @param request
     * @return
     */
    @GetMapping(value = "/getBillListByLinkNumber")
    @ApiOperation(value = "根据原单号查询关联的单据列表")
    public BaseResponseInfo getBillListByLinkNumber(@RequestParam("number") String number,
                                                    HttpServletRequest request) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        DepotHead dh = new DepotHead();
        try {
            List<DepotHead> list = depotHeadService.getBillListByLinkNumber(number);
            res.code = 200;
            res.data = list;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 新增单据主表及单据子表信息
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/addDepotHeadAndDetail")
    @ApiOperation(value = "新增单据主表及单据子表信息")
    public Object addDepotHeadAndDetail(@RequestBody DepotHeadVo4Body body, HttpServletRequest request) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        String beanJson = body.getInfo();
        String rows = body.getRows();
        depotHeadService.addDepotHeadAndDetail(beanJson, rows, request);
        return result;
    }

    /**
     * 新增单据主表及单据子表信息
     *
     * @param body
     * @return
     * @throws Exception
     * @author zdw
     */
    @PostMapping(value = "/addDepotHead")
    @ApiOperation(value = "新增单据主表及单据子表信息")
    public Object addDepotHead(@RequestBody DepotHead body) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        boolean increment = false;
        User userInfo = userService.getCurrentUser();
        if (body.getId() == null) {
            body.setCreateTime(DateUtil.date());
            body.setSalesMan(userInfo.getUsername());
            body.setCreator(userInfo.getId());
            body.setAccountId(userInfo.getId());
            body.setDivideType(userInfo.getDivideType());
        } else {
            DepotHead dh = depotHeadNewService.getById(body.getId());
            if (StrUtil.isNotBlank(dh.getNumber())) {
                increment = true;
            }
        }
        if (StrUtil.equals(body.getStatus(), "9") && !increment) {
            String number = depotHeadNewService.buildNumberForYsld(body.getOperTime());
            body.setDefaultNumber(number);
            body.setNumber(number);
        }
        if (body.getSize1() == null) {
            body.setSize1(0);
        }
        if (body.getSize2() == null) {
            body.setSize2(0);
        }
        if (body.getSize3() == null) {
            body.setSize3(0);
        }
        if (body.getSize4() == null) {
            body.setSize4(0);
        }
        if (body.getSize5() == null) {
            body.setSize5(0);
        }
        if (body.getSize6() == null) {
            body.setSize6(0);
        }
        if (body.getSize7() == null) {
            body.setSize7(0);
        }
        if (body.getSize8() == null) {
            body.setSize8(0);
        }
        if (body.getSize9() == null) {
            body.setSize9(0);
        }
        if (body.getSize10() == null) {
            body.setSize10(0);
        }
        if (body.getSize11() == null) {
            body.setSize11(0);
        }
        if (StrUtil.isNotBlank(body.getCommodityNo())) {
            Commodity co = commodityService.getOne(new LambdaQueryWrapper<Commodity>().eq(Commodity::getCommodityNo, body.getCommodityNo()));
            body.setCommodityName(co.getName());
        }
        depotHeadNewService.saveOrUpdate(body);
        return result;
    }

    /**
     * 更新单据主表及单据子表信息
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/updateDepotHeadAndDetail")
    @ApiOperation(value = "更新单据主表及单据子表信息")
    public Object updateDepotHeadAndDetail(@RequestBody DepotHeadVo4Body body, HttpServletRequest request) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        String beanJson = body.getInfo();
        String rows = body.getRows();
        depotHeadService.updateDepotHeadAndDetail(beanJson, rows, request);
        return result;
    }

    /**
     * 统计今日采购额、昨日采购额、本月采购额、今年采购额|销售额|零售额
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/getBuyAndSaleStatistics")
    @ApiOperation(value = "统计今日采购额、昨日采购额、本月采购额、今年采购额|销售额|零售额")
    public BaseResponseInfo getBuyAndSaleStatistics(@RequestParam(value = "roleType", required = false) String roleType,
                                                    @RequestParam(value = "deptName", required = false) String deptName,
                                                    HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            String today = Tools.getNow() + BusinessConstants.DAY_FIRST_TIME;
            String monthFirstDay = Tools.firstDayOfMonth(Tools.getCurrentMonth()) + BusinessConstants.DAY_FIRST_TIME;
            String yesterdayBegin = Tools.getYesterday() + BusinessConstants.DAY_FIRST_TIME;
            String yesterdayEnd = Tools.getYesterday() + BusinessConstants.DAY_LAST_TIME;
            String yearBegin = Tools.getYearBegin() + BusinessConstants.DAY_FIRST_TIME;
            String yearEnd = Tools.getYearEnd() + BusinessConstants.DAY_LAST_TIME;
            Map<String, Object> map = depotHeadService.getBuyAndSaleStatistics(today, monthFirstDay,
                    yesterdayBegin, yesterdayEnd, yearBegin, yearEnd, roleType, request, deptName);
            res.code = 200;
            res.data = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据当前用户获取操作员数组，用于控制当前用户的数据权限，限制可以看到的单据范围
     * 注意：该接口提供给部分插件使用，勿删
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/getCreatorByCurrentUser")
    @ApiOperation(value = "根据当前用户获取操作员数组")
    public BaseResponseInfo getCreatorByRoleType(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String creator = "";
            String roleType = redisService.getObjectFromSessionByKey(request, "roleType").toString();
            if (StringUtil.isNotEmpty(roleType)) {
                creator = depotHeadService.getCreatorByRoleType(roleType);
            }
            res.code = 200;
            res.data = creator;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 查询存在欠款的单据
     *
     * @param search
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/debtList")
    @ApiOperation(value = "查询存在欠款的单据")
    public String debtList(@RequestParam(value = Constants.SEARCH, required = false) String search,
                           @RequestParam("currentPage") Integer currentPage,
                           @RequestParam("pageSize") Integer pageSize,
                           HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        String organIdStr = StringUtil.getInfo(search, "organId");
        Long organId = Long.parseLong(organIdStr);
        String materialParam = StringUtil.getInfo(search, "materialParam");
        String number = StringUtil.getInfo(search, "number");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        String roleType = StringUtil.getInfo(search, "roleType");
        String status = StringUtil.getInfo(search, "status");
        List<DepotHeadVo4List> list = depotHeadService.debtList(organId, materialParam, number, beginTime, endTime, roleType,
                status, (currentPage - 1) * pageSize, pageSize);
        int total = depotHeadService.debtListCount(organId, materialParam, number, beginTime, endTime, roleType, status);
        if (list != null) {
            objectMap.put("rows", list);
            objectMap.put("total", total);
            return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else {
            objectMap.put("rows", new ArrayList<>());
            objectMap.put("total", 0);
            return returnJson(objectMap, "查找不到数据", ErpInfo.OK.code);
        }
    }

    /**
     * 导出excel
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/exportDebtHeadList")
    @ApiOperation(value = "导出销售订单excel")
    public void exportDebtHeadList(@RequestParam(value = "salesMan", required = false) String salesMan,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "roleType", required = false) String roleType,
                                   @RequestParam(value = "subType", required = false) String subType,
                                   @RequestParam(value = "status", required = false) String status,
                                   @RequestParam(value = "printer", required = false) String printer,
                                   @RequestParam(value = "color", required = false) String color,
                                   @RequestParam(value = "printingReconciliationDate", required = false) String printingReconciliationDate,
                                   @RequestParam(value = "ironingStaff", required = false) String ironingStaff,
                                   @RequestParam(value = "stackStaff", required = false) String stackStaff,
                                   @RequestParam(value = "stampingReconciliationDate", required = false) String stampingReconciliationDate,
                                   @RequestParam(value = "rustStaff", required = false) String rustStaff,
                                   @RequestParam(value = "creator", required = false) String creator,
                                   @RequestParam(value = "operTime", required = false) String operTime,
                                   @RequestParam(value = "number", required = false) String number,
                                   @RequestParam(value = "isFinalPay", required = false) String isFinalPay,
                                   @RequestParam(value = "commodityNo", required = false) String commodityNo,
                                   @RequestParam(value = "customer", required = false) String customer,
                                   @RequestParam(value = "logisticsStatus", required = false) String logisticsStatus,
                                   @RequestParam(value = "deptName", required = false) String deptName,
                                   @RequestParam(value = "ids", required = false) String ids,
                                   @RequestParam(value = "beginTime", required = false) String beginTime,
                                   @RequestParam(value = "endTime", required = false) String endTime,
                                   @RequestParam(value = "embroideryReconciliationDate", required = false) String embroideryReconciliationDate,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        User userInfo = userService.getCurrentUser();
        LambdaQueryWrapper<DepotHead> query = new LambdaQueryWrapper<>();
//        query.select(DepotHead::getId);
        List<Map<String, String>> users = Lists.newArrayList();
        if (StrUtil.equals(deptName, "全部") || StrUtil.isBlank(deptName)) {
            users = userMapperEx.selectConditionUserList(null);
        } else if (StrUtil.contains(deptName, "销售")) {
            users = userMapperEx.selectConditionUserList(deptName);
        }
        List<String> userIds = users.stream().map(o -> StrUtil.toString(o.get("id"))).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(userIds)) {
            query.in(DepotHead::getCreator, userIds);
        }

        if (StrUtil.equals(isFinalPay, "是")) {
            query.apply("IFNULL(end_staff,'')='是'");
        } else if (StrUtil.equals(isFinalPay, "否")) {
            query.apply("IFNULL(end_staff,'')!='是'");
        }
        if (StrUtil.contains(userInfo.getRoleName(), "业务员") || StrUtil.equals("个人数据", roleType)) {
            query.eq(DepotHead::getCreator, userInfo.getId());
        } else if (StrUtil.contains(userInfo.getRoleName(), "财务")) {
            query.isNotNull(DepotHead::getNumber);
        } else if (StrUtil.contains(userInfo.getRoleName(), "仓库")) {
            query.in(DepotHead::getStatus, 9, 4, 1);
        } else {
            query.eq(StrUtil.isNotBlank(creator), DepotHead::getCreator, creator);
        }
        if (StrUtil.isNotBlank(ids)) {
            List<String> splitToList = Splitter.on(",").trimResults().splitToList(ids);
            query.in(splitToList != null, DepotHead::getId, splitToList);
        }
        query.like(StrUtil.isNotBlank(salesMan), DepotHead::getSalesMan, salesMan);
        query.eq(StrUtil.isNotBlank(type), DepotHead::getType, type);
        query.eq(StrUtil.isNotBlank(logisticsStatus), DepotHead::getLogisticsStatus, logisticsStatus);
        query.eq(StrUtil.isNotBlank(subType), DepotHead::getSubType, subType);
        query.eq(StrUtil.isNotBlank(color), DepotHead::getColor, color);
        query.eq(StrUtil.isNotBlank(status), DepotHead::getStatus, status);
        query.eq(StrUtil.isNotBlank(commodityNo), DepotHead::getCommodityNo, commodityNo);
        if (StrUtil.isNotBlank(number)) {
            List<String> numberList = Splitter.on("，").trimResults().splitToList(number.replaceAll(",", "，"));
            query.in(DepotHead::getNumber, numberList);
        }
        query.eq(StrUtil.isNotBlank(printer), DepotHead::getPrinter, printer);
        query.eq(StrUtil.isNotBlank(ironingStaff), DepotHead::getIroningStaff, ironingStaff);
        query.like(StrUtil.isNotBlank(customer), DepotHead::getCustomer, customer);
//        query.eq(StrUtil.isNotBlank(depotId), DepotHead::getId, depotId);
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
        query.orderByAsc(DepotHead::getNumber, DepotHead::getId);
        query.last("limit 20000");

        List<DepotHead> depots = depotHeadNewService.mixList(query);
        log.error("11111: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset(); // 重置计时器
        stopwatch.start(); // 重新开始计时

        // 分片大小
        int batchSize = 500;
        processDepotsInParallel2(depots, batchSize);
        log.error("22222: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset(); // 重置计时器
        stopwatch.start(); // 重新开始计时
        if (StrUtil.contains(userInfo.getRoleName(), "业务员") || StrUtil.contains(userInfo.getRoleName(), "财务") || StrUtil.contains(userInfo.getRoleName(), "管理员")) {
            DepotHead dh = new DepotHead();
            dh.setSaleType("合计：");
//            long projectTotal = list.stream().filter(e -> e.getConstYear().equals(item.getConstYear()))
//                    .mapToLong(BhPrProjectInfo::getProjectCount).sum();
            Long size1 = depots.stream().filter(e -> e.getSize1() != null).mapToLong(DepotHead::getSize1).sum();
            dh.setSize1(size1.intValue());
            Long size2 = depots.stream().filter(e -> e.getSize2() != null).mapToLong(DepotHead::getSize2).sum();
            dh.setSize2(size2.intValue());
            Long size3 = depots.stream().filter(e -> e.getSize3() != null).mapToLong(DepotHead::getSize3).sum();
            dh.setSize3(size3.intValue());
            Long size4 = depots.stream().filter(e -> e.getSize4() != null).mapToLong(DepotHead::getSize4).sum();
            dh.setSize4(size4.intValue());
            Long size5 = depots.stream().filter(e -> e.getSize5() != null).mapToLong(DepotHead::getSize5).sum();
            dh.setSize5(size5.intValue());
            Long size6 = depots.stream().filter(e -> e.getSize6() != null).mapToLong(DepotHead::getSize6).sum();
            dh.setSize6(size6.intValue());
            Long size7 = depots.stream().filter(e -> e.getSize7() != null).mapToLong(DepotHead::getSize7).sum();
            dh.setSize7(size7.intValue());
            Long size8 = depots.stream().filter(e -> e.getSize8() != null).mapToLong(DepotHead::getSize8).sum();
            dh.setSize8(size8.intValue());
            Long size9 = depots.stream().filter(e -> e.getSize9() != null).mapToLong(DepotHead::getSize9).sum();
            dh.setSize9(size9.intValue());
            Long size10 = depots.stream().filter(e -> e.getSize10() != null).mapToLong(DepotHead::getSize10).sum();
            dh.setSize10(size10.intValue());
            Long size11 = depots.stream().filter(e -> e.getSize11() != null).mapToLong(DepotHead::getSize11).sum();
            dh.setSize11(size11.intValue());
            Long nms = depots.stream().filter(Objects::nonNull).mapToLong(DepotHead::getNumbers).sum();
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
        log.error("33333: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset(); // 重置计时器
        stopwatch.start(); // 重新开始计时
        //会员
        String[] names = {
                "销售类型",
                "销售订单",
                "下单时间",
                "业务员",
                "客户",
                "颜色",
                "货品名称",
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
                "单位",
                "下单备注",
                "运费付款方",
                "地址",
                "电话",
                "联系人",
                "仓库备注",
                "印刷单价",
                "印刷总价",
                "印刷人员",
                "印刷对帐日期",
                "烫画人员",
                "烫画单价",
                "烫画总价",
                "叠单价",
                "叠总价",
                "叠人员",
                "烫叠对帐日期",
                "刺锈单价",
                "刺绣总价",
                "刺绣人员",
                "刺绣对账日期",
                "刺绣备注",
                "销售单价",
                "销售总价",
                "订单定金",
                "定金到账日",
                "定金到账方式",
                "应收尾款",
                "实收尾款",
                "财务备注",
                "扣款、费用",
                "尾款到账日期",
                "尾款到账方式",
                "回扣",
                "回扣备注",
                "其他支出费用",
                "其他支出费用2",
                "其他支出备注",
                "是否含税",
                "开票抬头",
                "LOGO制作",
                "开票税种",
                "税额",
                "成本单价",
                "成本金额",
                "订单物流费",
                "订单提成",
                "尾款是否结清"
        };
        String title = "销售订单";
        List<String[]> objects = new ArrayList<String[]>();
        if (null != depots) {
            for (DepotHead s : depots) {
                String[] objs = new String[68];
                objs[0] = s.getSaleType();
                objs[1] = s.getNumber();
                objs[2] = DateUtil.format(s.getOperTime(), "yyyy-MM-dd");
                objs[3] = s.getSalesMan();
                objs[4] = s.getCustomer();
                objs[5] = s.getColor();
                objs[6] = StrUtil.isNotBlank(s.getCommodityName()) ? s.getCommodityName().concat("(").concat(s.getCommodityNo()).concat(")") : "";
                objs[7] = s.getSize1() == null || s.getSize1() == 0 ? "" : StrUtil.toString(s.getSize1());
                objs[8] = s.getSize2() == null || s.getSize2() == 0 ? "" : StrUtil.toString(s.getSize2());
                objs[9] = s.getSize3() == null || s.getSize3() == 0 ? "" : StrUtil.toString(s.getSize3());
                objs[10] = s.getSize4() == null || s.getSize4() == 0 ? "" : StrUtil.toString(s.getSize4());
                objs[11] = s.getSize5() == null || s.getSize5() == 0 ? "" : StrUtil.toString(s.getSize5());
                objs[12] = s.getSize6() == null || s.getSize6() == 0 ? "" : StrUtil.toString(s.getSize6());
                objs[13] = s.getSize7() == null || s.getSize7() == 0 ? "" : StrUtil.toString(s.getSize7());
                objs[14] = s.getSize8() == null || s.getSize8() == 0 ? "" : StrUtil.toString(s.getSize8());
                objs[15] = s.getSize9() == null || s.getSize9() == 0 ? "" : StrUtil.toString(s.getSize9());
                objs[16] = s.getSize10() == null || s.getSize10() == 0 ? "" : StrUtil.toString(s.getSize10());
                objs[17] = s.getSize11() == null || s.getSize11() == 0 ? "" : StrUtil.toString(s.getSize11());
                objs[18] = StrUtil.toString(s.getNumbers());
                objs[19] = s.getUnit();
                objs[20] = s.getOrderRemark();
                objs[21] = s.getFreightPayer();
                objs[22] = s.getAddress();
                objs[23] = s.getTelephone();
                objs[24] = s.getContactPerson();
                objs[25] = s.getWarehouseRemark();
                objs[26] = s.getPrintingUnitPrice() == null ? "" : StrUtil.toString(s.getPrintingUnitPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[27] = StrUtil.toString(s.getTotalPrintingPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[28] = s.getPrinter();
                objs[29] = DateUtil.format(s.getPrintingReconciliationDate(), "yyyy-MM-dd");
                objs[30] = s.getIroningStaff();
                objs[31] = s.getHotPaintingUnitPrice() == null ? "" : StrUtil.toString(s.getHotPaintingUnitPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[32] = StrUtil.toString(s.getHotStampingPaintingTotalPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[33] = s.getStackedUnitPrice() == null ? "" : StrUtil.toString(s.getStackedUnitPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[34] = StrUtil.toString(s.getTotalStackPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[35] = s.getStackStaff();
                objs[36] = DateUtil.format(s.getStampingReconciliationDate(), "yyyy-MM-dd");
                objs[37] = s.getRustUnitPrice() == null ? "" : StrUtil.toString(s.getRustUnitPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[38] = StrUtil.toString(s.getEmbroideryTotalPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[39] = s.getRustStaff();
                objs[40] = DateUtil.format(s.getEmbroideryReconciliationDate(), "yyyy-MM-dd");
                objs[41] = s.getEmbroideryRemark();
                objs[42] = s.getFinancialUnitPrice() == null ? "" : StrUtil.toString(s.getFinancialUnitPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[43] = StrUtil.toString(s.getTotalFinancialPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[44] = StrUtil.toString(s.getFinancialDeposit().setScale(4, BigDecimal.ROUND_UP));
                objs[45] = DateUtil.format(s.getFinancialDepositArrivalDate(), "yyyy-MM-dd");
                objs[46] = s.getFinancialDepositToAccountMethod();
                objs[47] = StrUtil.toString(s.getFinancialBalance().setScale(4, BigDecimal.ROUND_UP));
                objs[48] = StrUtil.toString(s.getFinancialStaff().setScale(4, BigDecimal.ROUND_UP));
                objs[49] = s.getFinancialRemarks();
                objs[50] = StrUtil.toString(s.getFinancialDeductionsFee().setScale(4, BigDecimal.ROUND_UP));
                objs[51] = DateUtil.format(s.getFinancialFinalPaymentDate(), "yyyy-MM-dd");
                objs[52] = s.getFinancialFinalPaymentToAccountMethod();
                objs[53] = StrUtil.toString(s.getFinancialRebate().setScale(4, BigDecimal.ROUND_UP));
                objs[54] = s.getFinancialRebateRemarks();
                objs[55] = StrUtil.toString(s.getOtherExpensesInFinance().setScale(4, BigDecimal.ROUND_UP));
                objs[56] = StrUtil.toString(s.getOtherExpensesInFinance2().setScale(4, BigDecimal.ROUND_UP));
                objs[57] = s.getFinancialOtherExpenditureRemarks();
                objs[58] = s.getDoesFinancialAffairsIncludeTax();
                objs[59] = s.getFinancialInvoicePayable();
                objs[60] = s.getLogoMark();
                objs[61] = s.getBillingRate();
                objs[62] = StrUtil.toString(s.getFinancialTaxAmount().setScale(4, BigDecimal.ROUND_UP));
                objs[63] = s.getFinancialCostUnitPrice() == null ? "" : StrUtil.toString(s.getFinancialCostUnitPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[64] = StrUtil.toString(s.getFinancialCostAmount().setScale(4, BigDecimal.ROUND_UP));
                objs[65] = s.getLogisticsFreight() == null ? "0" : StrUtil.toString(s.getLogisticsFreight().setScale(4, BigDecimal.ROUND_UP));
                objs[66] = StrUtil.toString(s.getOrderCommission().setScale(4, BigDecimal.ROUND_UP));
                objs[67] = StrUtil.isBlank(s.getEndStaff()) ? "否" : s.getEndStaff();
                objects.add(objs);
            }
        }
        File file = ExcelUtils.exportObjectsWithoutTitle(title, "", names, title, objects);
        log.error("44444: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset(); // 重置计时器
        stopwatch.start(); // 重新开始计时
        ExcelUtils.downloadExcel(file, file.getName(), response);
        log.error("55555: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.stop();
    }

    private void processDepotsInParallel2(List<DepotHead> depots, int batchSize) {
        // 1. 定义线程池
        ExecutorService executor = Executors.newFixedThreadPool(10); // 根据实际需求调整线程数
        List<Future<Void>> futures = new ArrayList<>();

        // 2. 分片并提交任务
        List<List<DepotHead>> partitions = Lists.partition(depots, batchSize);
        for (List<DepotHead> batch : partitions) {
            Future<Void> future = executor.submit(() -> {
                batch.forEach(e -> {

                    // 计算税率
                    BigDecimal rate = null;
                    if (e.getFinancialTaxAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        if (StrUtil.equals("增票11个点", e.getBillingRate())) {
                            rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.11"));
                        } else if (StrUtil.equals("普票2个点", e.getBillingRate())) {
                            rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.02"));
                        } else if (StrUtil.equals("普票3个点", e.getBillingRate())) {
                            rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.03"));
                        }
                        e.setFinancialTaxAmount(rate == null ? BigDecimal.ZERO : rate);
                    } else {
                        rate = e.getFinancialTaxAmount();
                    }

                    // 物流费
                    e.setLogisticsFreight(e.getLogisticsFreightNew());

                    // 计算订单提成
                    BigDecimal ocm = (e.getTotalFinancialPrice()
                            .subtract(rate != null ? rate : BigDecimal.ZERO)
                            .subtract(e.getTotalPrintingPrice())
                            .subtract(e.getHotStampingPaintingTotalPrice())
                            .subtract(e.getTotalStackPrice())
                            .subtract(e.getEmbroideryTotalPrice())
                            .subtract(e.getFinancialDeductionsFee())
                            .subtract(e.getFinancialRebate())
                            .subtract(e.getFinancialCostAmount())
                            .subtract(e.getOtherExpensesInFinance())
                            .subtract(e.getOtherExpensesInFinance2())
                            .subtract(e.getLogisticsFreight()))
                            .setScale(4, BigDecimal.ROUND_UP);

                    // 2026年之前：保持原有逻辑（按 divideType 计算）
                    if (e.getDivideType() == 1) {
                        ocm = ocm.multiply(new BigDecimal("0.5"));
                    } else if (e.getDivideType() == 2) {
                        ocm = ocm.multiply(new BigDecimal("0.4"));
                    }
                    e.setOrderCommission(ocm);

                    // 设置状态名称
                    if (StrUtil.equals(e.getStatus(), "0")) {
                        e.setStatusName("暂存");
                    } else if (StrUtil.equals(e.getStatus(), "1")) {
                        e.setStatusName("财务已归档");
                    } else if (StrUtil.equals(e.getStatus(), "9")) {
                        e.setStatusName("业务员已提交");
                    } else if (StrUtil.equals(e.getStatus(), "4")) {
                        e.setStatusName("仓库已提交");
                    }
                });
                return null; // Void 类型返回值
            });
            futures.add(future);
        }

        // 3. 等待所有任务完成
        try {
            for (Future<Void> future : futures) {
                future.get(); // 阻塞等待任务完成，可捕获异常
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("多线程处理异常", e);
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown(); // 关闭线程池
        }

        log.info("所有分片处理完成");
    }

    private void processDepotsInParallel(List<DepotHead> depots, int batchSize) {
        // 1. 定义线程池
        ExecutorService executor = Executors.newFixedThreadPool(10); // 根据实际需求调整线程数
        List<Future<Void>> futures = new ArrayList<>();

        // 2. 分片并提交任务
        List<List<DepotHead>> partitions = Lists.partition(depots, batchSize);
        for (List<DepotHead> batch : partitions) {
            Future<Void> future = executor.submit(() -> {
                batch.forEach(e -> {
                    // 重新从数据库获取最新数据
                    DepotHead detail = depotHeadNewService.getById(e.getId());
                    BeanUtil.copyProperties(detail, e);

                    // 计算税率
                    BigDecimal rate = null;
                    if (e.getFinancialTaxAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        if (StrUtil.equals("增票11个点", e.getBillingRate())) {
                            rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.11"));
                        } else if (StrUtil.equals("普票2个点", e.getBillingRate())) {
                            rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.02"));
                        } else if (StrUtil.equals("普票3个点", e.getBillingRate())) {
                            rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.03"));
                        }
                        e.setFinancialTaxAmount(rate == null ? BigDecimal.ZERO : rate);
                    } else {
                        rate = e.getFinancialTaxAmount();
                    }

                    // 物流费
                    BigDecimal lf = new BigDecimal(depotHead2Map.getLogisticsFreight(e.getNumber()));
                    e.setLogisticsFreight(lf);

                    // 计算订单提成
                    BigDecimal ocm = (e.getTotalFinancialPrice()
                            .subtract(rate != null ? rate : BigDecimal.ZERO)
                            .subtract(e.getTotalPrintingPrice())
                            .subtract(e.getHotStampingPaintingTotalPrice())
                            .subtract(e.getTotalStackPrice())
                            .subtract(e.getEmbroideryTotalPrice())
                            .subtract(e.getFinancialDeductionsFee())
                            .subtract(e.getFinancialRebate())
                            .subtract(e.getFinancialCostAmount())
                            .subtract(e.getOtherExpensesInFinance())
                            .subtract(e.getOtherExpensesInFinance2())
                            .subtract(e.getLogisticsFreight()))
                            .setScale(4, BigDecimal.ROUND_UP);

                    // 2026年之前：保持原有逻辑（按 divideType 计算）
                    if (e.getDivideType() == 1) {
                        ocm = ocm.multiply(new BigDecimal("0.5"));
                    } else if (e.getDivideType() == 2) {
                        ocm = ocm.multiply(new BigDecimal("0.4"));
                    }
                    e.setOrderCommission(ocm);

                    // 设置状态名称
                    if (StrUtil.equals(e.getStatus(), "0")) {
                        e.setStatusName("暂存");
                    } else if (StrUtil.equals(e.getStatus(), "1")) {
                        e.setStatusName("财务已归档");
                    } else if (StrUtil.equals(e.getStatus(), "9")) {
                        e.setStatusName("业务员已提交");
                    } else if (StrUtil.equals(e.getStatus(), "4")) {
                        e.setStatusName("仓库已提交");
                    }
                });
                return null; // Void 类型返回值
            });
            futures.add(future);
        }

        // 3. 等待所有任务完成
        try {
            for (Future<Void> future : futures) {
                future.get(); // 阻塞等待任务完成，可捕获异常
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("多线程处理异常", e);
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown(); // 关闭线程池
        }

        log.info("所有分片处理完成");
    }

    /**
     * 仓库导出excel
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/exportDebtHeadListByWare")
    @ApiOperation(value = "仓库导出销售excel")
    public void exportDebtHeadListByWare(@RequestParam(value = "salesMan", required = false) String salesMan,
                                         @RequestParam(value = "printer", required = false) String printer,
                                         @RequestParam(value = "printingReconciliationDate", required = false) String printingReconciliationDate,
                                         @RequestParam(value = "ironingStaff", required = false) String ironingStaff,
                                         @RequestParam(value = "stackStaff", required = false) String stackStaff,
                                         @RequestParam(value = "stampingReconciliationDate", required = false) String stampingReconciliationDate,
                                         @RequestParam(value = "rustStaff", required = false) String rustStaff,
                                         @RequestParam(value = "creator", required = false) String creator,
                                         @RequestParam(value = "operTime", required = false) String operTime,
                                         @RequestParam(value = "number", required = false) String number,
                                         @RequestParam(value = "isFinalPay", required = false) String isFinalPay,
                                         @RequestParam(value = "logisticsStatus", required = false) String logisticsStatus,
                                         @RequestParam(value = "ids", required = false) String ids,
                                         @RequestParam(value = "beginTime", required = false) String beginTime,
                                         @RequestParam(value = "endTime", required = false) String endTime,
                                         @RequestParam(value = "embroideryReconciliationDate", required = false) String embroideryReconciliationDate,
                                         @RequestParam(value = "status", required = false) String status,
                                         @RequestParam(value = "commodityNo", required = false) String commodityNo,
                                         @RequestParam(value = "color", required = false) String color,
                                         @RequestParam(value = "customer", required = false) String customer,

                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        User userInfo = userService.getCurrentUser();
        LambdaQueryWrapper<DepotHead> query = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(beginTime)) {
            query.ge(DepotHead::getCreateTime, beginTime.concat(" 00:00:00"));
        }
        if (StrUtil.isNotBlank(endTime)) {
            query.le(StrUtil.isNotBlank(endTime), DepotHead::getCreateTime, endTime.concat(" 23:59:59"));
        }
        if (StrUtil.equals(isFinalPay, "是")) {
            query.apply("IFNULL(end_staff,'')='是'");
        } else if (StrUtil.equals(isFinalPay, "否")) {
            query.apply("IFNULL(end_staff,'')!='是'");
        }
        if (StrUtil.contains(userInfo.getRoleName(), "业务员")) {
            query.eq(DepotHead::getCreator, userInfo.getId());
        } else if (StrUtil.contains(userInfo.getRoleName(), "财务")) {
            query.ne(DepotHead::getStatus, 0);
        } else if (StrUtil.contains(userInfo.getRoleName(), "仓库")) {
            query.in(DepotHead::getStatus, 9, 4, 1);
        } else {
            query.eq(StrUtil.isNotBlank(creator), DepotHead::getCreator, creator);
        }
        if (StrUtil.isNotBlank(ids)) {
            List<String> splitToList = Splitter.on(",").trimResults().splitToList(ids);
            query.in(splitToList != null, DepotHead::getId, splitToList);
        }
        query.eq(StrUtil.isNotBlank(logisticsStatus), DepotHead::getLogisticsStatus, logisticsStatus);
        query.like(StrUtil.isNotBlank(salesMan), DepotHead::getSalesMan, salesMan);
//        query.eq(StrUtil.isNotBlank(type), DepotHead::getType, type);
        query.eq(StrUtil.isNotBlank(commodityNo), DepotHead::getCommodityNo, commodityNo);
//        query.eq(StrUtil.isNotBlank(number), DepotHead::getNumber, number);
        query.eq(StrUtil.isNotBlank(color), DepotHead::getColor, color);
        query.eq(StrUtil.isNotBlank(status), DepotHead::getStatus, status);
        query.like(StrUtil.isNotBlank(customer), DepotHead::getCustomer, customer);
        if (StrUtil.isNotBlank(number)) {
            List<String> numberList = Splitter.on("，").trimResults().splitToList(number.replaceAll(",", "，"));
            query.in(DepotHead::getNumber, numberList);
        }

        query.eq(StrUtil.isNotBlank(printer), DepotHead::getPrinter, printer);
        query.eq(StrUtil.isNotBlank(ironingStaff), DepotHead::getIroningStaff, ironingStaff);
//        query.like(StrUtil.isNotBlank(customer), DepotHead::getCustomer, customer);
//        query.eq(StrUtil.isNotBlank(depotId), DepotHead::getId, depotId);
//        query.ge(StrUtil.isNotBlank(beginTime), DepotHead::getOperTime, beginTime);
//        query.le(StrUtil.isNotBlank(endTime), DepotHead::getOperTime, endTime);
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
        query.orderByAsc(DepotHead::getNumber, DepotHead::getId);
        query.last("limit 20000");
        List<DepotHead> depots = depotHeadNewService.mixList(query);
        // 分片大小
        int batchSize = 500;
        processDepotsInParallel2(depots, batchSize);
        if (StrUtil.contains(userInfo.getRoleName(), "财务") || StrUtil.contains(userInfo.getRoleName(), "管理员")) {
            DepotHead dh = new DepotHead();
            dh.setSaleType("合计：");
//            long projectTotal = list.stream().filter(e -> e.getConstYear().equals(item.getConstYear()))
//                    .mapToLong(BhPrProjectInfo::getProjectCount).sum();
            Long size1 = depots.stream().filter(e -> e.getSize1() != null).mapToLong(DepotHead::getSize1).sum();
            dh.setSize1(size1.intValue());
            Long size2 = depots.stream().filter(e -> e.getSize2() != null).mapToLong(DepotHead::getSize2).sum();
            dh.setSize2(size2.intValue());
            Long size3 = depots.stream().filter(e -> e.getSize3() != null).mapToLong(DepotHead::getSize3).sum();
            dh.setSize3(size3.intValue());
            Long size4 = depots.stream().filter(e -> e.getSize4() != null).mapToLong(DepotHead::getSize4).sum();
            dh.setSize4(size4.intValue());
            Long size5 = depots.stream().filter(e -> e.getSize5() != null).mapToLong(DepotHead::getSize5).sum();
            dh.setSize5(size5.intValue());
            Long size6 = depots.stream().filter(e -> e.getSize6() != null).mapToLong(DepotHead::getSize6).sum();
            dh.setSize6(size6.intValue());
            Long size7 = depots.stream().filter(e -> e.getSize7() != null).mapToLong(DepotHead::getSize7).sum();
            dh.setSize7(size7.intValue());
            Long size8 = depots.stream().filter(e -> e.getSize8() != null).mapToLong(DepotHead::getSize8).sum();
            dh.setSize8(size8.intValue());
            Long size9 = depots.stream().filter(e -> e.getSize9() != null).mapToLong(DepotHead::getSize9).sum();
            dh.setSize9(size9.intValue());
            Long size10 = depots.stream().filter(e -> e.getSize10() != null).mapToLong(DepotHead::getSize10).sum();
            dh.setSize10(size10.intValue());
            Long size11 = depots.stream().filter(e -> e.getSize11() != null).mapToLong(DepotHead::getSize11).sum();
            dh.setSize11(size11.intValue());
            Long nms = depots.stream().filter(Objects::nonNull).mapToLong(DepotHead::getNumbers).sum();
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
        //会员
        String[] names = {
                "销售类型",
                "销售订单",
                "下单时间",
                "业务员",
                "客户",
                "颜色",
                "货品名称",
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
                "单位",
                "下单备注",
                "运费付款方",
                "仓库备注",
                "印刷单价",
                "印刷总价",
                "印刷人员",
                "印刷对帐日期",
                "烫画人员",
                "烫画单价",
                "烫画总价",
                "叠单价",
                "叠总价",
                "叠人员",
                "烫叠对帐日期",
                "刺锈单价",
                "刺绣总价",
                "刺绣人员",
                "刺绣对账日期",
                "刺绣备注",
                "LOGO制作",
                "订单物流费"
        };
        String title = "销售订单";
        List<String[]> objects = new ArrayList<String[]>();
        if (null != depots) {
            for (DepotHead s : depots) {
                String[] objs = new String[41];
                objs[0] = s.getSaleType();
                objs[1] = s.getNumber();
                objs[2] = DateUtil.format(s.getOperTime(), "yyyy-MM-dd");
                objs[3] = s.getSalesMan();
                objs[4] = s.getCustomer();
                objs[5] = s.getColor();
                objs[6] = StrUtil.isNotBlank(s.getCommodityName()) ? s.getCommodityName().concat("(").concat(s.getCommodityNo()).concat(")") : "";
                objs[7] = s.getSize1() == null || s.getSize1() == 0 ? "" : StrUtil.toString(s.getSize1());
                objs[8] = s.getSize2() == null || s.getSize2() == 0 ? "" : StrUtil.toString(s.getSize2());
                objs[9] = s.getSize3() == null || s.getSize3() == 0 ? "" : StrUtil.toString(s.getSize3());
                objs[10] = s.getSize4() == null || s.getSize4() == 0 ? "" : StrUtil.toString(s.getSize4());
                objs[11] = s.getSize5() == null || s.getSize5() == 0 ? "" : StrUtil.toString(s.getSize5());
                objs[12] = s.getSize6() == null || s.getSize6() == 0 ? "" : StrUtil.toString(s.getSize6());
                objs[13] = s.getSize7() == null || s.getSize7() == 0 ? "" : StrUtil.toString(s.getSize7());
                objs[14] = s.getSize8() == null || s.getSize8() == 0 ? "" : StrUtil.toString(s.getSize8());
                objs[15] = s.getSize9() == null || s.getSize9() == 0 ? "" : StrUtil.toString(s.getSize9());
                objs[16] = s.getSize10() == null || s.getSize10() == 0 ? "" : StrUtil.toString(s.getSize10());
                objs[17] = s.getSize11() == null || s.getSize11() == 0 ? "" : StrUtil.toString(s.getSize11());
                objs[18] = StrUtil.toString(s.getNumbers());
                objs[19] = s.getUnit();
                objs[20] = s.getOrderRemark();
                objs[21] = s.getFreightPayer();
                objs[22] = s.getWarehouseRemark();
                objs[23] = s.getPrintingUnitPrice() == null ? "" : StrUtil.toString(s.getPrintingUnitPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[24] = StrUtil.toString(s.getTotalPrintingPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[25] = s.getPrinter();
                objs[26] = DateUtil.format(s.getPrintingReconciliationDate(), "yyyy-MM-dd");
                objs[27] = s.getIroningStaff();
                objs[28] = s.getHotPaintingUnitPrice() == null ? "" : StrUtil.toString(s.getHotPaintingUnitPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[29] = StrUtil.toString(s.getHotStampingPaintingTotalPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[30] = s.getStackedUnitPrice() == null ? "" : StrUtil.toString(s.getStackedUnitPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[31] = StrUtil.toString(s.getTotalStackPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[32] = s.getStackStaff();
                objs[33] = DateUtil.format(s.getStampingReconciliationDate(), "yyyy-MM-dd");
                objs[34] = s.getRustUnitPrice() == null ? "" : StrUtil.toString(s.getRustUnitPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[35] = StrUtil.toString(s.getEmbroideryTotalPrice().setScale(4, BigDecimal.ROUND_UP));
                objs[36] = s.getRustStaff();
                objs[37] = DateUtil.format(s.getEmbroideryReconciliationDate(), "yyyy-MM-dd");
                objs[38] = s.getEmbroideryRemark();
                objs[39] = s.getLogoMark();
                objs[40] = s.getLogisticsFreight() == null ? "0" : StrUtil.toString(s.getLogisticsFreight().setScale(4, BigDecimal.ROUND_UP));
                objects.add(objs);
            }
        }
        File file = ExcelUtils.exportObjectsWithoutTitle(title, "", names, title, objects);
        ExcelUtils.downloadExcel(file, file.getName(), response);
    }

    /**
     * 检查订单出库情况
     *
     * @param type
     * @param depotId
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/checkNumberOutInfo")
    @ApiOperation(value = "检查订单出库情况")
    public Result checkNumberOutInfo(@RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "depotId", required = false) String depotId,
                                     HttpServletRequest request) throws Exception {
        return depotHeadNewService.checkNumberOutInfo(type, depotId);
    }

    /**
     * 按照英诗兰得规定生成单据编号接口
     *
     * @return
     */
    @GetMapping(value = "/buildNumberForYsld")
    @ApiOperation(value = "单据编号生成接口new")
    public Result buildNumberForYsld(@RequestParam(value = "operTime", required = true) Date operTime) {
        return Result.success(depotHeadNewService.buildNumberForYsld(operTime));
    }

    /**
     * 导出系统货品销售信息
     */
    @RequestMapping("/exportCommodityList")
    public void exportCommodityList(@RequestParam(value = "beginTime", required = false) String beginTime,
                                    @RequestParam(value = "endTime", required = false) String endTime,
                                    HttpServletResponse response) {
        DepotHeadVO vo = new DepotHeadVO();
        vo.setBeginTime(beginTime);
        vo.setEndTime(endTime);
        depotHeadNewService.exportCommodityList(vo, response);
        return;
    }

    /**
     * 导出系统业务员信息
     */
    @RequestMapping("/exportSalesManList")
    public void exportSalesManList(@RequestParam(value = "beginTime", required = false) String beginTime,
                                   @RequestParam(value = "endTime", required = false) String endTime,
                                   HttpServletResponse response) {
        DepotHeadVO vo = new DepotHeadVO();
        vo.setBeginTime(beginTime);
        vo.setEndTime(endTime);
        depotHeadNewService.exportSalesManList(vo, response);
        return;
    }

    /**
     * 款式销量统计
     */
    @GetMapping("/getStatisticsStyleList")
    public Result getStatisticsStyleList(@RequestParam(value = "commodityNo", required = false) String commodityNo,
                                         @RequestParam(value = "commodityName", required = false) String commodityName,
                                         @RequestParam(value = "years", required = false) String years) {
        DepotHeadVO query = new DepotHeadVO();
        query.setCommodityNo(commodityNo);
        query.setCommodityName(commodityName);
        query.setYears(years);
//        query.setLimit(" limit ".concat(page).concat(",").concat(pageSize));
        return Result.success(depotHead2Map.exportCommodityNoList(query));
    }

    /**
     * 统计款式数量
     */
    @GetMapping("/getStatisticsStyleCount")
    public Result getStatisticsStyleCount(@RequestParam(value = "commodityNo", required = false) String commodityNo,
                                          @RequestParam(value = "commodityName", required = false) String commodityName) {
        DepotHeadVO query = new DepotHeadVO();
        query.setCommodityNo(commodityNo);
        query.setCommodityName(commodityName);
        return Result.success(depotHead2Map.exportCommodityNoList(query).size());
    }
}