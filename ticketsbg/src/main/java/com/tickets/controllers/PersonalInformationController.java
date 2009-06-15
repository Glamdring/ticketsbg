package com.tickets.controllers;

import org.apache.myfaces.orchestra.conversation.annotations.ConversationName;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("personalInformationController")
@Scope("conversation.access")
@ConversationName("purchaseConversation")
public class PersonalInformationController extends BaseController {

    private String name;
    private String contactPhone;
    private String email;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContactPhone() {
        return contactPhone;
    }
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
