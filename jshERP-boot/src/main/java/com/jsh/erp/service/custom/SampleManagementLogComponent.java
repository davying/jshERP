package com.jsh.erp.service.custom;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entitie.custom.SampleManagement;
import com.jsh.erp.datasource.entitie.custom.SampleManagementLog;
import com.jsh.erp.datasource.entities.DepotHead;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.datasource.mappers.UserMapperEx;
import com.jsh.erp.exception.BusinessRunTimeException;
import com.jsh.erp.service.ICommonQuery;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(value = "sampleManagementLog_component")
@SampleManagementLogResource
public class SampleManagementLogComponent implements ICommonQuery {

    @Autowired
    private SampleManagementLogService sampleManagementLogService;

    @Autowired
    private SampleManagementService sampleManagementService;

    @Resource
    private UserService userService;

    @Autowired
    private UserMapperEx userMapperEx;

    @Override
    public Object selectOne(Long id) throws Exception {
        return sampleManagementLogService.getById(id);
    }

    @Override
    public List<?> select(Map<String, String> map) throws Exception {
        return getMaterialList(map);
    }

    private List<?> getMaterialList(Map<String, String> map) throws Exception {
        String search = map.get(Constants.SEARCH);
        String salesMan = StringUtil.getInfo(search, "salesMan");
        String sampleId = StringUtil.getInfo(search, "sampleId");
        String createTime = StringUtil.getInfo(search, "createTime");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        String price = StringUtil.getInfo(search, "price");
        String totalPrice = StringUtil.getInfo(search, "totalPrice");
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String sampleNumber = StringUtil.getInfo(search, "sampleNumber");
        String deptName = StringUtil.getInfo(search, "deptName");
        List<Map<String, String>> users = Lists.newArrayList();
        if (StrUtil.equals(deptName, "全部") || StrUtil.isBlank(deptName)) {
            users = userMapperEx.selectConditionUserList(null);
        } else if (StrUtil.contains(deptName, "销售一部")) {
            List<String> deptNames = Lists.newArrayList();
            deptNames.add("仓库管理员");
            deptNames.add("财务部");
            deptNames.add("销售一部");
            users = userMapperEx.selectUserList(deptNames);
        } else {
            users = userMapperEx.selectConditionUserList(deptName);
        }
        if (CollUtil.isEmpty(users)) {
            return new ArrayList<DepotHead>();
        }
        List<String> userNames = users.stream().map(o -> StrUtil.toString(o.get("username"))).collect(Collectors.toList());
        LambdaQueryWrapper<SampleManagementLog> query = new LambdaQueryWrapper<>();
        if (CollUtil.isNotEmpty(userNames)) {
            query.in(SampleManagementLog::getMember, userNames);
        }
        if (StrUtil.isNotBlank(beginTime)) {
            query.ge(SampleManagementLog::getCreateTime, beginTime.concat(" 00:00:00"));
        }
        if (StrUtil.isNotBlank(endTime)) {
            query.le(StrUtil.isNotBlank(endTime), SampleManagementLog::getCreateTime, endTime.concat(" 23:59:59"));
        }
//        query.like(StrUtil.isNotBlank(commodityNo), SampleManagementLog::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(salesMan), SampleManagementLog::getMember, salesMan);
        if (StrUtil.isNotBlank(createTime)) {
            query.apply("date_format(create_time,'%Y-%m-%d')='" + createTime + "'");
        }
        query.like(StrUtil.isNotBlank(sampleNumber), SampleManagementLog::getSampleNumber, sampleNumber);
        query.eq(StrUtil.isNotBlank(sampleId), SampleManagementLog::getSampleId, sampleId);
        query.like(StrUtil.isNotBlank(commodityNo), SampleManagementLog::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(price), SampleManagementLog::getPrice, price);
        query.eq(StrUtil.isNotBlank(totalPrice), SampleManagementLog::getTotalPrice, totalPrice);
//        query.like(StrUtil.isNotBlank(price), SampleManagementLog::getPrice, price);
        query.orderByAsc(SampleManagementLog::getId);
        query.last("limit ".concat(StrUtil.toString(QueryUtils.offset(map))).concat(",").concat(StrUtil.toString(QueryUtils.rows(map))));
        List<SampleManagementLog> logs = sampleManagementLogService.list(query);
        logs.forEach(e -> {
            e.setSampleColor(sampleManagementService.getById(e.getSampleId()).getColor());
            e.setCreateTimeStr(DateUtil.format(e.getCreateTime(), "yyyy-MM-dd"));
        });
        return logs;
    }

