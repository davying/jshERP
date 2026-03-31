package com.jsh.erp.datasource.entities.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
@ContentStyle(borderBottom = BorderStyleEnum.THIN, borderLeft = BorderStyleEnum.THIN, borderRight = BorderStyleEnum.THIN, borderTop = BorderStyleEnum.THIN)
public class WarehouseInExportVO {
    @ColumnWidth(value = 8)
    @ExcelProperty(value = {"入库记录", "序号"}, index = 0)
    private Integer ids;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "类型"}, index = 1)
    private String type;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = {"入库记录", "货品信息"}, index = 2)
    private String commodityNo;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "颜色"}, index = 3)
    private String color;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "S"}, index = 4)
    private Integer size1;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "M"}, index = 5)
    private Integer size2;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "L"}, index = 6)
    private Integer size3;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "XL"}, index = 7)
    private Integer size4;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "2XL"}, index = 8)
    private Integer size5;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "3XL"}, index = 9)
    private Integer size6;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "4XL"}, index = 10)
    private Integer size7;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "5XL"}, index = 11)
    private Integer size8;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "6XL"}, index = 12)
    private Integer size9;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "7XL"}, index = 13)
    private Integer size10;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "XS"}, index = 14)
    private Integer size11;

    @ColumnWidth(value = 10)
    @ExcelProperty(value = {"入库记录", "数量"}, index = 15)
    private Integer numbers;

    @ColumnWidth(value = 20)

    @ExcelProperty(value = {"入库记录", "到货时间"}, index = 16)
    private String inTime;

    @ColumnWidth(value = 15)
    @ExcelProperty(value = {"入库记录", "单价(元)"}, index = 17)
    private String price;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = {"入库记录", "入库备注"}, index = 18)
    private String remark;
}
