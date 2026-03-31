package com.jsh.erp.service.custom;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsh.erp.datasource.entitie.custom.SampleManagementOld;
import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.QueryUtils;
import com.jsh.erp.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "sampleManagementOld_component")
@SampleManagementOldResource
public class SampleManagementOldComponent implements ICommonQuery {

    @Resource
    private SampleManagementOldService sampleManagementOldService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return sampleManagementOldService.getById(id);
    }

    @Override
    public List<?> select(Map<String, String> map) throws Exception {
        return getMaterialList(map);
    }

    private List<?> getMaterialList(Map<String, String> map) throws Exception {
        String search = map.get(Constants.SEARCH);
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String color = StringUtil.getInfo(search, "color");
        String price = StringUtil.getInfo(search, "price");
        LambdaQueryWrapper<SampleManagementOld> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(commodityNo), SampleManagementOld::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(color), SampleManagementOld::getColor, color);
        query.like(StrUtil.isNotBlank(price), SampleManagementOld::getPrice, price);
        query.orderByAsc(SampleManagementOld::getId);
        query.last("limit ".concat(StrUtil.toString(QueryUtils.offset(map))).concat(",").concat(StrUtil.toString(QueryUtils.rows(map))));
        return sampleManagementOldService.list(query);
    }

    @Override
    public Long counts(Map<String, String> map) throws Exception {
        String search = map.get(Constants.SEARCH);
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String color = StringUtil.getInfo(search, "color");
        String price = StringUtil.getInfo(search, "price");
        LambdaQueryWrapper<SampleManagementOld> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(commodityNo), SampleManagementOld::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(color), SampleManagementOld::getColor, color);
        query.like(StrUtil.isNotBlank(price), SampleManagementOld::getPrice, price);
        return sampleManagementOldService.count(query);
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
