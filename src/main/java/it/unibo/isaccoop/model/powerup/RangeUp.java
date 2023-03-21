package it.unibo.isaccoop.model.powerup;

import it.unibo.isaccoop.model.player.PlayerStat;

/**
 * Represents the power up range.
 * */
public class RangeUp extends PowerUp {

    private static final float RANGE_SUPER_UP = 2;
    private static final float RANGE_UP = 1;
    /**
     * Increase the player's range.
     * @param p reference to player.
     * */
    @Override
    public void interact(final PlayerStat p) {
        if (super.isSuperItem()) {
            p.setRange(p.getRange() + RANGE_SUPER_UP);
            this.setPrice(10);
        } else {
            p.setRange(p.getRange() + RANGE_UP);
            this.setPrice(5);
        }
    }

}