    @Override
    public Long counts(Map<String, String> map) throws Exception {
        String search = map.get(Constants.SEARCH);
        String salesMan = StringUtil.getInfo(search, "salesMan");
        String sampleId = StringUtil.getInfo(search, "sampleId");
        String createTime = StringUtil.getInfo(search, "createTime");
        String price = StringUtil.getInfo(search, "price");
        String totalPrice = StringUtil.getInfo(search, "totalPrice");
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String sampleNumber = StringUtil.getInfo(search, "sampleNumber");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        String deptName = StringUtil.getInfo(search, "deptName");
        List<Map<String, String>> users = Lists.newArrayList();
        if (StrUtil.equals(deptName, "全部") || StrUtil.isBlank(deptName)) {
            users = userMapperEx.selectConditionUserList(null);
        } else if (StrUtil.contains(deptName, "销售一部")) {
            List<String> deptNames = Lists.newArrayList();
            deptNames.add("仓库管理员");
            deptNames.add("财务部");
            deptNames.add("销售一部");
            users = userMapperEx.selectUserList(deptNames);
        } else {
            users = userMapperEx.selectConditionUserList(deptName);
        }
        if (CollUtil.isEmpty(users)) {
            return 0L;
        }
        List<String> userNames = users.stream().map(o -> StrUtil.toString(o.get("username"))).collect(Collectors.toList());
        LambdaQueryWrapper<SampleManagementLog> query = new LambdaQueryWrapper<>();
        if (CollUtil.isNotEmpty(userNames)) {
            query.in(SampleManagementLog::getMember, userNames);
        }
        if (StrUtil.isNotBlank(beginTime)) {
            query.ge(SampleManagementLog::getCreateTime, beginTime.concat(" 00:00:00"));
        }
        if (StrUtil.isNotBlank(endTime)) {
            query.le(StrUtil.isNotBlank(endTime), SampleManagementLog::getCreateTime, endTime.concat(" 23:59:59"));
        }
//        query.like(StrUtil.isNotBlank(commodityNo), SampleManagementLog::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(salesMan), SampleManagementLog::getMember, salesMan);
        if (StrUtil.isNotBlank(createTime)) {
            query.apply("date_format(create_time,'%Y-%m-%d')='" + createTime + "'");
        }
        query.like(StrUtil.isNotBlank(sampleNumber), SampleManagementLog::getSampleNumber, sampleNumber);
        query.eq(StrUtil.isNotBlank(sampleId), SampleManagementLog::getSampleId, sampleId);
        query.like(StrUtil.isNotBlank(commodityNo), SampleManagementLog::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(price), SampleManagementLog::getPrice, price);
        query.eq(StrUtil.isNotBlank(totalPrice), SampleManagementLog::getTotalPrice, totalPrice);
//        query.like(StrUtil.isNotBlank(commodityNo), SampleManagement::getCommodityNo, commodityNo);
//        query.eq(StrUtil.isNotBlank(color), SampleManagement::getColor, color);
//        query.like(StrUtil.isNotBlank(price), SampleManagement::getPrice, price);
        return sampleManagementLogService.count(query);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception {
        SampleManagementLog log = JSONObject.parseObject(obj.toJSONString(), SampleManagementLog.class);
        User userInfo = userService.getCurrentUser();
        SampleManagement sample = new SampleManagement();
        sample.setId(log.getSampleId());
        List<SampleManagement> smList = sampleManagementService.getSampleInOutFinalListNoPage(sample);
        SampleManagement sm = null;
        if (CollUtil.isNotEmpty(smList)) {
            sm = smList.get(0);
        } else {
            throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                    "样衣库存不足，无法出样！");
        }
        // 新增
        if (log.getId() == null) {
            log.setUpdateBy(userInfo.getUsername());
            log.setUpdateTime(DateUtil.date());
            log.setCreateBy(userInfo.getUsername());
            if (log.getSize1() != null) {
                int size1 = new BigDecimal(sm.getSize1()).subtract(new BigDecimal(log.getSize1())).intValue();
                if (size1 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "S库存不足，无法出样！");
                }
            }
            if (log.getSize2() != null) {
                int size2 = new BigDecimal(sm.getSize2()).subtract(new BigDecimal(log.getSize2())).intValue();
                if (size2 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "M库存不足，无法出样！");
                }
            }
            if (log.getSize3() != null) {
                int size3 = new BigDecimal(sm.getSize3()).subtract(new BigDecimal(log.getSize3())).intValue();
                if (size3 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "L库存不足，无法出样！");
                }
            }
            if (log.getSize4() != null) {
                int size4 = new BigDecimal(sm.getSize4()).subtract(new BigDecimal(log.getSize4())).intValue();
                if (size4 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "XL库存不足，无法出样！");
                }
            }
            if (log.getSize5() != null) {
                int size5 = new BigDecimal(sm.getSize5()).subtract(new BigDecimal(log.getSize5())).intValue();
                if (size5 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "2XL库存不足，无法出样！");
                }
            }
            if (log.getSize6() != null) {
                int size6 = new BigDecimal(sm.getSize6()).subtract(new BigDecimal(log.getSize6())).intValue();
                if (size6 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "3XL库存不足，无法出样！");
                }
            }
            if (log.getSize7() != null) {
                int size7 = new BigDecimal(sm.getSize7()).subtract(new BigDecimal(log.getSize7())).intValue();
                if (size7 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "4XL库存不足，无法出样！");
                }
            }
            if (log.getSize8() != null) {
                int size8 = new BigDecimal(sm.getSize8()).subtract(new BigDecimal(log.getSize8())).intValue();
                if (size8 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "5XL库存不足，无法出样！");
                }
            }
            if (log.getSize9() != null) {
                int size9 = new BigDecimal(sm.getSize9()).subtract(new BigDecimal(log.getSize9())).intValue();
                if (size9 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "6XL库存不足，无法出样！");
                }
            }
            if (log.getSize10() != null) {
                int size10 = new BigDecimal(sm.getSize10()).subtract(new BigDecimal(log.getSize10())).intValue();
                if (size10 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "7XL库存不足，无法出样！");
                }
            }
            if (log.getSize11() != null) {
                int size11 = new BigDecimal(sm.getSize11()).subtract(new BigDecimal(log.getSize11())).intValue();
                if (size11 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "XS库存不足，无法出样！");
                }
            }
            sm.setNumbers(new BigDecimal(sm.getNumbers()).subtract(new BigDecimal(log.getNumbers())).intValue());
        } else {
            // 编辑（反哺在扣除）
            SampleManagementLog oldLog = sampleManagementLogService.getById(log.getId());
            if (log.getSize1() != null) {
                int size1 = new BigDecimal(sm.getSize1()).add(new BigDecimal(oldLog.getSize1())).subtract(new BigDecimal(log.getSize1())).intValue();
                if (size1 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "S库存不足，无法出样！");
                }
            }
            if (log.getSize2() != null) {
                int size2 = new BigDecimal(sm.getSize2()).add(new BigDecimal(oldLog.getSize2())).subtract(new BigDecimal(log.getSize2())).intValue();
                if (size2 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "M库存不足，无法出样！");
                }
            }
            if (log.getSize3() != null) {
                int size3 = new BigDecimal(sm.getSize3()).add(new BigDecimal(oldLog.getSize3())).subtract(new BigDecimal(log.getSize3())).intValue();
                if (size3 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "L库存不足，无法出样！");
                }
            }
            if (log.getSize4() != null) {
                int size4 = new BigDecimal(sm.getSize4()).add(new BigDecimal(oldLog.getSize4())).subtract(new BigDecimal(log.getSize4())).intValue();
                if (size4 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "XL库存不足，无法出样！");
                }
            }
            if (log.getSize5() != null) {
                int size5 = new BigDecimal(sm.getSize5()).add(new BigDecimal(oldLog.getSize5())).subtract(new BigDecimal(log.getSize5())).intValue();
                if (size5 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "2XL库存不足，无法出样！");
                }
            }
            if (log.getSize6() != null) {
                int size6 = new BigDecimal(sm.getSize6()).add(new BigDecimal(oldLog.getSize6())).subtract(new BigDecimal(log.getSize6())).intValue();
                if (size6 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "3XL库存不足，无法出样！");
                }
            }
            if (log.getSize7() != null) {
                int size7 = new BigDecimal(sm.getSize7()).add(new BigDecimal(oldLog.getSize7())).subtract(new BigDecimal(log.getSize7())).intValue();
                if (size7 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "4XL库存不足，无法出样！");
                }
            }
            if (log.getSize8() != null) {
                int size8 = new BigDecimal(sm.getSize8()).add(new BigDecimal(oldLog.getSize8())).subtract(new BigDecimal(log.getSize8())).intValue();
                if (size8 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "5XL库存不足，无法出样！");
                }
            }
            if (log.getSize9() != null) {
                int size9 = new BigDecimal(sm.getSize9()).add(new BigDecimal(oldLog.getSize9())).subtract(new BigDecimal(log.getSize9())).intValue();
                if (size9 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "6XL库存不足，无法出样！");
                }
            }
            if (log.getSize10() != null) {
                int size10 = new BigDecimal(sm.getSize10()).add(new BigDecimal(oldLog.getSize10())).subtract(new BigDecimal(log.getSize10())).intValue();
                if (size10 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "7XL库存不足，无法出样！");
                }
            }
            if (log.getSize11() != null) {
                int size11 = new BigDecimal(sm.getSize11()).add(new BigDecimal(oldLog.getSize11())).subtract(new BigDecimal(log.getSize11())).intValue();
                if (size11 < 0) {
                    throw new BusinessRunTimeException(ExceptionConstants.SERVICE_SYSTEM_ERROR_CODE,
                            "XS库存不足，无法出样！");
                }
            }
            log.setUpdateBy(userInfo.getUsername());
            log.setUpdateTime(DateUtil.date());
        }
        log.setCommodityNo(sm.getCommodityNo());
        log.setCommodityName(sm.getCommodityName());
        sampleManagementLogService.saveOrUpdate(log);
        return 1;
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request) throws Exception {
        SampleManagementLog commodity = JSONObject.parseObject(obj.toJSONString(), SampleManagementLog.class);
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
        sampleManagementLogService.saveOrUpdate(commodity);
        return 1;
    }

    @Override
    public int delete(Long id, HttpServletRequest request) throws Exception {
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
        return 1;
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request) throws Exception {
        List<String> list = Arrays.asList(ids.split(","));
        sampleManagementLogService.removeByIds(list);
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
