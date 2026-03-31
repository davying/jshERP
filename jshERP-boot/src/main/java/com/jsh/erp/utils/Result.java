package com.jsh.erp.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description: TODO
 * @Author huangkaizheng
 * @Date 2021/6/8
 * @Version V1.0
 **/
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {


    private static final long serialVersionUID = 1L;
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILED = "FAILED";
    private static final String SUCCESS_CODE = "1";
    private static final String FAILED_CODE = "0";

    private Object msg;
    private String code;
    private T data;
    private String error = null;
    private Integer status = 200;
    private Integer size = 0;


    //加密前对象类型
    private String encryptForClass;
    //使用加密方法 默认AES/ECB/PKCS5Padding
    private String encryptMethod;

    //默认不加密
    private Boolean isEncrypt = false;

    private Long timestamp = System.currentTimeMillis();

    public Result(String msg, String code, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
        this.status = 200;
        this.error = null;

    }

    public Result(String msg, String code) {
        this.msg = msg;
        this.code = code;
        this.status = 500;
        this.error = msg;
    }

    /**
     * 不加密返回结果对象
     *
     * @param data
     * @return
     */
    public static Result success(Object data) {
        return new Result(SUCCESS, SUCCESS_CODE, data);
    }


    /**
     * 失败响应结果对象
     *
     * @param msg
     * @return
     */
    public static Result failed(String msg) {
        return new Result(msg, FAILED_CODE);
    }
}
