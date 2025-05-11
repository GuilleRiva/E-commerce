package com.ecommerce.ecommerce.service;
import com.ecommerce.ecommerce.dto.XRequestDTO.PaymentRequestDTO;
import com.ecommerce.ecommerce.enums.OrderStatus;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Orders;
import com.ecommerce.ecommerce.model.PaymentMethods;
import com.ecommerce.ecommerce.model.Payments;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.repository.PaymentMethodsRepository;
import com.ecommerce.ecommerce.repository.PaymentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
        log.info("Fetching retrieves all payments");
        return paymentsRepository.findAll();
    }

    public Payments getPaymentById(Long id){
        log.info("Fetching payment with ID: {}", id);

        return paymentsRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Payment not found with ID: {}", id);
                   return new ResourceNotFoundException("Payment not found with ID: " + id);
                });
    }

    public List<Payments>getPaymentsByOrderId(Long orderId){
        log.info("Fecthing order with order ID: {}", orderId);
        return paymentsRepository.findByOrderId(orderId);
    }


    public Payments toPaymentEntity(PaymentRequestDTO dto){
        log.debug("Mapping PaymentRequestDTO to Payment entity: {}", dto);

        Orders orders = orderRepository.findById(dto.getOrderId())
                .orElseThrow(()-> {
                    log.error("Order not found with ID: {}", dto.getOrderId());
                    return new ResourceNotFoundException("Order not found with ID" + dto.getOrderId());
                });


        PaymentMethods methods = paymentMethodsRepository.findById(dto.getPaymentMethod())
                .orElseThrow(()-> {
                    log.error("Payment method not found with ID:" + dto.getPaymentMethod());
                        return new ResourceNotFoundException("Payment method not found with ID" + dto.getPaymentMethod());
                });


        Payments payments = new Payments();
        payments.setOrder(orders);
        payments.setPaymentMethods(methods);
        payments.setAmount(dto.getAmount());
        payments.setOrderStatus(OrderStatus.SENT);
        payments.setPaymentDate(LocalDateTime.now());

        log.debug("Payments entity mapped successfully for Order ID: {}, Method ID: {}",
                dto.getOrderId(), dto.getPaymentMethod());

        return payments;
    }


    public Payments createPayment(Long orderId, Long methodId, BigDecimal amount){
        log.info("Attempting to create payment. Order ID: {}, methodId: {}, amount: {}",
                orderId, methodId, amount);

        Orders orders=orderRepository.findById(orderId)
                .orElseThrow(()-> {
                    log.error("Order not found with order ID: {}", orderId);
                     return new ResourceNotFoundException("Order not found with ID:" + orderId);
                });

        PaymentMethods paymentMethods=  paymentMethodsRepository.findById(methodId)
                .orElseThrow(()-> {
                    log.error("Payment method not found with ID: {}", methodId);
                    return new ResourceNotFoundException("Payment method not found with ID: " + methodId);
                });

        Payments payments= new Payments();
        payments.setOrder(orders);
        payments.setPaymentMethods(paymentMethods);
        payments.setAmount(amount);
        payments.setPaymentDate(LocalDateTime.now());
        payments.setOrderStatus(OrderStatus.valueOf("PAID"));

        Payments saved = paymentsRepository.save(payments);
        log.info("Payment created successfully for Order ID: {}, with method ID: {} and amount: {}",
                orderId, methodId, amount);

        return saved;
   }


   public Payments updatePaymentStatus(Long id, String newStatus){
        log.info("Attempting to update payment status. ID: {}, NewStatus: {}", id, newStatus);

        Payments payments= getPaymentById(id);
        payments.setOrderStatus(OrderStatus.valueOf(newStatus));
        return paymentsRepository.save(payments);
   }

   public void deletePayment(Long id){
        log.info("Attempting to delete payment with ID: {} ",id);

        if (!paymentsRepository.existsById(id)){
            log.warn("Payment not found with ID: {}", id);
            throw new ResourceNotFoundException("Payment not found with ID:" + id);
        }
        
        paymentsRepository.deleteById(id);
        log.info("Payment deleted successfully. ID: {}",id);
   }
}
