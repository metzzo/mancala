package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.interfaces.Board;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

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

    public List<PlayerDepot> getDepots() {
        return depots;
    }

    public String next(String id) {
        for (Element elem : getElements()) {
            if (elem.getId().equals(id)) {
                return elem.getNext();
            }
        }
        return null;
    }

    public List<Element> getElements() {
        List<Element> elements = new ArrayList<>();
        elements.addAll(slots);
        elements.addAll(depots);
        return elements;
    }

    public int getNumStones() {
        return numStones;
    }
}

class Element {
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
}

@Root(name="slot")
class Slot extends Element { }

@Root(name="player-depot")
class PlayerDepot extends Element {
    @Attribute
    private int player;

    public int getPlayer() {
        return player;
    }
}

