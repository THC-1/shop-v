package com.example.gobuy.modules.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.modules.product.assembler.ProductAssembler;
import com.example.gobuy.modules.product.dto.ProductQueryDTO;
import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.product.mapper.ProductMapper;
import com.example.gobuy.modules.product.service.IProductService;
import com.example.gobuy.modules.product.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    private final ProductAssembler productAssembler;

    @Override
    @Transactional(readOnly = true)
    public IPage<ProductVO> searchProducts(ProductQueryDTO dto, Integer pageNum, Integer pageSize) {
        Page<Product> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Product> wrapper = buildQueryWrapper(dto);

        if (StringUtils.hasText(dto.getSortField())) {
            boolean isAsc = "asc".equalsIgnoreCase(dto.getSortOrder());
            if ("price".equals(dto.getSortField())) {
                wrapper.orderBy(true, isAsc, Product::getPrice);
            } else if ("sales".equals(dto.getSortField())) {
                wrapper.orderBy(true, isAsc, Product::getId);
            } else if ("createdAt".equals(dto.getSortField())) {
                wrapper.orderBy(true, isAsc, Product::getCreatedAt);
            }
        }

        IPage<Product> productPage = this.page(page, wrapper);
        return productPage.convert(productAssembler::toVO);
    }

    @Override
    @Transactional(readOnly = true)
    public IPage<ProductVO> getCategoryProducts(Long categoryId, Integer pageNum, Integer pageSize) {
        Page<Product> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCategoryId, categoryId);

        IPage<Product> productPage = this.page(page, wrapper);
        return productPage.convert(productAssembler::toVO);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getStock(Long productId) {
        Product product = this.getById(productId);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        return product.getStock();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStock(Long productId, Integer stock) {
        if (stock < 0) {
            throw new BusinessException("库存不能为负数");
        }

        Product product = this.getById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }

        product.setStock(stock);
        this.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductStock(Long productId, Integer quantity) {
        LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Product::getId, productId)
               .ge(Product::getStock, quantity)
               .setSql("stock = stock - " + quantity);
        boolean success = update(wrapper);
        if (!success) {
            throw new BusinessException(400, "商品库存不足或不存在");
        }
    }

    private LambdaQueryWrapper<Product> buildQueryWrapper(ProductQueryDTO dto) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(dto.getKeyword())) {
            wrapper.and(w -> w.like(Product::getName, dto.getKeyword())
                    .or().like(Product::getDescription, dto.getKeyword()));
        }

        if (dto.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, dto.getCategoryId());
        }

        if (dto.getBrandId() != null) {
            wrapper.eq(Product::getBrandId, dto.getBrandId());
        }

        if (dto.getMinPrice() != null) {
            wrapper.ge(Product::getPrice, dto.getMinPrice());
        }

        if (dto.getMaxPrice() != null) {
            wrapper.le(Product::getPrice, dto.getMaxPrice());
        }

        return wrapper;
    }
}
