package com.jsh.erp.datasource.entities.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jsh.erp.datasource.entities.Commodity;
import lombok.Data;

@Data
public class CommodityVO extends Commodity {

    private Long pageNo = 1L;

    private Long pageSize = 10L;
}
