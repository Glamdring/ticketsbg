package com.tickets.controllers;

import javax.faces.model.ListDataModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tickets.annotations.Action;
import com.tickets.controllers.security.AccessLevel;
import com.tickets.controllers.users.LoggedUserHolder;
import com.tickets.model.Agent;
import com.tickets.services.AgentService;
import com.tickets.services.Service;


@Controller("agentController")
@Scope("conversation.access")
@Action(accessLevel=AccessLevel.FIRM_COORDINATOR)
public class AgentController extends BaseCRUDController<Agent> {

    @Autowired
    private AgentService service;

    @Autowired
    private LoggedUserHolder loggedUserHolder;

    private Agent agent = new Agent();

    private ListDataModel agentsModel;

    @Override
    protected void refreshList() {
        agentsModel = new ListDataModel(service.list(Agent.class));

        // End the current conversation in case the list of roles
        // is refreshed, but only if the bean has not just been constructed
        if (agent != null)
            endConversation();
    }

    @Override
    public void save() {
        agent.setFirm(loggedUserHolder.getLoggedUser().getFirm());
        super.save();
    }

    public Agent getAgent() {
        return agent;
    }


    public void setAgent(Agent agent) {
        this.agent = agent;
    }


    public ListDataModel getAgentsModel() {
        return agentsModel;
    }

    public void setAgentsModel(ListDataModel agentsModel) {
        this.agentsModel = agentsModel;
    }

    @Override
    protected Agent createEntity() {
        return new Agent();
    }

    @Override
    protected Agent getEntity() {
        return agent;
    }

    @Override
    protected void setEntity(Agent entity) {
        setAgent(entity);
    }

    @Override
    protected String getListScreenName() {
        return "agentsList";
    }

    @Override
    protected String getSingleScreenName() {
        return "agentScreen";
    }

    @Override
    protected ListDataModel getModel() {
        return agentsModel;
    }

    @Override
    protected Service getService() {
        return service;
    }

}
