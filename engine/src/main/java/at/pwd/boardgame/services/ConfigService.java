package at.pwd.boardgame.services;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by rfischer on 14/08/2017.
 */
public class ConfigService {
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

            computationTime = 10;
            stonesPerSlot = 6;
            slotsPerPlayer = 6;
        }
    }

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


    private ConfigService() {

    }

    public static ConfigService getInstance() {
        if (instance == null) {
            instance = new ConfigService();
        }
        return instance;
    }

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

    public ConfigService save() {
        Serializer serializer = new Persister();
        try {
            serializer.write(config, configFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public void setComputationTime(int computationTime) {
        this.config.computationTime = computationTime;
        save();
    }

    public int getComputationTime() {
        return this.config.computationTime;
    }

    public void setStonesPerSlot(int stonesPerSlot) {
        this.config.stonesPerSlot = stonesPerSlot;
        save();
    }

    public int getStonesPerSlot() {
        return this.config.stonesPerSlot;
    }

    public void setSlotsPerPlayer(int slotsPerPlayer) {
        this.config.slotsPerPlayer = slotsPerPlayer;
        save();
    }

    public int getSlotsPerPlayer() {
        return this.config.slotsPerPlayer;
    }

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
