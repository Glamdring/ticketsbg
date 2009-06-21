package com.tickets.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.tickets.model.Discount;

@Service
public class DiscountServiceImpl extends BaseService<Discount> implements DiscountService {

    @Override
    public BigDecimal calculatePrice(BigDecimal base, Discount discount) {
        // TODO calculate
        return null;
    }
}
