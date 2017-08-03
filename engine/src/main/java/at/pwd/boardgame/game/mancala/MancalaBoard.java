package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.base.Board;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rfischer on 14/04/2017.
 */

@Root(name="board")
public class MancalaBoard implements Board {
    @ElementList(inline=true)
    private List<Slot> slots;
    @ElementList(inline=true)
    private List<PlayerDepot> depots;
    @Attribute(name = "numstones")
    private int numStones;

    public List<Slot> getSlots() {
        return slots;
    }

    List<PlayerDepot> getDepots() {
        return depots;
    }

    String next(String id) {
        for (Element elem : getElements()) {
            if (elem.getId().equals(id)) {
                return elem.getNext();
            }
        }
        return null;
    }

    List<Element> getElements() {
        List<Element> elements = new ArrayList<>();
        elements.addAll(slots);
        elements.addAll(depots);
        return elements;
    }

    int getNumStones() {
        return numStones;
    }

    public void setNumStones(int numStones) {
        this.numStones = numStones;
    }

    public boolean isSlot(String id) {
        for (Slot s : slots) {
            if (s.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDepot(String id) {
        for (PlayerDepot d : depots) {
            if(d.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    Element getElement(String id) {
        for (Element elem : getElements()) {
            if (elem.getId().equals(id)) {
                return elem;
            }
        }
        return null;
    }

    Set<Integer> getPlayers() {
        Set<Integer> players = new HashSet<>();
        for (Element elem : getElements()) {
            players.add(elem.getOwner());
        }
        return players;
    }

    String getEnemySlotOf(String id) {
        for (Slot slot : getSlots()) {
            if (slot.getId().equals(id)) {
                return slot.getEnemySlot();
            }
        }
        return null;
    }

    String getDepotOf(String slot) {
        Element elem = getElement(slot);
        if (elem instanceof Slot) {
            Slot s = (Slot)elem;
            for (PlayerDepot depot : depots) {
                if (depot.getPlayer() == s.getOwner()) {
                    return depot.getId();
                }
            }
            throw new RuntimeException("Unknown id");
        } else {
            return slot;
        }
    }

    public String getDepotOfPlayer(int playerId) {
        for (PlayerDepot depot : depots) {
            if (depot.getPlayer() == playerId) {
                return depot.getId();
            }
        }
        throw new RuntimeException("Unknwon player id");
    }

    @Root(name="slot")
    public static class Slot extends Element {
        @Attribute
        private int belongs;

        @Attribute
        private String enemy;

        public int belongsToPlayer() {
            return belongs;
        }

        public String getEnemySlot() { return enemy; }

        @Override
        public int getOwner() {
            return belongs;
        }
    }

    @Root(name="player-depot")
    public static class PlayerDepot extends Element {
        @Attribute
        private int player;

        public int getPlayer() {
            return player;
        }

        @Override
        public int getOwner() {
            return player;
        }
    }
}

abstract class Element {
    @Attribute
    private int column;

    @Attribute
    private int row;

    @Attribute(name="columnspan", required = false)
    private int columnSpan;

    @Attribute(name="rowspan", required = false)
    private int rowSpan;

    @Attribute
    private String id;

    @Attribute
    private String next;

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getColumnSpan() {
        return columnSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public String getId() {
        return id;
    }

    public String getNext() {
        return next;
    }

    public abstract int getOwner();
}

