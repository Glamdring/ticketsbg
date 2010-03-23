package com.tickets.controllers.payment;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.tickets.constants.Settings;
import com.tickets.exceptions.PaymentException;
import com.tickets.services.PaymentService;
import com.tickets.utils.SecurityUtils;
import com.tickets.utils.base64.Base64Decoder;

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
            String returned = request.getParameter(Settings.getValue("borica.param"));
            String decoded = new String(Base64.decodeBase64(returned.getBytes()), "cp1251");

            int orderIdStart = 2 + 14 + 12 + 8;
            String orderId = decoded.substring(orderIdStart, orderIdStart + 15 ).trim();
            // using the date for payment code
            String paymentCode = decoded.substring(2, 14);
            String resultCode = decoded.substring(orderIdStart + 15, orderIdStart + 17);

            if (resultCode.equals("00")) {
                getPaymentService().confirmPayment(orderId, paymentCode);
            } else {
                throw new PaymentException("BORICA payment failed with code: " + resultCode);
            }

        } catch (Exception ex) {
            logger.error("", ex);

            // don't throw
            // throw new ServletException(ex);
        }
    }
}
