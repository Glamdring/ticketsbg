package com.tickets.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.tickets.model.Discount;
import com.tickets.model.DiscountType;

@Service
public class DiscountServiceImpl extends BaseService<Discount> implements DiscountService {

    @Override
    public BigDecimal calculatePrice(BigDecimal base, Discount discount) {
       if (discount.getDiscountType() == DiscountType.FIXED) {
           return base.subtract(discount.getValue());
       }
       if (discount.getDiscountType() == DiscountType.PERCENTAGE) {
           return base.subtract(discount.getValue().divide(base).multiply(new BigDecimal("100")));
       }

       // Unknown case
       return BigDecimal.ZERO;
    }
}
