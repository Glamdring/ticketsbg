package com.tickets.controllers.payment;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.tickets.constants.Settings;
import com.tickets.exceptions.PaymentException;
import com.tickets.services.PaymentService;
import com.tickets.utils.SecurityUtils;
import com.tickets.utils.base64.Base64Decoder;

public class PaymentServlet extends HttpServlet {

    private static final Pattern RESPONSE_PATTERN
        = Pattern.compile("/^INVOICE=(\\d+):STATUS=(PAID|DENIED|EXPIRED)(:PAY_TIME=(\\d+):STAN=(\\d+):BCODE=([0-9a-zA-Z]+))?$/");

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        try {
            String encoded = request.getParameter("ENCODED");
            String checksum = request.getParameter("CHECKSUM");

            String secret = Settings.getValue("epay.secret");
            String hmac  = SecurityUtils.hmac(encoded, secret);

            if (checksum.equalsIgnoreCase(hmac)) {

                String data = new String(Base64Decoder.toByteArray(encoded));
                String[] lines = data.split("\n");
                String infoData = "";

                PaymentService service = (PaymentService) WebApplicationContextUtils
                        .getRequiredWebApplicationContext(getServletContext())
                        .getBean("paymentService");

                for (String line : lines) {
                    Matcher matcher = RESPONSE_PATTERN.matcher(line);
                    if (matcher.groupCount() > 1) {
                        String orderId = matcher.group(1);
                        String status = matcher.group(2);
                        String paymentDate = matcher.group(4);
                        String stan = matcher.group(5);
                        String bcode = matcher.group(6);

                        System.out.println(stan);
                        infoData += "INVOICE=" + orderId + ":STATUS=";

                        if (status.equals("PAID")) {
                            try {
                                if (service.confirmPayment(orderId, bcode)) {
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

                response.getWriter().println(infoData);
            }
            else {
                response.getWriter().println("ERR=Not valid CHECKSUM\n"); // Required
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

}
