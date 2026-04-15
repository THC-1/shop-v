package com.example.gobuy.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.modules.admin.assembler.AdminBrandAssembler;
import com.example.gobuy.modules.admin.dto.BrandCreateDTO;
import com.example.gobuy.modules.admin.dto.BrandQueryDTO;
import com.example.gobuy.modules.admin.dto.BrandUpdateDTO;
import com.example.gobuy.modules.admin.service.IAdminBrandService;
import com.example.gobuy.modules.admin.vo.BrandDetailVO;
import com.example.gobuy.modules.admin.vo.BrandVO;
import com.example.gobuy.modules.product.entity.Brand;
import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.product.mapper.BrandMapper;
import com.example.gobuy.modules.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminBrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IAdminBrandService {

    private final AdminBrandAssembler assembler;
    private final ProductMapper productMapper;

    @Override
    public IPage<BrandVO> listBrands(BrandQueryDTO queryDTO) {
        LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getName())) {
            wrapper.like(Brand::getName, queryDTO.getName());
        }
        wrapper.orderByDesc(Brand::getCreatedAt);

        IPage<Brand> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        IPage<Brand> result = page(page, wrapper);

        List<Long> brandIds = result.getRecords().stream().map(Brand::getId).collect(Collectors.toList());
        Map<Long, Long> productCountMap = batchCountProducts(brandIds);

        List<BrandVO> voList = result.getRecords().stream().map(brand -> {
            BrandVO vo = assembler.toVO(brand);
            vo.setProductCount(productCountMap.getOrDefault(brand.getId(), 0L).intValue());
            return vo;
        }).collect(Collectors.toList());

        IPage<BrandVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public BrandDetailVO getBrandDetail(Long id) {
        Brand brand = getById(id);
        if (brand == null) {
            throw new BusinessException(404, "品牌不存在");
        }
        BrandDetailVO vo = assembler.toDetailVO(brand);
        vo.setProductCount(countProducts(id));
        return vo;
    }

    @Override
    public Long createBrand(BrandCreateDTO dto) {
        LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<Brand>().eq(Brand::getName, dto.getName());
        if (count(wrapper) > 0) {
            throw new BusinessException(409, "品牌名称已存在");
        }
        Brand brand = assembler.toEntity(dto);
        save(brand);
        return brand.getId();
    }

    @Override
    public void updateBrand(Long id, BrandUpdateDTO dto) {
        Brand brand = getById(id);
        if (brand == null) {
            throw new BusinessException(404, "品牌不存在");
        }
        if (StringUtils.hasText(dto.getName()) && !dto.getName().equals(brand.getName())) {
            LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<Brand>().eq(Brand::getName, dto.getName());
            if (count(wrapper) > 0) {
                throw new BusinessException(409, "品牌名称已存在");
            }
        }
        assembler.updateEntity(brand, dto);
        updateById(brand);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBrand(Long id) {
        Brand brand = getById(id);
        if (brand == null) {
            throw new BusinessException(404, "品牌不存在");
        }
        if (countProducts(id) > 0) {
            throw new BusinessException(422, "该品牌下存在商品，无法删除");
        }
        removeById(id);
    }

    private Map<Long, Long> batchCountProducts(List<Long> brandIds) {
        if (brandIds.isEmpty()) {
            return Map.of();
        }
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                .in(Product::getBrandId, brandIds)
                .select(Product::getBrandId);
        List<Product> products = productMapper.selectList(wrapper);
        return products.stream()
                .collect(Collectors.groupingBy(Product::getBrandId, Collectors.counting()));
    }

    private int countProducts(Long brandId) {
        return Math.toIntExact(productMapper.selectCount(new LambdaQueryWrapper<Product>().eq(Product::getBrandId, brandId)));
    }
}
