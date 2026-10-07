package com.jsh.erp.datasource.entities.vo;

import java.math.BigDecimal;

/**
 * 订单物流费汇总
 */
public class DepotLogisticsFreightVO {

    private String depotId;

    private BigDecimal logisticsFreight;

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public BigDecimal getLogisticsFreight() {
        return logisticsFreight;
    }

    public void setLogisticsFreight(BigDecimal logisticsFreight) {
        this.logisticsFreight = logisticsFreight;
    }
}
