package com.tickets.utils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.tickets.model.Day;
import com.tickets.model.Firm;
import com.tickets.model.Price;
import com.tickets.model.Route;
import com.tickets.model.RouteDay;
import com.tickets.model.RouteHour;
import com.tickets.model.Run;
import com.tickets.model.Stop;
import com.tickets.model.Ticket;
import com.tickets.model.User;
import com.tickets.model.UsersHistory;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new AnnotationConfiguration()
            .addPackage("com.tickets.model")
            .addAnnotatedClass(Day.class)
            .addAnnotatedClass(Firm.class)
            .addAnnotatedClass(Price.class)
            .addAnnotatedClass(Route.class)
            .addAnnotatedClass(RouteHour.class)
            .addAnnotatedClass(RouteDay.class)
            .addAnnotatedClass(Run.class)
            .addAnnotatedClass(Stop.class)
            .addAnnotatedClass(Ticket.class)
            .addAnnotatedClass(User.class)
            .addAnnotatedClass(UsersHistory.class)
            .setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver")
            .setProperty("hibernate.connection.url", "jdbc:mysql://169.254.61.23/tickets?characterEncoding=utf8")
            .setProperty("hibernate.connection.username", "common")
            .setProperty("hibernate.connection.password", "qaz")
            .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
            .buildSessionFactory();

        } catch (Throwable ex) {
            // Log exception!
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session openSession() throws HibernateException {
        return sessionFactory.openSession();
    }

    public static Session getSession() throws HibernateException {
        return sessionFactory.getCurrentSession();
    }
}