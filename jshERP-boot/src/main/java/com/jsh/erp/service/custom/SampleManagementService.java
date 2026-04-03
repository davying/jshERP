package com.jsh.erp.service.custom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsh.erp.datasource.entitie.custom.SampleManagement;

import java.util.List;

/**
 * <p>
 * 样品记录表 服务类
 * </p>
 *
 * @author dawei
 * @since 2023-07-11 19:52:53
 */
public interface SampleManagementService extends IService<SampleManagement> {
    IPage<SampleManagement> getSampleInOutFinalList(SampleManagement wm);

    List<SampleManagement> getSampleInOutFinalListNoPage(SampleManagement wm);
}
