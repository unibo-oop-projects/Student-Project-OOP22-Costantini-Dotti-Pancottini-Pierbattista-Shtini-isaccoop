package it.unibo.isaccoop.model.powerup;

import org.apache.commons.lang3.tuple.Pair;

import it.unibo.isaccoop.model.player.PlayerStat;

/**
 * Represents the power up tears.
 * */
public class TearsUp extends PowerUp {

    private static final int TEARS_SUPER_UP = 2;
    private static final int TEARS_UP = 1;
    /**
     *
     * @param coords
     */
    public TearsUp(Pair<Double, Double> coords) {
        super(coords);
    }
    /**
     *  Increase the player's speed.
     *  @param p reference to player.
     * */
    @Override
    public void interact(final PlayerStat p) {
        if (super.isSuperItem()) {
            p.setTears(p.getTears() + TEARS_SUPER_UP);
        } else {
            p.setTears(p.getTears() + TEARS_UP);
        }
    }

}
