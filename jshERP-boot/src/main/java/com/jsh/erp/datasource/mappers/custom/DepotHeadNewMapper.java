package com.jsh.erp.datasource.mappers.custom;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jsh.erp.datasource.entities.DepotHead;
import com.jsh.erp.datasource.entities.vo.CommodityExportVO;
import com.jsh.erp.datasource.entities.vo.DepotHeadVO;
import com.jsh.erp.datasource.entities.vo.DepotLogisticsFreightVO;
import com.jsh.erp.datasource.entities.vo.SalesManExportVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 单据主表 Mapper 接口
 * </p>
 *
 * @author dawei
 * @since 2023-07-11 22:42:34
 */
public interface DepotHeadNewMapper extends BaseMapper<DepotHead> {

    List<DepotHead> getErrorFinancialTaxAmount();

    Double getLogisticsFreight(String number);

    /**
     * 按订单号批量汇总发货物流费，避免全表 GROUP BY
     */
    List<DepotLogisticsFreightVO> sumLogisticsFreightByDepotIds(@Param("depotIds") List<String> depotIds);

    List<CommodityExportVO> exportCommodityList(@Param("param") DepotHeadVO param);

    List<CommodityExportVO> exportCommodityNoList(@Param("param") DepotHeadVO param);

    List<SalesManExportVO> exportSalesManList(@Param("param") DepotHeadVO param);

    List<DepotHead> mixList(@Param(Constants.WRAPPER) Wrapper<DepotHead> queryWrapper);


}
