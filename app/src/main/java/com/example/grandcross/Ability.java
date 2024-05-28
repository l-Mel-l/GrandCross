package com.example.grandcross;

import java.io.Serializable;

public class Ability implements Serializable {
    private int icon;
    private String description;

    // Пустой конструктор для Firebase
    public Ability() {
    }

    public Ability(int icon, String description) {
        this.icon = icon;
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
