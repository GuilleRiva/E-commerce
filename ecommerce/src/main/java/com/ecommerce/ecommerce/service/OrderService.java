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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<OrderItemDTO> items = order.getOrderDetails().stream()
                .map(orderDetails -> new OrderItemDTO(
                        orderDetails.getProduct().getName(),
                        orderDetails.getAmount(),
                        orderDetails.getUnitPrice()
                ))
                .collect(Collectors.toList());

        dto.setItems(items);

        return dto;
    }


    public OrderResponseDTO createOrder(OrderRequestDTO dto){
        Users users= userRepository.findById(dto.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        Address address= addressRepository.findById(dto.getAddressId())
                .orElseThrow(()-> new ResourceNotFoundException("Address not found"));

        PaymentMethods paymentMethods= paymentMethodsRepository.findById(dto.getPaymentMethodId())
                .orElseThrow(()-> new ResourceNotFoundException("Payment method not found"));


        //creates order
        Orders order= new Orders();
        order.setUser(users);

        Orders savedOrder= orderRepository.save(order);

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

            orderDetailsRepository.saveAll(detailsList);
            savedOrder.setOrderDetails(detailsList);

        }

        return toOrderResponseDTO(savedOrder);
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
