package com.microservice.order_service.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.order_service.client.InventoryClient;
import com.microservice.order_service.dto.OrderRequest;
import com.microservice.order_service.model.Order;
import com.microservice.order_service.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryClient inventoryClient;
    
    public void placeOrder(OrderRequest orderRequest) {

        var isProductinStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (isProductinStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);
        }
        else
            throw new RuntimeException("Product with skuCode " + orderRequest.skuCode() + " is not in stock.");
        
    }
}
