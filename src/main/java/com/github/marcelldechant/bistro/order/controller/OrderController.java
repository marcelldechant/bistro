package com.github.marcelldechant.bistro.order.controller;

import com.github.marcelldechant.bistro.order.api.OrderApi;
import com.github.marcelldechant.bistro.order.dto.CreateOrderDto;
import com.github.marcelldechant.bistro.order.dto.OrderResponseDto;
import com.github.marcelldechant.bistro.order.entity.Order;
import com.github.marcelldechant.bistro.order.service.OrderService;
import com.github.marcelldechant.bistro.order.util.ReceiptFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController implements OrderApi {
    private final OrderService orderService;

    @Override
    public OrderResponseDto createOrder(CreateOrderDto createOrderDto) {
        return orderService.createOrder(createOrderDto);
    }

    @Override
    public OrderResponseDto getOrderById(long id) {
        return orderService.getOrderById(id);
    }

    @Override
    public String getReceiptByOrderId(long id) {
        Order order = orderService.getOrderByIdEntity(id);
        return ReceiptFormatter.format(order);
    }
}
