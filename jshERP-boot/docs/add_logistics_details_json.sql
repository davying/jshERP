-- 订单物流增加物流明细 JSON 字段
-- 执行时间：2026-07-22

ALTER TABLE jsh_warehouse_management ADD logistics_details_json TEXT DEFAULT NULL COMMENT '物流明细JSON' AFTER shipping_order_number;