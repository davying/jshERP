package com.jsh.erp.datasource.mappers.custom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsh.erp.datasource.entitie.custom.SampleManagement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 样品记录表 Mapper 接口
 * </p>
 *
 * @author dawei
 * @since 2023-07-11 19:52:53
 */
public interface SampleManagementMapper extends BaseMapper<SampleManagement> {
    IPage<SampleManagement> getSampleInOutFinalList(Page page, @Param("request") SampleManagement param);

    List<SampleManagement> getSampleInOutFinalListNoPage(@Param("request") SampleManagement param);

    List<SampleManagement> getSampleInOutFinalList(@Param("request") SampleManagement param);
}
