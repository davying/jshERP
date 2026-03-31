package com.jsh.erp.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ResultForPage<T> implements Serializable {

    private static final long serialVersionUID = -133L;

    private String code;//返回结果 0 失败 1 成功

    private String msg;//返回消息

    /** 当前页 */
    private long page;

    /** 总数据量 */
    private long total;

    /** 总页数 */
    private long totalPages;

    private List<T> data;

    private Object ext;

    public static ResultForPage buildSuccess(IPage resultPage) {

        ResultForPage result=new ResultForPage();

        result.setCode("1");
        result.setMsg("查询成功");
        result.setData(resultPage.getRecords());
        result.setTotal(((Long)resultPage.getTotal()).intValue());
        result.setPage((int) resultPage.getCurrent());
        result.setTotalPages((int)resultPage.getPages());
        return result;

    }
    public static ResultForPage buildSuccess(Page resultPage) {

        ResultForPage result=new ResultForPage();

        result.setCode("1");
        result.setMsg("查询成功");
        result.setData(resultPage.getRecords());
        result.setTotal(((Long)resultPage.getTotal()).intValue());
        result.setPage(resultPage.getPages());
        result.setTotalPages(resultPage.getPages());
        return result;

    }
    public static ResultForPage buildSuccess(Page resultPage,Object ext) {

        ResultForPage result=new ResultForPage();
        result.setExt(ext);

        result.setCode("1");
        result.setMsg("查询成功");
        result.setData(resultPage.getRecords());
        result.setTotal(((Long)resultPage.getTotal()).intValue());
        result.setPage(resultPage.getPages());
        result.setTotalPages(resultPage.getPages());
        return result;

    }
    public static ResultForPage buildSuccess(IPage resultPage, Object ext) {

        ResultForPage result=new ResultForPage();
        result.setExt(ext);
        result.setCode("1");
        result.setMsg("查询成功");
        result.setData(resultPage.getRecords());
        result.setTotal(((Long)resultPage.getTotal()).intValue());
        result.setPage((int) resultPage.getCurrent());
        result.setTotalPages((int)resultPage.getPages());
        return result;

    }
    public static ResultForPage buildFailed(String message) {

        ResultForPage result=new ResultForPage();

        result.setCode("0");
        result.setMsg(message);

        return result;

    }

    public static ResultForPage buildSuccess(String message) {

        ResultForPage result=new ResultForPage();

        result.setCode("1");
        result.setMsg(message);

        return result;

    }

    public static ResultForPage buildForService(String code,String message) {

        ResultForPage result=new ResultForPage();

        result.setCode(code);
        result.setMsg(message);

        return result;

    }

}
