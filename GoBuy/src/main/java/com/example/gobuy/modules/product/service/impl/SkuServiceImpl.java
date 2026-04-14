package com.example.gobuy.modules.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.gobuy.modules.product.entity.Sku;
import com.example.gobuy.modules.product.mapper.SkuMapper;
import com.example.gobuy.modules.product.service.ISkuService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {

    @Override
    public List<Sku> getSkusByProductId(Long productId) {
        LambdaQueryWrapper<Sku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sku::getProductId, productId);
        return this.list(wrapper);
    }
}