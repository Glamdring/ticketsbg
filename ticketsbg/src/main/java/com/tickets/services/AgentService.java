package com.tickets.services;

import java.util.List;

import com.tickets.model.Agent;
import com.tickets.model.Firm;

public interface AgentService extends Service<Agent> {

    List<Agent> getAgents(Firm firm);
}
