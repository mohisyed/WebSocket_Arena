package org.example.model;



public class Player {
    private String id;
    private String name;
    private int x, y;
    private PlayerColor color;
    private boolean up, down, left, right;

    public Player() {}

    public Player(String id, String name, int x, int y, PlayerColor color) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public PlayerColor getColor() { return color; }
    public void setColor(PlayerColor color) { this.color = color; }

    public boolean isUp() { return up; }
    public void setUp(boolean up) { this.up = up; }

    public boolean isDown() { return down; }
    public void setDown(boolean down) { this.down = down; }

    public boolean isLeft() { return left; }
    public void setLeft(boolean left) { this.left = left; }

    public boolean isRight() { return right; }
    public void setRight(boolean right) { this.right = right; }
}
