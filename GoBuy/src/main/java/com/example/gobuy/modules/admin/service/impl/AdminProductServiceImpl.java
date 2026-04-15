package com.example.gobuy.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.modules.admin.assembler.AdminProductAssembler;
import com.example.gobuy.modules.admin.dto.AdminProductCreateDTO;
import com.example.gobuy.modules.admin.dto.AdminProductQueryDTO;
import com.example.gobuy.modules.admin.dto.AdminProductUpdateDTO;
import com.example.gobuy.modules.admin.dto.BatchStatusDTO;
import com.example.gobuy.modules.admin.service.IAdminProductService;
import com.example.gobuy.modules.admin.vo.AdminProductDetailVO;
import com.example.gobuy.modules.admin.vo.AdminProductVO;
import com.example.gobuy.modules.admin.vo.BatchStatusResult;
import com.example.gobuy.modules.product.entity.Brand;
import com.example.gobuy.modules.product.entity.Category;
import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.product.mapper.BrandMapper;
import com.example.gobuy.modules.product.mapper.CategoryMapper;
import com.example.gobuy.modules.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IAdminProductService {

    private final AdminProductAssembler assembler;
    private final CategoryMapper categoryMapper;
    private final BrandMapper brandMapper;

    @Override
    public IPage<AdminProductVO> listProducts(AdminProductQueryDTO queryDTO) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getName())) {
            wrapper.like(Product::getName, queryDTO.getName());
        }
        if (queryDTO.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, queryDTO.getCategoryId());
        }
        if (queryDTO.getBrandId() != null) {
            wrapper.eq(Product::getBrandId, queryDTO.getBrandId());
        }
        if (StringUtils.hasText(queryDTO.getStatus())) {
            wrapper.eq(Product::getStatus, queryDTO.getStatus());
        }
        wrapper.orderByDesc(Product::getCreatedAt);

        IPage<Product> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        IPage<Product> result = page(page, wrapper);

        List<Product> records = result.getRecords();
        Map<Long, String> categoryMap = getCategoryNameMap();
        Map<Long, String> brandMap = getBrandNameMap();

        List<AdminProductVO> voList = records.stream().map(p -> {
            AdminProductVO vo = assembler.toVO(p);
            vo.setCategoryName(categoryMap.getOrDefault(p.getCategoryId(), ""));
            vo.setBrandName(brandMap.getOrDefault(p.getBrandId(), ""));
            return vo;
        }).collect(Collectors.toList());

        IPage<AdminProductVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public AdminProductDetailVO getProductDetail(Long id) {
        Product product = getById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        AdminProductDetailVO vo = assembler.toDetailVO(product);
        vo.setCategoryName(getCategoryName(product.getCategoryId()));
        vo.setBrandName(getBrandName(product.getBrandId()));
        return vo;
    }

    @Override
    public Long createProduct(AdminProductCreateDTO dto) {
        Product product = assembler.toEntity(dto);
        product.setStatus("ON_SALE");
        product.setSalesCount(0);
        save(product);
        return product.getId();
    }

    @Override
    public void updateProduct(Long id, AdminProductUpdateDTO dto) {
        Product product = getById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        assembler.updateEntity(product, dto);
        updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        if (!removeById(id)) {
            throw new BusinessException(404, "商品不存在");
        }
    }

    @Override
    public void updateStatus(Long id, String status) {
        Product product = getById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        product.setStatus(status);
        updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchStatusResult batchUpdateStatus(BatchStatusDTO dto) {
        if (dto.getProductIds().size() > 100) {
            throw new BusinessException(400, "单次批量操作最多100条记录");
        }
        BatchStatusResult result = new BatchStatusResult();
        result.setSuccessCount(0);
        result.setFailCount(0);
        result.setFailIds(new ArrayList<>());

        for (Long id : dto.getProductIds()) {
            try {
                updateStatus(id, dto.getStatus());
                result.setSuccessCount(result.getSuccessCount() + 1);
            } catch (Exception e) {
                result.setFailCount(result.getFailCount() + 1);
                result.getFailIds().add(id);
            }
        }
        return result;
    }

    private Map<Long, String> getCategoryNameMap() {
        List<Category> categories = categoryMapper.selectList(null);
        return categories.stream().collect(Collectors.toMap(Category::getId, Category::getName, (a, b) -> a));
    }

    private Map<Long, String> getBrandNameMap() {
        List<Brand> brands = brandMapper.selectList(null);
        return brands.stream().collect(Collectors.toMap(Brand::getId, Brand::getName, (a, b) -> a));
    }

    private String getCategoryName(Long categoryId) {
        if (categoryId == null) return "";
        Category category = categoryMapper.selectById(categoryId);
        return category != null ? category.getName() : "";
    }

    private String getBrandName(Long brandId) {
        if (brandId == null) return "";
        Brand brand = brandMapper.selectById(brandId);
        return brand != null ? brand.getName() : "";
    }
}
