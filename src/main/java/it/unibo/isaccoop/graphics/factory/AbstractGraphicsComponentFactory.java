package it.unibo.isaccoop.graphics.factory;

import it.unibo.isaccoop.graphics.GraphicsComponent;

/**
 * Abstract class used as container for common GraphicsComponentFactories.

 * Warning suppressed because this class needs to be abstract,
 * it is a container for common graphics component factories behavior and state
 */
public abstract class AbstractGraphicsComponentFactory {

    /**
     * Get element graphics component as image to be drawn.
     * @param imageName
     * @return a MapElement as image
     */
    protected GraphicsComponent getElementAsImageGraphicsComponent(final String imageName) {
        return (obj, w) -> w.drawElementAsImage(obj, imageName);
    }
}
