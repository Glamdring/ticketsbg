package com.tickets.controllers.payment;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.tickets.constants.Settings;
import com.tickets.services.PaymentService;

public class BoricaPaymentServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(BoricaPaymentServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }

    private PaymentService paymentService;

    public PaymentService getPaymentService() {
        if (paymentService == null) {
            paymentService = (PaymentService) WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext())
                .getBean("paymentService");
        }
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        try {
            String message = request.getParameter(Settings.getValue("borica.param"));
            boolean success = getPaymentService().handleBoricaConfirmation(message);
            if (success) {
                response.sendRedirect("paymentSuccess.jsp");
            } else {
                response.sendRedirect("paymentCancelled.jsp");
            }
        } catch (Exception ex) {
            logger.error("Error with payment", ex);
            response.sendRedirect("paymentCancelled.jsp");
        }


    }
}
