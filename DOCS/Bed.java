package com.mss;

public class Bed {
    private String style;
    private int pilow,height,sheet,quilt;

    public Bed(String style, int pilow, int height, int sheet, int quilt) {
        this.style = style;
        this.pilow = pilow;
        this.height = height;
        this.sheet = sheet;
        this.quilt = quilt;
    }

    public String getStyle() {
        return style;
    }

    public int getPilow() {
        return pilow;
    }

    public int getHeight() {
        return height;
    }

    public int getSheet() {
        return sheet;
    }

    public int getQuilt() {
        return quilt;
    }
    public void makeBed(){
        System.out.println("Bed -> made");
    }
}
