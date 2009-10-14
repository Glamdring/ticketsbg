package com.tickets.controllers.users;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.controllers.BaseController;
import com.tickets.exceptions.UserException;
import com.tickets.model.User;
import com.tickets.services.UserService;

@Controller("activationController")
@Scope("request")
public class ActivationController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    LoggedUserHolder loggedUserHolder;

    public void performActivation(PhaseEvent evt){
        if (evt.getPhaseId() == PhaseId.INVOKE_APPLICATION) {
            String activationCode = evt.getFacesContext().getExternalContext().getRequestParameterMap().get("code");
            if (activationCode != null) {
                try {
                    User user = userService.activateUserWithCode(activationCode);
                    loggedUserHolder.setLoggedUser(user);
                    addMessage("activationSuccessful");
                } catch (UserException ex) {
                    addError(ex.getMessageKey());
                }
            }
        }
    }
}
