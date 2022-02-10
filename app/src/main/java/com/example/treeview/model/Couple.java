package com.example.treeview.model;

public class Couple {

    private People husband, wife;

    public Couple(People husband, People wife) {
        this.husband = husband;
        this.wife = wife;
    }

    public People getHusband() {
        return husband;
    }

    public void setHusband(People husband) {
        this.husband = husband;
    }

    public People getWife() {
        return wife;
    }

    public void setWife(People wife) {
        this.wife = wife;
    }

}
