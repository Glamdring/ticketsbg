package com.tickets.services;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tickets.dao.Dao;
import com.tickets.model.Run;

public class RunServiceTest extends BaseServiceTest {

    private static RunService service;

    @BeforeClass
    public static void initTest() {
        service = (RunService) getBean("runService");
    }

    @Test
    public void testCreateRoutes() {
        service.createRuns();
    }

    @Test
    public void testFetchQuery() {
        List result =((Dao) getBean("dao")).findByNamedQuery("Run.getLastRuns");

        for (Object obj : result) {
            Run run = (Run) obj;
            System.out.println(run.getRoute());
        }
    }

    @Override
    public BaseService getService() {
        return (BaseService) service;
    }
}
