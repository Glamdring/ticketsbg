package com.tickets.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;

import com.tickets.dao.Dao;
import com.tickets.utils.SpringContext;

public abstract class BaseServiceTest {

    private static ApplicationContext ctx;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public void init() {
        Dao daoMock = mock(Dao.class);
        Object[] ar = new Object[]{};
        String[] strar = new String[]{};
        when(daoMock.findByNamedQuery(anyString(),
                any(strar.getClass()),
                any(ar.getClass()))).thenReturn(new ArrayList());

        when(daoMock.findByQuery(anyString(),
                any(strar.getClass()),
                any(ar.getClass()))).thenReturn(new ArrayList());

        getService().setDao(daoMock);

        String path = new File("").getAbsolutePath() + "/src/main/resources/";
        SpringContext.init(path);
        ctx = SpringContext.getContext();
    }

    public Object getBean(String name) {
        return ctx.getBean(name);
    }

    public abstract BaseService getService();
}
