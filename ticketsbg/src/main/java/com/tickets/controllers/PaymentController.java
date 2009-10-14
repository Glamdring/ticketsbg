package com.tickets.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.exceptions.PaymentException;
import com.tickets.model.PaymentMethod;
import com.tickets.services.PaymentService;
import com.tickets.services.valueobjects.PaymentData;
import com.tickets.utils.SelectItemUtils;

@Component("paymentController")
@Scope("session")
public class PaymentController extends BaseController {

    private List<SelectItem> paymentMethods = new ArrayList<SelectItem>();
    private String selectedPaymentMethod;

    @Autowired
    private transient PaymentService paymentService;

    @Autowired
    private transient PurchaseController purchaseController;

    @Autowired
    private PersonalInformationController personalInformationController;

    private String submitUrl;
    private String encoded;
    private String checksum;

    {
        //List all except the CASH_DESK option
        paymentMethods = SelectItemUtils.formSelectItems(PaymentMethod.class,
                EnumSet.of(PaymentMethod.CASH_DESK));
    }

    @PostConstruct
    public void init() {
        refreshPaymentData();
    }

    public void pay() {
        try {
            if (selectedPaymentMethod == null) {
                addError("selectPaymentMethod");
                return;
            }
            if (purchaseController.getTickets().size() == 0) {
                addError("atLeastOneTicketNeeded");
                return;
            }
            PaymentMethod paymentMethod = PaymentMethod.valueOf(selectedPaymentMethod);
            purchaseController.setPaymentMethod(paymentMethod);

            // Update the customer information for the purchase
            personalInformationController.updateCustomerInPurchase();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // After this method completes, if no errors exist, the
        // payment form is submitted to the payment provider
    }

    public BigDecimal getServiceFee() {
        return paymentService.getServiceFee(purchaseController.getTotalPrice());
    }

    /**
     * Method is called after modification in the tickets list (removal),
     * and on loading of the page (before RENDER RESPONSE phase)
     * @param evt
     */
    public void refreshPaymentData() {
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

    public void refreshPaymentData(@SuppressWarnings("unused") ActionEvent evt) {
        refreshPaymentData();
    }

    public void refreshPaymentData(PhaseEvent evt) {
        if (evt.getPhaseId() == PhaseId.RENDER_RESPONSE) {
            refreshPaymentData();
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
