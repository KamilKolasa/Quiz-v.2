package com.app.model;

public enum Level {
    ALL("WSZYSTKO", 0),
    EASY("ŁATWY", 50),
    NORMAL("NORMALNY", 100),
    HARD("TRUDNY", 150);

    private String name;
    private int point;


    Level(String name, int point) {
        this.name = name;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }
}
