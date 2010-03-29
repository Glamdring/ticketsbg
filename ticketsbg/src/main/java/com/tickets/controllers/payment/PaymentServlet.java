package com.tickets.controllers.payment;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.tickets.constants.Settings;
import com.tickets.exceptions.PaymentException;
import com.tickets.services.PaymentService;
import com.tickets.utils.SecurityUtils;
import com.tickets.utils.base64.Base64Decoder;

public class PaymentServlet extends HttpServlet {

    private static final Pattern RESPONSE_PATTERN
        = Pattern.compile("^INVOICE=(\\d+):STATUS=(PAID|DENIED|EXPIRED)(:PAY_TIME=(\\d+):STAN=(\\d+):BCODE=([0-9a-zA-Z]+))?$");

    private static final Logger logger = Logger.getLogger(PaymentServlet.class);

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
            String encoded = request.getParameter("encoded");
            String checksum = request.getParameter("checksum");

            String secret = Settings.getValue("epay.secret");
            String hmac = SecurityUtils.hmac(encoded, secret);

            if (checksum.equalsIgnoreCase(hmac)) {
                String infoData = handlePayment(encoded);
                response.getWriter().println(infoData);
            } else {
                logger.info("Invalid checksum from IP: " + request.getRemoteAddr());
                response.getWriter().println("ERR=Not valid CHECKSUM\n"); // Required
            }
        } catch (Exception ex) {
            logger.error("", ex);

            // don't throw
            // throw new ServletException(ex);
        }
    }

    public String handlePayment(String encoded) throws Exception {
        String data = new String(Base64Decoder.toByteArray(encoded));
        String[] lines = data.split("\n");
        String infoData = "";


        for (String line : lines) {
            Matcher matcher = RESPONSE_PATTERN.matcher(line);

            if (matcher.matches()) {
                String orderId = matcher.group(1);
                String status = matcher.group(2);
                //String paymntDate = matcher.group(4);
                //String stan = matcher.group(5);
                String bcode = matcher.group(6);

                infoData += "INVOICE=" + orderId + ":STATUS=";

                logger.debug("Processing payment for orderId=" + orderId);

                if (status.equals("PAID")) {
                    try {
                        if (getPaymentService().confirmPayment(orderId, bcode)) {
                            infoData += "OK\n";
                        } else {
                            infoData += "NO\n";
                        }
                    } catch (PaymentException ex) {
                        infoData += "ERROR\n";
                    }
                } else {
                    infoData += "OK\n"; // TODO ?
                }
            }
        }
        return infoData + "\n";
    }
}
