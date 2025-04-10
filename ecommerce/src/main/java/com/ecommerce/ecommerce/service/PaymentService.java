package com.ecommerce.ecommerce.service;
import com.ecommerce.ecommerce.enums.OrderStatus;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Orders;
import com.ecommerce.ecommerce.model.PaymentMethods;
import com.ecommerce.ecommerce.model.Payments;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.repository.PaymentMethodsRepository;
import com.ecommerce.ecommerce.repository.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentsRepository paymentsRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public PaymentService(PaymentsRepository paymentsRepository, PaymentMethodsRepository paymentMethodsRepository, OrderRepository orderRepository) {
        this.paymentsRepository = paymentsRepository;
        this.paymentMethodsRepository = paymentMethodsRepository;
        this.orderRepository = orderRepository;
    }

    public List<Payments> getAllPayments(){
        return paymentsRepository.findAll();
    }

    public Payments getPaymentById(Long id){
        return paymentsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Payment not found with ID: " + id));
    }

    public List<Payments>getPaymentsByOrderId(Long orderId){
        return paymentsRepository.findByOrderId(orderId);
    }

    public Payments createPayment(Long orderId, Long methodId, BigDecimal amount){
        Orders orders=orderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order not found with ID:" + orderId));
        PaymentMethods paymentMethods= (PaymentMethods) paymentMethodsRepository.findById(methodId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment method not found with ID: " + methodId));

        Payments payments= new Payments();
        payments.setOrder(orders);
        payments.setPaymentMethods(paymentMethods);
        payments.setAmount(amount);
        payments.setPaymentDate(LocalDateTime.now());
        payments.setOrderStatus(OrderStatus.valueOf("PAID"));

        return paymentsRepository.save(payments);
   }

   public Payments updatePaymentStatus(Long id, String newStatus){
        Payments payments= getPaymentById(id);
        payments.setOrderStatus(OrderStatus.valueOf(newStatus));
        return paymentsRepository.save(payments);
   }

   public void deletePayment(Long id){
        if (!paymentsRepository.existsById(id)){
            throw new ResourceNotFoundException("Payment not found with ID:" + id);
        }
        paymentsRepository.deleteById(id);
   }
}
