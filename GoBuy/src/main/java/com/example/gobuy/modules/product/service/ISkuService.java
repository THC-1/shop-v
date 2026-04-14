package com.example.gobuy.modules.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.gobuy.modules.product.entity.Sku;
import java.util.List;

public interface ISkuService extends IService<Sku> {
    List<Sku> getSkusByProductId(Long productId);
}