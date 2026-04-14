package com.example.gobuy.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.common.result.Result;
import com.example.gobuy.modules.address.entity.Address;
import com.example.gobuy.modules.address.mapper.AddressMapper;
import com.example.gobuy.modules.order.assembler.OrderAssembler;
import com.example.gobuy.modules.order.dto.OrderCreateDTO;
import com.example.gobuy.modules.order.dto.OrderItemCreateDTO;
import com.example.gobuy.modules.order.entity.Order;
import com.example.gobuy.modules.order.entity.OrderItem;
import com.example.gobuy.modules.order.mapper.OrderItemMapper;
import com.example.gobuy.modules.order.mapper.OrderMapper;
import com.example.gobuy.modules.order.service.IOrderService;
import com.example.gobuy.modules.order.vo.OrderDetailVO;
import com.example.gobuy.modules.order.vo.OrderVO;
import com.example.gobuy.modules.product.entity.Product;
import com.example.gobuy.modules.product.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final OrderItemMapper orderItemMapper;
    private final OrderAssembler orderAssembler;
    private final AddressMapper addressMapper;
    private final IProductService productService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderDetailVO> createOrder(Long userId, OrderCreateDTO dto) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemCreateDTO itemDTO : dto.getItems()) {
            Product product = productService.getById(itemDTO.getProductId());
            if (product == null) {
                throw new BusinessException(404, "商品不存在，ID: " + itemDTO.getProductId());
            }
            if (product.getStock() < itemDTO.getQuantity()) {
                throw new BusinessException(400, "商品库存不足: " + product.getName());
            }
            itemDTO.setProductName(product.getName());
            itemDTO.setPrice(product.getPrice());
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
        }

        for (OrderItemCreateDTO itemDTO : dto.getItems()) {
            productService.deductStock(itemDTO.getProductId(), itemDTO.getQuantity());
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(dto.getAddressId());
        order.setNote(dto.getNote());
        order.setTotalAmount(totalAmount);
        order.setOrderNo(generateOrderNo());
        order.setStatus("PENDING_PAYMENT");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        save(order);

        for (OrderItemCreateDTO itemDTO : dto.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(itemDTO.getProductId());
            orderItem.setSkuId(itemDTO.getSkuId());
            orderItem.setProductName(itemDTO.getProductName());
            orderItem.setPrice(itemDTO.getPrice());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setUpdatedAt(LocalDateTime.now());
            orderItemMapper.insert(orderItem);
        }

        return getOrderDetail(userId, order.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Result<IPage<OrderVO>> listOrders(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, getStatusName(status));
        }
        wrapper.orderByDesc(Order::getCreatedAt);

        IPage<Order> orderPage = page(page, wrapper);
        IPage<OrderVO> voPage = orderPage.convert(orderAssembler::toVO);

        return Result.success(voPage);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<OrderDetailVO> getOrderDetail(Long userId, Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权查看此订单");
        }

        OrderDetailVO detailVO = new OrderDetailVO();
        detailVO.setId(order.getId());
        detailVO.setOrderNo(order.getOrderNo());
        detailVO.setUserId(order.getUserId());
        detailVO.setAddressId(order.getAddressId());
        detailVO.setTotalAmount(order.getTotalAmount());
        detailVO.setStatus(order.getStatus());
        detailVO.setNote(order.getNote());
        detailVO.setCreatedAt(order.getCreatedAt());
        detailVO.setUpdatedAt(order.getUpdatedAt());

        if (order.getAddressId() != null) {
            Address address = addressMapper.selectById(order.getAddressId());
            if (address != null) {
                detailVO.setReceiverName(address.getReceiverName());
                detailVO.setReceiverPhone(address.getPhone());
                detailVO.setReceiverAddress(buildFullAddress(address));
            }
        }

        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(itemWrapper);
        List<OrderDetailVO.OrderItemDetailVO> itemVOs = orderItems.stream()
                .map(this::convertToItemDetailVO)
                .toList();
        detailVO.setItems(itemVOs);

        return Result.success(detailVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> cancelOrder(Long userId, Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此订单");
        }
        if (!"PENDING_PAYMENT".equals(order.getStatus()) && !"PENDING_SHIPMENT".equals(order.getStatus())) {
            throw new BusinessException(400, "当前订单状态无法取消");
        }
        order.setStatus("CANCELLED");
        updateById(order);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> confirmOrder(Long userId, Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此订单");
        }
        if (!"SHIPPED".equals(order.getStatus())) {
            throw new BusinessException(400, "只有已发货的订单才能确认收货");
        }
        order.setStatus("DELIVERED");
        updateById(order);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> refundOrder(Long userId, Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此订单");
        }
        if (!"DELIVERED".equals(order.getStatus()) && !"COMPLETED".equals(order.getStatus())) {
            throw new BusinessException(400, "当前订单状态无法申请退款");
        }
        order.setStatus("REFUNDING");
        updateById(order);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteOrder(Long userId, Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此订单");
        }
        if (!"CANCELLED".equals(order.getStatus()) && !"COMPLETED".equals(order.getStatus())) {
            throw new BusinessException(400, "只有已取消或已完成的订单才能删除");
        }
        order.setDeleted(true);
        updateById(order);
        return Result.success();
    }

    private OrderDetailVO.OrderItemDetailVO convertToItemDetailVO(OrderItem item) {
        OrderDetailVO.OrderItemDetailVO vo = new OrderDetailVO.OrderItemDetailVO();
        vo.setId(item.getId());
        vo.setProductId(item.getProductId());
        vo.setSkuId(item.getSkuId());
        vo.setProductName(item.getProductName());
        vo.setQuantity(item.getQuantity());
        vo.setPrice(item.getPrice());
        return vo;
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + (long) (Math.random() * 10000);
    }

    private String getStatusName(Integer status) {
        return switch (status) {
            case 0 -> "PENDING_PAYMENT";
            case 1 -> "PAID";
            case 2 -> "SHIPPED";
            case 3 -> "DELIVERED";
            case 4 -> "COMPLETED";
            case 5 -> "CANCELLED";
            default -> "UNKNOWN";
        };
    }

    private String buildFullAddress(Address address) {
        StringBuilder sb = new StringBuilder();
        if (address.getProvince() != null) {
            sb.append(address.getProvince());
        }
        if (address.getCity() != null) {
            sb.append(address.getCity());
        }
        if (address.getDistrict() != null) {
            sb.append(address.getDistrict());
        }
        if (address.getDetailAddress() != null) {
            sb.append(address.getDetailAddress());
        }
        return sb.toString();
    }
}
