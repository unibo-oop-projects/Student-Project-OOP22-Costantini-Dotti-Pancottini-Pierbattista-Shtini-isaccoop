package it.unibo.isaccoop.model.collision;

import java.util.List;

import it.unibo.isaccoop.model.common.MapElement;
import it.unibo.isaccoop.model.player.Player;

/**
 *
 * Factory for collision check.
 *
 */
public interface CollisionCheckFactory {
    /**
     * Get collision with item.
     * @param p reference to player.
     * @param i list of mapElements to check collision, in this case Item.
     *
     * @return CollisionCheck refer to item.
     */
    CollisionCheck getCollisionWithItemChecker(Player p, List<MapElement> i);
    /**
     * Get collision with player shot.
     * @param p reference to player.
     * @param i list of mapElements to check collision, in this case Enemy.
     *
     * @return CollisionCheck refer to player shot.
     */
    CollisionCheck getCollisionPlayerShotChecker(Player p, List<MapElement> e);
    /**
     * Get collision with enemy.
     * @param p reference to player.
     * @param i list of mapElements to check collision, in this case Enemy.
     *
     * @return CollisionCheck refer to enemy.
     */
    CollisionCheck getCollisionWithEnemyChecker(Player p, List<MapElement> e);
    /**
     * Get collision with enemy shot.
     * @param p reference to player.
     * @param i list of mapElements to check collision, in this case Enemy.
     *
     * @return CollisionCheck refer to enemy shot.
     */
    CollisionCheck getCollisionWithEnemyShotChecker(Player p, List<MapElement> e);
}
