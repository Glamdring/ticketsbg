package com.tickets.controllers;

import javax.annotation.PostConstruct;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tickets.controllers.users.LoggedUserHolder;
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

    public Customer getCustomer() {
        return customer;
    }

    @PostConstruct
    public void init() {
        customer = loggedUserHolder.getLoggedUser();
        if (customer == null) {
            customer = new Customer();
        }
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void updateCustomer() {
        System.out.println("ASDf");
    }

    public String proceedToPayment() {
        if (customer instanceof User) {
            purchaseController.getTicket().setUser((User) customer);
        }
        purchaseController.getTicket().setCustomerInformation(customer);

        return "paymentScreen";
    }
}
