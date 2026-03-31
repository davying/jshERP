package com.jsh.erp.schedule;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Stopwatch;
import com.jsh.erp.datasource.entitie.custom.WarehouseManagement;
import com.jsh.erp.datasource.entities.*;
import com.jsh.erp.datasource.mappers.DepotHeadMapper;
import com.jsh.erp.datasource.mappers.MsgMapper;
import com.jsh.erp.datasource.mappers.UserMapper;
import com.jsh.erp.datasource.mappers.custom.DepotHeadNewMapper;
import com.jsh.erp.datasource.mappers.custom.WarehouseManagementMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DepotHeadTask {

    // 尺码映射：索引0对应size1，即S码
    private static final List<String> SIZE_NAMES = Arrays.asList(
            "S", "M", "L", "XL", "2XL", "3XL", "4XL", "5XL", "6XL", "7XL", "XS"
    );
    @Autowired
    private DepotHeadMapper depotHeadMapper;
    @Autowired
    private DepotHeadNewMapper depotHead2Map;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WarehouseManagementMapper wmMapper;

    //    /**
//     * 校准订单总价
//     */
//    @Async
//    @Scheduled(cron = "0 0 3 * * ?")
//    @PostConstruct
//    public void deleteZipFiles() {
//        Stopwatch watch = Stopwatch.createStarted();
//        depotHeadMapper.updateByTotalMoney();
//        log.info("校准订单总价耗时:{}秒", watch.elapsed(TimeUnit.SECONDS));
//        watch.stop();
//    }
    @Autowired
//    private MsgService msgService;
    private MsgMapper msgMapper;

    /**
     * 校准订单税额
     */
    @Async
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void updDepotHeadFinancialTaxAmount() {
        Stopwatch watch = Stopwatch.createStarted();
        List<DepotHead> errors = depotHead2Map.getErrorFinancialTaxAmount();
        errors.forEach(e -> {
            BigDecimal rate = new BigDecimal(0);
            if (StrUtil.equals("增票11个点", e.getBillingRate())) {
                rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.11")).setScale(4, BigDecimal.ROUND_UP);
            } else if (StrUtil.equals("普票2个点", e.getBillingRate())) {
                rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.02")).setScale(4, BigDecimal.ROUND_UP);
            } else if (StrUtil.equals("普票3个点", e.getBillingRate())) {
                rate = e.getTotalFinancialPrice().multiply(new BigDecimal("0.03")).setScale(4, BigDecimal.ROUND_UP);
            }
            e.setFinancialTaxAmount(rate);
            depotHead2Map.updateById(e);
        });
        log.info("校准订单税额{}条，耗时:{}秒", errors.size(), watch.elapsed(TimeUnit.SECONDS));
        watch.stop();
    }

//    /**
//     * 校准提成以及物流费 TODO 感觉有问题
//     */
//    @Async
//    @Scheduled(cron = "0 0 0/2 * * ?")
////    @PostConstruct
//    public void updOrderCommission() {
//        Stopwatch watch = Stopwatch.createStarted();
//        List<DepotHead> depots = depotHead2Map.selectList(new LambdaQueryWrapper<DepotHead>().eq(DepotHead::getStatus, 4));
//        AtomicReference<Integer> count = new AtomicReference<>(0);
//        depots.forEach(e -> {
//            // 物流费
//            BigDecimal lf = new BigDecimal(depotHead2Map.getLogisticsFreight(e.getNumber()));
//            e.setLogisticsFreight(lf);
//            BigDecimal comm = (e.getTotalFinancialPrice()
//                    .subtract(e.getTotalPrintingPrice())
//                    .subtract(e.getHotStampingPaintingTotalPrice())
//                    .subtract(e.getTotalStackPrice())
//                    .subtract(e.getEmbroideryTotalPrice())
//                    .subtract(e.getFinancialDeductionsFee())
//                    .subtract(e.getFinancialRebate())
//                    .subtract(e.getFinancialCostAmount())
//                    .subtract(e.getOtherExpensesInFinance())
//                    .subtract(e.getFinancialTaxAmount())
//                    .subtract(lf)).divide(new BigDecimal(2)).setScale(4, BigDecimal.ROUND_UP);
//            if (e.getOrderCommission().compareTo(comm) != 0) {
//                count.getAndSet(count.get() + 1);
//                log.info("订单：" + e.getNumber() + "，原提成:" + e.getOrderCommission() + ",现提成" + comm);
//                e.setOrderCommission(comm);
//                depotHead2Map.updateById(e);
//            }
//        });
//        log.info("校准提成提交到财务{}条,金额不一致{}条，耗时:{}秒", depots.size(), count.get(), watch.elapsed(TimeUnit.SECONDS));
//        watch.stop();
//    }

    /**
     * 校准订单总数量
     */
    @Async
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void updDepotHeadNumbers() {
        Stopwatch watch = Stopwatch.createStarted();
        depotHeadMapper.updDepotHeadNumbers();
        log.info("校准订单总数量耗时:{}秒", watch.elapsed(TimeUnit.SECONDS));
        watch.stop();
    }

    /**
     * 校准物流状态
     */
    @Async
    @Scheduled(cron = "0 0 0/3 * * ?")
    public void updLogisticsStatus() {
        Stopwatch watch = Stopwatch.createStarted();
        depotHeadMapper.updLogisticsStatus();
        log.info("校准物流状态耗时:{}秒", watch.elapsed(TimeUnit.SECONDS));
        watch.stop();
    }

    /**
     * 库存不足消息推送
     */
    @Async
    @Scheduled(cron = "0 0 8 * * ?")
    public void belowSafetyStockMsg() {
        Stopwatch watch = Stopwatch.createStarted();
        msgMapper.deleteByExample(new MsgExample());
        List<User> list = getNotifyUsers();
        if (list.isEmpty()) {
            log.warn("未找到库存预警消息接收用户");
            return;
        }

        List<WarehouseManagement> wms = wmMapper.getInOutFinalList2(new WarehouseManagement());

        // 按货品+颜色分组，合并多个尺码的预警信息
        wms.forEach(e -> {
            List<SizeStock> lowStocks = checkLowStocks(e);
            if (!lowStocks.isEmpty()) {
                sendMergedStockAlert(e, lowStocks, list);
            }
        });
        log.info("库存不足消息推送完成，耗时:{}秒", watch.elapsed(TimeUnit.SECONDS));
        watch.stop();
    }


    /**
     * 提取所有尺码库存
     */
    private List<Integer> extractSizes(WarehouseManagement e) {
        return Arrays.asList(
                e.getSize1(), e.getSize2(), e.getSize3(), e.getSize4(),
                e.getSize5(), e.getSize6(), e.getSize7(), e.getSize8(),
                e.getSize9(), e.getSize10(), e.getSize11()
        );
    }

//    /**
//     * 发送库存预警消息
//     */
//    private void sendStockAlert(WarehouseManagement e, String sizeName,
//                                Integer stock, List<User> users) {
//        users.forEach(user -> {
//            Msg msg = new Msg();
//            msg.setMsgTitle(String.format("货品%s ,颜色%s, %s码库存预警",
//                    e.getCommodityNo(), e.getColor(), sizeName));
//            msg.setMsgContent(String.format("货品：%s, %s码库存已不足%d件，当前库存：%d，请确认！",
//                    e.getCommodityNo().concat("(").concat(e.getCommodityName().concat(")")), sizeName, 100, stock));
//            msg.setUserId(user.getId());
//            msg.setCreateTime(new Date());
//            msg.setStatus("1");
//            msg.setType("库存预警");
//            msgMapper.insert(msg);
//        });
//
//    }

    /**
     * 获取消息接收用户
     */
    private List<User> getNotifyUsers() {
        UserExample example = new UserExample();
        List<String> excludeNames = Arrays.asList("admin", "yanghao", "test123");
        example.createCriteria()
                .andLoginNameNotIn(excludeNames)
                .andStatusEqualTo((byte) 0);
        return userMapper.selectByExample(example);
    }

    /**
     * 检查所有尺码，返回库存不足的尺码列表
     */
    private List<SizeStock> checkLowStocks(WarehouseManagement e) {
        List<Integer> sizes = extractSizes(e);
        List<SizeStock> result = new ArrayList<>();

        for (int i = 0; i < sizes.size(); i++) {
            Integer stock = sizes.get(i);
            if (stock != null && stock != -9999 && stock < 100) {
                result.add(new SizeStock(SIZE_NAMES.get(i), stock));
            }
        }
        return result;
    }

    /**
     * 发送合并后的库存预警消息
     */
    private void sendMergedStockAlert(WarehouseManagement e, List<SizeStock> lowStocks, List<User> users) {
        // 组装消息内容：多个尺码合并
        String sizeDetails = lowStocks.stream()
                .map(s -> String.format("%s码(%d件)", s.getSizeName(), s.getStock()))
                .collect(Collectors.joining("、"));

        String totalInfo = String.format("共%d个尺码库存不足", lowStocks.size());

        users.forEach(user -> {
            Msg msg = new Msg();
            msg.setMsgTitle(String.format("货品%s ,颜色%s, %s",
                    e.getCommodityNo(), e.getColor(), totalInfo));
            msg.setMsgContent(String.format("货品：%s, %s库存紧张，请确认！详情：%s",
                    e.getCommodityNo().concat("(").concat(e.getCommodityName()).concat(")"),
                    totalInfo,
                    sizeDetails));
            msg.setUserId(user.getId());
            msg.setCreateTime(new Date());
            msg.setStatus("1");
            msg.setDeleteFlag("0");
            msg.setType("库存预警");
            msgMapper.insert(msg);
        });
    }

    /**
     * 尺码库存内部类
     */
    @Data
    private static class SizeStock {
        private final String sizeName;
        private final Integer stock;
    }
}
