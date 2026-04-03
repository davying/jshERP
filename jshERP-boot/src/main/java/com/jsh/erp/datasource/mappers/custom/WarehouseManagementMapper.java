package com.jsh.erp.datasource.mappers.custom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsh.erp.datasource.entitie.custom.WarehouseManagement;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入出库记录表 Mapper 接口
 * </p>
 *
 * @author dawei
 * @since 2023-07-12 21:59:29
 */
public interface WarehouseManagementMapper extends BaseMapper<WarehouseManagement> {
    IPage<WarehouseManagement> getInOutFinalList(Page page, @Param("request") WarehouseManagement param);

    List<WarehouseManagement> getInOutFinalList(@Param("request") WarehouseManagement param);

    List<WarehouseManagement> getInOutFinalList2(@Param("request") WarehouseManagement param);

    List<Map<String, String>> getCommodityNoColors(@Param("commodityNo") String commodityNo);


}
