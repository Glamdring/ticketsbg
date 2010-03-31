package com.tickets.services;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tickets.constants.Messages;
import com.tickets.constants.Settings;
import com.tickets.exceptions.PaymentException;
import com.tickets.model.Order;
import com.tickets.model.PaymentMethod;
import com.tickets.model.Ticket;
import com.tickets.services.valueobjects.PaymentData;
import com.tickets.utils.GeneralUtils;
import com.tickets.utils.SecurityUtils;
import com.tickets.utils.base64.Base64Encoder;

@Service("paymentService")
public class PaymentServiceImpl extends BaseService implements PaymentService {

    private static final Logger logger = Logger.getLogger(PaymentServiceImpl.class);

    private static final String E_PAY_DATA_PATTERN = "MIN={0}\nINVOICE={1}\nAMOUNT={2}\nEXP_TIME={3}\nDESCR={4}";

    private static final String BORICA_PROTOCOL_VERSION = "1.1";

    @Autowired
    private TicketService ticketService;

    @Override
    public PaymentData getPaymentData(Order order, PaymentMethod paymentMethod) throws PaymentException {
       if (paymentMethod == PaymentMethod.E_PAY) {
           return getEpayPaymentData(order);
       }
       if (paymentMethod == PaymentMethod.CREDIT_CARD) {
           return getBoricaPaymentData(order);
       }

       return new PaymentData(); // empty object
    }

    private PaymentData getBoricaPaymentData(Order order) {

        // Do not touch the code below. It is dark magic in order to conform
        // to Borica odd choice of encodings and byte array representations

        List<Ticket> tickets = order.getTickets();

        String transactionType = "10"; // purchase

        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = df.format(GeneralUtils.createCalendar().getTime());

        String formattedSum = createDecimalFormat().format(getTotalPrice(tickets));
        String[] decimalParts = formattedSum.split("\\.");
        int sumInt = Integer.parseInt(decimalParts[0]) * 100 + Integer.parseInt(decimalParts[1]);
        String sum = String.format("%012d", sumInt);

        String orderId = order.getId() + "";
        orderId = StringUtils.rightPad(addDummyDigits(orderId), 15);

        String description = StringUtils.rightPad(getDescription(tickets), 125);

        String tid = Settings.getValue("borica.tid");

        String language = order.getLanguageCode().toLowerCase().indexOf("bg") != -1 ? "BG" : "EN";
        String protocolVersion = BORICA_PROTOCOL_VERSION;

        String currency = "BGN"; // TODO currencies

        String textToSign = StringUtils.join(new String[] { transactionType,
                dateTime, sum, tid, orderId, description, language,
                protocolVersion, currency});

        String encoded = null;
        try {

            byte[] tBytes = textToSign.getBytes("cp1251");
            byte[] signature = SecurityUtils.sign(tBytes, SecurityUtils.getBoricaClientPrivateKey());

            byte[] allBytes = ArrayUtils.addAll(tBytes, signature);
            encoded = new String(Base64.encodeBase64(allBytes));

            logger.info("Sending to BORICA: " + textToSign);

            encoded = URLEncoder.encode(encoded, "ISO-8859-1");

        } catch (UnsupportedEncodingException ex) {
            // it is supported
        }

        PaymentData pd = new PaymentData();
        pd.setEncoded(encoded);

        return pd;
    }

    private PaymentData getEpayPaymentData(Order order) throws PaymentException {
        String secret = Settings.getValue("epay.secret");
        String min = Settings.getValue("epay.min");

        List<Ticket> tickets = order.getTickets();

        DecimalFormat df = createDecimalFormat();
        String sum = df.format(getTotalPrice(tickets));
        Calendar inTenMinutes = GeneralUtils.createCalendar();
        inTenMinutes.add(Calendar.MINUTE, 10);
        String expiryDate = new SimpleDateFormat("dd.MM.yyyy HH:mm")
                .format(inTenMinutes.getTime());

        String description = getDescription(tickets);

        if (order.getId() == 0) {
            order = getDao().persist(order);
        }

        String orderId = order.getId() + "";
        orderId = addDummyDigits(orderId);

        String data = MessageFormat.format(E_PAY_DATA_PATTERN, min, orderId,
                sum, expiryDate, description);

        try {
            PaymentData paymentData = new PaymentData();
            paymentData.setOrder(order);

            paymentData.setEncoded(Base64Encoder
                    .toBase64String(data.getBytes("cp1251")));
            paymentData.setChecksum(SecurityUtils.hmac(
                    paymentData.getEncoded(), secret));

            return paymentData;
        } catch (Exception ex) {
            throw new PaymentException(ex);
        }
    }

