package at.pwd.boardgame.services;

import at.pwd.boardgame.game.agent.Agent;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
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
        // agent name is unique
        for (Agent a : agents) {
            if (a.toString().equals(agent.toString())) {
                return;
            }
        }

        agents.add(agent);
    }

    public List<Agent> getAgents() {
        return Collections.unmodifiableList(agents);
    }

    public void load(File jarFile, String className) throws Exception {
        Agent agent;
        try {
            URLClassLoader child = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, this.getClass().getClassLoader());
            Class classToLoad = Class.forName (className, true, child);
            agent = (Agent) classToLoad.newInstance ();
        } catch (MalformedURLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new Exception(e);
        }

        if (agent != null) {
            AgentService.getInstance().register(agent);
        }
    }
}
