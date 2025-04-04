package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.OrderDetails;
import com.ecommerce.ecommerce.model.Orders;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.repository.OrderDetailsRepository;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return orderDetailsRepository.findAll();
    }

    public OrderDetails getOrderDetailById(Long id){
        return orderDetailsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Details order not found eith ID: " + id));
    }

    public OrderDetails addOrderDetail(Long orderId, Long productId, int quality){
        Orders orders= orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("order not found with ID: "+ orderId));
        Products products= productsRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product not found with ID:" + productId));

        OrderDetails orderDetails= new OrderDetails();
        orderDetails.setOrders(orders);
        orderDetails.setProducts(products);
        orderDetails.setUnitPrice(products.getPrice());

        return orderDetailsRepository.save(orderDetails);
    }

    public void deleteOrderDetail(Long id){
        if (!orderDetailsRepository.existsById(id)){
            throw new ResourceNotFoundException("Order details not found with ID: " + id);
        }
        orderDetailsRepository.deleteById(id);
    }
}