    private String getDescription(List<Ticket> tickets) {
        String description = Messages.getString("paymentDescriptionPrefix") + ": "; // TODO somehow put new line

        for (Ticket ticket : tickets) {
            description += ticket.getStartStop() + " - " + ticket.getEndStop()
                    + " : " + ticket.getTicketCode() + "; ";
        }
        return description;
    }

    private DecimalFormat createDecimalFormat() {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');

        df.setDecimalFormatSymbols(dfs);
        return df;
    }

    private String addDummyDigits(String orderId) {
        int prefix = (int) (Math.random() * 89 + 10);
        int suffix = (int) (Math.random() * 89 + 10);

        return prefix + orderId + suffix;
    }

    private String stripDummyDigits(String orderId) {
        String withoutPrefix = orderId.substring(2);
        String original = withoutPrefix.substring(0, withoutPrefix.length() - 2);

        return original;
    }

    @Override
    public BigDecimal getTotalPrice(List<Ticket> tickets) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Ticket ticket : tickets) {
            if (!ticket.isAltered()) {
                sum = sum.add(ticket.getTotalPrice());
            } else {
                sum = sum.add(ticket.getAlterationPriceDifference());
            }
        }

        return sum;
    }

    @Override
    public boolean confirmPayment(String orderId, String paymentCode) throws PaymentException {
        try {
            orderId = stripDummyDigits(orderId);
            log.debug("Confirming payment with stripped orderId=" + orderId);

            Order order = ticketService.get(Order.class, Integer.parseInt(orderId));

            ticketService.finalizePurchase(order.getTickets(), paymentCode);

            return true; //TODO sometimes return "false". check with ePay protocol
        } catch (Exception ex) {
            log.error("Error in committing payment", ex);
            throw new PaymentException(ex);
        }
    }

    @Override
    public BigDecimal getServiceFee(BigDecimal totalPrice) {
        BigDecimal fee = totalPrice.multiply(
                new BigDecimal(Settings.getValue("credit.card.service.fee"))
                        .divide(new BigDecimal("100")));

        BigDecimal minFee = new BigDecimal(Settings.getValue("credit.card.minimum.fee"));

        if (minFee.compareTo(fee) > 0) {
            fee = minFee;
        }

        return fee;
    }

    @Override
    public String formBoricaURL(PaymentData pd) {
        return Settings.getValue("borica.url") + "?"
                + Settings.getValue("borica.param") + "=" + pd.getEncoded();
    }

    public String formBoricaStatusURL(PaymentData pd) {
        return Settings.getValue("borica.status.url") + "?"
                + Settings.getValue("borica.param") + "=" + pd.getEncoded();
    }

    @Override
    public boolean handleBoricaConfirmation(String message) throws PaymentException {
        byte[] bytes = Base64.decodeBase64(message.getBytes());
        String decoded = new String(bytes);

        int orderIdStart = 2 + 14 + 12 + 8;
        String orderId = decoded.substring(orderIdStart, orderIdStart + 15)
                .trim();
        // using the date for payment code
        String paymentCode = decoded.substring(2, 14);
        String resultCode = decoded.substring(orderIdStart + 15,
                orderIdStart + 17);

        if (resultCode.equals("00")) {

            byte[] sigBytes = Arrays.copyOfRange(bytes, bytes.length - 128,
                    bytes.length);
            byte[] textBytes = Arrays.copyOfRange(bytes, 0, bytes.length - 128);

            boolean verified = SecurityUtils.verify(sigBytes, textBytes,
                    SecurityUtils.getBoricaCertificate());
            if (verified) {
                return confirmPayment(orderId, paymentCode);
            } else {
                throw new PaymentException("Failed verifying Borica signature");
            }
        } else {
            throw new PaymentException("BORICA payment failed with code: "
                    + resultCode);
        }
    }
}
