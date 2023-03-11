package it.unibo.isaccoop.model.room;

import java.util.List;
import java.util.Optional;

import it.unibo.isaccoop.model.ai.AIEnemy;
import it.unibo.isaccoop.model.common.MapElement;
import it.unibo.isaccoop.model.common.RoomType;

/**
 * Interface to model the concept of Room inside the game.
 */
public interface Room extends MapElement {

    /**
     * Get the doors in this room.
     * @return the doors in this room
     */
    List<Door> getDoors();

    //TODO get per : lista powerup, obstacles, enemy, optional<Boss>

    /**
     * @return the type of this room
     */
    RoomType getRoomType();

    /**
     * Get {@link AIEnemy} attached to this {@link Room}.
     *
     * @return {@link AIEnemy} attached to this {@link Room}
     * */
    Optional<AIEnemy> getRoomAI();
}
