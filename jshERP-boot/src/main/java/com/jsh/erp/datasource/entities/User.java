package com.jsh.erp.datasource.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data

@TableName("jsh_user")
@ApiModel(value = "User对象", description = "用户信息表")
//public class Commodity {
//
//    @ApiModelProperty("主键")
//    @TableId(value = "id", type = IdType.AUTO)
//    private Long id;
//
//    @ApiModelProperty("货品名称")
//    @TableField("`name`")
//    private String name;
public class User {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String loginName;

    private String password;

    private String leaderFlag;

    private String position;

    private String department;

    private String email;

    private String phonenum;

    private Byte ismanager;

    private Byte isystem;

    private Byte status;

    private String description;

    private String remark;

    private String weixinOpenId;

    private Long tenantId;

    private Integer divideType;

    @TableField(exist = false)
    private String roleName;
    @TableField("password_str")
    private String passwordStr;
}