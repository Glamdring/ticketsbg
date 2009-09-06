package com.tickets.services;

import java.math.BigDecimal;
import java.util.List;

import com.tickets.model.Discount;
import com.tickets.model.Firm;
import com.tickets.model.GenericDiscount;

public interface DiscountService extends Service<Discount> {

    BigDecimal calculatePrice(BigDecimal base, Discount discount);

    List<GenericDiscount> getGenericDiscounts(Firm firm);

    GenericDiscount findGenericDiscount(String name, Firm firm);
}
