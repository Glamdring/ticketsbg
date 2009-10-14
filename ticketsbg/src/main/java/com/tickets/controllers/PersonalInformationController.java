package com.tickets.controllers;

import java.util.List;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.Customer;
import com.tickets.model.CustomerType;
import com.tickets.model.User;
import com.tickets.utils.SelectItemUtils;

@Component("personalInformationController")
@Scope("session")
public class PersonalInformationController extends BaseController {

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    @Autowired
    private PurchaseController purchaseController;

    private Customer customer = new Customer();

    private List<SelectItem> customerTypeItems = SelectItemUtils
            .formSelectItems(CustomerType.class);

    /**
     * Copies the data from the currently logged user (if such exists)
     * to the customer object
     * @param evt
     */
    public void beforeRenderResponse(PhaseEvent evt) {
        if (evt.getPhaseId() == PhaseId.RENDER_RESPONSE) {
            if (customer.getName() == null) {
                User logged = loggedUserHolder.getLoggedUser();
                if (logged != null) {
                    customer.setName(logged.getName());
                    customer.setContactPhone(logged.getContactPhone());
                    customer.setCustomerType(logged.getCustomerType());
                    customer.setCompanyName(logged.getCompanyName());
                    customer.setEmail(logged.getEmail());
                }
            }
        }
    }

    public void updateCustomerInPurchase() {
        // In case the user has come directly to this page
        // without choosing run, return
        if (purchaseController.getTickets().size() == 0) {
            return;
        }

        User loggedUser = loggedUserHolder.getLoggedUser();
        if (loggedUser != null) {
            purchaseController.setUser(loggedUser);
        }

        purchaseController.setCustomerInformation(customer);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<SelectItem> getcustomerTypeItems() {
        return customerTypeItems;
    }

    public void setcustomerTypeItems(List<SelectItem> customerTypeItems) {
        this.customerTypeItems = customerTypeItems;
    }
}
