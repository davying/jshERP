package com.jsh.erp.datasource.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("jsh_material_property")
@ApiModel(value = "商品扩展信息对象", description = "商品扩展信息表")
public class MaterialProperty {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String nativeName;
    private Boolean enabled;
    private String sort;
    private String anotherName;
    private String deleteFlag;
}