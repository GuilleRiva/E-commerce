package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Orders;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Orders createOrder(Orders orders){
        return orderRepository.save(orders);
    }

    public Orders getOrderById(Long id){
        return orderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Order not found with ID:" + id));
    }

    public List<Orders>getAllOrders(){
        return orderRepository.findAll();
    }

    public List<Orders>getOrdersByUserId(Long userId){
        return orderRepository.findByUserId(userId);
    }

    public Orders updateOrderStatus(Long ordersId, String newStatus){
        Orders orders= getOrderById(ordersId);

        try {
            OrderStatus status= OrderStatus.valueOf(newStatus.toUpperCase());
            orders.setOrderStatus(status);
            return orderRepository.save(orders);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }
    }

    public void deleteOrder(Long id){
        if (!orderRepository.existsById(id)){
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
    }
}
