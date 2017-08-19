# Overview

The Mancala Boardgame Engine is split into several projects for it to be as extensible as possible:

* engine: Project containing the main mancala engine. It handles the User Interface, game logic and loading of new agents. 
* mctsagent: Project containing an agent playing by Monte Carlo Treesearch.
* alphabetaagent: Project containing an agent playing by Alpha Beta Pruning.
* randomagent: Project containing an agent playing completely random.

## What is an Agent?

What is an agent? An agent is basically some class implementing the doTurn() method and returning an action the engine should play for the particular agent in the current turn. The engine itself implements a HumanAgent where the human decides what turn to play, but there are several other types of agents (mctsagent, alphabetaagent, randomagent and maybe your new agent) that will be able to play the game.

Loading new agents can be done at runtime, so no recompilation is needed. You just have to create a .JAR file containing the agent class, load the jar with the full class name (including package) and voila the engine is able to play with your new agent. Please read the documentation for more details.

## How to create a new Agent?

We will develop an agent that will always return the most left selectable slot on the board.
Tested IDEs include Eclipse () or IntelliJ IDEA (2017.5), other configurations should work too.

### Using IntelliJ

First check out the GitHub repository on your local machine.

![IntelliJ - Create Java project](assets/intellij_1.png)

We create a new Java project using Gradle and Java 1.8 (please install proper JDK if missing). Now we press Next.


![IntelliJ - Set up gradle](assets/intellij_2.png)

The GroupId should be a unique identifier, which is usually the same as your package name. For example, I chose at.pwd.choosefirstagent and choosefirstagent for the ArtifactId. The version does not need to be changed. Now we press Next.


![IntelliJ - Set up gradle 2](assets/intellij_3.png)

Gradle JVM should be set to Java 1.8 and the checkbox "Create directories for empty content roots automatically" should be checked. Now we press Next.


![IntelliJ - Set up project](assets/intellij_4.png)

Now choose a Project name and a Project location. I will keep it at the default. Now we press Finish, if you are prompted with the message, that the directory does not exist, press OK.

We need to add some JAR libraries as a dependency to the project. For this please copy the /lib folder of the repository into the root folder of your new project. This folder contains the dependencies (including the Mancala Boardgame Engine).

Gradle needs to know about these dependencies. In the dependencies scope of the build.gradle file add
```
compile files('lib/mancala-engine.jar', 'lib/lib/simple-xml-2.7.jar', 'lib/lib/stax-1.2.0.jar', 'lib/lib/stax-api-1.0.1.jar', 'lib/lib/xpp3-1.1.3.3.jar')
```
now import the changes and build the project. This may take a few seconds.


The build.gradle file should look something like this
```
group 'at.pwd.choosefirstagent'
version '1.0-SNAPSHOT'

apply plugin: 'java'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
}
dependencies {
    compile files('lib/mancala-engine.jar', 'lib/lib/simple-xml-2.7.jar', 'lib/lib/stax-1.2.0.jar', 'lib/lib/stax-api-1.0.1.jar', 'lib/lib/xpp3-1.1.3.3.jar')
    testCompile group: 'junit', name: 'junit', version: '4.12'
}
```

Now create a new Java package in the src/main folder, by right clicking on the folder in the Project tree. I will name the package *at.pwd.choosefirstagent*. Create a new class in the newly created package that will contain the agent. The name I choose is ChooseFirstAgent. This class should implement the MancalaAgent interface, the doTurn method and a toString method. It should look something like the following:
```
package at.pwd.choosefirstagent;

import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.mancala.agent.MancalaAgent;
import at.pwd.boardgame.game.mancala.agent.MancalaAgentAction;

public class ChooseFirstAgent implements MancalaAgent {
    @Override
    public MancalaAgentAction doTurn(int i, MancalaGame mancalaGame) {
        return null;
    }
    @Override
    public String toString() {
    	return "";
    }
}
```

Now the class needs some logic. We will simply get a list of the currently selectable slots by mancalaGame.getSelectableSlots() and use the first slotId in this list as a move. We need to pack this slot id in a MancalaAgentAction instance. Additionally we need to implement a toString method returning the name of this agent.

```
package at.pwd.choosefirstagent;

import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.mancala.agent.MancalaAgent;
import at.pwd.boardgame.game.mancala.agent.MancalaAgentAction;

import java.util.List;

public class ChooseFirstAgent implements MancalaAgent {
    @Override
    public MancalaAgentAction doTurn(int computationTime, MancalaGame mancalaGame) {
        // get a list of all currently selectable slots
        List<String> slots = mancalaGame.getSelectableSlots();
        // since this list will never be empty (otherwise the game would be over), we dont need a additional check
        // Slot IDs are unique strings strings
        String selectedSlot = slots.get(0);
        // now we pack the selected slot in an agent action and return it
        // the Mancala Boardgame Engine will then apply this action onto the slot
        return new MancalaAgentAction(selectedSlot);
    }
	
    @Override
    public String toString() {
        return "Choose First Agent";
    }
}
```

![IntelliJ - Create run configuration](assets/intellij_5.png)
To test the new agent we need a new build configuration. Select in the menu Run/Run... and then Edit Configurations. Now press on the "+" symbol and select Application. The main class should be *at.pwd.boardgame.Main* - which should be listed if you imported the jar files properly.
The Program arguments should be the full classname (including the package), in this case at.pwd.choosefirstagent.ChooseFirstAgent this is necessary for the engine to load the agent. The "Use classpath of module" box should be "choosefirstagent_main".

Press Okay and now run the application using the newly created build configuration. The Choose First Agent should be selectable as an agent and play as intended.

![IntelliJ - Export](assets/intellij_6.png)
To allow other people to import the agent into the engine, without compiling it, we need to export the agent as a JAR file. To do so, we need to create a new build configuration. So select Edit Configurations. Press the "+" symbol again. This time select "Gradle". The selected gradle project should be "choosefirstagent" and the Task should be "jar". After running the gradle task the jar should be placed in the build/libs folder named "choosefirstagent-1.0-SNAPSHOT.jar" (name may be different depending on the selected ArtifactId and version).

This jar can now be loaded into any Mancala Boardgame Engine instance.


### Using Eclipse
TODO






Recommended configurations: 

IntelliJ IDEA 2017.5
Windows 10 / OS X El Capitan, 64 Bit, Java 1.8
