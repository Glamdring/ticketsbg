package com.tickets.utils;

import org.junit.Test;

import com.tickets.server.utils.HibernateUtil;

public class HibernateUtilTest {

    @Test
    public void main(){
        try {
            HibernateUtil.openSession();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
