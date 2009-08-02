package com.tickets.services;

import org.junit.BeforeClass;
import org.junit.Test;

public class RunServiceTest extends BaseServiceTest {

    private static RunService service;

    @BeforeClass
    public static void initTest() {
        service = (RunService) getBean("runService");
    }

    @Test
    public void testCreateRoutes() {
        if (true) return;
        //service.createRuns();
    }

    @Test
    public void testFetchQuery() {
        if (true) return;
//        List result =((Dao) getBean("dao")).findByNamedQuery("Run.getLastRuns");
//
//        for (Object obj : result) {
//            Run run = (Run) obj;
//            System.out.println(run.getRoute());
//        }
    }

    @Override
    public BaseService getService() {
        return (BaseService) service;
    }
}
