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
 * Board for the game Mancala. Keep in mind that this class is not optimized for speed.
 * If your agent needs special optimizations it is recommended to use your own data structure
 * in your own agent.
 *
 * Keep in mind, that there is a clear distinction between slot and depot. A slot is a place
 * that initially contains stones (standard rules say 6) and a depot (=Kalaha) is a place that
 * initially do not have any stones.
 *
 * Each player has multiple slots but only one depot.
 *
 */
@Root(name="board")
public class MancalaBoard implements Board {
    @ElementList(inline=true)
    private List<Slot> slots;
    @ElementList(inline=true)
    private List<PlayerDepot> depots;
    @Attribute(name = "stones-per-slot")
    private int stonesPerSlot;

    /**
     * Getter for slots
     * @return Returns all player slots (not depot) of this board
     */
    List<Slot> getSlots() {
        return slots;
    }

    /**
     * Getter for depots
     * @return Returns all player depots (=Kalaha, not slots) of this board.
     */
    List<PlayerDepot> getDepots() {
        return depots;
    }

    /**
     * Returns the next slot/depot given the id. Since the mancala board is cyclic this will always return
     * a valid slot/depot id.
     *
     * @param id The current slot/depot id
     * @return The next slot/depot id
     */
    public String next(String id) {
        for (Element elem : getElements()) {
            if (elem.getId().equals(id)) {
                return elem.getNext();
            }
        }
        return null;
    }

    /**
     * Convenient method to get all elements (slots and depots) of the board
     * @return
     */
    List<Element> getElements() {
        List<Element> elements = new ArrayList<>();
        elements.addAll(slots);
        elements.addAll(depots);
        return elements;
    }

    /**
     * Getter for stones per slot
     * @return Returns the amount of stones per slot at the beginning of the game
     */
    public int getStonesPerSlot() {
        return stonesPerSlot;
    }

    /**
     * Setter for stones per slot. Keep in mind this does not actually alter the slots.
     * @param stonesPerSlot Sets the amount of stones per slot at the beginning of the game.
     */
    public void setStonesPerSlot(int stonesPerSlot) {
        this.stonesPerSlot = stonesPerSlot;
    }

    /**
     * Checks whether the given ID is a valid slot id
     * @param id The questioned slot id
     * @return true if it is a slot, false if it is not a slot (does not exist or is a depot)
     */
    public boolean isSlot(String id) {
        for (Slot s : slots) {
            if (s.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the given ID is a valid depot id
     * @param id The questioned slot id
     * @return true if it is a depot, false if it is not a depot (does not exist or is a slot)
     */
    public boolean isDepot(String id) {
        for (PlayerDepot d : depots) {
            if(d.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the element by the given id
     * @param id the ID of the element
     * @return The Element instance or null if the element does not exist.
     */
    Element getElement(String id) {
        for (Element elem : getElements()) {
            if (elem.getId().equals(id)) {
                return elem;
            }
        }
        return null;
    }

    /**
     * Returns the player IDs of this board
     * @return A set containing the player IDs
     */
    Set<Integer> getPlayers() {
        Set<Integer> players = new HashSet<>();
        for (Element elem : getElements()) {
            players.add(elem.getOwner());
        }
        return players;
    }

    /**
     * Returns the ID of the enemy slot of the given id
     * @param id the ID of the own slot
     * @return the ID of the enemy slot or null if the given id does not exist
     */
    public String getEnemySlotOf(String id) {
        for (Slot slot : getSlots()) {
            if (slot.getId().equals(id)) {
                return slot.getEnemySlot();
            }
        }
        return null;
    }

    /**
     * The depot ID of the given slot ID. If a depot is given the depot ID is returned instead.
     * @param slotId The given slot ID
     * @return the corresponding depot ID or null if the slot does not exist.
     */
    String getDepotOf(String slotId) {
        Element elem = getElement(slotId);
        if (elem instanceof Slot) {
            Slot s = (Slot)elem;
            for (PlayerDepot depot : depots) {
                if (depot.getPlayer() == s.getOwner()) {
                    return depot.getId();
                }
            }
            return null;
        } else {
            return slotId;
        }
    }

    /**
     * Returns the depot id of the given playerId
     * @param playerId the given player ID
     * @return returns the depot id or null if the player does not exist
     */
    public String getDepotOfPlayer(int playerId) {
        for (PlayerDepot depot : depots) {
            if (depot.getPlayer() == playerId) {
                return depot.getId();
            }
        }
        return null;
    }

    /**
     * Class describing a slot of the board
     */
    @Root(name="slot")
    static class Slot extends Element {
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

    /**
     * Class describing the PlayerDepot of the board
     */
    @Root(name="player-depot")
    static class PlayerDepot extends Element {
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

    /**
     * abstract class describing some element (PlayerDepot/Slot) of the board
     */
    abstract static class Element {
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
}
