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
 * Singleton service that manages the agents.
 */
public class AgentService {
    private static AgentService instance;

    /**
     * @return Returns the singleton instance
     */
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

    /**
     * Registers a new Agent. If the same agent (based on the name returned by toString())
     * already exists it is not added.
     * @param agent The agent to be added.
     */
    public void register(Agent agent) {
        // agent name is unique
        for (Agent a : agents) {
            if (a.toString().equals(agent.toString())) {
                return;
            }
        }

        agents.add(agent);
    }

    /**
     * @return Returns a list containing all the available agents. This list may not be modified.
     */
    public List<Agent> getAgents() {
        return Collections.unmodifiableList(agents);
    }

    /**
     * Loads a new agent from the jarfile and registers it.
     * Keep in mind: It is not added to the ConfigService, this has to be done manually.
     *
     * @param jarFile what jar should be loaded
     * @param className  what is the class name of the agent
     * @throws Exception Thrown if loading of the agent failed
     */
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
