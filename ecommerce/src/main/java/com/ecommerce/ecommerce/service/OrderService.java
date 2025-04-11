package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.exception.BusinessException;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.*;
import com.ecommerce.ecommerce.repository.CartRepository;
import com.ecommerce.ecommerce.repository.OrderDetailsRepository;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.enums.OrderStatus;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final UserRepository userRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, OrderDetailsRepository orderDetailsRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.userRepository = userRepository;
    }


    public Orders createOrder(Orders orders){
        return orderRepository.save(orders);
    }


    public Orders createOrderFromCart(Long userId){
        Users users=userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with ID: " + userId));

        Cart cart=cartRepository.findByUser_Id(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart not found user with ID: " + userId));

        List<Products>cartProducts=cart.getProduct();
        if (cartProducts.isEmpty()){
            throw new BusinessException("Cart is empty. cannot create order.");
        }

        Orders orders= new Orders();
        orders.setOrderStatus(OrderStatus.PAID);
        Orders savedOrder= orderRepository.save(orders);

        for (Products products : cartProducts){
            OrderDetails orderDetails= new OrderDetails();
            orderDetails.setOrder(savedOrder);
            orderDetails.setProduct(products);
            orderDetails.setUnitPrice(products.getPrice());
            orderDetailsRepository.save(orderDetails);
        }

        cart.getProduct().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    public Orders getOrderById(Long id){
        return orderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Order not found with ID:" + id));
    }

    public List<Orders>getAllOrders(){
        return orderRepository.findAll();
    }

    public List<Orders>getOrdersByUserId(Long userId){
        return orderRepository.findByUser_Id(userId);
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
