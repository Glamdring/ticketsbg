package com.tickets.services;

import java.math.BigDecimal;
import java.util.List;

import com.tickets.exceptions.PaymentException;
import com.tickets.model.Order;
import com.tickets.model.Ticket;
import com.tickets.services.valueobjects.PaymentData;

public interface PaymentService extends Service {

    PaymentData getPaymentData(Order order) throws PaymentException;

    BigDecimal getTotalPrice(List<Ticket> tickets);

    boolean confirmPayment(String orderId, String paymentCode) throws PaymentException;

    BigDecimal getServiceFee(BigDecimal totalPrice);

}
