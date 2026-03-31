package com.jsh.erp.datasource.entities.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ExcelIgnoreUnannotated
@ContentStyle(borderBottom = BorderStyleEnum.THIN, borderLeft = BorderStyleEnum.THIN, borderRight = BorderStyleEnum.THIN, borderTop = BorderStyleEnum.THIN)
public class SalesManExportVO {
    @ColumnWidth(value = 8)
    @ExcelProperty(value = {"业务员汇总表", "序号"}, index = 0)
    private Integer ids;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = {"业务员汇总表", "业务员"}, index = 1)
    private String salesMan;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = {"业务员汇总表", "年份"}, index = 2)
    private String years;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = {"业务员汇总表", "数量"}, index = 3)
    private Integer numbers;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = {"业务员汇总表", "金额"}, index = 4)
    private BigDecimal amount;

    private Integer depots;
}
