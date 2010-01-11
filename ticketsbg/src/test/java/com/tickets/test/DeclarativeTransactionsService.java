package com.tickets.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tickets.dao.Dao;
import com.tickets.model.Firm;

@Component
@TransactionConfiguration(transactionManager="jpaTransactionManager")
public class DeclarativeTransactionsService {

    @Autowired
    private Dao dao;

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Firm get() {
        Firm firm = dao.persist(new Firm());
        Firm loaded = dao.getById(Firm.class, firm.getFirmId());
        return loaded;
    }
}
