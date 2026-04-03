package com.jsh.erp.service.custom.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsh.erp.datasource.entitie.custom.SampleManagement;
import com.jsh.erp.datasource.mappers.custom.SampleManagementMapper;
import com.jsh.erp.service.custom.SampleManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 样品记录表 服务实现类
 * </p>
 *
 * @author dawei
 * @since 2023-07-11 19:52:53
 */
@Service
public class SampleManagementServiceImpl extends ServiceImpl<SampleManagementMapper, SampleManagement> implements SampleManagementService {

    @Autowired
    private SampleManagementMapper sampleManagementMapper;

    @Override
    public IPage<SampleManagement> getSampleInOutFinalList(SampleManagement wm) {
        Page<SampleManagement> page = new Page<>(wm.getCurrentPage(), wm.getPageSize());
        IPage<SampleManagement> res = sampleManagementMapper.getSampleInOutFinalList(page, wm);
        return res;
    }

    @Override
    public List<SampleManagement> getSampleInOutFinalListNoPage(SampleManagement wm) {
        List<SampleManagement> res = sampleManagementMapper.getSampleInOutFinalListNoPage(wm);
        return res;
    }

}
