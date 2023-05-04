package it.unibo.isaccoop.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.isaccoop.model.room.Level;

/**
 * Creates a GUI to display the {@link Minimap}, the {@link PlayerStat}s and a legend explaining
 * the meaning of colors of {@link Minimap}.
 * This GUI shows: <br> the room the player is, <br> the completed rooms, <br> the uncompleted rooms
 * <br>a legend to explain the minimap colors, <br>the player statistics.
 */
public class OverlayGUI extends JPanel {

    private static final long serialVersionUID = -4109905993803098411L;
    private static final Font FONT = new Font("Verdana", Font.PLAIN, 12);
    private static final Map<CellStatus, Color> COLOR_MAP = Map.of(
            CellStatus.PLAYER, new Color(255, 0, 0), //red
            CellStatus.UNCOMPLETED_ROOM, new Color(243, 225, 75), //yellow
            CellStatus.COMPLETED_ROOM, new Color(139, 218, 83)); //green

    private final JLabel lblInfoRoom = new JLabel();
    private final JPanel statsPanel1 = new JPanel();
    private final JPanel statsPanel2 = new JPanel();
    private final List<JLabel> stats1 = new LinkedList<>();
    private final List<JLabel> stats2 = new LinkedList<>();
    private final transient Level lvl;
    private final List<JButton> roomBtns = new ArrayList<>();

    private enum CellStatus {
        /**
         * Where the player is.
         */
        PLAYER("You are here"),
        /**
         * Uncompleted room (there are still enemies to defeat).
         */
        UNCOMPLETED_ROOM("Uncompleted room"),
        /**
         * Completed room (there are no more enemies to defeat).
         */
        COMPLETED_ROOM("Completed room");

        private final String descr;
        CellStatus(final String descr) {
            this.descr = descr;
        }

        private String getDescr() {
            return this.descr;
        }
    }

    /**
     * Create a new GUI for {@link Minimap}.
     * @param level the level of which to show the layout
     * @param roomWidth the width of the minimap
     * @param minimapHeight the height of the minimap
     *
     * The warning is suppressed because we need level reference in this Class.
     *
     */
   
    public OverlayGUI(final Level level, final int roomWidth, final int minimapHeight) {
        this.lvl = level;
        final int proportion = 21;
        final int horizontalGap = roomWidth / proportion;
        final int verticalGap = 10;

        // main layout
        this.setSize(roomWidth, minimapHeight);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, horizontalGap, verticalGap));

        // minimap container
        final JPanel minimapPanel = new JPanel(new BorderLayout());
        // panel where to place the rooms
        final JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        minimapPanel.add(centerPanel, BorderLayout.CENTER);
        // panel where to place some information about the current level
        final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(lblInfoRoom);
        minimapPanel.add(topPanel, BorderLayout.NORTH);

        // legend to explain the meaning of minimap
        final JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.X_AXIS));
        final JLabel lblInfo = new JLabel("Legend: ");
        legendPanel.add(lblInfo);
        COLOR_MAP.forEach((cellStatus, color) -> {
            final JButton btn = new JButton(cellStatus.getDescr());
            btn.setBackground(color);
            legendPanel.add(btn);
        });

        // creates labels for stat strings part 1
        this.statsPanel1.setLayout(new BoxLayout(this.statsPanel1, BoxLayout.Y_AXIS));
        getStatsStringsPart1().forEach(s -> {
            final JLabel lbl = new JLabel(" ");
            this.stats1.add(lbl);
            this.statsPanel1.add(lbl);
        });
        // creates labels for stat strings part 2
        this.statsPanel2.setLayout(new BoxLayout(this.statsPanel2, BoxLayout.Y_AXIS));
        getStatsStringsPart2().forEach(s -> {
            final JLabel lbl = new JLabel(" ");
            this.stats2.add(lbl);
            this.statsPanel2.add(lbl);
        });
        this.add(minimapPanel);
        this.add(this.statsPanel1);
        this.add(this.statsPanel2);
        this.add(legendPanel);

        for (int i = 0; i < lvl.getRooms().size(); i++) {
            final JButton jb = new JButton(Integer.toString(i + 1));
            jb.setFont(FONT);
            centerPanel.add(jb);
            jb.setEnabled(false);
            this.roomBtns.add(jb);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        lblInfoRoom.setText(getRoomStatusString());
        this.roomBtns.forEach(btn -> {
            final var currRoom = this.lvl.getCurrentRoom();
            final var completeRooms = this.lvl.getMinimap().getCompletedRooms();
            final var uncompleteRooms = this.lvl.getMinimap().getUncompletedRooms();
            if (currRoom.equals(this.lvl.getRooms().get(this.roomBtns.indexOf(btn)))) {
                btn.setBackground(COLOR_MAP.get(CellStatus.PLAYER));
            } else if (completeRooms.contains(this.lvl.getRooms().get(this.roomBtns.indexOf(btn)))) {
                btn.setBackground(COLOR_MAP.get(CellStatus.COMPLETED_ROOM));
            } else if (uncompleteRooms.contains(this.lvl.getRooms().get(this.roomBtns.indexOf(btn)))) {
                btn.setBackground(COLOR_MAP.get(CellStatus.UNCOMPLETED_ROOM));
            }
        });
        updatePlayerStats();
    }

    /**
     * Updates player stats.
     */
    private void updatePlayerStats() {
        for (int i = 0; i < this.stats1.size(); i++) {
            this.stats1.get(i).setText(getStatsStringsPart1().get(i));
        }
        for (int i = 0; i < this.stats2.size(); i++) {
            this.stats2.get(i).setText(getStatsStringsPart2().get(i));
        }
    }

    /**
     * Return a string to show information about the status of current room.
     * @return a string that contains information about status of current room
     */
    private String getRoomStatusString() {
        return "Rooms Completed: " + lvl.getMinimap().getCompletedRooms().size()
                + " of " + lvl.getRooms().size();
    }

    /**
     * Build a list containing first part of player statistics, as strings.
     * @return an unmodifiable list containing first part of player statistics
     */
    private List<String> getStatsStringsPart1() {
        return List.of("Player statistics:",
                "Coins: " + this.lvl.getPlayer().getCoin(),
                "Damage: " + this.lvl.getPlayer().getDamage());
    }

    /**
     * Build a list containing second part of player statistics, as strings.
     * @return an unmodifiable list containing second part of player statistics
     */
    private List<String> getStatsStringsPart2() {
        return List.of(
                "Hearts: " + this.lvl.getPlayer().getHeart() + " of " + this.lvl.getPlayer().getMaxHeart(),
                "Speed: " + this.lvl.getPlayer().getSpeed(),
                "Tears: " + this.lvl.getPlayer().getTears());
    }
}
