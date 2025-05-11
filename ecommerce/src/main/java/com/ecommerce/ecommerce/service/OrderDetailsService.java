package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.OrderDetails;
import com.ecommerce.ecommerce.model.Orders;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.repository.OrderDetailsRepository;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.repository.ProductsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderDetailsService {
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderRepository orderRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository, OrderRepository orderRepository, ProductsRepository productsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderRepository = orderRepository;
        this.productsRepository = productsRepository;
    }

    public List<OrderDetails>getAllOrderDetail(){
        log.info("Fetching retrieves all orders detail");
        return orderDetailsRepository.findAll();
    }

    public OrderDetails getOrderDetailById(Long id){
        log.info("Fetching order detail with ID: {}", id);

        return orderDetailsRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Order detail not found with ID: {} ",id);
                   return new ResourceNotFoundException("Details order not found eith ID: " + id);
                });
    }


    public void deleteOrderDetail(Long id){
        log.info("Attempting to delete order detail with ID: {}", id);

        if (!orderDetailsRepository.existsById(id)){
            log.warn("Order detail with ID {} not found. Cannot delete.", id);
            throw new ResourceNotFoundException("Order details not found with ID: " + id);
        }

        orderDetailsRepository.deleteById(id);
        log.info("Order detail with ID {} successfully deleed", id);
    }

}
