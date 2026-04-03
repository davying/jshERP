package com.jsh.erp.service.custom.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsh.erp.datasource.entities.Commodity;
import com.jsh.erp.datasource.mappers.CommodityMapper;
import com.jsh.erp.service.custom.CommodityService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author dawei
 * @since 2023-07-08 19:35:18
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

}
