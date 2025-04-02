package com.ecommerce.ecommerce.roles;

public enum OrderStatus {
    EARRING, //orden creada, esperando el pago
    PAID,     //Pago confirmado
    SENT,    //Orden despachado
    DELIVERED,  // Pedido recibido por el cliente
    CANCELED,   //Orden anulada
    REIMBURSED  //Se devolvió el dinero al cliente
}
