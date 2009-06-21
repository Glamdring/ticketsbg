package com.tickets.services;

import java.math.BigDecimal;

import com.tickets.model.Discount;

public interface DiscountService extends Service<Discount> {

    BigDecimal calculatePrice(BigDecimal base, Discount discount);
}
