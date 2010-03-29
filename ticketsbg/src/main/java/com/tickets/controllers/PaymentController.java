package com.tickets.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.constants.Settings;
import com.tickets.exceptions.PaymentException;
import com.tickets.model.PaymentMethod;
import com.tickets.services.PaymentService;
import com.tickets.services.valueobjects.PaymentData;
import com.tickets.utils.SelectItemUtils;

@Component("paymentController")
@Scope("request")
public class PaymentController extends BaseController {

    private static final Logger logger = Logger.getLogger(PaymentController.class);

    private List<SelectItem> paymentMethods = new ArrayList<SelectItem>();
    private String selectedPaymentMethod;

    @Autowired
    private transient PaymentService paymentService;

    @Autowired
    private transient PurchaseController purchaseController;

    @Autowired
    private transient PersonalInformationController personalInformationController;

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

            if (paymentMethod == PaymentMethod.CREDIT_CARD) {

                PaymentData pd = paymentService.getPaymentData(purchaseController.getOrder(), paymentMethod);
                String url = paymentService.formBoricaURL(pd);

                System.out.println(url);
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            }
        } catch (Exception ex) {
            logger.error("Problem with getting BORICA payment data", ex);
        }

        // If the method is ePay, after this method completes, if no errors
        // occur, the payment form is submitted to the ePay
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
            PaymentData paymentData = paymentService
                    .getPaymentData(purchaseController.getOrder(),
                            PaymentMethod.E_PAY);
            setEncoded(paymentData.getEncoded());
            setChecksum(paymentData.getChecksum());
            // the next line is not redundant! The order in the payment data is changed (acquired an id)
            purchaseController.setOrder(paymentData.getOrder());
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

    public String getEpayUrl() {
        return Settings.getValue("epay.url");
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
