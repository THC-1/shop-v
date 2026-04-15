package com.example.gobuy.modules.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.gobuy.modules.admin.dto.AdminProductCreateDTO;
import com.example.gobuy.modules.admin.dto.AdminProductQueryDTO;
import com.example.gobuy.modules.admin.dto.AdminProductUpdateDTO;
import com.example.gobuy.modules.admin.dto.BatchStatusDTO;
import com.example.gobuy.modules.admin.vo.AdminProductDetailVO;
import com.example.gobuy.modules.admin.vo.AdminProductVO;
import com.example.gobuy.modules.admin.vo.BatchStatusResult;
import com.example.gobuy.modules.product.entity.Product;

public interface IAdminProductService extends IService<Product> {
    IPage<AdminProductVO> listProducts(AdminProductQueryDTO queryDTO);
    AdminProductDetailVO getProductDetail(Long id);
    Long createProduct(AdminProductCreateDTO dto);
    void updateProduct(Long id, AdminProductUpdateDTO dto);
    void deleteProduct(Long id);
    void updateStatus(Long id, String status);
    BatchStatusResult batchUpdateStatus(BatchStatusDTO dto);
}
