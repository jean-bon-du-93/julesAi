package game;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents the snake in the game.
 */
public class Snake {
    private final LinkedList<Point> body = new LinkedList<>();
    private Direction direction = Direction.RIGHT;

    /**
     * Constructs a new Snake with an initial position.
     * @param initialPosition the initial position of the snake's head
     */
    public Snake(Point initialPosition) {
        body.add(initialPosition);
    }

    /**
     * Moves the snake in the current direction.
     */
    public void move() {
        Point head = getHead();
        Point newHead = switch (direction) {
            case UP -> new Point(head.x, head.y - 1);
            case DOWN -> new Point(head.x, head.y + 1);
            case LEFT -> new Point(head.x - 1, head.y);
            case RIGHT -> new Point(head.x + 1, head.y);
        };
        body.addFirst(newHead);
        body.removeLast();
    }

    /**
     * Grows the snake by one segment.
     */
    public void grow() {
        body.addLast(body.getLast());
    }

    /**
     * Checks for self-collision.
     * @return true if the snake collides with itself, false otherwise
     */
    public boolean checkSelfCollision() {
        Point head = getHead();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public Point getHead() {
        return body.getFirst();
    }

    public List<Point> getBody() {
        return body;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        // Prevent the snake from reversing
        if (this.direction == Direction.UP && direction == Direction.DOWN) return;
        if (this.direction == Direction.DOWN && direction == Direction.UP) return;
        if (this.direction == Direction.LEFT && direction == Direction.RIGHT) return;
        if (this.direction == Direction.RIGHT && direction == Direction.LEFT) return;
        this.direction = direction;
    }
}
