package com.jsh.erp.service.custom;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsh.erp.datasource.entitie.custom.SampleManagementLogOld;
import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.QueryUtils;
import com.jsh.erp.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "sampleManagementLogOld_component")
@SampleManagementLogOldResource
public class SampleManagementLogOldComponent implements ICommonQuery {

    @Autowired
    private SampleManagementLogOldService sampleManagementLogOldService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return sampleManagementLogOldService.getById(id);
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
        LambdaQueryWrapper<SampleManagementLogOld> query = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(beginTime)) {
            query.ge(SampleManagementLogOld::getCreateTime, beginTime.concat(" 00:00:00"));
        }
        if (StrUtil.isNotBlank(endTime)) {
            query.le(StrUtil.isNotBlank(endTime), SampleManagementLogOld::getCreateTime, endTime.concat(" 23:59:59"));
        }
//        query.like(StrUtil.isNotBlank(commodityNo), SampleManagementLog::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(salesMan), SampleManagementLogOld::getMember, salesMan);
        if (StrUtil.isNotBlank(createTime)) {
            query.apply("date_format(create_time,'%Y-%m-%d')='" + createTime + "'");
        }
        query.like(StrUtil.isNotBlank(sampleNumber), SampleManagementLogOld::getSampleNumber, sampleNumber);
        query.eq(StrUtil.isNotBlank(sampleId), SampleManagementLogOld::getSampleId, sampleId);
        query.like(StrUtil.isNotBlank(commodityNo), SampleManagementLogOld::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(price), SampleManagementLogOld::getPrice, price);
        query.eq(StrUtil.isNotBlank(totalPrice), SampleManagementLogOld::getTotalPrice, totalPrice);
//        query.like(StrUtil.isNotBlank(price), SampleManagementLog::getPrice, price);
        query.orderByAsc(SampleManagementLogOld::getId);
        query.last("limit ".concat(StrUtil.toString(QueryUtils.offset(map))).concat(",").concat(StrUtil.toString(QueryUtils.rows(map))));
        List<SampleManagementLogOld> logs = sampleManagementLogOldService.list(query);
        logs.forEach(e -> {
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
        LambdaQueryWrapper<SampleManagementLogOld> query = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(beginTime)) {
            query.ge(SampleManagementLogOld::getCreateTime, beginTime.concat(" 00:00:00"));
        }
        if (StrUtil.isNotBlank(endTime)) {
            query.le(StrUtil.isNotBlank(endTime), SampleManagementLogOld::getCreateTime, endTime.concat(" 23:59:59"));
        }
//        query.like(StrUtil.isNotBlank(commodityNo), SampleManagementLog::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(salesMan), SampleManagementLogOld::getMember, salesMan);
        if (StrUtil.isNotBlank(createTime)) {
            query.apply("date_format(create_time,'%Y-%m-%d')='" + createTime + "'");
        }
        query.like(StrUtil.isNotBlank(sampleNumber), SampleManagementLogOld::getSampleNumber, sampleNumber);
        query.eq(StrUtil.isNotBlank(sampleId), SampleManagementLogOld::getSampleId, sampleId);
        query.like(StrUtil.isNotBlank(commodityNo), SampleManagementLogOld::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(price), SampleManagementLogOld::getPrice, price);
        query.eq(StrUtil.isNotBlank(totalPrice), SampleManagementLogOld::getTotalPrice, totalPrice);
//        query.like(StrUtil.isNotBlank(commodityNo), SampleManagement::getCommodityNo, commodityNo);
//        query.eq(StrUtil.isNotBlank(color), SampleManagement::getColor, color);
//        query.like(StrUtil.isNotBlank(price), SampleManagement::getPrice, price);
        return sampleManagementLogOldService.count(query);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception {
        return 1;
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request) throws Exception {
        return 1;
    }

    @Override
    public int delete(Long id, HttpServletRequest request) throws Exception {
        return 1;
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request) throws Exception {
        return 1;
    }

    @Override
    public int checkIsNameExist(Long id, String name) throws Exception {
        return 1;
    }
}
