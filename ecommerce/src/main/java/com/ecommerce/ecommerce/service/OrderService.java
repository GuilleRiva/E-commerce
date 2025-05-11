package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XRequestDTO.OrderDetailRequestDTO;
import com.ecommerce.ecommerce.dto.XResponseDTO.OrderItemDTO;
import com.ecommerce.ecommerce.dto.XRequestDTO.OrderRequestDTO;
import com.ecommerce.ecommerce.dto.XResponseDTO.OrderResponseDTO;
import com.ecommerce.ecommerce.exception.BusinessException;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.*;
import com.ecommerce.ecommerce.repository.*;
import com.ecommerce.ecommerce.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final ProductsRepository productsRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, OrderDetailsRepository orderDetailsRepository, UserRepository userRepository, AddressRepository addressRepository, PaymentMethodsRepository paymentMethodsRepository, ProductsRepository productsRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.paymentMethodsRepository = paymentMethodsRepository;
        this.productsRepository = productsRepository;
    }

    public OrderResponseDTO toOrderResponseDTO(Orders order){

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setStatus(String.valueOf(order.getOrderStatus()));
        dto.setCreatedAt(order.getCreatedAt());
        dto.setTotal(order.getTotal());

        //Convert order details
        log.debug("Starting mapping of Orders entity to OrderResponseDTO for Order ID={}", order.getId());
        List<OrderItemDTO> items = order.getOrderDetails().stream()
                .map(orderDetails -> new OrderItemDTO(
                        orderDetails.getProduct().getName(),
                        orderDetails.getAmount(),
                        orderDetails.getUnitPrice()
                ))
                .collect(Collectors.toList());

        BigDecimal total = items.stream()
                        .map(OrderItemDTO::getPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);


        return new OrderResponseDTO(order.getId(),
                order.getUser().getId(), items, total, order.getOrderStatus(),
                order.getCreatedAt());
    }


    public OrderResponseDTO createOrder(OrderRequestDTO dto){
        log.info("Creating new order for user ID: {}", dto.getUserId());

        Users users= userRepository.findById(dto.getUserId())
                .orElseThrow(()-> {

                    log.error("User not found with ID: {}", dto.getUserId());
                     return new ResourceNotFoundException("User not found with ID:"
                                    + dto.getUserId());
                });

        Address address= addressRepository.findById(dto.getAddressId())
                .orElseThrow(()-> new ResourceNotFoundException("Address not found with ID:"
                + dto.getAddressId()));

        PaymentMethods paymentMethods= paymentMethodsRepository.findById(dto.getPaymentMethodId())
                .orElseThrow(()-> new ResourceNotFoundException("Payment method not found with ID:"
                + dto.getPaymentMethodId()));


        //creates order
        Orders order= new Orders();
        order.setUser(users);
        order.setCreatedAt(LocalDateTime.now());

        Orders savedOrder= orderRepository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getId());

        //creates and save details
        List<OrderDetails>detailsList= new ArrayList<>();

        for (OrderDetailRequestDTO item : dto.getOrderDetail()){
            Products products = productsRepository.findById(item.getProductId())
                    .orElseThrow(()-> new ResourceNotFoundException("Product not found"));

            OrderDetails details= new OrderDetails();
            details.setOrder(savedOrder);
            details.setProduct(products);
            details.setAmount(item.getQuantity());
            details.setUnitPrice(products.getPrice());

            detailsList.add(details);
            log.debug("Added order details: product ID {}, quantity {}, price {}",
                    products.getId(), item.getQuantity(), products.getPrice());

            orderDetailsRepository.saveAll(detailsList);
            savedOrder.setOrderDetails(detailsList);

            log.info("Order (ID: {}) completed with {} items", savedOrder.getId(), detailsList.size());

        }
        return toOrderResponseDTO(savedOrder);
    }


    public Orders createOrderFromCart(Long userId){

        log.info("Starting order creation from cart for user ID: {}", userId);
        Users users=userRepository.findById(userId)
                .orElseThrow(()-> {
                    log.error("Cart not found for user ID: {}", userId);
                       return new ResourceNotFoundException("User not found with ID: " + userId);
                });

        Cart cart=cartRepository.findByUser_Id(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart not found user with ID: " + userId));

        List<Products>cartProducts=cart.getProduct();

        if (cartProducts.isEmpty()){
            log.warn("Cart is empty for user ID: {}",userId);
            throw new BusinessException("Cart is empty. cannot create order.");
        }

        Orders orders= new Orders();
        orders.setUser(users);
        orders.setCreatedAt(LocalDateTime.now());
        orders.setOrderStatus(OrderStatus.PAID);

        Orders savedOrder= orderRepository.save(orders);
        log.info("New order created with ID: {} for user ID: {}",
                savedOrder.getId(), userId);

        List<OrderDetails> orderDetails = new ArrayList<>();

        for (Products products : cartProducts){
            OrderDetails details = new OrderDetails();
            details.setOrder(savedOrder);
            details.setProduct(products);
            details.setAmount(1);

            details.setUnitPrice(products.getPrice());
        }
        orderDetailsRepository.saveAll(orderDetails);
        savedOrder.setOrderDetails(orderDetails);

        cart.getProduct().clear();
        cartRepository.save(cart);
        log.info("Cart cleared after creating order for user ID: {}", userId);

        return savedOrder;
    }


    public Orders getOrderById(Long id){
        log.info("Fetching order with ID: {}", id);

        return orderRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Order not found with ID: {}", id);
                    return new ResourceNotFoundException("Order not found with ID:" + id);
                });
    }


    public List<Orders>getAllOrders(){
        log.info("Fetching retrieves all orders");
        return orderRepository.findAll();
    }

    public List<Orders>getOrdersByUserId(Long userId){
        log.info("Fetching order by user ID: {}", userId);
        return orderRepository.findByUser_Id(userId);
    }


    public Orders updateOrderStatus(Long ordersId, String newStatus){
        log.info("Attempting to update order status. Order ID: {}, New Status: {}",ordersId,newStatus);

        Orders orders= getOrderById(ordersId);

        try {
            OrderStatus status= OrderStatus.valueOf(newStatus.toUpperCase());
            orders.setOrderStatus(status);
            Orders updatedOrder = orderRepository.save(orders);

            log.info("Order ID: {} status updated successfully to {}",ordersId, newStatus.toUpperCase());
            return updatedOrder;
        }catch (IllegalArgumentException e){
            log.error("Invalid order status provided: {}", newStatus);
            throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }
    }


    public void deleteOrder(Long id){
        log.info("Attempting to delete order with ID: {}", id);

        if (!orderRepository.existsById(id)){
            log.warn("Order not found with ID: {}", id);
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
        log.info("Order deleted successfully. ID: {}", id);
    }
}
