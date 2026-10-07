# exportDebtHeadList 导出优化备份（2026-10-07）

## 说明

生产当前在用的是改动前逻辑。本目录同时备份了：

- `original/`：改动前（与当前 git HEAD / 生产同源）文件
- `optimized/`：本次优化后的文件
- `export-optimize.patch`：相对 HEAD 的完整 diff，便于审查或回滚对照

## 涉及文件

| 文件 | 作用 |
|------|------|
| `DepotHeadController.java` | 导出处理、合计行 |
| `DepotHeadNewServiceImpl.java` | `mixList` + 批量物流费 |
| `DepotHeadNewMapper.java` / `.xml` | 批量物流费 SQL、`mixList` |
| `DepotLogisticsFreightVO.java` | 仅优化版新增 |

## 若上线后有问题：回滚到生产原版

在项目根目录执行：

```bash
cp jshERP-boot/docs/backup/exportDebtHeadList-optimize-20261007/original/DepotHeadController.java \
  jshERP-boot/src/main/java/com/jsh/erp/controller/DepotHeadController.java

cp jshERP-boot/docs/backup/exportDebtHeadList-optimize-20261007/original/DepotHeadNewServiceImpl.java \
  jshERP-boot/src/main/java/com/jsh/erp/service/custom/impl/DepotHeadNewServiceImpl.java

cp jshERP-boot/docs/backup/exportDebtHeadList-optimize-20261007/original/DepotHeadNewMapper.java \
  jshERP-boot/src/main/java/com/jsh/erp/datasource/mappers/custom/DepotHeadNewMapper.java

cp jshERP-boot/docs/backup/exportDebtHeadList-optimize-20261007/original/DepotHeadNewMapper.xml \
  jshERP-boot/src/main/resources/mapper_xml/DepotHeadNewMapper.xml

# 优化版新增类，回滚后可删除（不删也不影响原逻辑）
rm -f jshERP-boot/src/main/java/com/jsh/erp/datasource/entities/vo/DepotLogisticsFreightVO.java
```

然后重新打包部署后端即可。

## 若需要重新套用优化版

```bash
cp jshERP-boot/docs/backup/exportDebtHeadList-optimize-20261007/optimized/DepotHeadController.java \
  jshERP-boot/src/main/java/com/jsh/erp/controller/DepotHeadController.java

cp jshERP-boot/docs/backup/exportDebtHeadList-optimize-20261007/optimized/DepotHeadNewServiceImpl.java \
  jshERP-boot/src/main/java/com/jsh/erp/service/custom/impl/DepotHeadNewServiceImpl.java

cp jshERP-boot/docs/backup/exportDebtHeadList-optimize-20261007/optimized/DepotHeadNewMapper.java \
  jshERP-boot/src/main/java/com/jsh/erp/datasource/mappers/custom/DepotHeadNewMapper.java

cp jshERP-boot/docs/backup/exportDebtHeadList-optimize-20261007/optimized/DepotHeadNewMapper.xml \
  jshERP-boot/src/main/resources/mapper_xml/DepotHeadNewMapper.xml

cp jshERP-boot/docs/backup/exportDebtHeadList-optimize-20261007/optimized/DepotLogisticsFreightVO.java \
  jshERP-boot/src/main/java/com/jsh/erp/datasource/entities/vo/DepotLogisticsFreightVO.java
```

## 建议上线方式

1. 先在测试环境用同一筛选条件导出，对比行数、物流费、提成、合计行
2. 生产低峰发布，保留本备份目录
3. 异常时按上面「回滚到生产原版」恢复并重新部署
