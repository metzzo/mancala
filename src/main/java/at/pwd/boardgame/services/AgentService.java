package at.pwd.boardgame.services;

import at.pwd.boardgame.game.base.Agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rfischer on 22/04/2017.
 */
public class AgentService {
    private static AgentService instance;

    public static AgentService getInstance() {
        if (instance == null) {
            instance = new AgentService();
        }
        return instance;
    }

    private List<Agent> agents;

    private AgentService() {
        this.agents = new ArrayList<>();
    }

    public void register(Agent agent) {
        agents.add(agent);
    }

    public List<Agent> getAgents() {
        return Collections.unmodifiableList(agents);
    }
}
