package com.tickets.controllers;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.exceptions.PaymentException;
import com.tickets.model.PaymentMethod;
import com.tickets.services.PaymentService;
import com.tickets.services.valueobjects.PaymentData;
import com.tickets.utils.SelectItemUtils;

@Component("paymentController")
@Scope("conversation.manual")
@ConversationName("purchaseConversation")
public class PaymentController extends BaseController {

    private List<SelectItem> paymentMethods = new ArrayList<SelectItem>();
    private String selectedPaymentMethod;

    @Autowired
    private PaymentService paymentService;

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
        try {
            PaymentData paymentData = paymentService.getPaymentData(purchaseController.getTickets());
            setEncoded(paymentData.getEncoded());
            setChecksum(paymentData.getChecksum());
        } catch (PaymentException ex) {
            String messageKey = ex.getMessage();
            if (messageKey == null) {
                messageKey = "unexpectedErrorReloadThePage";
            }
            addError(messageKey);
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
