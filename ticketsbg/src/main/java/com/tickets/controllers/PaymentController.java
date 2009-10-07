package com.tickets.controllers;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.model.PaymentMethod;
import com.tickets.utils.SelectItemUtils;

@Component("paymentController")
@Scope("conversation.manual")
@ConversationName("purchaseConversation")
public class PaymentController extends BaseController {

    private List<SelectItem> paymentMethods = new ArrayList<SelectItem>();
    private String selectedPaymentMethod;

    @Autowired
    private PurchaseController purchaseController;

    @Autowired
    private PersonalInformationController personalInformationController;

    @PostConstruct
    public void init() {
        //List all except the CASH_DESK option
        paymentMethods = SelectItemUtils.formSelectItems(PaymentMethod.class,
                EnumSet.of(PaymentMethod.CASH_DESK));
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
}
