package com.jsh.erp.service.custom;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsh.erp.datasource.entitie.custom.WarehouseManagementOld;
import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.QueryUtils;
import com.jsh.erp.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "warehouseManagementOld_component")
@WarehouseManagementOldResource
public class WarehouseManagementOldComponent implements ICommonQuery {
    @Autowired
    private WarehouseManagementOldService warehouseManagementOldService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return warehouseManagementOldService.getById(id);
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
        LambdaQueryWrapper<WarehouseManagementOld> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(type), WarehouseManagementOld::getType, type);
        if (StrUtil.isNotBlank(commodityNo)) {
            query.and(i -> i.eq(WarehouseManagementOld::getCommodityNo, commodityNo)
                    .or().eq(WarehouseManagementOld::getCommodityName, commodityNo));
        }
        query.eq(StrUtil.isNotBlank(salesMan), WarehouseManagementOld::getSalesMan, salesMan);
        query.eq(StrUtil.isNotBlank(number), WarehouseManagementOld::getDepotId, number);
        query.eq(StrUtil.isNotBlank(commodityNo), WarehouseManagementOld::getCommodityNo, commodityNo);
        query.like(StrUtil.isNotBlank(commodityName), WarehouseManagementOld::getCommodityName, commodityName);
        query.eq(StrUtil.isNotBlank(color), WarehouseManagementOld::getColor, color);
        if (StrUtil.isNotBlank(inTime)) {
            query.apply("date_format(in_time,'%Y-%m-%d')='" + inTime + "'");
        }
//        query.like(StrUtil.isNotBlank(price), SampleManagementLog::getPrice, price);
        if (StrUtil.contains(type, "发货") || StrUtil.contains(type, "出库")) {
            query.orderByAsc(WarehouseManagementOld::getDepotId);
        } else if (StrUtil.contains(type, "入库")) {
            query.orderByAsc(WarehouseManagementOld::getInTime);
        } else {
            query.orderByAsc(WarehouseManagementOld::getCreateTime);
        }
        query.last("limit ".concat(StrUtil.toString(QueryUtils.offset(map))).concat(",").concat(StrUtil.toString(QueryUtils.rows(map))));
        return warehouseManagementOldService.list(query);
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
        LambdaQueryWrapper<WarehouseManagementOld> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(type), WarehouseManagementOld::getType, type);
        query.eq(StrUtil.isNotBlank(number), WarehouseManagementOld::getNumbers, number);
        query.eq(StrUtil.isNotBlank(commodityNo), WarehouseManagementOld::getCommodityNo, commodityNo);
        query.like(StrUtil.isNotBlank(commodityName), WarehouseManagementOld::getCommodityName, commodityName);
//        query.like(StrUtil.isNotBlank(commodityNo), SampleManagement::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(color), WarehouseManagementOld::getColor, color);
//        query.like(StrUtil.isNotBlank(price), SampleManagement::getPrice, price);
        return warehouseManagementOldService.count(query);
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