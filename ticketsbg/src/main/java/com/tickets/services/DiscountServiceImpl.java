package com.tickets.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.model.Discount;
import com.tickets.model.DiscountType;
import com.tickets.model.Firm;
import com.tickets.model.GenericDiscount;

@Service
public class DiscountServiceImpl extends BaseService<Discount> implements
        DiscountService {

    @Override
    public BigDecimal calculatePrice(BigDecimal base, Discount discount) {
        if (discount.getDiscountType() == DiscountType.FIXED) {
            return base.subtract(discount.getValue());
        }
        if (discount.getDiscountType() == DiscountType.PERCENTAGE) {
            return base.subtract(discount.getValue().divide(base).multiply(
                    new BigDecimal("100")));
        }

        // Unknown case
        return BigDecimal.ZERO;
    }

    @Override
    public List<GenericDiscount> getGenericDiscounts(Firm firm) {
        return getDao().findByQuery(
                "SELECT gd FROM GenericDiscount gd WHERE gd.firm=:firm",
                new String[] { "firm" }, new Object[] { firm });
    }

    @Override
    public GenericDiscount findGenericDiscount(String name, Firm firm) {
        List<GenericDiscount> result = getDao()
                .findByQuery(
                        "SELECT gd FROM GenericDiscount gd WHERE gd.name=:name AND gd.firm=:firm",
                        new String[] { "name", "firm" },
                        new Object[] { name, firm });
        if (result.size() > 0) {
            return result.get(0);
        }

        return null;
    }
}
