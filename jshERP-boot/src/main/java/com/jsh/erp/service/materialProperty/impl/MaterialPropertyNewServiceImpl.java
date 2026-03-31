package com.jsh.erp.service.materialProperty.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsh.erp.datasource.entities.MaterialProperty;
import com.jsh.erp.datasource.mappers.custom.MaterialPropertyNewMapper;
import com.jsh.erp.service.materialProperty.MaterialPropertyNewService;
import org.springframework.stereotype.Service;

@Service
public class MaterialPropertyNewServiceImpl extends ServiceImpl<MaterialPropertyNewMapper, MaterialProperty> implements MaterialPropertyNewService {

}
