package at.pwd.boardgame.services;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;

/**
 * Persists and loads the configuration of the mancala game.
 */
public class ConfigService {
    private static final int DEFAULT_COMPUTATION_TIME = 10;
    private static final int DEFAULT_STONES_PER_SLOT = 6;
    private static final int DEFAULT_SLOTS_PER_PLAYER = 6;

    /**
     * Private class for storing the main config as XML using SimpleXML
     */
    @Root(name="config")
    private static class MainConfig {


        @ElementList(name = "agents")
        ArrayList<AgentConfig> agents;

        @Element(name = "computation-time")
        int computationTime;

        @Element(name="stones-per-slot")
        int stonesPerSlot;

        @Element(name="slots-per-player")
        int slotsPerPlayer;

        MainConfig() {
            agents = new ArrayList<>();

            computationTime = DEFAULT_COMPUTATION_TIME;
            stonesPerSlot = DEFAULT_STONES_PER_SLOT;
            slotsPerPlayer = DEFAULT_SLOTS_PER_PLAYER;
        }
    }

    /**
     * Private class for storing the agent config as XML using SimpleXML
     */
    @Root(name = "agent")
    private static class AgentConfig {
        @Element(name = "path")
        String path;

        @Element(name = "classname")
        String className;

        AgentConfig() { }
        AgentConfig(String path, String className) {
            this.path = path;
            this.className = className;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof AgentConfig) {
                AgentConfig ac = (AgentConfig)obj;
                return path.equals(ac.path) && className.equals(ac.className);
            } else {
                return false;
            }
        }
    }


    private static ConfigService instance;
    private MainConfig config;
    private File configFile;


    private ConfigService() { }

    /**
     * @return Returns the singleton instance of ConfigServie
     */
    public static ConfigService getInstance() {
        if (instance == null) {
            instance = new ConfigService();
        }
        return instance;
    }

    /**
     * Loads the config from the given file
     * @param file Which file to load
     * @return the same instance (for chaining)
     */
    public ConfigService load(File file) {
        if (file.exists()) {
            Serializer serializer = new Persister();
            try {
                config = serializer.read(MainConfig.class, file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            config = new MainConfig();
        }
        this.configFile = file;

        // load Agents
        for (AgentConfig agentConfig : config.agents) {
            try {
                AgentService.getInstance().load(new File(agentConfig.path), agentConfig.className);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return this;
    }

    /**
     * Saves the config to the previously (in load()) specified configuration
     * @return The same instance (for chaining)
     */
    public ConfigService save() {
        Serializer serializer = new Persister();
        try {
            serializer.write(config, configFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    /**
     * Sets the computation time (in seconds) and saves
     * @param computationTime the value
     */
    public void setComputationTime(int computationTime) {
        this.config.computationTime = computationTime;
        save();
    }

    /**
     * @return Returns the computation time  (in seconds)
     */
    public int getComputationTime() {
        return this.config.computationTime;
    }

    /**
     * Sets the stones per slot and saves
     * @param stonesPerSlot the value
     */
    public void setStonesPerSlot(int stonesPerSlot) {
        this.config.stonesPerSlot = stonesPerSlot;
        save();
    }

    /**
     * @return Returns the stones per slot
     */
    public int getStonesPerSlot() {
        return this.config.stonesPerSlot;
    }

    /**
     * Sets the slots per player and saves
     * @param slotsPerPlayer the value
     */
    public void setSlotsPerPlayer(int slotsPerPlayer) {
        this.config.slotsPerPlayer = slotsPerPlayer;
        save();
    }

    /**
     * @return Returns the slot per player
     */
    public int getSlotsPerPlayer() {
        return this.config.slotsPerPlayer;
    }

    /**
     * adds a new agent to the configuration. If the same agent already exists it is not added.
     * @param jarFile The jar file of the given agent
     * @param className The class name of the given agent
     */
    public void addAgent(File jarFile, String className) {
        AgentConfig ac = new AgentConfig(jarFile.getPath(), className);
        for (AgentConfig oldConfigs : config.agents) {
            if (oldConfigs.equals(ac)) {
                return; // do not add twice
            }
        }
        config.agents.add(ac);
        save();
    }
}
