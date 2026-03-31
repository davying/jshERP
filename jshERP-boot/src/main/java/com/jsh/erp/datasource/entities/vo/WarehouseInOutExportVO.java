package com.jsh.erp.datasource.entities.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import lombok.Data;

/**
 * 库存盘点
 */
@Data
@ExcelIgnoreUnannotated
@ContentStyle(borderBottom = BorderStyleEnum.THIN, borderLeft = BorderStyleEnum.THIN, borderRight = BorderStyleEnum.THIN, borderTop = BorderStyleEnum.THIN)
public class WarehouseInOutExportVO {
    @ColumnWidth(value = 8)
    @ExcelProperty(value = {"库存盘点", "序号"}, index = 0)
    private Integer ids;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = {"库存盘点", "货品信息"}, index = 1)
    private String commodityNo;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "颜色"}, index = 2)
    private String color;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "S"}, index = 3)
    private Integer size1;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "M"}, index = 4)
    private Integer size2;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "L"}, index = 5)
    private Integer size3;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "XL"}, index = 6)
    private Integer size4;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "2XL"}, index = 7)
    private Integer size5;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "3XL"}, index = 8)
    private Integer size6;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "4XL"}, index = 9)
    private Integer size7;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "5XL"}, index = 10)
    private Integer size8;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "6XL"}, index = 11)
    private Integer size9;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "7XL"}, index = 12)
    private Integer size10;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "XS"}, index = 13)
    private Integer size11;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"库存盘点", "数量"}, index = 14)
    private Integer numbers;
}
