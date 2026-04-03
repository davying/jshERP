package com.jsh.erp.service.custom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsh.erp.datasource.entities.DepotHead;
import com.jsh.erp.datasource.entities.vo.DepotHeadVO;
import com.jsh.erp.utils.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * 单据主表 服务类
 * </p>
 *
 * @author dawei
 * @since 2023-07-11 22:42:34
 */
public interface DepotHeadNewService extends IService<DepotHead> {

    String buildNumberForYsld(Date operTime);

    Result checkNumberOutInfo(String type, String depotId);

    void exportCommodityList(DepotHeadVO param, HttpServletResponse response);

    void exportSalesManList(DepotHeadVO param, HttpServletResponse response);

    List<DepotHead> mixList(LambdaQueryWrapper<DepotHead> wrapper);

}
