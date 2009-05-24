package com.tickets.services;

import java.io.File;

import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;

import com.tickets.utils.SpringContext;

public abstract class BaseServiceTest {

    private static ApplicationContext ctx;

    @BeforeClass
    public static void init() {
//        Dao daoMock = mock(Dao.class);
//        Object[] ar = new Object[]{};
//        String[] strar = new String[]{};
//        when(daoMock.findByNamedQuery(anyString(),
//                any(strar.getClass()),
//                any(ar.getClass()))).thenReturn(new ArrayList());
//
//        when(daoMock.findByQuery(anyString(),
//                any(strar.getClass()),
//                any(ar.getClass()))).thenReturn(new ArrayList());
//
//        getService().setDao(daoMock);

        String path = new File("").getAbsolutePath() + "/src/main/resources/";
        SpringContext.init(path);
        ctx = SpringContext.getContext();
    }

    public static Object getBean(String name) {
        return ctx.getBean(name);
    }

    public abstract BaseService getService();
}
