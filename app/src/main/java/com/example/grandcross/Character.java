package com.example.grandcross;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Character implements Serializable {
    public enum Attribute {
        ALL, RED, GREEN, BLUE, DARK, LIGHT
    }
    private int listImage;
    private int detailImage;
    private String name;
    private int lvl;
    private int attack;
    private int defense;
    private int hp;
    private Attribute attribute;
    private String userId;
    private List<Ability> abilities;

    // Пустой конструктор для Firebase
    public Character() {
    }

    public Character(int listImage, int detailImage, String name,int lvl, int attack, int defense, int hp, Attribute attribute, String userId,List<Ability> abilities) {
        this.listImage = listImage;
        this.detailImage = detailImage;
        this.name = name;
        this.lvl = lvl;
        this.attack = attack;
        this.defense = defense;
        this.hp = hp;
        this.attribute = attribute;
        this.userId = userId;
        this.abilities = abilities;
    }
    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getListImage() {
        return listImage;
    }

    public void setListImage(int listImage) {
        this.listImage = listImage;
    }

    public int getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(int detailImage) {
        this.detailImage = detailImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }
}