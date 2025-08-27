package platformer;

import lombok.Data;

@Data
public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
	this.x = x;
	this.y = y;
    }

    public Coordinate(Coordinate start) {
	this.x = start.getX();
	this.y = start.getY();
    }

    public void changeX(int i) {
	this.x += i;
    }
    public void changeY(int i) {
	this.y += i;
    }

    public int getX() {
	return x;
    }
    public int getY() {
	return y;
    }
}

