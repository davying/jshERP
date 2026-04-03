package com.jsh.erp.service.custom;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsh.erp.datasource.entitie.custom.SampleManagement;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.QueryUtils;
import com.jsh.erp.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service(value = "sampleManagement_component")
@SampleManagementResource
public class SampleManagementComponent implements ICommonQuery {

    @Resource
    private SampleManagementService sampleManagementService;

    @Resource
    private UserService userService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return sampleManagementService.getById(id);
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
        LambdaQueryWrapper<SampleManagement> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(commodityNo), SampleManagement::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(color), SampleManagement::getColor, color);
        query.like(StrUtil.isNotBlank(price), SampleManagement::getPrice, price);
        query.orderByAsc(SampleManagement::getId);
        query.last("limit ".concat(StrUtil.toString(QueryUtils.offset(map))).concat(",").concat(StrUtil.toString(QueryUtils.rows(map))));
        return sampleManagementService.list(query);
    }

    @Override
    public Long counts(Map<String, String> map) throws Exception {
        String search = map.get(Constants.SEARCH);
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String color = StringUtil.getInfo(search, "color");
        String price = StringUtil.getInfo(search, "price");
        LambdaQueryWrapper<SampleManagement> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(commodityNo), SampleManagement::getCommodityNo, commodityNo);
        query.eq(StrUtil.isNotBlank(color), SampleManagement::getColor, color);
        query.like(StrUtil.isNotBlank(price), SampleManagement::getPrice, price);
        return sampleManagementService.count(query);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception {
        SampleManagement commodity = JSONObject.parseObject(obj.toJSONString(), SampleManagement.class);
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
        sampleManagementService.saveOrUpdate(commodity);
        return 1;
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request) throws Exception {
        SampleManagement commodity = JSONObject.parseObject(obj.toJSONString(), SampleManagement.class);
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
        sampleManagementService.saveOrUpdate(commodity);
        return 1;
    }

    @Override
    public int delete(Long id, HttpServletRequest request) throws Exception {
        sampleManagementService.removeById(id);
        return 1;
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request) throws Exception {
        List<String> list = Arrays.asList(ids.split(","));
        sampleManagementService.removeByIds(list);
        return 1;
    }

    @Override
    public int checkIsNameExist(Long id, String name) throws Exception {
        LambdaQueryWrapper<SampleManagement> query = new LambdaQueryWrapper<>();
        query.eq(SampleManagement::getId, id);
        Long co = sampleManagementService.count(query);
        return co.intValue();
    }

//    public int checkIsExist(Long id, String name)throws Exception {
//        return commodityService.checkIsExist(id, name, model, color, standard, mfrs, otherField1,
//                otherField2, otherField3, unit, unitId);
//    }

}
