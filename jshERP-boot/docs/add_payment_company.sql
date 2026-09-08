-- 销售订单(财) 增加付款公司字段
-- 执行时间：2026-09-07

ALTER TABLE jsh_depot_head ADD payment_company varchar(255) DEFAULT NULL COMMENT '付款公司' AFTER payment_date;