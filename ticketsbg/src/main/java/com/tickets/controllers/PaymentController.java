package com.tickets.controllers;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.constants.Settings;
import com.tickets.model.PaymentMethod;
import com.tickets.utils.GeneralUtils;
import com.tickets.utils.SelectItemUtils;
import com.tickets.utils.base64.Base64Encoder;

@Component("paymentController")
@Scope("conversation.manual")
@ConversationName("purchaseConversation")
public class PaymentController extends BaseController {

    private static final String E_PAY_DATA_PATTERN = "MIN={0}\nINVOICE={1}\nAMOUNT={2}\nEXP_TIME={3}\nDESCR={4}";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private List<SelectItem> paymentMethods = new ArrayList<SelectItem>();
    private String selectedPaymentMethod;

    @Autowired
    private PurchaseController purchaseController;

    @Autowired
    private PersonalInformationController personalInformationController;

    private String submitUrl;
    private String encoded;
    private String checksum;

    @PostConstruct
    public void init() {
        //List all except the CASH_DESK option
        paymentMethods = SelectItemUtils.formSelectItems(PaymentMethod.class,
                EnumSet.of(PaymentMethod.CASH_DESK));

        refreshPaymentData(null);
    }

    public void buy() {
        try {
            PaymentMethod paymentMethod = PaymentMethod.valueOf(selectedPaymentMethod);
            purchaseController.setPaymentMethod(paymentMethod);

            // Update the customer information for the purchase
            personalInformationController.updateCustomer();
            // TODO submit the payment. The "buy" method should be called as an
            // action listener, before a form is submitted to the payment
            // gateway.
        } catch (Exception ex) {
            //TODO request a valid payment method
        }

    }

    public void refreshPaymentData(@SuppressWarnings("unused") ActionEvent evt) {
        String secret = Settings.getValue("epay.secret");
        String min = Settings.getValue("epay.min");
        //TODO generate & persist orderId
        String orderId = purchaseController.getTickets().get(0).getId() + "";
        String sum = purchaseController.getTotalPrice().toString();
        String expiryDate = new SimpleDateFormat("dd.MM.yyyy")
                .format(GeneralUtils.createEmptyCalendar().getTime());

        String description = ""; // TODO output ticket codes and destinations;

        String data = MessageFormat.format(E_PAY_DATA_PATTERN, min, orderId,
                sum, expiryDate, description);

        try {
            encoded = Base64Encoder.toBase64String(data.getBytes());
        } catch (IOException ex) {
            // ingore
        }

        checksum = hmac(encoded, secret);
    }

    private String hmac(String data, String key) {
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());

            String result = Base64Encoder.toBase64String(rawHmac);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<SelectItem> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<SelectItem> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }

    public void setSelectedPaymentMethod(String selectedPaymentMethod) {
        this.selectedPaymentMethod = selectedPaymentMethod;
    }

    public String getSubmitUrl() {
        return submitUrl;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }

    public String getEncoded() {
        return encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
