package com.tickets.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tickets.model.Agent;
import com.tickets.model.Firm;

@Service("agentsService")
public class AgentServiceImpl extends BaseService<Agent> implements
        AgentService {

    @SuppressWarnings("unchecked")
    @Override
    public List<Agent> getAgents(Firm firm) {
        return getDao().findByNamedQuery("Agent.findByFirm",
                new String[] { "firm" }, new Object[] { firm });
    }


}
