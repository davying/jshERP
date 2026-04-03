package com.jsh.erp.service.custom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsh.erp.datasource.entitie.custom.WarehouseManagement;
import com.jsh.erp.datasource.entities.vo.DepotHeadVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入出库记录表 服务类
 * </p>
 *
 * @author dawei
 * @since 2023-07-12 21:59:29
 */
public interface WarehouseManagementService extends IService<WarehouseManagement> {

    IPage<WarehouseManagement> getInOutFinalList(WarehouseManagement wm);

    List<WarehouseManagement> getInOutFinalListNoPage(WarehouseManagement wm);

    void exportWarehouse(DepotHeadVO param, HttpServletResponse response);

    void exportWarehouseInOut(DepotHeadVO param, HttpServletResponse response);

    List<Map<String, String>> getCommodityNoColors(String commodityNo);

}