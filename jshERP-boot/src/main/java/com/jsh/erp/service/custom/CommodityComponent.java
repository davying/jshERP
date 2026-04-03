package com.jsh.erp.service.custom;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
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

@Service(value = "commodity_component")
@CommodityResource
public class CommodityComponent implements ICommonQuery {

    @Resource
    private CommodityService commodityService;

    @Autowired
    private DepotHeadNewService depotHeadService;

    @Resource
    private UserService userService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return commodityService.getById(id);
    }

    @Override
    public List<?> select(Map<String, String> map) throws Exception {
        return getMaterialList(map);
    }

    private List<?> getMaterialList(Map<String, String> map) throws Exception {
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String remark = StringUtil.getInfo(search, "remark");
        LambdaQueryWrapper<Commodity> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(name), Commodity::getName, name);
        query.eq(StrUtil.isNotBlank(commodityNo), Commodity::getCommodityNo, commodityNo);
        query.like(StrUtil.isNotBlank(remark), Commodity::getRemark, remark);
        query.orderByAsc(Commodity::getId);
        query.last("limit ".concat(StrUtil.toString(QueryUtils.offset(map))).concat(",").concat(StrUtil.toString(QueryUtils.rows(map))));
        return commodityService.list(query);
    }

    @Override
    public Long counts(Map<String, String> map) throws Exception {
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String commodityNo = StringUtil.getInfo(search, "commodityNo");
        String remark = StringUtil.getInfo(search, "remark");
        LambdaQueryWrapper<Commodity> query = new LambdaQueryWrapper<>();
        query.like(StrUtil.isNotBlank(name), Commodity::getName, name);
        query.eq(StrUtil.isNotBlank(commodityNo), Commodity::getCommodityNo, commodityNo);
        query.like(StrUtil.isNotBlank(remark), Commodity::getRemark, remark);
        return commodityService.count(query);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception {
        Commodity commodity = JSONObject.parseObject(obj.toJSONString(), Commodity.class);
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
        commodityService.saveOrUpdate(commodity);
        return 1;
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request) throws Exception {
        Commodity commodity = JSONObject.parseObject(obj.toJSONString(), Commodity.class);
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
        commodityService.saveOrUpdate(commodity);
        List<DepotHead> dhs = depotHeadService.list(new LambdaQueryWrapper<DepotHead>().eq(DepotHead::getCommodityNo, commodity.getCommodityNo()));
        for (DepotHead dh : dhs) {
            dh.setCommodityName(commodity.getName());
        }
        List<List<DepotHead>> dhPartitionList = Lists.partition(dhs, 100);
        dhPartitionList.parallelStream().forEach(partitionList -> {
            depotHeadService.saveOrUpdateBatch(partitionList);
        });
        return 1;
    }

    @Override
    public int delete(Long id, HttpServletRequest request) throws Exception {
        commodityService.removeById(id);
        return 1;
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request) throws Exception {
        List<String> list = Arrays.asList(ids.split(","));
        commodityService.removeByIds(list);
        return 1;
    }

    @Override
    public int checkIsNameExist(Long id, String name) throws Exception {
        LambdaQueryWrapper<Commodity> query = new LambdaQueryWrapper<>();
        query.ne(Commodity::getId, id);
        query.eq(Commodity::getCommodityNo, name);
        Long co = commodityService.count(query);
        return co.intValue();
    }

//    public int checkIsExist(Long id, String name)throws Exception {
//        return commodityService.checkIsExist(id, name, model, color, standard, mfrs, otherField1,
//                otherField2, otherField3, unit, unitId);
//    }

}
