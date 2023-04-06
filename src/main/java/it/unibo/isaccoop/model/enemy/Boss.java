package it.unibo.isaccoop.model.enemy;

import java.util.Optional;

import it.unibo.isaccoop.graphics.factory.ConcreteEnemyGraphicsComponentFactory;
import it.unibo.isaccoop.model.action.NonShootingHitStrategy;
import it.unibo.isaccoop.model.action.NonShootingMovementStrategy;
import it.unibo.isaccoop.model.action.ShootingHitStrategy;
import it.unibo.isaccoop.model.action.ShootingMovementStrategy;
import it.unibo.isaccoop.model.boundingbox.BoundingBox;
import it.unibo.isaccoop.model.common.Point2D;
import it.unibo.isaccoop.model.weapon.BaseWeaponShot;
import it.unibo.isaccoop.model.weapon.TimeIntervalWeapon;

/**
 * The class for the boss.
 * */
public class Boss extends AbstractEnemy {

    /**
     * The time to wait to change the boss's attack type.
     * */
    private static final int CHANGE_TIME = 10_000;

    /**
     * Weapon Time interval between shots
     * */
    private static final double WEAPON_INTERVAL = 1_000;

    /**
     * The time since the last change.
     * */
    private long lastChangeTime;

    /**
     * To figure out what kind of attack the boss has.
     * */
    private boolean isShotBoss;

    /**
     * Boss constructor.
     * */
    public Boss() {
        super(EnemyHearts.BOSS_HEARTS, new NonShootingHitStrategy(), new NonShootingMovementStrategy(),
                new ConcreteEnemyGraphicsComponentFactory().getBossGraphicsComponent());
        this.lastChangeTime = System.currentTimeMillis();
        this.isShotBoss = false;
    }

    /**
     * Change the type of attack of the boss based on time, with shooting or not.
     * @return if the Boss have to change the type of attack
     * */
    public boolean changeMode() {
        if (System.currentTimeMillis() - this.lastChangeTime >= Boss.CHANGE_TIME) {
            this.isShotBoss = !this.isShotBoss;
            this.lastChangeTime = System.currentTimeMillis();
        }
        return this.isShotBoss;
    }

    /**
     * Hit the player.
     * */
    @Override
    public void hit(final Point2D playerPosition) {
        if (this.changeMode()) {
            if (super.getHitStrategy() instanceof ShootingHitStrategy) {
                super.setHitStrategy(new NonShootingHitStrategy());
            } else {
                super.setHitStrategy(new ShootingHitStrategy(new TimeIntervalWeapon(Boss.WEAPON_INTERVAL,
                        (start, direction) -> new BaseWeaponShot(start, direction, new ConcreteEnemyGraphicsComponentFactory().getBossBaseWeaponShotGraphicsComponent()))));
            }
        }
        super.getHitStrategy().hit(Optional.of(playerPosition.sub(this.getCoords())), this);
    }

    /**
     * Move the boss towards the player.
     * */
    @Override
    public void move(final Point2D playerPosition, final BoundingBox containerBox) {
        if (this.changeMode()) {
            if (super.getMovementStrategy() instanceof ShootingMovementStrategy) {
                super.setMovementStrategy(new NonShootingMovementStrategy());
            } else {
                super.setMovementStrategy(new ShootingMovementStrategy());
            }
        }
        super.move(playerPosition, containerBox);
    }

}
