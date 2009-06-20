package com.tickets.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.model.PaymentMethod;

@Component("paymentController")
@Scope("conversation.manual")
@ConversationName("purchaseConversation")
public class PaymentController extends BaseController {

    private List<String> paymentMethods;
    private String selectedPaymentMethod;

    private PurchaseController purchaseController;

    @PostConstruct
    public void init() {
        PaymentMethod[] pms = PaymentMethod.values();
        paymentMethods = new ArrayList<String>(pms.length);
        for (PaymentMethod pm : pms) {
            paymentMethods.add(pm.toString());
        }
    }

    public void buy() {
        try {
            PaymentMethod paymentMethod = PaymentMethod.valueOf(selectedPaymentMethod);
            purchaseController.setPaymentMethod(paymentMethod);

            // TODO submit the payment. The "buy" method should be called as an
            // action listener, before a form is submitted to the payment
            // gateway
        } catch (Exception ex) {
            //TODO request a valid payment method
        }

    }

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<String> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }

    public void setSelectedPaymentMethod(String selectedPaymentMethod) {
        this.selectedPaymentMethod = selectedPaymentMethod;
    }
}
