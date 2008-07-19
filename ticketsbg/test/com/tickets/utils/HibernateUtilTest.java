package com.tickets.utils;

import org.junit.Test;

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
