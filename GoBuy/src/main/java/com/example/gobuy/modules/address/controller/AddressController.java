package com.example.gobuy.modules.address.controller;

import com.example.gobuy.common.result.Result;
import com.example.gobuy.common.utils.UserContextHolder;
import com.example.gobuy.modules.address.assembler.AddressAssembler;
import com.example.gobuy.modules.address.dto.AddressDTO;
import com.example.gobuy.modules.address.entity.Address;
import com.example.gobuy.modules.address.service.IAddressService;
import com.example.gobuy.modules.address.vo.AddressVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "地址管理", description = "收货地址管理相关接口")
@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;
    private final AddressAssembler addressAssembler;

    @GetMapping("/my")
    @Operation(summary = "获取当前用户的地址列表", description = "获取当前登录用户的所有收货地址")
    @SecurityRequirement(name = "Bearer Authentication")
    public Result<List<AddressVO>> listMyAddresses() {
        Long userId = UserContextHolder.getRequiredUserId();
        List<Address> list = addressService.listMyAddresses(userId);
        return Result.success(addressAssembler.toVOList(list));
    }

    @PutMapping("/{id}/default")
    @Operation(summary = "设置为默认地址", description = "将指定地址设置为默认收货地址")
    @SecurityRequirement(name = "Bearer Authentication")
    public Result<Void> setDefaultAddress(
            @Parameter(description = "地址 ID", example = "1") @PathVariable("id") Long id) {
        Long userId = UserContextHolder.getRequiredUserId();
        addressService.setDefaultAddress(userId, id);
        return Result.success();
    }

    @GetMapping("/default")
    @Operation(summary = "获取默认地址", description = "获取当前登录用户的默认收货地址")
    @SecurityRequirement(name = "Bearer Authentication")
    public Result<AddressVO> getDefaultAddress() {
        Long userId = UserContextHolder.getRequiredUserId();
        Address address = addressService.getDefaultAddress(userId);
        if (address == null) {
            return Result.fail(404, "未找到默认地址");
        }
        return Result.success(addressAssembler.toVO(address));
    }

    @PostMapping
    @Operation(summary = "创建收货地址", description = "为当前登录用户添加新的收货地址")
    @SecurityRequirement(name = "Bearer Authentication")
    public Result<AddressVO> createAddress(@Valid @RequestBody AddressDTO dto) {
        Long userId = UserContextHolder.getRequiredUserId();
        Address entity = addressService.createAddress(userId, dto);
        return Result.success(addressAssembler.toVO(entity));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新收货地址", description = "更新指定收货地址的信息")
    @SecurityRequirement(name = "Bearer Authentication")
    public Result<AddressVO> updateAddress(
            @Parameter(description = "地址 ID", example = "1") @PathVariable("id") Long id,
            @Valid @RequestBody AddressDTO dto) {
        Long userId = UserContextHolder.getRequiredUserId();
        Address entity = addressService.updateAddress(userId, id, dto);
        return Result.success(addressAssembler.toVO(entity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除收货地址", description = "删除指定的收货地址")
    @SecurityRequirement(name = "Bearer Authentication")
    public Result<Void> deleteAddress(
            @Parameter(description = "地址 ID", example = "1") @PathVariable("id") Long id) {
        Long userId = UserContextHolder.getRequiredUserId();
        addressService.deleteAddress(userId, id);
        return Result.success();
    }
}
