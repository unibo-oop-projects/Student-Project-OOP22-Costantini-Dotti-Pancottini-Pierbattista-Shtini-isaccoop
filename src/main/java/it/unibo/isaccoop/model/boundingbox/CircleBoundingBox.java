package it.unibo.isaccoop.model.boundingbox;

import it.unibo.isaccoop.model.common.Point2D;
import it.unibo.isaccoop.model.common.Vector2D;

/**
 * Implements the BoundingBox interface of a circle.
 */
public class CircleBoundingBox implements BoundingBox {

    private final double radius;

    /**
     * Constractor of the circle bounding box.
     * @param radius
     */
    public CircleBoundingBox(final double radius) {
        this.radius = radius;
    }

    /**
     * 
     * @return radius of the circle bounding box.
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * Check for collisions of two bounding box.
     * @param center of first bounding box
     * @param center1 of the second bounding box
     * @param circleBox of the second bounding box
     * @return true if a collision occours
     */
    @Override
    public boolean isCollidingWithCricle(final Point2D center, final Point2D center1, final CircleBoundingBox box1) {
        return new Vector2D(center, center1).module() <= box1.getRadius() + this.radius;
    }

    /**
     * Check for collisions of a bounding box with the perimeter of a rectangle bounding box.
     * (We suppose that the first bounding box is inside the second one).
     * @param center of first bounding box
     * @param rectangleBox
     * @return true if a collision occours
     */
    @Override
    public boolean isCollidingWithRecPerimeter(Point2D center, RectBoundingBox rectangleBox/*int height, int width*/) {
        if(rectangleBox.getHeight() -  center.getY() <= this.radius || center.getY() <= this.radius 
                || rectangleBox.getWidth() - center.getX() <= this.radius|| center.getX() <= this.radius) {
            return true;
        } else {
            return false;        
        }
    }
}
