package com.tickets.services;

import java.math.BigDecimal;
import java.util.List;

import com.tickets.exceptions.PaymentException;
import com.tickets.model.Ticket;
import com.tickets.services.valueobjects.PaymentData;

public interface PaymentService extends Service {

    PaymentData getPaymentData(List<Ticket> tickets) throws PaymentException;

    BigDecimal getTotalPrice(List<Ticket> tickets);

}
