package it.unibo.isaccoop;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unibo.isaccoop.model.common.RoomType;
import it.unibo.isaccoop.model.room.LevelFactory;
import it.unibo.isaccoop.model.room.LevelFactoryImpl;
import it.unibo.isaccoop.model.room.Room;

/**
 * RoomFactory test.
 * */
class LevelFactoryTest {
    private static final int NUMBER_OF_ROOMS = 10;
    private static final int SINGLE_ROOM_COUNT = 1;

    private LevelFactory lvlFactory = new LevelFactoryImpl();

    @Test 
    void testRoomCreation() {
        final var lvl = this.lvlFactory.createLevel(NUMBER_OF_ROOMS);
        final var rooms = lvl.getRooms();
        // check if a just created level has the correct number of rooms
        assertTrue(rooms.size() == NUMBER_OF_ROOMS);
        // and if it is not complete
        assertFalse(lvl.isComplete());
    }

    @Test
    void testRoomTypeCount() {
        final var lvl = this.lvlFactory.createLevel(NUMBER_OF_ROOMS);
        final var rooms = lvl.getRooms();
        // in a level, there must be ONLY ONE START, ONE SHOP, ONE TREASURE and ONE BOSS room
        assertTrue(rooms.stream()
                .filter(r -> r.getRoomType() == RoomType.START)
                .count() == SINGLE_ROOM_COUNT);
        assertTrue(rooms.stream()
                .filter(r -> r.getRoomType() == RoomType.SHOP)
                .count() == SINGLE_ROOM_COUNT);
        assertTrue(rooms.stream()
                .filter(r -> r.getRoomType() == RoomType.TREASURE)
                .count() == SINGLE_ROOM_COUNT);
        assertTrue(rooms.stream()
                .filter(r -> r.getRoomType() == RoomType.BOSS)
                .count() == SINGLE_ROOM_COUNT);

        // in a level, the remaining rooms must be STANDARD
        assertTrue(rooms.stream()
                .filter(r -> r.getRoomType() == RoomType.STANDARD)
                .count() == (NUMBER_OF_ROOMS - 4));

        //System.out.println("rooms" + rooms);
        /*rooms.forEach(r -> System.out.println("\n\n\nitems: " + r.getItems() + "\n"
        + "player: " + r.getPlayer() + "\n"
        + "powerups: " + r.getPowerUps() + "\n"
        + "roomAi: " + r.getRoomAI() + "\n"
        + "roomtype: " + r.getRoomType()));*/
    }

    @Test 
    void testPlayerLocationInLevel() {
        // when a level is created, the player must be in the START room ONLY
        final var lvl = this.lvlFactory.createLevel(NUMBER_OF_ROOMS);
        final var rooms = lvl.getRooms();

        // considering only the START rooms (there will be only one), and in that one
        // where will be the player
        assertTrue(rooms.stream().filter(r -> r.getRoomType() == RoomType.START)
                .allMatch(r -> r.getPlayer().isPresent()));
        // considering the other rooms, the player will not be in any of them
        assertTrue(rooms.stream().filter(r -> r.getRoomType() != RoomType.START)
                .allMatch(r -> r.getPlayer().isEmpty()));
    }

    @Test
    void testItemsInTreasureRoom() {
        // when a level is created, there must be powerups only in TREASURE and SHOP rooms
        final var lvl = this.lvlFactory.createLevel(NUMBER_OF_ROOMS);
        final var rooms = lvl.getRooms();

        // considering only the TREASURE rooms (there will be only one), and in that one
        // where will be at least one powerup
        assertTrue(rooms.stream().filter(r -> r.getRoomType() == RoomType.TREASURE)
                .allMatch(r -> r.getPowerUps().isPresent()));
        assertTrue(rooms.stream().filter(r -> r.getRoomType() == RoomType.TREASURE)
                .allMatch(r -> r.getPowerUps().get().size() == 1));
    }

    @Test
    void testItemsInShopRoom() {
        // when a level is created, there must be powerups only in TREASURE and SHOP rooms
        final var lvl = this.lvlFactory.createLevel(NUMBER_OF_ROOMS);
        final var rooms = lvl.getRooms();

        // considering only the SHOP rooms (there will be only one), and in that one
        // where will be at least one powerup
        assertTrue(rooms.stream().filter(r -> r.getRoomType() == RoomType.SHOP)
                .allMatch(r -> r.getPowerUps().isPresent()));
        assertTrue(rooms.stream().filter(r -> r.getRoomType() == RoomType.SHOP)
                .allMatch(r -> r.getPowerUps().get().size() == 3));
    }

    @Test
    void testRoomAiInRooms() {
        // when a level is created, there must be an AIenemy only in BOSS and STANDARD rooms
        final var lvl = this.lvlFactory.createLevel(NUMBER_OF_ROOMS);
        final var rooms = lvl.getRooms();

        // BOSS and STANDARD rooms must have an AIenemy 
        assertTrue(rooms.stream().filter(r -> r.getRoomAI().isPresent())
                .allMatch(r -> checkConditionForAiRoom(r)));
        // all other room types must NOT have an AIenemy
        assertTrue(rooms.stream().filter(r -> r.getRoomAI().isEmpty())
                .allMatch(r -> !checkConditionForAiRoom(r)));
    }
    
    @Test
    void testItemsInStandardRooms() {
        // when a level is created, there must be at least one items ONLY in STANDARD rooms
        final var lvl = this.lvlFactory.createLevel(NUMBER_OF_ROOMS);
        final var rooms = lvl.getRooms();

        // only STANDARD rooms must have an item list 
        assertTrue(rooms.stream().filter(r -> r.getItems().isPresent())
                .allMatch(r -> r.getRoomType() == RoomType.STANDARD));
        // all other room types must NOT have an item list
        assertTrue(rooms.stream().filter(r -> r.getRoomAI().isEmpty())
                .allMatch(r -> r.getRoomType() != RoomType.STANDARD));
    }
    
    /**
     * Check if the current room to build needs the AiEnemy object.
     * @return true if the room need the AiEnemy object, false otherwise
     */
    private boolean checkConditionForAiRoom(final Room room) {
        return room.getRoomType() == RoomType.STANDARD || room.getRoomType() == RoomType.BOSS;
    }
}
