package com.example.gobuy.modules.cart.assembler;

import com.example.gobuy.modules.cart.entity.Cart;
import com.example.gobuy.modules.cart.dto.CartAddDTO;
import com.example.gobuy.modules.cart.vo.CartItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartAssembler {

    Cart toEntity(CartAddDTO dto);

    CartItemVO toCartItemVO(Cart cart);

    List<CartItemVO> toCartItemVOList(List<Cart> cartList);
}
