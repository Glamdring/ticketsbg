package com.tickets.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.controllers.users.RegisterController;
import com.tickets.model.Customer;
import com.tickets.model.User;

@Component("personalInformationController")
@Scope("conversation.manual")
@ConversationName("purchaseConversation")
public class PersonalInformationController extends BaseController {

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    @Autowired
    private PurchaseController purchaseController;

    private Customer customer;

    private List<SelectItem> customerTypeItems = new ArrayList<SelectItem>();

    @PostConstruct
    public void init() {
        customer = loggedUserHolder.getLoggedUser();
        if (customer == null) {
            customer = new Customer();
        }

        RegisterController.formcustomerTypeSelectItems(customerTypeItems);
    }


    public void updateCustomer() {
        System.out.println("ASDf");
    }

    public String proceedToPayment() {
        //In case the user has come directly to this page
        //without choosing route
        if (purchaseController.getTickets().size() == 0) {
            return Screen.SEARCH_SCREEN.getOutcome();
        }

        if (customer instanceof User) {
            purchaseController.setUser((User) customer);
        }
        purchaseController.setCustomerInformation(customer);

        return Screen.PAYMENT_SCREEN.getOutcome();
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
