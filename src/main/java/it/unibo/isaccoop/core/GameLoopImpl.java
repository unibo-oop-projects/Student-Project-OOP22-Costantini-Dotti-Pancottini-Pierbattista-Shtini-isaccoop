package it.unibo.isaccoop.core;

import java.util.List;
import java.util.logging.Logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.isaccoop.controller.input.ActionComponent;
import it.unibo.isaccoop.controller.input.ActionComponentImpl;
import it.unibo.isaccoop.controller.input.ActionController;
import it.unibo.isaccoop.controller.input.InputComponent;
import it.unibo.isaccoop.controller.input.PlayerInputComponent;
import it.unibo.isaccoop.controller.input.ShotInputComponent;
import it.unibo.isaccoop.graphics.Scene;
import it.unibo.isaccoop.model.room.Level;

/**
 * Implementation of GameLoop.
 */
public final class GameLoopImpl implements GameLoop {

    private final Scene view;
    private final Level level;
    private final List<InputComponent> inputComponents;
    private final ActionComponent actionComponent;
    private final ActionController actionController;
    private static final long DEFAULT_PERIOD = 20;
    private static final Logger LOGGER = Logger.getLogger(GameLoopImpl.class.getName());
    private boolean isPause;

    /**
     * GameLoopImpl constructor.
     *
     * @param view to be handled into game loop
     * @param level to be handled into game loop
     * @param actionController the actionController to manage keys pressed

     * The warning is suppressed because we need level reference in this class, the level is immutable.
     */
   
    public GameLoopImpl(final Scene view, final Level level, final ActionController actionController) {
        this.view = view;
        this.level = level;
        this.inputComponents = List.of(new PlayerInputComponent(this.level.getCurrentRoom()),
                new ShotInputComponent());
        this.actionComponent = new ActionComponentImpl();
        this.actionController = actionController;
        this.isPause = false;
    }

    /**
     * Method that represents the main loop of the game.
     */
    @Override
    public void gameLoop() {
        while (!level.isLevelComplete()) {
            final long current = System.currentTimeMillis();
            this.processActionsInput();
            if (!this.isPause()) {
                this.processPlayerInput();
                this.updateGame();
            }
            this.render();
            this.waitForNextFrame(current);
        }

    }

    /**
     * Method to update actions input.
     * */
    private void processActionsInput() {
        this.actionComponent.update(this.actionController, this);
    }

    /**
     * Private method that updates the input.
     */
    private void processPlayerInput() {
        level.getRooms().stream().filter(r -> r.getPlayer().isPresent())
            .forEach(x -> this.inputComponents.forEach(c -> c.update(x.getPlayer().get())));
    }
    /**
     * For each room present, update the room and check the events.
     * */
    private void updateGame() {
        level.getRooms().stream().filter(r -> r.getPlayer().isPresent())
            .forEach(x -> {
                x.updateRoom();
                x.executeEvents();
            });
    }

    /**
     * Method that renders the view.
     * */
    private void render() {
        view.render();
    }

    /**
     * Method to make the thread wait for the new frame.
     *
     * @param current represent current time.
     */
    private void waitForNextFrame(final long current) {
        final var period = GameLoopImpl.DEFAULT_PERIOD;
        final long dt = System.currentTimeMillis() - current;
        if (dt < period) {
            try {
                Thread.sleep(period - dt);
            } catch (InterruptedException e) {
                LOGGER.severe(e.getMessage());
            }
        }
    }

    @Override
    public boolean isPause() {
        return isPause;
    }

    @Override
    public void setPause(final boolean isPause) {
        this.isPause = isPause;
    }

    @Override
    //Warning suppressed because we need to expose level state to the ActionComponent (level is immutable)
   
    public Level getLevel() {
        return this.level;
    }
}
