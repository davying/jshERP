-- 销售订单(财) 增加付款日期字段
-- 执行时间：2026-06-26

ALTER TABLE jsh_depot_head ADD payment_date datetime DEFAULT NULL COMMENT '付款日期' AFTER invoice_remark;
