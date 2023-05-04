package it.unibo.isaccoop.core;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_P;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_W;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.isaccoop.controller.input.ActionController;
import it.unibo.isaccoop.controller.input.ActionControllerImpl;
import it.unibo.isaccoop.controller.input.InputController;
import it.unibo.isaccoop.controller.input.KeyboardInputController;
import it.unibo.isaccoop.graphics.SwingScene;
import it.unibo.isaccoop.model.room.LevelControllerImpl;

/**
 * Implementation of gameEngine.
 */
public final class GameEngineImpl implements GameEngine {

    private final Map<String, InputController> controllers = new HashMap<>();
    private ActionController actionController;
    private GameLoop gameLoop;

    private static final int MAX_ROOMS = 5;

    /**
     * GameEngineImp constructor.
     * */
    public GameEngineImpl() {
        this.initGame();
    }

    @Override
    public void run() {
        this.gameLoop.gameLoop();
    }

    /**
     * Method that initializes the initial game values.
     */
    private void initGame() {
        this.controllers.put("keyMove", new KeyboardInputController(VK_W, VK_S, VK_A, VK_D));
        this.controllers.put("keyShot", new KeyboardInputController(VK_UP, VK_DOWN, VK_LEFT, VK_RIGHT));
        this.actionController = new ActionControllerImpl(VK_ESCAPE, VK_N, VK_P);
        final var gameState = new LevelControllerImpl(GameEngineImpl.MAX_ROOMS, this).getCurrentLevel();
        this.gameLoop = new GameLoopImpl(new SwingScene(gameState, this),
                gameState, this.actionController);

    }

    @Override
    public InputController getController(final String name) {
        return this.controllers.get(name);
    }

    @Override
    //Warning suppressed because we need the action controller state into the game loop
   
    public ActionControllerImpl getActionController() {
        return (ActionControllerImpl) this.actionController;
    }

    @Override
    public Collection<KeyboardInputController> getKeyboardInputControllers() {
        final Collection<KeyboardInputController> contr = new ArrayList<>();
        for (final InputController c: controllers.values()) {
                if (c instanceof KeyboardInputController) {
                        contr.add((KeyboardInputController) c);
                }
        }
        return contr;
    }

    @Override
    public boolean isGameLoopInPause() {
        return this.gameLoop.isPause();
    }
}
