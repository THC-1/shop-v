package com.example.gobuy.modules.product.service;

import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.product.dto.ProductQueryDTO;
import com.example.gobuy.modules.product.vo.ProductVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 产品表 服务接口
 */
public interface IProductService extends IService<Product> {

    /**
     * 搜索商品
     * @param dto 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 商品分页列表
     */
    IPage<ProductVO> searchProducts(ProductQueryDTO dto, Integer pageNum, Integer pageSize);

    /**
     * 获取分类下的商品
     * @param categoryId 分类 ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 商品分页列表
     */
    IPage<ProductVO> getCategoryProducts(Long categoryId, Integer pageNum, Integer pageSize);

    /**
     * 获取商品库存
     * @param productId 商品 ID
     * @return 库存数量
     */
    Integer getStock(Long productId);

    /**
     * 更新商品库存
     * @param productId 商品 ID
     * @param stock 库存数量
     */
    void updateStock(Long productId, Integer stock);

    void deductStock(Long productId, Integer quantity);
}