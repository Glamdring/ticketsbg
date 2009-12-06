package com.tickets.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring.xml")
@TransactionConfiguration(transactionManager="jpaTransactionManager", defaultRollback=false)
public abstract class BaseTest {

}
